package com.hngf.service.exam;

import com.hngf.common.utils.PageUtils;
import com.hngf.entity.exam.PaperMark;
import java.util.Map;
import java.util.List;

/**
 * 试卷类型分数
 *
 * @author hngf
 * @email 
 * @date 2020-08-14 09:39:02
 */
public interface PaperMarkService {

    PageUtils queryPage(Map<String, Object> params,Integer pageNum,Integer pageSize,String order);

    /**
    * 根据ID查询
    */
    PaperMark getById(Long id);

    /**
    * 保存
    */
    void save(PaperMark paperMark);

    /**
    * 更新
    */
    void update(PaperMark paperMark);

    /**
    * 批量删除
    */
    void removeByIds(List ids,Long updatedBy);

    /**
    * 删除
    */
    void removeById(Long id,Long updatedBy);

}

