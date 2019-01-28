package com.zbjdl.cache;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import com.google.common.collect.Maps;

/**
 *  封装redis缓存服务
 * 
 *
 */
public class RedisCache {
	
	public static Long SESSION_TIMEOUT = 60*30l;
	
    @Autowired
	private RedisTemplate<String, KeyValuePair<String, Object>> redisDictTemplate;
    
    @Autowired
	private RedisTemplate<String, Serializable> redisCacheTemplate;
    
    @Autowired
	private RedisTemplate<String, Map<String,Serializable>> redisSessionTemplate;
    
    /**
     * 设置缓存值
     * @param key
     * @param value
     */
    public void setCacheValue(String key, Serializable value){
    	redisCacheTemplate.opsForValue().set(key, value);
    }
    
    /**
     * 获取缓存值
     * @param key
     * @return
     */
    public Serializable getCacheValue(String key){
    	return redisCacheTemplate.opsForValue().get(key);
    }
    
    /**
     * 移除缓存
     * @param key
     */
    public void removeCache(String key){
    	redisCacheTemplate.delete(key);
    }
    
    /**
     * 设置缓存过期
     * @param key
     * @param second
     */
    public void expireCache(String key, Long second){
    	redisCacheTemplate.expire(key, second, TimeUnit.SECONDS);
    }
    
    /**
     * 设置session值
     * @param sessionId
     * @param key
     * @param value
     */
    public void setSessionValue(String sessionId, String key, Serializable value){
    	Map<String, Serializable> sessionData = redisSessionTemplate.opsForValue().get(sessionId);
    	if(sessionData == null) {
    		sessionData = Maps.newConcurrentMap();
    	}
    	sessionData.put(key, value);
    	redisSessionTemplate.opsForValue().set(sessionId, sessionData);
    	expireSession(sessionId);
    }
    
    /**
     * 获取session值
     * @param sessionId
     * @param key
     * @return
     */
    public Serializable getSessionValue(String sessionId, String key){
    	Map<String, Serializable> sessionData = redisSessionTemplate.opsForValue().get(sessionId);
    	expireSession(sessionId);
    	if(sessionData != null) {
        	return sessionData.get(key);
    	}
    	return null;
    }
    
    /**
     * 移除session值
     * @param sessionId
     * @param key
     */
    public void removeSessionValue(String sessionId, String key){
    	Map<String, Serializable> sessionData = redisSessionTemplate.opsForValue().get(sessionId);
    	expireSession(sessionId);
    	if(sessionData != null) {
    		sessionData.remove(key);
        	redisSessionTemplate.opsForValue().set(sessionId, sessionData);
    	}
    }
    
    /**
     * 设置session过期时间
     * @param sessionId
     * @param seconds
     */
    public void expireSession(String sessionId){
    	redisSessionTemplate.expire(sessionId, SESSION_TIMEOUT, TimeUnit.SECONDS);
    }
    
    /**
     * 根据字典id获取键值对列表
     * @param dict
     * @return
     */
    public List<KeyValuePair<String, Object>> getDictList(String dict){
    	return redisDictTemplate.opsForList().range(dict, 0, redisDictTemplate.opsForList().size(dict));
    }
    
    /**
     * 为字典id设置键值对列表
     * @param dict
     * @param values
     */
    public void setDictList(String dict, List<KeyValuePair<String, Object>> values){
    	redisDictTemplate.opsForList().remove(dict, 0, redisDictTemplate.opsForList().size(dict));
    	
    	for(KeyValuePair<String, Object> value : values){
    		redisDictTemplate.opsForList().rightPush(dict, value);
    	}
    }
    
    /**
     * 为字典增加值
     * @param dict
     * @param value
     */
    public void setDictList(String dict, KeyValuePair<String, Object> value){
    	redisDictTemplate.opsForList().rightPush(dict, value);
    }
    
    /**
     * 设置字典值
     * @param dict
     * @param value
     */
    public void setDictValue(String dict, Serializable value){
    	redisCacheTemplate.opsForValue().set(dict, value);
    }
    
    /**
     * 获取字典值
     * @param dict
     * @return
     */
    public Serializable getDictValue(String dict){
    	return redisDictTemplate.opsForValue().get(dict);
    }
    
    /**
     * 删除字典值
     * @param dict
     */
    public void removeDictValue(String dict){
    	redisDictTemplate.delete(dict);
    }
    
    /**
     * 设置过期时间
     * @param dict
     * @param seconds
     */
    public void expireDictValue(String dict, long seconds){
    	redisDictTemplate.expire(dict, seconds, TimeUnit.SECONDS);
    }
}
