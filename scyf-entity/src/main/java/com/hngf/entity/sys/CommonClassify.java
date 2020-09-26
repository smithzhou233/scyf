package com.hngf.entity.sys;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * 公司通用分类表
 * 
 * @author hngf
 * @email 
 * @date 2020-05-20 17:31:55
 */
@Data
public class CommonClassify implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 分类ID
	 */
	
	private Long classifyId;
	/**
	 * 类型：1隐患类型；2检查表类型；4任务检查类型；（可拓展使用，）
	 */
	@NotNull(message = "类型不能为空")
	private Integer classifyType;
	/**
	 * 公司ID
	 */
	private Long companyId;
	/**
	 * 分类名称
	 */
	private String classifyName;
	/**
	 * 配置值：
【检查表类型：0分级管控；1专业检查表；】
（可以按需配置，已备注的已经启用）
	 */
	private String classifyValue;
	/**
	 * 描述
	 */
	private String classifyDesc;
	/**
	 * 排序
	 */
	private Integer sortNo;
	/**
	 * 创建时间
	 */
	private Date createdTime;
	/**
	 * 更新时间
	 */
	private Date updatedTime;
	/**
	 * 创建人
	 */
	private Long createdBy;
	/**
	 * 更新人
	 */
	private Long updatedBy;
	/**
	 * 删除标记：0正常；1已删除
	 */
	private Integer delFlag;

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public Long getClassifyId() {
		return classifyId;
	}

	public void setClassifyId(Long classifyId) {
		this.classifyId = classifyId;
	}

	public Integer getClassifyType() {
		return classifyType;
	}

	public void setClassifyType(Integer classifyType) {
		this.classifyType = classifyType;
	}

	public Long getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Long companyId) {
		this.companyId = companyId;
	}

	public String getClassifyName() {
		return classifyName;
	}

	public void setClassifyName(String classifyName) {
		this.classifyName = classifyName;
	}

	public String getClassifyValue() {
		return classifyValue;
	}

	public void setClassifyValue(String classifyValue) {
		this.classifyValue = classifyValue;
	}

	public String getClassifyDesc() {
		return classifyDesc;
	}

	public void setClassifyDesc(String classifyDesc) {
		this.classifyDesc = classifyDesc;
	}

	public Integer getSortNo() {
		return sortNo;
	}

	public void setSortNo(Integer sortNo) {
		this.sortNo = sortNo;
	}

	public Date getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}

	public Date getUpdatedTime() {
		return updatedTime;
	}

	public void setUpdatedTime(Date updatedTime) {
		this.updatedTime = updatedTime;
	}

	public Long getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Long createdBy) {
		this.createdBy = createdBy;
	}

	public Long getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(Long updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Integer getDelFlag() {
		return delFlag;
	}

	public void setDelFlag(Integer delFlag) {
		this.delFlag = delFlag;
	}

	public CommonClassify(){
		super();
	}
	public CommonClassify(Long companyId, Long createdBy, Integer classifyType){
		this.setCreatedBy(createdBy);
		this.setCreatedTime(new Date());
		this.setUpdatedBy(createdBy);
		this.setUpdatedTime(new Date());
		this.setCompanyId(companyId);
		this.setClassifyType(classifyType);
		this.setDelFlag(0);
	}
	@Override
	public String toString() {
		return "CommonClassify{" +
				"classifyId=" + classifyId +
				", classifyType=" + classifyType +
				", companyId=" + companyId +
				", classifyName='" + classifyName + '\'' +
				", classifyValue='" + classifyValue + '\'' +
				", classifyDesc='" + classifyDesc + '\'' +
				", sortNo=" + sortNo +
				", createdTime=" + createdTime +
				", updatedTime=" + updatedTime +
				", createdBy=" + createdBy +
				", updatedBy=" + updatedBy +
				", delFlag=" + delFlag +
				'}';
	}
}
