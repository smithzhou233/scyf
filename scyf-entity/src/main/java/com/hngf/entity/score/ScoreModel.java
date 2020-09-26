package com.hngf.entity.score;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 绩效考核模式配置表
 * 
 * @author hngf
 * @email 
 * @date 2020-05-27 10:11:53
 */
@Data
public class ScoreModel implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 绩效考核得分配置ID
	 */
	
	private Long scoreModelId;
	/**
	 * 企业ID
	 */
	private Long companyId;
	/**
	 * 打分项目
	 */
	private String scoreItemName;
	/**
	 * 打分项目描述
	 */
	private String scoreItemDesc;
	/**
	 * 父级ID
	 */
	private Long scoreParentId;
	/**
	 * 配置类型：1周期考核模式；2积分制模式；
	 */
	private Integer scoreModelType;
	/**
	 * 配置状态：1已启动；0未启动
	 */
	private Integer scoreModelStatus;
	/**
	 * 开始日期 日，1号 - 28号 
	 */
	private String startTimeStr;
	/**
	 * 周期开始日期
	 */
	private Date startTime;
	/**
	 * 周期结束日期
	 */
	private Date endTime;
	/**
	 * 用户初始化低分
	 */
	private Integer initScore;
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
	 * 更新者Id
	 */
	private Long updatedBy;
	/**
	 * 删除标记 0正常 1删除
	 */
	private Integer delFlag;

}
