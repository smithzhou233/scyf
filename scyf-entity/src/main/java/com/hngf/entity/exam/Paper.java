package com.hngf.entity.exam;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 试卷表
 * 
 * @author hngf
 * @email 
 * @date 2020-08-14 09:39:02
 */
@Data
@ApiModel("试卷表")
public class Paper implements Serializable {

	private static final long serialVersionUID = -8521276692219662640L;
	/**
	 * 试卷id
	 */
    @ApiModelProperty(value = "试卷id" , position = 1 )
    private Long paperId;
	/**
	 * 试卷名称
	 */
    @ApiModelProperty(value = "试卷名称" , position = 2 )
    private String paperName;
	/**
	 * 试卷说明
	 */
    @ApiModelProperty(value = "试卷说明" , position = 3 )
    private String paperInfo;
	/**
	 * 答题时间 单位：分钟
	 */
    @ApiModelProperty(value = "答题时间" , position = 4 )
    private Integer answerTime;
	/**
	 * 群组ID
	 */
    @ApiModelProperty(value = "群组ID" , position = 5 )
    private Long groupId;
	/**
	 * 企业ID
	 */
    @ApiModelProperty(value = "企业ID" , position = 6 )
    private Long companyId;
	/**
	 * 试卷状态:0未发布、1发布
	 */
    @ApiModelProperty(value = "试卷状态:0未发布、1发布" , position = 7 )
    private Integer paperStatus;
    @ApiModelProperty(value = "试卷状态:未发布/发布" , position = 7 )
    private String paperStatusName;
	/**
	 * 创建人
	 */
    @ApiModelProperty(value = "创建人" , position = 8 )
    private Long createdBy;
	/**
	 * 创建时间
	 */
    @ApiModelProperty(value = "创建时间" , position = 9 )
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

	/**
	 * 新增 初始化
	 * @param createdBy
	 * @param companyId
	 * @param delFlag
	 */
	public void insertPrefix(Long createdBy, Long companyId, Integer delFlag){
		this.setCreatedBy(createdBy);
		this.setCreatedTime(new Date());
		this.setDelFlag(delFlag);
		this.setCompanyId(companyId);
	}

	/**
	 * 修改 初始化
	 * @param updatedBy
	 */
	public void updatePrefix(Long updatedBy){
		this.setUpdatedBy(updatedBy);
		this.setUpdatedTime(new Date());
	}
}
