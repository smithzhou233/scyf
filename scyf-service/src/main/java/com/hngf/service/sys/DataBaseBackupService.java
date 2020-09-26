package com.hngf.service.sys;


import com.hngf.common.utils.PageUtils;
import com.hngf.entity.sys.BackupLog;

import java.util.List;
import java.util.Map;

public interface DataBaseBackupService {

    void save(BackupLog backupLog) ;
    List<BackupLog> findLastMonthData();
    void deleteBackupLog();
    PageUtils findAllData(Map<String, Object> params);
}
