package com.hngf.service.risk;

import com.hngf.common.utils.PageUtils;
import com.hngf.entity.danger.Hidden;
import com.hngf.entity.risk.RiskPointControlRecord;
import org.apache.ibatis.annotations.Param;

import java.util.Map;
import java.util.List;

/**
 * 风险点管控实时告警记录表
 *
 * @author hngf
 * @email 
 * @date 2020-05-20 17:36:23
 */
public interface RiskPointControlRecordService {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * 查询风险点实时预警记录
     * @param params
     * @return
     */
    PageUtils getControlRecord(Map<String, Object> params,Integer pageNum, Integer pageSize, String order);

    /**
     * 查询风险点实时预警记录
     * @param params
     * @return
     */
    PageUtils getHistoryControlRecord(Map<String, Object> params,Integer pageNum, Integer pageSize, String order);
    /**
     * @Author: zyj
     * @Description:风险，风险点预警记录统计折线图
     * @Param year 年份 companyId企业id
     * @Date 15:50 2020/6/19
     */
    List<Map<String,Object>> getStatistics(Long companyId,Integer year);
    /**
     * @Author: zyj
     * @Description:风险，风险点预警记录统计折线图 年份下拉框
     * @Param companyId企业id
     * @Date 15:28 2020/6/23
     */
    List<Map<String,Object>> getStatisticsYear(Long companyId);
    /**
    * 根据ID查询
    */
    RiskPointControlRecord getById(Long id);

    /**
     * 新增一条隐患记录
     * @param hidden
     * @return
     * @throws Exception
     */
    int insertRiskPointControlRecord(Hidden hidden) throws Exception;

    /**
    * 保存
    */
    void save(RiskPointControlRecord RiskPointControlRecord);

    /**
    * 更新
    */
    void update(RiskPointControlRecord RiskPointControlRecord);

    /**
    * 批量删除
    */
    void removeByIds(List ids);

    /**
    * 删除
    */
    void removeById(Long id);
}

