package com.hngf.mapper.risk;

import com.hngf.entity.risk.Risk;
import com.hngf.entity.risk.RiskRecord;
import com.hngf.entity.risk.RiskSource;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Map;
import java.util.List;

/**
 * 风险点关联风险表
 * 
 * @author hngf
 * @email 
 * @date 2020-05-20 17:36:24
 */
@Mapper
public interface RiskRecordMapper {

    List<RiskRecord> findList(Map<String, Object> params);

    List<Risk> findRiskByRiskPointId(Long riskPointId);

    List<Map<String, Object>> findMeasureList(Long riskPointId);

    RiskRecord findById(Long id);

    void add(RiskRecord RiskRecord);

    void update(RiskRecord RiskRecord);

    int deleteById(@Param("id") Long id);

    int deleteByIds(@Param("ids") List ids);

    int deleteByMap(Map<String, Object> params);

    void delDangerSource(@Param("dangerSrcId")Long dangerSrcId, @Param("companyId")Long companyId);

    void remove(@Param("parentRiskDangerId")Long parentRiskDangerId, @Param("list")List<Long> riskPointIds);

    int saveBatch(@Param("list")List<RiskRecord> addEntRiskRecord);

    List<Map<String, Object>> getRiskPointByDangerId(@Param("parentRiskDangerId")Long parentRiskDangerId);


    List<RiskRecord> list(@Param("riskDangerId")Long riskDangerId, @Param("companyId")Long companyId);

    void delete(@Param("riskId")Integer riskId, @Param("companyId")Long companyId);
}
