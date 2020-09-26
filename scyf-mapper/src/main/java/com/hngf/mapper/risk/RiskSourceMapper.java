package com.hngf.mapper.risk;

import com.hngf.dto.danger.RiskSourceDto;
import com.hngf.dto.risk.RiskInspectItemDto;
import com.hngf.entity.risk.Risk;
import com.hngf.entity.risk.RiskSource;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Map;
import java.util.List;

/**
 * 危险源
 * 
 * @author hngf
 * @email 
 * @date 2020-05-20 17:36:23
 */
@Mapper
public interface RiskSourceMapper {
    /**
     * 根据企业id 危险源类型 危险源名称 危险状态 节点层级 获取危险源对象
     * @param cId
     * @param riskDangerType
     * @param dangerName
     * @param nodeLevel
     * @return
     */
    RiskSource getRiskSource(@Param("companyId") Long cId, @Param("riskDangerType") Integer riskDangerType,@Param("dangerName")String dangerName,@Param("nodeLevel")Integer nodeLevel);

    List<RiskSourceDto> findList(Map<String, Object> params);

    /**
     * 查询分级管控检查项
     * @param params
     * @return
     */
    List<RiskInspectItemDto> findControlInspectItemList(Map<String, Object> params);
    /**
     * 查询非分级管控检查项
     * @param params
     * @return
     */
    List<RiskInspectItemDto> findUnControlInspectItemList(Map<String, Object> params);

    RiskSource findById(Long id);
    /**
    * @Author: zyj
    * @Description:作业活动比较图统计图柱形图
    * @Param riskDangerType危险源类型（1 设备设施 2 作业活动） companyId企业id
    * @Date 16:32 2020/6/19
    */
    List<Map<String,Object>> queryRiskLevel(@Param("riskDangerType") Integer riskDangerType,@Param("companyId") Long companyId);
    /**
    * @Author: lxf
    * @Description:作业活动比较图统计图柱形图
    * @Param riskDangerType危险源类型（1 设备设施 2 作业活动） companyId企业id
    * @Date 14:32 2020/9/18
    */
    List<Map<String,Object>> queryRiskLevelAnalyze(@Param("riskDangerType") Integer riskDangerType,@Param("companyId") Long companyId);

    void add(RiskSource RiskSource);

    void update(RiskSource RiskSource);

    int deleteById(@Param("id") Long id);

    int deleteByIds(@Param("ids") List ids);

    List<RiskSource> list(@Param("parentDangerSrcId")Long parentDangerSrcId, @Param("nodeLevel")Long nodeLevel);

    List<RiskSource> sel(@Param("parentDangerSrcId")Long parentDangerSrcId, @Param("nodeLevel")Long nodeLevel);

    void remove(@Param("riskDangerId")Long riskDangerId);

    List<Risk> selectRisks(@Param("cId")Long cId, @Param("riskCode")String riskCode,@Param("riskId")Long riskId);


    List<RiskSource> count(@Param("parentRiskDangerId")Long parentRiskDangerId);

    Integer selLV(@Param("parentRiskDangerId")Long parentRiskDangerId);
    Integer selLV1(@Param("parentRiskDangerId")Long parentRiskDangerId,@Param("riskDangerId")Long riskId);

    Integer selLV2(@Param("rootNode")Long rootNode);
    Integer selLV3(@Param("rootNode")Long rootNode,@Param("riskDangerId")Long riskId);

    List<RiskSource> sourceList(@Param("parentRiskDangerId")Long parentRiskDangerId, @Param("companyId")Long companyId);
    List<RiskSource> sourceLists(@Param("parentRiskDangerId")Long parentRiskDangerId, @Param("companyId")Long companyId,@Param("riskDangerId")Long riskDangerId);
    List<RiskSource> findByMap(Map<String, Object> params);

    /**
     * 批量新增危险源
     * @param eds
     */
    void saveBatch(@Param("list")List<RiskSource> eds);

    List<RiskSource> getDangerSrcId(@Param("companyId")Long companyId, @Param("riskDangerId")Long riskDangerId);

    List<RiskSource> selectDangerSrcId(@Param("companyId")Long companyId, @Param("riskDangerId")Long riskDangerId);

    void removeRiskSource(@Param("riskDangerId")Integer riskDangerId, @Param("companyId")Long companyId);

    /**
     * 查询检查项数量
     * @param map
     * @return
     */
    int findControlInspectItemCount(Map<String, Object> map);
}
