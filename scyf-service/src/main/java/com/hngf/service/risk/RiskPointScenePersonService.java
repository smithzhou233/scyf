package com.hngf.service.risk;

import com.hngf.common.utils.PageUtils;
import com.hngf.entity.risk.RiskPointScenePerson;

import java.util.Map;
import java.util.List;

/**
 * 风险点 现场人员
 *
 * @author hngf
 * @email 
 * @date 2020-05-20 17:36:24
 */
public interface RiskPointScenePersonService {

    PageUtils queryPage(Map<String, Object> params, int pageNum, int pageSize, String order);

    /**
    * 根据ID查询
    */
    RiskPointScenePerson getById(Long id);

    List<RiskPointScenePerson> getByRiskPointId(Map<String, Object> map);

    RiskPointScenePerson getByAccountAndRiskpointId(Map<String, Object> params);

    /**
    * 保存
    */
    void save(RiskPointScenePerson RiskPointScenePerson);

    /**
    * 更新
    */
    void update(RiskPointScenePerson RiskPointScenePerson);

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
     * 批量保存
     */
    int saveBatch(List<RiskPointScenePerson> list);


}

