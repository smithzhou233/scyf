package com.hngf.mapper.common;

import com.hngf.dto.commonPost.CommonPostDto;
import com.hngf.entity.common.CommonPost;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Map;
import java.util.List;

/**
 * 知识库
 * 
 * @author hngf
 * @email 
 * @date 2020-06-04 11:36:32
 */
@Mapper
public interface CommonPostMapper {

    List<CommonPost> findList(Map<String, Object> params);
    /**
    * @Author: zyj
    * @Description:查询风险规章制度
    * @Param
    * @Date 9:43 2020/6/15
    */
    List<CommonPostDto> queryInstitution(Map<String, Object> params);

    CommonPost findById(Long id);

    void add(CommonPost CommonPost);

    void update(CommonPost CommonPost);

    int deleteById(@Param("id") Long id);

    int deleteByIds(@Param("ids") List ids);

    /**
     * 发布知识点
     * @param ids 知识点id集合
     * @param publishBy  发布人id
     * @return
     */
    int publishByIds(@Param("ids") List ids, @Param("publishBy")Long publishBy) ;
}
