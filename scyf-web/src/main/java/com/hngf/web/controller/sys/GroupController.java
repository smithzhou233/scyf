package com.hngf.web.controller.sys;

import com.hngf.common.utils.PageUtils;
import com.hngf.common.utils.R;
import com.hngf.common.validator.ValidatorUtils;
import com.hngf.dto.sys.GroupDetailDto;
import com.hngf.entity.sys.Group;
import com.hngf.service.sys.GroupService;
import com.hngf.web.common.annotation.DataFilter;
import com.hngf.web.common.annotation.SysLog;
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
@RequestMapping("sys/group")
@Api(value = "组织管理",tags = {"组织管理"})
public class GroupController extends BaseController {
    @Autowired
    private GroupService GroupService;

    /**
     * 列表
     */
    @RequestMapping(value = "/list",method = RequestMethod.GET)
    @RequiresPermissions("sys:group:list")
    @ApiOperation(value="列表", response = Group.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum", value = "第几页", required = true, paramType = "query", dataType = "int" , defaultValue = "1"),
            @ApiImplicitParam(name = "pageSize", value = "每页多少条数据", required = true, paramType = "query", dataType = "int", defaultValue = "10"),
            @ApiImplicitParam(name = "keyword", value = "查询关键字", required = false, paramType = "query", dataType = "String")
    })
    @DataFilter(tableAliasGroup = "A",tableAliasUser = "D",user = true,subGroup = false)
    public R list(@RequestParam Map<String, Object> params){

        params.put("companyId", getCompanyId());

        PageUtils page = GroupService.queryPage(params);

        return R.ok().put("data", page);
    }

    /**
     * 当前用户所属群组列表
     * @return
     */
    @RequestMapping(value ="/getTreeList",method =RequestMethod.GET)
    @ApiOperation(value="组织树", response = GroupDetailDto.class)
    public R getGroupTreeList() {
        Long companyId = getUser().getCompanyId();
        Long companyGroupId = getUser().getCompanyGroupId();

        GroupDetailDto groupDetailDto = GroupService.getById(companyId, companyGroupId);
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

    /**
     * 信息
     */
    @RequestMapping(value="/info/{groupId}",method = RequestMethod.GET)
    @RequiresPermissions("sys:group:info")
    @ApiOperation(value="信息", response = Group.class)
    @ApiImplicitParam(name = "groupId", value = "群组ID", paramType = "path", required = true, dataType = "long")
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
    @RequiresPermissions("sys:group:save")
    @ApiOperation(value="保存", produces="application/json")
    @SysLog("保存群组")
    public R save( @RequestBody Group Group){
        ValidatorUtils.validateEntity(Group);
        Group.insertPrefix(getUserId());
        Group.setCompanyId(getCompanyId());
        GroupService.save(Group);
        return R.ok();
    }

    /**
     * 修改
     */
    @PostMapping("/update")
    @RequiresPermissions("sys:group:update")
    @ApiOperation(value="修改", produces="application/json")
    @SysLog("修改群组")
    public R update(@RequestBody Group Group){
        ValidatorUtils.validateEntity(Group);
        Group.setUpdatedTime(new Date());
        Group.setUpdatedBy(getUserId());
        Group.setCompanyId(getCompanyId());
        GroupService.update(Group);
        return R.ok();
    }
    /**
     * 删除
     */
    @SysLog("删除群组")
    @RequestMapping(value="/delete/{groupIds}",method = RequestMethod.DELETE)
    @RequiresPermissions("sys:group:delete")
    @ApiOperation(value="删除")
    @ApiImplicitParam(name = "groupIds", value = "ID", paramType = "path", required = true, dataType = "long")
    public R delete(@PathVariable("groupIds") Long groupIds){
        GroupService.removeById(groupIds);
        return R.ok();
    }
    /**
     * 删除多条
     */
    @SysLog("删除群组")
    @RequestMapping(value="/deletes",method = RequestMethod.DELETE)
    @RequiresPermissions("sys:group:delete")
    @ApiOperation(value="删除")
    @ApiImplicitParam(name = "groupIds", value = "ID数组集合", paramType = "body", required = true, dataType = "long", allowMultiple = true)
    public R deletes(@RequestBody Long[] groupIds){
        GroupService.removeByIds(Arrays.asList(groupIds));
        return R.ok();
    }


    /**
     * 修改群组经纬度
     */
    @PostMapping("/updatePlaces")
    @ApiOperation(value="修改群组经纬度")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "groupId", value = "群组id", required = true, paramType = "json", dataType = "long"),
            @ApiImplicitParam(name = "longitude", value = "群组经度", required = true, paramType = "json", dataType = "string"),
            @ApiImplicitParam(name = "latitude", value = "群组纬度", required = true, paramType = "json", dataType = "string")
    })
    @SysLog("修改群组经纬度")
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
