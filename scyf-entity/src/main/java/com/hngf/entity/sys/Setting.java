package com.hngf.entity.sys;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * 
 * @author hngf
 * @email 
 * @date 2020-05-20 17:31:55
 */
@ApiModel(value = "系统配置信息")
@Data
public class Setting implements Serializable {

	private static final long serialVersionUID = 2455415871319784197L;
	/**
	 * 系统配置ID
	 */
	@ApiModelProperty(value = "系统配置ID", position = 1)
	private Long settingId;
	/**
	 * 系统配置名称
	 */
	@ApiModelProperty(value = "系统配置名称", position = 2)
	private String settingKeyName;
	/**
	 * 系统配置key
	 */
	@ApiModelProperty(value = "系统配置key", position = 3)
	private String settingKey;
	/**
	 * 系统配置值
	 */
	@ApiModelProperty(value = "系统配置值", position = 4)
	private String settingValue;
	/**
	 * 系统配置描述
	 */
	@ApiModelProperty(value = "系统配置描述", position = 5)
	private String settingDesc;
	/**
	 * 创建人
	 */
	@ApiModelProperty(hidden = true)
	private Long createdBy;
	/**
	 * 创建时间
	 */
	@ApiModelProperty(hidden = true)
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8" )
	private Date createdTime;
	/**
	 * 修改人
	 */
	@ApiModelProperty(hidden = true)
	@JsonIgnore
	private Long updatedBy;
	/**
	 * 修改时间
	 */
	@ApiModelProperty(hidden = true)
	@JsonIgnore
	private Date updatedTime;
	/**
	 * 删除标记
	 */
	@ApiModelProperty(hidden = true)
	@JsonIgnore
	private Integer delFlag;

	public void updatePrefixInit(Long createdBy ,Date updatedTime){
		this.setUpdatedBy(createdBy);
		this.setUpdatedTime(updatedTime);
	}
}
