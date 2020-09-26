package com.hngf.service.risk;

import com.alibaba.fastjson.JSONObject;
import com.hngf.common.utils.PageUtils;
import com.hngf.common.utils.R;
import com.hngf.entity.risk.Risk;
import java.util.Map;
import java.util.List;

/**
 * 风险定义表
 *
 * @author hngf
 * @email 
 * @date 2020-05-20 17:36:24
 */
public interface RiskService {

    PageUtils queryPage(Map<String, Object> params);

    /**
    * 根据ID查询
    */
    Risk getById(Long id);

    /**
    * 保存
    */
    void save(Risk Risk);

    /**
    * 更新
    */
    void update(Risk Risk);

    /**
    * 批量删除
    */
    void removeByIds(List ids);

    /**
    * 删除
    */
    void removeById(Long id);

    /**
     * 查询管控层级
     * yfh
     * 2020/05/29
     * @param companyId
     * @return
     */
    List<Map<String, Object>> selectLevel(Long companyId);
    /**
     * 查询管控岗位
     * yfh
     * 2020/05/29
     * @param companyId
     * @return
     */
    List<Map<String, Object>> selectPosition(Long companyId);

    Integer selModel(Long companyId);

    /**
     * 查看风险等级
     * yfh
     * 2020/05/29
     * @param evaluateIndexModel
     * @param companyId
     * @return
     */
    List<Map<String, Object>> selectComEvaluate(String evaluateIndexModel, Long companyId);
    List<Map<String, Object>> selectAEvaluate(String evaluateIndexType, Long companyId);

    /**
     * 查看风险管控层级
     * yfh
     * 2020/05/29
     * @param riskCtrlLevelValue
     * @param companyId
     * @return
     */
    List<Map<String, Object>> riskPositionLevel(Integer riskCtrlLevelValue, Long companyId);

    Map<String, Object> getRiskJson(Long riskId, Long companyId);

    List<Map<String, Object>> queryRisk(JSONObject json);
    List<Map<String, Object>> queryARisk(JSONObject json);
    R deleteRisk(Integer riskId, Long companyId);
}

