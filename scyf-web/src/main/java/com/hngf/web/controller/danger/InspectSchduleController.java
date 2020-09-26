package com.hngf.web.controller.danger;

import com.hngf.common.utils.Constant;
import com.hngf.common.utils.ParamUtils;
import com.hngf.common.utils.R;
import com.hngf.common.validator.Assert;
import com.hngf.entity.danger.InspectItemDef;
import com.hngf.entity.danger.InspectSchdule;
import com.hngf.entity.risk.RiskInspectRecord;
import com.hngf.entity.risk.RiskPointCheckRecord;
import com.hngf.service.danger.InspectSchduleService;
import com.hngf.service.risk.RiskInspectRecordService;
import com.hngf.service.risk.RiskPointCheckRecordService;
import com.hngf.service.risk.RiskPointService;
import com.hngf.web.controller.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;



/**
 * 检查任务
 * @author zhangfei
 * @email 
 * @date 2020-06-20 17:36:24
 */
@RestController
@RequestMapping("scyf/inspectschdule")
@Api(value="检查任务管理",tags = {"检查任务管理"})
public class InspectSchduleController extends BaseController {
    @Autowired
    private InspectSchduleService inspectSchduleService;
    @Autowired
    private RiskPointService riskPointService;
    @Autowired
    private RiskPointCheckRecordService riskPointCheckRecordService;
    @Autowired
    private RiskInspectRecordService riskInspectRecordService;

    /**
     * 任务列表
     */
    @RequestMapping(value = "/list",method = RequestMethod.GET)
    @ApiOperation(value="任务列表",response = InspectSchdule.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "positionId", value = "岗位ID",required = false, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "groupId", value = "部门ID",required = false, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "action", value = "0未检查,2已检查,3已逾期",required = false, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "inspectType", value = "检查周期类型  临时性（random）、常规性（fixed）",required = false, paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "inspectMode", value = "检查方式 1，现场检查2、基础检查",required = false, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "status", value = "任务状态(0未检查；1检查中；2已检查；3逾期；4，忽略检查)",required = false, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "dateRange", value = "日期范围,开始日期 - 结束日期(例如：2020-06-20 - 2020-07-20)",required = false, paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "pageNum", value = "第几页", required = true, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "pageSize", value = "每页多少条数据", required = true, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "keyword", value = "查询关键字", required = false, paramType = "query", dataType = "string")
    })
    public R list(@RequestParam Map<String, Object> params){

        if(null != params.get("positionId")){
            //如果岗位ID为-1或0，从params条件中移除
            String positionId = params.get("positionId").toString();
            if("-1".equals(positionId) || "0".equals(positionId)){
                params.remove("positionId");
            }
        }
        if (null == params.get("action")) {
            params.put("action", "0");
        }

        String action = params.get("action").toString();
        //如果传来的action不是 0,2,3 ，设为默认0
        if(!"0".equals(action) && !"2".equals(action) && !"3".equals(action)){
            params.put("action", "0");
        }

        //日期范围条件重组:开始日期 - 结束日期
        if(null != params.get("dateRange") && StringUtils.isNotBlank(params.get("dateRange").toString())){
            String dateRange = params.get("dateRange").toString();
            if (!"null".equals(dateRange)) {
                String[] split = dateRange.split(" - ");
                if (null != split && split.length > 0) {
                    if (split.length > 1) {
                        params.put("endDate", split[1]);
                    }
                    params.put("startDate", split[0]);
                }
            }
        }

        if( null ==params.get("groupId") || StringUtils.isEmpty(params.get("groupId").toString()) || StringUtils.isBlank(params.get("groupId").toString())){
            params.put("groupId",getGroupId());
        }
        params.put("companyId",getCompanyId());
        params.put("api", "no");
        int pageNum = null!= params.get(Constant.PAGE_PAGENUM) ? Integer.parseInt(params.get(Constant.PAGE_PAGENUM).toString()) : 1;
        int pageSize = null != params.get(Constant.PAGE_PAGESIZE) ? Integer.parseInt(params.get(Constant.PAGE_PAGESIZE).toString()) : Constant.PAGE_SIZE;

        return R.ok().put("data", inspectSchduleService.queryPage(params,pageNum,pageSize,null));
    }

    /**
     * 任务详情
     */
    @GetMapping("/info/{scheduleId}")
    @RequiresPermissions("scyf:inspectschdule:info")
    @ApiOperation(value="任务详情",response = InspectSchdule.class)
    @ApiImplicitParam(name = "scheduleId", value = "任务ID", required = true, paramType = "path", dataType = "int")
    public R info(@PathVariable("scheduleId") Long scheduleId){
        return R.ok().put("data", inspectSchduleService.getDetailById(scheduleId));
    }

    /**
     * 子任务列表
     */
    @GetMapping("/sonlist")
    @RequiresPermissions("scyf:inspectschdule:info")
    @ApiOperation(value="子任务列表",response = InspectSchdule.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "inspectDefId", value = "检查定义ID",required = true, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "pageNum", value = "第几页", required = true, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "pageSize", value = "每页多少条数据", required = true, paramType = "query", dataType = "int")
    })
    public R sonlist(@RequestParam Map<String, Object> params){
        Assert.isBlank(ParamUtils.paramsToString(params,"inspectDefId"),"检查定义ID[inspectDefId]不能为空");
        int pageNum = null!= params.get(Constant.PAGE_PAGENUM) ? Integer.parseInt(params.get(Constant.PAGE_PAGENUM).toString()) : 1;
        int pageSize = null != params.get(Constant.PAGE_PAGESIZE) ? Integer.parseInt(params.get(Constant.PAGE_PAGESIZE).toString()) : Constant.PAGE_SIZE;

        params.put("companyId", getCompanyId());
        params.put("groupId", getGroupId());
        return R.ok().put("data", inspectSchduleService.querySonList(params,pageNum,pageSize,null));
    }

    /**
     * 删除任务
     */
    @RequestMapping(value="/delete/{scheduleId}",method = RequestMethod.DELETE)
    @ApiOperation(value="删除任务",response = InspectSchdule.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "scheduleId", value = "任务ID",required = true, paramType = "query", dataType = "int"),
    })
    public R delete(@PathVariable("scheduleId") Long scheduleId){
        int flag = inspectSchduleService.removeById(scheduleId);
        return flag > 0 ? R.ok() : R.error("删除失败");
    }


    /**
     * 我的任务
     */
    @GetMapping("/myTask")
    @RequiresPermissions("scyf:inspectschdule:list")
    @ApiOperation(value="我的任务",response = InspectSchdule.class,
            notes = "注意：查询我的任务列表时，后台会生成检查记录编号(checkRecordNo)，我的任务下的其他需要传递checkRecordNo参数的接口，把这个值带过去<br>" +
                    "检查记录按钮关联接口：<br>" +
                    "1、我的任务详情：scyf/inspectschdule/myDetail/{scheduleId} <br>" +
                    "2、检查记录列表：scyf/inspectschdule/taskInspectRecord <br>" +
                    "3、记录明细列表：scyf/inspectschdule/inspectRecordDetail <br>" +
                    "<br>" +
                    "开始检查按钮关联接口：<br>" +
                    "1、风险点列表：scyf/inspectschdule/getScheduleRiskPointList <br>" +
                    "2、查询风险点下的危险源：scyf/riskpoint/getRiskPointDangerSrc <br>" +
                    "3、查询风险点详情：scyf/riskpoint/get <br>" +
                    "4、查询分级管控检查项：scyf/risksource/getRiskMeasureByDangerId <br>" +
                    "5、通过/不通过/不涉及按钮：scyf/inspectschdule/saveInspectRecord <br>" +
                    "6、提交为隐患：scyf/hidden/save <br>" +
                    "7、完成检查：scyf/inspectschdule/schduleInspectFinish <br>"
    )
    @ApiImplicitParams({
            @ApiImplicitParam(name = "action", value = "查询类型：0待检查，2已检查，3已逾期 ",defaultValue = "0",required = true, paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "pageNum", value = "第几页", required = true, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "pageSize", value = "每页多少条数据", required = true, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "keyword", value = "查询关键字", required = false, paramType = "query", dataType = "string")
    })
    public R myTask(@RequestParam Map<String, Object> params){
        if (null == params.get("action")) {
            params.put("action", "0");
        }
        String action = params.get("action").toString();
        params.put("api", "no");
        if ("0".equals(action)) {
            params.put("inspectType", "fixed");
        }
        params.put("userId", getUserId());
        params.put("positionId", getPositionId());
        params.put("groupId", getGroupId());

        int pageNum = null!= params.get(Constant.PAGE_PAGENUM) ? Integer.parseInt(params.get(Constant.PAGE_PAGENUM).toString()) : 1;
        int pageSize = null != params.get(Constant.PAGE_PAGESIZE) ? Integer.parseInt(params.get(Constant.PAGE_PAGESIZE).toString()) : Constant.PAGE_SIZE;
        return R.ok().put("data", inspectSchduleService.getMyTask(params,pageNum,pageSize,null));
    }

    /**
     * 我的任务-检查记录
     */
    @GetMapping("/taskInspectRecord")
    @RequiresPermissions("scyf:inspectschdule:list")
    @ApiOperation(value="我的任务-检查记录",response = RiskPointCheckRecord.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "inspectDefId", value = "检查表ID ",defaultValue = "0",required = true, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "inspectScheduleId", value = "任务Id",defaultValue = "0",required = true, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "pageNum", value = "第几页", required = true, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "pageSize", value = "每页多少条数据", required = true, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "keyword", value = "查询关键字", required = false, paramType = "query", dataType = "string")
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
     * 我的任务详情
     */
    @RequestMapping(value="/myDetail/{scheduleId}",method = RequestMethod.GET)
    @RequiresPermissions("scyf:inspectschdule:info")
    @ApiOperation(value="我的任务详情",response = InspectSchdule.class)
    @ApiImplicitParam(name = "scheduleId", value = "任务ID", required = true, paramType = "path", dataType = "int")
    public R myDetail(@PathVariable("scheduleId") Long scheduleId){
        return R.ok().put("data", inspectSchduleService.getMyDetailById(scheduleId));
    }

    /**
     * 我的任务-记录明细
     */
    @GetMapping("/inspectRecordDetail")
    @RequiresPermissions("scyf:inspectschdule:list")
    @ApiOperation(value="我的任务-记录明细",response = RiskInspectRecord.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "riskPointId", value = "风险点ID",required = true, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "checkRecordNo", value = "检查记录编号",required = true, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "scheduleId", value = "任务ID",required = true, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "pageNum", value = "第几页", required = true, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "pageSize", value = "每页多少条数据", required = true, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "keyword", value = "查询关键字", required = false, paramType = "query", dataType = "string")
    })
    public R inspectRecordDetail(@RequestParam Map<String, Object> params){
        Assert.isNull(params.get("riskPointId"),"风险点ID不能为空");
        Assert.isNull(params.get("checkRecordNo"),"检查记录编号不能为空");
        Assert.isNull(params.get("scheduleId"),"任务ID不能为空");
        params.put("companyId", getCompanyId());
        int pageNum = null!= params.get(Constant.PAGE_PAGENUM) ? Integer.parseInt(params.get(Constant.PAGE_PAGENUM).toString()) : 1;
        int pageSize = null != params.get(Constant.PAGE_PAGESIZE) ? Integer.parseInt(params.get(Constant.PAGE_PAGESIZE).toString()) : Constant.PAGE_SIZE;
        return R.ok().put("data", riskInspectRecordService.getRiskInspectRecordPage(params,pageNum,pageSize,null));
    }

    /**
     * 我的任务-风险点列表
     */
    @GetMapping("/getScheduleRiskPointList")
    @RequiresPermissions("scyf:inspectschdule:list")
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
    @RequiresPermissions("scyf:inspectschdule:list")
    @ApiOperation(value="我的任务-检查项列表",response = InspectItemDef.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "inspectDefId", value = "检查表ID",required = true, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "checkRecordNo", value = "检查记录编号",required = false, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "scheduleId", value = "检查任务Id",required = true, paramType = "query", dataType = "int")
    })
    public R getSchduleInspectItems(@RequestParam Map<String, Object> params){
        Assert.isNull(params.get("inspectDefId"),"检查表ID不能为空");
        Assert.isNull(params.get("scheduleId"),"检查任务Id不能为空");

        params.put("companyId", getCompanyId());
        params.put("userId", getUserId());
        List<InspectItemDef> list = this.inspectSchduleService.getSchduleInspectItems(params);
        Iterator<InspectItemDef> itemDefIterator = list.iterator();
        InspectItemDef inspectItemDef = null;
        int i = 0;
        //给检查项增加颜色样式
        while (itemDefIterator.hasNext()) {
            inspectItemDef = itemDefIterator.next();
            if (null != inspectItemDef) {
                if (inspectItemDef.getParentId() != null && inspectItemDef.getParentId() == 0L) {
 /*                   inspectItemDef.setInspectItemDefName("<span style='color: orange;'>（" + i + "）检查项：" + inspectItemDef.getInspectItemDefName() + "</span>");
                    inspectItemDef.setInspectItemDefDesc("<span style='color: orange;'>" + inspectItemDef.getInspectItemDefDesc() + "</span>");
                    inspectItemDef.setInspectItemDefMethod("<span style='color: orange;'>" + inspectItemDef.getInspectItemDefMethod() + "</span>");
                    inspectItemDef.setInspectItemDefRule("<span style='color: orange;'>" + inspectItemDef.getInspectItemDefRule() + "</span>");
                    inspectItemDef.setPenalizeItemDefRule("<span style='color: orange;'>" + inspectItemDef.getPenalizeItemDefRule() + "</span>");*/
                    inspectItemDef.setInspectItemDefName(" （" + i + "）检查项：" + inspectItemDef.getInspectItemDefName() + " ");
                    inspectItemDef.setInspectItemDefDesc(" " + inspectItemDef.getInspectItemDefDesc() );
                    inspectItemDef.setInspectItemDefMethod(" " + inspectItemDef.getInspectItemDefMethod()  );
                    inspectItemDef.setInspectItemDefRule(" " + inspectItemDef.getInspectItemDefRule() );
                    inspectItemDef.setPenalizeItemDefRule(" " + inspectItemDef.getPenalizeItemDefRule()  );

                    ++i;
                } else {
                  /*  inspectItemDef.setInspectItemDefName("<span style='color: #1E9FFF;'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;➢ 内容：" + inspectItemDef.getInspectItemDefName() + "</span>");
                    inspectItemDef.setInspectItemDefDesc("<span style='color: #1E9FFF;'>" + inspectItemDef.getInspectItemDefDesc() + "</span>");
                    inspectItemDef.setInspectItemDefMethod("<span style='color: #1E9FFF;'>" + inspectItemDef.getInspectItemDefMethod() + "</span>");
                    inspectItemDef.setInspectItemDefRule("<span style='color: #1E9FFF;'>" + inspectItemDef.getInspectItemDefRule() + "</span>");
                    inspectItemDef.setPenalizeItemDefRule("<span style='color: #1E9FFF;'>" + inspectItemDef.getPenalizeItemDefRule() + "</span>");*/
                    inspectItemDef.setInspectItemDefName("➢ 内容：" + inspectItemDef.getInspectItemDefName()  );
                    inspectItemDef.setInspectItemDefDesc(  inspectItemDef.getInspectItemDefDesc()  );
                    inspectItemDef.setInspectItemDefMethod(  inspectItemDef.getInspectItemDefMethod() );
                    inspectItemDef.setInspectItemDefRule( inspectItemDef.getInspectItemDefRule() );
                    inspectItemDef.setPenalizeItemDefRule( inspectItemDef.getPenalizeItemDefRule()  );
                }
            }
        }
        return R.ok().put("data", list);
    }

    /**
     * 保存检查记录
     */
    @PostMapping("/saveInspectRecord")
   /* @RequiresPermissions("scyf:inspectschdule:saveinspectrecord")*/
    @ApiOperation(value="保存检查记录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "riskPointId", value = "风险点Id",required = true, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "scheduleId", value = "检查任务Id",required = true, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "inspectDefType", value = "检查定义类型",required = true, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "inspectDefId", value = "检查表ID",required = true, paramType = "query", dataType = "int"),
             /* @ApiImplicitParam(name = "checkNumber", value = "检查次数",required = false, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "totalInspectCount", value = "检查总次数",required = false, paramType = "query", dataType = "int"),*/
            @ApiImplicitParam(name = "checkResult", value = "检查结果：1通过,2不通过,3发现隐患,4不涉及",required = true, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "checkRecordNo", value = "检查记录编号(查询我的任务时有生成)",required = true, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "isScheduleCheck", value = "",required = false, paramType = "query", dataType = "int"),
          /*  @ApiImplicitParam(name = "inspectRecordLogId", value = "检查记录日志表ID",required = false, paramType = "query", dataType = "int"),*/
            @ApiImplicitParam(name = "inspectRecordLogId", value = "检查记录日志表ID(如果存在检查记录日志是必须传)",required = false, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "remark", value = "备注",required = false, paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "inspectItemContentDefIds", value = "检查项ID，多个逗号分隔", required = true, paramType = "query", dataType = "string")
    })
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
    /*@RequiresPermissions("scyf:inspectschdule:schduleinspectfinish")*/
    @ApiOperation(value="完成检查")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "riskPointId", value = "风险点Id",required = true, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "scheduleId", value = "检查任务Id",required = true, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "inspectDefType", value = "检查定义类型",required = true, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "inspectDefId", value = "检查表ID",required = true, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "checkRecordNo", value = "检查记录编号(查询我的任务时有生成)",required = true, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "isScheduleCheck", value = "是否完成检查：1是，0否",required = false, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "dangerSourceIds", value = "危险源ID，多个逗号分隔", required = false, paramType = "query", dataType = "string")
    })
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

    /**
     * 任务详情
     */
    @GetMapping("/info/executorScheduleList/{scheduleId}/{action}")
    @RequiresPermissions("scyf:inspectschdule:info")
    @ApiOperation(value="任务定义的任务列表",response = InspectSchdule.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "scheduleId", value = "任务ID", required = true, paramType = "path", dataType = "int"),
            @ApiImplicitParam(name = "action", value = "0未检查,2已检查,3已逾期", required = false, paramType = "path", dataType = "String")
    })
    public R executorScheduleList(@PathVariable("scheduleId") Long scheduleId,@PathVariable(value = "action" , required=false ) String action){
        Map<String, Object> params = new HashMap<>(8);
        params.put("action", action);
        //如果传来的action不是 0,2,3 ，设为默认0
        if(!"0".equals(action) && !"2".equals(action) && !"3".equals(action)){
            params.put("action", "0");
        }
        params.put("scheduleId",scheduleId);
        return R.ok().put("data", inspectSchduleService.executorScheduleList(params));
    }

}
