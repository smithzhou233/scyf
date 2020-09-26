package com.hngf.mapper.sys;

import com.hngf.dto.sys.GroupMemberPositionDto;
import com.hngf.entity.sys.GroupMemberPosition;
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
public interface GroupMemberPositionMapper {

    List<GroupMemberPosition> findList(Map<String, Object> params);

    GroupMemberPosition findById(Long id);

    void add(GroupMemberPosition GroupMemberPosition);

    void update(GroupMemberPosition GroupMemberPosition);

    int deleteByUserId(Long userId);

    int deleteById(@Param("id") Long id);

    int deleteByIds(@Param("ids") List ids);

    GroupMemberPosition findByMap(Map<String, Object> params);

    GroupMemberPositionDto findGrantGroupByUserId(@Param("userId")Long userId ,@Param("companyId")Long companyId);

    List<GroupMemberPositionDto> getGroupMemberPositionList(Map<String, Object> map);

    int deleteByCompanyId(@Param("companyId") Long companyId , @Param("updatedBy") Long updatedBy );

}
