package com.hngf.web.controller.risk;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.hngf.common.validator.Assert;
import com.hngf.entity.risk.Risk;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.hngf.entity.risk.RiskRecord;
import com.hngf.service.risk.RiskRecordService;
import com.hngf.common.utils.PageUtils;
import com.hngf.common.utils.R;
import com.hngf.common.validator.ValidatorUtils;



/**
 * 风险点关联风险表
 *
 * @author zhangfei
 * @email 
 * @date 2020-05-20 17:36:24
 */
@RestController
@RequestMapping("scyf/riskrecord")
@Api(value="风险点关联风险数据",tags = {"风险点关联风险数据"})
public class RiskRecordController {
    @Autowired
    private RiskRecordService RiskRecordService;

    /**
     * 列表
     */
    @GetMapping(value = "/list")
    @RequiresPermissions("scyf:riskrecord:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = RiskRecordService.queryPage(params);

        return R.ok().put("data", page);
    }

    /**
     * 风险点详情 - 风险表
     */
    @GetMapping(value = "/getRiskList")
    @RequiresPermissions(value = {"scyf:riskpoint:save","scyf:riskpointmap:save","scyf:riskpointdangersource:save","scyf:riskrecord:list"},logical = Logical.OR)
    @ApiOperation(value="查询风险点关联的风险数据",response = Risk.class)
    @ApiImplicitParam(name = "riskPointId", value = "风险点ID", required = true, paramType = "form", dataType = "int")
    public R getRiskList(@RequestParam(required = true) Long riskPointId){
        Assert.isNull(riskPointId,"风险点ID不能为空");
        List<Risk> list = RiskRecordService.getRiskByRiskPointId(riskPointId);
        return R.ok().put("data", list);
    }

    /**
     * 查询风险点安全检查表
     */
    @RequestMapping(value = "/getMeasureList",method = RequestMethod.GET)
    @ApiOperation(value="查询风险点关联的风险数据",notes="")
    @ApiImplicitParam(name = "riskPointId", value = "风险点ID", required = true, paramType = "form", dataType = "int")
    public R getMeasureList(Long riskPointId){
        Assert.isNull(riskPointId,"风险点ID不能为空");
        return R.ok().put("data", RiskRecordService.getMeasureList(riskPointId));
    }


    /**
     * 信息
     */
    @GetMapping(value="/info/{riskId}")
    @RequiresPermissions("scyf:riskrecord:info")
    public R info(@PathVariable("riskId") Long riskId){
        RiskRecord RiskRecord = RiskRecordService.getById(riskId);

        return R.ok().put("data", RiskRecord);
    }

    /**
     * 保存
     */
    @PostMapping("/save")
    @RequiresPermissions("scyf:riskrecord:save")
    public R save(@RequestBody RiskRecord RiskRecord){

        ValidatorUtils.validateEntity(RiskRecord);
        RiskRecordService.save(RiskRecord);

        return R.ok();
    }

    /**
     * 修改
     */
    @PostMapping("/update")
    @RequiresPermissions("scyf:riskrecord:update")
    public R update(@RequestBody RiskRecord RiskRecord){
        ValidatorUtils.validateEntity(RiskRecord);
        RiskRecordService.update(RiskRecord);
        
        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping(value="/delete",method = RequestMethod.DELETE)
    @RequiresPermissions("scyf:riskrecord:delete")
    public R delete(@RequestBody Long[] riskIds){
        RiskRecordService.removeByIds(Arrays.asList(riskIds));

        return R.ok();
    }

}
