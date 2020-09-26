package com.hngf.service.risk.impl;

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

import com.hngf.mapper.risk.RiskPointGuideRelMapper;
import com.hngf.entity.risk.RiskPointGuideRel;
import com.hngf.service.risk.RiskPointGuideRelService;


@Service("RiskPointGuideRelService")
public class RiskPointGuideRelServiceImpl implements RiskPointGuideRelService {

    @Autowired
    private RiskPointGuideRelMapper RiskPointGuideRelMapper;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        int pageNum = null!= params.get("pageNum") ? Integer.parseInt(params.get("pageNum").toString()) : 1;
        int pageSize = null != params.get("pageSize") ? Integer.parseInt(params.get("pageSize").toString()) : Constant.PAGE_SIZE;
        //利用PageHelper分页查询 注意：这个一定要放查询语句的前一行,否则无法进行分页,因为它对紧随其后第一个sql语句有效
        PageHelper.startPage(pageNum, pageSize);
        List<RiskPointGuideRel> list = RiskPointGuideRelMapper.findList(params);
        PageInfo<RiskPointGuideRel> pageInfo = new PageInfo<RiskPointGuideRel>(list);
        return new PageUtils(list,(int)pageInfo.getTotal(),pageInfo.getPages(),pageInfo.getPageSize(),pageInfo.getPrePage() + 1);
    }

    @Override
    public RiskPointGuideRel getById(Long id){
        return RiskPointGuideRelMapper.findById(id);
    }

    @Override
    public List<Long> getPostIdsByRiskPoint(Long riskPointId) {
        return RiskPointGuideRelMapper.findPostIdsByRiskPoint(riskPointId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(RiskPointGuideRel RiskPointGuideRel) {
        RiskPointGuideRelMapper.add(RiskPointGuideRel);
    }

    @Override
    public int saveBatch(List<RiskPointGuideRel> list) {
        return RiskPointGuideRelMapper.addForeach(list);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(RiskPointGuideRel RiskPointGuideRel) {
        RiskPointGuideRelMapper.update(RiskPointGuideRel);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeByIds(List ids) {
        RiskPointGuideRelMapper.deleteByIds(ids);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeById(Long id) {
        RiskPointGuideRelMapper.deleteById(id);
    }

    @Override
    public int removeByPointIdAndPostId(Long riskPointId, Long postId,Long companyId) {
        Map<String, Object> params = new HashMap<>();
        params.put("riskPointId", riskPointId);
        params.put("postId", postId);
        params.put("companyId", companyId);
        return RiskPointGuideRelMapper.deleteByMap(params);
    }
}
