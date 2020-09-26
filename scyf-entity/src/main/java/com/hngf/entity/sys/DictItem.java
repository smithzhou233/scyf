package com.hngf.entity.sys;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 字典项表
 * 
 * @author hngf
 * @email 
 * @date 2020-05-20 17:31:55
 */
@Data
public class DictItem implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 项目ID
	 */
	
	private String itemId;
	/**
	 * 名称
	 */
	private String itemName;
	/**
	 * 项目值
	 */
	private String itemValue;
	/**
	 * 项目图标
	 */
	private String itemImage;
	/**
	 * 字典ID
	 */
	private String dictId;
	/**
	 * 类型ID 0系统1用户自定义
	 */
	private Integer typeId;
	/**
	 * 自定义数据所属企业
	 */
	private String ownerId;
	/**
	 * 排序
	 */
	private Long sortNo;
	/**
	 * 父ID
	 */
	private String parentId;
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

}
