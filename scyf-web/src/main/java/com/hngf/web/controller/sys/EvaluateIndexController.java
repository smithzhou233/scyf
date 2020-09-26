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

import com.hngf.entity.sys.EvaluateIndex;
import com.hngf.service.sys.EvaluateIndexService;
import com.hngf.common.utils.PageUtils;
import com.hngf.common.utils.R;
import com.hngf.common.validator.ValidatorUtils;
import com.hngf.common.utils.Constant;



/**
 * 评价指标：
评价方式两种：ls、lec；
 *
 * @author hngf
 * @email 
 * @date 2020-05-27 17:51:02
 */
@RestController
@RequestMapping("scyf/evaluateindex")
public class EvaluateIndexController extends BaseController{
    @Autowired
    private EvaluateIndexService EvaluateIndexService;

    /**
     * 列表
     */
    @RequestMapping(value = "/list",method = RequestMethod.GET)
    @RequiresPermissions("scyf:evaluateindex:list")
    public R list(@RequestParam Map<String, Object> params){

        int pageNum = null!= params.get(Constant.PAGE_PAGENUM) ? Integer.parseInt(params.get(Constant.PAGE_PAGENUM).toString()) : 1;
        int pageSize = null != params.get(Constant.PAGE_PAGESIZE) ? Integer.parseInt(params.get(Constant.PAGE_PAGESIZE).toString()) : Constant.PAGE_SIZE;

        PageUtils page = EvaluateIndexService.queryPage(params,pageNum,pageSize,null);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping(value="/info/{evaluateIndexId}",method = RequestMethod.GET)
    @RequiresPermissions("scyf:evaluateindex:info")
    public R info(@PathVariable("evaluateIndexId") Long evaluateIndexId){
        EvaluateIndex EvaluateIndex = EvaluateIndexService.getById(evaluateIndexId);

        return R.ok().put("EvaluateIndex", EvaluateIndex);
    }

    /**
     * 保存
     */
    @PostMapping("/save")
    @RequiresPermissions("scyf:evaluateindex:save")
    public R save(@RequestBody EvaluateIndex EvaluateIndex){

        ValidatorUtils.validateEntity(EvaluateIndex);
        EvaluateIndex.setDelFlag(0);
        EvaluateIndex.setCreatedTime(new Date());
        EvaluateIndex.setCreatedBy(getUserId());
        EvaluateIndexService.save(EvaluateIndex);

        return R.ok();
    }

    /**
     * 修改
     */
    @PostMapping("/update")
    @RequiresPermissions("scyf:evaluateindex:update")
    public R update(@RequestBody EvaluateIndex EvaluateIndex){
        ValidatorUtils.validateEntity(EvaluateIndex);
        EvaluateIndex.setUpdatedTime(new Date());
        EvaluateIndex.setUpdatedBy(getUserId());
        EvaluateIndexService.update(EvaluateIndex);
        
        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping(value="/delete",method = RequestMethod.DELETE)
    @RequiresPermissions("scyf:evaluateindex:delete")
    public R delete(@RequestBody Long[] evaluateIndexIds){
        EvaluateIndexService.removeByIds(Arrays.asList(evaluateIndexIds));

        return R.ok();
    }

}
