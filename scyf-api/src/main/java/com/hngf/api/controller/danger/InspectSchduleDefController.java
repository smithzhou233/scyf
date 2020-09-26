package com.hngf.api.controller.danger;

import com.alibaba.fastjson.JSON;
import com.hngf.api.common.annotation.RepeatSubmit;
import com.hngf.api.controller.BaseController;
import com.hngf.common.exception.ScyfException;
import com.hngf.common.utils.DateUtils;
import com.hngf.common.utils.R;
import com.hngf.common.validator.ValidatorUtils;
import com.hngf.entity.danger.InspectDef;
import com.hngf.entity.danger.InspectSchduleDef;
import com.hngf.service.danger.InspectSchduleDefService;
import com.hngf.service.utils.IdKit;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 检查任务定义
 *
 * @author yss
 * @email
 * @date 2020-05-20 17:36:23
 */
@RestController
@RequestMapping("/api/scyf/inspectschduledef")
@Api(value = "检查任务定义",tags = {"检查任务定义"})
public class InspectSchduleDefController extends BaseController {
    @Autowired
    private InspectSchduleDefService InspectSchduleDefService;

    /**
     * 保存
     */
    @PostMapping("/save")
    @ApiImplicitParams({
            //@ApiImplicitParam(name = "companyId", value = "所属企业", required = true, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "riskInspectPosition", value = "岗位", required = true, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "riskInspectGroup", value = "受检部门", required = true, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "groupId", value = "执行部门", required = true, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "inspectDefId", value = "检查类型：检查定义表Id", required = true, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "riskInspectDefTitle", value = "定义名称", required = true, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "riskInspectDefDesc", value = "定义描述", required = true, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "inspectMode", value = "临时检查（random）检查方式 1，现场检查2、基础检查  ", required = true, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "riskInspectType", value = "检查类型", required = true, paramType = "query", dataType = "int"),
            //@ApiImplicitParam(name = "inspectType", value = "检查周期类型  临时性（random）、常规性（fixed）", required = false, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "executor", value = "执行人", required = false, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "riskInspectParticipant", value = "参与人", required = false, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "startDate", value = "临时性-检查开始时间 必填", required = false, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "endDate", value = "临时性-检查结束时间  必填", required = false, paramType = "query", dataType = "String")
    })
    @ApiOperation(value="【临时任务】保存", produces="application/json",notes = "" +
            "检查定义接口如下：<br>" +
            "1、查询隐患类型：/sys/common/getCommonClassifyByType?classifyType=4 <br>"+
            "2、查询整改部门/验收部门/所属部门列表：/sys/common/getCompanyGroupLists <br>" +
            "3、查询整改人员/验收人员列表：sys/common/getUserByGroupId <br>"+
            "4、根据groupId查询岗位 sys/common/getPositionListByGroupId"+
            "5、查询该机构下所有人员  sys/common/getAllAccountByCompanyId"
    )
    @RepeatSubmit()
    public R save(@RequestBody Map paramMap){
        Object startDateObj = paramMap.get("startDate");
        Object endDateObj = paramMap.get("endDate");
        if(null == startDateObj ){
            throw  new ScyfException("临时性-检查开始时间不能为空！");
        }
        if(null == endDateObj ){
            throw  new ScyfException("临时性-检查结束时间不能为空！");
        }

        String startDateStr = startDateObj.toString();
        String endDateStr = endDateObj.toString();
        paramMap.put("startDate", DateUtils.stringToDate(startDateStr,DateUtils.DATE_TIME_PATTERN));
        paramMap.put("endDate", DateUtils.stringToDate(endDateStr,DateUtils.DATE_TIME_PATTERN));
        LocalDateTime ldtstart = LocalDateTime.parse(startDateStr, DateTimeFormatter.ofPattern(DateUtils.DATE_TIME_PATTERN)) ;
        LocalDateTime ldtend = LocalDateTime.parse(endDateStr, DateTimeFormatter.ofPattern(DateUtils.DATE_TIME_PATTERN)) ;
        if(ldtend.isBefore(ldtstart)){
            throw new ScyfException("临时性-检查结束时间不能再开始时间之前！");
        }

        InspectSchduleDef inspectSchduleDef =  JSON.parseObject(JSON.toJSONString(paramMap), InspectSchduleDef.class);
        Long id  = IdKit.getInspectSchduleDefId();
        inspectSchduleDef.setSchduleDefId(id);
        inspectSchduleDef.setInspectType("random");//检查周期类型  临时性（random）、常规性（fixed）
        inspectSchduleDef.setCreatedBy(getUserId());
        inspectSchduleDef.setCompanyId(getCompanyId());
        // InspectSchduleDef.setGroupId(getGroupId());

        inspectSchduleDef.setDelFlag(0);
        ValidatorUtils.validateEntity(inspectSchduleDef);
        InspectSchduleDefService.save(inspectSchduleDef);
        return R.ok();
    }

    /**
     *根据类型查询检查表
     */
    @ApiOperation(value = "根据类型查询检查表")
    @GetMapping("/findInspectDefListByType")
    public R selectInspectDefListByType(){
        //1隐患类型；2检查表类型；4任务检查类型
        Map<String,Object> param = new HashMap<>();
        param.put("type","2");
        param.put("companyId",getCompanyId());
        List<InspectDef> inspectDefList = InspectSchduleDefService.selectInspectDefListByType(param);
        return R.ok().put("data",inspectDefList);
    }
}
