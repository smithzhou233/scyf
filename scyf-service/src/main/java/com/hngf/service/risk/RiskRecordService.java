package com.hngf.service.risk;

import com.hngf.common.utils.PageUtils;
import com.hngf.entity.risk.Risk;
import com.hngf.entity.risk.RiskRecord;
import java.util.Map;
import java.util.List;

/**
 * 风险点关联风险表
 *
 * @author hngf
 * @email 
 * @date 2020-05-20 17:36:24
 */
public interface RiskRecordService {

    PageUtils queryPage(Map<String, Object> params);

    /**
    * 根据ID查询
    */
    RiskRecord getById(Long id);

    /**
    * 保存
    */
    void save(RiskRecord RiskRecord);

    /**
    * 批量保存
    */
    int saveBatch(List<RiskRecord> list);

    /**
    * 更新
    */
    void update(RiskRecord RiskRecord);

    /**
    * 批量删除
    */
    void removeByIds(List ids);

    /**
    * 删除
    */
    void removeById(Long id);

    /**
     * 删除
     */
    int removeByMap(Map<String, Object> params);

    /**
     * 根据风险点ID查询风险
     * @param riskPointId
     * @return
     */
    List<Risk> getRiskByRiskPointId(Long riskPointId);

    /**
     * 根据风险点ID查询安全检查表
     * @param riskPointId
     * @return
     */
    List<Map<String, Object>> getMeasureList(Long riskPointId);
}

