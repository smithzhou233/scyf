package com.hngf.entity.danger;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.Date;

/**
 * 检查定义表
 * 
 * @author hngf
 * @email 
 * @date 2020-05-20 17:36:24
 */
@JsonIgnoreProperties(value = {"delFlag"})
@Data
@ApiModel("检查定义表model")
public class InspectDef implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 检查表ID
	 */
	@ApiModelProperty("检查表ID")
	private Long inspectDefId;
	/**
	 * 所属公司ID
	 */
	@ApiModelProperty("所属公司ID")
	private Long companyId;
	/**
	 * 检查项名称
	 */
	@NotBlank(message = "检查项名称不能为空")
	@ApiModelProperty("检查项名称")
	private String inspectDefName;
	/**
	 * 检查表分类ID
	 */
	@ApiModelProperty("检查表分类ID")
	private Long inspectDefTypeId;
	/**
	 * 检查方法
	 */
	@ApiModelProperty("检查方法")
	private String inspectDefMethod;
	/**
	 * 检查依据
	 */
	@ApiModelProperty("检查依据")
	private String inspectDefRule;
	/**
	 * 处罚依据
	 */
	@ApiModelProperty("处罚依据")
	private String penalizeDefRule;
	/**
	 * 检查描述
	 */
	@ApiModelProperty("检查描述")
	private String inspectDefDesc;
	/**
	 * 创建人
	 */
	private Long createdBy;
	/**
	 * 创建时间
	 */
	private Date createdTime;
	/**
	 * 修改人
	 */
	private Long updatedBy;
	/**
	 * 更新时间
	 */
	private Date updatedTime;
	/**
	 * 删除标记
	 */
	private Integer delFlag;


	//扩展字段

	/**
	 * 检查表分类名称
	 */
	@ApiModelProperty("检查表分类名称")
	private String classifyName;
	/**
	 * 配置值：\r\n【检查表类型：0分级管控；1专业检查表；】\r\n（可以按需配置，已备注的已经启用）
	 */
	@ApiModelProperty("检查表类型：0分级管控；1专业检查表")
	private String classifyValue;

}
