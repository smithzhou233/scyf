package com.hngf.entity.sys;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * 
 * @author hngf
 * @email 
 * @date 2020-05-20 17:31:55
 */
@Data
public class CompanyIndustry implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 企业ID
	 */
	
	private Long companyId;
	/**
	 * 行业编码
	 */
	private Long industryId;
	/**
	 * 组织机构ID 
	 */
	private Long orgId;
	/**
	 * 创建者
	 */
	private Long createdBy;
	/**
	 * 创建时间
	 */
	private Date createdTime;
	/**
	 * 修改者
	 */
	private Long updatedBy;
	/**
	 * 修改时间
	 */
	private Date updatedTime;
	/**
	 * 删除标记
	 */
	private Integer delFlag;

}
