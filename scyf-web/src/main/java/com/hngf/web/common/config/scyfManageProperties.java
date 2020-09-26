package com.hngf.web.common.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author lixinjie
 * @since 2018-11-29
 */
@Configuration
@ConfigurationProperties(prefix = "scyfmanage")
public class scyfManageProperties {

	private Integer sessionTimeout;
	private String loginNeedMsg;
	private Integer smsExpireSeconds;
	
	public Integer getSessionTimeout() {
		return sessionTimeout;
	}

	public void setSessionTimeout(Integer sessionTimeout) {
		this.sessionTimeout = sessionTimeout;
	}

	public String getLoginNeedMsg() {
		return loginNeedMsg;
	}

	public void setLoginNeedMsg(String loginNeedMsg) {
		this.loginNeedMsg = loginNeedMsg;
	}

	public Integer getSmsExpireSeconds() {
		return smsExpireSeconds;
	}

	public void setSmsExpireSeconds(Integer smsExpireSeconds) {
		this.smsExpireSeconds = smsExpireSeconds;
	}
	
}
