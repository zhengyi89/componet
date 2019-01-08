package com.zbjdl.utils.query.utils;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class QueryLogUtils {
	
	private static Logger logger = LoggerFactory.getLogger(QueryLogUtils.class);
	
	public static final Map<String, Long> sqlQueryTime = new HashMap<String, Long>();//存放querykey和SQL查询时间
	
	private static String appName;
	
	
	private static Integer limitTime=5000;
	
	private static Map<String, Long> limitApp;
	
	private static ScheduledExecutorService executor;
	
	static {
		executor = Executors.newSingleThreadScheduledExecutor();
		// 1小时清空一次
		executor.scheduleWithFixedDelay(new Runnable() {
			public void run() {
				limitTime = null;
				limitApp = null;
			}
		}, 1, 1, TimeUnit.HOURS);
	}
	
	
	/**
	 * @desc    记录执行耗时的日志
	 * @param queryKey
	 * @param sqlTemplete
	 * @param parm
	 */
	public static void wasteTimeLog(String queryKey, String sqlTemplete, String querySql, Map requestParam, List sqlParam, long startTime){
		initAppName();
		if(appName==null){
			return;
		}
		long endTime = System.currentTimeMillis();
		long queryTime = endTime-startTime;
		Integer limitTimeTemp = getDefaultLimitTimeByAppName(appName);
		if(queryTime > limitTimeTemp){
			insertWasteTimeLog(queryKey, sqlTemplete, querySql, requestParam, sqlParam,startTime, endTime);
		}
	}


	private static void insertWasteTimeLog(String queryKey, String sqlTemplete,
			String querySql, Map requestParam, List sqlParam, long startTime,
			long endTime) {
		//TODO

//		QueryLogEntity logEntity = new QueryLogEntity();
//		
//		logEntity.setLogType(LogTypeEnum.WASTE_TIME);
//		logEntity.setCreateTime(new Date());
//		logEntity.setQueryKey(subMaxString(queryKey,100));
//		logEntity.setQuerySql(subMaxString(querySql,2000));
//		logEntity.setQueryTemplete(subMaxString(sqlTemplete,3000));
//		
//		logEntity.setQueryTime(endTime - startTime);
//		
//		logEntity.setRequestParam(subMaxString(requestParam.toString(),1000));
//		logEntity.setSqlParam(subMaxString(sqlParam.toString(),1000));
//		logger.saveToDB(logEntity);
	}
	
	/**
	 * @desc    字符串截断
	 * @version 1.0
	 * @param orgString
	 * @param maxLength
	 * @return
	 */
	private static String subMaxString(String orgString,int maxLength){
		if (orgString.length() > maxLength) {
			return orgString.substring(0, maxLength);
		}
		return orgString;
	}
	
	private static void initAppName() {
		if (appName != null) {
			return;
		}
		try {
			synchronized (QueryLogUtils.class) {
				if (appName == null) {
					Class<?> cl = Class
							.forName("com.zbjdl.common.utils.springboot.autoconfigure.dubbo.DubboApplicationContextHelper");
					Method m = cl.getMethod("getAppName");
					appName = (String)m.invoke(null);
				}
			}
		} catch (Throwable t) {
			logger.error("initAppName error " + t.getClass().getName(), t);
		}
	}
	
	/**
	 * @desc    根据应用名获取该应用的限制时间
	 * @version 1.0
	 * @param appName
	 * @return
	 */
	private static Integer getDefaultLimitTimeByAppName(String appName){

		if(limitTime==null){//如果系统默认限制时间没有取到，则默认给5000毫秒
			limitTime=5000;
		}
		return limitTime;
	}
	

}
