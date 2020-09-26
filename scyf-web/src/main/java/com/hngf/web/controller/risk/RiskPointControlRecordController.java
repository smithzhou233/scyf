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

import com.hngf.entity.risk.RiskPointControlRecord;
import com.hngf.service.risk.RiskPointControlRecordService;
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
@RequestMapping("scyf/riskpointcontrolrecord")
public class RiskPointControlRecordController extends BaseController {
    @Autowired
    private RiskPointControlRecordService RiskPointControlRecordService;

    /**
     * 列表
     */
    @RequestMapping(value = "/list",method = RequestMethod.GET)
    @RequiresPermissions("scyf:riskpointcontrolrecord:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = RiskPointControlRecordService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping(value="/info/{recordId}",method = RequestMethod.GET)
    @RequiresPermissions("scyf:riskpointcontrolrecord:info")
    public R info(@PathVariable("recordId") Long recordId){
        RiskPointControlRecord RiskPointControlRecord = RiskPointControlRecordService.getById(recordId);

        return R.ok().put("RiskPointControlRecord", RiskPointControlRecord);
    }

    /**
     * 保存
     */
    @PostMapping("/save")
    @RequiresPermissions("scyf:riskpointcontrolrecord:save")
    public R save(@RequestBody RiskPointControlRecord RiskPointControlRecord){

        ValidatorUtils.validateEntity(RiskPointControlRecord);
        RiskPointControlRecord.setCreatedTime(new Date());
        RiskPointControlRecord.setCreatedBy(getUserId());
        RiskPointControlRecord.setDelFlag(0);
        RiskPointControlRecordService.save(RiskPointControlRecord);

        return R.ok();
    }

    /**
     * 修改
     */
    @PostMapping("/update")
    @RequiresPermissions("scyf:riskpointcontrolrecord:update")
    public R update(@RequestBody RiskPointControlRecord RiskPointControlRecord){
        ValidatorUtils.validateEntity(RiskPointControlRecord);
        RiskPointControlRecord.setUpdatedTime(new Date());
        RiskPointControlRecord.setCupdatedBy(getUserId());
        RiskPointControlRecordService.update(RiskPointControlRecord);
        
        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping(value="/delete",method = RequestMethod.DELETE)
    @RequiresPermissions("scyf:riskpointcontrolrecord:delete")
    public R delete(@RequestBody Long[] recordIds){
        RiskPointControlRecordService.removeByIds(Arrays.asList(recordIds));

        return R.ok();
    }

}
