package com.hngf.api.controller.train;


import com.hngf.api.controller.BaseController;
import com.hngf.common.utils.Constant;
import com.hngf.common.utils.PageUtils;
import com.hngf.common.utils.R;
import com.hngf.service.train.TrainInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@Api(value = "App培训计划内容管理",tags = {"培训计划内容管理"})
@RestController
@RequestMapping("api/traininfo")
public class TrainInfoController extends BaseController {
    @Autowired
    private TrainInfoService trainInfoService;

    /**
     * 查询公司培训计划内容列表
     * yfh
     * 2020/07/07
     * @param params
     * @return
     */
    @ApiOperation(value = "查询公司培训计划内容", notes="获取公司培训计划内容数据")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "keyword", value = "培训内容名称", paramType = "query", required = false, dataType = "String")
    })
    @RequestMapping(value = "/list",method = RequestMethod.GET)
    public R list(@RequestParam Map<String, Object> params, HttpServletRequest req){
        params.put("companyId",getCompanyId());
        params.put("groupId",getGroupId());
        int pageNum = null!= params.get(Constant.PAGE_PAGENUM) ? Integer.parseInt(params.get(Constant.PAGE_PAGENUM).toString()) : 1;
        int pageSize = null != params.get(Constant.PAGE_PAGESIZE) ? Integer.parseInt(params.get(Constant.PAGE_PAGESIZE).toString()) : Constant.PAGE_SIZE;

        PageUtils page = trainInfoService.queryPage(params,pageNum,pageSize,null);

        return R.ok().put("data", page).put("path",req.getScheme() + "://" + req.getServerName() + ":" + req.getServerPort() + "/source");
    }

    /**
     * 根据id查看培训内容
     * lxf
     * 2020/09/16
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
    public R info(Long trainInfoId, HttpServletRequest req){
        List<Map<String,Object>> trainInfo = trainInfoService.getById(trainInfoId);
        List<Map<String,Object>> trainInfoAttachment=null;
        if(trainInfo.size()>0){
            for (int i=0;i<trainInfo.size();i++){
                trainInfoAttachment=trainInfoService.findTrainInfoAttachMent(trainInfo.get(i).get("trainInfoId").toString(),1);
                trainInfo.get(i).put("trainInfoAttachment",trainInfoAttachment);
            }
        }
        return R.ok().put("trainInfo", trainInfo).put("path",req.getScheme() + "://" + req.getServerName() + ":" + req.getServerPort() + "/source");
    }
}
