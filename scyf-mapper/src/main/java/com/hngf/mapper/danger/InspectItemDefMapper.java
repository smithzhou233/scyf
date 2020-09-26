package com.hngf.mapper.danger;

import com.hngf.entity.danger.InspectItemDef;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Map;
import java.util.List;

/**
 * 检查项
 * 
 * @author hngf
 * @email 
 * @date 2020-05-20 17:36:24
 */
@Mapper
public interface InspectItemDefMapper {

    List<InspectItemDef> findList(Map<String, Object> params);

    /**
     * 基础任务检查 - 查询检查项
     * @param params
     * @return
     */
    List<InspectItemDef> findSchduleInspectItems(Map<String, Object> params);

    InspectItemDef findById(Long id);

    int add(InspectItemDef InspectItemDef);

    int update(InspectItemDef InspectItemDef);

    int deleteById(@Param("id") Long id);

    int deleteByIds(@Param("ids") List ids);

    /**
     * 查询检查项总数
     * @param params
     * @return
     */
    int findInspectItemCount(Map<String, Object> params);
}
