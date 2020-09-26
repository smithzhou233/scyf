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

import com.hngf.entity.sys.GroupMemberPosition;
import com.hngf.service.sys.GroupMemberPositionService;
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
@RequestMapping("sys/groupmemberposition")
public class GroupMemberPositionController {
    @Autowired
    private GroupMemberPositionService GroupMemberPositionService;

    /**
     * 列表
     */
    @RequestMapping(value = "/list",method = RequestMethod.GET)
    @RequiresPermissions("sys:groupmemberposition:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = GroupMemberPositionService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping(value="/info/{groupMemberPositionId}",method = RequestMethod.GET)
    @RequiresPermissions("sys:groupmemberposition:info")
    public R info(@PathVariable("groupMemberPositionId") Long groupMemberPositionId){
        GroupMemberPosition GroupMemberPosition = GroupMemberPositionService.getById(groupMemberPositionId);

        return R.ok().put("GroupMemberPosition", GroupMemberPosition);
    }

    /**
     * 保存
     */
    @PostMapping("/save")
    @RequiresPermissions("sys:groupmemberposition:save")
    public R save(@RequestBody GroupMemberPosition GroupMemberPosition){

        ValidatorUtils.validateEntity(GroupMemberPosition);
        GroupMemberPositionService.save(GroupMemberPosition);

        return R.ok();
    }

    /**
     * 修改
     */
    @PostMapping("/update")
    @RequiresPermissions("sys:groupmemberposition:update")
    public R update(@RequestBody GroupMemberPosition GroupMemberPosition){
        ValidatorUtils.validateEntity(GroupMemberPosition);
        GroupMemberPositionService.update(GroupMemberPosition);
        
        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping(value="/delete",method = RequestMethod.DELETE)
    @RequiresPermissions("sys:groupmemberposition:delete")
    public R delete(@RequestBody Long[] groupMemberPositionIds){
        GroupMemberPositionService.removeByIds(Arrays.asList(groupMemberPositionIds));

        return R.ok();
    }

}
