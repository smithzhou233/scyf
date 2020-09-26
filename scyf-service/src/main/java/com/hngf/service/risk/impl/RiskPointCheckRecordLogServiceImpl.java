package com.hngf.service.risk.impl;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import java.util.Map;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hngf.common.utils.PageUtils;

import com.hngf.mapper.risk.RiskPointCheckRecordLogMapper;
import com.hngf.entity.risk.RiskPointCheckRecordLog;
import com.hngf.service.risk.RiskPointCheckRecordLogService;


@Service("RiskPointCheckRecordLogService")
public class RiskPointCheckRecordLogServiceImpl implements RiskPointCheckRecordLogService {

    @Autowired
    private RiskPointCheckRecordLogMapper RiskPointCheckRecordLogMapper;

    @Override
    public PageUtils queryPage(Map<String, Object> params,Integer pageNum,Integer pageSize,String order) {
        //利用PageHelper分页查询 注意：这个一定要放查询语句的前一行,否则无法进行分页,因为它对紧随其后第一个sql语句有效
        if(StringUtils.isNotBlank(order)){
            PageHelper.startPage(pageNum, pageSize,order);
        }else{
            PageHelper.startPage(pageNum, pageSize);
        }
        List<RiskPointCheckRecordLog> list = RiskPointCheckRecordLogMapper.findList(params);
        PageInfo<RiskPointCheckRecordLog> pageInfo = new PageInfo<RiskPointCheckRecordLog>(list);
        return new PageUtils(list,(int)pageInfo.getTotal(),pageInfo.getPages(),pageInfo.getPageSize(),pageInfo.getPrePage() + 1);
    }

    @Override
    public RiskPointCheckRecordLog getById(Long id){
        return RiskPointCheckRecordLogMapper.findById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(RiskPointCheckRecordLog RiskPointCheckRecordLog) {
        RiskPointCheckRecordLogMapper.add(RiskPointCheckRecordLog);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(RiskPointCheckRecordLog RiskPointCheckRecordLog) {
        RiskPointCheckRecordLogMapper.update(RiskPointCheckRecordLog);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeByIds(List ids) {
        RiskPointCheckRecordLogMapper.deleteByIds(ids);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeById(Long id) {
        RiskPointCheckRecordLogMapper.deleteById(id);
    }
}
