package com.hngf.web.controller.quartz.config;

import com.hngf.service.quartz.job.BackupDataClearJob;
import com.hngf.service.quartz.job.OverdueJob;
import com.hngf.service.quartz.job.OverdueUpdateJob;
import com.hngf.service.quartz.job.backupDataBaseJob;
import org.quartz.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

@Configuration
public class QuartzConfig {

    /**
     * 1.创建job对象
     * 查询超期任务
     */

    @Bean(name ="overdueJobDetail")
    public JobDetail overdueJobDetail(){
        return JobBuilder.newJob(OverdueJob.class)//业务类
                .withIdentity("overdueJobDetail")//可以给该JobDetail起一个id
                //每个JobDetail内都有一个Map，包含了关联到这个Job的数据，在Job类中可以通过context获取
                .storeDurably()//即使没有Trigger关联时，也不需要删除该JobDetail
                .build();
    }

    /**
     * 每天进行日志清除和 对超期任务进行评分
     * @return
     */
    @Bean(name ="overdueUpdateJobDetail")
    public JobDetail overdueUpdateJobDetail(){
        return JobBuilder.newJob(OverdueUpdateJob.class)//业务类
                .withIdentity("overdueUpdateJobDetail")//可以给该JobDetail起一个id
                //每个JobDetail内都有一个Map，包含了关联到这个Job的数据，在Job类中可以通过context获取
                .storeDurably()//即使没有Trigger关联时，也不需要删除该JobDetail
                .build();
    }

    @Bean(name="JobTrigger1")
    public Trigger JobTrigger1(){
        //      0 * * * * ?
        CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule("0 0 1 * * ?");
        return TriggerBuilder.newTrigger()
                .forJob("overdueJobDetail")//关联上述的JobDetail
                .withIdentity("JobTrigger1")//给Trigger起个名字
                .withSchedule(cronScheduleBuilder)
                .build();
    }

    @Bean(name="JobTrigger2")
    public Trigger JobTrigger2(){
        CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule("0 5 0 * * ?");
        return TriggerBuilder.newTrigger()
                .forJob("overdueUpdateJobDetail")//关联上述的JobDetail
                .withIdentity("JobTrigger2")//给Trigger起个名字
                .withSchedule(cronScheduleBuilder)
                .build();
    }

    @Bean(name ="backupmysql")
    public JobDetail backupmysql(){
        return JobBuilder.newJob(backupDataBaseJob.class)//业务类
                .withIdentity("backupScyfMysql")//可以给该JobDetail起一个id
                .storeDurably()//即使没有Trigger关联时，也不需要删除该JobDetail
                .build();
    }
    //数据库备份  每周备份
    @Bean(name="backupTrigger1")
    public Trigger backupTrigger1(){
        CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule("0 0 1 ? * L");    //每周凌晨一点执行一次
        return TriggerBuilder.newTrigger()
                .forJob("backupScyfMysql")//关联上述的JobDetail
                .withIdentity("backupTrigger1")//给Trigger起个名字
                .withSchedule(cronScheduleBuilder)
                .build();
    }

    @Bean(name ="backupClear")
    public JobDetail backupClear(){
        return JobBuilder.newJob(BackupDataClearJob.class)//业务类
                .withIdentity("backupClear")//可以给该JobDetail起一个id
                .storeDurably()//即使没有Trigger关联时，也不需要删除该JobDetail
                .build();
    }
    //数据库备份  每月底清除上个月备份数据
    @Bean(name="backupClearTrigger")
    public Trigger backupClearTrigger(){
        CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule("0 0 0 L * ?");
        return TriggerBuilder.newTrigger()
                .forJob("backupClear")//关联上述的JobDetail
                .withIdentity("backupClearTrigger")//给Trigger起个名字
                .withSchedule(cronScheduleBuilder)
                .build();
    }
}
