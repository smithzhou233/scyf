package com.hngf.service.exam;

import com.hngf.common.utils.PageUtils;
import com.hngf.entity.exam.PaperQuestions;
import java.util.Map;
import java.util.List;

/**
 * 试卷题库关系表
 *
 * @author hngf
 * @email 
 * @date 2020-08-14 09:39:02
 */
public interface PaperQuestionsService {

    PageUtils queryPage(Map<String, Object> params,Integer pageNum,Integer pageSize,String order);

    /**
    * 根据ID查询
    */
    PaperQuestions getById(Long id);

    /**
    * 保存
    */
    void save(PaperQuestions paperQuestions);

    /**
    * 更新
    */
    void update(PaperQuestions paperQuestions);

    /**
    * 批量删除
    */
    void removeByIds(List ids,Long updatedBy);

    /**
    * 删除
    */
    void removeById(Long id,Long updatedBy);

}

