package com.hngf.web.controller.bigscreen;

import com.alibaba.fastjson.JSONObject;
import com.hngf.common.exception.ScyfException;
import com.hngf.common.utils.Constant;
import com.hngf.common.utils.PageUtils;
import com.hngf.common.utils.ParamUtils;
import com.hngf.common.utils.R;
import com.hngf.common.validator.Assert;
import com.hngf.dto.commonPost.CommonPostDto;
import com.hngf.entity.danger.HiddenAttach;
import com.hngf.entity.risk.MapInfo;
import com.hngf.entity.risk.RiskPoint;
import com.hngf.service.bigscreen.BigScreenMessageService;
import com.hngf.service.common.CommonPostService;
import com.hngf.service.danger.HiddenService;
import com.hngf.service.risk.MapInfoService;
import com.hngf.service.risk.RiskPointMapService;
import com.hngf.service.risk.RiskPointService;
import com.hngf.service.sys.CompanyService;
import com.hngf.service.sys.GroupService;
import com.hngf.web.controller.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 大屏展示数据接口
 *
 * @author zhangfei
 * @date 2020-07-01
 */
@RestController
@RequestMapping("/bigscreen/")
@Api(value = "大屏展示数据接口", tags = {"大屏展示数据接口"})
public class BigScreenDataController extends BaseController {

    @Autowired
    private BigScreenMessageService bigScreenMessageService;
    @Autowired
    private RiskPointService riskPointService;
    @Autowired
    private CompanyService companyService;
    @Autowired
    private CommonPostService commonPostService;
    @Autowired
    private HiddenService hiddenService;
    @Autowired
    private MapInfoService mapInfoService;
    @Autowired
    private RiskPointMapService riskPointMapService;

    @Autowired
    private GroupService groupService;

    /**
     * 大屏信息 - 首页
     * @param params
     * @return
     */
    @GetMapping("index/bigScreenMessage")
    @ApiOperation(value="【企业后台大屏】大屏信息",notes = "返回值说明：<br>" +
            "<br>" +
            "riskPointCount(风险点统计):all(总数量),lv1(重大),lv2(较大),lv3(一般),lv4(较低) <br>" +
            "<br>" +
            "todayCheck(今日待检查任务)：all(总数量) ,title(标题),groupName(部门),rate(完成率)<br>" +
            "<br>" +
            "riskPointControlRecord(风险预警信息)：updatedTime(更新时间),causeReasonName(产生原因说明),riskPointName(风险点名称),causeRemark(原因描述)," +
            "detailId(明细ID：隐患、任务、传感器（当前业务层ID）)," +
            "detailType(类型：1隐患告警；2任务逾期告警；3传感器告警；)," +
            "companyId(公司ID),riskPointId(风险点ID),hiddenLevel(隐患等级)," +
            "isOutOfControl(是否失控 1失控0受控),isControl(是否失控：0失控；1受控;),riskPointLevel(风险点等级),isCloseUp(是否闭环：1已闭环；0未闭环),causeReason(产生原因编码)<br>" +
            "<br>" +
            "warningRiskPoint(预警风险点)：all(总数量),lv1(重大),lv2(较大),lv3(一般),lv4(较低)<br>" +
            "<br>" +
            "hiddenCount(隐患统计)：all(总数量),lv1(重大),lv2(较大),lv3(一般),lv4(较低),toBeRectified(待整改),rate(整改完成率),finish(已完成),waitForChecking(待验收)<br>" +
            "<br>" +
            "hiddenBulletin(隐患通报)：time(整改期限),personLiable(责任人),title(隐患名称),groupName(责任部门)<br>")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "companyId", value = "公司ID",required = true, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "groupId", value = "群组ID",required = true, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "path", value = "请求路径：" +
                    "riskPointCount(风险点统计)," +
                    "todayCheck(今日检查)" +
                    "riskPointControlRecord(风险点预警信息)" +
                    "warningRiskPoint(预警风险点)" +
                    "hiddenCount(隐患统计)" +
                    "hiddenBulletin(隐患通报)"
                    ,required = true, paramType = "query", dataType = "string")
    })
    public R bigScreenMessage(@RequestParam Map<String, Object> params) {
        this.hasKey(params, new String[]{"companyId", "groupId", "path"});
        Assert.isNull(params.get("companyId"),"companyId不能为空");
        Assert.isNull(params.get("groupId"),"groupId不能为空");
        Assert.isNull(params.get("path"),"path不能为空");
        String path = (String)params.get("path");
        //判断path路径是否在允许值的范围
        if (!path.equals("riskPointCount")
                && !path.equals("todayCheck")
                && !path.equals("riskPointControlRecord")
                && !path.equals("warningRiskPoint")
                && !path.equals("hiddenCount")
                && !path.equals("hiddenBulletin")) {
            throw new ScyfException("path路径不存在，请确认");
        }
        String data = bigScreenMessageService.bigScreenMessage(params);
        JSONObject jsonObject1 =JSONObject.parseObject(data);
        return R.ok().put("data",jsonObject1);
    }

    /**
     * 企业评分 - 首页
     * @param params
     * @return
     */
    @GetMapping("index/getCompanyScoreInfo")
    @ApiOperation(value="【企业后台大屏】企业评分",notes = "返回值说明：<br>" +
            "riskScore(风险指数),safeScore(安全指数)")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "companyId", value = "公司ID",required = true, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "groupId", value = "群组ID",required = true, paramType = "query", dataType = "int")
    })
    public R getCompanyScoreInfo(@RequestParam Map<String, Object> params) {
        this.hasKey(params, new String[]{"companyId", "groupId"});
        Assert.isNull(params.get("companyId"),"companyId不能为空");
        Assert.isNull(params.get("groupId"),"groupId不能为空");
        return R.ok().put("data",companyService.companyScore(params,1, Constant.PAGE_DB_SIZE,null));
    }

    /**
     * 风险分布图 - 首页
     * @param params
     * @return
     */
    @GetMapping("index/getMapList")
    @ApiOperation(value="【企业后台大屏】风险分布图",response = MapInfo.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "companyId", value = "公司ID",required = true, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "groupId", value = "群组ID",required = true, paramType = "query", dataType = "int")
    })
    public R getMapList(@RequestParam Map<String, Object> params, HttpServletRequest req) {
        this.hasKey(params, new String[]{"companyId", "groupId"});
        Assert.isNull(params.get("companyId"),"companyId不能为空");
        Assert.isNull(params.get("groupId"),"groupId不能为空");

        Map map=new HashMap();
        PageUtils page = mapInfoService.queryPage(params,1,Constant.PAGE_SIZE,null);
        map.put("path",req.getScheme() + "://" + req.getServerName() + ":" + req.getServerPort() + "/source");
        map.put("page",page);
        return R.ok().put("data",map);
    }

    /**
     * 风险分布图上的风险点 - 首页
     * @param params
     * @return
     */
    @GetMapping("index/riskPointList")
    @ApiOperation(value="【企业后台大屏】风险分布图上的风险点",notes = "返回值说明：<br>" +
            "riskPointId(风险点ID)<br>" +
            "riskPointName(风险点名称)<br>" +
            "mapName(地图名称)<br>" +
            "mapData(地图坐标)<br>" +
            "mapType(地图类型)<br>" +
            "riskPointLevel(风险点等级)<br>" +
            "hdangerNumber(隐患数量)<br>" +
            "riskNumber(风险数量)<br>" +
            "isOutOfControl(是否失控：0受控 1预警)<br>" +
            "hiddenLevel(隐患等级)<br>" +
            "isGreatDangerSrc(是否重大危险源)<br>" +
            "")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "companyId", value = "公司ID",required = true, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "groupId", value = "群组ID",required = true, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "mapId", value = "地图ID",required = false, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "riskPointLevel", value = "风险点等级",required = false, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "riskPointType", value = "风险点类型",required = false, paramType = "query", dataType = "int")
    })
    public R getRiskPointMap(@RequestParam Map<String, Object> params) {
        this.hasKey(params, new String[]{"companyId", "groupId"});
        Assert.isNull(params.get("companyId"),"companyId不能为空");
        Assert.isNull(params.get("groupId"),"groupId不能为空");
        return R.ok().put("data",riskPointMapService.getRiskPointMap(params));
    }

    /**
     * 查询风险点详情 - 首页
     * @param params
     * @return
     */
    @GetMapping("index/getRiskPointDetails")
    @ApiOperation(value="【企业后台大屏】查询风险点详情",response = RiskPoint.class)
    @ApiImplicitParam(name = "riskPointId", value = "风险点ID",required = true, paramType = "query", dataType = "int")
    public R getRiskPointDetails(@RequestParam Map<String, Object> params) {
        this.hasKey(params, new String[]{"riskPointId"});
        Assert.isNull(params.get("riskPointId"),"riskPointId不能为空");
        return R.ok().put("data",riskPointService.riskPointDetailsAll(ParamUtils.paramsToLong(params,"riskPointId")));
    }

    /**
     *  风险点子部门总数 - 风险点总数鼠标悬浮使用
     * @param params
     * @return
     */
    @GetMapping("index/riskPointChildrenCount")
    @ApiOperation(value="【企业后台大屏】风险点子部门总数",notes = "返回值说明：<br>" +
            "ristCount(风险点数量)<br>" +
            "groupName(部门名称)<br>" +
            "groupId(部门ID)<br>" +
            "riskPointLevel(风险点等级)<br>"
    )
    @ApiImplicitParams({
            @ApiImplicitParam(name = "companyId", value = "公司ID",required = true, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "groupId", value = "群组ID",required = true, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "riskPointLevel", value = "风险点等级",required = true, paramType = "query", dataType = "int")
    })
    public R riskPointChildrenCount(@RequestParam Map<String, Object> params) {
        this.hasKey(params, new String[]{"companyId", "groupId"});
        Assert.isNull(params.get("companyId"),"companyId不能为空");
        Assert.isNull(params.get("groupId"),"groupId不能为空");

        List<Map<String,Object>> res = riskPointService.getRiskPointChuldrenCount(params);
        return R.ok().put("data",res);
    }


    /**
     * 规章制度
     * @param params
     * @return
     */
    @GetMapping("system/getRulesRegulationsList")
    @ApiOperation(value="【企业后台大屏】规章制度",response = CommonPostDto.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "companyId", value = "公司ID",required = true, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "groupId", value = "群组ID",required = true, paramType = "query", dataType = "int")
    })
    public R getRulesRegulationsList(@RequestParam Map<String, Object> params) {
        this.hasKey(params, new String[]{"companyId", "groupId"});
        Assert.isNull(params.get("companyId"),"companyId不能为空");
        Assert.isNull(params.get("groupId"),"groupId不能为空");

        params.put("postType",9);
        params.put("isPublish",1);
        return R.ok().put("data",commonPostService.queryInstitution(params,1,Constant.PAGE_DB_SIZE,null));
    }

    /**
     * 风险点列表
     * @param params
     * @return
     */
    @GetMapping("riskpoint/list")
    @ApiOperation(value="【企业后台大屏】风险点列表",notes = "返回值说明：<br>" +
            "groupName(责任部门名称),riskPointId(风险点ID),riskPointName(风险点名称),isOutOfControl(是否失控 1失控0受控),userName(责任人),riskPointType(风险点类型 1设备设施 2作业活动),riskPointLevel(风险点的等级) <br>")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "companyId", value = "公司ID",required = true, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "groupId", value = "群组ID",required = true, paramType = "query", dataType = "int")
    })
    public R riskPointList(@RequestParam Map<String, Object> params) {
        this.hasKey(params, new String[]{"companyId", "groupId"});
        Assert.isNull(params.get("companyId"),"companyId不能为空");
        Assert.isNull(params.get("groupId"),"groupId不能为空");
        return R.ok().put("data",riskPointService.getAllRiskPoint(Long.parseLong(params.get("companyId").toString()),Long.parseLong(params.get("groupId").toString())));
    }

    /**
     * 隐患列表
     * @param params
     * @return
     */
    @GetMapping("hidden/list")
    @ApiOperation(value="【企业后台大屏】隐患列表",response = HiddenAttach.class,notes = "返回值说明：<br>" +
            "hiddenId(隐患ID),<br>" +
            "status(隐患状态),<br>" +
            "hiddenTitle(隐患名称),<br>" +
            "hiddenLevel(隐患等级),<br>" +
            "hdStatus(隐患状态说明),<br>" +
            "executor(执行人),<br>" +
            "groupName(执行人所属部门名称),<br>" +
            "hiddenRetifyTime(整改期限),<br>" +
            "beforeAnnex(整改前附件信息),<br>" +
            "afterAnnex(整改后附件信息)" )
    @ApiImplicitParams({
            @ApiImplicitParam(name = "companyId", value = "公司ID",required = true, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "groupId", value = "群组ID",required = true, paramType = "query", dataType = "int")
    })
    public R getHiddenList(@RequestParam Map<String, Object> params) {
        this.hasKey(params, new String[]{"companyId", "groupId"});
        Assert.isNull(params.get("companyId"),"companyId不能为空");
        Assert.isNull(params.get("groupId"),"groupId不能为空");

        params.put("postType",9);
        params.put("isPublish",1);
        return R.ok().put("data",hiddenService.getHiddenListForBigScreen(params));
    }


    /**-------------------------------------- 监管层大屏接口 --------------------------------------*/

    /**
     * 查询企业下所有子企业列表和企业安全分值
     * @param params
     * @return
     */
    @GetMapping({"/gent/companyList"})
    @ApiOperation(value="【监管级大屏】查询企业下所有子企业列表和企业安全分值",notes = "返回值说明：<br>" +
            "securityPoint(安全指数),<br>" +
            "ristPoint(风险指数),<br>" +
            "companyList(公司列表){<br>" +
            "companyId(企业ID)," +
            "companyName(企业名称)," +
            "longitude(经度)," +
            "latitude(维度)," +
            "webUrl(大屏跳转地址)," +
            "risk1(重大风险数量？)," +
            "risk2(较大风险数量？)," +
            "hd1(重大隐患数量？)," +
            "hd2(较大隐患数量？)" +
            "}"
    )
    @ApiImplicitParams({
            @ApiImplicitParam(name = "companyId", value = "公司ID",required = true, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "groupId", value = "群组ID",required = true, paramType = "query", dataType = "int")
    })
    public R queryCompanyListOfGent(@RequestParam Map<String, Object> params) {
        this.hasKey(params, new String[]{"companyId", "groupId"});
        Map<String, Object> result = this.companyService.queryCompanyListForBigScreen(params);
        return R.ok().put("data", result);
    }

    /**
     * 风险点总计
     * @param params
     * @return
     */
    @GetMapping({"/gent/riskCount"})
    @ApiOperation(value="【监管级大屏】风险点总计",notes = "返回值说明：<br>" +
            "total(全部风险点数量),<br>" +
            "list{<br>" +

            "ristCount(风险点数量)，" +
            "riskLevel(风险点等级)" +

            "}"
    )
    @ApiImplicitParams({
            @ApiImplicitParam(name = "companyId", value = "公司ID",required = true, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "groupId", value = "群组ID",required = true, paramType = "query", dataType = "int")
    })
    public R queryRiskCountForGent(@RequestParam Map<String, Object> params) {
        this.hasKey(params, new String[]{"companyId", "groupId"});
        Map<String, Object> result = this.companyService.queryRiskCountForGent(params);
        return R.ok().put("data", result);
    }

    /**
     * 隐患总计
     * @param params
     * @return
     */
    @GetMapping({"/gent/hiddenCount"})
    @ApiOperation(value="【监管级大屏】隐患总计",notes = "返回值说明：<br>" +
            "total(总隐患数量),<br>" +
            "biggest(重大数量),<br>" +
            "bigger(较大数量),<br>" +
            "third(一般数量),<br>" +
            "fourth(较低数量),<br>" +
            "rate(整改完成率),<br>" +
            "status2(待整改数量),<br>" +
            "status3(待验收数量),<br>" +
            "status4(已完成数量),<br>"
    )
    @ApiImplicitParams({
            @ApiImplicitParam(name = "companyId", value = "公司ID",required = true, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "groupId", value = "群组ID",required = true, paramType = "query", dataType = "int")
    })
    public R queryHiddenDangerCountForGent(@RequestParam Map<String, Object> params) {
        this.hasKey(params, new String[]{"companyId", "groupId"});
        Map<String, Object> result = this.companyService.queryHiddenDangerCountForGent(params);
        return R.ok().put("data", result);
    }

    /**
     * 查询统计预警风险点数
     * @param params
     * @return
     */
    @GetMapping({"/gent/preWarning"})
    @ApiOperation(value="【监管级大屏】查询统计预警风险点数",notes = "返回值说明：<br>" +
            "total(总数量),<br>" +
            "list{<br>" +

            "hiddenCount(数量)，" +
            "riskLevel(等级)" +

            "}"
    )
    @ApiImplicitParams({
            @ApiImplicitParam(name = "companyId", value = "公司ID",required = true, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "groupId", value = "群组ID",required = true, paramType = "query", dataType = "int")
    })
    public R queryPreWarningRiskPoint(@RequestParam Map<String, Object> params) {
        this.hasKey(params, new String[]{"companyId", "groupId"});
        Map<String, Object> result = this.companyService.queryRiskCountOfOutOfControlForGent(params);
        return R.ok().put("data", result);
    }

    /**
     * 查询隐患通报
     * @param params
     * @return
     */
    @GetMapping({"/gent/hiddenNotice"})
    @ApiOperation(value="【监管级大屏】查询隐患通报",notes = "返回值说明：<br>" +
            "hiddenRetifyGroupName(整改部门),<br>" +
            "hiddenRetifyDeadline(整改期限),<br>" +
            "companyName(公司名称),<br>" +
            "hiddenTitle(隐患标题),<br>" +
            "hiddenRetifyBy(整改人),<br>"
    )
    @ApiImplicitParams({
            @ApiImplicitParam(name = "companyId", value = "公司ID",required = true, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "groupId", value = "群组ID",required = true, paramType = "query", dataType = "int")
    })
    public R queryHiddenDanger(@RequestParam Map<String, Object> params) {
        this.hasKey(params, new String[]{"companyId", "groupId"});
        List<Map<String, Object>> list = this.companyService.queryHiddenForGent(params);
        return R.ok().put("data", list);
    }

    /**
     * 查询统计下级企业风险指数
     * @param params
     * @return
     */
    @GetMapping({"/gent/riskPointList"})
    @ApiOperation(value="【监管级大屏】查询统计下级企业风险指数",notes = "返回值说明：<br>" +
            "companyId(公司ID),<br>" +
            "companyName(公司名称),<br>" +
            "levelPoint(风险指数),<br>"
    )
    @ApiImplicitParams({
            @ApiImplicitParam(name = "companyId", value = "公司ID",required = true, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "groupId", value = "群组ID",required = true, paramType = "query", dataType = "int")
    })
    public R queryRiskPoint(@RequestParam Map<String, Object> params) {
        this.hasKey(params, new String[]{"companyId", "groupId"});
        List<Map<String, Object>> list = this.companyService.queryRiskPointForGent(params);
        return R.ok().put("data", list);
    }

    /**
     * 查询下级单位安全生产分值
     * @param params
     * @return
     */
    @GetMapping({"/gent/securityPointList"})
    @ApiOperation(value="【监管级大屏】查询下级单位安全生产分值",notes = "返回值说明：<br>" +
            "companyId(公司ID),<br>" +
            "companyName(公司名称),<br>" +
            "levelPoint(安全生产指数),<br>"
    )
    @ApiImplicitParams({
            @ApiImplicitParam(name = "companyId", value = "公司ID",required = true, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "groupId", value = "群组ID",required = true, paramType = "query", dataType = "int")
    })
    public R querySecurityPoint(@RequestParam Map<String, Object> params) {
        this.hasKey(params, new String[]{"companyId", "groupId"});
        List<Map<String, Object>> list = this.companyService.queryHiddenPointForGent(params);
        return R.ok().put("data", list);
    }
    /**
     * 查询公司/群组下的坐标  及最大风险点
     * @param params
     * @return
     */
    @GetMapping({"/gent/groupPositionAndLvl"})
    @ApiOperation(value="【监管级大屏】查询公司/群组下的坐标  及最大风险点",notes = "返回值说明：<br>" +
            "companyId(公司ID),<br>" +
            "groupId( 群组id)"+
            "typeId( 0公司，1群组),<br>" +
            "maxlvl(最高等级风险点的风险等级),<br>"+
            "longitude (群组的经度) latitude(群组的维度) <br>" +
            "riskPointId 群组最高等级风险点的风险点Id <br>" +
            "mapId 群组最高等级风险点的风险点对应的地图Id "
    )
    @ApiImplicitParams({
            @ApiImplicitParam(name = "companyId", value = "公司ID",required = true, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "groupId", value = "群组ID",required = true, paramType = "query", dataType = "int")
    })
    public R queryGroupPositionAndLvl(@ApiIgnore @RequestParam(required = false) Map<String, Object> params) {
        List<Map<String, Object>> glist  =  this.companyService.queryGroupPositionAndLvl(params);
        return R.ok().put("data",glist);
    }


    @ApiOperation(value = "【监管级大屏】",notes = "返回数据：industryCompanyCount：企业数量，<br>" +
            "totalCompany：企业/单位总数, industryCompanyCount：各个单位数量 <br>" +
            "riskAndHiddenMap: 行业领域-风险点及隐患、分数值<br>" +
            "topRiskScoreCompany：排名前十得风险指数公司<br>" +
            "topSafeScoreCompany：排名前十得安全指数公司<br>")
    @GetMapping({"/allIndustryStatisticsBigScreen"})
    @ApiImplicitParams({
            @ApiImplicitParam(name = "companyId", value = "公司ID",required = true, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "groupId", value = "群组ID",required = false, paramType = "query", dataType = "int")
    })
    public R allIndustryStatisticsBigScreen(@RequestParam Map<String, Object> map) {
        Map<String ,Object> resultmap = this.bigScreenMessageService.allIndustryStatistics(map);
        return  R.ok().put("data",resultmap);
    }


    @ApiOperation(value="【集团大屏】企业地址信息列表",notes = "企业经纬度")
    @ApiImplicitParam(name = "companyId", value = "当前集团Id", required = true, paramType = "path", dataType = "long")
    @GetMapping(value = "/getCompanyMapList/{companyId}")
    public R getCompanyMapListByParentId(@PathVariable("companyId") Long companyId){
        return R.ok().put("data",this.companyService.getCompanyMapByParentId(companyId));
    }

    @ApiOperation(value = "【企业大屏】背景图列表", notes="根据企业Id和groupId获取背景图列表",response = MapInfo.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "companyId",value = "企业Id", paramType = "query", required = true, dataType = "long")  ,
            @ApiImplicitParam(name = "groupId",value = "企业的群组ID", paramType = "query",  dataType = "long")
    })
    @GetMapping("getAllMapList")
    public R getAllMapList(@ApiIgnore @RequestParam(required = false) Map<String, Object> params, HttpServletRequest req){
        if(null == params || null == params.get("companyId") || StringUtils.isBlank("companyId")){
            return R.error("企业Id不能为空！");
        }
        List<Map<String, Object>> mapList = this.mapInfoService.getAllMapList(params);
        int total = 0;
        if(null != mapList ){
            total = mapList.size();
        }
        Map<String, Object> map=new HashMap<>();
        PageUtils page = new PageUtils(mapList,total,1,total,1);
        map.put("path",req.getScheme() + "://" + req.getServerName() + ":" + req.getServerPort() + "/source");
        map.put("page",page);
        return R.ok().put("data",map);
    }

    /**
     * 根据mapId反显 企业地图信息
     * yfh
     * 2020/05/26
     * @param mapId
     * @return
     */
    @ApiOperation(value = "【企业大屏】根据mapId查询企业地图信息", notes="根据mapId获取企业地图信息数据",response = MapInfo.class)
    @ApiImplicitParam(name = "mapId" , value = "地图背景图Id", paramType = "query", required = true ,dataType = "long")
    @RequestMapping(value="/mapinfo/info",method = RequestMethod.GET)
    public R info(Long mapId){
        MapInfo mapInfo = mapInfoService.getById(mapId);
        return R.ok().put("data", mapInfo);
    }

    /**
     * 查询有权限的组织树形菜单
     * @return
     */
    @ApiOperation(value="查询有权限的组织树形菜单")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "companyId",value = "企业Id", paramType = "query", required = true, dataType = "long")  ,
            @ApiImplicitParam(name = "groupId",value = "企业的群组ID", paramType = "query", required = true, dataType = "long")
    })
    @GetMapping("/getGroupLists")
    public R getGroupLists(Long companyId, Long groupId ) {
        Map<String, Object> params = new HashMap();
        params.put("companyOrOrgId", companyId);
        params.put("groupId", groupId);
        return R.ok().put("data", this.groupService.getEleuiTreeList(params));
    }
}
