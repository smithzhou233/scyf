package com.hngf.service.danger.impl;

import cn.hutool.core.date.DateUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hngf.common.enums.BigScreenDataTypeEnum;
import com.hngf.common.exception.ScyfException;
import com.hngf.common.utils.Constant;
import com.hngf.common.utils.PageUtils;
import com.hngf.common.utils.ParamUtils;
import com.hngf.entity.danger.Feedback;
import com.hngf.entity.danger.Hidden;
import com.hngf.entity.danger.HiddenRetify;
import com.hngf.entity.danger.InspectItemDef;
import com.hngf.entity.danger.InspectSchdule;
import com.hngf.entity.risk.RiskInspectRecord;
import com.hngf.entity.risk.RiskInspectRecordLog;
import com.hngf.entity.risk.RiskMeasure;
import com.hngf.entity.risk.RiskPoint;
import com.hngf.entity.sys.Company;
import com.hngf.entity.sys.Info;
import com.hngf.mapper.danger.HiddenAttachMapper;
import com.hngf.mapper.danger.HiddenMapper;
import com.hngf.mapper.sys.CompanyMapper;
import com.hngf.service.danger.HiddenAttachService;
import com.hngf.service.danger.HiddenRecordService;
import com.hngf.service.danger.HiddenRetifyService;
import com.hngf.service.danger.HiddenService;
import com.hngf.service.danger.InspectItemDefService;
import com.hngf.service.danger.InspectSchduleService;
import com.hngf.service.risk.RiskInspectRecordLogService;
import com.hngf.service.risk.RiskInspectRecordService;
import com.hngf.service.risk.RiskMeasureService;
import com.hngf.service.risk.RiskPointControlRecordService;
import com.hngf.service.risk.RiskPointService;
import com.hngf.service.sys.CompanyService;
import com.hngf.service.sys.DictService;
import com.hngf.service.sys.InfoService;
import com.hngf.service.sys.MessageService;
import com.hngf.service.sys.SettingService;
import com.hngf.service.utils.SendMessageUtil;
import com.hngf.service.utils.jpush.JPushKit;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


/**
 * 隐患管理
 * @author zhangfei
 * @date 2020-06-10
 */
@Service("HiddenService")
public class HiddenServiceImpl implements HiddenService {

    @Autowired
    private HiddenMapper HiddenMapper;
    @Autowired
    private InfoService infoService;
    @Autowired
    private DictService dictService;
    @Autowired
    private InspectSchduleService inspectSchduleService;
    @Autowired
    private RiskInspectRecordLogService riskInspectRecordLogService;
    @Autowired
    private RiskInspectRecordService riskInspectRecordService;
    @Autowired
    private RiskPointService riskPointService;
    @Autowired
    private HiddenAttachService hiddenAttachService;
    @Autowired
    private RiskMeasureService riskMeasureService;
    @Autowired
    private InspectItemDefService inspectItemDefService;
    @Autowired
    private HiddenRetifyService hiddenRetifyService;
    @Autowired
    private RiskPointControlRecordService riskPointControlRecordService;
    @Autowired
    private SettingService settingService;
    @Autowired
    private MessageService messageService;
    @Autowired
    private HiddenRecordService hiddenRecordService;
    @Autowired
    private HiddenAttachMapper hiddenAttachMapper;
    @Autowired
    private com.hngf.mapper.danger.FeedbackMapper FeedbackMapper;
    @Autowired
    private CompanyService companyService;
    @Autowired
    private CompanyMapper companyMapper;

    @Override
    public PageUtils queryPage(Map<String, Object> params,Integer pageNum,Integer pageSize,String order) {
        if(StringUtils.isNotBlank(order)){
            PageHelper.startPage(pageNum, pageSize,order);
        }else{
            PageHelper.startPage(pageNum, pageSize);
        }
        List<Hidden> list = HiddenMapper.findGroupHidden(params);
        PageInfo<Hidden> pageInfo = new PageInfo<Hidden>(list);
        return new PageUtils(list,(int)pageInfo.getTotal(),pageInfo.getPages(),pageInfo.getPageSize(),pageInfo.getPrePage() + 1);
    }

    @Override
    public PageUtils queryMyTodoPage(Map<String, Object> params, Integer pageNum, Integer pageSize, String order) {
        if(StringUtils.isNotBlank(order)){
            PageHelper.startPage(pageNum, pageSize,order);
        }else{
            PageHelper.startPage(pageNum, pageSize);
        }
        List<Hidden> list = null;
        if (null!= params.get("module") && params.get("module").toString().equals("1")) { //api 我的
            String type = params.get("type").toString();
            if (type.equals("1")) {
                list = this.HiddenMapper.queryAlreadyHDangerRecordByPage(params);
            } else if (type.equals("0")) {
                list = this.HiddenMapper.findGroupHidden(params);
            }
        }else{
            list = this.HiddenMapper.findGroupHidden(params);
           /* for(int i=0;i<list.size();i++){
                Map<String, Object> attachMap  = new HashMap<>();
                attachMap.put("detailId",list.get(i).getHiddenId());
                attachMap.put("hiddenDetailType",1);  //1 隐患 2.验收
                List<HiddenAttach>  halist = hiddenAttachService.findByDetailId(attachMap);
                if(null!=halist && halist.size()>0){
                    list.get(i).setHiddenAttachPath(halist.get(0).getHiddenAttachPath());
                }
            }*/
        }
        PageInfo<Hidden> pageInfo = new PageInfo<Hidden>(list);
        return new PageUtils(list,(int)pageInfo.getTotal(),pageInfo.getPages(),pageInfo.getPageSize(),pageInfo.getPrePage() + 1);
    }

    @Override
    public PageUtils queryRiskPointHDanger(Map<String, Object> params,Integer pageNum,Integer pageSize,String order) {
        //利用PageHelper分页查询 注意：这个一定要放查询语句的前一行,否则无法进行分页,因为它对紧随其后第一个sql语句有效
        if(StringUtils.isNotBlank(order)){
            PageHelper.startPage(pageNum, pageSize,order);
        }else{
            PageHelper.startPage(pageNum, pageSize);
        }
        List<Hidden> list = HiddenMapper.findGroupHidden(params);
        PageInfo<Hidden> pageInfo = new PageInfo<Hidden>(list);
        return new PageUtils(list,(int)pageInfo.getTotal(),pageInfo.getPages(),pageInfo.getPageSize(),pageInfo.getPrePage() + 1);
    }

    @Override
    public List<Hidden> getGroupHidden(Map<String, Object> params) {
        return HiddenMapper.findGroupHidden(params);
    }

    @Override
    public List<Hidden> getByCompanyId(Long companyId) {
        return HiddenMapper.findByCompanyId(companyId);
    }

    @Override
    public Hidden getById(Long id){
        return HiddenMapper.findById(id);
    }

    @Override
    public Hidden getHiddenById(Long hiddenId) {
        return HiddenMapper.findHiddenById(hiddenId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(Hidden hidden, Integer userType) throws Exception {

        //检查总次数
        Integer checkNumber = 1;

        Long userId = hidden.getCreatedBy();
        //部组ID
        Long groupId = hidden.getGroupId();
        //检查任务ID
        Long scheduleId = hidden.getInspectScheduleId();
        Long companyId = hidden.getCompanyId();

        //isScheduleCheck == 1,进行任务检查
        if (null != hidden.getIsScheduleCheck() && hidden.getIsScheduleCheck().intValue() == 1) {
            InspectSchdule inspectSchdule = inspectSchduleService.getById(scheduleId);
            if (null != inspectSchdule) {
                checkNumber = inspectSchdule.getInspectTotalCount();

                //任务开始执行时间如果为空，更新为当前时间
                if (null == inspectSchdule.getExecutorDate()) {
                    inspectSchdule.setExecutorDate(new Date());
                    inspectSchduleService.update(inspectSchdule);
                }
            }
        }

        Long newCheckedRecordId = null;
        //根据隐患表中的inspectRecordId查询检查记录，若不为空，将
        RiskInspectRecordLog record_ = riskInspectRecordLogService.getById(hidden.getInspectRecordLogId());
        if (null != record_) {
            newCheckedRecordId = hidden.getInspectRecordId();
        }else{
            newCheckedRecordId = dictService.getTabId("scyf_risk_inspect_record");
        }

        //生成隐患ID
        Long hiddenId = dictService.getTabId("scyf_hidden");


        hidden.setInspectRecordId(newCheckedRecordId);
        hidden.setHiddenId(hiddenId);
        hidden.setHiddenQuondamRetifyBy(hidden.getHiddenRetifyBy());
        RiskPoint riskPoint = riskPointService.getById(hidden.getRiskPointId());
        if (riskPoint != null) {
            hidden.setGroupId(riskPoint.getDutyGroupId());
        }

        if (null != hidden.getStatus() && hidden.getStatus().intValue() == 1) {
            if (checkedHiddenReview(companyId,userType)) {
                hidden.setStatus(Constant.DANGER_DPS);
            } else {
                hidden.setStatus(Constant.DANGER_DZG);
                hidden.setHappenedTime(new Date());
            }
        }

        //保存隐患附件信息
        if (null != hidden.getHiddenAttachPath() && hidden.getHiddenAttachPath().length() > 0) {
            this.hiddenAttachService.saveBatch(companyId,hiddenId,hidden.getHiddenAttachPath().split(","),Constant.ATTACH_HDANGER);
        }

        this.checkIsHiddenReview(hidden,userType);
        String inspectDefType = hidden.getInspectDefType();
        String riskMeasureContent = "未知的检查项内容（管控措施），可能被修改或者已删除";
        long itemDetailId = 0L;
        if ("0".equals(inspectDefType)) {
            itemDetailId = hidden.getItemDetailId();
            RiskMeasure riskMeasure = this.riskMeasureService.getById(hidden.getInspectContentId());
            if (null != riskMeasure) {
                riskMeasureContent = riskMeasure.getRiskMeasureContent();
            }
        } else if ("1".equals(inspectDefType)) {
            InspectItemDef inspectItemDef = this.inspectItemDefService.getById(hidden.getInspectContentId());
            if (null != inspectItemDef) {
                riskMeasureContent = inspectItemDef.getInspectItemDefDesc();
                itemDetailId = inspectItemDef.getParentId();
            }
        } else if ("2".equals(inspectDefType)) {
            riskMeasureContent = "直接登记隐患，不通过安全检查表检查！";
        }

        hidden.setInspectContentDesc(riskMeasureContent);
        //设置整改相关字段的值
        if (hidden.getIsRetify().intValue() == 1) {
            hidden.setHiddenRetifyDeadline(new Date());
            hidden.setHiddenReviewBy(userId);
            hidden.setHiddenRetifyGroup(groupId);
            hidden.setHiddenRetifyBy(userId);
            hidden.setHiddenRetifyGroup(groupId);
            hidden.setHiddenAcceptedBy(userId);
            hidden.setHiddenAcceptedGroup(groupId);
            hidden.setStatus(Constant.DANGER_YSTG);
        }

        this.HiddenMapper.add(hidden);

        //如果检查记录日志不为空，更新相关字段
        //为空时，新建一条检查记录
        if (record_ != null) {
            record_.setRiskMeasureContent(riskMeasureContent);
            record_.setInspectResult(Constant.CHECK_RESULT_CZYH);
            record_.setInspectNumber(checkNumber);
            record_.setRemark(hidden.getHiddenDesc());
            record_.setSpotData(hidden.getSpotData());
            record_.setUpdatedBy(userId);
            record_.setUpdatedTime(new Date());
            this.riskInspectRecordLogService.update(record_);
        } else {
            RiskInspectRecord record = new RiskInspectRecord();
            record.setInspectRecordId(newCheckedRecordId);
            record.setInspectRecordNo(hidden.getInspectRecordNo());
            record.setInspectResult(Constant.CHECK_RESULT_CZYH);
            record.setInspectNumber(checkNumber);
            record.setCompanyId(companyId);
            record.setGroupId(hidden.getGroupId());
            record.setRiskPointId(hidden.getRiskPointId());
            record.setRiskMeasureContent(riskMeasureContent);
            record.setSpotData(hidden.getSpotData());
            record.setRemark(hidden.getHiddenDesc());
            record.setItemDetailId(itemDetailId);
            record.setRiskMeasureId(hidden.getInspectContentId());
            record.setSchduleDefId(hidden.getInspectDefId());
            record.setInspectScheduleId(scheduleId);
            record.setInspectItemDefId(hidden.getInspectItemId());
            record.setCreatedBy(userId);
            record.setCreatedTime(hidden.getCreatedTime());
            this.riskInspectRecordService.save(record);
        }

        //如果isRetify==1，立即进行一次整改
        if (hidden.getIsRetify().intValue() == 1) {
            HiddenRetify retify = new HiddenRetify();

            retify.setHiddenRetifyReasons(hidden.getHiddenRetifyReasons());
            retify.setHiddenRetifyMeasures(hidden.getHiddenRetifyMeasures());
            retify.setHiddenRetifyType(hidden.getHiddenRetifyType());
            retify.setHiddenRetifyAssisting(hidden.getHiddenRetifyAssisting());
            retify.setHiddenRetifyAmount(hidden.getHiddenRetifyAmount());
            retify.setHiddenRetifyDeadline(new Date());
            retify.setCreatedTime(new Date());
            retify.setHiddenId(hiddenId);
            retify.setCreatedBy(userId);
            retify.setHiddenRetifyBy(userId);
            retify.setCompanyId(companyId);
            retify.setHiddenRetifyGroup(groupId);

            this.hiddenRetifyService.rightAwayRetify(retify, userType);
        } else {
            //消息推送/风险点数量更新
            this.propellingMovement(hidden,userType);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(Hidden hidden , Integer userType) throws Exception{
        this.checkIsHiddenReview(hidden, userType);

        int recordStatus = 0;
        this.setHiddenStatus(hidden,userType);
        //如果隐患附件不为空，保存附件路径
        String hiddenAttachPath = hidden.getHiddenAttachPath();
        if (StringUtils.isNotEmpty(hiddenAttachPath)) {
            //先删除之前的所有附件
            this.hiddenAttachService.removeByDetailId(hidden.getCompanyId(),hidden.getHiddenId(),Constant.ATTACH_HDANGER);
            this.hiddenAttachService.saveBatch(hidden.getCompanyId(),hidden.getHiddenId(),hiddenAttachPath.split(","),Constant.ATTACH_HDANGER);
        }

        this.HiddenMapper.update(hidden);
        if (Constant.HIDDEN_DANGER_YTJ == recordStatus) {
            hidden.setStatus(recordStatus);
            //将当前隐患数据，保存到scyf_hidden_record-隐患记录表中
            hidden.setCreatedTime(new Date());
            hidden.setCreatedBy(hidden.getUpdatedBy());
            this.hiddenRecordService.save(hidden);
        }

        this.propellingMovement(hidden,userType);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int cancel(Long id,Long userId) throws Exception {

        Hidden hidden = this.getHiddenById(id);
        if (hidden != null) {
            hidden.setStatus(Constant.DANGER_YCX);
            hidden.setFinishedTime(new Date());
            hidden.setUpdatedTime(new Date());
            hidden.setUpdatedBy(userId);
            //更新隐患状态
            this.HiddenMapper.update(hidden);
            hidden.setStatus(Constant.HIDDEN_DANGER_YCX);
            hidden.setCreatedTime(new Date());
            hidden.setCreatedBy(userId);
            //保存隐患记录
            this.hiddenRecordService.save(hidden);
            if (null != hidden.getRiskPointId()) {
                hidden.setStatus(Constant.DANGER_YCX);
                this.riskPointControlRecordService.insertRiskPointControlRecord(hidden);
                this.riskPointService.updateRiskPointHiddenCount(hidden.getRiskPointId(), 0);
            }

            SendMessageUtil.sendBigScreen(hidden.getCompanyId(), hidden.getGroupId(), BigScreenDataTypeEnum.hidden.idType);
            return 1;
        }

        return 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int removeById(Long id,Long userId) throws Exception{
        Hidden hidden = this.getHiddenById(id);
        if (hidden != null) {
            hidden.setStatus(Constant.DANGER_DELETE);
            hidden.setFinishedTime(new Date());
            hidden.setUpdatedTime(new Date());
            hidden.setUpdatedBy(userId);
            this.HiddenMapper.update(hidden);
            if (null != hidden.getRiskPointId()) {
                hidden.setStatus(Constant.DANGER_DELETE);
                hidden.setCreatedBy(userId);
                hidden.setCreatedTime(new Date());
                this.riskPointControlRecordService.insertRiskPointControlRecord(hidden);
                this.riskPointService.updateRiskPointHiddenCount(hidden.getRiskPointId(), 0);
            }

            SendMessageUtil.sendBigScreen(hidden.getCompanyId(), hidden.getGroupId(), BigScreenDataTypeEnum.hidden.idType);
            return 1;
        }
        return 0;
    }

    @Override
    public int permanentRemoveById(Long id) {
        return HiddenMapper.deleteById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteAll(Long companyId,Long userId){
        List<Hidden> list = this.getByCompanyId(companyId);
        if (!list.isEmpty()) {
            int del = 0;
            Iterator<Hidden> it = list.iterator();
            Hidden hd = null;
            while (it.hasNext()) {
                hd = it.next();
                if (null != hd) {
                    try {
                        del += this.removeById(hd.getHiddenId(), userId);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            return del;
        }
        return 0;
    }

    @Override
    public Boolean checkedHiddenReview(Long companyId ,Integer userType) {
        Info info = infoService.getByCId(companyId, userType);
        if (null != info && null != info.getHdangerreviewOn()) {
            return info.getHdangerreviewOn().intValue() == Constant.IS_HDANGER_REVIEW_YES;
        }
        return false;
    }

    @Override
    public String saveFeedbackHiddenDanger(Hidden hidden, Integer userType) throws Exception {
        try {
            Feedback feedback = this.FeedbackMapper.findById(hidden.getFeedbackId());
            if (feedback != null && null != feedback.getGroupId()) {
                if (feedback.getResultValue() == 1) {
                    return "反馈已处理,请勿重复处理";
                } else {
                    hidden.setGroupId(feedback.getGroupId());
                    //生成隐患ID
                    Long hiddenId = dictService.getTabId("scyf_hidden");
                    hidden.setHiddenId(hiddenId);
                    HiddenMapper.add(hidden);
                    Feedback feedbackVO = new Feedback();
                    feedbackVO.setFeedbackId(hidden.getFeedbackId());
                    feedbackVO.setResultValue(1);
                    feedbackVO.setResultDesc("已处理，已下发为隐患；");
                    feedbackVO.setUpdatedBy(hidden.getCreatedBy());
                    feedbackVO.setUpdatedTime(DateUtil.date());
                    this.FeedbackMapper.update(feedbackVO);
                    this.propellingMovement(hidden,userType);
                    return "";
                }
            } else {
                return "反馈不存在";
            }
        } catch (Exception var4) {
            var4.printStackTrace();
            throw new Exception();
        }
    }

    @Override
    public Integer findHomePageHiddenOverdueCount(Map<String, Object> params) {
        return HiddenMapper.findHomePageHiddenOverdueCount(params);
    }

    /**
     * 查询隐患台账清单整改台账
     * yfh
     * 2020/06/28
     * @param dataMap
     * @param pageNum
     * @param pageSize
     * @param order
     * @return
     */
    @Override
    public PageUtils queryRectifyStandingBookByPage(Map<String, Object> dataMap, int pageNum, int pageSize, String order) {
        //利用PageHelper分页查询 注意：这个一定要放查询语句的前一行,否则无法进行分页,因为它对紧随其后第一个sql语句有效
        if(StringUtils.isNotBlank(order)){
            PageHelper.startPage(pageNum, pageSize,order);
        }else{
            PageHelper.startPage(pageNum, pageSize);
        }
        List<Map<String,Object>> list = HiddenMapper.queryRectifyStandingBookByPage(dataMap);
        PageInfo pageInfo = new PageInfo(list);
        return new PageUtils(list,(int)pageInfo.getTotal(),pageInfo.getPages(),pageInfo.getPageSize(),pageInfo.getPrePage() + 1);
    }
    /**
     * 查询隐患台账清单排查清单
     * yfh
     * 2020/06/29
     * @param dataMap
     * @return
     */
    @Override
    public List<Map<String, Object>> queryHiddenCheckedListByPage(Map<String, Object> dataMap) {

        return HiddenMapper.queryHiddenCheckedListByPage(dataMap);
    }

    @Override
    public List<Map<String, Object>> hiddenCount(Map<String, Object> params) {
        return HiddenMapper.hiddenCount(params);
    }

    @Override
    public List<Map<String, Object>> hiddenBulletin(Map<String, Object> params) {
        return HiddenMapper.hiddenBulletin(params);
    }

    @Override
    public List<Map<String, Object>> getHiddenListForBigScreen(Map<String, Object> params) {
        List<Map<String, Object>> data = this.HiddenMapper.findHiddenListForBigScreen(params);
        data.forEach((item) -> {
            Long id = Long.parseLong(item.get("hiddenId").toString());
            item.put("beforeAnnex", this.hiddenAttachMapper.findBeforeHiddenAttach(id));
            item.put("afterAnnex", this.hiddenAttachMapper.findAfterHiddenAttach(id));
        });
        return data;
    }

    @Override
    public PageUtils queryRiskPointHiddenListForGent(Map<String, Object> params, int pageNum, int pageSize, String order) {

        Long companyId = ParamUtils.paramsToLong(params, "companyId");
        Company company = companyService.getCompanyId(companyId);
        if (null == company || null == company.getCompanyId()) {
            throw new ScyfException("选择的集团、企业不存在，companyId= "+companyId);
        }

        //利用PageHelper分页查询 注意：这个一定要放查询语句的前一行,否则无法进行分页,因为它对紧随其后第一个sql语句有效
        if(StringUtils.isNotBlank(order)){
            PageHelper.startPage(pageNum, pageSize,order);
        }else{
            PageHelper.startPage(pageNum, pageSize);
        }

        List<Hidden> list = HiddenMapper.findRiskPointHiddenListForGent(params);
        PageInfo pageInfo = new PageInfo(list);
        return new PageUtils(list,(int)pageInfo.getTotal(),pageInfo.getPages(),pageInfo.getPageSize(),pageInfo.getPrePage() + 1);
    }

    @Override
    public PageUtils queryHiddenListByCompanyId(Map<String, Object> params, int pageNum, int pageSize, String order) {
        //利用PageHelper分页查询 注意：这个一定要放查询语句的前一行,否则无法进行分页,因为它对紧随其后第一个sql语句有效
        if(StringUtils.isNotBlank(order)){
            PageHelper.startPage(pageNum, pageSize,order);
        }else{
            PageHelper.startPage(pageNum, pageSize);
        }
        List<Hidden> hlist = HiddenMapper.selectHiddenListByCompanyId(params);
        PageInfo pageInfo = new PageInfo(hlist);
        return new PageUtils(hlist,(int)pageInfo.getTotal(),pageInfo.getPages(),pageInfo.getPageSize(),pageInfo.getPrePage() + 1);
    }

    @Override
    public PageUtils indexHiddenNotice(Map<String, Object> params, int pageNum, int pageSize, String order) {
        //利用PageHelper分页查询 注意：这个一定要放查询语句的前一行,否则无法进行分页,因为它对紧随其后第一个sql语句有效
        if(StringUtils.isNotBlank(order)){
            PageHelper.startPage(pageNum, pageSize,order);
        }else{
            PageHelper.startPage(pageNum, pageSize);
        }

        List<Map<String, Object>> list = HiddenMapper.findHiddenGroup(params);
        PageInfo pageInfo = new PageInfo(list);
        return new PageUtils(list,(int)pageInfo.getTotal(),pageInfo.getPages(),pageInfo.getPageSize(),pageInfo.getPrePage() + 1);
    }

    @Override
    public List<Map<String, String>> indexHiddenCount(Map<String, Object> params) {
        return HiddenMapper.findHiddenCount(params);
    }

    @Override
    public void toSupervise(Map<String, Object> params) {
        String title;
        String content ;
        String superviseToUserId;
        String superviseType = params.get("superviseType").toString();
        if(superviseType.equals("1")){
            String groupIds = companyMapper.findParentGroupIds(Long.parseLong(params.get("superviseGroupId").toString()));
            List<Long>  userlist  =  companyMapper.findParentGroupLeaderId(groupIds,Constant.LEADER_POSITON_IDS);
            superviseToUserId = StringUtils.join(userlist, ",");
            title = "督导：隐患【"+ params.get("hiddenOrSchduleTitle")+"】已逾期";
            content = "隐患【"+ params.get("hiddenOrSchduleTitle")+"】已逾期,请及时整改。 督导人："+params.get("currentUser");
        }else{
            Info curInfo = infoService.getByCId(Long.getLong(params.get("companyId").toString()), Integer.getInteger(params.get("currentUserType").toString()));
            superviseToUserId =String.valueOf(curInfo.getSuperviserId()) ;
            title = "督导：任务【"+ params.get("hiddenOrSchduleTitle")+"】即将逾期";
            content="任务【"+ params.get("hiddenOrSchduleTitle")+"】即将逾期,请及时处理。 督导人："+params.get("currentUser");
        }
        Map<String, String> extras = new HashMap();
        extras.put("msgId", "");
        extras.put("title", title);
        extras.put("body",content);
        JPushKit.sendPush(settingService, title,content,superviseToUserId , extras);
    }

    /**
     * 推送消息
     * @param hidden
     * @throws Exception
     */
    private void propellingMovement(Hidden hidden, Integer userType) throws Exception {
        if (this.checkedHiddenReview(hidden.getCompanyId(), userType)) {
            if (hidden.getStatus().equals(Constant.DANGER_DPS)) {
                SendMessageUtil.sendMessage(hidden.getCompanyId(), "隐患待评审【" + hidden.getHiddenTitle() + "】", hidden.getHiddenReviewBy(), "隐患【" + hidden.getHiddenTitle() + "】待评审 ", this.messageService, this.settingService);
            }

            if (hidden.getStatus().equals(Constant.DANGER_NOT)) {
                SendMessageUtil.sendMessage(hidden.getCompanyId(), "隐患待提交【" + hidden.getHiddenTitle() + "】", hidden.getCreatedBy(), "隐患【" + hidden.getHiddenTitle() + "】待提交", this.messageService, this.settingService);
            } else if (hidden.getStatus().equals(Constant.DANGER_DZG)) {
                SendMessageUtil.sendMessage(hidden.getCompanyId(), "隐患待整改【" + hidden.getHiddenTitle() + "】", hidden.getHiddenRetifyBy(), "隐患【" + hidden.getHiddenTitle() + "】待整改", this.messageService, this.settingService);
            }
        } else {
            if (null != hidden.getRiskPointId() && hidden.getStatus().intValue() == Constant.DANGER_DZG.intValue()) {
                this.riskPointControlRecordService.insertRiskPointControlRecord(hidden);
                this.riskPointService.updateRiskPointHiddenCount(hidden.getRiskPointId(), 1);
            }

            if (hidden.getStatus().equals(Constant.DANGER_NOT)) {
                SendMessageUtil.sendMessage(hidden.getCompanyId(), "隐患待提交【" + hidden.getHiddenTitle() + "】", hidden.getCreatedBy(), "隐患【 " + hidden.getHiddenTitle() + "】待提交! ", this.messageService, this.settingService);
            } else if (hidden.getStatus().equals(Constant.DANGER_DZG)) {
                SendMessageUtil.sendMessage(hidden.getCompanyId(), "隐患待整改【" + hidden.getHiddenTitle() + "】", hidden.getHiddenRetifyBy(), "隐患【 " + hidden.getHiddenTitle() + "】待整改! ", this.messageService, this.settingService);
                SendMessageUtil.sendBigScreen(hidden.getCompanyId(), hidden.getGroupId(), BigScreenDataTypeEnum.hidden.idType);
            }
        }

    }

    //检查隐患是否可以评审
    private void checkIsHiddenReview(Hidden hidden, Integer userType) throws Exception {
        if (this.checkedHiddenReview(hidden.getCompanyId() , userType)) {
            hidden.setHiddenAcceptedBy(null);
            hidden.setHiddenAcceptedGroup(null);
            hidden.setHiddenRetifyBy(null);
            hidden.setHiddenRetifyGroup(null);
        }
    }

    //设置隐患状态
    private void setHiddenStatus(Hidden hidden, Integer userType) throws Exception {
        if (hidden.getStatus() == 1) {
            if (this.checkedHiddenReview(hidden.getCompanyId(), userType)) {
                hidden.setStatus(Constant.DANGER_DPS);
            } else {
                hidden.setStatus(Constant.DANGER_DZG);
                hidden.setHappenedTime(new Date());
            }
        }
    }

}
