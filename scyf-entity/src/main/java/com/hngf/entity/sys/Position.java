package com.hngf.entity.sys;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.Date;

/**
 * 岗位表
 * 
 * @author hngf
 * @email 
 * @date 2020-05-20 17:31:55
 */
@ApiModel(value = "岗位信息")
@JsonIgnoreProperties({"delFlag"})
@Data
public class Position implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 主键
	 */
	@ApiModelProperty(value = "岗位Id", position = 1)
	private Long positionId;
	/**
	 * 企业id
	 */
	@ApiModelProperty(value = "岗位所属企业Id", position = 2)
	private Long companyId;
	/**
	 * 岗位名称
	 */
	@ApiModelProperty(value = "岗位名称", position = 3)
	@NotBlank(message = "岗位名称不能为空")
	@Length(message = "岗位名称长度范围1-200",min=1, max = 200)
	private String positionTitle;
	/**
	 * 岗位描述
	 */
	@ApiModelProperty(value = "岗位描述", position = 5)
	private String positionDesc;
	/**
	 * 岗位序号
	 */
	@ApiModelProperty(value = "岗位序号", position = 6)
	@Length(message = "岗位序号长度范围0-8", max = 8)
	private String positionOrder;
	/**
	 * 
	 */
	@ApiModelProperty(hidden = true )
	@Max(value = 9999,message = "最大值不能超过9999")
	private Integer positionFixed;
	/**
	 * 创建人
	 */
	@ApiModelProperty(hidden = true )
	private Long createdBy;
	/**
	 * 创建时间
	 */
	@ApiModelProperty(hidden = true )
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8" )
	private Date createdTime;
	/**
	 * 更新人
	 */
	@ApiModelProperty(hidden = true )
    @JsonIgnore
	private Long updatedBy;
	/**
	 * 更新时间
	 */
	@ApiModelProperty(hidden = true )
    @JsonIgnore
	private Date updatedTime;
	/**
	 * 删除标记0正常 1删除
	 */
	@ApiModelProperty(hidden = true )
    @JsonIgnore
	private Integer delFlag;

	@Override
	public String toString() {
		return "Position{" +
				"positionId=" + positionId +
				", companyId=" + companyId +
				", positionTitle='" + positionTitle + '\'' +
				", positionDesc='" + positionDesc + '\'' +
				", positionOrder='" + positionOrder + '\'' +
				", positionFixed=" + positionFixed +
				", createdBy=" + createdBy +
				", createdTime=" + createdTime +
				", updatedBy=" + updatedBy +
				", updatedTime=" + updatedTime +
				", delFlag=" + delFlag +
				'}';
	}
}
