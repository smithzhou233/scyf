package com.hngf.entity.danger;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 检查任务
 * 
 * @author hngf
 * @email 
 * @date 2020-05-20 17:36:24
 */
@Data
@ApiModel("检查任务model")
public class InspectSchdule implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 检查任务计划Id
	 */
	@ApiModelProperty("主键ID")
	private Long inspectScheduleId;
	/**
	 * 检查总次数
	 */
	@ApiModelProperty("检查总次数")
	private Integer inspectTotalCount;
	/**
	 * 当前任务执行次数
	 */
	@ApiModelProperty("当前任务执行次数")
	private Integer inspectScheduleCount;
	/**
	 * 状态：0未检查；1检查中；2已检查；3逾期；4，忽略检查
	 */
	@ApiModelProperty("状态：0未检查；1检查中；2已检查；3逾期；4，忽略检查")
	private Integer status;
	/**
	 * 执行人
	 */
	@ApiModelProperty("执行人")
	private Long executor;
	/**
	 * 检查方式 1，现场检查2、基础检查
	 */
	@ApiModelProperty("检查方式 1，现场检查2、基础检查")
	private Integer inspectMode;
	/**
	 * 检查表ID
	 */
	@ApiModelProperty("检查表ID")
	private Long inspectDefId;
	/**
	 * 所属企业ID
	 */
	@ApiModelProperty("所属企业ID")
	private Long companyId;
	/**
	 * 检查任务定义Id
	 */
	@ApiModelProperty("检查任务定义Id")
	private Long schduleDefId;
	/**
	 * 父任务ID，默认0
	 */
	@ApiModelProperty("父任务ID，默认0")
	private Long parentScheduleId;
	/**
	 * 受检群组ID
	 */
	@ApiModelProperty("受检群组ID")
	private Long inspectGroupId;
	/**
	 * 受检岗位ID
	 */
	@ApiModelProperty("受检岗位ID")
	private Long inspectPositionId;
	/**
	 * 计划开始时间
	 */
	@ApiModelProperty("计划开始时间")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date startDate;
	@ApiModelProperty("计划开始时间-字符类型")
	private String startDateStr;
	/**
	 * 计划结束时间
	 */
	@ApiModelProperty("计划结束时间")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date endDate;
	@ApiModelProperty("计划结束时间-字符类型")
	private String endDateStr;
	/**
	 * 开始执行时间
	 */
	@ApiModelProperty("开始执行时间")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date executorDate;
	/**
	 * 执行结束时间
	 */
	@ApiModelProperty("执行结束时间")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date finishDate;
	/**
	 * 完成人
	 */
	@ApiModelProperty("完成人")
	private Long finishBy;
	/**
	 * 下一次任务是否已生成0否1已生成
	 */
	@ApiModelProperty("下一次任务是否已生成0否1已生成")
	private Integer nextTaskGenerated;
	/**
	 * 岗位相关人
	 */
	@ApiModelProperty("岗位相关人")
	private String positionRelPersons;
	/**
	 * 创建时间
	 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date createdTime;
	/**
	 * 创建人
	 */
	private Long createdBy;
	/**
	 * 更新时间
	 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date updatedTime;
	/**
	 * 更新者Id
	 */
	private Long updatedBy;
	/**
	 * 删除标记 0正常 1删除
	 */
	private Integer delFlag;

	//扩展字段

	/**
	 * 受检群组名称
	 */
	@ApiModelProperty("受检群组名称")
	private String inspectGroupName;
	/**
	 * 受检岗位名称
	 */
	@ApiModelProperty("受检岗位名称")
	private String inspectPositionName;

	/**
	 * 执行人名称
	 */
	@ApiModelProperty("执行人名称")
	private String executorName;

	/**
	 * 检查项名称
	 */
	@ApiModelProperty("检查项名称")
	private String inspectDefName;
	/**
	 * 检查定义名称
	 */
	@ApiModelProperty("检查定义名称")
	private String riskInspectDefTitle;
	/**
	 * 检查周期类型  临时性（random）、常规性（fixed）
	 */
	@ApiModelProperty("检查周期类型  临时性（random）、常规性（fixed）")
	private String inspectType;
	/**
	 * 检查频率: year一年；half_year半年；quarter三个月；month一个月；week一周；day一天；custom自定义（天为单位）
	 */
	@ApiModelProperty("检查频率: 一年；半年；三个月；一个月；一周；一天")
	private String scheduleFrequency;
	/**
	 * 检查次数
	 */
	@ApiModelProperty("检查次数")
	private Integer scheduleCount;
	/**
	 * 检查定义描述
	 */
	@ApiModelProperty("检查定义描述")
	private String riskInspectDefDesc;
	/**
	 * 检查类别名称
	 */
	@ApiModelProperty("检查类别名称")
	private String riskInspectTypeName;
	/**
	 * 检查类别值
	 */
	@ApiModelProperty("检查类别值")
	private String riskInspectType;
	/**
	 * 部门名称
	 */
	@ApiModelProperty("部门名称")
	private String groupName;
	/**
	 * 检查参与人名称
	 */
	@ApiModelProperty("检查参与人名称")
	private String riskInspectParticipantName;

	/**
	 * 检查记录编号
	 */
	@ApiModelProperty("检查记录编号")
	private Long checkRecordNo;

	@ApiModelProperty("任务类型")
	private String inspectTypeStr;
}
