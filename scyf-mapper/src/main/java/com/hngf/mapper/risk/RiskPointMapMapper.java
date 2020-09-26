package com.hngf.mapper.risk;

import com.hngf.entity.risk.RiskPointMap;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Map;
import java.util.List;

/**
 * 风险点地图标记数据
 * 
 * @author hngf
 * @email 
 * @date 2020-05-20 17:36:23
 */
@Mapper
public interface RiskPointMapMapper {

    List<RiskPointMap> findList(Map<String, Object> params);

    RiskPointMap findById(Long id);

    void add(RiskPointMap RiskPointMap);

    /**
     * 批量新增
     * @param list
     * @return
     */
    int addForeach(List<RiskPointMap> list);

    void update(RiskPointMap RiskPointMap);

    int deleteById(@Param("id") Long id);

    int deleteByIds(@Param("ids") List ids);

    /**
     * 删除风险点位置
     * yfh
     * 2020/07/1
     * @param riskPointId
     * @param companyId
     */
    void del(@Param("riskPointId")Long riskPointId, @Param("companyId")Long companyId);
    /**
     * 根据风险点Id 获取该风险点位置
     * yfh
     * 2020/07/01
     * @param riskPointId
     * @return
     */
    RiskPointMap getRiskPointId(@Param("riskPointId")Long riskPointId);
    /**
     * 可视化编辑 获取地图上风险点
     * yfh
     * 2020/07/01
     * @param map
     * @return
     */
    List<Map<String, Object>> getMapPoints(Map<String, Object> map);

    /**
     * 可视化编辑 获取地图上风险点
     * zhangfei
     * 2020/07/30
     * @param map
     * @return
     */
    List<Map<String, Object>> getRiskPointMap(Map<String, Object> map);
}
