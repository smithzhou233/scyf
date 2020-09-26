package com.hngf.service.danger.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hngf.common.utils.Constant;
import com.hngf.common.utils.CronErpParser;
import com.hngf.common.utils.PageUtils;
import com.hngf.entity.danger.InspectDef;
import com.hngf.entity.danger.InspectSchdule;
import com.hngf.entity.danger.InspectSchduleDef;
import com.hngf.entity.danger.InspectSchduleStaff;
import com.hngf.mapper.danger.InspectSchduleDefMapper;
import com.hngf.mapper.danger.InspectSchduleMapper;
import com.hngf.mapper.sys.GroupMemberPositionMapper;
import com.hngf.service.danger.InspectSchduleDefService;
import com.hngf.service.quartz.job.QuartzJobExample;
import com.hngf.service.sys.MessageService;
import com.hngf.service.sys.SettingService;
import com.hngf.service.utils.SendMessageUtil;
import org.apache.commons.lang.StringUtils;
import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service("InspectSchduleDefService")
public class InspectSchduleDefServiceImpl implements InspectSchduleDefService {

    @Autowired
    private InspectSchduleDefMapper InspectSchduleDefMapper;
    @Autowired
    private Scheduler scheduler;
    @Autowired
    private  InspectSchduleMapper InspectSchduleMapper;
    @Autowired
    private SettingService settingService;
    @Autowired
    private MessageService messageService;
    @Autowired
    private GroupMemberPositionMapper groupMemberPositionMapper;


    @Override
    public PageUtils queryPage(Map<String, Object> params,Integer pageNum,Integer pageSize,String order) {
        //利用PageHelper分页查询 注意：这个一定要放查询语句的前一行,否则无法进行分页,因为它对紧随其后第一个sql语句有效
        if(StringUtils.isNotBlank(order)){
            PageHelper.startPage(pageNum, pageSize,order);
        }else{
            PageHelper.startPage(pageNum, pageSize);
        }
        List<InspectSchduleDef> list = InspectSchduleDefMapper.findList(params);
        for (InspectSchduleDef inspectSchduleDef: list ) {
            if(StringUtils.isNotEmpty(inspectSchduleDef.getScheduleCronExpression() )){
                inspectSchduleDef.setCronExpressionByParse(CronErpParser.descCorn(inspectSchduleDef.getScheduleCronExpression()));
            }
        }
        PageInfo<InspectSchduleDef> pageInfo = new PageInfo<InspectSchduleDef>(list);
        return new PageUtils(list,(int)pageInfo.getTotal(),pageInfo.getPages(),pageInfo.getPageSize(),pageInfo.getPrePage() + 1);
    }

    @Override
    public void scheduleJobs() throws SchedulerException{
        queryAllSchduleDef(scheduler);
    }

    /**
     * 添加定时任务
     *
     * @param
     * @param cronExpression
     * @param
     */
    private void schedulerAdd(String schduleDefId, String cronExpression ) throws Exception {
        try {
            // 启动调度器
           // scheduler.start();
            JobKey jobKey = new JobKey(schduleDefId.toString() );

            // 构建job信息
            if (!scheduler.checkExists(jobKey)) {
                JobDetail jobDetail = JobBuilder.newJob(QuartzJobExample.class).storeDurably(true).withIdentity(jobKey).build();
                scheduler.addJob(jobDetail, true);
            }

            // 表达式调度构建器(即任务执行的时间)
            CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(cronExpression);
            // 按新的cronExpression表达式构建一个新的trigger
            TriggerKey triggerKey= TriggerKey.triggerKey(schduleDefId);
            CronTrigger trigger = TriggerBuilder.newTrigger().withIdentity(triggerKey).forJob(jobKey).startNow().withSchedule(scheduleBuilder).build();
            trigger.getJobDataMap().put("SchduleDefId",schduleDefId);
            scheduler.scheduleJob(trigger);
        } catch (Exception e) {
            throw new Exception("该定时任务出错：" + schduleDefId, e);
        }
    }

    /**
     * 删除定时任务
     *
     * @param schduleDefId
     */
    private void schedulerDelete(String schduleDefId)  throws  Exception{
        try {
            scheduler.pauseTrigger(TriggerKey.triggerKey(schduleDefId));
            scheduler.unscheduleJob(TriggerKey.triggerKey(schduleDefId));
            scheduler.deleteJob(JobKey.jobKey(schduleDefId));
        } catch (Exception e) {
            throw new Exception("删除定时任务失败");
        }
    }

    /**
     * 检查任务分为两种 1.常规检查  a.设置周期性定时任务
     *                  2.临时检查  a.开始执行时间和截至时间
     * @param scheduler
     * @return
     * @throws SchedulerException
     */
    public List<InspectSchduleDef> queryAllSchduleDef(Scheduler scheduler) throws SchedulerException  {
       // scheduler.shutdown();
        Map<String, Object> params  = new HashMap<>();
        params.put("isOpen",0);   //查询所有已开启的定时任务状态
        List<InspectSchduleDef> jobList = InspectSchduleDefMapper.findListBySchedule(params);
         scheduler.start();
        synchronized(this) {
            if (jobList != null && !jobList.isEmpty()) {
                for (int i = 0; i < jobList.size(); i++) {

                    InspectSchduleDef inspectSchduleDef = jobList.get(i);
                    if(inspectSchduleDef.getInspectType().equals("fixed")){
                        JobKey jobKey = new JobKey(inspectSchduleDef.getSchduleDefId().toString());
                        JobDetail jobDetail = JobBuilder.newJob(QuartzJobExample.class).withIdentity(jobKey).build();
                        CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(inspectSchduleDef.getScheduleCronExpression());
                        TriggerKey triggerKey = TriggerKey.triggerKey(inspectSchduleDef.getSchduleDefId().toString());
                        Trigger cronTrigger = TriggerBuilder.newTrigger().withIdentity(triggerKey).withSchedule(scheduleBuilder).startNow().build();
                        cronTrigger.getJobDataMap().put("SchduleDefId", inspectSchduleDef.getSchduleDefId());
                        if (!scheduler.checkExists(jobKey)) {
                            scheduler.scheduleJob(jobDetail, cronTrigger);
                        }
                    }/*else if(inspectSchduleDef.getInspectType().equals("random")){
                        JobKey jobKey = new JobKey(inspectSchduleDef.getSchduleDefId().toString());
                        JobDetail jobDetail = JobBuilder.newJob(QuartzJobExample.class).withIdentity(jobKey).build();
                    }*/
                       /*  scheduler.pauseTrigger(cronTrigger.getKey());   // 停止触发器
                        scheduler.unscheduleJob(cronTrigger.getKey());// 移除触发器
                        scheduler.deleteJob(jobKey);// 删除任务*/
                }
            }
        }
        return jobList;
    }

    @Override
    public InspectSchduleDef getById(Long id){
        InspectSchduleDef inspectSchduleDef = InspectSchduleDefMapper.findById(id);
        if(null != inspectSchduleDef && null!=inspectSchduleDef.getScheduleCronExpression()){
            inspectSchduleDef.setCronExpressionByParse(CronErpParser.descCorn(inspectSchduleDef.getScheduleCronExpression()));
        }
        return inspectSchduleDef;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(InspectSchduleDef InspectSchduleDef) {
        InspectSchduleDefMapper.add(InspectSchduleDef);
        List<InspectSchduleStaff>  inspectSchduleStaffList = commonAddInspectSchduleStaff(InspectSchduleDef);
        //对于临时检查  直接进行分发，设置开始和截至时间
        if(InspectSchduleDef.getInspectType().equals("random")){
            addInspectScheduleList(InspectSchduleDef,inspectSchduleStaffList);
        }else{
            //设置定时任务默认开启
            try {
                schedulerAdd(InspectSchduleDef.getSchduleDefId().toString(),InspectSchduleDef.getScheduleCronExpression());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void addInspectScheduleList(InspectSchduleDef inspectSchduleDef,List<InspectSchduleStaff>  inspectSchduleStaffList){
        InspectSchdule inspectSchdule = null;
        List<InspectSchdule> inspectSchduleList = new ArrayList<>();
        for(int i=0;i<inspectSchduleStaffList.size();i++){
            InspectSchduleStaff inspectSchduleStaff = inspectSchduleStaffList.get(i);
             if(inspectSchduleStaff.getInspectMode()==0){
                 inspectSchdule = new InspectSchdule();
                 inspectSchdule.setInspectMode (inspectSchduleDef.getInspectMode());
                 inspectSchdule.setCompanyId(inspectSchduleStaff.getCompanyId());
                 inspectSchdule.setExecutor(inspectSchduleStaff.getStaffNo());
                 inspectSchdule.setInspectDefId(inspectSchduleDef.getInspectDefId());
                 inspectSchdule.setStatus(0);
                 inspectSchdule.setStartDate(inspectSchduleDef.getStartDate());
                 inspectSchdule.setEndDate(inspectSchduleDef.getEndDate());
                 inspectSchdule.setSchduleDefId(inspectSchduleDef.getSchduleDefId());
                 inspectSchdule.setInspectGroupId(inspectSchduleDef.getRiskInspectGroup());
                 inspectSchdule.setInspectPositionId(inspectSchduleDef.getRiskInspectPosition());
                 inspectSchduleList.add(inspectSchdule);
             }

        }
        try {
            if(null!=inspectSchduleList && inspectSchduleList.size()>0){
                InspectSchduleMapper.addList(inspectSchduleList);
                propellingMovement(inspectSchduleList);
                System.out.println("*****************************************************************");
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }


    /**
     * 推送消息
     * @param inspectSchdules
     * @throws Exception
     */
    private void propellingMovement( List<InspectSchdule>  inspectSchdules) throws Exception {
        for (InspectSchdule inspectSchdule:inspectSchdules ) {
            String stype = inspectSchdule.getInspectMode()==1?"现场检查":"基础检查";
            SendMessageUtil.sendMessage(inspectSchdule.getCompanyId(), "【" + stype+ "】", inspectSchdule.getExecutor(), "开始检查时间【" + inspectSchdule.getStartDate() + "】", this.messageService, this.settingService);
        }

    }
    private InspectSchduleStaff toSetInspectSchduleStaff(InspectSchduleDef inspectSchduleDef,String StaffNo,int inspectMode ){
        InspectSchduleStaff  inspectSchduleStaff = new InspectSchduleStaff();
        inspectSchduleStaff.setStaffNo(Long.parseLong(StaffNo));
        inspectSchduleStaff.setCompanyId(inspectSchduleDef.getCompanyId());
        inspectSchduleStaff.setGroupId(inspectSchduleDef.getGroupId());
        inspectSchduleStaff.setDelFlag(Constant.DEL_FLAG_0);
        inspectSchduleStaff.setCreatedBy(inspectSchduleDef.getCreatedBy());
        inspectSchduleStaff.setInspectMode(inspectMode);
        inspectSchduleStaff.setSchduleDefId(inspectSchduleDef.getSchduleDefId());
        return inspectSchduleStaff;
    }

    private   List<InspectSchduleStaff> commonAddInspectSchduleStaff(InspectSchduleDef inspectSchduleDef){
        List<InspectSchduleStaff> inspectSchduleStaffList = new ArrayList<>();
        //保存人员  Participants 参与人     excutor 执行人
        if(StringUtils.isNotEmpty(inspectSchduleDef.getExecutor())){
            String[] excutorArr = inspectSchduleDef.getExecutor().split(",");
            for (String excutorStr: excutorArr  ) {
                InspectSchduleStaff inspectSchduleStaff = toSetInspectSchduleStaff(inspectSchduleDef,excutorStr,0);
                inspectSchduleStaffList.add(inspectSchduleStaff);
            }
        }

        if(StringUtils.isNotEmpty(inspectSchduleDef.getRiskInspectParticipant())){
            String[] participantArr = inspectSchduleDef.getRiskInspectParticipant().split(",");
            for (String partStr:participantArr){
                InspectSchduleStaff inspectSchduleStaff = toSetInspectSchduleStaff(inspectSchduleDef,partStr,1);
                inspectSchduleStaffList.add(inspectSchduleStaff);
            }
        }
        if(null!=inspectSchduleStaffList && inspectSchduleStaffList.size()>0){
            InspectSchduleDefMapper.addInspectSchduleStaff(inspectSchduleStaffList);
        }
        return inspectSchduleStaffList;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(InspectSchduleDef InspectSchduleDef)     {
        InspectSchduleDefMapper.update(InspectSchduleDef);
        //删除之前的执行人参与人
        deleteInspectSchduleDefStaffs(InspectSchduleDef.getSchduleDefId());
        //添加新的执行人参与人
        commonAddInspectSchduleStaff(InspectSchduleDef);
        //在定时任务启动状态下   每次修改都先停止之前的定时任务  然后重新设置定时任务
        try {
            if(InspectSchduleDef.getIsOpen().equals(Constant.STATUS_NORMAL)){
                schedulerDelete(InspectSchduleDef.getSchduleDefId().toString());
                schedulerAdd(InspectSchduleDef.getSchduleDefId().toString(),InspectSchduleDef.getScheduleCronExpression());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }



    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeByIds(List ids) {
        InspectSchduleDefMapper.deleteByIds(ids);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeById(Long id) {
        InspectSchduleDefMapper.deleteById(id);
    }

    @Override
    public List<InspectDef> selectInspectDefListByType(Map param) {
        return InspectSchduleDefMapper.selectInspectDefListByType(param);
    }

    @Override
    public List<InspectSchduleStaff> selectInspectSchduleStaffList(Long id) {
        return InspectSchduleDefMapper.selectInspectSchduleStaffList(id);
    }

    @Override
    public boolean pauseSchedulaBySchduleDefId(Long schduleDefId)  throws  SchedulerException{
        TriggerKey triggerKey = TriggerKey.triggerKey(schduleDefId.toString());
        Trigger   trigger = scheduler.getTrigger(triggerKey);
     	if (trigger == null) {
     	    return false;
     	}
        scheduler.pauseTrigger(triggerKey);// 停止触发器
        InspectSchduleDef inspectSchduleDef = new InspectSchduleDef();
        inspectSchduleDef.setSchduleDefId(schduleDefId);
        inspectSchduleDef.setIsOpen(1);
        InspectSchduleDefMapper.update(inspectSchduleDef);
        return  true;

    }

    @Override
    public boolean startScheduleByScheduleId(Long schduleDefId) throws SchedulerException {
        TriggerKey triggerKey = TriggerKey.triggerKey(schduleDefId.toString());
        Trigger   trigger = scheduler.getTrigger(triggerKey);
        if (trigger == null) {
            JobKey jobKey = new JobKey(schduleDefId.toString());
            JobDetail jobDetail = JobBuilder.newJob(QuartzJobExample.class).withIdentity(jobKey).build();
            InspectSchduleDef inspectSchduleDefDetail = getById(schduleDefId);
            CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(inspectSchduleDefDetail.getScheduleCronExpression());
            TriggerKey triggerKeyN = TriggerKey.triggerKey(schduleDefId.toString());
            Trigger cronTrigger = TriggerBuilder.newTrigger().withIdentity(triggerKeyN).withSchedule(scheduleBuilder).startNow().build();
            cronTrigger.getJobDataMap().put("SchduleDefId", inspectSchduleDefDetail.getSchduleDefId());
            if (!scheduler.checkExists(jobKey)) {
                scheduler.scheduleJob(jobDetail, cronTrigger);
            }
         //   return false;
        }else {
            scheduler.resumeTrigger(triggerKey);// 启动触发器
        }
        InspectSchduleDef inspectSchduleDef = new InspectSchduleDef();
        inspectSchduleDef.setSchduleDefId(schduleDefId);
        inspectSchduleDef.setIsOpen(0);
        InspectSchduleDefMapper.update(inspectSchduleDef);
        return  true;
    }

    private void deleteInspectSchduleDefStaffs(Long schduleDefId){
        InspectSchduleDefMapper.deleteStaffByschduleDefId(schduleDefId);
    }

}
