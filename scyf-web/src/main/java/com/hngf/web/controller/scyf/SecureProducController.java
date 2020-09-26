package com.hngf.web.controller.scyf;

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

import com.hngf.entity.scyf.SecureProduc;
import com.hngf.service.scyf.SecureProducService;
import com.hngf.common.utils.PageUtils;
import com.hngf.common.utils.R;
import com.hngf.common.validator.ValidatorUtils;



/**
 * 安全生产基本信息表
 *
 * @author hngf
 * @email 
 * @date 2020-05-22 16:52:31
 */
@RestController
@RequestMapping("scyf/secureproduc")
public class SecureProducController {
    @Autowired
    private SecureProducService SecureProducService;

    /**
     * 列表
     */
    @RequestMapping(value = "/list",method = RequestMethod.GET)
    @RequiresPermissions("scyf:secureproduc:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = SecureProducService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping(value="/info/{companyId}",method = RequestMethod.GET)
    @RequiresPermissions("scyf:secureproduc:info")
    public R info(@PathVariable("companyId") Long companyId){
        SecureProduc SecureProduc = SecureProducService.getById(companyId);

        return R.ok().put("SecureProduc", SecureProduc);
    }

    /**
     * 保存
     */
    @PostMapping("/save")
    @RequiresPermissions("scyf:secureproduc:save")
    public R save(@RequestBody SecureProduc SecureProduc){

        ValidatorUtils.validateEntity(SecureProduc);
        SecureProducService.save(SecureProduc);

        return R.ok();
    }

    /**
     * 修改
     */
    @PostMapping("/update")
    @RequiresPermissions("scyf:secureproduc:update")
    public R update(@RequestBody SecureProduc SecureProduc){
        ValidatorUtils.validateEntity(SecureProduc);
        SecureProducService.update(SecureProduc);
        
        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping(value="/delete",method = RequestMethod.DELETE)
    @RequiresPermissions("scyf:secureproduc:delete")
    public R delete(@RequestBody Long[] companyIds){
        SecureProducService.removeByIds(Arrays.asList(companyIds));

        return R.ok();
    }

}
