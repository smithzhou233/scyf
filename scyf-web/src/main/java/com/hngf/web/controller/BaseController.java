package com.hngf.web.controller;

import com.hngf.common.enums.AccountType;
import com.hngf.common.exception.ScyfException;
import com.hngf.entity.sys.User;
import com.hngf.service.sys.DictService;
import com.hngf.web.common.shiro.ShiroUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

/**
 * 公共Controller
 * @author dell
 * @since 2020/5/25 9:23
 */
public abstract class BaseController {
@Autowired
private DictService dictService;

    protected Long getId(String tabName){
       return dictService.getTabId(tabName);
    }
    protected Logger logger = LoggerFactory.getLogger(getClass());

    protected User getUser() {
        return (User) ShiroUtils.getSubject().getPrincipal();
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

    protected AccountType getCurrentUserAccountType() {
        Integer iCurrentUserAccountType = this.getUser().getUserType();
        return AccountType.values()[iCurrentUserAccountType];
    }

    /**
     * 判断Map是否存在指定的key，不存在抛出异常
     * @param map
     * @param key
     * @return
     */
    public String hasKey(Map<String, Object> map, String... key) {
        String msg = "";
        if (null == map || map.isEmpty()) {
            msg = "缺少参数";
            throw new ScyfException(msg);
        } else {
            if (key != null && key.length > 0) {
                for(int i = 0; i < key.length; ++i) {
                    if (!map.containsKey(key[i])) {
                        msg = msg + "缺少参数[" + key[i] + "]";
                    }
                }
            }
            if (!msg.equals("")) {
                throw new ScyfException(msg);
            } else {
                return msg;
            }
        }
    }
}
