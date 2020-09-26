package com.hngf.service.risk;

import com.hngf.common.utils.PageUtils;
import com.hngf.entity.risk.RiskPoint;
import com.hngf.entity.risk.RiskPointMap;

import java.text.ParseException;
import java.util.Map;
import java.util.List;

/**
 * 风险点地图标记数据
 *
 * @author hngf
 * @email 
 * @date 2020-05-20 17:36:23
 */
public interface RiskPointMapService {

    PageUtils queryPage(Map<String, Object> params);

    /**
    * 根据ID查询
    */
    RiskPointMap getById(Long id);

    /**
    * 保存
    */
    void save(RiskPointMap RiskPointMap);

    /**
    * 更新
    */
    void update(RiskPointMap RiskPointMap);

    /**
    * 批量删除
    */
    void removeByIds(List ids);

    /**
    * 删除
    */
    void removeById(Long id);

    /**
     * 新增或者保存
     * @param riskPoint
     */
    void insertOrUpdate(RiskPoint riskPoint) throws ParseException;

    /**
     * 新增风险点位置
     * @param riskPoint
     * @param mapId
     */
    void insertMapData(RiskPoint riskPoint,Long mapId,Long companyId);

    /**
     * 根据风险点Id 获取该风险点位置
     * yfh
     * 2020/07/01
     * @param riskPointId
     * @return
     */
    RiskPointMap getRiskPointId(Long riskPointId);

    /**
     * 可视化编辑 获取地图上风险点
     * yfh
     * 2020/07/1
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

