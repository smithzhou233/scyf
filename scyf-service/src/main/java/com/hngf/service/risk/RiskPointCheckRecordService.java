package com.hngf.service.risk;

import com.hngf.common.utils.PageUtils;
import com.hngf.entity.risk.RiskPointCheckRecord;
import java.util.Map;
import java.util.List;

/**
 * 风险点检查记录
 *
 * @author hngf
 * @email 
 * @date 2020-06-03 17:15:30
 */
public interface RiskPointCheckRecordService {

    PageUtils queryPage(Map<String, Object> params, Integer pageNum, Integer pageSize, String order);

    PageUtils getListPage(Map<String, Object> params,Integer pageNum, Integer pageSize, String order);

    /**
    * 根据ID查询
    */
    RiskPointCheckRecord getById(Long id);

    /**
    * 保存
    */
    void save(RiskPointCheckRecord RiskPointCheckRecord);

    /**
    * 更新
    */
    void update(RiskPointCheckRecord RiskPointCheckRecord);

    /**
    * 批量删除
    */
    void removeByIds(List ids);

    /**
    * 删除
    */
    void removeById(Long id);

    /**
     * 每周检查
     * @param params
     * @return
     */
    List<Map<String, Object>> weeklyCheck(Map<String, Object> params);
}

