package com.hngf.service.danger;

import com.hngf.common.utils.PageUtils;
import com.hngf.entity.danger.Hidden;
import com.hngf.entity.danger.HiddenRecord;

import java.util.Map;
import java.util.List;

/**
 * 隐患表
 *
 * @author hngf
 * @email 
 * @date 2020-05-20 17:36:24
 */
public interface HiddenRecordService {

    PageUtils queryPage(Map<String, Object> params);
    /**
     * @Author: zyj
     * @Description:隐患年度统计 表格 柱形图
     * @Param companyId 企业id groupId群组id year年份
     * @Date 11:32 2020/6/29
     */
    Map<String,Object> getHdangerYearStatistics(Long companyId, Long groupId,Integer year);
    /**
     * @Author: zyj
     * @Description:隐患类型统计柱形图
     * @Param companyId 企业id groupId群组id startTime 开始时间 YYYY-MM-DD endTime结束时间 YYYY-MM-DD
     * @Date 16:32 2020/6/29
     */
    Map<String, Object> getHdangerTypeStatistics(Long companyId,Long groupId, String startTime,String endTime);
    /**
    * 根据ID查询
    */
    HiddenRecord getById(Long id);

    /**
    * 保存
    */
    void save(Hidden hidden);

    /**
    * 更新
    */
    void update(HiddenRecord HiddenRecord);

    /**
    * 批量删除
    */
    void removeByIds(List ids);

    /**
    * 删除
    */
    void removeById(Long id);
}

