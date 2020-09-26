package com.hngf.api.controller.riskpoint;

import com.hngf.api.controller.BaseController;
import com.hngf.common.utils.Constant;
import com.hngf.common.utils.ParamUtils;
import com.hngf.common.utils.R;
import com.hngf.common.validator.Assert;
import com.hngf.entity.risk.RiskInspectRecord;
import com.hngf.entity.risk.RiskPointCheckRecord;
import com.hngf.service.risk.RiskInspectRecordService;
import com.hngf.service.risk.RiskPointCheckRecordService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

/**
 * 风险点检查记录
 * @author hngf
 * @email 
 * @date 2020-07-14
 */
@RestController
@RequestMapping("api/riskpointcheckrecord")
@Api(value = "【API】-风险点检查记录",tags = "【API】-风险点检查记录")
public class RiskPointCheckRecordController extends BaseController {
    @Autowired
    private RiskPointCheckRecordService riskPointCheckRecordService;
    @Autowired
    private RiskInspectRecordService riskInspectRecordService;

    /**
     * 风险点检查记录【分页查询（我的管控的、部门）】
     */
    @GetMapping("/list")
    @ApiOperation(value = "风险点检查记录【分页查询（我的管控的、部门）】",response = RiskPointCheckRecord.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum", value = "第几页", required = true, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "pageSize", value = "每页多少条数据", required = true, paramType = "query", dataType = "int"),
            @ApiImplicitParam(
                    paramType = "query",
                    name = "keyword",
                    dataType = "String",
                    required = false,
                    value = "关键字搜索",
                    defaultValue = ""
            ), @ApiImplicitParam(
            paramType = "query",
            name = "module",
            dataType = "int",
            required = true,
            value = "1=我的，2=部门，3任务的/历史任务，4风险点详情",
            defaultValue = "2"
            ), @ApiImplicitParam(
                    paramType = "query",
                    name = "groupId",
                    dataType = "int",
                    required = false,
                    value = "全部（-1）,部门ID"
            ), @ApiImplicitParam(
                    paramType = "query",
                    name = "riskPointLevel",
                    dataType = "int",
                    required = false,
                    value = "全部（-1）,1重大,2较大,3一般,4较低",
                    defaultValue = ""
            ), @ApiImplicitParam(
                    paramType = "query",
                    name = "result",
                    dataType = "int",
                    required = false,
                    value = "全部（-1）,1通过；2不通过；3存在隐患；4不涉及",
                    defaultValue = ""
            ), @ApiImplicitParam(
                    paramType = "query",
                    name = "riskPointId",
                    dataType = "int",
                    required = false,
                    value = "风险点ID（风险点详情的查询条件）",
                    defaultValue = ""
            ), @ApiImplicitParam(
                    paramType = "query",
                    name = "inspectDefId",
                    dataType = "int",
                    required = false,
                    value = "检查表ID",
                    defaultValue = ""
            ), @ApiImplicitParam(
                paramType = "query",
                name = "inspectScheduleId",
                dataType = "int",
                required = true,
                value = "任务ID,module=3时必传（查询任务下的风险点检查记录）",
                defaultValue = ""
            ), @ApiImplicitParam(
                    paramType = "query",
                    name = "dateTime",
                    dataType = "String",
                    required = false,
                    value = "日期（2020-07-14）",
                    defaultValue = ""
            )
    })
    public R list(@RequestParam Map<String, Object> params){

        int pageNum = null!= params.get(Constant.PAGE_PAGENUM) ? Integer.parseInt(params.get(Constant.PAGE_PAGENUM).toString()) : 1;
        int pageSize = null != params.get(Constant.PAGE_PAGESIZE) ? Integer.parseInt(params.get(Constant.PAGE_PAGESIZE).toString()) : Constant.PAGE_SIZE;

        if (ParamUtils.paramsToString(params,"riskPointLevel").equals("-1")) {
            params.remove("riskPointLevel");
        }
        if (ParamUtils.paramsToString(params,"checkedResult").equals("-1")) {
            params.remove("checkedResult");
        }
        //当module=3时 inspectScheduleId 必传
        if (ParamUtils.paramsToString(params,"module").equals("3")) {
            Assert.isBlank(ParamUtils.paramsToString(params,"inspectScheduleId"),"任务ID不能为空");
        }
        //当module=2时  查询本部门下所有任务
        if (ParamUtils.paramsToString(params,"module").equals("2")) {
           params.put("groupId",getGroupId());
        }
        params.put("userId",getUserId());
        params.put("companyId", getCompanyId());
        return R.ok().put("data", riskPointCheckRecordService.getListPage(params,pageNum,pageSize,null));
    }


    /**
     * 风险点检查记录
     * @param params
     * @return
     */
    @GetMapping("/getCheckedRecordByRiskPointId")
    @ApiOperation(value = "【风险点下的检查日志】分页查询",response = RiskInspectRecord.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum", value = "第几页", required = true, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "pageSize", value = "每页多少条数据", required = true, paramType = "query", dataType = "int"),
            @ApiImplicitParam(
                    paramType = "query",
                    name = "riskPointId",
                    dataType = "int",
                    required = false,
                    value = "风险点ID（风险点详情的查询条件）",
                    defaultValue = ""
            ), @ApiImplicitParam(
                    paramType = "query",
                    name = "inspectDefId",
                    dataType = "int",
                    required = false,
                    value = "检查表ID",
                    defaultValue = ""
            ), @ApiImplicitParam(
                    paramType = "query",
                    name = "inspectScheduleId",
                    dataType = "int",
                    required = true,
                    value = "历史任务ID（历史任务记录）",
                    defaultValue = ""
            ),@ApiImplicitParam(
                paramType = "query",
                name = "inspectRecordNo",
                dataType = "int",
                required = false,
                value = "检查记录编号"
            )
    })
    public R getCheckedRecordByRiskPointId(@RequestParam Map<String, Object> params){

        int pageNum = null!= params.get(Constant.PAGE_PAGENUM) ? Integer.parseInt(params.get(Constant.PAGE_PAGENUM).toString()) : 1;
        int pageSize = null != params.get(Constant.PAGE_PAGESIZE) ? Integer.parseInt(params.get(Constant.PAGE_PAGESIZE).toString()) : Constant.PAGE_SIZE;

        Assert.isBlank(ParamUtils.paramsToString(params,"inspectScheduleId"),"任务ID不能为空");

        params.put("companyId", getCompanyId());

        return R.ok().put("data", riskInspectRecordService.getRiskInspectRecordPage(params,pageNum,pageSize,null));
    }

}
