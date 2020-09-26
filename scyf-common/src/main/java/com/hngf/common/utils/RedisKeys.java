package com.hngf.common.utils;

/**
 * Redis所有Key
 */
public class RedisKeys {

    /**
     * 用户KEY
     * @param loginName
     * @return
     */
    public static String getUserKey(String loginName){
        return "sys:user:" + loginName;
    }

    /**
     * 用户token KEY
     * @param userId
     * @return
     */
    public static String getUserTokenKey(Long userId){
        return "sys:user:apitoken:" + userId;
    }

    /**
     * 风险点 key
     * checkType: 0 未检查 1 已检查
     *
     */
    public static String getRiskPointKey(String userId,String checkType,String scheduleId){return "sys:riskPoint:"+userId+":"+checkType+":"+scheduleId;}

    /**
     * 查询风险点下的风险管控措施清单
     * @param userId
     * @param riskPointId
     * @return
     */
    public static String getriskMeasureItemList(String userId,String riskPointId,String scheduleId){return "sys:MeasureItem:"+userId+":"+riskPointId+":"+scheduleId;}

    /**
     * 查询检查表下的检查项
     * @param userId
     * @param
     * @param
     * @return
     */
    public static String getScheduleDefItemList(Long userId,String inspectDefId,String scheduleId){return "sys:ScheduleDefItem:"+userId+":"+inspectDefId+":"+scheduleId;}


}
