package com.hngf.service.danger;

import java.util.List;
import java.util.Map;

public interface HiddenStatisticsService {
    /**
     * api根据隐患等级统计
     */
    List<Map<String ,Object>> hiddenLvlCount( Map<String, Object> params);
}
