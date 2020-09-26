package com.hngf.service.risk.impl;

import com.alibaba.fastjson.JSONObject;
import com.hngf.entity.risk.Risk;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hngf.common.utils.PageUtils;
import com.hngf.common.utils.Constant;

import com.hngf.mapper.risk.RiskRecordMapper;
import com.hngf.entity.risk.RiskRecord;
import com.hngf.service.risk.RiskRecordService;


@Service("RiskRecordService")
public class RiskRecordServiceImpl implements RiskRecordService {

    @Autowired
    private RiskRecordMapper RiskRecordMapper;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        int pageNum = null!= params.get("pageNum") ? Integer.parseInt(params.get("pageNum").toString()) : 1;
        int pageSize = null != params.get("pageSize") ? Integer.parseInt(params.get("pageSize").toString()) : Constant.PAGE_SIZE;
        //利用PageHelper分页查询 注意：这个一定要放查询语句的前一行,否则无法进行分页,因为它对紧随其后第一个sql语句有效
        PageHelper.startPage(pageNum, pageSize);
        List<RiskRecord> list = RiskRecordMapper.findList(params);
        PageInfo<RiskRecord> pageInfo = new PageInfo<RiskRecord>(list);
        return new PageUtils(list,(int)pageInfo.getTotal(),pageInfo.getPages(),pageInfo.getPageSize(),pageInfo.getPrePage() + 1);
    }

    @Override
    public List<Risk> getRiskByRiskPointId(Long riskPointId) {
        return RiskRecordMapper.findRiskByRiskPointId(riskPointId);
    }

    @Override
    public List<Map<String, Object>> getMeasureList(Long riskPointId) {
        List<Map<String, Object>> mapList = RiskRecordMapper.findMeasureList(riskPointId);
        Set<Long> dsId = new HashSet();
        mapList.forEach((e) -> {
            dsId.add(Long.valueOf(e.get("riskDangerId").toString()));
        });
        List<Long> list = new ArrayList();
        list.addAll(dsId);
        List<Map<String, Object>> out = new ArrayList();
        mapList.forEach((e) -> {
            if (list.contains(Long.valueOf(e.get("riskDangerId").toString()))) {
                JSONObject one = new JSONObject();
                one.put("riskDangerId", e.get("riskDangerId"));
                one.put("rootName", e.get("rootName"));
                out.add(one);
                list.remove(Long.valueOf(e.get("riskDangerId").toString()));
            }

        });
        out.forEach((e) -> {
            String dsId_ = e.get("riskDangerId").toString();
            List<Map<String, Object>> collect = (List)mapList.stream().filter((e1) -> {
                return dsId_.equals(e1.get("riskDangerId").toString());
            }).collect(Collectors.toList());
            e.put("list", collect);
        });
        return out;
    }

    @Override
    public RiskRecord getById(Long id){
        return RiskRecordMapper.findById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(RiskRecord RiskRecord) {
        RiskRecordMapper.add(RiskRecord);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(RiskRecord RiskRecord) {
        RiskRecordMapper.update(RiskRecord);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeByIds(List ids) {
        RiskRecordMapper.deleteByIds(ids);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeById(Long id) {
        RiskRecordMapper.deleteById(id);
    }

    @Override
    public int saveBatch(List<RiskRecord> list) {
        return RiskRecordMapper.saveBatch(list);
    }

    @Override
    public int removeByMap(Map<String, Object> params) {
        return RiskRecordMapper.deleteByMap(params);
    }
}
