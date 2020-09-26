package com.hngf.service.danger;

import com.hngf.common.utils.PageUtils;
import com.hngf.entity.danger.InspectItemDef;
import com.hngf.entity.danger.InspectSchdule;

import java.util.List;
import java.util.Map;

/**
 * 检查任务
 *
 * @author hngf
 * @email 
 * @date 2020-05-20 17:36:24
 */
public interface InspectSchduleService {

    PageUtils queryPage(Map<String, Object> params, int pageNum, int pageSize, String order);

    /**
     * 查询子任务列表
     * @param params
     * @param pageNum
     * @param pageSize
     * @param order
     * @return
     */
    PageUtils querySonList(Map<String, Object> params, int pageNum, int pageSize, String order);

    /**
     * 根据任务ID查询详情
     * @param scheduleId
     * @return
     */
    InspectSchdule getDetailById(Long scheduleId);

    /**
     * 我的任务列表
     * @param params
     * @return
     */
    PageUtils getMyTask(Map<String, Object> params,int pageNum,int pageSize,String order);

    /**
     * 统计-我的任务数量
     * @return
     */

    Integer getMyTaskCoutForAPI(Map<String, Object> params);

    //查询当天逾期任务
    List<InspectSchdule>  queryInspectSchduleList();
    //查询所有逾期任务
    List<InspectSchdule>  queryAllOverdueMainTask();


    /**
     * 基础任务检查 - 查询检查项
     * @param params
     * @return
     */
    List<InspectItemDef> getSchduleInspectItems(Map<String, Object> params);

    /**
    * 根据ID查询
    */
    InspectSchdule getById(Long id);

    /**
     * 根据任务ID查询我的任务详情
     * @param scheduleId
     * @return
     */
    InspectSchdule getMyDetailById(Long scheduleId);

    /**
     * 保存
     */
    void saveList(List<InspectSchdule> inspectSchduleList);

    /**
    * 更新
    */
    void update(InspectSchdule InspectSchdule);

    /**
    * 删除
    */
    int removeById(Long id);

    /**
     * 今日检查（企业大屏）
     * @param params
     * @return
     */
    List<Map<String, Object>> todayCheck(Map<String, Object> params);

    Boolean checkedDeviceOn(Long companyId,Integer userType);
    /**
     * 根据任务ID查询任务执行人列表
     * @param params
     * @return
     */
    public List<InspectSchdule> executorScheduleList(Map<String, Object> params);
}

