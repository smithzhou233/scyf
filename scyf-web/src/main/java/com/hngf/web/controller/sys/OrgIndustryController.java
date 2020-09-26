package com.hngf.web.controller.sys;

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

import com.hngf.entity.sys.OrgIndustry;
import com.hngf.service.sys.OrgIndustryService;
import com.hngf.common.utils.PageUtils;
import com.hngf.common.utils.R;
import com.hngf.common.validator.ValidatorUtils;



/**
 * 组织机构和行业映射
 *
 * @author hngf
 * @email 
 * @date 2020-05-20 17:31:55
 */
@RestController
@RequestMapping("sys/orgindustry")
public class OrgIndustryController {
    @Autowired
    private OrgIndustryService OrgIndustryService;

    /**
     * 列表
     */
    @RequestMapping(value = "/list",method = RequestMethod.GET)
    @RequiresPermissions("sys:orgindustry:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = OrgIndustryService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping(value="/info/{orgIndustryId}",method = RequestMethod.GET)
    @RequiresPermissions("sys:orgindustry:info")
    public R info(@PathVariable("orgIndustryId") Long orgIndustryId){
        OrgIndustry OrgIndustry = OrgIndustryService.getById(orgIndustryId);

        return R.ok().put("OrgIndustry", OrgIndustry);
    }

    /**
     * 保存
     */
    @PostMapping("/save")
    @RequiresPermissions("sys:orgindustry:save")
    public R save(@RequestBody OrgIndustry OrgIndustry){

        ValidatorUtils.validateEntity(OrgIndustry);
        OrgIndustryService.save(OrgIndustry);

        return R.ok();
    }

    /**
     * 修改
     */
    @PostMapping("/update")
    @RequiresPermissions("sys:orgindustry:update")
    public R update(@RequestBody OrgIndustry OrgIndustry){
        ValidatorUtils.validateEntity(OrgIndustry);
        OrgIndustryService.update(OrgIndustry);
        
        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping(value="/delete",method = RequestMethod.DELETE)
    @RequiresPermissions("sys:orgindustry:delete")
    public R delete(@RequestBody Long[] orgIndustryIds){
        OrgIndustryService.removeByIds(Arrays.asList(orgIndustryIds));

        return R.ok();
    }

}
