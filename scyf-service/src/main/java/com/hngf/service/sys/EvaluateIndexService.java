package com.hngf.service.sys;

import com.hngf.common.utils.PageUtils;
import com.hngf.entity.sys.EvaluateIndex;
import java.util.Map;
import java.util.List;

/**
 * 评价指标：
评价方式两种：ls、lec；
 *
 * @author hngf
 * @email 
 * @date 2020-05-27 17:51:02
 */
public interface EvaluateIndexService {

    PageUtils queryPage(Map<String, Object> params,Integer pageNum,Integer pageSize,String order);

    /**
    * 根据ID查询
    */
    EvaluateIndex getById(Long id);

    /**
    * 保存
    */
    void save(EvaluateIndex EvaluateIndex);

    /**
    * 更新
    */
    void update(EvaluateIndex EvaluateIndex);

    /**
    * 批量删除
    */
    void removeByIds(List ids);

    /**
    * 删除
    */
    void removeById(Long id);

    void initEvaluate(Long createdBy, Long companyId,String evaluateType);

    String getEvaluateTypeByCompanyId( Long companyId) ;

    int deleteBatchByCompanyId(Long companyId, Long updatedBy) ;
}

