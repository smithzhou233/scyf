package com.hngf.service.train.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hngf.common.utils.PageUtils;
import com.hngf.entity.train.TrainInfo;
import com.hngf.mapper.train.TrainInfoMapper;
import com.hngf.mapper.train.TrainPlanMapper;
import com.hngf.service.train.TrainInfoService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;


@Service("TrainInfoService")
public class TrainInfoServiceImpl implements TrainInfoService {

    @Autowired
    private TrainInfoMapper trainInfoMapper;
    @Autowired
    private TrainPlanMapper trainPlanMapper;

    @Override
    public PageUtils queryPage(Map<String, Object> params,Integer pageNum,Integer pageSize,String order) {
        //利用PageHelper分页查询 注意：这个一定要放查询语句的前一行,否则无法进行分页,因为它对紧随其后第一个sql语句有效
        if(StringUtils.isNotBlank(order)){
            PageHelper.startPage(pageNum, pageSize,order);
        }else{
            PageHelper.startPage(pageNum, pageSize);
        }
        List<Map<String,Object>> list = trainInfoMapper.findList(params);
        if(list.size()>0){
            for(int i=0;i<list.size();i++){
                Map map=list.get(i);
                String trainType =map.get("trainType").toString();
                String dictType="train_type";
                String dictName=trainPlanMapper.getType(trainType,dictType);
                list.get(i).put("dictName",dictName);
            }
        }
        PageInfo pageInfo = new PageInfo(list);
        return new PageUtils(list,(int)pageInfo.getTotal(),pageInfo.getPages(),pageInfo.getPageSize(),pageInfo.getPrePage() + 1);
    }

    @Override
    public List<Map<String,Object>> getById(Long id){
        return trainInfoMapper.findById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(TrainInfo TrainInfo) {
        trainInfoMapper.add(TrainInfo);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(TrainInfo TrainInfo) {
        trainInfoMapper.update(TrainInfo);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeByIds(List ids) {
        trainInfoMapper.deleteByIds(ids);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeById(Long id, Long updatedBy) {
        trainInfoMapper.deleteById(id,updatedBy);
    }

    @Override
    public List<Map<String, Object>> selectPlans(Integer companyId) {
        return trainInfoMapper.selectPlans(companyId);
    }

    /**
     * 新增培训内容附件
     * @param trainInfoId
     * @param trainAttachmentName
     * @param trainExtendName
     * @param trainSavePath
     * @param fileSize
     * @param userId
     * @param createTime
     */
    @Override
    public void insertAttachment(Long trainInfoId,Integer attachmentType,String trainAttachmentName, String trainExtendName, String trainSavePath, String fileSize, Long userId, Date createTime) {
        trainInfoMapper.insertAttachment(trainInfoId,attachmentType,trainAttachmentName,trainExtendName,trainSavePath,fileSize,userId,createTime);
    }

    /**
     * 删除培训内容附件
     * yfh
     * 2020/07/09
     * @param trainInfoId
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delAttachment(Long trainInfoId,Integer attachmentType) {
        Integer integer = Integer.valueOf(trainInfoId.toString());
        trainInfoMapper.delAttachment(integer,attachmentType);
    }

    /**
     * 查看培训内容相关的附件信息
     * yfh
     * @param trainInfoId
     * @param attachMentType
     * @return
     */
    @Override
    public List<Map<String, Object>> findTrainInfoAttachMent(String trainInfoId, Integer attachMentType) {
        return  trainInfoMapper.findTrainInfoAttachMent(trainInfoId,attachMentType);
    }
}
