package com.hngf.web.controller.risk;

import java.util.Arrays;
import java.util.Map;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.hngf.entity.risk.RiskPointDangerSource;
import com.hngf.service.risk.RiskPointDangerSourceService;
import com.hngf.common.utils.PageUtils;
import com.hngf.common.utils.R;
import com.hngf.common.validator.ValidatorUtils;



/**
 * 风险点涉及的危险源
 *
 * @author hngf
 * @email 
 * @date 2020-05-20 17:36:24
 */
@RestController
@RequestMapping("scyf/riskpointdangersource")
public class RiskPointDangerSourceController {
    @Autowired
    private RiskPointDangerSourceService RiskPointDangerSourceService;

    /**
     * 列表
     */
    @RequestMapping(value = "/list",method = RequestMethod.GET)
    @RequiresPermissions("scyf:riskpointdangersource:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = RiskPointDangerSourceService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping(value="/info/{riskPointId}",method = RequestMethod.GET)
    @RequiresPermissions("scyf:riskpointdangersource:info")
    public R info(@PathVariable("riskPointId") Long riskPointId){
        RiskPointDangerSource RiskPointDangerSource = RiskPointDangerSourceService.getById(riskPointId);

        return R.ok().put("RiskPointDangerSource", RiskPointDangerSource);
    }

    /**
     * 保存
     */
    @PostMapping("/save")
    @RequiresPermissions("scyf:riskpointdangersource:save")
    public R save(@RequestBody RiskPointDangerSource RiskPointDangerSource){

        ValidatorUtils.validateEntity(RiskPointDangerSource);
        RiskPointDangerSourceService.save(RiskPointDangerSource);

        return R.ok();
    }

    /**
     * 修改
     */
    @PostMapping("/update")
    @RequiresPermissions("scyf:riskpointdangersource:update")
    public R update(@RequestBody RiskPointDangerSource RiskPointDangerSource){
        ValidatorUtils.validateEntity(RiskPointDangerSource);
        RiskPointDangerSourceService.update(RiskPointDangerSource);
        
        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping(value="/delete",method = RequestMethod.DELETE)
    @RequiresPermissions("scyf:riskpointdangersource:delete")
    public R delete(@RequestBody Long[] riskPointIds){
        RiskPointDangerSourceService.removeByIds(Arrays.asList(riskPointIds));

        return R.ok();
    }

}
