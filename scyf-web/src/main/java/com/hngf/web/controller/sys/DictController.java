package com.hngf.web.controller.sys;

import java.util.Arrays;
import java.util.Date;
import java.util.Map;

import com.hngf.common.utils.Constant;
import com.hngf.web.common.annotation.SysLog;
import com.hngf.web.controller.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.hngf.entity.sys.Dict;
import com.hngf.service.sys.DictService;
import com.hngf.common.utils.PageUtils;
import com.hngf.common.utils.R;
import com.hngf.common.validator.ValidatorUtils;



/**
 * 字典表
 *
 * @author hngf
 * @email 
 * @date 2020-05-20 17:31:55
 */
@RestController
@RequestMapping("sys/dict")
@Api(value="字典表",tags = {"字典表"})
public class DictController extends BaseController {
    @Autowired
    private DictService dictService;

    /**
     * 列表
     */
    @RequestMapping(value = "/list",method = RequestMethod.GET)
    @RequiresPermissions("sys:dict:list")
    @ApiOperation(value = "字典表查询", notes="字典表查询")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum", value = "第几页", required = true, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "pageSize", value = "每页多少条数据", required = true, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "keyword", value = "查询关键字", required = false, paramType = "query", dataType = "string")
    })
    public R list(@RequestParam(required = false) Map<String, Object> params){
        int pageNum = null!= params.get(Constant.PAGE_PAGENUM) ? Integer.parseInt(params.get(Constant.PAGE_PAGENUM).toString()) : 1;
        int pageSize = null != params.get(Constant.PAGE_PAGESIZE) ? Integer.parseInt(params.get(Constant.PAGE_PAGESIZE).toString()) : Constant.PAGE_SIZE;
        PageUtils page = dictService.queryPage(params,pageNum,pageSize,null);

        return R.ok().put("data", page);
    }


    /**
     * 信息
     */
    @RequestMapping(value="/info/{dictId}",method = RequestMethod.GET)
    @RequiresPermissions("sys:dict:info")
    public R info(@PathVariable("dictId") String dictId){
        Dict Dict = dictService.getById(dictId);

        return R.ok().put("data", Dict);
    }

    /**
     * 保存
     */
    @PostMapping("/save")
    @RequiresPermissions("sys:dict:save")
    @ApiOperation(value = "字典表添加", notes="字典表添加")
    @SysLog("保存字典")
    public R save(@RequestBody Dict Dict){

        ValidatorUtils.validateEntity(Dict);
        Dict.setCreatedBy(getUserId());
        Dict.setCreatedTime(new Date());
        dictService.save(Dict);

        return R.ok();
    }

    /**
     * 修改
     */
    @PostMapping("/update")
    @RequiresPermissions("sys:dict:update")
    @ApiOperation(value = "字典表修改", notes="字典表修改")
    @SysLog("修改字典")
    public R update(@RequestBody Dict Dict){
        ValidatorUtils.validateEntity(Dict);
        Dict.setUpdatedBy(getUserId());
        Dict.setUpdatedTime(new Date());
        dictService.update(Dict);
        
        return R.ok();
    }
    /**
     * 删除
     */
    @SysLog("删除字典")
    @RequestMapping(value="/delete/{dictIds}",method = RequestMethod.DELETE)
    @RequiresPermissions("sys:dict:delete")
    @ApiOperation(value = "字典表删除", notes="字典表删除")
    @ApiImplicitParam(name = "dictIds", value = "字典表id", required = true, paramType = "query", dataType = "int")
    public R delete(@PathVariable("dictIds") Long dictIds){
        dictService.removeById(dictIds);

        return R.ok();
    }
    /**
     * 删除
     */
    @SysLog("删除字典")
    @RequestMapping(value="/deletes",method = RequestMethod.DELETE)
    @RequiresPermissions("sys:dict:delete")
    public R deletes(@RequestBody String[] dictIds){
        dictService.removeByIds(Arrays.asList(dictIds));

        return R.ok();
    }

}
