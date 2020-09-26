package com.hngf.service.risk.impl;

import com.hngf.entity.risk.RiskInspectRecord;
import com.hngf.service.risk.RiskInspectRecordService;
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

import com.hngf.mapper.risk.RiskInspectRecordLogMapper;
import com.hngf.entity.risk.RiskInspectRecordLog;
import com.hngf.service.risk.RiskInspectRecordLogService;


@Service("RiskInspectRecordLogService")
public class RiskInspectRecordLogServiceImpl implements RiskInspectRecordLogService {

    @Autowired
    private RiskInspectRecordLogMapper riskInspectRecordLogMapper;
    @Autowired
    private RiskInspectRecordService riskInspectRecordService;

    @Override
    public RiskInspectRecordLog getById(Long id){
        return riskInspectRecordLogMapper.findById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(RiskInspectRecordLog riskInspectRecordLog) {
        riskInspectRecordLog.setUpdatedTime(new Date());
        riskInspectRecordLogMapper.update(riskInspectRecordLog);

        //同步更新 RiskInspectRecord
        RiskInspectRecord checkedRecord = this.riskInspectRecordService.getById(riskInspectRecordLog.getInspectRecordId());
        if (checkedRecord != null) {
            checkedRecord.setInspectResult(riskInspectRecordLog.getInspectResult());
            checkedRecord.setInspectNumber(riskInspectRecordLog.getInspectNumber());
            checkedRecord.setRemark(riskInspectRecordLog.getRemark());
            checkedRecord.setUpdatedTime(riskInspectRecordLog.getUpdatedTime());
            checkedRecord.setUpdatedBy(riskInspectRecordLog.getUpdatedBy());
            this.riskInspectRecordService.update(checkedRecord);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeByIds(List ids) {
        riskInspectRecordLogMapper.deleteByIds(ids);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeById(Long id) {
        riskInspectRecordLogMapper.deleteById(id);
    }

    @Override
    public int removeRecord(Map<String, Object> params) {
        return riskInspectRecordLogMapper.deleteRecordLog(params);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int saveBatch(List<RiskInspectRecordLog> recordLogs) {
        return riskInspectRecordLogMapper.addForeach(recordLogs);
    }

    @Override
    public int getRiskInspectLogCount(Map<String, Object> params) {
        return riskInspectRecordLogMapper.findRiskInspectLogCount(params);
    }

    @Override
    public List<RiskInspectRecordLog> getByRiskInspectRecordLog(Map<String, Object> params) {
        return riskInspectRecordLogMapper.findByRiskInspectRecordLog(params);
    }
}
