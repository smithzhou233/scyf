package com.hngf.service.sys;

import com.hngf.common.utils.PageUtils;
import com.hngf.entity.sys.GroupType;

import java.util.List;
import java.util.Map;

/**
 * 部门类型
 *
 * @author hngf
 * @email 
 * @date 2020-05-22 11:08:22
 */
public interface GroupTypeService {

    PageUtils queryPage(Map<String, Object> params);

    List<GroupType> getList(Long companyId);

    /**
    * 根据ID查询
    */
    GroupType getById(Long id);

    /**
    * 保存
    */
    void save(GroupType GroupType);

    /**
    * 更新
    */
    void update(GroupType GroupType);

    /**
    * 批量删除
    */
    void removeByIds(List ids);

    /**
    * 删除
    */
    void removeById(Long id, Long updatedBy);


}

