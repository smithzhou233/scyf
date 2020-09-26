package com.hngf.service.sys.impl;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import java.util.Map;
import java.util.List;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hngf.common.utils.PageUtils;
import com.hngf.common.utils.Constant;

import com.hngf.mapper.sys.GroupMemberPositionGrantMapper;
import com.hngf.entity.sys.GroupMemberPositionGrant;
import com.hngf.service.sys.GroupMemberPositionGrantService;


@Service("GroupMemberPositionGrantService")
public class GroupMemberPositionGrantServiceImpl implements GroupMemberPositionGrantService {

    @Autowired
    private GroupMemberPositionGrantMapper GroupMemberPositionGrantMapper;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        int pageNum = null!= params.get("pageNum") ? Integer.parseInt(params.get("pageNum").toString()) : 1;
        int pageSize = null != params.get("pageSize") ? Integer.parseInt(params.get("pageSize").toString()) : Constant.PAGE_SIZE;
        //利用PageHelper分页查询 注意：这个一定要放查询语句的前一行,否则无法进行分页,因为它对紧随其后第一个sql语句有效
        PageHelper.startPage(pageNum, pageSize);
        List<GroupMemberPositionGrant> list = GroupMemberPositionGrantMapper.findList(params);
        PageInfo<GroupMemberPositionGrant> pageInfo = new PageInfo<GroupMemberPositionGrant>(list);
        return new PageUtils(list,(int)pageInfo.getTotal(),pageInfo.getPages(),pageInfo.getPageSize(),pageInfo.getPrePage() + 1);
    }

    @Override
    public GroupMemberPositionGrant getById(Long id){
        return GroupMemberPositionGrantMapper.findById(id);
    }

    @Override
    public List<Long> getGrantGroupId(Map<String, Object> params) {
        return GroupMemberPositionGrantMapper.findGrantGroupId(params);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(GroupMemberPositionGrant GroupMemberPositionGrant) {
        GroupMemberPositionGrant.setDelFlag(0);
        GroupMemberPositionGrantMapper.add(GroupMemberPositionGrant);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(GroupMemberPositionGrant GroupMemberPositionGrant) {
        GroupMemberPositionGrantMapper.update(GroupMemberPositionGrant);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeByIds(List ids) {
        GroupMemberPositionGrantMapper.deleteByIds(ids);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeById(Long id) {
        GroupMemberPositionGrantMapper.deleteById(id);
    }
}
