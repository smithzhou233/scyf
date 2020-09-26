package com.hngf.entity.risk;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 检查记录表
 * 
 * @author hngf
 * @email 
 * @date 2020-05-20 17:36:23
 */
@Data
@ApiModel("检查记录表model")
public class RiskInspectRecord implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 检查记录Id
	 */
	@ApiModelProperty("检查记录Id")
	private Long inspectRecordId;
	/**
	 * 检查记录编号
	 */
	@ApiModelProperty("检查记录编号")
	private Long inspectRecordNo;
	/**
	 * 所属企业
	 */
	@ApiModelProperty("所属企业")
	private Long companyId;
	/**
	 * 所属部门
	 */
	@ApiModelProperty("所属部门")
	private Long groupId;
	/**
	 * 风险点ID
	 */
	@ApiModelProperty("风险点ID")
	private Long riskPointId;
	/**
	 * 任务ID
	 */
	@ApiModelProperty("任务ID")
	private Long inspectScheduleId;
	/**
	 * 检查计划项Id
	 */
	@ApiModelProperty("检查计划项Id")
	private Long inspectItemDefId;
	/**
	 * 任务检查定义ID/检查表ID
	 */
	@ApiModelProperty("务检查定义ID/检查表ID")
	private Long schduleDefId;
	/**
	 * 检查项明细ID/(风险明细ID/危险源明细ID)
	 */
	@ApiModelProperty("检查项明细ID/(风险明细ID/危险源明细ID)")
	private Long itemDetailId;
	/**
	 * 检查项内容明细ID/风险管控措施明细ID
	 */
	@ApiModelProperty("检查项内容明细ID/风险管控措施明细ID")
	private Long riskMeasureId;
	/**
	 * 检查项内容 / 管控措施内容
	 */
	@ApiModelProperty("检查项内容 / 管控措施内容")
	private String riskMeasureContent;
	/**
	 * 检查结果 1通过；2不通过；3发现隐患；4未涉及；
	 */
	@ApiModelProperty("检查结果 1通过；2不通过；3发现隐患；4未涉及；")
	private Integer inspectResult;
	/**
	 * 当前是第几次检查
	 */
	@ApiModelProperty("当前是第几次检查")
	private Integer inspectNumber;
	/**
	 * 现场数据采集
	 */
	@ApiModelProperty("现场数据采集")
	private String spotData;
	/**
	 * 检查记录备注
	 */
	@ApiModelProperty("检查记录备注")
	private String remark;
	/**
	 * 排序
	 */
	@ApiModelProperty("排序")
	private Integer checkSort;
	/**
	 * 更新时间
	 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@ApiModelProperty("更新时间")
	private Date updatedTime;
	/**
	 * 更新者Id
	 */
	private Long updatedBy;
	/**
	 * 创建时间
	 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@ApiModelProperty("创建时间")
	private Date createdTime;
	/**
	 * 创建人
	 */
	private Long createdBy;
	/**
	 * 删除标记 0正常 1删除
	 */
	private Integer delFlag;

	//以下是扩展字段，查询显示使用

	/**
	 * 所属部门名称
	 */
	@ApiModelProperty("所属部门名称")
	private String inspectGroupName;

	//隐患相关字段
	@ApiModelProperty("隐患ID")
	private Long hiddenrId;
	@ApiModelProperty("隐患等级")
	private Integer hiddenLevel;//风险等级
	@ApiModelProperty("隐患名称")
	private String hiddenTitle;//名称
	@ApiModelProperty("隐患状态")
	private Integer status;//状态

	//风险点相关字段
	@ApiModelProperty("风险点名称")
	private String riskPointName;//风险点名称
	@ApiModelProperty("风险点类型")
	private Integer riskPointType;//风险点类型
	/**
	 * 检查项名称
	 */
	@ApiModelProperty("检查项名称")
	private String inspectDefName;
	/**
	 * 任务风险
	 */
	@ApiModelProperty("任务风险")
	private String riskDangerName;
	/**
	 * 检查人
	 */
	@ApiModelProperty("检查人")
	private String inspectUserName;

	//附件相关
	@ApiModelProperty("附件ID")
	private Long attachmentId;//附件ID
	@ApiModelProperty("附件类型")
	private String attachmentType;//附件类型
	@ApiModelProperty("附件路径")
	private String attachmentPath;//附件路径

}
