package com.hngf.entity.sys;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hngf.common.validator.group.AddGroup;
import com.hngf.common.validator.group.UpdateGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * 组织机构
 * 
 * @author hngf
 * @email 
 * @date 2020-05-20 17:31:55
 */
@ApiModel(value = "机构信息",description = "监管机构")
@Data
public class Org implements Serializable {

	private static final long serialVersionUID = 8833049657255392057L;
	/**
	 * 组织机构ID 
	 */
	@ApiModelProperty(value = "组织机构ID",position = 1 )
	@NotBlank(message = "组织机构名称不能为空" ,groups = UpdateGroup.class)
	private Long orgId;
	/**
	 * 组织机构名称
	 */
	@ApiModelProperty(value = "组织机构名称",position = 3 , required =  true)
	@NotBlank(message = "组织机构名称不能为空",groups = {AddGroup.class, UpdateGroup.class})
	@Length(message = "组织机构名称长度范围1-64", min=1, max= 64,groups = {AddGroup.class, UpdateGroup.class})
	private String orgName;
	/**
	 * 组织机构代码
	 */
	@ApiModelProperty(value = "组织机构代码",position = 5 )
	@Length(message = "组织机构名称长度范围0-32",max= 32,groups = {AddGroup.class, UpdateGroup.class})
	private String orgCode;
	/**
	 * 父级组织机构ID
	 */
	@ApiModelProperty(value = "父级组织机构ID", position =  7 )
	@NotNull(message = "父级组织机构Id不能为空！", groups = { UpdateGroup.class})
	private Long orgParentId;
	/**
	 * 1安监局2交通局3水利局
	 */
	@ApiModelProperty(value = "1安监局2交通局3水利局", allowableValues = "1,2,3", required =  true, position =  9 )
	@NotNull(message = "机构类型不能为空" ,groups = {AddGroup.class, UpdateGroup.class})
	@Range(message = "1安监局2交通局3水利局 ",min=1, max = 4,groups = {AddGroup.class, UpdateGroup.class})
	private Integer orgTypeId;
	/**
	 * 左
	 */
	@ApiModelProperty(hidden = true)
	private Integer orgLeft;
	/**
	 * 右
	 */
	@ApiModelProperty(hidden = true)
	private Integer orgRight;
	/**
	 * 层级
	 */
	@ApiModelProperty(value = "层级", position =  13 )
	private Integer orgLevel;
	/**
	 * 区域代码
	 */
	@ApiModelProperty(value = "区域代码", position =  15 )
	private String orgAreaCode;
	/**
	 * 区域名称
	 */
	@ApiModelProperty(value = "区域名称", position =  17 )
	private String orgAreaName;
	/**
	 * 新组织ID,位编码
	 */
	@ApiModelProperty(value = "组织ID", position =  18 )
	private Long orgGroupId;
	/**
	 * 管理员账号
	 */
	@ApiModelProperty(value = "管理员账号", position =  19 )
	private Long orgAdminId;
	/**
	 * 顶级组织机构代码
	 */
	@ApiModelProperty(value = "顶级组织机构代码", position =  20 )
	private Long orgRootId;
	/**
	 * 创建时间
	 */
	@ApiModelProperty(hidden = true)
	private Date createdTime;
	/**
	 * 创建者
	 */
	@ApiModelProperty(hidden = true)
	private Long createdBy;
	/**
	 * 更新时间
	 */
	@ApiModelProperty(hidden = true)
	private Date updatedTime;
	/**
	 * 更新者
	 */
	@ApiModelProperty(hidden = true)
	private Long updatedBy;
	/**
	 * 删除标识
	 */
	@ApiModelProperty(hidden = true)
	@JsonIgnore
	private Integer delFlag;

	public void addPrefixInit(Long createdBy){
		this.setDelFlag(0);
		this.setCreatedBy(createdBy);
		this.setCreatedTime(new Date());
		this.setUpdatedBy(createdBy);
		this.setUpdatedTime(new Date());
	}
	public void updatePrefixInit(Long updatedBy ){
		this.setUpdatedBy(updatedBy);
		this.setUpdatedTime(new Date());
	}

	@Override
	public String toString() {
		return "Org{" +
				"orgId=" + orgId +
				", orgName='" + orgName + '\'' +
				", orgCode='" + orgCode + '\'' +
				", orgParentId=" + orgParentId +
				", orgTypeId=" + orgTypeId +
				", orgLeft=" + orgLeft +
				", orgRight=" + orgRight +
				", orgLevel=" + orgLevel +
				", orgAreaCode='" + orgAreaCode + '\'' +
				", orgAreaName='" + orgAreaName + '\'' +
				", orgGroupId=" + orgGroupId +
				", orgAdminId=" + orgAdminId +
				", orgRootId=" + orgRootId +
				", createdTime=" + createdTime +
				", createdBy=" + createdBy +
				", updatedTime=" + updatedTime +
				", updatedBy=" + updatedBy +
				", delFlag=" + delFlag +
				'}';
	}
}
