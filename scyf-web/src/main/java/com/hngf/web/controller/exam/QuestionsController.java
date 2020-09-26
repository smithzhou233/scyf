package com.hngf.web.controller.exam;

import com.hngf.common.utils.Constant;
import com.hngf.common.utils.PageUtils;
import com.hngf.common.utils.R;
import com.hngf.common.validator.ValidatorUtils;
import com.hngf.entity.exam.Questions;
import com.hngf.service.exam.QuestionsService;
import com.hngf.web.controller.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import java.util.Arrays;
import java.util.Date;
import java.util.Map;

/**
 * 试题表
 *
 * @author hngf
 * @email 
 * @date 2020-08-12 15:25:51
 */
@RestController
@RequestMapping("exam/questions")
@Api(value="试题库管理",tags = {"试题管理"})
public class QuestionsController extends BaseController{
    @Autowired
    private QuestionsService questionsService;

    /**
     * 列表
     */
    @RequestMapping(value = "/list",method = RequestMethod.GET)
    @RequiresPermissions("exam:questions:list")
    @ApiOperation(value = "查询试题数据列表",response = Questions.class, notes="查询试题表数据列表"+
            "试题信息查询列表接口关联接口如下：<br>" +
            "1、试题种类：/sys/common/getDictByType?dictType=questions_type  <br>" +
            "2、试题状态：/sys/common/getDictByType?dictType=questions_status  <br>" +
            "3、部门：/sys/common/getGroupLists  <br>"
    )
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query",name = "pageNum",dataType = "Long",required = true,value = "当前页码",defaultValue = "1"),
            @ApiImplicitParam(paramType = "query",name = "pageSize",dataType = "Long",required = true,value = "每页显示记录数",defaultValue = "10"),
            @ApiImplicitParam(paramType = "query",name = "questionsName", dataType = "String", required = false, value = "试题名称" ),
            @ApiImplicitParam(paramType = "query",name = "questionsType", dataType = "int", required = false, value = "试题类型" ),
            @ApiImplicitParam(paramType = "query",name = "questionsStatus", dataType = "int", required = false, value = "启用状态" ),
            @ApiImplicitParam(paramType = "query",name = "groupId", dataType = "Long", required = false, value = "部门" )
    })
    public R list( @ApiIgnore @RequestParam(required = false) Map<String, Object> params){
        // 设置所属企业id
        params.put("companyId", getCompanyId());

        int pageNum = null!= params.get(Constant.PAGE_PAGENUM) ? Integer.parseInt(params.get(Constant.PAGE_PAGENUM).toString()) : 1;
        int pageSize = null != params.get(Constant.PAGE_PAGESIZE) ? Integer.parseInt(params.get(Constant.PAGE_PAGESIZE).toString()) : Constant.PAGE_SIZE;

        PageUtils page = questionsService.queryPage(params,pageNum,pageSize,null);

        return R.ok().put("data", page);
    }
    /**
     * 列表
     */
    @RequestMapping(value = "/list/paper",method = RequestMethod.GET)
    @RequiresPermissions(value = {"exam:paper:save", "exam:paper:update"}, logical = Logical.OR )
    @ApiOperation(value = "查询试题数据列表", response = Questions.class, notes="查询试题数据列表 用来组卷" )
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query",name = "pageNum",dataType = "Long",required = true,value = "当前页码",defaultValue = "1"),
            @ApiImplicitParam(paramType = "query",name = "pageSize",dataType = "Long",required = true,value = "每页显示记录数",defaultValue = "1000"),
            @ApiImplicitParam(paramType = "query",name = "questionsType", dataType = "int", required = false, value = "试题类型1单选2多选3判断" )
    })
    public R listToPaper( @ApiIgnore @RequestParam(required = false) Map<String, Object> params){
        // 设置所属企业id
        params.put("companyId", getCompanyId());

        int pageNum = null!= params.get(Constant.PAGE_PAGENUM) ? Integer.parseInt(params.get(Constant.PAGE_PAGENUM).toString()) : 1;
        int pageSize = null != params.get(Constant.PAGE_PAGESIZE) ? Integer.parseInt(params.get(Constant.PAGE_PAGESIZE).toString()) : Constant.PAGE_SIZE;

        PageUtils page = questionsService.queryPageToPaper(params,pageNum,pageSize);

        return R.ok().put("data", page);
    }


    /**
     * 信息
     */
    @RequestMapping(value="/info/{questionsId}",method = RequestMethod.GET)
    @RequiresPermissions("exam:questions:info")
    @ApiOperation(value="详情信息",response = Questions.class,notes = "试题信息")
    @ApiImplicitParam(name = "questionsId", value = "试题表ID", required = true, paramType = "path", dataType = "long")
    public R info(@PathVariable("questionsId") Long questionsId){
        Questions questions = questionsService.getById(questionsId);

        return R.ok().put("data", questions);
    }

    /**
     * 保存
     */
    @PostMapping("/save")
    @RequiresPermissions("exam:questions:save")
    @ApiOperation(value="保存",produces="application/json",notes = "试题保存" )
    public R save(@RequestBody Questions questions){

        ValidatorUtils.validateEntity(questions);
        questions.addPrefixInit(getUserId(), new Date(), getGroupId(), getCompanyId());
        questionsService.save(questions);

        return R.ok();
    }

    /**
     * 修改
     */
    @PostMapping("/update")
    @RequiresPermissions("exam:questions:update")
    @ApiOperation(value="修改",produces="application/json",notes = "试题修改" )
    public R update(@RequestBody Questions questions){

        ValidatorUtils.validateEntity(questions);
        questions.updatePrefixInit(getUserId());
        questionsService.update(questions);
        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping(value="/delete/{questionsId}",method = RequestMethod.DELETE)
    @RequiresPermissions("exam:questions:delete")
    @ApiOperation(value="删除",produces="application/json",notes = "试题删除")
    @ApiImplicitParam(name = "questionsId", value = "试题ID数组", required = true, paramType = "path", dataType = "long")
    public R delete(@PathVariable("questionsId") Long questionsId){
        if(null != questionsId ){
            questionsService.removeById(questionsId, getUserId());
        }
        return R.ok();
    }

    /**
     * 试题尘封/启用
     */
    @RequestMapping(value ={"/enable","/enable/{status}"}, method = RequestMethod.POST)
    @RequiresPermissions("exam:questions:enable")
    @ApiOperation(value="试题尘封/启用",produces="application/json",notes = "试题尘封1/启用0;地址: /enable 启用 ,/enable/0 启用, /enable/1 尘封")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "ids", value = "试题Id", required = true, paramType = "body", dataType = "long", allowMultiple = true)
    })
    public R enableOrUnable(@RequestBody Long[] ids , @PathVariable(value = "status",required = false)Integer status){
        if(null != ids && ids.length > 0 ){
            if(null == status && 1 != status.intValue() ){
                status = 0;
            }
            questionsService.enableOrUnable(Arrays.asList(ids), status, getUserId());
        }
        return R.ok();
    }
}
