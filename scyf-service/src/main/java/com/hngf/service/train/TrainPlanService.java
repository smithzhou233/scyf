package com.hngf.service.train;

import com.hngf.common.utils.PageUtils;
import com.hngf.dto.sys.GroupDetailDto;
import com.hngf.entity.train.TrainPlan;

import java.util.List;
import java.util.Map;

/**
 * 
 *
 * @author hngf
 * @email 
 * @date 2020-05-26 08:32:53
 */
public interface TrainPlanService {

    PageUtils queryPage(Map<String, Object> params,Integer pageNum,Integer pageSize,String order);
    PageUtils queryPagePc(Map<String, Object> params,Integer pageNum,Integer pageSize,String order);
    /**
    * 根据ID查询
    */
    List<Map<String,Object>> getById(Integer id);

    /**
    * 保存
    */
    void save(TrainPlan TrainPlan);

    /**
    * 更新
    */
    void update(TrainPlan TrainPlan);

    /**
    * 批量删除
    */
    void removeByIds(List ids);

    /**
    * 删除
    */
    void removeById(Long id, Long updatedBy);

    /**
     * 根据计划id查看 培训内容表 是否有该计划的内容
     * yfh
     * 2020/05/26
     * @param trainPlanId
     * @return
     */
    List isDel(Long trainPlanId);

    /**
     * 查看培训计划类型
     * yfh
     * 2020/05/26
     * @param  trainType
     * @return
     */
    List<Map<String, Object>> selectTrainType(String trainType);
    /**
     * 修改培训计划 是否提前一天预警
     * yfh
     * 2020/07/09
     * @param trainPlanId
     * @param warnFlag
     * @return
     */
    void updateWarnFlag(Long trainPlanId, Integer warnFlag, Long updatedBy);

    /**
     * 获取 ElementUI树形结构数据
     * @param params
     * @return
     */
    List<GroupDetailDto> getEleuiTreeList(Map<String, Object> params);
}

