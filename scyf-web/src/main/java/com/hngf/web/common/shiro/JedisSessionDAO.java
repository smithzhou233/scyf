package com.hngf.web.common.shiro;

import java.io.Serializable;
import java.util.Collection;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.eis.AbstractSessionDAO;


public class JedisSessionDAO extends AbstractSessionDAO {

	private JedisSessionRepository shiroSessionRepository;
	
	public JedisSessionDAO(JedisSessionRepository sessionRepository) {
		super();
		this.shiroSessionRepository = sessionRepository;
	}
	
	@Override
	protected Serializable doCreate(Session session) {
		Serializable sessionId = this.generateSessionId(session);
		this.assignSessionId(session, sessionId);
		getShiroSessionRepository().saveSession(session);
		return sessionId;
	}

	@Override
	public void update(Session session)  {
		getShiroSessionRepository().saveSession(session);
	}
	
	@Override
	protected Session doReadSession(Serializable sessionId) {
		return getShiroSessionRepository().getSession(sessionId);
	}

	@Override
	public void delete(Session session) {
		if (session == null || session.getId() == null) {
			return;
		}
		getShiroSessionRepository().deleteSession(session.getId());
	}

	@Override
	public Collection<Session> getActiveSessions() {
		return getShiroSessionRepository().getAllSessions();
	}

	public JedisSessionRepository getShiroSessionRepository() {
		return shiroSessionRepository;
	}
	
}
