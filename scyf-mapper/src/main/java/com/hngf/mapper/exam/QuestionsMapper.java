package com.hngf.mapper.exam;

import com.hngf.entity.exam.Questions;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 试题表
 * 
 * @author hngf
 * @email 
 * @date 2020-08-12 15:25:51
 */
@Mapper
public interface QuestionsMapper {

    List<Questions> findList(Map<String, Object> params);

    List<Questions> findToPaperInfoList(Map<String, Object> params);

    List<Questions> findPageList(Map<String, Object> params);

    List<Questions> findGroupList(Map<String, Object> params);

    Questions findById(Long id);

    void add(Questions questions);

    void update(Questions questions);

    int deleteById(@Param("id")Long id , @Param("updatedBy") Long updatedBy);

    int deleteByIds(@Param("ids") List ids, @Param("updatedBy") Long updatedBy);

    /**
     * 试题启用/尘封
     * @param ids
     * @param questionsStatus
     * @return
     */
    int updateQuestionsStatusByIds(@Param("ids") List<Long> ids , @Param("questionsStatus")Integer questionsStatus, @Param("updatedBy")Long updatedBy );
}
