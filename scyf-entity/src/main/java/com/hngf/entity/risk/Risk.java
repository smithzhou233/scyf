package com.hngf.entity.risk;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 风险定义表
 * 
 * @author hngf
 * @email 
 * @date 2020-05-20 17:36:24
 */
@Data
public class Risk implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 风险ID
	 */
	@ApiModelProperty(value = "风险ID")
	private Long riskId;
	/**
	 * 所属单位id
	 */
	@ApiModelProperty(value = "所属单位id")
	private Long companyId;
	/**
	 * 危险源ID
	 */
	@ApiModelProperty(value = "危险源ID")
	private Long riskDangerId;
	/**
	 * 风险名称
	 */
	@NotBlank(message = "风险名称不能为空")
	@ApiModelProperty(value = "风险名称",required=true)
	private String riskName;
	/**
	 * 风险编码
	 */
	@ApiModelProperty(value = "风险编码")
	private String riskCode;
	/**
	 * 危害因素
	 */
	@ApiModelProperty(value = "危害因素")
	private String riskHramFactor;
	/**
	 * 危害产生的后果
	 */
	@ApiModelProperty(value = "危害产生的后果")
	private String riskConsequence;
	/**
	 * 风险类型（1、常规风险；2、专业性风险；）
	 */
	@ApiModelProperty(value = "风险类型（1、常规风险；2、专业性风险；）")
	private Long riskTypeId;

	/**
	 * 风险类型
	 */
	@ApiModelProperty()
	private String riskType;
	/**
	 * 风险描述
	 */
	@ApiModelProperty(value = "风险描述")
	private String riskDesc;
	/**
	 * 风险等级（1、红色；2、橙色；3、黄色；4、蓝色）
	 */
	@ApiModelProperty(value = "风险等级（1、红色；2、橙色；3、黄色；4、蓝色）")
	private Integer riskLevel;
	/**
	 * 风险考核分数
	 */
	@ApiModelProperty(value = "风险考核分数")
	private Long riskScore;
	/**
	 * 可能性大小L
	 */
	private BigDecimal lecL;
	/**
	 * 暴露频繁程度E
	 */
	private BigDecimal lecE;
	/**
	 * 结果严重程度C
	 */
	private BigDecimal lecC;
	/**
	 * 风险值D
	 */
	private BigDecimal lecD;
	/**
	 * 可能性大小L
	 */
	private BigDecimal lcL;
	/**
	 * 结果严重程度C
	 */
	private BigDecimal lcC;
	/**
	 * 风险值D
	 */
	private BigDecimal lcD;
	/**
	 * 可能性大小L
	 */
	private BigDecimal lsL;
	/**
	 * 结果严重程度S
	 */
	private BigDecimal lsS;
	/**
	 * 风险值R
	 */
	private BigDecimal lsR;
	/**
	 * 风险类型:0 lec,1lc,2ls,从数据库字典表取
	 */
	private Integer typeId;
	/**
	 * 责任部门(数据展示不做关联)
	 */
	private String groupName;
	/**
	 * 责任人(数据展示不做关联)
	 */
	private String groupUser;
	/**
	 * 添加时间
	 */
	@ApiModelProperty(value = "添加时间")
	private Date createdTime;
	/**
	 * 创建人
	 */
	@ApiModelProperty(value = "创建人")
	private Long createdBy;
	/**
	 * 更新时间
	 */
	@ApiModelProperty(value = "更新时间")
	private Date updatedTime;
	/**
	 * 更新人
	 */
	@ApiModelProperty(value = "更新人")
	private Long updatedBy;
	/**
	 * 删除标记0正常1删除
	 */
	@ApiModelProperty(value = "删除标记0正常1删除")
	private Integer delFlag;

	//扩展字段
	/**
	 * 危险源名称
	 */
	@ApiModelProperty(value = "危险源名称")
	private String riskDangerName;

	/**
	 * 线型特征
	 */
	@ApiModelProperty(value = "线型特征")
	private String a1;

	/**
	 * 线位特征
	 */
	@ApiModelProperty(value = "线位特征")
	private String a2;

	/**
	 * 节点特征
	 */
	@ApiModelProperty(value = "节点特征")
	private String a3;

	/**
	 * 气象条件
	 */
	@ApiModelProperty(value = "气象条件")
	private String a4;

	/**
	 * 地质条件
	 */
	@ApiModelProperty(value = "地质条件")
	private String a5;

	/**
	 * 区位特征
	 */
	@ApiModelProperty(value = "区位特征")
	private String a6;

	/**
	 * 交通运行特征
	 */
	@ApiModelProperty(value = "交通运行特征")
	private String a7;
	/**
	 * 交安设施技术状况
	 */
	@ApiModelProperty(value = "交安设施技术状况")
	private String a8;

	/**
	 * A评价总分
	 */
	@ApiModelProperty(value = "A评价总分")
	private String ta;
}
