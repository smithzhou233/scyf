package com.hngf.mapper.risk;

import com.hngf.entity.risk.RiskPointPersonLiable;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Map;
import java.util.List;

/**
 * 风险点责任人表
 * 
 * @author hngf
 * @email 
 * @date 2020-06-12 09:20:57
 */
@Mapper
public interface RiskPointPersonLiableMapper {

    int add(RiskPointPersonLiable RiskPointPersonLiable);

    /**
     * 批量新增
     * @param list
     * @return
     */
    int addForeach(List<RiskPointPersonLiable> list);

    /**
     * 根据风险点删除
     * @param riskPointId
     * @param companyId
     * @return
     */
    int deleteByRiskPoint(@Param("riskPointId")Long riskPointId,@Param("companyId") Long companyId);
}
