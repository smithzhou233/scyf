package com.hngf.web.controller.sys;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hngf.common.utils.PageUtils;
import com.hngf.common.utils.R;
import com.hngf.common.validator.ValidatorUtils;
import com.hngf.dto.sys.ElementUIDto;
import com.hngf.entity.sys.Menu;
import com.hngf.entity.sys.Role;
import com.hngf.service.sys.RoleMenuService;
import com.hngf.service.sys.RoleService;
import com.hngf.web.common.annotation.SysLog;
import com.hngf.web.common.shiro.ShiroUtils;
import com.hngf.web.controller.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;



/**
 * 角色管理
 * @author zhangfei
 * @email 
 * @date 2020-05-20 17:31:55
 */
@RestController
@RequestMapping("sys/role")
@Api(value="角色管理",tags = {"角色管理"})
public class RoleController extends BaseController {
    @Autowired
    private RoleService roleService;
    @Autowired
    private RoleMenuService roleMenuService;


    /**
     * 列表
     */
    @RequestMapping(value = "/list",method = RequestMethod.GET)
    @RequiresPermissions("sys:role:list")
    @ApiOperation(value="列表", response = Role.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum", value = "第几页", required = true, paramType = "query", dataType = "int", defaultValue = "1"),
            @ApiImplicitParam(name = "pageSize", value = "每页多少条数据", required = true, paramType = "query", dataType = "int", defaultValue = "10"),
            @ApiImplicitParam(name = "keyword", value = "查询关键字", required = false, paramType = "query", dataType = "string")
    })
    public R list(@ApiIgnore @RequestParam(required = false) Map<String, Object> params){
        PageUtils page = roleService.queryPage(params);

        return R.ok().put("data", page);
    }
    /**
     * 查询所有角色
     */
    @RequestMapping(value = "/getListAll",method = RequestMethod.GET)
    @RequiresPermissions("sys:role:list")
    @ApiOperation(value="所有角色列表", response = Role.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "roleStatus", value = "状态", required = false, paramType = "query", dataType = "int")
    })
    public R findListAll(@ApiIgnore @RequestParam(required = false) Map<String, Object> params){
        return R.ok().put("data", roleService.getListAllByMap(params));
    }




    /**
     * 信息
     */
    @RequestMapping(value="/info/{roleId}",method = RequestMethod.GET)
    @RequiresPermissions("sys:role:info")
    @ApiOperation(value="信息")
    @ApiImplicitParam(name = "roleId", value = "角色ID", required = true, paramType = "path", dataType = "integer")
    public R info(@PathVariable("roleId") Long roleId){
        Role Role = roleService.getById(roleId);

        return R.ok().put("data", Role);
    }

    /**
     * 角色对应菜单列表
     */
    @RequestMapping(value="/menu/{roleId}",method = RequestMethod.GET)
    @ApiOperation(value="角色对应菜单列表")
    @ApiImplicitParam(name = "roleId", value = "角色ID", paramType = "path", required = true, dataType = "long")
    public R menu(@PathVariable("roleId") Long roleId){
        List<Menu> list = roleService.getMenuByRoleId(roleId);
        List<ElementUIDto> tree = roleService.getElementUIDto(roleId,getUserId());
        List<Long> checkedIds = new ArrayList<>();
        for (Menu menu : list) {
            if(null == menu || null == menu.getMenuId()){
                continue;
            }
            checkedIds.add(menu.getMenuId());
        }
        return R.ok().put("data", tree).put("checked",checkedIds);
    }

    /**
     * 更新角色对应的权限
     */
    @RequestMapping(value="/updateMenu",method = {RequestMethod.POST,RequestMethod.GET})
    @ApiOperation(value="更新角色对应的权限")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "roleId", value = "角色ID", required = true, paramType = "form", dataType = "integer"),
            @ApiImplicitParam(name = "menuIds", value = "菜单ID集合", required = true, paramType = "form", dataType = "integer")
    })
    @SysLog("更新角色对应的权限")
    public R updateMenu(Long roleId,String menuIds){

        //List<String> list = Arrays.asList(menuIds);
        JSONArray json = JSONObject.parseArray(menuIds);
        //创建一个数组对象 长度和json数组一样 即json.size()
        Integer[] a = new Integer[json.size()];
        //然后将之转换成我们需要的数组就好了
        Integer[] array = json.toArray(a);
        Long[] aa=new Long[json.size()];
        for (int i = 0; i < array.length; i++) {
            aa[i]=Long.valueOf(array[i]);
        }
        List<Long> list = Arrays.asList(aa);
        //保存角色与菜单关系
        roleMenuService.updateMenu(roleId,list,getUserId());

        return R.ok();
    }

    /**
     * 保存
     */
    @PostMapping("/save")
    @RequiresPermissions("sys:role:save")
    @ApiOperation(value="保存", produces="application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "roleName", value = "角色名称", required = true, paramType = "json", dataType = "string"),
            @ApiImplicitParam(name = "roleDesc", value = "描述", required = false, paramType = "json", dataType = "string"),
            @ApiImplicitParam(name = "roleType", value = "角色类型：1私有、2公共", required = true, paramType = "json", dataType = "integer",defaultValue="0"),
            @ApiImplicitParam(name = "roleOrder", value = "排序", required = false, paramType = "json", dataType = "integer")
    })
    @SysLog("保存角色")
    public R save(@RequestBody Role Role){

        ValidatorUtils.validateEntity(Role);
        Role.setDelFlag(0);
        Role.setCreatedTime(new Date());
        Role.setCreatedBy(ShiroUtils.getUserId());
        roleService.save(Role);

        return R.ok();
    }

    /**
     * 修改
     */
    @PostMapping("/update")
    @RequiresPermissions("sys:role:update")
    @ApiOperation(value="修改", produces="application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "roleId", value = "角色ID", required = true, paramType = "json", dataType = "integer"),
            @ApiImplicitParam(name = "roleName", value = "角色名称", required = true, paramType = "json", dataType = "string"),
            @ApiImplicitParam(name = "roleDesc", value = "描述", required = false, paramType = "json", dataType = "string"),
            @ApiImplicitParam(name = "roleType", value = "角色类型：1私有、2公共", required = true, paramType = "json", dataType = "integer",defaultValue="0"),
            @ApiImplicitParam(name = "roleOrder", value = "排序", required = false, paramType = "json", dataType = "integer")
    })
    @SysLog("修改角色")
    public R update(@RequestBody Role Role){
        ValidatorUtils.validateEntity(Role);
        Role.setUpdatedTime(new Date());
        Role.setUpdatedBy(ShiroUtils.getUserId());
        roleService.update(Role);
        
        return R.ok();
    }
    /**
     * 删除
     */
    @SysLog("删除角色")
    @RequestMapping(value="/delete/{roleId}",method = RequestMethod.DELETE)
    @RequiresPermissions("sys:role:delete")
    @ApiOperation(value="删除")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "roleId", value = "ID", required = true, paramType = "主键", dataType = "Long")
    })
    public R delete(@PathVariable Long roleId){
        roleService.removeById(roleId);

        return R.ok();
    }
    /**
     * 删除
     */
    @SysLog("删除角色")
    @RequestMapping(value="/deletes",method = RequestMethod.DELETE)
    @RequiresPermissions("sys:role:delete")
    @ApiOperation(value="删除")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "roleIds", value = "ID数组集合", required = true, paramType = "query", dataType = "long", allowMultiple = true )
    })
    public R deletes(@RequestBody Long[] roleIds){
        roleService.removeByIds(Arrays.asList(roleIds));

        return R.ok();
    }
    @ApiOperation(value = "角色下拉框里的角色信息列表",notes = "各种角色下拉框里的角色信息列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name="roleType", value = "角色类型", paramType = "query", dataType = "int"),
            @ApiImplicitParam(name="userId", value = "用户Id", paramType = "query", dataType = "long"),
            @ApiImplicitParam(name="roleStatus", value = "角色状态", paramType = "query", dataType = "long"),
    })
    @RequestMapping(value = "/getSelectRoleList", method = RequestMethod.GET)
    public R getAllRoleList(@RequestParam (required = false) Map<String, Object> params){
        List<Map<String, Object>> listAllByMap = this.roleService.getListAllByMap(params);
        return R.ok().put("data", listAllByMap);
    }
}
