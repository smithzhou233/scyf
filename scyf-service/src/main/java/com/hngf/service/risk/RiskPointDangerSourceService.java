package com.hngf.service.risk;

import com.hngf.common.utils.PageUtils;
import com.hngf.entity.risk.RiskPointDangerSource;
import java.util.Map;
import java.util.List;

/**
 * 风险点涉及的危险源
 *
 * @author hngf
 * @email 
 * @date 2020-05-20 17:36:24
 */
public interface RiskPointDangerSourceService {

    PageUtils queryPage(Map<String, Object> params);

    /**
    * 根据ID查询
    */
    RiskPointDangerSource getById(Long id);

    /**
    * 保存
    */
    void save(RiskPointDangerSource RiskPointDangerSource);

    /**
    * 批量保存
    */
    int saveBatch(List<RiskPointDangerSource> list);

    /**
    * 更新
    */
    void update(RiskPointDangerSource RiskPointDangerSource);

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
     * 查询风险点下的危险源
     * @param map
     * @return
     */
    PageUtils findRiskAndRiskSourceList(Map<String, Object> map);

}

