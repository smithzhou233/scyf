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

import com.hngf.entity.risk.RiskMeasure;
import com.hngf.service.risk.RiskMeasureService;
import com.hngf.common.utils.PageUtils;
import com.hngf.common.utils.R;
import com.hngf.common.validator.ValidatorUtils;



/**
 * 风险管控措施
 *
 * @author hngf
 * @email 
 * @date 2020-05-20 17:36:23
 */
@RestController
@RequestMapping("scyf/riskmeasure")
public class RiskMeasureController {
    @Autowired
    private RiskMeasureService RiskMeasureService;

    /**
     * 列表
     */
    @RequestMapping(value = "/list",method = RequestMethod.GET)
    @RequiresPermissions("scyf:riskmeasure:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = RiskMeasureService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping(value="/info/{riskMeasureId}",method = RequestMethod.GET)
    @RequiresPermissions("scyf:riskmeasure:info")
    public R info(@PathVariable("riskMeasureId") Long riskMeasureId){
        RiskMeasure RiskMeasure = RiskMeasureService.getById(riskMeasureId);

        return R.ok().put("RiskMeasure", RiskMeasure);
    }

    /**
     * 保存
     */
    @PostMapping("/save")
    @RequiresPermissions("scyf:riskmeasure:save")
    public R save(@RequestBody RiskMeasure RiskMeasure){

        ValidatorUtils.validateEntity(RiskMeasure);
        RiskMeasureService.save(RiskMeasure);

        return R.ok();
    }

    /**
     * 修改
     */
    @PostMapping("/update")
    @RequiresPermissions("scyf:riskmeasure:update")
    public R update(@RequestBody RiskMeasure RiskMeasure){
        ValidatorUtils.validateEntity(RiskMeasure);
        RiskMeasureService.update(RiskMeasure);
        
        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping(value="/delete",method = RequestMethod.DELETE)
    @RequiresPermissions("scyf:riskmeasure:delete")
    public R delete(@RequestBody Long[] riskMeasureIds){
        RiskMeasureService.removeByIds(Arrays.asList(riskMeasureIds));

        return R.ok();
    }

}
