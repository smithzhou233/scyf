package com.hngf.web.controller.sys;

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

import com.hngf.entity.sys.CompanySeo;
import com.hngf.service.sys.CompanySeoService;
import com.hngf.common.utils.PageUtils;
import com.hngf.common.utils.R;
import com.hngf.common.validator.ValidatorUtils;
import com.hngf.common.utils.Constant;



/**
 * 企业 oem 表 
 *
 * @author hngf
 * @email 
 * @date 2020-07-07 17:02:00
 */
@RestController
@RequestMapping("sys/companyseo")
public class CompanySeoController extends BaseController{
    @Autowired
    private CompanySeoService CompanySeoService;

    /**
     * 信息
     */
    @RequestMapping(value="/info/{companyId}",method = RequestMethod.GET)
    @RequiresPermissions("sys:companyseo:info")
    public R info(@PathVariable("companyId") Long companyId){
        CompanySeo CompanySeo = CompanySeoService.getById(companyId);

        return R.ok().put("data", CompanySeo);
    }

    /**
     * 保存
     */
    @PostMapping("/save")
    @RequiresPermissions("sys:companyseo:save")
    public R save(@RequestBody CompanySeo CompanySeo){

        ValidatorUtils.validateEntity(CompanySeo);
        CompanySeoService.save(CompanySeo);

        return R.ok();
    }

    /**
     * 修改
     */
    @PostMapping("/update")
    @RequiresPermissions("sys:companyseo:update")
    public R update(@RequestBody CompanySeo CompanySeo){
        ValidatorUtils.validateEntity(CompanySeo);
        CompanySeoService.update(CompanySeo);
        
        return R.ok();
    }

}
