package com.hngf.dto.exam;

import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
@Data
@ApiModel("试卷试题信息")
public class UserQuestionsDto implements Serializable {
    private static final long serialVersionUID = -3039773615384390370L;

    /**
     * 题库id
     */
    @ApiModelProperty(value = "试题id" , position = 2 )
    private Long questionsId;
    /**
     * 题目
     */
    @ApiModelProperty(value = "题目", position = 3 )
    private String questionsName;
    /**
     * 试题类型 1单选2多选3判断
     *
     * 4问答 以后扩展
     */
    @ApiModelProperty(value = "试题类型1单选2多选3判断" , allowEmptyValue=false ,position = 5 )
    private Integer questionsType;

    /**
     * 试题答案
     */
    @ApiModelProperty(value = "试题答案" , position = 6 )
    @JSONField
    private String answerContent;

    /**
     * 正确答案
     */
    @ApiModelProperty(value = "正确答案", position = 7)
    private String answerRight;

    /**
     * 用户答案
     */
    @ApiModelProperty(value = "用户答案", position = 8 )
    private String answerUser;

    /**
     * 题目
     */
    @ApiModelProperty(value = "试题得分", position = 9 )
    private Integer score;
}
