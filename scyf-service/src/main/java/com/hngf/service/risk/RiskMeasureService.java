package com.hngf.service.risk;

import com.hngf.common.utils.PageUtils;
import com.hngf.entity.risk.RiskMeasure;
import java.util.Map;
import java.util.List;

/**
 * 风险管控措施
 *
 * @author hngf
 * @email 
 * @date 2020-05-20 17:36:23
 */
public interface RiskMeasureService {

    PageUtils queryPage(Map<String, Object> params);

    /**
    * 根据ID查询
    */
    RiskMeasure getById(Long id);

    /**
    * 保存
    */
    void save(RiskMeasure RiskMeasure);

    /**
    * 更新
    */
    void update(RiskMeasure RiskMeasure);

    /**
    * 批量删除
    */
    void removeByIds(List ids);

    /**
    * 删除
    */
    void removeById(Long id);
}

