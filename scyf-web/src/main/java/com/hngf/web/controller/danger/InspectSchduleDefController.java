package com.hngf.web.controller.danger;

import java.util.*;
import java.util.stream.Collectors;

import com.alibaba.fastjson.JSON;
import com.hngf.common.utils.Constant;
import com.hngf.common.utils.DateUtils;
import com.hngf.common.validator.Assert;
import com.hngf.entity.danger.InspectDef;
import com.hngf.entity.danger.InspectSchdule;
import com.hngf.service.danger.InspectDefService;
import com.hngf.service.danger.InspectSchduleService;
import com.hngf.web.controller.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.hngf.entity.danger.InspectSchduleDef;
import com.hngf.service.danger.InspectSchduleDefService;
import com.hngf.common.utils.PageUtils;
import com.hngf.common.utils.R;
import com.hngf.common.validator.ValidatorUtils;

import javax.annotation.Resource;


/**
 * 检查任务定义
 *
 * @author hngf
 * @email 
 * @date 2020-05-20 17:36:23
 */
@RestController
@RequestMapping("scyf/inspectschduledef")
@Api(value = "常规检查任务定义",tags = "常规检查任务定义")
public class InspectSchduleDefController extends BaseController {
    @Autowired
    private InspectSchduleDefService InspectSchduleDefService;

    @Autowired
    private InspectDefService inspectDefService;

    /**
     * 列表
     */

    @ApiOperation(value="列表",response = InspectSchduleDef.class)
    @GetMapping("/list")
    @RequiresPermissions("scyf:inspectschduledef:list")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "isOpen", value = "开启状态 0.开启   1.停止", required = false, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "riskInspectPosition", value = "岗位", required = false, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "pageNum", value = "第几页", required = true, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "pageSize", value = "每页多少条数据", required = true, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "keyword", value = "查询关键字", required = false, paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "riskInspectGroup", value = "受检部门", required = false, paramType = "query", dataType = "string")
    })
    public R list(@RequestParam Map<String, Object> params){
        params.put("companyId",getCompanyId());
        int pageNum = null!= params.get("pageNum") ? Integer.parseInt(params.get("pageNum").toString()) : 1;
        int pageSize = null != params.get("pageSize") ? Integer.parseInt(params.get("pageSize").toString()) : Constant.PAGE_SIZE;
        return R.ok().put("data", InspectSchduleDefService.queryPage(params,pageNum,pageSize,null));
    }


    /**
     * 信息
     */
    @ApiOperation(value="信息",response = InspectSchduleDef.class)
    @GetMapping("/info/{schduleDefId}")
    @RequiresPermissions("scyf:inspectschduledef:info")
    @ApiImplicitParam(name = "schduleDefId", value = "检查任务定义ID", required = true, paramType = "query", dataType = "int")
    public R info(@PathVariable("schduleDefId") Long schduleDefId){

        return R.ok().put("data", InspectSchduleDefService.getById(schduleDefId));
    }

    /**
     * 保存
     */
    @PostMapping("/save")
    @RequiresPermissions("scyf:inspectschduledef:save")
    @ApiImplicitParams({
           // @ApiImplicitParam(name = "companyId", value = "所属企业", required = true, paramType = "query", dataType = "int"),
            //@ApiImplicitParam(name = "groupId", value = "执行部门", required = true, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "riskInspectPosition", value = "岗位", required = true, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "riskInspectGroup", value = "受检部门", required = true, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "inspectDefId", value = "检查类型：检查定义表Id", required = true, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "riskInspectDefTitle", value = "定义名称", required = true, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "riskInspectDefDesc", value = "定义描述", required = true, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "inspectType", value = "检查周期类型  临时性（random）、常规性（fixed）", required = false, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "inspectMode", value = "临时检查（random）检查方式 1，现场检查2、基础检查  ", required = false, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "riskInspectType", value = "检查类型", required = true, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "executor", value = "执行人", required = false, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "riskInspectParticipant", value = "参与人", required = false, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "startDate", value = "临时性-检查开始时间 必填", required = false, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "endDate", value = "临时性-检查结束时间  必填", required = false, paramType = "query", dataType = "String")
    })
    @ApiOperation(value="保存", produces="application/json",notes = "" +
            "检查定义接口如下：<br>" +
            "1、查询隐患类型：/sys/common/getCommonClassifyByType?classifyType=4 <br>"+
            "2、查询整改部门/验收部门/所属部门列表：/sys/common/getCompanyGroupLists <br>" +
            "3、查询整改人员/验收人员列表：sys/common/getUserByGroupId <br>"+
            "4、根据groupId查询岗位 sys/common/getPositionListByGroupId"+
            "5、查询该机构下所有人员  sys/common/getAllAccountByCompanyId"
    )
    public R save(@RequestBody Map paramMap){
        if("random".equals(paramMap.get("inspectType"))){
            Object startDateObj = paramMap.get("startDate");
            Object endDateObj = paramMap.get("endDate");
            if(startDateObj!=null){
                String startDateStr = startDateObj.toString();
                paramMap.put("startDate", DateUtils.stringToDate(startDateStr,DateUtils.DATE_PATTERN));
            }
            if(null!=endDateObj){
                String endDateStr = endDateObj.toString();
                paramMap.put("endDate", DateUtils.stringToDate(endDateStr,DateUtils.DATE_PATTERN));
            }
        }else{
            paramMap.put("startDate",null);
            paramMap.put("endDate",null);
        }

        InspectSchduleDef InspectSchduleDef =  JSON.parseObject(JSON.toJSONString(paramMap), InspectSchduleDef.class);
        Long id  =super.getId("scyf_inspect_schdule_def");
        InspectSchduleDef.setSchduleDefId(id);

        InspectSchduleDef.setCreatedBy(getUserId());
        InspectSchduleDef.setCompanyId(getCompanyId());
        InspectSchduleDef.setGroupId(InspectSchduleDef.getRiskInspectGroup()==null?getGroupId():InspectSchduleDef.getRiskInspectGroup());
        ValidatorUtils.validateEntity(InspectSchduleDef);
        String checkType = InspectSchduleDef.getInspectType();
        Integer checkMode = InspectSchduleDef.getInspectMode();
        Long riskCheckPosition = InspectSchduleDef.getRiskInspectPosition();
        if (null == riskCheckPosition && 0L == riskCheckPosition) {
            return R.error("执行岗位不能为空");
        }
        if (!StringUtils.isBlank(checkType) && null != checkMode && checkMode != 0) {
            if ("fixed".equalsIgnoreCase(checkType) && 1 == checkMode) {
                Map<String,Object> pmap  = new HashMap<>();
                pmap.put("companyId",getCompanyId());
                pmap.put("controlTable",0);
                pmap.put("defTypeValue",0);
                List<InspectDef> bizCheckDefList =  this.inspectDefService.getInspectDefByMap(pmap);
                InspectDef inspectDef = null;
                if(bizCheckDefList!=null && bizCheckDefList.size()>0){
                     inspectDef = bizCheckDefList.get(0);
                    if (null == inspectDef || null == inspectDef.getInspectDefId()) {
                        return R.error( "分级管控检查表不存在" );
                    }
                    InspectSchduleDef.setInspectDefId(inspectDef.getInspectDefId());
                    //InspectSchduleDef.setRiskInspectType(inspectDef.getInspectDefTypeId());
                }else{
                        return R.error( "分级管控检查表不存在" );
                }

             /*   Date startDate = InspectSchduleDef.getStartDate();
                if (null == startDate) {
                    return R.error( "请选择开始时间");
                }
                InspectSchduleDef.setStartDate(startDate);*/
                InspectSchduleDefService.save(InspectSchduleDef);
        }else if ("fixed".equalsIgnoreCase(checkType) && 2 == checkMode) {
                if (null == InspectSchduleDef.getInspectDefId()|| InspectSchduleDef.getInspectDefId().equals(0L)) {
                    return R.error("请选择检查表");
                }
              /*  Date startDate = InspectSchduleDef.getStartDate();
                if (null == startDate) {
                    //return this.showJsonResult(false, "请选择开始时间", (Object)null);
                }*/
               // riskCheckSchduleDef.setStartTime(startDate);
                InspectDef InspectDef = inspectDefService.getById(InspectSchduleDef.getInspectDefId());
                if(null!=InspectDef){
                    InspectSchduleDef.setRiskInspectType(InspectDef.getInspectDefTypeId());
                }
                InspectSchduleDefService.save(InspectSchduleDef);
            }else if ("random".equalsIgnoreCase(checkType)) {
                String person = InspectSchduleDef.getExecutor();
                if (StringUtils.isBlank(person)) {
                    return R.error("请选择执行人");
                }

                String[] personArr = person.split(",");
                if (null == personArr || personArr.length <= 0) {
                    return R.error("请选择执行人");
                }
             /*   List<Long> executor = (List)Arrays.stream(personArr).map((a) -> {
                    return Long.parseLong(a);
                }).collect(Collectors.toList());*/
                InspectSchduleDefService.save(InspectSchduleDef);
            }
            return R.ok();
        }else {
            return R.error("任务检查类型不能为空");
        }

    }

    /**
     * 修改
     */
    @ApiOperation(value = "修改")
    @PostMapping("/update")
    @RequiresPermissions("scyf:inspectschduledef:update")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "schduleDefId", value = "id", required = true, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "companyId", value = "所属企业", required = true, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "riskInspectPosition", value = "岗位", required = true, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "riskInspectGroup", value = "受检部门", required = true, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "inspectDefId", value = "检查类型：检查定义表Id", required = true, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "riskInspectDefTitle", value = "定义名称", required = true, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "riskInspectDefDesc", value = "定义描述", required = false, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "scheduleCronExpression", value = "定时任务表达式", required = true, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "executor", value = "执行人", required = false, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "riskInspectParticipant", value = "参与人", required = false, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "inspectType", value = "检查周期类型  临时性（random）、常规性（fixed）", required = true, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "inspectMode", value = "临时检查方式 1，现场检查2、基础检查", required = false, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "startDate", value = "检查开始时间", required = false, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "endDate", value = "检查结束时间", required = false, paramType = "query", dataType = "String")

    })
    public R update(@RequestBody InspectSchduleDef InspectSchduleDef){
        ValidatorUtils.validateEntity(InspectSchduleDef);
        Assert.isNull(InspectSchduleDef.getSchduleDefId(),"检查定义表ID不能为空");
        InspectSchduleDef.setUpdatedBy(getUserId());
        InspectSchduleDef.setUpdatedTime(new Date());
        InspectSchduleDefService.update(InspectSchduleDef);

        return R.ok();
    }

    /**
     * 删除
     */
    @ApiOperation(value = "删除")
    @RequestMapping(value="/delete",method = RequestMethod.DELETE)
    @RequiresPermissions("scyf:inspectschduledef:delete")
    @ApiImplicitParam(name = "schduleDefId", value = "检查任务定义表ID", required = true, paramType = "query", dataType = "int")
    public R delete(@RequestParam(value = "schduleDefId",defaultValue="0") Long  schduleDefId){
        Assert.isNull(schduleDefId,"检查表ID不能为空");
        InspectSchduleDefService.removeById(schduleDefId);
        return R.ok();
    }
    /**
     *根据类型查询检查表
     */
    @ApiOperation(value = "根据类型查询检查表",response = InspectDef.class)
    @GetMapping("/findInspectDefListByType")
    public R selectInspectDefListByType(){
        //1隐患类型；2检查表类型；4任务检查类型
        Map<String,Object> param = new HashMap<>();
        param.put("type","2");
        param.put("companyId",getCompanyId());
        param.put("classifyValue",1);
        List<InspectDef> inspectDefList = InspectSchduleDefService.selectInspectDefListByType(param);
        return R.ok().put("inspectDefList",inspectDefList);
    }

    @ApiOperation(value="暂停定时任务")
    @PostMapping("/pauseScheduleByScheduleId/{schduleDefId}")
    @ApiImplicitParam(name = "schduleDefId", value = "检查任务定义表ID", required = true, paramType = "query", dataType = "Long")
    @RequiresPermissions("scyf:inspectschduledef:pause")
    public R pauseSchedulaBySchduleDefId(@PathVariable("schduleDefId") Long  schduleDefId){
        boolean bool =false;
        try {
            bool= InspectSchduleDefService.pauseSchedulaBySchduleDefId(schduleDefId);
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
        if(bool){
            return R.ok();
        }else{
            return  R.error("停止任务失败！");
        }
    }

    @ApiOperation(value="启动定时任务")
    @PostMapping(value="/startScheduleByScheduleId/{schduleDefId}")
    @ApiImplicitParam(name = "schduleDefId", value = "检查任务定义表ID", required = true, paramType = "query", dataType = "Long")
    @RequiresPermissions("scyf:inspectschduledef:pause")
    public R startScheduleByScheduleId(@PathVariable("schduleDefId") Long  schduleDefId){
        boolean bool =false;
        try {
            bool= InspectSchduleDefService.startScheduleByScheduleId(schduleDefId);
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
        if(bool){
            return R.ok();
        }else{
            return  R.error("开启任务失败！");
        }
    }
}
