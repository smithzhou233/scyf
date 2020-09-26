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

import com.hngf.entity.sys.GroupMember;
import com.hngf.service.sys.GroupMemberService;
import com.hngf.common.utils.PageUtils;
import com.hngf.common.utils.R;
import com.hngf.common.validator.ValidatorUtils;



/**
 * 群组成员表
 *
 * @author hngf
 * @email 
 * @date 2020-05-20 17:31:55
 */
@RestController
@RequestMapping("sys/groupmember")
public class GroupMemberController {
    @Autowired
    private GroupMemberService GroupMemberService;

    /**
     * 列表
     */
    @RequestMapping(value = "/list",method = RequestMethod.GET)
    @RequiresPermissions("sys:groupmember:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = GroupMemberService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping(value="/info/{groupMemberId}",method = RequestMethod.GET)
    @RequiresPermissions("sys:groupmember:info")
    public R info(@PathVariable("groupMemberId") Long groupMemberId){
        GroupMember GroupMember = GroupMemberService.getById(groupMemberId);

        return R.ok().put("GroupMember", GroupMember);
    }

    /**
     * 保存
     */
    @PostMapping("/save")
    @RequiresPermissions("sys:groupmember:save")
    public R save(@RequestBody GroupMember GroupMember){

        ValidatorUtils.validateEntity(GroupMember);
        GroupMemberService.save(GroupMember);

        return R.ok();
    }

    /**
     * 修改
     */
    @PostMapping("/update")
    @RequiresPermissions("sys:groupmember:update")
    public R update(@RequestBody GroupMember GroupMember){
        ValidatorUtils.validateEntity(GroupMember);
        GroupMemberService.update(GroupMember);
        
        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping(value="/delete",method = RequestMethod.DELETE)
    @RequiresPermissions("sys:groupmember:delete")
    public R delete(@RequestBody Long[] groupMemberIds){
        GroupMemberService.removeByIds(Arrays.asList(groupMemberIds));

        return R.ok();
    }

}
