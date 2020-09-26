package com.hngf.entity.risk;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 风险点关联风险表
 * 
 * @author hngf
 * @email 
 * @date 2020-05-20 17:36:24
 */
@Data
public class RiskRecord implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 风险ID
	 */
	
	private Long riskId;
	/**
	 * 风险点ID
	 */
	private Long riskPointId;
	/**
	 * 危险源ID
	 */
	private Long riskDangerId;
	/**
	 * 风险名称
	 */
	private String riskName;
	/**
	 * 公司ID
	 */
	private Long companyId;
	/**
	 * 删除标记
	 */
	private Integer delFlag;

}
