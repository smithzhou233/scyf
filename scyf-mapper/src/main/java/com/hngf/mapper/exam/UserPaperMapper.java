package com.hngf.mapper.exam;

import com.hngf.dto.exam.UserPaperDto;
import com.hngf.entity.exam.UserPaper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 用户试卷表
 * 
 * @author lxf
 * @email 
 * @date 2020-08-15 11:57:10
 */
@Mapper
public interface UserPaperMapper {

    List<UserPaper> findList(Map<String, Object> params);

    UserPaper findById(Long id);

    void add(UserPaper userPaper);

    void update(UserPaper userPaper);

    int deleteById(@Param("id")Long id, @Param("updatedBy")Long updatedBy);

    int deleteByIds(@Param("ids") List ids, @Param("updatedBy")Long updatedBy);

    /**
     * 根据试卷id删除 试卷分发给的用户
     * @param paperId
     * @param companyId
     * @return
     */
    int deleteByPaperId(@Param("paperId")Long paperId , @Param("companyId")Long companyId, @Param("updatedBy")Long updatedBy, @Param("updatedTime") Date updatedTime) ;

    /**
     * 发布试卷，把试卷分发给多个人，批量插入用户试卷关联关系
     * @param userPaperList
     * @return
     */
    int addBatch(@Param("userPaperList") List<UserPaper> userPaperList);


    UserPaperDto findDtoById(@Param("id") Long id);

    /**
     * 用户交卷时更新用户试卷状态：由0更新为1
     * @param userPaperId
     * @param userPaperStatus
     * @param updatedBy
     * @return
     */
    int updateUserPaperStatusByUserPaperId(@Param("userPaperId") Long userPaperId , @Param("userPaperStatus") Integer userPaperStatus, @Param("updatedBy")Long updatedBy);

    List<Long> usedPaperIdList(@Param("paperId") Long paperId ) ;

    int updateUserScoreByUserPaperId(@Param("userPaperId") Long userPaperId , @Param("score") Integer score);

}
