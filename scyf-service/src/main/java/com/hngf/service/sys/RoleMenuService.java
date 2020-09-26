package com.hngf.service.sys;

import com.hngf.common.utils.PageUtils;
import com.hngf.entity.sys.Role;
import com.hngf.entity.sys.RoleMenu;
import java.util.Map;
import java.util.List;

/**
 * 角色授权菜单表
 *
 * @author hngf
 * @email 
 * @date 2020-05-20 17:31:55
 */
public interface RoleMenuService {

    PageUtils queryPage(Map<String, Object> params);

    /**
    * 根据ID查询
    */
    RoleMenu getById(Long id);

    /**
     * 根据角色ID查询
     */
    List<RoleMenu> getByRoleId(Long roleId);

    /**
    * 保存
    */
    void save(RoleMenu RoleMenu);

    /**
    * 更新
    */
    void update(RoleMenu RoleMenu);

    /**
    * 批量删除
    */
    void removeByIds(List ids);

    /**
    * 删除
    */
    void removeById(Long id);

    /**
    * 根据角色ID删除
    */
    int removeByRoleId(Long roleId);

    /**
     * 跟新角色对应的菜单
     * @param roleId
     * @param menuIds
     */
    void updateMenu(Long roleId, List<Long> menuIds,Long userId);
}

