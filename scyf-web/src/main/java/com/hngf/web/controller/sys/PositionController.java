package com.hngf.web.controller.sys;

import com.hngf.common.utils.Constant;
import com.hngf.common.utils.PageUtils;
import com.hngf.common.utils.R;
import com.hngf.common.validator.ValidatorUtils;
import com.hngf.entity.sys.Position;
import com.hngf.service.sys.PositionService;
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

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 岗位管理
 *
 * @author hngf
 * @email 
 * @date 2020-05-20 17:31:55
 */
@RestController
@RequestMapping("sys/position")
@Api(value = "岗位管理",tags = {"岗位管理"})
public class PositionController extends BaseController {
    @Autowired
    private PositionService positionService;

    /**
     * 列表
     */
    @RequestMapping(value = "/list",method = RequestMethod.GET)
    @RequiresPermissions("sys:position:list")
    @ApiOperation(value="列表", response = Position.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum", value = "第几页", required = true, paramType = "query", dataType = "integer"),
            @ApiImplicitParam(name = "pageSize", value = "每页多少条数据", required = true, paramType = "query", dataType = "integer"),
            @ApiImplicitParam(name = "keyword", value = "查询关键字", required = false, paramType = "query", dataType = "string")
    })
    public R list(@RequestParam Map<String, Object> params){
        params.put("companyId", getCompanyId());
        int pageNum = null!= params.get(Constant.PAGE_PAGENUM) ? Integer.parseInt(params.get(Constant.PAGE_PAGENUM).toString()) : 1;
        int pageSize = null != params.get(Constant.PAGE_PAGESIZE) ? Integer.parseInt(params.get(Constant.PAGE_PAGESIZE).toString()) : Constant.PAGE_SIZE;
        PageUtils page = positionService.queryPage(params,pageNum,pageSize,null);

        return R.ok().put("data", page);
    }


    /**
     * 信息
     */
    @RequestMapping(value="/info/{positionId}",method = RequestMethod.GET)
    @RequiresPermissions("sys:position:info")
    @ApiOperation(value="信息", response = Position.class)
    @ApiImplicitParam(name = "positionId", value = "岗位ID", paramType = "path", required = true, dataType = "integer")
    public R info(@PathVariable("positionId") Long positionId){
        Position position = positionService.getById(positionId);

        return R.ok().put("data", position);
    }

    /**
     * 保存
     */
    @PostMapping("/save")
    @RequiresPermissions("sys:position:save")
    @ApiOperation(value="保存", produces="application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "positionTitle", value = "岗位名称", required = true, paramType = "json", dataType = "string"),
            @ApiImplicitParam(name = "positionDesc", value = "岗位描述", required = false, paramType = "json", dataType = "string"),
            @ApiImplicitParam(name = "positionOrder", value = "岗位序号", required = false, paramType = "json", dataType = "string")
    })
    @SysLog("保存岗位")
    public R save(@RequestBody Position position){

        ValidatorUtils.validateEntity(position);

        //检查是否存在同名岗位
        HashMap<String, Object> params = new HashMap();
        params.put("companyId", getCompanyId());
        params.put("positionTitle", position.getPositionTitle());
        List<Position> positions = positionService.getList(params);
        if(null != positions && positions.size() > 0){
            return R.error(position.getPositionTitle() + "已存在!");
        }

        position.setPositionId(getId("sys_position"));
        position.setDelFlag(0);
        position.setCreatedBy(getUserId());
        position.setCompanyId(getCompanyId());
        positionService.save(position);

        return R.ok();
    }

    /**
     * 修改
     */
    @PostMapping("/update")
    @RequiresPermissions("sys:position:update")
    @ApiOperation(value="修改", produces="application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "positionId", value = "岗位ID", required = true, paramType = "json", dataType = "integer"),
            @ApiImplicitParam(name = "positionTitle", value = "岗位名称", required = true, paramType = "json", dataType = "string"),
            @ApiImplicitParam(name = "positionDesc", value = "岗位描述", required = false, paramType = "json", dataType = "string"),
            @ApiImplicitParam(name = "positionOrder", value = "岗位序号", required = false, paramType = "json", dataType = "string")
    })
    @SysLog("修改岗位")
    public R update(@RequestBody Position Position){
        ValidatorUtils.validateEntity(Position);
        Position.setUpdatedBy(getUserId());
        positionService.update(Position);
        
        return R.ok();
    }
    /**
     * 删除
     */
    @SysLog("删除岗位")
    @RequestMapping(value="/delete/{positionIds}",method = RequestMethod.DELETE)
    @RequiresPermissions("sys:position:delete")
    @ApiOperation(value="删除")
    @ApiImplicitParam(name = "positionIds", value = "ID", paramType = "path", required = true, dataType = "数组")
    public R delete(@PathVariable("positionIds") Long positionIds){
        positionService.removeByIds(Arrays.asList(positionIds));

        return R.ok();
    }
    /**
     * 删除多条
     */
    @SysLog("删除岗位")
    @RequestMapping(value="/deletes",method = RequestMethod.DELETE)
    @RequiresPermissions("sys:position:delete")
    @ApiOperation(value="删除")
    @ApiImplicitParam(name = "positionIds", value = "ID数组集合", paramType = "path", required = true, dataType = "数组")
    public R deletes(@RequestBody Long[] positionIds){
        positionService.removeByIds(Arrays.asList(positionIds));

        return R.ok();
    }

}
