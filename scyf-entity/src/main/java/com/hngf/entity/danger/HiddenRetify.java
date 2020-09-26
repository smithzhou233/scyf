package com.hngf.entity.danger;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 隐患整改记录
 * 
 * @author hngf
 * @email 
 * @date 2020-05-20 17:36:24
 */
@Data
@ApiModel("隐患整改记录model")
public class HiddenRetify implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 主键
	 */
	
	private Long hiddenRetifyId;
	/**
	 * 公司ID
	 */
	private Long companyId;
	/**
	 * 隐患id
	 */
	private Long hiddenId;
	/**
	 * 原因分析
	 */
	@ApiModelProperty("原因分析")
	private String hiddenRetifyReasons;
	/**
	 * 整改措施
	 */
	@ApiModelProperty("整改措施")
	private String hiddenRetifyMeasures;
	/**
	 * 整改单位
	 */
	@ApiModelProperty("整改单位")
	private Long hiddenRetifyGroup;
	/**
	 * 整改人
	 */
	@ApiModelProperty("整改人")
	private Long hiddenRetifyBy;
	/**
	 * 隐患原来整改人
	 */
	@ApiModelProperty("隐患原来整改人")
	private Long hiddenOldRetifyBy;
	/**
	 * 整改类型，0、自行整改，1、协助整改,2、委托整改
	 */
	@ApiModelProperty("整改类型")
	private Integer hiddenRetifyType;
	/**
	 * 整改协助单位
	 */
	@ApiModelProperty("整改协助单位")
	private String hiddenRetifyAssisting;
	/**
	 * 整改花费
	 */
	@ApiModelProperty("整改花费")
	private Double hiddenRetifyAmount;
	/**
	 * 整改期限
	 */
	@ApiModelProperty("整改期限")
	private Date hiddenRetifyDeadline;
	@ApiModelProperty("整改期限")
	private String hiddenRetifyDeadlineStr;
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
	 * 删除标记
	 */
	private Integer delFlag;

	//扩展字段
	/**
	 * 整改附件地址，多个逗号分隔
	 */
	@ApiModelProperty("整改附件地址，多个逗号分隔")
	private String retifyUploadPath;
	/**
	 * 整改单位名称
	 */
	@ApiModelProperty("整改单位名称")
	private String hiddenRetifyGroupName;
	/**
	 * 整改类型名称
	 */
	@ApiModelProperty("整改类型名称")
	private String hiddenRetifyTypeName;
	/**
	 * 整改人名称
	 */
	@ApiModelProperty("整改人名称")
	private String hiddenRetifyByName;
	/**
	 * 隐患附件Id
	 */
	@ApiModelProperty("隐患附件Id")
	private Long hiddenAttachId;
	/**
	 * 附件类型image、video
	 */
	@ApiModelProperty("附件类型image、video")
	private String hiddenAttachType;
	/**
	 * 附件URL
	 */
	@ApiModelProperty("附件URL")
	private String hiddenAttachPath;

	@ApiModelProperty("签名图片地址url")
	private String signUrl;
}
