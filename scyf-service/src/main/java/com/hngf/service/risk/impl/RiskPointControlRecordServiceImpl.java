package com.hngf.service.risk.impl;

import com.hngf.entity.danger.Hidden;
import com.hngf.entity.danger.InspectSchdule;
import com.hngf.entity.risk.RiskPoint;
import com.hngf.entity.risk.RiskPointControlRecordLog;
import com.hngf.service.danger.HiddenService;
import com.hngf.service.danger.InspectSchduleService;
import com.hngf.service.risk.RiskPointControlRecordLogService;
import com.hngf.service.risk.RiskPointService;
import com.hngf.service.sys.DictService;
import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hngf.common.utils.PageUtils;
import com.hngf.common.utils.Constant;

import com.hngf.mapper.risk.RiskPointControlRecordMapper;
import com.hngf.entity.risk.RiskPointControlRecord;
import com.hngf.service.risk.RiskPointControlRecordService;


@Service("RiskPointControlRecordService")
public class RiskPointControlRecordServiceImpl implements RiskPointControlRecordService {

    private static Logger logger = LoggerFactory.getLogger(RiskPointControlRecordServiceImpl.class);

    @Autowired
    private RiskPointControlRecordMapper RiskPointControlRecordMapper;
    @Autowired
    private RiskPointControlRecordLogService riskPointControlRecordLogService;
    @Autowired
    private RiskPointService riskPointService;
    @Autowired
    private HiddenService hiddenService;
    @Autowired
    private InspectSchduleService inspectSchduleService;
    @Autowired
    private DictService dictService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        int pageNum = null!= params.get("pageNum") ? Integer.parseInt(params.get("pageNum").toString()) : 1;
        int pageSize = null != params.get("pageSize") ? Integer.parseInt(params.get("pageSize").toString()) : Constant.PAGE_SIZE;
        //利用PageHelper分页查询 注意：这个一定要放查询语句的前一行,否则无法进行分页,因为它对紧随其后第一个sql语句有效
        PageHelper.startPage(pageNum, pageSize);
        List<RiskPointControlRecord> list = RiskPointControlRecordMapper.findList(params);
        PageInfo<RiskPointControlRecord> pageInfo = new PageInfo<RiskPointControlRecord>(list);
        return new PageUtils(list,(int)pageInfo.getTotal(),pageInfo.getPages(),pageInfo.getPageSize(),pageInfo.getPrePage() + 1);
    }

    @Override
    public PageUtils getControlRecord(Map<String, Object> params,Integer pageNum, Integer pageSize, String order) {
        if(StringUtils.isNotBlank(order)){
            PageHelper.startPage(pageNum, pageSize,order);
        }else{
            PageHelper.startPage(pageNum, pageSize);
        }
        List<Map<String,Object>> list = RiskPointControlRecordMapper.findControlRecord(params);
        PageInfo pageInfo = new PageInfo(list);
        return new PageUtils(list,(int)pageInfo.getTotal(),pageInfo.getPages(),pageInfo.getPageSize(),pageInfo.getPrePage() + 1);
    }

    @Override
    public PageUtils getHistoryControlRecord(Map<String, Object> params,Integer pageNum, Integer pageSize, String order) {
        if(StringUtils.isNotBlank(order)){
            PageHelper.startPage(pageNum, pageSize,order);
        }else{
            PageHelper.startPage(pageNum, pageSize);
        }
        List<Map<String,Object>> list = RiskPointControlRecordMapper.findHistoryControlRecord(params);
        PageInfo pageInfo = new PageInfo(list);
        return new PageUtils(list,(int)pageInfo.getTotal(),pageInfo.getPages(),pageInfo.getPageSize(),pageInfo.getPrePage() + 1);
    }
    /**
     * @Author: zyj
     * @Description:风险，风险点预警记录统计折线图
     * @Param  year 年份 companyId企业id
     * @Date 15:45 2020/6/19
     */
    @Override
    public List<Map<String, Object>> getStatistics(Long companyId, Integer year) {
        return RiskPointControlRecordMapper.getStatistics(companyId,year);
    }
    /**
     * @Author: zyj
     * @Description:风险，风险点预警记录统计折线图 年份下拉框
     * @Param companyId企业id
     * @Date 15:28 2020/6/23
     */
    @Override
    public List<Map<String,Object>> getStatisticsYear(@Param("companyId") Long companyId){
       return RiskPointControlRecordMapper.getStatisticsYear(companyId);
    }
    @Override
    public RiskPointControlRecord getById(Long id){
        return RiskPointControlRecordMapper.findById(id);
    }

    @Override
    public int insertRiskPointControlRecord(Hidden hidden) throws Exception {
        if (null != hidden && null != hidden.getHiddenId()) {
            if (null == hidden.getHiddenId()) {
                return 0;
            } else {
                Long scheduleId = hidden.getInspectScheduleId();
                if (null == scheduleId) {
                    hidden = this.hiddenService.getById(hidden.getHiddenId());
                } else {
                    InspectSchdule inspectSchdule = this.inspectSchduleService.getById(scheduleId);
                    if (null != inspectSchdule && inspectSchdule.getInspectMode() != 1) {
                        return 0;
                    }
                }

                Date date = new Date(System.currentTimeMillis());
                RiskPointControlRecord record = new RiskPointControlRecord();
                record.setCompanyId(hidden.getCompanyId());
                record.setDetailId(hidden.getHiddenId());
                record.setDetailType(Constant.RISK_POINT_ALARM_HDANGER);
                record.setRiskPointId(hidden.getRiskPointId());
                record.setCreatedTime(date);
                record.setIsCloseUp(0);
                RiskPointControlRecordLog riskPointControlRecordLog = new RiskPointControlRecordLog();
                riskPointControlRecordLog.setCompanyId(hidden.getCompanyId());
                riskPointControlRecordLog.setRiskPointId(hidden.getRiskPointId());
                riskPointControlRecordLog.setDetailId(hidden.getHiddenId());
                riskPointControlRecordLog.setDetailType(Constant.RISK_POINT_ALARM_HDANGER);
                riskPointControlRecordLog.setCreatedTime(date);
                riskPointControlRecordLog.setCreatedBy(hidden.getCreatedBy());
                boolean isControl = false;
                if (Constant.DANGER_DZG.intValue() == hidden.getStatus().intValue()) {
                    record.setCauseReason("hidden_danger_create");
                    record.setIsControl(0);
                    record.setCauseRemark("隐患：“ " + hidden.getHiddenTitle() + " ” 正在整改中！");
                    record.setCreatedBy(hidden.getCreatedBy());
                    riskPointControlRecordLog.setCauseRemark("隐患：“ " + hidden.getHiddenTitle() + " ” 正在整改中！");
                    riskPointControlRecordLog.setIsControl(isControl ? 1 : 0);
                    riskPointControlRecordLog.setIsCloseUp(isControl ? 1 : 0);
                    riskPointControlRecordLog.setCauseReason("hidden_danger_create");
                    this.insertRiskPointActualControlRecord(riskPointControlRecordLog);

                } else if (Constant.DANGER_YCX.intValue() == hidden.getStatus().intValue()) {
                    this.colseRiskPointActualControlRecord(riskPointControlRecordLog);
                    isControl = this.checkRiskPointControl(hidden.getCompanyId(), hidden.getRiskPointId());
                    record.setIsControl(isControl ? 1 : 0);
                    record.setCauseReason("hidden_danger_cancel");
                    record.setCauseRemark("隐患撤销");
                    record.setCreatedBy(hidden.getCreatedBy());
                } else if (Constant.DANGER_DELETE.intValue() == hidden.getStatus().intValue()) {
                    this.colseRiskPointActualControlRecord(riskPointControlRecordLog);
                    isControl = this.checkRiskPointControl(hidden.getCompanyId(), hidden.getRiskPointId());
                    record.setIsControl(isControl ? 1 : 0);
                    record.setCauseReason("hidden_danger_cancel");
                    record.setCauseRemark("隐患已删除");
                    record.setCreatedBy(hidden.getCreatedBy());
                } else if (Constant.DANGER_YSTG.equals(hidden.getStatus())) {
                    this.colseRiskPointActualControlRecord(riskPointControlRecordLog);
                    isControl = this.checkRiskPointControl(hidden.getCompanyId(), hidden.getRiskPointId());
                    record.setIsControl(isControl ? 1 : 0);
                    record.setCauseReason("hidden_danger_cancel");
                    if (record.getIsControl().equals(1)) {
                        record.setCauseRemark("隐患验收已通过:“ " + hidden.getHiddenTitle() + " ”");
                    } else {
                        record.setCauseRemark("隐患已处理，风险点处于预警:“ " + hidden.getHiddenTitle() + " ”");
                    }

                    record.setCreatedBy(hidden.getCreatedBy());
                } else {
                    if (Constant.DANGER_NOT.intValue() != hidden.getStatus().intValue()) {
                        return 0;
                    }

                    this.colseRiskPointActualControlRecord(riskPointControlRecordLog);
                    isControl = this.checkRiskPointControl(hidden.getCompanyId(), hidden.getRiskPointId());
                    record.setIsControl(isControl ? 1 : 0);
                    record.setCauseReason("hidden_danger_cancel");
                    record.setCauseRemark("隐患已退回至待提交:“ " + hidden.getHiddenTitle() + " ”");
                    record.setCreatedBy(hidden.getCreatedBy());
                }

                return this.insertRiskPointControlRecord(record);
            }
        } else {
            return 0;
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(RiskPointControlRecord RiskPointControlRecord) {
        RiskPointControlRecordMapper.add(RiskPointControlRecord);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(RiskPointControlRecord RiskPointControlRecord) {
        RiskPointControlRecordMapper.update(RiskPointControlRecord);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeByIds(List ids) {
        RiskPointControlRecordMapper.deleteByIds(ids);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeById(Long id) {
        RiskPointControlRecordMapper.deleteById(id);
    }

    /**
     * 检查失控的记录是否等于 0
     * @param companyId
     * @param riskPointId
     * @return
     */
    public boolean checkRiskPointControl(Long companyId, Long riskPointId) {
        Map<String, Object> map = new HashMap();
        map.put("companyId", companyId);
        map.put("riskPointId", riskPointId);
        List<RiskPointControlRecordLog> list = this.riskPointControlRecordLogService.getIsControlRecordList(map);
        return list.size() <= 0;
    }

    private int insertRiskPointControlRecord(RiskPointControlRecord record) {
        int result = this.RiskPointControlRecordMapper.add(record);
        if (record.getIsControl() != null) {
            RiskPoint rp = new RiskPoint();
            rp.setRiskPointId(record.getRiskPointId());
            rp.setIsOutOfControl(record.getIsControl() == 1 ? 0 : 1);
            this.riskPointService.update(rp);

        }
        this.riskPointService.checkedRiskPointIsControl(record.getCompanyId(), record.getRiskPointId());
        return result;
    }

    private void colseRiskPointActualControlRecord(RiskPointControlRecordLog record) {
        this.riskPointControlRecordLogService.deleteIsCloseUp(record.getCompanyId(), record.getRiskPointId(), record.getDetailId(), null);
        logger.info("成功物理闭环一条告警信息");
    }

    private void insertRiskPointActualControlRecord(RiskPointControlRecordLog record) {
        this.riskPointControlRecordLogService.save(record);
        logger.info("成功新增一条实时告警记录");
    }
}
