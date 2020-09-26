package com.hngf.mapper.danger;

import com.hngf.entity.danger.HiddenReview;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Map;
import java.util.List;

/**
 * 整改记录日志
 * 
 * @author hngf
 * @email 
 * @date 2020-06-11 10:27:06
 */
@Mapper
public interface HiddenReviewMapper {

    List<HiddenReview> findList(Map<String, Object> params);

    HiddenReview findById(Long id);

    void add(HiddenReview HiddenReview);

    void update(HiddenReview HiddenReview);

    int deleteById(@Param("id") Long id);

    int deleteByIds(@Param("ids") List ids);

    /**
     * 根据隐患ID查询
     * @param hiddenId
     * @return
     */
    List<HiddenReview> findByHiddenId(Long hiddenId);
}
