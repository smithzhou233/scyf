package com.hngf.service.danger;

import com.hngf.common.utils.PageUtils;
import com.hngf.entity.danger.InspectItemDef;
import java.util.Map;
import java.util.List;

/**
 * 检查项
 *
 * @author hngf
 * @email 
 * @date 2020-05-20 17:36:24
 */
public interface InspectItemDefService {

    PageUtils queryPage(Map<String, Object> params);

    /**
    * 根据ID查询
    */
    InspectItemDef getById(Long id);

    /**
     * 查询检查项总数
     * @param params
     * @return
     */
    int getInspectItemCount(Map<String, Object> params);

    /**
    * 保存
    */
    int save(InspectItemDef InspectItemDef);

    /**
    * 更新
    */
    int update(InspectItemDef InspectItemDef);

    /**
    * 批量删除
    */
    int removeByIds(List ids);

    /**
    * 删除
    */
    int removeById(Long id);
}

