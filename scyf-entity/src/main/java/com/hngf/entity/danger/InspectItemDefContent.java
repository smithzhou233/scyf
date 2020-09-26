package com.hngf.entity.danger;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 检查项内容
 * 
 * @author hngf
 * @email 
 * @date 2020-05-20 17:36:24
 */
@Data
public class InspectItemDefContent implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 检查项内容ID
	 */
	
	private Long inspectItemDefContentId;
	/**
	 * 检查项ID
	 */
	private Long inspectItemDefId;
	/**
	 * 所属企业
	 */
	private Long companyId;
	/**
	 * 检查项条目名称
	 */
	private String inspectItemDefContentName;
	/**
	 * 内容
	 */
	private String inspectItemDefContentDesc;
	/**
	 * 创建人
	 */
	private Long createdBy;
	/**
	 * 新增时间
	 */
	private Date createdTime;
	/**
	 * 修改人
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
