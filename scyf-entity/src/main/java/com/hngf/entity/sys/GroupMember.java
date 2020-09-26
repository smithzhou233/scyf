package com.hngf.entity.sys;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 群组成员表
 * 
 * @author hngf
 * @email 
 * @date 2020-05-20 17:31:55
 */
@Data
public class GroupMember implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 群组成员id
	 */
	
	private Long groupMemberId;
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
	 * 
	 */
	private Integer groupMemberFixed;
	/**
	 * 群组成员备注
	 */
	private String groupMemberRemark;
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
	 * 删除人
	 */
	private Long deletedBy;
	/**
	 * 删除标记
	 */
	private Integer delFlag;

}
