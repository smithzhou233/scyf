package com.hngf.entity.risk;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 风险管控措施
 * 
 * @author hngf
 * @email 
 * @date 2020-05-20 17:36:23
 */
@Data
public class RiskMeasure implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 风险管控措施ID
	 */
	
	private Long riskMeasureId;
	/**
	 * 风险点ID
	 */
	private Long riskId;
	/**
	 * 管控措施类别:1工程技术；2管理措施；3教育措施；4应急措施；5个体防护；
	 */
	private Integer riskMeasureTypeId;
	/**
	 * 管控措施内容
	 */
	private String riskMeasureContent;
	/**
	 * 描述
	 */
	private String riskMeasureDesc;
	/**
	 * 所属公司ID
	 */
	private Long companyId;
	/**
	 * 添加时间
	 */
	private Date createdTime;
	/**
	 * 创建人
	 */
	private Long createdBy;
	/**
	 * 更新时间
	 */
	private Date updatedTime;
	/**
	 * 修改人
	 */
	private Long updatedBy;
	/**
	 * 删除标记0正常1删除
	 */
	private Integer delFlag;

}
