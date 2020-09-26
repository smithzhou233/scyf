package com.hngf.mapper.scyf;

import com.hngf.entity.scyf.SecureProduc;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Map;
import java.util.List;

/**
 * 安全生产基本信息表
 * 
 * @author hngf
 * @email 
 * @date 2020-05-22 16:52:31
 */
@Mapper
public interface SecureProducMapper {

    List<SecureProduc> findList(Map<String, Object> params);

    SecureProduc findById(Long id);

    void add(SecureProduc SecureProduc);

    void update(SecureProduc SecureProduc);

    int deleteById(@Param("id")Long id);

    int deleteByIds(@Param("ids") List ids);


    void deleteWithCompanyIds(String cIds);
}
