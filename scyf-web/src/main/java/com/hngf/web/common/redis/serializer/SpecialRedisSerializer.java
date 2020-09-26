package com.hngf.web.common.redis.serializer;

import java.math.BigDecimal;
import java.math.BigInteger;

import org.springframework.data.redis.serializer.RedisSerializer;

/**
 * <p>继承redis序列化器
 * <p>并添加一些方法
 * @author lixinjie
 * @since 2018-11-28
 */

public interface SpecialRedisSerializer extends RedisSerializer<Object> {
	
	String ofString(byte[] bytes);
	
	Integer ofInteger(byte[] bytes);
	
	Long ofLong(byte[] bytes);
	
	Double ofDouble(byte[] bytes);
	
	BigInteger ofBigInteger(byte[] bytes);
	
	BigDecimal ofBigDecimal(byte[] bytes);
	
	Object ofObject(byte[] bytes);
}
