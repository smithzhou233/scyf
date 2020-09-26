package com.hngf.service.danger.impl;

import com.hngf.entity.danger.HiddenAttach;
import com.hngf.mapper.danger.HiddenAttachMapper;
import com.hngf.service.danger.HiddenAttachService;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hngf.common.utils.PageUtils;
import com.hngf.common.utils.Constant;

import com.hngf.mapper.danger.FeedbackMapper;
import com.hngf.entity.danger.Feedback;
import com.hngf.service.danger.FeedbackService;


@Service("FeedbackService")
public class FeedbackServiceImpl implements FeedbackService {

    @Autowired
    private FeedbackMapper FeedbackMapper;
    @Autowired
    private HiddenAttachMapper hiddenAttachMapper;
    @Autowired
    private HiddenAttachService hiddenAttachService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        int pageNum = null!= params.get("pageNum") ? Integer.parseInt(params.get("pageNum").toString()) : 1;
        int pageSize = null != params.get("pageSize") ? Integer.parseInt(params.get("pageSize").toString()) : Constant.PAGE_SIZE;
        //利用PageHelper分页查询 注意：这个一定要放查询语句的前一行,否则无法进行分页,因为它对紧随其后第一个sql语句有效
        PageHelper.startPage(pageNum, pageSize);
        List<Map<String,Object>> list = FeedbackMapper.findList(params);
        PageInfo pageInfo = new PageInfo(list);
        return new PageUtils(list,(int)pageInfo.getTotal(),pageInfo.getPages(),pageInfo.getPageSize(),pageInfo.getPrePage() + 1);
    }

    @Override
    public Feedback getById(Long id){
        Feedback feedback =  FeedbackMapper.findById(id);
        if (null != feedback && null != feedback.getFeedbackId() && StringUtils.isNotBlank(feedback.getFeedbackId().toString())) {
            List<HiddenAttach> attachList =hiddenAttachMapper .findListByDetailId(feedback.getFeedbackId());
            feedback.setAttachList(attachList);
        }
        return feedback;
    }
    /**
     * @Author: zyj
     * @Description:【APP】我的，反馈列表查询
     * @Param companyId企业id resultValue 处理结果： 0未处理；1已处理 creater 创建人，即检查人
     * @Date 15:50 2020/6/18
     */
    @Override
    public PageUtils findAllList(Map<String, Object> params) {
        int pageNum = null!= params.get("pageNum") ? Integer.parseInt(params.get("pageNum").toString()) : 1;
        int pageSize = null != params.get("pageSize") ? Integer.parseInt(params.get("pageSize").toString()) : Constant.PAGE_SIZE;
        //利用PageHelper分页查询 注意：这个一定要放查询语句的前一行,否则无法进行分页,因为它对紧随其后第一个sql语句有效
        PageHelper.startPage(pageNum, pageSize);
        List<Map<String, Object>> list = FeedbackMapper.findAllList(params);
        PageInfo<Map<String, Object>> pageInfo = new PageInfo<Map<String, Object>>(list);
        return new PageUtils(list,(int)pageInfo.getTotal(),pageInfo.getPages(),pageInfo.getPageSize(),pageInfo.getPrePage() + 1);
    }
    /**
     * @Author: zyj
     * @Description:【APP】我的，反馈列表详情信息
     * @Param feedbackId 主键id
     * @Date 16:17 2020/6/18
     */
    @Override
    public Map<String, Object> getDetailById(Long feedbackId) {
        Map<String, Object> detailById = FeedbackMapper.getDetailById(feedbackId);
        if (null != detailById && null != detailById.get("feedbackId") && StringUtils.isNotBlank(detailById.get("feedbackId").toString())){
            Map<String, Object> attachParam = new HashMap<>();
            attachParam.put("detailId",Long.parseLong(detailById.get("feedbackId").toString()));
            attachParam.put("hiddenDetailType",1);  //1 隐患 2.验收
            List<HiddenAttach> feedbackId1 = hiddenAttachMapper.findByDetailId(attachParam);
            detailById.put("attachList",feedbackId1);
        }
        return detailById;
    }
    /**
     * @Author: zyj
     * @Description:【APP】提交反馈信息
     * @Param
     * @Date 17:01 2020/6/18
     */
    @Override
    public void saveBaseChecked(Feedback feedback, String url, String extendName) {
        FeedbackMapper.add(feedback);
        //保存附件信息
        if (null != feedback.getImgUrlStr() && feedback.getImgUrlStr().length() > 0) {
            this.hiddenAttachService.saveBatch(feedback.getCompanyId(),feedback.getFeedbackId(),feedback.getImgUrlStr().split(","),Constant.ATTACH_HDANGER);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(Feedback Feedback) {
        FeedbackMapper.add(Feedback);
        String imgUrlStr =Feedback.getImgUrlStr();
        if(StringUtils.isNotEmpty(imgUrlStr)){
            String[] urlArr = imgUrlStr.split(",");
            List<HiddenAttach> attachList = new ArrayList<>();
            HiddenAttach attach =null;
            for (String str : urlArr) {
                attach =  new HiddenAttach();
                attach.setHiddenAttachType("image");
                attach.setHiddenAttachPath(str);
                attach.setCompanyId(Feedback.getCompanyId());
                attach.setHiddenDetailType(1);
                attach.setHiddenDetailId(Feedback.getFeedbackId());
                attachList.add(attach);
            }
            hiddenAttachMapper.addForeach(attachList);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(Feedback Feedback) {
        FeedbackMapper.update(Feedback);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeByIds(List ids) {
        FeedbackMapper.deleteByIds(ids);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeById(Long id) {
        FeedbackMapper.deleteById(id);
    }
}
