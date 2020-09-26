package com.hngf.mapper.danger;

import com.hngf.entity.danger.InspectItemDefContent;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Map;
import java.util.List;

/**
 * 检查项内容
 * 
 * @author hngf
 * @email 
 * @date 2020-05-20 17:36:24
 */
@Mapper
public interface InspectItemDefContentMapper {

    List<InspectItemDefContent> findList(Map<String, Object> params);

    InspectItemDefContent findById(Long id);

    void add(InspectItemDefContent InspectItemDefContent);

    void update(InspectItemDefContent InspectItemDefContent);

    int deleteById(@Param("id") Long id);

    int deleteByIds(@Param("ids") List ids);
}
