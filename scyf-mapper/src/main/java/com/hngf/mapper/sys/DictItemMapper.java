package com.hngf.mapper.sys;

import com.hngf.entity.sys.DictItem;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Map;
import java.util.List;

/**
 * 字典项表
 * 
 * @author hngf
 * @email 
 * @date 2020-05-20 17:31:55
 */
@Mapper
public interface DictItemMapper {

    List<DictItem> findList(Map<String, Object> params);

    DictItem findById(String id);

    void add(DictItem DictItem);

    void update(DictItem DictItem);

    int deleteById(@Param("id") String id);

    int deleteByIds(@Param("ids") List ids);
}
