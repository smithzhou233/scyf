package com.hngf.service.sys;

import com.hngf.common.utils.PageUtils;
import com.hngf.entity.sys.CompanyIndustry;
import java.util.Map;
import java.util.List;

/**
 * 
 *
 * @author hngf
 * @email 
 * @date 2020-05-20 17:31:55
 */
public interface CompanyIndustryService {

    PageUtils queryPage(Map<String, Object> params);

    /**
    * 根据ID查询
    */
    CompanyIndustry getById(Long id);

    /**
    * 保存
    */
    void save(CompanyIndustry CompanyIndustry);

    /**
    * 更新
    */
    void update(CompanyIndustry CompanyIndustry);

    /**
    * 批量删除
    */
    void removeByIds(List ids);

    /**
    * 删除
    */
    void removeById(Long id);


}

