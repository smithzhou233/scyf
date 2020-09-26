package com.hngf.entity.risk;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * 
 * @author hngf
 * @email 
 * @date 2020-06-03 17:15:29
 */
@Data
public class RiskPointCheckRecordLog implements Serializable {
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
	 * 检查表ID
	 */
	private Long inspectDefId;
	/**
	 * 任务ID
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
	 * 0未完成；1已完成
	 */
	private Integer status;
	/**
	 * 经度
	 */
	private String longitude;
	/**
	 * 纬度
	 */
	private String latitude;
	/**
	 * 检查记录地址
	 */
	private String address;
	/**
	 * 手机型号
	 */
	private String phoneCode;
	/**
	 * 评价描述
	 */
	private String evaluateDesc;
	/**
	 * 检查结果 1通过； 2不通过；3发现隐患
	 */
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
