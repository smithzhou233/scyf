package com.hngf.entity.common;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 业务通用附件表
 * 
 * @author hngf
 * @email 
 * @date 2020-06-13 15:24:30
 */
@Data
public class CommonAttachment implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 附件ID
	 */
	
	private Long attachmentId;
	/**
	 * 关联ID
	 */
	private Long ownerId;
	/**
	 * 关联类型0知识库1检查项2应急物资,3项目部
	 */
	private Integer ownerType;
	/**
	 * 附件名称
	 */
	private String attachmentName;
	/**
	 * 扩展名
	 */
	private String extendName;
	/**
	 * 保存路径
	 */
	private String savePath;
	/**
	 * 缩略图路径
	 */
	private String thumbnailUrl;
	/**
	 * 文件大小
	 */
	private Integer fileSize;
	/**
	 * MIME
	 */
	private String mimeType;
	/**
	 * 图片宽度
	 */
	private Integer imageWidth;
	/**
	 * 图片高度
	 */
	private Integer imageHeight;
	/**
	 * 创建时间
	 */
	private Date createdTime;
	/**
	 * 更新时间
	 */
	private Date updatedTime;
	/**
	 * 创建人
	 */
	private Long createdBy;
	/**
	 * 更新人
	 */
	private Long updatedBy;
	/**
	 * 删除标记
	 */
	private Integer delFlag;

	public void addPrefixInit(Long createdBy){
		this.setCreatedBy(createdBy);
		this.setUpdatedBy(createdBy);
		this.setCreatedTime(new Date());
		this.setUpdatedTime(new Date());
		this.setDelFlag(0);
	}

}
