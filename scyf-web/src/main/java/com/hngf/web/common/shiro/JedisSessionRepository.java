package com.hngf.web.common.shiro;

import java.io.Serializable;
import java.util.Collection;
import java.util.concurrent.TimeUnit;

import com.hngf.web.common.config.scyfManageProperties;
import com.hngf.web.common.redis.serializer.SpecialRedisSerializer;
import org.apache.shiro.session.Session;
import org.crazycake.shiro.SerializeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.Resource;


public class JedisSessionRepository {

	private static final Logger logger = LoggerFactory.getLogger(JedisSessionRepository.class);
	
	private static final String REDIS_SHIRO_SESSION = "SHIRO-ADMIN-SESSION:";
	
	@Autowired
	private scyfManageProperties scyfManageProperties;

	@Resource
	private SpecialRedisSerializer specialRedisSerializer;

	@Resource
	RedisTemplate<String, Object> specialRedisTemplate ;

	public void saveSession(Session session) {
		if (session == null || session.getId() == null) {
			throw new IllegalArgumentException("session is null");
		}
		try {
			String sessionKey = buildRedisSessionKey(session.getId());
			int timeout = scyfManageProperties.getSessionTimeout();
			byte[] value = SerializeUtils.serialize(session);
			specialRedisTemplate.opsForValue().set(sessionKey, value, timeout, TimeUnit.SECONDS);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("save session error");
		}
	}

	public void deleteSession(Serializable id) {
		if (id == null) {
			throw new IllegalArgumentException("session id is null");
		}
		try {
			String sessionKey = buildRedisSessionKey(id);
			specialRedisTemplate.delete(sessionKey);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("delete session error");
		}
	}

	public Session getSession(Serializable id) {
		if (id == null)
			throw new IllegalArgumentException("session id is null");
		Session session = null;
		try {
			String sessionKey = buildRedisSessionKey(id);
			byte[] bytes = (byte[])specialRedisTemplate.opsForValue().get(sessionKey);
			if (bytes != null && bytes.length > 0) {
				session = (Session)specialRedisSerializer.ofObject(bytes);
				int timeout = scyfManageProperties.getSessionTimeout();
				specialRedisTemplate.expire(sessionKey, timeout, TimeUnit.SECONDS);
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("get session error");
		}
		return session;
	}

	public Collection<Session> getAllSessions() {
		return null;
	}
	
	private String buildRedisSessionKey(Serializable sessionId) {
		return REDIS_SHIRO_SESSION + sessionId;
	}
}