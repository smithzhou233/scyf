package com.hngf.entity.risk;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 风险点责任人表
 * 
 * @author hngf
 * @email 
 * @date 2020-06-12 09:20:57
 */
@Data
public class RiskPointPersonLiable implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 风险点
	 */
	
	private Long riskPointId;
	/**
	 * 公司ID
	 */
	private Long companyId;
	/**
	 * 账号ID
	 */
	private Long userId;

}
