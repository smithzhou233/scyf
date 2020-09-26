package com.hngf.web.controller.sys;

import java.util.Arrays;
import java.util.Map;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.hngf.entity.sys.RoleMember;
import com.hngf.service.sys.RoleMemberService;
import com.hngf.common.utils.PageUtils;
import com.hngf.common.utils.R;
import com.hngf.common.validator.ValidatorUtils;



/**
 * 角色成员表
 *
 * @author hngf
 * @email 
 * @date 2020-05-20 17:31:55
 */
@RestController
@RequestMapping("sys/rolemember")
public class RoleMemberController {
    @Autowired
    private RoleMemberService RoleMemberService;

    /**
     * 列表
     */
    @RequestMapping(value = "/list",method = RequestMethod.GET)
    @RequiresPermissions("sys:rolemember:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = RoleMemberService.queryPage(params);

        return R.ok().put("data", page);
    }


    /**
     * 信息
     */
    @RequestMapping(value="/info/{roleId}",method = RequestMethod.GET)
    @RequiresPermissions("sys:rolemember:info")
    public R info(@PathVariable("roleId") Long roleId){
        RoleMember RoleMember = RoleMemberService.getById(roleId);

        return R.ok().put("data", RoleMember);
    }

    /**
     * 保存
     */
    @PostMapping("/save")
    @RequiresPermissions("sys:rolemember:save")
    public R save(@RequestBody RoleMember RoleMember){

        ValidatorUtils.validateEntity(RoleMember);
        RoleMemberService.save(RoleMember);

        return R.ok();
    }

    /**
     * 修改
     */
    @PostMapping("/update")
    @RequiresPermissions("sys:rolemember:update")
    public R update(@RequestBody RoleMember RoleMember){
        ValidatorUtils.validateEntity(RoleMember);
        RoleMemberService.update(RoleMember);
        
        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping(value="/delete",method = RequestMethod.DELETE)
    @RequiresPermissions("sys:rolemember:delete")
    public R delete(@RequestBody Long[] roleIds){
        RoleMemberService.removeByIds(Arrays.asList(roleIds));

        return R.ok();
    }

}
