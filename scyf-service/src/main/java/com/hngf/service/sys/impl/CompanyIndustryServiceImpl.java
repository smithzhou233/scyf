package com.hngf.service.sys.impl;

import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.hngf.common.utils.R;
import com.hngf.mapper.sys.CompanyMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import java.util.Map;
import java.util.List;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hngf.common.utils.PageUtils;
import com.hngf.common.utils.Constant;

import com.hngf.mapper.sys.CompanyIndustryMapper;
import com.hngf.entity.sys.CompanyIndustry;
import com.hngf.service.sys.CompanyIndustryService;


@Service("CompanyIndustryService")
public class CompanyIndustryServiceImpl implements CompanyIndustryService {

    @Autowired
    private CompanyIndustryMapper  CompanyIndustryMapper;
    @Autowired
    private CompanyMapper companyMapper;
    @Value("${scyf.riskPoints}")
    private String riskPoints;
    @Value("${scyf.securityPoints}")
    private String securityPoints;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        int pageNum = null!= params.get("pageNum") ? Integer.parseInt(params.get("pageNum").toString()) : 1;
        int pageSize = null != params.get("pageSize") ? Integer.parseInt(params.get("pageSize").toString()) : Constant.PAGE_SIZE;
        //利用PageHelper分页查询 注意：这个一定要放查询语句的前一行,否则无法进行分页,因为它对紧随其后第一个sql语句有效
        PageHelper.startPage(pageNum, pageSize);
        List<CompanyIndustry> list = CompanyIndustryMapper.findList(params);
        PageInfo<CompanyIndustry> pageInfo = new PageInfo<CompanyIndustry>(list);
        return new PageUtils(list,(int)pageInfo.getTotal(),pageInfo.getPages(),pageInfo.getPageSize(),pageInfo.getPrePage() + 1);
    }

    @Override
    public CompanyIndustry getById(Long id){
        return CompanyIndustryMapper.findById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(CompanyIndustry CompanyIndustry) {
        CompanyIndustryMapper.add(CompanyIndustry);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(CompanyIndustry CompanyIndustry) {
        CompanyIndustryMapper.update(CompanyIndustry);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeByIds(List ids) {
        CompanyIndustryMapper.deleteByIds(ids);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeById(Long id) {
        CompanyIndustryMapper.deleteById(id);
    }


}
