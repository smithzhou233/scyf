package com.hngf.entity.sys;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.Date;

/**
 * 手机APP位置坐标
 * 
 * @author hngf
 * @email 
 * @date 2020-07-09 14:03:19
 */
@Data
@ApiModel("手机APP位置坐标model")
public class UserSite implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@ApiModelProperty("主键")
	private Long siteId;
	/**
	 * 用户ID
	 */
	@ApiModelProperty("用户ID")
	private Long userId;
	/**
	 * 公司ID
	 */
	@ApiModelProperty("公司ID")
	private Long companyId;
	/**
	 * 部组ID
	 */
	@ApiModelProperty("部组ID")
	private Long groupId;
	/**
	 * 经度
	 */
	@ApiModelProperty("经度")
	@NotBlank(message = "经度不能为空")
	private String longitude;
	/**
	 * 纬度
	 */
	@ApiModelProperty("纬度")
	@NotBlank(message = "纬度不能为空")
	private String latitude;
	/**
	 * 手机型号
	 */
	@ApiModelProperty("手机型号")
	private String phoneCode;
	/**
	 * 定位时间
	 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@ApiModelProperty("定位时间")
	private Date siteTime;
	/**
	 * 创建时间
	 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@ApiModelProperty("创建时间")
	private Date createdTime;

}
