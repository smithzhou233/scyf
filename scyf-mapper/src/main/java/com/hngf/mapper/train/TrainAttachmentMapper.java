package com.hngf.mapper.train;

import com.hngf.entity.train.TrainAttachment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Map;
import java.util.List;

/**
 * 
 * 
 * @author lxf
 * @email 
 * @date 2020-08-26 14:21:58
 */
@Mapper
public interface TrainAttachmentMapper {

    List<TrainAttachment> findList(Map<String, Object> params);

    TrainAttachment findById(Long id);

    void add(TrainAttachment trainAttachment);

    void update(TrainAttachment trainAttachment);

    int deleteById(@Param("id") Long id, @Param("updatedBy") Long updatedBy);

    int deleteByIds(@Param("ids") List ids, @Param("updatedBy") Long updatedBy);

    int addBatch(@Param("trainAttachmentList") List<TrainAttachment> trainAttachmentList);

    /**
     * 删除指定关联id的附件信息
     * @param trainKeyId
     * @param attachmentType 附件类型
     * @param updatedBy
     * @return
     */
    int removeByTrainKeyId (@Param("trainKeyId")Long trainKeyId, @Param("attachmentType")Integer attachmentType, @Param("updatedBy")Long updatedBy);

    /**
     * 查询关联id的附件信息列表
     * @param trainKeyId
     * @return
     */
    List<TrainAttachment> findListByTrainKeyId(@Param("trainKeyId")Long trainKeyId);
}
