package com.zbjdl.common.redis;

import java.io.InputStream;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zbjdl.common.utils.StringUtils;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisSentinelPool;
import redis.clients.util.Pool;

/**
 * 
 * <p>Title:Redis客户端工具类</p>
 * <p>Description: 用于根据配置文件redis-conf.properties，初始化连接池 ，取得RedisClientTemplate 对象实例。
 * RedisClientTemplate 真正提供封装Redis的操作 。</br>
 * 要求Redis部署为 基于 Redis sentinel的主从复制 
 * 
 *
 * @author jiyong.ye
 * @version 1.0
 * @since 2015年5月15日
 */
public class RedisClientUtils {
	private static Logger LOG =LoggerFactory.getLogger(RedisClientUtils.class);
	private static Pool<Jedis> pool;
	private static boolean initialized = false;


	/**
	 * 获取资源
	 * 
	 * @return
	 */
	public static Jedis getResource() {
		if (pool == null) {
			throw new RuntimeException("Redis Client not init!");
		}
		return pool.getResource();
	}

	/**
	 * 关闭资源
	 * 
	 * @param jedis
	 * @param broken 是否出现异常
	 */
	public static void closeResource(Jedis jedis,boolean broken) {
		if(broken){
			closeBrokenResource(jedis);
		}else{
			closeResource(jedis);
		}
	}
	/**
	 * 关闭资源
	 * 
	 * @param jedis
	 */
	public static void closeResource(Jedis jedis) {
		pool.returnResource(jedis);
	}

	/**
	 * 关闭破坏资源
	 * 
	 * @param jedis
	 */
	public static void closeBrokenResource(Jedis jedis) {
		pool.returnBrokenResource(jedis);
	}
	/**
	 * 组件初始化方法
	 */
	public synchronized static void init(){
		if(initialized()){
			return;
		}
		initRedis();
		initialized = true;
	}
	
	public static boolean initialized(){
		return initialized;
	}

	public synchronized static void initRedis() {
		try {
			InputStream is = Thread.currentThread().getContextClassLoader()
					.getResourceAsStream("redis-conf.properties");
			Properties prop = new Properties();
			prop.load(is);
			JedisPoolConfig config = new JedisPoolConfig();
			config.setMaxTotal(toInteger(prop, "maxTotal"));
			config.setMaxIdle(toInteger(prop, "maxIdle"));
			if(StringUtils.isNotBlank(prop.getProperty("minIdle"))){
				config.setMinIdle(toInteger(prop, "minIdle"));
			}
			config.setMaxWaitMillis(toInteger(prop, "maxWaitMillis"));
			config.setTestOnBorrow(toBoolean(prop, "testOnBorrow"));
			config.setTestOnReturn(toBoolean(prop, "testOnReturn"));
			String[] hostAndPorts = prop.getProperty("sentinels").trim()
					.split(",");
			if (hostAndPorts.length > 1) {
				Set<String> sentinels = new HashSet<String>(
						Arrays.asList(hostAndPorts));
				pool = new JedisSentinelPool(prop.getProperty("masterName")
						.trim(), sentinels, config, toInteger(prop, "timeout"));
			} else {
//		        Set<String> sentinels = new HashSet<String>();  
//		        sentinels.add(hostAndPorts[0]);  
//				pool = new JedisSentinelPool(prop.getProperty("masterName")
//						.trim(), sentinels, config, toInteger(prop, "timeout"));
				//仅仅一个主机和端口的配置，则表示为一个普通的redis数据库。没有配置为sentinel主从复制
				String[] hostAndPort = hostAndPorts[0].split(":");
				String host = hostAndPort[0];
				int port = Integer.parseInt(hostAndPort[1]);
				pool = new JedisPool(config, host, port, toInteger(prop,
						"timeout"));
			}
		} catch (Exception e) {
			LOG.error("init redis pool fail", e);
			throw new RuntimeException("init redis pool fail", e);
		}
	}
	private static int toInteger(Properties prop, String key) {
		return Integer.parseInt(prop.getProperty(key).trim());
	}

	private static boolean toBoolean(Properties prop, String key) {
		return Boolean.valueOf(prop.getProperty(key).trim());
	}
	
	public static RedisClientTemplate getRedisTemplate(){
		RedisClientTemplate template = new RedisClientTemplate();
		return template;
	}
   
    
}
