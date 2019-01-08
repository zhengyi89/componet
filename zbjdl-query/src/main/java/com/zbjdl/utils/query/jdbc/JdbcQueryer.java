
package com.zbjdl.utils.query.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zbjdl.utils.query.QueryException;

/** 
 * @version:   
 */
public class JdbcQueryer implements IJdbcQueryer{
	private static Logger logger = LoggerFactory.getLogger(JdbcQueryer.class);
	private static int DEFAUL_FETCH_SIZE = 50;
	private DataSource dataSource;
	
	public JdbcQueryer(DataSource dataSource){
		this.dataSource = dataSource;
	}
	
	public JdbcQueryResult query(Integer currentPage, Integer startIndex, Integer maxSize, String sql, List sqlParams){
		if(startIndex==null || startIndex<0){
			startIndex = 1;
		}
		logger.debug("sql : "+sql);
		logger.debug("startIndex : " + startIndex + ", maxSize : "+maxSize+", sqlParams : "+sqlParams);
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = dataSource.getConnection();
			ps = conn.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			ps.setFetchSize(DEFAUL_FETCH_SIZE);
			JdbcParamSetter.setParams(ps, sqlParams);
			rs = ps.executeQuery();
			rs.last();
			int rowCount = rs.getRow();
			JdbcRowDescriptor rowDescriptor = new JdbcRowDescriptor(rs, startIndex, maxSize);
			JdbcQueryResult result = new JdbcQueryResult(rowDescriptor.getResult());
			result.setTotalCount(rowCount);
			return result;
		} catch (SQLException e) {
			handelSQLException(e);
			JdbcQueryResult result = new JdbcQueryResult(new ArrayList());
			result.setTotalCount(0);
			return result;
		} finally {
			release(conn, ps, rs);
		}
	}
	
	public List queryList(Integer currentPage, Integer startIndex, Integer maxSize, String sql, List sqlParams){
		if(startIndex==null || startIndex<0){
			startIndex = 1;
		}
		logger.debug("sql : "+sql);
		logger.debug("startIndex : " + startIndex + ", maxSize : "+maxSize+", sqlParams : "+sqlParams);
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = dataSource.getConnection();
			ps = conn.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			ps.setFetchSize(DEFAUL_FETCH_SIZE);
			JdbcParamSetter.setParams(ps, sqlParams);
			rs = ps.executeQuery();
			JdbcRowDescriptor rowDescriptor = new JdbcRowDescriptor(rs, startIndex, maxSize);
			return rowDescriptor.getResult();
		} catch (SQLException e) {
			handelSQLException(e);
			return new ArrayList();
		} finally {
			release(conn, ps, rs);
		}
	}
	
	public List query(String sql, List sqlParams){
		logger.debug("sql : "+sql);
		logger.debug("sqlParams : "+sqlParams);
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = dataSource.getConnection();
			ps = conn.prepareStatement(sql);
			ps.setFetchSize(DEFAUL_FETCH_SIZE);
			JdbcParamSetter.setParams(ps, sqlParams);
			rs = ps.executeQuery();
			JdbcRowDescriptor rowDescriptor = new JdbcRowDescriptor(rs, null, null);
			return rowDescriptor.getResult();
		} catch (SQLException e) {
			handelSQLException(e);
			return new ArrayList();
		} finally {
			release(conn, ps, rs);
		}
	}
	
	public Map queryUniqueRow(String sql, List sqlParams, boolean checkUnique){
		logger.debug("sql : "+sql);
		logger.debug("sqlParams : "+sqlParams);
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = dataSource.getConnection();
			ps = conn.prepareStatement(sql);
			ps.setFetchSize(DEFAUL_FETCH_SIZE);
			JdbcParamSetter.setParams(ps, sqlParams);
			rs = ps.executeQuery();
			JdbcRowDescriptor rowDescriptor = new JdbcRowDescriptor(rs, 1, 1);
			return rowDescriptor.getUniqueResult(checkUnique);
		} catch (SQLException e) {
			handelSQLException(e);
			return null;
		} finally {
			release(conn, ps, rs);
		}
	}
	
	private void handelSQLException(SQLException e){
		//忽略由于参数长度导致的异常
		if(e.getErrorCode()==-302 && "22001".equals(e.getSQLState())){
			logger.warn("sql param invalid, SQLCODE="+e.getErrorCode()+" SQLSTAT="+e.getSQLState());
			return;
		}
		throw new QueryException("query jdbc exception : "+e.getMessage(), e);
	}
	
	public static void release(Connection conn, Statement ps, ResultSet rs) {
		try{
			if (rs != null) {
				rs.close();
			}
		}catch(SQLException e){
		}
		try{
			if (ps != null) {
				ps.close();
			}
		}catch(SQLException e){
		}
		try{
			if (conn != null) {
				conn.close();
			}
		}catch(SQLException e){
		}
	}
	
}
