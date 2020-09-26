package com.hngf.service.train.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hngf.common.exception.ScyfException;
import com.hngf.common.utils.PageUtils;
import com.hngf.entity.train.TrainAttachment;
import com.hngf.mapper.train.TrainAttachmentMapper;
import com.hngf.service.train.TrainAttachmentService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Service("trainAttachmentService")
public class TrainAttachmentServiceImpl implements TrainAttachmentService {

    @Autowired
    private TrainAttachmentMapper trainAttachmentMapper;

    @Override
    public PageUtils queryPage(Map<String, Object> params,Integer pageNum,Integer pageSize,String order) {
        //利用PageHelper分页查询 注意：这个一定要放查询语句的前一行,否则无法进行分页,因为它对紧随其后第一个sql语句有效
        if(StringUtils.isNotBlank(order)){
            PageHelper.startPage(pageNum, pageSize,order);
        }else{
            PageHelper.startPage(pageNum, pageSize);
        }
        List<TrainAttachment> list = trainAttachmentMapper.findList(params);
        PageInfo<TrainAttachment> pageInfo = new PageInfo<TrainAttachment>(list);
        return new PageUtils(list,(int)pageInfo.getTotal(),pageInfo.getPages(),pageInfo.getPageSize(),pageInfo.getPrePage() + 1);
    }

    @Override
    public TrainAttachment getById(Long id){
        if(null != id ){return trainAttachmentMapper.findById(id);}
        return null ;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(TrainAttachment trainAttachment) {
        trainAttachmentMapper.add(trainAttachment);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(TrainAttachment trainAttachment) {
        if(null != trainAttachment && null != trainAttachment.getTrainAttachmentId()){trainAttachmentMapper.update(trainAttachment);}
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeByIds(List ids , Long updatedBy) {
        if(null != ids && ids.size()>0){trainAttachmentMapper.deleteByIds(ids, updatedBy);}
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeById(Long id, Long updatedBy) {
        if(null != id){trainAttachmentMapper.deleteById(id, updatedBy);}
    }

    /**
     * 批量增加附件信息
     * @param map 附件信息集合
     * @param relationId 关联id
     * @param createdBy 创建人id
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int addBatchFromMap(Map<String,Object> map, Long relationId, Long createdBy, boolean updateFlag){
        Integer attachmentType = 0;
        try {
            attachmentType = Integer.parseInt(map.get("attachmentType").toString()) ;
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        if(updateFlag){
            trainAttachmentMapper.removeByTrainKeyId(relationId, attachmentType, createdBy);
        }
        Object trainAttachmentNamesObj = map.get("trainAttachmentName") ;
        Object trainSavePathObj = map.get("trainSavePath");
        Object trainExtendNameObj = map.get("trainExtendName");
        Object fileSizeObj = map.get("fileSize");
        Object trainThumbnailUrlObj = map.get("trainThumbnailUrl");
        Object mimeTypeObj = map.get("mimeType");
        Object imageWidthObj = map.get("imageWidth");
        Object imageHeightObj = map.get("imageHeight");
        if(null != trainAttachmentNamesObj && StringUtils.isNotEmpty(trainAttachmentNamesObj.toString()) ){
            if(null !=trainSavePathObj && StringUtils.isNotBlank(trainSavePathObj.toString()) ){
                String[] trainAttachmentNames = trainAttachmentNamesObj.toString().split(",");
                String[] trainSavePaths = trainSavePathObj.toString().split(",");
                if(trainSavePaths.length != trainAttachmentNames.length ){
                    throw new ScyfException("附件信息有误！");
                }
                String[] trainExtendNames = getArrayFromObj(trainExtendNameObj,",");
                String[] fileSizes = getArrayFromObj(fileSizeObj,",");
                String[] trainThumbnailUrls = getArrayFromObj(trainThumbnailUrlObj,",");
                String[] mimeTypes = getArrayFromObj(mimeTypeObj,",");
                String[] imageWidths = getArrayFromObj(imageWidthObj,",");
                String[] imageHeights = getArrayFromObj(imageHeightObj,",");


                List<TrainAttachment> trainAttachmentList = new ArrayList<>(trainAttachmentNames.length);
                for(int i=0; i< trainAttachmentNames.length ;i++ ){
                    TrainAttachment trainAttachment = new TrainAttachment(relationId, createdBy ,attachmentType);
                    trainAttachment.setTrainAttachmentName(trainAttachmentNames[i]);
                    trainAttachment.setTrainSavePath(trainSavePaths[i]);
                    if(null != trainExtendNames && trainExtendNames.length > i){
                        trainAttachment.setTrainExtendName(trainExtendNames[i]);
                    }
                    if(null != fileSizes && fileSizes.length > i){
                        trainAttachment.setFileSize(new Integer(fileSizes[i]));
                    }
                    if(null != trainThumbnailUrls && trainThumbnailUrls.length > i){
                        trainAttachment.setTrainThumbnailUrl(trainThumbnailUrls[i]);
                    }
                    if(null != mimeTypes && mimeTypes.length > i){
                        trainAttachment.setMimeType(mimeTypes[i]);
                    }
                    if(null != imageWidths && imageWidths.length > i){
                        trainAttachment.setImageWidth(new Integer(imageWidths[i]));
                    }
                    if(null != imageHeights && imageHeights.length > i){
                        trainAttachment.setImageHeight(new Integer(imageHeights[i]));
                    }
                    trainAttachmentList.add(trainAttachment);
                }
                if(null != trainAttachmentList.get(0)){
                   return trainAttachmentMapper.addBatch(trainAttachmentList);
                }
            }
        }
        return 0;
    }

    private String[] getArrayFromObj(Object obj ,String separator){
        String[] objArray = null;
        if(null !=obj && StringUtils.isNotBlank(obj.toString())){
            objArray = obj.toString().split(separator);
        }
        return objArray;
    }

    /**
     * 删除指定关联id的附件信息
     * @param trainKeyId
     * @param removeBy
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int removeByTrainKeyId (Long trainKeyId, Integer attachmentType, Long removeBy){return trainAttachmentMapper.removeByTrainKeyId(trainKeyId, attachmentType, removeBy);}

}
