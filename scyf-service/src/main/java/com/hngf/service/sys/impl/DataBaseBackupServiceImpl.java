package com.hngf.service.sys.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hngf.common.utils.Constant;
import com.hngf.common.utils.PageUtils;
import com.hngf.entity.sys.BackupLog;
import com.hngf.mapper.sys.DataBaseBackupMapper;
import com.hngf.service.sys.DataBaseBackupService;
import com.hngf.service.sys.DictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service("DataBaseBackupService")
public class DataBaseBackupServiceImpl implements DataBaseBackupService {

    @Autowired
    DataBaseBackupMapper dataBaseBackupMapper;
    @Autowired
    private DictService dictService;

    @Override
    public void save(BackupLog backupLog) {
        Long buId = dictService.getTabId("sys_backup_log");
        backupLog.setBuId(buId);
        dataBaseBackupMapper.save(backupLog);
    }

    @Override
    public List<BackupLog> findLastMonthData() {
        return dataBaseBackupMapper.findLastMonthData();
    }

    @Override
    public void deleteBackupLog() {
        dataBaseBackupMapper.deleteBackupLog();
    }

    @Override
    public PageUtils findAllData(Map<String, Object> params) {
        int pageNum = null!= params.get("pageNum") ? Integer.parseInt(params.get("pageNum").toString()) : 1;
        int pageSize = null != params.get("pageSize") ? Integer.parseInt(params.get("pageSize").toString()) : Constant.PAGE_SIZE;
        PageHelper.startPage(pageNum, pageSize);
        List<BackupLog> list =dataBaseBackupMapper.findAllData();
        PageInfo pageInfo = new PageInfo(list);
        return new PageUtils(list,(int)pageInfo.getTotal(),pageInfo.getPages(),pageInfo.getPageSize(),pageInfo.getPrePage() + 1);

    }

}
