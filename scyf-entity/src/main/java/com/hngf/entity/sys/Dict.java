package com.hngf.entity.sys;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 字典表
 * 
 * @author hngf
 * @email 
 * @date 2020-05-20 17:31:55
 */
@Data
public class Dict implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * ID
	 */
	
	private String dictId;
	/**
	 * 字典名称
	 */
	private String dictName;
	/**
	 * 字典描述
	 */
	private String dictDesc;
	/**
	 * 字典编码
	 */
	private String dictCode;
	/**
	 * 字典类型
	 */
	private String dictType;
	/**
	 * 类型0系统1用户自定义
	 */
	private Integer typeId;
	/**
	 * 自定义字典所属企业
	 */
	private String ownerId;
	/**
	 * 所属行业0通用
	 */
	private String industryCode;
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
	 * 删除标记
	 */
	private Integer delFlag;

	@Override
	public String toString() {
		return "Dict{" +
				"dictId='" + dictId + '\'' +
				", dictName='" + dictName + '\'' +
				", dictDesc='" + dictDesc + '\'' +
				", dictCode='" + dictCode + '\'' +
				", dictType='" + dictType + '\'' +
				", typeId=" + typeId +
				", ownerId='" + ownerId + '\'' +
				", industryCode='" + industryCode + '\'' +
				", createdTime=" + createdTime +
				", updatedTime=" + updatedTime +
				", createdBy=" + createdBy +
				", updatedBy=" + updatedBy +
				", delFlag=" + delFlag +
				'}';
	}
}
