package com.hngf.service.train;

import com.hngf.common.utils.PageUtils;
import com.hngf.entity.train.TrainInfo;

import java.util.Date;
import java.util.Map;
import java.util.List;

/**
 * 
 *
 * @author hngf
 * @email 
 * @date 2020-05-26 11:59:17
 */
public interface TrainInfoService {

    PageUtils queryPage(Map<String, Object> params,Integer pageNum,Integer pageSize,String order);
    /**
    * 根据ID查询
    */
    List<Map<String,Object>> getById(Long id);

    /**
    * 保存
    */
    void save(TrainInfo TrainInfo);

    /**
    * 更新
    */
    void update(TrainInfo TrainInfo);

    /**
    * 批量删除
    */
    void removeByIds(List ids);

    /**
    * 删除
    */
    void removeById(Long id, Long updatedBy);

    /**
     * 查询公司培训计划
     * @return
     */
    List<Map<String, Object>> selectPlans(Integer companyId);

    void insertAttachment(Long trainInfoId,Integer attachmentType,String trainAttachmentName, String trainExtendName, String trainSavePath, String fileSize, Long userId, Date createTime);

    /**
     * 删除培训内容附件
     * @param trainInfoId
     */
    void delAttachment(Long trainInfoId,Integer attachmentType);

    /**
     * 查看培训内容相关的附件信息
     * @param trainInfoId
     * @return
     */
    List<Map<String, Object>> findTrainInfoAttachMent(String trainInfoId,Integer attachMentType);
}

