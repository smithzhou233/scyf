package com.hngf.mapper.exam;

import com.hngf.entity.exam.PaperQuestions;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Map;
import java.util.List;

/**
 * 试卷题库关系表
 * 
 * @author hngf
 * @email 
 * @date 2020-08-14 09:39:02
 */
@Mapper
public interface PaperQuestionsMapper {

    List<PaperQuestions> findList(Map<String, Object> params);

    PaperQuestions findById(Long id);

    void add(PaperQuestions paperQuestions);

    void update(PaperQuestions paperQuestions);

    int deleteById(@Param("id")Long id, @Param("updatedBy")Long updatedBy);

    int deleteByIds(@Param("ids") List ids, @Param("updatedBy")Long updatedBy);

    int addBatch(@Param("paperQuestionsList") List<PaperQuestions> paperQuestionsList);

    /**
     * 根据试卷类型分数id删除属于该试卷类型的试题；试卷类型分数与试卷试题关联关系 是一对多的关系
     * @param paperMarkIds
     * @return
     */
    int deleteBatch(@Param("paperMarkIds") List<Long> paperMarkIds, @Param("updatedBy")Long updatedBy);

    int deleteBatchByPaperId(@Param("paperId") Long paperId, @Param("updatedBy")Long updatedBy);

    long getCountByQuestionsId(@Param("questionsId")Long questionsId);
}
