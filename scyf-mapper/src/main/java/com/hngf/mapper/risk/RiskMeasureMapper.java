package com.hngf.mapper.risk;

import com.hngf.entity.risk.RiskMeasure;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;
import java.util.Map;
import java.util.List;

/**
 * 风险管控措施
 * 
 * @author hngf
 * @email 
 * @date 2020-05-20 17:36:23
 */
@Mapper
public interface RiskMeasureMapper {


     List<Map<String, Object>> listMaps(@Param("riskId")Long riskId, @Param("companyId")Long companyId);

    List<RiskMeasure> findList(Map<String, Object> params);

    RiskMeasure findById(Long id);

    void add(RiskMeasure RiskMeasure);

    void update(RiskMeasure RiskMeasure);

    int deleteById(@Param("id") Long id);

    int deleteByIds(@Param("ids") List ids);

    void del(@Param("id")Long id, @Param("companyId")Long companyId);

    /**
     * 批量新增风险管控措施表信息
     * @param measureList
     */
    void saveBatch(@Param("list")List measureList);
}
