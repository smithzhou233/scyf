package com.hngf.entity.danger;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * 
 * @author hngf
 * @email 
 * @date 2020-05-20 17:36:24
 */
@Data
public class InspectPointRel implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 关联ID
	 */
	
	private Long inspectPointRelId;
	/**
	 * 分类ID
	 */
	private Long inspectTypeId;
	/**
	 * 公司ID
	 */
	private Long companyId;
	/**
	 * 风险点ID
	 */
	private Long riskPointId;
	/**
	 * 创建人
	 */
	private Long createdBy;
	/**
	 * 创建时间
	 */
	private Date createdTime;
	/**
	 * 更新者Id
	 */
	private Long updatedBy;
	/**
	 * 更新时间
	 */
	private Date updatedTime;
	/**
	 * 删除：0未删除；1已删除；
	 */
	private Integer delFlag;

}
