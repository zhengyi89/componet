package com.zbjdl.common.persistence;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * <p>Title: 通用Dao接口</p>
 * <p>Description: 所有实体的Dao接口实现该通用接口，泛型传入实体<br/>
 * 	具有基本的增删改查方法</p>
 * <p>Copyright: 2011</p>
 * 
 * @author feng
 * @version 1.0,2011-1-13
 */
@SuppressWarnings("rawtypes")
public interface GenericDao<E extends Entity> {
	
	/**
	 * 保存一个实体
	 * @param entity 实体对象
	 */
	public void add(E entity);
	
	/**
	 * 根据ID保存一个实体
	 * @param sql
	 * @param entity
	 */
	public void add(String sql, E entity);
	
	/**
	 * 更新一个实体
	 * @param entity 实体对象
	 */
	public void update(E entity);
	
	/**
	 * 删除一个实体
	 * @param id 主键
	 */
	public void delete(Serializable id);
	
	/**
	 * 删除一个实体
	 * @param id 主键
	 */
	public void delete(String ql,Object... args);
	
	/**
	 * 删除一个实体
	 * @param entity 实体对象
	 */
	public void delete(E entity);
	
	/**
	 * 查询所有
	 * @return
	 */
	public List<E> getAll();
	
	/**
	 * 通过主键查询
	 * @param id 主键
	 * @return 实体对象
	 */
	public E get(Serializable id);
	
	/**
	 * 通过查询语句查询
	 * @param ql 查询语句
	 * @param arg 查询条件参数
	 * @return 查询结果
	 */
	public List query(String ql, Object... arg);
	
	/**
	 * 通过查询语句查询
	 * @param sql 查询语句
	 * @param offset 查询起始行,注意：最小有效值为0
	 * @param limit 查询返回的总行数
	 * @param arg 查询条件参数
	 * @return
	 */
	public List query(String sql, int offset, int limit, Object...arg);
	
	/**
	 * 根据唯一的条件的E
	 * @param ql 查询语句
	 * @param arg 查询条件参数
	 * @return  e 查询结果
	 */
	public Object queryOne(String ql, Object... arg);
	
	/**
	 * 通过更新语句更新
	 * @param ql 更新语句
	 * @param arg 更新条件参数
	 */
	public void update(String ql, Object... arg);
	
	/**
	 * 通过查询语句查询,以Map形式返回,key:字段名
	 * @param ql 查询语句
	 * @param arg 查询条件参数
	 * @return 查询结果Map
	 */
	public Map getMap(String ql, Object... arg);
	
	/**
	 * 
	 * 批量更新实体
	 * @param entities 列表
	 */
	public void batchUpdate(List<E> entities);
	/**
	 * 
	 * 批量插入实体
	 * @param us
	 */
	public void batchInsert(String sql, List<E> entities) ;
	
	@Deprecated
	public void batchInsert(List<E> entities) ;
	/**
	 * 
	 * 批量删除实体
	 * @param entities 实体列表
	 */
	public void batchDelete( List<E> entities) ;
	
	/**
	 * 
	 * 批量删除实体
	 * @param 实体的Id列表
	 */
	public void batchDeleteById( List<Serializable> ids) ;

}
