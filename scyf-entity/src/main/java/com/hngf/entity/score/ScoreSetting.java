package com.hngf.entity.score;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 绩效考核打分规则配置表
 * 
 * @author hngf
 * @email 
 * @date 2020-05-27 10:11:53
 */
@Data
public class ScoreSetting implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 绩效考核得分配置ID
	 */
	@ApiModelProperty("绩效考核得分配置ID")
	private Long scoreSettingId;
	/**
	 * 企业ID
	 */
	@ApiModelProperty("企业ID")
	private Long companyId;
	/**
	 * 打分项目名称
	 */
	@ApiModelProperty("绩效考核得分配置ID")
	private String scoreItemName;
	/**
	 * 打分项目描述
	 */
	@ApiModelProperty("打分项目描述")
	private String scoreItemDesc;
	/**
	 * 配置编号：1任务逾期；2隐患超时整改；（可拓展）
	 */
	@ApiModelProperty("配置编号：1任务逾期；2隐患超时整改；（可拓展）")
	private Integer settingCode;
	/**
	 * 打分项目的得分配置，默认为1分；
	 */
	@ApiModelProperty("打分项目的得分配置，默认为1分；")
	private Integer settingScore;
	/**
	 * 配置类型：1加分计算法；2扣分计算法；
	 */
	@ApiModelProperty("配置类型：1加分计算法；2扣分计算法；")
	private Integer settingType;
	/**
	 * 配置状态：1已启动；0未启动
	 */
	@ApiModelProperty("配置状态：1已启动；0未启动")
	private Integer settingStatus;
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
