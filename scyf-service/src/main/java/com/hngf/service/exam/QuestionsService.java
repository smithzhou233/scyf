package com.hngf.service.exam;

import com.hngf.common.utils.PageUtils;
import com.hngf.entity.exam.Questions;
import java.util.Map;
import java.util.List;

/**
 * 试题表
 *
 * @author hngf
 * @email 
 * @date 2020-08-12 15:25:51
 */
public interface QuestionsService {

    PageUtils queryPage(Map<String, Object> params,Integer pageNum,Integer pageSize,String order);

    PageUtils queryPageToPaper(Map<String, Object> params,Integer pageNum,Integer pageSize);

    /**
    * 根据ID查询
    */
    Questions getById(Long id);

    /**
    * 保存
    */
    void save(Questions questions);

    /**
    * 更新
    */
    void update(Questions questions);

    /**
    * 批量删除
    */
    void removeByIds(List ids, Long updatedBy);

    /**
    * 删除
    */
    void removeById(Long id, Long updatedBy);

    void enableOrUnable(List<Long> questionsIdList ,Integer questionsStatus , Long updatedBy) ;
}

