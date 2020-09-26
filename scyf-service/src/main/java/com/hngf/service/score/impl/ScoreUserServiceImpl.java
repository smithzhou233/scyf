package com.hngf.service.score.impl;

import com.hngf.common.utils.DateUtils;
import com.hngf.entity.score.ScoreLog;
import com.hngf.entity.score.ScoreModel;
import com.hngf.entity.score.ScoreSetting;
import com.hngf.mapper.score.ScoreLogMapper;
import com.hngf.mapper.score.ScoreModelMapper;
import com.hngf.service.score.ScoreLogService;
import com.hngf.service.score.ScoreSettingService;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

import org.apache.commons.lang.StringUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hngf.common.utils.PageUtils;

import com.hngf.mapper.score.ScoreUserMapper;
import com.hngf.entity.score.ScoreUser;
import com.hngf.service.score.ScoreUserService;


@Service("ScoreUserService")
public class ScoreUserServiceImpl implements ScoreUserService {

    @Autowired
    private ScoreUserMapper ScoreUserMapper;
    @Autowired
    private ScoreSettingService scoreSettingService;
    @Autowired
    private ScoreLogService scoreLogService;
    @Autowired
    private ScoreModelMapper scoreModelMapper;
    @Autowired
    private ScoreLogMapper scoreLogMapper;

    @Override
    public PageUtils queryPage(Map<String, Object> params,Integer pageNum,Integer pageSize,String order) {
        //利用PageHelper分页查询 注意：这个一定要放查询语句的前一行,否则无法进行分页,因为它对紧随其后第一个sql语句有效
        if(StringUtils.isNotBlank(order)){
            PageHelper.startPage(pageNum, pageSize,order);
        }else{
            PageHelper.startPage(pageNum, pageSize);
        }
        List<ScoreUser> list = ScoreUserMapper.findList(params);
        PageInfo<ScoreUser> pageInfo = new PageInfo<ScoreUser>(list);
        return new PageUtils(list,(int)pageInfo.getTotal(),pageInfo.getPages(),pageInfo.getPageSize(),pageInfo.getPrePage() + 1);
    }
    /**
     * @Author: zyj
     * @Description:用户得分统计图和部门得分统计图
     * @Param
     * @Date 15:00 2020/6/12
     */
    @Override
    public Map<String, List<Integer>> getMonthGradeStatistics(Map<String, Object> map) {
        Map<String, List<Integer>> dataMap = new HashMap();
        //获取企业id
        long companyId = Long.parseLong(map.get("companyId").toString());
        //0或1(0部门统计，1用户统计)
        String isMyself = map.get("isMyself").toString();
        Integer[] monthStr = new Integer[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        List<Integer> monthScore = new ArrayList();
        ScoreModel modelSetting = scoreModelMapper.getModelSetting(companyId, (Integer)null, 1);
        if (modelSetting != null) {
            Map<String, Object> pamap = new HashMap();
            pamap.put("companyId", companyId);
            Calendar calendar = Calendar.getInstance();
            String year = map.get("year").toString();
            //如果为空设置为当前年份
            if (StringUtils.isEmpty(year)) {
                year = String.valueOf(calendar.get(1));
            }

            pamap.put("year", year);
            pamap.put("scoreParentId", modelSetting.getScoreModelId());
            List<ScoreModel> modelSettingList = scoreModelMapper.getModelSettingList(pamap);
            if (isMyself.equals("1")) {
                pamap.put("userId", map.get("userId"));
            } else {
                pamap.put("groupId", map.get("groupId"));
            }

            ScoreModel pModel;
            Integer score_;
            if (!modelSettingList.isEmpty()) {
                for(Iterator var17 = modelSettingList.iterator(); var17.hasNext(); monthScore.add(score_ + pModel.getInitScore())) {
                    pModel = (ScoreModel) var17.next();
                    pamap.put("startTime", DateUtils.format(pModel.getStartTime(), "yyyy-MM-dd"));
                    pamap.put("endTime", DateUtils.format(pModel.getEndTime(), "yyyy-MM-dd"));
                    //统计得分数和群组所在的用户数量
                    Map<String, Object> obj = scoreLogMapper.periodicSum(pamap);
                    if (isMyself.equals("1")) {
                        score_ = Integer.parseInt(obj.get("sumScore").toString());
                    } else {
                        //算平均分了？？？
                        /*Long groupUserCount = Long.parseLong(obj.get("groupUserCount").toString());
                        BigDecimal sumScore = (BigDecimal)obj.get("sumScore");
                        if (sumScore.intValue() > 0) {
                            Double score = (new BigDecimal((double)((float)sumScore.intValue() / (float)groupUserCount.intValue()))).setScale(0, 4).doubleValue();
                            score_ = score.intValue();
                        } else {
                            score_ = 0;
                        }*/
                        score_=Integer.parseInt(obj.get("sumScore").toString());
                    }
                }
            } else {
                Integer[] var24 = monthStr;
                int var25 = monthStr.length;

                for(int var26 = 0; var26 < var25; ++var26) {
                    score_ = var24[var26];
                    monthScore.add(score_);
                }
            }
        }

        dataMap.put("monthScore", monthScore);
        return dataMap;
    }

    @Override
    public ScoreUser getById(Long id){
        return ScoreUserMapper.findById(id);
    }

    @Override
    public int goGrade(Long companyId, Long groupId, Long userId, Integer settingCode, Long detailId) throws Exception {
        ScoreSetting scoreSetting = this.scoreSettingService.getBySettingCode(companyId, settingCode);

        if (null != scoreSetting) {
            Integer score = 0;
            Integer settingScore = scoreSetting.getSettingScore();
            ScoreLog scoreLog = new ScoreLog();
            scoreLog.setCompanyId(companyId);
            scoreLog.setGroupId(groupId);
            scoreLog.setUserId(userId);
            scoreLog.setProfessionId(detailId);
            scoreLog.setPerformanceRemark(scoreSetting.getScoreItemDesc());
            scoreLog.setCreatedTime(new Date());
            scoreLog.setDelFlag(0);
            if (scoreSetting.getSettingType().intValue() == 1) {
                score = score + settingScore;
            } else if (scoreSetting.getSettingType().intValue() == 2) {
                score = score - settingScore;
            }

            scoreLog.setCreatedBy(userId);
            scoreLog.setPerformanceScore(score);
            this.scoreLogService.save(scoreLog);
            Map<String, Object> map = new HashMap();
            map.put("companyId", companyId);
            map.put("userId", userId);
            map.put("scoreType", 1);
            ScoreUser myHistoryScoreRecord = this.ScoreUserMapper.getMyScoreRecord(map);
            this.saveMyScore(companyId, groupId, userId, score, 1, myHistoryScoreRecord);
            map.put("scoreType", 2);
            Calendar date = Calendar.getInstance();
            String year = String.valueOf(date.get(1));
            map.put("year", year);
            ScoreUser myYearScoreRecord = this.ScoreUserMapper.getMyScoreRecord(map);
            this.saveMyScore(companyId, groupId, userId, score, 2, myYearScoreRecord);
            map.remove("year");
            map.put("scoreType", 3);
            map.put("createdTime", DateUtils.format(DateUtils.getStartTime(), "yyyy-MM"));
            ScoreUser myMonthScoreRecord = this.ScoreUserMapper.getMyScoreRecord(map);
            this.saveMyScore(companyId, groupId, userId, score, 3, myMonthScoreRecord);
            return 1;
        }

        return 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(ScoreUser ScoreUser) {
        ScoreUserMapper.add(ScoreUser);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(ScoreUser ScoreUser) {
        ScoreUserMapper.update(ScoreUser);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeByIds(List ids) {
        ScoreUserMapper.deleteByIds(ids);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeById(Long id) {
        ScoreUserMapper.deleteById(id);
    }


    private void saveMyScore(Long companyId, Long groupId, Long userId, Integer score, Integer scoreType, ScoreUser myScoreRecord) throws Exception {
        if (myScoreRecord == null) {
            ScoreUser scoreUser = new ScoreUser();
            scoreUser.setCompanyId(null != companyId ? companyId.toString() : null);
            scoreUser.setGroupId(null != groupId ? groupId.intValue() : null);
            scoreUser.setScoreUserId(userId);
            if (scoreType == 1) {
                scoreUser.setRemark("我的历史累计得分（包括：加分或者扣分累加，不包括初始低分）");
            } else if (scoreType == 2) {
                Calendar date = Calendar.getInstance();
                String year = String.valueOf(date.get(1));
                scoreUser.setRemark("我的" + year + "年 度得分（包括：加分或者扣分累加）");
            } else if (scoreType == 3) {
                int month = (new Date()).getMonth() + 1;
                scoreUser.setRemark("我的" + month + "月 度得分（包括：加分或者扣分累加）");
            }

            scoreUser.setGrossScore(score);
            scoreUser.setCreatedTime(new Date());
            scoreUser.setCreatedBy(userId);
            scoreUser.setDelFlag(0);
            scoreUser.setScoreType(scoreType);
            this.ScoreUserMapper.add(scoreUser);
        } else {
            myScoreRecord.setGrossScore(score + myScoreRecord.getGrossScore());
            myScoreRecord.setUpdatedTime(new Date());
            this.ScoreUserMapper.update(myScoreRecord);
        }

    }
}
