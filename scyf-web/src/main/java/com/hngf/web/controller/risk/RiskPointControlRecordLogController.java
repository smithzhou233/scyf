package com.hngf.web.controller.risk;

import java.util.Arrays;
import java.util.Date;
import java.util.Map;

import com.hngf.web.controller.BaseController;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.hngf.entity.risk.RiskPointControlRecordLog;
import com.hngf.service.risk.RiskPointControlRecordLogService;
import com.hngf.common.utils.PageUtils;
import com.hngf.common.utils.R;
import com.hngf.common.validator.ValidatorUtils;



/**
 * 风险点管控实时告警记录表
 *
 * @author hngf
 * @email 
 * @date 2020-05-20 17:36:23
 */
@RestController
@RequestMapping("scyf/riskpointcontrolrecordlog")
public class RiskPointControlRecordLogController extends BaseController {
    @Autowired
    private RiskPointControlRecordLogService RiskPointControlRecordLogService;

    /**
     * 列表
     */
    @RequestMapping(value = "/list",method = RequestMethod.GET)
    @RequiresPermissions("scyf:riskpointcontrolrecordlog:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = RiskPointControlRecordLogService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping(value="/info/{recordId}",method = RequestMethod.GET)
    @RequiresPermissions("scyf:riskpointcontrolrecordlog:info")
    public R info(@PathVariable("recordId") Long recordId){
        RiskPointControlRecordLog RiskPointControlRecordLog = RiskPointControlRecordLogService.getById(recordId);

        return R.ok().put("RiskPointControlRecordLog", RiskPointControlRecordLog);
    }

    /**
     * 保存
     */
    @PostMapping("/save")
    @RequiresPermissions("scyf:riskpointcontrolrecordlog:save")
    public R save(@RequestBody RiskPointControlRecordLog RiskPointControlRecordLog){

        ValidatorUtils.validateEntity(RiskPointControlRecordLog);
        RiskPointControlRecordLog.setCreatedTime(new Date());
        RiskPointControlRecordLog.setCreatedBy(getUserId());
        RiskPointControlRecordLog.setDelFlag(0);
        RiskPointControlRecordLogService.save(RiskPointControlRecordLog);

        return R.ok();
    }

    /**
     * 修改
     */
    @PostMapping("/update")
    @RequiresPermissions("scyf:riskpointcontrolrecordlog:update")
    public R update(@RequestBody RiskPointControlRecordLog RiskPointControlRecordLog){
        ValidatorUtils.validateEntity(RiskPointControlRecordLog);
        RiskPointControlRecordLog.setUpdatedTime(new Date());
        RiskPointControlRecordLog.setCupdatedBy(getUserId());
        RiskPointControlRecordLogService.update(RiskPointControlRecordLog);
        
        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping(value="/delete",method = RequestMethod.DELETE)
    @RequiresPermissions("scyf:riskpointcontrolrecordlog:delete")
    public R delete(@RequestBody Long[] recordIds){
        RiskPointControlRecordLogService.removeByIds(Arrays.asList(recordIds));

        return R.ok();
    }

}
