package com.hngf.service.danger.impl;

import com.hngf.common.enums.BigScreenDataTypeEnum;
import com.hngf.entity.danger.Hidden;
import com.hngf.mapper.danger.HiddenMapper;
import com.hngf.service.danger.HiddenAttachService;
import com.hngf.service.danger.HiddenRecordService;
import com.hngf.service.danger.HiddenService;
import com.hngf.service.risk.RiskPointControlRecordService;
import com.hngf.service.risk.RiskPointService;
import com.hngf.service.score.ScoreUserService;
import com.hngf.service.sys.MessageService;
import com.hngf.service.sys.SettingService;
import com.hngf.service.utils.SendMessageUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Map;
import java.util.List;
import com.hngf.common.utils.Constant;

import com.hngf.mapper.danger.HiddenAcceptMapper;
import com.hngf.entity.danger.HiddenAccept;
import com.hngf.service.danger.HiddenAcceptService;


@Service("HiddenAcceptService")
public class HiddenAcceptServiceImpl implements HiddenAcceptService {

    @Autowired
    private HiddenAcceptMapper HiddenAcceptMapper;
    @Autowired
    private HiddenMapper hiddenMapper;
    @Autowired
    private HiddenService hiddenService;
    @Autowired
    private HiddenRecordService hiddenRecordService;
    @Autowired
    private HiddenAttachService hiddenAttachService;
    @Autowired
    private RiskPointService riskPointService;
    @Autowired
    private ScoreUserService scoreUserService;
    @Autowired
    private RiskPointControlRecordService riskPointControlRecordService;
    @Autowired
    private SettingService settingService;
    @Autowired
    private MessageService messageService;

    @Override
    public List<HiddenAccept> getByHiddenId(Long hiddenId) {
        return HiddenAcceptMapper.findByHiddenId(hiddenId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveAccept(HiddenAccept accept) throws Exception{
        Long hiddenId = accept.getHiddenId();
        Long userId = accept.getCreatedBy();
        if (null != hiddenId) {
            accept.setHiddenAcceptedTime(new Date());
            accept.setHiddenAcceptedBy(userId);
            accept.setHiddenId(hiddenId);
            Hidden hd = this.hiddenService.getHiddenById(accept.getHiddenId());
            if (hd != null) {
                accept.setCompanyId(hd.getCompanyId());
                //保存验收数据
                this.HiddenAcceptMapper.add(accept);
                //保存附件
                String uploadPathStr = accept.getHiddenAttachPath();
                if (StringUtils.isNotEmpty(uploadPathStr)) {
                    this.hiddenAttachService.saveBatch(accept.getCompanyId(), accept.getHiddenAcceptId(), uploadPathStr.split(","),Constant.ATTACH_ACCEPT);
                }

                if (Constant.DANGER_YSTG.intValue() == accept.getHiddenAcceptedResult().intValue()) {
                    hd.setFinishedTime(new Date());
                    SendMessageUtil.sendMessage(hd.getCompanyId(), "隐患验收通过【" + hd.getHiddenTitle() + "】", hd.getCreatedBy(), "隐患【" + hd.getHiddenTitle() + "】已通过验收! 流程已闭环！", this.messageService, this.settingService);
                } else {
                    SendMessageUtil.sendMessage(hd.getCompanyId(), "隐患验收不通过【" + hd.getHiddenTitle() + "】", hd.getHiddenRetifyBy(), "隐患【" + hd.getHiddenTitle() + "】验收未通过，请重新整改! ", this.messageService, this.settingService);
                }
                //设置隐患表中的验收人，验收部门字段值
                hd.setHiddenAcceptedBy(accept.getHiddenAcceptedBy());
                hd.setHiddenAcceptedGroup(accept.getGroupId());
                hd.setStatus(accept.getHiddenAcceptedResult());
                hd.setUpdatedTime(new Date());
                hd.setUpdatedBy(userId);
                //更新隐患表
                this.hiddenMapper.update(hd);
                hd.setStatus(Constant.HIDDEN_DANGER_YYS);
                hd.setCreatedBy(userId);
                hd.setCreatedTime(new Date());
                //保存隐患记录
                this.hiddenRecordService.save(hd);
                if (Constant.DANGER_YSTG.intValue() == accept.getHiddenAcceptedResult().intValue() && null != hd.getRiskPointId()) {
                    this.riskPointControlRecordService.insertRiskPointControlRecord(hd);
                    this.riskPointService.updateRiskPointHiddenCount(hd.getRiskPointId(), 0);
                }

                SendMessageUtil.sendBigScreen(hd.getCompanyId(), hd.getGroupId(), BigScreenDataTypeEnum.hidden.idType);
                this.scoreUserService.goGrade(hd.getCompanyId(), hd.getHiddenAcceptedGroup(), hd.getHiddenAcceptedBy(), Constant.SCORE_SETTING_HDANGER_YSWC_5, hd.getHiddenId());
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void entrustHiddenAccept(Map<String, Object> params) throws Exception {
        String entrustDesc = (String)params.get("entrustDesc");
        String entrustUserName = (String)params.get("entrustUserName");
        Long userId = Long.parseLong(params.get("userId").toString());
        Long companyId = Long.parseLong(params.get("companyId").toString());
        Long groupId = Long.parseLong(params.get("groupId").toString());
        Long entrustGroup = Long.parseLong(params.get("entrustGroup").toString());
        Long entrustUserId = Long.parseLong(params.get("entrustUserId").toString());
        Long hiddenId = Long.parseLong(params.get("hiddenId").toString());
        Hidden hidden = this.hiddenService.getHiddenById(hiddenId);
        String signUrl = params.get("signUrl")==null?null:params.get("signUrl").toString();
        if (hidden != null) {
            HiddenAccept accept = new HiddenAccept();
            accept.setHiddenId(hiddenId);
            accept.setCompanyId(companyId);
            accept.setGroupId(groupId);
            accept.setHiddenAcceptedBy(userId);
            accept.setHiddenAcceptedResult(3);
            accept.setSignUrl(signUrl);
            if (StringUtils.isNotEmpty(entrustDesc)) {
                accept.setHiddenAcceptedDesc(entrustDesc + "。（已委托给【" + entrustUserName + "】去验收）");
            } else {
                accept.setHiddenAcceptedDesc("已委托给【" + entrustUserName + "】去验收");
            }

            accept.setCreatedBy(userId);
            accept.setCreatedTime(new Date());
            accept.setHiddenAcceptedTime(new Date());
            //保存委托验收
            this.HiddenAcceptMapper.add(accept);
            hidden.setHiddenAcceptedBy(entrustUserId);
            hidden.setHiddenAcceptedGroup(entrustGroup);
            hidden.setStatus(Constant.DANGER_DYS);
            hidden.setUpdatedBy(userId);
            hidden.setUpdatedTime(new Date());
            //更新隐患状态
            this.hiddenMapper.update(hidden);
            hidden.setStatus(Constant.HIDDEN_DANGER_YYS);
            hidden.setCreatedBy(userId);
            hidden.setCreatedTime(new Date());
            //保存隐患记录
            this.hiddenRecordService.save(hidden);
            SendMessageUtil.sendMessage(hidden.getCompanyId(), "【 " + hidden.getHiddenTitle() + " 】隐患已委托验收!", entrustUserId, "【" + entrustUserName + "】将隐患委托给您去验收!", this.messageService, this.settingService);
            SendMessageUtil.sendBigScreen(hidden.getCompanyId(), hidden.getGroupId(), BigScreenDataTypeEnum.hidden.idType);
        }
    }

    @Override
    public HiddenAccept getById(Long id){
        return HiddenAcceptMapper.findById(id);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeById(Long id) {
        HiddenAcceptMapper.deleteById(id);
    }
}
