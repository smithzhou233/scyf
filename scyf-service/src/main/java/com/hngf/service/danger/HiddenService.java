package com.hngf.service.danger;

import com.hngf.common.utils.PageUtils;
import com.hngf.entity.danger.Hidden;

import java.util.Map;
import java.util.List;

/**
 * 隐患表
 *
 * @author zhangfei
 * @email 
 * @date 2020-05-20 17:36:24
 */
public interface HiddenService {

    PageUtils queryPage(Map<String, Object> params,Integer pageNum,Integer pageSize,String order);
    PageUtils queryMyTodoPage(Map<String, Object> params,Integer pageNum,Integer pageSize,String order);

    PageUtils queryRiskPointHDanger(Map<String, Object> params,Integer pageNum,Integer pageSize,String order);

    /**
     * 查询未处理隐患
     * @param params
     * @return
     */
    List<Hidden> getGroupHidden(Map<String, Object> params);

    /**
     * 根据 companyId 查询
     * @param companyId
     * @return
     */
    List<Hidden> getByCompanyId(Long companyId);

    /**
    * 根据ID查询
    */
    Hidden getById(Long id);

    /**
     * 根据隐患ID查询隐患状态 - 不关联其他表
     * @param hiddenId
     * @return
     */
    Hidden getHiddenById(Long hiddenId);

    /**
    * 保存
    */
    void save(Hidden Hidden, Integer userType) throws Exception;

    /**
    * 更新
    */
    void update(Hidden Hidden, Integer userType) throws Exception;

    /**
    * 删除
     *  @param id 隐患ID
     *  @param userId 用户ID
    */
    int removeById(Long id,Long userId) throws Exception;

    /**
    * 永久删除
     *  @param id 隐患ID
    */
    int permanentRemoveById(Long id);

    /**
     * 删除所有隐患数据
     * @param companyId
     * @return
     */
    int deleteAll(Long companyId,Long userId);

    /**
    * 撤销隐患
     * @param id 隐患ID
     * @param userId 用户ID
    */
    int cancel(Long id,Long userId) throws Exception;

    /**
     * 检查隐患是否可以评审
     * @param companyId
     * @return
     */
    Boolean checkedHiddenReview(Long companyId,Integer userType);


    String saveFeedbackHiddenDanger(Hidden hidden, Integer userType) throws Exception;

    /**
     * api首页 统计逾期数量
     */
    Integer findHomePageHiddenOverdueCount(Map<String , Object> params);

    /**
     * 查询隐患台账清单整改台账
     * yfh
     * 2020/06/28
     * @param dataMap
     * @param pageNum
     * @param pageSize
     * @param order
     * @return
     */
    PageUtils queryRectifyStandingBookByPage(Map<String, Object> dataMap, int pageNum, int pageSize,String order);
    /**
     * 查询隐患台账清单排查清单
     * yfh
     * 2020/06/29
     * @param dataMap
     * @return
     */
    List<Map<String, Object>> queryHiddenCheckedListByPage(Map<String, Object> dataMap);

    /**
     * 隐患数量统计
     * @param params
     * @return
     */
    List<Map<String, Object>> hiddenCount(Map<String, Object> params);

    /**
     * 隐患通报（企业大屏）
     * @param params
     * @return
     */
    List<Map<String, Object>> hiddenBulletin(Map<String, Object> params);

    /**
     * 隐患列表（企业大屏）
     * @param params
     * @return
     */
    List<Map<String, Object>> getHiddenListForBigScreen(Map<String, Object> params);

    /**
     * 查询集团、企业下的风险点隐患
     * @param params
     * @param pageNum
     * @param pageSize
     * @param order
     * @return
     */
    PageUtils queryRiskPointHiddenListForGent(Map<String, Object> params, int pageNum, int pageSize, String order);
    PageUtils queryHiddenListByCompanyId(Map<String, Object> params, int pageNum, int pageSize, String order);



    /**
     * 首页隐患通报
     * @param params
     * @param pageNum
     * @param pageSize
     * @param order
     * @return
     */
    PageUtils indexHiddenNotice(Map<String, Object> params, int pageNum, int pageSize, String order);

    /**
     * 首页隐患总计
     * @param params
     * @return
     */
    List<Map<String, String>> indexHiddenCount(Map<String, Object> params);

    /**
     * 督导
     */
    void toSupervise(Map<String, Object> params);

    /**
     *
     */
}

