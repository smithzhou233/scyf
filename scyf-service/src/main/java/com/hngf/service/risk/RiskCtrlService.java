package com.hngf.service.risk;

import com.alibaba.fastjson.JSONObject;
import com.hngf.common.utils.PageUtils;
import com.hngf.entity.risk.RiskCtrl;
import java.util.Map;
import java.util.List;

/**
 * 风险管控配置
 *
 * @author hngf
 * @email 
 * @date 2020-05-20 17:36:24
 */
public interface RiskCtrlService {

    PageUtils queryPage(Map<String, Object> params);

    /**
    * 根据ID查询
    */
    RiskCtrl getById(Long id);

    /**
    * 保存
    */
    void save(RiskCtrl RiskCtrl);

    /**
    * 更新
    */
    void update(RiskCtrl RiskCtrl);

    /**
    * 批量删除
    */
    void removeByIds(List ids);

    /**
    * 删除
    */
    void removeById(Long id);

    void updateEntRiskCtrl(JSONObject json);

    /**
     * 根据岗位ID 企业ID查看管控层级
     * YFH
     * 2020/06/11
     * @param params
     * @return
     */
    Integer getCurrentUserCtlLevel(Map<String, Object> params);
}

