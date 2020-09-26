package com.hngf.mapper.train;

import com.hngf.entity.train.TrainPlan;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Map;
import java.util.List;

/**
 * 
 * 
 * @author hngf
 * @email 
 * @date 2020-05-26 08:32:53
 */
@Mapper
public interface TrainPlanMapper {

    List<Map<String,Object>> findList(Map<String, Object> params);
    List<Map<String,Object>> findListPc(Map<String, Object> params);
    List<Map<String,Object>> findById(@Param("id")Integer id);

    void add(TrainPlan TrainPlan);

    void update(TrainPlan TrainPlan);

    int deleteById(@Param("id")Long id, @Param("updatedBy")Long updatedBy);

    int deleteByIds(@Param("ids") List ids);

    List<Map<String,Object>> isDel(@Param("trainPlanId")Long trainPlanId);

    List<Map<String, Object>> selectTrainType(@Param("trainType")String trainType);
    /**
     * 修改培训计划 是否提前一天预警
     * yfh
     * 2020/07/09
     * @param trainPlanId
     * @param warnFlag
     * @return
     */
    void updateWarnFlag(@Param("trainPlanId")Long trainPlanId, @Param("warnFlag")Integer warnFlag, @Param("updatedBy")Long updatedBy);

    String getType(@Param("trainType")String trainType, @Param("dictType")String dictType);
}
