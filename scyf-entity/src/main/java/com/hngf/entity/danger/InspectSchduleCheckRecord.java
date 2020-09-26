package com.hngf.entity.danger;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 检查任务记录表
 * 
 * @author hngf
 * @email 
 * @date 2020-06-18 16:57:21
 */
@Data
public class InspectSchduleCheckRecord implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 记录ID
	 */
	
	private Long recordId;
	/**
	 * 检查记录编号
	 */
	private Long checkRecordNo;
	/**
	 * 检查任务定义ID
	 */
	private Long schduleDefId;
	/**
	 * 检查表ID
	 */
	private Long inspectDefId;
	/**
	 * 检查任务计划Id
	 */
	private Long inspectScheduleId;
	/**
	 * 公司ID
	 */
	private Long companyId;
	/**
	 * 风险点ID
	 */
	private Long riskPointId;
	/**
	 * 风险点检查次数
	 */
	private Integer checkedCount;
	/**
	 * 群组ID
	 */
	private Long groupId;
	/**
	 * 岗位ID
	 */
	private Long positionId;
	/**
	 * 状态：0未检查完成；1检查完成；
	 */
	private Integer status;
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
	private Date createdTime;
	/**
	 * 更新时间
	 */
	private Date updatedTime;
	/**
	 * 删除标记 0正常 1删除
	 */
	private Integer delFlag;

}
