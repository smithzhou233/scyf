package com.hngf.mapper.risk;

import com.hngf.entity.risk.RiskPointGuideRel;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Map;
import java.util.List;

/**
 * 风险点作业指导书关系表
 * 
 * @author hngf
 * @email 
 * @date 2020-05-20 17:36:24
 */
@Mapper
public interface RiskPointGuideRelMapper {

    List<RiskPointGuideRel> findList(Map<String, Object> params);

    RiskPointGuideRel findById(Long id);

    List<Long> findPostIdsByRiskPoint(Long riskPointId);

    void add(RiskPointGuideRel RiskPointGuideRel);

    int addForeach(List<RiskPointGuideRel> list);

    void update(RiskPointGuideRel RiskPointGuideRel);

    int deleteById(@Param("id") Long id);

    int deleteByIds(@Param("ids") List ids);

    int deleteByMap(Map<String, Object> params);


}
