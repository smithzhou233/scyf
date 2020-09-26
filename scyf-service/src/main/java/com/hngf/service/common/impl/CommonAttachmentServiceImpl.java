package com.hngf.service.common.impl;

import com.hngf.common.utils.Constant;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hngf.common.utils.PageUtils;

import com.hngf.mapper.common.CommonAttachmentMapper;
import com.hngf.entity.common.CommonAttachment;
import com.hngf.service.common.CommonAttachmentService;


@Service("CommonAttachmentService")
public class CommonAttachmentServiceImpl implements CommonAttachmentService {

    @Autowired
    private CommonAttachmentMapper commonAttachmentMapper;

    @Override
    public PageUtils queryPage(Map<String, Object> params,Integer pageNum,Integer pageSize,String order) {
        //利用PageHelper分页查询 注意：这个一定要放查询语句的前一行,否则无法进行分页,因为它对紧随其后第一个sql语句有效
        if(StringUtils.isNotBlank(order)){
            PageHelper.startPage(pageNum, pageSize,order);
        }else{
            PageHelper.startPage(pageNum, pageSize);
        }
        List<CommonAttachment> list = commonAttachmentMapper.findList(params);
        PageInfo<CommonAttachment> pageInfo = new PageInfo<CommonAttachment>(list);
        return new PageUtils(list,(int)pageInfo.getTotal(),pageInfo.getPages(),pageInfo.getPageSize(),pageInfo.getPrePage() + 1);
    }

    @Override
    public CommonAttachment getById(Long id){
        return commonAttachmentMapper.findById(id);
    }
    /**
     * @Author: zyj
     * @Description:根据关联id查找附件
     * @Param
     * @Date 16:10 2020/6/13
     */
    @Override
    public  List<CommonAttachment> selectByOwnerKey(Long ownerId) {
        return commonAttachmentMapper.selectByOwnerKey(ownerId);
    }
    /**
     * @Author: zyj
     * @Description:删除关联的附件
     * @Param   ownerId 关联的id
     * @Date 16:33 2020/6/13
     */
    @Override
   public void deleteByOwnerId(Long ownerId){
       commonAttachmentMapper.deleteByOwnerId(ownerId);
   }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(CommonAttachment CommonAttachment) {
        commonAttachmentMapper.add(CommonAttachment);
    }

    @Override
    public int saveBatch(Long detailId, String[] uploadPathStr) {
        if (null != uploadPathStr && uploadPathStr.length > 0) {
            //保存之前删除
            this.commonAttachmentMapper.deleteByOwnerId(detailId);
            List<CommonAttachment> list = new ArrayList<>();
            String uploadPath = "" ;
            for (int i = 0; i < uploadPathStr.length; i++) {
                uploadPath = uploadPathStr[i];
                if (StringUtils.isEmpty(uploadPath)) {
                    continue;
                }
                CommonAttachment attachment = new CommonAttachment();
                if (!uploadPath.contains(".jpg") && !uploadPath.contains(".png")) {
                    if (uploadPath.contains(".mp4")) {
                        attachment.setMimeType("video");
                    }
                } else {
                    attachment.setMimeType("image");
                }

                attachment.setOwnerType(Constant.OWNER_TYPE_3);
                attachment.setOwnerId(detailId);
                attachment.setSavePath(uploadPath);
                attachment.setCreatedTime(new Date());
                attachment.setDelFlag(0);

                list.add(attachment);
            }

            return commonAttachmentMapper.addForeach(list);
        }
        return 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(CommonAttachment CommonAttachment) {
        commonAttachmentMapper.update(CommonAttachment);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeByIds(List ids) {
        commonAttachmentMapper.deleteByIds(ids);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeById(Long id) {
        commonAttachmentMapper.deleteById(id);
    }
}
