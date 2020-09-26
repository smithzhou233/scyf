package com.hngf.service.bigscreen;

import java.util.Map;

/**
 * 大屏信息
 */
public interface BigScreenMessageService {

    /**
     * 查询大屏信息
     * @param params
     * @return
     */
    String bigScreenMessage(Map<String, Object> params);

    String bigScreenMessage(String params);

    Map<String,Object> allIndustryStatistics(Map<String, Object> params);
}
