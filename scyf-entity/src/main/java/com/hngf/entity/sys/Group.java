package com.hngf.entity.sys;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 群组表
 * 
 * @author hngf
 * @email 
 * @date 2020-05-20 17:31:55
 */
@JsonIgnoreProperties({"delFlag"})
@Data
@ApiModel(value = "群组、组织", description = "群组信息、企业的组织")
public class Group implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 群组id
	 */
	@ApiModelProperty(name = "groupId", value = "群组id", dataType = "long", position = 1)
	private Long groupId;
	/**
	 * 企业id
	 */
	@ApiModelProperty(hidden = true)
	private Long companyId;
	/**
	 * 群组名称
	 */
	@ApiModelProperty(name = "groupId", value = "群组id", dataType = "long", position = 1)
	@NotBlank(message = "群组名称不能为空")
	private String groupName;
	/**
	 * 群组类型id
	 */
	private Long groupTypeId;
	/**
	 * 左
	 */
	private Integer groupLeft;
	/**
	 * 右
	 */
	private Integer groupRight;
	/**
	 * 群组父级id
	 */
	@NotNull(message = "群组父级ID不能为空")
	private Long groupParent;
	/**
	 * 
	 */
	private Integer groupLevel;
	/**
	 * 
	 */
	private Integer groupLeaf;
	/**
	 * 群组路径
	 */
	private String groupPath;
	/**
	 * 群组描述
	 */
	private String groupDesc;
	/**
	 * 群组状态
	 */
	private Integer groupStatus;
	/**
	 * 
	 */
	private Integer groupFixed;
	/**
	 * 群组代码
	 */
	private String groupCode;
	/**
	 * 
	 */
	private Long groupOrder;
	/**
	 * 群组排序
	 */
	private String groupSort;
	/**
	 * 拼音及首字母搜索
	 */
	private String groupSelect;
	/**
	 * 创建人
	 */
	@ApiModelProperty(hidden = true)
	private Long createdBy;
	/**
	 * 创建时间
	 */
	@ApiModelProperty(hidden = true)
	private Date createdTime;
	/**
	 * 更新人
	 */
	@ApiModelProperty(hidden = true)
	private Long updatedBy;
	/**
	 * 更新时间
	 */
	@ApiModelProperty(hidden = true)
	private Date updatedTime;
	/**
	 * 删除标记
	 */
	@ApiModelProperty(hidden = true)
	private Integer delFlag;
	/**
	 * 经度
	 */
	private BigDecimal longitude;
	/**
	 * 纬度
	 */
	private  BigDecimal latitude;

	//扩展字段
	private String groupTypeTitle;
	private String groupParentName;

	public void insertPrefix(Long userId){
		this.setCreatedBy(userId );
		this.setCreatedTime(new Date());
		this.setUpdatedBy(userId );
		this.setUpdatedTime(new Date());
		this.setDelFlag(0);
	}

	@Override
	public String toString() {
		return "Group{" +
				"groupId=" + groupId +
				", companyId=" + companyId +
				", groupName='" + groupName + '\'' +
				", groupTypeId=" + groupTypeId +
				", groupLeft=" + groupLeft +
				", groupRight=" + groupRight +
				", groupParent=" + groupParent +
				", groupLevel=" + groupLevel +
				", groupLeaf=" + groupLeaf +
				", groupPath='" + groupPath + '\'' +
				", groupDesc='" + groupDesc + '\'' +
				", groupStatus=" + groupStatus +
				", groupFixed=" + groupFixed +
				", groupCode='" + groupCode + '\'' +
				", groupOrder=" + groupOrder +
				", groupSort='" + groupSort + '\'' +
				", groupSelect='" + groupSelect + '\'' +
				", createdBy=" + createdBy +
				", createdTime=" + createdTime +
				", updatedBy=" + updatedBy +
				", updatedTime=" + updatedTime +
				", delFlag=" + delFlag +
				'}';
	}
}
