package com.hngf.mapper.danger;

import com.hngf.entity.danger.Hidden;
import com.hngf.entity.danger.HiddenRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Map;
import java.util.List;

/**
 * 隐患表
 * 
 * @author hngf
 * @email 
 * @date 2020-05-20 17:36:24
 */
@Mapper
public interface HiddenRecordMapper {

    List<HiddenRecord> findList(Map<String, Object> params);
    /**
    * @Author: zyj
    * @Description:隐患年度统计 表格 柱形图
    * @Param companyId 企业id groupId群组id year年份
    * @Date 11:32 2020/6/29
    */
    List<Map<String,Object>> getHdangerYearStatistics(@Param("companyId") Long companyId,@Param("groupId") Long groupId,@Param("year") Integer year);
    /**
    * @Author: zyj
    * @Description:隐患类型统计柱形图
    * @Param companyId 企业id groupId群组id startTime 开始时间 YYYY-MM-DD endTime结束时间 YYYY-MM-DD
    * @Date 16:32 2020/6/29
    */
    List<Map<String,Object>> getHdangerTypeStatistics(@Param("companyId") Long companyId,@Param("groupId") Long groupId,@Param("startTime") String startTime,@Param("endTime")String endTime);

    HiddenRecord findById(Long id);

    void add(Hidden hidden);

    void update(HiddenRecord HiddenRecord);

    int deleteById(@Param("id") Long id);

    int deleteByIds(@Param("ids") List ids);
}
