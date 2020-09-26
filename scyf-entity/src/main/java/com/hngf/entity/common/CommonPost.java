package com.hngf.entity.common;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 知识库
 * 
 * @author hngf
 * @email 
 * @date 2020-06-04 11:36:32
 */
@Data
public class CommonPost implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 主键id
	 */
	
	private Long postId;
	/**
	 * 所属公司ID
	 */
	private Long companyId;
	/**
	 * 所属单位
	 */
	private Long groupId;
	/**
	 * 分类ID
	 */
	private Long categoryId;
	/**
	 * 标题
	 */
	private String postTitle;
	/**
	 * 文章-章节-条目(article/chapter/item)5作业指导书；6安全标准化；7安全教育 ；
     * 8 应急预案；9规章制度；10岗位应知信息卡;11 两单一卡
	 */
	private String postType;
	/**
	 * 内容类型 富文本-richtext/纯文本-puretext
	 */
	private String postContentType;
	/**
	 * 作者
	 */
	private String postAuthor;
	/**
	 * 来源
	 */
	private String postSource;
	/**
	 * 描述
	 */
	private String postDesc;
	/**
	 * 图片地址
	 */
	private String postPicUrl;
	/**
	 * 详情访问地址
	 */
	private String viewUrl;
	/**
	 * 内容
	 */
	private String postContent;
	/**
	 * 是否发布
	 */
	private Integer isPublish;
	/**
	 * 发布时间
	 */
	private Date publishTime;
	private String publishTimeStr;
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
	 * 更新人
	 */
	private Long updatedBy;
	/**
	 * 删除标记
	 */
	private Integer delFlag;

	public void addPrefixInit(Long createdBy, Long companyId){
		this.setCreatedBy(createdBy);
		this.setUpdatedBy(createdBy);
		this.setCreatedTime(new Date());
		this.setUpdatedTime(new Date());
		this.setDelFlag(0);
		this.setCompanyId(companyId);
	}
}
