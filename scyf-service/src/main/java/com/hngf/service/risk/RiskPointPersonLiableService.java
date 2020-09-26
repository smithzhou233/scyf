package com.hngf.service.risk;

import com.hngf.common.utils.PageUtils;
import com.hngf.entity.risk.RiskPointPersonLiable;
import java.util.Map;
import java.util.List;

/**
 * 风险点责任人表
 *
 * @author hngf
 * @email 
 * @date 2020-06-12 09:20:57
 */
public interface RiskPointPersonLiableService {


    /**
    * 保存
    */
    int save(RiskPointPersonLiable RiskPointPersonLiable);

    /**
    * 批量保存
    */
    int saveBatch(List<RiskPointPersonLiable> list);

    /**
    * 根据风险点删除
    */
    int removeByRiskPoint(Long riskPointId,Long companyId);
}

