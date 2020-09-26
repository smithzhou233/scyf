package com.hngf.entity.danger;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 反馈
 * 
 * @author hngf
 * @email 
 * @date 2020-05-20 17:36:24
 */
@Data
public class Feedback implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 检查记录Id
	 */
	@ApiModelProperty(value = "检查记录Id")
	private Long feedbackId;
	/**
	 * 所属企业
	 */
	@ApiModelProperty(value = "所属企业")
	private Long companyId;
	/**
	 * 所属部门
	 */
	@ApiModelProperty(value = "所属部门")
	private Long groupId;
	/**
	 * 现场数据值
	 */
	@ApiModelProperty(value = "现场数据值")
	private String sceneDataValue;
	/**
	 * 现场描述
	 */
	@ApiModelProperty(value = "现场描述")
	private String sceneRemark;
	/**
	 * 创建人，即检查人
	 */
	@ApiModelProperty(value = "创建人，即检查人")
	private Long creater;
	/**
	 * 处理结果： 0未处理；1已处理
	 */
	@ApiModelProperty(value = "处理结果： 0未处理；1已处理")
	private Integer resultValue;
	/**
	 * 处理结果描述
	 */
	@ApiModelProperty(value = "处理结果描述")
	private String resultDesc;
	/**
	 * 创建时间
	 */
	@ApiModelProperty(value = "创建时间")
	private Date createdTime;
	/**
	 * 创建人
	 */
	@ApiModelProperty(value = "创建人")
	private Long createdBy;
	/**
	 * 更新时间
	 */
	@ApiModelProperty(value = "更新时间")
	private Date updatedTime;
	/**
	 * 更新者Id
	 */
	@ApiModelProperty(value = "更新者Id")
	private Long updatedBy;
	/**
	 * 删除标记 0正常 1删除
	 */
	@ApiModelProperty(value = "删除标记 0正常 1删除")
	private Integer delFlag;

	@ApiModelProperty(value = "隐患附件")
	private List<HiddenAttach> attachList;

	//上传图片路径
	@ApiModelProperty(value = "上传图片路径")
	private String imgUrlStr;

	@ApiModelProperty(value="执行人")
	private String executorName;
	@ApiModelProperty(value = "处理人")
	private String disposeName;
	@ApiModelProperty(value = "执行部门/问题所在部门")
	private String executorGroupName;

}
