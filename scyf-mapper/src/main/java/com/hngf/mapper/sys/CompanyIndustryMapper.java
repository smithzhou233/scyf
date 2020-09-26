package com.hngf.mapper.sys;

import com.hngf.entity.sys.CompanyIndustry;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Map;
import java.util.List;

/**
 * 
 * 
 * @author hngf
 * @email 
 * @date 2020-05-20 17:31:55
 */
@Mapper
public interface CompanyIndustryMapper {

    List<CompanyIndustry> findList(Map<String, Object> params);

    CompanyIndustry findById(Long id);

    void add(CompanyIndustry CompanyIndustry);

    void update(CompanyIndustry CompanyIndustry);

    int deleteById(@Param("id") Long id);

    int deleteByIds(@Param("ids") List ids);
}
