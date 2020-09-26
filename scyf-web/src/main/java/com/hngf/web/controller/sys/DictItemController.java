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

import com.hngf.entity.sys.DictItem;
import com.hngf.service.sys.DictItemService;
import com.hngf.common.utils.PageUtils;
import com.hngf.common.utils.R;
import com.hngf.common.validator.ValidatorUtils;



/**
 * 字典项表
 *
 * @author hngf
 * @email 
 * @date 2020-05-20 17:31:55
 */
@RestController
@RequestMapping("sys/dictitem")
public class DictItemController {
    @Autowired
    private DictItemService DictItemService;

    /**
     * 列表
     */
    @RequestMapping(value = "/list",method = RequestMethod.GET)
    @RequiresPermissions("sys:dictitem:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = DictItemService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping(value="/info/{itemId}",method = RequestMethod.GET)
    @RequiresPermissions("sys:dictitem:info")
    public R info(@PathVariable("itemId") String itemId){
        DictItem DictItem = DictItemService.getById(itemId);

        return R.ok().put("DictItem", DictItem);
    }

    /**
     * 保存
     */
    @PostMapping("/save")
    @RequiresPermissions("sys:dictitem:save")
    public R save(@RequestBody DictItem DictItem){

        ValidatorUtils.validateEntity(DictItem);
        DictItemService.save(DictItem);

        return R.ok();
    }

    /**
     * 修改
     */
    @PostMapping("/update")
    @RequiresPermissions("sys:dictitem:update")
    public R update(@RequestBody DictItem DictItem){
        ValidatorUtils.validateEntity(DictItem);
        DictItemService.update(DictItem);
        
        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping(value="/delete",method = RequestMethod.DELETE)
    @RequiresPermissions("sys:dictitem:delete")
    public R delete(@RequestBody String[] itemIds){
        DictItemService.removeByIds(Arrays.asList(itemIds));

        return R.ok();
    }

}
