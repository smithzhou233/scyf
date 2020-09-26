package com.hngf.web.controller.danger;

import java.util.Arrays;
import java.util.Map;

import com.hngf.common.utils.RedisKeys;
import com.hngf.common.utils.RedisUtils;
import com.hngf.web.controller.BaseController;
import io.swagger.annotations.Api;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.hngf.entity.danger.InspectItemDefContent;
import com.hngf.service.danger.InspectItemDefContentService;
import com.hngf.common.utils.PageUtils;
import com.hngf.common.utils.R;
import com.hngf.common.validator.ValidatorUtils;



/**
 * 检查项内容
 *
 * @author hngf
 * @email 
 * @date 2020-05-20 17:36:24
 */
@RestController
@Api(value = "检查项内容",tags = "检查项内容")
@RequestMapping("scyf/inspectitemdefcontent")
public class InspectItemDefContentController extends BaseController {
    @Autowired
    private InspectItemDefContentService InspectItemDefContentService;


    /**
     * 列表
     */
    @RequestMapping(value = "/list",method = RequestMethod.GET)
    @RequiresPermissions("scyf:inspectitemdefcontent:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = InspectItemDefContentService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping(value="/info/{inspectItemDefContentId}",method = RequestMethod.GET)
    @RequiresPermissions("scyf:inspectitemdefcontent:info")
    public R info(@PathVariable("inspectItemDefContentId") Long inspectItemDefContentId){
        InspectItemDefContent InspectItemDefContent = InspectItemDefContentService.getById(inspectItemDefContentId);

        return R.ok().put("InspectItemDefContent", InspectItemDefContent);
    }

    /**
     * 保存
     */
    @PostMapping("/save")
    @RequiresPermissions("scyf:inspectitemdefcontent:save")
    public R save(@RequestBody InspectItemDefContent InspectItemDefContent){
        ValidatorUtils.validateEntity(InspectItemDefContent);
        InspectItemDefContentService.save(InspectItemDefContent);
        return R.ok();
    }

    /**
     * 修改
     */
    @PostMapping("/update")
    @RequiresPermissions("scyf:inspectitemdefcontent:update")
    public R update(@RequestBody InspectItemDefContent InspectItemDefContent){
        ValidatorUtils.validateEntity(InspectItemDefContent);
        InspectItemDefContentService.update(InspectItemDefContent);
        
        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping(value="/delete",method = RequestMethod.DELETE)
    @RequiresPermissions("scyf:inspectitemdefcontent:delete")
    public R delete(@RequestBody Long[] inspectItemDefContentIds){
        InspectItemDefContentService.removeByIds(Arrays.asList(inspectItemDefContentIds));

        return R.ok();
    }

}
