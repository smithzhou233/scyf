package com.hngf.web.controller.risk;

import java.util.Arrays;
import java.util.Map;
import java.util.Date;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.hngf.web.controller.BaseController;

import com.hngf.entity.risk.RiskInspectRecordLog;
import com.hngf.service.risk.RiskInspectRecordLogService;
import com.hngf.common.utils.PageUtils;
import com.hngf.common.utils.R;
import com.hngf.common.validator.ValidatorUtils;
import com.hngf.common.utils.Constant;



/**
 * 检查记录日志表
 *
 * @author hngf
 * @email 
 * @date 2020-06-10 17:16:34
 */
@RestController
@RequestMapping("common/riskinspectrecordlog")
public class RiskInspectRecordLogController extends BaseController{
    @Autowired
    private RiskInspectRecordLogService RiskInspectRecordLogService;


    /**
     * 信息
     */
    @RequestMapping(value="/info/{inspectRecordId}",method = RequestMethod.GET)
    @RequiresPermissions("common:riskinspectrecordlog:info")
    public R info(@PathVariable("inspectRecordId") Long inspectRecordId){
        RiskInspectRecordLog RiskInspectRecordLog = RiskInspectRecordLogService.getById(inspectRecordId);

        return R.ok().put("data", RiskInspectRecordLog);
    }

    /**
     * 修改
     */
    @PostMapping("/update")
    @RequiresPermissions("common:riskinspectrecordlog:update")
    public R update(@RequestBody RiskInspectRecordLog RiskInspectRecordLog){
        ValidatorUtils.validateEntity(RiskInspectRecordLog);
        RiskInspectRecordLog.setUpdatedTime(new Date());
        RiskInspectRecordLog.setUpdatedBy(getUserId());
        RiskInspectRecordLogService.update(RiskInspectRecordLog);
        
        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping(value="/delete",method = RequestMethod.DELETE)
    @RequiresPermissions("common:riskinspectrecordlog:delete")
    public R delete(@RequestBody Long[] inspectRecordIds){
        RiskInspectRecordLogService.removeByIds(Arrays.asList(inspectRecordIds));

        return R.ok();
    }

}
