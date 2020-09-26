package com.hngf.web.controller.risk;

import com.hngf.common.utils.PageUtils;
import com.hngf.common.utils.R;
import com.hngf.common.validator.ValidatorUtils;
import com.hngf.entity.risk.RiskCtrl;
import com.hngf.service.risk.RiskCtrlService;
import com.hngf.web.controller.BaseController;
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
import java.util.Map;



/**
 * 风险管控配置
 *
 * @author hngf
 * @email 
 * @date 2020-05-20 17:36:24
 */
@RestController
@RequestMapping("scyf/riskctrl")
public class RiskCtrlController extends BaseController {
    @Autowired
    private RiskCtrlService riskCtrlService;

    /**
     * 列表
     */
    @RequestMapping(value = "/list",method = RequestMethod.GET)
    @RequiresPermissions("scyf:riskctrl:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = riskCtrlService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping(value="/info/{riskId}",method = RequestMethod.GET)
    @RequiresPermissions("scyf:riskctrl:info")
    public R info(@PathVariable("riskId") Long riskId){
        RiskCtrl RiskCtrl = riskCtrlService.getById(riskId);

        return R.ok().put("RiskCtrl", RiskCtrl);
    }

    /**
     * 保存
     */
    @PostMapping("/save")
    @RequiresPermissions("scyf:riskctrl:save")
    public R save(@RequestBody RiskCtrl RiskCtrl){

        ValidatorUtils.validateEntity(RiskCtrl);
        riskCtrlService.save(RiskCtrl);

        return R.ok();
    }

    /**
     * 修改
     */
    @PostMapping("/update")
    @RequiresPermissions("scyf:riskctrl:update")
    public R update(@RequestBody RiskCtrl RiskCtrl){
        ValidatorUtils.validateEntity(RiskCtrl);
        riskCtrlService.update(RiskCtrl);
        
        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping(value="/delete",method = RequestMethod.DELETE)
    @RequiresPermissions("scyf:riskctrl:delete")
    public R delete(@RequestBody Long[] riskIds){
        riskCtrlService.removeByIds(Arrays.asList(riskIds));
        return R.ok();
    }

}
