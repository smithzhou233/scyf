package com.hngf.service.quartz.job;

import com.hngf.service.danger.InspectSchduleDefService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;

public class pauseTriggerJob implements Job  {

    @Autowired
    private InspectSchduleDefService InspectSchduleDefService;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException  {
        Long SchduleDefId =  (Long) jobExecutionContext.getTrigger().getJobDataMap().get("SchduleDefId");
        try {
            InspectSchduleDefService.pauseSchedulaBySchduleDefId(SchduleDefId);
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }
}
