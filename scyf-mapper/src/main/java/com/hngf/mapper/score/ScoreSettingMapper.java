package com.hngf.mapper.score;

import com.hngf.entity.score.ScoreSetting;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Map;
import java.util.List;

/**
 * 绩效考核打分规则配置表
 * 
 * @author hngf
 * @email 
 * @date 2020-05-27 10:11:53
 */
@Mapper
public interface ScoreSettingMapper {

    List<ScoreSetting> findList(Map<String, Object> params);

    ScoreSetting findById(Long id);

    void add(ScoreSetting ScoreSetting);

    void update(ScoreSetting ScoreSetting);

    int deleteById(@Param("id")Long id);

    int deleteByIds(@Param("ids") List ids);

    /**
     * 根据配置编号查询
     * @param companyId
     * @param settingCode
     * @return
     */
    ScoreSetting findBySettingCode(Long companyId, Integer settingCode);

    int deleteBatchByCompanyId(@Param("companyId") Long companyId, @Param("updatedBy") Long updatedBy) ;
}
