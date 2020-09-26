package com.hngf.service.sys;

import com.hngf.common.utils.PageUtils;
import com.hngf.entity.sys.RoleMember;
import java.util.Map;
import java.util.List;

/**
 * 角色成员表
 *
 * @author hngf
 * @email 
 * @date 2020-05-20 17:31:55
 */
public interface RoleMemberService {

    PageUtils queryPage(Map<String, Object> params);

    /**
    * 根据ID查询
    */
    RoleMember getById(Long id);

    /**
    * 保存
    */
    void save(RoleMember RoleMember);

    /**
    * 更新
    */
    void update(RoleMember RoleMember);

    /**
    * 批量删除
    */
    void removeByIds(List ids);

    /**
    * 删除
    */
    void removeById(Long id);
}

