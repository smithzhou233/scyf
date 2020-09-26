package com.hngf.service.sys;

import com.hngf.common.utils.PageUtils;
import com.hngf.entity.sys.GroupMember;
import java.util.Map;
import java.util.List;

/**
 * 群组成员表
 *
 * @author hngf
 * @email 
 * @date 2020-05-20 17:31:55
 */
public interface GroupMemberService {

    PageUtils queryPage(Map<String, Object> params);

    /**
    * 根据ID查询
    */
    GroupMember getById(Long id);

    /**
    * 保存
    */
    void save(GroupMember GroupMember);

    /**
    * 更新
    */
    void update(GroupMember GroupMember);

    /**
    * 批量删除
    */
    void removeByIds(List ids);

    /**
    * 删除
    */
    void removeById(Long id);
}

