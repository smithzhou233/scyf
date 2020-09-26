package com.hngf.mapper.risk;

import com.hngf.entity.risk.RiskPointDangerSource;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Map;
import java.util.List;

/**
 * 风险点涉及的危险源
 * 
 * @author hngf
 * @email 
 * @date 2020-05-20 17:36:24
 */
@Mapper
public interface RiskPointDangerSourceMapper {

    List<RiskPointDangerSource> findList(Map<String, Object> params);

    RiskPointDangerSource findById(Long id);

    void add(RiskPointDangerSource RiskPointDangerSource);

    int addForeach(List<RiskPointDangerSource> list);

    void update(RiskPointDangerSource RiskPointDangerSource);

    int deleteById(@Param("id") Long id);

    int deleteByIds(@Param("ids") List ids);

    int deleteByMap(Map<String, Object> params);

    void delDangerSource(@Param("dangerSrcId")Long dangerSrcId, @Param("companyId")Long companyId);

    List<Map<String, Object>> listMaps(@Param("parentRiskDangerId")Long parentRiskDangerId, @Param("companyId")Long companyId);

    List<Map<String, Object>> selectRiskAndRiskSourceList(Map<String, Object> map);

}
