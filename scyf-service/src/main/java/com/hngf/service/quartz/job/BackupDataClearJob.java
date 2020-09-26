package com.hngf.service.quartz.job;

import com.hngf.entity.sys.BackupLog;
import com.hngf.service.sys.DataBaseBackupService;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.List;

@Component
public class BackupDataClearJob  extends QuartzJobBean {

    @Autowired
    DataBaseBackupService dataBaseBackupService;

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        List<BackupLog> backupLogs = dataBaseBackupService.findLastMonthData();
        for (BackupLog backupLog: backupLogs ) {
            String path=backupLog.getBuUrl();
            File file  = new File(path);
            if(file.exists()){
                System.out.println("删除文件"+path);
                file.delete();
            }
        }
    }
}
