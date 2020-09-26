package com.hngf.entity.risk;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 检查记录日志表
 * 
 * @author hngf
 * @email 
 * @date 2020-06-10 17:16:34
 */
@Data
public class RiskInspectRecordLog implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 检查记录Id
	 */
	
	private Long inspectRecordId;
	/**
	 * 检查记录编号
	 */
	private Long inspectRecordNo;
	/**
	 * 所属企业
	 */
	private Long companyId;
	/**
	 * 所属部门
	 */
	private Long groupId;
	/**
	 * 风险点ID
	 */
	private Long riskPointId;
	/**
	 * 任务ID
	 */
	private Long inspectScheduleId;
	/**
	 * 检查计划项Id
	 */
	private Long inspectItemDefId;
	/**
	 * 任务检查定义ID/检查表ID
	 */
	private Long schduleDefId;
	/**
	 * 检查项明细ID/(风险明细ID/危险源明细ID)
	 */
	private Long itemDetailId;
	/**
	 * 检查项内容明细ID/风险管控措施明细ID
	 */
	private Long riskMeasureId;
	/**
	 * 检查项内容 / 管控措施内容
	 */
	private String riskMeasureContent;
	/**
	 * 检查结果 1通过；2不通过；3发现隐患；4未涉及；
	 */
	private Integer inspectResult;
	/**
	 * 当前是第几次检查
	 */
	private Integer inspectNumber;
	/**
	 * 现场数据采集
	 */
	private String spotData;
	/**
	 * 检查记录备注
	 */
	private String remark;
	/**
	 * 更新时间
	 */
	private Date updatedTime;
	/**
	 * 更新者Id
	 */
	private Long updatedBy;
	/**
	 * 创建时间
	 */
	private Date createdTime;
	/**
	 * 创建人
	 */
	private Long createdBy;
	/**
	 * 删除标记 0正常 1删除
	 */
	private Integer delFlag;

}
