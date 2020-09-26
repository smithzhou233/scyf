package com.hngf.web.controller.danger;

import com.hngf.common.utils.R;
import com.hngf.common.validator.ValidatorUtils;
import com.hngf.entity.danger.HiddenReview;
import com.hngf.service.danger.HiddenReviewService;
import com.hngf.web.controller.BaseController;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.Date;



/**
 * 整改记录日志
 *
 * @author hngf
 * @email 
 * @date 2020-06-11 10:27:06
 */
@RestController
@RequestMapping("danger/hiddenreview")
public class HiddenReviewController extends BaseController{
    @Autowired
    private HiddenReviewService HiddenReviewService;

    /**
     * 信息
     */
    @RequestMapping(value="/info/{hiddenReviewId}",method = RequestMethod.GET)
    @RequiresPermissions("danger:hiddenreview:info")
    public R info(@PathVariable("hiddenReviewId") Long hiddenReviewId){
        HiddenReview HiddenReview = HiddenReviewService.getById(hiddenReviewId);

        return R.ok().put("data", HiddenReview);
    }

    /**
     * 保存
     */
    @PostMapping("/save")
    @RequiresPermissions("danger:hiddenreview:save")
    public R save(@RequestBody HiddenReview HiddenReview){

        ValidatorUtils.validateEntity(HiddenReview);
        HiddenReview.setDelFlag(0);
        HiddenReview.setCreatedTime(new Date());
        HiddenReview.setCreatedBy(getUserId());
        try {
            HiddenReviewService.save(HiddenReview,getUser().getUserType());
        } catch (Exception e) {
            e.printStackTrace();
            return R.error("保存失败");
        }

        return R.ok();
    }

    /**
     * 修改
     */
    @PostMapping("/update")
    @RequiresPermissions("danger:hiddenreview:update")
    public R update(@RequestBody HiddenReview HiddenReview){
        ValidatorUtils.validateEntity(HiddenReview);
        HiddenReview.setUpdatedTime(new Date());
        HiddenReview.setUpdatedBy(getUserId());
        HiddenReviewService.update(HiddenReview);
        
        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping(value="/delete",method = RequestMethod.DELETE)
    @RequiresPermissions("danger:hiddenreview:delete")
    public R delete(@RequestBody Long[] hiddenReviewIds){
        HiddenReviewService.removeByIds(Arrays.asList(hiddenReviewIds));

        return R.ok();
    }

}
