package com.hngf.entity.sys;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 角色成员表
 * 
 * @author hngf
 * @email 
 * @date 2020-05-20 17:31:55
 */
@Data
public class RoleMember implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	
	private Long roleId;
	/**
	 * 用户id
	 */
	private Long userId;
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
