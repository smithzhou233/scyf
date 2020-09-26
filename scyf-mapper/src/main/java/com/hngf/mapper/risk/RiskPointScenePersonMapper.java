package com.hngf.mapper.risk;

import com.hngf.entity.risk.RiskPointScenePerson;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Map;
import java.util.List;

/**
 * 风险点 现场人员
 * 
 * @author hngf
 * @email 
 * @date 2020-05-20 17:36:24
 */
@Mapper
public interface RiskPointScenePersonMapper {

    List<RiskPointScenePerson> findList(Map<String, Object> params);

    RiskPointScenePerson findById(Long id);

    RiskPointScenePerson findByAccountAndRiskpointId(Map<String, Object> params);

    List<RiskPointScenePerson> findByMap(Map<String, Object> params);

    void add(RiskPointScenePerson RiskPointScenePerson);

    void update(RiskPointScenePerson RiskPointScenePerson);

    int deleteById(@Param("id") Long id);

    int deleteByIds(@Param("ids") List ids);

    int deleteByMap(Map<String, Object> params);

    /**
     * 批量新增
     * @param list
     * @return
     */
    int addForeach(List<RiskPointScenePerson> list);
}
