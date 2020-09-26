package com.hngf.service.sys.impl;

import com.hngf.entity.sys.Industry;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import java.util.Map;
import java.util.List;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hngf.common.utils.PageUtils;
import com.hngf.common.utils.Constant;

import com.hngf.mapper.sys.OrgIndustryMapper;
import com.hngf.entity.sys.OrgIndustry;
import com.hngf.service.sys.OrgIndustryService;


@Service("OrgIndustryService")
public class OrgIndustryServiceImpl implements OrgIndustryService {

    @Autowired
    private OrgIndustryMapper OrgIndustryMapper;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        int pageNum = null!= params.get("pageNum") ? Integer.parseInt(params.get("pageNum").toString()) : 1;
        int pageSize = null != params.get("pageSize") ? Integer.parseInt(params.get("pageSize").toString()) : Constant.PAGE_SIZE;
        //利用PageHelper分页查询 注意：这个一定要放查询语句的前一行,否则无法进行分页,因为它对紧随其后第一个sql语句有效
        PageHelper.startPage(pageNum, pageSize);
        List<OrgIndustry> list = OrgIndustryMapper.findList(params);
        PageInfo<OrgIndustry> pageInfo = new PageInfo<OrgIndustry>(list);
        return new PageUtils(list,(int)pageInfo.getTotal(),pageInfo.getPages(),pageInfo.getPageSize(),pageInfo.getPrePage() + 1);
    }

    @Override
    public OrgIndustry getById(Long id){
        return OrgIndustryMapper.findById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(OrgIndustry OrgIndustry) {
        OrgIndustryMapper.add(OrgIndustry);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(OrgIndustry OrgIndustry) {
        OrgIndustryMapper.update(OrgIndustry);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeByIds(List ids) {
        OrgIndustryMapper.deleteByIds(ids);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeById(Long id) {
        OrgIndustryMapper.deleteById(id);
    }
    /**
     * @Author: zyj
     * @Description:查询所选中组织下的行业
     * @Param
     * @Date 17:46 2020/6/4
     */
    @Override
    public List<OrgIndustry> orgIndustryChecked(Long orgId) {
        return OrgIndustryMapper.orgIndustryChecked(orgId);
    }

    @Override
    public List<Industry> selectIndustryCodeByPid(Long orgId) {
        return OrgIndustryMapper.selectIndustryCodeByPid(orgId);
    }

    @Override
    public String selectIndustryCodeByOrgId(Long orgId) {
        return OrgIndustryMapper.selectIndustryCodeByOrgId(orgId);
    }

}
