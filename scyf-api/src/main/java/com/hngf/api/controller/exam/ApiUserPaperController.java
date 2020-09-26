package com.hngf.api.controller.exam;

import com.hngf.api.controller.BaseController;
import com.hngf.common.utils.Constant;
import com.hngf.common.utils.PageUtils;
import com.hngf.common.utils.R;
import com.hngf.dto.exam.UserPaperDto;
import com.hngf.entity.exam.UserPaper;
import com.hngf.service.exam.UserPaperService;
import com.hngf.vo.exam.UserPaperVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.Example;
import io.swagger.annotations.ExampleProperty;
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
@RequestMapping("api/exam/userpaper")
@Api(value="用户试卷管理",tags = {"用户试卷管理"})
public class ApiUserPaperController extends BaseController {
    @Autowired
    private UserPaperService userPaperService;

    /**
     * 列表
     */
    @RequestMapping(value = "/list",method = RequestMethod.GET)
    @ApiOperation(value = "查询用户试卷数据列表", response = UserPaper.class, produces="application/json" , notes="查询用户试卷数据列表")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name="token", dataType = "String" , required = true, value = "token值" ),
            @ApiImplicitParam(paramType = "query",name = "pageNum",dataType = "long",required = true,value = "当前页码",defaultValue = "1"),
            @ApiImplicitParam(paramType = "query",name = "pageSize",dataType = "long",required = true,value = "每页显示记录数",defaultValue = "10"),
            @ApiImplicitParam(paramType = "query",name = "userPaperStatus",dataType = "int",required = false,value = "试卷状态：0 未提交。1 提交",defaultValue = "0"),
            @ApiImplicitParam(paramType = "query",name = "paperName", dataType = "String", required = false, value = "试卷名称" )
    })
    public R userPaperList(@ApiIgnore @RequestParam(required = false) Map<String, Object> params){

        // 设置当前用户的id
        params.put("userId", getUserId());
        int pageNum = null!= params.get(Constant.PAGE_PAGENUM) ? Integer.parseInt(params.get(Constant.PAGE_PAGENUM).toString()) : 1;
        int pageSize = null != params.get(Constant.PAGE_PAGESIZE) ? Integer.parseInt(params.get(Constant.PAGE_PAGESIZE).toString()) : Constant.PAGE_SIZE;

        PageUtils page = userPaperService.queryPage(params,pageNum,pageSize,null);

        return R.ok().put("data", page);
    }

    /**
     * 信息
     */
    @RequestMapping(value="/info/{userPaperId}",method = RequestMethod.GET)
    @ApiOperation(value="用户试卷信息",response = UserPaperDto.class, produces="application/json" ,notes = "用户试卷信息" )
    @ApiImplicitParams({
        @ApiImplicitParam(paramType = "header", name="token", dataType = "String" , required = true, value = "token值" ),
        @ApiImplicitParam(name = "userPaperId", value = "用户试卷Id", required = true, paramType = "path", dataType = "long")
    })
    public R info(@PathVariable("userPaperId") Long userPaperId){
        return R.ok().put("data", userPaperService.findDtoById(userPaperId));
    }

    /**
     * 考试 提交答案
     */
    @RequestMapping(value="/exam/submit",method = RequestMethod.POST)
    @ApiOperation(value="交卷", produces="application/json" ,notes = "交卷" )
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name="token", dataType = "String" , required = true, value = "token值" )
    })
    public R examSubmit(@ApiParam(name = "userPaperVo",value = "交卷信息", examples = @Example({@ExampleProperty(value = "{\"paperId\":\"试卷Id\",\"paperQuestionsMap\":{\"试题Id\":\"试题答案\",\"试题Id\":\"试题答案\"}}", mediaType = "application/json")})) @RequestBody UserPaperVo userPaperVo){
        userPaperService.examSumbit(userPaperVo,super.getUserId(),super.getGroupId(),super.getCompanyId());
        return R.ok();
    }
}
