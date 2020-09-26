package com.hngf.vo.exam;

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
public class PaperVo implements Serializable {

    private static final long serialVersionUID = -7542434914650388720L;
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
     * 试卷状态:0未发布、1发布
     */
    @ApiModelProperty(value = "试卷状态:0未发布、1发布" , position = 7 )
    private Integer paperStatus;
    /**
     * 试卷类型分数 list
     */
    @ApiModelProperty(value = "试卷类型分数" , position = 8 )
    private List<PaperMarkVo> paperMarkVoList ;
}
