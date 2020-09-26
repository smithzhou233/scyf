package com.hngf.service.risk;

import com.hngf.common.utils.PageUtils;
import com.hngf.common.utils.R;
import com.hngf.entity.risk.RiskInspectRecord;
import java.util.Map;
import java.util.List;

/**
 * 检查记录表
 *
 * @author hngf
 * @email 
 * @date 2020-05-20 17:36:23
 */
public interface RiskInspectRecordService {

    PageUtils queryPage(Map<String, Object> params);

    /**
    * 根据ID查询
    */
    RiskInspectRecord getById(Long id);

    /**
    * 查询风险点检查记录
    */
    List<RiskInspectRecord> getRiskInspectRecordList(Map<String, Object> params);

    /**
    * 查询风险点检查记录-分页
    */
    PageUtils getRiskInspectRecordPage(Map<String, Object> params,Integer pageNum,Integer pageSize,String order);

    /**
    * 保存
    */
    void save(RiskInspectRecord RiskInspectRecord);


    /**
    * 更新
    */
    void update(RiskInspectRecord RiskInspectRecord);

    /**
    * 批量删除
    */
    void removeByIds(List ids);

    /**
    * 删除
    */
    void removeById(Long id);

    /**
     * 新增风险检查记录
     * @param params
     * @param uploadPathStr
     */
    void addRiskInspect(Map<String, Object> params, String[] uploadPathStr) throws Exception;

    /**
     * 风险检查记录完成操作
     * @param params
     * @return
     */
    R schduleInspectFinish(Map<String, Object> params) throws Exception ;
}

