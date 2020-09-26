package com.hngf.mapper.risk;

import com.hngf.entity.risk.RiskPointControlRecordLog;
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
public interface RiskPointControlRecordLogMapper {

    List<RiskPointControlRecordLog> findList(Map<String, Object> params);

    RiskPointControlRecordLog findById(Long id);

    void add(RiskPointControlRecordLog RiskPointControlRecordLog);

    void update(RiskPointControlRecordLog RiskPointControlRecordLog);

    int deleteById(@Param("id") Long id);

    int deleteByIds(@Param("ids") List ids);

    List<RiskPointControlRecordLog> findIsControlRecordList(Map<String, Object> params);

    /**
     * 闭环预警信息
     * @param companyId
     * @param riskPointId
     * @param detailId
     * @param positionId
     * @return
     */
    int deleteIsCloseUp(@Param("companyId")Long companyId, @Param("riskPointId")Long riskPointId, @Param("detailId")Long detailId, @Param("positionId")Long positionId);

    /**
     * 闭环逾期任务
     * @param companyId
     * @param riskPointId
     * @param detailType
     * @param positionId
     * @return
     */
    int scheduleIsCloseUp(@Param("companyId")Long companyId, @Param("riskPointId")Long riskPointId, @Param("detailType")Integer detailType, @Param("positionId")Long positionId);
}
