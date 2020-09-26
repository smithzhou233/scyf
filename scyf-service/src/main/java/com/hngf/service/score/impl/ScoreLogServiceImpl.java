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

import com.hngf.mapper.score.ScoreLogMapper;
import com.hngf.entity.score.ScoreLog;
import com.hngf.service.score.ScoreLogService;


@Service("ScoreLogService")
public class ScoreLogServiceImpl implements ScoreLogService {

    @Autowired
    private ScoreLogMapper ScoreLogMapper;

    @Override
    public PageUtils queryPage(Map<String, Object> params,Integer pageNum,Integer pageSize,String order) {
        //利用PageHelper分页查询 注意：这个一定要放查询语句的前一行,否则无法进行分页,因为它对紧随其后第一个sql语句有效
        if(StringUtils.isNotBlank(order)){
            PageHelper.startPage(pageNum, pageSize,order);
        }else{
            PageHelper.startPage(pageNum, pageSize);
        }
        List<ScoreLog> list = ScoreLogMapper.findList(params);
        PageInfo<ScoreLog> pageInfo = new PageInfo<ScoreLog>(list);
        return new PageUtils(list,(int)pageInfo.getTotal(),pageInfo.getPages(),pageInfo.getPageSize(),pageInfo.getPrePage() + 1);
    }

    @Override
    public ScoreLog getById(Long id){
        return ScoreLogMapper.findById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(ScoreLog ScoreLog) {
        ScoreLogMapper.add(ScoreLog);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(ScoreLog ScoreLog) {
        ScoreLogMapper.update(ScoreLog);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeByIds(List ids) {
        ScoreLogMapper.deleteByIds(ids);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeById(Long id) {
        ScoreLogMapper.deleteById(id);
    }
}
