package com.hngf.web.controller.score;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Date;

import com.hngf.entity.sys.Group;
import com.hngf.service.sys.GroupService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
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

import com.hngf.entity.score.ScoreUser;
import com.hngf.service.score.ScoreUserService;
import com.hngf.common.utils.PageUtils;
import com.hngf.common.utils.R;
import com.hngf.common.validator.ValidatorUtils;
import com.hngf.common.utils.Constant;



/**
 * 用户绩效考核总得分表
 *
 * @author hngf
 * @email 
 * @date 2020-05-27 10:11:53
 */
@Api(value="用户绩效考核总得分表",tags = {"用户绩效考核总得分表"})
@RestController
@RequestMapping("scyf/scoreuser")
public class ScoreUserController extends BaseController{
    @Autowired
    private ScoreUserService ScoreUserService;
    @Autowired
    private GroupService groupService;

    /**
     * 列表
     */
    @RequestMapping(value = "/list",method = RequestMethod.GET)
    @RequiresPermissions("scyf:scoreuser:list")
    public R list(@RequestParam Map<String, Object> params){

        int pageNum = null!= params.get(Constant.PAGE_PAGENUM) ? Integer.parseInt(params.get(Constant.PAGE_PAGENUM).toString()) : 1;
        int pageSize = null != params.get(Constant.PAGE_PAGESIZE) ? Integer.parseInt(params.get(Constant.PAGE_PAGESIZE).toString()) : Constant.PAGE_SIZE;

        PageUtils page = ScoreUserService.queryPage(params,pageNum,pageSize,null);

        return R.ok().put("page", page);
    }
    /**
    * @Author: zyj
    * @Description:用户得分统计图和部门得分统计图
    * @Param
    * @Date 14:41 2020/6/12
    */
        @RequestMapping(value = "/findScoreMonthStatistics",method = RequestMethod.GET)
        @RequiresPermissions("scyf:scoreuser:list")
        @ApiOperation(value = "用户得分统计图和部门得分统计图", notes="用户得分统计图和部门得分统计图")
        @ApiImplicitParams({
                @ApiImplicitParam(name = "isMyself", value = "0或1(0部门统计，1用户统计)", required = true, paramType = "query", dataType = "String"),
                @ApiImplicitParam(name = "groupId", value = "只有在部门统计的时候传", required = false, paramType = "query", dataType = "Long"),
                @ApiImplicitParam(name = "year", value = "年份", required = false, paramType = "query", dataType = "string")
        })
      public R findScoreMonthStatistics(@RequestParam(required = false) Map<String, Object> params){
         try {

             String isMyself = params.get("isMyself").toString();
             if (isMyself.equals("1")) {
                 params.put("userId", getUserId());
             } else if (isMyself.equals("0") && StringUtils.isNotEmpty(params.get("groupId").toString())) {
                 Group group = groupService.getById(Long.parseLong(params.get("groupId").toString()));
                 params.put("gLId", group.getGroupLeft());
                 params.put("gRId", group.getGroupRight());
             }

             params.put("companyId",getCompanyId());
             Map<String, List<Integer>> stringListMap = ScoreUserService.getMonthGradeStatistics(params);
             return R.ok().put("data",stringListMap);
         }catch (Exception e){
             logger.error(e.getMessage());
             return R.error("查询失败");
         }
      }
    /**
     * 信息
     */
    @RequestMapping(value="/info/{scoreUserId}",method = RequestMethod.GET)
    @RequiresPermissions("scyf:scoreuser:info")
    public R info(@PathVariable("scoreUserId") Long scoreUserId){
        ScoreUser ScoreUser = ScoreUserService.getById(scoreUserId);

        return R.ok().put("data", ScoreUser);
    }

    /**
     * 保存
     */
    @PostMapping("/save")
    @RequiresPermissions("scyf:scoreuser:save")
    public R save(@RequestBody ScoreUser ScoreUser){

        ValidatorUtils.validateEntity(ScoreUser);
        ScoreUser.setDelFlag(0);
        ScoreUser.setCreatedTime(new Date());
        ScoreUser.setCreatedBy(getUserId());
        ScoreUserService.save(ScoreUser);

        return R.ok();
    }

    /**
     * 修改
     */
    @PostMapping("/update")
    @RequiresPermissions("scyf:scoreuser:update")
    public R update(@RequestBody ScoreUser ScoreUser){
        ValidatorUtils.validateEntity(ScoreUser);
        ScoreUser.setUpdatedTime(new Date());
        ScoreUser.setUpdatedBy(getUserId());
        ScoreUserService.update(ScoreUser);
        
        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping(value="/delete",method = RequestMethod.DELETE)
    @RequiresPermissions("scyf:scoreuser:delete")
    public R delete(@RequestBody Long[] scoreUserIds){
        ScoreUserService.removeByIds(Arrays.asList(scoreUserIds));

        return R.ok();
    }

}
