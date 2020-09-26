package com.hngf.entity.exam;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserPaperMarkQuestions implements Serializable {
    private static final long serialVersionUID = 361206157895798932L;
    /**
     * 试卷id
     */
    private Long paperId;
    /**
     * 1单选2多选3判断
     *
     */
    private Integer questionsType;
    /**
     * 分数
     */
    private Integer mark;
    /**
     * 多选题答案少选分值
     */
    private Integer lessMark;
    /**
     * 多选题答案多选分值
     */
    private Integer moreMark;

    /**
     * 题库id
     */
    private Long questionsId;

    /**
     * 正确答案
     */
    private String rightAnswer;

}
