package com.hngf.service.sys;

import com.hngf.common.utils.PageUtils;
import com.hngf.entity.sys.Log;
import java.util.Map;
import java.util.List;

/**
 * 系统日志表
 *
 * @author hngf
 * @email 
 * @date 2020-05-20 17:31:55
 */
public interface LogService {

    PageUtils queryPage(Map<String, Object> params,Integer pageNum,Integer pageSize,String order);

    /**
    * 根据ID查询
    */
    Log getById(Long id);

    /**
    * 保存
    */
    void save(Log Log);

    /**
    * 批量删除
    */
    void removeByIds(List ids);

    /**
    * 删除
    */
    void removeById(Long id);
}

