package com.hngf.web.controller.sys;

import java.util.Arrays;
import java.util.List;
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

import com.hngf.entity.sys.CompanyIndustry;
import com.hngf.service.sys.CompanyIndustryService;
import com.hngf.common.utils.PageUtils;
import com.hngf.common.utils.R;
import com.hngf.common.validator.ValidatorUtils;



/**
 * 
 *
 * @author hngf
 * @email 
 * @date 2020-05-20 17:31:55
 */
@RestController
@RequestMapping("sys/companyindustry")
public class CompanyIndustryController  extends BaseController {
    @Autowired
    private CompanyIndustryService CompanyIndustryService;

    /**
     * 列表
     */
    @RequestMapping(value = "/list",method = RequestMethod.GET)
    @RequiresPermissions("sys:companyindustry:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = CompanyIndustryService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping(value="/info/{companyId}",method = RequestMethod.GET)
    @RequiresPermissions("sys:companyindustry:info")
    public R info(@PathVariable("companyId") Long companyId){
        CompanyIndustry CompanyIndustry = CompanyIndustryService.getById(companyId);

        return R.ok().put("CompanyIndustry", CompanyIndustry);
    }

    /**
     * 保存
     */
    @PostMapping("/save")
    @RequiresPermissions("sys:companyindustry:save")
    public R save(@RequestBody CompanyIndustry CompanyIndustry){

        ValidatorUtils.validateEntity(CompanyIndustry);
        CompanyIndustryService.save(CompanyIndustry);

        return R.ok();
    }

    /**
     * 修改
     */
    @PostMapping("/update")
    @RequiresPermissions("sys:companyindustry:update")
    public R update(@RequestBody CompanyIndustry CompanyIndustry){
        ValidatorUtils.validateEntity(CompanyIndustry);
        CompanyIndustryService.update(CompanyIndustry);
        
        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping(value="/delete",method = RequestMethod.DELETE)
    @RequiresPermissions("sys:companyindustry:delete")
    public R delete(@RequestBody Long[] companyIds){
        CompanyIndustryService.removeByIds(Arrays.asList(companyIds));

        return R.ok();
    }

}
