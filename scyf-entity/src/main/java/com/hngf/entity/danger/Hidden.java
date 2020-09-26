package com.hngf.entity.danger;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * 隐患表
 * 
 * @author hngf
 * @email 
 * @date 2020-05-20 17:36:24
 */
@Data
@ApiModel("隐患")
public class Hidden implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 主键ID
	 */
	
	private Long hiddenId;
	/**
	 * 所属单位
	 */
	private Long companyId;
	/**
	 * 组织机构ID
	 */
	private Long groupId;
	/**
	 * 危险点ID
	 */
	@ApiModelProperty("危险点ID")
	private Long riskPointId;
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
	 * 检查记录Id
	 */
	@ApiModelProperty("检查记录Id")
	private Long inspectRecordId;
	/**
	 * 检查项ID/风险ID
	 */
	@ApiModelProperty("检查项ID/风险ID")
	private Long inspectItemId;
	/**
	 * 检查内容ID/风险管控措施ID
	 */
	@ApiModelProperty("检查内容ID/风险管控措施ID")
	private Long inspectContentId;
	/**
	 * 检查项内容/管控措施内容
	 */
	@ApiModelProperty("检查项内容/管控措施内容")
	private String inspectContentDesc;
	/**
	 * 隐患类型ID
	 */
	@ApiModelProperty("隐患类型ID")
	private Long hiddenCatId;
	/**
	 * 隐患等级：1重大，2较大，3一般，4较低；
【二级取：1,3】
【三级取：1,2,3】
【四级取：1,2,3,4】
	 */
	@ApiModelProperty("隐患等级")
	private Integer hiddenLevel;
	/**
	 * 隐患名称
	 */
	@ApiModelProperty("隐患名称")
	private String hiddenTitle;
	/**
	 * 隐患描述
	 */
	@ApiModelProperty("隐患描述")
	private String hiddenDesc;
	/**
	 * 风险等级
	 */
	@ApiModelProperty("风险等级")
	private Integer riskLevel;
	/**
	 * 整改单位
	 */
	@ApiModelProperty("整改单位")
	private String hiddenRectifyDept;
	/**
	 * 整改部门
	 */
	@ApiModelProperty("整改部门")
	private Long hiddenRetifyGroup;
	/**
	 * 隐患原来整改人
	 */
	@ApiModelProperty("隐患原来整改人")
	private Long hiddenQuondamRetifyBy;
	/**
	 * 整改人
	 */
	@ApiModelProperty("整改人")
	private Long hiddenRetifyBy;
	/**
	 * 整改期限
	 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@ApiModelProperty("整改期限")
	private Date hiddenRetifyDeadline;
	@ApiModelProperty("整改期限格式化日期")
	private String hiddenRetifyDeadlineStr;
	/**
	 * 验收单位
	 */
	@ApiModelProperty("验收单位")
	private Long hiddenAcceptedGroup;
	/**
	 * 验收人
	 */
	@ApiModelProperty("验收人")
	private Long hiddenAcceptedBy;
	/**
	 * 评审单位
	 */
	@ApiModelProperty("评审单位")
	private Long hiddenReviewGroup;
	/**
	 * 评审人
	 */
	@ApiModelProperty("评审人")
	private Long hiddenReviewBy;
	/**
	 * 隐患整改方案表ID
	 */
	@ApiModelProperty("隐患整改方案表ID")
	private Long programmeId;
	/**
	 * 发生时间
	 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@ApiModelProperty("发生时间")
	private Date happenedTime;
	@ApiModelProperty("发生时间格式化")
	private String happenedTimeStr;
	/**
	 * 结束时间
	 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@ApiModelProperty("结束时间")
	private Date finishedTime;
	@ApiModelProperty("结束时间格式化")
	private String finishedTimeStr;
	/**
	 * 隐患状态
 0: 待提交；
1: 待评审；
2：待整改；
3：待验收；
4：验收通过；
5：验收不通过；
6：已撤消；
7：已删除；
	 */
	@NotNull(message = "隐患状态不能为空")
	@ApiModelProperty("隐患状态")
	private Integer status;
	/**
	 * 创建人
	 */
	@ApiModelProperty("创建人")
	private Long createdBy;
	/**
	 * 创建时间
	 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@ApiModelProperty("创建时间")
	private Date createdTime;
	private String createdTimeStr;
	/**
	 * 更新者Id
	 */
	private Long updatedBy;
	/**
	 * 更新时间
	 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date updatedTime;
	/**
	 * 删除：0未删除；1已删除；
	 */
	private Integer delFlag;



	//扩展字段1 checkResult  2 hdangerReviewOn  3 feedbackId
	private Integer checkResult;
	private Integer hdangerReviewOn;
	private Long feedbackId;



	//是否整改
	@ApiModelProperty("是否整改")
	private Integer isRetify;

	//是否进行任务检查：1检查,0不检查
	@ApiModelProperty("否进行任务检查：1检查,0不检查")
	private Integer isScheduleCheck;
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

	private Long itemDetailId;

	/**
	 * 隐患附件地址，多个逗号分隔
	 */
	@ApiModelProperty("隐患附件地址，多个逗号分隔")
	private String hiddenAttachPath;

	/**
	 * 隐患附件Id
	 */
	@ApiModelProperty("隐患附件Id")
	private String hiddenAttachId;
	/**
	 * 附件类型image、video
	 */
	@ApiModelProperty("附件类型image、video")
	private String hiddenAttachType;

	/**
	 * 检查类型
	 */
	@ApiModelProperty("检查类型")
	private String inspectDefType;

	/**
	 * 现场数据采集
	 */
	@ApiModelProperty("现场数据采集")
	private String spotData;


	//================整改相关的参数=====================

	/**
	 * 原因分析
	 */
	@ApiModelProperty("原因分析")
	private String hiddenRetifyReasons;
	/**
	 * 整改措施
	 */
	@ApiModelProperty("整改措施")
	private String hiddenRetifyMeasures;
	/**
	 * 整改协助单位
	 */
	@ApiModelProperty("整改协助单位")
	private String hiddenRetifyAssisting;
	/**
	 * 整改花费
	 */
	@ApiModelProperty("整改花费")
	private Double hiddenRetifyAmount;
	/**
	 * 整改类型，0、自行整改，1、协助整改,2、委托整改
	 */
	@ApiModelProperty("整改类型，0、自行整改，1、协助整改,2、委托整改")
	private Integer hiddenRetifyType;

	/**
	 * 部门名称
	 */
	@ApiModelProperty("部门名称")
	private String groupName;
	/**
	 * 隐患创建人名称
	 */
	@ApiModelProperty("隐患创建人名称")
	private String hiddenCreatedByName;
	/**
	 * 隐患分类名称
	 */
	@ApiModelProperty("隐患分类名称")
	private String hiddenCatTitle;

	/**
	 * 危险源名称
	 */
	@ApiModelProperty("危险源名称")
	private String riskDangerName;
	/**
	 * 执行人
	 */
	@ApiModelProperty("执行人")
	private String executor;
	@ApiModelProperty("执行人Id")
	private String executorId;

	/**
	 * 风险点类型名称
	 */
	@ApiModelProperty("风险点类型名称")
	private String riskPointTypeName;
	/**
	 * 风险点名称
	 */
	@ApiModelProperty("风险点名称")
	private String riskPointName;
	/**
	 * 风险点等级名称
	 */
	@ApiModelProperty("风险点等级名称")
	private String riskPointLevelName;
	/**
	 *检查项名称
	 */
	@ApiModelProperty("检查项名称")
	private String inspectDefName;
	/**
	 * 隐患状态名称：待提交/待评审/待整改/待验收/已撤销...
	 */
	@ApiModelProperty("隐患状态名称：待提交/待评审/待整改/待验收/已撤销...")
	private String disposeStatusStr;
	/**
	 * 隐患等级说明：重大/较大/一般/较低
	 */
	@ApiModelProperty("隐患等级说明：重大/较大/一般/较低")
	private String hiddenLevelStr;
	/**
	 * 隐患附件URL地址
	 */
	@ApiModelProperty("隐患附件URL地址")
	private String imgUrl;

	/**
	 * 评审单位名称
	 */
	@ApiModelProperty("评审单位名称")
	private String hiddenReviewGroupName;
	/**
	 * 评审人名称
	 */
	@ApiModelProperty("评审人名称")
	private String hiddenReviewByName;
	/**
	 * 整改部门名称
	 */
	@ApiModelProperty("整改部门名称")
	private String hiddenRetifyGroupName;
	/**
	 * 整改人名称
	 */
	@ApiModelProperty("整改人名称")
	private String hiddenRetifyByName;
	/**
	 * 验收单位名称
	 */
	@ApiModelProperty("验收单位名称")
	private String hiddenAcceptedGroupName;
	/**
	 * 验收人名称
	 */
	@ApiModelProperty("验收人名称")
	private String hiddenAcceptedByName;

	/**
	 * 公司名称
	 */
	@ApiModelProperty("公司名称")
	private String companyName;
	/**
	 * 危害因素
	 */
	@ApiModelProperty("危害因素")
	private String riskHramFactor;
	/**
	 * 详情-多个附件返回
	 */
	@ApiModelProperty("多个附件返回map")
	private String hiddenAttachMap;

	@ApiModelProperty("签名图片地址url")
	private String signUrl;
}
