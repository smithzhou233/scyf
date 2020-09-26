package com.hngf.service.sys;

import com.hngf.common.utils.PageUtils;
import com.hngf.dto.sys.ElementUIDto;
import com.hngf.entity.sys.Menu;
import com.hngf.entity.sys.Role;
import java.util.Map;
import java.util.List;

/**
 * 
 *
 * @author hngf
 * @email 
 * @date 2020-05-20 17:31:55
 */
public interface RoleService {

    PageUtils queryPage(Map<String, Object> params);

    /**
    * 根据ID查询
    */
    Role getById(Long id);

    /**
    * 查询全部
    */
    List<Role> getList();
    /**
     * 查询添加用户的角色
     * zyj
     * @return
     */
    List<Role> findUserRole();
    /**
     * 查询当前角色对应的菜单
     * @param roleId
     * @return
     */
    List<Menu> getMenuByRoleId(Long roleId);
    /**
     * 查询当前角色对应的菜单 - ElementUID树
     * @param roleId
     * @param userId
     * @return
     */
    List<ElementUIDto> getElementUIDto(Long roleId,Long userId);

    /**
    * 保存
    */
    void save(Role Role);

    /**
    * 更新
    */
    void update(Role Role);

    /**
    * 批量删除
    */
    void removeByIds(List ids);

    /**
    * 删除
    */
    void removeById(Long id);

    /**
     * 获取所有
     * @param params
     * @return
     */
    List<Map<String,Object>> getListAllByMap (Map<String, Object> params);
}

