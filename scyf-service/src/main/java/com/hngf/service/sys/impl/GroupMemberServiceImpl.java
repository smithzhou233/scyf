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

import com.hngf.mapper.sys.GroupMemberMapper;
import com.hngf.entity.sys.GroupMember;
import com.hngf.service.sys.GroupMemberService;


@Service("GroupMemberService")
public class GroupMemberServiceImpl implements GroupMemberService {

    @Autowired
    private GroupMemberMapper GroupMemberMapper;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        int pageNum = null!= params.get("pageNum") ? Integer.parseInt(params.get("pageNum").toString()) : 1;
        int pageSize = null != params.get("pageSize") ? Integer.parseInt(params.get("pageSize").toString()) : Constant.PAGE_SIZE;
        //利用PageHelper分页查询 注意：这个一定要放查询语句的前一行,否则无法进行分页,因为它对紧随其后第一个sql语句有效
        PageHelper.startPage(pageNum, pageSize);
        List<GroupMember> list = GroupMemberMapper.findList(params);
        PageInfo<GroupMember> pageInfo = new PageInfo<GroupMember>(list);
        return new PageUtils(list,(int)pageInfo.getTotal(),pageInfo.getPages(),pageInfo.getPageSize(),pageInfo.getPrePage() + 1);
    }

    @Override
    public GroupMember getById(Long id){
        return GroupMemberMapper.findById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(GroupMember GroupMember) {
        GroupMemberMapper.add(GroupMember);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(GroupMember GroupMember) {
        GroupMemberMapper.update(GroupMember);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeByIds(List ids) {
        GroupMemberMapper.deleteByIds(ids);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeById(Long id) {
        GroupMemberMapper.deleteById(id);
    }
}
