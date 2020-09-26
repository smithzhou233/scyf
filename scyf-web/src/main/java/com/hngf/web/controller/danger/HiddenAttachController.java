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

import com.hngf.entity.danger.HiddenAttach;
import com.hngf.service.danger.HiddenAttachService;
import com.hngf.common.utils.PageUtils;
import com.hngf.common.utils.R;
import com.hngf.common.validator.ValidatorUtils;



/**
 * 隐患附件表
 *
 * @author hngf
 * @email 
 * @date 2020-05-20 17:36:24
 */
@RestController
@RequestMapping("scyf/hiddenattach")
public class HiddenAttachController {
    @Autowired
    private HiddenAttachService HiddenAttachService;

    /**
     * 列表
     */
    @RequestMapping(value = "/list",method = RequestMethod.GET)
    @RequiresPermissions("scyf:hiddenattach:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = HiddenAttachService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping(value="/info/{hiddenAttachId}",method = RequestMethod.GET)
    @RequiresPermissions("scyf:hiddenattach:info")
    public R info(@PathVariable("hiddenAttachId") Long hiddenAttachId){
        HiddenAttach HiddenAttach = HiddenAttachService.getById(hiddenAttachId);

        return R.ok().put("HiddenAttach", HiddenAttach);
    }

    /**
     * 保存
     */
    @PostMapping("/save")
    @RequiresPermissions("scyf:hiddenattach:save")
    public R save(@RequestBody HiddenAttach HiddenAttach){

        ValidatorUtils.validateEntity(HiddenAttach);
        HiddenAttachService.save(HiddenAttach);

        return R.ok();
    }

    /**
     * 修改
     */
    @PostMapping("/update")
    @RequiresPermissions("scyf:hiddenattach:update")
    public R update(@RequestBody HiddenAttach HiddenAttach){
        ValidatorUtils.validateEntity(HiddenAttach);
        HiddenAttachService.update(HiddenAttach);
        
        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping(value="/delete",method = RequestMethod.DELETE)
    @RequiresPermissions("scyf:hiddenattach:delete")
    public R delete(@RequestBody Long[] hiddenAttachIds){
        HiddenAttachService.removeByIds(Arrays.asList(hiddenAttachIds));

        return R.ok();
    }

}
