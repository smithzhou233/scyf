package com.hngf.api.controller.danger;

import com.hngf.api.common.annotation.RepeatSubmit;
import com.hngf.api.common.utils.DateUtils;
import com.hngf.api.controller.BaseController;
import com.hngf.common.utils.Constant;
import com.hngf.common.utils.R;
import com.hngf.common.utils.RedisKeys;
import com.hngf.common.utils.RedisUtils;
import com.hngf.common.validator.Assert;
import com.hngf.dto.risk.InspectItemContentDto;
import com.hngf.dto.risk.RiskInspectItemDto;
import com.hngf.entity.danger.InspectDef;
import com.hngf.entity.danger.InspectSchdule;
import com.hngf.entity.risk.RiskSource;
import com.hngf.entity.sys.Info;
import com.hngf.mapper.risk.RiskSourceMapper;
import com.hngf.service.danger.InspectDefService;
import com.hngf.service.danger.InspectSchduleDefService;
import com.hngf.service.danger.InspectSchduleService;
import com.hngf.service.risk.*;
import com.hngf.service.sys.InfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;


/**
 * 检查任务
 *
 * @author yss
 * @email
 * @date 2020-06-18
 */
@RestController
@RequestMapping("/api/scyf/inspectschdule")
@Api(value="检查任务管理",tags = {"检查任务管理"})
public class InspectSchduleController  extends BaseController {
    @Autowired
    private InspectSchduleService inspectSchduleService;
    @Autowired
    private RiskPointService riskPointService;
    @Autowired
    private RiskPointCheckRecordService riskPointCheckRecordService;
    @Autowired
    private RiskInspectRecordService riskInspectRecordService;
    @Autowired
    private RiskCtrlService riskCtrlService;
    @Autowired
    private RiskSourceMapper riskSourceMapper;
    @Autowired
    private RiskSourceService RiskSourceService;
    @Autowired
    private RedisUtils redisUtils;
    @Autowired
    private InspectDefService inspectDefService;
    @Autowired
    private InfoService infoService;
    /**
     * 我的任务
     */
    @GetMapping("/myTask")
    @ApiOperation(value="我的任务",response = InspectSchdule.class,
            notes = "检查记录按钮关联接口：<br>" +
                    "1、我的任务详情：api/scyf/inspectschdule/myDetail/{scheduleId} <br>" +
                    "2、检查记录列表：api/scyf/inspectschdule/taskInspectRecord <br>" +
                    "3、记录明细列表：api/scyf/inspectschdule/inspectRecordDetail <br>" +
                    "<br>" +
                    "开始检查按钮关联接口：<br>" +
                    "1、风险点列表：api/scyf/inspectschdule/getScheduleRiskPointList <br>" +
                    "2、查询风险点下的危险源：api/scyf/riskpoint/getRiskPointDangerSrc <br>" +
                    "3、查询风险点详情：api/scyf/riskpoint/get <br>" +
                    "4、查询分级管控检查项：api/scyf/inspectschdule/getRiskMeasureByDangerId <br>" +
                    "5、通过/不通过/不涉及按钮：api/scyf/inspectschdule/saveInspectRecord <br>" +
                    "6、提交为隐患：scyf/hidden/save <br>" +
                    "7、完成检查：scyf/inspectschdule/schduleInspectFinish <br>"
    )
    @ApiImplicitParams({
            @ApiImplicitParam(name = "action", value = "查询类型：0待检查， 3已逾期 ",defaultValue = "0",required = true, paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "pageNum", value = "第几页", required = true, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "pageSize", value = "每页多少条数据", required = true, paramType = "query", dataType = "int"),
    })
    public R myTask(@RequestParam Map<String, Object> params){
        if (null == params.get("action")) {
            params.put("action", "0");
        }
        params.put("api", "yes");
        params.put("historyStatus", 0);
        params.put("companyId", getCompanyId());
        params.put("positionId", getPositionId());

        if (params.get("action").toString().equals("3")) {  //逾期任务只查询本周内的
            params.put("startDate",  DateUtils.getBeforeDay(7) );
        }else{
            params.put("startDate", DateUtils.dateFormat(DateUtils.getStartTime(), "yyyy-MM-dd HH:mm:ss"));
            params.put("endDate", DateUtils.dateFormat(DateUtils.getnowEndTime(new Date()), "yyyy-MM-dd HH:mm:ss"));
        }

        params.put("userId", getUserId());
        params.put("groupId",getGroupId());
        int pageNum = null!= params.get(Constant.PAGE_PAGENUM) ? Integer.parseInt(params.get(Constant.PAGE_PAGENUM).toString()) : 1;
        int pageSize = null != params.get(Constant.PAGE_PAGESIZE) ? Integer.parseInt(params.get(Constant.PAGE_PAGESIZE).toString()) : Constant.PAGE_SIZE;
        return R.ok().put("data", inspectSchduleService.getMyTask(params,pageNum,pageSize,null));
    }

    /**
     * 历史任务
     */
    @GetMapping("/getHistorySchedule")
    @ApiOperation(value="历史任务",response = InspectSchdule.class)
    @ApiImplicitParams({
            @ApiImplicitParam(
                    paramType = "query",
                    name = "dateTime",
                    dataType = "String",
                    required = false,
                    value = "时间过滤条件（年-月-日）",
                    defaultValue = "2020-07-13"
            ),
            @ApiImplicitParam(
                    paramType = "query",
                    name = "checkType",
                    dataType = "String",
                    required = false,
                    value = "检查类型：全部（-1）；1专业；2日常；3节假；4事故类比；5季节；6综合",
                    defaultValue = "2"
            ),
            @ApiImplicitParam(name = "status", value = "状态：全部（-1），2已检查； 3已逾期 ",defaultValue = "2",required = false, paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "pageNum", value = "第几页", required = true, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "pageSize", value = "每页多少条数据", required = true, paramType = "query", dataType = "int"),
    })
    public R getHistoryScheduleList(@RequestParam Map<String, Object> params){

        params.put("historyStatus", 1);
        if (this.paramsToString(params,"checkType").equals("-1")) {
            params.put("classifyValue", params.get("checkType"));
        }
        params.remove("checkType");
        //状态为-1：查询全部
        if (this.paramsToString(params,"status").equals("-1")) {
            params.remove("status");
        }

        params.put("companyId", getCompanyId());
        params.put("positionId", getPositionId());
        params.put("userId", getUserId());
        params.put("groupId",getGroupId());
        int pageNum = null!= params.get(Constant.PAGE_PAGENUM) ? Integer.parseInt(params.get(Constant.PAGE_PAGENUM).toString()) : 1;
        int pageSize = null != params.get(Constant.PAGE_PAGESIZE) ? Integer.parseInt(params.get(Constant.PAGE_PAGESIZE).toString()) : Constant.PAGE_SIZE;
        return R.ok().put("data", inspectSchduleService.getMyTask(params,pageNum,pageSize,null));
    }



    /**
     * 我的任务-风险点列表
     */
    @GetMapping("/getScheduleRiskPointList")
    @ApiOperation(value="我的任务-风险点列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "type", value = "类型：0未检查，1已检查",defaultValue = "0",required = true, paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "checkNumber", value = "风险点检查次数",defaultValue = "0",required = false, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "scheduleId", value = "检查任务Id",required = true, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "pageNum", value = "第几页", required = true, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "pageSize", value = "每页多少条数据", required = true, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "keyword", value = "查询关键字", required = false, paramType = "query", dataType = "string")
    })
    public R getScheduleRiskPointList(@RequestParam Map<String, Object> params){
        Assert.isNull(params.get("scheduleId"),"检查任务Id不能为空");
        if (null == params.get("type")) {
            params.put("type", "0");
        }
        params.put("companyId", getCompanyId());
        params.put("positionId", getPositionId());
        params.put("isActive", 1);
        params.put("userId", getUserId());
        if(null==params.get("checkNumber")){
            params.put("checkNumber",0);
        }
        params.put("createdBy",getUserId());
        int pageNum = null!= params.get(Constant.PAGE_PAGENUM) ? Integer.parseInt(params.get(Constant.PAGE_PAGENUM).toString()) : 1;
        int pageSize = null != params.get(Constant.PAGE_PAGESIZE) ? Integer.parseInt(params.get(Constant.PAGE_PAGESIZE).toString()) : Constant.PAGE_SIZE;
        return R.ok().put("data", riskPointService.getScheduleRiskPointList(params,pageNum,pageSize,null));
    }

    /**
     * 我的任务-检查项列表
     * @param params
     * @return
     */
    @GetMapping("/getSchduleInspectItems")
    @ApiOperation(value="我的任务-检查项列表",response = RiskInspectItemDto.class,notes = "" +
            "inspectDefType检查类型的值，从我的任务接口中返回的inspectMode字段中获取 <br>" +
            "inspectMode=1 inspectDefType = 1 <br>" +
            "inspectMode=2 inspectDefType = 0 <br>")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "inspectDefId", value = "检查表ID",required = true, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "checkRecordNo", value = "检查记录编号",required = false, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "scheduleId", value = "检查任务Id",required = false, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "riskPointId", value = "风险点ID",required = false, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "dangerSrcId", value = "危险源ID(分级管控检查)，当检查类型为0时必填",defaultValue = "1",required = false, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "inspectDefType", value = "检查类型：0分级管控检查；1非分级管控检查",defaultValue = "1",required = true, paramType = "query", dataType = "int"),
    })
    public R getSchduleInspectItems(@RequestParam Map<String, Object> params){
        Assert.isNull(params.get("inspectDefId"),"检查表ID不能为空");
        //Assert.isNull(params.get("scheduleId"),"检查任务Id不能为空");
        Assert.isNull(params.get("inspectDefType"),"检查类型不能为空");

        params.put("companyId", getCompanyId());
        params.put("positionId", getPositionId());
        params.put("userId", getUserId());
        Integer inspectDefType = Integer.parseInt(params.get("inspectDefType").toString());
        if (null == params.get("riskPointId")) {
            params.put("riskPointId", 0);
        }

        List itemDtos = new ArrayList<RiskInspectItemDto>();

        InspectSchdule inspectSchdule = this.inspectSchduleService.getById(Long.parseLong(params.get("scheduleId").toString()));
        if (null != inspectSchdule && null != inspectSchdule.getInspectScheduleId()) {
            params.put("inspectDefId", inspectSchdule.getInspectDefId());//将任务表的inspectDefId赋值给inspectDefId参数
        }

        if (inspectDefType == 0) {
            Assert.isNull(params.get("dangerSrcId"),"危险源ID不能为空");

            Integer ctlLevel = this.riskCtrlService.getCurrentUserCtlLevel(params);
            params.put("ctlLevel", ctlLevel);
            params.put("dangerId",params.get("dangerSrcId"));
            itemDtos=  redisUtils.get(RedisKeys.getriskMeasureItemList(getUserId().toString(),params.get("riskPointId").toString(),params.get("scheduleId").toString()),List.class);
            if(null==itemDtos){
                itemDtos = this.riskSourceMapper.findControlInspectItemList(params);
                redisUtils.set(RedisKeys.getriskMeasureItemList(getUserId().toString(),params.get("riskPointId").toString(),params.get("scheduleId").toString()),itemDtos);
           }
        } else if (inspectDefType == 1) {
           itemDtos=  redisUtils.get(RedisKeys.getScheduleDefItemList(getUserId(),params.get("inspectDefId").toString(),params.get("scheduleId").toString()),List.class);
            if(null==itemDtos){
                itemDtos = this.riskSourceMapper.findUnControlInspectItemList(params);
               redisUtils.set(RedisKeys.getScheduleDefItemList(getUserId(),params.get("inspectDefId").toString(),params.get("scheduleId").toString()),itemDtos);
            }
        }
        return R.ok().put("data", itemDtos);
    }

    /**
     * 我的任务-检查记录
     */
    @GetMapping("/taskInspectRecord")
    @ApiOperation(value="我的任务-检查记录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "inspectDefId", value = "检查表ID ",defaultValue = "0",required = true, paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "inspectScheduleId", value = "检查任务计划Id",defaultValue = "0",required = true, paramType = "query", dataType = "string"),

    })
    public R taskInspectRecord(@RequestParam Map<String, Object> params){
        params.put("module", 3);
        params.put("companyId", getCompanyId());
        params.put("userId",getUserId());
        int pageNum = null!= params.get(Constant.PAGE_PAGENUM) ? Integer.parseInt(params.get(Constant.PAGE_PAGENUM).toString()) : 1;
        int pageSize = null != params.get(Constant.PAGE_PAGESIZE) ? Integer.parseInt(params.get(Constant.PAGE_PAGESIZE).toString()) : Constant.PAGE_SIZE;
        return R.ok().put("data", riskPointCheckRecordService.getListPage(params,pageNum,pageSize,null));
    }

    /**
     * 查询危险源的风险管控措施
     * @param params
     * @return
     */
    @GetMapping("/getRiskMeasureByDangerId")
    @ApiOperation(value = "查询危险源的风险管控措施", response = InspectItemContentDto.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "dangerId", value = "危险源ID", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = "inspectDefId", value = "检查表ID", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = "scheduleId", value = "任务ID", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = "riskPointId", value = "风险点ID", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = "checkRecordNo", value = "检查记录编号", paramType = "query", required = true, dataType = "int")
    })
    public R getRiskMeasureByDangerId(@RequestParam() Map<String, Object> params){
        Assert.isNull(params.get("dangerId"),"危险源ID不能为空");
        Assert.isNull(params.get("inspectDefId"),"检查表ID不能为空");
        Assert.isNull(params.get("scheduleId"),"任务ID不能为空");
        Assert.isNull(params.get("riskPointId"),"风险点ID不能为空");
        Assert.isNull(params.get("checkRecordNo"),"检查记录编号不能为空");

        params.put("companyId", getCompanyId());
        params.put("positionId", getPositionId());
        params.put("userId", getUserId());

        Integer ctlLevel = this.riskCtrlService.getCurrentUserCtlLevel(params);
        params.put("ctlLevel", ctlLevel);
        List<RiskInspectItemDto> controlCheckItemList = this.RiskSourceService.getControlInspectItemList(params);
        List<InspectItemContentDto> itemContentDtos = new ArrayList();
        if (!controlCheckItemList.isEmpty()) {
            for (RiskInspectItemDto item : controlCheckItemList) {
                if (null != item && null != item.getInspectItemes() && !item.getInspectItemes().isEmpty()) {
                    itemContentDtos.addAll(item.getInspectItemes());
                }
            }
        }
        return R.ok().put("data",itemContentDtos);
    }

    /**
     * 保存检查记录
     */
    @PostMapping("/saveInspectRecord")
    @ApiOperation(value="保存检查记录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "riskPointId", value = "风险点Id",required = true, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "scheduleId", value = "检查任务Id",required = true, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "inspectDefType", value = "检查定义类型",required = true, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "inspectDefId", value = "检查表ID",required = true, paramType = "query", dataType = "int"),
            /* @ApiImplicitParam(name = "checkNumber", value = "检查次数",required = false, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "totalInspectCount", value = "检查总次数",required = false, paramType = "query", dataType = "int"),*/
            @ApiImplicitParam(name = "checkResult", value = "检查结果：1通过,2不通过,3发现隐患,4不涉及",required = true, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "checkRecordNo", value = "检查记录编号",required = false, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "isScheduleCheck", value = "",required = false, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "inspectRecordLogId", value = "检查记录日志表ID",required = false, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "spotData", value = "现场数据采集",required = false, paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "remark", value = "备注",required = false, paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "inspectItemContentDefIds", value = "检查项ID，多个逗号分隔", required = true, paramType = "query", dataType = "string"),

    })
    @RepeatSubmit()
    public R saveInspectRecord(@RequestParam Map<String, Object> params){
        //Assert.isNull(params.get("riskPointId"),"风险点Id不能为空");
        Assert.isNull(params.get("scheduleId"),"检查任务Id不能为空");
        Assert.isNull(params.get("inspectDefId"),"检查表ID不能为空");
        Assert.isNull(params.get("checkResult"),"检查结果不能为空");
        params.put("isScheduleCheck", true);
        params.put("companyId", getCompanyId());
        params.put("userId", getUserId());
        params.put("groupId", getGroupId());
        try {
            riskInspectRecordService.addRiskInspect(params,null);
        } catch (Exception e) {
            e.printStackTrace();
            return R.error("保存失败");
        }
        return R.ok();
    }

    /**
     * 完成检查
     * @param params
     * @return
     */
    @PostMapping("/schduleInspectFinish")
    @ApiOperation(value="完成检查")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "riskPointId", value = "风险点Id",required = true, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "scheduleId", value = "检查任务Id",required = true, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "inspectDefType", value = "检查定义类型",required = true, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "inspectDefId", value = "检查表ID",required = true, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "checkRecordNo", value = "检查记录编号(查询我的任务时有生成)",required = true, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "isScheduleCheck", value = "是否完成检查：1是，0否",required = false, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "dangerSourceIds", value = "危险源ID，多个逗号分隔", required = false, paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "longitude", value = "经度",required = false, paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "latitude", value = "维度",required = false, paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "address", value = "地址名称",required = false, paramType = "query", dataType = "string")
    })
    @RepeatSubmit()
    public R schduleInspectFinish(@RequestParam Map<String, Object> params){
        //Assert.isNull(params.get("riskPointId"),"风险点Id不能为空");
        Assert.isNull(params.get("scheduleId"),"检查任务Id不能为空");
        Assert.isNull(params.get("inspectDefId"),"检查表ID不能为空");
        Assert.isNull(params.get("inspectDefType"),"检查定义类型不能为空");

        params.put("companyId", getCompanyId());
        params.put("positionId", getPositionId());
        params.put("userId", getUserId());
        params.put("groupId", getGroupId());
        params.put("isScheduleCheck", true);
        try {
            return this.riskInspectRecordService.schduleInspectFinish(params);
        } catch (Exception e) {
            e.printStackTrace();
            return R.error("保存失败");
        }
    }

    @ApiOperation("【获取危险源列表】分级管控表检查")
    @ApiImplicitParams({@ApiImplicitParam(
            paramType = "query",
            name = "riskPointId",
            dataType = "String",
            required = true,
            value = "风险点ID【非NFC/二维码检查】",
            defaultValue = "1111"
    ), @ApiImplicitParam(
            paramType = "query",
            name = "nfcCode",
            dataType = "String",
            required = true,
            value = "NFC内容【NFC检查】",
            defaultValue = "1111"
    ), @ApiImplicitParam(
            paramType = "query",
            name = "qrCode",
            dataType = "String",
            required = true,
            value = "二维码内容【二维码检查】",
            defaultValue = "11111"
    ), @ApiImplicitParam(
            paramType = "query",
            name = "scheduleId",
            dataType = "String",
            required = false,
            value = "任务ID",
            defaultValue = "11111"
    )})
    @RequestMapping(
            value = {"/getRiskSourceByRiskPointId"},
            method = {RequestMethod.GET}
    )
    @ResponseBody
    public R getRiskSourceByRiskPointId(HttpServletRequest request, @RequestParam Map<String, Object> map) {
        try {
            if (null!=map.get("nfcCode") && this.riskPointService.getById(this.getParaToLong("nfcCode")) == null) {
                return R.error( "NFC卡不匹配");
            } else if (StringUtils.isNotEmpty(this.getPara("qrCode")) && this.riskPointService.selectByQrCode(this.getPara("qrCode")) == null) {
                return R.error( "二维码不匹配");
            } else if (null!=this.getParaToLong("riskPointId") && this.riskPointService.getById(this.getParaToLong("riskPointId")) == null) {
                return R.error( "NFC卡不匹配");
            } else {
                map.put("companyId", getCompanyId());
                map.put("positionId", getPositionId());
                if(null!=map.get("nfcCode") && StringUtils.isNotEmpty(map.get("nfcCode").toString())){
                    map.put("riskPointId", map.get("nfcCode"));
                }else if(null!=map.get("qrCode") && StringUtils.isNotEmpty(map.get("qrCode").toString()) ){
                    map.put("riskPointId", map.get("qrCode"));
                }
                //危险源ID集合
                List<RiskSource>  riskSourceList = this.riskPointService.getRiskPointDangerSrc(map);
                Map<String,Object> pmap  = new HashMap<>();
                pmap.put("companyId",getCompanyId());
                pmap.put("controlTable",0);
                pmap.put("defTypeValue",0);
                List<InspectDef> bizCheckDefList =  this.inspectDefService.getInspectDefByMap(pmap);
                InspectDef inspectDef = null;
                if(bizCheckDefList!=null && bizCheckDefList.size()>0) {
                    inspectDef = bizCheckDefList.get(0);
                    if (null == inspectDef || null == inspectDef.getInspectDefId()) {
                        return R.error( "分级管控检查表不存在" );
                    }
                }
                return R.ok().put("data",riskSourceList).put("inspectDef",inspectDef);
            }
        } catch (Exception var4) {
            var4.printStackTrace();
            return R.error( "获取失败");
        }
    }
    /**
     * 检查
     */
    @GetMapping(value="/checkedDeviceOn")
    @ApiOperation(value="检查巡检开关",notes = "true开,false关")
    public R checkedDeviceOn(){
        Map<String, Object> res = new HashMap<>();
        res.put("deviceOn", inspectSchduleService.checkedDeviceOn(getCompanyId(),getUser().getUserType()));
        return R.ok().put("data", res);
    }

    @GetMapping(value="/isShowCheckAddressOn")
    @ApiOperation(value="是否显示检查地址开关",notes = "true开,false关")
    public Boolean isShowCheckAddressOn(Long companyId,Integer userType) {
        Info info = infoService.getByCId(companyId, userType );
        if (null != info && null != info.getIsShowCheckAddress()) {
            return info.getIsShowCheckAddress().intValue() == Constant.IS_SHOW_CHECK_ADDRESS_YES;
        }
        return false;
    }

}
