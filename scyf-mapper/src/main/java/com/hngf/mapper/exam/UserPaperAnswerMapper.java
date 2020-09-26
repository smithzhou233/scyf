package com.hngf.mapper.exam;

import com.hngf.entity.exam.UserPaperAnswer;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Map;
import java.util.List;

/**
 * 用户试卷答案表
 * 
 * @author lxf
 * @email 
 * @date 2020-08-15 11:57:10
 */
@Mapper
public interface UserPaperAnswerMapper {

    List<UserPaperAnswer> findList(Map<String, Object> params);

    UserPaperAnswer findById(Long id);

    void add(UserPaperAnswer userPaperAnswer);

    void update(UserPaperAnswer userPaperAnswer);

    int deleteById(@Param("id")Long id, @Param("updatedBy")Long updatedBy);

    int deleteByIds(@Param("ids") List ids, @Param("updatedBy")Long updatedBy);

    int addBatch(@Param("userPaperAnswerList") List<UserPaperAnswer> userPaperAnswerList);

    List<UserPaperAnswer> findListByUserPaperId(@Param("userPaperId")Long userPaperId );
}
