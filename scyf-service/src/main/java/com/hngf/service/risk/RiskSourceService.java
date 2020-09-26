package com.hngf.service.risk;

import com.alibaba.fastjson.JSONObject;
import com.hngf.common.utils.R;
import com.hngf.dto.danger.RiskSourceDto;
import com.hngf.dto.risk.RiskInspectItemDto;
import com.hngf.entity.risk.RiskSource;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.util.List;
import java.util.Map;

/**
 * 危险源
 *
 * @author hngf
 * @email 
 * @date 2020-05-20 17:36:23
 */
public interface RiskSourceService {


    /**
    * 根据ID查询
    */
    RiskSource getById(Long id);

    /**
    * 保存
    */
    void save(RiskSource RiskSource);

    /**
    * 更新
    */
    void update(RiskSource RiskSource);

    /**
    * 批量删除
    */
    void removeByIds(List ids);

    /**
    * 删除
    */
    R removeById(Long riskDangerId);

    R addEntRisk(JSONObject paramsJson, Long userId,Long id);

    List<RiskSource> queryByMap(Map<String, Object> params);

    void importExcelCheck(String path, JSONObject paramsJsonPage) throws Exception;
    void importLecExcel(String path, JSONObject paramsJsonPage) throws Exception;
    void importLcExcel(String path, JSONObject paramsJsonPage) throws Exception;
    void importLsExcel(String path, JSONObject paramsJsonPage) throws Exception;
    void importRiverLecExcel(String path, JSONObject paramsJsonPage) throws Exception;
    void importRiverLsExcel(String path, JSONObject paramsJsonPage) throws Exception;
    String importExcel(String path, JSONObject paramsJsonPage,Long id) throws Exception;
    String importNewExcel(String path, JSONObject paramsJsonPage,Long id,String type) throws Exception;
    String importRiverNewExcel(String path, JSONObject paramsJsonPage,Long id,String type) throws Exception;
    String importNewAExcel(String path, JSONObject paramsJsonPage,Long id) throws Exception;
    XSSFWorkbook exportExcel(Integer riskDangerType, Long cId);
    XSSFWorkbook exportLExcel(Integer riskDangerType, Long cId);
    XSSFWorkbook exportAExcel(Integer riskDangerType, Long cId, Integer evaluateType);
    XSSFWorkbook exportRiverExcel(Integer riskDangerType, Long cId);
    boolean updateLvAndLeaf(Integer riskId, Long companyId, boolean b);

    List<Map<String,Object>> getRiskDangerLevel();

    List<RiskSourceDto> findList(Map<String, Object> params);

    /**
     * 查询分级管控检查项
     * @param params
     * @return
     */
    List<RiskInspectItemDto> getControlInspectItemList(Map<String, Object> params);

    /**
     * 查询检查项的数量
     * @param map
     * @return
     */
    int getControlInspectItemCount(Map<String, Object> map);
    /**
     * @Author: zyj
     * @Description:作业活动比较图统计图柱形图
     * @Param riskDangerType危险源类型（1 设备设施 2 作业活动） companyId企业id
     * @Date 16:32 2020/6/19
     */
    List<Map<String,Object>> queryRiskLevel(Integer riskDangerType, Long companyId);

    /**
     * @Author: lxf
     * @Description:作业活动比较图统计图柱形图
     * @Param riskDangerType危险源类型（1 设备设施 2 作业活动） companyId企业id
     * @Date 14:32 2020/9/18
     */
    List<Map<String,Object>> queryRiskLevelAnalyze(Integer riskDangerType, Long companyId);

}

