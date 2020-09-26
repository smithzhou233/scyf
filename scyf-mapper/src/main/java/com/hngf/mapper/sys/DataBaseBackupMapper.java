package com.hngf.mapper.sys;

import com.hngf.entity.sys.BackupLog;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface DataBaseBackupMapper {
    void save(BackupLog backupLog);
    List<BackupLog> findLastMonthData();
    void deleteBackupLog();
    List<BackupLog>  findAllData();
}
