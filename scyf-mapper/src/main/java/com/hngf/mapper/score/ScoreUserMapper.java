package com.hngf.mapper.score;

import com.hngf.entity.score.ScoreUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Map;
import java.util.List;

/**
 * 用户绩效考核总得分表
 * 
 * @author zhangfei
 * @email 
 * @date 2020-05-27 10:11:53
 */
@Mapper
public interface ScoreUserMapper {

    List<ScoreUser> findList(Map<String, Object> params);

    ScoreUser findById(Long id);

    void add(ScoreUser ScoreUser);

    void update(ScoreUser ScoreUser);

    int deleteById(@Param("id")Long id);

    int deleteByIds(@Param("ids") List ids);

    /**
     * 查询我的绩效考核信息
     * @param params(companyId,userId,scoreType)
     * @return
     */
    ScoreUser getMyScoreRecord(Map<String, Object> params);
}
