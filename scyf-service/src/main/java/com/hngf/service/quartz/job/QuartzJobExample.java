package com.hngf.service.quartz.job;

import com.hngf.common.utils.DateUtils;
import com.hngf.common.utils.HolidayUtil;
import com.hngf.entity.danger.Hidden;
import com.hngf.entity.danger.InspectSchdule;
import com.hngf.entity.danger.InspectSchduleDef;
import com.hngf.entity.danger.InspectSchduleStaff;
import com.hngf.service.danger.InspectSchduleDefService;
import com.hngf.service.danger.InspectSchduleService;
import com.hngf.service.sys.MessageService;
import com.hngf.service.sys.SettingService;
import com.hngf.service.utils.SendMessageUtil;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


//@PersistJobDataAfterExecution
//@DisallowConcurrentExecution
public class QuartzJobExample implements Job  {


    @Autowired
    private InspectSchduleDefService InspectSchduleDefService;
    @Autowired
    private InspectSchduleService inspectSchduleService;
    @Autowired
    private SettingService settingService;
    @Autowired
    private MessageService messageService;

    @Override
    public synchronized    void  execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        Date date = new Date();
        SimpleDateFormat f=new SimpleDateFormat("yyyyMMdd");
        String nowdate = f.format(date);
      //  int bool =HolidayUtil.request(nowdate);   //判断是否为节假日，如果为节假日则不执行
     //   if(bool==0) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
            //此处得到检查任务定义的id   然后去执行分发任务
            // System.out.println(jobExecutionContext.getTrigger().getJobDataMap().get("SchduleDefId") +"    "+jobExecutionContext.getJobInstance());
            System.out.println(jobExecutionContext.getTrigger().getJobDataMap().get("SchduleDefId") + "----  Date" + simpleDateFormat.format(new Date()));
            Object SchduleDefId = jobExecutionContext.getTrigger().getJobDataMap().get("SchduleDefId");
            InspectSchduleDef inspectSchduleDef = InspectSchduleDefService.getById(Long.parseLong(SchduleDefId.toString()));
            List<InspectSchduleStaff> inspectSchduleStaffList = InspectSchduleDefService.selectInspectSchduleStaffList(inspectSchduleDef.getSchduleDefId());
            List<InspectSchdule> inspectSchduleList = new ArrayList<>();
            InspectSchdule inspectSchdule;
            java.util.Calendar cal = java.util.Calendar.getInstance();
            cal.add(java.util.Calendar.DATE, 1);//这里改为1
            Date time = cal.getTime();
            Date sdfDate = null;
            try {
                sdfDate = simpleDateFormat.parse(simpleDateFormat.format(time));
                if(inspectSchduleDef.getInspectMode()==1 && null!=inspectSchduleStaffList && inspectSchduleStaffList.size()>0){
                    for (int i = 0; i < inspectSchduleStaffList.size(); i++) {
                        inspectSchdule = new InspectSchdule();
                        inspectSchdule.setInspectMode(inspectSchduleDef.getInspectType().equals("random") ? inspectSchduleDef.getInspectMode() : 1);
                        InspectSchduleStaff inspectSchduleStaff = inspectSchduleStaffList.get(i);
                        inspectSchdule.setCompanyId(inspectSchduleStaff.getCompanyId());
                        inspectSchdule.setExecutor(inspectSchduleStaff.getStaffNo());
                        inspectSchdule.setInspectDefId(inspectSchduleDef.getInspectDefId());
                        inspectSchdule.setStatus(0);
                        inspectSchdule.setSchduleDefId(inspectSchduleDef.getSchduleDefId());
                        inspectSchdule.setInspectGroupId(inspectSchduleDef.getRiskInspectGroup());
                        inspectSchdule.setInspectPositionId(inspectSchduleDef.getRiskInspectPosition());
                        inspectSchdule.setEndDate(sdfDate);
                        //System.out.println(inspectSchdule.getSchduleDefId()+"----"+ inspectSchdule.getEndDate()+"-----"+inspectSchdule.getInspectMode());
                        inspectSchduleList.add(inspectSchdule);
                    }
                }else if(inspectSchduleDef.getInspectMode()==2){  //基础检查
                    inspectSchdule = new InspectSchdule();
                    inspectSchdule.setInspectMode(inspectSchduleDef.getInspectMode());
                    inspectSchdule.setCompanyId(inspectSchduleDef.getCompanyId());
                    inspectSchdule.setInspectDefId(inspectSchduleDef.getInspectDefId());
                    inspectSchdule.setSchduleDefId(inspectSchduleDef.getSchduleDefId());
                    inspectSchdule.setInspectGroupId(inspectSchduleDef.getRiskInspectGroup());
                    inspectSchdule.setStatus(0);
                    inspectSchdule.setInspectPositionId(inspectSchduleDef.getRiskInspectPosition());
                    inspectSchdule.setEndDate(sdfDate);
                    inspectSchduleList.add(inspectSchdule);
                }
                if (null != inspectSchduleList && inspectSchduleList.size() > 0) {
                    inspectSchduleService.saveList(inspectSchduleList);
                    propellingMovement(inspectSchduleList);  //消息推送
                    //  System.out.println("*****************************************************************");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        //}
    }

    /**
     * 推送消息
     * @param inspectSchdules
     * @throws Exception
     */
    private void propellingMovement( List<InspectSchdule>  inspectSchdules) throws Exception {
        SimpleDateFormat simpleDateFormat =new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        for (InspectSchdule inspectSchdule:inspectSchdules ) {
            String stype = inspectSchdule.getInspectMode()==1?"现场检查":"基础检查";
            SendMessageUtil.sendMessage(inspectSchdule.getCompanyId(), "【" + stype+ "】", inspectSchdule.getExecutor(), "开始检查时间【" +
                    simpleDateFormat.format(new Date()) + "】", this.messageService, this.settingService);
        }

    }
}
