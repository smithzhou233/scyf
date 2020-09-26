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

import com.hngf.entity.risk.RiskPointCheckRecordLog;
import com.hngf.service.risk.RiskPointCheckRecordLogService;
import com.hngf.common.utils.PageUtils;
import com.hngf.common.utils.R;
import com.hngf.common.validator.ValidatorUtils;
import com.hngf.common.utils.Constant;



/**
 * 
 *
 * @author hngf
 * @email 
 * @date 2020-06-03 17:15:29
 */
@RestController
@RequestMapping("risk/riskpointcheckrecordlog")
public class RiskPointCheckRecordLogController extends BaseController{
    @Autowired
    private RiskPointCheckRecordLogService RiskPointCheckRecordLogService;

    /**
     * 列表
     */
    @RequestMapping(value = "/list",method = RequestMethod.GET)
    @RequiresPermissions("risk:riskpointcheckrecordlog:list")
    public R list(@RequestParam Map<String, Object> params){

        int pageNum = null!= params.get(Constant.PAGE_PAGENUM) ? Integer.parseInt(params.get(Constant.PAGE_PAGENUM).toString()) : 1;
        int pageSize = null != params.get(Constant.PAGE_PAGESIZE) ? Integer.parseInt(params.get(Constant.PAGE_PAGESIZE).toString()) : Constant.PAGE_SIZE;

        PageUtils page = RiskPointCheckRecordLogService.queryPage(params,pageNum,pageSize,null);

        return R.ok().put("data", page);
    }


    /**
     * 信息
     */
    @RequestMapping(value="/info/{recordId}",method = RequestMethod.GET)
    @RequiresPermissions("risk:riskpointcheckrecordlog:info")
    public R info(@PathVariable("recordId") Long recordId){
        RiskPointCheckRecordLog RiskPointCheckRecordLog = RiskPointCheckRecordLogService.getById(recordId);

        return R.ok().put("data", RiskPointCheckRecordLog);
    }

    /**
     * 保存
     */
    @PostMapping("/save")
    @RequiresPermissions("risk:riskpointcheckrecordlog:save")
    public R save(@RequestBody RiskPointCheckRecordLog RiskPointCheckRecordLog){

        ValidatorUtils.validateEntity(RiskPointCheckRecordLog);
        RiskPointCheckRecordLog.setDelFlag(0);
        RiskPointCheckRecordLog.setCreatedTime(new Date());
        RiskPointCheckRecordLog.setCreatedBy(getUserId());
        RiskPointCheckRecordLogService.save(RiskPointCheckRecordLog);

        return R.ok();
    }

    /**
     * 修改
     */
    @PostMapping("/update")
    @RequiresPermissions("risk:riskpointcheckrecordlog:update")
    public R update(@RequestBody RiskPointCheckRecordLog RiskPointCheckRecordLog){
        ValidatorUtils.validateEntity(RiskPointCheckRecordLog);
        RiskPointCheckRecordLog.setUpdatedTime(new Date());
        RiskPointCheckRecordLog.setUpdatedBy(getUserId());
        RiskPointCheckRecordLogService.update(RiskPointCheckRecordLog);
        
        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping(value="/delete",method = RequestMethod.DELETE)
    @RequiresPermissions("risk:riskpointcheckrecordlog:delete")
    public R delete(@RequestBody Long[] recordIds){
        RiskPointCheckRecordLogService.removeByIds(Arrays.asList(recordIds));

        return R.ok();
    }

}
