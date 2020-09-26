package com.hngf.entity.sys;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 行业领域表
 * 
 * @author hngf
 * @email 
 * @date 2020-06-03 10:47:12
 */
@ApiModel(value = "行业信息")
@Data
public class Industry implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 行业领域ID
	 */
	@ApiModelProperty(value = "行业领域ID", position = 1)
	private Long industryId;
	/**
	 * 行业领域编码
	 */
	@ApiModelProperty(value = "行业领域编码", position = 2)
	@NotNull(message = "行业编码不能为空")
	@Length(message = "行业编码长度范围1-32", min=1, max=32)
	private String industryCode;
	@ApiModelProperty(hidden = true)
	private String childIndustryCodes;
	/**
	 * 行业领域名称
	 */
	@ApiModelProperty(value = "行业领域名称", position = 3)
	@NotNull(message = "行业名称不能为空")
	@Length(message = "行业名称长度范围1-100", min=1, max=100)
	private String industryName;
	/**
	 * 
	 */
	@ApiModelProperty(value = "行业领域父级行业Id", position = 5)
	private Long industryParentId;
	/**
	 * 父级编码
	 */
	@ApiModelProperty(value = "行业领域父级编码", position = 6)
	private String industryParentCode;
	/**
	 * 行业；子行业；领域；子领域。
	 */
	@ApiModelProperty(value = "行业领域类型", position = 7)
	private String industryType;
	/**
	 * 行业领域图标地址
	 */
	@ApiModelProperty(value = "行业领域图标地址", position = 8)
	private String industryIconUrl;
	/**
	 * 
	 */
	@ApiModelProperty(value = "行业领域排序序号", position = 9)
	private Integer sortNo;
	/**
	 * 创建时间
	 */
	@JsonIgnore
	private Date createdTime;
	/**
	 * 创建人
	 */
	@JsonIgnore
	private Long createdBy;
	/**
	 * 更新时间
	 */
	@ApiModelProperty(hidden = true)
	private Date updatedTime;
	/**
	 * 更新人
	 */
	@ApiModelProperty(hidden = true)
	private Long updatedBy;
	/**
	 * 删除标记
	 */
	@ApiModelProperty(hidden = true)
	private Integer delFlag;
	/**
	 * 存放子级数据
	 */
   private List<Industry> children;
	/**
	 * 树形名称
	 */
	private String label;

	@Override
	public String toString() {
		return "Industry{" +
				"industryId=" + industryId +
				", industryCode='" + industryCode + '\'' +
				", industryName='" + industryName + '\'' +
				", industryParentId=" + industryParentId +
				", industryParentCode='" + industryParentCode + '\'' +
				", industryType='" + industryType + '\'' +
				", industryIconUrl='" + industryIconUrl + '\'' +
				", sortNo=" + sortNo +
				", createdTime=" + createdTime +
				", createdBy=" + createdBy +
				", updatedTime=" + updatedTime +
				", updatedBy=" + updatedBy +
				", delFlag=" + delFlag +
				'}';
	}
}
