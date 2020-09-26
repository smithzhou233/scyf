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

import com.hngf.entity.sys.GroupMemberPositionGrant;
import com.hngf.service.sys.GroupMemberPositionGrantService;
import com.hngf.common.utils.PageUtils;
import com.hngf.common.utils.R;
import com.hngf.common.validator.ValidatorUtils;



/**
 * 群组成员岗位表
 *
 * @author hngf
 * @email 
 * @date 2020-05-20 17:31:55
 */
@RestController
@RequestMapping("sys/groupmemberpositiongrant")
public class GroupMemberPositionGrantController {
    @Autowired
    private GroupMemberPositionGrantService GroupMemberPositionGrantService;

    /**
     * 列表
     */
    @RequestMapping(value = "/list",method = RequestMethod.GET)
    @RequiresPermissions("sys:groupmemberpositiongrant:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = GroupMemberPositionGrantService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping(value="/info/{groupMemberPositionId}",method = RequestMethod.GET)
    @RequiresPermissions("sys:groupmemberpositiongrant:info")
    public R info(@PathVariable("groupMemberPositionId") Long groupMemberPositionId){
        GroupMemberPositionGrant GroupMemberPositionGrant = GroupMemberPositionGrantService.getById(groupMemberPositionId);

        return R.ok().put("GroupMemberPositionGrant", GroupMemberPositionGrant);
    }

    /**
     * 保存
     */
    @PostMapping("/save")
    @RequiresPermissions("sys:groupmemberpositiongrant:save")
    public R save(@RequestBody GroupMemberPositionGrant GroupMemberPositionGrant){

        ValidatorUtils.validateEntity(GroupMemberPositionGrant);
        GroupMemberPositionGrantService.save(GroupMemberPositionGrant);

        return R.ok();
    }

    /**
     * 修改
     */
    @PostMapping("/update")
    @RequiresPermissions("sys:groupmemberpositiongrant:update")
    public R update(@RequestBody GroupMemberPositionGrant GroupMemberPositionGrant){
        ValidatorUtils.validateEntity(GroupMemberPositionGrant);
        GroupMemberPositionGrantService.update(GroupMemberPositionGrant);
        
        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping(value="/delete",method = RequestMethod.DELETE)
    @RequiresPermissions("sys:groupmemberpositiongrant:delete")
    public R delete(@RequestBody Long[] groupMemberPositionIds){
        GroupMemberPositionGrantService.removeByIds(Arrays.asList(groupMemberPositionIds));

        return R.ok();
    }

}
