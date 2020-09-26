package com.hngf.mapper.sys;

import com.hngf.entity.sys.Dict;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Map;
import java.util.List;

/**
 * 字典表
 * 
 * @author hngf
 * @email 
 * @date 2020-05-20 17:31:55
 */
@Mapper
public interface DictMapper {

    List<Dict> findList(Map<String, Object> params);

    List<Dict> findByMap(Map<String, Object> params);

    Dict findById(String id);

    void add(Dict Dict);

    void update(Dict Dict);

    int deleteById(@Param("id") Long id);

    int deleteByIds(@Param("ids") List ids);

    Long getTabId(@Param("tabName")String tabName);

    void setTabId(@Param("tabName")String tabName, @Param("newTabId")Long newTabId);

    void insertTab(@Param("tabName")String tabName, @Param("tabId")long tabId);

    List<Map<String, Object>> getRiskDangerLevel();
}
