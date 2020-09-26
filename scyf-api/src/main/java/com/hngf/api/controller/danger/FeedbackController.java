package com.hngf.api.controller.danger;

import com.hngf.api.controller.BaseController;
import com.hngf.common.utils.PageUtils;
import com.hngf.common.utils.R;
import com.hngf.common.validator.Assert;
import com.hngf.entity.danger.Feedback;
import com.hngf.service.danger.FeedbackService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.Map;

/**
 *  yss
 */
@RestController
@RequestMapping("/api/scyf/feedback")
@Api(value="问题反馈",tags = {"问题反馈"})
public class FeedbackController  extends BaseController {
    @Autowired
    private  FeedbackService FeedbackService;

    /**
     * 保存
     */
  /*  @PostMapping("/save")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "groupId", value = "问题所在部门", required = true,   dataType = "int"),
            @ApiImplicitParam(name = "imgUrlStr", value = "拍照取证路径", required = false,   dataType = "String"),
            @ApiImplicitParam(name = "sceneRemark", value = "描述", required = false,   dataType = "String"),
    })
    @ApiOperation(value="api【随手拍】问题反馈保存",response = Feedback.class, produces="application/json",notes = "" +
            "问题反馈关联接口如下：<br>" +
            "1、查询当前公司下所有部门  /sys/group/getGroupListOfCompany"
    )
    public R save(@RequestBody Feedback feedback){
        ValidatorUtils.validateEntity(feedback);
        feedback.setCompanyId(getCompanyId());
        feedback.setCreatedBy(getUserId());
        feedback.setCreater(getUserId());
       // feedback.setGroupId(getGroupId());
        feedback.setResultValue(0);      //未处理
        feedback.setDelFlag(0);
        FeedbackService.save(feedback);
        return R.ok();
    }*/
    /**
     * @Author: zyj
     * @Description:【APP】我的，反馈列表查询
     * @Param companyId企业id resultValue 处理结果： 0未处理；1已处理 creater 创建人，即检查人
     * @Date 16:05 2020/6/18
     */
    @RequestMapping(value = "/findAllList",method = RequestMethod.GET)
    @RequiresPermissions("scyf:feedback:list")
    @ApiOperation(value = "【APP】我的，反馈列表查询", notes="【APP】我的，反馈列表查询")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum", value = "第几页", required = true, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "pageSize", value = "每页多少条数据", required = true, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "resultValue", value = "处理结果类型：0未处理；1已处理", required = true, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "creater", value = "创建人，即检查人", required = false, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "companyId", value = "企业id", required = false, paramType = "query", dataType = "int"),

    })
    public R findAllList(@RequestParam(required = false) Map<String, Object> params){
        params.put("companyId",getCompanyId());
        params.put("creater",getUserId());
        PageUtils page = FeedbackService.findAllList(params);
        return R.ok().put("data", page);
    }
    /**
     * @Author: zyj
     * @Description:【APP】我的，反馈列表详情信息
     * @Param feedbackId 主键id
     * @Date 16:30 2020/6/18
     */
    @RequestMapping(value = "/getDetailById",method = RequestMethod.GET)
    @RequiresPermissions("scyf:feedback:list")
    @ApiOperation(value = "【APP】我的，反馈列表详情", notes="【APP】我的，反馈列表详情")
    @ApiImplicitParam(name = "feedbackId", value = "主键id", required = true, paramType = "query", dataType = "Long")
    public R getDetailById(Long feedbackId){
        Assert.isNull(feedbackId,"主键id不能为空");
        Map<String, Object> detailById = FeedbackService.getDetailById(feedbackId);
        return R.ok().put("data",detailById);
    }
    /**
     * @Author: zyj
     * @Description:【APP】提交反馈信息
     * @Param
     * @Date 16:49 2020/6/18
     */
    @PostMapping(value="saveBaseChecked")
    @RequiresPermissions("scyf:feedback:save")
    @ApiOperation(value = "【APP】提交反馈信息", notes="【APP】提交反馈信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", paramType = "header", required = true, dataType = "String"),
            @ApiImplicitParam(name = "url", value = "文件路径", paramType = "query", required = false, dataType = "String"),
            @ApiImplicitParam(name = "extendName", value = "extendName文件尾名", paramType = "query", required = false, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "groupId", dataType = "Long", required = false, value = "问题所在部门"),
            @ApiImplicitParam(paramType = "query", name = "sceneRemark", dataType = "String", required = false, value = "现场情况描述"),
    })
    public R saveBaseChecked(@RequestBody Feedback feedback, String url,String extendName){
        Assert.isNull(feedback.getGroupId(),"群组id不能为空");
        Assert.isNull(feedback.getSceneRemark(),"现场情况描述不能为空");
        feedback.setCompanyId(getCompanyId());
        feedback.setCreatedBy(getUserId());
        feedback.setCreatedTime(new Date());
        feedback.setResultValue(0);
        feedback.setCreater(getUserId());
        feedback.setResultDesc("未处理");
        feedback.setDelFlag(0);
        FeedbackService.saveBaseChecked(feedback,url,extendName);
        return R.ok("添加成功");
    }
}
