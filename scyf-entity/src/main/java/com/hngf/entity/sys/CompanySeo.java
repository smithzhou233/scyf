package com.hngf.entity.sys;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 企业 oem 表 
 * 
 * @author hngf
 * @email 
 * @date 2020-07-07 17:02:00
 */
@Data
public class CompanySeo implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	
	private Long companyId;
	/**
	 * logo
	 */
	private String seoLogo;
	/**
	 * 标题
	 */
	private String seoTitle;
	/**
	 * ws 推送地址
	 */
	private String seoWs;
	/**
	 * 1新0 旧
	 */
	private Integer haveNewHomePage;
	/**
	 * 0 不显示 1 显示
	 */
	private Integer appHideShow;
	/**
	 * 融云key
	 */
	private String rongAppKey;
	/**
	 * 融云secret
	 */
	private String rongAppSecret;
	/**
	 * 
	 */
	private String bigScreenTitle;
	/**
	 * 1显示 0 不显示
	 */
	private Integer bigScreenScore;

}
