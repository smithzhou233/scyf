package com.hngf.entity.danger;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.Date;

/**
 * 检查项
 * 
 * @author hngf
 * @email 
 * @date 2020-05-20 17:36:24
 */
@Data
@ApiModel("检查项model")
public class InspectItemDef implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 检查项ID
	 */
	@ApiModelProperty("检查项ID")
	private Long inspectItemDefId;
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
	private String inspectItemDefName;
	/**
	 * 具体内容描述
	 */
	@NotBlank(message = "具体内容描述不能为空")
	@ApiModelProperty("具体内容描述")
	private String inspectItemDefDesc;
	/**
	 * 检查方法
	 */
	@ApiModelProperty("检查方法")
	private String inspectItemDefMethod;
	/**
	 * 检查依据
	 */
	@ApiModelProperty("检查依据")
	private String inspectItemDefRule;
	/**
	 * 处罚依据
	 */
	@ApiModelProperty("处罚依据")
	private String penalizeItemDefRule;
	/**
	 * 父ID
	 */
	@ApiModelProperty("父ID")
	private Long parentId;
	/**
	 * 是否根节点
	 */
	@ApiModelProperty("是否根节点")
	private Integer isRoot;
	/**
	 * 创建人
	 */
	private Long createdBy;
	/**
	 * 新增时间
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
	 * 检查结果 1通过；2不通过；3发现隐患；4未涉及；
	 */
	@ApiModelProperty("检查结果 1通过；2不通过；3发现隐患；4未涉及；")
	private Integer inspectResult;
	/**
	 * 检查记录编号
	 */
	@ApiModelProperty("检查记录编号")
	private Long inspectRecordNo;
	/**
	 * 检查记录日志ID
	 */
	@ApiModelProperty("检查记录日志ID")
	private Long inspectRecordLogId;
}
