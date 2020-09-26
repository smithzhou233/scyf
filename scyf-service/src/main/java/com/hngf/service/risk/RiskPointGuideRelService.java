package com.hngf.service.risk;

import com.hngf.common.utils.PageUtils;
import com.hngf.entity.risk.RiskPointGuideRel;
import java.util.Map;
import java.util.List;

/**
 * 风险点作业指导书关系表
 *
 * @author hngf
 * @email 
 * @date 2020-05-20 17:36:24
 */
public interface RiskPointGuideRelService {

    PageUtils queryPage(Map<String, Object> params);

    /**
    * 根据ID查询
    */
    RiskPointGuideRel getById(Long id);

    /**
     * 根据风险点ID，查询作业指导书ID
     * @param riskPointId
     * @return
     */
    List<Long> getPostIdsByRiskPoint(Long riskPointId);

    /**
    * 保存
    */
    void save(RiskPointGuideRel RiskPointGuideRel);

    /**
    * 批量保存
    */
    int saveBatch(List<RiskPointGuideRel> list);

    /**
    * 更新
    */
    void update(RiskPointGuideRel RiskPointGuideRel);

    /**
    * 批量删除
    */
    void removeByIds(List ids);

    /**
    * 删除
    */
    void removeById(Long id);

    /**
     * 根据风险点ID和指导书ID删除
     * @param riskPointId
     * @param postId
     * @return
     */
    int removeByPointIdAndPostId(Long riskPointId, Long postId,Long companyId);
}

