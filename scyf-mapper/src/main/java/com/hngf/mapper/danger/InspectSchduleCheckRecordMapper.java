package com.hngf.mapper.danger;

import com.hngf.entity.danger.InspectSchduleCheckRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Map;
import java.util.List;

/**
 * 检查任务记录表
 * 
 * @author hngf
 * @email 
 * @date 2020-06-18 16:57:21
 */
@Mapper
public interface InspectSchduleCheckRecordMapper {

    List<InspectSchduleCheckRecord> findList(Map<String, Object> params);

    InspectSchduleCheckRecord findById(Long id);

    /**
     * 查询单条记录
     * @param companyId
     * @param scheduleId
     * @param riskPointId
     * @param userId
     * @param checkedCount
     * @return
     */
    InspectSchduleCheckRecord findOne(@Param("companyId") Long companyId, @Param("scheduleId") Long scheduleId, @Param("riskPointId") Long riskPointId, @Param("userId") Long userId, @Param("checkedCount") Integer checkedCount);

    void add(InspectSchduleCheckRecord InspectSchduleCheckRecord);

    void update(InspectSchduleCheckRecord InspectSchduleCheckRecord);

    int deleteById(@Param("id") Long id);

    int deleteByIds(@Param("ids") List ids);

    /**
     * 查询任务检查记录
     * @param companyId
     * @param scheduleId
     * @param userId
     * @return
     */
    List<InspectSchduleCheckRecord> findByScheduleId(@Param("companyId") Long companyId, @Param("scheduleId")Long scheduleId,@Param("userId") Long userId);
    int emptyScheduleCheckRecord();
}
