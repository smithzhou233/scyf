package com.hngf.service.quartz.job;

import com.hngf.common.utils.Constant;
import com.hngf.dto.sys.GroupMemberPositionDto;
import com.hngf.entity.danger.InspectSchdule;
import com.hngf.entity.danger.InspectSchduleDef;
import com.hngf.entity.sys.GroupMemberPosition;
import com.hngf.mapper.danger.InspectSchduleCheckRecordMapper;
import com.hngf.mapper.risk.RiskInspectRecordLogMapper;
import com.hngf.mapper.risk.RiskPointCheckRecordLogMapper;
import com.hngf.service.danger.InspectSchduleDefService;
import com.hngf.service.danger.InspectSchduleService;
import com.hngf.service.risk.RiskCtrlService;
import com.hngf.service.score.ScoreUserService;
import com.hngf.service.sys.GroupMemberPositionService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.*;

@Component
public class OverdueUpdateJob extends QuartzJobBean {
    private static Logger logger = LoggerFactory.getLogger(OverdueUpdateJob.class);
    @Autowired
    private InspectSchduleService inspectSchduleService;
    @Autowired
    private InspectSchduleDefService inspectSchduleDefService;

    @Autowired
    private RiskPointCheckRecordLogMapper riskPointCheckRecordLogMapper;
    @Autowired
    private InspectSchduleCheckRecordMapper inspectSchduleCheckRecordMapper;
    @Autowired
    private RiskInspectRecordLogMapper RiskInspectRecordLogMapper;

    @Autowired
    private RiskCtrlService riskCtrlService;

    @Autowired
    private ScoreUserService scoreUserService;

    @Resource
    private GroupMemberPositionService groupMemberPositionService;


    private void info(InspectSchdule dto, String message) {
        logger.info(String.format("任务%s[ID:%s], 岗位- %s，%s", dto.getRiskInspectDefTitle(), dto.getInspectScheduleId(), dto.getInspectPositionName(), message));
    }


    @Override
    public void executeInternal(JobExecutionContext jobExecutionContext)   {
        System.out.println("************OverdueUpdateJob**************");
        logger.info("start 清空管控措施检查记录日志【" + new Date() + "】");
        this.RiskInspectRecordLogMapper.truncateCheckedRecordLog();
        logger.info("end 清空完成【" + new Date() + "】table = scyf_risk_inspect_record_log");

        logger.info("start 清空风险点检查日志【" + new Date() + "】");
        this.riskPointCheckRecordLogMapper.truncateRiskPointCheckRecordLog();
        logger.info("end 清空完成【" + new Date() + "】table = scyf_risk_point_check_record_log");

        logger.info("start 清除检查完成的检查记录【" + new Date() + "】");
        this.inspectSchduleCheckRecordMapper.emptyScheduleCheckRecord();
        logger.info("end 完成清除【" + new Date() + "】table = scyf_inspect_schdule_check_record");

        List<InspectSchdule> inspectSchduleList =  inspectSchduleService.queryInspectSchduleList();
        if (inspectSchduleList.isEmpty()) {
            logger.info("没有逾期任务，定时任务结束");
        } else {
            Iterator var2 = inspectSchduleList.iterator();
            while(true) {
                label88:
                while(true) {
                    InspectSchdule dto;
                    do {
                        if (!var2.hasNext()) {
                            logger.info("逾期任务检查结束");
                            return;
                        }

                        dto = (InspectSchdule)var2.next();
                    } while(dto.getStatus() == 4);

                    logger.info("task:" + dto.getRiskInspectDefTitle() + dto.getInspectPositionName());
                    InspectSchduleDef riskCheckDefDto = this.inspectSchduleDefService.getById(dto.getSchduleDefId());
                    Long positionId = dto.getInspectPositionId();
                    Integer scheduleCount = dto.getScheduleCount();
                    Long groupId = dto.getInspectGroupId();
                    Map map = new HashMap(2);
                    map.put("companyId", dto.getCompanyId());
                    map.put("positionId", dto.getInspectPositionId());
                    Integer ctlLevel = this.riskCtrlService.getCurrentUserCtlLevel(map);
                    if (ctlLevel == null) {
                        this.info(dto, "管控层级没有定义");
                    } else {
                        logger.info("逾期任务打分开始");
                        HashMap map1;
                        Iterator var12;
                        try {
                            if (riskCheckDefDto.getInspectType().equals("random")) {
                                if (ctlLevel.equals(0)) {
                                    this.scoreUserService.goGrade(dto.getCompanyId(), groupId, dto.getExecutor(), Constant.SCORE_SETTING_SCHDULE_YQ_1, dto.getInspectScheduleId());
                                    logger.info("临时任务逾期：【" + dto.getRiskInspectDefTitle() + "】岗位：【" + dto.getInspectPositionName() + "】人员：【" + dto.getExecutorName() + "】 已打分处理！");
                                }
                            } else if (riskCheckDefDto.getInspectType().equals("fixed")) {
                                if (ctlLevel.equals(0)) {
                                    this.scoreUserService.goGrade(dto.getCompanyId(), groupId, dto.getExecutor(), Constant.SCORE_SETTING_SCHDULE_YQ_1, dto.getInspectScheduleId());
                                    logger.info("常规基础任务逾期：【" + dto.getRiskInspectDefTitle() + "】岗位：【" + dto.getInspectPositionName() + "】人员：【" + dto.getExecutorName() + "】 已打分处理！");
                                } else {
                                    map1 = new HashMap();
                                    map1.put("positionId", dto.getInspectPositionId());
                                    map1.put("groupId", groupId);
                                    List<GroupMemberPositionDto> groupMemberPositions = this.groupMemberPositionService.getGroupMemberPositionList(map1);
                                    if (!groupMemberPositions.isEmpty()) {
                                        var12 = groupMemberPositions.iterator();

                                        while(var12.hasNext()) {
                                            GroupMemberPosition user = (GroupMemberPosition)var12.next();
                                            this.scoreUserService.goGrade(dto.getCompanyId(), groupId, user.getUserId(), Constant.SCORE_SETTING_SCHDULE_YQ_1, dto.getInspectScheduleId());
                                            logger.info("常规现场任务逾期：【" + dto.getRiskInspectDefTitle() + "】岗位：【" + dto.getInspectPositionName() + "】人员ID：【" + user.getUserId() + "】 已打分处理！");
                                        }
                                    }
                                }
                            }
                        }catch (Exception e){
                            logger.error("逾期任务打分出错");
                        }

                        logger.info("逾期任务打分结束");
                        logger.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>><<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
                    }
                }
            }
        }
    }
}
