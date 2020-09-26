package com.hngf.web.controller.train;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.hngf.common.utils.Constant;
import com.hngf.common.utils.PageUtils;
import com.hngf.common.utils.R;
import com.hngf.common.validator.ValidatorUtils;
import com.hngf.entity.train.TrainInfo;
import com.hngf.service.train.TrainAttachmentService;
import com.hngf.service.train.TrainInfoService;
import com.hngf.web.controller.BaseController;
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
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;
import java.util.Map;


/**
 *
 *
 * @author yfh
 * @email 
 * @date 2020-05-26 11:59:17
 */
@Api(value = "培训计划内容管理",tags = {"培训计划内容管理"})
@RestController
@RequestMapping("scyf/traininfo")
public class TrainInfoController extends BaseController{
    @Autowired
    private TrainInfoService trainInfoService;

    @Autowired
    private TrainAttachmentService trainAttachmentService;


    /**
     * 查询公司培训计划内容列表
     * yfh
     * 2020/05/26
     * @param params
     * @return
     */
    @ApiOperation(value = "查询公司培训计划内容", notes="获取公司培训计划内容数据")
    @ApiImplicitParams({
    @ApiImplicitParam(name = "keyword", value = "培训内容名称", paramType = "query", required = false, dataType = "String"),
    @ApiImplicitParam(name = "pageNum", value = "第几页", required = true, paramType = "query", dataType = "int"),
    @ApiImplicitParam(name = "pageSize", value = "每页多少条数据", required = true, paramType = "query", dataType = "int")
    })
    @RequestMapping(value = "/list",method = RequestMethod.GET)
    @RequiresPermissions("scyf:traininfo:list")
    public R list(@RequestParam(required = false) Map<String, Object> params){
        params.put("companyId",getCompanyId());
//        params.put("groupId",getGroupId());
        params.put("userId",getUserId());
        int pageNum = null!= params.get(Constant.PAGE_PAGENUM) ? Integer.parseInt(params.get(Constant.PAGE_PAGENUM).toString()) : 1;
        int pageSize = null != params.get(Constant.PAGE_PAGESIZE) ? Integer.parseInt(params.get(Constant.PAGE_PAGESIZE).toString()) : Constant.PAGE_SIZE;

        PageUtils page = trainInfoService.queryPage(params,pageNum,pageSize,null);

        return R.ok().put("page", page);
    }


    /**
     * 根据id查看培训内容
     * yfh
     * 2020/05/26
     * @param trainInfoId
     * @return
     */
    @ApiOperation(value = "根据id查看培训内容", notes="根据id查看培训内容")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "trainInfoId", value = "培训内容Id", required = true, paramType = "query", dataType = "long"),
            @ApiImplicitParam(name = "返回值", value = "返回值：" +
                    "trainInfoId 培训内容id     trainInfoName 培训内容名称   dictName 培训类型(trainType 代码)<br/>" +
                    "trainNum  培训人数  startDate 开始时间   endDate 结束时间  trainGroupIds 受训群组<br/>" +
                    "trainInfoLesson  培训课时  trainInfoAddress 培训地点  trainInfoContent 培训主要内容<br/>" +
                    "trainPlanName 培训计划名称<br/>" +
                    "trainAttachmentName  附件名称  trainExtendName 扩展名  trainSavePath 保存路径 <br/>" +
                    "trainThumbnailUrl  缩略图路径  fileSize  文件大小  &nbsp;path  访问文件前缀", required = false, paramType = "query", dataType = "string")
    })

    @RequestMapping(value="/info",method = RequestMethod.GET)
    @RequiresPermissions("scyf:traininfo:info")
    public R info(Long trainInfoId, HttpServletRequest req){
        List<Map<String,Object>> trainInfo = trainInfoService.getById(trainInfoId);
        List<Map<String,Object>> trainInfoAttachment=null;
        if(trainInfo.size()>0){
            for (int i=0;i<trainInfo.size();i++){
                trainInfoAttachment=trainInfoService.findTrainInfoAttachMent(trainInfo.get(i).get("trainInfoId").toString(),1);
            }
        }
        return R.ok().put("trainInfo", trainInfo).put("trainInfoAttachment",trainInfoAttachment).put("path",req.getScheme() + "://" + req.getServerName() + ":" + req.getServerPort() + "/source");
    }

    /**
     * 保存培训计划内容
     * yfh
     * 2020/05/26
     * @param map
     * @return
     */
    @ApiOperation(value = "保存培训计划内容", notes="保存培训计划内容")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "trainPlanId", value = "培训计划Id", paramType = "query",  required = false, dataType = "int"),
        @ApiImplicitParam(name = "trainInfoName", value = "培训主题", paramType = "query",  required = true, dataType = "String"),
        @ApiImplicitParam(name = "trainType", value = "培训类型",  paramType = "query", required = true, dataType = "int"),
        @ApiImplicitParam(name = "trainGroupIds", value = "受训群组",  paramType = "query", required = true, dataType = "String"),
        @ApiImplicitParam(name = "trainNum", value = "培训人数",  paramType = "query", required = true, dataType = "int"),
        @ApiImplicitParam(name = "startDate", value = "开始时间",  paramType = "query", required = true, dataType = "date"),
        @ApiImplicitParam(name = "endDate", value = "结束时间",  paramType = "query", required = true, dataType = "date"),
        @ApiImplicitParam(name = "trainInfoLesson", value = "培训课时",  paramType = "query", required = true, dataType = "int"),
        @ApiImplicitParam(name = "trainInfoAddress", value = "培训地点",  paramType = "query", required = true, dataType = "String"),
        @ApiImplicitParam(name = "trainInfoContent", value = "培训主要内容",  paramType = "query", required = true, dataType = "String"),
        @ApiImplicitParam(name = "trainAttachmentName", value = "附件名称",  paramType = "query", required = false, dataType = "String"),
        @ApiImplicitParam(name = "trainExtendName", value = "扩展名",  paramType = "query", required = false, dataType = "String"),
        @ApiImplicitParam(name = "trainSavePath", value = "保存路径",  paramType = "query", required = false, dataType = "String"),
        @ApiImplicitParam(name = "fileSize", value = "文件大小",  paramType = "query", required = false, dataType = "int")
    })
    @PostMapping("/save")
    @RequiresPermissions("scyf:traininfo:save")
    public R save(@ApiIgnore @RequestParam(required = false) Map<String,Object> map){
        JSONObject paramsJson = new JSONObject(map);
        TrainInfo trainInfo = (TrainInfo) JSON.toJavaObject(paramsJson, TrainInfo.class);
        ValidatorUtils.validateEntity(trainInfo);
        trainInfo.addPrefix(getUserId(), getCompanyId(), getCompanyGroupId());
        trainInfoService.save(trainInfo);
        // 处理培训计划附件
        map.put("attachmentType",1);
        trainAttachmentService.addBatchFromMap(map,trainInfo.getTrainInfoId(), getUserId(),false);
        return R.ok();
    }

    /**
     * 修改培训计划内容
     * yfh
     * 2020/05/26
     * @param map
     * @return
     */
    @ApiOperation(value = "修改培训计划内容", notes="修改培训计划内容")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "trainInfoId", value = "培训内容Id", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = "trainPlanId", value = "培训计划Id", paramType = "query", required = false, dataType = "int"),
            @ApiImplicitParam(name = "trainInfoName", value = "培训主题", paramType = "query", required = false, dataType = "String"),
            @ApiImplicitParam(name = "trainType", value = "培训类型", paramType = "query", required = false, dataType = "int"),
            @ApiImplicitParam(name = "trainGroupIds", value = "受训群组", paramType = "query", required = false, dataType = "String"),
            @ApiImplicitParam(name = "trainNum", value = "培训人数", paramType = "query", required = false, dataType = "int"),
            @ApiImplicitParam(name = "startDate", value = "开始时间", paramType = "query", required = false, dataType = "date"),
            @ApiImplicitParam(name = "endDate", value = "结束时间", paramType = "query", required = false, dataType = "date"),
            @ApiImplicitParam(name = "trainInfoLesson", value = "培训课时", paramType = "query", required = false, dataType = "int"),
            @ApiImplicitParam(name = "trainInfoAddress", value = "培训地点", paramType = "query", required = false, dataType = "String"),
            @ApiImplicitParam(name = "trainInfoContent", value = "培训主要内容", paramType = "query", required = false, dataType = "String"),
            @ApiImplicitParam(name = "trainAttachmentName", value = "附件名称", paramType = "query", required = false, dataType = "String"),
            @ApiImplicitParam(name = "trainExtendName", value = "扩展名", paramType = "query", required = false, dataType = "String"),
            @ApiImplicitParam(name = "trainSavePath", value = "保存路径", paramType = "query", required = false, dataType = "String"),
            @ApiImplicitParam(name = "fileSize", value = "文件大小", paramType = "query", required = false, dataType = "int")
    })
    @PostMapping("/update")
    @RequiresPermissions("scyf:traininfo:update")
    public R update(@ApiIgnore @RequestParam(required = false) Map<String,Object> map){
        JSONObject paramsJson = new JSONObject(map);
        TrainInfo trainInfo = (TrainInfo) JSON.toJavaObject(paramsJson, TrainInfo.class);
        ValidatorUtils.validateEntity(trainInfo);
        trainInfo.updatePrefix(getUserId());
        trainInfoService.update(trainInfo);

        // 处理培训计划附件
        map.put("attachmentType",1);
        trainAttachmentService.addBatchFromMap(map,trainInfo.getTrainInfoId(), getUserId(),true);
        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping(value="/deletes",method = RequestMethod.DELETE)
    @RequiresPermissions("scyf:traininfo:delete")
    public R deletes(@RequestBody Long[] trainInfoIds){
        trainInfoService.removeByIds(Arrays.asList(trainInfoIds));

        return R.ok();
    }

    /**
     * 单条删除培训计划内容
     * yfh
     * 2020/05/26
     * @param trainInfoId
     * @return
     */
    @ApiOperation(value = "单条删除培训计划内容", notes="单条删除培训计划内容")
    @ApiImplicitParam(name = "trainInfoId", value = "培训内容Id", paramType = "query", required = true, dataType = "long")
    @RequestMapping(value="/delete",method = RequestMethod.DELETE)
    @RequiresPermissions("scyf:traininfo:delete")
    public R delete(Long trainInfoId){
        trainInfoService.removeById(trainInfoId,getUserId());
        trainAttachmentService.removeByTrainKeyId(trainInfoId,1,getUserId());
        return R.ok();
    }

    /**
     * 查询公司培训计划
     * yfh
     * 2020/05/26
     * @return
     */
    @ApiOperation(value = "查询公司培训计划", notes="获取公司培训计划数据")
    @RequestMapping(value = "selectPlans",method = RequestMethod.GET)
    public R selectPlans(){
        List<Map<String,Object>> trainPlans=trainInfoService.selectPlans(Integer.valueOf(getCompanyId().toString()));
        return R.ok().put("trainPlans", trainPlans);
    }

}
