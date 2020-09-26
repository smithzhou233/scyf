package com.hngf.api.controller.danger;

import com.hngf.api.common.utils.DateUtils;
import com.hngf.api.controller.BaseController;
import com.hngf.common.utils.R;
import com.hngf.common.validator.Assert;
import com.hngf.entity.danger.HiddenAccept;
import com.hngf.service.danger.HiddenService;
import com.hngf.service.danger.HiddenStatisticsService;
import com.hngf.service.danger.InspectSchduleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * yanshanshan
 */
@RestController
@RequestMapping("/api/scyf/hiddenStatistics")
@Api(value="隐患统计",tags = {"隐患统计"})
public class HiddenStatisticsController  extends BaseController {

    @Autowired
    private HiddenStatisticsService hiddenStatisticsService;

    @Autowired
    private InspectSchduleService inspectSchduleService;
    @Autowired
    private HiddenService hiddenService;


    @GetMapping(value="/getHiddenLvlCount")
    @ApiOperation(value="隐患tab统计查询",response = HiddenAccept.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "module", value = "类型 1.我的 2.部门 ", required = true, paramType = "query", dataType = "int")
    })
    public R getHiddenLvlCount(@RequestParam Map<String, Object> params){
        params.put("companyId",getCompanyId());
        params.put("userId",getUserId());
        return R.ok().put("data", hiddenStatisticsService.hiddenLvlCount(params));
    }

    @GetMapping(value="/getHomePageTopCount")
    @ApiOperation(value="首页统计查询",response = HiddenAccept.class)
    public R getHomePageTopCount(){
        Map<String ,Object> resultMap = new HashMap<>();
        //首页   待办任务/逾期任务/逾期隐患 数量统计
        Map<String ,Object> paramsMap = new HashMap<>();
        //0待检查， 3已逾期
        paramsMap.put("userId",getUserId());
        paramsMap.put("companyId",getCompanyId());
        paramsMap.put("groupId",getGroupId());
        paramsMap.put("positionId",getPositionId());
        paramsMap.put("action",0);
        Integer undoCount = inspectSchduleService.getMyTaskCoutForAPI(paramsMap);
        resultMap.put("undoCount",undoCount);
        paramsMap.put("action",3);    //逾期查询一周内的记录
        //paramsMap.put("startDate", DateUtils.dateFormat(DateUtils.getStartTime(), "yyyy-MM-dd HH:mm:ss"));
        paramsMap.put("startDate",  DateUtils.getBeforeDay(7) );
        paramsMap.put("api", "yes");
        Integer overDueCount = inspectSchduleService.getMyTaskCoutForAPI(paramsMap);
        resultMap.put("overDueCount",overDueCount);
        paramsMap.put("type",0);
        paramsMap.put("module",1);
        paramsMap.put("overTimeRetify",1);
        Integer overHiddenCount= hiddenService.findHomePageHiddenOverdueCount(paramsMap);
       resultMap.put("overHiddenCount",overHiddenCount);
       return R.ok().put("data",resultMap);
    }


}
