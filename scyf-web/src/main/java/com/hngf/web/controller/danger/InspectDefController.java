package com.hngf.web.controller.danger;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.hngf.common.exception.ScyfException;
import com.hngf.common.utils.*;
import com.hngf.common.validator.Assert;
import com.hngf.dto.danger.InspectDefTreeDto;
import com.hngf.entity.danger.InspectItemDef;
import com.hngf.web.controller.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.hngf.entity.danger.InspectDef;
import com.hngf.service.danger.InspectDefService;
import com.hngf.common.validator.ValidatorUtils;



/**
 * 检查定义表
 *
 * @author zhangfei
 * @email 
 * @date 2020-06-09
 */
@RestController
@RequestMapping("scyf/inspectdef")
@Api(value = "检查定义管理",tags = "检查定义管理")
public class InspectDefController extends BaseController {
    @Autowired
    private InspectDefService InspectDefService;


    /**
     * 列表
     * zhangfei
     * 2020/06/03
     */
    @RequestMapping(value = "/list",method = RequestMethod.GET)
    @RequiresPermissions("scyf:inspectdef:list")
    @ApiOperation(value="列表",response = InspectDef.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "controlTable", value = "检查表类型：0分级管控；1专业检查表；", required = false, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "inspectDefTypeId", value = "检查表分类ID", required = false, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "pageNum", value = "第几页", required = true, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "pageSize", value = "每页多少条数据", required = true, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "keyword", value = "查询关键字", required = false, paramType = "query", dataType = "string")
    })
    public R list(@RequestParam(required = false) Map<String, Object> params){
        int pageNum = null!= params.get(Constant.PAGE_PAGENUM) ? Integer.parseInt(params.get(Constant.PAGE_PAGENUM).toString()) : 1;
        int pageSize = null != params.get(Constant.PAGE_PAGESIZE) ? Integer.parseInt(params.get(Constant.PAGE_PAGESIZE).toString()) : Constant.PAGE_SIZE;
        params.put("companyId", getCompanyId());
        return R.ok().put("data", InspectDefService.queryPage(params,pageNum,pageSize,null));
    }

    /**
     * 信息
     */
    @RequestMapping(value="/info/{inspectDefId}",method = RequestMethod.GET)
    @RequiresPermissions("scyf:inspectdef:info")
    @ApiOperation(value="信息",response = InspectDef.class)
    @ApiImplicitParam(name = "inspectDefId", value = "检查表ID", required = true, paramType = "query", dataType = "int")
    public R info(@PathVariable("inspectDefId") Long inspectDefId){
        return R.ok().put("data", InspectDefService.getById(inspectDefId));
    }

    /**
     * 保存
     */
    @PostMapping("/save")
    @RequiresPermissions("scyf:inspectdef:save")
    @ApiOperation(value="保存")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "inspectDefName", value = "检查表名称", required = true, paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "inspectDefTypeId", value = "检查表分类ID", required = true, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "inspectDefMethod", value = "检查方法", required = false, paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "inspectDefRule", value = "检查依据", required = false, paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "penalizeDefRule", value = "处罚依据", required = false, paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "inspectDefDesc", value = "检查描述", required = false, paramType = "query", dataType = "string")
    })
    public R save(@RequestBody InspectDef InspectDef){
        ValidatorUtils.validateEntity(InspectDef);

        //保存之前检查同名
        Map<String, Object> params = new HashMap<>();
        params.put("companyId", getCompanyId());
        params.put("inspectDefTypeId",InspectDef.getInspectDefTypeId());
        params.put("inspectDefName", InspectDef.getInspectDefName());
        List<Long> idList = InspectDefService.getIdByName(params);
        if (null != idList && idList.size() > 0) {
            throw new ScyfException("检查表名称【" + InspectDef.getInspectDefName() + "】已存在");
        }

        InspectDef.setCreatedBy(getUserId());
        InspectDef.setCompanyId(getCompanyId());
        int res = InspectDefService.save(InspectDef);

        return res > 0 ? R.ok() : R.error("保存失败");
    }

    /**
     * 修改
     */
    @PostMapping("/update")
    @RequiresPermissions("scyf:inspectdef:update")
    @ApiOperation(value="修改")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "inspectDefId", value = "检查表ID", required = true, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "inspectDefName", value = "检查表名称", required = true, paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "inspectDefTypeId", value = "检查表分类ID", required = true, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "inspectDefMethod", value = "检查方法", required = false, paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "inspectDefRule", value = "检查依据", required = false, paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "penalizeDefRule", value = "处罚依据", required = false, paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "inspectDefDesc", value = "检查描述", required = false, paramType = "query", dataType = "string")
    })
    public R update(@RequestBody InspectDef InspectDef){
        ValidatorUtils.validateEntity(InspectDef);
        Assert.isNull(InspectDef.getInspectDefId(),"检查表ID不能为空");

        InspectDef db = InspectDefService.getById(InspectDef.getInspectDefId());

        //如果修改了检查表分类，判断重名问题
        if (null != db.getInspectDefTypeId() && null != InspectDef.getInspectDefTypeId() && db.getInspectDefTypeId().intValue() != InspectDef.getInspectDefTypeId().intValue()) {
            Map<String, Object> params = new HashMap<>();
            params.put("companyId", getCompanyId());
            params.put("inspectDefTypeId",InspectDef.getInspectDefTypeId());
            params.put("inspectDefName", InspectDef.getInspectDefName());
            List<Long> idList = InspectDefService.getIdByName(params);
            if (null != idList && idList.size() > 0) {
                throw new ScyfException("检查表名称【" + InspectDef.getInspectDefName() + "】已存在");
            }
        }

        InspectDef.setUpdatedBy(getUserId());
        int res = InspectDefService.update(InspectDef);

        return res > 0 ? R.ok() : R.error("更新失败");
    }

    /**
     * 删除
     * @param inspectDefId
     * @return
     */
    @ApiOperation(value = "删除")
    @RequestMapping(value="/delete/{inspectDefId}",method = RequestMethod.DELETE)
    @RequiresPermissions("scyf:inspectdef:delete")
    @ApiImplicitParam(name = "inspectDefId", value = "检查表ID", required = true, paramType = "query", dataType = "int")
    public R delete(@PathVariable("inspectDefId") Long inspectDefId){
        Assert.isNull(inspectDefId,"检查表ID不能为空");
        int d = InspectDefService.removeById(inspectDefId);
        return d > 0 ? R.ok() : R.error("删除失败");
    }

    /**
     * 批量删除
     */
    @RequestMapping(value="/deleteBatch",method = RequestMethod.DELETE)
    @RequiresPermissions("scyf:inspectdef:delete")
    @ApiOperation(value="批量删除")
    @ApiImplicitParam(name = "inspectDefIds", value = "检查表ID数组", required = true, paramType = "query", dataType = "int")
    public R delete(Long[] inspectDefIds){
        Assert.isNull(inspectDefIds,"检查表ID不能为空");
        int d = InspectDefService.removeByIds(Arrays.asList(inspectDefIds));
        return d > 0 ? R.ok() : R.error("删除失败");
    }


    /**
     * 查询检查项定义列表
     */
    @GetMapping(value="/getItems")
    @RequiresPermissions(value = {"scyf:inspectdef:list","scyf:inspectitemdef:list"},logical = Logical.OR)
    @ApiOperation(value="查询检查项定义列表",response = InspectItemDef.class)
    @ApiImplicitParam(name = "inspectDefId", value = "检查表ID", required = true, paramType = "query", dataType = "int")
    public R getItems(Long inspectDefId){
        Assert.isNull(inspectDefId,"检查表ID不能为空");
        Map<String, Object> params = new HashMap<>();
        params.put("inspectDefId", inspectDefId);
        return R.ok().put("data", InspectDefService.getItems(params));
    }

    /**
     * 查询检查项定义树状列表
     */
    @GetMapping(value="/getTreeItems")
    @RequiresPermissions(value = {"scyf:inspectdef:list","scyf:inspectitemdef:list"},logical = Logical.OR)
    @ApiOperation(value="查询检查项定义树状列表",response = InspectDefTreeDto.class)
    @ApiImplicitParam(name = "inspectDefId", value = "检查表ID", required = true, paramType = "query", dataType = "int")
    public R getTreeItems(Long inspectDefId){
        Assert.isNull(inspectDefId,"检查表ID不能为空");
        Map<String, Object> params = new HashMap<>();
        params.put("inspectDefId", inspectDefId);
        return R.ok().put("data", InspectDefService.getTreeItems(params));
    }
}
