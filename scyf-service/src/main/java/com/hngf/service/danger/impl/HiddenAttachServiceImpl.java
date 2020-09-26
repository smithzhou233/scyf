package com.hngf.service.danger.impl;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import java.util.List;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hngf.common.utils.PageUtils;
import com.hngf.common.utils.Constant;

import com.hngf.mapper.danger.HiddenAttachMapper;
import com.hngf.entity.danger.HiddenAttach;
import com.hngf.service.danger.HiddenAttachService;


@Service("HiddenAttachService")
public class HiddenAttachServiceImpl implements HiddenAttachService {

    @Autowired
    private HiddenAttachMapper HiddenAttachMapper;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        int pageNum = null!= params.get("pageNum") ? Integer.parseInt(params.get("pageNum").toString()) : 1;
        int pageSize = null != params.get("pageSize") ? Integer.parseInt(params.get("pageSize").toString()) : Constant.PAGE_SIZE;
        //利用PageHelper分页查询 注意：这个一定要放查询语句的前一行,否则无法进行分页,因为它对紧随其后第一个sql语句有效
        PageHelper.startPage(pageNum, pageSize);
        List<HiddenAttach> list = HiddenAttachMapper.findList(params);
        PageInfo<HiddenAttach> pageInfo = new PageInfo<HiddenAttach>(list);
        return new PageUtils(list,(int)pageInfo.getTotal(),pageInfo.getPages(),pageInfo.getPageSize(),pageInfo.getPrePage() + 1);
    }

    @Override
    public HiddenAttach getById(Long id){
        return HiddenAttachMapper.findById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int save(HiddenAttach HiddenAttach) {
        return HiddenAttachMapper.add(HiddenAttach);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int saveBatch(Long companyId, Long detailId, String[] uploadPathList, Integer attachType) {
        //保存之前先删除
        this.removeByDetailId(companyId, detailId,attachType);
        List<HiddenAttach> list = new ArrayList<>();
        for (int i = 0; i < uploadPathList.length; i++) {
            String uploadPath = uploadPathList[i];
            if (StringUtils.isEmpty(uploadPath)) {
                continue;
            }
            HiddenAttach hiddenAttach = new HiddenAttach();
            if (!uploadPath.contains(".jpg") && !uploadPath.contains(".png") && !uploadPath.contains(".gif") && !uploadPath.contains(".jpeg")) {
                if (uploadPath.contains(".mp4")) {
                    hiddenAttach.setHiddenAttachType("video");
                }
            } else {
                hiddenAttach.setHiddenAttachType("image");
            }

            hiddenAttach.setCompanyId(companyId);
            hiddenAttach.setHiddenDetailType(attachType);
            hiddenAttach.setHiddenAttachPath(uploadPath);
            hiddenAttach.setHiddenDetailId(detailId);
            hiddenAttach.setCreatedTime(new Date());
            list.add(hiddenAttach);
        }
        return HiddenAttachMapper.addForeach(list);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int update(HiddenAttach HiddenAttach) {
        return HiddenAttachMapper.update(HiddenAttach);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public int removeByDetailId(Long companyId,Long detailId, Integer attachType) {
        return HiddenAttachMapper.deleteByDetailId(companyId,detailId,attachType);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int removeByIds(List ids) {
        return HiddenAttachMapper.deleteByIds(ids);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int removeById(Long id) {
        return HiddenAttachMapper.deleteById(id);
    }

    @Override
    public List<HiddenAttach> findByDetailId(Map<String, Object> params) {
        return HiddenAttachMapper.findByDetailId(params);
    }


}
