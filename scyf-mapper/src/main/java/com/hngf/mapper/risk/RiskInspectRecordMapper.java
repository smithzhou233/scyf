package com.hngf.mapper.risk;

import com.hngf.entity.risk.RiskInspectRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Map;
import java.util.List;

/**
 * 检查记录表
 * 
 * @author hngf
 * @email 
 * @date 2020-05-20 17:36:23
 */
@Mapper
public interface RiskInspectRecordMapper {

    List<RiskInspectRecord> findList(Map<String, Object> params);

    /**
     * 风险点设备设施/作业活动接口(scyf/riskpoint/info/{riskPointId})，查询info接口返回更多信息使用
     * @param params
     * @return
     */
    List<RiskInspectRecord> findByMap(Map<String, Object> params);

    /**
     * 风险点检查记录查询-记录明细按钮调用
     * @param params
     * @return
     */
    List<RiskInspectRecord> findRiskInspectRecord(Map<String, Object> params);

    RiskInspectRecord findById(Long id);

    void add(RiskInspectRecord RiskInspectRecord);

    /**
     * 批量新增
     * @param list
     * @return
     */
    int addForeach(List<RiskInspectRecord> list);

    void update(RiskInspectRecord RiskInspectRecord);

    int deleteById(@Param("id") Long id);

    int deleteByIds(@Param("ids") List ids);
}
