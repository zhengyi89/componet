package com.zbjdl.database;

import java.util.List;

import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

/**
 * 数据库操作基类，使用时需要注入SqlSessionFactory
 * 
 *
 */
public class BaseDao extends SqlSessionDaoSupport {
	
	@Autowired(required = false)
	@Qualifier("sqlSessionFactory") 
	public void setSqlSessionFactory(SqlSessionFactory sqlSessionFactory) {
		super.setSqlSessionFactory(sqlSessionFactory);
    }
	
	protected static int BATCH_DEAL_NUM = 2000;
	
    protected static SqlSessionFactory batchSqlSessionFactory;
    
	/**
	 * 批量插入记录
	 * 默认500条，也可以通过修改BATCH_DEAL_NUM字段改变记录数量
	 * @param statement
	 * @param list
	 * @return
	 */
    public int batchInsert(String statement, List<?> list) {
    	SqlSession batchSession = batchSqlSessionFactory.openSession(ExecutorType.BATCH, false);
    	int i = 0;
    	//失败的条数
    	int failCount = 0;
		try {
	    	for(int cnt = list.size(); i < cnt; i++) {
	    		try {
	    			batchSession.insert(statement, list.get(i));
				} catch (Exception e) {
					e.printStackTrace();
					failCount++;
				}
	    		
	    		if((i + 1) % BATCH_DEAL_NUM == 0) { //BATCH_DEAL_NUM为批量提交的条数
	    			batchSession.commit();
	    		}
	    	}
			batchSession.commit();
		} catch (Throwable ex) {
			ex.printStackTrace();
			//1只是随意设置的值，为了表示插入数据有失败
			failCount = 1;
		} finally {
			batchSession.close();
		}
    	return i-failCount;
    }
    
    /**
     * 批量更新记录
     * 默认500条，也可以通过修改BATCH_DEAL_NUM字段改变记录数量
     * @param statement
     * @param list
     * @return
     */
    public int batchUpdate(String statement, List<?> list) {
    	SqlSession batchSession = batchSqlSessionFactory.openSession(ExecutorType.BATCH, false);
    	int i = 0;
		try {
	    	for(int cnt = list.size(); i < cnt; i++) {
	    		batchSession.update(statement, list.get(i));
	    		if((i + 1) % BATCH_DEAL_NUM == 0) {
	    			batchSession.commit();
	    		}
	    	}
			batchSession.commit();
		} catch (Throwable ex) {
			ex.printStackTrace();
		} finally {
			batchSession.close();
		}
    	return i;
    }
    
    /**
     * 批量删除记录
     * 默认500条，也可以通过修改BATCH_DEAL_NUM字段改变记录数量
     * @param statement
     * @param list
     * @return
     */
    public int batchDelete(String statement, List<?> list) {
    	SqlSession batchSession = batchSqlSessionFactory.openSession(ExecutorType.BATCH, false);
    	int i = 0;
		try {
	    	for(int cnt = list.size(); i < cnt; i++) {
	    		batchSession.delete(statement, list.get(i));
	    		if((i + 1) % BATCH_DEAL_NUM == 0) {
	    			batchSession.commit();
	    		}
	    	}
			batchSession.commit();
		} catch (Throwable ex) {
			ex.printStackTrace();
		} finally {
			batchSession.close();
		}
    	return i;
    }
    
    /**<p>
     * 批量更新所有
     * @param statement
     * @param list
     * @return <p>
     * int
     */
    public int batchUpdateAll(String statement, List<?> list) {
    	SqlSession batchSession = batchSqlSessionFactory.openSession(ExecutorType.BATCH, false);
    	int i = 0;
		try {
	    	for(int cnt = list.size(); i < cnt; i++) {
	    		batchSession.update(statement, list.get(i));
	    	}
			batchSession.commit();
		} catch (Throwable ex) {
			ex.printStackTrace();
		} finally {
			batchSession.close();
		}
    	return i;
    }

	public void setBatchSqlSessionFactory(SqlSessionFactory sqlSessionFactory) {
		batchSqlSessionFactory = sqlSessionFactory;
	}
}