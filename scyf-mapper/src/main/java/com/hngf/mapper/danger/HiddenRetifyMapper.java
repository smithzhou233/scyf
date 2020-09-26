package com.hngf.mapper.danger;

import com.hngf.entity.danger.HiddenRetify;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Map;
import java.util.List;

/**
 * 隐患整改记录
 * 
 * @author hngf
 * @email 
 * @date 2020-05-20 17:36:24
 */
@Mapper
public interface HiddenRetifyMapper {

    List<HiddenRetify> findList(Map<String, Object> params);

    HiddenRetify findById(Long id);

    void add(HiddenRetify HiddenRetify);

    void update(HiddenRetify HiddenRetify);

    int deleteById(@Param("id") Long id);

    int deleteByIds(@Param("ids") List ids);

    /**
     * 根据隐患ID查询整改记录
     * @param hiddenId
     * @return
     */
    List<HiddenRetify> findByHiddenId(Long hiddenId);
}
