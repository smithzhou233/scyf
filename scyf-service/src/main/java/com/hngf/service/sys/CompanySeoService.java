package com.hngf.service.sys;

import com.hngf.common.utils.PageUtils;
import com.hngf.entity.sys.CompanySeo;
import java.util.Map;
import java.util.List;

/**
 * 企业 oem 表 
 *
 * @author hngf
 * @email 
 * @date 2020-07-07 17:02:00
 */
public interface CompanySeoService {

    PageUtils queryPage(Map<String, Object> params, Integer pageNum, Integer pageSize, String order);

    /**
    * 根据ID查询
    */
    CompanySeo getById(Long id);

    /**
    * 保存
    */
    void save(CompanySeo CompanySeo);

    /**
    * 更新
    */
    void update(CompanySeo CompanySeo);

}

