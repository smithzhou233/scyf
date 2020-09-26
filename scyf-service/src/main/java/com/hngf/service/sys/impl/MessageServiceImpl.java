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

import com.hngf.mapper.sys.MessageMapper;
import com.hngf.entity.sys.Message;
import com.hngf.service.sys.MessageService;


@Service("MessageService")
public class MessageServiceImpl implements MessageService {

    @Autowired
    private MessageMapper messageMapper;

    @Override
    public PageUtils queryPage(Map<String, Object> params,Integer pageNum,Integer pageSize,String order) {
        //利用PageHelper分页查询 注意：这个一定要放查询语句的前一行,否则无法进行分页,因为它对紧随其后第一个sql语句有效
        if(StringUtils.isNotBlank(order)){
            PageHelper.startPage(pageNum, pageSize,order);
        }else{
            PageHelper.startPage(pageNum, pageSize);
        }
        List<Message> list = messageMapper.findList(params);
        PageInfo<Message> pageInfo = new PageInfo<Message>(list);
        return new PageUtils(list,(int)pageInfo.getTotal(),pageInfo.getPages(),pageInfo.getPageSize(),pageInfo.getPrePage() + 1);
    }

    @Override
    public List<Message> getList(Map<String, Object> params) {
        return messageMapper.findByMap(params);
    }

    @Override
    public Integer getMessageCount(Map<String, Object> params) {
        return messageMapper.findMessageCount(params);
    }

    @Override
    public Integer setRead(Map<String, Object> params) {
        return messageMapper.setRead(params);
    }

    @Override
    public Message getById(Long id){
        return messageMapper.findById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(Message Message) {
        messageMapper.add(Message);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(Message Message) {
        messageMapper.update(Message);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeByIds(List ids) {
        messageMapper.deleteByIds(ids);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeById(Long id) {
        messageMapper.deleteById(id);
    }
}
