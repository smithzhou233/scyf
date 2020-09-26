package com.hngf.dto.exam;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@ApiModel("用户考试试卷信息")
public class UserPaperDto implements Serializable {

    private static final long serialVersionUID = 2300994359828934008L;

    /**
     * 用户试卷Id
     */
    @ApiModelProperty(value = "用户试卷Id" , position = 1 )
    private Long userPaperId;

    /**
     * 试卷id
     */
    @ApiModelProperty(value = "试卷id" , position = 3)
    private Long paperId;

    /**
     * 试卷名称
     */
    @ApiModelProperty(value = "试卷名称" , position = 5 )
    private String paperName;

    /**
     * 答题时间 单位：分钟
     */
    @ApiModelProperty(value = "答题时间" , position = 7 )
    private Integer answerTime;

    /**
     * 0 未提交。1 提交
     */
    @ApiModelProperty(value = "0 未提交。1 提交 " , position = 9 )
    private Integer userPaperStatus;


    /**
     * 试卷中的试题类分数
     */
    @ApiModelProperty(value = "试卷中的试题类分数" , position = 9 )
    private List<UserPaperMarkDto> userPaperMarkDtoList ;

}
