package com.hngf.web.controller.danger;

import java.util.Arrays;
import java.util.Date;
import java.util.Map;

import cn.hutool.core.date.DateUtil;
import com.hngf.common.validator.Assert;
import com.hngf.web.controller.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.hngf.entity.danger.Feedback;
import com.hngf.service.danger.FeedbackService;
import com.hngf.common.utils.PageUtils;
import com.hngf.common.utils.R;
import com.hngf.common.validator.ValidatorUtils;



/**
 * 反馈
 *
 * @author hngf
 * @email 
 * @date 2020-05-20 17:36:24
 */
@Api(value="反馈",tags = {"反馈"})
@RestController
@RequestMapping("scyf/feedback")
public class FeedbackController  extends BaseController {
    @Autowired
    private FeedbackService FeedbackService;

    /**
     * 列表
     */
    @RequestMapping(value = "/list",method = RequestMethod.GET)
    @RequiresPermissions("scyf:feedback:list")
    @ApiOperation(value = "问题反馈", notes="问题反馈查询列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum", value = "第几页", required = true, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "pageSize", value = "每页多少条数据", required = true, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "resultValue", value = "处理结果类型：0未处理；1已处理", required = true, paramType = "query", dataType = "int")
    })
    public R list(@RequestParam(required = false) Map<String, Object> params){
        params.put("companyId",getCompanyId());
        PageUtils page = FeedbackService.queryPage(params);
        return R.ok().put("page", page);
    }
    /**
     * 信息
     */
    @RequestMapping(value="/info/{feedbackId}",method = RequestMethod.GET)
    @RequiresPermissions("scyf:feedback:info")
    @ApiOperation(value = "问题反馈", notes="问题反馈详情",response = Feedback.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "feedbackId", value = "选中反馈记录id", required = true, paramType = "query", dataType = "int")
    })
    public R info(@PathVariable("feedbackId") Long feedbackId){
        
        Feedback Feedback = FeedbackService.getById(feedbackId);
        return R.ok().put("Feedback", Feedback);
    }

    /**
     * 保存
     */
    @PostMapping("/save")
    @RequiresPermissions("scyf:feedback:save")
    public R save(@RequestBody Feedback Feedback){
        ValidatorUtils.validateEntity(Feedback);
        FeedbackService.save(Feedback);
        return R.ok();
    }

    /**
     * 修改
     */
    @PostMapping("/update")
    @RequiresPermissions("scyf:feedback:update")
    public R update(@RequestBody Feedback Feedback){
        ValidatorUtils.validateEntity(Feedback);
        FeedbackService.update(Feedback);
        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping(value="/delete",method = RequestMethod.DELETE)
    @RequiresPermissions("scyf:feedback:delete")
    public R delete(@RequestBody Long[] feedbackIds){
        FeedbackService.removeByIds(Arrays.asList(feedbackIds));

        return R.ok();
    }


    @ApiOperation(value = "问题反馈", notes="关闭问题反馈")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "feedbackId", value = "选中反馈记录id", required = true, paramType = "query", dataType = "int")
    })
    @PostMapping("/close")
    @RequiresPermissions("scyf:feedback:close")
    public R close(Long feedbackId) {
        try {
            Feedback detail = this.FeedbackService.getById(feedbackId);
            if (null != detail && StringUtils.isNotBlank(detail.getResultValue().toString())) {
                Integer resultValue = Integer.parseInt(detail.getResultValue().toString());
                if (resultValue == 1) {
                    return R.ok().put ("msg" , "已经处理,请勿重复处理");
                }
                Feedback feedback = new Feedback();
                feedback.setFeedbackId(feedbackId);
                feedback.setResultValue(1);
                feedback.setResultDesc("已处理，反馈问题已关闭；");
                feedback.setUpdatedBy(super.getUserId());
                feedback.setUpdatedTime(DateUtil.date());
                this.FeedbackService.update(feedback);
                return  R.ok ( "更新成功");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return R.ok( "更新失败");
    }

}
