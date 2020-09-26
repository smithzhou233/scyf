package com.hngf.mapper.sys;

import com.hngf.entity.sys.UserRole;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户角色关联
 * @author dell
 * @since 2020/5/21 10:54
 */
@Mapper
public interface UserRoleMapper{

    /**
     * 根据用户ID，获取角色ID列表
     * @param  userId
     */
    List<Long> findRoleIdList(Long userId);

    /**
     * 新增
     * @param userRole
     * @return
     */
    int add(UserRole userRole);

    /**
     * 根据用户ID删除
     * @param userId
     * @return
     */
    int deleteByUserId(Long userId);

    /**
     * 根据角色ID数组，批量删除
     * @param roleIds
     */
    int deleteBatch(@Param("roleIds") List roleIds);
}
