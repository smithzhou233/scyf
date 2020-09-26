package com.hngf.web.controller.quartz.config;

import com.hngf.service.quartz.job.OverdueJob;
import com.hngf.service.quartz.job.OverdueUpdateJob;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

//@Configuration
public class OverDueUpdateConfig {

    /**
     * 1.创建job对象
     */
    @Bean
    public JobDetailFactoryBean OverdueUpdateJobDetailFactoryBean(){
        JobDetailFactoryBean factory =new JobDetailFactoryBean();
        factory.setJobClass(OverdueUpdateJob.class);
        return  factory;
    }


    /**
     * 2.创建trriger对象
     */
  /*  @Bean
    public SimpleTriggerFactoryBean simpleTriggerFactoryBean(JobDetailFactoryBean jobDetailFactoryBean){
        SimpleTriggerFactoryBean factoryBean  = new SimpleTriggerFactoryBean();

        //关联jobDetail
        factoryBean.setJobDetail(jobDetailFactoryBean.getObject());
        factoryBean.setRepeatInterval(5000);
        factoryBean.setRepeatCount(0);
        return factoryBean;
    }*/
    //0 0 1 * * ?   每天凌晨一点执行一次
    @Bean
    public CronTriggerFactoryBean overDueUpdateTrigger(JobDetailFactoryBean jobDetailFactoryBean){
        CronTriggerFactoryBean factoryBean  = new CronTriggerFactoryBean();
        //关联jobDetail
        factoryBean.setJobDetail(jobDetailFactoryBean.getObject());
        factoryBean.setCronExpression("0 0 1 * * ?");
        System.out.println("---*********************************---");
        return factoryBean;
    }


    /**
    * 3.创建schedual对象
    */
  @Bean
    public SchedulerFactoryBean schedulerFactoryBean(CronTriggerFactoryBean cronTriggerFactoryBean  ,MyAdaptableJobFactory myAdaptableJobFactory){
      SchedulerFactoryBean schedulerFactoryBean  = new SchedulerFactoryBean();
      //关联trriger
      schedulerFactoryBean.setTriggers(cronTriggerFactoryBean.getObject());
      schedulerFactoryBean.setJobFactory(myAdaptableJobFactory);
      return  schedulerFactoryBean;
  }

}
