package com.hngf.mapper.risk;

import com.hngf.entity.risk.MapInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Map;
import java.util.List;

/**
 * 企业地图信息表
 * 
 * @author hngf
 * @email 
 * @date 2020-05-26 16:06:37
 */
@Mapper
public interface MapInfoMapper {

    List<MapInfo> findList(Map<String, Object> params);

    MapInfo findById(Long id);

    void add(MapInfo MapInfo);

    void update(MapInfo MapInfo);

    int deleteById(@Param("id")Long id);

    int deleteByIds(@Param("ids") List ids);

    List<Map<String, Object>> getMapType(@Param("dictType")String dictType);

    List<Map<String, Object>> getAllMapList(Map<String, Object> params);


}
