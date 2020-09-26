package com.hngf.service.sys.impl;

import com.hngf.dto.sys.ElementUIDto;
import com.hngf.dto.sys.GroupDetailDto;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hngf.common.utils.PageUtils;
import com.hngf.common.utils.Constant;

import com.hngf.mapper.sys.GroupMapper;
import com.hngf.entity.sys.Group;
import com.hngf.service.sys.GroupService;


@Service("GroupService")
public class GroupServiceImpl implements GroupService {

    @Autowired
    private GroupMapper GroupMapper;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        int pageNum = null!= params.get("pageNum") ? Integer.parseInt(params.get("pageNum").toString()) : 1;
        int pageSize = null != params.get("pageSize") ? Integer.parseInt(params.get("pageSize").toString()) : Constant.PAGE_SIZE;
        //利用PageHelper分页查询 注意：这个一定要放查询语句的前一行,否则无法进行分页,因为它对紧随其后第一个sql语句有效
        PageHelper.startPage(pageNum, pageSize);
        List<Group> list = GroupMapper.findList(params);
        PageInfo<Group> pageInfo = new PageInfo<Group>(list);
        return new PageUtils(list,(int)pageInfo.getTotal(),pageInfo.getPages(),pageInfo.getPageSize(),pageInfo.getPrePage() + 1);
    }

    @Override
    public Group getById(Long id){
        return GroupMapper.findById(id);
    }

    @Override
    public GroupDetailDto getById(Long companyOrOrgId, Long groupId) {
        Map<String, Object> params = new HashMap();
        params.put("companyOrOrgId", companyOrOrgId);
        params.put("groupId", groupId);
        return GroupMapper.findByMap(params);
    }

    @Override
    public List<GroupDetailDto> getGroupTreeList(Long companyId, Integer groupLeft, Integer groupRight) {
        Map<String, Object> map = new HashMap();
        map.put("companyOrOrgId", companyId);
        //map.put("groupLeft", groupLeft);
        //map.put("groupRight", groupRight);
        return this.GroupMapper.getChildren(map);
    }

    @Override
    public List<ElementUIDto> getEleuiTreeList(Map<String, Object> params) {
        List<GroupDetailDto> list = this.GroupMapper.getChildren(params);

        List<ElementUIDto> rootMenu = new ArrayList<>();
        //遍历取出顶级菜单
        list.forEach(root ->{
            //判断当前节点是否为父节点
            int tmp = 0 ;
            for (int i = 0; i < list.size(); i++) {
                if (root.getGroupId().intValue() != list.get(i).getGroupId().intValue()) {
                    if (list.get(i).getGroupId().intValue() == root.getGroupParent().intValue()) {
                        tmp ++;
                    }
                }
            }
            //tmp为0，代表当前节点为顶级父节点，将其加入rootMenu中
            if (tmp == 0) {
                ElementUIDto etd = new ElementUIDto();
                etd.setId(root.getGroupId());
                etd.setLabel(root.getGroupName());
                etd.setType(root.getGroupLevel());
                etd.setIcon(null);
                rootMenu.add(etd);
            }
        });

        //如果顶级菜单有数据，开始查找子节点
        if (null != rootMenu && rootMenu.size() > 0) {
            rootMenu.forEach(root ->{
                //子节点递归查找添加  传递父节点
                root.setChildren(getChildren(root.getId(),list));
            });
        }
        //清空菜单
        list.clear();
        return rootMenu;
    }

    public List<Long> queryGroupIdList(Long parentId) {
        return GroupMapper.findGroupIdList(parentId);
    }

    @Override
    public List<Long> getSubGroupIdList(Long groupId) {
        //部门及子部门ID列表
        List<Long> groupIdList = new ArrayList<>();
        //获取子部门ID列表
        List<Long> groupIds = queryGroupIdList(groupId);
        getGroupTreeList(groupIds, groupIdList);

        return groupIdList;
    }

    @Override
    public List<GroupDetailDto> getSubGroupDetailList(Long groupId){
        ///List<Long> ids =   getSubGroupIdList(groupId);
        Map<String, Object> map = new HashMap();
        map.put("groupId", groupId);
        //map.put("groupLeft", groupLeft);
        //map.put("groupRight", groupRight);
        return this.GroupMapper.getChildren(map);
       // return GroupMapper.findGroupDetailList(groupId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(Group Group) {
        GroupDetailDto groupParent = this.getById(Group.getCompanyId(), Group.getGroupParent());
        if (null != groupParent) {
            //设置左右树
            Group.setGroupLeft(null != groupParent.getGroupRight() ? groupParent.getGroupRight() : null);
            Group.setGroupRight(null != groupParent.getGroupRight() ? groupParent.getGroupRight() + 1 : null);

            //设置层级
            Group.setGroupLevel(null != groupParent.getGroupLevel() ? groupParent.getGroupLevel() + 1 : null);

            Group.setGroupLeaf(1);

            try {
                GroupMapper.updateGroupLeaf(Group.getCompanyId(), Group.getGroupParent(), 0);
            } catch (Exception e) {
                //更新失败不做处理
            }
        }

        GroupMapper.add(Group);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(Group Group) {
        GroupMapper.update(Group);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeByIds(List ids) {
        GroupMapper.deleteByIds(ids);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeById(Long id) {
        GroupMapper.deleteById(id);
    }

    //递归获取children节点
    private List<ElementUIDto> getChildren(Long pid,List<GroupDetailDto> list) {

        List<ElementUIDto> children = new ArrayList<>();

        if (null != pid){

            list.forEach(data -> {
                //若遍历的数据中的父节点id等于参数id
                //则判定当前节点为该参数id节点下的子节点
                if (data.getGroupParent().longValue() == pid.longValue()) {
                    //构造添加结果集合
                    ElementUIDto etd = new ElementUIDto();
                    etd.setId(data.getGroupId());
                    etd.setLabel(data.getGroupName());
                    etd.setType(data.getGroupLevel());
                    etd.setIcon(null);
                    children.add(etd);
                }
            });


        }

        //如果children不为空，继续递归遍历children下的子节点
        if (children.size() > 0) {
            children.forEach(data -> {
                data.setChildren(getChildren(data.getId(),list));
            });
        }
        return children;
    }

    /**
     * 递归组织ID
     */
    private void getGroupTreeList(List<Long> subIdList, List<Long> groupIdList){
        for(Long groupId : subIdList){
            List<Long> list = queryGroupIdList(groupId);
            if(list.size() > 0){
                getGroupTreeList(list, groupIdList);
            }

            groupIdList.add(groupId);
        }
    }
}
