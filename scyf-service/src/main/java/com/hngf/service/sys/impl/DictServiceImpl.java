package com.hngf.service.sys.impl;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;
import java.util.List;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hngf.common.utils.PageUtils;
import com.hngf.common.utils.Constant;

import com.hngf.mapper.sys.DictMapper;
import com.hngf.entity.sys.Dict;
import com.hngf.service.sys.DictService;


@Service("DictService")
public class DictServiceImpl implements DictService {

    @Autowired
    private DictMapper dictMapper;

    @Override
    public PageUtils queryPage(Map<String, Object> params,Integer pageNum,Integer pageSize,String order) {
        //利用PageHelper分页查询 注意：这个一定要放查询语句的前一行,否则无法进行分页,因为它对紧随其后第一个sql语句有效
        if(StringUtils.isNotBlank(order)){
            PageHelper.startPage(pageNum, pageSize,order);
        }else{
            PageHelper.startPage(pageNum, pageSize);
        }
        List<Dict> list = dictMapper.findList(params);
        PageInfo<Dict> pageInfo = new PageInfo<Dict>(list);
        return new PageUtils(list,(int)pageInfo.getTotal(),pageInfo.getPages(),pageInfo.getPageSize(),pageInfo.getPrePage() + 1);
    }

    @Override
    public Dict getById(String id){
        return dictMapper.findById(id);
    }

    @Override
    public List<Dict> getByDictType(String dictType) {
        Map<String, Object> params = new HashMap<>();
        params.put("dictType", dictType);
        return dictMapper.findByMap(params);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(Dict Dict) {
        dictMapper.add(Dict);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(Dict Dict) {
        dictMapper.update(Dict);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeByIds(List ids) {
        dictMapper.deleteByIds(ids);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeById(Long id) {
        dictMapper.deleteById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long getTabId(String tabName) {
        Long tabId= dictMapper.getTabId(tabName);
        if(tabId!=null){
            Long newTabId=tabId+1;
            dictMapper.setTabId(tabName,tabId + 1);
            return  newTabId;
        }
        dictMapper.insertTab(tabName,1);
        return 1L;
    }
}
