package com.hngf.entity.sys;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 
 * 
 * @author hngf
 * @email 
 * @date 2020-05-20 17:31:55
 */
@ApiModel
@JsonIgnoreProperties({"delFlag"})
@Data
public class Role implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 角色ID
	 */
	@ApiModelProperty(value = "角色id",position = 1)
	private Long roleId;
	/**
	 * 角色名称
	 */
	@ApiModelProperty(value = "角色名称",position = 2)
	@NotBlank(message = "角色名称不能为空")
	private String roleName;
	/**
	 * 角色描述
	 */
	@ApiModelProperty(value = "角色描述",position = 3)
	private String roleDesc;
	/**
	 * 角色类型：1私有、2公共
	 */
	@ApiModelProperty(value = "角色类型：1私有、2公共",position = 5)
	private Long roleType;
	/**
	 * 排序
	 */
	@ApiModelProperty(value = "角色排序",position = 6)
	private Long roleOrder;
	/**
	 * 状态
	 */
	@ApiModelProperty(value = "角色状态",position = 7)
	private Integer roleStatus;
	/**
	 * 
	 */
	@ApiModelProperty(hidden = true)
	private Integer roleFixed;
	/**
	 * 创建人
	 */
	@ApiModelProperty(hidden = true)
	private Long createdBy;
	/**
	 * 创建时间
	 */
	@ApiModelProperty(hidden = true)
	private Date createdTime;
	/**
	 * 修改人
	 */
	@ApiModelProperty(hidden = true)
	private Long updatedBy;
	/**
	 * 修改时间
	 */
	@ApiModelProperty(hidden = true)
	private Date updatedTime;
	/**
	 * 删除标记
	 */
	@ApiModelProperty(hidden = true)
	private Integer delFlag;

	//扩展字段，与数据库业务无关
	/**
	 * 角色对应菜单列表
	 */
	@ApiModelProperty(hidden = true)
	private List<Long> menuIdList;

	@Override
	public String toString() {
		return "Role{" +
				"roleId=" + roleId +
				", roleName='" + roleName + '\'' +
				", roleDesc='" + roleDesc + '\'' +
				", roleType=" + roleType +
				", roleOrder=" + roleOrder +
				", roleStatus=" + roleStatus +
				", roleFixed=" + roleFixed +
				", createdBy=" + createdBy +
				", createdTime=" + createdTime +
				", updatedBy=" + updatedBy +
				", updatedTime=" + updatedTime +
				", delFlag=" + delFlag +
				'}';
	}
}
