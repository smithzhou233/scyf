package com.hngf.service.sys;

import com.hngf.common.utils.PageUtils;
import com.hngf.dto.sys.ElementUIDto;
import com.hngf.dto.sys.GroupDetailDto;
import com.hngf.entity.sys.Group;
import java.util.Map;
import java.util.List;

/**
 * 群组表
 *
 * @author hngf
 * @email 
 * @date 2020-05-20 17:31:55
 */
public interface GroupService {

    PageUtils queryPage(Map<String, Object> params);

    /**
    * 根据ID查询
    */
    Group getById(Long id);

    GroupDetailDto getById(Long companyOrOrgId, Long groupId);

    /**
    * 保存
    */
    void save(Group Group);

    /**
    * 更新
    */
    void update(Group Group);

    /**
    * 批量删除
    */
    void removeByIds(List ids);

    /**
    * 删除
    */
    void removeById(Long id);

    List<GroupDetailDto> getGroupTreeList(Long companyId, Integer groupLeft, Integer groupRight);

    /**
     * 获取 ElementUI树形结构数据
     * @param params
     * @return
     */
    List<ElementUIDto> getEleuiTreeList(Map<String, Object> params);

    /**
     * 查询子部门ID列表
     * @param parentId
     * @return
     */
    public List<Long> queryGroupIdList(Long parentId);
    /**
     * 获取子部门ID，用于数据过滤
     * @param groupId
     * @return
     */
    List<Long> getSubGroupIdList(Long groupId);

    List<GroupDetailDto> getSubGroupDetailList(Long id);
}

