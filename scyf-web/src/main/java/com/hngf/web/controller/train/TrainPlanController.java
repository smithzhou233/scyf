package com.hngf.web.controller.train;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.hngf.common.utils.Constant;
import com.hngf.common.utils.PageUtils;
import com.hngf.common.utils.R;
import com.hngf.common.validator.ValidatorUtils;
import com.hngf.entity.train.TrainPlan;
import com.hngf.service.train.TrainAttachmentService;
import com.hngf.service.train.TrainInfoService;
import com.hngf.service.train.TrainPlanService;
import com.hngf.web.controller.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 公司培训计划
 *
 * @author yfh
 * @email 
 * @date 2020-05-26 08:32:53
 */
@Api(value = "公司培训计划管理",tags = {"公司培训计划管理"})
@RestController
@RequestMapping("scyf/trainplan")
public class TrainPlanController extends BaseController{
    @Autowired
    private TrainPlanService trainPlanService;
    @Autowired
    private TrainInfoService trainInfoService;

    @Autowired
    private TrainAttachmentService trainAttachmentService;

    /**
     * 查看培训计划列表
     * yfh
     * 2020/05/26
     * @param params
     * @return
     */
    @ApiOperation(value = "查询公司培训计划", notes="获取公司培训计划数据")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "keyword", value = "培训计划名称", paramType = "query", required = false, dataType = "String"),
        @ApiImplicitParam(name = "pageNum", value = "第几页", required = true, paramType = "query", dataType = "int"),
        @ApiImplicitParam(name = "pageSize", value = "每页多少条数据", required = true, paramType = "query", dataType = "int")
    })
    @RequestMapping(value = "/list",method = RequestMethod.GET)
    @RequiresPermissions("scyf:trainplan:list")
    public R list(@RequestParam(required = false) Map<String, Object> params){
        params.put("companyId",getCompanyId());
//        params.put("groupId",getGroupId());
        params.put("userId",getUserId());
        int pageNum = null!= params.get(Constant.PAGE_PAGENUM) ? Integer.parseInt(params.get(Constant.PAGE_PAGENUM).toString()) : 1;
        int pageSize = null != params.get(Constant.PAGE_PAGESIZE) ? Integer.parseInt(params.get(Constant.PAGE_PAGESIZE).toString()) : Constant.PAGE_SIZE;

        PageUtils page = trainPlanService.queryPagePc(params,pageNum,pageSize,null);

        return R.ok().put("data", page);
    }


    /**
     *根据培训计划id回显培训计划
     * yfh
     * 2020/05/26
     * @param trainPlanId
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
    @RequiresPermissions("scyf:trainplan:info")
    public R info(Integer trainPlanId,HttpServletRequest req){
        List<Map<String,Object>> trainPlan = trainPlanService.getById(trainPlanId);
        List<Map<String,Object>> trainPlanAttachment=null;
        if(trainPlan.size()>0){
            for (int i=0;i<trainPlan.size();i++){
                trainPlanAttachment=trainInfoService.findTrainInfoAttachMent(trainPlan.get(i).get("trainPlanId").toString(),2);
                trainPlan.get(i).put("trainPlanAttachment",trainPlanAttachment);
            }
        }
        return R.ok().put("data", trainPlan).put("path",req.getScheme() + "://" + req.getServerName() + ":" + req.getServerPort() + "/source");
    }

    /**
     * 保存培训计划
     * yfh
     * 2020/05/26
     * @param map
     * @return
     */
    @ApiOperation(value = " 保存培训计划", notes=" 保存培训计划")
    @ApiImplicitParams({
       @ApiImplicitParam(name = "trainPlanName", value = "培训计划名称", paramType = "query", required = true, dataType = "String"),
       @ApiImplicitParam(name = "trainType", value = "培训类型",  paramType = "query", required = true, dataType = "int"),
       @ApiImplicitParam(name = "trainGroupIds", value = "受训群组",  paramType = "query", required = true, dataType = "String"),
       @ApiImplicitParam(name = "trainPlanDate", value = "培训计划时间",  paramType = "query", required = true, dataType = "date"),
       @ApiImplicitParam(name = "trainPlanContent", value = "计划培训内容", paramType = "query",  required = true, dataType = "String"),
       @ApiImplicitParam(name = "trainPlanAddress", value = "培训地点",  paramType = "query", required = true, dataType = "String"),
       @ApiImplicitParam(name = "warnFlag", value = "是否提前一天预警",  paramType = "query", required = true, dataType = "int"),
       @ApiImplicitParam(name = "trainAttachmentName", value = "附件名称",  paramType = "query",  dataType = "String"),
       @ApiImplicitParam(name = "trainExtendName", value = "扩展名",  paramType = "query",  dataType = "String"),
       @ApiImplicitParam(name = "trainThumbnailUrl", value = "缩略图地址",  paramType = "query", dataType = "String"),
       @ApiImplicitParam(name = "mimeType", value = "MIME类型",  paramType = "query", dataType = "String"),
       @ApiImplicitParam(name = "imageWidth", value = "图片宽度",  paramType = "query",  dataType = "String"),
       @ApiImplicitParam(name = "imageHeight", value = "图片高度",  paramType = "query", dataType = "String"),
       @ApiImplicitParam(name = "trainSavePath", value = "保存路径",  paramType = "query",  dataType = "String"),
       @ApiImplicitParam(name = "fileSize", value = "文件大小",  paramType = "query", dataType = "String")
    })
    @PostMapping("/save")
    @RequiresPermissions("scyf:trainplan:save")
    @Transactional(rollbackFor = Exception.class)
    public R save(@ApiIgnore @RequestParam(required = false)  Map<String,Object> map){
        JSONObject paramsJson = new JSONObject(map);
        TrainPlan trainPlan = (TrainPlan) JSON.toJavaObject(paramsJson, TrainPlan.class);
        ValidatorUtils.validateEntity(trainPlan);

        trainPlan.addPrefix(getUserId(), getCompanyId(), getCompanyGroupId());
        trainPlanService.save(trainPlan);
        // 处理培训计划附件
        map.put("attachmentType",2);
        trainAttachmentService.addBatchFromMap(map,trainPlan.getTrainPlanId(), getUserId(),false);
        return R.ok();
    }

    /**
     * 修改培训计划
     * yfh
     * 2020/05/26
     * @param map
     * @return
     */
    @ApiOperation(value = "修改培训计划", notes=" 修改培训计划")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "trainPlanId", value = "培训计划ID", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = "trainPlanName", value = "培训计划名称", paramType = "query", required = false, dataType = "String"),
            @ApiImplicitParam(name = "trainType", value = "培训类型", paramType = "query", required = false, dataType = "int"),
            @ApiImplicitParam(name = "trainGroupIds", value = "受训群组", paramType = "query", required = false, dataType = "String"),
            @ApiImplicitParam(name = "trainPlanDate", value = "培训计划时间", paramType = "query", required = false, dataType = "data"),
            @ApiImplicitParam(name = "trainPlanContent", value = "计划培训内容", paramType = "query", required = false, dataType = "String"),
            @ApiImplicitParam(name = "trainPlanAddress", value = "培训地点", paramType = "query", required = false, dataType = "String"),
            @ApiImplicitParam(name = "warnFlag", value = "是否提前一天预警", paramType = "query", required = false, dataType = "int"),
            @ApiImplicitParam(name = "trainAttachmentName", value = "附件名称", paramType = "query", required = false, dataType = "String"),
            @ApiImplicitParam(name = "trainExtendName", value = "扩展名", paramType = "query", required = false, dataType = "String"),
            @ApiImplicitParam(name = "trainSavePath", value = "保存路径", paramType = "query", required = false, dataType = "String"),
            @ApiImplicitParam(name = "fileSize", value = "文件大小", paramType = "query", required = false, dataType = "int")
    })
    @PostMapping("/update")
    @RequiresPermissions("scyf:trainplan:update")
    public R update(@ApiIgnore @RequestParam(required = false) Map<String,Object> map){
        JSONObject paramsJson = new JSONObject(map);
        TrainPlan trainPlan = (TrainPlan) JSON.toJavaObject(paramsJson, TrainPlan.class);
        ValidatorUtils.validateEntity(trainPlan);
        trainPlan.updatePrefix(getUserId());
        trainPlanService.update(trainPlan);

        // 处理培训计划附件
        map.put("attachmentType",2);
        trainAttachmentService.addBatchFromMap(map,trainPlan.getTrainPlanId(), getUserId(),true);

        return R.ok();
    }

    /**
     * 批量删除
     */
    @RequestMapping(value="/deletes",method = RequestMethod.DELETE)
    @RequiresPermissions("scyf:trainplan:delete")
    public R deletes(@RequestBody Long[] trainPlanIds){
        trainPlanService.removeByIds(Arrays.asList(trainPlanIds));

        return R.ok();
    }

    /**
     * 修改培训计划 是否提前一天预警
     * yfh
     * 2020/07/09
     * @param trainPlanId
     * @param warnFlag
     * @return
     */
    @ApiOperation(value = "修改培训计划 是否提前一天预警", notes=" 修改培训计划 是否提前一天预警")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "trainPlanId", value = "培训计划ID", paramType = "query", required = true, dataType = "long"),
            @ApiImplicitParam(name = "warnFlag", value = "是否提前一天预警", paramType = "query", required = true, dataType = "int")
    })
    @PostMapping("/updateWarnFlag")
    public R updateWarnFlag(Long trainPlanId,Integer warnFlag){
        trainPlanService.updateWarnFlag(trainPlanId,warnFlag,getUserId());
        return R.ok();
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
     * 单条删除培训计划
     * yfh
     * 2020/05/26
     * @param trainPlanId
     * @return
     */
    @ApiOperation(value = "删除培训计划", notes=" 删除培训计划")
    @ApiImplicitParam(name = "trainPlanId", value = "培训计划ID", paramType = "query", required = true, dataType = "long")
    @RequestMapping(value="/delete",method = RequestMethod.DELETE)
    @RequiresPermissions("scyf:trainplan:delete")
    public R delete(Long trainPlanId){
        List list=trainPlanService.isDel(trainPlanId);
        if(list.size()>0){
            return R.error(500,"该培训计划有相关内容 不允许删除!");
        }
        long longValue = trainPlanId.longValue();
        trainPlanService.removeById(trainPlanId,getUserId());
        trainAttachmentService.removeByTrainKeyId(longValue,2,getUserId());
        return R.ok();
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

}
