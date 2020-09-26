package com.hngf.service.risk.impl;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import java.util.Map;
import java.util.List;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hngf.common.utils.PageUtils;
import com.hngf.common.utils.Constant;

import com.hngf.mapper.risk.RiskPointScenePersonMapper;
import com.hngf.entity.risk.RiskPointScenePerson;
import com.hngf.service.risk.RiskPointScenePersonService;


@Service("RiskPointScenePersonService")
public class RiskPointScenePersonServiceImpl implements RiskPointScenePersonService {

    @Autowired
    private RiskPointScenePersonMapper RiskPointScenePersonMapper;

    @Override
    public PageUtils queryPage(Map<String, Object> params, int pageNum, int pageSize, String order) {
        if(StringUtils.isNotBlank(order)){
            PageHelper.startPage(pageNum, pageSize,order);
        }else{
            PageHelper.startPage(pageNum, pageSize);
        }
        List<RiskPointScenePerson> list = RiskPointScenePersonMapper.findByMap(params);
        PageInfo<RiskPointScenePerson> pageInfo = new PageInfo<RiskPointScenePerson>(list);
        return new PageUtils(list,(int)pageInfo.getTotal(),pageInfo.getPages(),pageInfo.getPageSize(),pageInfo.getPrePage() + 1);
    }

    @Override
    public List<RiskPointScenePerson> getByRiskPointId(Map<String, Object> map) {
        return RiskPointScenePersonMapper.findByMap(map);
    }

    @Override
    public RiskPointScenePerson getByAccountAndRiskpointId(Map<String, Object> params) {
        return RiskPointScenePersonMapper.findByAccountAndRiskpointId(params);
    }

    @Override
    public RiskPointScenePerson getById(Long id){
        return RiskPointScenePersonMapper.findById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(RiskPointScenePerson RiskPointScenePerson) {
        RiskPointScenePersonMapper.add(RiskPointScenePerson);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(RiskPointScenePerson RiskPointScenePerson) {
        RiskPointScenePersonMapper.update(RiskPointScenePerson);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeByIds(List ids) {
        RiskPointScenePersonMapper.deleteByIds(ids);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeById(Long id) {
        RiskPointScenePersonMapper.deleteById(id);
    }

    @Override
    public int removeByMap(Map<String, Object> params) {
        return RiskPointScenePersonMapper.deleteByMap(params);
    }

    @Override
    public int saveBatch(List<RiskPointScenePerson> list) {
        return RiskPointScenePersonMapper.addForeach(list);
    }
}
