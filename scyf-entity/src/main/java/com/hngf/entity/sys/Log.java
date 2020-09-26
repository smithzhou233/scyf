package com.hngf.entity.sys;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 系统日志表
 * 
 * @author hngf
 * @email 
 * @date 2020-05-20 17:31:55
 */
@Data
@ApiModel("系统日志model")
public class Log implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 系统日志id
	 */
	@ApiModelProperty("系统日志id")
	private Long sysLogId;
	/**
	 * 系统日志类型id
	 */
	@ApiModelProperty("系统日志类型id")
	private Integer sysLogTypeId;
	/**
	 * 系统日志描述
	 */
	@ApiModelProperty("系统日志描述")
	private String sysLogDesc;
	/**
	 * 参数1
	 */
	@ApiModelProperty("参数1")
	private String sysLogParam1;
	/**
	 * 参数2
	 */
	@ApiModelProperty("参数2")
	private String sysLogParam2;
	/**
	 * 系统日志时间
	 */
	@ApiModelProperty("系统日志时间")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date sysLogTime;

	/**
	 * 请求方法名称
	 */
	@ApiModelProperty("请求方法名称")
	private String sysLogMethod;

	/**
	 * 请求Ip地址
	 */
	@ApiModelProperty("请求Ip地址")
	private String sysLogIp;
	/**
	 * 操作人ID
	 */
	@ApiModelProperty("操作人ID")
	private Long sysLogUserid;
	/**
	 * 方法执行时长(毫秒)
	 */
	@ApiModelProperty("方法执行时长(毫秒)")
	private Long sysLogExetime;

	//扩展字段
	/**
	 * 操作人名称
	 */
	@ApiModelProperty("操作人名称")
	private String sysLogUsername;
}
