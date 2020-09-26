package com.hngf.service.danger;

import com.hngf.common.utils.PageUtils;
import com.hngf.entity.danger.InspectType;
import java.util.Map;
import java.util.List;

/**
 * 1专业性检查；2日常检查；3节假日前后；4事故类比检查；5季节性检查；6综合性检查
 *
 * @author hngf
 * @email 
 * @date 2020-05-28 16:40:19
 */
public interface InspectTypeService {

    PageUtils queryPage(Map<String, Object> params,Integer pageNum,Integer pageSize,String order);

    /**
    * 根据ID查询
    */
    InspectType getById(Long id);

    /**
    * 保存
    */
    void save(InspectType InspectType);

    /**
    * 更新
    */
    void update(InspectType InspectType);

    /**
    * 批量删除
    */
    void removeByIds(List ids);

    /**
    * 删除
    */
    void removeById(Long id);

    int initScheduleCheckTypeSetting(Long companyId);

    int deleteBatchByCompanyId(Long companyId, Long updatedBy) ;
}

