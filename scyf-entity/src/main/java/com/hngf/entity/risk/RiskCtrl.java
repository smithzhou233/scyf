package com.hngf.entity.risk;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 风险管控配置
 * 
 * @author hngf
 * @email 
 * @date 2020-05-20 17:36:24
 */
@ApiModel(value = "风险与管控层级关系")
@Data
public class RiskCtrl implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 风险ID
	 */
	@ApiModelProperty(value = "风险Id")
	private Long riskId;
	/**
	 * 风险管控层级ID
	 */
	@ApiModelProperty(value = "风险管控层级ID")
	private Long riskCtrlLevelId;
	/**
	 * 风险管控人id
	 */
	@ApiModelProperty(value = "风险管控岗位Id")
	private Long riskCtrlPositionId;
	/**
	 * 所属公司ID
	 */
	@ApiModelProperty(value = "风险所属公司ID")
	private Long companyId;
	/**
	 * 创建人
	 */
	@ApiModelProperty(hidden = true)
	private Long createdBy;
	/**
	 * 创建时间
	 */
	@ApiModelProperty(hidden = true)
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8" )
	private Date createdTime;
	/**
	 * 更新人
	 */
	@ApiModelProperty(hidden = true)
	private Long updatedBy;
	/**
	 * 更新时间
	 */
	@ApiModelProperty(hidden = true)
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8" )
	private Date updatedTime;
	/**
	 * 删除标识
	 */
	@ApiModelProperty(hidden = true)
	@JsonIgnore
	private Integer delFlag;

	public void insertPrefixInit(Long createdBy, Long companyId ){
		this.setCompanyId(companyId);
		this.setCreatedBy(createdBy);
		this.setUpdatedBy(createdBy);
		this.setCreatedTime(new Date());
		this.setUpdatedTime(new Date());
		this.setDelFlag(0);
	}
}
