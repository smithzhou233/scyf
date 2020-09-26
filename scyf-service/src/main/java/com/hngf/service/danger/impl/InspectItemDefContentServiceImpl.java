package com.hngf.service.danger.impl;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import java.util.Map;
import java.util.List;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hngf.common.utils.PageUtils;
import com.hngf.common.utils.Constant;

import com.hngf.mapper.danger.InspectItemDefContentMapper;
import com.hngf.entity.danger.InspectItemDefContent;
import com.hngf.service.danger.InspectItemDefContentService;


@Service("InspectItemDefContentService")
public class InspectItemDefContentServiceImpl implements InspectItemDefContentService {

    @Autowired
    private InspectItemDefContentMapper InspectItemDefContentMapper;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        int pageNum = null!= params.get("pageNum") ? Integer.parseInt(params.get("pageNum").toString()) : 1;
        int pageSize = null != params.get("pageSize") ? Integer.parseInt(params.get("pageSize").toString()) : Constant.PAGE_SIZE;
        //利用PageHelper分页查询 注意：这个一定要放查询语句的前一行,否则无法进行分页,因为它对紧随其后第一个sql语句有效
        PageHelper.startPage(pageNum, pageSize);
        List<InspectItemDefContent> list = InspectItemDefContentMapper.findList(params);
        PageInfo<InspectItemDefContent> pageInfo = new PageInfo<InspectItemDefContent>(list);
        return new PageUtils(list,(int)pageInfo.getTotal(),pageInfo.getPages(),pageInfo.getPageSize(),pageInfo.getPrePage() + 1);
    }

    @Override
    public InspectItemDefContent getById(Long id){
        return InspectItemDefContentMapper.findById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(InspectItemDefContent InspectItemDefContent) {
        InspectItemDefContentMapper.add(InspectItemDefContent);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(InspectItemDefContent InspectItemDefContent) {
        InspectItemDefContentMapper.update(InspectItemDefContent);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeByIds(List ids) {
        InspectItemDefContentMapper.deleteByIds(ids);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeById(Long id) {
        InspectItemDefContentMapper.deleteById(id);
    }
}
