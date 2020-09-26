package com.hngf.service.sys;

import com.hngf.common.utils.PageUtils;
import com.hngf.entity.sys.GroupMemberPositionGrant;
import java.util.Map;
import java.util.List;

/**
 * 群组成员岗位表
 *
 * @author hngf
 * @email 
 * @date 2020-05-20 17:31:55
 */
public interface GroupMemberPositionGrantService {

    PageUtils queryPage(Map<String, Object> params);

    /**
    * 根据ID查询
    */
    GroupMemberPositionGrant getById(Long id);

    /**
     * 查询已授权的部门ID
     * @param params
     * @return
     */
    List<Long> getGrantGroupId(Map<String, Object> params);

    /**
    * 保存
    */
    void save(GroupMemberPositionGrant GroupMemberPositionGrant);

    /**
    * 更新
    */
    void update(GroupMemberPositionGrant GroupMemberPositionGrant);

    /**
    * 批量删除
    */
    void removeByIds(List ids);

    /**
    * 删除
    */
    void removeById(Long id);
}

