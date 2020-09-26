package com.hngf.service.danger.impl;

import com.hngf.mapper.danger.HiddenMapper;
import com.hngf.service.danger.HiddenStatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
/**
 * 隐患管理
 * @author yanshanshan
 * @date 2020-06-16
 */
@Service("HiddenStatisticsService")
public class HiddenStatisticsServiceImpl implements HiddenStatisticsService {
    @Autowired
    private  HiddenMapper HiddenMapper;
    @Override
    public List<Map<String, Object>> hiddenLvlCount( Map<String, Object> params) {
        return HiddenMapper.hiddenLvlCount(params);
    }
}
