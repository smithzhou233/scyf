package com.hngf.entity.score;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 绩效考核打分记录日志表
 * 
 * @author hngf
 * @email 
 * @date 2020-05-27 10:11:53
 */
@Data
public class ScoreLog implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 绩效考核记录ID
	 */
	
	private Long scoreLogId;
	/**
	 * 企业ID
	 */
	private Long companyId;
	/**
	 * 部门ID
	 */
	private Long groupId;
	/**
	 * 考核得分人员ID
	 */
	private Long userId;
	/**
	 * 业务ID：隐患、任务、等等
	 */
	private Long professionId;
	/**
	 * 累计扣分值（单次扣分标准见考核配置）
	 */
	private Integer performanceScore;
	/**
	 * 考核描述
	 */
	private String performanceRemark;
	/**
	 * 更新时间
	 */
	private Date updatedTime;
	/**
	 * 更新者ID
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
