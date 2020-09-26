package com.hngf.mapper.risk;

import com.hngf.entity.risk.RiskPointCheckRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Map;
import java.util.List;

/**
 * 
 * 
 * @author hngf
 * @email 
 * @date 2020-06-03 17:15:30
 */
@Mapper
public interface RiskPointCheckRecordMapper {

    List<RiskPointCheckRecord> findList(Map<String, Object> params);

    List<RiskPointCheckRecord> findByMap(Map<String, Object> params);

    RiskPointCheckRecord findById(Long id);

    void add(RiskPointCheckRecord RiskPointCheckRecord);

    void update(RiskPointCheckRecord RiskPointCheckRecord);

    int deleteById(@Param("id") Long id);

    int deleteByIds(@Param("ids") List ids);

    /**
     * 每周检查
     * @param params
     * @return
     */
    List<Map<String, Object>> weeklyCheck(Map<String, Object> params);
}
