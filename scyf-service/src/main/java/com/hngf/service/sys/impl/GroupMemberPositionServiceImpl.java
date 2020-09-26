package com.hngf.service.sys.impl;

import com.hngf.dto.sys.GroupMemberPositionDto;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.List;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hngf.common.utils.PageUtils;
import com.hngf.common.utils.Constant;

import com.hngf.mapper.sys.GroupMemberPositionMapper;
import com.hngf.entity.sys.GroupMemberPosition;
import com.hngf.service.sys.GroupMemberPositionService;


@Service("GroupMemberPositionService")
public class GroupMemberPositionServiceImpl implements GroupMemberPositionService {

    @Autowired
    private GroupMemberPositionMapper GroupMemberPositionMapper;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        int pageNum = null!= params.get("pageNum") ? Integer.parseInt(params.get("pageNum").toString()) : 1;
        int pageSize = null != params.get("pageSize") ? Integer.parseInt(params.get("pageSize").toString()) : Constant.PAGE_SIZE;
        //利用PageHelper分页查询 注意：这个一定要放查询语句的前一行,否则无法进行分页,因为它对紧随其后第一个sql语句有效
        PageHelper.startPage(pageNum, pageSize);
        List<GroupMemberPosition> list = GroupMemberPositionMapper.findList(params);
        PageInfo<GroupMemberPosition> pageInfo = new PageInfo<GroupMemberPosition>(list);
        return new PageUtils(list,(int)pageInfo.getTotal(),pageInfo.getPages(),pageInfo.getPageSize(),pageInfo.getPrePage() + 1);
    }

    @Override
    public GroupMemberPosition getById(Long id){
        return GroupMemberPositionMapper.findById(id);
    }

    @Override
    public GroupMemberPositionDto getByUserIdAndCompanyId(Long userId, Long companyId) {
        return GroupMemberPositionMapper.findGrantGroupByUserId(userId,companyId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(GroupMemberPosition GroupMemberPosition) {
        GroupMemberPositionMapper.add(GroupMemberPosition);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(GroupMemberPosition GroupMemberPosition) {
        GroupMemberPositionMapper.update(GroupMemberPosition);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeByIds(List ids) {
        GroupMemberPositionMapper.deleteByIds(ids);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeById(Long id) {
        GroupMemberPositionMapper.deleteById(id);
    }

    @Override
    public List<GroupMemberPositionDto> getGroupMemberPositionList(Map<String, Object> map) {
        List<GroupMemberPositionDto> groupMemberPositionList = GroupMemberPositionMapper.getGroupMemberPositionList(map);
        return groupMemberPositionList;
    }
}
