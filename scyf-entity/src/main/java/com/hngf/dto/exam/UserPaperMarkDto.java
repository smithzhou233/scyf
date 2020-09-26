package com.hngf.dto.exam;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;
@Data
@ApiModel("试卷类型分数信息")
public class UserPaperMarkDto implements Serializable {
    private static final long serialVersionUID = 3707036350789141964L;

    /**
     * 试卷类型分数id
     */
    private Long paperMarkId;

    /**
     * 1单选2多选3判断
     *
     */
    @ApiModelProperty(value = "1单选2多选3判断"  , position = 2 )
    private Integer questionsType;
    /**
     * 分数
     */
    @ApiModelProperty(value = "分数" , position = 3  )
    private Integer mark;

    /**
     * 试卷中试题类型的试题集合
     */
    @ApiModelProperty(value = "试卷中试题类型的试题集合"  , position = 5)
    private List<UserQuestionsDto> questionsDtoList;
}
