package com.hngf.mapper.train;

import com.hngf.entity.train.TrainInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.Map;
import java.util.List;

/**
 * 
 * 
 * @author yfh
 * @email 
 * @date 2020-05-26 11:59:17
 */
@Mapper
public interface TrainInfoMapper {

    List<Map<String,Object>> findList(Map<String, Object> params);

    List<Map<String,Object>> findById(Long id);

    void add(TrainInfo TrainInfo);

    void update(TrainInfo TrainInfo);

    int deleteById(@Param("id")Long id, @Param("updatedBy")Long updatedBy);

    int deleteByIds(@Param("ids") List ids);

    List<Map<String, Object>> selectPlans(@Param("companyId")Integer companyId);

    void insertAttachment(@Param("trainInfoId")Long trainInfoId,@Param("attachmentType")Integer attachmentType,@Param("trainAttachmentName")String trainAttachmentName, @Param("trainExtendName")String trainExtendName, @Param("trainSavePath")String trainSavePath, @Param("fileSize")String fileSize,@Param("userId") Long userId, @Param("createTime")Date createTime);
    /**
     * 删除培训内容附件
     * yfh
     * 2020/07/09
     * @param trainId
     */
    void delAttachment(@Param("trainId")Integer trainId,@Param("attachmentType")Integer attachmentType);

    /**
     * 查看培训内容相关的附件信息
     * @param trainInfoId
     * @param attachMentType
     * @return
     */
    List<Map<String, Object>> findTrainInfoAttachMent(@Param("trainInfoId")String trainInfoId, @Param("attachMentType")Integer attachMentType);
}
