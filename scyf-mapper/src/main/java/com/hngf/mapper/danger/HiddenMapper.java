package com.hngf.mapper.danger;

import com.hngf.entity.danger.Hidden;
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
public interface HiddenMapper {

    List<Hidden> findList(Map<String, Object> params);

    /**
     * 根据隐患ID查询隐患 - 不关联其他表
     * @param id
     * @return
     */
    Hidden findHiddenById(Long id);

    /**
     * 查询企业下的所有隐患数据
     * @param companyId
     * @return
     */
    List<Hidden> findByCompanyId(Long companyId);

    Hidden findById(Long id);

    void add(Hidden Hidden);

    void update(Hidden Hidden);

    int deleteById(@Param("id") Long id);

    int deleteByIds(@Param("ids") List ids);

    /**
     * 隐患未处理
     * @param params
     * @return
     */
    List<Hidden> findGroupHidden(Map<String, Object> params);

    /**
     * 隐患已处理
     * @param params
     * @return
     */
    List<Hidden> queryAlreadyHDangerRecordByPage(Map<String, Object> params);

    List<Map<String ,Object>>    hiddenLvlCount(Map<String, Object> params);

    Integer findHomePageHiddenOverdueCount(Map<String, Object> params);

    List<Map<String, Object>> queryRectifyStandingBookByPage(Map<String, Object> dataMap);

    List<Map<String, Object>> queryHiddenCheckedListByPage(Map<String, Object> dataMap);

    /**
     * 隐患统计（企业大屏）
     * @param params
     * @return
     */
    List<Map<String, Object>> hiddenCount(Map<String, Object> params);

    /**
     * 隐患列表（企业大屏）
     * @param params
     * @return
     */
    List<Map<String, Object>> findHiddenListForBigScreen(Map<String, Object> params);

    /**
     * 隐患通报（企业大屏）
     * @param params
     * @return
     */
    List<Map<String, Object>> hiddenBulletin(Map<String, Object> params);

    /**
     * 查询集团、企业下的风险点隐患
     * @param params
     * @return
     */
    List<Hidden> findRiskPointHiddenListForGent(Map<String, Object> params);

    /**
     *  首页隐患通报列表
     * @param params
     * @return
     */
    List<Map<String, Object>> findHiddenGroup(Map<String, Object> params);

    /**
     * 首页隐患总计
     * @param params
     * @return
     */
    List<Map<String, String>> findHiddenCount(Map<String, Object> params);

    /**
     * 查询统计集团下素有企业重大、较大隐患数   -监管层大屏使用
     * @param companyIds
     * @return
     */
    Map<String, Object> findHiddenDangerCountForGent(String companyIds);

    /**
     * 查询统计集团下所有企业待整改、待验收、已完成隐患数及完成率   -监管层大屏使用
     * @param companyIds
     * @return
     */
    Map<String, Object> findHiddenDangerCountAndRateForGent(String companyIds);

    /**
     * 查询隐患通报  -监管层大屏使用
     * @param companyIds
     * @return
     */
    List<Map<String, Object>> findHiddenListForGent(String companyIds);

    /**
     * 查询下级单位安全生产分值 -监管层大屏使用
     * @param params
     * @return
     */
    List<Map<String, Object>> findHiddenPointForGent(Map<String, Object> params);

    List<Hidden>  selectHiddenListByCompanyId(Map<String, Object> params);
}
