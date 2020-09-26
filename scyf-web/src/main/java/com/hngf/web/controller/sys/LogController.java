package com.hngf.web.controller.sys;

import java.util.Map;

import com.hngf.common.utils.Constant;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.hngf.entity.sys.Log;
import com.hngf.service.sys.LogService;
import com.hngf.common.utils.R;



/**
 * 系统日志表
 *
 * @author hngf
 * @email 
 * @date 2020-05-20 17:31:55
 */
@RestController
@RequestMapping("sys/log")
@Api(value="系统日志管理",tags = {"系统日志管理"})
public class LogController {
    @Autowired
    private LogService LogService;

    /**
     * 列表
     */
    @GetMapping("/list")
    @RequiresPermissions("sys:log:list")
    @ApiOperation(value="列表",response = Log.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum", value = "第几页", required = true, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "pageSize", value = "每页多少条数据", required = true, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "keyword", value = "查询关键字", required = false, paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "startTime", value = "开始时间", required = false, paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "endTime", value = "结束时间", required = false, paramType = "query", dataType = "string")
    })
    public R list(@RequestParam Map<String, Object> params){

        int pageNum = null!= params.get(Constant.PAGE_PAGENUM) ? Integer.parseInt(params.get(Constant.PAGE_PAGENUM).toString()) : 1;
        int pageSize = null != params.get(Constant.PAGE_PAGESIZE) ? Integer.parseInt(params.get(Constant.PAGE_PAGESIZE).toString()) : Constant.PAGE_SIZE;

        return R.ok().put("data", LogService.queryPage(params,pageNum,pageSize,null));
    }


    /**
     * 查看详情
     */
    @GetMapping("/info/{logId}")
    @RequiresPermissions("sys:log:info")
    @ApiOperation(value="查看详情",response = Log.class)
    @ApiImplicitParam(name = "logId", value = "日志ID", required = true, paramType = "path", dataType = "int")
    public R info(@PathVariable("logId") Long logId){
        return R.ok().put("data", LogService.getById(logId));
    }

}
