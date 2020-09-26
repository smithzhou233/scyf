package com.hngf.mapper.danger;

import com.hngf.entity.danger.InspectDef;
import com.hngf.entity.danger.InspectSchduleDef;
import com.hngf.entity.danger.InspectSchduleStaff;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Map;
import java.util.List;

/**
 * 检查任务定义
 * 
 * @author hngf
 * @email 
 * @date 2020-05-20 17:36:23
 */
@Mapper
public interface InspectSchduleDefMapper {

    List<InspectSchduleDef> findList(Map<String, Object> params);


    List<InspectSchduleDef>  findListBySchedule(Map<String, Object> params);

    InspectSchduleDef findById(Long id);

    void add(InspectSchduleDef InspectSchduleDef);

    void update(InspectSchduleDef InspectSchduleDef);

    int deleteById(@Param("id") Long id);

    int deleteByIds(@Param("ids") List ids);

    void addInspectSchduleStaff(@Param("inspectSchduleStaffs") List<InspectSchduleStaff> inspectSchduleStaffs);

    List<InspectDef>    selectInspectDefListByType(Map<String, Object> params);

    void deleteStaffByschduleDefId(@Param("schduleDefId") Long schduleDefId);

    List<InspectSchduleStaff>  selectInspectSchduleStaffList(@Param("schduleDefId") Long schduleDefId);


}
