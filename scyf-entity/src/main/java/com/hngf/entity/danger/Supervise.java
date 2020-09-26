package com.hngf.entity.danger;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 督察记录表
 * 
 * @author hngf
 * @email 
 * @date 2020-08-18 10:38:26
 */
@Data
public class Supervise implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 督导id
	 */
	@ApiModelProperty("主键ID")
	private Long supId;
	/**
	 * 群组id
	 */
	@ApiModelProperty(" 群组id")
	private Long groupId;
	/**
	 * 公司Id
	 */
	@ApiModelProperty(" 公司Id")
	private Long companyId;
	/**
	 * 督导人Id
	 */
	@ApiModelProperty(" 督导人Id")
	private Long superviserId;
	/**
	 * 督导类型  1.隐患  2.任务
	 */
	@ApiModelProperty(" 群组id")
	private Integer superviseType;
	/**
	 * 对应表Id
	 */
	@ApiModelProperty(" 对应表Id")
	private Integer detailId;
	/**
	 * 消息接收人
	 */
	@ApiModelProperty(" 群组id")
	private Long reciverId;
	/**
	 * 创建人
	 */
	@ApiModelProperty(" 创建人")
	private Long createdBy;
	/**
	 * 创建时间
	 */
	@ApiModelProperty(" 创建时间")
	private Date createdTime;
	/**
	 * 删除：0未删除；1已删除；
	 */
	private Integer delFlag;

}
