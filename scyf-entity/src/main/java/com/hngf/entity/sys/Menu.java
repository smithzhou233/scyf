package com.hngf.entity.sys;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

/**
 * 菜单表
 * 
 * @author hngf
 * @email 
 * @date 2020-05-20 17:31:55
 */
@JsonIgnoreProperties({"delFlag"})
@Data
@ApiModel("菜单model")
public class Menu implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 菜单ID
	 */
	@ApiModelProperty("菜单ID")
	private Long menuId;
	/**
	 * 父级菜单
	 */
	@ApiModelProperty("父级菜单Id")
	private Long menuParentId;
	/**
	 * 所有父菜单
	 */
	private String menuParentIds;
	/**
	 * 菜单内容
	 */
	@ApiModelProperty("菜单名称")
	private String menuText;
	/**
	 * 菜单url
	 */
	@ApiModelProperty("菜单url")
	private String menuUrl;
	/**
	 * 菜单样式
	 */
	@ApiModelProperty("菜单样式")
	private String menuCss;
	/**
	 * 菜单描述
	 */
	@ApiModelProperty("菜单描述")
	private String menuDesc;
	/**
	 * shiro权限标志
	 */
	@ApiModelProperty("shiro权限标志")
	private String menuPermissions;
	/**
	 * 排序序号
	 */
	@ApiModelProperty("排序序号")
	private Integer sortNo;
	/**
	 * 菜单类型
	 */
	@ApiModelProperty("菜单类型 1顶层 2 目录 3 菜单 4 按钮")
	private Integer menuType;
	/**
	 * 账号类型:0系统账号1监管账号2企业账号
	 */
	private Integer accountType;
	/**
	 * 是否在菜单中显示：0显示1不显示
	 */
	private Integer isShow;
	/**
	 * 创建时间
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
	 * 更新人
	 */
	private Long updatedBy;
	/**
	 * 删除标记
	 */
	private Integer delFlag;


	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Menu menu = (Menu) o;
		return menuId.longValue() == menu.menuId.longValue();
	}

	@Override
	public int hashCode() {
		return Objects.hash(menuId);
	}

	@Override
	public String toString() {
		return "Menu{" +
				"menuId=" + menuId +
				", menuParentId=" + menuParentId +
				", menuParentIds='" + menuParentIds + '\'' +
				", menuText='" + menuText + '\'' +
				", menuUrl='" + menuUrl + '\'' +
				", menuCss='" + menuCss + '\'' +
				", menuDesc='" + menuDesc + '\'' +
				", menuPermissions='" + menuPermissions + '\'' +
				", sortNo=" + sortNo +
				", menuType=" + menuType +
				", accountType=" + accountType +
				", isShow=" + isShow +
				", createdTime=" + createdTime +
				", createdBy=" + createdBy +
				", updatedTime=" + updatedTime +
				", updatedBy=" + updatedBy +
				", delFlag=" + delFlag +
				'}';
	}
}
