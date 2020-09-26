package com.hngf.service.score.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hngf.common.utils.Constant;
import com.hngf.common.utils.DateUtils;
import com.hngf.common.utils.PageUtils;
import com.hngf.entity.score.ScoreModel;
import com.hngf.entity.score.ScoreSetting;
import com.hngf.mapper.score.ScoreModelMapper;
import com.hngf.mapper.score.ScoreSettingMapper;
import com.hngf.service.score.ScoreModelService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


@Service("ScoreModelService")
public class ScoreModelServiceImpl implements ScoreModelService {

    @Autowired
    private ScoreModelMapper scoreModelMapper;
    @Autowired
    private ScoreSettingMapper scoreSettingMapper;

    @Override
    public PageUtils queryPage(Map<String, Object> params,Integer pageNum,Integer pageSize,String order) {
        //利用PageHelper分页查询 注意：这个一定要放查询语句的前一行,否则无法进行分页,因为它对紧随其后第一个sql语句有效
        if(StringUtils.isNotBlank(order)){
            PageHelper.startPage(pageNum, pageSize,order);
        }else{
            PageHelper.startPage(pageNum, pageSize);
        }
        List<ScoreModel> list = scoreModelMapper.findList(params);
        PageInfo<ScoreModel> pageInfo = new PageInfo<ScoreModel>(list);
        return new PageUtils(list,(int)pageInfo.getTotal(),pageInfo.getPages(),pageInfo.getPageSize(),pageInfo.getPrePage() + 1);
    }
    /**
     * @Author: zyj
     * @Description:查询考核模式配置
     * @Param
     * @Date 16:03 2020/6/11
     */
    @Override
    public List<ScoreModel> queryByModelSettingPage(Long companyId) {
        return scoreModelMapper.queryByModelSettingPage(companyId);
    }
    /**
     * @Author: zyj
     * @Description:通过企业id,父级企业id,年份,查询初始化分数
     * @Param companyId 企业id  scoreParentId企业父级id year年份
     * @Date 16:19 2020/6/11
     */
    @Override
    public ScoreModel getThisYearinitScore(Long companyId, Long scoreParentId, String year) {
        return scoreModelMapper.getThisYearinitScore(companyId,scoreParentId,year);
    }
    /**
     * @Author: zyj
     * @Description:绩效考核模式配置表添加配置
     * @Param
     * @Date 16:59 2020/6/11
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateByModelSetting(ScoreModel scoreModel) {
             scoreModelMapper.update(scoreModel);
        if (scoreModel.getScoreModelStatus().equals(1)) {
            Calendar calendar = Calendar.getInstance();
            String year = String.valueOf(calendar.get(1));
            scoreModelMapper.deleteByParentId(scoreModel.getScoreModelId(), year);
            String[] monthArr = new String[]{"01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12"};
            String moth = "";
            String day = scoreModel.getStartTimeStr();
            if (scoreModel.getScoreModelType().equals(1)) {
                scoreModel.getStartTimeStr();
            } else if (scoreModel.getScoreModelType().equals(2)) {
                day = "01";
            }

            String[] var7 = monthArr;
            int var8 = monthArr.length;

            for(int var9 = 0; var9 < var8; ++var9) {
                String s = var7[var9];
                String startTimeStr = year + "-" + s + "-" + day;
                Date startTime = DateUtils.stringToDate(startTimeStr, "yyyy-MM-dd");
                calendar.setTime(startTime);
                calendar.set(11, 0);
                calendar.set(12, 0);
                calendar.set(13, 0);
                calendar.set(14, 0);
                calendar.add(2, 1);
                calendar.add(5, -1);
                Date endTime = calendar.getTime();
                ScoreModel thisYearinitScore = new ScoreModel();
                if (scoreModel.getScoreModelType().equals(1)) {
                    thisYearinitScore.setScoreItemName("考核模式");
                } else if (scoreModel.getScoreModelType().equals(2)) {
                    thisYearinitScore.setScoreItemName("积分模式");
                }

                thisYearinitScore.setCompanyId(scoreModel.getCompanyId());
                thisYearinitScore.setScoreParentId(scoreModel.getScoreModelId());
                thisYearinitScore.setInitScore(scoreModel.getInitScore());
                thisYearinitScore.setScoreModelStatus(scoreModel.getScoreModelStatus());
                thisYearinitScore.setStartTime(startTime);
                thisYearinitScore.setEndTime(endTime);
                thisYearinitScore.setScoreModelType(scoreModel.getScoreModelType());
                thisYearinitScore.setCreatedBy(scoreModel.getCreatedBy());
                thisYearinitScore.setCreatedTime(new Date());
                thisYearinitScore.setDelFlag(0);
                scoreModelMapper.add(thisYearinitScore);
            }
        }
    }

    @Override
    public ScoreModel getById(Long id){
        return scoreModelMapper.findById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(ScoreModel ScoreModel) {
        scoreModelMapper.add(ScoreModel);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(ScoreModel ScoreModel) {
        scoreModelMapper.update(ScoreModel);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeByIds(List ids) {
        scoreModelMapper.deleteByIds(ids);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeById(Long id) {
        scoreModelMapper.deleteById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int initSetting(Long companyId) throws Exception {
        ScoreModel model = new ScoreModel();
        model.setCreatedTime(new Date());
        model.setScoreModelStatus(1);
        model.setDelFlag(0);
        model.setCompanyId(companyId);
        model.setScoreParentId(0L);
        model.setInitScore(0);
        model.setScoreModelType(1);
        model.setScoreItemName("月度考核");
        model.setScoreItemDesc("月度考核模式：通过加分或者扣分来考核工作完成情况");
        scoreModelMapper.add(model);
        model.setScoreModelType(2);
        model.setScoreModelStatus(0);
        model.setScoreItemName("累计积分");
        model.setScoreItemDesc("累计积分模式：通过工作完成情况累计加分");
        scoreModelMapper.add(model);
        List<Map<String, Object>> data = new ArrayList();
        Map<String, Object> map1 = new HashMap();
        map1.put("settingCode", Constant.SCORE_SETTING_SCHDULE_YQ_1);
        map1.put("settingType", Constant.SCORE_TYPE_DEDCUT_2);
        map1.put("scoreItemName", "任务逾期");
        map1.put("scoreItemDesc", "【扣分】任务逾期,没有按时完成检查");
        data.add(map1);
        Map<String, Object> map2 = new HashMap();
        map2.put("settingCode", Constant.SCORE_SETTING_HDANGER_ZGCS_2);
        map2.put("settingType", Constant.SCORE_TYPE_DEDCUT_2);
        map2.put("scoreItemName", "隐患整改超时");
        map2.put("scoreItemDesc", "【扣分】整改超时,没有在规定期限内完成整改");
        data.add(map2);
        Map<String, Object> map3 = new HashMap();
        map3.put("settingCode", Constant.SCORE_SETTING_SCHDULE_JCWC_3);
        map3.put("settingType", Constant.SCORE_TYPE_ADDING_1);
        map3.put("scoreItemName", "任务完成检查");
        map3.put("scoreItemDesc", "【加分】任务按时完成检查");
        data.add(map3);
        Map<String, Object> map4 = new HashMap();
        map4.put("settingCode", Constant.SCORE_SETTING_HDANGER_ZGWC_4);
        map4.put("settingType", Constant.SCORE_TYPE_ADDING_1);
        map4.put("scoreItemName", "隐患完成整改");
        map4.put("scoreItemDesc", "【加分】隐患按时完成整改");
        data.add(map4);
        Map<String, Object> map5 = new HashMap();
        map5.put("settingCode", Constant.SCORE_SETTING_HDANGER_YSWC_5);
        map5.put("settingType", Constant.SCORE_TYPE_ADDING_1);
        map5.put("scoreItemName", "隐患完成验收");
        map5.put("scoreItemDesc", "【加分】隐患按时完成验收");
        data.add(map5);
        Map<String, Object> map6 = new HashMap();
        map6.put("settingCode", Constant.SCORE_SETTING_HDANGER_PSWC_6);
        map6.put("settingType", Constant.SCORE_TYPE_ADDING_1);
        map6.put("scoreItemName", "隐患完成评审");
        map6.put("scoreItemDesc", "【加分】隐患按时完成评审");
        data.add(map6);
        Iterator var10 = data.iterator();

        while(var10.hasNext()) {
            Map<String, Object> datum = (Map)var10.next();
            ScoreSetting score = new ScoreSetting();
            score.setCompanyId(companyId);
            score.setCreatedTime(new Date());
            score.setDelFlag(0);
            score.setSettingStatus(0);
            score.setSettingScore(0);
            score.setSettingCode((Integer)datum.get("settingCode"));
            score.setSettingType((Integer)datum.get("settingType"));
            score.setScoreItemName(datum.get("scoreItemName").toString());
            score.setScoreItemDesc(datum.get("scoreItemDesc").toString());
            scoreSettingMapper.add(score);
        }

        return 1;
    }

    @Override
    public int deleteBatchByCompanyId(Long companyId, Long updatedBy) {
        this.scoreSettingMapper.deleteBatchByCompanyId(companyId, updatedBy) ;
        return this.scoreModelMapper.deleteBatchByCompanyId(companyId,updatedBy);
    }
}
