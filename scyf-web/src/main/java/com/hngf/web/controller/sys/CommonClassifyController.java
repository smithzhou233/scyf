package com.hngf.web.controller.sys;

import java.util.Arrays;
import java.util.Date;
import java.util.Map;


import com.hngf.common.utils.Constant;
import com.hngf.web.common.annotation.SysLog;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import com.hngf.entity.sys.CommonClassify;
import com.hngf.service.sys.CommonClassifyService;
import com.hngf.common.utils.PageUtils;
import com.hngf.common.utils.R;
import com.hngf.common.validator.ValidatorUtils;

import static com.hngf.web.common.shiro.ShiroUtils.*;


/**
 * 公司通用分类表
 *
 * @author hngf
 * @email 
 * @date 2020-05-20 17:31:55
 */
@Api(value="公司通用分类表",tags = {"公司通用分类表"})
@RestController
@RequestMapping("sys/commonclassify")
public class CommonClassifyController {
    @Autowired
    private CommonClassifyService CommonClassifyService;

    /**
    * @Author: zyj
    * @Description:默认渲染列表数据
    * @Param params companyId 企业id   classifyType 类型：1隐患类型；2检查表类型；4任务检查类型；（可拓展使用，）
     * classifyValue 配置值：
     * 【检查表类型：0分级管控；1专业检查表；】
     * （可以按需配置，已备注的已经启用）
     * keyword 关键词
    * @Date 14:48 2020/5/22
    */
    @ApiOperation(value = "公司通用分类表", notes="params获取公司通用分类表数据")
    @RequestMapping(value = "/list",method = RequestMethod.GET)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum", value = "第几页", required = true, paramType = "query", dataType = "integer"),
            @ApiImplicitParam(name = "pageSize", value = "每页多少条数据", required = true, paramType = "query", dataType = "integer"),
            @ApiImplicitParam(name = "keyword", value = "查询关键字", required = false, paramType = "query", dataType = "string")
    })
    @RequiresPermissions("sys:commonclassify:list")
    public R list(@RequestParam(required = false) Map<String, Object> params){
        int pageNum = null!= params.get(Constant.PAGE_PAGENUM) ? Integer.parseInt(params.get(Constant.PAGE_PAGENUM).toString()) : 1;
        int pageSize = null != params.get(Constant.PAGE_PAGESIZE) ? Integer.parseInt(params.get(Constant.PAGE_PAGESIZE).toString()) : Constant.PAGE_SIZE;
        //获取企业id  companyId
        Long companyId=getUser().getCompanyId();
        params.put("companyId",companyId);
        PageUtils page = CommonClassifyService.queryPage(params,pageNum,pageSize,null);
        return R.ok().put("data", page);
    }


    /**
    * @Author: zyj
    * @Description:通过classifyId获取公司通用分类表数据
    * @Param classifyId 公司通用分类表id
    * @Date 14:48 2020/5/22
    */
    @ApiOperation(value = "公司通用分类表", notes="通过classifyId获取公司通用分类表数据")
    @RequestMapping(value="/info/{classifyId}",method = RequestMethod.GET)
    @RequiresPermissions("sys:commonclassify:info")
    public R info(@PathVariable("classifyId") Long classifyId){
        CommonClassify CommonClassify = CommonClassifyService.getById(classifyId);

        return R.ok().put("data", CommonClassify);
    }

    /**
    * @Author: zyj
    * @Description:保存信息
    * @Param CommonClassify 公司通用分类表实体类
    * @Date 14:48 2020/5/22
     */
    @ApiOperation(value = "公司通用分类表", notes="添加一条数据")
    @PostMapping("/save")
    @RequiresPermissions("sys:commonclassify:save")
    @SysLog("保存公司通用分类")
    public R save(@RequestBody CommonClassify commonClassify){

        ValidatorUtils.validateEntity(commonClassify);
        commonClassify.setCompanyId(getCompanyId());
        commonClassify.setCreatedBy(getUserId());
        commonClassify.setCreatedTime(new Date());
        commonClassify.setDelFlag(0);
        if (commonClassify.getClassifyType().equals(1)) {
            commonClassify.setClassifyValue("hdanger");
        } else if (commonClassify.getClassifyType().equals(2)) {
            commonClassify.setClassifyValue("1");
        } else if (commonClassify.getClassifyType().equals(4)) {
            commonClassify.setClassifyValue("1");
        }
        if (!StringUtils.isEmpty(commonClassify.getClassifyId()) && commonClassify.getClassifyId() > 0L) {
            this.CommonClassifyService.update(commonClassify);
        } else {
            this.CommonClassifyService.save(commonClassify);
        }

        return R.ok();
    }

    /**
     * @Author: zyj
     * @Description:修改信息
     * @Param CommonClassify 公司通用分类表实体类
     * @Date 14:48 2020/5/22
     */
    @ApiOperation(value = "公司通用分类表", notes="修改一条数据")
    @PostMapping("/update")
    @RequiresPermissions("sys:commonclassify:update")
    @SysLog("修改公司通用分类")
    public R update(@RequestBody CommonClassify CommonClassify){
        ValidatorUtils.validateEntity(CommonClassify);
        CommonClassify.setUpdatedBy(getUserId());
        CommonClassify.setUpdatedTime(new Date());
        CommonClassifyService.update(CommonClassify);
        
        return R.ok();
    }
    /**
     * @Author: zyj
     * @Description:删除信息
     * @Param classifyId 公司通用分类表id
     * @Date 14:48 2020/5/22
     */
    @ApiOperation(value = "公司通用分类表", notes="删除一条数据")
    @RequestMapping(value="/delete/{classifyIds}",method = RequestMethod.DELETE)
    @RequiresPermissions("sys:commonclassify:delete")
    @SysLog("删除公司通用分类")
     public R delete(@PathVariable("classifyIds") Long classifyIds){
        CommonClassifyService.removeById(classifyIds);

        return R.ok();
     }
    /**
     * @Author: zyj
     * @Description:批量删除信息
     * @Param classifyId 公司通用分类表id
     * @Date 14:48 2020/5/22
     */
    @RequestMapping(value="/deletes",method = RequestMethod.DELETE)
    @RequiresPermissions("sys:commonclassify:delete")
    @SysLog("删除公司通用分类")
    public R deletes(@RequestBody Long[] classifyIds){
        CommonClassifyService.removeByIds(Arrays.asList(classifyIds));

        return R.ok();
    }

}
