package com.hngf.api.controller;

import com.hngf.common.exception.ScyfException;
import com.hngf.common.utils.Constant;
import com.hngf.entity.sys.User;
import com.hngf.service.sys.UserService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public abstract class BaseController {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private UserService userService;
    @Autowired
    protected HttpServletRequest request;

    protected User getUser() {
        String loginName = (String)request.getAttribute(Constant.USER_LOGIN_NAME);
        if (null == loginName) {
            throw new ScyfException("当前用户获取失败");
        }
        return userService.getByLoginName(loginName);
    }

    protected Long getUserId() {
        return getUser().getUserId();
    }

    protected String getLoginName() {
        return getUser().getLoginName();
    }

    protected Long getCompanyId() {
        return getUser().getCompanyId();
    }

    /**
     * 公司对应的根群组ID
     * @return
     */
    protected Long getCompanyGroupId() {
        return getUser().getCompanyGroupId();
    }

    /**
     * 顶层集团公司id
     * @return
     */
    protected Long getCompanyRootId() {
        return getUser().getCompanyRootId();
    }

    /**
     * 用户所在部门ID
     */
    protected Long getGroupId() {
        return getUser().getGroupId();
    }

    /**
     * 用户岗位ID
     */
    protected Long getPositionId() {
        return getUser().getPositionId();
    }

    /**
     * 根据名称从request获取参数
     * @param name
     * @return
     */
    public String getPara(String name) {
        return this.request.getParameter(name);
    }

    /**
     * 根据名称从request获取参数，带默认值
     * @param name
     * @param defaultValue
     * @return
     */
    public String getPara(String name, String defaultValue) {
        String result = this.request.getParameter(name);
        return result != null && !"".equals(result) ? result : defaultValue;
    }

    /**
     * 获取request参数：long类型
     * @param name
     * @return
     */
    public Long getParaToLong(String name) {
        return this.toLong(this.request.getParameter(name), null);
    }

    /**
     * 获取request参数：int类型
     * @param name
     * @return
     */
    public Integer getParaToInt(String name) {
        return this.toInt(this.request.getParameter(name), (Integer)null);
    }

    private Long toLong(String value, Long defaultValue) {
        try {
            if (StringUtils.isBlank(value)) {
                return defaultValue;
            } else {
                value = value.trim();
                return !value.startsWith("N") && !value.startsWith("n") ? Long.parseLong(value) : -Long.parseLong(value.substring(1));
            }
        } catch (Exception e) {
            logger.error("toLong", e);
            throw new ScyfException(e.getMessage());
        }
    }

    private Integer toInt(String value, Integer defaultValue) {
        try {
            if (StringUtils.isBlank(value)) {
                return defaultValue;
            } else {
                value = value.trim();
                return !value.startsWith("N") && !value.startsWith("n") ? Integer.parseInt(value) : -Integer.parseInt(value.substring(1));
            }
        } catch (Exception e) {
            logger.error("toInt", e);
            throw new ScyfException(e.getMessage());
        }
    }

    /**
     * 获取Map参数中指定key，并转换为string类型
     * @param params
     * @param key
     * @return
     */
    protected String paramsToString(Map<String,Object> params,String key) {
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
}
