package com.hngf.entity.danger;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 隐患整改验收记录
 * 
 * @author hngf
 * @email 
 * @date 2020-05-20 17:36:24
 */
@Data
@ApiModel("隐患整改验收记录model")
public class HiddenAccept implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 隐患验收ID
	 */
	
	private Long hiddenAcceptId;
	/**
	 * 所属单位
	 */
	private Long companyId;
	/**
	 * 验收单位
	 */
	@ApiModelProperty("验收单位")
	private Long groupId;
	/**
	 * 隐患ID
	 */
	@ApiModelProperty("隐患ID")
	private Long hiddenId;
	/**
	 * 验收人
	 */
	@ApiModelProperty("验收人ID")
	private Long hiddenAcceptedBy;
	/**
	 * 验收时间
	 */
	@ApiModelProperty("验收时间")
	private Date hiddenAcceptedTime;
	/**
	 * 验收结果：4通过；5不通过
	 */
	@ApiModelProperty("验收结果：4通过；5不通过")
	private Integer hiddenAcceptedResult;
	/**
	 * 验收说明
	 */
	@ApiModelProperty("验收说明")
	private String hiddenAcceptedDesc;
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
	 * 验收单位名称
	 */
	@ApiModelProperty("验收单位名称")
	private String groupName;
	/**
	 * 验收人名称
	 */
	@ApiModelProperty("验收人名称")
	private String hiddenAcceptedByName;
	/**
	 * 验收结果名称
	 */
	@ApiModelProperty("验收结果名称")
	private String hiddenAcceptedResultName;
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
	 * 附件URL，多个逗号分隔
	 */
	@ApiModelProperty("附件URL")
	private String hiddenAttachPath;

	@ApiModelProperty("签名图片地址url")
	private String signUrl;
}
