package com.hngf.entity.sys;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class BackupLog implements Serializable {
    private static final long serialVersionUID = 1L;
    @ApiModelProperty("备份日志ID")
    private Long buId;
    @ApiModelProperty("备份时间")
    private Date buTime;

    @ApiModelProperty("备份文件地址")
    private String buUrl;

}
