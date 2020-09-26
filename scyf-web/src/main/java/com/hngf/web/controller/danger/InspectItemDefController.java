package com.hngf.web.controller.danger;

import com.hngf.common.utils.R;
import com.hngf.common.utils.RedisKeys;
import com.hngf.common.utils.RedisUtils;
import com.hngf.common.validator.Assert;
import com.hngf.common.validator.ValidatorUtils;
import com.hngf.entity.danger.InspectItemDef;
import com.hngf.service.danger.InspectItemDefService;
import com.hngf.web.controller.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * 检查项管理
 * @author zhangfei
 * @date 2020-06-09
 */
@RestController
@RequestMapping("scyf/inspectitemdef")
@Api(value = "检查项管理",tags = "检查项管理")
public class InspectItemDefController extends BaseController {
    @Autowired
    private InspectItemDefService InspectItemDefService;
    @Autowired
    private RedisUtils redisUtils;
    /**
     * 列表
     */
    /*@RequestMapping(value = "/list",method = RequestMethod.GET)
    @RequiresPermissions("scyf:inspectitemdef:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = InspectItemDefService.queryPage(params);

        return R.ok().put("page", page);
    }*/


    /**
     * 信息
     */
    @RequestMapping(value="/info/{inspectItemDefId}",method = RequestMethod.GET)
    @RequiresPermissions(value = {"scyf:inspectitemdef:info","scyf:inspectdef:list"},logical = Logical.OR)
    @ApiOperation(value="信息",response = InspectItemDef.class)
    @ApiImplicitParam(name = "inspectItemDefId", value = "检查项ID", required = true, paramType = "query", dataType = "int")
    public R info(@PathVariable("inspectItemDefId") Long inspectItemDefId){
        return R.ok().put("data", InspectItemDefService.getById(inspectItemDefId));
    }

    /**
     * 保存
     */
    @PostMapping("/save")
    @RequiresPermissions(value = {"scyf:inspectitemdef:save","scyf:inspectdef:list"},logical = Logical.OR)
    @ApiOperation(value="保存")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "inspectDefId", value = "检查表ID；", required = true, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "inspectItemDefName", value = "检查项名称", required = true, paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "inspectItemDefDesc", value = "检查项内容", required = true, paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "inspectItemDefMethod", value = "检查方法", required = false, paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "inspectItemDefRule", value = "检查依据", required = false, paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "penalizeItemDefRule", value = "处罚依据", required = false, paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "parentId", value = "父ID", required = false, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "isRoot", value = "是否根节点", required = false, paramType = "query", dataType = "int")
    })
    public R save(@RequestBody InspectItemDef InspectItemDef){
        ValidatorUtils.validateEntity(InspectItemDef);
        InspectItemDef.setDelFlag(0);
        InspectItemDef.setCompanyId(getCompanyId());
        InspectItemDef.setCreatedBy(getUserId());
        if (null == InspectItemDef.getParentId()) {
            InspectItemDef.setParentId(0L);
        }
        //添加检查项  删除redis缓存
        redisUtils.delete(RedisKeys.getScheduleDefItemList(getUserId(),InspectItemDef.getInspectDefId().toString(),":*"));
        return InspectItemDefService.save(InspectItemDef) > 0 ? R.ok() : R.error("保存失败");
    }

    /**
     * 修改
     */
    @PostMapping("/update")
    @RequiresPermissions(value = {"scyf:inspectitemdef:update","scyf:inspectdef:list"},logical = Logical.OR)
    @ApiOperation(value="更新")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "inspectItemDefId", value = "检查项ID；", required = true, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "inspectDefId", value = "检查表ID；", required = true, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "inspectItemDefName", value = "检查项名称", required = true, paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "inspectItemDefDesc", value = "检查项内容", required = true, paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "inspectItemDefMethod", value = "检查方法", required = false, paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "inspectItemDefRule", value = "检查依据", required = false, paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "penalizeItemDefRule", value = "处罚依据", required = false, paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "parentId", value = "父ID", required = false, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "isRoot", value = "是否根节点", required = false, paramType = "query", dataType = "int")
    })
    public R update(@RequestBody InspectItemDef InspectItemDef){
        Assert.isNull(InspectItemDef.getInspectItemDefId(), "检查项ID不能为空");
        ValidatorUtils.validateEntity(InspectItemDef);
        InspectItemDef.setUpdatedBy(getUserId());
        if (null == InspectItemDef.getParentId()) {
            InspectItemDef.setParentId(0L);
        }
        //添加检查项  删除redis缓存
        redisUtils.delete(RedisKeys.getScheduleDefItemList(getUserId(),InspectItemDef.getInspectDefId().toString(),":*"));
        return InspectItemDefService.update(InspectItemDef) > 0 ? R.ok() : R.error("更新失败");
    }

    /**
     * 删除
     */
    @RequestMapping(value="/delete/{inspectItemDefId}",method = RequestMethod.DELETE)
    @RequiresPermissions(value = {"scyf:inspectitemdef:delete","scyf:inspectdef:list"},logical = Logical.OR)
    @ApiOperation(value="删除")
    @ApiImplicitParam(name = "inspectItemDefId", value = "检查项ID", required = true, paramType = "query", dataType = "int")
    public R delete(@PathVariable("inspectItemDefId") Long inspectItemDefId){
        Assert.isNull(inspectItemDefId, "检查项ID不能为空");
        return InspectItemDefService.removeById(inspectItemDefId) > 0 ? R.ok() : R.error("删除失败");
    }

    /**
     * 批量删除
     */
    /*@RequestMapping(value="/deleteBatch",method = RequestMethod.DELETE)
    @RequiresPermissions(value = {"scyf:inspectitemdef:delete","scyf:inspectdef:list"},logical = Logical.OR)
    @ApiOperation(value="批量删除")
    @ApiImplicitParam(name = "inspectItemDefIds", value = "检查项ID数组", required = true, paramType = "query", dataType = "int")
    public R deleteBatch(Long[] inspectItemDefIds){
        Assert.isNull(inspectItemDefIds, "检查项ID不能为空");
        return InspectItemDefService.removeByIds(Arrays.asList(inspectItemDefIds)) > 0 ? R.ok() : R.error("删除失败");
    }*/

}
