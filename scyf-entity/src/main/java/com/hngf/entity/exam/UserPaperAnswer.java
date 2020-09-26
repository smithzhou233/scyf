package com.hngf.entity.exam;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户试卷答案表
 * 
 * @author lxf
 * @email 
 * @date 2020-08-15 11:57:10
 */
@Data
@ApiModel("用户试卷答案表")
public class UserPaperAnswer implements Serializable {
	private static final long serialVersionUID = -8176137622591059490L;
	/**
	 * 用户试卷答案id
	 */
    @ApiModelProperty(value = "" , position = 1 )
    private Long userPaperAnswerId;

	/**
	 * 用户试卷id
	 */
	@ApiModelProperty(value = "用户试卷id" , position = 2 )
	private Long userPaperId;

	/**
	 * 用户id
	 */
    @ApiModelProperty(value = "用户id" , position = 3 )
    private Long userId;
	/**
	 * 试卷id
	 */
    @ApiModelProperty(value = "试卷id" , position =5 )
    private Long paperId;
	/**
	 * 试题id
	 */
    @ApiModelProperty(value = "试题id" , position = 6 )
    private Long questionsId;
	/**
	 * 试题答案
	 */
    @ApiModelProperty(value = "试题答案" , position = 7 )
    private String defAnswer;
	/**
	 * 答案
	 */
    @ApiModelProperty(value = "答案" , position = 8 )
    private String answer;
	/**
	 * 试题类型
	 */
    @ApiModelProperty(value = "试题类型" , position = 9 )
    private Integer questionsType;
	/**
	 * 试题分数
	 */
    @ApiModelProperty(value = "试题分数" , position = 10 )
    private Integer mark;
	/**
	 * 试题得分
	 */
    @ApiModelProperty(value = "试题得分" , position = 11 )
    private Integer score;
	/**
	 * 群组id
	 */
    @ApiModelProperty(value = "群组id" , position = 12 )
    private Long groupId;
	/**
	 * 企业id
	 */
    @ApiModelProperty(value = "企业id" , position = 13 )
    private Long companyId;
	/**
	 * 创建人
	 */
    @ApiModelProperty(value = "创建人" , position = 16 )
    private Long createdBy;
	/**
	 * 创建时间
	 */
	@ApiModelProperty(hidden = true)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createdTime;
	@ApiModelProperty(value = "创建时间" , position = 18 )
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
	@ApiModelProperty(hidden = true)
    private Integer delFlag;

    /**
     * 部门名称
     */
    @ApiModelProperty(value = "部门名称" )
    private String groupName;
    /**
     * 企业名称
     */
    @ApiModelProperty(value = "企业名称" )
    private String companyName;

    public void insertPrefix(Long userId ,Long groupId, Long companyId ){
    	this.setCreatedBy(userId);
    	this.setCreatedTime(new Date());
    	this.setUserId(userId);
    	this.setGroupId(groupId);
    	this.setCompanyId(companyId);
    	this.setDelFlag(0);
	}
	public void initInsert(Long paperId, Long questionsId, String defAnswer, String answer, Integer questionsType, Integer mark ){
		this.setPaperId(paperId);
		this.setQuestionsId(questionsId);
		this.setDefAnswer(defAnswer);
		this.setAnswer(answer);
		this.setQuestionsType(questionsType);
		this.setMark(mark);
	}
	public void updatePrefix(Long userId){
    	this.setUpdatedBy(userId);
    	this.setUpdatedTime(new Date());
	}
}
