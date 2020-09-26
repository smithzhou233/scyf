package com.hngf.mapper.sys;

import com.hngf.entity.sys.RoleMenu;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Map;
import java.util.List;

/**
 * 角色授权菜单表
 * 
 * @author hngf
 * @email 
 * @date 2020-05-20 17:31:55
 */
@Mapper
public interface RoleMenuMapper {

    List<RoleMenu> findList(Map<String, Object> params);

    RoleMenu findById(Long id);

    List<RoleMenu> findByRoleId(Long roleId);

    void add(RoleMenu RoleMenu);

    void update(RoleMenu RoleMenu);

    int deleteByRoleId(Long roleId);

    int deleteById(@Param("id") Long id);

    int deleteByIds(@Param("ids") List ids);
}
