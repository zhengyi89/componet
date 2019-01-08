
package com.zbjdl.utils.query;

import java.util.List;
import java.util.Map;

/** 
 * 查询服务接口  
 */
public interface QueryService {
	
	/**
	 * 分页查询
	 * @param key 查询key
	 * @param param 查询参数集
	 * @return 查询结果对象,如果不满足配置中定义的查询要求则返回空
	 */
	public QueryResult query(String key, QueryParam param);

	/**
	 * 分页查询方法(一般在查询的action中调用)
	 * @param startIndex 起始记录行数(最小值为1)
	 * @param maxResult 最大查询行数(也就是每页记录数)
	 * @param queryKey 配置文件中SQL语句的Key
	 * @param param 查询参数(一般是页面传递上来的http请求参数)
	 * @param orderByColumn 排序的列名(可以为空，如果指定了这个参数，并且原来的语句中有排序关键字，会使用这个参数替换)
	 * @param isAsc 是否升序
	 * @param doSum 是否执行sum语句(为true是，如果配置文件中定义了 sum语句才会执行，否则报错)
	 * @return 查询结果对象(包括了总共记录数和本次查询结果，数据格式为List<Map>)，如果不满足配置中定义的查询要求则返回空
	 */
	public QueryResult query(Integer startIndex, Integer maxResult, String queryKey, Map<String, Object> param, String orderByColumn, Boolean isAsc, Boolean doSum);
	
	/**
	 * 不带分页计数功能的查询
	 * @param key 查询key
	 * @param param 查询参数集
	 * @return 查询结果,如果不满足配置中定义的查询要求则返回null
	 */
	public List queryList(String key, QueryParam param);

	/**
	 * 不带分页计数功能的查询
	 * @param startIndex 起始记录行数(最小值为1)
	 * @param maxResult 最大查询行数(也就是每页记录数)
	 * @param queryKey 配置文件中SQL语句的Key
	 * @param param 查询参数(一般是页面传递上来的http请求参数)
	 * @param orderByColumn 排序的列名(可以为空，如果指定了这个参数，并且原来的语句中有排序关键字，会使用这个参数替换)
	 * @param isAsc 是否升序
	 * @return 查询结果,如果不满足配置中定义的查询要求则返回null
	 */
	public List query(Integer startIndex, Integer maxResult, String queryKey, Map<String, Object> param, String orderByColumn, Boolean isAsc);
	
	/**
	 * 简单的查询方法(不统计总数)，在service中需要进行一些简易查询时使用的方法
	 * @param queryKey 配置文件中SQL语句的Key
	 * @param param 查询参数(一般是页面传递上来的http请求参数)
	 * @return 查询结果(无数据则返回null)
	 */
	public List query(String queryKey, Map<String, Object> param);
	
	/**
	 * 简单的查询方法(不统计总数)，在service中需要进行一些简易查询时使用的方法
	 * @param queryKey 配置文件中SQL语句的Key
	 * @param param 查询参数(一般是页面传递上来的http请求参数)
	 * @param sortColumn 结果集排序列
	 * @param isAsc 结果集排序方式
	 * @return 查询结果(无数据则返回null)
	 */
	public List query(String queryKey, Map<String, Object> param,String sortColumn,boolean isAsc);
	
	/**
	 * 查询单行数据(前提条件是SQL语句只返回单行数据)，执行统计语句时可以使用该方法
	 * @param queryKey 配置文件中SQL语句的Key
	 * @param param 查询参数(一般是页面传递上来的http请求参数)
	 * @param checkUnique 如果查询结果不止一行数据时是否报错(不报错则返回第一行)
	 * @return 查询结果，无数据则返回空
	 */
	public Map queryUnique(String queryKey, Map<String, Object> param, boolean checkUnique);
	
}
