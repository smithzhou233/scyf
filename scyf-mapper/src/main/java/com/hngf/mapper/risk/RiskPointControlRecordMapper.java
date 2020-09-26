package com.hngf.mapper.risk;

import com.hngf.entity.risk.RiskPointControlRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Map;
import java.util.List;

/**
 * 风险点管控实时告警记录表
 * 
 * @author hngf
 * @email 
 * @date 2020-05-20 17:36:23
 */
@Mapper
public interface RiskPointControlRecordMapper {

    List<RiskPointControlRecord> findList(Map<String, Object> params);

    /**
     * 查询风险点实时预警记录
     * @param params
     * @return
     */
    List<Map<String,Object>> findControlRecord(Map<String, Object> params);

    /**
     * 查询风险点历史预警记录
     * @param params
     * @return
     */
    List<Map<String,Object>> findHistoryControlRecord(Map<String, Object> params);
    /**
    * @Author: zyj
    * @Description:风险，风险点预警记录统计折线图
    * @Param year 年份 companyId企业id
    * @Date 15:50 2020/6/19
    */
    List<Map<String,Object>> getStatistics(@Param("companyId") Long companyId,@Param("year") Integer year);
    /**
    * @Author: zyj
    * @Description:风险，风险点预警记录统计折线图 年份下拉框
    * @Param companyId企业id
    * @Date 15:28 2020/6/23
    */
    List<Map<String,Object>> getStatisticsYear(@Param("companyId") Long companyId);

    RiskPointControlRecord findById(Long id);

    int add(RiskPointControlRecord RiskPointControlRecord);

    int update(RiskPointControlRecord RiskPointControlRecord);

    int deleteById(@Param("id") Long id);

    int deleteByIds(@Param("ids") List ids);
}
