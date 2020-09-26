package com.hngf.dto.sys;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户token信息操作
 */
@Data
public class UserTokenDto implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     *
     */

    private Long userId;
    /**
     * token
     */
    private String apiToken;
    /**
     * 过期时间
     */
    private Date apiExpireTime;
    /**
     * 更新时间
     */
    private Date apiUpdateTime;
}
