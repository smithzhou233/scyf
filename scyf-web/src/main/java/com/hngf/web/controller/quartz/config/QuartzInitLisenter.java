package com.hngf.web.controller.quartz.config;

import com.hngf.service.danger.InspectSchduleDefService;

import org.quartz.Scheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

@Component
public class QuartzInitLisenter implements ApplicationListener<ContextRefreshedEvent> {
    @Autowired
    private InspectSchduleDefService inspectSchduleDefService;


    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        try {
           inspectSchduleDefService.scheduleJobs();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("QuartzInitLisenter  start >>>>>>>");

    }

}
