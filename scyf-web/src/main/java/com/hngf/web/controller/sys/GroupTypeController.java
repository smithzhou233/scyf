package com.hngf.web.controller.sys;

import com.hngf.common.utils.PageUtils;
import com.hngf.common.utils.R;
import com.hngf.common.validator.ValidatorUtils;
import com.hngf.common.validator.group.AddGroup;
import com.hngf.common.validator.group.UpdateGroup;
import com.hngf.entity.sys.GroupType;
import com.hngf.service.sys.GroupTypeService;
import com.hngf.web.common.annotation.SysLog;
import com.hngf.web.controller.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import java.util.Map;


/**
 * 部门类型
 *
 * @author hngf
 * @email 
 * @date 2020-05-22 11:08:22
 */
@RestController
@RequestMapping("sys/grouptype")
@Api(value = "部门类型管理",tags = {"部门类型管理"})
public class GroupTypeController extends BaseController {
    @Autowired
    private GroupTypeService GroupTypeService;

    /**
     * 列表
     */
    @GetMapping("/list")
    @RequiresPermissions("sys:grouptype:list")
    @ApiOperation(value="部门类型列表",notes = "列表" , response = GroupType.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum", value = "第几页", required = true, paramType = "query", dataType = "int", defaultValue = "1"),
            @ApiImplicitParam(name = "pageSize", value = "每页多少条数据", required = true, paramType = "query", dataType = "int", defaultValue = "10"),
            @ApiImplicitParam(name = "keyword", value = "查询关键字", required = false, paramType = "query", dataType = "string")
    })
    public R list(@ApiIgnore @RequestParam(required = false)Map<String, Object> params){
        // 默认查询所在企业的部门分类
        params.put("companyId",getCompanyId());
        PageUtils page = GroupTypeService.queryPage(params);
        return R.ok().put("data", page);
    }
    /**
     * 查询当前公司下所有部门类型
     */
    @GetMapping("/getAll")
    @RequiresPermissions("sys:grouptype:list")
    @ApiOperation(value="查询当前公司下所有部门类型")
    public R getAllGroupType(){
        return R.ok().put("data", GroupTypeService.getList(getCompanyId()));
    }


    /**
     * 信息
     */
    @GetMapping("/info/{groupTypeId}")
    @RequiresPermissions("sys:grouptype:info")
    @ApiOperation(value="信息" ,response = GroupType.class)
    @ApiImplicitParam(name = "groupTypeId", value = "群组类别ID", paramType = "path", required = true, dataType = "long")
    public R info(@PathVariable("groupTypeId") Long groupTypeId){
        GroupType GroupType = GroupTypeService.getById(groupTypeId);

        return R.ok().put("data", GroupType);
    }

    /**
     * 保存
     */
    @PostMapping("/save")
    @RequiresPermissions("sys:grouptype:save")
    @ApiOperation(value="保存", produces="application/json")
    @SysLog("保存部门类型")
    public R save(@RequestBody GroupType groupType){

        ValidatorUtils.validateEntity(groupType, AddGroup.class);
        groupType.insertPrefix(getUserId());
        groupType.setCompanyId(getCompanyId());

        GroupTypeService.save(groupType);
        return R.ok();
    }

    /**
     * 修改
     */
    @PostMapping("/update")
    @RequiresPermissions("sys:grouptype:update")
    @ApiOperation(value="修改", produces="application/json")
    @SysLog("修改部门类型")
    public R update(@RequestBody GroupType groupType){
        ValidatorUtils.validateEntity(groupType, UpdateGroup.class, AddGroup.class);
        groupType.updatePrefix(getUserId());
        groupType.setCompanyId(getCompanyId());
        GroupTypeService.update(groupType);
        return R.ok();
    }

    /**
     * 删除
     */
    @SysLog("删除部门类型")
    @RequestMapping(value="/delete/{groupTypeId}",method = RequestMethod.DELETE)
    @RequiresPermissions("sys:grouptype:delete")
    @ApiOperation(value="删除", produces="application/json")
    @ApiImplicitParam(name = "groupTypeId", value = "群组类别ID", paramType = "path", required = true, dataType = "long")
    public R delete(@PathVariable("groupTypeId") Long groupTypeId){
        GroupTypeService.removeById(groupTypeId, getUserId());
        return R.ok();
    }

}
