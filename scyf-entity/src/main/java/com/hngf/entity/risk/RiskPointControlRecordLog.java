package com.hngf.entity.risk;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 风险点管控实时告警记录表
 * 
 * @author hngf
 * @email 
 * @date 2020-05-20 17:36:23
 */
@JsonIgnoreProperties("delFlag")
@Data
public class RiskPointControlRecordLog implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 记录ID
	 */
	
	private Long recordId;
	/**
	 * 公司ID
	 */
	private Long companyId;
	/**
	 * 风险点ID
	 */
	private Long riskPointId;
	/**
	 * 明细ID：隐患、任务、传感器（当前业务层ID）
	 */
	private Long detailId;
	/**
	 * 类型：1隐患告警；2任务逾期告警；3传感器告警；
	 */
	private Integer detailType;
	/**
	 * 岗位ID
	 */
	private Long positionId;
	/**
	 * 是否失控：0失控；1受控;
	 */
	private Integer isControl;
	/**
	 * 是否闭环：1已闭环；0未闭环
	 */
	private Integer isCloseUp;
	/**
	 * 产生原因
	 */
	private String causeReason;
	/**
	 * 原因描述
	 */
	private String causeRemark;
	/**
	 * 新增人员
	 */
	private Long createdBy;
	/**
	 * 更新人员
	 */
	private Long cupdatedBy;
	/**
	 * 创建时间
	 */
	private Date createdTime;
	/**
	 * 更新时间
	 */
	private Date updatedTime;
	/**
	 * 删除标记
	 */
	private Integer delFlag;


	//以下是扩展字段
	/**
	 * 风险点的等级
	 */
	private Integer riskPointLevel;
	/**
	 * 部门名称
	 */
	private String groupName;
	/**
	 * 风险点名称
	 */
	private String riskPointName;

}
