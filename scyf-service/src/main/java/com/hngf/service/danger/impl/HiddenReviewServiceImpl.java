package com.hngf.service.danger.impl;

import com.hngf.common.enums.BigScreenDataTypeEnum;
import com.hngf.common.utils.Constant;
import com.hngf.entity.danger.Hidden;
import com.hngf.entity.danger.HiddenReview;
import com.hngf.mapper.danger.HiddenMapper;
import com.hngf.mapper.danger.HiddenReviewMapper;
import com.hngf.service.danger.HiddenRecordService;
import com.hngf.service.danger.HiddenReviewService;
import com.hngf.service.danger.HiddenService;
import com.hngf.service.risk.RiskPointControlRecordService;
import com.hngf.service.risk.RiskPointService;
import com.hngf.service.score.ScoreUserService;
import com.hngf.service.sys.MessageService;
import com.hngf.service.sys.SettingService;
import com.hngf.service.utils.SendMessageUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;


@Service("HiddenReviewService")
public class HiddenReviewServiceImpl implements HiddenReviewService {

    @Autowired
    private HiddenReviewMapper HiddenReviewMapper;
    @Autowired
    private HiddenMapper hiddenMapper;
    @Autowired
    private HiddenService hiddenService;
    @Autowired
    private HiddenRecordService hiddenRecordService;
    @Autowired
    private RiskPointControlRecordService riskPointControlRecordService;
    @Autowired
    private MessageService messageService;
    @Autowired
    private SettingService settingService;
    @Autowired
    private ScoreUserService scoreUserService;
    @Autowired
    private RiskPointService riskPointService;


    @Override
    public List<HiddenReview> getByHiddenId(Long hiddenId) {
        return HiddenReviewMapper.findByHiddenId(hiddenId);
    }

    @Override
    public HiddenReview getById(Long id){
        return HiddenReviewMapper.findById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(HiddenReview hiddenReview, Integer userType) throws Exception{

        Long hiddenId = hiddenReview.getHiddenId();
        Long programmeId = hiddenReview.getProgrammeId();
        Long hiddenAcceptedGroup = hiddenReview.getHiddenAcceptedGroup();
        Long hiddenAcceptedBy = hiddenReview.getHiddenAcceptedBy();
        Long hiddenReviewGroup = 0L;
        Long hiddenReviewBy = 0L;
       
        //评审结构
        Integer hiddenReviewResult = hiddenReview.getHiddenReviewResult();
        if (null!=hiddenReviewResult &&hiddenReviewResult.intValue() == 1) {
            hiddenReviewGroup = hiddenReview.getHiddenReviewGroup();
            hiddenReviewBy = hiddenReview.getHiddenReviewBy();
        }

        Long userId = hiddenReview.getCreatedBy();
        Long companyId = hiddenReview.getCompanyId();
        Long groupId = hiddenReview.getGroupId();

        String hiddenReviewRemark = hiddenReview.getHiddenReviewRemark();

        if (null != hiddenId) {
            Hidden hd = this.hiddenService.getHiddenById(hiddenId);
            if (hd != null) {
                hiddenReview.setHiddenReviewTime(new Date());
                hiddenReview.setHiddenReviewBy(userId);
                hiddenReview.setHiddenReviewRemark(hiddenReviewRemark);
                hiddenReview.setHiddenReviewResult(hiddenReviewResult);
                hiddenReview.setCreatedTime(new Date());
                hiddenReview.setCreatedBy(userId);
                hiddenReview.setCompanyId(companyId);
                hiddenReview.setGroupId(groupId);
                hiddenReview.setHiddenId(hiddenId);

                this.HiddenReviewMapper.add(hiddenReview);
                hd.setHiddenRetifyDeadline(hiddenReview.getHiddenRetifyDeadline());
                hd.setHiddenLevel(hiddenReview.getHiddenLevel());
                hd.setUpdatedTime(new Date());
                hd.setUpdatedBy(userId);
                if (this.hiddenService.checkedHiddenReview(hiddenReview.getCompanyId(),  userType)) {
                    hd.setHiddenRetifyBy(hiddenReview.getHiddenRetifyBy());
                    hd.setHiddenRetifyGroup(hiddenReview.getHiddenRetifyGroup());
                    if (null == hiddenAcceptedBy || hiddenAcceptedBy == 0) {
                        hiddenAcceptedBy = hiddenReview.getHiddenRetifyBy();
                    }

                    if (null == hiddenAcceptedGroup || hiddenAcceptedGroup == 0) {
                        hiddenAcceptedGroup = hiddenReview.getHiddenRetifyGroup();
                    }

                    hd.setHiddenAcceptedBy(hiddenAcceptedBy);
                    hd.setHiddenAcceptedGroup(hiddenAcceptedGroup);
                }

                if (Constant.REVIEW_YES.intValue() == hiddenReview.getHiddenReviewResult().intValue()) {
                    hd.setStatus(Constant.DANGER_DZG);
                    hd.setHappenedTime(new Date());
                    SendMessageUtil.sendMessage(hd.getCompanyId(), "隐患待整改【" + hd.getHiddenTitle() + "】", hd.getHiddenRetifyBy(), "隐患【" + hd.getHiddenTitle() + "】待整改!", this.messageService, this.settingService);
                } else {
                    hd.setStatus(Constant.DANGER_YCX);
                    SendMessageUtil.sendMessage(hd.getCompanyId(), "隐患评审未通过【 " + hd.getHiddenTitle() + "】", hd.getCreatedBy(), "隐患【" + hd.getHiddenTitle() + "】评审未通过", this.messageService, this.settingService);
                }

                hd.setProgrammeId(programmeId);
                this.hiddenMapper.update(hd);
                hd.setStatus(Constant.HIDDEN_DANGER_YPS);
                hd.setCreatedBy(userId);
                hd.setCreatedTime(new Date());
                //关联保存隐患表记录
                this.hiddenRecordService.save(hd);
                if (Constant.REVIEW_YES.intValue() == hiddenReview.getHiddenReviewResult().intValue() && null != hd.getRiskPointId()) {
                    //保存风险点管控实时告警记录
                    this.riskPointControlRecordService.insertRiskPointControlRecord(hd);
                    if (hd.getStatus().intValue() == Constant.DANGER_DZG.intValue()) {
                        this.riskPointService.updateRiskPointHiddenCount(hd.getRiskPointId(), 1);
                    }
                }

                SendMessageUtil.sendBigScreen(companyId, groupId, BigScreenDataTypeEnum.hidden.idType);
                //保持评分等级
                this.scoreUserService.goGrade(companyId, groupId,hiddenReview.getCreatedBy() , Constant.SCORE_SETTING_HDANGER_PSWC_6, hiddenId);
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void entrustHiddenReview(Map<String, Object> params) throws Exception {
        String entrustDesc = (String)params.get("entrustDesc");
        String entrustUserName = (String)params.get("entrustUserName");
        Long userId = Long.parseLong(params.get("userId").toString());
        Long companyId = Long.parseLong(params.get("companyId").toString());
        Long groupId = Long.parseLong(params.get("groupId").toString());
        Long entrustGroup = Long.parseLong(params.get("entrustGroup").toString());
        Long entrustUserId = Long.parseLong(params.get("entrustUserId").toString());
        Long hiddenId = Long.parseLong(params.get("hiddenId").toString());
        String signUrl = params.get("signUrl")!=null ? params.get("signUrl").toString():null;
        Hidden hidden = this.hiddenService.getHiddenById(hiddenId);
        if (hidden != null) {
            HiddenReview review = new HiddenReview();
            review.setCompanyId(companyId);
            review.setGroupId(groupId);
            review.setHiddenId(hiddenId);
            review.setHiddenReviewBy(userId);
            review.setSignUrl(signUrl);
            if (StringUtils.isNotEmpty(entrustDesc)) {
                review.setHiddenReviewRemark(entrustDesc + "。已委托给【" + entrustUserName + "】去评审");
            } else {
                review.setHiddenReviewRemark("已委托给【" + entrustUserName + "】去评审");
            }

            review.setHiddenReviewResult(2);
            review.setHiddenReviewTime(new Date());
            review.setCreatedBy(userId);
            review.setCreatedTime(new Date());
            this.HiddenReviewMapper.add(review);
            hidden.setHiddenReviewBy(entrustUserId);
            hidden.setHiddenReviewGroup(entrustGroup);
            hidden.setStatus(Constant.DANGER_DPS);
            hidden.setUpdatedTime(new Date());
            hidden.setUpdatedBy(userId);
            this.hiddenMapper.update(hidden);
            hidden.setStatus(Constant.HIDDEN_DANGER_YPS);
            hidden.setCreatedBy(userId);
            hidden.setCreatedTime(new Date());
            this.hiddenRecordService.save(hidden);
            SendMessageUtil.sendMessage(hidden.getCompanyId(), "【 " + hidden.getHiddenTitle() + " 】隐患已委托评审!", entrustUserId, "【" + entrustUserName + "】将隐患委托给您去评审!", this.messageService, this.settingService);
            SendMessageUtil.sendBigScreen(hidden.getCompanyId(), hidden.getGroupId(), BigScreenDataTypeEnum.hidden.idType);
        }

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(HiddenReview HiddenReview) {
        HiddenReviewMapper.update(HiddenReview);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeByIds(List ids) {
        HiddenReviewMapper.deleteByIds(ids);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeById(Long id) {
        HiddenReviewMapper.deleteById(id);
    }
}
