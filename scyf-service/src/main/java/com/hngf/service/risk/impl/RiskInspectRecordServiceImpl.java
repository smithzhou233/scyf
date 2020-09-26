package com.hngf.service.risk.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hngf.common.enums.BigScreenDataTypeEnum;
import com.hngf.common.utils.Constant;
import com.hngf.common.utils.PageUtils;
import com.hngf.common.utils.ParamUtils;
import com.hngf.common.utils.R;
import com.hngf.common.utils.RedisKeys;
import com.hngf.common.utils.RedisUtils;
import com.hngf.entity.danger.InspectItemDef;
import com.hngf.entity.danger.InspectSchdule;
import com.hngf.entity.danger.InspectSchduleDef;
import com.hngf.entity.risk.RiskInspectRecord;
import com.hngf.entity.risk.RiskInspectRecordLog;
import com.hngf.entity.risk.RiskMeasure;
import com.hngf.entity.risk.RiskPointCheckRecord;
import com.hngf.mapper.danger.InspectSchduleMapper;
import com.hngf.mapper.risk.RiskInspectRecordLogMapper;
import com.hngf.mapper.risk.RiskInspectRecordMapper;
import com.hngf.mapper.risk.RiskPointMapper;
import com.hngf.service.common.CommonAttachmentService;
import com.hngf.service.danger.InspectItemDefService;
import com.hngf.service.danger.InspectSchduleCheckRecordService;
import com.hngf.service.danger.InspectSchduleDefService;
import com.hngf.service.risk.RiskCtrlService;
import com.hngf.service.risk.RiskInspectRecordLogService;
import com.hngf.service.risk.RiskInspectRecordService;
import com.hngf.service.risk.RiskMeasureService;
import com.hngf.service.risk.RiskPointCheckRecordService;
import com.hngf.service.risk.RiskPointControlRecordLogService;
import com.hngf.service.risk.RiskPointService;
import com.hngf.service.risk.RiskSourceService;
import com.hngf.service.score.ScoreUserService;
import com.hngf.service.utils.IdKit;
import com.hngf.service.utils.SendMessageUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


@Service("RiskInspectRecordService")
public class RiskInspectRecordServiceImpl implements RiskInspectRecordService {

    @Autowired
    private RiskInspectRecordMapper riskInspectRecordMapper;
    @Autowired
    private InspectSchduleMapper inspectSchduleMapper;
    @Autowired
    private RiskPointMapper riskPointMapper;
    @Autowired
    private RiskInspectRecordLogMapper riskInspectRecordLogMapper;
    @Autowired
    private RiskMeasureService riskMeasureService;
    @Autowired
    private InspectItemDefService inspectItemDefService;
    @Autowired
    private RiskInspectRecordLogService riskInspectRecordLogService;
    @Autowired
    private CommonAttachmentService commonAttachmentService;
    @Autowired
    private RiskCtrlService riskCtrlService;
    @Autowired
    private InspectSchduleDefService inspectSchduleDefService;
    @Autowired
    private RiskSourceService riskSourceService;
    @Autowired
    private RiskPointCheckRecordService riskPointCheckRecordService;
    @Autowired
    private RiskPointControlRecordLogService riskPointControlRecordLogService;
    @Autowired
    private ScoreUserService scoreUserService;
    @Autowired
    private InspectSchduleCheckRecordService inspectSchduleCheckRecordService;
    @Autowired
    private RiskPointService riskPointService;

    @Autowired
    private RedisUtils redisUtils;
    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        int pageNum = null!= params.get("pageNum") ? Integer.parseInt(params.get("pageNum").toString()) : 1;
        int pageSize = null != params.get("pageSize") ? Integer.parseInt(params.get("pageSize").toString()) : Constant.PAGE_SIZE;
        //利用PageHelper分页查询 注意：这个一定要放查询语句的前一行,否则无法进行分页,因为它对紧随其后第一个sql语句有效
        PageHelper.startPage(pageNum, pageSize);
        List<RiskInspectRecord> list = riskInspectRecordMapper.findList(params);
        PageInfo<RiskInspectRecord> pageInfo = new PageInfo<RiskInspectRecord>(list);
        return new PageUtils(list,(int)pageInfo.getTotal(),pageInfo.getPages(),pageInfo.getPageSize(),pageInfo.getPrePage() + 1);
    }

    @Override
    public List<RiskInspectRecord> getRiskInspectRecordList(Map<String, Object> params) {
        return riskInspectRecordMapper.findByMap(params);
    }

    @Override
    public PageUtils getRiskInspectRecordPage(Map<String, Object> params,Integer pageNum,Integer pageSize,String order) {
        if(StringUtils.isNotBlank(order)){
            PageHelper.startPage(pageNum, pageSize,order);
        }else{
            PageHelper.startPage(pageNum, pageSize);
        }
        List<RiskInspectRecord> list = riskInspectRecordMapper.findRiskInspectRecord(params);
        PageInfo pageInfo = new PageInfo(list);
        return new PageUtils(list,(int)pageInfo.getTotal(),pageInfo.getPages(),pageInfo.getPageSize(),pageInfo.getPrePage() + 1);
    }

    @Override
    public RiskInspectRecord getById(Long id){
        return riskInspectRecordMapper.findById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(RiskInspectRecord RiskInspectRecord){
        if (null != RiskInspectRecord) {
            RiskInspectRecord.setDelFlag(0);
            riskInspectRecordMapper.add(RiskInspectRecord);
            //同步保存日志表数据
            RiskInspectRecordLog log = new RiskInspectRecordLog();
            log.setInspectRecordId(RiskInspectRecord.getInspectRecordId());
            log.setSchduleDefId(RiskInspectRecord.getSchduleDefId());
            log.setRiskPointId(RiskInspectRecord.getRiskPointId());
            log.setInspectScheduleId(RiskInspectRecord.getInspectScheduleId());
            log.setInspectRecordNo(RiskInspectRecord.getInspectRecordNo());
            log.setItemDetailId(RiskInspectRecord.getItemDetailId());
            log.setRiskMeasureId(RiskInspectRecord.getRiskMeasureId());
            log.setGroupId(RiskInspectRecord.getGroupId());
            log.setCompanyId(RiskInspectRecord.getCompanyId());
            log.setInspectNumber(RiskInspectRecord.getInspectNumber());
            log.setRemark(RiskInspectRecord.getRemark());
            log.setInspectResult(RiskInspectRecord.getInspectResult());
            log.setCreatedBy(RiskInspectRecord.getCreatedBy());
            log.setCreatedTime(RiskInspectRecord.getCreatedTime());
            log.setUpdatedBy(RiskInspectRecord.getUpdatedBy());
            log.setUpdatedTime(RiskInspectRecord.getUpdatedTime());
            log.setDelFlag(RiskInspectRecord.getDelFlag());
            this.riskInspectRecordLogMapper.add(log);
        }

    }

    //保存检查记录
    private void saveRiskInspectRecord(Map<String, Object> dataMap, String[] uploadPathStr, String[] contentIds) throws Exception{
        String checkRecordLogId = (String)dataMap.get("inspectRecordLogId");
        Long id = IdKit.getRiskInspectRecordId();
        if (StringUtils.isNotEmpty(checkRecordLogId)) {
            RiskInspectRecordLog recordLog = this.riskInspectRecordLogService.getById(Long.parseLong(checkRecordLogId));
            if (recordLog != null) {
                recordLog.setInspectResult(Integer.parseInt(dataMap.get("checkResult").toString()));
                recordLog.setRemark((String)dataMap.get("remark"));
                recordLog.setCompanyId(Long.parseLong(dataMap.get("companyId").toString()));
                recordLog.setUpdatedTime(new Date());
                recordLog.setUpdatedBy(Long.parseLong(dataMap.get("userId").toString()));
                recordLog.setSpotData(ParamUtils.paramsToString(dataMap,"spotData"));
                this.riskInspectRecordLogService.update(recordLog);
                if (uploadPathStr != null) {
                    this.commonAttachmentService.saveBatch(recordLog.getInspectRecordId(), uploadPathStr);
                }
            } else {
                RiskInspectRecord record = this.setRiskInspectRecord(dataMap, contentIds[0], id);
                this.save(record);//保存风险检查记录
                if (uploadPathStr != null) {
                    this.commonAttachmentService.saveBatch(record.getInspectRecordId(), uploadPathStr);
                }
            }
        } else {
            RiskInspectRecord record = this.setRiskInspectRecord(dataMap, contentIds[0], id);
            this.save(record);//保存风险检查记录
            if (uploadPathStr != null) {
                this.commonAttachmentService.saveBatch(record.getInspectRecordId(), uploadPathStr);
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(RiskInspectRecord RiskInspectRecord) {
        riskInspectRecordMapper.update(RiskInspectRecord);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeByIds(List ids) {
        riskInspectRecordMapper.deleteByIds(ids);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeById(Long id) {
        riskInspectRecordMapper.deleteById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public R schduleInspectFinish(Map<String, Object> params) throws Exception {
        Integer ctlLevel = this.riskCtrlService.getCurrentUserCtlLevel(params);
        params.put("ctlLevel", ctlLevel);
        Map<String, Object> dataMap = new HashMap();
        dataMap.put("checkFinish", true);
        dataMap.put("hint", "检查完成");
        Integer checkNumber = 1;
        String riskPointId = ParamUtils.paramsToString(params,"riskPointId");
        String checkRecordNo = params.get("checkRecordNo").toString();
        String bizCheckDefId = params.get("inspectDefId").toString();
        String scheduleId = params.get("scheduleId").toString();
        Long userId = Long.parseLong(params.get("userId").toString());
        Long companyId = Long.parseLong(params.get("companyId").toString());
        Long positionId = Long.parseLong(params.get("positionId").toString());
        Long groupId = Long.parseLong(params.get("groupId").toString());

        Integer bizCheckDefType = Integer.parseInt(params.get("inspectDefType").toString());
        boolean isSchduleFinishChcek = false;
        boolean finishChcek = false;
        InspectSchdule schdule = null;
        InspectSchdule parentSchdule = null;
        String scheduleCheckType = "random";
        Integer scheduleModeType = 1;

        if ((Boolean)params.get("isScheduleCheck")) {
            schdule = this.inspectSchduleMapper.findById(Long.parseLong(scheduleId));
            if (schdule != null) {
                scheduleModeType = schdule.getInspectMode();
                parentSchdule = this.inspectSchduleMapper.findById(schdule.getParentScheduleId());
                checkNumber = schdule.getInspectScheduleCount();
                InspectSchduleDef schduleDef = this.inspectSchduleDefService.getById(schdule.getSchduleDefId());
                if (schduleDef != null && schduleDef.getInspectType().equals("fixed")) {
                    scheduleCheckType = schduleDef.getInspectType();
                }

                if ("fixed".equals(scheduleCheckType)) {
                    if (schdule.getStatus() == 2) {
                        redisUtils.delete(RedisKeys.getRiskPointKey(params.get("userId").toString(),"0",params.get("scheduleId").toString()));
                        return R.ok("检查完成");
                    }

                    if (schdule.getStatus() == 3) {
                        return R.error("任务已逾期，无法提交数据！");
                    }

                }
            }
        }

        params.put("checkNumber", checkNumber);
        int riskCheckedLogCount;
        HashMap spMap;

        if (bizCheckDefType == 0) {
            String[] dangerSrcIds = null;
            if (null != (params.get("dangerSourceIds"))) {
                dangerSrcIds = params.get("dangerSourceIds").toString().split(",");
            }

            Map<String, Object> logMap = new HashMap();
            logMap.put("riskPointId", riskPointId);
            logMap.put("userId", userId);
            logMap.put("companyId", companyId);
            logMap.put("scheduleId", scheduleId);
            riskCheckedLogCount = this.riskInspectRecordLogService.getRiskInspectLogCount(logMap);
            int itemCount = 0;
            if (dangerSrcIds != null && dangerSrcIds.length > 0) {
                for(int i = 0; i < dangerSrcIds.length; i ++) {
                    String dangerSrcId = dangerSrcIds[i];
                    if (null == dangerSrcId || StringUtils.isEmpty(dangerSrcId) || StringUtils.isBlank(dangerSrcId)) {
                        continue;
                    }
                    logMap.put("dangerSourceId", dangerSrcId);
                    logMap.put("ctlLevel", ctlLevel);
                    itemCount += this.riskSourceService.getControlInspectItemCount(logMap);
                }
            }

            if (riskCheckedLogCount < itemCount) {
                return R.error("存在未检查的检查项");
            }

            finishChcek = true;
        } else if (bizCheckDefType == 1) {
            spMap = new HashMap();
            spMap.put("checkNumber", checkNumber);
            spMap.put("scheduleId", scheduleId);
            spMap.put("inspectDefId", bizCheckDefId);
            spMap.put("riskPointId", riskPointId);
            spMap.put("userId", userId);
            spMap.put("companyId", companyId);

            riskCheckedLogCount = this.riskInspectRecordLogService.getRiskInspectLogCount(spMap);
            int inspectItemCount = this.inspectItemDefService.getInspectItemCount(spMap);
            if (!(Boolean)params.get("isScheduleCheck")) {
                if (riskCheckedLogCount < inspectItemCount) {
                    return R.error("存在未检查的检查项");
                }
                finishChcek = true;
                isSchduleFinishChcek = false;
            } else if ((Boolean)params.get("isScheduleCheck")) {
                if (riskCheckedLogCount < inspectItemCount) {
                    return R.error("存在未检查的检查项");
                }

                if (scheduleCheckType.equals("fixed")) {
                    finishChcek = true;
                    isSchduleFinishChcek = true;
                } else if (scheduleCheckType.equals("random")) {
                    finishChcek = true;
                    isSchduleFinishChcek = true;
                }
            }
        }

        if (finishChcek) {
            params.put("checkedCount", checkNumber);
            Long recordId = this.createdRiskPointCheckRecord(params);
            dataMap.put("riskPointCheckRecordId", recordId);
            //if(scheduleCheckType.equals("random")){
            redisUtils.delete(RedisKeys.getRiskPointKey(params.get("userId").toString(),"0",params.get("scheduleId").toString()));
            redisUtils.delete(RedisKeys.getRiskPointKey(params.get("userId").toString(),"1",params.get("scheduleId").toString()));
           // }

        }

        List logList;

        if ((Boolean)params.get("isScheduleCheck")) {
            if (finishChcek) {
                schdule.setStatus(1);
                schdule.setExecutorDate(new Date());
                schdule.setExecutor(userId);
                schdule.setUpdatedTime(new Date());
                //更新检查任务
                this.inspectSchduleMapper.update(schdule);
                //如果父级任务不为空，更新其状态
                if(null != parentSchdule){
                    parentSchdule.setStatus(1);
                    parentSchdule.setExecutorDate(new Date());
                    parentSchdule.setUpdatedTime(new Date());
                    this.inspectSchduleMapper.update(parentSchdule);
                }

            }

            if (bizCheckDefType == 0 && scheduleCheckType.equals("fixed")) {
                spMap = new HashMap();
                spMap.put("companyId", companyId);
                spMap.put("userId", userId);
                spMap.put("createdBy", userId);
                spMap.put("positionId", positionId);
                spMap.put("groupId", groupId);
                spMap.put("ctlLevelOff", "no");
                spMap.put("type", 0);
                spMap.put("isActive", 1);
                spMap.put("scheduleId", schdule.getInspectScheduleId());
                spMap.put("checkNumber", schdule.getInspectScheduleCount());

                spMap.put("ctlLevelStr", true);
                if (ctlLevel != 4) {
                    spMap.put("ctlLevel", ctlLevel);
                }

                //查询任务下的风险点列表
                logList = this.riskPointMapper.findSchduleRiskPoint(spMap);
                if (logList.isEmpty()) {
                    isSchduleFinishChcek = true;
                }
            }
        }

        if (finishChcek) {
            spMap = new HashMap();
            spMap.put("checkRecordNo", checkRecordNo);
            spMap.put("inspectDefId", bizCheckDefId);
            spMap.put("userId",userId);
            spMap.put("companyId", companyId);
            spMap.put("riskPointId", riskPointId);
            //删除检查记录日志数据
            this.riskInspectRecordLogService.removeRecord(spMap);
            Map<String ,Object> schedulMap =new HashMap();
            schedulMap.put("companyId", companyId);
            schedulMap.put("positionId", positionId  );
            schedulMap.put("isActive", 1);
            schedulMap.put("userId", userId);
            schedulMap.put("type", "0");
            schedulMap.put("createdBy",userId);
            schedulMap.put("scheduleId", schdule.getInspectScheduleId());
            PageUtils page =  riskPointService.getScheduleRiskPointList(schedulMap,1,100,null); //判断该任务下是否存在风险点，不存在测更改状态
            if (schdule != null && isSchduleFinishChcek) {
                if(null==page.getList() || page.getList().size()==0){

                if (scheduleCheckType.equals("fixed")) {

                    //不判断总次数和当前执行次数，任务只执行一次，直接设置任务状态为检查完成
                    /*if (schdule.getInspectScheduleCount().intValue() != schdule.getInspectTotalCount().intValue()) {
                        schdule.setInspectScheduleCount(schdule.getInspectScheduleCount() + 1);
                        schdule.setUpdatedBy(userId);
                        schdule.setUpdatedTime(new Date());
                        this.inspectSchduleMapper.update(schdule);
                    } else {

                    }*/

                    schdule.setStatus(2);
                    schdule.setFinishDate(new Date());
                    schdule.setInspectScheduleCount(schdule.getInspectScheduleCount() + 1);
                    schdule.setFinishBy(userId);
                    schdule.setUpdatedBy(userId);
                    schdule.setUpdatedTime(new Date());
                    this.inspectSchduleMapper.update(schdule);

                    //因为任务只执行一次，生成任务检查记录时，直接设置状态为已完成，以下代码注释掉
                    /*logList = this.inspectSchduleCheckRecordService.getList(schdule.getCompanyId(), Long.parseLong(scheduleId), userId);
                    if (!logList.isEmpty()) {
                        Iterator iterator = logList.iterator();

                        while(iterator.hasNext()) {
                            InspectSchduleCheckRecord schduleCheckRecordLog = (InspectSchduleCheckRecord)iterator.next();
                            schduleCheckRecordLog.setUpdatedBy(schduleCheckRecordLog.getCreatedBy());
                            schduleCheckRecordLog.setUpdatedTime(new Date());
                            schduleCheckRecordLog.setStatus(1);
                            this.inspectSchduleCheckRecordService.update(schduleCheckRecordLog);
                        }
                    }*/
                    //生成下一个任务
                    //if (parentSchdule.getNextTaskGenerated() == 0) {
                    //TODO 生成下一个任务由定时任务完成
                    ///this.riskCheckSchduleDefService.execute(schdule.getRiskCheckDefId(), userTokenDto, parentSchdule.getScheduleId());
                    //}

                    this.scoreUserService.goGrade(companyId, groupId, userId, Constant.SCORE_SETTING_SCHDULE_JCWC_3, schdule.getInspectScheduleId());
                } else if (scheduleCheckType.equals("random")) {
                    schdule.setStatus(2);
                    schdule.setInspectScheduleCount(schdule.getInspectScheduleCount() + 1);
                    schdule.setUpdatedBy(userId);
                    schdule.setUpdatedTime(new Date());
                    schdule.setFinishDate(new Date());
                    this.inspectSchduleMapper.update(schdule);
                }
                }
                if (null != parentSchdule) {
                    //查询状态为0(未检查)和1(检查中)的父级下的子任务，如果没有数据，将父级任务的状态设为 2（已检查）
                    logList = this.inspectSchduleMapper.getByParentScheduleId(parentSchdule.getInspectScheduleId());
                    if (logList.isEmpty()) {
                        parentSchdule.setStatus(2);
                        parentSchdule.setFinishDate(new Date());
                        parentSchdule.setUpdatedTime(new Date());
                        parentSchdule.setUpdatedBy(userId);
                        this.inspectSchduleMapper.update(parentSchdule);
                    }
                }
            }

            if (StringUtils.isNotEmpty(riskPointId)) {
                //闭环逾期任务
                this.riskPointControlRecordLogService.scheduleIsCloseUp(companyId, Long.parseLong(riskPointId), 2, positionId);
                this.riskPointService.checkedRiskPointIsControl(companyId, Long.parseLong(riskPointId));
                SendMessageUtil.sendBigScreen(companyId, groupId, BigScreenDataTypeEnum.riskPoint.idType);
            }
        } else {
            return R.error("检查未完成");
        }

        return R.ok();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addRiskInspect(Map<String, Object> params, String[] uploadPathStr) throws Exception{
        List<RiskInspectRecord> records = new ArrayList();
        List<RiskInspectRecordLog> recordLogs = new ArrayList();
        params.put("checkNumber", 1);
        if ((Boolean)params.get("isScheduleCheck")) {
            Long scheduleId = Long.parseLong(params.get("scheduleId").toString());
            InspectSchdule checkSchdule = this.inspectSchduleMapper.findById(scheduleId);
           // params.put("checkNumber", checkSchdule.getInspectScheduleCount());
            if (checkSchdule != null && checkSchdule.getExecutorDate() == null) {
                checkSchdule.setExecutorDate(new Date());
                this.inspectSchduleMapper.update(checkSchdule);
            }
        }

        String inspectItemContentDefIds = (String)params.get("inspectItemContentDefIds");
        if (StringUtils.isNotEmpty(inspectItemContentDefIds)) {
            String[] contentIds = inspectItemContentDefIds.split(",");
            if (contentIds.length == 1) {
                this.saveRiskInspectRecord(params, uploadPathStr, contentIds);
            } else if (contentIds.length > 1) {
                for (int i = 0; i < contentIds.length; i++) {
                    if (null == contentIds[i] || StringUtils.isEmpty(contentIds[i]) || StringUtils.isBlank(contentIds[i])) {
                        continue;
                    }
                    Long id = IdKit.getRiskInspectRecordId();
                    records.add(this.setRiskInspectRecord(params, contentIds[i], id));
                    recordLogs.add(this.setRiskInspectRecordLog(params, contentIds[i], id));
                }

                if (records.size() < 1000) {
                    this.riskInspectRecordMapper.addForeach(records);
                }

                if (recordLogs.size() < 1000) {
                    this.riskInspectRecordLogService.saveBatch(recordLogs);
                }

            }
        }
        if(null!= params.get("riskPointId")){
             redisUtils.delete(RedisKeys.getriskMeasureItemList(params.get("userId").toString(),params.get("riskPointId").toString(),params.get("scheduleId").toString()));
        }else if(null!=params.get("inspectDefId")){
            redisUtils.delete(RedisKeys.getScheduleDefItemList(Long.parseLong(params.get("userId").toString()),params.get("inspectDefId").toString(),params.get("scheduleId").toString()));
        }
    }

    //生成 RiskInspectRecord 检查记录对象
    private RiskInspectRecord setRiskInspectRecord(Map<String, Object> dataMap, String contentId, Long recordId) throws Exception {
        String inspectDefType = dataMap.get("inspectDefType") + "";
        RiskInspectRecord record = new RiskInspectRecord();
        record.setRiskPointId(ParamUtils.paramsToLong(dataMap,"riskPointId"));

        record.setGroupId(Long.parseLong(dataMap.get("groupId").toString()));
        record.setInspectScheduleId(Long.parseLong(dataMap.get("scheduleId").toString()));
        record.setInspectRecordId(recordId);
        if (null != dataMap.get("inspectDefId")) {
            record.setSchduleDefId(Long.parseLong(dataMap.get("inspectDefId").toString()));
        }
        if (null != dataMap.get("checkNumber")) {
            record.setInspectNumber(Integer.parseInt(dataMap.get("checkNumber").toString()));
        }
        record.setInspectRecordNo(ParamUtils.paramsToLong(dataMap,"checkRecordNo"));

        if ("1".equals(inspectDefType)) {
            RiskMeasure riskMeasure = this.riskMeasureService.getById(Long.parseLong(contentId));
            if (riskMeasure != null) {
                record.setRiskMeasureContent(riskMeasure.getRiskMeasureContent());
            } else {
                record.setRiskMeasureContent("未知的检查内容，可能被修改或者已删除");
            }
        } else if ("0".equals(inspectDefType)) {
            InspectItemDef inspectItemDef = this.inspectItemDefService.getById(Long.parseLong(contentId));
            if (inspectItemDef != null) {
                record.setRiskMeasureContent(inspectItemDef.getInspectItemDefDesc());
                record.setItemDetailId(inspectItemDef.getParentId());
            } else {
                record.setRiskMeasureContent("未知的检查内容，可能被修改或者已删除");
            }
        }

        Object remarkObj = dataMap.get("remark");
        if ( null == remarkObj || StringUtils.isEmpty( remarkObj.toString() ) || StringUtils.isBlank( remarkObj.toString() )) {
            if (Integer.parseInt(dataMap.get("checkResult") + "") == Constant.CHECK_RESULT_BSJ) {
                record.setRemark("不涉及本次检查");
                record.setCheckSort(4);
            } else if (Integer.parseInt(dataMap.get("checkResult") + "") == Constant.CHECK_RESULT_BTG) {
                record.setRemark("检查不通过");
                record.setCheckSort(2);
            } else if (Integer.parseInt(dataMap.get("checkResult") + "") == Constant.CHECK_RESULT_TG) {
                record.setRemark("检查通过");
                record.setCheckSort(3);
            } else if (Integer.parseInt(dataMap.get("checkResult") + "") == Constant.CHECK_RESULT_CZYH) {
                record.setRemark("发现隐患");
                record.setCheckSort(1);
            }
        } else {
            record.setRemark((String)dataMap.get("remark"));
        }

        if (Integer.parseInt(dataMap.get("checkResult") + "") == Constant.CHECK_RESULT_BSJ) {
            record.setCheckSort(4);
        } else if (Integer.parseInt(dataMap.get("checkResult") + "") == Constant.CHECK_RESULT_BTG) {
            record.setCheckSort(2);
        } else if (Integer.parseInt(dataMap.get("checkResult") + "") == Constant.CHECK_RESULT_TG) {
            record.setCheckSort(3);
        } else if (Integer.parseInt(dataMap.get("checkResult") + "") == Constant.CHECK_RESULT_CZYH) {
            record.setCheckSort(1);
        }

        record.setInspectResult(Integer.parseInt(dataMap.get("checkResult") + ""));
        record.setCompanyId(Long.parseLong(dataMap.get("companyId").toString()));
        record.setRiskMeasureId(Long.parseLong(contentId));
        record.setCreatedBy(Long.parseLong(dataMap.get("userId").toString()));
        record.setCreatedTime(new Date());
        record.setSpotData(ParamUtils.paramsToString(dataMap,"spotData"));
        return record;
    }

    //生成 InspectRecordLog 检查记录日志对象
    private RiskInspectRecordLog setRiskInspectRecordLog(Map<String, Object> dataMap, String contentId, Long recordId) throws Exception {
        RiskInspectRecordLog record = new RiskInspectRecordLog();
        record.setRiskPointId(ParamUtils.paramsToLong(dataMap,"riskPointId"));
        record.setGroupId(Long.parseLong(dataMap.get("groupId").toString()));
        record.setInspectScheduleId(Long.parseLong(dataMap.get("scheduleId").toString()));
        record.setInspectRecordId(recordId);

        if (null != dataMap.get("inspectDefId")) {
            record.setSchduleDefId(Long.parseLong(dataMap.get("inspectDefId").toString()));
        }
        if (null != dataMap.get("checkNumber")) {
            record.setInspectNumber(Integer.parseInt(dataMap.get("checkNumber").toString()));
        }
        record.setInspectRecordNo(ParamUtils.paramsToLong(dataMap,"checkRecordNo"));

        RiskMeasure riskMeasure = this.riskMeasureService.getById(Long.parseLong(contentId));
        if (riskMeasure != null) {
            record.setRiskMeasureContent(riskMeasure.getRiskMeasureContent());
        } else {
            record.setRiskMeasureContent("未知的检查项内容（管控措施），可能被修改或者已删除");
        }

        if (StringUtils.isNotEmpty((String)dataMap.get("remark"))) {
            if (Integer.parseInt(dataMap.get("checkResult") + "") == Constant.CHECK_RESULT_BSJ) {
                record.setRemark("不涉及本次检查");
            } else if (Integer.parseInt(dataMap.get("checkResult") + "") == Constant.CHECK_RESULT_BTG) {
                record.setRemark("不通过");
            } else if (Integer.parseInt(dataMap.get("checkResult") + "") == Constant.CHECK_RESULT_TG) {
                record.setRemark("通过");
            }
        } else {
            record.setRemark((String)dataMap.get("remark"));
        }

        record.setInspectResult(Integer.parseInt(dataMap.get("checkResult") + ""));
        record.setCompanyId(Long.parseLong(dataMap.get("companyId").toString()));
        record.setRiskMeasureId(Long.parseLong(contentId));
        record.setCreatedBy(Long.parseLong(dataMap.get("userId").toString()));
        record.setCreatedTime(new Date());
        record.setSpotData(ParamUtils.paramsToString(dataMap,"spotData"));
        return record;
    }

    //生成风险点检查记录对象(RiskPointCheckRecord)
    private Long createdRiskPointCheckRecord(Map<String, Object> dataMap) {
        try {
            boolean isTG = false;
            boolean isBTG = false;
            boolean isHasHdanger = false;
            boolean isBSJ = false;
            Integer checkResult = 1;
            Map<String, Object> logMap = new HashMap();
            logMap.put("companyId", Long.parseLong(dataMap.get("companyId") + ""));
            logMap.put("userId", Long.parseLong(dataMap.get("userId") + ""));
            logMap.put("checkRecordNo", Long.parseLong(dataMap.get("checkRecordNo") + ""));
            logMap.put("inspectDefId", Long.parseLong(dataMap.get("inspectDefId") + ""));
            List<RiskInspectRecordLog> checkRecordLog = this.riskInspectRecordLogService.getByRiskInspectRecordLog(logMap);
            Iterator<RiskInspectRecordLog> recordLogIterator = checkRecordLog.iterator();

            while(recordLogIterator.hasNext()) {
                RiskInspectRecordLog recordLog = recordLogIterator.next();
                if (recordLog.getInspectResult().intValue() == Constant.CHECK_RESULT_TG.intValue()) {
                    isTG = true;
                }

                if (recordLog.getInspectResult().intValue() == Constant.CHECK_RESULT_BTG.intValue()) {
                    isBTG = true;
                }

                if (recordLog.getInspectResult().intValue() == Constant.CHECK_RESULT_CZYH.intValue()) {
                    isHasHdanger = true;
                }

                if (recordLog.getInspectResult().intValue() == Constant.CHECK_RESULT_BSJ.intValue()) {
                    isBSJ = true;
                }
            }

            if (isBSJ) {
                checkResult = Constant.CHECK_RESULT_BSJ;
            }

            if (isTG) {
                checkResult = Constant.CHECK_RESULT_TG;
            }

            if (isBTG) {
                checkResult = Constant.CHECK_RESULT_BTG;
            }

            if (isHasHdanger) {
                checkResult = Constant.CHECK_RESULT_CZYH;
            }

            //创建风险点检查记录并保存
            RiskPointCheckRecord checkRecord = new RiskPointCheckRecord();

            checkRecord.setCheckRecordNo(Long.parseLong(dataMap.get("checkRecordNo") + ""));
            checkRecord.setCompanyId(Long.parseLong(dataMap.get("companyId") + ""));
            checkRecord.setInspectDefId(Long.parseLong(dataMap.get("inspectDefId") + ""));
            checkRecord.setInspectScheduleId(Long.parseLong(dataMap.get("scheduleId") + ""));
            checkRecord.setCheckedCount(null != dataMap.get("checkedCount") ? Integer.parseInt(dataMap.get("checkedCount") + "") : null);
            checkRecord.setGroupId(null != dataMap.get("groupId") ? Long.parseLong(dataMap.get("groupId") + "") : null);
            checkRecord.setPositionId(null != dataMap.get("positionId") ? Long.parseLong(dataMap.get("positionId") + "") : null);
            checkRecord.setRiskPointId(null != dataMap.get("riskPointId") ? Long.parseLong(dataMap.get("riskPointId") + "") : null);
            checkRecord.setCreatedTime(new Date());
            checkRecord.setResult(checkResult);
            checkRecord.setStatus(1);
            checkRecord.setCreatedBy(Long.parseLong(dataMap.get("userId") + ""));
            checkRecord.setLatitude(dataMap.get("latitude") + "");
            checkRecord.setLongitude(dataMap.get("longitude")+"");
            checkRecord.setAddress(dataMap.get("address")+"");
            if (checkRecord.getRiskPointId() == null) {
                checkRecord.setRiskPointId(0L);
            }

            this.riskPointCheckRecordService.save(checkRecord);

            return checkRecord.getRecordId();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
