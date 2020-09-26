package com.hngf.api.controller.train;


import com.hngf.api.controller.BaseController;
import com.hngf.common.utils.Constant;
import com.hngf.common.utils.PageUtils;
import com.hngf.common.utils.R;
import com.hngf.common.validator.Assert;
import com.hngf.common.validator.ValidatorUtils;
import com.hngf.entity.train.TrainPlan;
import com.hngf.service.train.TrainInfoService;
import com.hngf.service.train.TrainPlanService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Api(value = "App公司培训计划管理",tags = {"公司培训计划管理"})
@RestController
@RequestMapping("api/trainplan")
public class TrainPlanController extends BaseController {
    @Autowired
    private TrainPlanService trainPlanService;
    @Autowired
    private TrainInfoService trainInfoService;


    /**
     * 查看培训计划列表
     * yfh
     * 2020/07/07
     * @param params
     * @return
     */
    @ApiOperation(value = "查询公司培训计划", notes="获取公司培训计划数据")
    @ApiImplicitParam(name = "keyword", value = "培训计划名称", paramType = "query", required = false, dataType = "String")
    @RequestMapping(value = "/list",method = RequestMethod.GET)
    public R list(@RequestParam(required = false) Map<String, Object> params){
        params.put("companyId",getCompanyId());
        params.put("groupId",getGroupId());
        int pageNum = null!= params.get(Constant.PAGE_PAGENUM) ? Integer.parseInt(params.get(Constant.PAGE_PAGENUM).toString()) : 1;
        int pageSize = null != params.get(Constant.PAGE_PAGESIZE) ? Integer.parseInt(params.get(Constant.PAGE_PAGESIZE).toString()) : Constant.PAGE_SIZE;
        PageUtils page = trainPlanService.queryPage(params,pageNum,pageSize,null);

        return R.ok().put("data", page);
    }

    /**
     * 查看培训计划类型
     * yfh
     * 2020/05/26
     * @return
     */
    @ApiOperation(value = "查看培训计划类型", notes="查看培训计划类型")
    @RequestMapping(value = "selectTrainType",method = RequestMethod.GET)
    public  R selectTrainType(){
        String trainType="train_type";
        List<Map<String,Object>> trainTypeList=trainPlanService.selectTrainType(trainType);

        return R.ok().put("data", trainTypeList);
    }
    /**
     * 查询有权限的组织树形菜单
     * @return
     */
    @GetMapping("/getGroupLists")
    @ApiOperation(value="查询有权限的组织树形菜单")
    public R getGroupLists() {
        Map<String, Object> params = new HashMap();
        params.put("companyOrOrgId", getCompanyId());
        params.put("groupId", getGroupId());
        return R.ok().put("data", this.trainPlanService.getEleuiTreeList(params));
    }

    /**
     *根据培训计划id回显培训计划
     * yfh
     * 2020/05/26
     * @param
     * @return
     */
    @ApiOperation(value = "根据id回显培训计划", notes="根据id回显培训计划")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "trainPlanId", value = "培训计划Id", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = "返回值", value = "返回值：" +
                    "trainPlanId &nbsp;&nbsp; 培训计划id  trainPlanName培训计划名称  dictName培训类型(trainType代码)  trainPlanDate培训时间<br/>" +
                    "trainPlanContent 培训内容  trainPlanAddress培训地点 trainGroupIds 受训群组<br/>" +
                    "trainAttachmentName  附件名称  trainExtendName 扩展名  trainSavePath 保存路径 <br/>" +
                    "trainThumbnailUrl  缩略图路径  fileSize  文件大小  &nbsp;path  访问文件前缀", paramType = "query", required = false, dataType = "string")
    })
    @RequestMapping(value="/info",method = RequestMethod.GET)
    public R info(@RequestParam(required = false) Map<String, Object> params, HttpServletRequest req){
        Assert.isNull(params.get("trainPlanId"),"培训计划Id不能为空");
        List<Map<String,Object>> trainPlan = trainPlanService.getById(Integer.parseInt(params.get("trainPlanId").toString()));
        List<Map<String,Object>> trainPlanAttachment=null;
        if(trainPlan.size()>0){
            for (int i=0;i<trainPlan.size();i++){
                trainPlanAttachment=trainInfoService.findTrainInfoAttachMent(trainPlan.get(i).get("trainPlanId").toString(),2);
            }
        }
        return R.ok().put("data", trainPlan).put("trainPlanAttachment",trainPlanAttachment).put("path",req.getScheme() + "://" + req.getServerName() + ":" + req.getServerPort() + "/source");
    }

}
