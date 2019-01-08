package com.zbjdl.common.redis;

import java.io.InputStream;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPoolConfig;

/**
 * 
 * <p>Title:Redis Cluster客户端工具类</p>
 * <p>Description:用此工具类是，要求操作的Redis服务器不失为Redis集群。</br> 
 * Redis 集群提供了以下两个好处：</br>
    将数据自动切分（split）到多个节点的能力。</br>
    当集群中的一部分节点失效或者无法进行通讯时， 仍然可以继续处理命令请求的能力。</br>
	</p>
 *
 * @author jiyong.ye
 * @version 1.0
 * @since 2015年5月15日
 */
public class RedisClusterClientUtils {
	private static Logger LOG =LoggerFactory.getLogger(RedisClusterClientUtils.class);
	private static Set<HostAndPort> jedisClusterNodes = new HashSet<HostAndPort>();
	private static JedisCluster jedisCluster;

	/**
	 * 初始化
	 */
	public synchronized static void init() {
		try {
			InputStream is = Thread.currentThread().getContextClassLoader()
					.getResourceAsStream("redis-cluster.properties");
			Properties prop = new Properties();
			prop.load(is);
			JedisPoolConfig config = new JedisPoolConfig();
			config.setMaxTotal(toInteger(prop, "maxTotal"));
			config.setMaxIdle(toInteger(prop, "maxIdle"));
			config.setMaxWaitMillis(toInteger(prop, "maxWaitMillis"));
			config.setTestOnBorrow(toBoolean(prop, "testOnBorrow"));
			config.setTestOnReturn(toBoolean(prop, "testOnReturn"));
			String[] hostAndPorts = prop.getProperty("clusterNodes").trim()
					.split(",");

			if (hostAndPorts.length > 1) {
				for(String hostAndPortStr:hostAndPorts){
					String[] hostAndPort = hostAndPortStr.split(":");
					String host = hostAndPort[0];
					int port = Integer.parseInt(hostAndPort[1]);
					jedisClusterNodes.add(new HostAndPort(host, port));

				}
				
			} else {
				String[] hostAndPort = hostAndPorts[0].split(":");
				String host = hostAndPort[0];
				int port = Integer.parseInt(hostAndPort[1]);
				jedisClusterNodes.add(new HostAndPort(host, port));
			}
			jedisCluster = new JedisCluster(jedisClusterNodes,config);

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

	/**
	 * 模板方法
	 * 
	 * @param rc
	 * @return
	 */
	public static JedisCluster getRedisTemplate() {
		return jedisCluster;
	}


	/** 
	* 向key赋值 
	* @param key 
	* @param value 
	*/  
	public static String set(String key, String value) {

		return jedisCluster.set(key, value);
	}
	
	/**
     * 获取单个值
     * 
     * @param key
     * @return
     */
    public  static String get(String key) {
        return jedisCluster.get(key);
    }	
	
    /**
     * 在某段时间后失效
     * 
     * @param key
     * @param seconds	秒
     * @return
     */
    public static Long expire(String key, int seconds) {
    	return jedisCluster.expire(key, seconds);
    }
    /**
     * 在某个时间点失效
     * 
     * @param key
     * @param unixTime
     * @return
     */
    public static Long expireAt(String key, long unixTime) {
    	return jedisCluster.expireAt(key, unixTime);
    }
    /**
     * 以秒为单位，返回给定 key 的剩余生存时间(TTL, time to live)。
     * @param key
     * @return
     */
    public static Long ttl(String key) {
    	return jedisCluster.ttl(key);
    }
    
    public static Long del(String key) {
    	return jedisCluster.del(key);
    }

 
    
}
