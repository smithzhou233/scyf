package com.hngf.service.danger;

import com.hngf.common.utils.PageUtils;
import com.hngf.entity.danger.HiddenAccept;
import java.util.Map;
import java.util.List;

/**
 * 隐患验收
 *
 * @author zhangfei
 * @email 
 * @date 2020-06-15
 */
public interface HiddenAcceptService {

    /**
    * 根据ID查询
    */
    HiddenAccept getById(Long id);

    /**
     * 根据隐患ID查询
     * @param hiddenId
     * @return
     */
    List<HiddenAccept> getByHiddenId(Long hiddenId);

    /**
    * 删除
    */
    void removeById(Long id);

    /**
     * 委托验收
     * @param params
     */
    void entrustHiddenAccept(Map<String, Object> params) throws Exception;

    /**
     * 保存验收
     * @param hiddenAccept
     */
    void saveAccept(HiddenAccept hiddenAccept) throws Exception;
}

