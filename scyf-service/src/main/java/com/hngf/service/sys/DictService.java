package com.hngf.service.sys;

import com.hngf.common.utils.PageUtils;
import com.hngf.entity.sys.Dict;
import java.util.Map;
import java.util.List;

/**
 * 字典表
 *
 * @author hngf
 * @email 
 * @date 2020-05-20 17:31:55
 */
public interface DictService {
    /**
    * @Author: zyj
    * @Description:查询字典表数据表数据
    * @Param
    * @Date 9:24 2020/6/11
    */
    PageUtils queryPage(Map<String, Object> params,Integer pageNum,Integer pageSize,String order);

    /**
    * 根据ID查询
    */
    Dict getById(String id);

    /**
    * 根据字典类型查询
     * @param dictType
    */
    List<Dict> getByDictType(String dictType);

    /**
    * 保存
    */
    void save(Dict Dict);

    /**
    * 更新
    */
    void update(Dict Dict);

    /**
    * 批量删除
    */
    void removeByIds(List ids);

    /**
    * 删除
    */
    void removeById(Long id);

    /**
     * 根据表名获取当前最大ID
     * @param tabName
     * @return
     */

    Long getTabId(String tabName);
}

