package com.hngf.service.risk.impl;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import java.util.Map;
import java.util.List;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hngf.common.utils.PageUtils;
import com.hngf.common.utils.Constant;

import com.hngf.mapper.risk.RiskMeasureMapper;
import com.hngf.entity.risk.RiskMeasure;
import com.hngf.service.risk.RiskMeasureService;


@Service("RiskMeasureService")
public class RiskMeasureServiceImpl implements RiskMeasureService {

    @Autowired
    private RiskMeasureMapper RiskMeasureMapper;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        int pageNum = null!= params.get("pageNum") ? Integer.parseInt(params.get("pageNum").toString()) : 1;
        int pageSize = null != params.get("pageSize") ? Integer.parseInt(params.get("pageSize").toString()) : Constant.PAGE_SIZE;
        //利用PageHelper分页查询 注意：这个一定要放查询语句的前一行,否则无法进行分页,因为它对紧随其后第一个sql语句有效
        PageHelper.startPage(pageNum, pageSize);
        List<RiskMeasure> list = RiskMeasureMapper.findList(params);
        PageInfo<RiskMeasure> pageInfo = new PageInfo<RiskMeasure>(list);
        return new PageUtils(list,(int)pageInfo.getTotal(),pageInfo.getPages(),pageInfo.getPageSize(),pageInfo.getPrePage() + 1);
    }

    @Override
    public RiskMeasure getById(Long id){
        return RiskMeasureMapper.findById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(RiskMeasure RiskMeasure) {
        RiskMeasureMapper.add(RiskMeasure);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(RiskMeasure RiskMeasure) {
        RiskMeasureMapper.update(RiskMeasure);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeByIds(List ids) {
        RiskMeasureMapper.deleteByIds(ids);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeById(Long id) {
        RiskMeasureMapper.deleteById(id);
    }
}
