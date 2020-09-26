package com.hngf.dto.sys;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@ApiModel("机构Tree信息")
@Data
public class OrgTreeDto implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 组织机构ID
     */
    @ApiModelProperty(value = "组织机构ID")
    private Long orgId;
    /**
     * 组织机构名称
     */
    @ApiModelProperty(value = "组织机构名称")
    @NotBlank(message = "组织机构名称不能为空")
    private String orgName;
    /**
     * 组织机构代码
     */
    @ApiModelProperty(value = "组织机构代码")
    private String orgCode;
    /**
     * 父级组织机构ID
     */
    @ApiModelProperty(value = "父级组织机构ID")
    private Long orgParentId;
    /**
     * 类型0组织1集团公司
     */
    @ApiModelProperty(value = "类型0组织1集团公司")
    private Integer orgTypeId;
    /**
     * 左
     */
    @ApiModelProperty(hidden = true)
    private Integer orgLeft;
    /**
     * 右
     */
    @ApiModelProperty(hidden = true)
    private Integer orgRight;
    /**
     * 层级
     */
    @ApiModelProperty(hidden = true)
    private Integer orgLevel;
    /**
     * 区域代码
     */
    @ApiModelProperty(hidden = true)
    private String orgAreaCode;
    /**
     * 区域名称
     */
    @ApiModelProperty(hidden = true)
    private String orgAreaName;
    /**
     * 新组织ID,位编码
     */
    @ApiModelProperty(value = "组织机构部门Id")
    private Long orgGroupId;
    /**
     * 管理员账号
     */
    @ApiModelProperty(value = "管理员账号,userId")
    private Long orgAdminId;
    /**
     * 顶级组织机构代码
     */
    @ApiModelProperty(value = "顶级组织机构代码")
    private Long orgRootId;
    /**
     * 创建时间
     */
    @ApiModelProperty(hidden = true)
    @JsonIgnore
    private Date createdTime;
    /**
     * 创建者
     */
    @ApiModelProperty(hidden = true)
    @JsonIgnore
    private Long createdBy;
    /**
     * 更新时间
     */
    @ApiModelProperty(hidden = true)
    @JsonIgnore
    private Date updatedTime;
    /**
     * 更新者
     */
    @ApiModelProperty(hidden = true)
    @JsonIgnore
    private Long updatedBy;
    /**
     * 删除标识
     */
    @ApiModelProperty(hidden = true)
    @JsonIgnore
    private Integer delFlag;
    /**
     * 机构类型
     */
    @ApiModelProperty(hidden = true)
    private String dictName;
    /**
     * 登录名称
     */
    @ApiModelProperty(hidden = true)
    private String loginName;
    /**
     * 用户名称
     */
    @ApiModelProperty(hidden = true)
    private String userName;
    /**
     * 用户电话
     */
    @ApiModelProperty(hidden = true)
    private String userMobile;
    /**
     * 行业id集合
     */
    @ApiModelProperty(hidden = true)
    private String industryIdList;
    /**
     * 行业名字集合
     */
    @ApiModelProperty(hidden = true)
    private String industryNameList;

    @ApiModelProperty(value = "子机构信息")
    private List<OrgTreeDto> children;
}
