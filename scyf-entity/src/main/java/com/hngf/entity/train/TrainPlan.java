package com.hngf.entity.train;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.Future;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * 
 * 
 * @author hngf
 * @email 
 * @date 2020-05-26 08:32:53
 */
@ApiModel(value = "培训计划信息")
@Data
public class TrainPlan implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 培训计划ID
	 */
	
	private Long trainPlanId;
	/**
	 * 公司ID
	 */
	private Long companyId;
	/**
	 * 群组ID
	 */
	private Long groupId;
	/**
	 * 受训群组
	 */
	@ApiModelProperty(value = "受训群组" , position =  5)
	private String trainGroupIds;
	/**
	 * 培训计划名称
	 */
	@ApiModelProperty(value ="培训计划名称", position =  6)
	@NotNull(message = "培训计划名称不能为空！")
	@Length(message = "培训计划名称长度范围1-200", min = 1,max = 200 )
	private String trainPlanName;
	/**
	 * 培训计划类型 1日常；2专业；3年度"
	 */
	@ApiModelProperty(value = "培训计划类型 1日常；2专业；3年度" , position =  7)
	@NotNull(message = "培训计划类型不能为空！")
	@Range(message = "培训类型值的长度范围1-4", min = 1,max = 9999 )
	private Long trainType;
	/**
	 * 培训计划时间
	 */
	@ApiModelProperty(value = "培训计划开始时间" , position =  8)
	@NotNull(message = "培训计划开始时间不能为空！")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8" )
	private Date trainPlanDate;
	/**
	 * 培训计划结束时间
	 */
	@ApiModelProperty(value = "培训计划结束时间" , position =  9)
	//@NotNull(message = "培训计划结束时间不能为空！")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8" )
	private Date trainPlanEndDate;
	/**
	 * 计划培训内容
	 */
	@ApiModelProperty(value = "计划培训内容" , position =  10)
	@NotNull(message = "计划培训内容不能为空！")
	@Length(message = "计划培训内容长度范围1-250",min=1 , max = 250 )
	private String trainPlanContent;
	/**
	 * 培训地点
	 */
	@ApiModelProperty(value = "培训地点" , position =  11)
	@NotNull(message = "培训地点不能为空！")
	@Length(message = "地址长度范围1-250", min=1, max = 250 )
	private String trainPlanAddress;
	/**
	 * 是否提前一天预警
	 */
	@ApiModelProperty(value = "是否提前一天预警" , position =  12)
	@NotNull(message = "是否预警不能为空！")
	@Min(value = 0,message = "最小值为0")
	@Max(value = 1,message = "最大值为1")
	private Integer warnFlag;
	/**
	 * 添加时间
	 */
	@ApiModelProperty(hidden = true)
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8" )
	private Date createdTime;
	/**
	 * 创建人
	 */
	@ApiModelProperty(hidden = true)
	private Long createdBy;
	/**
	 * 更新时间
	 */
	@ApiModelProperty(hidden = true)
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8" )
	private Date updatedTime;
	/**
	 * 更新人
	 */
	@ApiModelProperty(hidden = true)
	@JsonIgnore
	private Long updatedBy;
	/**
	 * 删除标记
	 */
	@ApiModelProperty(hidden = true)
	@JsonIgnore
	private Integer delFlag;

	public void addPrefix(Long createdBy, Long companyId, Long groupId){
		this.setCompanyId(companyId);
		this.setGroupId(groupId);
		this.setCreatedBy(createdBy);
		this.setUpdatedBy(createdBy);
		this.setCreatedTime(new Date());
		this.setUpdatedTime(new Date());
		this.setDelFlag(0);

	}
	public void updatePrefix(Long updatedBy){
		this.setUpdatedBy(updatedBy);
		this.setUpdatedTime(new Date());
	}

}
