package com.hngf.web.controller.risk;

import java.util.Arrays;
import java.util.Date;
import java.util.Map;

import com.hngf.common.utils.Constant;
import com.hngf.web.controller.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.hngf.entity.risk.RiskInspectRecord;
import com.hngf.service.risk.RiskInspectRecordService;
import com.hngf.common.utils.PageUtils;
import com.hngf.common.utils.R;
import com.hngf.common.validator.ValidatorUtils;



/**
 * 检查记录表
 *
 * @author hngf
 * @email 
 * @date 2020-05-20 17:36:23
 */
@RestController
@RequestMapping("scyf/riskinspectrecord")
@Api(value = "检查记录",tags = {" 检查记录"})
public class RiskInspectRecordController extends BaseController {
    @Autowired
    private RiskInspectRecordService RiskInspectRecordService;

    /**
     * 列表
     */
    @RequestMapping(value = "/list",method = RequestMethod.GET)
    @RequiresPermissions("scyf:riskinspectrecord:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = RiskInspectRecordService.queryPage(params);

        return R.ok().put("data", page);
    }

    /**
     * 风险点-检查记录-记录明细
     */
    @GetMapping("/recordDetails")
    @RequiresPermissions(value = {"scyf:inspectschdule:list","scyf:riskpoint:list"},logical = Logical.OR)
    @ApiOperation(value = "风险点二维码列表",response = RiskInspectRecord.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "riskPointId", value = "风险点ID", paramType = "query", required = false, dataType = "int"),
            @ApiImplicitParam(name = "inspectScheduleId", value = "检查任务ID", paramType = "query", required = false, dataType = "int"),
            @ApiImplicitParam(name = "inspectRecordNo", value = "检查记录编号", paramType = "query", required = false, dataType = "string"),
            @ApiImplicitParam(name = "pageNum", value = "第几页", required = true, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "pageSize", value = "每页多少条数据", required = true, paramType = "query", dataType = "int"),
    })
    public R recordDetails(@RequestParam Map<String, Object> params){
        params.put("companyId",getCompanyId());
        int pageNum = null!= params.get(Constant.PAGE_PAGENUM) ? Integer.parseInt(params.get(Constant.PAGE_PAGENUM).toString()) : 1;
        int pageSize = null != params.get(Constant.PAGE_PAGESIZE) ? Integer.parseInt(params.get(Constant.PAGE_PAGESIZE).toString()) : Constant.PAGE_SIZE;
        return R.ok().put("data", RiskInspectRecordService.getRiskInspectRecordPage(params,pageNum,pageSize,null));
    }


    /**
     * 信息
     */
    @RequestMapping(value="/info/{inspectRecordId}",method = RequestMethod.GET)
    @RequiresPermissions("scyf:riskinspectrecord:info")
    public R info(@PathVariable("inspectRecordId") Long inspectRecordId){
        RiskInspectRecord RiskInspectRecord = RiskInspectRecordService.getById(inspectRecordId);

        return R.ok().put("data", RiskInspectRecord);
    }

    /**
     * 保存
     */
    @PostMapping("/save")
    @RequiresPermissions("scyf:riskinspectrecord:save")
    public R save(@RequestBody RiskInspectRecord RiskInspectRecord){

        ValidatorUtils.validateEntity(RiskInspectRecord);
        RiskInspectRecord.setCreatedTime(new Date());
        RiskInspectRecord.setCreatedBy(getUserId());
        RiskInspectRecord.setDelFlag(0);
        RiskInspectRecordService.save(RiskInspectRecord);

        return R.ok();
    }

    /**
     * 修改
     */
    @PostMapping("/update")
    @RequiresPermissions("scyf:riskinspectrecord:update")
    public R update(@RequestBody RiskInspectRecord RiskInspectRecord){
        ValidatorUtils.validateEntity(RiskInspectRecord);
        RiskInspectRecord.setUpdatedTime(new Date());
        RiskInspectRecord.setUpdatedBy(getUserId());
        RiskInspectRecordService.update(RiskInspectRecord);
        
        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping(value="/delete",method = RequestMethod.DELETE)
    @RequiresPermissions("scyf:riskinspectrecord:delete")
    public R delete(@RequestBody Long[] inspectRecordIds){
        RiskInspectRecordService.removeByIds(Arrays.asList(inspectRecordIds));

        return R.ok();
    }

}
