package com.hngf.mapper.danger;

import com.hngf.entity.danger.HiddenAccept;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Map;
import java.util.List;

/**
 * 隐患整改验收记录
 * 
 * @author hngf
 * @email 
 * @date 2020-05-20 17:36:24
 */
@Mapper
public interface HiddenAcceptMapper {

    List<HiddenAccept> findList(Map<String, Object> params);

    HiddenAccept findById(Long id);

    void add(HiddenAccept HiddenAccept);

    void update(HiddenAccept HiddenAccept);

    int deleteById(@Param("id") Long id);

    int deleteByIds(@Param("ids") List ids);

    /**
     * 根据隐患ID查询整改记录
     * @param hiddenId
     * @return
     */
    List<HiddenAccept> findByHiddenId(Long hiddenId);
}
