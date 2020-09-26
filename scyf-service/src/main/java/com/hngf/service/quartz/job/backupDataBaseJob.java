package com.hngf.service.quartz.job;

import com.hngf.entity.sys.BackupLog;
import com.hngf.service.sys.DataBaseBackupService;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Date;
import java.util.UUID;

@Component
public class backupDataBaseJob  extends QuartzJobBean {

    @Autowired
    DataBaseBackupService dataBaseBackupService;
    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        Runtime runtime = Runtime.getRuntime(); //获取执行命令的对象
        String fileName = UUID.randomUUID().toString()+".sql";//备份文件的名字
        ///usr/local/mysql/bin/  mysql按照路径bin路径
        String command = "cmd /c c:/mysqldump -hlocalhost -uroot -paaaaaa  slj_scyf>d:/scyfDataBaseBackup/"+fileName;//-c代表立即执行；c:/mysqldump代表执行c盘下的mysqldump程序命令；day06是要备份的数据库名；d:/代表备份在D盘下

        //执行命令
        Process exec = null;
        try {
            exec = runtime.exec(command);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("备份成功！");
        BackupLog backupLog = new BackupLog();
        backupLog.setBuTime(new Date());
        backupLog.setBuUrl("d:/scyfDataBaseBackup/"+fileName);
        dataBaseBackupService.save(backupLog);
    }
}
