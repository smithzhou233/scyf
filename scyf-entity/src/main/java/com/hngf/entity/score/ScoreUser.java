package com.hngf.entity.score;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户绩效考核总得分表
 * 
 * @author hngf
 * @email 
 * @date 2020-05-27 10:11:53
 */
@Data
public class ScoreUser implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 绩效考核记录ID
	 */
	
	private Long scoreUserId;
	/**
	 * 企业ID
	 */
	private String companyId;
	/**
	 * 部门ID
	 */
	private Integer groupId;
	/**
	 * 考核得分人员ID
	 */
	private Integer userId;
	/**
	 * 得分类型：1总分；2年度度得分；3月度得分；
	 */
	private Integer scoreType;
	/**
	 * 总得分
	 */
	private Integer grossScore;
	/**
	 * 考核描述
	 */
	private String remark;
	/**
	 * 更新时间
	 */
	private Date updatedTime;
	/**
	 * 更新者Id
	 */
	private Long updatedBy;
	/**
	 * 创建时间
	 */
	private Date createdTime;
	/**
	 * 创建人
	 */
	private Long createdBy;
	/**
	 * 删除标记 0正常 1删除
	 */
	private Integer delFlag;

}
