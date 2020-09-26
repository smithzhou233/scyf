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

import com.hngf.entity.score.ScoreSetting;
import com.hngf.service.score.ScoreSettingService;
import com.hngf.common.utils.PageUtils;
import com.hngf.common.utils.R;
import com.hngf.common.validator.ValidatorUtils;
import com.hngf.common.utils.Constant;



/**
 * 绩效考核打分规则配置表
 *
 * @author hngf
 * @email 
 * @date 2020-05-27 10:11:53
 */
@Api(value="绩效考核打分规则配置表",tags = {"绩效考核打分规则配置表"})
@RestController
@RequestMapping("scyf/scoresetting")
public class ScoreSettingController extends BaseController{
    @Autowired
    private ScoreSettingService ScoreSettingService;

    /**
    * @Author: zyj
    * @Description:渲染加分规则表和扣分规则表
    * @Param 
    * @Date 9:51 2020/6/12
    */
    @RequestMapping(value = "/list",method = RequestMethod.GET)
    @RequiresPermissions("scyf:scoresetting:list")
    @ApiOperation(value = "渲染加分规则表和扣分规则表", notes="渲染加分规则表和扣分规则表",response = ScoreSetting.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum", value = "第几页", required = true, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "pageSize", value = "每页多少条数据", required = true, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "companyId", value = "企业id", required = false, paramType = "query", dataType = "Long"),
            @ApiImplicitParam(name = "settingType", value = "加分项还是扣分项;1加分计算法；2扣分计算法；", required = false, paramType = "query", dataType = "String")
    })
    public R list(@RequestParam(required = false) Map<String, Object> params){

        int pageNum = null!= params.get(Constant.PAGE_PAGENUM) ? Integer.parseInt(params.get(Constant.PAGE_PAGENUM).toString()) : 1;
        int pageSize = null != params.get(Constant.PAGE_PAGESIZE) ? Integer.parseInt(params.get(Constant.PAGE_PAGESIZE).toString()) : Constant.PAGE_SIZE;
         Long companyId=getCompanyId();
         params.put("companyId",companyId);
        PageUtils pageUtils = ScoreSettingService.queryPage(params, pageNum, pageSize, null);
        PageUtils page = pageUtils;

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping(value="/info/{scoreSettingId}",method = RequestMethod.GET)
    @RequiresPermissions("scyf:scoresetting:info")
    public R info(@PathVariable("scoreSettingId") Long scoreSettingId){
        ScoreSetting ScoreSetting = ScoreSettingService.getById(scoreSettingId);

        return R.ok().put("ScoreSetting", ScoreSetting);
    }

    /**
     * 保存
     */
    @PostMapping("/save")
    @RequiresPermissions("scyf:scoresetting:save")
    public R save(@RequestBody ScoreSetting ScoreSetting){

        ValidatorUtils.validateEntity(ScoreSetting);
        ScoreSetting.setDelFlag(0);
        ScoreSetting.setCreatedTime(new Date());
        ScoreSetting.setCreatedBy(getUserId());
        ScoreSettingService.save(ScoreSetting);

        return R.ok();
    }

    /**
    * @Author: zyj
    * @Description:修改加分或扣分的分数，修改配置状态
    * @Param
    * @Date 10:20 2020/6/12
    */
    @PostMapping("/update")
    @RequiresPermissions("scyf:scoresetting:update")
    @ApiOperation(value = "渲染加分规则表和扣分规则表", notes="渲染加分规则表和扣分规则表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "scoreSettingId", value = "主键id", required = false, paramType = "query", dataType = "Long"),
            @ApiImplicitParam(name = "settingScore", value = "分数", required = false, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "settingStatus", value = "状态:1已启动；0未启动", required = false, paramType = "query", dataType = "int")
    })
    public R update(@RequestBody ScoreSetting ScoreSetting){
        ValidatorUtils.validateEntity(ScoreSetting);
        ScoreSetting.setUpdatedTime(new Date());
        ScoreSetting.setUpdatedBy(getUserId());
        ScoreSettingService.update(ScoreSetting);
        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping(value="/delete",method = RequestMethod.DELETE)
    @RequiresPermissions("scyf:scoresetting:delete")
    public R delete(@RequestBody Long[] scoreSettingIds){
        ScoreSettingService.removeByIds(Arrays.asList(scoreSettingIds));

        return R.ok();
    }

}
