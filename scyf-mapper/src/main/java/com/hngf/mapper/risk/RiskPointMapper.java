package com.hngf.mapper.risk;

import com.hngf.entity.risk.RiskPoint;
import com.hngf.entity.risk.RiskSource;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Map;
import java.util.List;

/**
 * 风险点索引表
 * 
 * @author hngf
 * @email 
 * @date 2020-05-20 17:36:23
 */
@Mapper
public interface RiskPointMapper {

    List<Map<String,Object>> findList(Map<String, Object> params);

    RiskPoint findById(Long id);
    RiskPoint findByQrCode(String id);
    /**
    * @Author: zyj
    * @Description:风险，风险点统计柱形图
    * @Param companyId 企业id
    * @Date 15:16 2020/6/19
    */
    String riskPointLvCount(Long companyId, Integer riskPointLevel);
    /**
     * 单表查询
     * @param id
     * @return
     */
    RiskPoint findRiskPointById(Long id);

    /**
     * 查询风险点列表，返回RiskPoint对象集合，二维码管理使用(返回少量字段信息)
     * @param params
     * @return
     */
    List<RiskPoint> findRiskPointList(Map<String, Object> params);

    void add(RiskPoint RiskPoint);

    void update(RiskPoint RiskPoint);

    int deleteById(@Param("id") Long id);

    int deleteByIds(@Param("ids") List ids);

    int countByMap(Map<String, Object> params);

    int updateIsActive(Integer isActive, List<Long> riskPointIdList);

    void updateBatchById(List<RiskPoint> list);

    /**
     * 查询任务下的风险点列表
     * @param map
     * @return
     */
    List<Map<String, Object>> findSchduleRiskPoint(Map<String, Object> map);

    List<Map<String, Object>> queryRiskPointPage(Map<String, Object> map);

    /**
     * 查询风险点下的危险源
     * @param params
     * @return
     */
    List<RiskSource> findRiskPointDangerSrc(Map<String, Object> params);
    /**
     *风险分级责任台账
     * yfh
     * 2020/06/19
     * @param map
     * @return
     */
    List<Map<String, Object>> findAllListByType(Map<String, Object> map);

    /**
     *预警产生原因
     * yfh
     * 2020/06/23
     * @param type
     * @return
     */
    List<Map<String, Object>> causeReasons(@Param("type")String type);
    /**
     *  风险分布图 风险点数量总计
     *  yfh
     *  2020/06/30
     * @param groupId
     * @param companyId
     * @return
     */
    String riskPointCount(@Param("groupId")Integer groupId, @Param("companyId")Long companyId);

    /**
     * 风险点数量总计 - 大屏
     * @param params
     * @return
     */
    List<Map<String, Object>> riskPointLvCountBigScreen(Map<String, Object> params);

    /**
     * 预警风险点数量 - 大屏
     * @param params
     * @return
     */
    List<Map<String, Object>> warningRiskPoint(Map<String, Object> params);

    /**
     * 根据公司ID和群组ID查询风险点列表 - 大屏
     * @param companyId
     * @param groupId
     * @return
     */
    List<Map<String, Object>> findAllRiskPoint(@Param("companyId")Long companyId,@Param("groupId") Long groupId);

    /**
     * 查询集团下的企业的风险点列表
     * @param params
     * @return
     */
    List<Map<String, Object>> findRiskPointForGent(Map<String, Object> params);

    String inspectResultStr();

    /**
     * 风险点子部门总数 - 风险点总数鼠标悬浮使用
     * @param params
     * @return
     */
    List<Map<String, Object>> findRiskPointChuldrenCount(Map<String, Object> params);

    List<Map<String, Object>> getRiskPointListByCompanyId(Map<String, Object> params);
}
