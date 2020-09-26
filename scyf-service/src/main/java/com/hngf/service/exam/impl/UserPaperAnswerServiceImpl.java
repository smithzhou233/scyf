package com.hngf.service.exam.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hngf.common.utils.PageUtils;
import com.hngf.entity.exam.UserPaperAnswer;
import com.hngf.mapper.exam.UserPaperAnswerMapper;
import com.hngf.service.exam.UserPaperAnswerService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;


@Service("userPaperAnswerService")
public class UserPaperAnswerServiceImpl implements UserPaperAnswerService {

    @Autowired
    private UserPaperAnswerMapper userPaperAnswerMapper;

    @Override
    public PageUtils queryPage(Map<String, Object> params,Integer pageNum,Integer pageSize,String order) {
        //利用PageHelper分页查询 注意：这个一定要放查询语句的前一行,否则无法进行分页,因为它对紧随其后第一个sql语句有效
        if(StringUtils.isNotBlank(order)){
            PageHelper.startPage(pageNum, pageSize,order);
        }else{
            PageHelper.startPage(pageNum, pageSize);
        }
        List<UserPaperAnswer> list = userPaperAnswerMapper.findList(params);
        PageInfo<UserPaperAnswer> pageInfo = new PageInfo<UserPaperAnswer>(list);
        return new PageUtils(list,(int)pageInfo.getTotal(),pageInfo.getPages(),pageInfo.getPageSize(),pageInfo.getPrePage() + 1);
    }

    @Override
    public UserPaperAnswer getById(Long id){
        if(null != id ){return userPaperAnswerMapper.findById(id);}
        return null ;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(UserPaperAnswer userPaperAnswer) {
        userPaperAnswerMapper.add(userPaperAnswer);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(UserPaperAnswer userPaperAnswer) {
        if(null != userPaperAnswer && null != userPaperAnswer.getUserPaperAnswerId() ){userPaperAnswerMapper.update(userPaperAnswer);}
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeByIds(List ids, Long updatedBy) {
        if(null != ids && ids.size()>0){userPaperAnswerMapper.deleteByIds(ids, updatedBy);}
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeById(Long id, Long updatedBy) {
        if(null != id){userPaperAnswerMapper.deleteById(id, updatedBy);}
    }
}
