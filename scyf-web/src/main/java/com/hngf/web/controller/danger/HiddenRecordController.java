package com.hngf.web.controller.danger;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.hngf.web.controller.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.hngf.entity.danger.HiddenRecord;
import com.hngf.service.danger.HiddenRecordService;
import com.hngf.common.utils.PageUtils;
import com.hngf.common.utils.R;
import com.hngf.common.validator.ValidatorUtils;



/**
 * 隐患表
 *
 * @author hngf
 * @email 
 * @date 2020-05-20 17:36:24
 */
@Api(value = "隐患表",tags = "隐患表")
@RestController
@RequestMapping("scyf/hiddenrecord")
public class HiddenRecordController extends BaseController {
    @Autowired
    private HiddenRecordService HiddenRecordService;

    /**
     * 列表
     */
    @RequestMapping(value = "/list",method = RequestMethod.GET)
    @RequiresPermissions("scyf:hiddenrecord:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = HiddenRecordService.queryPage(params);

        return R.ok().put("page", page);
    }
    /**
     * @Author: zyj
     * @Description:隐患年度统计 表格 柱形图
     * @Param companyId 企业id groupId群组id year年份
     * @Date 11:32 2020/6/29
     */
    @GetMapping(value = "/getHdangerYearStatistics")
    @RequiresPermissions("scyf:hiddenrecord:list")
    @ApiOperation(value = "隐患年度统计 表格 柱形图")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "groupId", value = "群组id", paramType = "query", required = false,dataType = "Long"),
            @ApiImplicitParam(name = "year", value = "year年份", paramType = "query", required = true, dataType = "int"),

    })
     public R getHdangerYearStatistics(Long groupId,Integer year){
        Long companyId=getCompanyId();
        if (groupId==null){
            groupId=getGroupId();
        }
         Map<String, Object> hdangerYearStatistics = HiddenRecordService.getHdangerYearStatistics(companyId, groupId, year);

         return R.ok().put("data",hdangerYearStatistics);
     }
    /**
     * @Author: zyj
     * @Description:隐患类型统计柱形图
     * @Param companyId 企业id groupId群组id startTime 开始时间 YYYY-MM-DD endTime结束时间 YYYY-MM-DD
     * @Date 16:32 2020/6/29
     */
    @GetMapping(value = "/getHdangerTypeStatistics")
    @RequiresPermissions("scyf:hiddenrecord:list")
    @ApiOperation(value = "隐患类型统计柱形图")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "groupId", value = "群组id", paramType = "query", required = false,dataType = "Long"),
            @ApiImplicitParam(name = "startTime", value = "开始时间 YYYY-MM-DD", paramType = "query", required = false, dataType = "String"),
            @ApiImplicitParam(name = "endTime", value = "结束时间 YYYY-MM-DD", paramType = "query", required = false, dataType = "String"),

    })
    public R getHdangerTypeStatistics(Long groupId, String startTime, String endTime){
        Long companyId=getCompanyId();
        if (groupId==null){
            groupId=getGroupId();
        }
        Map<String, Object> hdangerYearStatistics = HiddenRecordService.getHdangerTypeStatistics(companyId,groupId,startTime,endTime);

        return R.ok().put("data",hdangerYearStatistics);
    }
    /**
     * 信息
     */
    @RequestMapping(value="/info/{hiddenRecordId}",method = RequestMethod.GET)
    @RequiresPermissions("scyf:hiddenrecord:info")
    public R info(@PathVariable("hiddenRecordId") Long hiddenRecordId){
        HiddenRecord HiddenRecord = HiddenRecordService.getById(hiddenRecordId);

        return R.ok().put("HiddenRecord", HiddenRecord);
    }

    /**
     * 保存
     */
    @PostMapping("/save")
    @RequiresPermissions("scyf:hiddenrecord:save")
    public R save(@RequestBody HiddenRecord HiddenRecord){

        //ValidatorUtils.validateEntity(HiddenRecord);
        //HiddenRecordService.save(HiddenRecord);

        return R.ok();
    }

    /**
     * 修改
     */
    @PostMapping("/update")
    @RequiresPermissions("scyf:hiddenrecord:update")
    public R update(@RequestBody HiddenRecord HiddenRecord){
        ValidatorUtils.validateEntity(HiddenRecord);
        HiddenRecordService.update(HiddenRecord);
        
        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping(value="/delete",method = RequestMethod.DELETE)
    @RequiresPermissions("scyf:hiddenrecord:delete")
    public R delete(@RequestBody Long[] hiddenRecordIds){
        HiddenRecordService.removeByIds(Arrays.asList(hiddenRecordIds));

        return R.ok();
    }

}
