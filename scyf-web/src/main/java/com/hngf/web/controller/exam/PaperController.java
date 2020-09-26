package com.hngf.web.controller.exam;

import com.hngf.common.utils.Constant;
import com.hngf.common.utils.PageUtils;
import com.hngf.common.utils.R;
import com.hngf.common.validator.ValidatorUtils;
import com.hngf.dto.exam.PaperDto;
import com.hngf.entity.exam.Paper;
import com.hngf.service.exam.PaperService;
import com.hngf.vo.exam.PaperToUserVo;
import com.hngf.vo.exam.PaperVo;
import com.hngf.web.controller.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
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

import java.util.Map;

/**
 * 试卷表
 *
 * @author lxf
 * @email 
 * @date 2020-08-14 09:39:02
 */
@RestController
@RequestMapping("exam/paper")
@Api(value="试卷管理",tags = {"试卷管理"})
public class PaperController extends BaseController{
    @Autowired
    private PaperService paperService;

    /**
     * 列表
     */
    @RequestMapping(value = "/list",method = RequestMethod.GET)
    @RequiresPermissions("exam:paper:list")
    @ApiOperation(value = "查询试卷表数据列表",  produces="application/json" , notes="查询试卷表数据列表"+
            "试卷信息查询列表接口关联接口如下：<br>" +
            "1、部门：/sys/common/getGroupLists  <br>"
    )
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query",name = "pageNum",dataType = "Long",required = true,value = "当前页码",defaultValue = "1"),
            @ApiImplicitParam(paramType = "query",name = "pageSize",dataType = "Long",required = true,value = "每页显示记录数",defaultValue = "10"),
            @ApiImplicitParam(paramType = "query",name = "paperName", dataType = "String", required = false, value = "试卷名称" ),
            @ApiImplicitParam(paramType = "query",name = "groupId", dataType = "Long", required = false, value = "部门")
    })
    public R list(@ApiIgnore @RequestParam(required = false) Map<String, Object> params){
        // 设置所属企业id
        params.put("companyId", getCompanyId());

        int pageNum = null!= params.get(Constant.PAGE_PAGENUM) ? Integer.parseInt(params.get(Constant.PAGE_PAGENUM).toString()) : 1;
        int pageSize = null != params.get(Constant.PAGE_PAGESIZE) ? Integer.parseInt(params.get(Constant.PAGE_PAGESIZE).toString()) : Constant.PAGE_SIZE;

        PageUtils page = paperService.queryPage(params,pageNum,pageSize,null);

        return R.ok().put("data", page);
    }


    /**
     * 信息
     */
    @RequestMapping(value="/info/{paperId}",method = RequestMethod.GET)
    @RequiresPermissions("exam:paper:info")
    @ApiOperation(value="试卷信息",response = PaperDto.class, produces="application/json" )
    @ApiImplicitParam(name = "paperId", value = "试卷ID", required = true, paramType = "path", dataType = "long")
    public R info(@PathVariable("paperId") Long paperId){
        PaperDto paperDto = paperService.getDtoById(paperId);
        return R.ok().put("data", paperDto);
    }

    /**
     * 保存
     */
    @PostMapping("/save")
    @RequiresPermissions("exam:paper:save")
    @ApiOperation(value="试卷保存" , produces="application/json" , notes = "试卷保存" +
            "试卷表信息保存接口关联接口如下：<br>" +
            "1、部门：/sys/common/getGroupLists  <br>"+
            "2、试题列表：/exam/questions/list/paper?page=1&limit=1000&questionsType=1  <br>"
    )
    public R save( @RequestBody PaperVo paperVo){
        ValidatorUtils.validateEntity(paperVo);
        paperService.save(paperVo, getUserId(),getCompanyId());
        return R.ok();
    }

    /**
     * 修改
     */
    @PostMapping("/update")
    @RequiresPermissions("exam:paper:update")
    @ApiOperation(value="试卷修改",  produces="application/json" , notes = "试卷修改" +
            "试卷信息修改接口关联接口如下：<br>" +
            "1、部门：/sys/common/getGroupLists  <br>" +
            "2、试题列表：/exam/questions/list/paper?page=1&limit=1000&questionsType=1  <br>"
    )
    public R update(@RequestBody PaperVo paperVo){
        ValidatorUtils.validateEntity(paperVo);
        paperService.update(paperVo,getUserId(),getCompanyId());
        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping(value="/delete/{paperId}",method = RequestMethod.DELETE)
    @RequiresPermissions("exam:paper:delete")
    @ApiOperation(value="试卷删除", produces="application/json" , notes = "试卷删除")
    @ApiImplicitParam(name = "paperId", value = "试卷ID", required = true, paramType = "path", dataType = "long" )
    public R delete(@PathVariable("paperId") Long paperId){
        if(null != paperId ){
            paperService.removeById(paperId, getUserId());
        }
        return R.ok();
    }

    /**
     * 发布试卷 、撤销发布 status=1 或不传值得时候 为发布，status=0时 撤销发布
     * @param paperToUserVo
     * @return
     */
    @RequestMapping(value="/release",method = RequestMethod.POST)
    @RequiresPermissions("exam:paper:release")
    @ApiOperation(value="试卷发布/撤销",response = PaperDto.class, produces="application/json" )

    public R releaseOrUnRelease( @RequestBody PaperToUserVo paperToUserVo){
        // 先校验paperId的有效性：paper是否存在，paper是否发布
        Long paperId = paperToUserVo.getPaperId() ;
        if(null == paperId){
            return R.ok("不是有效的参数");
        }
        Paper paper = paperService.getById(paperId) ;
        if(null == paper){
            return R.ok("不是有效的试卷");
        }
        paperService.updatePaperStatus(paperToUserVo,getUserId(),getGroupId(),getCompanyId());
        return R.ok();
    }

    //TODO 自动创建试卷，设置自动创建的规则，然后从题库里随机抽取试题组卷

}
