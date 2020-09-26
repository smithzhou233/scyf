package com.hngf.api.controller.sys;

import com.hngf.api.common.annotation.DataFilter;
import com.hngf.api.controller.BaseController;
import com.hngf.common.utils.PageUtils;
import com.hngf.common.utils.R;
import com.hngf.common.validator.ValidatorUtils;
import com.hngf.dto.sys.GroupDetailDto;
import com.hngf.entity.sys.Group;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 组织管理
 * @author zhangfei
 * @email
 * @date 2020-05-20 17:31:55
 */
@RestController
@RequestMapping("/api/sys/group")
@Api(value = "组织管理",tags = {"组织管理"})
public class GroupController extends BaseController {
    @Autowired
    private com.hngf.service.sys.GroupService GroupService;

    /**
     * 列表
     */
    @RequestMapping(value = "/list",method = RequestMethod.GET)
    @ApiOperation(value="列表")

    @DataFilter(tableAliasGroup = "A",tableAliasUser = "D",user = true,subGroup = false)
    public R list(@RequestParam Map<String, Object> params){

        params.put("companyId", getCompanyId());
        params.put("groupId",getGroupId());
        PageUtils page = GroupService.queryPage(params);
        return R.ok().put("data", page);
    }

    /**
     * 当前用户所属群组列表
     * @return
     */
    @RequestMapping(value ="/getTreeList",method =RequestMethod.GET)
    @ApiOperation(value="组织树")
    public R getGroupTreeList() {
        Long companyId = getUser().getCompanyId();
        Long companyGroupId = getUser().getCompanyGroupId();
        Long groupId = getUser().getGroupId();
        GroupDetailDto groupDetailDto = GroupService.getById(companyId, groupId);
        if (groupDetailDto == null) {
            return R.error("公司机构数据不完整！");
        } else {
            Integer groupLeft = groupDetailDto.getGroupLeft();
            Integer groupRight = groupDetailDto.getGroupRight();
            List<GroupDetailDto> groupTree = this.GroupService.getGroupTreeList(companyId, groupLeft, groupRight);
            return R.ok().put("data", groupTree);
        }
    }


    @RequestMapping(value ="/getSubGroupList",method =RequestMethod.GET)
    public R getSubGroupList(){
     /*   Long companyId = getUser().getCompanyId();
        Long companyGroupId = getUser().getCompanyGroupId();*/
        Long groupId = getUser().getGroupId();
        List <GroupDetailDto> groupDetailDtos = GroupService.getSubGroupDetailList(groupId);
        return R.ok().put("data",groupDetailDtos);
    }

    @RequestMapping(value ="/getGroupListOfCompany",method =RequestMethod.GET)
    public R getGroupListOfCompany(){
        Long companyId = getUser().getCompanyId();
        Long companyGroupId = getUser().getCompanyGroupId();
        //Long groupId = getUser().getGroupId();
        List <GroupDetailDto> groupDetailDtos = GroupService.getSubGroupDetailList(companyGroupId);
        return R.ok().put("groupDetailDtos",groupDetailDtos);
    }


    /**
     * 信息
     */
    @RequestMapping(value="/info/{groupId}",method = RequestMethod.GET)
    @ApiOperation(value="信息")
    @ApiImplicitParam(name = "groupId", value = "群组ID", paramType = "path", required = true, dataType = "integer")
    public R info(@PathVariable("groupId") Long groupId){
        Group Group = GroupService.getById(groupId);
        if (null != Group && null != Group.getGroupParent() && Group.getGroupParent().longValue() > 0L) {
            Group parent = GroupService.getById(Group.getGroupParent());
            Group.setGroupParentName(parent.getGroupName());
        }
        return R.ok().put("data", Group);
    }

    /**
     * 保存
     */
    @PostMapping("/save")
    @ApiOperation(value="保存", produces="application/json")
    @ApiImplicitParam(name = "Group", value = "组织信息", paramType = "必须", required = true, dataType = "json")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "groupName", value = "群组名称", required = true, paramType = "json", dataType = "string"),
            @ApiImplicitParam(name = "groupTypeId", value = "群组类型id", required = false, paramType = "json", dataType = "integer"),
            @ApiImplicitParam(name = "groupParent", value = "群组父级id", required = true, paramType = "json", dataType = "integer"),
            @ApiImplicitParam(name = "groupLevel", value = "级别", required = false, paramType = "json", dataType = "integer"),
            @ApiImplicitParam(name = "groupDesc", value = "群组描述", required = false, paramType = "json", dataType = "string"),
            @ApiImplicitParam(name = "groupStatus", value = "群组状态", required = false, paramType = "json", dataType = "integer"),
            @ApiImplicitParam(name = "groupCode", value = "群组代码", required = false, paramType = "json", dataType = "string"),
            @ApiImplicitParam(name = "groupOrder", value = "排序", required = false, paramType = "json", dataType = "integer"),
            @ApiImplicitParam(name = "groupSelect", value = "拼音及首字母搜索", required = false, paramType = "json", dataType = "string")
    })
    public R save(@RequestBody Group Group){
        Group.setDelFlag(0);
        Group.setCreatedTime(new Date());
        Group.setCreatedBy(getUserId());
        Group.setCompanyId(getCompanyId());
        ValidatorUtils.validateEntity(Group);
        GroupService.save(Group);
        return R.ok();
    }

    /**
     * 修改
     */
    @PostMapping("/update")
    @ApiOperation(value="修改", produces="application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "groupId", value = "群组id", required = true, paramType = "json", dataType = "integer"),
            @ApiImplicitParam(name = "groupName", value = "群组名称", required = true, paramType = "json", dataType = "string"),
            @ApiImplicitParam(name = "groupTypeId", value = "群组类型id", required = false, paramType = "json", dataType = "integer"),
            @ApiImplicitParam(name = "groupParent", value = "群组父级id", required = true, paramType = "json", dataType = "integer"),
            @ApiImplicitParam(name = "groupLevel", value = "级别", required = false, paramType = "json", dataType = "integer"),
            @ApiImplicitParam(name = "groupDesc", value = "群组描述", required = false, paramType = "json", dataType = "string"),
            @ApiImplicitParam(name = "groupStatus", value = "群组状态", required = false, paramType = "json", dataType = "integer"),
            @ApiImplicitParam(name = "groupCode", value = "群组代码", required = false, paramType = "json", dataType = "string"),
            @ApiImplicitParam(name = "groupOrder", value = "排序", required = false, paramType = "json", dataType = "integer"),
            @ApiImplicitParam(name = "groupSelect", value = "拼音及首字母搜索", required = false, paramType = "json", dataType = "string")
    })
    public R update(@RequestBody Group Group){
        Group.setUpdatedTime(new Date());
        Group.setUpdatedBy(getUserId());
        Group.setCompanyId(getCompanyId());
        ValidatorUtils.validateEntity(Group);
        GroupService.update(Group);
        return R.ok();
    }
    /**
     * 删除
     */
    @RequestMapping(value="/delete/{groupIds}",method = RequestMethod.DELETE)
    @RequiresPermissions("sys:group:delete")
    @ApiOperation(value="删除")
    @ApiImplicitParam(name = "groupIds", value = "ID", paramType = "主键id", required = true, dataType = "主键id")
    public R delete(@PathVariable("groupIds") Long groupIds){
        GroupService.removeById(groupIds);
        return R.ok();
    }
    /**
     * 删除多条
     */
    @RequestMapping(value="/deletes",method = RequestMethod.DELETE)
    @RequiresPermissions("sys:group:delete")
    @ApiOperation(value="删除")
    @ApiImplicitParam(name = "groupIds", value = "ID数组集合", paramType = "path", required = true, dataType = "数组")
    public R deletes(@RequestBody Long[] groupIds){
        GroupService.removeByIds(Arrays.asList(groupIds));
        return R.ok();
    }


    /**
     * 修改群组经纬度
     */
    @PostMapping("/updatePlaces")
    @ApiOperation(value="修改群组经纬度", produces="application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "groupId", value = "群组id", required = true, paramType = "json", dataType = "long"),
            @ApiImplicitParam(name = "longitude", value = "群组经度", required = true, paramType = "json", dataType = "string"),
            @ApiImplicitParam(name = "latitude", value = "群组纬度", required = true, paramType = "json", dataType = "string")
    })
    public R updatePlaces(Long groupId, String longitude,String latitude){
        BigDecimal longitude1 =new BigDecimal(longitude);
        BigDecimal latitude2 =new BigDecimal(latitude);
        Group group=new Group();
        group.setGroupId(groupId);
        group.setLongitude(longitude1);
        group.setLatitude(latitude2);
        GroupService.update(group);
        return R.ok();
    }

}
