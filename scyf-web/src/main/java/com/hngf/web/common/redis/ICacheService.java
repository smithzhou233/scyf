package com.hngf.web.common.redis;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import redis.clients.jedis.Tuple;

public interface ICacheService {
	
	public boolean isExist(String paramString);

	public boolean isExist(byte[] paramArrayOfByte);

	public Long del(String paramString);

	public Long del(byte[] paramArrayOfByte);

	public Long del(byte[]... paramVarArgs);

	public Long del(String... paramVarArgs);

	public Long ttl(String paramString);

	public Long ttl(byte[] paramArrayOfByte);

	public Long expire(String paramString, int paramInt);

	public Long expire(byte[] paramArrayOfByte, int paramInt);

	public boolean setString(String paramString1, String paramString2);

	@Deprecated
	public boolean setByte(String paramString, byte[] paramArrayOfByte);

	public boolean setByte(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2);

	public boolean setObject(String paramString, Object paramObject);

	public boolean setex(String paramString1, String paramString2, int paramInt);

	public boolean setex(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, int paramInt);

	public boolean setexObject(String paramString, Object paramObject, int paramInt);

	public String getString(String paramString);

	@Deprecated
	public byte[] getByte(String paramString);

	public byte[] getByte(byte[] paramArrayOfByte);

	public Object getObject(String paramString);

	public Long incr(String paramString);

	public Long incrBy(String paramString, long paramLong);

	public Long incr(byte[] paramArrayOfByte);

	public Long incrBy(byte[] paramArrayOfByte, long paramLong);

	public Long decr(String paramString);

	public Long decrBy(String paramString, long paramLong);

	public Long decr(byte[] paramArrayOfByte);

	public Long decrBy(byte[] paramArrayOfByte, long paramLong);

	public Long llen(byte[] paramArrayOfByte);

	public Long llen(String paramString);

	public Long lpush(byte[] paramArrayOfByte, byte[]... paramVarArgs);

	public Long lpush(String paramString, String... paramVarArgs);

	public Long rpush(String paramString, String... paramVarArgs);

	public List<String> lrange(String paramString, long paramLong1, long paramLong2);

	public List<byte[]> lrange(byte[] paramArrayOfByte, long paramLong1, long paramLong2);
	
	public <T> List<T> lrangeObject(String key, long start, long stop);

	public byte[] lindex(byte[] paramArrayOfByte, long paramLong);

	public String lindex(String paramString, long paramLong);

	public byte[] lpop(byte[] paramArrayOfByte);

	public String lpop(String paramString);

	public Long lrem(String paramString1, long paramLong, String paramString2);

	public Long lrem(byte[] paramArrayOfByte1, long paramLong, byte[] paramArrayOfByte2);
	
	public String rpop(String paramString);
	
	public byte[] rpop(byte[] paramArrayOfByte);
	
	public boolean ltrim(String paramString, long paramLong1, long paramLong2);
	
	public boolean ltrim(byte[] paramArrayOfByte, long paramLong1, long paramLong2);
	
	public Long rpush(byte[] paramArrayOfByte, byte[]... paramVarArgs);
	
	public <T> Long rpushObject(String key, T element);
	
	@Deprecated
	public String rpoplpush(String paramString1, String paramString2);
	
	@Deprecated
	public byte[] rpoplpush(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2);
	
	public Long lpushx(String paramString, String... paramVarArgs);
	
	public Long lpushx(byte[] paramArrayOfByte, byte[]... paramVarArgs);
	
	public Long rpushx(String paramString, String... paramVarArgs);
	
	public Long rpushx(byte[] paramArrayOfByte, byte[]... paramVarArgs);

	public String putObjectMap(String paramString, Map<String, Object> paramMap);

	public String putMap(String paramString, Map<String, String> paramMap);

	public String putMap(byte[] paramArrayOfByte, Map<byte[], byte[]> paramMap);

	public Map<String, String> getMap(String paramString);

	public Map<byte[], byte[]> getMap(byte[] paramArrayOfByte);

	public Long hset(String paramString1, String paramString2, String paramString3);

	public Long hset(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, byte[] paramArrayOfByte3);

	public Long hsetObject(String paramString1, String paramString2, Object paramObject);

	public String hget(String paramString1, String paramString2);

	public byte[] hget(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2);

	public Object hgetObject(String paramString1, String paramString2);

	public Long hsetnx(String paramString1, String paramString2, String paramString3);

	public Long hsetnxObject(String paramString1, String paramString2, Object paramObject);

	public Long hsetnx(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, byte[] paramArrayOfByte3);

	public Long hlen(String paramString);

	public Long hlen(byte[] paramArrayOfByte);

	public Boolean hexists(String paramString1, String paramString2);

	public Boolean hexists(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2);

	public Set<String> hkeys(String paramString);

	public Set<byte[]> hkeys(byte[] paramArrayOfByte);

	public List<String> hvals(String paramString);

	public Collection<byte[]> hvals(byte[] paramArrayOfByte);

	public Long hdel(String paramString, String... paramVarArgs);

	public Long hdel(byte[] paramArrayOfByte, byte[]... paramVarArgs);
	
	public Long hincrBy(String key, String field, int delta);

	public Long sadd(String paramString, String... paramVarArgs);

	public Long sadd(byte[] paramArrayOfByte, byte[]... paramVarArgs);

	public Long scard(String paramString);

	public Long scard(byte[] paramArrayOfByte);

	public Set<String> smembers(String paramString);

	public Set<byte[]> smembers(byte[] paramArrayOfByte);

	public Long zadd(String paramString1, double paramDouble, String paramString2);

	public Long zadd(byte[] paramArrayOfByte1, double paramDouble, byte[] paramArrayOfByte2);

	public Double zincrby(byte[] paramArrayOfByte1, double paramDouble, byte[] paramArrayOfByte2);

	public Double zincrby(String paramString1, double paramDouble, String paramString2);

	public Set<String> zrevrangeByScore(String paramString, double paramDouble1, double paramDouble2);

	public Set<String> zrevrangeByScore(String paramString, double paramDouble1, double paramDouble2, int paramInt1,
                                        int paramInt2);

	public Set<byte[]> zrevrangeByScore(byte[] paramArrayOfByte, double paramDouble1, double paramDouble2);

	public Set<byte[]> zrevrangeByScore(byte[] paramArrayOfByte, double paramDouble1, double paramDouble2,
                                        int paramInt1, int paramInt2);

	public Set<Tuple> zrevrangeByScoreWithScores(String paramString, double paramDouble1, double paramDouble2);

	public Set<Tuple> zrevrangeByScoreWithScores(byte[] paramArrayOfByte, double paramDouble1, double paramDouble2);

	public Set<Tuple> zrevrangeByScoreWithScores(String paramString, double paramDouble1, double paramDouble2,
                                                 int paramInt1, int paramInt2);

	public Set<Tuple> zrevrangeByScoreWithScores(byte[] paramArrayOfByte, double paramDouble1, double paramDouble2,
                                                 int paramInt1, int paramInt2);

	public Long zremrangeByScore(String paramString, double paramDouble1, double paramDouble2);

	public Long zremrangeByScore(String paramString1, String paramString2, String paramString3);

	public Long zremrangeByScore(byte[] paramArrayOfByte, double paramDouble1, double paramDouble2);

	public Long zremrangeByScore(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, byte[] paramArrayOfByte3);

	public Long zrem(String paramString, String... paramVarArgs);

	public Long zrem(byte[] paramArrayOfByte, byte[]... paramVarArgs);

	public Set<String> keys(String paramString);

	public Double zscore(String paramString1, String paramString2);

	public Double zscore(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2);

	public Set<String> zrange(String paramString, long paramLong1, long paramLong2);

	public Set<byte[]> zrange(byte[] paramArrayOfByte, long paramLong1, long paramLong2);

	public Set<Tuple> zrangeWithScores(String paramString, long paramLong1, long paramLong2);

	public Set<Tuple> zrangeWithScores(byte[] paramArrayOfByte, long paramLong1, long paramLong2);

	public Set<Tuple> zrangeByScoreWithScores(String paramString, double paramDouble1, double paramDouble2);

	public Set<Tuple> zrangeByScoreWithScores(String paramString, double paramDouble1, double paramDouble2,
                                              int paramInt1, int paramInt2);

	public Set<Tuple> zrangeByScoreWithScores(byte[] paramArrayOfByte, double paramDouble1, double paramDouble2);

	public Set<Tuple> zrangeByScoreWithScores(byte[] paramArrayOfByte, double paramDouble1, double paramDouble2,
                                              int paramInt1, int paramInt2);

	public Set<String> zrevrange(String paramString, long paramLong1, long paramLong2);

	public Set<byte[]> zrevrange(byte[] paramArrayOfByte, long paramLong1, long paramLong2);

	public Long zrank(String paramString1, String paramString2);

	public Long zrank(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2);

	public Long zrevrank(String paramString1, String paramString2);

	public Long zrevrank(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2);

	public List<String> mget(String... paramVarArgs);

	public List<byte[]> mget(byte[]... paramVarArgs);

	public String spop(String paramString);

	public byte[] spop(byte[] paramArrayOfByte);

	public Long srem(String paramString, String... paramVarArgs);

	public Long srem(byte[] paramArrayOfByte, byte[]... paramVarArgs);

	public Long setnx(String paramString1, String paramString2);

	public Long setnx(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2);

	public Map<String, String> hgetFromCache(String cacheKey);

}
