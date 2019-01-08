package com.zbjdl.common.redis;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import redis.clients.jedis.BinaryClient.LIST_POSITION;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;
import redis.clients.jedis.SortingParams;
import redis.clients.jedis.Tuple;

/**
 * 
 * <p>Title:封装Redis 具体操作方法</p>
 * <p>Description:</p>
 *
 * @author jiyong.ye
 * @version 1.0
 * @since 2015年6月4日
 */
public class RedisClientTemplate {

	private static Logger log =LoggerFactory.getLogger(RedisClientTemplate.class);


    /**
     * 设置单个值
     * 
     * @param key
     * @param value
     * @return
     */
    public String set(String key, String value) {
        String result = null;
        Jedis jedis = null;
        boolean broken = false;
        try {
        	jedis = RedisClientUtils.getResource();
        	result = jedis.set(key, value);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            broken = true;
        } finally {
            RedisClientUtils.closeResource(jedis,broken);;
        }
        return result;
    }

    /**
     * 获取单个值
     * 
     * @param key
     * @return
     */
    public String get(String key) {
        String result = null;
        Jedis jedis = null;
        boolean broken = false;
        try {
        	jedis = RedisClientUtils.getResource();
            result = jedis.get(key);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            broken = true;
        } finally {
            RedisClientUtils.closeResource(jedis,broken);;
        }
        return result;
    }

    public Boolean exists(String key) {
        Boolean result = false;
        Jedis jedis = null;
        boolean broken = false;
        try {
        	jedis = RedisClientUtils.getResource();
            result = jedis.exists(key);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            broken = true;
        } finally {
            RedisClientUtils.closeResource(jedis,broken);;
        }
        return result;
    }

    public String type(String key) {
        String result = null;
        Jedis jedis = null;
        boolean broken = false;
        try {
        	jedis = RedisClientUtils.getResource();
            result = jedis.type(key);

        } catch (Exception e) {
            log.error(e.getMessage(), e);
            broken = true;
        } finally {
            RedisClientUtils.closeResource(jedis,broken);;
        }
        return result;
    }

    /**
     * 在某段时间后失效
     * 
     * @param key
     * @param seconds	秒
     * @return
     */
    public Long expire(String key, int seconds) {
        Long result = null;
        Jedis jedis = null;
        boolean broken = false;
        try {
        	jedis = RedisClientUtils.getResource();
            result = jedis.expire(key, seconds);

        } catch (Exception e) {
            log.error(e.getMessage(), e);
            broken = true;
        } finally {
            RedisClientUtils.closeResource(jedis,broken);;
        }
        return result;
    }

    /**
     * 在某个时间点失效
     * 
     * @param key
     * @param unixTime
     * @return
     */
    public Long expireAt(String key, long unixTime) {
        Long result = null;
        Jedis jedis = null;
        boolean broken = false;
        try {
        	jedis = RedisClientUtils.getResource();
            result = jedis.expireAt(key, unixTime);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            broken = true;
        } finally {
            RedisClientUtils.closeResource(jedis,broken);;
        }
        return result;
    }
    /**
     * 以秒为单位，返回给定 key 的剩余生存时间(TTL, time to live)。
     * @param key
     * @return
     */
    public Long ttl(String key) {
        Long result = null;
        Jedis jedis = null;
        boolean broken = false;
        try {
        	jedis = RedisClientUtils.getResource();
            result = jedis.ttl(key);

        } catch (Exception e) {
            log.error(e.getMessage(), e);
            broken = true;
        } finally {
            RedisClientUtils.closeResource(jedis,broken);;
        }
        return result;
    }
    /**
     * 对 key 所储存的字符串值，设置或清除指定偏移量上的位(bit)。
     * @param key
     * @param offset
     * @param value
     * @return
     */
    public boolean setbit(String key, long offset, boolean value) {

        boolean result = false;
        Jedis jedis = null;
        boolean broken = false;
        try {
        	jedis = RedisClientUtils.getResource();
            result = jedis.setbit(key, offset, value);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            broken = true;
        } finally {
            RedisClientUtils.closeResource(jedis,broken);;
        }
        return result;
    }

    public boolean getbit(String key, long offset) {
        boolean result = false;
        Jedis jedis = null;
        boolean broken = false;

        try {
        	jedis = RedisClientUtils.getResource();
            result = jedis.getbit(key, offset);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            broken = true;
        } finally {
            RedisClientUtils.closeResource(jedis,broken);;
        }
        return result;
    }

    public long setrange(String key, long offset, String value) {
        Jedis jedis = null;
        long result = 0;
        boolean broken = false;
        try {
        	jedis = RedisClientUtils.getResource();
            result = jedis.setrange(key, offset, value);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            broken = true;
        } finally {
            RedisClientUtils.closeResource(jedis,broken);;
        }
        return result;
    }

    public String getrange(String key, long startOffset, long endOffset) {
        Jedis jedis = null;
        String result = null;
        boolean broken = false;
        try {
        	jedis = RedisClientUtils.getResource();
            result = jedis.getrange(key, startOffset, endOffset);

        } catch (Exception e) {
            log.error(e.getMessage(), e);
            broken = true;
        } finally {
            RedisClientUtils.closeResource(jedis,broken);;
        }
        return result;
    }

    public String getSet(String key, String value) {
        String result = null;
        Jedis jedis = null;
        boolean broken = false;
        try {
        	jedis = RedisClientUtils.getResource();
            result = jedis.getSet(key, value);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            broken = true;
        } finally {
            RedisClientUtils.closeResource(jedis,broken);;
        }
        return result;
    }

    public Long setnx(String key, String value) {
        Long result = null;
        Jedis jedis = null;
        boolean broken = false;
        try {
        	jedis = RedisClientUtils.getResource();
        	result = jedis.setnx(key, value);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            broken = true;
        } finally {
            RedisClientUtils.closeResource(jedis,broken);;
        }
        return result;
    }

    public String setex(String key, int seconds, String value) {
        String result = null;
        Jedis jedis = null;
        
        boolean broken = false;
        try {
        	jedis = RedisClientUtils.getResource();
        	result = jedis.setex(key, seconds, value);

        } catch (Exception e) {
            log.error(e.getMessage(), e);
            broken = true;
        } finally {
            RedisClientUtils.closeResource(jedis,broken);;
        }
        return result;
    }

    public Long decrBy(String key, long integer) {
        Long result = null;
        Jedis jedis = null;
        boolean broken = false;
        try {
        	jedis = RedisClientUtils.getResource();
        	result = jedis.decrBy(key, integer);

        } catch (Exception e) {
            log.error(e.getMessage(), e);
            broken = true;
        } finally {
            RedisClientUtils.closeResource(jedis,broken);;
        }
        return result;
    }

    public Long decr(String key) {
        Long result = null;
        Jedis jedis = null;
        boolean broken = false;
        try {
        	jedis = RedisClientUtils.getResource();
        	result = jedis.decr(key);

        } catch (Exception e) {
            log.error(e.getMessage(), e);
            broken = true;
        } finally {
            RedisClientUtils.closeResource(jedis,broken);;
        }
        return result;
    }
    
    public Long incrBy(String key, long integer) {
        Long result = null;
        Jedis jedis = null;
        boolean broken = false;
        try {
        	jedis = RedisClientUtils.getResource();
        	result = jedis.incrBy(key, integer);

        } catch (Exception e) {
            log.error(e.getMessage(), e);
            broken = true;
        } finally {
            RedisClientUtils.closeResource(jedis,broken);;
        }
        return result;
    }
    public Double incrByFloat(byte[] key, double value) {
    	Double result = null;
        Jedis jedis = null;
        boolean broken = false;
        try {
        	jedis = RedisClientUtils.getResource();
        	result = jedis.incrByFloat(key, value);

        } catch (Exception e) {
            log.error(e.getMessage(), e);
            broken = true;
        } finally {
            RedisClientUtils.closeResource(jedis,broken);;
        }
        return result;
    }
    public Double incrByFloat(String key, double value) {
    	Double result = null;
        Jedis jedis = null;
        boolean broken = false;
        try {
        	jedis = RedisClientUtils.getResource();
        	result = jedis.incrByFloat(key, value);

        } catch (Exception e) {
            log.error(e.getMessage(), e);
            broken = true;
        } finally {
            RedisClientUtils.closeResource(jedis,broken);;
        }
        return result;
    }
    public Long incr(String key) {
        Long result = null;
        Jedis jedis = null;
        boolean broken = false;
        try {
        	jedis = RedisClientUtils.getResource();
            result = jedis.incr(key);

        } catch (Exception e) {
            log.error(e.getMessage(), e);
            broken = true;
        } finally {
            RedisClientUtils.closeResource(jedis,broken);;
        }
        return result;
    }

    public Long append(String key, String value) {
        Long result = null;
        Jedis jedis = null;
        boolean broken = false;
        try {
        	jedis = RedisClientUtils.getResource();
            result = jedis.append(key, value);

        } catch (Exception e) {
            log.error(e.getMessage(), e);
            broken = true;
        } finally {
            RedisClientUtils.closeResource(jedis,broken);;
        }
        return result;
    }

    public String substr(String key, int start, int end) {
        String result = null;
        Jedis jedis = null;
        boolean broken = false;
        try {
        	jedis = RedisClientUtils.getResource();
            result = jedis.substr(key, start, end);

        } catch (Exception e) {
            log.error(e.getMessage(), e);
            broken = true;
        } finally {
            RedisClientUtils.closeResource(jedis,broken);;
        }
        return result;
    }

    public Long hset(String key, String field, String value) {
        Long result = null;
        Jedis jedis = null;
        boolean broken = false;
        try {
        	jedis = RedisClientUtils.getResource();
            result = jedis.hset(key, field, value);

        } catch (Exception e) {
            log.error(e.getMessage(), e);
            broken = true;
        } finally {
            RedisClientUtils.closeResource(jedis,broken);;
        }
        return result;
    }

    public String hget(String key, String field) {
        String result = null;
        Jedis jedis = null;
        boolean broken = false;
        try {
        	jedis = RedisClientUtils.getResource();
            result = jedis.hget(key, field);

        } catch (Exception e) {
            log.error(e.getMessage(), e);
            broken = true;
        } finally {
            RedisClientUtils.closeResource(jedis,broken);;
        }
        return result;
    }

    public Long hsetnx(String key, String field, String value) {
        Long result = null;
        Jedis jedis = null;
        boolean broken = false;
        try {
        	jedis = RedisClientUtils.getResource();
 
            result = jedis.hsetnx(key, field, value);

        } catch (Exception e) {
            log.error(e.getMessage(), e);
            broken = true;
        } finally {
            RedisClientUtils.closeResource(jedis,broken);;
        }
        return result;
    }

    public String hmset(String key, Map<String, String> hash) {
        String result = null;
        Jedis jedis = null;
        boolean broken = false;
        try {
        	jedis = RedisClientUtils.getResource();
 
            result = jedis.hmset(key, hash);

        } catch (Exception e) {
            log.error(e.getMessage(), e);
            broken = true;
        } finally {
            RedisClientUtils.closeResource(jedis,broken);;
        }
        return result;
    }

    public List<String> hmget(String key, String... fields) {
        List<String> result = null;
        Jedis jedis = null;
        boolean broken = false;
        try {
        	jedis = RedisClientUtils.getResource();
 
            result = jedis.hmget(key, fields);

        } catch (Exception e) {
            log.error(e.getMessage(), e);
            broken = true;
        } finally {
            RedisClientUtils.closeResource(jedis,broken);;
        }
        return result;
    }

    public Long hincrBy(String key, String field, long value) {
        Long result = null;
        Jedis jedis = null;
        boolean broken = false;
        try {
        	jedis = RedisClientUtils.getResource();
 
            result = jedis.hincrBy(key, field, value);

        } catch (Exception e) {
            log.error(e.getMessage(), e);
            broken = true;
        } finally {
            RedisClientUtils.closeResource(jedis,broken);;
        }
        return result;
    }

    public Boolean hexists(String key, String field) {
        Boolean result = false;
        Jedis jedis = null;
        boolean broken = false;
        try {
        	jedis = RedisClientUtils.getResource();
 
            result = jedis.hexists(key, field);

        } catch (Exception e) {
            log.error(e.getMessage(), e);
            broken = true;
        } finally {
            RedisClientUtils.closeResource(jedis,broken);;
        }
        return result;
    }

    public Long del(String key) {
        Long result = null;
        Jedis jedis = null;
        boolean broken = false;
        try {
        	jedis = RedisClientUtils.getResource();
 
            result = jedis.del(key);

        } catch (Exception e) {
            log.error(e.getMessage(), e);
            broken = true;
        } finally {
            RedisClientUtils.closeResource(jedis,broken);;
        }
        return result;
    }

    public Long hdel(String key, String field) {
        Long result = null;
        Jedis jedis = null;
        boolean broken = false;
        try {
        	jedis = RedisClientUtils.getResource();
 
            result = jedis.hdel(key, field);

        } catch (Exception e) {
            log.error(e.getMessage(), e);
            broken = true;
        } finally {
            RedisClientUtils.closeResource(jedis,broken);;
        }
        return result;
    }

    public Long hlen(String key) {
        Long result = null;
        Jedis jedis = null;
        boolean broken = false;
        try {
        	jedis = RedisClientUtils.getResource();
 
            result = jedis.hlen(key);

        } catch (Exception e) {
            log.error(e.getMessage(), e);
            broken = true;
        } finally {
            RedisClientUtils.closeResource(jedis,broken);;
        }
        return result;
    }

    public Set<String> hkeys(String key) {
        Set<String> result = null;
        Jedis jedis = null;
        boolean broken = false;
        try {
        	jedis = RedisClientUtils.getResource();
 
            result = jedis.hkeys(key);

        } catch (Exception e) {
            log.error(e.getMessage(), e);
            broken = true;
        } finally {
            RedisClientUtils.closeResource(jedis,broken);;
        }
        return result;
    }

    public List<String> hvals(String key) {
        List<String> result = null;
        Jedis jedis = null;
        boolean broken = false;
        try {
        	jedis = RedisClientUtils.getResource();
 
            result = jedis.hvals(key);

        } catch (Exception e) {
            log.error(e.getMessage(), e);
            broken = true;
        } finally {
            RedisClientUtils.closeResource(jedis,broken);;
        }
        return result;
    }

    public Map<String, String> hgetAll(String key) {
        Map<String, String> result = null;
        Jedis jedis = null;
        boolean broken = false;
        try {
        	jedis = RedisClientUtils.getResource();
 
            result = jedis.hgetAll(key);

        } catch (Exception e) {
            log.error(e.getMessage(), e);
            broken = true;
        } finally {
            RedisClientUtils.closeResource(jedis,broken);;
        }
        return result;
    }

    // ================list ====== l表示 list或 left, r表示right====================
    public Long rpush(String key, String string) {
        Long result = null;
        Jedis jedis = null;
        boolean broken = false;
        try {
        	jedis = RedisClientUtils.getResource();
 
            result = jedis.rpush(key, string);

        } catch (Exception e) {
            log.error(e.getMessage(), e);
            broken = true;
        } finally {
            RedisClientUtils.closeResource(jedis,broken);;
        }
        return result;
    }

    public Long lpush(String key, String string) {
        Long result = null;
        Jedis jedis = null;
        boolean broken = false;
        try {
        	jedis = RedisClientUtils.getResource();
 
            result = jedis.lpush(key, string);

        } catch (Exception e) {
            log.error(e.getMessage(), e);
            broken = true;
        } finally {
            RedisClientUtils.closeResource(jedis,broken);;
        }
        return result;
    }

    public Long llen(String key) {
        Long result = null;
        Jedis jedis = null;
        boolean broken = false;
        try {
        	jedis = RedisClientUtils.getResource();
 
            result = jedis.llen(key);

        } catch (Exception e) {
            log.error(e.getMessage(), e);
            broken = true;
        } finally {
            RedisClientUtils.closeResource(jedis,broken);;
        }
        return result;
    }

    public List<String> lrange(String key, long start, long end) {
        List<String> result = null;
        Jedis jedis = null;
        boolean broken = false;
        try {
        	jedis = RedisClientUtils.getResource();
 
            result = jedis.lrange(key, start, end);

        } catch (Exception e) {
            log.error(e.getMessage(), e);
            broken = true;
        } finally {
            RedisClientUtils.closeResource(jedis,broken);;
        }
        return result;
    }

    public String ltrim(String key, long start, long end) {
        String result = null;
        Jedis jedis = null;
        boolean broken = false;
        try {
        	jedis = RedisClientUtils.getResource();
 
            result = jedis.ltrim(key, start, end);

        } catch (Exception e) {
            log.error(e.getMessage(), e);
            broken = true;
        } finally {
            RedisClientUtils.closeResource(jedis,broken);;
        }
        return result;
    }

    public String lindex(String key, long index) {
        String result = null;
        Jedis jedis = null;
        boolean broken = false;
        try {
        	jedis = RedisClientUtils.getResource();
 
            result = jedis.lindex(key, index);

        } catch (Exception e) {
            log.error(e.getMessage(), e);
            broken = true;
        } finally {
            RedisClientUtils.closeResource(jedis,broken);;
        }
        return result;
    }

    public String lset(String key, long index, String value) {
        String result = null;
        Jedis jedis = null;
        boolean broken = false;
        try {
        	jedis = RedisClientUtils.getResource();
 
            result = jedis.lset(key, index, value);

        } catch (Exception e) {
            log.error(e.getMessage(), e);
            broken = true;
        } finally {
            RedisClientUtils.closeResource(jedis,broken);;
        }
        return result;
    }

    public Long lrem(String key, long count, String value) {
        Long result = null;
        Jedis jedis = null;
        boolean broken = false;
        try {
        	jedis = RedisClientUtils.getResource();
 
            result = jedis.lrem(key, count, value);

        } catch (Exception e) {
            log.error(e.getMessage(), e);
            broken = true;
        } finally {
            RedisClientUtils.closeResource(jedis,broken);;
        }
        return result;
    }

    public String lpop(String key) {
        String result = null;
        Jedis jedis = null;
        boolean broken = false;
        try {
        	jedis = RedisClientUtils.getResource();
 
            result = jedis.lpop(key);

        } catch (Exception e) {
            log.error(e.getMessage(), e);
            broken = true;
        } finally {
            RedisClientUtils.closeResource(jedis,broken);;
        }
        return result;
    }
    public List<String> blpop(int timeout,String key) {
    	List<String> result = null;
        Jedis jedis = null;
        boolean broken = false;
        try {
        	jedis = RedisClientUtils.getResource();
 
            result = jedis.blpop(timeout, key);

        } catch (Exception e) {
            log.error(e.getMessage(), e);
            broken = true;
        } finally {
            RedisClientUtils.closeResource(jedis,broken);;
        }
        return result;
    }
    public List<String> blpop(int timeout,String... keys) {
    	List<String> result = null;
        Jedis jedis = null;
        boolean broken = false;
        try {
        	jedis = RedisClientUtils.getResource();
 
            result = jedis.blpop(timeout, keys);

        } catch (Exception e) {
            log.error(e.getMessage(), e);
            broken = true;
        } finally {
            RedisClientUtils.closeResource(jedis,broken);;
        }
        return result;
    }
    public String rpop(String key) {
        String result = null;
        Jedis jedis = null;
        boolean broken = false;
        try {
        	jedis = RedisClientUtils.getResource();
 
            result = jedis.rpop(key);

        } catch (Exception e) {
            log.error(e.getMessage(), e);
            broken = true;
        } finally {
            RedisClientUtils.closeResource(jedis,broken);;
        }
        return result;
    }
    public List<String> brpop(int timeout,String key) {
    	List<String> result = null;
        Jedis jedis = null;
        boolean broken = false;
        try {
        	jedis = RedisClientUtils.getResource();
 
            result = jedis.brpop(timeout,key);

        } catch (Exception e) {
            log.error(e.getMessage(), e);
            broken = true;
        } finally {
            RedisClientUtils.closeResource(jedis,broken);;
        }
        return result;
    }
    public List<String> brpop(int timeout,String... keys) {
    	List<String> result = null;
        Jedis jedis = null;
        boolean broken = false;
        try {
        	jedis = RedisClientUtils.getResource();
 
            result = jedis.brpop(timeout, keys);

        } catch (Exception e) {
            log.error(e.getMessage(), e);
            broken = true;
        } finally {
            RedisClientUtils.closeResource(jedis,broken);;
        }
        return result;
    }
    //return 1 add a not exist value ,
    //return 0 add a exist value
    public Long sadd(String key, String member) {
        Long result = null;
        Jedis jedis = null;
        boolean broken = false;
        try {
        	jedis = RedisClientUtils.getResource();
 
            result = jedis.sadd(key, member);

        } catch (Exception e) {
            log.error(e.getMessage(), e);
            broken = true;
        } finally {
            RedisClientUtils.closeResource(jedis,broken);;
        }
        return result;
    }

    public Set<String> smembers(String key) {
        Set<String> result = null;
        Jedis jedis = null;
        boolean broken = false;
        try {
        	jedis = RedisClientUtils.getResource();
 
            result = jedis.smembers(key);

        } catch (Exception e) {
            log.error(e.getMessage(), e);
            broken = true;
        } finally {
            RedisClientUtils.closeResource(jedis,broken);;
        }
        return result;
    }

    public Long srem(String key, String member) {
        Jedis jedis = null;

        Long result = null;
        boolean broken = false;
        try {
        	jedis = RedisClientUtils.getResource();
 
            result = jedis.srem(key, member);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            broken = true;
        } finally {
            RedisClientUtils.closeResource(jedis,broken);;
        }
        return result;
    }

    public String spop(String key) {
        Jedis jedis = null;
        String result = null;
        boolean broken = false;
        try {
        	jedis = RedisClientUtils.getResource();
 
            result = jedis.spop(key);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            broken = true;
        } finally {
            RedisClientUtils.closeResource(jedis,broken);;
        }
        return result;
    }

    public Long scard(String key) {
        Jedis jedis = null;
        Long result = null;
        boolean broken = false;
        try {
        	jedis = RedisClientUtils.getResource();
 
            result = jedis.scard(key);

        } catch (Exception e) {
            log.error(e.getMessage(), e);
            broken = true;
        } finally {
            RedisClientUtils.closeResource(jedis,broken);;
        }
        return result;
    }

    public Boolean sismember(String key, String member) {
        Jedis jedis = null;
        Boolean result = null;
        boolean broken = false;
        try {
        	jedis = RedisClientUtils.getResource();
 
            result = jedis.sismember(key, member);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            broken = true;
        } finally {
            RedisClientUtils.closeResource(jedis,broken);;
        }
        return result;
    }

    public String srandmember(String key) {
        Jedis jedis = null;
        String result = null;
        boolean broken = false;
        try {
        	jedis = RedisClientUtils.getResource();
 
            result = jedis.srandmember(key);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            broken = true;
        } finally {
            RedisClientUtils.closeResource(jedis,broken);;
        }
        return result;
    }

    public Long zadd(String key, double score, String member) {
        Long result = null;
        Jedis jedis = null;
        boolean broken = false;
        try {
        	jedis = RedisClientUtils.getResource();
 
            result = jedis.zadd(key, score, member);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            broken = true;
        } finally {
            RedisClientUtils.closeResource(jedis,broken);;
        }
        return result;
    }

    public Set<String> zrange(String key, int start, int end) {
        Set<String> result = null;
        Jedis jedis = null;
        boolean broken = false;
        try {
        	jedis = RedisClientUtils.getResource();
 
            result = jedis.zrange(key, start, end);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            broken = true;
        } finally {
            RedisClientUtils.closeResource(jedis,broken);;
        }
        return result;
    }

    public Long zrem(String key, String member) {
        Long result = null;
        Jedis jedis = null;
        boolean broken = false;
        try {
        	jedis = RedisClientUtils.getResource();
 
            result = jedis.zrem(key, member);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            broken = true;
        } finally {
            RedisClientUtils.closeResource(jedis,broken);;
        }
        return result;
    }

    public Double zincrby(String key, double score, String member) {
        Double result = null;
        Jedis jedis = null;
        boolean broken = false;
        try {
        	jedis = RedisClientUtils.getResource();
 

            result = jedis.zincrby(key, score, member);

        } catch (Exception e) {
            log.error(e.getMessage(), e);
            broken = true;
        } finally {
            RedisClientUtils.closeResource(jedis,broken);;
        }
        return result;
    }

    public Long zrank(String key, String member) {
        Long result = null;
        Jedis jedis = null;
        boolean broken = false;
        try {
        	jedis = RedisClientUtils.getResource();
 

            result = jedis.zrank(key, member);

        } catch (Exception e) {
            log.error(e.getMessage(), e);
            broken = true;
        } finally {
            RedisClientUtils.closeResource(jedis,broken);;
        }
        return result;
    }

    public Long zrevrank(String key, String member) {
        Long result = null;
        Jedis jedis = null;
        boolean broken = false;
        try {
        	jedis = RedisClientUtils.getResource();
 

            result = jedis.zrevrank(key, member);

        } catch (Exception e) {
            log.error(e.getMessage(), e);
            broken = true;
        } finally {
            RedisClientUtils.closeResource(jedis,broken);;
        }
        return result;
    }

    public Set<String> zrevrange(String key, int start, int end) {
        Set<String> result = null;
        Jedis jedis = null;
        boolean broken = false;
        try {
        	jedis = RedisClientUtils.getResource();
 

            result = jedis.zrevrange(key, start, end);

        } catch (Exception e) {
            log.error(e.getMessage(), e);
            broken = true;
        } finally {
            RedisClientUtils.closeResource(jedis,broken);;
        }
        return result;
    }

    public Set<Tuple> zrangeWithScores(String key, int start, int end) {
        Set<Tuple> result = null;
        Jedis jedis = null;
        boolean broken = false;
        try {
        	jedis = RedisClientUtils.getResource();
 

            result = jedis.zrangeWithScores(key, start, end);

        } catch (Exception e) {
            log.error(e.getMessage(), e);
            broken = true;
        } finally {
            RedisClientUtils.closeResource(jedis,broken);;
        }
        return result;
    }

    public Set<Tuple> zrevrangeWithScores(String key, int start, int end) {
        Set<Tuple> result = null;
        Jedis jedis = null;
        boolean broken = false;
        try {
        	jedis = RedisClientUtils.getResource();
 

            result = jedis.zrevrangeWithScores(key, start, end);

        } catch (Exception e) {
            log.error(e.getMessage(), e);
            broken = true;
        } finally {
            RedisClientUtils.closeResource(jedis,broken);;
        }
        return result;
    }

    public Long zcard(String key) {
        Long result = null;
        Jedis jedis = null;
        boolean broken = false;
        try {
        	jedis = RedisClientUtils.getResource();
 

            result = jedis.zcard(key);

        } catch (Exception e) {
            log.error(e.getMessage(), e);
            broken = true;
        } finally {
            RedisClientUtils.closeResource(jedis,broken);;
        }
        return result;
    }

    public Double zscore(String key, String member) {
        Double result = null;
        Jedis jedis = null;
        boolean broken = false;
        try {
        	jedis = RedisClientUtils.getResource();
 

            result = jedis.zscore(key, member);

        } catch (Exception e) {
            log.error(e.getMessage(), e);
            broken = true;
        } finally {
            RedisClientUtils.closeResource(jedis,broken);;
        }
        return result;
    }

    public List<String> sort(String key) {
        List<String> result = null;
        Jedis jedis = null;
        boolean broken = false;
        try {
        	jedis = RedisClientUtils.getResource();
 

            result = jedis.sort(key);

        } catch (Exception e) {
            log.error(e.getMessage(), e);
            broken = true;
        } finally {
            RedisClientUtils.closeResource(jedis,broken);;
        }
        return result;
    }

    public List<String> sort(String key, SortingParams sortingParameters) {
        List<String> result = null;
        Jedis jedis = null;
        boolean broken = false;
        try {
        	jedis = RedisClientUtils.getResource();
 

            result = jedis.sort(key, sortingParameters);

        } catch (Exception e) {
            log.error(e.getMessage(), e);
            broken = true;
        } finally {
            RedisClientUtils.closeResource(jedis,broken);;
        }
        return result;
    }

    public Long zcount(String key, double min, double max) {
        Long result = null;
        Jedis jedis = null;
        boolean broken = false;
        try {
        	jedis = RedisClientUtils.getResource();
 

            result = jedis.zcount(key, min, max);

        } catch (Exception e) {
            log.error(e.getMessage(), e);
            broken = true;
        } finally {
            RedisClientUtils.closeResource(jedis,broken);;
        }
        return result;
    }

    public Set<String> zrangeByScore(String key, double min, double max) {
        Set<String> result = null;
        Jedis jedis = null;
        boolean broken = false;
        try {
        	jedis = RedisClientUtils.getResource();
 

            result = jedis.zrangeByScore(key, min, max);

        } catch (Exception e) {
            log.error(e.getMessage(), e);
            broken = true;
        } finally {
            RedisClientUtils.closeResource(jedis,broken);;
        }
        return result;
    }

    public Set<String> zrevrangeByScore(String key, double max, double min) {
        Set<String> result = null;
        Jedis jedis = null;
        boolean broken = false;
        try {
        	jedis = RedisClientUtils.getResource();
 

            result = jedis.zrevrangeByScore(key, max, min);

        } catch (Exception e) {
            log.error(e.getMessage(), e);
            broken = true;
        } finally {
            RedisClientUtils.closeResource(jedis,broken);;
        }
        return result;
    }

    public Set<String> zrangeByScore(String key, double min, double max, int offset, int count) {
        Set<String> result = null;
        Jedis jedis = null;
        boolean broken = false;
        try {
        	jedis = RedisClientUtils.getResource();
 

            result = jedis.zrangeByScore(key, min, max, offset, count);

        } catch (Exception e) {
            log.error(e.getMessage(), e);
            broken = true;
        } finally {
            RedisClientUtils.closeResource(jedis,broken);;
        }
        return result;
    }

    public Set<String> zrevrangeByScore(String key, double max, double min, int offset, int count) {
        Set<String> result = null;
        Jedis jedis = null;
        boolean broken = false;
        try {
        	jedis = RedisClientUtils.getResource();
 

            result = jedis.zrevrangeByScore(key, max, min, offset, count);

        } catch (Exception e) {
            log.error(e.getMessage(), e);
            broken = true;
        } finally {
            RedisClientUtils.closeResource(jedis,broken);;
        }
        return result;
    }

    public Set<Tuple> zrangeByScoreWithScores(String key, double min, double max) {
        Set<Tuple> result = null;
        Jedis jedis = null;
        boolean broken = false;
        try {
        	jedis = RedisClientUtils.getResource();
 

            result = jedis.zrangeByScoreWithScores(key, min, max);

        } catch (Exception e) {
            log.error(e.getMessage(), e);
            broken = true;
        } finally {
            RedisClientUtils.closeResource(jedis,broken);;
        }
        return result;
    }

    public Set<Tuple> zrevrangeByScoreWithScores(String key, double max, double min) {
        Set<Tuple> result = null;
        Jedis jedis = null;
        boolean broken = false;
        try {
        	jedis = RedisClientUtils.getResource();
 

            result = jedis.zrevrangeByScoreWithScores(key, max, min);

        } catch (Exception e) {
            log.error(e.getMessage(), e);
            broken = true;
        } finally {
            RedisClientUtils.closeResource(jedis,broken);;
        }
        return result;
    }

    public Set<Tuple> zrangeByScoreWithScores(String key, double min, double max, int offset, int count) {
        Set<Tuple> result = null;
        Jedis jedis = null;
        boolean broken = false;
        try {
        	jedis = RedisClientUtils.getResource();
 

            result = jedis.zrangeByScoreWithScores(key, min, max, offset, count);

        } catch (Exception e) {
            log.error(e.getMessage(), e);
            broken = true;
        } finally {
            RedisClientUtils.closeResource(jedis,broken);;
        }
        return result;
    }

    public Set<Tuple> zrevrangeByScoreWithScores(String key, double max, double min, int offset, int count) {
        Set<Tuple> result = null;
        Jedis jedis = null;
        boolean broken = false;
        try {
        	jedis = RedisClientUtils.getResource();
 

            result = jedis.zrevrangeByScoreWithScores(key, max, min, offset, count);

        } catch (Exception e) {
            log.error(e.getMessage(), e);
            broken = true;
        } finally {
            RedisClientUtils.closeResource(jedis,broken);;
        }
        return result;
    }

    public Long zremrangeByRank(String key, int start, int end) {
        Long result = null;
        Jedis jedis = null;
        boolean broken = false;
        try {
        	jedis = RedisClientUtils.getResource();
 

            result = jedis.zremrangeByRank(key, start, end);

        } catch (Exception e) {
            log.error(e.getMessage(), e);
            broken = true;
        } finally {
            RedisClientUtils.closeResource(jedis,broken);;
        }
        return result;
    }

    public Long zremrangeByScore(String key, double start, double end) {
        Long result = null;
        Jedis jedis = null;
        boolean broken = false;
        try {
        	jedis = RedisClientUtils.getResource();
 

            result = jedis.zremrangeByScore(key, start, end);

        } catch (Exception e) {
            log.error(e.getMessage(), e);
            broken = true;
        } finally {
            RedisClientUtils.closeResource(jedis,broken);;
        }
        return result;
    }

    public Long linsert(String key, LIST_POSITION where, String pivot, String value) {
        Long result = null;
        Jedis jedis = null;
        boolean broken = false;
        try {
        	jedis = RedisClientUtils.getResource();
 

            result = jedis.linsert(key, where, pivot, value);

        } catch (Exception e) {
            log.error(e.getMessage(), e);
            broken = true;
        } finally {
            RedisClientUtils.closeResource(jedis,broken);;
        }
        return result;
    }

    public String set(byte[] key, byte[] value) {
        String result = null;
        Jedis jedis = null;
        boolean broken = false;
        try {
        	jedis = RedisClientUtils.getResource();
 

            result = jedis.set(key, value);

        } catch (Exception e) {
            log.error(e.getMessage(), e);
            broken = true;
        } finally {
            RedisClientUtils.closeResource(jedis,broken);;
        }
        return result;
    }

    public byte[] get(byte[] key) {
        byte[] result = null;
        Jedis jedis = null;
        boolean broken = false;
        try {
        	jedis = RedisClientUtils.getResource();
 

            result = jedis.get(key);

        } catch (Exception e) {
            log.error(e.getMessage(), e);
            broken = true;
        } finally {
            RedisClientUtils.closeResource(jedis,broken);;
        }
        return result;
    }

    public Boolean exists(byte[] key) {
        Boolean result = false;
        Jedis jedis = null;
        boolean broken = false;
        try {
        	jedis = RedisClientUtils.getResource();
 

            result = jedis.exists(key);

        } catch (Exception e) {
            log.error(e.getMessage(), e);
            broken = true;
        } finally {
            RedisClientUtils.closeResource(jedis,broken);;
        }
        return result;
    }

    public String type(byte[] key) {
        String result = null;
        Jedis jedis = null;
        boolean broken = false;
        try {
        	jedis = RedisClientUtils.getResource();
 

            result = jedis.type(key);

        } catch (Exception e) {
            log.error(e.getMessage(), e);
            broken = true;
        } finally {
            RedisClientUtils.closeResource(jedis,broken);;
        }
        return result;
    }

    public Long expire(byte[] key, int seconds) {
        Long result = null;
        Jedis jedis = null;
        boolean broken = false;
        try {
        	jedis = RedisClientUtils.getResource();
 

            result = jedis.expire(key, seconds);

        } catch (Exception e) {
            log.error(e.getMessage(), e);
            broken = true;
        } finally {
            RedisClientUtils.closeResource(jedis,broken);;
        }
        return result;
    }

    public Long expireAt(byte[] key, long unixTime) {
        Long result = null;
        Jedis jedis = null;
        boolean broken = false;
        try {
        	jedis = RedisClientUtils.getResource();
 

            result = jedis.expireAt(key, unixTime);

        } catch (Exception e) {
            log.error(e.getMessage(), e);
            broken = true;
        } finally {
            RedisClientUtils.closeResource(jedis,broken);;
        }
        return result;
    }

    public Long ttl(byte[] key) {
        Long result = null;
        Jedis jedis = null;
        boolean broken = false;
        try {
        	jedis = RedisClientUtils.getResource();
 

            result = jedis.ttl(key);

        } catch (Exception e) {
            log.error(e.getMessage(), e);
            broken = true;
        } finally {
            RedisClientUtils.closeResource(jedis,broken);;
        }
        return result;
    }

    public byte[] getSet(byte[] key, byte[] value) {
        byte[] result = null;
        Jedis jedis = null;
        boolean broken = false;
        try {
        	jedis = RedisClientUtils.getResource();
 

            result = jedis.getSet(key, value);

        } catch (Exception e) {
            log.error(e.getMessage(), e);
            broken = true;
        } finally {
            RedisClientUtils.closeResource(jedis,broken);;
        }
        return result;
    }

    public Long setnx(byte[] key, byte[] value) {
        Long result = null;
        Jedis jedis = null;
        boolean broken = false;
        try {
        	jedis = RedisClientUtils.getResource();
 

            result = jedis.setnx(key, value);

        } catch (Exception e) {
            log.error(e.getMessage(), e);
            broken = true;
        } finally {
            RedisClientUtils.closeResource(jedis,broken);;
        }
        return result;
    }

    public String setex(byte[] key, int seconds, byte[] value) {
        String result = null;
        Jedis jedis = null;
        boolean broken = false;
        try {
        	jedis = RedisClientUtils.getResource();
 

            result = jedis.setex(key, seconds, value);

        } catch (Exception e) {
            log.error(e.getMessage(), e);
            broken = true;
        } finally {
            RedisClientUtils.closeResource(jedis,broken);;
        }
        return result;
    }

    public Long decrBy(byte[] key, long integer) {
        Long result = null;
        Jedis jedis = null;
        boolean broken = false;
        try {
        	jedis = RedisClientUtils.getResource();
 

            result = jedis.decrBy(key, integer);

        } catch (Exception e) {
            log.error(e.getMessage(), e);
            broken = true;
        } finally {
            RedisClientUtils.closeResource(jedis,broken);;
        }
        return result;
    }

    public Long decr(byte[] key) {
        Long result = null;
        Jedis jedis = null;
        boolean broken = false;
        try {
        	jedis = RedisClientUtils.getResource();
 

            result = jedis.decr(key);

        } catch (Exception e) {
            log.error(e.getMessage(), e);
            broken = true;
        } finally {
            RedisClientUtils.closeResource(jedis,broken);;
        }
        return result;
    }

    public Long incrBy(byte[] key, long integer) {
        Long result = null;
        Jedis jedis = null;
        boolean broken = false;
        try {
        	jedis = RedisClientUtils.getResource();
 

            result = jedis.incrBy(key, integer);

        } catch (Exception e) {
            log.error(e.getMessage(), e);
            broken = true;
        } finally {
            RedisClientUtils.closeResource(jedis,broken);;
        }
        return result;
    }

    public Long incr(byte[] key) {
        Long result = null;
        Jedis jedis = null;
        boolean broken = false;
        try {
        	jedis = RedisClientUtils.getResource();
 

            result = jedis.incr(key);

        } catch (Exception e) {
            log.error(e.getMessage(), e);
            broken = true;
        } finally {
            RedisClientUtils.closeResource(jedis,broken);;
        }
        return result;
    }

    public Long append(byte[] key, byte[] value) {
        Long result = null;
        Jedis jedis = null;
        boolean broken = false;
        try {
        	jedis = RedisClientUtils.getResource();
 

            result = jedis.append(key, value);

        } catch (Exception e) {
            log.error(e.getMessage(), e);
            broken = true;
        } finally {
            RedisClientUtils.closeResource(jedis,broken);;
        }
        return result;
    }

    public byte[] substr(byte[] key, int start, int end) {
        byte[] result = null;
        Jedis jedis = null;
        boolean broken = false;
        try {
        	jedis = RedisClientUtils.getResource();
 

            result = jedis.substr(key, start, end);

        } catch (Exception e) {
            log.error(e.getMessage(), e);
            broken = true;
        } finally {
            RedisClientUtils.closeResource(jedis,broken);;
        }
        return result;
    }

    public Long hset(byte[] key, byte[] field, byte[] value) {
        Long result = null;
        Jedis jedis = null;
        boolean broken = false;
        try {
        	jedis = RedisClientUtils.getResource();
 

            result = jedis.hset(key, field, value);

        } catch (Exception e) {
            log.error(e.getMessage(), e);
            broken = true;
        } finally {
            RedisClientUtils.closeResource(jedis,broken);;
        }
        return result;
    }

    public byte[] hget(byte[] key, byte[] field) {
        byte[] result = null;
        Jedis jedis = null;
        boolean broken = false;
        try {
        	jedis = RedisClientUtils.getResource();
 

            result = jedis.hget(key, field);

        } catch (Exception e) {
            log.error(e.getMessage(), e);
            broken = true;
        } finally {
            RedisClientUtils.closeResource(jedis,broken);;
        }
        return result;
    }

    public Long hsetnx(byte[] key, byte[] field, byte[] value) {
        Long result = null;
        Jedis jedis = null;
        boolean broken = false;
        try {
        	jedis = RedisClientUtils.getResource();
 

            result = jedis.hsetnx(key, field, value);

        } catch (Exception e) {

            log.error(e.getMessage(), e);
            broken = true;
        } finally {
            RedisClientUtils.closeResource(jedis,broken);;
        }
        return result;
    }

    public String hmset(byte[] key, Map<byte[], byte[]> hash) {
        String result = null;
        Jedis jedis = null;
        boolean broken = false;
        try {
        	jedis = RedisClientUtils.getResource();
 

            result = jedis.hmset(key, hash);

        } catch (Exception e) {

            log.error(e.getMessage(), e);
            broken = true;
        } finally {
            RedisClientUtils.closeResource(jedis,broken);;
        }
        return result;
    }

    public List<byte[]> hmget(byte[] key, byte[]... fields) {
        List<byte[]> result = null;
        Jedis jedis = null;
        boolean broken = false;
        try {
        	jedis = RedisClientUtils.getResource();
 

            result = jedis.hmget(key, fields);

        } catch (Exception e) {

            log.error(e.getMessage(), e);
            broken = true;
        } finally {
            RedisClientUtils.closeResource(jedis,broken);;
        }
        return result;
    }

    public Long hincrBy(byte[] key, byte[] field, long value) {
        Long result = null;
        Jedis jedis = null;
        boolean broken = false;
        try {
        	jedis = RedisClientUtils.getResource();
 

            result = jedis.hincrBy(key, field, value);

        } catch (Exception e) {

            log.error(e.getMessage(), e);
            broken = true;
        } finally {
            RedisClientUtils.closeResource(jedis,broken);;
        }
        return result;
    }

    public Boolean hexists(byte[] key, byte[] field) {
        Boolean result = false;
        Jedis jedis = null;
        boolean broken = false;
        try {
        	jedis = RedisClientUtils.getResource();
 

            result = jedis.hexists(key, field);

        } catch (Exception e) {

            log.error(e.getMessage(), e);
            broken = true;
        } finally {
            RedisClientUtils.closeResource(jedis,broken);;
        }
        return result;
    }

    public Long hdel(byte[] key, byte[] field) {
        Long result = null;
        Jedis jedis = null;
        boolean broken = false;
        try {
        	jedis = RedisClientUtils.getResource();
 

            result = jedis.hdel(key, field);

        } catch (Exception e) {

            log.error(e.getMessage(), e);
            broken = true;
        } finally {
            RedisClientUtils.closeResource(jedis,broken);;
        }
        return result;
    }

    public Long hlen(byte[] key) {
        Long result = null;
        Jedis jedis = null;
        boolean broken = false;
        try {
        	jedis = RedisClientUtils.getResource();
 

            result = jedis.hlen(key);

        } catch (Exception e) {

            log.error(e.getMessage(), e);
            broken = true;
        } finally {
            RedisClientUtils.closeResource(jedis,broken);;
        }
        return result;
    }

    public Set<byte[]> hkeys(byte[] key) {
        Set<byte[]> result = null;
        Jedis jedis = null;
        boolean broken = false;
        try {
        	jedis = RedisClientUtils.getResource();
 

            result = jedis.hkeys(key);

        } catch (Exception e) {

            log.error(e.getMessage(), e);
            broken = true;
        } finally {
            RedisClientUtils.closeResource(jedis,broken);;
        }
        return result;
    }

    public Collection<byte[]> hvals(byte[] key) {
        Collection<byte[]> result = null;
        Jedis jedis = null;
        boolean broken = false;
        try {
        	jedis = RedisClientUtils.getResource();
 

            result = jedis.hvals(key);

        } catch (Exception e) {

            log.error(e.getMessage(), e);
            broken = true;
        } finally {
            RedisClientUtils.closeResource(jedis,broken);;
        }
        return result;
    }

    public Map<byte[], byte[]> hgetAll(byte[] key) {
        Map<byte[], byte[]> result = null;
        Jedis jedis = null;
        boolean broken = false;
        try {
        	jedis = RedisClientUtils.getResource();
 

            result = jedis.hgetAll(key);

        } catch (Exception e) {

            log.error(e.getMessage(), e);
            broken = true;
        } finally {
            RedisClientUtils.closeResource(jedis,broken);;
        }
        return result;
    }

    public Long rpush(byte[] key, byte[] string) {
        Long result = null;
        Jedis jedis = null;
        boolean broken = false;
        try {
        	jedis = RedisClientUtils.getResource();
 

            result = jedis.rpush(key, string);

        } catch (Exception e) {

            log.error(e.getMessage(), e);
            broken = true;
        } finally {
            RedisClientUtils.closeResource(jedis,broken);;
        }
        return result;
    }

    public Long lpush(byte[] key, byte[] string) {
        Long result = null;
        Jedis jedis = null;
        boolean broken = false;
        try {
        	jedis = RedisClientUtils.getResource();
 

            result = jedis.lpush(key, string);

        } catch (Exception e) {

            log.error(e.getMessage(), e);
            broken = true;
        } finally {
            RedisClientUtils.closeResource(jedis,broken);;
        }
        return result;
    }

    public Long llen(byte[] key) {
        Long result = null;
        Jedis jedis = null;
        boolean broken = false;
        try {
        	jedis = RedisClientUtils.getResource();
 

            result = jedis.llen(key);

        } catch (Exception e) {

            log.error(e.getMessage(), e);
            broken = true;
        } finally {
            RedisClientUtils.closeResource(jedis,broken);;
        }
        return result;
    }

    public List<byte[]> lrange(byte[] key, int start, int end) {
        List<byte[]> result = null;
        Jedis jedis = null;
        boolean broken = false;
        try {
        	jedis = RedisClientUtils.getResource();
 

            result = jedis.lrange(key, start, end);

        } catch (Exception e) {

            log.error(e.getMessage(), e);
            broken = true;
        } finally {
            RedisClientUtils.closeResource(jedis,broken);;
        }
        return result;
    }

    public String ltrim(byte[] key, int start, int end) {
        String result = null;
        Jedis jedis = null;
        boolean broken = false;
        try {
        	jedis = RedisClientUtils.getResource();
 

            result = jedis.ltrim(key, start, end);

        } catch (Exception e) {
            log.error(e.getMessage(), e);
            broken = true;
        } finally {
            RedisClientUtils.closeResource(jedis,broken);;
        }
        return result;
    }

    public byte[] lindex(byte[] key, int index) {
        byte[] result = null;
        Jedis jedis = null;
        boolean broken = false;
        try {
        	jedis = RedisClientUtils.getResource();
 

            result = jedis.lindex(key, index);

        } catch (Exception e) {

            log.error(e.getMessage(), e);
            broken = true;
        } finally {
            RedisClientUtils.closeResource(jedis,broken);;
        }
        return result;
    }

    public String lset(byte[] key, int index, byte[] value) {
        String result = null;
        Jedis jedis = null;
        boolean broken = false;
        try {
        	jedis = RedisClientUtils.getResource();
 

            result = jedis.lset(key, index, value);

        } catch (Exception e) {

            log.error(e.getMessage(), e);
            broken = true;
        } finally {
            RedisClientUtils.closeResource(jedis,broken);;
        }
        return result;
    }

    public Long lrem(byte[] key, int count, byte[] value) {
        Long result = null;
        Jedis jedis = null;
        boolean broken = false;
        try {
        	jedis = RedisClientUtils.getResource();
 

            result = jedis.lrem(key, count, value);

        } catch (Exception e) {

            log.error(e.getMessage(), e);
            broken = true;
        } finally {
            RedisClientUtils.closeResource(jedis,broken);;
        }
        return result;
    }

    public byte[] lpop(byte[] key) {
        byte[] result = null;
        Jedis jedis = null;
        boolean broken = false;
        try {
        	jedis = RedisClientUtils.getResource();
 

            result = jedis.lpop(key);

        } catch (Exception e) {

            log.error(e.getMessage(), e);
            broken = true;
        } finally {
            RedisClientUtils.closeResource(jedis,broken);;
        }
        return result;
    }

    public byte[] rpop(byte[] key) {
        byte[] result = null;
        Jedis jedis = null;
        boolean broken = false;
        try {
        	jedis = RedisClientUtils.getResource();
 

            result = jedis.rpop(key);

        } catch (Exception e) {

            log.error(e.getMessage(), e);
            broken = true;
        } finally {
            RedisClientUtils.closeResource(jedis,broken);;
        }
        return result;
    }

    public Long sadd(byte[] key, byte[] member) {
        Long result = null;
        Jedis jedis = null;
        boolean broken = false;
        try {
        	jedis = RedisClientUtils.getResource();
 

            result = jedis.sadd(key, member);

        } catch (Exception e) {

            log.error(e.getMessage(), e);
            broken = true;
        } finally {
            RedisClientUtils.closeResource(jedis,broken);;
        }
        return result;
    }

    public Set<byte[]> smembers(byte[] key) {
        Set<byte[]> result = null;
        Jedis jedis = null;
        boolean broken = false;
        try {
        	jedis = RedisClientUtils.getResource();
 

            result = jedis.smembers(key);

        } catch (Exception e) {

            log.error(e.getMessage(), e);
            broken = true;
        } finally {
            RedisClientUtils.closeResource(jedis,broken);;
        }
        return result;
    }

    public Long srem(byte[] key, byte[] member) {
        Long result = null;
        Jedis jedis = null;
        boolean broken = false;
        try {
        	jedis = RedisClientUtils.getResource();
 

            result = jedis.srem(key, member);

        } catch (Exception e) {

            log.error(e.getMessage(), e);
            broken = true;
        } finally {
            RedisClientUtils.closeResource(jedis,broken);;
        }
        return result;
    }

    public byte[] spop(byte[] key) {
        byte[] result = null;
        Jedis jedis = null;
        boolean broken = false;
        try {
        	jedis = RedisClientUtils.getResource();
 

            result = jedis.spop(key);

        } catch (Exception e) {

            log.error(e.getMessage(), e);
            broken = true;
        } finally {
            RedisClientUtils.closeResource(jedis,broken);;
        }
        return result;
    }

    public Long scard(byte[] key) {
        Long result = null;
        Jedis jedis = null;
        boolean broken = false;
        try {
        	jedis = RedisClientUtils.getResource();
 

            result = jedis.scard(key);

        } catch (Exception e) {

            log.error(e.getMessage(), e);
            broken = true;
        } finally {
            RedisClientUtils.closeResource(jedis,broken);;
        }
        return result;
    }

    public Boolean sismember(byte[] key, byte[] member) {
        Boolean result = false;
        Jedis jedis = null;
        boolean broken = false;
        try {
        	jedis = RedisClientUtils.getResource();
 

            result = jedis.sismember(key, member);

        } catch (Exception e) {

            log.error(e.getMessage(), e);
            broken = true;
        } finally {
            RedisClientUtils.closeResource(jedis,broken);;
        }
        return result;
    }

    public byte[] srandmember(byte[] key) {
        byte[] result = null;
        Jedis jedis = null;
        boolean broken = false;
        try {
        	jedis = RedisClientUtils.getResource();
 

            result = jedis.srandmember(key);

        } catch (Exception e) {

            log.error(e.getMessage(), e);
            broken = true;
        } finally {
            RedisClientUtils.closeResource(jedis,broken);;
        }
        return result;
    }

    public Long zadd(byte[] key, double score, byte[] member) {
        Long result = null;
        Jedis jedis = null;
        boolean broken = false;
        try {
        	jedis = RedisClientUtils.getResource();
 

            result = jedis.zadd(key, score, member);

        } catch (Exception e) {

            log.error(e.getMessage(), e);
            broken = true;
        } finally {
            RedisClientUtils.closeResource(jedis,broken);;
        }
        return result;
    }

    public Set<byte[]> zrange(byte[] key, int start, int end) {
        Set<byte[]> result = null;
        Jedis jedis = null;
        boolean broken = false;
        try {
        	jedis = RedisClientUtils.getResource();
 

            result = jedis.zrange(key, start, end);

        } catch (Exception e) {

            log.error(e.getMessage(), e);
            broken = true;
        } finally {
            RedisClientUtils.closeResource(jedis,broken);;
        }
        return result;
    }

    public Long zrem(byte[] key, byte[] member) {
        Long result = null;
        Jedis jedis = null;
        boolean broken = false;
        try {
        	jedis = RedisClientUtils.getResource();
 

            result = jedis.zrem(key, member);

        } catch (Exception e) {

            log.error(e.getMessage(), e);
            broken = true;
        } finally {
            RedisClientUtils.closeResource(jedis,broken);;
        }
        return result;
    }

    public Double zincrby(byte[] key, double score, byte[] member) {
        Double result = null;
        Jedis jedis = null;
        boolean broken = false;
        try {
        	jedis = RedisClientUtils.getResource();
 

            result = jedis.zincrby(key, score, member);

        } catch (Exception e) {

            log.error(e.getMessage(), e);
            broken = true;
        } finally {
            RedisClientUtils.closeResource(jedis,broken);;
        }
        return result;
    }

    public Long zrank(byte[] key, byte[] member) {
        Long result = null;
        Jedis jedis = null;
        boolean broken = false;
        try {
        	jedis = RedisClientUtils.getResource();
 

            result = jedis.zrank(key, member);

        } catch (Exception e) {

            log.error(e.getMessage(), e);
            broken = true;
        } finally {
            RedisClientUtils.closeResource(jedis,broken);;
        }
        return result;
    }

    public Long zrevrank(byte[] key, byte[] member) {
        Long result = null;
        Jedis jedis = null;
        boolean broken = false;
        try {
        	jedis = RedisClientUtils.getResource();
 

            result = jedis.zrevrank(key, member);

        } catch (Exception e) {

            log.error(e.getMessage(), e);
            broken = true;
        } finally {
            RedisClientUtils.closeResource(jedis,broken);;
        }
        return result;
    }

    public Set<byte[]> zrevrange(byte[] key, int start, int end) {
        Set<byte[]> result = null;
        Jedis jedis = null;
        boolean broken = false;
        try {
        	jedis = RedisClientUtils.getResource();
 

            result = jedis.zrevrange(key, start, end);

        } catch (Exception e) {

            log.error(e.getMessage(), e);
            broken = true;
        } finally {
            RedisClientUtils.closeResource(jedis,broken);;
        }
        return result;
    }

    public Set<Tuple> zrangeWithScores(byte[] key, int start, int end) {
        Set<Tuple> result = null;
        Jedis jedis = null;
        boolean broken = false;
        try {
        	jedis = RedisClientUtils.getResource();
 

            result = jedis.zrangeWithScores(key, start, end);

        } catch (Exception e) {

            log.error(e.getMessage(), e);
            broken = true;
        } finally {
            RedisClientUtils.closeResource(jedis,broken);;
        }
        return result;
    }

    public Set<Tuple> zrevrangeWithScores(byte[] key, int start, int end) {
        Set<Tuple> result = null;
        Jedis jedis = null;
        boolean broken = false;
        try {
        	jedis = RedisClientUtils.getResource();
 

            result = jedis.zrevrangeWithScores(key, start, end);

        } catch (Exception e) {

            log.error(e.getMessage(), e);
            broken = true;
        } finally {
            RedisClientUtils.closeResource(jedis,broken);;
        }
        return result;
    }

    public Long zcard(byte[] key) {
        Long result = null;
        Jedis jedis = null;
        boolean broken = false;
        try {
        	jedis = RedisClientUtils.getResource();
 

            result = jedis.zcard(key);

        } catch (Exception e) {

            log.error(e.getMessage(), e);
            broken = true;
        } finally {
            RedisClientUtils.closeResource(jedis,broken);;
        }
        return result;
    }

    public Double zscore(byte[] key, byte[] member) {
        Double result = null;
        Jedis jedis = null;
        boolean broken = false;
        try {
        	jedis = RedisClientUtils.getResource();
 

            result = jedis.zscore(key, member);

        } catch (Exception e) {

            log.error(e.getMessage(), e);
            broken = true;
        } finally {
            RedisClientUtils.closeResource(jedis,broken);;
        }
        return result;
    }

    public List<byte[]> sort(byte[] key) {
        List<byte[]> result = null;
        Jedis jedis = null;
        boolean broken = false;
        try {
        	jedis = RedisClientUtils.getResource();
 

            result = jedis.sort(key);

        } catch (Exception e) {

            log.error(e.getMessage(), e);
            broken = true;
        } finally {
            RedisClientUtils.closeResource(jedis,broken);;
        }
        return result;
    }

    public List<byte[]> sort(byte[] key, SortingParams sortingParameters) {
        List<byte[]> result = null;
        Jedis jedis = null;
        boolean broken = false;
        try {
        	jedis = RedisClientUtils.getResource();
 

            result = jedis.sort(key, sortingParameters);

        } catch (Exception e) {

            log.error(e.getMessage(), e);
            broken = true;
        } finally {
            RedisClientUtils.closeResource(jedis,broken);;
        }
        return result;
    }

    public Long zcount(byte[] key, double min, double max) {
        Long result = null;
        Jedis jedis = null;
        boolean broken = false;
        try {
        	jedis = RedisClientUtils.getResource();
 

            result = jedis.zcount(key, min, max);

        } catch (Exception e) {

            log.error(e.getMessage(), e);
            broken = true;
        } finally {
            RedisClientUtils.closeResource(jedis,broken);;
        }
        return result;
    }

    public Set<byte[]> zrangeByScore(byte[] key, double min, double max) {
        Set<byte[]> result = null;
        Jedis jedis = null;
        boolean broken = false;
        try {
        	jedis = RedisClientUtils.getResource();
 

            result = jedis.zrangeByScore(key, min, max);

        } catch (Exception e) {

            log.error(e.getMessage(), e);
            broken = true;
        } finally {
            RedisClientUtils.closeResource(jedis,broken);;
        }
        return result;
    }

    public Set<byte[]> zrangeByScore(byte[] key, double min, double max, int offset, int count) {
        Set<byte[]> result = null;
        Jedis jedis = null;
        boolean broken = false;
        try {
        	jedis = RedisClientUtils.getResource();
 

            result = jedis.zrangeByScore(key, min, max, offset, count);

        } catch (Exception e) {

            log.error(e.getMessage(), e);
            broken = true;
        } finally {
            RedisClientUtils.closeResource(jedis,broken);;
        }
        return result;
    }

    public Set<Tuple> zrangeByScoreWithScores(byte[] key, double min, double max) {
        Set<Tuple> result = null;
        Jedis jedis = null;
        boolean broken = false;
        try {
        	jedis = RedisClientUtils.getResource();
 

            result = jedis.zrangeByScoreWithScores(key, min, max);

        } catch (Exception e) {

            log.error(e.getMessage(), e);
            broken = true;
        } finally {
            RedisClientUtils.closeResource(jedis,broken);;
        }
        return result;
    }

    public Set<Tuple> zrangeByScoreWithScores(byte[] key, double min, double max, int offset, int count) {
        Set<Tuple> result = null;
        Jedis jedis = null;
        boolean broken = false;
        try {
        	jedis = RedisClientUtils.getResource();
 

            result = jedis.zrangeByScoreWithScores(key, min, max, offset, count);

        } catch (Exception e) {

            log.error(e.getMessage(), e);
            broken = true;
        } finally {
            RedisClientUtils.closeResource(jedis,broken);;
        }
        return result;
    }

    public Set<byte[]> zrevrangeByScore(byte[] key, double max, double min) {
        Set<byte[]> result = null;
        Jedis jedis = null;
        boolean broken = false;
        try {
        	jedis = RedisClientUtils.getResource();
 

            result = jedis.zrevrangeByScore(key, max, min);

        } catch (Exception e) {

            log.error(e.getMessage(), e);
            broken = true;
        } finally {
            RedisClientUtils.closeResource(jedis,broken);;
        }
        return result;
    }

    public Set<byte[]> zrevrangeByScore(byte[] key, double max, double min, int offset, int count) {
        Set<byte[]> result = null;
        Jedis jedis = null;
        boolean broken = false;
        try {
        	jedis = RedisClientUtils.getResource();
 

            result = jedis.zrevrangeByScore(key, max, min, offset, count);

        } catch (Exception e) {

            log.error(e.getMessage(), e);
            broken = true;
        } finally {
            RedisClientUtils.closeResource(jedis,broken);;
        }
        return result;
    }

    public Set<Tuple> zrevrangeByScoreWithScores(byte[] key, double max, double min) {
        Set<Tuple> result = null;
        Jedis jedis = null;
        boolean broken = false;
        try {
        	jedis = RedisClientUtils.getResource();
 

            result = jedis.zrevrangeByScoreWithScores(key, max, min);

        } catch (Exception e) {

            log.error(e.getMessage(), e);
            broken = true;
        } finally {
            RedisClientUtils.closeResource(jedis,broken);;
        }
        return result;
    }

    public Set<Tuple> zrevrangeByScoreWithScores(byte[] key, double max, double min, int offset, int count) {
        Set<Tuple> result = null;
        Jedis jedis = null;
        boolean broken = false;
        try {
        	jedis = RedisClientUtils.getResource();
 

            result = jedis.zrevrangeByScoreWithScores(key, max, min, offset, count);

        } catch (Exception e) {

            log.error(e.getMessage(), e);
            broken = true;
        } finally {
            RedisClientUtils.closeResource(jedis,broken);;
        }
        return result;
    }

    public Long zremrangeByRank(byte[] key, int start, int end) {
        Long result = null;
        Jedis jedis = null;
        boolean broken = false;
        try {
        	jedis = RedisClientUtils.getResource();
 

            result = jedis.zremrangeByRank(key, start, end);

        } catch (Exception e) {

            log.error(e.getMessage(), e);
            broken = true;
        } finally {
            RedisClientUtils.closeResource(jedis,broken);;
        }
        return result;
    }

    public Long zremrangeByScore(byte[] key, double start, double end) {
        Long result = null;
        Jedis jedis = null;
        boolean broken = false;
        try {
        	jedis = RedisClientUtils.getResource();
 

            result = jedis.zremrangeByScore(key, start, end);

        } catch (Exception e) {
            log.error(e.getMessage(), e);
            broken = true;
        } finally {
            RedisClientUtils.closeResource(jedis,broken);;
        }
        return result;
    }

    public Long linsert(byte[] key, LIST_POSITION where, byte[] pivot, byte[] value) {
        Long result = null;
        Jedis jedis = null;
        boolean broken = false;
        try {
        	jedis = RedisClientUtils.getResource();
 

            result = jedis.linsert(key, where, pivot, value);

        } catch (Exception e) {
            log.error(e.getMessage(), e);
            broken = true;
        } finally {
            RedisClientUtils.closeResource(jedis,broken);;
        }
        return result;
    }
    /**
     * 查找所有符合给定模式 pattern 的 key 。
		KEYS * 匹配数据库中所有 key 。
		KEYS h?llo 匹配 hello ， hallo 和 hxllo 等。
		KEYS h*llo 匹配 hllo 和 heeeeello 等。
		KEYS h[ae]llo 匹配 hello 和 hallo ，但不匹配 hillo 。
     * @param pattern
     * @return
     */
    public Set<String> keys(String pattern) {
        Set<String> result = null;
        Jedis jedis = null;
        boolean broken = false;
        try {
        	jedis = RedisClientUtils.getResource();
            result = jedis.keys(pattern);
        } catch (Exception e) {

            log.error(e.getMessage(), e);
            broken = true;
        } finally {
            RedisClientUtils.closeResource(jedis,broken);;
        }
        return result;
    }
 
    /**
     * 发布订阅
     * 
     * @param key
     * @param value
     * @return
     */
    public Long publish(String channel, String message) {
    	Long result = null;
        Jedis jedis = null;
        boolean broken = false;
        try {
        	jedis = RedisClientUtils.getResource();
        	result = jedis.publish(channel, message);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            broken = true;
        } finally {
            RedisClientUtils.closeResource(jedis,broken);;
        }
        return result;
    }
    /**
     * 订阅多个频道
     * @param jedisPubSub
     * @param channels
     */
    public void subscribe(final JedisPubSub jedisPubSub, final String... channels) {
    	Jedis jedis = null;
    	boolean broken = false;
    	try {
    		jedis = RedisClientUtils.getResource();
    		jedis.subscribe(jedisPubSub, channels);
    	} catch (Exception e) {
    		log.error(e.getMessage(), e);
    		broken = true;
    	} finally {
    		RedisClientUtils.closeResource(jedis,broken);;
    	}
    }
    /**
     * 订阅多个模式
     * @param jedisPubSub
     * @param patterns
     */
    public void psubscribe(final JedisPubSub jedisPubSub, final String... patterns) {
    	Jedis jedis = null;
    	boolean broken = false;
    	try {
    		jedis = RedisClientUtils.getResource();
    		jedis.psubscribe(jedisPubSub, patterns);
    	} catch (Exception e) {
    		log.error(e.getMessage(), e);
    		broken = true;
    	} finally {
    		RedisClientUtils.closeResource(jedis,broken);;
    	}
    }
}