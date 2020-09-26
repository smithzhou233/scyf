package com.hngf.entity.danger;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 1专业性检查；2日常检查；3节假日前后；4事故类比检查；5季节性检查；6综合性检查
 * 
 * @author hngf
 * @email 
 * @date 2020-05-28 16:40:19
 */
@Data
public class InspectType implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 分类ID
	 */
	
	private Long inspectTypeId;
	/**
	 * 公司ID
	 */
	private Long companyId;
	/**
	 * 类型：1隐患类型；2检查表类型；4任务检查类型；（可拓展使用，）
	 */
	private Integer inspectType;
	/**
	 * 分类名称
	 */
	private String inspectTypeName;
	/**
	 * 配置值：
【检查表类型：0分级管控；1专业检查表；】
（可以按需配置，已备注的已经启用）
	 */
	private String inspectTypeValue;
	/**
	 * 描述
	 */
	private String inspectTypeDesc;
	/**
	 * 排序
	 */
	private Integer sortNo;
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
	 * 删除标记：0正常；1已删除
	 */
	private Integer delFlag;

}
