package com.hngf.entity.risk;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 风险点作业指导书关系表
 * 
 * @author hngf
 * @email 
 * @date 2020-05-20 17:36:24
 */
@Data
public class RiskPointGuideRel implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 风险点ID
	 */
	
	private Long riskPointId;
	/**
	 * 文章ID
	 */
	private Long entPostId;
	/**
	 * 公司ID
	 */
	private Long companyId;
	/**
	 * 删除标记
	 */
	private Integer delFlag;

}
