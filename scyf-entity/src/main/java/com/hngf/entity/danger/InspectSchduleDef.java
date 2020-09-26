package com.hngf.entity.danger;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Future;
import java.io.Serializable;
import java.util.Date;

/**
 * 检查任务定义
 * 
 * @author hngf
 * @email 
 * @date 2020-05-20 17:36:23
 */
@Data
@ApiModel("检查任务定义model")
public class InspectSchduleDef implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 检查定义ID
	 */
	@ApiModelProperty("检查定义ID")
	private Long schduleDefId;


	/**
	 * 检查表ID
	 */
	@ApiModelProperty("检查表ID")
	private Long inspectDefId;
	@ApiModelProperty("检查表名称")
	private String inspectDefName;

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
	@ApiModelProperty("所属部门名称")
	private String groupName;

	/**
	 * 受检部门
	 */
	@ApiModelProperty("受检部门")
	private Long riskInspectGroup;
	@ApiModelProperty("受检部门名称")
	private String riskInspectGroupName;
	/**
	 * 受检岗位
	 */
	@ApiModelProperty("受检岗位")
	private Long riskInspectPosition;
	@ApiModelProperty("受检岗位")
	private String riskInspectPositionName;
	/**
	 * 检查定义名称
	 */
	@ApiModelProperty("检查定义名称")
	private String riskInspectDefTitle;
	/**
	 * 检查定义描述
	 */
	@ApiModelProperty("检查定义描述")
	private String riskInspectDefDesc;
	/**
	 * 检查类别
	 */
	@ApiModelProperty("检查类别")
	private Long riskInspectType;
	@ApiModelProperty("检查类别名称")
	private String riskInspectTypeName;

	/**
	 * 检查级别
	 */
	@ApiModelProperty("检查级别")
	private Long riskInspectLevel;
	/**
	 * 检查周期类型  临时性（random）、常规性（fixed）
	 */
	@ApiModelProperty("检查周期类型  临时性（random）、常规性（fixed）")
	private String inspectType;

	/**
	 * 检查频率: year一年；half_year半年；quarter三个月；month一个月；week一周；day一天；custom自定义（天为单位）
	 */
/*
	@ApiModelProperty("检查频率: year一年；half_year半年；quarter三个月；month一个月；week一周；day一天；custom自定义（天为单位）")
	private String scheduleFrequency;
*/

	/**
	 * 定时任务表达式：
	 */
	@ApiModelProperty("定时任务表达式")
	private  String scheduleCronExpression;

	@ApiModelProperty("定时任务表达式解析")
	private  String cronExpressionByParse;
	/**
	 * 检查次数
	 */
/*	@ApiModelProperty("检查次数")
	private Integer scheduleCount;*/
	/**
	 * 计划截至日期
	 */
	@ApiModelProperty("计划截至日期")
	private Date lastInspectDate;
	/**
	 * 检查参与人
	 */
	@ApiModelProperty("检查参与人")
	private String riskInspectParticipant;
	@ApiModelProperty("检查参与人姓名")
	private String riskCheckParticipantName;


	/**
	 * 检查方式 1，现场检查2、基础检查
	 */
	@ApiModelProperty("检查方式 1，现场检查2、基础检查")
	private Integer inspectMode;
	/**
	 * 临时性检查开始时间
	 */
	@ApiModelProperty("临时性检查开始时间")
	//@Future(message = "请选择检查开始时间")
	private Date startDate;
	/**
	 * 临时性检查结束时间
	 */
	@ApiModelProperty("临时性检查结束时间")
	//@Future(message = "请选择检查结束时间")
	private Date endDate;
	/**
	 * 开启状态
	 */
	@ApiModelProperty("开启状态")
	private Integer isOpen;
	/**
	 * 执行人
	 */
	@ApiModelProperty("执行人")
	private String executor;
	@ApiModelProperty("执行人名称")
	private String executorName;
	/**
	 * 任务过滤周几 多个用，号隔开，周日为1 周一为2，以此类推
	 */
/*	@ApiModelProperty("任务过滤周几 多个用，号隔开，周日为1 周一为2，以此类推")
	private String filterWeek;*/
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
	 * 删除标记 0正常 1删除
	 */
	private Integer delFlag;

}
