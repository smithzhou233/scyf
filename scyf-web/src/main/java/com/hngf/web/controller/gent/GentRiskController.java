package com.hngf.web.controller.gent;

import com.hngf.common.utils.Constant;
import com.hngf.common.utils.PageUtils;
import com.hngf.common.utils.ParamUtils;
import com.hngf.common.utils.R;
import com.hngf.common.validator.Assert;
import com.hngf.dto.danger.RiskSourceDto;
import com.hngf.service.risk.RiskPointService;
import com.hngf.service.risk.RiskService;
import com.hngf.web.controller.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Api(value = "【集团】风险管理",tags = {"【集团】风险管理"})
@RestController
@RequestMapping("gent/risk")
public class GentRiskController  extends BaseController {

    @Autowired
    RiskService riskService;

    @Autowired
    RiskPointService riskPointService;

    @GetMapping("/list")
    @ApiOperation(value = "列表查询",notes = "查询详情接口：<br>" +
            "scyf/riskpoint/info(回显风险点索引表信息)")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "keyword", value = "查询关键字", required = false, paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "companyId", value = "企业ID", required = true, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "hiddenLevel", value = "隐患等级", required = false, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "isOutOfControl", value = "是否失控 1失控 0受控", required = false, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "isActive", value = "是否激活：1激活；0未激活", required = false, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "pageNum", value = "第几页", required = true, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "pageSize", value = "每页多少条数据", required = true, paramType = "query", dataType = "int")
    })
    public R queryPage(@RequestParam Map<String, Object> params) {
        Assert.isNull(ParamUtils.paramsToLong(params,"companyId"),"企业ID不能为空");
        int pageNum = null!= params.get(Constant.PAGE_PAGENUM) ? Integer.parseInt(params.get(Constant.PAGE_PAGENUM).toString()) : 1;
        int pageSize = null != params.get(Constant.PAGE_PAGESIZE) ? Integer.parseInt(params.get(Constant.PAGE_PAGESIZE).toString()) : Constant.PAGE_SIZE;
        PageUtils page = riskPointService.getRiskPointPageForGent(params,pageNum,pageSize,"");
        return R.ok().put("data",page);
    }

    @ApiOperation(value = "查询风险库列表", notes="查询设备风险库列表",response = RiskSourceDto.class)
    @GetMapping({"/queryRiskList"})
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query",name = "pageNum",dataType = "Long",required = true,value = "当前页码",defaultValue = "1"),
            @ApiImplicitParam(paramType = "query",name = "pageSize",dataType = "Long",required = true,value = "每页显示记录数",defaultValue = "10"),
            @ApiImplicitParam(name = "riskDangerType", value = "危险源类型", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = "dangerSrcId", value = "根节点", paramType = "query", required = false, dataType = "int"),
            @ApiImplicitParam(name = "riskCtrlLevelId", value = "管控层级", paramType = "query", required = false, dataType = "int"),
            @ApiImplicitParam(name = "riskCtrlPositionId", value = "管控岗位", paramType = "query", required = false, dataType = "int"),
            @ApiImplicitParam(name = "keyword", value = "风险名称", paramType = "query", required = false, dataType = "string"),
            @ApiImplicitParam(name = "riskLevel", value = "风险等级", paramType = "query", required = false, dataType = "string"),
            @ApiImplicitParam(name = "vtype", paramType = "query", required = false, dataType = "string"),
    })
    public R queryRiskList(@RequestParam Map<String, Object> map) {
        if (map.containsKey("dangerSrcId") && !String.valueOf(map.get("dangerSrcId")).equals("")) {
            map.put("vtype", "pd");
            map.put("vDangerSrcId", map.get("dangerSrcId"));
            map.remove("dangerSrcId");
        }
        map.put("companyId",getCompanyId());
        PageUtils page = riskService.queryPage(map);
        return R.ok().put("data", page);
    }

}
