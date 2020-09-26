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

import com.hngf.entity.risk.RiskPointGuideRel;
import com.hngf.service.risk.RiskPointGuideRelService;
import com.hngf.common.utils.PageUtils;
import com.hngf.common.utils.R;
import com.hngf.common.validator.ValidatorUtils;



/**
 * 风险点作业指导书关系表
 *
 * @author hngf
 * @email 
 * @date 2020-05-20 17:36:24
 */
@RestController
@RequestMapping("scyf/riskpointguiderel")
public class RiskPointGuideRelController {
    @Autowired
    private RiskPointGuideRelService RiskPointGuideRelService;

    /**
     * 列表
     */
    @RequestMapping(value = "/list",method = RequestMethod.GET)
    @RequiresPermissions("scyf:riskpointguiderel:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = RiskPointGuideRelService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping(value="/info/{riskPointId}",method = RequestMethod.GET)
    @RequiresPermissions("scyf:riskpointguiderel:info")
    public R info(@PathVariable("riskPointId") Long riskPointId){
        RiskPointGuideRel RiskPointGuideRel = RiskPointGuideRelService.getById(riskPointId);

        return R.ok().put("RiskPointGuideRel", RiskPointGuideRel);
    }

    /**
     * 保存
     */
    @PostMapping("/save")
    @RequiresPermissions("scyf:riskpointguiderel:save")
    public R save(@RequestBody RiskPointGuideRel RiskPointGuideRel){

        ValidatorUtils.validateEntity(RiskPointGuideRel);
        RiskPointGuideRelService.save(RiskPointGuideRel);

        return R.ok();
    }

    /**
     * 修改
     */
    @PostMapping("/update")
    @RequiresPermissions("scyf:riskpointguiderel:update")
    public R update(@RequestBody RiskPointGuideRel RiskPointGuideRel){
        ValidatorUtils.validateEntity(RiskPointGuideRel);
        RiskPointGuideRelService.update(RiskPointGuideRel);
        
        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping(value="/delete",method = RequestMethod.DELETE)
    @RequiresPermissions("scyf:riskpointguiderel:delete")
    public R delete(@RequestBody Long[] riskPointIds){
        RiskPointGuideRelService.removeByIds(Arrays.asList(riskPointIds));

        return R.ok();
    }

}
