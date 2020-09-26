package com.hngf.common.utils;

import org.apache.commons.lang.StringUtils;

import java.util.Map;

/**
 * map参数工具类
 * @author zhangfei
 * @date 2020-07-14
 */
public class ParamUtils {

    /**
     * 获取Map参数中指定key，并转换为string类型
     * @param params
     * @param key
     * @return
     */
    public static String paramsToString(Map<String,Object> params, String key) {
        if (null == params || StringUtils.isBlank(key)) {
            return "";
        }
        if (null == params.get(key)) {
            return "";
        }
        try {
            return params.get(key).toString();
        } catch (Exception e) {
            return "" ;
        }
    }

    /**
     * 获取Map参数中指定key，并转换为Long类型
     * @param params
     * @param key
     * @return
     */
    public static Long paramsToLong(Map<String,Object> params, String key) {
        if (null == params || StringUtils.isBlank(key)) {
            return null;
        }
        if (null == params.get(key)) {
            return null;
        }
        try {
            return Long.parseLong(params.get(key).toString());
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 获取Map参数中指定key，并转换为Integer类型
     * @param params
     * @param key
     * @return
     */
    public static Integer paramsToInteger(Map<String,Object> params, String key) {
        if (null == params || StringUtils.isBlank(key)) {
            return null;
        }
        if (null == params.get(key)) {
            return null;
        }
        try {
            return Integer.parseInt(params.get(key).toString());
        } catch (Exception e) {
            return null;
        }
    }
}
