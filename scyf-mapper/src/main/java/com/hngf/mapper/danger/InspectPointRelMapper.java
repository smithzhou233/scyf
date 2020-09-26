package com.hngf.mapper.danger;

import com.hngf.entity.danger.InspectPointRel;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Map;
import java.util.List;

/**
 * 
 * 
 * @author hngf
 * @email 
 * @date 2020-05-20 17:36:24
 */
@Mapper
public interface InspectPointRelMapper {

    List<InspectPointRel> findList(Map<String, Object> params);

    InspectPointRel findById(Long id);

    void add(InspectPointRel InspectPointRel);

    void update(InspectPointRel InspectPointRel);

    int deleteById(@Param("id") Long id);

    int deleteByIds(@Param("ids") List ids);

    int deleteByMap(Map<String, Object> params);

    /**
     * 批量新增
     * @param list
     * @return
     */
    int addForeach(List<InspectPointRel> list);
}
