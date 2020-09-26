package com.hngf.service.risk;

import com.hngf.common.utils.PageUtils;
import com.hngf.entity.risk.RiskInspectRecordLog;
import java.util.Map;
import java.util.List;

/**
 * 检查记录日志表
 *
 * @author hngf
 * @email 
 * @date 2020-06-10 17:16:34
 */
public interface RiskInspectRecordLogService {

    /**
    * 根据ID查询
    */
    RiskInspectRecordLog getById(Long id);

    /**
    * 更新
    */
    void update(RiskInspectRecordLog RiskInspectRecordLog);

    /**
    * 批量删除
    */
    void removeByIds(List ids);

    /**
    * 删除
    */
    void removeById(Long id);

    int removeRecord(Map<String, Object> params);

    /**
     * 批量新增
     * @param recordLogs
     */
    int saveBatch(List<RiskInspectRecordLog> recordLogs);

    /**
     * 查询检查记录日志表记录count
     * @param params
     * @return
     */
    int getRiskInspectLogCount(Map<String, Object> params);

    List<RiskInspectRecordLog> getByRiskInspectRecordLog(Map<String, Object> params);
}

