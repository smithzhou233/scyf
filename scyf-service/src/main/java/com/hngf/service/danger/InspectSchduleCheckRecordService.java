package com.hngf.service.danger;

import com.hngf.common.utils.PageUtils;
import com.hngf.entity.danger.InspectSchduleCheckRecord;
import java.util.Map;
import java.util.List;

/**
 * 检查任务记录表
 *
 * @author hngf
 * @email 
 * @date 2020-06-18 16:57:21
 */
public interface InspectSchduleCheckRecordService {

    PageUtils queryPage(Map<String, Object> params, Integer pageNum, Integer pageSize, String order);

    /**
     * 根据任务ID查询检查任务记录
     * @param companyId
     * @param scheduleId
     * @param userId
     * @return
     */
    List<InspectSchduleCheckRecord> getList(Long companyId, Long scheduleId, Long userId);

    /**
    * 根据ID查询
    */
    InspectSchduleCheckRecord getById(Long id);

    /**
     * 查询一条记录
     * @param companyId
     * @param scheduleId
     * @param riskPointId
     * @param userId
     * @param checkedCount
     * @return
     */
    InspectSchduleCheckRecord getOne(Long companyId, Long scheduleId,Long riskPointId, Long userId,Integer checkedCount);

    /**
    * 保存
    */
    void save(InspectSchduleCheckRecord InspectSchduleCheckRecord);

    /**
    * 更新
    */
    void update(InspectSchduleCheckRecord InspectSchduleCheckRecord);

    /**
    * 批量删除
    */
    void removeByIds(List ids);

    /**
    * 删除
    */
    void removeById(Long id);
}

