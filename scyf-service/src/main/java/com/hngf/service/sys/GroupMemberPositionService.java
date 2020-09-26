package com.hngf.service.sys;

import com.hngf.common.utils.PageUtils;
import com.hngf.dto.sys.GroupMemberPositionDto;
import com.hngf.entity.sys.GroupMemberPosition;
import java.util.Map;
import java.util.List;

/**
 * 群组成员岗位表
 *
 * @author hngf
 * @email 
 * @date 2020-05-20 17:31:55
 */
public interface GroupMemberPositionService {

    PageUtils queryPage(Map<String, Object> params);

    /**
    * 根据ID查询
    */
    GroupMemberPosition getById(Long id);

    GroupMemberPositionDto getByUserIdAndCompanyId(Long userId, Long companyId);

    /**
    * 保存
    */
    void save(GroupMemberPosition GroupMemberPosition);

    /**
    * 更新
    */
    void update(GroupMemberPosition GroupMemberPosition);

    /**
    * 批量删除
    */
    void removeByIds(List ids);

    /**
    * 删除
    */
    void removeById(Long id);

      List<GroupMemberPositionDto> getGroupMemberPositionList(Map<String, Object> map);
}

