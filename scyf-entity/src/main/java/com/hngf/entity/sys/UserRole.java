package com.hngf.entity.sys;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class UserRole implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    private Long userRoleId;

    /**
     * 账号ID
     */
    private Long userId;
    /**
     * 角色ID
     */
    private Long roleId;

    /**
     * 创建人
     */
    private Long createdBy;
    /**
     * 创建时间
     */
    private Date createdTime;
    /**
     * 修改人
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
