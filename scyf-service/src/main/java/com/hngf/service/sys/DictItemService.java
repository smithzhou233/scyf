package com.hngf.service.sys;

import com.hngf.common.utils.PageUtils;
import com.hngf.entity.sys.DictItem;
import java.util.Map;
import java.util.List;

/**
 * 字典项表
 *
 * @author hngf
 * @email 
 * @date 2020-05-20 17:31:55
 */
public interface DictItemService {

    PageUtils queryPage(Map<String, Object> params);

    /**
    * 根据ID查询
    */
    DictItem getById(String id);

    /**
    * 保存
    */
    void save(DictItem DictItem);

    /**
    * 更新
    */
    void update(DictItem DictItem);

    /**
    * 批量删除
    */
    void removeByIds(List ids);

    /**
    * 删除
    */
    void removeById(String id);
}

