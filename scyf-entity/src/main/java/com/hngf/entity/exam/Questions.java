package com.hngf.entity.exam;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 试题表
 * 
 * @author hngf
 * @email 
 * @date 2020-08-12 15:25:51
 */
@Data
@ApiModel("试题表")
public class Questions implements Serializable {

	private static final long serialVersionUID = 9005554467712442332L;
	/**
	 * 题库id
	 */
    @ApiModelProperty(value = "试题id" , position = 1)
    private Long questionsId;
	/**
	 * 题目
	 */
    @ApiModelProperty(value = "题目", position = 2)
    private String questionsName;
	/**
	 * 试题类型 1单选2多选3判断
	 *
	 * 4问答 以后扩展
	 */
    @ApiModelProperty(value = "试题类型1单选2多选3判断" , allowEmptyValue=false ,position = 3 )
    private Integer questionsType;
    @ApiModelProperty(value = "试题类型 单选/多选/判断" , position = 4)
    private String questionsTypeName;
	/**
	 * 试题答案
	 */
    @ApiModelProperty(value = "试题答案" , position = 5)
	@JSONField
    private String answerContent;
	/**
	 * 正确答案
	 */
    @ApiModelProperty(value = "正确答案" ,allowEmptyValue=false , position = 6)
    private String rightAnswer;
	/**
	 * 启用状态 1表示尘封，0表示启用
	 */
    @ApiModelProperty(value = "启用状态 1表示尘封，0表示启用" ,allowEmptyValue=false , position = 7)
    private Integer questionsStatus;

	@ApiModelProperty(value = "启用状态 尘封/启用" , position = 8)
	private String questionsStatusName;
	/**
	 * 群组ID
	 */
	@ApiModelProperty(value = "部门id" ,allowEmptyValue=false, position = 9 )
    private Long groupId;

	/**
	 * 部门名称
	 */
	@ApiModelProperty(value = "部门名称" , position = 10)
	private String groupName;

	/**
	 * 企业Id
	 */
	@ApiModelProperty(hidden = true)
    private Long companyId;

	/**
	 * 企业名称
	 */
	@ApiModelProperty(value = "企业名称" , position = 11)
	private String companyName;
	/**
	 * 创建人
	 */
    @ApiModelProperty(value = "创建人", position = 12)
    private Long createdBy;
	/**
	 * 创建时间
	 */
    @ApiModelProperty(hidden = true )
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8" )
    private Date createdTime;
	@ApiModelProperty(value = "创建时间" , position = 13)
	private String createdTimeStr;
	/**
	 * 修改人
	 */
	@ApiModelProperty(hidden = true)
    private Long updatedBy;
	/**
	 * 修改时间
	 */
	@ApiModelProperty(hidden = true)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updatedTime;
	/**
	 * 删除状态1.已删除 0 ，正常
	 */
	@ApiModelProperty(hidden = true )
    private Integer delFlag;

	public void addPrefixInit(Long createdBy, Date createdTime, Long groupId, Long companyId){
		this.setCreatedBy(createdBy);
		this.setUpdatedBy(createdBy);
		this.setCreatedTime(createdTime);
		this.setUpdatedTime(createdTime);
		this.setDelFlag(0);
		this.setGroupId(groupId);
		this.setCompanyId(companyId);
	}

	public void updatePrefixInit(Long updatedBy){
		this.setUpdatedBy(updatedBy);
		this.setUpdatedTime(new Date());
	}

}
