package com.hngf.web.common.redis.serializer;

import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * <p>主要用于RedisTemplate里的Value和HashValue的
 * <p>序列化器
 * <p>序列化时：
 * <p>字符串、数字按字符串类型序列化
 * <p>其他按Jdk标准序列化
 * <p>反序列化时：
 * <p>先按Jdk标准反序列化
 * <p>出现失败时，再按字符串类型反序列化
 * <p>注：存入是数字时，取出时是字符串，需自己转换成数字
 * @author lixinjie
 * @since 2018-04-23
 */
public class JdkAndStringValueRedisSerializer implements RedisSerializer<Object> {

	private StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
	private JdkSerializationRedisSerializer jdkSerializationRedisSerializer = new JdkSerializationRedisSerializer();
	
	@Override
	public byte[] serialize(Object t) {
		if (t == null) {
			return jdkSerializationRedisSerializer.serialize(t);
		}
		if (t instanceof String) {
			return stringRedisSerializer.serialize((String)t);
		}
		if (t instanceof Number) {
			return stringRedisSerializer.serialize(t.toString());
		}
		return jdkSerializationRedisSerializer.serialize(t);
	}

	@Override
	public Object deserialize(byte[] bytes) {
		try {
			return jdkSerializationRedisSerializer.deserialize(bytes);
		} catch (Exception ex) {
			//ignore
		}
		return stringRedisSerializer.deserialize(bytes);
	}

}
