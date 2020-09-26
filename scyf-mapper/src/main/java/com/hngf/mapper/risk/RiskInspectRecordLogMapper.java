package com.hngf.mapper.risk;

import com.hngf.entity.risk.RiskInspectRecordLog;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Map;
import java.util.List;

/**
 * 检查记录日志表
 * 
 * @author hngf
 * @email 
 * @date 2020-06-10 17:16:34
 */
@Mapper
public interface RiskInspectRecordLogMapper {

    List<RiskInspectRecordLog> findList(Map<String, Object> params);

    RiskInspectRecordLog findById(Long id);

    void add(RiskInspectRecordLog RiskInspectRecordLog);

    void update(RiskInspectRecordLog RiskInspectRecordLog);

    int deleteById(@Param("id") Long id);

    int deleteByIds(@Param("ids") List ids);

    /**
     * 批量新增
     * @param recordLogs
     * @return
     */
    int addForeach(List<RiskInspectRecordLog> recordLogs);

    /**
     * 查询检查记录日志表记录count
     * @param params
     * @return
     */
    int findRiskInspectLogCount(Map<String, Object> params);

    /**
     * 查询检查记录日志
     * @param params
     * @return
     */
    List<RiskInspectRecordLog> findByRiskInspectRecordLog(Map<String, Object> params);

    /**
     * 清空检查记录日志
     * @param params
     * @return
     */
    int deleteRecordLog(Map<String, Object> params);
    /**
     * 清空检查记录日志
     * @return
     */
    int truncateCheckedRecordLog();
}
