package com.hngf.service.danger;

import com.hngf.common.utils.PageUtils;
import com.hngf.entity.danger.HiddenAttach;
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
public interface HiddenAttachService {

    PageUtils queryPage(Map<String, Object> params);

    /**
    * 根据ID查询
    */
    HiddenAttach getById(Long id);

    /**
    * 保存
    */
    int save(HiddenAttach HiddenAttach);

    /**
     * 批量保存
     * @param companyId
     * @param detailId
     * @param uploadPathList
     * @return
     */
    int saveBatch(Long companyId, Long detailId, String[] uploadPathList,Integer attachType);

    /**
    * 更新
    */
    int update(HiddenAttach HiddenAttach);

    /**
     * 根据detailId删除
     * @param companyId
     * @param detailId
     * @return
     */
    int removeByDetailId(Long companyId,Long detailId, Integer attachType);
    /**
    * 批量删除
    */
    int removeByIds(List ids);

    /**
    * 删除
    */
    int removeById(Long id);

    List<HiddenAttach>  findByDetailId(Map<String, Object> params);
}

