package com.hngf.service.risk;

import com.hngf.common.utils.PageUtils;
import com.hngf.entity.risk.RiskPointControlRecordLog;
import java.util.Map;
import java.util.List;

/**
 * 风险点管控实时告警记录表
 *
 * @author hngf
 * @email 
 * @date 2020-05-20 17:36:23
 */
public interface RiskPointControlRecordLogService {

    PageUtils queryPage(Map<String, Object> params);

    /**
    * 根据ID查询
    */
    RiskPointControlRecordLog getById(Long id);

    /**
     * 风险点警告信息
     * @param params
     * @return
     */
    List<RiskPointControlRecordLog> riskPointWarn(Map<String, Object> params);

    /**
    * 保存
    */
    void save(RiskPointControlRecordLog RiskPointControlRecordLog);

    /**
    * 更新
    */
    void update(RiskPointControlRecordLog RiskPointControlRecordLog);

    /**
    * 批量删除
    */
    void removeByIds(List ids);

    /**
    * 删除
    */
    void removeById(Long id);

    /**
     * 闭环预警信息
     * @param companyId
     * @param riskPointId
     * @param detailId
     * @param positionId
     * @return
     */
    int deleteIsCloseUp(Long companyId,Long riskPointId,Long detailId,Long positionId);

    /**
     * 闭环逾期任务
     * @param companyId
     * @param riskPointId
     * @param detailType
     * @param positionId
     * @return
     */
    int scheduleIsCloseUp(Long companyId,Long riskPointId,Integer detailType,Long positionId);

    /**
     * 查询失控的记录
     * @param params
     * @return
     */
    List<RiskPointControlRecordLog> getIsControlRecordList(Map<String, Object> params);
}

