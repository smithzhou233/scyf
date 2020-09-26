package com.hngf.mapper.sys;

import com.hngf.entity.sys.CompanySeo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Map;
import java.util.List;

/**
 * 企业 oem 表 
 * 
 * @author hngf
 * @email 
 * @date 2020-07-07 17:02:00
 */
@Mapper
public interface CompanySeoMapper {

    List<CompanySeo> findList(Map<String, Object> params);

    CompanySeo findById(Long id);

    void add(CompanySeo CompanySeo);

    void update(CompanySeo CompanySeo);
}
