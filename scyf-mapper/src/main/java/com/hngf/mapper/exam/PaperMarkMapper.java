package com.hngf.mapper.exam;

import com.hngf.entity.exam.PaperMark;
import com.hngf.entity.exam.UserPaperMarkQuestions;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 试卷类型分数
 * 
 * @author hngf
 * @email 
 * @date 2020-08-14 09:39:02
 */
@Mapper
public interface PaperMarkMapper {

    List<PaperMark> findList(Map<String, Object> params);

    PaperMark findById(Long id);

    void add(PaperMark paperMark);

    void update(PaperMark paperMark);

    int deleteById(@Param("id")Long id, @Param("updatedBy")Long updatedBy);

    int deleteByIds(@Param("ids") List ids, @Param("updatedBy")Long updatedBy);

    int addBatch(@Param("paperMarkList")List<PaperMark> paperMarkList);

    /**
     * 根据试卷Id 删除试卷类型分数 ；试卷 和 试卷类型分数是一对多的关系
     * @param paperId
     * @return
     */
    int deleteBatchPaperMarkByPaperId(@Param("paperId")Long paperId, @Param("updatedBy")Long updatedBy);

    /**
     * 根据试卷id和试题类型获取 试卷类型分数id集合，用来作为校验同一张试卷的试卷类型分数是否传的有重复
     * @param paperId
     * @param questionsType
     * @return
     */
    List<Long> findIdByPaperIdAndQuestionsType(@Param("paperId")Long paperId, @Param("questionsType")Integer questionsType ) ;
    /**
     * 根据试卷id获取试卷的总分数
     * @param paperId
     * @return
     */
    Integer getTotalMarkByPaperId(@Param("paperId")Long paperId);


    List<UserPaperMarkQuestions> findUserPaperQuestionsListByPaperId(@Param("paperId") Long paperId) ;

}
