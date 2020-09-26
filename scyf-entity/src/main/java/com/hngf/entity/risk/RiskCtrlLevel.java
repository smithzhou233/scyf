package com.hngf.entity.risk;

import com.hngf.common.validator.group.AddGroup;
import com.hngf.common.validator.group.UpdateGroup;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.util.Date;

/**
 * 风险管控层级
 * 
 * @author hngf
 * @email 
 * @date 2020-05-20 17:36:24
 */
@Data
public class RiskCtrlLevel implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 风险管控层级ID
	 */
	@NotNull(message = "Id不能为空", groups = {UpdateGroup.class})
	@ApiModelProperty(value = "风险管控层级ID")
	private Long riskCtrlLevelId;
	/**
	 * 公司ID
	 */
	@ApiModelProperty(value = "公司ID")
	private Long companyId;
	/**
	 * 风险管控层级名称
	 */
	@NotNull(message = "名称不能为空", groups = {AddGroup.class})
	@Length(message = "名称长度1-200位字符", groups = {AddGroup.class}, min=1,max=200 )
	@ApiModelProperty(value = " 风险管控层级名称")
	private String riskCtrlLevelTitle;
	/**
	 * 风险管控层级值
	 */
	@NotNull(message = "层级值不能为空", groups = {AddGroup.class})
	@Range(message = "层级值范围0-9999",min=0,max=9999 ,groups = {AddGroup.class})
	@ApiModelProperty(value = "风险管控层级值")
	private Integer riskCtrlLevelValue;
	/**
	 * 风险管控层级描述
	 */
	@Pattern(regexp= "[\\s\\S]{0,500}", message = "描述长度0-500位字符", groups = {AddGroup.class} )
	@ApiModelProperty(value = "风险管控层级描述")
	private String riskCtrlLevelDesc;
	/**
	 * 是否固定
	 */
	@ApiModelProperty(value = "是否固定")
	private Integer riskCtrlLevelFixed;
	/**
	 * 状态
	 */
	@ApiModelProperty(value = "状态")
	private Integer riskCtrlLevelStatus;
	/**
	 * 创建人
	 */
	@ApiModelProperty(value = "创建人")
	private Long createdBy;
	/**
	 * 创建时间
	 */
	@ApiModelProperty(value = "创建时间")
	private Date createdTime;
	/**
	 * 更新人
	 */
	@ApiModelProperty(value = "更新人")
	private Long updatedBy;
	/**
	 * 更新时间
	 */
	@ApiModelProperty(value = "更新时间")
	private Date updatedTime;
	/**
	 * 删除标识
	 */
	private Integer delFlag;

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public Long getRiskCtrlLevelId() {
		return riskCtrlLevelId;
	}

	public void setRiskCtrlLevelId(Long riskCtrlLevelId) {
		this.riskCtrlLevelId = riskCtrlLevelId;
	}

	public Long getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Long companyId) {
		this.companyId = companyId;
	}

	public String getRiskCtrlLevelTitle() {
		return riskCtrlLevelTitle;
	}

	public void setRiskCtrlLevelTitle(String riskCtrlLevelTitle) {
		this.riskCtrlLevelTitle = riskCtrlLevelTitle;
	}

	public Integer getRiskCtrlLevelValue() {
		return riskCtrlLevelValue;
	}

	public void setRiskCtrlLevelValue(Integer riskCtrlLevelValue) {
		this.riskCtrlLevelValue = riskCtrlLevelValue;
	}

	public String getRiskCtrlLevelDesc() {
		return riskCtrlLevelDesc;
	}

	public void setRiskCtrlLevelDesc(String riskCtrlLevelDesc) {
		this.riskCtrlLevelDesc = riskCtrlLevelDesc;
	}

	public Integer getRiskCtrlLevelFixed() {
		return riskCtrlLevelFixed;
	}

	public void setRiskCtrlLevelFixed(Integer riskCtrlLevelFixed) {
		this.riskCtrlLevelFixed = riskCtrlLevelFixed;
	}

	public Integer getRiskCtrlLevelStatus() {
		return riskCtrlLevelStatus;
	}

	public void setRiskCtrlLevelStatus(Integer riskCtrlLevelStatus) {
		this.riskCtrlLevelStatus = riskCtrlLevelStatus;
	}

	public Long getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Long createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}

	public Long getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(Long updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Date getUpdatedTime() {
		return updatedTime;
	}

	public void setUpdatedTime(Date updatedTime) {
		this.updatedTime = updatedTime;
	}

	public Integer getDelFlag() {
		return delFlag;
	}

	public void setDelFlag(Integer delFlag) {
		this.delFlag = delFlag;
	}

	public void addPrefixInit(Long createdBy, Long companyId){
		this.setDelFlag(0);
		this.setCompanyId(companyId);
		this.setCreatedBy(createdBy);
		this.setCreatedTime(new Date());
		this.setUpdatedBy(createdBy);
		this.setUpdatedTime(new Date());
	}

	public void updatePrefixInit(Long updatedBy){
		this.setUpdatedBy(updatedBy);
		this.setUpdatedTime(new Date());
	}
}
