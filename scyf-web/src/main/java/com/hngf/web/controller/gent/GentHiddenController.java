package com.hngf.web.controller.gent;

import com.hngf.common.utils.Constant;
import com.hngf.common.utils.ParamUtils;
import com.hngf.common.utils.R;
import com.hngf.common.validator.Assert;
import com.hngf.entity.danger.Hidden;
import com.hngf.service.danger.HiddenService;
import com.hngf.web.common.annotation.ApiJsonProperty;
import com.hngf.web.common.annotation.ApiParameterJsonObject;
import com.hngf.web.controller.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * 集团 -- 隐患查询
 */
@RestController
@RequestMapping("/gent/hidden")
@Api(value = "【集团】隐患管理",tags = {"【集团】隐患管理"})
public class GentHiddenController extends BaseController {

    @Autowired
    private HiddenService hiddenService;

    @GetMapping("/list")
    @ApiOperation(value = "列表查询",response = Hidden.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "keyword", value = "查询关键字", required = false, paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "companyId", value = "企业ID", required = true, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "hiddenLevel", value = "隐患等级", required = false, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "pageNum", value = "第几页", required = true, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "pageSize", value = "每页多少条数据", required = true, paramType = "query", dataType = "int")
    })
    public R queryPage(@RequestParam Map<String, Object> params) {
        Assert.isNull(ParamUtils.paramsToLong(params,"companyId"),"企业ID不能为空");
        int pageNum = null!= params.get(Constant.PAGE_PAGENUM) ? Integer.parseInt(params.get(Constant.PAGE_PAGENUM).toString()) : 1;
        int pageSize = null != params.get(Constant.PAGE_PAGESIZE) ? Integer.parseInt(params.get(Constant.PAGE_PAGESIZE).toString()) : Constant.PAGE_SIZE;
        return R.ok().put("data",hiddenService.queryRiskPointHiddenListForGent(params,pageNum,pageSize,""));
    }

    @GetMapping("/details")
    @ApiOperation(value = "隐患详情",response = Hidden.class,notes = "" +
            "隐患信息详情接口关联接口如下：<br>" +
            "1、查询评审记录：scyf/hidden/getReviewRecord <br>" +
            "2、查询整改记录：scyf/hidden/getRetifyRecord <br>" +
            "3、查询整改验收记录：scyf/hidden/getAcceptRecord <br>")
    @ApiImplicitParam(name = "hiddenId", value = "隐患ID", required = true, paramType = "query", dataType = "int")
    public R details(Long hiddenId) {
        Assert.isNull(hiddenId,"隐患ID不能为空");
        return R.ok().put("data", hiddenService.getById(hiddenId));
    }
    //@ApiOperation(value="监管级-主页-即将逾期任务-督导")


    @ApiOperation(value="【集团级】--首页隐患督导")
    @PostMapping("/toSupervise")
    public R toSupervise(@ApiParameterJsonObject(name="params",
            value = {@ApiJsonProperty(key = "hiddenOrSchduleIds",value = "隐患/任务id",  required = true ,type = "long"),
                    @ApiJsonProperty(key = "hiddenOrSchduleTitle",value = "隐患/任务标题",  required = true ),
                    @ApiJsonProperty(key = "hiddenLvl",value = "隐患等级", type = "int"),
                    @ApiJsonProperty(key = "superviseGroupId",value = "隐患部门Id",  required = true ,type = "long"),
                    @ApiJsonProperty(key = "superviseType",value = "督导类型 1.隐患  2.任务",  required = true ,type = "int")
            })  @RequestBody Map<String, Object> params) {
        Assert.isNull(params.get("superviseType"),"督导类型不能为空");
        Assert.isNull(params.get("superviseToUserId"),"执行人不能为空");
        Assert.isNull(params.get("hiddenOrSchduleIds"),"隐患/任务id不能为空");
        params.put("companyId", getCompanyId());
        params.put("currentUser",getUser().getUserName());
        params.put("currentUserType",getUser().getUserType());
        hiddenService.toSupervise(params);
        return R.ok().put("data","督导消息下发成功");
    }
}
