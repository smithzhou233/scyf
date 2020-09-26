package com.hngf.web.controller.exam;

import com.hngf.common.utils.Constant;
import com.hngf.common.utils.PageUtils;
import com.hngf.common.utils.R;
import com.hngf.dto.exam.UserPaperDto;
import com.hngf.entity.exam.UserPaper;
import com.hngf.service.exam.UserPaperService;
import com.hngf.vo.exam.UserPaperVo;
import com.hngf.web.controller.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import java.util.Map;

/**
 * 用户试卷表
 *
 * @author lxf
 * @email 
 * @date 2020-08-15 11:57:10
 */
@RestController
@RequestMapping("exam/userpaper")
@Api(value="用户试卷管理",tags = {"用户试卷管理"})
public class UserPaperController extends BaseController{
    @Autowired
    private UserPaperService userPaperService;

    /**
     * 列表
     */
    @RequestMapping(value = "/list",method = RequestMethod.GET)
    @RequiresPermissions("exam:userpaper:list")
    @ApiOperation(value = "查询用户试卷数据列表", response = UserPaper.class, produces="application/json" , notes="查询用户试卷数据列表")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query",name = "pageNum",dataType = "Long",required = true,value = "当前页码",defaultValue = "1"),
            @ApiImplicitParam(paramType = "query",name = "pageSize",dataType = "Long",required = true,value = "每页显示记录数",defaultValue = "10"),
            @ApiImplicitParam(paramType = "query",name = "paperName", dataType = "String", required = false, value = "试卷名称" )
    })
    public R list(@ApiIgnore @RequestParam(required = false) Map<String, Object> params){
        // 设置所属企业id
        params.put("companyId", getCompanyId());

        int pageNum = null!= params.get(Constant.PAGE_PAGENUM) ? Integer.parseInt(params.get(Constant.PAGE_PAGENUM).toString()) : 1;
        int pageSize = null != params.get(Constant.PAGE_PAGESIZE) ? Integer.parseInt(params.get(Constant.PAGE_PAGESIZE).toString()) : Constant.PAGE_SIZE;

        PageUtils page = userPaperService.queryPage(params,pageNum,pageSize,null);

        return R.ok().put("data", page);
    }


    /**
     * 信息
     */
    @RequestMapping(value="/info/{userPaperId}",method = RequestMethod.GET)
    @RequiresPermissions("exam:userpaper:info")
    @ApiOperation(value="用户试卷信息",response = UserPaperDto.class, produces="application/json" ,notes = "用户试卷信息" )
    @ApiImplicitParam(name = "userPaperId", value = "用户试卷Id", required = true, paramType = "path", dataType = "long")
    public R info(@PathVariable("userPaperId") Long userPaperId){
        return R.ok().put("data", userPaperService.findDtoById(userPaperId));
    }

//    /**
//     * 保存
//     */
//    @PostMapping("/save")
//    @RequiresPermissions("exam:userpaper:save")
//    @ApiOperation(value="用户试卷表保存" , produces="application/json" , notes = "用户试卷表保存" +
//            "用户试卷表信息保存接口关联接口如下：<br>" +
//            "1、" +
//            "2、"
//    )
//    public R save(@RequestBody UserPaper userPaper){
//
//        ValidatorUtils.validateEntity(userPaper);
//        userPaper.setDelFlag(0);
//        userPaper.setCreatedTime(new Date());
//        userPaper.setCreatedBy(getUserId());
//        userPaperService.save(userPaper);
//
//        return R.ok();
//    }
//
//    /**
//     * 修改
//     */
//    @PostMapping("/update")
//    @RequiresPermissions("exam:userpaper:update")
//    @ApiOperation(value="用户试卷表修改",  produces="application/json" , notes = "用户试卷表修改" +
//            "用户试卷表信息修改接口关联接口如下：<br>" +
//            "1、" +
//            "2、"
//    )
//    public R update(@RequestBody UserPaper userPaper){
//        ValidatorUtils.validateEntity(userPaper);
//        userPaper.setUpdatedTime(new Date());
//        userPaper.setUpdatedBy(getUserId());
//        userPaperService.update(userPaper);
//        return R.ok();
//    }
//
//    /**
//     * 删除
//     */
//    @RequestMapping(value="/delete",method = RequestMethod.DELETE)
//    @RequiresPermissions("exam:userpaper:delete")
//    @ApiOperation(value="用户试卷表删除", produces="application/json" , notes = "用户试卷表删除")
//    @ApiImplicitParam(name = "userPaperIds", value = "用户试卷表ID", required = true, paramType = "body", dataType = "long" ,allowMultiple = true)
//    public R delete(@RequestBody Long[] userPaperIds){
//        if(null != userPaperIds && userPaperIds.length>0 ){
//            userPaperService.removeByIds(Arrays.asList(userPaperIds));
//        }
//        return R.ok();
//    }


    /**
     * 考试 提交答案
     */
    @RequestMapping(value="/exam/submit",method = RequestMethod.POST)
    @RequiresPermissions("exam:userpaper:info")
//    @ApiOperation(value="交卷", produces="application/json" ,notes = "交卷" )
    public R examSubmit(@RequestBody UserPaperVo userPaperVo){
        userPaperService.examSumbit(userPaperVo,getUserId(),getGroupId(),getCompanyId());
        return R.ok();
    }

}
