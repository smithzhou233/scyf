package com.hngf.service.bigscreen.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.hngf.common.enums.BigScreenDataTypeEnum;
import com.hngf.common.utils.*;
import com.hngf.entity.bigscreen.BigScreenMessage;
import com.hngf.entity.sys.Industry;
import com.hngf.mapper.sys.OrgIndustryMapper;
import com.hngf.service.bigscreen.BigScreenMessageService;
import com.hngf.service.danger.HiddenService;
import com.hngf.service.danger.InspectSchduleService;
import com.hngf.service.risk.RiskPointCheckRecordService;
import com.hngf.service.risk.RiskPointControlRecordService;
import com.hngf.service.risk.RiskPointService;
import com.hngf.service.sys.CompanyService;
import com.hngf.service.sys.OrgIndustryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

@Service("BigScreenMessageService")
public class BigScreenMessageServiceImpl implements BigScreenMessageService {

    @Autowired
    private RiskPointService iRiskPointService;
    @Autowired
    private InspectSchduleService inspectSchduleService;
    @Autowired
    private RiskPointCheckRecordService riskPointCheckRecordService;
    @Autowired
    private RiskPointControlRecordService riskPointControlRecordService;
    @Autowired
    private RiskPointService riskPointService;
    @Autowired
    private HiddenService hiddenService;
    @Autowired
    private RedisUtils redisUtils;
    @Autowired
    private OrgIndustryService orgIndustryService;
    @Autowired
    private CompanyService companyService;


    @Value("${scyf.riskPoints}")
    private String riskPoints;
    @Value("${scyf.securityPoints}")
    private String securityPoints;

    @Override
    public String bigScreenMessage(String params) {
        Map<String, Object> maps = new HashMap();
        maps.put("companyId", params.split("_")[0]);
        maps.put("groupId", params.split("_")[1]);
        maps.put("path", params.split("_")[2]);
        return this.bigScreenMessage(maps);
    }

    @Override
    public  Map<String,Object> allIndustryStatistics(Map<String, Object> params) {
        Long orgId = Long.parseLong(params.get("companyId").toString());
        List<Industry>  industryCodeList  = getAllIndustryCodeByPid(orgId);
        //企业
        List<Map<String,Object>> industryCompanyList = new ArrayList<>();
        List<Map<String,Object>> industryCompanyCount = new ArrayList<>();

        //企业总数
        Integer totalCount = 0;
        Integer threeLevel = 0;
        Integer threeLevelFinish = 0;
        Integer oneLevel = 0;
        Integer oneLevelFinish = 0;
        Integer one = 0;
        Integer two = 0;
        Integer three = 0;
        Integer four = 0;
        BigDecimal riskScore = new BigDecimal(0);
        BigDecimal safeScore = new BigDecimal(0);
        Map<String,Object> resultMap =  new HashMap<>();
        Map<String,Object> top10Params = new HashMap<>();
        Map<String,Object> rsParams = new HashMap<>();
        Map<String, Object>  riskAndHiddenMap  =null;
        List<Map<String, Object>> hiddenMonthList=new ArrayList<>();
        //风险点分数配置
        Map<String, Object> rpParam = (Map)JSONArray.parseObject(this.riskPoints, Map.class);
        Map<String, Object> spParam = (Map)JSONArray.parseObject(this.securityPoints, Map.class);
        rsParams.putAll(rpParam);
        rsParams.putAll(spParam);
        for(int i=0;i<industryCodeList.size();i++){
            Industry industry = industryCodeList.get(i);
            Map<String, Object> hiddenMonthMap = new HashMap();
            Map<String,Object> mdata = new HashMap<>();
            mdata.put("industryName",industry.getIndustryName());
            hiddenMonthMap.put("industryName",industry.getIndustryName());
            String chilCodes = industry.getChildIndustryCodes();
            Integer companyCount = companyService.getCompanyCountByIndustryCode(chilCodes);  //查询该行业下的企业
            mdata.put("count",companyCount);
            industryCompanyCount.add(mdata);
            totalCount+=companyCount;
            rsParams.put("industryCode",chilCodes);
            hiddenMonthMap.put("month",companyService.getMonthCountByIndustry(chilCodes));
            hiddenMonthList.add(hiddenMonthMap);
            Map dataByIndustry;
            //查询companyData
            dataByIndustry = this.companyService.getCompanyDataByIndustryCode(rsParams);
            if (null != dataByIndustry && !dataByIndustry.isEmpty()) {
                one = one + Integer.parseInt(dataByIndustry.get("one").toString());
                two = two + Integer.parseInt(dataByIndustry.get("two").toString());
                three = three + Integer.parseInt(dataByIndustry.get("three").toString());
                four = four + Integer.parseInt(dataByIndustry.get("four").toString());
                oneLevel = oneLevel + Integer.parseInt(dataByIndustry.get("oneDangerLevel").toString());
                oneLevelFinish = oneLevelFinish + Integer.parseInt(dataByIndustry.get("oneDangerLevelFinish").toString());
                dataByIndustry.put("oneHiddenLevelNoComplete",oneLevel-oneLevelFinish);
                threeLevel = threeLevel + Integer.parseInt(dataByIndustry.get("threeDangerLevel").toString());
                threeLevelFinish = threeLevelFinish + Integer.parseInt(dataByIndustry.get("threeDangerLevelFinish").toString());
                dataByIndustry.put("threeHiddenLevelNoComplete",threeLevel-threeLevelFinish);
                safeScore=safeScore.add(new BigDecimal(dataByIndustry.get("safeScore").toString()));
                riskScore= riskScore.add(new BigDecimal(dataByIndustry.get("riskScore").toString()));
            } else {
                dataByIndustry = this.packageDataInit((Map)dataByIndustry);
                dataByIndustry.put("industryName",industry.getIndustryName());
            }
            industryCompanyList.add(dataByIndustry);
        }

       // riskAndHiddenMap =  companyService.getOrgRiskAnalysis(orgId);

        top10Params.putAll(rpParam);
        top10Params.putAll(spParam);
        top10Params.put("type","riskScore");
        top10Params.put("industryCode",selectIndustryCodeByOrgId(orgId));
        resultMap.put("topRiskScoreCompany", getTop10RiskOrSafe(top10Params)); //排名前10的风险指数
        top10Params.put("type","safeScore");
        resultMap.put("topSafeScoreCompany", getTop10RiskOrSafe(top10Params)); //排名前十的安全生产分值排名
        resultMap.put("industryCompanyCount",industryCompanyCount);
        resultMap.put("totalCompany",totalCount);
        resultMap.put("riskAndHiddenMap",industryCompanyList); //查询风险点和隐患数量统计
        resultMap.put("hiddenMonth",hiddenMonthList);
       return  resultMap;
    }

    private Map<String, Object>  packageDataInit(Map<String, Object> map) {
        map.put("planCheckCount", 0);
        map.put("finishCheckCount", 0);
        map.put("riskScore", 0);
        map.put("safeScore", 100);
        map.put("oneLevel", 0);
        map.put("twoLevel", 0);
        map.put("oneDangerLevel", 0);
        map.put("oneDangerLevelFinish", 0);
        map.put("threeDangerLevel", 0);
        map.put("threeDangerLevelFinish", 0);
        map.put("one", 0);
        map.put("two", 0);
        map.put("three", 0);
        map.put("four", 0);
        return map;
    }
    private  List<Industry> getAllIndustryCodeByPid(Long orgId){
       return   orgIndustryService.selectIndustryCodeByPid(orgId);
    }

    private String selectIndustryCodeByOrgId(Long orgId){
        return orgIndustryService.selectIndustryCodeByOrgId(orgId);
    }
    private  List<Map<String,Object>> getTop10RiskOrSafe(Map<String,Object> map){
        List<Map<String,Object>> topData =  companyService.selectTop10ScoreOrRisk(map);
        return topData;
    }



    @Override
    public String bigScreenMessage(Map<String, Object> params) {
        JSONObject one = null;
        Map<String, Object> map = params;
        String text = params.get("path").toString();
        if (BigScreenDataTypeEnum.valueOf(text) != null) {
            JSONArray riskPointControlRecord;
            int lv1;
            List maps;
            JSONObject def;
            if (text.equals(BigScreenDataTypeEnum.riskPointCount.idType)) {
                maps = this.iRiskPointService.riskPointLvCountBigScreen(map);
                riskPointControlRecord = new JSONArray();
                def = new JSONObject();
                def.put("all", 0);
                def.put("lv1", 0);
                def.put("lv2", 0);
                def.put("lv3", 0);
                def.put("lv4", 0);
                if (!maps.isEmpty()) {
                    Integer lvAll = 0;

                    for(lv1 = 0; lv1 < maps.size(); ++lv1) {
                        Map<String, Object> e = (Map)maps.get(lv1);
                        if (e.get("riskPointLevel") != null) {
                            Integer lvCount;
                            if (e.get("riskPointLevel").toString().equals("1")) {
                                lvCount = Integer.valueOf(e.get("lvCount").toString());
                                lvAll = lvAll + lvCount;
                                def.put("lv1", lvCount);
                            }

                            if (e.get("riskPointLevel").toString().equals("2")) {
                                lvCount = Integer.valueOf(e.get("lvCount").toString());
                                lvAll = lvAll + lvCount;
                                def.put("lv2", lvCount);
                            }

                            if (e.get("riskPointLevel").toString().equals("3")) {
                                lvCount = Integer.valueOf(e.get("lvCount").toString());
                                lvAll = lvAll + lvCount;
                                def.put("lv3", lvCount);
                            }

                            if (e.get("riskPointLevel").toString().equals("4")) {
                                lvCount = Integer.valueOf(e.get("lvCount").toString());
                                lvAll = lvAll + lvCount;
                                def.put("lv4", lvCount);
                            }
                        }
                    }

                    def.put("all", lvAll);
                }

                riskPointControlRecord.add(def);
                return new Gson().toJson(BigScreenMessage.builder().riskPointCount(riskPointControlRecord).build());
            }

            if (text.equals(BigScreenDataTypeEnum.todayCheck.idType)) {
                map.put("startDate", DateUtils.format(DateUtils.getDayStartTime(new Date()),DateUtils.DATE_TIME_PATTERN));
                map.put("endDate", DateUtils.format(DateUtils.getnowEndTime(new Date()),DateUtils.DATE_TIME_PATTERN));
                maps = this.inspectSchduleService.todayCheck(map);
                riskPointControlRecord = new JSONArray();
                def = new JSONObject();
                def.put("all", maps.size());
                def.put("list", maps);
                riskPointControlRecord.add(def);
                return new Gson().toJson(BigScreenMessage.builder().todayCheck(riskPointControlRecord).build());
            }

            JSONArray hiddenDangerCount;
            if (text.equals(BigScreenDataTypeEnum.weeklyCheck.idType)) {
                hiddenDangerCount = new JSONArray();
                JSONObject one1 = this.getWeekCheck(map);
                hiddenDangerCount.add(one1);
                return new Gson().toJson(BigScreenMessage.builder().weeklyCheck(hiddenDangerCount).build());
            }

            int hdAll;
            int lv4;
            List hiddenDangerBulletinInfo;
            if (text.equals(BigScreenDataTypeEnum.warningRiskPoint.idType)) {
                hiddenDangerCount = new JSONArray();
                one = new JSONObject();
                hiddenDangerBulletinInfo = this.riskPointService.warningRiskPoint(map);
                int alls = 0;
                hdAll = 0;

                for(lv1 = 1; lv1 <= 4; ++lv1) {
                    Iterator var28 = hiddenDangerBulletinInfo.iterator();

                    while(var28.hasNext()) {
                        Map<String, Object> riskPoint = (Map)var28.next();
                        lv4 = Integer.parseInt(riskPoint.get("rPlv").toString());
                        if (lv4 == lv1) {
                            hdAll = lv1;
                            alls += Integer.parseInt(riskPoint.get("rpCount").toString());
                            one.put("lv".concat(String.valueOf(lv1)), riskPoint.get("rpCount"));
                        }
                    }

                    if (hdAll != lv1) {
                        one.put("lv".concat(String.valueOf(lv1)), 0);
                    }
                }

                one.put("all", alls);
                hiddenDangerCount.add(one);
                return new Gson().toJson(BigScreenMessage.builder().warningRiskPoint(hiddenDangerCount).build());
            }

            if (text.equals(BigScreenDataTypeEnum.hiddenCount.idType)) {
                hiddenDangerCount = new JSONArray();
                one = new JSONObject();
                riskPointControlRecord = this.redisUtils.get("hidden_danger_level_language",JSONArray.class);
                map.put("module", 1);
                List<Map<String, Object>> hiddenDangerCounts = this.hiddenService.hiddenCount(map);
                hdAll = 0;
                lv1 = 0;
                int lv2 = 0;
                int lv3 = 0;
                lv4 = 0;
                boolean isOpen = true;
                if (null != riskPointControlRecord && riskPointControlRecord.size() < 4) {
                    isOpen = false;
                }

                Iterator var14 = hiddenDangerCounts.iterator();

                while(var14.hasNext()) {
                    Map<String, Object> dangerCount = (Map)var14.next();
                    Integer hdLevel = Integer.valueOf(dangerCount.get("hdLevel").toString());
                    Integer hdCount = Integer.valueOf(dangerCount.get("hdCount").toString());
                    if (hdLevel == 1) {
                        hdAll += hdCount;
                        lv1 = hdCount;
                        one.put("lv1", hdCount);
                    }

                    if (hdLevel == 3) {
                        lv3 = hdCount;
                        hdAll += hdCount;
                        one.put("lv3", hdCount);
                    }

                    if (isOpen) {
                        if (hdLevel == 2) {
                            lv2 = hdCount;
                            hdAll += hdCount;
                            one.put("lv2", hdCount);
                        }

                        if (hdLevel == 4) {
                            lv4 = hdCount;
                            hdAll += hdCount;
                            one.put("lv4", hdCount);
                        }
                    }
                }

                one.put("all", hdAll);
                if (lv1 == 0) {
                    one.put("lv1", 0);
                }

                if (lv3 == 0) {
                    one.put("lv3", 0);
                }

                if (isOpen) {
                    if (lv2 == 0) {
                        one.put("lv2", 0);
                    }

                    if (lv4 == 0) {
                        one.put("lv4", 0);
                    }
                }

                map.put("module", 2);
                List<Map<String, Object>> hiddenDangerCounts2 = this.hiddenService.hiddenCount(map);
                int toBeRectified = 0;
                int waitForChecking = 0;
                int finish = 0;
                Iterator var18 = hiddenDangerCounts2.iterator();

                while(var18.hasNext()) {
                    Map<String, Object> stringObjectMap = (Map)var18.next();
                    Integer hdStatus = Integer.valueOf(stringObjectMap.get("hdStatus").toString());
                    Integer hdCount = Integer.valueOf(stringObjectMap.get("hdCount").toString());
                    if (hdStatus == 2) {
                        toBeRectified = hdCount;
                        one.put("toBeRectified", hdCount);
                    }

                    if (hdStatus == 3) {
                        waitForChecking = hdCount;
                        one.put("waitForChecking", hdCount);
                    }

                    if (hdStatus == 4) {
                        finish = hdCount;
                        one.put("finish", hdCount);
                    }
                }

                if (toBeRectified == 0) {
                    one.put("toBeRectified", 0);
                }

                if (waitForChecking == 0) {
                    one.put("waitForChecking", 0);
                }

                if (finish == 0) {
                    one.put("finish", 0);
                    one.put("rate", "0%");
                } else {
                    BigDecimal a = BigDecimal.valueOf((long)finish).divide(BigDecimal.valueOf((long)(hdAll + finish)), 1, 0);
                    one.put("rate", (new BigDecimal(a.multiply(BigDecimal.valueOf(100L)).toString())).stripTrailingZeros().toPlainString() + "%");
                }

                hiddenDangerCount.add(one);
                return new Gson().toJson(BigScreenMessage.builder().hiddenCount(hiddenDangerCount).build());
            }

            if (text.equals(BigScreenDataTypeEnum.hiddenBulletin.idType)) {
                hiddenDangerCount = new JSONArray();
                hiddenDangerBulletinInfo = this.hiddenService.hiddenBulletin(map);
                hiddenDangerCount.add(hiddenDangerBulletinInfo);
                return new Gson().toJson(BigScreenMessage.builder().hiddenBulletin(hiddenDangerCount).build());
            }

            if (text.equals(BigScreenDataTypeEnum.riskPointControlRecord.idType)) {
                //查询参数 map中放入detailType=2，只查询任务逾期告警信息
                map.put("detailType", 2);
                PageUtils pageInfo = this.riskPointControlRecordService.getControlRecord(map,1,Constant.PAGE_SIZE_20,null);
                riskPointControlRecord = new JSONArray();
                riskPointControlRecord.add(pageInfo.getList());
                return new Gson().toJson(BigScreenMessage.builder().riskPointControlRecord(riskPointControlRecord).build());
            }

            //查询下级单位安全生产分值
            if (text.equals(BigScreenDataTypeEnum.securityScore.idType)) {
                List<Map<String, Object>> list = this.companyService.queryHiddenPointForGent(params);
                JSONArray securityScore = new JSONArray();
                securityScore.add(list);
                return new Gson().toJson(BigScreenMessage.builder().securityScore(securityScore).build());
            }

            //查询统计下级企业风险指数
            if (text.equals(BigScreenDataTypeEnum.securityScore.idType)) {
                List<Map<String, Object>> list = this.companyService.queryRiskPointForGent(params);
                JSONArray riskIndex = new JSONArray();
                riskIndex.add(list);
                return new Gson().toJson(BigScreenMessage.builder().riskIndex(riskIndex).build());
            }
        }

        return "{}";
    }

    private JSONObject getWeekCheck(Map<String, Object> map) {
        map.put("startDate", DateUtils.format(DateUtils.addDateDays(new Date(),-6),DateUtils.DATE_PATTERN));//6天前的日期
        map.put("endDate", DateUtils.format(DateUtils.getnowEndTime(new Date()),DateUtils.DATE_TIME_PATTERN));
        List<Map<String, Object>> weeklyCheckS = this.riskPointCheckRecordService.weeklyCheck(map);
        int seven = 7;
        String time = map.get("startDate").toString().substring(8, 10);
        if (((List)weeklyCheckS).size() < seven) {
            List<Map<String, Object>> weeklyCheckP = new ArrayList();
            int t = Integer.parseInt(time);

            for(int i = t; i < t + seven; ++i) {
                Map<String, Object> map1 = new HashMap(2);
                boolean result = false;
                Iterator var11 = ((List)weeklyCheckS).iterator();

                while(var11.hasNext()) {
                    Map<String, Object> check = (Map)var11.next();
                    if (Integer.parseInt(check.get("timep").toString()) == i) {
                        result = true;
                        map1.put("times", check.get("times"));
                        map1.put("rpCount", check.get("rpCount"));
                        weeklyCheckP.add(map1);
                    }
                }

                if (!result) {
                    map1.put("times", DateUtils.format(DateUtils.addDateDays(new Date(),i),DateUtils.DATE_PATTERN).substring(5, 10));
                    map1.put("rpCount", 0);
                    weeklyCheckP.add(map1);
                }
            }

            weeklyCheckS = weeklyCheckP;
        }

        JSONArray timeList = new JSONArray();
        JSONArray rpCount = new JSONArray();
        Iterator var15 = ((List)weeklyCheckS).iterator();

        while(var15.hasNext()) {
            Map<String, Object> check = (Map)var15.next();
            timeList.add(check.get("times"));
            rpCount.add(check.get("rpCount"));
        }

        JSONObject one = new JSONObject();
        JSONObject one1 = new JSONObject();
        one.put("timeList", timeList);
        one.put("rpCount", rpCount);
        one1.put("data", one);
        return one1;
    }
}
