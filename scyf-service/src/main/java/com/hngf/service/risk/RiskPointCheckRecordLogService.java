package com.hngf.service.risk;

import com.hngf.common.utils.PageUtils;
import com.hngf.entity.risk.RiskPointCheckRecordLog;
import java.util.Map;
import java.util.List;

/**
 * 
 *
 * @author hngf
 * @email 
 * @date 2020-06-03 17:15:29
 */
public interface RiskPointCheckRecordLogService {

    PageUtils queryPage(Map<String, Object> params, Integer pageNum, Integer pageSize, String order);

    /**
    * 根据ID查询
    */
    RiskPointCheckRecordLog getById(Long id);

    /**
    * 保存
    */
    void save(RiskPointCheckRecordLog RiskPointCheckRecordLog);

    /**
    * 更新
    */
    void update(RiskPointCheckRecordLog RiskPointCheckRecordLog);

    /**
    * 批量删除
    */
    void removeByIds(List ids);

    /**
    * 删除
    */
    void removeById(Long id);
}

