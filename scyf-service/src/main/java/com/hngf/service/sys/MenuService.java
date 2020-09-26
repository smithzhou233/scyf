package com.hngf.service.sys;

import com.hngf.common.utils.PageUtils;
import com.hngf.entity.sys.Menu;
import java.util.Map;
import java.util.List;

/**
 * 菜单表
 *
 * @author hngf
 * @email 
 * @date 2020-05-20 17:31:55
 */
public interface MenuService {

    List<Menu> getAll(Map<String, Object> params);
    
    List<Menu> getAll();

    /**
     * 查询父级节点下所以子节点
     * @param parentId
     * @return
     */
    List<Menu> getByParentId(Long parentId);

    /**
    * 根据ID查询
    */
    Menu getById(Long id);

    /**
    * 保存
    */
    void save(Menu Menu);

    /**
    * 更新
    */
    void update(Menu Menu);

    /**
    * 批量删除
    */
    void removeByIds(List ids);

    /**
    * 删除
    */
    void removeById(Long id);
}

