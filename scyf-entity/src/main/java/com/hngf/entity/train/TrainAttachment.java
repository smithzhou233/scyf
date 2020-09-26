package com.hngf.entity.train;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * 
 * @author lxf
 * @email 
 * @date 2020-08-26 14:21:58
 */
@Data
@ApiModel("培训计划/内容附件")
public class TrainAttachment implements Serializable {

	private static final long serialVersionUID = 8517477799617069283L;
	/**
	 * 培训附件ID
	 */
    @ApiModelProperty(value = "培训附件ID" , position = 1 )
    private Long trainAttachmentId;
	/**
	 * 培训内容/计划ID，附件关联id
	 */
    @ApiModelProperty(value = "培训内容ID" , position = 2 )
    private Long trainKeyId;
	/**
	 * 附件名称
	 */
    @ApiModelProperty(value = "附件名称" , position = 3 )
    private String trainAttachmentName;
	/**
	 * 附件类型
	 */
    @ApiModelProperty(value = "附件类型" , position = 4 )
    private Integer attachmentType;
	/**
	 * 扩展名
	 */
    @ApiModelProperty(value = "扩展名" , position = 5 )
    private String trainExtendName;
	/**
	 * 保存路径
	 */
    @ApiModelProperty(value = "保存路径" , position = 6 )
    private String trainSavePath;
	/**
	 * 缩略图路径
	 */
    @ApiModelProperty(value = "缩略图路径" , position = 7 )
    private String trainThumbnailUrl;
	/**
	 * 文件大小
	 */
    @ApiModelProperty(value = "文件大小" , position = 8 )
    private Integer fileSize;
	/**
	 * MIME
	 */
    @ApiModelProperty(value = "MIME" , position = 9 )
    private String mimeType;
	/**
	 * 图片宽度
	 */
    @ApiModelProperty(value = "图片宽度" , position = 10 )
    private Integer imageWidth;
	/**
	 * 图片高度
	 */
    @ApiModelProperty(value = "图片高度" , position = 11 )
    private Integer imageHeight;
	/**
	 * 创建时间
	 */
    @ApiModelProperty(value = "创建时间" , position = 12 )
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createdTime;
    private String createdTimeStr;
	/**
	 * 更新时间
	 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updatedTime;
    private String updatedTimeStr;
	/**
	 * 创建人
	 */
    @ApiModelProperty(value = "创建人" , position = 14 )
    private Long createdBy;
	/**
	 * 更新人
	 */
    private Long updatedBy;
	/**
	 * 删除标记
	 */
    private Integer delFlag;

    /**
     * 部门名称
     */
    @ApiModelProperty(value = "部门名称" )
    private String groupName;
    /**
     * 企业名称
     */
    @ApiModelProperty(value = "企业名称" )
    private String companyName;

    public TrainAttachment (Long relationId, Long createdBy, Integer attachmentType){
    	this.setTrainKeyId(relationId);
    	this.setAttachmentType(attachmentType);
    	this.setCreatedBy(createdBy);
    	this.setUpdatedBy(createdBy);
    	this.setCreatedTime(new Date());
    	this.setUpdatedTime(new Date());
    	this.setDelFlag(0);
	}
}
