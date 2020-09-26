package com.hngf.entity.sys;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 消息表
 * 
 * @author hngf
 * @email 
 * @date 2020-06-11 14:03:07
 */
@Data
@ApiModel("消息model")
public class Message implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 通知Id
	 */
	@ApiModelProperty("通知Id")
	private Long msgId;
	/**
	 * 主题
	 */
	@ApiModelProperty("主题")
	private String msgTitle;
	/**
	 * 1消息；2通知；3回复信息；4极光推送信息
	 */
	@ApiModelProperty("1消息；2通知；3回复信息；4极光推送信息")
	private Integer msgType;
	/**
	 * 收件部门
	 */
	@ApiModelProperty("收件部门")
	private Long addresseeGroupId;
	/**
	 * 收件人员
	 */
	@ApiModelProperty("收件人员")
	private Long addresseeId;
	/**
	 * 发件部门
	 */
	@ApiModelProperty("发件部门")
	private Long addresserGroupId;
	/**
	 * 发件人员
	 */
	@ApiModelProperty("发件人员")
	private Long addresserId;
	/**
	 * 发件时间
	 */
	@ApiModelProperty("发件时间")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "GMT+8")
	private Date addresseeTime;
	/**
	 * 过期时间
	 */
	@ApiModelProperty("过期时间")
	private Date expiredTime;
	/**
	 * 内容
	 */
	@ApiModelProperty("内容")
	private String msgContent;
	/**
	 * 0未读；1已读；
	 */
	@ApiModelProperty(" 0未读；1已读；")
	private Integer msgStatus;
	/**
	 * 1 推送 0 未推送
	 */
	@ApiModelProperty("1 推送 0 未推送")
	private Integer ifPush;
	/**
	 * 1 极光推送；2系统推送；3消息发送/接收
	 */
	@ApiModelProperty("1 极光推送；2系统推送；3消息发送/接收")
	private Integer pushType;
	/**
	 * 消息所属单位
	 */
	@ApiModelProperty("消息所属单位")
	private Long companyId;
	/**
	 * 记录ID
	 */
	@ApiModelProperty("记录ID")
	private Long msgRecordId;
	/**
	 * 父级ID
	 */
	@ApiModelProperty("父级ID")
	private Long msgParentId;
	/**
	 * 添加时间
	 */
	private Date createdTime;
	/**
	 * 创建人
	 */
	private Long createdBy;
	/**
	 * 更新时间
	 */
	private Date updatedTime;
	/**
	 * 修改人
	 */
	private Long updatedBy;
	/**
	 * 删除标志  0 正常 1删除
	 */
	private Integer delFlag;

	//扩展字段
	/**
	 * 发件人员名称
	 */
	@ApiModelProperty("发件人员名称")
	private String addresserName;
}
