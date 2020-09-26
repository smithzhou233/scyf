package com.hngf.api.controller.common;

import com.hngf.api.controller.BaseController;
import com.hngf.common.utils.Constant;
import com.hngf.common.utils.PageUtils;
import com.hngf.common.utils.R;
import com.hngf.common.validator.Assert;
import com.hngf.entity.common.CommonAttachment;
import com.hngf.entity.common.CommonPost;
import com.hngf.service.common.CommonAttachmentService;
import com.hngf.service.common.CommonPostService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 知识库
 *
 * @author hngf
 * @email 
 * @date 2020-06-04 11:36:32
 */
@RestController
@RequestMapping("/api/common/commonpost")
@Api(value = "知识库管理",tags = "知识库管理")
public class CommonPostController extends BaseController {
    @Autowired
    private CommonPostService CommonPostService;
    @Autowired
    private CommonAttachmentService commonAttachmentService;

    /**
     * 选择作业指导书
     */
    @GetMapping(value = "/selectWorkInstruction")
    @ApiOperation(value = "选择作业指导书")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "riskPointId", value = "风险点ID", paramType = "form", required = true, dataType = "int"),
            @ApiImplicitParam(name = "showType", value = "查询类型(already：已选择；select：未选择),不传默认already", paramType = "form", required = false,defaultValue="already", dataType = "String"),
            @ApiImplicitParam(name = "keyword", value = "搜索关键字", paramType = "query", required = false, dataType = "String"),
            @ApiImplicitParam(name = "pageNum", value = "第几页", required = true, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "pageSize", value = "每页多少条数据", required = true, paramType = "query", dataType = "int")
    })
    public R selectWorkInstruction(@RequestParam(required = false) Map<String, Object> params,HttpServletRequest req){
        params.put("companyId",getCompanyId());
        Assert.isNull(params.get("riskPointId"),"风险点ID不能为空");
        //params.put("createdBy", getUserId());
        params.put("postType", Constant.WORK_INSTRUCTION);
        if (null == params.get("showType") || "".equals(params.get("showType").toString())) {
            params.put("showType", "already");
        }
        Map map=new HashMap();
        map.put("postBook",CommonPostService.getWorkInstruction(params));
        map.put("path",req.getScheme() + "://" + req.getServerName() + ":" + req.getServerPort() + "/source");
        return R.ok().put("data", map);
    }

    /**
    * @Author: zyj
    * @Description:修改指导书返回的数据以及详情
    * @Param
    * @Date 16:01 2020/6/13
    */
     @RequestMapping(value = "/getById/{postId}",method = RequestMethod.GET)
     @ApiOperation(value = "修改指导书返回的数据以及详情")
     @ApiImplicitParam(name = "postId", value = "主键id", required = true, paramType = "query", dataType = "Long")
     @RequiresPermissions("common:commonpost:info")
     public R getById(@PathVariable("postId") Long postId, HttpServletRequest req){
        try {
            Map<String,Object> data=new HashMap<>();
            CommonPost commonPost = CommonPostService.getById(postId);
            if (null==commonPost){
                return R.error("获取失败");
            }else {
                //通过postid查出附件信息
                List<CommonAttachment> commonAttachment = commonAttachmentService.selectByOwnerKey(postId);
                data.put("path",req.getScheme() + "://" + req.getServerName() + ":" + req.getServerPort() + "/source");
                data.put("commonPost",commonPost);
                data.put("commonAttachment",commonAttachment);
            }
            return R.ok().put("data",data);
        }catch (Exception e){
            return R.error("获取失败");
        }
     }


    /**
     * @Author:
     * @Description:风险规章制度列表查询
     * @Param
     * @Date
     */
    @GetMapping(value = "/queryInstitution")
    @ApiOperation(value = "风险规章制度列表查询")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "postType", value = "规章制度类型默认为9", paramType = "query", required = false,defaultValue="9", dataType = "String"),
            @ApiImplicitParam(name = "pageNum", value = "第几页", required = true, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "pageSize", value = "每页多少条数据", required = true, paramType = "query", dataType = "int"),
    })
    public R queryInstitution(@RequestParam(required = false) Map<String, Object> params,HttpServletRequest req){
        Map map=new HashMap();
        int pageNum = null!= params.get(Constant.PAGE_PAGENUM) ? Integer.parseInt(params.get(Constant.PAGE_PAGENUM).toString()) : 1;
        int pageSize = null != params.get(Constant.PAGE_PAGESIZE) ? Integer.parseInt(params.get(Constant.PAGE_PAGESIZE).toString()) : Constant.PAGE_SIZE;
        params.put("companyId",getCompanyId());
        params.put("isPublish","1");
        PageUtils page = CommonPostService.queryInstitution(params,pageNum,pageSize,null);
        map.put("path",req.getScheme() + "://" + req.getServerName() + ":" + req.getServerPort() + "/source");
        map.put("page",page);
        return R.ok().put("data", map);
    }
}
