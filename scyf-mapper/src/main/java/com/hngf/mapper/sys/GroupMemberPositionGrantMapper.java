package com.hngf.mapper.sys;

import com.hngf.entity.sys.GroupMemberPositionGrant;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Map;
import java.util.List;

/**
 * 群组成员岗位表
 * 
 * @author hngf
 * @email 
 * @date 2020-05-20 17:31:55
 */
@Mapper
public interface GroupMemberPositionGrantMapper {

    List<GroupMemberPositionGrant> findList(Map<String, Object> params);

    List<Long> findGrantGroupId(Map<String, Object> params);

    GroupMemberPositionGrant findById(Long id);

    void add(GroupMemberPositionGrant GroupMemberPositionGrant);

    void update(GroupMemberPositionGrant GroupMemberPositionGrant);

    int deleteById(@Param("id") Long id);

    int deleteByIds(@Param("ids") List ids);

    int deleteByCompanyId(@Param("companyId") Long companyId );
}
