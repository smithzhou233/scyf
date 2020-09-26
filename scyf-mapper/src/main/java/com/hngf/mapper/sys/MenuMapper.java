package com.hngf.mapper.sys;

import java.util.List;
import java.util.Map;

import com.hngf.dto.sys.ElementUIDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.hngf.dto.sys.MenuDto;
import com.hngf.entity.sys.Menu;

/**
 * 菜单表
 * 
 * @author hngf
 * @email 
 * @date 2020-05-20 17:31:55
 */
@Mapper
public interface MenuMapper {

    List<Menu> findList(Map<String, Object> params);
    
    List<Menu> findAll();

    /**
     * 查询父级节点下所以子节点
     * @param parentId
     * @return
     */
    List<Menu> findByParentId(Long parentId);

    Menu findById(Long id);

    void add(Menu Menu);

    void update(Menu Menu);

    int deleteById(@Param("id") Long id);

    int deleteByIds(@Param("ids") List ids);

}
