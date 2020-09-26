package com.hngf.mapper.sys;

import com.hngf.entity.sys.Log;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Map;
import java.util.List;

/**
 * 系统日志表
 * 
 * @author hngf
 * @email 
 * @date 2020-05-20 17:31:55
 */
@Mapper
public interface LogMapper {

    List<Log> findList(Map<String, Object> params);

    Log findById(Long id);

    void add(Log Log);

    int deleteById(@Param("id") Long id);

    int deleteByIds(@Param("ids") List ids);
}
