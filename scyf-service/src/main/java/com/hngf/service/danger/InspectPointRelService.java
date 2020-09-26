package com.hngf.service.danger;

import com.hngf.common.utils.PageUtils;
import com.hngf.entity.danger.InspectPointRel;
import java.util.Map;
import java.util.List;

/**
 * 
 *
 * @author hngf
 * @email 
 * @date 2020-05-20 17:36:24
 */
public interface InspectPointRelService {

    PageUtils queryPage(Map<String, Object> params);

    /**
    * 根据ID查询
    */
    InspectPointRel getById(Long id);

    /**
    * 保存
    */
    void save(InspectPointRel InspectPointRel);

    /**
    * 更新
    */
    void update(InspectPointRel InspectPointRel);

    /**
    * 批量删除
    */
    void removeByIds(List ids);

    /**
    * 删除
    */
    void removeById(Long id);

    /**
     * 删除
     */
    int removeByMap(Map<String, Object> params);

    /**
     * 批量保存
     */
    int saveBatch(List<InspectPointRel> list);
}

