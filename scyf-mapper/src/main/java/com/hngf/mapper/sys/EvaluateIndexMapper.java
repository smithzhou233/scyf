package com.hngf.mapper.sys;
import com.hngf.entity.sys.EvaluateIndex;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 评价指标：
评价方式两种：ls、lec；
 * 
 * @author hngf
 * @email 
 * @date 2020-05-27 17:51:02
 */
@Mapper
public interface EvaluateIndexMapper {

    List<EvaluateIndex> findList(Map<String, Object> params);

    EvaluateIndex findById(Long id);

    void add(EvaluateIndex EvaluateIndex);

    void update(EvaluateIndex EvaluateIndex);

    int deleteById(@Param("id")Long id);

    int deleteByIds(@Param("ids") List ids);

    String getEvaluateTypeByCompanyId(@Param("companyId") Long companyId) ;

    int deleteBatchByCompanyId(@Param("companyId") Long companyId, @Param("updatedBy") Long updatedBy) ;

    int addBatch(@Param("evaluateIndexList") List<EvaluateIndex> evaluateIndexList);
}
