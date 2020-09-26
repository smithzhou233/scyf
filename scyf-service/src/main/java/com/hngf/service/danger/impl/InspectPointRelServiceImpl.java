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

import com.hngf.mapper.danger.InspectPointRelMapper;
import com.hngf.entity.danger.InspectPointRel;
import com.hngf.service.danger.InspectPointRelService;


@Service("InspectPointRelService")
public class InspectPointRelServiceImpl implements InspectPointRelService {

    @Autowired
    private InspectPointRelMapper InspectPointRelMapper;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        int pageNum = null!= params.get("pageNum") ? Integer.parseInt(params.get("pageNum").toString()) : 1;
        int pageSize = null != params.get("pageSize") ? Integer.parseInt(params.get("pageSize").toString()) : Constant.PAGE_SIZE;
        //利用PageHelper分页查询 注意：这个一定要放查询语句的前一行,否则无法进行分页,因为它对紧随其后第一个sql语句有效
        PageHelper.startPage(pageNum, pageSize);
        List<InspectPointRel> list = InspectPointRelMapper.findList(params);
        PageInfo<InspectPointRel> pageInfo = new PageInfo<InspectPointRel>(list);
        return new PageUtils(list,(int)pageInfo.getTotal(),pageInfo.getPages(),pageInfo.getPageSize(),pageInfo.getPrePage() + 1);
    }

    @Override
    public InspectPointRel getById(Long id){
        return InspectPointRelMapper.findById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(InspectPointRel InspectPointRel) {
        InspectPointRelMapper.add(InspectPointRel);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(InspectPointRel InspectPointRel) {
        InspectPointRelMapper.update(InspectPointRel);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeByIds(List ids) {
        InspectPointRelMapper.deleteByIds(ids);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeById(Long id) {
        InspectPointRelMapper.deleteById(id);
    }

    @Override
    public int removeByMap(Map<String, Object> params) {
        return InspectPointRelMapper.deleteByMap(params);
    }

    @Override
    public int saveBatch(List<InspectPointRel> list) {
        return InspectPointRelMapper.addForeach(list);
    }
}
