package com.hngf.entity.sys;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * 角色授权菜单表
 * 
 * @author hngf
 * @email 
 * @date 2020-05-20 17:31:55
 */
@JsonIgnoreProperties({"delFlag"})
@Data
public class RoleMenu implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 角色ID
	 */
	@NotNull(message = "角色ID不能为空")
	private Long roleId;
	/**
	 * 菜单ID
	 */
	
	private Long menuId;
	/**
	 * 父ID
	 */
	private Long menuParent;
	/**
	 * 菜单URL
	 */
	private String menuUrl;
	/**
	 * 菜单名称
	 */
	private String menuName;
	/**
	 * 0分组1菜单项2分隔符
	 */
	@NotNull(message = "菜单类型必选")
	private Integer menuType;
	/**
	 * icon样式
	 */
	private String menuCss;
	/**
	 * 说明
	 */
	private String menuDesc;
	/**
	 * 层级
	 */
	private Integer menuLevel;
	/**
	 * 排序
	 */
	private Integer sortNo;
	/**
	 * 创建人
	 */
	private Long createdBy;
	/**
	 * 创建时间
	 */
	private Date createdTime;
	/**
	 * 修改人
	 */
	private Long updatedBy;
	/**
	 * 修改时间
	 */
	private Date updatedTime;
	/**
	 * 删除标记
	 */
	private Integer delFlag;

}
