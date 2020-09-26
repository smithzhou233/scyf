package com.hngf.entity.exam;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户试卷表
 * 
 * @author lxf
 * @email 
 * @date 2020-08-15 11:57:10
 */
@Data
@ApiModel("用户试卷表")
public class UserPaper implements Serializable {

	private static final long serialVersionUID = 5222832763153703769L;
	/**
	 * 用户试卷id
	 */
    @ApiModelProperty(value = "用户试卷关联id" , position = 1 )
    private Long userPaperId;
	/**
	 * 用户id
	 */
    @ApiModelProperty(value = "用户id" , position = 2 )
    private Long userId;
	/**
	 * 试卷id
	 */
    @ApiModelProperty(value = "试卷id" , position = 3 )
    private Long paperId;
	/**
	 * 试卷名称
	 */
    @ApiModelProperty(value = "试卷名称" , position = 4 )
    private String paperName;
	/**
	 * 试卷总分
	 */
    @ApiModelProperty(value = "试卷总分" , position = 5 )
    private Integer mark;
	/**
	 * 试卷得分
	 */
    @ApiModelProperty(value = "试卷得分" , position = 6 )
    private Integer score;
	/**
	 * 群组id
	 */
    @ApiModelProperty(value = "群组id" , position = 7 )
    private Long groupId;
	/**
	 * 企业id
	 */
    @ApiModelProperty(value = "企业id" , position = 8 )
    private Long companyId;
	/**
	 * 0 未提交。1 提交 
	 */
    @ApiModelProperty(value = "0 未提交。1 提交 " , position = 9 )
    private Integer userPaperStatus;
	/**
	 * 创建人
	 */
    @ApiModelProperty(value = "创建人" , position = 10 )
    private Long createdBy;
	/**
	 * 创建时间
	 */
    @ApiModelProperty(value = "创建时间" , position = 11 )
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createdTime;
    private String createdTimeStr;
	/**
	 * 修改人
	 */
    private Long updatedBy;
	/**
	 * 修改时间
	 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updatedTime;
	/**
	 * 删除状态1.已删除 0 ，正常
	 */
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

    public void insertPrefix(Long userId ,Long groupId, Long companyId){
		this.setCreatedBy(userId);
		this.setCreatedTime(new Date());
		this.setGroupId(groupId);
		this.setCompanyId(companyId);
		this.setDelFlag(0);
	}
	public void initAddBatch(Long userId , Long paperId, String paperName, Integer userPaperStatus,Integer totalMark){
		this.setUserId(userId);
    	this.setPaperId(paperId);
    	this.setPaperName(paperName);
    	this.setUserPaperStatus(userPaperStatus);
        this.setMark(totalMark);
	}
}
