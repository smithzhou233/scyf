package com.hngf.service.train;

import com.hngf.common.utils.PageUtils;
import com.hngf.entity.train.TrainAttachment;
import java.util.Map;
import java.util.List;

/**
 * 
 *
 * @author lxf
 * @email 
 * @date 2020-08-26 14:21:58
 */
public interface TrainAttachmentService {

    PageUtils queryPage(Map<String, Object> params, Integer pageNum, Integer pageSize, String order);

    /**
    * 根据ID查询
    */
    TrainAttachment getById(Long id);

    /**
    * 保存
    */
    void save(TrainAttachment trainAttachment);

    /**
    * 更新
    */
    void update(TrainAttachment trainAttachment);

    /**
    * 批量删除
    */
    void removeByIds(List ids, Long updatedBy);

    /**
    * 删除
    */
    void removeById(Long id, Long updatedBy);

    /**
     * 批量增加附件信息
     * @param map 附件信息集合
     * @param relationId 关联id
     * @param createdBy 创建人id
     * @param updateFlag true为更新，false 为新增
     * @return
     */
    int addBatchFromMap(Map<String,Object> map, Long relationId, Long createdBy, boolean updateFlag);

    /**
     * 删除指定关联id的附件信息
     * @param trainKeyId
     * @param removeBy
     * @return
     */
    int removeByTrainKeyId (Long trainKeyId, Integer attachmentType, Long removeBy);
}

