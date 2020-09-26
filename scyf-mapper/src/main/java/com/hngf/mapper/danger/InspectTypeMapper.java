package com.hngf.mapper.danger;

import com.hngf.entity.danger.InspectType;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Map;
import java.util.List;

/**
 * 1专业性检查；2日常检查；3节假日前后；4事故类比检查；5季节性检查；6综合性检查
 * 
 * @author hngf
 * @email 
 * @date 2020-05-28 16:40:19
 */
@Mapper
public interface InspectTypeMapper {

    List<InspectType> findList(Map<String, Object> params);

    InspectType findById(Long id);

    void add(InspectType InspectType);

    void update(InspectType InspectType);

    int deleteById(@Param("id")Long id);

    int deleteByIds(@Param("ids") List ids);

    int deleteBatchByCompanyId(@Param("companyId") Long companyId, @Param("updatedBy") Long updatedBy) ;
}
