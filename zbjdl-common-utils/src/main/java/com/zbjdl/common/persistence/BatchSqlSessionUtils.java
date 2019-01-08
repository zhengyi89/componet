/** 
 * Copyright: Copyright (c)2015
 * Company: 云宝金服
 */
package com.zbjdl.common.persistence;

import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.transaction.SpringManagedTransactionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.TransientDataAccessResourceException;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.jdbc.datasource.TransactionAwareDataSourceProxy;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * 用来执行batch操作时创建session
 * 它和mybatis-spring的BatchSqlSessionUtils区别在于，它不判断线程中是否已经绑定了SqlSession，而是每次都新创建一个SqlSession
 * @author：feng    
 * @since：2012-10-18 下午06:57:19 
 * @version:   
 */
public class BatchSqlSessionUtils {
	
	private static final Logger logger = LoggerFactory.getLogger(BatchSqlSessionUtils.class);
	
	/**
     * If a Spring transaction is active it uses {@link DataSourceUtils} to get a 
     * Spring managed {@link Connection}, then creates a new {@link SqlSession}
     * with this connection and synchronizes it with the transaction.
     * If there is not an active transaction it gets a connection directly from 
     * the {@link DataSource} and creates a {@link SqlSession} with it. 
     *
     * @throws TransientDataAccessResourceException if a transaction is active and the
     *             {@link SqlSessionFactory} is not using a {@link SpringManagedTransactionFactory}
     * @see org.mybatis.spring.transaction.SpringManagedTransactionFactory
     */
    public static SqlSession getSqlSession(SqlSessionFactory sessionFactory, ExecutorType executorType) {
        // either return the existing SqlSession or create a new one
       
        DataSource dataSource = sessionFactory.getConfiguration().getEnvironment().getDataSource();
        boolean transactionAware = (dataSource instanceof TransactionAwareDataSourceProxy);
        Connection conn;

        try {
            conn = transactionAware ? dataSource.getConnection() : DataSourceUtils.getConnection(dataSource);
        } catch (SQLException e) {
            throw new CannotGetJdbcConnectionException("Could not get JDBC Connection for SqlSession", e);
        }

        if (logger.isDebugEnabled()) {
            logger.debug("Creating SqlSession from SqlSessionFactory");
        }

        // Assume either DataSourceTransactionManager or the underlying
        // connection pool already dealt with enabling auto commit.
        // This may not be a good assumption, but the overhead of checking
        // connection.getAutoCommit() again may be expensive (?) in some drivers
        // (see DataSourceTransactionManager.doBegin()). One option would be to
        // only check for auto commit if this function is being called outside
        // of DSTxMgr, but to do that we would need to be able to call
        // ConnectionHolder.isTransactionActive(), which is protected and not
        // visible to this class.
        SqlSession session = sessionFactory.openSession(executorType, conn);

        // Register session holder and bind it to enable synchronization.
        //
        // Note: The DataSource should be synchronized with the transaction
        // either through DataSourceTxMgr or another tx synchronization.
        // Further assume that if an exception is thrown, whatever started the transaction will
        // handle closing / rolling back the Connection associated with the SqlSession.
        if (TransactionSynchronizationManager.isSynchronizationActive()) {
            if (!(sessionFactory.getConfiguration().getEnvironment().getTransactionFactory() instanceof SpringManagedTransactionFactory)
                    && DataSourceUtils.isConnectionTransactional(conn, dataSource)) {
                throw new TransientDataAccessResourceException(
                        "SqlSessionFactory must be using a SpringManagedTransactionFactory in order to use Spring transaction synchronization");
            }
        }
        return session;
    }
    
    public static void closeSqlSession(SqlSession session) {
        if (session != null) {
        	try{
        		session.close();
        	}catch(Exception e){
        		logger.error(e.getMessage(), e);
        	}
        }
    }
}
