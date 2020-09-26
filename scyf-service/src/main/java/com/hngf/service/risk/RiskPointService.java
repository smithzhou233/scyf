package com.hngf.service.risk;

import com.hngf.common.utils.PageUtils;
import com.hngf.entity.risk.RiskPoint;
import com.hngf.entity.risk.RiskSource;

import com.hngf.entity.sys.User;

import java.util.Map;
import java.util.List;

/**
 * 风险点索引表
 *
 * @author hngf
 * @email 
 * @date 2020-05-20 17:36:23
 */
public interface RiskPointService {

    PageUtils queryPage(Map<String, Object> params,Integer pageNum,Integer pageSize,String order);

    /**
     * 查询风险点列表，返回RiskPoint对象集合，二维码管理使用(返回少量字段信息)
     * @param params
     * @param pageNum
     * @param pageSize
     * @param order
     * @return
     */
    PageUtils getRiskPointList(Map<String, Object> params,Integer pageNum,Integer pageSize,String order);

    /**
     * 监管级-重大隐患
     * @param params
     * @param pageNum
     * @param pageSize
     * @param order
     * @return
     */
    PageUtils getRiskPointListByCompanyId(Map<String, Object> params,Integer pageNum,Integer pageSize,String order);

    List<RiskPoint> getRiskPointList(Map<String, Object> params);
    /**
     * @Author: zyj
     * @Description:风险，风险点统计柱形图
     * @Param companyId 企业id
     * @Date 15:16 2020/6/19
     */
    String riskPointLvCount(Long companyId, Integer riskPointLevel);

    /**
     * 风险点统计
     * @param params
     * @return
     */
    List<Map<String, Object>> riskPointLvCountBigScreen(Map<String, Object> params);

    /**
    * 根据ID查询
    */
    RiskPoint getById(Long id);
    RiskPoint selectByQrCode(String id);
    /**
    * 根据ID查询风险点信息-不关联其他表
    */
    RiskPoint getRiskPointById(Long id);

    /**
    * 查询结果总数
    */
    int count(Map<String, Object> params);

    /**
    * 保存
    */
    void save(RiskPoint RiskPoint);

    /**
    * 更新
    */
    void update(RiskPoint RiskPoint);

    /**
    * 批量删除
    */
    void removeByIds(List ids);

    /**
    * 删除
    */
    void removeById(Long id,Long companyId);

    /**
     * 批量激活/取消激活
     * @param isActive
     * @param riskPointIdList
     */
    int updateIsActive(Integer isActive, Long[] riskPointIdList);

    /**
     * 检查风险点是否受控
     * @param companyId
     * @param riskPointId
     */
    void checkedRiskPointIsControl(Long companyId, Long riskPointId);

    /**
     * 更新风险点的隐患数量
     * @param riskPointId
     * @param type
     * @throws Exception
     */
    void updateRiskPointHiddenCount(Long riskPointId, Integer type) throws Exception;

    /**
     * App查询风险点列表
     * @param params
     * @param pageNum
     * @param pageSize
     * @param order
     * @return
     */
    PageUtils queryApiPage(Map<String, Object> params, int pageNum, int pageSize, String order);


    /**
     * 查询任务下的风险点列表
     * @param params
     * @param pageNum
     * @param pageSize
     * @param order
     * @return
     */
    PageUtils getScheduleRiskPointList(Map<String, Object> params, int pageNum, int pageSize, String order);

    /**
     * 查询风险点下的危险源
     * @param params
     * @return
     */
    List<RiskSource> getRiskPointDangerSrc(Map<String, Object> params);

    /**
     * 根据部门ID查询责任人
     * @param params
     * @return
     */
    List<User> queryUser(Map<String, Object> params);

    /**
     *风险分级责任台账
     * yfh
     * 2020/06/19
     * @param map
     * @return
     */
    List<Map<String, Object>> findAllListByType(Map<String, Object> map);

    List<Map<String, Object>> causeReasons(String type);

    /**
     * app查询 风险点数量
     * yfh
     * 2020/06/23
     * @param params
     * @return
     */
    Map<String, Object> queryRiskPointNum(Map<String, Object> params);

    /**
     *  风险分布图 风险点数量总计
     *  yfh
     *  2020/06/30
     * @param groupId
     * @param companyId
     * @return
     */
    String riskPointCount(Integer groupId, Long companyId);

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
    List<Map<String, Object>> getAllRiskPoint(Long companyId,Long groupId);

    /**
     * 查询风险点详情 - 大屏
     * @param riskPointId
     * @return
     */
    RiskPoint riskPointDetailsAll(Long riskPointId);

    /**
     * 监管端风险点列表查询
     * @param params
     * @param pageNum
     * @param pageSize
     * @param order
     * @return
     */
    PageUtils getRiskPointPageForGent(Map<String, Object> params, int pageNum, int pageSize, String order);

    /**
     * 风险点子部门总数 - 风险点总数鼠标悬浮使用
     * @param params
     * @return
     */
    List<Map<String, Object>> getRiskPointChuldrenCount(Map<String, Object> params);
}

