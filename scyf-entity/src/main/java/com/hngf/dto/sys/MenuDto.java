package com.hngf.dto.sys;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

@Data
public class MenuDto implements Serializable{

	private static final long serialVersionUID = 1L;
	
	/**
	 * 菜单ID
	 */
	private Long menuId;
	/**
	 * 父级菜单
	 */
	private Long menuParentId;
	/**
	 * 菜单内容
	 */
	private String menuText;
	/**
	 * 菜单url
	 */
	private String menuUrl;
	/**
	 * 菜单样式
	 */
	private String menuCss;
	
	/**
	 * 排序序号
	 */
	private Integer sortNo;
	/**
	 * 菜单类型
	 */
	private Integer menuType;
	/**
	 * shiro权限标志
	 */
	private String menuPermissions;
	/**
	 * 菜单描述
	 */
	private String menuDesc;
	/**
	 * 子节点菜单
	 */
	private List<MenuDto> children;

}
