package com.hngf.service.risk.impl;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import java.util.Map;
import java.util.List;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hngf.common.utils.PageUtils;
import com.hngf.common.utils.Constant;

import com.hngf.mapper.risk.RiskPointControlRecordLogMapper;
import com.hngf.entity.risk.RiskPointControlRecordLog;
import com.hngf.service.risk.RiskPointControlRecordLogService;


@Service("RiskPointControlRecordLogService")
public class RiskPointControlRecordLogServiceImpl implements RiskPointControlRecordLogService {

    @Autowired
    private RiskPointControlRecordLogMapper RiskPointControlRecordLogMapper;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        int pageNum = null!= params.get("pageNum") ? Integer.parseInt(params.get("pageNum").toString()) : 1;
        int pageSize = null != params.get("pageSize") ? Integer.parseInt(params.get("pageSize").toString()) : Constant.PAGE_SIZE;
        //利用PageHelper分页查询 注意：这个一定要放查询语句的前一行,否则无法进行分页,因为它对紧随其后第一个sql语句有效
        PageHelper.startPage(pageNum, pageSize);
        List<RiskPointControlRecordLog> list = RiskPointControlRecordLogMapper.findList(params);
        PageInfo<RiskPointControlRecordLog> pageInfo = new PageInfo<RiskPointControlRecordLog>(list);
        return new PageUtils(list,(int)pageInfo.getTotal(),pageInfo.getPages(),pageInfo.getPageSize(),pageInfo.getPrePage() + 1);
    }

    @Override
    public RiskPointControlRecordLog getById(Long id){
        return RiskPointControlRecordLogMapper.findById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(RiskPointControlRecordLog RiskPointControlRecordLog) {
        RiskPointControlRecordLogMapper.add(RiskPointControlRecordLog);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(RiskPointControlRecordLog RiskPointControlRecordLog) {
        RiskPointControlRecordLogMapper.update(RiskPointControlRecordLog);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeByIds(List ids) {
        RiskPointControlRecordLogMapper.deleteByIds(ids);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeById(Long id) {
        RiskPointControlRecordLogMapper.deleteById(id);
    }

    @Override
    public List<RiskPointControlRecordLog> riskPointWarn(Map<String, Object> params) {
        return RiskPointControlRecordLogMapper.findIsControlRecordList(params);
    }

    @Override
    public int deleteIsCloseUp(Long companyId, Long riskPointId, Long detailId, Long positionId) {
        return RiskPointControlRecordLogMapper.deleteIsCloseUp(companyId,riskPointId,detailId,positionId);
    }

    @Override
    public int scheduleIsCloseUp(Long companyId, Long riskPointId,Integer detailType, Long positionId) {
        return RiskPointControlRecordLogMapper.scheduleIsCloseUp(companyId,riskPointId,detailType,positionId);
    }

    @Override
    public List<RiskPointControlRecordLog> getIsControlRecordList(Map<String, Object> params) {
        return RiskPointControlRecordLogMapper.findIsControlRecordList(params);
    }
}
