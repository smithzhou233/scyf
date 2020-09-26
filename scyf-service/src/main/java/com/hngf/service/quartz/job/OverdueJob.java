package com.hngf.service.quartz.job;

import cn.hutool.core.util.PageUtil;
import com.hngf.common.utils.Constant;
import com.hngf.common.utils.DateUtils;
import com.hngf.common.utils.PageUtils;
import com.hngf.entity.danger.InspectSchdule;
import com.hngf.entity.risk.RiskPoint;
import com.hngf.entity.risk.RiskPointControlRecord;
import com.hngf.entity.risk.RiskPointControlRecordLog;
import com.hngf.mapper.danger.InspectSchduleCheckRecordMapper;
import com.hngf.mapper.risk.RiskInspectRecordLogMapper;
import com.hngf.mapper.risk.RiskPointCheckRecordLogMapper;
import com.hngf.mapper.risk.RiskPointCheckRecordMapper;
import com.hngf.mapper.risk.RiskPointControlRecordMapper;
import com.hngf.service.danger.InspectSchduleService;
import com.hngf.service.risk.RiskPointControlRecordLogService;
import com.hngf.service.risk.RiskPointControlRecordService;
import com.hngf.service.risk.RiskPointService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class OverdueJob extends QuartzJobBean   {
    private static Logger logger = LoggerFactory.getLogger(OverdueJob.class);
    @Autowired
    private InspectSchduleService inspectSchduleService;
    @Autowired
    private RiskPointService riskPointService;
    @Autowired
    private RiskPointControlRecordLogService riskPointControlRecordLogService;
    @Autowired
    private RiskPointCheckRecordLogMapper riskPointCheckRecordLogMapper;
    @Autowired
    private InspectSchduleCheckRecordMapper inspectSchduleCheckRecordMapper;
    @Autowired
    private RiskInspectRecordLogMapper RiskInspectRecordLogMapper;
    @Autowired
    private RiskPointControlRecordService RiskPointControlRecordService;
    @Autowired
    private RiskPointControlRecordMapper RiskPointControlRecordMapper;

    @Override
    public void executeInternal(JobExecutionContext jobExecutionContext)  {
        System.out.println("************OverdueJob**************");
        List<InspectSchdule> inspectSchduleList =  inspectSchduleService.queryAllOverdueMainTask();
        for(InspectSchdule inspectSchdule: inspectSchduleList ){
            int a=0;
            //1. 如果当前时间 > 计划结束时间 且还没执行  则逾期, 2 执行时间大于计划时间  则逾期执行
            Date date = new Date();
            if(inspectSchdule.getEndDate().before(date)  && null==inspectSchdule.getFinishDate() ){
                inspectSchdule.setStatus(3);  //逾期
                a= 1;
            }
            if(null!= inspectSchdule.getFinishDate() && (inspectSchdule.getEndDate().before(inspectSchdule.getFinishDate())) ){
                inspectSchdule.setStatus(3);  //逾期
                a=1;
            }
            if(a==1){
                InspectSchdule inspectSchdule1VO = new InspectSchdule();
                inspectSchdule1VO.setInspectScheduleId(inspectSchdule.getInspectScheduleId());
                inspectSchdule1VO.setStatus(inspectSchdule.getStatus());
                inspectSchduleService.update(inspectSchdule1VO);
                //查询任务下的风险点
                Map<String,Object> params = new HashMap<>();
                if (null == params.get("type")) {
                    params.put("type", "0");
                }
                params.put("companyId", inspectSchdule.getCompanyId());
                params.put("positionId", inspectSchdule.getInspectPositionId());
                params.put("isActive", 1);
                params.put("userId", inspectSchdule.getExecutor());
                params.put("scheduleId",inspectSchdule.getInspectScheduleId());
                params.put("createdBy",inspectSchdule.getCreatedBy());
                PageUtils scheduleRiskPointList = riskPointService.getScheduleRiskPointList(params, 1, 100, null);
                PageUtils page  = scheduleRiskPointList;
                List<Map<String, Object>>  riskpoints =(List<Map<String, Object>>)page.getList();
                boolean isControl = false;
                for(int k=0;k<riskpoints.size();k++){
                    RiskPointControlRecord record = new RiskPointControlRecord();
                    record.setCompanyId(inspectSchdule.getCompanyId());
                    record.setDetailId(inspectSchdule.getInspectScheduleId());
                    record.setDetailType(Constant.RISK_POINT_ALARM_TASK);
                    record.setRiskPointId(Long.parseLong(riskpoints.get(k).get("riskPointId").toString()));
                    record.setCreatedTime(date);
                    record.setIsCloseUp(0);
                    record.setCauseReason("schdule_overdue_create");
                    record.setIsControl(0);
                    record.setCauseRemark("任务：“ " + inspectSchdule.getRiskInspectDefTitle() + " ” -部门："+inspectSchdule.getInspectGroupName()+" -岗位："+inspectSchdule.getInspectPositionName()+"检查已逾期！");
                    record.setCreatedBy(inspectSchdule.getCreatedBy());

                    RiskPointControlRecordLog riskPointControlRecordLog = new RiskPointControlRecordLog();
                    riskPointControlRecordLog.setCompanyId(inspectSchdule.getCompanyId());
                    riskPointControlRecordLog.setRiskPointId(Long.parseLong(riskpoints.get(k).get("riskPointId").toString()));
                    riskPointControlRecordLog.setDetailId(inspectSchdule.getInspectScheduleId());
                    riskPointControlRecordLog.setDetailType(Constant.RISK_POINT_ALARM_TASK);
                    riskPointControlRecordLog.setCreatedTime(date);
                    riskPointControlRecordLog.setCreatedBy(inspectSchdule.getCreatedBy());
                    //任务[测试2] - 部门[陕西云康-A部门1] - 岗位[班组长]任务检查逾期
                    riskPointControlRecordLog.setCauseRemark("任务：“ " + inspectSchdule.getRiskInspectDefTitle() + " ” -部门："+inspectSchdule.getInspectGroupName()+" -岗位："+inspectSchdule.getInspectPositionName()+"检查已逾期！");
                    //isControl = this.checkRiskPointControl(inspectSchdule.getCompanyId(),Long.parseLong(riskpoints.get(k).get("riskPointId").toString()));
                    riskPointControlRecordLog.setIsControl(isControl ? 1 : 0);
                    riskPointControlRecordLog.setIsCloseUp(isControl ? 1 : 0);
                    riskPointControlRecordLog.setCauseReason("schdule_overdue_create");

                    this.insertRiskPointControlRecordForTask(record);
                    this.riskPointControlRecordLogService.save(riskPointControlRecordLog);
                }

            }
        }
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

    private int insertRiskPointControlRecordForTask(RiskPointControlRecord record) {
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
}
