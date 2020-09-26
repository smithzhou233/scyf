package com.hngf.mapper.danger;

import com.hngf.entity.danger.InspectSchdule;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Map;
import java.util.List;

/**
 * 检查任务
 * 
 * @author hngf
 * @email 
 * @date 2020-05-20 17:36:24
 */
@Mapper
public interface InspectSchduleMapper {

    List<InspectSchdule> findList(Map<String, Object> params);

    /**
     * 查询延期任务
     * @param params
     * @return
     */
    List<InspectSchdule> getTodayOverdueTask(Map<String, Object> params);
    List<InspectSchdule> getAllOverdueMainTask(Map<String, Object> params);

    /**
     * 查询子任务列表
     * @param params
     * @return
     */
    List<InspectSchdule> findSonList(Map<String, Object> params);

    /**
     * 查询任务列表
     * @param params
     * @return
     */
    List<InspectSchdule> findListByMap(Map<String, Object> params);

    /**
     * 我的任务/历史任务
     * @param params
     * @return
     */
    List<InspectSchdule> findMyTask(Map<String, Object> params);

    /**
     * 根据父级任务ID查询
     * @param inspectScheduleId
     * @return
     */
    List<InspectSchdule> getByParentScheduleId(Long inspectScheduleId);

    InspectSchdule findById(Long id);

    /**
     * 根据任务ID和类型查询
     * @param scheduleId
     * @param type
     * @return
     */
    List<InspectSchdule> findDetailByIdAndType(@Param("scheduleId")Long scheduleId,@Param("type") String type);

    void add(InspectSchdule InspectSchdule);

    void update(InspectSchdule InspectSchdule);

    int deleteById(@Param("id") Long id);

    int deleteByIds(@Param("ids") List ids);

    void  addList( @Param("inspectSchdules") List<InspectSchdule> inspectSchdules);

    Integer findMyTaskCountForAPI(Map<String, Object> params);

    /**
     * 删除当前任务及子任务
     * @param
     * @return
     */
    int deleteAllTaskById(Map<String,Object> map);

    /**
     * 今日检查（企业大屏）
     * @param params
     * @return
     */
    List<Map<String, Object>> todayCheck(Map<String, Object> params);

    /**
     * 根据任务ID查询任务执行人列表
     * @param params
     * @return
     */
    public List<InspectSchdule> executorScheduleList(Map<String, Object> params);

}
