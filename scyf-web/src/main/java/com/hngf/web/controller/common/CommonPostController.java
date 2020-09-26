package com.hngf.web.controller.common;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.hngf.common.utils.Constant;
import com.hngf.common.utils.PageUtils;
import com.hngf.common.utils.R;
import com.hngf.common.validator.Assert;
import com.hngf.common.validator.ValidatorUtils;
import com.hngf.entity.common.CommonAttachment;
import com.hngf.entity.common.CommonPost;
import com.hngf.service.common.CommonAttachmentService;
import com.hngf.service.common.CommonPostService;
import com.hngf.web.controller.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Date;
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
@RequestMapping("common/commonpost")
@Api(value = "知识库管理",tags = "知识库管理")
public class CommonPostController extends BaseController{
    @Autowired
    private CommonPostService commonPostService;
    @Autowired
    private CommonAttachmentService commonAttachmentService;

    /**
     * 列表
     */
    @GetMapping(value = "/list")
    @RequiresPermissions("common:commonpost:list")
    @ApiOperation(value = "作业指导书列表查询")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "postType", value = "指导书为5不传默认为5", paramType = "query", required = false,defaultValue="5", dataType = "String"),
            @ApiImplicitParam(name = "keyword", value = "搜索关键字", paramType = "query", required = false, dataType = "String"),
            @ApiImplicitParam(name = "pageNum", value = "第几页", required = true, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "pageSize", value = "每页多少条数据", required = true, paramType = "query", dataType = "int")
    })
    public R list(@RequestParam(required = false) Map<String, Object> params){

        int pageNum = null!= params.get(Constant.PAGE_PAGENUM) ? Integer.parseInt(params.get(Constant.PAGE_PAGENUM).toString()) : 1;
        int pageSize = null != params.get(Constant.PAGE_PAGESIZE) ? Integer.parseInt(params.get(Constant.PAGE_PAGESIZE).toString()) : Constant.PAGE_SIZE;
        params.put("createBy",getUserId());
        Object postType=params.get("postType");
        if (postType == null || StringUtils.isBlank(postType.toString())) {
            params.put("postType", Constant.KNOWLEDGE);
        }
        PageUtils page = commonPostService.queryPage(params,pageNum,pageSize,null);
        return R.ok().put("data", page);
    }
    /**
    * @Author: zyj
    * @Description:风险规章制度列表查询
    * @Param
    * @Date 9:48 2020/6/15
    */
    @GetMapping(value = "/queryInstitution")
    @RequiresPermissions("common:commonpost:list")
    @ApiOperation(value = "风险规章制度列表查询")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "postType", value = "规章制度类型默认为9", paramType = "query", required = false,defaultValue="9", dataType = "String"),
            @ApiImplicitParam(name = "keyword", value = "搜索关键字", paramType = "query", required = false, dataType = "String"),
            @ApiImplicitParam(name = "pageNum", value = "第几页", required = true, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "pageSize", value = "每页多少条数据", required = true, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "isPublish", value = "0草稿，1发布", required = true, paramType = "query", dataType = "int")
    })
    public R queryInstitution(@RequestParam(required = false) Map<String, Object> params,HttpServletRequest req){
        Map map=new HashMap();
        int pageNum = null!= params.get(Constant.PAGE_PAGENUM) ? Integer.parseInt(params.get(Constant.PAGE_PAGENUM).toString()) : 1;
        int pageSize = null != params.get(Constant.PAGE_PAGESIZE) ? Integer.parseInt(params.get(Constant.PAGE_PAGESIZE).toString()) : Constant.PAGE_SIZE;
        params.put("companyId",getCompanyId());
        PageUtils page = commonPostService.queryInstitution(params,pageNum,pageSize,null);
        map.put("path",req.getScheme() + "://" + req.getServerName() + ":" + req.getServerPort() + "/source");
        map.put("page",page);
        return R.ok().put("data", map);
    }
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
    public R selectWorkInstruction(@RequestParam Map<String, Object> params,HttpServletRequest req){
        params.put("companyId",getCompanyId());
        Assert.isNull(params.get("riskPointId"),"风险点ID不能为空");
        params.put("createdBy", getUserId());
        params.put("postType", Constant.WORK_INSTRUCTION);
        if (null == params.get("showType") || "".equals(params.get("showType").toString())) {
            params.put("showType", "already");
        }
        Map map=new HashMap();
        map.put("postBook",commonPostService.getWorkInstruction(params));
        map.put("path",req.getScheme() + "://" + req.getServerName() + ":" + req.getServerPort() + "/source");
        return R.ok().put("data", map);
    }


    /**
     * 信息
     */
    @GetMapping(value="/info/{postId}")
    @RequiresPermissions("common:commonpost:info")
    @ApiOperation(value = "信息")
    @ApiImplicitParam(name = "postId", value = "作业指导书ID", paramType = "path", required = true, dataType = "int")
    public R info(@PathVariable("postId") Long postId){
        Assert.isNull(postId,"作业指导书ID不能为空");
        return R.ok().put("data", commonPostService.getById(postId));
    }

    /**
     * 保存
     */
    @PostMapping("/save")
    @RequiresPermissions("common:commonpost:save")
    public R save(@RequestBody CommonPost CommonPost){

        ValidatorUtils.validateEntity(CommonPost);
        CommonPost.setDelFlag(0);
        CommonPost.setCreatedTime(new Date());
        CommonPost.setCreatedBy(getUserId());
        commonPostService.save(CommonPost);

        return R.ok();
    }
    /**
    * @Author: zyj
    * @Description:新增指导书或规章制度
    * @Param  url 文件路径 attachmentName文件名称  extendName文件尾名 size 文件大小
    * @Date 14:59 2020/6/13
    */
    @PostMapping("/savePost")
    @RequiresPermissions("common:commonpost:save")
    @ApiOperation(value = "新增指导书或规章制度")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "url", value = "文件路径", paramType = "query", required = true, dataType = "String"),
            @ApiImplicitParam(name = "attachmentName", value = "attachmentName文件名称", paramType = "query", required = false, dataType = "String"),
            @ApiImplicitParam(name = "extendName", value = "extendName文件尾名", paramType = "query", required = false, dataType = "String"),
            @ApiImplicitParam(name = "size", value = "文件大小", required = false, paramType = "query", dataType = "int")
    })
    public R savePost(@RequestParam(required = false) Map<String, Object> params){
            try {
                JSONObject paramsJson = new JSONObject(params);
                CommonPost commonPost = (CommonPost) JSON.toJavaObject(paramsJson, CommonPost.class);
                String url=null; String attachmentName=null; String extendName=null; String size=null;
                if(params.get("url")!=null&&params.get("url")!=""){
                    url=params.get("url").toString();
                }
                if(params.get("attachmentName")!=null&&params.get("attachmentName")!=""){
                    attachmentName=params.get("attachmentName").toString();
                }
                if(params.get("extendName")!=null&&params.get("extendName")!=""){
                    extendName=params.get("extendName").toString();
                }
                if(params.get("size")!=null&&params.get("size")!=""){
                    size=params.get("size").toString();
                }
                Long accountId = getUserId();
                Long companyId = getCompanyId();
                Long groupId = getGroupId();

                if (commonPost.getGroupId() == null || commonPost.getGroupId() <= 0L) {
                    commonPost.setGroupId(groupId);
                }
                commonPost.addPrefixInit(accountId, companyId);
                String postType = commonPost.getPostType() ;
                if (null == postType || StringUtils.isBlank(postType)) {
                    commonPost.setPostType("0");
                }
                if (null != commonPost.getIsPublish() && 1 == commonPost.getIsPublish()) {
                    commonPost.setPublishTime(new Date());
                }
                commonPostService.save(commonPost);
                String[] url1 =null;
                String[] attachmentName1 =null;
                String[] extendName1 =null;
                String[] size1 =null;
                if(url!=null){
                    url1=url.split(";");
                }
                if(attachmentName!=null){
                    attachmentName1 = attachmentName.split(";");
                }
                if(extendName!=null){
                    extendName1 = extendName.split(";");
                }
                if(size!=null){
                    size1 = size.split(";");
                }
                if(url1!=null){
                    //0代表 知识库--作业指导书
                    Integer ownerType=Constant.OWNER_TYPE_0;
                    for(int i=0;i<url1.length;i++){
                        //获取指导书id
                        Long postId=  commonPost.getPostId();
                        CommonAttachment commonAttachment=new CommonAttachment();
                        commonAttachment.setOwnerId(postId);
                        commonAttachment.setOwnerType(ownerType);
                        commonAttachment.setAttachmentName(attachmentName1[i]);
                        commonAttachment.setExtendName(extendName1[i]);
                        commonAttachment.setFileSize(Integer.valueOf(size1[i]));
                        commonAttachment.setSavePath(url1[i]);
                        commonAttachment.addPrefixInit(accountId);
                        commonAttachmentService.save(commonAttachment);
                    }
                }
            }catch (Exception e){
                logger.error(e.getMessage());
                return R.error("保存失败");
            }
            return   R.ok("保存成功");
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
            CommonPost byId = commonPostService.getById(postId);
            if (null==byId){
                return R.error("获取失败");
            }else {
                //通过postid查出附件信息
                List<CommonAttachment> commonAttachment = commonAttachmentService.selectByOwnerKey(postId);
                data.put("path",req.getScheme() + "://" + req.getServerName() + ":" + req.getServerPort() + "/source");
                data.put("commonPost",byId);
                data.put("commonAttachment",commonAttachment);
            }
            return R.ok().put("data",data);
        }catch (Exception e){
            logger.error(e.getMessage());
            return R.error("获取失败");
        }
     }
    @PostMapping("/updatePost")
    @RequiresPermissions("common:commonpost:update")
    @ApiOperation(value = "修改指导书或规章制度")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "url", value = "文件路径", paramType = "query", required = true, dataType = "String"),
            @ApiImplicitParam(name = "attachmentName", value = "attachmentName文件名称", paramType = "query", required = false, dataType = "String"),
            @ApiImplicitParam(name = "extendName", value = "extendName文件尾名", paramType = "query", required = false, dataType = "String"),
            @ApiImplicitParam(name = "size", value = "文件大小", required = false, paramType = "query", dataType = "int")
    })
     public R updatePost(@RequestParam(required = false) Map<String, Object> params){
        try {
            //@RequestBody CommonPost commonPost,String url,String attachmentName,String extendName,String size
            JSONObject paramsJson = new JSONObject(params);
            CommonPost commonPost = (CommonPost) JSON.toJavaObject(paramsJson, CommonPost.class);
            String url=null; String attachmentName=null; String extendName=null; String size=null;
            if(params.get("url")!=null&&params.get("url")!=""){
                url=params.get("url").toString();
            }
            if(params.get("attachmentName")!=null&&params.get("attachmentName")!=""){
                attachmentName=params.get("attachmentName").toString();
            }
            if(params.get("extendName")!=null&&params.get("extendName")!=""){
                extendName=params.get("extendName").toString();
            }
            if(params.get("size")!=null&&params.get("size")!=""){
                size=params.get("size").toString();
            }
            Long accountId = getUserId();
            Long companyId = getCompanyId();
            Long groupId = getGroupId();
            //0代表指导书
            Integer ownerType=Constant.OWNER_TYPE_0;
            if (commonPost.getGroupId() == null || commonPost.getGroupId() <= 0L) {
                commonPost.setGroupId(groupId);
            }
            commonPost.setCompanyId(companyId);
            commonPost.setUpdatedBy(accountId);
            commonPost.setCreatedTime(new Date());
            commonPost.setDelFlag(0);
            if (StringUtils.isBlank(commonPost.getPostType())) {
                commonPost.setPostType("0");
            }
            if (null != commonPost.getIsPublish() && 1 == commonPost.getIsPublish()) {
                commonPost.setPublishTime(new Date());
            }
            commonPostService.update(commonPost);
            commonAttachmentService.deleteByOwnerId(commonPost.getPostId());
            String[] url1 =null;
            String[] attachmentName1 =null;
            String[] extendName1 =null;
            String[] size1 =null;
            if(url!=null){
                url1=url.split(";");
            }
            if(attachmentName!=null){
                attachmentName1 = attachmentName.split(";");
            }
            if(extendName!=null){
                extendName1 = extendName.split(";");
            }
            if(size!=null){
                size1 = size.split(";");
            }
            if(url1!=null){
                for(int i=0;i<url1.length;i++) {
                    CommonAttachment commonAttachment = new CommonAttachment();
                    commonAttachment.setOwnerId(commonPost.getPostId());
                    commonAttachment.setOwnerType(ownerType);
                    commonAttachment.setAttachmentName(attachmentName1[i]);
                    commonAttachment.setExtendName(extendName1[i]);
                    commonAttachment.setFileSize(Integer.valueOf(size1[i]));
                    commonAttachment.setSavePath(url1[i]);
                    commonAttachment.setCreatedBy(accountId);
                    commonAttachment.setCreatedTime(new Date());
                    commonAttachment.setDelFlag(0);
                    commonAttachmentService.save(commonAttachment);
                }
            }
        }catch (Exception e){
            logger.error(e.getMessage());
            return R.error("保存失败");
        }
        return   R.ok("保存成功");
     }
    /**
     * 修改
     */
    @PostMapping("/update")
    @RequiresPermissions("common:commonpost:update")
    public R update(@RequestBody CommonPost CommonPost){
        ValidatorUtils.validateEntity(CommonPost);
        CommonPost.setUpdatedTime(new Date());
        CommonPost.setUpdatedBy(getUserId());
        commonPostService.update(CommonPost);
        
        return R.ok();
    }
    /**
     * 删除
     */
    @RequestMapping(value="/delete/{postIds}",method = RequestMethod.DELETE)
    @RequiresPermissions("common:commonpost:delete")
    @ApiOperation(value = "删除指导书")
    @ApiImplicitParam(name = "postIds", value = "指导书id", required = true, paramType = "query", dataType = "Long")

    public R delete(@PathVariable Long postIds){
        try {
            CommonPost byId = commonPostService.getById(postIds);
            if (null==byId){
                return R.error("删除失败");
            }
            commonPostService.removeById(postIds);
        }catch (Exception e){
            logger.error(e.getMessage());
            return R.error("删除失败");
        }
        return R.ok("删除成功");
    }
    /**
     * 删除
     */
    @RequestMapping(value="/deletes",method = RequestMethod.DELETE)
    @RequiresPermissions("common:commonpost:delete")
    public R deletes(@RequestBody Long[] postIds){
        commonPostService.removeByIds(Arrays.asList(postIds));

        return R.ok();
    }

    /**
     * 知识点发布
     * 默认是发布 status = 1
     */
    @RequestMapping(value="/publish",method = RequestMethod.POST)
    @ApiOperation(value = "发布指导书", notes = "知识点发布")
    @ApiImplicitParam(name = "postIds", value = "指导书id数组(long[])", required = true, paramType = "body", dataType = "Long")
    public R publish(@RequestBody Long[] postIds){
        if(null == postIds || 0 == postIds.length ){
            return R.error("发布内容为空，发布失败！");
        }
        commonPostService.publishByIds(Arrays.asList(postIds),getUserId());
       return R.ok();
    }
}
