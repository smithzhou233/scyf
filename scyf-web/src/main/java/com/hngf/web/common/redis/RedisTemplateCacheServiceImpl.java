package com.hngf.web.common.redis;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import com.hngf.web.common.redis.serializer.SpecialRedisSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations.TypedTuple;
import org.springframework.util.StringUtils;

import redis.clients.jedis.BinaryClient;
import redis.clients.jedis.Tuple;

/**
 * <p>使用RedisTemplate重新实现
 * @author lixinjie
 * @since 2018-11-28
 */
public class RedisTemplateCacheServiceImpl implements ICacheService {

	private static final Logger LOGGER = LoggerFactory.getLogger(RedisTemplateCacheServiceImpl.class);

	@Autowired
	private SpecialRedisSerializer specialRedisSerializer;
	
	@Autowired
	private RedisTemplate<Object, Object> specialRedisTemplate;

	public boolean isExist(String key) {
		boolean flag = false;
		if (StringUtils.isEmpty(key)) {
			return flag;
		}
		flag = specialRedisTemplate.hasKey(key);
		return flag;
	}

	public boolean isExist(byte[] key) {
		boolean flag = false;
		if ((key == null) || (key.length == 0)) {
			return flag;
		}
		flag = specialRedisTemplate.hasKey(key);
		return flag;
	}

	public Long del(String key) {
		long result = 0L;
		if (StringUtils.isEmpty(key)) {
			return Long.valueOf(result);
		}
		specialRedisTemplate.delete(key);
		result = 1;
		return Long.valueOf(result);
	}

	public Long del(byte[] key) {
		long result = 0L;
		if ((key == null) || (key.length == 0)) {
			return Long.valueOf(result);
		}
		specialRedisTemplate.delete(key);
		result = 1;
		return Long.valueOf(result);
	}

	@SuppressWarnings("unchecked")
	public Long del(byte[]... keys) {
		long result = 0L;
		specialRedisTemplate.delete((Collection<Object>) (Object) Arrays.asList(keys));
		result = 1;
		return Long.valueOf(result);
	}

	@SuppressWarnings("unchecked")
	public Long del(String... keys) {
		long result = 0L;
		specialRedisTemplate.delete((Collection<Object>) (Object) Arrays.asList(keys));
		result = 1;
		return Long.valueOf(result);
	}

	public Long ttl(String key) {
		long result = 0L;
		if (StringUtils.isEmpty(key)) {
			return Long.valueOf(result);
		}
		result = specialRedisTemplate.getExpire(key);
		return Long.valueOf(result);
	}

	public Long ttl(byte[] key) {
		long result = 0L;
		if ((key == null) || (key.length == 0)) {
			return Long.valueOf(result);
		}
		result = specialRedisTemplate.getExpire(key);
		return Long.valueOf(result);
	}

	public Long expire(String key, int seconds) {
		long result = 0L;
		if (StringUtils.isEmpty(key)) {
			return Long.valueOf(result);
		}
		if (seconds >= 0) {
			boolean value = specialRedisTemplate.expire(key, seconds, TimeUnit.SECONDS);
			if (value) {
				result = 1;
			}
		}
		return Long.valueOf(result);
	}

	public Long expire(byte[] key, int seconds) {
		long result = 0L;
		if ((key == null) || (key.length == 0)) {
			return Long.valueOf(result);
		}
		if (seconds >= 0) {
			boolean value = specialRedisTemplate.expire(key, seconds, TimeUnit.SECONDS);
			if (value) {
				result = 1;
			}
		}
		return Long.valueOf(result);
	}

	public boolean setString(String key, String value) {
		boolean flag = false;
		if ((StringUtils.isEmpty(key)) || (StringUtils.isEmpty(value))) {
			return flag;
		}
		specialRedisTemplate.opsForValue().set(key, value);
		flag = true;
		return flag;
	}

	public boolean setObject(String key, Object value) {
		boolean flag = false;
		if ((StringUtils.isEmpty(key)) || (null == value)) {
			return flag;
		}
		specialRedisTemplate.opsForValue().set(key, value);
		flag = true;
		return flag;
	}

	public boolean setByte(String key, byte[] value) {
		if (StringUtils.isEmpty(key)) {
			return false;
		}
		return setByte(specialRedisSerializer.serialize(key), value);
	}

	public boolean setByte(byte[] key, byte[] value) {
		boolean flag = false;
		if ((key == null) || (key.length == 0) || (null == value) || (value.length == 0)) {
			return flag;
		}
		specialRedisTemplate.opsForValue().set(key, value);
		flag = true;
		return flag;
	}

	public boolean setex(String key, String value, int seconds) {
		boolean flag = false;
		if ((StringUtils.isEmpty(key)) || (StringUtils.isEmpty(value))) {
			return flag;
		}
		if (seconds >= 0) {
			specialRedisTemplate.opsForValue().set(key, value, seconds, TimeUnit.SECONDS);
			flag = true;
		}
		return flag;
	}

	public boolean setex(byte[] key, byte[] value, int seconds) {
		boolean flag = false;
		if ((key == null) || (key.length == 0) || (value == null) || (value.length == 0)) {
			return flag;
		}
		if (seconds >= 0) {
			specialRedisTemplate.opsForValue().set(key, value, seconds, TimeUnit.SECONDS);
			flag = true;
		}
		return flag;
	}

	public boolean setexObject(String key, Object value, int seconds) {
		boolean flag = false;
		if ((StringUtils.isEmpty(key)) || (null == value)) {
			return flag;
		}
		if (seconds >= 0) {
			specialRedisTemplate.opsForValue().set(key, value, seconds, TimeUnit.SECONDS);
			flag = true;
		}
		return flag;
	}

	public String getString(String key) {
		String value = null;
		if (StringUtils.isEmpty(key)) {
			return value;
		}
		byte[] bytes = (byte[])specialRedisTemplate.opsForValue().get(key);
		value = bytesToString(bytes);
		return value;
	}

	public byte[] getByte(String key) {
		if (StringUtils.isEmpty(key)) {
			return null;
		}
		return getByte(specialRedisSerializer.serialize(key));
	}

	public byte[] getByte(byte[] key) {
		byte[] value = null;
		if ((key == null) || (key.length == 0)) {
			return value;
		}
		value = (byte[])specialRedisTemplate.opsForValue().get(key);
		return value;
	}

	public Object getObject(String key) {
		Object value = null;
		if (StringUtils.isEmpty(key)) {
			return value;
		}
		byte[] result = (byte[])specialRedisTemplate.opsForValue().get(key);
		if ((result == null) || (result.length == 0)) {
			return value;
		}
		value = specialRedisSerializer.ofObject(result);
		return value;
	}

	public Long incr(String key) {
		long result = 0L;
		if (StringUtils.isEmpty(key)) {
			return Long.valueOf(result);
		}
		result = specialRedisTemplate.opsForValue().increment(key, 1);
		return Long.valueOf(result);
	}

	public Long incrBy(String key, long integer) {
		long result = 0L;
		if (StringUtils.isEmpty(key)) {
			return Long.valueOf(result);
		}
		result = specialRedisTemplate.opsForValue().increment(key, integer);
		return Long.valueOf(result);
	}

	public Long incr(byte[] key) {
		long result = 0L;
		if ((key == null) || (key.length == 0)) {
			return Long.valueOf(result);
		}
		result = specialRedisTemplate.opsForValue().increment(key, 1);
		return Long.valueOf(result);
	}

	public Long incrBy(byte[] key, long integer) {
		long result = 0L;
		if ((key == null) || (key.length == 0)) {
			return Long.valueOf(result);
		}
		result = specialRedisTemplate.opsForValue().increment(key, integer);
		return Long.valueOf(result);
	}

	public Long decr(String key) {
		long result = 0L;
		if (StringUtils.isEmpty(key)) {
			return Long.valueOf(result);
		}
		result = specialRedisTemplate.opsForValue().increment(key, -1);
		return Long.valueOf(result);
	}

	public Long decrBy(String key, long integer) {
		long result = 0L;
		if (StringUtils.isEmpty(key)) {
			return Long.valueOf(result);
		}
		result = specialRedisTemplate.opsForValue().increment(key, -integer);
		return Long.valueOf(result);
	}

	public Long decr(byte[] key) {
		long result = 0L;
		if ((key == null) || (key.length == 0)) {
			return Long.valueOf(result);
		}
		result = specialRedisTemplate.opsForValue().increment(key, -1);
		return Long.valueOf(result);
	}

	public Long decrBy(byte[] key, long integer) {
		long result = 0L;
		if ((key == null) || (key.length == 0)) {
			return Long.valueOf(result);
		}
		result = specialRedisTemplate.opsForValue().increment(key, -integer);
		return Long.valueOf(result);
	}

	public Long llen(byte[] key) {
		long result = 0L;
		if ((key == null) || (key.length == 0)) {
			return Long.valueOf(result);
		}
		result = specialRedisTemplate.opsForList().size(key);
		return Long.valueOf(result);
	}

	public Long llen(String key) {
		long result = 0L;
		if (StringUtils.isEmpty(key)) {
			return Long.valueOf(result);
		}
		result = specialRedisTemplate.opsForList().size(key);
		return Long.valueOf(result);
	}

	public Long lpush(byte[] key, byte[]... values) {
		long result = 0L;
		if ((key == null) || (key.length == 0)) {
			return Long.valueOf(result);
		}
		result = specialRedisTemplate.opsForList().leftPushAll(key, values);
		return Long.valueOf(result);
	}

	public Long lpush(String key, String... values) {
		long result = 0L;
		if (StringUtils.isEmpty(key)) {
			return Long.valueOf(result);
		}
		result = specialRedisTemplate.opsForList().leftPushAll(key, values);
		return Long.valueOf(result);
	}

	public Long rpush(String key, String... values) {
		long result = 0L;
		if (StringUtils.isEmpty(key)) {
			return Long.valueOf(result);
		}
		result = specialRedisTemplate.opsForList().rightPushAll(key, values);
		return Long.valueOf(result);
	}

	@SuppressWarnings("unchecked")
	public List<String> lrange(String key, long start, long end) {
		List<String> value = null;
		if (StringUtils.isEmpty(key)) {
			return value;
		}
		List<byte[]> bytesList = (List<byte[]>)(Object)specialRedisTemplate.opsForList().range(key, start, end);
		value = bytesToString(bytesList);
		return value;
	}

	@SuppressWarnings("unchecked")
	public List<byte[]> lrange(byte[] key, long start, long end) {
		List<byte[]> value = null;
		if ((key == null) || (key.length == 0)) {
			return value;
		}
		value = (List<byte[]>)(Object)specialRedisTemplate.opsForList().range(key, start, end);
		return value;
	}
	
	@Override
	public <T> List<T> lrangeObject(String key, long start, long stop) {
		if ((key == null) || (key.length() == 0)) {
			return Collections.emptyList();
		}
		List<byte[]> values = (List<byte[]>)(Object)specialRedisTemplate.opsForList().range(key, start, stop);
		return bytesToObject(values);
	}

	public byte[] lindex(byte[] key, long index) {
		byte[] value = null;
		if ((key == null) || (key.length == 0)) {
			return value;
		}
		value = (byte[])specialRedisTemplate.opsForList().index(key, index);
		return value;
	}

	public String lindex(String key, long index) {
		String value = null;
		if (StringUtils.isEmpty(key)) {
			return value;
		}
		byte[] bytes = (byte[])specialRedisTemplate.opsForList().index(key, index);
		value = bytesToString(bytes);
		return value;
	}

	public byte[] lpop(byte[] key) {
		byte[] value = null;
		if ((key == null) || (key.length == 0)) {
			return value;
		}
		value = (byte[])specialRedisTemplate.opsForList().leftPop(key);
		return value;
	}

	public String lpop(String key) {
		String value = null;
		if (StringUtils.isEmpty(key)) {
			return value;
		}
		byte[] bytes = (byte[])specialRedisTemplate.opsForList().leftPop(key);
		value = bytesToString(bytes);
		return value;
	}

	public Long lrem(String key, long count, String value) {
		long result = 0L;
		if (StringUtils.isEmpty(key)) {
			return Long.valueOf(result);
		}
		result = specialRedisTemplate.opsForList().remove(key, count, value);
		return result;
	}

	public Long lrem(byte[] key, long count, byte[] value) {
		long result = 0L;
		if (StringUtils.isEmpty(key)) {
			return Long.valueOf(result);
		}
		result = specialRedisTemplate.opsForList().remove(key, count, value);
		return result;
	}

	public String putObjectMap(String key, Map<String, Object> map) {
		String result = null;
		if ((StringUtils.isEmpty(key)) || (map == null) || (map.size() < 1)) {
			return result;
		}
		specialRedisTemplate.opsForHash().putAll(key, map);
		result = "OK";
		return result;
	}

	public String putMap(String key, Map<String, String> map) {
		String result = null;
		if ((StringUtils.isEmpty(key)) || (map == null) || (map.size() < 1)) {
			return result;
		}
		specialRedisTemplate.opsForHash().putAll(key, map);
		result = "OK";
		return result;
	}

	public String putMap(byte[] key, Map<byte[], byte[]> map) {
		String result = null;
		if ((key == null) || (key.length == 0) || (map == null) || (map.size() < 1)) {
			return result;
		}
		specialRedisTemplate.opsForHash().putAll(key, map);
		result = "OK";
		return result;
	}

	@SuppressWarnings("unchecked")
	public Map<String, String> getMap(String key) {
		Map<String, String> value = null;
		if (StringUtils.isEmpty(key)) {
			return value;
		}
		Map<byte[], byte[]> bytesMap = (Map<byte[], byte[]>)(Object)specialRedisTemplate.opsForHash().entries(key);
		value = bytesToString(bytesMap);
		return value;
	}

	@SuppressWarnings("unchecked")
	public Map<byte[], byte[]> getMap(byte[] key) {
		Map<byte[], byte[]> value = null;
		if ((key == null) || (key.length == 0)) {
			return value;
		}
		value = (Map<byte[], byte[]>)(Object)specialRedisTemplate.opsForHash().entries(key);
		return value;
	}

	public Long hset(String key, String field, String value) {
		long result = 0L;
		if ((StringUtils.isEmpty(key)) || (StringUtils.isEmpty(field)) || (StringUtils.isEmpty(value))) {
			return Long.valueOf(result);
		}
		specialRedisTemplate.opsForHash().put(key, field, value);
		result = 1;
		return Long.valueOf(result);
	}

	public Long hset(byte[] key, byte[] field, byte[] value) {
		long result = 0L;
		if ((key == null) || (key.length == 0) || (field == null) || (field.length == 0) || (value == null)
				|| (value.length == 0)) {
			return Long.valueOf(result);
		}
		specialRedisTemplate.opsForHash().put(key, field, value);
		result = 1;
		return Long.valueOf(result);
	}

	public Long hsetObject(String key, String field, Object value) {
		long result = 0L;
		if ((StringUtils.isEmpty(key)) || (StringUtils.isEmpty(field)) || (null == value)) {
			return Long.valueOf(result);
		}
		specialRedisTemplate.opsForHash().put(key, field, value);
		result = 1;
		return Long.valueOf(result);
	}

	public String hget(String key, String field) {
		String value = null;
		if ((StringUtils.isEmpty(key)) || (StringUtils.isEmpty(field))) {
			return value;
		}
		byte[] result = (byte[])specialRedisTemplate.opsForHash().get(key, field);
		value = bytesToString(result);
		return value;
	}

	public byte[] hget(byte[] key, byte[] field) {
		byte[] value = null;
		if ((key == null) || (key.length == 0) || (field == null) || (field.length == 0)) {
			return value;
		}
		value = (byte[])specialRedisTemplate.opsForHash().get(key, field);
		return value;
	}

	public Object hgetObject(String key, String field) {
		Object value = null;
		if ((StringUtils.isEmpty(key)) || (StringUtils.isEmpty(field))) {
			return value;
		}
		byte[] result = (byte[])specialRedisTemplate.opsForHash().get(key, field);
		if ((result == null) || (result.length == 0)) {
			return value;
		}
		value = specialRedisSerializer.ofObject(result);
		return value;
	}

	public Long hsetnx(String key, String field, String value) {
		long result = 0L;
		if ((StringUtils.isEmpty(key)) || (StringUtils.isEmpty(field)) || (StringUtils.isEmpty(value))) {
			return Long.valueOf(result);
		}
		boolean flag = specialRedisTemplate.opsForHash().putIfAbsent(key, field, value);
		if (flag) {
			result = 1;
		}
		return Long.valueOf(result);
	}

	public Long hsetnx(byte[] key, byte[] field, byte[] value) {
		long result = 0L;
		if ((key == null) || (key.length == 0) || (field == null) || (field.length == 0) || (value == null)
				|| (value.length == 0)) {
			return Long.valueOf(result);
		}
		boolean flag = specialRedisTemplate.opsForHash().putIfAbsent(key, field, value);
		if (flag) {
			result = 1;
		}
		return Long.valueOf(result);
	}

	public Long hsetnxObject(String key, String field, Object value) {
		long result = 0L;
		if ((StringUtils.isEmpty(key)) || (StringUtils.isEmpty(field)) || (null == value)) {
			return Long.valueOf(result);
		}
		boolean flag = specialRedisTemplate.opsForHash().putIfAbsent(key, field, value);
		if (flag) {
			result = 1;
		}
		return Long.valueOf(result);
	}

	public Long hlen(String key) {
		long result = 0L;
		if (StringUtils.isEmpty(key)) {
			return Long.valueOf(result);
		}
		result = specialRedisTemplate.opsForHash().size(key);
		return Long.valueOf(result);
	}

	public Long hlen(byte[] key) {
		long result = 0L;
		if ((key == null) || (key.length == 0)) {
			return Long.valueOf(result);
		}
		result = specialRedisTemplate.opsForHash().size(key);
		return Long.valueOf(result);
	}

	public Boolean hexists(String key, String field) {
		boolean flag = false;
		if ((StringUtils.isEmpty(key)) || (StringUtils.isEmpty(field))) {
			return Boolean.valueOf(flag);
		}
		flag = specialRedisTemplate.opsForHash().hasKey(key, field);
		return Boolean.valueOf(flag);
	}

	public Boolean hexists(byte[] key, byte[] field) {
		boolean flag = false;
		if ((key == null) || (key.length == 0) || (field == null) || (field.length == 0)) {
			return Boolean.valueOf(flag);
		}
		flag = specialRedisTemplate.opsForHash().hasKey(key, field);
		return Boolean.valueOf(flag);
	}

	@SuppressWarnings("unchecked")
	public Set<String> hkeys(String key) {
		Set<String> keys = null;
		if (StringUtils.isEmpty(key)) {
			return keys;
		}
		Set<byte[]> bytesSet = (Set<byte[]>)(Object)specialRedisTemplate.opsForHash().keys(key);
		keys = bytesToString(bytesSet);
		return keys;
	}

	@SuppressWarnings("unchecked")
	public Set<byte[]> hkeys(byte[] key) {
		Set<byte[]> keys = null;
		if ((key == null) || (key.length == 0)) {
			return keys;
		}
		keys = (Set<byte[]>)(Object)specialRedisTemplate.opsForHash().keys(key);
		return keys;
	}

	@SuppressWarnings("unchecked")
	public List<String> hvals(String key) {
		List<String> values = null;
		if (StringUtils.isEmpty(key)) {
			return values;
		}
		List<byte[]> bytesList = (List<byte[]>)(Object)specialRedisTemplate.opsForHash().values(key);
		values = bytesToString(bytesList);
		return values;
	}

	@SuppressWarnings("unchecked")
	public Collection<byte[]> hvals(byte[] key) {
		Collection<byte[]> values = null;
		if ((key == null) || (key.length == 0)) {
			return values;
		}
		values = (Collection<byte[]>)(Object)specialRedisTemplate.opsForHash().values(key);
		return values;
	}

	public Long hdel(String key, String... field) {
		long result = 0L;
		if (StringUtils.isEmpty(key)) {
			return Long.valueOf(result);
		}
		result = specialRedisTemplate.opsForHash().delete(key, field);
		return result;
	}

	public Long hdel(byte[] key, byte[]... field) {
		long result = 0L;
		if ((key == null) || (key.length == 0)) {
			return Long.valueOf(result);
		}
		result = specialRedisTemplate.opsForHash().delete(key, field);
		return result;
	}
	
	@Override
	public Long hincrBy(String key, String field, int delta) {
		Long result = 0L;
		if (StringUtils.isEmpty(key) || StringUtils.isEmpty(field)) {
			return result;
		}
		result = specialRedisTemplate.opsForHash().increment(key, field, delta);
		return result;
	}

	public Long sadd(String key, String... member) {
		long result = 0L;
		if (StringUtils.isEmpty(key)) {
			return Long.valueOf(result);
		}
		result = specialRedisTemplate.opsForSet().add(key, member);
		return Long.valueOf(result);
	}

	public Long sadd(byte[] key, byte[]... member) {
		long result = 0L;
		if ((key == null) || (key.length == 0)) {
			return Long.valueOf(result);
		}
		result = specialRedisTemplate.opsForSet().add(key, member);
		return Long.valueOf(result);
	}

	public Long scard(String key) {
		long result = 0L;
		if (StringUtils.isEmpty(key)) {
			return Long.valueOf(result);
		}
		result = specialRedisTemplate.opsForSet().size(key);
		return Long.valueOf(result);
	}

	public Long scard(byte[] key) {
		long result = 0L;
		if ((key == null) || (key.length == 0)) {
			return Long.valueOf(result);
		}
		result = specialRedisTemplate.opsForSet().size(key);
		return Long.valueOf(result);
	}

	@SuppressWarnings("unchecked")
	public Set<String> smembers(String key) {
		Set<String> value = null;
		if (StringUtils.isEmpty(key)) {
			return value;
		}
		Set<byte[]> bytesSet = (Set<byte[]>)(Object)specialRedisTemplate.opsForSet().members(key);
		value = bytesToString(bytesSet);
		return value;
	}

	@SuppressWarnings("unchecked")
	public Set<byte[]> smembers(byte[] key) {
		Set<byte[]> value = null;
		if ((key == null) || (key.length == 0)) {
			return value;
		}
		value = (Set<byte[]>)(Object)specialRedisTemplate.opsForSet().members(key);
		return value;
	}

	public Long zadd(String key, double score, String member) {
		long result = 0L;
		if ((StringUtils.isEmpty(key)) || (StringUtils.isEmpty(member))) {
			return Long.valueOf(result);
		}
		boolean value = specialRedisTemplate.opsForZSet().add(key, member, score);
		if (value) {
			result = 1;
		}
		return Long.valueOf(result);
	}

	public Long zadd(byte[] key, double score, byte[] member) {
		long result = 0L;
		if ((key == null) || (key.length == 0) || (member == null) || (member.length == 0)) {
			return Long.valueOf(result);
		}
		boolean value = specialRedisTemplate.opsForZSet().add(key, member, score);
		if (value) {
			result = 1;
		}
		return Long.valueOf(result);
	}

	public Double zincrby(byte[] key, double score, byte[] member) {
		Double result = null;
		if ((key == null) || (key.length == 0) || (member == null) || (member.length == 0)) {
			return result;
		}
		result = specialRedisTemplate.opsForZSet().incrementScore(key, member, score);
		return result;
	}

	public Double zincrby(String key, double score, String member) {
		Double result = null;
		if ((StringUtils.isEmpty(key)) || (StringUtils.isEmpty(member))) {
			return result;
		}
		result = specialRedisTemplate.opsForZSet().incrementScore(key, member, score);
		return result;
	}

	@SuppressWarnings("unchecked")
	public Set<String> zrevrangeByScore(String key, double max, double min) {
		Set<String> value = null;
		if (StringUtils.isEmpty(key)) {
			return value;
		}
		if (max > min) {
			Set<byte[]> bytesSet = (Set<byte[]>)(Object)specialRedisTemplate.opsForZSet().reverseRangeByScore(key, min, max);
			value = bytesToString(bytesSet);
		}
		return value;
	}

	@SuppressWarnings("unchecked")
	public Set<String> zrevrangeByScore(String key, double max, double min, int offset, int count) {
		Set<String> value = null;
		if (StringUtils.isEmpty(key)) {
			return value;
		}
		if (max > min) {
			Set<byte[]> bytesSet = (Set<byte[]>)(Object)specialRedisTemplate.opsForZSet().reverseRangeByScore(key, min, max, offset, count);
			value = bytesToString(bytesSet);
		}
		return value;
	}

	@SuppressWarnings("unchecked")
	public Set<byte[]> zrevrangeByScore(byte[] key, double max, double min) {
		Set<byte[]> value = null;
		if ((key == null) || (key.length == 0)) {
			return value;
		}
		if (max > min) {
			value = (Set<byte[]>)(Object)specialRedisTemplate.opsForZSet().reverseRangeByScore(key, min, max);
		}
		return value;
	}

	@SuppressWarnings("unchecked")
	public Set<byte[]> zrevrangeByScore(byte[] key, double max, double min, int offset, int count) {
		Set<byte[]> value = null;
		if ((key == null) || (key.length == 0)) {
			return value;
		}
		if (max > min) {
			value = (Set<byte[]>)(Object)specialRedisTemplate.opsForZSet().reverseRangeByScore(key, min, max, offset, count);
		}
		return value;
	}

	@SuppressWarnings("unchecked")
	public Set<Tuple> zrevrangeByScoreWithScores(String key, double max, double min) {
		Set<Tuple> value = null;
		if (StringUtils.isEmpty(key)) {
			return value;
		}
		if (max > min) {
			Set<TypedTuple<byte[]>> tTupleSet = (Set<TypedTuple<byte[]>>)(Object)specialRedisTemplate.opsForZSet().reverseRangeByScoreWithScores(key, min, max);
			value = tupleToTuple(tTupleSet);
		}
		return value;
	}

	@SuppressWarnings("unchecked")
	public Set<Tuple> zrevrangeByScoreWithScores(byte[] key, double max, double min) {
		Set<Tuple> value = null;
		if (StringUtils.isEmpty(key)) {
			return value;
		}
		if (max > min) {
			Set<TypedTuple<byte[]>> tTupleSet = (Set<TypedTuple<byte[]>>)(Object)specialRedisTemplate.opsForZSet().reverseRangeByScoreWithScores(key, min, max);
			value = tupleToTuple(tTupleSet);
		}
		return value;
	}

	@SuppressWarnings("unchecked")
	public Set<Tuple> zrevrangeByScoreWithScores(String key, double max, double min, int offset, int count) {
		Set<Tuple> value = null;
		if (StringUtils.isEmpty(key)) {
			return value;
		}
		if (max > min) {
			Set<TypedTuple<byte[]>> tTupleSet = (Set<TypedTuple<byte[]>>)(Object)specialRedisTemplate.opsForZSet().reverseRangeByScoreWithScores(key, min, max, offset, count);
			value = tupleToTuple(tTupleSet);
		}
		return value;
	}

	@SuppressWarnings("unchecked")
	public Set<Tuple> zrevrangeByScoreWithScores(byte[] key, double max, double min, int offset, int count) {
		Set<Tuple> value = null;
		if ((key == null) || (key.length == 0)) {
			return value;
		}
		if (max > min) {
			Set<TypedTuple<byte[]>> tTupleSet = (Set<TypedTuple<byte[]>>)(Object)specialRedisTemplate.opsForZSet().reverseRangeByScoreWithScores(key, min, max, offset, count);
			value = tupleToTuple(tTupleSet);
		}
		return value;
	}

	public Long zremrangeByScore(String key, double min, double max) {
		long result = 0L;
		if (StringUtils.isEmpty(key)) {
			return Long.valueOf(result);
		}
		result = specialRedisTemplate.opsForZSet().removeRangeByScore(key, min, max);
		return Long.valueOf(result);
	}

	public Long zremrangeByScore(String key, String min, String max) {
		long result = 0L;
		if (StringUtils.isEmpty(key)) {
			return Long.valueOf(result);
		}
		result = specialRedisTemplate.opsForZSet().removeRangeByScore(key, Double.parseDouble(min),
				Double.parseDouble(max));
		return Long.valueOf(result);
	}

	public Long zremrangeByScore(byte[] key, double min, double max) {
		long result = 0L;
		if (StringUtils.isEmpty(key)) {
			return Long.valueOf(result);
		}
		result = specialRedisTemplate.opsForZSet().removeRangeByScore(key, min, max);
		return Long.valueOf(result);
	}

	public Long zremrangeByScore(byte[] key, byte[] min, byte[] max) {
		long result = 0L;
		if (StringUtils.isEmpty(key)) {
			return Long.valueOf(result);
		}
		result = specialRedisTemplate.opsForZSet().removeRangeByScore(key, specialRedisSerializer.ofDouble(min),
				specialRedisSerializer.ofDouble(max));
		return Long.valueOf(result);
	}

	public Long zrem(String key, String... member) {
		long result = 0L;
		if (StringUtils.isEmpty(key)) {
			return Long.valueOf(result);
		}
		result = specialRedisTemplate.opsForZSet().remove(key, member);
		return Long.valueOf(result);
	}

	public Long zrem(byte[] key, byte[]... member) {
		long result = 0L;
		if ((key == null) || (key.length == 0)) {
			return Long.valueOf(result);
		}
		result = specialRedisTemplate.opsForZSet().remove(key, member);
		return Long.valueOf(result);
	}

	@SuppressWarnings("unchecked")
	public Set<String> keys(String pattern) {
		if (StringUtils.isEmpty(pattern)) {
			return Collections.emptySet();
		}
		Set<byte[]> bytesSet = (Set<byte[]>)(Object)specialRedisTemplate.keys(pattern);
		Set<String> keys = bytesToString(bytesSet);
		return keys;
	}

	public String rpop(String key) {
		String value = null;
		if (StringUtils.isEmpty(key)) {
			return value;
		}
		byte[] result = (byte[])specialRedisTemplate.opsForList().rightPop(key);
		if ((result == null) || (result.length == 0)) {
			return value;
		}
		value = bytesToString(result);
		return value;
	}

	public byte[] rpop(byte[] key) {
		byte[] value = null;
		if ((key == null) || (key.length == 0)) {
			return value;
		}
		value = (byte[])specialRedisTemplate.opsForList().rightPop(key);
		return value;
	}

	public boolean ltrim(String key, long start, long end) {
		boolean flag = false;
		if (StringUtils.isEmpty(key)) {
			return flag;
		}
		specialRedisTemplate.opsForList().trim(key, start, end);
		flag = true;
		return flag;
	}

	public boolean ltrim(byte[] key, long start, long end) {
		boolean flag = false;
		if ((key == null) || (key.length == 0)) {
			return flag;
		}
		specialRedisTemplate.opsForList().trim(key, start, end);
		flag = true;
		return flag;
	}

	public Long rpush(byte[] key, byte[]... values) {
		long result = 0L;
		if ((key == null) || (key.length == 0)) {
			return Long.valueOf(result);
		}
		result = specialRedisTemplate.opsForList().rightPushAll(key, values);
		return Long.valueOf(result);
	}
	
	@Override
	public <T> Long rpushObject(String key, T element) {
		long result = 0L;
		if ((key == null) || (key.length() == 0)) {
			return Long.valueOf(result);
		}
		result = specialRedisTemplate.opsForList().rightPushAll(key, element);
		return Long.valueOf(result);
	}

	public String rpoplpush(String srckey, String dstkey) {
		String value = null;
		if ((StringUtils.isEmpty(srckey)) || (StringUtils.isEmpty(dstkey))) {
			return value;
		}
		byte[] result = (byte[])specialRedisTemplate.opsForList().rightPopAndLeftPush(srckey, dstkey);
		value = bytesToString(result);
		return value;
	}

	public byte[] rpoplpush(byte[] srckey, byte[] dstkey) {
		byte[] value = null;
		if ((srckey == null) || (srckey.length == 0) || (dstkey == null) || (dstkey.length == 0)) {
			return value;
		}
		value = (byte[])specialRedisTemplate.opsForList().rightPopAndLeftPush(srckey, dstkey);

		return value;
	}

	public Long linsert(String key, BinaryClient.LIST_POSITION where, String pivot, String value) {
		long result = 0L;
		if (StringUtils.isEmpty(key)) {
			return Long.valueOf(result);
		}
		if (where == BinaryClient.LIST_POSITION.BEFORE) {
			result = specialRedisTemplate.opsForList().leftPush(key, pivot, value);
		} else if (where == BinaryClient.LIST_POSITION.AFTER) {
			result = specialRedisTemplate.opsForList().rightPush(key, pivot, value);
		}
		return Long.valueOf(result);
	}

	public Long linsert(byte[] key, BinaryClient.LIST_POSITION where, byte[] pivot, byte[] value) {
		long result = 0L;
		if ((key == null) || (key.length == 0)) {
			return Long.valueOf(result);
		}
		if (where == BinaryClient.LIST_POSITION.BEFORE) {
			result = specialRedisTemplate.opsForList().leftPush(key, pivot, value);
		} else if (where == BinaryClient.LIST_POSITION.AFTER) {
			result = specialRedisTemplate.opsForList().rightPush(key, pivot, value);
		}
		return Long.valueOf(result);
	}

	public Long lpushx(String key, String... values) {
		long result = 0L;
		if (StringUtils.isEmpty(key)) {
			return Long.valueOf(result);
		}
		for (String value : values) {
			result = specialRedisTemplate.opsForList().leftPushIfPresent(key, value);
		}
		return Long.valueOf(result);
	}

	public Long lpushx(byte[] key, byte[]... values) {
		long result = 0L;
		if ((key == null) || (key.length == 0)) {
			return Long.valueOf(result);
		}
		for (byte[] value : values) {
			result = specialRedisTemplate.opsForList().leftPushIfPresent(key, value);
		}
		return Long.valueOf(result);
	}

	public Long rpushx(String key, String... values) {
		long result = 0L;
		if (StringUtils.isEmpty(key)) {
			return Long.valueOf(result);
		}
		for (String value : values) {
			result = specialRedisTemplate.opsForList().rightPushIfPresent(key, value);
		}
		return Long.valueOf(result);
	}

	public Long rpushx(byte[] key, byte[]... values) {
		long result = 0L;
		if ((key == null) || (key.length == 0)) {
			return Long.valueOf(result);
		}
		for (byte[] value : values) {
			result = specialRedisTemplate.opsForList().rightPushIfPresent(key, value);
		}
		return Long.valueOf(result);
	}

	public Double zscore(String key, String member) {
		Double result = null;
		if (StringUtils.isEmpty(key)) {
			return result;
		}
		result = specialRedisTemplate.opsForZSet().score(key, member);
		return result;
	}

	public Double zscore(byte[] key, byte[] member) {
		Double result = null;
		if ((key == null) || (key.length == 0) || (member == null) || (member.length == 0)) {
			return result;
		}
		result = specialRedisTemplate.opsForZSet().score(key, member);
		return result;
	}

	@SuppressWarnings("unchecked")
	public Set<String> zrange(String key, long start, long end) {
		Set<String> value = null;
		if (StringUtils.isEmpty(key)) {
			return value;
		}
		Set<byte[]> bytesSet = (Set<byte[]>)(Object)specialRedisTemplate.opsForZSet().range(key, start, end);
		value = bytesToString(bytesSet);
		return value;
	}

	@SuppressWarnings("unchecked")
	public Set<byte[]> zrange(byte[] key, long start, long end) {
		Set<byte[]> value = null;
		if ((key == null) || (key.length == 0)) {
			return value;
		}
		value = (Set<byte[]>)(Object)specialRedisTemplate.opsForZSet().range(key, start, end);
		return value;
	}

	@SuppressWarnings("unchecked")
	public Set<Tuple> zrangeWithScores(String key, long start, long end) {
		Set<Tuple> value = null;
		if (StringUtils.isEmpty(key)) {
			return value;
		}
		Set<TypedTuple<byte[]>> tTupleSet = (Set<TypedTuple<byte[]>>)(Object)specialRedisTemplate.opsForZSet().rangeWithScores(key, start, end);
		value = tupleToTuple(tTupleSet);
		return value;
	}

	@SuppressWarnings("unchecked")
	public Set<Tuple> zrangeWithScores(byte[] key, long start, long end) {
		Set<Tuple> value = null;
		if ((key == null) || (key.length == 0)) {
			return value;
		}
		Set<TypedTuple<byte[]>> tTupleSet = (Set<TypedTuple<byte[]>>)(Object)specialRedisTemplate.opsForZSet().rangeWithScores(key, start, end);
		value = tupleToTuple(tTupleSet);
		return value;
	}

	@SuppressWarnings("unchecked")
	public Set<Tuple> zrangeByScoreWithScores(String key, double min, double max) {
		Set<Tuple> value = null;
		if (StringUtils.isEmpty(key)) {
			return value;
		}
		if (max > min) {
			Set<TypedTuple<byte[]>> tTupleSet = (Set<TypedTuple<byte[]>>)(Object)specialRedisTemplate.opsForZSet().rangeByScoreWithScores(key, min, max);
			value = tupleToTuple(tTupleSet);
		}
		return value;
	}

	@SuppressWarnings("unchecked")
	public Set<Tuple> zrangeByScoreWithScores(String key, double min, double max, int offset, int count) {
		Set<Tuple> value = null;
		if (StringUtils.isEmpty(key)) {
			return value;
		}
		if (max > min) {
			Set<TypedTuple<byte[]>> tTupleSet = (Set<TypedTuple<byte[]>>)(Object)specialRedisTemplate.opsForZSet().rangeByScoreWithScores(key, min, max, offset, count);
			value = tupleToTuple(tTupleSet);
		}
		return value;
	}

	@SuppressWarnings("unchecked")
	public Set<Tuple> zrangeByScoreWithScores(byte[] key, double min, double max) {
		Set<Tuple> value = null;
		if ((key == null) || (key.length == 0)) {
			return value;
		}
		if (max > min) {
			Set<TypedTuple<byte[]>> tTupleSet = (Set<TypedTuple<byte[]>>)(Object)specialRedisTemplate.opsForZSet().rangeByScoreWithScores(key, min, max);
			value = tupleToTuple(tTupleSet);
		}
		return value;
	}

	@SuppressWarnings("unchecked")
	public Set<Tuple> zrangeByScoreWithScores(byte[] key, double min, double max, int offset, int count) {
		Set<Tuple> value = null;
		if ((key == null) || (key.length == 0)) {
			return value;
		}
		if (max > min) {
			Set<TypedTuple<byte[]>> tTupleSet = (Set<TypedTuple<byte[]>>)(Object)specialRedisTemplate.opsForZSet().rangeByScoreWithScores(key, min, max, offset, count);
			value = tupleToTuple(tTupleSet);
		}
		return value;
	}

	@SuppressWarnings("unchecked")
	public Set<String> zrevrange(String key, long start, long end) {
		Set<String> value = null;
		if (StringUtils.isEmpty(key)) {
			return value;
		}
		Set<byte[]> bytesSet = (Set<byte[]>)(Object)specialRedisTemplate.opsForZSet().reverseRange(key, start, end);
		value = bytesToString(bytesSet);
		return value;
	}

	@SuppressWarnings("unchecked")
	public Set<byte[]> zrevrange(byte[] key, long start, long end) {
		Set<byte[]> value = null;
		if ((key == null) || (key.length == 0)) {
			return value;
		}
		value = (Set<byte[]>)(Object)specialRedisTemplate.opsForZSet().reverseRange(key, start, end);
		return value;
	}

	public Long zrank(String key, String member) {
		Long result = null;
		if (StringUtils.isEmpty(key)) {
			return result;
		}
		result = specialRedisTemplate.opsForZSet().rank(key, member);
		return result;
	}

	public Long zrank(byte[] key, byte[] member) {
		Long result = null;
		if ((key == null) || (key.length == 0)) {
			return result;
		}
		result = specialRedisTemplate.opsForZSet().rank(key, member);
		return result;
	}

	public Long zrevrank(String key, String member) {
		Long result = null;
		if (StringUtils.isEmpty(key)) {
			return result;
		}
		result = specialRedisTemplate.opsForZSet().reverseRank(key, member);
		return result;
	}

	public Long zrevrank(byte[] key, byte[] member) {
		Long result = null;
		if ((key == null) || (key.length == 0)) {
			return result;
		}
		result = specialRedisTemplate.opsForZSet().reverseRank(key, member);
		return result;
	}

	@SuppressWarnings("unchecked")
	public List<String> mget(String... keys) {
		List<String> result = null;
		if ((keys == null) || (keys.length == 0)) {
			return result;
		}
		List<byte[]> bytesList = (List<byte[]>)(Object)specialRedisTemplate.opsForValue().multiGet((Collection<Object>)(Object)Arrays.asList(keys));
		result = bytesToString(bytesList);
		return result;
	}

	@SuppressWarnings("unchecked")
	public List<byte[]> mget(byte[]... keys) {
		List<byte[]> result = null;
		if ((keys == null) || (keys.length == 0)) {
			return result;
		}
		result = (List<byte[]>)(Object)specialRedisTemplate.opsForValue().multiGet((Collection<Object>)(Object)Arrays.asList(keys));
		return result;
	}

	public String spop(String key) {
		String result = null;
		if (StringUtils.isEmpty(key)) {
			return result;
		}
		byte[] bytes = (byte[])specialRedisTemplate.opsForSet().pop(key);
		result = bytesToString(bytes);
		return result;
	}

	public byte[] spop(byte[] key) {
		byte[] result = null;
		if ((key == null) || (key.length == 0)) {
			return result;
		}
		result = (byte[])specialRedisTemplate.opsForSet().pop(key);
		return result;
	}

	public Long srem(String key, String... member) {
		long result = 0L;
		if (StringUtils.isEmpty(key)) {
			return Long.valueOf(result);
		}
		result = specialRedisTemplate.opsForSet().remove(key, member);
		return Long.valueOf(result);
	}

	public Long srem(byte[] key, byte[]... member) {
		long result = 0L;
		if ((key == null) || (key.length == 0)) {
			return Long.valueOf(result);
		}
		result = specialRedisTemplate.opsForSet().remove(key, member);
		return Long.valueOf(result);
	}

	public Long setnx(String key, String value) {
		long result = 0L;
		if ((StringUtils.isEmpty(key)) || (StringUtils.isEmpty(value))) {
			return Long.valueOf(result);
		}
		boolean flag = specialRedisTemplate.opsForValue().setIfAbsent(key, value);
		if (flag) {
			result = 1;
		}
		return Long.valueOf(result);
	}

	public Long setnx(byte[] key, byte[] value) {
		long result = 0L;
		if ((key == null) || (key.length == 0) || (null == value) || (value.length == 0)) {
			return Long.valueOf(result);
		}
		boolean flag = specialRedisTemplate.opsForValue().setIfAbsent(key, value);
		if (flag) {
			result = 1;
		}
		return Long.valueOf(result);
	}

	/**
	 * 从缓存中获取信息
	 * 
	 * @param cacheKey
	 *            缓存的key值
	 * @return 根据cacheKey取到的值
	 * @throws Exception
	 * @ReturnType String
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Map<String, String> hgetFromCache(String cacheKey) {
		Map<byte[], byte[]> bytesMap = (Map<byte[], byte[]>)(Object)specialRedisTemplate.opsForHash().entries(cacheKey);
		return bytesToString(bytesMap);
	}
	
	@SuppressWarnings({ "unchecked" })
	private <T> List<T> bytesToObject(List<byte[]> bytesList) {
		List<T> objectList = new ArrayList<>(bytesList.size());
		for (byte[] bytes : bytesList) {
			objectList.add((T)bytesToObject(bytes));
		}
		return objectList;
	}
	
	private List<String> bytesToString(List<byte[]> bytesList) {
		List<String> stringList = new ArrayList<>(bytesList.size());
		for (byte[] bytes : bytesList) {
			stringList.add(bytesToString(bytes));
		}
		return stringList;
	}

	private Set<String> bytesToString(Set<byte[]> bytesSet) {
		Set<String> stringSet = new HashSet<>(bytesSet.size());
		for (byte[] bytes : bytesSet) {
			stringSet.add(bytesToString(bytes));
		}
		return stringSet;
	}
	
	private Map<String, String> bytesToString(Map<byte[], byte[]> bytesMap) {
		Map<String, String> stringMap = new HashMap<>(bytesMap.size());
		for (Map.Entry<byte[], byte[]> entry : bytesMap.entrySet()) {
			stringMap.put(bytesToString(entry.getKey()),
					bytesToString(entry.getValue()));
		}
		return stringMap;
	}
	
	private Set<Tuple> tupleToTuple(Set<TypedTuple<byte[]>> tTupleSet) {
		Set<Tuple> tupleSet = new HashSet<>(tTupleSet.size());
		for (TypedTuple<byte[]> tTuple : tTupleSet) {
			tupleSet.add(new Tuple(tTuple.getValue(), tTuple.getScore()));
		}
		return tupleSet;
	}
	
	private String bytesToString(byte[] bytes) {
		return specialRedisSerializer.ofString(bytes);
	}
	
	@SuppressWarnings({ "unchecked" })
	private <T> T bytesToObject(byte[] bytes) {
		return (T)specialRedisSerializer.ofObject(bytes);
	}
}
