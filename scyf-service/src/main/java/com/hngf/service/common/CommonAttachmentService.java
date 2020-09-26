package com.hngf.service.common;

import com.hngf.common.utils.PageUtils;
import com.hngf.entity.common.CommonAttachment;
import java.util.Map;
import java.util.List;

/**
 * 业务通用附件表
 *
 * @author hngf
 * @email 
 * @date 2020-06-13 15:24:30
 */
public interface CommonAttachmentService {

    PageUtils queryPage(Map<String, Object> params,Integer pageNum,Integer pageSize,String order);

    /**
    * 根据ID查询
    */
    CommonAttachment getById(Long id);
    /**
     * @Author: zyj
     * @Description:根据关联id查找附件
     * @Param
     * @Date 16:10 2020/6/13
     */
    List<CommonAttachment> selectByOwnerKey(Long ownerId);
    /**
     * @Author: zyj
     * @Description:删除关联的附件
     * @Param
     * @Date 16:33 2020/6/13
     */
    void deleteByOwnerId(Long ownerId);

    /**
    * 保存
    */
    void save(CommonAttachment CommonAttachment);

    /**
    * 批量保存
    */
    int saveBatch(Long detailId, String[] uploadPathStr);

    /**
    * 更新
    */
    void update(CommonAttachment CommonAttachment);

    /**
    * 批量删除
    */
    void removeByIds(List ids);

    /**
    * 删除
    */
    void removeById(Long id);
}

