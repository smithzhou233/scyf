package com.hngf.entity.danger;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 整改记录日志
 * 
 * @author hngf
 * @email 
 * @date 2020-06-11 10:27:06
 */
@Data
@ApiModel("评审记录")
public class HiddenReview implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 隐患评审ID
	 */
	
	private Long hiddenReviewId;
	/**
	 * 所属单位
	 */
	private Long companyId;
	/**
	 * 评审单位
	 */
	@ApiModelProperty("评审单位")
	private Long groupId;
	/**
	 * 隐患ID
	 */
	@ApiModelProperty("隐患ID")
	private Long hiddenId;
	/**
	 * 隐患评审人
	 */
	@ApiModelProperty("隐患评审人")
	private Long hiddenReviewBy;
	/**
	 * 隐患评审时间
	 */
	@ApiModelProperty("隐患评审时间")
	private Date hiddenReviewTime;
	@ApiModelProperty("隐患评审时间")
	private String hiddenReviewTimeStr;
	/**
	 * 评审结果 0未通过；1通过
	 */
	@ApiModelProperty("评审结果 0未通过；1通过")
	private Integer hiddenReviewResult;
	/**
	 * 隐患评审说明
	 */
	@ApiModelProperty("隐患评审说明")
	private String hiddenReviewRemark;
	/**
	 * 创建者Id
	 */
	private Long createdBy;
	/**
	 * 创建时间
	 */
	private Date createdTime;
	/**
	 * 更新者Id
	 */
	private Long updatedBy;
	/**
	 * 更新时间
	 */
	private Date updatedTime;
	/**
	 * 删除：0未删除；1已删除；
	 */
	private Integer delFlag;


	//扩展字段

	/**
	 * 隐患状态
	 */
	@ApiModelProperty("隐患状态")
	private Integer status;

	/**
	 * 隐患评审人名称
	 */
	@ApiModelProperty("隐患评审人名称")
	private String hiddenReviewByName;
	/**
	 * 是否委托评审
	 */
	@ApiModelProperty("是否委托评审")
	private Integer isEntrust;
	/**
	 * 委托类型
	 */
	@ApiModelProperty("委托类型")
	private Integer entrustType;
	/**
	 * 评审结果说明
	 */
	@ApiModelProperty("评审结果说明")
	private String hiddenReviewResultStr;

	/**
	 * 评审单位
	 */
	@ApiModelProperty("评审单位")
	private Long hiddenReviewGroup;
	/**
	 * 评审单位名称
	 */
	@ApiModelProperty("评审单位名称")
	private String hiddenReviewGroupName;

	/**
	 * 隐患整改方案表ID
	 */
	@ApiModelProperty("隐患整改方案表ID")
	private Long programmeId;

	/**
	 * 验收单位
	 */
	@ApiModelProperty("验收单位")
	private Long hiddenAcceptedGroup;
	/**
	 * 验收人
	 */
	@ApiModelProperty("验收人")
	private Long hiddenAcceptedBy;

	/**
	 * 整改部门
	 */
	@ApiModelProperty("整改部门")
	private Long hiddenRetifyGroup;
	/**
	 * 整改人
	 */
	@ApiModelProperty("整改人")
	private Long hiddenRetifyBy;

	/**
	 * 整改期限
	 */
	@ApiModelProperty("整改期限")
	private Date hiddenRetifyDeadline;


	/**
	 * 隐患等级：1重大，2较大，3一般，4较低；
	 */
	@ApiModelProperty("隐患等级")
	private Integer hiddenLevel;

	/**
	 * 委托评审部门
	 */
	@ApiModelProperty("委托评审部门")
	private Long hiddenEntrustGroup;
	/**
	 * 委托评审人
	 */
	@ApiModelProperty("委托评审人")
	private Long hiddenEntrustBy;

	@ApiModelProperty("签名图片地址url")
	private String signUrl;
}
