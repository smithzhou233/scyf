package com.hngf.service.risk.impl;

import com.hngf.service.risk.RiskCtrlService;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Map;
import java.util.List;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hngf.common.utils.PageUtils;
import com.hngf.common.utils.Constant;

import com.hngf.mapper.risk.RiskPointDangerSourceMapper;
import com.hngf.entity.risk.RiskPointDangerSource;
import com.hngf.service.risk.RiskPointDangerSourceService;


@Service("RiskPointDangerSourceService")
public class RiskPointDangerSourceServiceImpl implements RiskPointDangerSourceService {

    @Autowired
    private RiskPointDangerSourceMapper RiskPointDangerSourceMapper;
    @Autowired
    private RiskCtrlService riskCtrlService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        int pageNum = null!= params.get("pageNum") ? Integer.parseInt(params.get("pageNum").toString()) : 1;
        int pageSize = null != params.get("pageSize") ? Integer.parseInt(params.get("pageSize").toString()) : Constant.PAGE_SIZE;
        //利用PageHelper分页查询 注意：这个一定要放查询语句的前一行,否则无法进行分页,因为它对紧随其后第一个sql语句有效
        PageHelper.startPage(pageNum, pageSize);
        List<RiskPointDangerSource> list = RiskPointDangerSourceMapper.findList(params);
        PageInfo<RiskPointDangerSource> pageInfo = new PageInfo<RiskPointDangerSource>(list);
        return new PageUtils(list,(int)pageInfo.getTotal(),pageInfo.getPages(),pageInfo.getPageSize(),pageInfo.getPrePage() + 1);
    }

    @Override
    public RiskPointDangerSource getById(Long id){
        return RiskPointDangerSourceMapper.findById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(RiskPointDangerSource RiskPointDangerSource) {
        RiskPointDangerSourceMapper.add(RiskPointDangerSource);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(RiskPointDangerSource RiskPointDangerSource) {
        RiskPointDangerSourceMapper.update(RiskPointDangerSource);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeByIds(List ids) {
        RiskPointDangerSourceMapper.deleteByIds(ids);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeById(Long id) {
        RiskPointDangerSourceMapper.deleteById(id);
    }

    @Override
    public int saveBatch(List<RiskPointDangerSource> list) {
        return RiskPointDangerSourceMapper.addForeach(list);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int removeByMap(Map<String, Object> params) {
        return RiskPointDangerSourceMapper.deleteByMap(params);
    }

    @Override
    public PageUtils findRiskAndRiskSourceList(Map<String, Object> params) {
        int pageNum = null!= params.get("pageNum") ? Integer.parseInt(params.get("pageNum").toString()) : 1;
        int pageSize = null != params.get("pageSize") ? Integer.parseInt(params.get("pageSize").toString()) : Constant.PAGE_SIZE;
        PageHelper.startPage(pageNum, pageSize);
        //根据岗位ID 企业ID查看管控层级
        Integer userCtlLevel = this.riskCtrlService.getCurrentUserCtlLevel(params);
        List  riskAndRiskSourceList;
        if (userCtlLevel != null && !userCtlLevel.equals(0)) {
            params.put("ctlLevel", userCtlLevel);
            riskAndRiskSourceList = this.RiskPointDangerSourceMapper.selectRiskAndRiskSourceList(params);
        } else {
            riskAndRiskSourceList = new ArrayList();
        }
        PageInfo<RiskPointDangerSource> pageInfo = new PageInfo<RiskPointDangerSource>(riskAndRiskSourceList);
        return new PageUtils( riskAndRiskSourceList,(int)pageInfo.getTotal(),pageInfo.getPages(),pageInfo.getPageSize(),pageInfo.getPrePage() + 1);
    }
}
