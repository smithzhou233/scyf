package com.hngf.web.controller.danger;

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

import com.hngf.entity.danger.InspectPointRel;
import com.hngf.service.danger.InspectPointRelService;
import com.hngf.common.utils.PageUtils;
import com.hngf.common.utils.R;
import com.hngf.common.validator.ValidatorUtils;



/**
 * 
 *
 * @author hngf
 * @email 
 * @date 2020-05-20 17:36:24
 */
@RestController
@RequestMapping("scyf/inspectpointrel")
public class InspectPointRelController {
    @Autowired
    private InspectPointRelService InspectPointRelService;

    /**
     * 列表
     */
    @RequestMapping(value = "/list",method = RequestMethod.GET)
    @RequiresPermissions("scyf:inspectpointrel:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = InspectPointRelService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping(value="/info/{inspectPointRelId}",method = RequestMethod.GET)
    @RequiresPermissions("scyf:inspectpointrel:info")
    public R info(@PathVariable("inspectPointRelId") Long inspectPointRelId){
        InspectPointRel InspectPointRel = InspectPointRelService.getById(inspectPointRelId);

        return R.ok().put("InspectPointRel", InspectPointRel);
    }

    /**
     * 保存
     */
    @PostMapping("/save")
    @RequiresPermissions("scyf:inspectpointrel:save")
    public R save(@RequestBody InspectPointRel InspectPointRel){

        ValidatorUtils.validateEntity(InspectPointRel);
        InspectPointRelService.save(InspectPointRel);

        return R.ok();
    }

    /**
     * 修改
     */
    @PostMapping("/update")
    @RequiresPermissions("scyf:inspectpointrel:update")
    public R update(@RequestBody InspectPointRel InspectPointRel){
        ValidatorUtils.validateEntity(InspectPointRel);
        InspectPointRelService.update(InspectPointRel);
        
        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping(value="/delete",method = RequestMethod.DELETE)
    @RequiresPermissions("scyf:inspectpointrel:delete")
    public R delete(@RequestBody Long[] inspectPointRelIds){
        InspectPointRelService.removeByIds(Arrays.asList(inspectPointRelIds));

        return R.ok();
    }

}
