package com.hngf.entity.risk;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * 风险点检查记录
 * @author hngf
 * @email 
 * @date 2020-06-03 17:15:30
 */
@Data
@ApiModel("风险点检查记录model")
public class RiskPointCheckRecord implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 记录ID
	 */
	@ApiModelProperty("记录ID")
	private Long recordId;
	/**
	 * 检查记录编号
	 */
	@ApiModelProperty("检查记录编号")
	private Long checkRecordNo;
	/**
	 * 检查表ID
	 */
	@ApiModelProperty("检查表ID")
	private Long inspectDefId;
	/**
	 * 任务ID
	 */
	@ApiModelProperty("任务ID")
	private Long inspectScheduleId;
	/**
	 * 公司ID
	 */
	@ApiModelProperty("公司ID")
	private Long companyId;
	/**
	 * 风险点ID
	 */
	@ApiModelProperty("风险点ID")
	private Long riskPointId;
	/**
	 * 风险点检查次数
	 */
	@ApiModelProperty("风险点检查次数")
	private Integer checkedCount;
	/**
	 * 群组ID
	 */
	@ApiModelProperty("群组ID")
	private Long groupId;
	/**
	 * 岗位ID
	 */
	@ApiModelProperty("岗位ID")
	private Long positionId;
	/**
	 * 0未完成；1已完成
	 */
	@ApiModelProperty("0未完成；1已完成")
	private Integer status;
	/**
	 * 经度
	 */
	@ApiModelProperty("经度")
	private String longitude;
	/**
	 * 纬度
	 */
	@ApiModelProperty("纬度")
	private String latitude;
	/**
	 * 检查记录地址
	 */
	@ApiModelProperty("检查记录地址")
	private String address;
	/**
	 * 手机型号
	 */
	@ApiModelProperty("手机型号")
	private String phoneCode;
	/**
	 * 评价描述
	 */
	@ApiModelProperty("评价描述")
	private String evaluateDesc;
	/**
	 * 检查结果 1通过； 2不通过；3发现隐患
	 */
	@ApiModelProperty("检查结果 1通过； 2不通过；3发现隐患")
	private Integer result;
	/**
	 * 创建人
	 */
	private Long createdBy;
	/**
	 * 更新人
	 */
	private Long updatedBy;
	/**
	 * 创建时间
	 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date createdTime;
	/**
	 * 更新时间
	 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date updatedTime;
	/**
	 * 删除标记 0正常 1删除
	 */
	private Integer delFlag;


	//扩展字段
	/**
	 * 风险点名称
	 */
	@ApiModelProperty("风险点名称")
	private String riskPointName;
	/**
	 * 风险点图片
	 */
	@ApiModelProperty("风险点图片")
	private String riskPointImg;
	/**
	 * 风险点类型（1 设备设施 2 作业活动）
	 */
	@ApiModelProperty("险点类型（1 设备设施 2 作业活动）")
	private Integer riskPointType;
	/**
	 * 风险点位置
	 */
	@ApiModelProperty("风险点位置")
	private String riskPointPlaces;
	/**
	 * 风险点是否失控1失控0受控
	 */
	@ApiModelProperty("风险点是否失控 1失控 0受控")
	private Integer isOutOfControl;
	/**
	 * 风险点的等级
	 */
	@ApiModelProperty("风险点的等级")
	private Integer riskPointLevel;

	/**
	 * 检查部门名称
	 */
	@ApiModelProperty("检查部门名称")
	private String inspectGroupName;
	/**
	 * 检查人名称
	 */
	@ApiModelProperty("检查人名称")
	private String inspectUserName;
	/**
	 * 检查项名称
	 */
	@ApiModelProperty("检查项名称")
	private String inspectDefName;

}
