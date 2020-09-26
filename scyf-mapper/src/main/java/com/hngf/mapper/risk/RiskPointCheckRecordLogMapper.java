package com.hngf.mapper.risk;

import com.hngf.entity.risk.RiskPointCheckRecordLog;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Map;
import java.util.List;

/**
 * 
 * 
 * @author hngf
 * @email 
 * @date 2020-06-03 17:15:29
 */
@Mapper
public interface RiskPointCheckRecordLogMapper {

    List<RiskPointCheckRecordLog> findList(Map<String, Object> params);

    RiskPointCheckRecordLog findById(Long id);

    void add(RiskPointCheckRecordLog RiskPointCheckRecordLog);

    void update(RiskPointCheckRecordLog RiskPointCheckRecordLog);

    int deleteById(@Param("id") Long id);

    int deleteByIds(@Param("ids") List ids);
    int truncateRiskPointCheckRecordLog();
}
