package com.hngf.service.risk.impl;

import com.hngf.entity.danger.InspectSchduleCheckRecord;
import com.hngf.entity.risk.RiskPointCheckRecordLog;
import com.hngf.mapper.risk.RiskPointCheckRecordLogMapper;
import com.hngf.service.danger.InspectSchduleCheckRecordService;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Map;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hngf.common.utils.PageUtils;

import com.hngf.mapper.risk.RiskPointCheckRecordMapper;
import com.hngf.entity.risk.RiskPointCheckRecord;
import com.hngf.service.risk.RiskPointCheckRecordService;


@Service("RiskPointCheckRecordService")
public class RiskPointCheckRecordServiceImpl implements RiskPointCheckRecordService {

    @Autowired
    private RiskPointCheckRecordMapper riskPointCheckRecordMapper;
    @Autowired
    private RiskPointCheckRecordLogMapper riskPointCheckRecordLogMapper;
    @Autowired
    private InspectSchduleCheckRecordService inspectSchduleCheckRecordService;

    @Override
    public PageUtils queryPage(Map<String, Object> params,Integer pageNum,Integer pageSize,String order) {
        //利用PageHelper分页查询 注意：这个一定要放查询语句的前一行,否则无法进行分页,因为它对紧随其后第一个sql语句有效
        if(StringUtils.isNotBlank(order)){
            PageHelper.startPage(pageNum, pageSize,order);
        }else{
            PageHelper.startPage(pageNum, pageSize);
        }
        List<RiskPointCheckRecord> list = riskPointCheckRecordMapper.findList(params);
        PageInfo<RiskPointCheckRecord> pageInfo = new PageInfo<RiskPointCheckRecord>(list);
        return new PageUtils(list,(int)pageInfo.getTotal(),pageInfo.getPages(),pageInfo.getPageSize(),pageInfo.getPrePage() + 1);
    }

    @Override
    public PageUtils getListPage(Map<String, Object> params, Integer pageNum, Integer pageSize, String order) {
        //利用PageHelper分页查询 注意：这个一定要放查询语句的前一行,否则无法进行分页,因为它对紧随其后第一个sql语句有效
        if(StringUtils.isNotBlank(order)){
            PageHelper.startPage(pageNum, pageSize,order);
        }else{
            PageHelper.startPage(pageNum, pageSize);
        }
        List<RiskPointCheckRecord> list = riskPointCheckRecordMapper.findByMap(params);
        PageInfo<RiskPointCheckRecord> pageInfo = new PageInfo<>(list);
        return new PageUtils(list,(int)pageInfo.getTotal(),pageInfo.getPages(),pageInfo.getPageSize(),pageInfo.getPrePage() + 1);
    }

    @Override
    public RiskPointCheckRecord getById(Long id){
        return riskPointCheckRecordMapper.findById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(RiskPointCheckRecord record) {
        riskPointCheckRecordMapper.add(record);

        //同步保存风险点检查记录日志表
        RiskPointCheckRecordLog log = new RiskPointCheckRecordLog();
        log.setRecordId(record.getRecordId());
        log.setInspectDefId(record.getInspectDefId());
        log.setCheckRecordNo(record.getCheckRecordNo());
        log.setCheckedCount(record.getCheckedCount());
        log.setCompanyId(record.getCompanyId());
        log.setGroupId(record.getGroupId());
        log.setPositionId(record.getPositionId());
        log.setRiskPointId(record.getRiskPointId());
        log.setInspectScheduleId(record.getInspectScheduleId());
        log.setResult(record.getResult());
        log.setAddress(record.getAddress());
        log.setPhoneCode(record.getPhoneCode());
        log.setLongitude(record.getLongitude());
        log.setLatitude(record.getLatitude());
        log.setStatus(record.getStatus());
        log.setCreatedBy(record.getCreatedBy());
        log.setCreatedTime(record.getCreatedTime());
        log.setEvaluateDesc(record.getEvaluateDesc());
        this.riskPointCheckRecordLogMapper.add(log);

        //如果风险点的任务ID存在，保存任务检查表数据
        if (null != record.getInspectScheduleId() && record.getInspectScheduleId() != 0) {
            InspectSchduleCheckRecord inspectSchduleCheckRecord = this.inspectSchduleCheckRecordService.getOne(record.getCompanyId(), record.getInspectScheduleId(), record.getRiskPointId(), record.getCreatedBy(),null);
            if (null == inspectSchduleCheckRecord) {
                inspectSchduleCheckRecord = new InspectSchduleCheckRecord();
                inspectSchduleCheckRecord.setCheckRecordNo(record.getCheckRecordNo());
                inspectSchduleCheckRecord.setCheckedCount(record.getCheckedCount());
                inspectSchduleCheckRecord.setCompanyId(record.getCompanyId());
                inspectSchduleCheckRecord.setInspectScheduleId(record.getInspectScheduleId());
                inspectSchduleCheckRecord.setRiskPointId(record.getRiskPointId());
                inspectSchduleCheckRecord.setCreatedBy(record.getCreatedBy());
                inspectSchduleCheckRecord.setCreatedTime(new Date());
                inspectSchduleCheckRecord.setUpdatedBy(record.getCreatedBy());
                inspectSchduleCheckRecord.setUpdatedTime(new Date());
                inspectSchduleCheckRecord.setStatus(1);
                this.inspectSchduleCheckRecordService.save(inspectSchduleCheckRecord);
            } else {
                inspectSchduleCheckRecord.setCheckedCount(inspectSchduleCheckRecord.getCheckedCount() + 1);
                inspectSchduleCheckRecord.setUpdatedBy(record.getCreatedBy());
                inspectSchduleCheckRecord.setUpdatedTime(new Date());
                this.inspectSchduleCheckRecordService.update(inspectSchduleCheckRecord);
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(RiskPointCheckRecord RiskPointCheckRecord) {
        riskPointCheckRecordMapper.update(RiskPointCheckRecord);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeByIds(List ids) {
        riskPointCheckRecordMapper.deleteByIds(ids);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeById(Long id) {
        riskPointCheckRecordMapper.deleteById(id);
    }

    @Override
    public List<Map<String, Object>> weeklyCheck(Map<String, Object> params) {
        return riskPointCheckRecordMapper.weeklyCheck(params);
    }
}
