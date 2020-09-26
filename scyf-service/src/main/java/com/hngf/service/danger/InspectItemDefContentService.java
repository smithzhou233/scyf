package com.hngf.service.danger;

import com.hngf.common.utils.PageUtils;
import com.hngf.entity.danger.InspectItemDefContent;
import java.util.Map;
import java.util.List;

/**
 * 检查项内容
 *
 * @author hngf
 * @email 
 * @date 2020-05-20 17:36:24
 */
public interface InspectItemDefContentService {

    PageUtils queryPage(Map<String, Object> params);

    /**
    * 根据ID查询
    */
    InspectItemDefContent getById(Long id);

    /**
    * 保存
    */
    void save(InspectItemDefContent InspectItemDefContent);

    /**
    * 更新
    */
    void update(InspectItemDefContent InspectItemDefContent);

    /**
    * 批量删除
    */
    void removeByIds(List ids);

    /**
    * 删除
    */
    void removeById(Long id);
}

