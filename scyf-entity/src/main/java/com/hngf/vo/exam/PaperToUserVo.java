package com.hngf.vo.exam;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@ApiModel("发布试卷")
public class PaperToUserVo implements Serializable {
    private static final long serialVersionUID = 1951664288004838165L;
    /**
     * 试卷id
     */
    @ApiModelProperty(value = "试卷id" , position = 1 )
    private Long paperId ;

    /**
     * 试卷名称
     */
    @ApiModelProperty(value = "试卷名称" , position = 2 )
    private String paperName;

    /**
     * 用户id
     */
    @ApiModelProperty(value = "用户id" , position = 5 )
    private List<Long> userIdList ;

    /**
     * 试卷状态:0未发布、1发布
     */
    @ApiModelProperty(value = "试卷状态:0未发布、1发布" , position = 7)
    private Integer status;

}
