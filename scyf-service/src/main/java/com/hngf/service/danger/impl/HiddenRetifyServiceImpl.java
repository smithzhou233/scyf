package com.hngf.service.danger.impl;

import com.hngf.common.enums.BigScreenDataTypeEnum;
import com.hngf.common.utils.Constant;
import com.hngf.entity.danger.Hidden;
import com.hngf.entity.danger.HiddenAccept;
import com.hngf.entity.danger.HiddenRetify;
import com.hngf.entity.danger.HiddenReview;
import com.hngf.mapper.danger.HiddenAcceptMapper;
import com.hngf.mapper.danger.HiddenMapper;
import com.hngf.mapper.danger.HiddenRetifyMapper;
import com.hngf.service.danger.HiddenAttachService;
import com.hngf.service.danger.HiddenRecordService;
import com.hngf.service.danger.HiddenRetifyService;
import com.hngf.service.danger.HiddenReviewService;
import com.hngf.service.danger.HiddenService;
import com.hngf.service.score.ScoreUserService;
import com.hngf.service.sys.MessageService;
import com.hngf.service.sys.SettingService;
import com.hngf.service.utils.SendMessageUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;


@Service("HiddenRetifyService")
public class HiddenRetifyServiceImpl implements HiddenRetifyService {

    @Autowired
    private HiddenRetifyMapper HiddenRetifyMapper;
    @Autowired
    private HiddenMapper hiddenMapper;
    @Autowired
    private HiddenService hiddenService;
    @Autowired
    private HiddenAttachService hiddenAttachService;
    @Autowired
    private HiddenReviewService hiddenReviewService;
    @Autowired
    private HiddenAcceptMapper hiddenAcceptMapper;
    @Autowired
    private ScoreUserService scoreUserService;
    @Autowired
    private HiddenRecordService hiddenRecordService;
    @Autowired
    private SettingService settingService;
    @Autowired
    private MessageService messageService;

    @Override
    public List<HiddenRetify> getByHiddenId(Long hiddenId) {
        return HiddenRetifyMapper.findByHiddenId(hiddenId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void rightAwayRetify(HiddenRetify retify, Integer userType) throws Exception {

        if (null != retify.getHiddenId()) {
            Hidden hidden = hiddenService.getHiddenById(retify.getHiddenId());

            if (hidden != null) {
                retify.setHiddenOldRetifyBy(hidden.getHiddenQuondamRetifyBy());
                HiddenReview review = new HiddenReview();
                review.setCompanyId(retify.getCompanyId());
                review.setGroupId(retify.getHiddenRetifyGroup());
                review.setHiddenId(retify.getHiddenId());
                review.setHiddenReviewBy(retify.getCreatedBy());
                review.setHiddenReviewRemark("评审通过");
                review.setHiddenReviewResult(1);
                review.setHiddenReviewTime(new Date());
                review.setCreatedBy(retify.getCreatedBy());
                review.setCreatedTime(new Date());
                this.hiddenReviewService.save(review, userType);
                this.HiddenRetifyMapper.add(retify);
                HiddenAccept accept = new HiddenAccept();
                accept.setHiddenAcceptedTime(new Date());
                accept.setHiddenAcceptedBy(retify.getCreatedBy());
                accept.setHiddenAcceptedResult(4);
                accept.setHiddenAcceptedDesc("验收通过");
                accept.setCreatedTime(new Date());
                accept.setCreatedBy(retify.getCreatedBy());
                accept.setGroupId(retify.getHiddenRetifyGroup());
                accept.setHiddenId(retify.getHiddenId());
                accept.setCompanyId(retify.getCompanyId());
                this.hiddenAcceptMapper.add(accept);

                //整改附件地址
                String uploadPathStr = retify.getRetifyUploadPath();
                if (StringUtils.isNotEmpty(uploadPathStr)) {
                    this.hiddenAttachService.saveBatch(retify.getCompanyId(), retify.getHiddenRetifyId(), uploadPathStr.split(","),Constant.ATTACH_RETIFY);
                }

                this.scoreUserService.goGrade(retify.getCompanyId(), retify.getHiddenRetifyGroup(), retify.getHiddenRetifyBy(), Constant.SCORE_SETTING_HDANGER_ZGWC_4, retify.getHiddenId());
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveRetify(HiddenRetify hiddenRetify) throws Exception{
        Long hiddenId = hiddenRetify.getHiddenId();
        Long userId = hiddenRetify.getCreatedBy();
        Long companyId = hiddenRetify.getCompanyId();
        if (null != hiddenId) {
            hiddenRetify.setHiddenRetifyDeadline(new Date());
            hiddenRetify.setCreatedTime(new Date());
            hiddenRetify.setHiddenRetifyBy(userId);
            Hidden hidden = this.hiddenService.getHiddenById(hiddenId);
            if (hidden != null) {
                hiddenRetify.setHiddenOldRetifyBy(hidden.getHiddenQuondamRetifyBy());
                //保存整改
                this.HiddenRetifyMapper.add(hiddenRetify);
                hidden.setHiddenRetifyGroup(hiddenRetify.getHiddenRetifyGroup());
                hidden.setHiddenRetifyBy(userId);
                hidden.setStatus(Constant.DANGER_DYS);
                hidden.setUpdatedBy(userId);
                hidden.setUpdatedTime(new Date());
                //更新隐患整改属性和隐患状态
                this.hiddenMapper.update(hidden);
                hidden.setStatus(Constant.HIDDEN_DANGER_YZG);
                hidden.setCreatedBy(userId);
                hidden.setCreatedTime(new Date());
                //保存隐患记录
                this.hiddenRecordService.save(hidden);
                SendMessageUtil.sendMessage(hidden.getCompanyId(), "隐患待验收【" + hidden.getHiddenTitle() + "】", hidden.getHiddenAcceptedBy(), "隐患【" + hidden.getHiddenTitle() + "】待验收!", this.messageService, this.settingService);
                if (null != hidden.getRiskPointId()) {
                    SendMessageUtil.sendBigScreen(hidden.getCompanyId(), hidden.getGroupId(), BigScreenDataTypeEnum.hidden.idType);
                }

                //保存附件
                String uploadPathStr = hiddenRetify.getRetifyUploadPath();
                if (StringUtils.isNotEmpty(uploadPathStr)) {
                    this.hiddenAttachService.saveBatch(companyId, hiddenRetify.getHiddenRetifyId(), uploadPathStr.split(","),Constant.ATTACH_RETIFY);
                }


                Calendar retifyDeadline = Calendar.getInstance();
                retifyDeadline.setTime(hidden.getHiddenRetifyDeadline());
                Date hdangerRetifyDeadline1 = retifyDeadline.getTime();
                Date today = Calendar.getInstance().getTime();
                Integer i = today.compareTo(hdangerRetifyDeadline1);
                if (i == 1) {
                    this.scoreUserService.goGrade(companyId, hidden.getHiddenRetifyGroup(), hidden.getHiddenRetifyBy(), Constant.SCORE_SETTING_HDANGER_ZGCS_2, hidden.getHiddenId());
                } else {
                    this.scoreUserService.goGrade(companyId, hidden.getHiddenRetifyGroup(), hidden.getHiddenRetifyBy(), Constant.SCORE_SETTING_HDANGER_ZGWC_4, hidden.getHiddenId());
                }
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void entrustHiddenRetify(Map<String, Object> params) throws Exception {
        String entrustDesc = (String)params.get("entrustDesc");
        String entrustUserName = (String)params.get("entrustUserName");
        Long userId = Long.parseLong(params.get("userId").toString());
        Long companyId = Long.parseLong(params.get("companyId").toString());
        Long groupId = Long.parseLong(params.get("groupId").toString());
        Long entrustGroup = Long.parseLong(params.get("entrustGroup").toString());
        Long entrustUserId = Long.parseLong(params.get("entrustUserId").toString());
        Long hiddenId = Long.parseLong(params.get("hiddenId").toString());
        Hidden hidden = this.hiddenService.getHiddenById(hiddenId);
        String signUrl = params.get("signUrl")==null ?null:params.get("signUrl").toString();
        if (hidden != null) {
            HiddenRetify retify = new HiddenRetify();
            retify.setHiddenId(hiddenId);
            retify.setHiddenRetifyBy(userId);
            retify.setHiddenRetifyGroup(groupId);
            retify.setCompanyId(companyId);
            retify.setHiddenOldRetifyBy(hidden.getHiddenQuondamRetifyBy());
            retify.setCreatedBy(userId);
            retify.setSignUrl(signUrl);
            if (StringUtils.isNotEmpty(entrustDesc)) {
                retify.setHiddenRetifyMeasures(entrustDesc + "。（已委托给【" + entrustUserName + "】去整改）");
            } else {
                retify.setHiddenRetifyMeasures("已委托给【" + entrustUserName + "】去整改");
            }

            retify.setCreatedTime(new Date());
            //保存委托整改
            this.HiddenRetifyMapper.add(retify);
            hidden.setHiddenRetifyBy(entrustUserId);
            hidden.setHiddenRetifyGroup(entrustGroup);
            hidden.setStatus(Constant.DANGER_DZG);
            hidden.setUpdatedTime(new Date());
            hidden.setUpdatedBy(userId);
            //更新隐患状态
            this.hiddenMapper.update(hidden);
            hidden.setStatus(Constant.HIDDEN_DANGER_YZG);
            hidden.setCreatedBy(userId);
            hidden.setCreatedTime(new Date());
            //保存隐患记录
            this.hiddenRecordService.save(hidden);
            SendMessageUtil.sendMessage(hidden.getCompanyId(), "【 " + hidden.getHiddenTitle() + " 】隐患已委托整改!", entrustUserId, "【" + entrustUserName + "】将隐患委托给您去整改!", this.messageService, this.settingService);
            SendMessageUtil.sendBigScreen(hidden.getCompanyId(), hidden.getGroupId(), BigScreenDataTypeEnum.hidden.idType);
        }
    }

    @Override
    public HiddenRetify getById(Long id){
        return HiddenRetifyMapper.findById(id);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeById(Long id) {
        HiddenRetifyMapper.deleteById(id);
    }
}
