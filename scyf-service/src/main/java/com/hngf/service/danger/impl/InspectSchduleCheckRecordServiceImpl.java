package com.hngf.service.danger.impl;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import java.util.Map;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hngf.common.utils.PageUtils;

import com.hngf.mapper.danger.InspectSchduleCheckRecordMapper;
import com.hngf.entity.danger.InspectSchduleCheckRecord;
import com.hngf.service.danger.InspectSchduleCheckRecordService;


@Service("InspectSchduleCheckRecordService")
public class InspectSchduleCheckRecordServiceImpl implements InspectSchduleCheckRecordService {

    @Autowired
    private InspectSchduleCheckRecordMapper InspectSchduleCheckRecordMapper;

    @Override
    public PageUtils queryPage(Map<String, Object> params,Integer pageNum,Integer pageSize,String order) {
        //利用PageHelper分页查询 注意：这个一定要放查询语句的前一行,否则无法进行分页,因为它对紧随其后第一个sql语句有效
        if(StringUtils.isNotBlank(order)){
            PageHelper.startPage(pageNum, pageSize,order);
        }else{
            PageHelper.startPage(pageNum, pageSize);
        }
        List<InspectSchduleCheckRecord> list = InspectSchduleCheckRecordMapper.findList(params);
        PageInfo<InspectSchduleCheckRecord> pageInfo = new PageInfo<InspectSchduleCheckRecord>(list);
        return new PageUtils(list,(int)pageInfo.getTotal(),pageInfo.getPages(),pageInfo.getPageSize(),pageInfo.getPrePage() + 1);
    }

    @Override
    public List<InspectSchduleCheckRecord> getList(Long companyId, Long scheduleId, Long userId) {
        return InspectSchduleCheckRecordMapper.findByScheduleId(companyId,scheduleId,userId);
    }

    @Override
    public InspectSchduleCheckRecord getById(Long id){
        return InspectSchduleCheckRecordMapper.findById(id);
    }

    @Override
    public InspectSchduleCheckRecord getOne(Long companyId, Long scheduleId, Long riskPointId, Long userId, Integer checkedCount) {
        return InspectSchduleCheckRecordMapper.findOne(companyId,scheduleId,riskPointId,userId,checkedCount);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(InspectSchduleCheckRecord InspectSchduleCheckRecord) {
        InspectSchduleCheckRecordMapper.add(InspectSchduleCheckRecord);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(InspectSchduleCheckRecord InspectSchduleCheckRecord) {
        InspectSchduleCheckRecordMapper.update(InspectSchduleCheckRecord);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeByIds(List ids) {
        InspectSchduleCheckRecordMapper.deleteByIds(ids);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeById(Long id) {
        InspectSchduleCheckRecordMapper.deleteById(id);
    }
}
