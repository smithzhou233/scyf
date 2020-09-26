package com.hngf.web.controller.org;

import com.hngf.common.exception.ScyfException;
import com.hngf.common.utils.Constant;
import com.hngf.common.utils.PageUtils;
import com.hngf.common.utils.ParamUtils;
import com.hngf.common.utils.R;
import com.hngf.common.validator.Assert;
import com.hngf.entity.risk.RiskPoint;
import com.hngf.entity.sys.Industry;
import com.hngf.service.risk.RiskPointService;
import com.hngf.service.sys.IndustryService;
import com.hngf.web.controller.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/org/riskpoint")
@Api(value = "【监管】-行业大数据",tags = {"【监管】-行业大数据-风险点"})
public class OrgRiskPointController  extends BaseController {
    @Autowired
    private IndustryService industryService;

    @Autowired
    private RiskPointService riskPointService;

    @GetMapping({"/query"})
    @ApiOperation(value="【监管】-重大风险点",notes = "重大风险点列表 </br>" +
            "搜索条件：行业下拉框-- /org/org/orgIndustryList ; 企业下拉框：/org/company/findCompanyListByIndustryCode  行业、企业下拉框为二级联动</br>" +
            "风险点名称：industryName </br>" +
            "标识：riskPointType  1 设备设施 2 作业活动 </br>" +
            "所属行业名称：industryName </br>" +
            "所属企业名称：companyName </br>" +
            "风险点等级：riskPointLevel </br>" +
            "是否激活：isActive 1激活；0未激活 </br>" +
            "状态：isOutOfControl 1预警0受控 </br>" +
            "风险数目：riskNumber </br>" +
            "隐患数量：hdangerNumber </br>")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum", value = "第几页", required = true, paramType = "query", dataType = "int", defaultValue = "1"),
            @ApiImplicitParam(name = "pageSize", value = "每页多少条数据", required = true, paramType = "query", dataType = "int", defaultValue = "10"),
            @ApiImplicitParam(name = "industryCode", value = "行业编码",paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "companyId", value = "企业Id",paramType = "query", dataType = "long"),
            @ApiImplicitParam(name = "isOutOfControl", value = "风险点状态 1预警0受控  ",paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "keyword", value = "查询关键字", paramType = "query", dataType = "String")
    })
    public R query(@ApiIgnore @RequestParam(required = false) Map<String, Object> map) {
        if(null == map ){
            map = new HashMap<>();
        }
        Object industryCode = map.get("industryCode");
        if (null == industryCode || StringUtils.isBlank(industryCode.toString())) {
            List<Industry> IndustyTypeCodeList = this.industryService.getIndustryTreeByOrgId(getCompanyId());
            if (null == IndustyTypeCodeList || IndustyTypeCodeList.size() == 0) {
                return R.ok("暂无数据");
            }
            map.put("industyTypeCodeList", IndustyTypeCodeList);
        }
        map.put("riskPointLevel", 1);
        int pageNum = null!= map.get(Constant.PAGE_PAGENUM) ? Integer.parseInt(map.get(Constant.PAGE_PAGENUM).toString()) : 1;
        int pageSize = null != map.get(Constant.PAGE_PAGESIZE) ? Integer.parseInt(map.get(Constant.PAGE_PAGESIZE).toString()) : Constant.PAGE_SIZE;
        PageUtils page= riskPointService.getRiskPointListByCompanyId(map,pageNum,pageSize,null);
        return R.ok().put("data",page);
    }

    /**
     *监管级-重大风险点
     * @param params
     * @return
     */
    @GetMapping("/getRiskPointDetail")
    @ApiOperation(value="【监管】-重大风险点详情",tags = "重大风险点详情", response = RiskPoint.class)
    @ApiImplicitParam(name = "riskPointId", value = "风险点ID",required = true, paramType = "query", dataType = "long")
    public R getRiskPointDetails(@ApiIgnore @RequestParam(required = false) Map<String, Object> params) {
        if(null == params ){throw new ScyfException("风险点ID不能为空"); }
        this.hasKey(params, new String[]{"riskPointId"});
        Assert.isNull(params.get("riskPointId"),"riskPointId不能为空");
        return R.ok().put("data",riskPointService.riskPointDetailsAll(ParamUtils.paramsToLong(params,"riskPointId")));
    }
}
