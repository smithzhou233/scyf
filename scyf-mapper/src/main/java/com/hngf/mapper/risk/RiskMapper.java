package com.hngf.mapper.risk;

import com.alibaba.fastjson.JSONObject;
import com.hngf.entity.risk.Risk;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Map;
import java.util.List;

/**
 * 风险定义表
 * 
 * @author hngf
 * @email 
 * @date 2020-05-20 17:36:24
 */
@Mapper
public interface RiskMapper {

    List<Map<String,Object>> findList(Map<String, Object> params);

    Risk findById(Long id);

    void add(Risk Risk);

    void update(Risk Risk);

    int deleteById(@Param("id") Long id);

    int deleteByIds(@Param("ids") List ids);

    void deleteByDangerSrcIds(@Param("ids") String ids);

    List<Map<String, Object>> selectLevel(@Param("companyId")Long companyId);

    List<Map<String, Object>> selectPosition(@Param("companyId")Long companyId);

    Integer selModel(@Param("companyId")Long companyId);

    List<Map<String, Object>> selectComEvaluate(@Param("evaluateIndexModel")String evaluateIndexModel, @Param("companyId")Long companyId);
    List<Map<String, Object>> selectAEvaluate(@Param("evaluateIndexType")String evaluateIndexType, @Param("companyId")Long companyId);
    List<Map<String, Object>> riskPositionLevel(@Param("riskCtrlLevelValue")Integer riskCtrlLevelValue, @Param("companyId")Long companyId);

    Map<String, Object> getRiskJson(@Param("riskId")Long riskId);

    Risk getOne(@Param("riskCode")String riskCode, @Param("companyId")Long cId);

    List<Map<String, Object>> queryRisk(JSONObject json);
    List<Map<String, Object>> queryARisk(JSONObject json);
    void remove(@Param("riskId")Integer riskId, @Param("companyId")Long companyId);


    /**
     * 统计集团下所有企业的风险指数和安全指数 - 监管层大屏调用
     * @param params
     * @return
     */
    Map<String, Object> findTotalRiskPointAndSecurityPointForGent(Map<String, Object> params);

    /**
     * 查询统计风险点总计 - 监管层大屏调用
     * @param companyIds
     * @return
     */
    List<Map<String, Object>> findRiskCountForGent(String companyIds);

    /**
     * 查询统计预警风险点数 - 监管层大屏调用
     * @param companyIds
     * @return
     */
    List<Map<String, Object>> findRiskCountOfOutOfControlForGent(String companyIds);

    /**
     * 查询统计下级企业风险指数 - 监管层大屏调用
     * @param params
     * @return
     */
    List<Map<String, Object>> findRiskPointForGent(Map<String, Object> params);
}
