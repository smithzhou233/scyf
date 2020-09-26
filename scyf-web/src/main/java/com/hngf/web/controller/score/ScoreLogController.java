package com.hngf.web.controller.score;

import java.util.Arrays;
import java.util.Map;
import java.util.Date;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.hngf.web.controller.BaseController;

import com.hngf.entity.score.ScoreLog;
import com.hngf.service.score.ScoreLogService;
import com.hngf.common.utils.PageUtils;
import com.hngf.common.utils.R;
import com.hngf.common.validator.ValidatorUtils;
import com.hngf.common.utils.Constant;



/**
 * 绩效考核打分记录日志表
 *
 * @author hngf
 * @email 
 * @date 2020-05-27 10:11:53
 */
@Api(value="绩效考核打分记录日志表",tags = {"绩效考核打分记录日志表"})
@RestController
@RequestMapping("scyf/scorelog")
public class ScoreLogController extends BaseController{
    @Autowired
    private ScoreLogService ScoreLogService;

    /**
    * @Author: zyj
    * @Description:查询用户得分记录日志
    * @Param
    * @Date 10:46 2020/6/12
    */
    @ApiOperation(value = "查询用户得分记录日志", notes="查询用户得分记录日志",response = ScoreLog.class)
    @RequestMapping(value = "/list",method = RequestMethod.GET)
    @RequiresPermissions("scyf:scorelog:list")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum", value = "第几页", required = true, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "pageSize", value = "每页多少条数据", required = true, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "year", value = "年份", required = false, paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "userId", value = "用户id", required = false, paramType = "query", dataType = "Long")
    })
    public R list(@RequestParam(required = false) Map<String, Object> params){

        int pageNum = null!= params.get(Constant.PAGE_PAGENUM) ? Integer.parseInt(params.get(Constant.PAGE_PAGENUM).toString()) : 1;
        int pageSize = null != params.get(Constant.PAGE_PAGESIZE) ? Integer.parseInt(params.get(Constant.PAGE_PAGESIZE).toString()) : Constant.PAGE_SIZE;
        Long companyId=getCompanyId();
        Long userId=getUserId();
        params.put("companyId",companyId);
        params.put("userId",userId);
        PageUtils page = ScoreLogService.queryPage(params,pageNum,pageSize,null);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping(value="/info/{scoreLogId}",method = RequestMethod.GET)
    @RequiresPermissions("scyf:scorelog:info")
    public R info(@PathVariable("scoreLogId") Long scoreLogId){
        ScoreLog ScoreLog = ScoreLogService.getById(scoreLogId);

        return R.ok().put("ScoreLog", ScoreLog);
    }

    /**
     * 保存
     */
    @PostMapping("/save")
    @RequiresPermissions("scyf:scorelog:save")
    public R save(@RequestBody ScoreLog ScoreLog){

        ValidatorUtils.validateEntity(ScoreLog);
        ScoreLog.setDelFlag(0);
        ScoreLog.setCreatedTime(new Date());
        ScoreLog.setCreatedBy(getUserId());
        ScoreLogService.save(ScoreLog);

        return R.ok();
    }

    /**
     * 修改
     */
    @PostMapping("/update")
    @RequiresPermissions("scyf:scorelog:update")
    public R update(@RequestBody ScoreLog ScoreLog){
        ValidatorUtils.validateEntity(ScoreLog);
        ScoreLog.setUpdatedTime(new Date());
        ScoreLog.setUpdatedBy(getUserId());
        ScoreLogService.update(ScoreLog);
        
        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping(value="/delete",method = RequestMethod.DELETE)
    @RequiresPermissions("scyf:scorelog:delete")
    public R delete(@RequestBody Long[] scoreLogIds){
        ScoreLogService.removeByIds(Arrays.asList(scoreLogIds));

        return R.ok();
    }

}
