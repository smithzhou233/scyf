package com.hngf.service.risk;

import com.hngf.common.utils.PageUtils;
import com.hngf.entity.risk.MapInfo;
import java.util.Map;
import java.util.List;

/**
 * 企业地图信息表
 *
 * @author hngf
 * @email 
 * @date 2020-05-26 16:06:37
 */
public interface MapInfoService {

    PageUtils queryPage(Map<String, Object> params,Integer pageNum,Integer pageSize,String order);

    /**
    * 根据ID查询
    */
    MapInfo getById(Long id);

    /**
    * 保存
    */
    void save(MapInfo MapInfo);

    /**
    * 更新
    */
    void update(MapInfo MapInfo);

    /**
    * 批量删除
    */
    void removeByIds(List ids);

    /**
    * 删除
    */
    void removeById(Long id);

    /**
     * 查看地图类型/使用类型
     * yfh
     * 2020/06/29
     * @param dictType
     * @return
     */
    List<Map<String, Object>> getMapType(String dictType);

    List<Map<String, Object>> getAllMapList(Map<String, Object> params);
}

