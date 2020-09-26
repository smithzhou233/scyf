package com.hngf.mapper.sys;

import com.hngf.entity.sys.RoleMember;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Map;
import java.util.List;

/**
 * 角色成员表
 * 
 * @author hngf
 * @email 
 * @date 2020-05-20 17:31:55
 */
@Mapper
public interface RoleMemberMapper {

    List<RoleMember> findList(Map<String, Object> params);

    RoleMember findById(Long id);

    void add(RoleMember RoleMember);

    void update(RoleMember RoleMember);

    int deleteById(@Param("id") Long id);

    int deleteByIds(@Param("ids") List ids);
}
