package com.hngf.web.controller.danger;

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

import com.hngf.entity.danger.InspectType;
import com.hngf.service.danger.InspectTypeService;
import com.hngf.common.utils.PageUtils;
import com.hngf.common.utils.R;
import com.hngf.common.validator.ValidatorUtils;
import com.hngf.common.utils.Constant;



/**
 * 1专业性检查；2日常检查；3节假日前后；4事故类比检查；5季节性检查；6综合性检查
 *
 * @author hngf
 * @email 
 * @date 2020-05-28 16:40:19
 */
@RestController
@RequestMapping("scyf/inspecttype")
public class InspectTypeController extends BaseController{
    @Autowired
    private InspectTypeService InspectTypeService;

    /**
     * 列表
     */
    @RequestMapping(value = "/list",method = RequestMethod.GET)
    @RequiresPermissions("scyf:inspecttype:list")
    public R list(@RequestParam Map<String, Object> params){

        int pageNum = null!= params.get(Constant.PAGE_PAGENUM) ? Integer.parseInt(params.get(Constant.PAGE_PAGENUM).toString()) : 1;
        int pageSize = null != params.get(Constant.PAGE_PAGESIZE) ? Integer.parseInt(params.get(Constant.PAGE_PAGESIZE).toString()) : Constant.PAGE_SIZE;

        PageUtils page = InspectTypeService.queryPage(params,pageNum,pageSize,null);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping(value="/info/{inspectTypeId}",method = RequestMethod.GET)
    @RequiresPermissions("scyf:inspecttype:info")
    public R info(@PathVariable("inspectTypeId") Long inspectTypeId){
        InspectType InspectType = InspectTypeService.getById(inspectTypeId);

        return R.ok().put("InspectType", InspectType);
    }

    /**
     * 保存
     */
    @PostMapping("/save")
    @RequiresPermissions("scyf:inspecttype:save")
    public R save(@RequestBody InspectType InspectType){
        ValidatorUtils.validateEntity(InspectType);
        InspectType.setDelFlag(0);
        InspectType.setCreatedTime(new Date());
        InspectType.setCreatedBy(getUserId());
        InspectTypeService.save(InspectType);

        return R.ok();
    }

    /**
     * 修改
     */
    @PostMapping("/update")
    @RequiresPermissions("scyf:inspecttype:update")
    public R update(@RequestBody InspectType InspectType){
        ValidatorUtils.validateEntity(InspectType);
        InspectType.setUpdatedTime(new Date());
        InspectType.setUpdatedBy(getUserId());
        InspectTypeService.update(InspectType);
        
        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping(value="/delete",method = RequestMethod.DELETE)
    @RequiresPermissions("scyf:inspecttype:delete")
    public R delete(@RequestBody Long[] inspectTypeIds){
        InspectTypeService.removeByIds(Arrays.asList(inspectTypeIds));

        return R.ok();
    }

}
