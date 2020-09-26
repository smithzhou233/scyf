package com.hngf.mapper.sys;

import com.hngf.entity.sys.GroupType;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Map;
import java.util.List;

/**
 * 部门类型
 * 
 * @author hngf
 * @email 
 * @date 2020-05-22 11:08:22
 */
@Mapper
public interface GroupTypeMapper {

    List<GroupType> findList(Map<String, Object> params);

    /**
     * 根据当前公司所有部门类型
     * @param companyId
     * @return
     */
    List<GroupType> findAll(@Param("companyId")Long companyId);

    GroupType findById(Long id);

    void add(GroupType GroupType);

    void update(GroupType GroupType);

    int deleteById(@Param("id") Long id, @Param("updatedBy") Long updatedBy);

    int deleteByIds(@Param("ids") List ids);

    void addBatch(@Param("groupTypeList") List<GroupType> groupTypeList);
}
