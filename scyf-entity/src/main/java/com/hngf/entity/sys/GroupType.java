package com.hngf.entity.sys;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.hngf.common.validator.group.AddGroup;
import com.hngf.common.validator.group.UpdateGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.util.Date;

/**
 * 部门类型
 * 
 * @author hngf
 * @email 
 * @date 2020-05-22 11:08:22
 */
@JsonIgnoreProperties({"delFlag"})
@Data
@ApiModel
public class GroupType implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */

	@NotNull(message = "部门类型id不能为空！", groups = {UpdateGroup.class})
	@ApiModelProperty(value = "企业部门类型id" , position = 1 )
	private Long groupTypeId;

	private Long companyId;
	/**
	 * 
	 */
	@NotBlank(message = "部门类型标题不能为空",groups = {AddGroup.class})
	@Length(message = "类型名称长度范围1-100",min =1 ,max = 100 , groups = {AddGroup.class})
	@ApiModelProperty(value = "企业部门类型名称" , position = 3 )
	private String groupTypeTitle;
	/**
	 * 
	 */
	@Pattern(regexp= "[\\s\\S]{0,500}", message = "描述长度0-500位字符", groups = {AddGroup.class} )
	@ApiModelProperty(value = "企业部门类型描述" , position =5 )
	private String groupTypeDesc;
	/**
	 * 
	 */
	private Integer groupTypeShowif;
	/**
	 * 
	 */
	private Long createdBy;
	/**
	 * 
	 */
	@ApiModelProperty(value = "企业部门类型创建时间" , position = 9 )
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date createdTime;
	/**
	 * 
	 */
	private Long updatedBy;
	/**
	 * 
	 */
	private Date updatedTime;
	/**
	 * 
	 */
	private Integer delFlag;

	public void insertPrefix(Long userId){
		this.setCreatedBy(userId );
		this.setCreatedTime(new Date());
		this.setUpdatedBy(userId );
		this.setUpdatedTime(new Date());
		this.setDelFlag(0);
	}

	public void updatePrefix(Long userId){
		this.setUpdatedBy(userId );
		this.setUpdatedTime(new Date());
	}
	@Override
	public String toString() {
		return "GroupType{" +
				"groupTypeId=" + groupTypeId +
				", companyId=" + companyId +
				", groupTypeTitle='" + groupTypeTitle + '\'' +
				", groupTypeDesc='" + groupTypeDesc + '\'' +
				", groupTypeShowif=" + groupTypeShowif +
				", createdBy=" + createdBy +
				", createdTime=" + createdTime +
				", updatedBy=" + updatedBy +
				", updatedTime=" + updatedTime +
				", delFlag=" + delFlag +
				'}';
	}
}
