package com.hngf.service.danger;

import com.hngf.entity.danger.HiddenReview;
import java.util.List;
import java.util.Map;

/**
 * 隐患评审
 *
 * @author zhangfei
 * @email 
 * @date 2020-06-15
 */
public interface HiddenReviewService {

    /**
    * 根据ID查询
    */
    HiddenReview getById(Long id);

    /**
    * 根据隐患ID查询
    */
    List<HiddenReview> getByHiddenId(Long hiddenId);

    /**
    * 保存
    */
    void save(HiddenReview hiddenReview, Integer userType) throws Exception;

    /**
    * 更新
    */
    void update(HiddenReview HiddenReview);

    /**
    * 批量删除
    */
    void removeByIds(List ids);

    /**
    * 删除
    */
    void removeById(Long id);

    /**
     * 委托评审
     * @param params
     */
    void entrustHiddenReview(Map<String, Object> params) throws Exception;
}

