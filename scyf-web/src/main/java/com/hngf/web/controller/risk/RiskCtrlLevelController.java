package com.hngf.web.controller.risk;

import com.hngf.common.utils.Constant;
import com.hngf.common.utils.PageUtils;
import com.hngf.common.utils.R;
import com.hngf.common.validator.ValidatorUtils;
import com.hngf.common.validator.group.AddGroup;
import com.hngf.common.validator.group.UpdateGroup;
import com.hngf.entity.risk.RiskCtrlLevel;
import com.hngf.service.risk.RiskCtrlLevelService;
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

import java.util.Map;


/**
 * 风险管控层级
 *
 * @author hngf
 * @email 
 * @date 2020-05-20 17:36:24
 */
@Api(value="风险管控层级",tags = {"风险管控层级"})
@RestController
@RequestMapping("scyf/riskctrllevel")
public class RiskCtrlLevelController extends BaseController {
    @Autowired
    private RiskCtrlLevelService riskCtrlLevelService;

    /**
     * @Author: zyj
     * @Description:根据条件返回相应的列表
     * @Param companyId 企业id  riskCtrlLevelId 风险管控层级ID keyword关键词
     * @Date 16:50 2020/5/21
     */
    @ApiOperation(value = "风险管控层级表", notes="查询所属企业下的所有数据",response = RiskCtrlLevel.class)
    @RequestMapping(value = "/list",method = RequestMethod.GET)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum", value = "第几页", required = true, paramType = "query", dataType = "int", defaultValue = "1"),
            @ApiImplicitParam(name = "pageSize", value = "每页多少条数据", required = true, paramType = "query", dataType = "int", defaultValue = "10"),
            @ApiImplicitParam(name = "keyword", value = "查询关键字", required = false, paramType = "query", dataType = "string")
    })
    @RequiresPermissions("scyf:riskctrllevel:list")
    public R list(@RequestParam(required = false) Map<String, Object> params){
        int pageNum = null!= params.get(Constant.PAGE_PAGENUM) ? Integer.parseInt(params.get(Constant.PAGE_PAGENUM).toString()) : 1;
        int pageSize = null != params.get(Constant.PAGE_PAGESIZE) ? Integer.parseInt(params.get(Constant.PAGE_PAGESIZE).toString()) : Constant.PAGE_SIZE;
        //获取企业id  companyId
        Long companyId=getUser().getCompanyId();
        Long userid=getUserId();
        params.put("createBy",userid);
        params.put("companyId",companyId);
        Integer count=riskCtrlLevelService.count(companyId);
        if (count==0){
           riskCtrlLevelService.initRiskCtrlLevel(params);
        }
        PageUtils page = riskCtrlLevelService.queryPage(params,pageNum,pageSize,null);
        return R.ok().put("data", page);
    }

     /**
     * @Author: zyj
     * @Description:根据id查询单条数据
     * @Param riskCtrlLevelId 风险管控层级ID
     * @Date 14:40 2020/5/22
     */
    @ApiOperation(value = "风险管控层级表", notes="根据id查询单条数据",response = RiskCtrlLevel.class)
    @RequestMapping(value="/info/{riskCtrlLevelId}",method = RequestMethod.GET)
    @RequiresPermissions("scyf:riskctrllevel:info")
    public R info(@PathVariable("riskCtrlLevelId") Long riskCtrlLevelId){
        RiskCtrlLevel RiskCtrlLevel = riskCtrlLevelService.getById(riskCtrlLevelId);

        return R.ok().put("data", RiskCtrlLevel);
    }

    /**
     * @Author: zyj
     * @Description:新增数据
     * @Param RiskCtrlLevel 风险管控层级表实体
     * @Date 14:40 2020/5/22
     */
    @ApiOperation(value = "风险管控层级表", notes="添加一条数据")
    @PostMapping("/save")
    @RequiresPermissions("scyf:riskctrllevel:save")
    public R save(@RequestBody RiskCtrlLevel riskCtrlLevel){

        ValidatorUtils.validateEntity(riskCtrlLevel, AddGroup.class);
        riskCtrlLevel.addPrefixInit(getUserId(),getCompanyId());
        riskCtrlLevelService.save(riskCtrlLevel);

        return R.ok();
    }

    /**
     * @Author: zyj
     * @Description:修改数据
     * @Param RiskCtrlLevel 风险管控层级表实体
     * @Date 14:40 2020/5/22
     */
    @ApiOperation(value = "风险管控层级表", notes="修改一条数据")
    @PostMapping("/update")
    @RequiresPermissions("scyf:riskctrllevel:update")
    public R update(@RequestBody RiskCtrlLevel riskCtrlLevel){
        ValidatorUtils.validateEntity(riskCtrlLevel, UpdateGroup.class, AddGroup.class);
        riskCtrlLevel.updatePrefixInit(getUserId());
        riskCtrlLevelService.update(riskCtrlLevel);
        
        return R.ok();
    }
    /**
     * @Author: zyj
     * @Description:删除数据
     * @Param riskCtrlLevelId 风险管控层级ID
     * @Date 14:40 2020/5/22
     */
    @ApiOperation(value = "风险管控层级删除", notes="风险管控层级删除，删除一条数据")
    @RequestMapping(value="/delete/{riskCtrlLevelId}",method = RequestMethod.DELETE)
    @RequiresPermissions("scyf:riskctrllevel:delete")
    public R delete(@PathVariable("riskCtrlLevelId") Long riskCtrlLevelId){
        riskCtrlLevelService.removeById(riskCtrlLevelId, getUserId());
        return R.ok();
    }

}
