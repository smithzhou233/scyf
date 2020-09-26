package com.hngf.service.exam;

import com.hngf.common.utils.PageUtils;
import com.hngf.entity.exam.UserPaperAnswer;
import java.util.Map;
import java.util.List;

/**
 * 用户试卷答案表
 *
 * @author lxf
 * @email 
 * @date 2020-08-15 11:57:10
 */
public interface UserPaperAnswerService {

    PageUtils queryPage(Map<String, Object> params,Integer pageNum,Integer pageSize,String order);

    /**
    * 根据ID查询
    */
    UserPaperAnswer getById(Long id);

    /**
    * 保存
    */
    void save(UserPaperAnswer userPaperAnswer);

    /**
    * 更新
    */
    void update(UserPaperAnswer userPaperAnswer);

    /**
    * 批量删除
    */
    void removeByIds(List ids,Long updatedBy);

    /**
    * 删除
    */
    void removeById(Long id,Long updatedBy);
}

