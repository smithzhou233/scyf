package com.hngf.mapper.sys;

import com.hngf.dto.sys.GroupDetailDto;
import com.hngf.entity.sys.Group;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import java.util.Map;
import java.util.List;

/**
 * 群组表
 * 
 * @author hngf
 * @email 
 * @date 2020-05-20 17:31:55
 */
@Mapper
public interface GroupMapper {

    List<Group> findList(Map<String, Object> params);

    Group findById(Long id);

    GroupDetailDto findByMap(Map<String, Object> params);

    List<GroupDetailDto> getChildren(Map<String, Object> map);

    /**
     * 查询子部门ID列表
     * @param groupId
     * @return
     */
    List<Long> findGroupIdList(Long groupId);

    void add(Group Group);

    void update(Group Group);

    int deleteById(@Param("id") Long id);

    int deleteByIds(@Param("ids") List ids);

    /**
     * 根据cids删除群组信息
     * @param cIds
     */
    void deleteByCompanyIds(String cIds);

    @Update({"UPDATE sys_group SET group_leaf = #{status} WHERE company_id=#{companyId} and group_id = #{groupId}  and del_flag = 0"})
    void updateGroupLeaf(@Param("companyId") Long companyId, @Param("groupId") Long groupId, @Param("status") int status) throws Exception;

    List<Group>  findGroupDetailList(Long groupId);

    Integer findCountByGroupTypeId(@Param("groupTypeId") Long groupTypeId);

    int deleteByCompanyId(@Param("companyId") Long companyId , @Param("updatedBy") Long updatedBy );
}
