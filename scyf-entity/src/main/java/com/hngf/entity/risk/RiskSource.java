package com.hngf.entity.risk;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 危险源
 * 
 * @author zhangfei
 * @email 
 * @date 2020-05-20 17:36:23
 */
@Data
@ApiModel("危险源model")
public class RiskSource implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * ID
	 */
	
	private Long riskDangerId;
	/**
	 * 所属单位
	 */
	@ApiModelProperty("所属单位")
	private Long companyId;
	/**
	 * 父级危险源ID
	 */
	@ApiModelProperty("父级危险源ID")
	private Long parentRiskDangerId;
	/**
	 * 危险源类型（1 设备设施 2 作业活动）
	 */
	@ApiModelProperty("危险源类型（1 设备设施 2 作业活动 3 作业环境 4.设施场所）")
	private Integer riskDangerType;
	/**
	 * 危险源名称
	 */
	@ApiModelProperty("危险源名称")
	private String riskDangerName;
	/**
	 * 风险源等级（1、红色；2、橙色；3、黄色；4、蓝色）
	 */
	@ApiModelProperty("风险源等级（1、红色；2、橙色；3、黄色；4、蓝色）")
	private Integer riskDangerLevel;
	/**
	 * 危险源编码
	 */
	@ApiModelProperty("危险源编码")
	private String riskDangerCode;
	/**
	 * 危险源图标
	 */
	@ApiModelProperty("危险源图标")
	private String riskDangerImg;
	/**
	 * 0：正常；1：锁定
	 */
	@ApiModelProperty("0：正常；1：锁定")
	private Integer riskDangerStatus;
	/**
	 * 行业ID-外键
	 */
	@ApiModelProperty("行业ID-外键")
	private Long industryId;
	/**
	 * 0固定 1移动
	 */
	@ApiModelProperty("0固定 1移动")
	private Integer isFixed;
	/**
	 * 是否叶子节点 1 叶子节点
	 */
	@ApiModelProperty("是否叶子节点 1 叶子节点")
	private Integer isLeaf;
	/**
	 * 节点左侧索引
	 */
	@ApiModelProperty("节点左侧索引")
	private Integer nodeLeft;
	/**
	 * 节点右侧索引
	 */
	@ApiModelProperty("节点右侧索引")
	private Integer nodeRight;
	/**
	 * 节点层级
	 */
	@ApiModelProperty("节点层级")
	private Integer nodeLevel;
	/**
	 * 根节点
	 */
	@ApiModelProperty("根节点")
	private Long rootNode;
	/**
	 * 添加时间
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
	 * 修改人
	 */
	private Long updatedBy;
	/**
	 * 删除标志  0 正常 1删除
	 */
	private Integer delFlag;

	//扩展字段
	/**
	 * 风险源等级说明
	 */
	@ApiModelProperty("风险源等级说明")
	private String riskDangerLevelName;

}
