package com.hngf.service.sys.impl;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import java.util.Map;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hngf.common.utils.PageUtils;

import com.hngf.mapper.sys.CompanySeoMapper;
import com.hngf.entity.sys.CompanySeo;
import com.hngf.service.sys.CompanySeoService;


@Service("CompanySeoService")
public class CompanySeoServiceImpl implements CompanySeoService {

    @Autowired
    private CompanySeoMapper CompanySeoMapper;

    @Override
    public PageUtils queryPage(Map<String, Object> params,Integer pageNum,Integer pageSize,String order) {
        //利用PageHelper分页查询 注意：这个一定要放查询语句的前一行,否则无法进行分页,因为它对紧随其后第一个sql语句有效
        if(StringUtils.isNotBlank(order)){
            PageHelper.startPage(pageNum, pageSize,order);
        }else{
            PageHelper.startPage(pageNum, pageSize);
        }
        List<CompanySeo> list = CompanySeoMapper.findList(params);
        PageInfo<CompanySeo> pageInfo = new PageInfo<CompanySeo>(list);
        return new PageUtils(list,(int)pageInfo.getTotal(),pageInfo.getPages(),pageInfo.getPageSize(),pageInfo.getPrePage() + 1);
    }

    @Override
    public CompanySeo getById(Long id){
        return CompanySeoMapper.findById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(CompanySeo CompanySeo) {
        CompanySeoMapper.add(CompanySeo);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(CompanySeo CompanySeo) {
        CompanySeoMapper.update(CompanySeo);
    }

}
