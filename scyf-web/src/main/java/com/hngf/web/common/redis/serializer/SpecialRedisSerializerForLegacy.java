package com.hngf.web.common.redis.serializer;

import java.math.BigDecimal;
import java.math.BigInteger;

import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Service;

/**
 * <p>注意，反序列不做处理，原样返回byte[]
 * @author lixinjie
 * @since 2018-11-28
 */
@Service
public class SpecialRedisSerializerForLegacy implements SpecialRedisSerializer {

	private static final byte[] EMPTY_ARRAY = new byte[0];
	
	private StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
	private JdkSerializationRedisSerializer jdkSerializationRedisSerializer = new JdkSerializationRedisSerializer();
	
	@Override
	public byte[] serialize(Object object) throws SerializationException {
		if (object == null) {
			return EMPTY_ARRAY;
		}
		if (object instanceof byte[]) {
			return (byte[])object;
		}
		if (object instanceof String) {
			return stringRedisSerializer.serialize((String)object);
		}
		if (object instanceof BigDecimal) {
			return stringRedisSerializer.serialize(((BigDecimal)object).toPlainString());
		}
		if (object instanceof Number) {
			return stringRedisSerializer.serialize(object.toString());
		}
		return jdkSerializationRedisSerializer.serialize(object);
	}

	@Override
	public Object deserialize(byte[] bytes) throws SerializationException {
		return bytes;
	}

	@Override
	public String ofString(byte[] bytes) {
		return stringRedisSerializer.deserialize(bytes);
	}

	@Override
	public Integer ofInteger(byte[] bytes) {
		return Integer.valueOf(ofString(bytes));
	}

	@Override
	public Long ofLong(byte[] bytes) {
		return Long.valueOf(ofString(bytes));
	}

	@Override
	public Double ofDouble(byte[] bytes) {
		return Double.valueOf(ofString(bytes));
	}
	
	@Override
	public BigInteger ofBigInteger(byte[] bytes) {
		return new BigInteger(ofString(bytes));
	}
	
	@Override
	public BigDecimal ofBigDecimal(byte[] bytes) {
		return new BigDecimal(ofString(bytes));
	}

	@Override
	public Object ofObject(byte[] bytes) {
		return jdkSerializationRedisSerializer.deserialize(bytes);
	}

}
