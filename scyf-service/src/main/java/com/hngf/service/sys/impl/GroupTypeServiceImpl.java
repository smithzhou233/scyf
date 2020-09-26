package com.hngf.service.sys.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hngf.common.exception.ScyfException;
import com.hngf.common.utils.Constant;
import com.hngf.common.utils.PageUtils;
import com.hngf.entity.sys.GroupType;
import com.hngf.mapper.sys.GroupMapper;
import com.hngf.mapper.sys.GroupTypeMapper;
import com.hngf.service.sys.GroupTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;


@Service("GroupTypeService")
public class GroupTypeServiceImpl implements GroupTypeService {

    @Autowired
    private GroupTypeMapper GroupTypeMapper;
    @Autowired
    private GroupMapper groupMapper;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        int pageNum = null!= params.get("pageNum") ? Integer.parseInt(params.get("pageNum").toString()) : 1;
        int pageSize = null != params.get("pageSize") ? Integer.parseInt(params.get("pageSize").toString()) : Constant.PAGE_SIZE;
        //利用PageHelper分页查询 注意：这个一定要放查询语句的前一行,否则无法进行分页,因为它对紧随其后第一个sql语句有效
        PageHelper.startPage(pageNum, pageSize);
        List<GroupType> list = GroupTypeMapper.findList(params);
        PageInfo<GroupType> pageInfo = new PageInfo<GroupType>(list);
        return new PageUtils(list,(int)pageInfo.getTotal(),pageInfo.getPages(),pageInfo.getPageSize(),pageInfo.getPrePage() + 1);
    }

    @Override
    public List<GroupType> getList(Long companyId) {
        return GroupTypeMapper.findAll(companyId);
    }

    @Override
    public GroupType getById(Long id){
        return GroupTypeMapper.findById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(GroupType GroupType) {
        GroupTypeMapper.add(GroupType);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(GroupType GroupType) {
        GroupTypeMapper.update(GroupType);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeByIds(List ids) {
        GroupTypeMapper.deleteByIds(ids);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeById(Long id, Long updatedBy) {
        if(null == id || 0 == id.intValue()){
            throw new ScyfException("部门分类Id不能为空");
        }
        Integer usedNumber = groupMapper.findCountByGroupTypeId(id);
        if(null == usedNumber || 0== usedNumber.intValue()){
            GroupTypeMapper.deleteById(id,updatedBy);
        }else {
            throw new ScyfException("该部门类型已被使用，不能删除");
        }

    }
}
