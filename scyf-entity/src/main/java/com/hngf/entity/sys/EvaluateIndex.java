package com.hngf.entity.sys;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 评价指标：
评价方式两种：ls、lec；
 * 
 * @author hngf
 * @email 
 * @date 2020-05-27 17:51:02
 */
@Data
public class EvaluateIndex implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 编号ID
	 */
	
	private Long evaluateIndexId;
	/**
	 * 企业id
	 */
	private Long companyId;
	/**
	 * 评价方式：LS、LEC
	 */
	private String evaluateIndexModel;
	/**
	 * 评价类型：L、S、L、E、C
	 */
	private String evaluateIndexType;
	/**
	 * 评价内容
	 */
	private String evaluateIndexContent;
	/**
	 * 评价分值
	 */
	private Float evaluateIndexScore;
	/**
	 * 备注
	 */
	private String evaluateIndexRemark;
	/**
	 * 创建时间
	 */
	private Date createdTime;
	/**
	 * 创建人
	 */
	private Long createdBy;
	/**
	 * 更新时间
	 */
	private Date updatedTime;
	/**
	 * 更新人
	 */
	private Long updatedBy;
	/**
	 * 删除标记
	 */
	private Integer delFlag;

	public void insertPrefixInit(Long createdBy, Long companyId){
		this.setCompanyId(companyId);
		this.setDelFlag(0);
		this.setCreatedBy(createdBy);
		this.setUpdatedBy(createdBy);
		this.setCreatedTime(new Date());
		this.setUpdatedTime(new Date());
	}

}
