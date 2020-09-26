package com.hngf.service.score.impl;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import java.util.Map;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hngf.common.utils.PageUtils;

import com.hngf.mapper.score.ScoreSettingMapper;
import com.hngf.entity.score.ScoreSetting;
import com.hngf.service.score.ScoreSettingService;


@Service("ScoreSettingService")
public class ScoreSettingServiceImpl implements ScoreSettingService {

    @Autowired
    private ScoreSettingMapper ScoreSettingMapper;

    @Override
    public PageUtils queryPage(Map<String, Object> params,Integer pageNum,Integer pageSize,String order) {
        //利用PageHelper分页查询 注意：这个一定要放查询语句的前一行,否则无法进行分页,因为它对紧随其后第一个sql语句有效
        if(StringUtils.isNotBlank(order)){
            PageHelper.startPage(pageNum, pageSize,order);
        }else{
            PageHelper.startPage(pageNum, pageSize);
        }
        List<ScoreSetting> list = ScoreSettingMapper.findList(params);
        PageInfo<ScoreSetting> pageInfo = new PageInfo<ScoreSetting>(list);
        return new PageUtils(list,(int)pageInfo.getTotal(),pageInfo.getPages(),pageInfo.getPageSize(),pageInfo.getPrePage() + 1);
    }

    @Override
    public ScoreSetting getById(Long id){
        return ScoreSettingMapper.findById(id);
    }

    @Override
    public ScoreSetting getBySettingCode(Long companyId, Integer settingCode) {
        return ScoreSettingMapper.findBySettingCode(companyId,settingCode);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(ScoreSetting ScoreSetting) {
        ScoreSettingMapper.add(ScoreSetting);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(ScoreSetting ScoreSetting) {
        ScoreSettingMapper.update(ScoreSetting);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeByIds(List ids) {
        ScoreSettingMapper.deleteByIds(ids);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeById(Long id) {
        ScoreSettingMapper.deleteById(id);
    }
}
