package com.hngf.entity.sys;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 群组成员岗位表
 * 
 * @author hngf
 * @email 
 * @date 2020-05-20 17:31:55
 */
@Data
public class GroupMemberPositionGrant implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 群组成员岗位id
	 */
	
	private Long groupMemberPositionId;
	/**
	 * 企业id
	 */
	private Long companyId;
	/**
	 * 群组id
	 */
	private Long groupId;
	/**
	 * 用户id
	 */
	private Long userId;
	/**
	 * 授权部门ID
	 */
	private Long grantGroupId;
	/**
	 * 岗位表
	 */
	private Long positionId;
	/**
	 * 创建人
	 */
	private Long createdBy;
	/**
	 * 创建时间
	 */
	private Date createdTime;
	/**
	 * 更新人
	 */
	private Long updatedBy;
	/**
	 * 更新时间
	 */
	private Date updatedTime;
	/**
	 * 删除标识
	 */
	private Integer delFlag;

}
