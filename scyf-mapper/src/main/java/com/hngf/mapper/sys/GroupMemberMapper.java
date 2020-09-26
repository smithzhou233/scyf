package com.hngf.mapper.sys;

import com.hngf.entity.sys.GroupMember;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Map;
import java.util.List;

/**
 * 群组成员表
 * 
 * @author hngf
 * @email 
 * @date 2020-05-20 17:31:55
 */
@Mapper
public interface GroupMemberMapper {

    List<GroupMember> findList(Map<String, Object> params);

    GroupMember findById(Long id);

    void add(GroupMember GroupMember);

    void update(GroupMember GroupMember);

    int deleteByUserId(Long userId);

    int deleteById(@Param("id") Long id);

    int deleteByIds(@Param("ids") List ids);

    int deleteByCompanyId(@Param("companyId") Long companyId , @Param("updatedBy") Long updatedBy );
}
