package com.hngf.service.danger;

import com.hngf.entity.danger.HiddenRetify;
import java.util.Map;
import java.util.List;

/**
 * 隐患整改
 *
 * @author zhangfei
 * @email 
 * @date 2020-06-11
 */
public interface HiddenRetifyService {

    /**
    * 根据ID查询
    */
    HiddenRetify getById(Long id);

    /**
     * 根据隐患ID查询
     * @param hiddenId
     * @return
     */
    List<HiddenRetify> getByHiddenId(Long hiddenId);

    /**
     * 立即进行整改
     * @param hiddenRetify
     * @throws Exception
     */
    void rightAwayRetify(HiddenRetify hiddenRetify, Integer userType) throws Exception;

    /**
    * 删除
    */
    void removeById(Long id);

    /**
     * 委托整改
     * @param params
     */
    void entrustHiddenRetify(Map<String, Object> params) throws Exception;

    /**
     * 保存整改
     * @param hiddenRetify
     */
    void saveRetify(HiddenRetify hiddenRetify) throws Exception;
}

