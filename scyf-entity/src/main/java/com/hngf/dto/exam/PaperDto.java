package com.hngf.dto.exam;

import com.hngf.entity.exam.Questions;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 试卷表
 * 
 * @author lxf
 * @email 
 * @date 2020-08-14 09:39:02
 */
@Data
@ApiModel("试卷信息")
public class PaperDto implements Serializable {

	private static final long serialVersionUID = 3000998669154908569L;
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
    private Long companyId;
	/**
	 * 试卷状态:0未发布、1发布
	 */
    @ApiModelProperty(value = "试卷状态:0未发布、1发布" , position = 7 )
    private Integer paperStatus;

	@ApiModelProperty(value = "创建时间" , position = 8)
	private String createdTimeStr;

	@ApiModelProperty(value = "试卷状态:未发布/发布" , position = 9 )
    private String paperStatusName;
	/**
	 * 试卷类型分数 list
	 */
	@ApiModelProperty(value = "试卷类型分数" , position = 10 )
    private List<PaperMarkDto> paperMarkDtoList ;

	/**
	 * 部门名称
	 */
	@ApiModelProperty(value = "部门名称" , position = 11)
	private String groupName;
	/**
	 * 题库列表
	 */
	@ApiModelProperty(value = "题库列表" , position = 12)
	List<Questions> questionsList ;
}
