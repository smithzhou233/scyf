package com.hngf.mapper.score;

import com.hngf.entity.score.ScoreLog;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Map;
import java.util.List;

/**
 * 绩效考核打分记录日志表
 * 
 * @author hngf
 * @email 
 * @date 2020-05-27 10:11:53
 */
@Mapper
public interface ScoreLogMapper {

    List<ScoreLog> findList(Map<String, Object> params);
    /**
    * @Author: zyj
    * @Description:统计用户得分和部门得分情况
    * @Param companyId 企业id  userId用户id groupId 群组id  startTime 开始时间 endTime结束时间
    * @Date 15:57 2020/6/12
    */
    Map<String, Object> periodicSum(Map<String, Object> params);

    ScoreLog findById(Long id);

    void add(ScoreLog ScoreLog);

    void update(ScoreLog ScoreLog);

    int deleteById(@Param("id")Long id);

    int deleteByIds(@Param("ids") List ids);
}
