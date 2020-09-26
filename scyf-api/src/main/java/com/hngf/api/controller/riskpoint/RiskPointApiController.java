package com.hngf.api.controller.riskpoint;

import com.hngf.api.controller.BaseController;
import com.hngf.common.utils.Constant;
import com.hngf.common.utils.PageUtils;
import com.hngf.common.utils.R;
import com.hngf.common.utils.RedisUtils;
import com.hngf.common.validator.Assert;
import com.hngf.entity.risk.RiskPoint;
import com.hngf.entity.risk.RiskSource;
import com.hngf.service.risk.RiskCtrlService;
import com.hngf.service.risk.RiskPointDangerSourceService;
import com.hngf.service.risk.RiskPointService;
import com.hngf.service.risk.RiskSourceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * App风险点
 * @author yfh
 * @email
 * @date 2020-06-11
 */
@Api(value="APP风险点管理",tags = {"APP风险点管理"})
@RestController
@RequestMapping("/api/riskPoint")
public class RiskPointApiController extends BaseController {
    @Autowired
    private RiskPointService riskPointService;
    @Autowired
    private RiskSourceService riskSourceService;
    @Autowired
    private RiskCtrlService riskCtrlService;
    @Autowired
    private RiskPointDangerSourceService riskPointDangerSourceService;

    /**
     * 风险点【分页查询（我的管控的、部门）】
     * yfh
     * 2020/06/11
     * @param params
     * @return
     */
    @ApiOperation("风险点【分页查询（我的管控的、部门）】")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query",name = "pageNum",dataType = "Long",required = true,value = "当前页码",defaultValue = "1"),
            @ApiImplicitParam(paramType = "query",name = "pageSize",dataType = "Long",required = true,value = "每页显示记录数",defaultValue = "10"),
            @ApiImplicitParam(paramType = "query",name = "module",dataType = "String",required = true,value = "1=我的，2=部门"),
            @ApiImplicitParam(paramType = "query",name = "groupId",dataType = "String",required = false,value = "全部（-1）,部门ID"),
            @ApiImplicitParam(paramType = "query", name = "riskPointLevel", dataType = "String", required = false, value = "1重大,2较大,3一般,4较低"),
            @ApiImplicitParam(paramType = "query", name = "isOutOfControl", dataType = "String", required = false, value = "0受控,1失控"),
            @ApiImplicitParam(paramType = "query", name = "dutyGroupId", dataType = "Long", required = false, value = "责任部门ID")
    })
    @RequestMapping(value = {"/queryRiskPointByPage"},method = {RequestMethod.GET})
    public R queryRiskPointByPage(@RequestParam(required = false) Map<String, Object> params){
        try {
            params.put("createdBy", getUserId());
            params.put("aId", getUserId());
            params.put("isActive", 1);
            params.put("companyId",getCompanyId());
            params.put("positionId",getPositionId());
            params.put("gId",getGroupId());
            int pageNum = null!= params.get(Constant.PAGE_PAGENUM) ? Integer.parseInt(params.get(Constant.PAGE_PAGENUM).toString()) : 1;
            int pageSize = null != params.get(Constant.PAGE_PAGESIZE) ? Integer.parseInt(params.get(Constant.PAGE_PAGESIZE).toString()) : Constant.PAGE_SIZE;
            PageUtils page = riskPointService.queryApiPage(params,pageNum,pageSize,null);
            return R.ok().put("data",page);
        } catch (Exception var3) {
            var3.printStackTrace();
            return R.error("获取失败");
        }
    }


    /**
     * 风险点 查询数量
     * yfh
     * 2020/06/11
     * @param params
     * @return
     */
    @ApiOperation("风险点数量【（我的管控的、部门）】")
    @ApiImplicitParam(paramType = "query",name = "module",dataType = "String",required = true,value = "1=我的，2=部门")
    @RequestMapping(value = {"/queryRiskPointNum"},method = {RequestMethod.GET})
    public R queryRiskPointNum(@RequestParam(required = false) Map<String, Object> params){
        try {
            params.put("createdBy", getUserId());
            params.put("aId", getUserId());
            params.put("isActive", 1);
            params.put("companyId",getCompanyId());
            params.put("positionId",getPositionId());
            params.put("gId",getGroupId());
            Map<String,Object> list =riskPointService.queryRiskPointNum(params);
            return R.ok().put("data",list);
        } catch (Exception var3) {
            var3.printStackTrace();
            return R.error("获取失败");
        }
    }

    /**
     * 查看风险点等级
     * yfh
     * 2020/06/231585
     * @return
     */
    @ApiOperation(value = "查看危险源等级", notes="查看危险源等级")
    @RequestMapping(value="/getRiskDangerLevel",method = RequestMethod.GET)
    public R getRiskDangerLevel(){
        return R.ok().put("data",riskSourceService.getRiskDangerLevel());
    }

    /**
     * 查询风险点信息
     * zhangfei 2020/06/17
     * @param riskPointId
     * @return
     */
    /*@ApiOperation(value = "查询风险点信息",response = RiskSource.class,
            notes = "作业指导书：/api/common/commonpost/selectWorkInstruction  <br> "+
                    "风险：/api/riskPoint/queryRiskAndRiskSourceList")
    @RequestMapping(value="/get/{riskPointId}",method = RequestMethod.GET)
    public R get(@PathVariable("riskPointId") Long riskPointId){
        Assert.isNull(riskPointId,"风险点ID不能为空");
        return R.ok().put("data", riskPointService.getRiskPointById(riskPointId));
    }
*/

    /**
     * 回显风险点索引表信息
     * yfh
     * 2020/05/23
     * zhangfei 2020/06/03 修改，返回更多信息
     * @param riskPointId
     * @return
     */
    @ApiOperation(value = "查询风险点信息", response = RiskPoint.class,
            notes = "作业指导书：/api/common/commonpost/selectWorkInstruction  <br> "+
                    "风险：/api/riskPoint/queryRiskAndRiskSourceList")
    @RequestMapping(value="/info/{riskPointId}",method = RequestMethod.GET)
    @RequiresPermissions("scyf:riskpoint:info")
    @ApiImplicitParam(paramType = "query",name = "riskPointId",dataType = "int",required = true,value = "风险点Id")
    public R info(@PathVariable("riskPointId") Long riskPointId, HttpServletRequest req){
        Map map=new HashMap();
        Assert.isNull(riskPointId,"风险点ID不能为空");
        RiskPoint riskPoint = riskPointService.getById(riskPointId);
        map.put("riskPoint",riskPoint);
        //map.put("path",req.getScheme() + "://" + req.getServerName() + ":" + req.getServerPort()+"/source");
        return R.ok().put("data",map );
    }



    /**
     * 查询风险点下的危险源
     * @param params
     * @return
     */
    @GetMapping("/getRiskPointDangerSrc")
    @ApiOperation(value="查询风险点下的危险源",response = RiskSource.class,
            notes = "本接口返回2个集合：<br>" +
                    "data：风险点下的危险源 <br>" +
                    "dangerSrcIds：风险点下危险源的主键 <br>"
    )
    @ApiImplicitParam(name = "riskPointId", value = "风险点ID",required = true, paramType = "query", dataType = "int")
    public R getRiskPointDangerSrc(@RequestParam Map<String, Object> params){
        Assert.isNull(params.get("riskPointId"),"风险点ID不能为空");
        params.put("companyId", getCompanyId());
        params.put("positionId", getPositionId());
        //查看管控层级
        Integer ctlLevel = this.riskCtrlService.getCurrentUserCtlLevel(params);
        //危险源集合
        List<RiskSource> riskSourceList;
        //危险源ID集合
        if (ctlLevel != null && ctlLevel != 0) {
            params.put("ctlLevel", ctlLevel);
            riskSourceList = this.riskPointService.getRiskPointDangerSrc(params);
        } else {
            riskSourceList = new ArrayList();
        }

        return R.ok().put("data",riskSourceList);
    }

    @ApiOperation(value = "风险点详情-【风险】",notes = "1、查询隐患等级数据字典：/sys/common/getDictByType?dictType=hidden_level" +
            "本接口返回参数：<br>" +
            "riskName：检查项 <br>" +
            "riskCode：风险编号 <br>"+
            "riskLevel: 风险等级  <br>" +
            "riskHramFactor :危害因素<br>"+
            "riskConsequence: 危害产生的后果")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query",name = "pageNum",dataType = "Long",required = true,value = "当前页码",defaultValue = "1"),
            @ApiImplicitParam(paramType = "query",name = "pageSize",dataType = "Long",required = true,value = "每页显示记录数",defaultValue = "10"),
            @ApiImplicitParam(paramType = "query", name = "riskPointId", dataType = "String", required = true, value = "风险点ID")})
    @RequestMapping(
            value = {"/queryRiskAndRiskSourceList"}, method = {RequestMethod.GET}
    )
    public R queryRiskAndRiskSourceList(@RequestParam Map<String, Object> params) {
        try {
            params.put("companyId", getCompanyId());
            params.put("positionId", getPositionId());
            PageUtils page =  this.riskPointDangerSourceService.findRiskAndRiskSourceList(params);
            return R.ok().put("data",page);
        } catch (Exception var3) {
            var3.printStackTrace();
            return R.error("查询风险点详情下的风险失败");
        }
    }


}
