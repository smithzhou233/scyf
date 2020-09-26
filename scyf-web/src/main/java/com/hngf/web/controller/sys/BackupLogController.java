package com.hngf.web.controller.sys;

import com.hngf.common.utils.PageUtils;
import com.hngf.common.utils.R;
import com.hngf.service.danger.FeedbackService;
import com.hngf.service.sys.DataBaseBackupService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Api(value="数据备份日志",tags = {"数据备份日志"})
@RestController
@RequestMapping("scyf/backupLog")
public class BackupLogController {
    @Autowired
    private DataBaseBackupService dataBaseBackupService;

    /**
     * 列表
     */
    @RequestMapping(value = "/list",method = RequestMethod.GET)
    @RequiresPermissions("scyf:feedback:list")
    @ApiOperation(value = "备份日志列表", notes="备份日志列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum", value = "第几页", required = true, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "pageSize", value = "每页多少条数据", required = true, paramType = "query", dataType = "int"),
    })
    public R list(@RequestParam(required = false) Map<String, Object> params){
        PageUtils page = dataBaseBackupService.findAllData(params);
        return R.ok().put("page", page);
    }
}
