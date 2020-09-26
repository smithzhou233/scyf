package com.hngf.mapper.danger;

import com.hngf.entity.danger.HiddenAttach;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Map;
import java.util.List;

/**
 * 隐患附件表
 * 
 * @author hngf
 * @email 
 * @date 2020-05-20 17:36:24
 */
@Mapper
public interface HiddenAttachMapper {

    List<HiddenAttach> findList(Map<String, Object> params);

    HiddenAttach findById(Long id);
    /**
    * @Author: zyj
    * @Description:【APP】我的，查询反馈列表附件详情信息
    * @Param  detailId  详情id
    * @Date 16:25 2020/6/18
    */
    List<HiddenAttach> findByDetailId(Map<String, Object>   params);

    int add(HiddenAttach HiddenAttach);

    int addForeach(List<HiddenAttach> list);

    int update(HiddenAttach HiddenAttach);

    int deleteById(@Param("id") Long id);

    int deleteByIds(@Param("ids") List ids);

    /**
     * 根据detailId删除
     * @param companyId
     * @param detailId
     * @return
     */
    int deleteByDetailId(Long companyId, Long detailId,Integer attachType);
    /**
     * 根据detailId查询
     * @param detailId
     * @return
     */
    List<HiddenAttach>  findListByDetailId(@Param("detailId") Long detailId);

    /**
     * 整改前附件
     * @param detailId
     * @return
     */
    List<HiddenAttach> findBeforeHiddenAttach(Long detailId);

    /**
     * 整改后附件
     * @param detailId
     * @return
     */
    List<HiddenAttach> findAfterHiddenAttach(Long detailId);
}
