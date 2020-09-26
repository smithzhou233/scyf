package com.hngf.entity.sys;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 组织机构和行业映射
 * 
 * @author hngf
 * @email 
 * @date 2020-05-20 17:31:55
 */
@Data
public class OrgIndustry  extends  Industry  implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 主键ID
	 */
	
	private Long orgIndustryId;
	/**
	 * 组织ID
	 */
	private Long orgId;
	/**
	 * 部门ID
	 */
	private Long groupId;
	/**
	 * 行业编码
	 */
	private Long industryId;
	/**
	 * 关联类型0组织1部门/创建人
	 */
	private Long createdBy;
	/**
	 * 创建时间
	 */
	private Date createdTime;
	/**
	 * 更新人
	 */
	private Long updatedBy;
	/**
	 * 更新时间
	 */
	private Date updatedTime;
	/**
	 * 删除标识
	 */
	private Integer delFlag;

	private String orgName;
	private String industryName;
	private String groupName;
	private String industryCode;

}
