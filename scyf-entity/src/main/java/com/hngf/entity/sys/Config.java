package com.hngf.entity.sys;

import lombok.Data;

/**
 * @author dell
 * @since 2020/5/19 14:53
 */
@Data
public class Config {

    private Long id;
    private String paramKey;
    private String paramValue;
    private String remark;
}
