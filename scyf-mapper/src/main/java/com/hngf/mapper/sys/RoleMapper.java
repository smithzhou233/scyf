package com.hngf.mapper.sys;

import com.hngf.entity.sys.Menu;
import com.hngf.entity.sys.Role;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Map;
import java.util.List;

/**
 * 
 * 
 * @author hngf
 * @email 
 * @date 2020-05-20 17:31:55
 */
@Mapper
public interface RoleMapper {

    List<Role> findList(Map<String, Object> params);

    Role findById(Long id);

    /**
     * 根据角色查询对应菜单
     * @param roleId
     * @return
     */
    List<Menu> findMenuByRoleId(Long roleId);

    void add(Role Role);

    void update(Role Role);

    int deleteById(@Param("id") Long id);

    int deleteByIds(@Param("ids") List ids);

    /**
     * 查询全部角色
     * @return
     */
    List<Role> findAll();

    /**
     * 查询添加用户的角色
     * zyj
     * @return
     */
    List<Role> findUserRole();
    /**
     * 获取所有
     * @param params
     * @return
     */
    List<Map<String,Object>> getListAllByMap (Map<String, Object> params);


}
