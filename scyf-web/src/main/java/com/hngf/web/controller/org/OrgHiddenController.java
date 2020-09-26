package com.hngf.web.controller.org;


import com.hngf.common.utils.Constant;
import com.hngf.common.utils.PageUtils;
import com.hngf.common.utils.R;
import com.hngf.entity.sys.Industry;
import com.hngf.service.danger.HiddenService;
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

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 监管-- 隐患查询
 */
@RestController
@RequestMapping("/org/hidden")
@Api(value = "【监管】-行业大数据-",tags = {"【监管】-行业大数据-重大隐患"} )
public class OrgHiddenController extends BaseController {
    @Autowired
    private HiddenService hiddenService;
    @Autowired
    private IndustryService industryService;

    @GetMapping({"/query"})
    @ApiOperation(value="重大隐患" , notes = "重大隐患列表 ：</br> " +
            "搜索条件：行业下拉框-- /org/org/orgIndustryList ; 企业下拉框：/org/company/findCompanyListByIndustryCode  行业、企业下拉框为二级联动</br> " +
            "返回属性列表：</br> " +
            "图片：path + imgUrl </br>" +
            "所属企业： companyName </br>" +
            "风险点名称：riskPointName </br>" +
            "隐患标题：hiddenTitle </br>" +
            "隐患等级：hiddenLevelStr </br>" +
            "隐患状态：disposeStatusStr </br>" +
            "审批负责人：hiddenReviewByName </br>" +
            "整改负责人：hiddenRetifyByName </br>" +
            "验收负责人：hiddenAcceptedByName </br>" +
            "整改期限(倒计时)：status =4 时显示 已整改； status !=4 时显示；hiddenRetifyDeadline 在当前时间之前的，整改已超时，在当前时间之后的 与当前时间相差的天数和小时 </br>" +
            "发现日期：happenedTimeStr </br>" +
            "结束日期：finishedTimeStr </br>" +
            "")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum", value = "第几页", required = true, paramType = "query", dataType = "int", defaultValue = "1"),
            @ApiImplicitParam(name = "pageSize", value = "每页多少条数据", required = true, paramType = "query", dataType = "int", defaultValue = "10"),
            @ApiImplicitParam(name = "industryCode", value = "行业编码",paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "companyId", value = "企业Id",paramType = "query", dataType = "long"),
            @ApiImplicitParam(name = "status", value = "隐患状态",paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "keyword", value = "查询关键字", paramType = "query", dataType = "String")
    })
    public R query(@ApiIgnore @RequestParam(required = false) Map<String, Object> map, HttpServletRequest req) {
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
        PageUtils page= hiddenService.queryHiddenListByCompanyId(map,pageNum,pageSize,null);
        return R.ok().put("data",page).put("path",req.getScheme() + "://" + req.getServerName() + ":" + req.getServerPort() + "/source");
    }

}
