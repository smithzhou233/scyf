package com.hngf.service.danger;

import com.hngf.common.utils.PageUtils;
import com.hngf.entity.danger.InspectDef;
import com.hngf.entity.danger.InspectSchduleDef;
import com.hngf.entity.danger.InspectSchduleStaff;
import org.quartz.SchedulerException;

import java.util.Map;
import java.util.List;

/**
 * 检查任务定义
 *
 * @author hngf
 * @email 
 * @date 2020-05-20 17:36:23
 */
public interface InspectSchduleDefService {

    PageUtils queryPage(Map<String, Object> params,Integer pageNum,Integer pageSize,String order);

    void scheduleJobs() throws SchedulerException;
    /**
    * 根据ID查询
    */
    InspectSchduleDef getById(Long id);

    /**
    * 保存
    */
    void save(InspectSchduleDef InspectSchduleDef);

    /**
    * 更新
    */
    void update(InspectSchduleDef InspectSchduleDef);

    /**
    * 批量删除
    */
    void removeByIds(List ids);

    /**
    * 删除
    */
    void removeById(Long id);
    /**
     * 根据类型查询检查表
     */
    List<InspectDef> selectInspectDefListByType(Map<String, Object> params);

    /**
    *查询该定义下所有人员
     */
    List<InspectSchduleStaff> selectInspectSchduleStaffList(Long id );
    /**
     * 暂停某定时任务
     */
    boolean pauseSchedulaBySchduleDefId(Long schduleDefId) throws  SchedulerException;

    /**
     * 启动某定时任务
     */
    boolean startScheduleByScheduleId(Long schduleDefId) throws  SchedulerException;
}

