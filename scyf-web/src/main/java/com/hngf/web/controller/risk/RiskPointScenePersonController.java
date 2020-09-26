package com.hngf.web.controller.risk;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import com.hngf.common.utils.Constant;
import com.hngf.common.validator.Assert;
import com.hngf.web.controller.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.hngf.entity.risk.RiskPointScenePerson;
import com.hngf.service.risk.RiskPointScenePersonService;
import com.hngf.common.utils.PageUtils;
import com.hngf.common.utils.R;
import com.hngf.common.validator.ValidatorUtils;



/**
 * 风险点 现场人员
 *
 * @author hngf
 * @email 
 * @date 2020-05-20 17:36:24
 */
@RestController
@RequestMapping("scyf/riskpointsceneperson")
@Api(value="风险点现场人员管理",tags = {"风险点现场人员管理"})
public class RiskPointScenePersonController extends BaseController {
    @Autowired
    private RiskPointScenePersonService RiskPointScenePersonService;

    /**
     * 列表
     */
    /*@GetMapping(value = "/list")
    @RequiresPermissions("scyf:riskpointsceneperson:list")
    public R list(@RequestParam Map<String, Object> params){
        int pageNum = null!= params.get(Constant.PAGE_PAGENUM) ? Integer.parseInt(params.get(Constant.PAGE_PAGENUM).toString()) : 1;
        int pageSize = null != params.get(Constant.PAGE_PAGESIZE) ? Integer.parseInt(params.get(Constant.PAGE_PAGESIZE).toString()) : Constant.PAGE_SIZE;
        params.put("companyId", getCompanyId());
        PageUtils page = RiskPointScenePersonService.queryPage(params,pageNum,pageSize,null);
        return R.ok().put("data", page);
    }*/

    /**
     * 查询已关联的现场人员信息
     */
    @GetMapping(value = "/getSelected")
    @ApiOperation(value="查询已关联的现场人员信息",response = RiskPointScenePerson.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "riskPointId", value = "风险点ID", required = true, paramType = "form", dataType = "int"),
            @ApiImplicitParam(name = "pageNum", value = "第几页", required = true, paramType = "form", dataType = "int"),
            @ApiImplicitParam(name = "pageSize", value = "每页多少条数据", required = true, paramType = "form", dataType = "int"),
            @ApiImplicitParam(name = "keyword", value = "查询关键字", required = false, paramType = "form", dataType = "string")
    })
    public R getSelected(@RequestParam Map<String, Object> params){
        int pageNum = null!= params.get(Constant.PAGE_PAGENUM) ? Integer.parseInt(params.get(Constant.PAGE_PAGENUM).toString()) : 1;
        int pageSize = null != params.get(Constant.PAGE_PAGESIZE) ? Integer.parseInt(params.get(Constant.PAGE_PAGESIZE).toString()) : Constant.PAGE_SIZE;
        if (null == params.get("riskPointId") || "".equals(params.get("riskPointId").toString())) {
            return R.error("风险点ID不能为空");
        }
        PageUtils page = RiskPointScenePersonService.queryPage(params,pageNum,pageSize,null);

        return R.ok().put("data", page);
    }


    /**
     * 信息
     */
    /*@RequestMapping(value="/info/{riskPointId}",method = RequestMethod.GET)
    @RequiresPermissions("scyf:riskpointsceneperson:info")
    public R info(@PathVariable("riskPointId") Long riskPointId){
        RiskPointScenePerson RiskPointScenePerson = RiskPointScenePersonService.getById(riskPointId);

        return R.ok().put("data", RiskPointScenePerson);
    }*/

    /**
     * 保存或删除现场人员
     */
    @PostMapping("/saveOrDelete")
    @RequiresPermissions("scyf:riskpointsceneperson:save")
    @ApiOperation(value="保存或删除现场人员")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "riskPointId", value = "风险点ID", required = true, paramType = "json", dataType = "int"),
            @ApiImplicitParam(name = "status", value = "状态：1保存，0删除", required = true, paramType = "json", dataType = "int"),
            @ApiImplicitParam(name = "userId", value = "用户ID", required = true, paramType = "json", dataType = "int")
    })
    public R save(@RequestBody RiskPointScenePerson RiskPointScenePerson){

        Assert.isNull(RiskPointScenePerson.getRiskPointId(),"风险点ID不能为空");
        Assert.isNull(RiskPointScenePerson.getStatus(),"状态status不能为空");
        Assert.isNull(RiskPointScenePerson.getUserId(),"用户ID不能为空");

        int status = RiskPointScenePerson.getStatus().intValue();

        //status == 1 保存选中用户关联
        if (status == 1) {
            //保存之前查询是否有记录
            Map<String, Object> params = new HashMap<>();
            params.put("riskPointId",RiskPointScenePerson.getRiskPointId());
            params.put("userId",RiskPointScenePerson.getUserId());
            RiskPointScenePerson r = RiskPointScenePersonService.getByAccountAndRiskpointId(params);
            if (null == r) {
                RiskPointScenePerson.setCompanyId(getCompanyId());
                RiskPointScenePerson.setStatus(0);
                RiskPointScenePersonService.save(RiskPointScenePerson);
            }
            return R.ok("选择成功");
        }else if(status == 0){//删除用户关联
            Map<String, Object> params = new HashMap<>();
            params.put("riskPointId",RiskPointScenePerson.getRiskPointId());
            params.put("userId",RiskPointScenePerson.getUserId());
            int count = RiskPointScenePersonService.removeByMap(params);
            return count > 0 ? R.ok("取消成功") : R.error("取消失败");
        }else{
            return R.error("状态status的值不正常，应该为1或0");
        }
    }

    /**
     * 上线下线
     */
    @PostMapping("/update")
    @RequiresPermissions("scyf:riskpointsceneperson:update")
    @ApiOperation(value="上线下线")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "riskPointId", value = "风险点ID", required = true, paramType = "json", dataType = "int"),
            @ApiImplicitParam(name = "status", value = "状态：1上线，0下线", required = true, paramType = "json", dataType = "int"),
            @ApiImplicitParam(name = "userId", value = "用户ID", required = true, paramType = "json", dataType = "int")
    })
    public R update(@RequestBody RiskPointScenePerson RiskPointScenePerson){
        Assert.isNull(RiskPointScenePerson.getRiskPointId(),"风险点ID不能为空");
        Assert.isNull(RiskPointScenePerson.getStatus(),"状态status不能为空");
        Assert.isNull(RiskPointScenePerson.getUserId(),"用户ID不能为空");
        RiskPointScenePersonService.update(RiskPointScenePerson);
        return R.ok();
    }

    /**
     * 删除
     */
    /*@RequestMapping(value="/delete",method = RequestMethod.DELETE)
    @RequiresPermissions("scyf:riskpointsceneperson:delete")
    public R delete(@RequestBody Long[] riskPointIds){
        RiskPointScenePersonService.removeByIds(Arrays.asList(riskPointIds));

        return R.ok();
    }*/

}
