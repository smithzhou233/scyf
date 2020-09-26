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

import com.hngf.entity.risk.RiskPointCheckRecord;
import com.hngf.service.risk.RiskPointCheckRecordService;
import com.hngf.common.utils.PageUtils;
import com.hngf.common.utils.R;
import com.hngf.common.validator.ValidatorUtils;
import com.hngf.common.utils.Constant;



/**
 * 
 *
 * @author hngf
 * @email 
 * @date 2020-06-03 17:15:30
 */
@RestController
@RequestMapping("risk/riskpointcheckrecord")
public class RiskPointCheckRecordController extends BaseController{
    @Autowired
    private RiskPointCheckRecordService RiskPointCheckRecordService;

    /**
     * 列表
     */
    @RequestMapping(value = "/list",method = RequestMethod.GET)
    @RequiresPermissions("risk:riskpointcheckrecord:list")
    public R list(@RequestParam Map<String, Object> params){

        int pageNum = null!= params.get(Constant.PAGE_PAGENUM) ? Integer.parseInt(params.get(Constant.PAGE_PAGENUM).toString()) : 1;
        int pageSize = null != params.get(Constant.PAGE_PAGESIZE) ? Integer.parseInt(params.get(Constant.PAGE_PAGESIZE).toString()) : Constant.PAGE_SIZE;

        PageUtils page = RiskPointCheckRecordService.queryPage(params,pageNum,pageSize,null);

        return R.ok().put("data", page);
    }


    /**
     * 信息
     */
    @RequestMapping(value="/info/{recordId}",method = RequestMethod.GET)
    @RequiresPermissions("risk:riskpointcheckrecord:info")
    public R info(@PathVariable("recordId") Long recordId){
        RiskPointCheckRecord RiskPointCheckRecord = RiskPointCheckRecordService.getById(recordId);

        return R.ok().put("data", RiskPointCheckRecord);
    }

    /**
     * 保存
     */
    @PostMapping("/save")
    @RequiresPermissions("risk:riskpointcheckrecord:save")
    public R save(@RequestBody RiskPointCheckRecord RiskPointCheckRecord){

        ValidatorUtils.validateEntity(RiskPointCheckRecord);
        RiskPointCheckRecord.setDelFlag(0);
        RiskPointCheckRecord.setCreatedTime(new Date());
        RiskPointCheckRecord.setCreatedBy(getUserId());
        RiskPointCheckRecordService.save(RiskPointCheckRecord);

        return R.ok();
    }

    /**
     * 修改
     */
    @PostMapping("/update")
    @RequiresPermissions("risk:riskpointcheckrecord:update")
    public R update(@RequestBody RiskPointCheckRecord RiskPointCheckRecord){
        ValidatorUtils.validateEntity(RiskPointCheckRecord);
        RiskPointCheckRecord.setUpdatedTime(new Date());
        RiskPointCheckRecord.setUpdatedBy(getUserId());
        RiskPointCheckRecordService.update(RiskPointCheckRecord);
        
        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping(value="/delete",method = RequestMethod.DELETE)
    @RequiresPermissions("risk:riskpointcheckrecord:delete")
    public R delete(@RequestBody Long[] recordIds){
        RiskPointCheckRecordService.removeByIds(Arrays.asList(recordIds));

        return R.ok();
    }

}
