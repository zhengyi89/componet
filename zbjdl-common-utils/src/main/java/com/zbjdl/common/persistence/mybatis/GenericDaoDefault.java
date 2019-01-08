package com.zbjdl.common.persistence.mybatis;

import com.zbjdl.common.persistence.*;
import com.zbjdl.common.utils.GenericUtils;

import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.support.SqlSessionDaoSupport;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * Title: 通用Dao Ibatis实现
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: 2011
 * </p>
 * <p>
 * </p>
 * 
 * @author feng
 * @version 1.0,2011-1-13
 */
@SuppressWarnings( { "unchecked", "rawtypes" })
public class GenericDaoDefault<E extends Entity> extends SqlSessionDaoSupport
		implements GenericDao<E> {

	protected SqlSessionFactory sqlSessionFactory;
	
	// 泛型类型
	protected Class entityClass;

	/**
	 * 构造方法
	 */
	public GenericDaoDefault() {
		this.entityClass = GenericUtils.getGenericClass(this.getClass());
	}

	@Override
	protected void checkDaoConfig() {
		super.checkDaoConfig();
		SqlSession sqlSession = super.getSqlSession();
		if (!(sqlSession instanceof SqlSessionTemplate))
			throw new VersionUncompatableException(
					"the version of this components is not compatable with mybatis-spring.jar version");
		sqlSessionFactory = ((SqlSessionTemplate)sqlSession).getSqlSessionFactory();
	}

	public void delete(Serializable id) {
		super.getSqlSession().delete(
				getStatementId(Constants.DELETE_STATEMENT), id);
	}

	@Override
	public void delete(String ql, Object... args) {
		if (args == null || args.length == 0) {
			super.getSqlSession().delete(getStatementId(ql));
		} else if (args.length == 1) {
			super.getSqlSession().delete(getStatementId(ql), args[0]);
		} else {
			Map map = new HashMap();
			for (int i = 0; i < args.length; i++)
				map.put("" + i, args[i]);
			super.getSqlSession().delete(getStatementId(ql), map);
		}
	}

	public void delete(E entity) {
		delete(entity.getId());
	}

	public E get(Serializable id) {
		return (E) super.getSqlSession().selectOne(
				getStatementId(Constants.GET_STATEMENT), id);
	}

	public List<E> getAll() {
		return super.getSqlSession().selectList(
				getStatementId(Constants.GET_ALL_STATEMENT));
	}

	public List query(String ql, Object... arg) {
		List result = null;
		if (arg == null || arg.length == 0) {
			result = super.getSqlSession().selectList(getStatementId(ql));
		} else if (arg.length == 1) {
			result = super.getSqlSession().selectList(getStatementId(ql),
					arg[0]);
		} else {
			Map map = new HashMap();
			for (int i = 0; i < arg.length; i++)
				map.put("" + i, arg[i]);
			result = super.getSqlSession().selectList(getStatementId(ql), map);
		}
		return result != null ? result : new ArrayList();
	}

	public List query(String sql, int offset, int limit, Object... arg) {
		List result = null;
		RowBounds rb = new RowBounds(offset, limit);
		if (arg == null || arg.length == 0) {
			result = this.getSqlSession().selectList(getStatementId(sql), null,
					rb);
		} else if (arg.length == 1) {
			result = this.getSqlSession().selectList(getStatementId(sql),
					arg[0], rb);
		} else {
			Map map = new HashMap();
			for (int i = 0; i < arg.length; i++)
				map.put("" + i, arg[i]);
			result = this.getSqlSession().selectList(getStatementId(sql), map,
					rb);
		}
		return result != null ? result : new ArrayList();
	}

	@Override
	public Object queryOne(String ql, Object... arg) {
		Object ob = null;
		if (arg == null || arg.length == 0) {
			ob = super.getSqlSession().selectOne(getStatementId(ql));
		} else if (arg.length == 1) {
			ob = super.getSqlSession().selectOne(getStatementId(ql), arg[0]);
		} else {
			Map map = new HashMap();
			for (int i = 0; i < arg.length; i++)
				map.put("" + i, arg[i]);
			ob = super.getSqlSession().selectOne(getStatementId(ql), map);
		}
		return ob;
	}

	/**
	 * 获取命名空间加StatementId全名
	 *
	 * @param postfix
	 * @return
	 */
	protected String getStatementId(String postfix) {
		return entityClass.getSimpleName() + "." + postfix;
	}

	public void update(E entity) {
		int row = super.getSqlSession().update(
				getStatementId(Constants.UPDATE_STATEMENT), entity);
		if (entity instanceof EntityVersion) {
			if (row == 0) {
				throw new OptimisticLockingException("乐观锁异常");
			}
		}
	}

	@Override
	public void add(E entity) {
		super.getSqlSession().insert(
				getStatementId(Constants.INSERT_STATEMENT), entity);
	}

	@Override
	public void add(String sql, E entity) {
		super.getSqlSession().insert(getStatementId(sql), entity);
	}

	@Override
	public void update(String ql, Object... arg) {
		if (arg == null || arg.length == 0) {
			super.getSqlSession().update(getStatementId(ql));
		} else if (arg.length == 1) {
			super.getSqlSession().update(getStatementId(ql), arg[0]);
		} else {
			Map map = new HashMap();
			for (int i = 0; i < arg.length; i++) {
				map.put("" + i, arg[i]);
			}
			super.getSqlSession().update(getStatementId(ql), map);
		}
	}

	@Override
	public Map getMap(String ql, Object... arg) {
		Map result = null;
		if (arg == null || arg.length == 0) {
			result = (Map) super.getSqlSession().selectOne(getStatementId(ql));
		} else if (arg.length == 1) {
			result = (Map) super.getSqlSession().selectOne(getStatementId(ql),
					arg[0]);
		} else {
			Map map = new HashMap();
			for (int i = 0; i < arg.length; i++) {
				map.put("" + i, arg[i]);
			}
			result = (Map) super.getSqlSession().selectOne(getStatementId(ql),
					map);
		}
		return result;
	}

	private SqlSession getBatchSession() {
		return BatchSqlSessionUtils.getSqlSession(sqlSessionFactory, ExecutorType.BATCH);
	}

	@Override
	public void batchInsert(String sql, List<E> entities) {
//		SqlSession batchSqlSession = getBatchSession();
		SqlSession batchSqlSession = sqlSessionFactory.openSession(ExecutorType.BATCH, false);// 获取批量方式的sqlsession
		try {
			for (Entity e : entities) {
				batchSqlSession.insert(getStatementId(sql), e);
			}
			batchSqlSession.commit();
		} finally {
			BatchSqlSessionUtils.closeSqlSession(batchSqlSession);
		}
//		for (E e : entities) {
//			add(e);
//		}

	}

	@Override
	public void batchUpdate(List<E> entities) {
//		SqlSession batchSqlSession = getBatchSession();
		SqlSession batchSqlSession = sqlSessionFactory.openSession(ExecutorType.BATCH, false);// 获取批量方式的sqlsession
		try {
			for (Entity e : entities) {
				batchSqlSession.update(
						getStatementId(Constants.UPDATE_STATEMENT), e);
			}
			batchSqlSession.commit();
		} finally {
			BatchSqlSessionUtils.closeSqlSession(batchSqlSession);
		}
//		for (E e : entities) {
//			update(e);
//		}

	}

	@Override
	@Deprecated
	public void batchInsert(List<E> entities) {
//		SqlSession batchSqlSession = getBatchSession();
		SqlSession batchSqlSession = sqlSessionFactory.openSession(ExecutorType.BATCH, false);// 获取批量方式的sqlsession
		try {
			for (Entity e : entities) {
				batchSqlSession.insert(
						getStatementId(Constants.INSERT_STATEMENT), e);
			}
			batchSqlSession.commit();
		} finally {
			BatchSqlSessionUtils.closeSqlSession(batchSqlSession);
		}
	}

	@Override
	public void batchDelete(List<E> entities) {
//		SqlSession batchSqlSession = getBatchSession();
		SqlSession batchSqlSession = sqlSessionFactory.openSession(ExecutorType.BATCH, false);// 获取批量方式的sqlsession
		try {
			for (Entity e : entities) {
				batchSqlSession.delete(
						getStatementId(Constants.DELETE_STATEMENT), e.getId());
			}
			batchSqlSession.commit();
		} finally {
			BatchSqlSessionUtils.closeSqlSession(batchSqlSession);
		}
//		for (E e : entities) {
//			delete(e);
//		}
	}

	@Override
	public void batchDeleteById(List<Serializable> ids) {
//		SqlSession batchSqlSession = getBatchSession();
		SqlSession batchSqlSession = sqlSessionFactory.openSession(ExecutorType.BATCH, false);// 获取批量方式的sqlsession
		try {
			for (Serializable id : ids) {
				batchSqlSession.delete(
						getStatementId(Constants.DELETE_STATEMENT), id);
			}
			batchSqlSession.commit();
		} finally {
			BatchSqlSessionUtils.closeSqlSession(batchSqlSession);
		}
//		for (Serializable id : ids) {
//			delete(id);
//		}
	}

}
