package com.hngf.dto.sys;

import lombok.Data;

import java.util.List;


@Data
public class OrgDto implements java.io.Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 组织机构ID
     */

    private Long orgId;
    /**
     * 组织机构名称
     */
    private String orgName;
    /**
     * 组织机构代码
     */
    private String orgCode;
    /**
     * 父级组织机构ID
     */
    private Long orgParentId;
    /**
     * 类型0组织1集团公司
     */
    private Integer orgTypeId;
    /**
     * 层级
     */
    private Integer orgLevel;
    /**
     * 区域代码
     */
    private String orgAreaCode;
    /**
     * 区域名称
     */
    private String orgAreaName;
    /**
     * 新组织ID,位编码
     */
    private Long orgGroupId;
    /**
     * 管理员账号
     */
    private Long orgAdminId;
    /**
     * 顶级组织机构代码
     */
    private Long orgRootId;

    private List<OrgDto> children;
}
