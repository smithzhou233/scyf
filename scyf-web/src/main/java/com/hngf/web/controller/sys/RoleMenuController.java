package com.hngf.web.controller.sys;

import java.util.Arrays;
import java.util.Date;
import java.util.Map;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.hngf.entity.sys.RoleMenu;
import com.hngf.service.sys.RoleMenuService;
import com.hngf.common.utils.PageUtils;
import com.hngf.common.utils.R;
import com.hngf.common.validator.ValidatorUtils;

import static com.hngf.web.common.shiro.ShiroUtils.getUserId;


/**
 * 角色授权菜单表
 *
 * @author zhangfei
 * @email 
 * @date 2020-05-20 17:31:55
 */
@RestController
@RequestMapping("sys/rolemenu")
@Api(value="角色菜单管理",tags = {"角色菜单管理"})
public class RoleMenuController {
    @Autowired
    private RoleMenuService RoleMenuService;

    /**
     * 列表
     */
    @RequestMapping(value = "/list",method = RequestMethod.GET)
    @RequiresPermissions("sys:rolemenu:list")
    @ApiOperation(value="列表")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = RoleMenuService.queryPage(params);

        return R.ok().put("data", page);
    }


    /**
     * 信息
     */
    @RequestMapping(value="/info/{menuId}",method = RequestMethod.GET)
    @RequiresPermissions("sys:rolemenu:info")
    @ApiOperation(value="信息")
    @ApiImplicitParam(name = "menuId", value = "菜单ID", paramType = "必须", required = true, dataType = "long")
    public R info(@PathVariable("menuId") Long menuId){
        RoleMenu RoleMenu = RoleMenuService.getById(menuId);

        return R.ok().put("data", RoleMenu);
    }

    /**
     * 保存
     */
    @PostMapping("/save")
    @RequiresPermissions("sys:rolemenu:save")
    @ApiOperation(value="保存", produces="application/json")
    @ApiImplicitParam(name = "RoleMenu", value = "角色菜单信息", paramType = "必须", required = true, dataType = "json")
    public R save(@RequestBody RoleMenu RoleMenu){

        ValidatorUtils.validateEntity(RoleMenu);
        if (null == RoleMenu.getMenuParent()) {
            RoleMenu.setMenuParent(0L);
        }
        RoleMenu.setDelFlag(0);
        RoleMenu.setCreatedTime(new Date());
        RoleMenu.setCreatedBy(getUserId());
        RoleMenuService.save(RoleMenu);

        return R.ok();
    }

    /**
     * 修改
     */
    @PostMapping("/update")
    @RequiresPermissions("sys:rolemenu:update")
    @ApiOperation(value="修改", produces="application/json")
    @ApiImplicitParam(name = "RoleMenu", value = "角色菜单信息", paramType = "必须", required = true, dataType = "json")
    public R update(@RequestBody RoleMenu RoleMenu){
        ValidatorUtils.validateEntity(RoleMenu);
        if (null == RoleMenu.getMenuParent()) {
            RoleMenu.setMenuParent(0L);
        }
        RoleMenu.setUpdatedTime(new Date());
        RoleMenu.setUpdatedBy(getUserId());
        RoleMenuService.update(RoleMenu);
        
        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping(value="/delete",method = RequestMethod.DELETE)
    @RequiresPermissions("sys:rolemenu:delete")
    @ApiOperation(value="删除")
    @ApiImplicitParam(name = "menuIds", value = "ID数组集合", paramType = "必须", required = true, dataType = "数组")
    public R delete(@RequestBody Long[] menuIds){
        RoleMenuService.removeByIds(Arrays.asList(menuIds));

        return R.ok();
    }

}
