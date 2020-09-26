package com.hngf.entity.danger;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 隐患附件表
 * 
 * @author hngf
 * @email 
 * @date 2020-05-20 17:36:24
 */
@Data
@ApiModel("隐患附件表model")
public class HiddenAttach implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 隐患附件Id
	 */
	@ApiModelProperty("隐患附件Id")
	private Long hiddenAttachId;
	/**
	 * 所属单位
	 */
	@ApiModelProperty("所属单位")
	private Long companyId;
	/**
	 * 1、隐患；2、验收
	 */
	@ApiModelProperty("1、隐患；2、验收")
	private Integer hiddenDetailType;
	/**
	 * 对应表Id
	 */
	@ApiModelProperty("对应表Id")
	private Long hiddenDetailId;
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

}
