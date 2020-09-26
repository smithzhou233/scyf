package com.hngf.web.controller.risk;

import com.alibaba.fastjson.JSONObject;
import com.hngf.common.utils.PageUtils;
import com.hngf.common.utils.R;
import com.hngf.common.validator.ValidatorUtils;
import com.hngf.entity.risk.Risk;
import com.hngf.entity.sys.Info;
import com.hngf.service.risk.RiskCtrlService;
import com.hngf.service.risk.RiskService;
import com.hngf.service.sys.InfoService;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



/**
 * 风险定义表
 *
 * @author hngf
 * @email 
 * @date 2020-05-20 17:36:24
 */
@Api(value = "风险定义信息",tags = {"风险定义信息"})
@RestController
@RequestMapping("scyf/risk")
public class RiskController extends BaseController {
    @Autowired
    private RiskService RiskService;
    @Autowired
    private InfoService infoService;
    @Autowired
    private RiskCtrlService riskCtrlService;
    /**
     * 查询风险定义表数据
     * yfh
     * 2020/05/27
     * @param params
     * @return
     */
    @ApiOperation(value = "查询风险定义表数据列表", notes="查询风险定义表数据列表")
    @ApiImplicitParams({
      @ApiImplicitParam(paramType = "query",name = "pageNum",dataType = "Long",required = true,value = "当前页码",defaultValue = "1"),
      @ApiImplicitParam(paramType = "query",name = "pageSize",dataType = "Long",required = true,value = "每页显示记录数",defaultValue = "10"),
      @ApiImplicitParam(name = "riskDangerType", value = "危险源类型", paramType = "query", required = true, dataType = "int"),
      @ApiImplicitParam(name = "dangerSrcId", value = "根节点", paramType = "query", required = false, dataType = "int"),
      @ApiImplicitParam(name = "riskCtrlLevelId", value = "管控层级", paramType = "query", required = false, dataType = "int"),
      @ApiImplicitParam(name = "riskCtrlPositionId", value = "管控岗位", paramType = "query", required = false, dataType = "int"),
      @ApiImplicitParam(name = "keyword", value = "风险名称", paramType = "query", required = false, dataType = "string"),
      @ApiImplicitParam(name = "riskLevel", value = "风险等级", paramType = "query", required = false, dataType = "string"),
      @ApiImplicitParam(name = "vtype", paramType = "query", required = false, dataType = "string"),
    })
    @RequestMapping(value = "/list",method = RequestMethod.GET)
  //  @RequiresPermissions("scyf:risk:list")
    public R list(@RequestParam(required = false) Map<String, Object> params){
        if (params.containsKey("dangerSrcId") && !String.valueOf(params.get("dangerSrcId")).equals("")) {
            params.put("vtype", "pd");
            params.put("vDangerSrcId", params.get("dangerSrcId"));
            params.remove("dangerSrcId");
        }
        params.put("companyId",getCompanyId());
        PageUtils page = RiskService.queryPage(params);
        return R.ok().put("data", page);
    }


    /**
     * 信息
     */
    @RequestMapping(value="/info/{riskId}",method = RequestMethod.GET)
    @RequiresPermissions("scyf:risk:info")
    public R info(@PathVariable("riskId") Long riskId){
        Risk Risk = RiskService.getById(riskId);

        return R.ok().put("Risk", Risk);
    }

    /**
     * 保存
     */
    @PostMapping("/save")
    @RequiresPermissions("scyf:risk:save")
    public R save(@RequestBody Risk Risk){

        ValidatorUtils.validateEntity(Risk);
        RiskService.save(Risk);

        return R.ok();
    }

    /**
     * 修改
     */
    @PostMapping("/update")
    @RequiresPermissions("scyf:risk:update")
    public R update(@RequestBody Risk Risk){
        ValidatorUtils.validateEntity(Risk);
        RiskService.update(Risk);

        return R.ok();
    }

    /**
     * 删除风险
     * yfh
     * 2020/06/08
     * @param riskId
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "删除风险", notes="删除风险")
    @RequestMapping(value = {"/delete"},method = {RequestMethod.DELETE})
    public R delete(Integer riskId) throws Exception {

        if (riskId<=0) {
            return R.error( "风险编号不能为空");
        } else {
            try {
                RiskService.deleteRisk(riskId,getCompanyId());
            } catch (Exception var4) {
                var4.printStackTrace();
                throw new Exception();
            }

            return R.ok("删除成功");
        }
    }

    /**
     * 查询设备设施库风险库管控层级 管控岗位信息
     * yfh
     *2020/05/29
     * @return
     */
    @ApiOperation(value = "查询设备设施库风险库管控层级 管控岗位信息", notes="查询设备设施库风险库 levelLists: 管控层级 positionLists:管控岗位信息")
    @RequestMapping(value = "/selectLevelOrPosition",method = RequestMethod.GET)
    public  R selectLevelOrPosition(){
        Map map=new HashMap();
        List<Map<String,Object>> levelLists=RiskService.selectLevel(getCompanyId());
        List<Map<String,Object>> positionLists=RiskService.selectPosition(getCompanyId());
        map.put("levelLists",levelLists);
        map.put("positionLists",positionLists);
        return R.ok().put("data", map);
    }

    /**
     * 查看当前登录公司风险等级
     * yfh
     * 2020/05/29
     * @return
     */
    @ApiOperation(value = "查看当前登录公司风险等级", notes="查看当前登录公司风险等级")
    @RequestMapping(value = "/selectComEvaluate",method = RequestMethod.GET)
    //@ApiImplicitParam(name = "evaluateIndex", value = "风险评价方法，1：LEC，2：LS", paramType = "query", required = true, dataType = "int")
    public R selectComEvaluate(Integer evaluateIndex){
        Map map=new HashMap();
        Map map1=new HashMap();
        List<Map<String,Object>> comEvaluates=RiskService.selectComEvaluate("LEC",getCompanyId());
        List L=new ArrayList();
        List E=new ArrayList();
        List C=new ArrayList();
        if(comEvaluates.size()>0){
            for(int i=0;i<comEvaluates.size();i++){
                if(comEvaluates.get(i).get("evaluateIndexType")!=null&&comEvaluates.get(i).get("evaluateIndexType").equals("L")){
                    L.add(comEvaluates.get(i));
                }
                if(comEvaluates.get(i).get("evaluateIndexType")!=null&&comEvaluates.get(i).get("evaluateIndexType").equals("E")){
                    E.add(comEvaluates.get(i));
                }
                if(comEvaluates.get(i).get("evaluateIndexType")!=null&&comEvaluates.get(i).get("evaluateIndexType").equals("C")){
                    C.add(comEvaluates.get(i));
                }
            }
            map1.put("L",L);
            map1.put("E",E);
            map1.put("C",C);
        }
        Map map2=new HashMap();
        List<Map<String,Object>> comEvaluates1=RiskService.selectComEvaluate("LS",getCompanyId());
        List L1=new ArrayList();
        List S=new ArrayList();
        if(comEvaluates1.size()>0){
            for(int i=0;i<comEvaluates1.size();i++){
                if(comEvaluates1.get(i).get("evaluateIndexType")!=null&&comEvaluates1.get(i).get("evaluateIndexType").equals("L")){
                    L1.add(comEvaluates1.get(i));
                }
                if(comEvaluates1.get(i).get("evaluateIndexType")!=null&&comEvaluates1.get(i).get("evaluateIndexType").equals("S")){
                    S.add(comEvaluates1.get(i));
                }
            }
            map2.put("L",L1);
            map2.put("S",S);
        }
        Map map3=new HashMap();
        List<Map<String,Object>> comEvaluates2=RiskService.selectComEvaluate("LC",getCompanyId());
        List L2=new ArrayList();
        List C1=new ArrayList();
        if(comEvaluates2.size()>0){
            for(int i=0;i<comEvaluates2.size();i++){
                if(comEvaluates2.get(i).get("evaluateIndexType")!=null&&comEvaluates2.get(i).get("evaluateIndexType").equals("L")){
                    L2.add(comEvaluates2.get(i));
                }
                if(comEvaluates2.get(i).get("evaluateIndexType")!=null&&comEvaluates2.get(i).get("evaluateIndexType").equals("C")){
                    C1.add(comEvaluates2.get(i));
                }
            }
            map3.put("L",L2);
            map3.put("C",C1);
        }
        if(map1.size()>0){
            map.put("LEC",map1);
        }
        if(map2.size()>0){
            map.put("LS",map2);
        }
        if(map3.size()>0){
            map.put("LC",map3);
        }
        return R.ok().put("data", map);
    }

    /**
     * 查看当前高速A评价方式风险等级
     * yfh
     * 2020/09/17
     * @return
     */
    @ApiOperation(value = "查看当前高速A评价方式风险等级", notes="查看当前高速A评价方式风险等级")
    @RequestMapping(value = "/selectAEvaluate",method = RequestMethod.GET)
    public R selectAEvaluate(){
        Map map=new HashMap();
        List<Map<String,Object>> comEvaluates1=RiskService.selectAEvaluate("A1",getCompanyId());
        List<Map<String,Object>> comEvaluates2=RiskService.selectAEvaluate("A2",getCompanyId());
        List<Map<String,Object>> comEvaluates3=RiskService.selectAEvaluate("A3",getCompanyId());
        List<Map<String,Object>> comEvaluates4=RiskService.selectAEvaluate("A4",getCompanyId());
        List<Map<String,Object>> comEvaluates5=RiskService.selectAEvaluate("A5",getCompanyId());
        List<Map<String,Object>> comEvaluates6=RiskService.selectAEvaluate("A6",getCompanyId());
        List<Map<String,Object>> comEvaluates7=RiskService.selectAEvaluate("A7",getCompanyId());
        List<Map<String,Object>> comEvaluates8=RiskService.selectAEvaluate("A8",getCompanyId());
        map.put("A1",comEvaluates1);
        map.put("A2",comEvaluates2);
        map.put("A3",comEvaluates3);
        map.put("A4",comEvaluates4);
        map.put("A5",comEvaluates5);
        map.put("A6",comEvaluates6);
        map.put("A7",comEvaluates7);
        map.put("A8",comEvaluates8);
        return R.ok().put("data", map);
    }



    /**
     *查看风险管控层级
     * yfh
     * 2020/05/29
     * @return
     */
    @ApiOperation(value = "查看风险管控层级", notes="查看风险管控层级")
    @ApiImplicitParam(name = "riskCtrlLevelValue", value = "风险管控层级值", paramType = "query", required = false, dataType = "int")
    @RequestMapping(value = "riskPositionLevel",method = RequestMethod.GET)
    public R riskPositionLevel(Integer riskCtrlLevelValue){
        List<Map<String, Object>> riskCtrlLevel=RiskService.riskPositionLevel(riskCtrlLevelValue,getCompanyId());
        return R.ok().put("data", riskCtrlLevel);
    }

    /**
     * 编辑数据及回显风险库 工程 管理 教育 应急 个体防护信息
     * yfh
     * 2020/06/02
     * @param riskId
     * @return
     */
    @ApiOperation(value = "编辑数据回显及风险库 工程 管理 教育 应急 个体防护信息", notes="编辑数据回显及回显风险库 工程 管理 教育 应急 个体防护信息")
    @ApiImplicitParam(name = "riskId", value = "风险ID", paramType = "query", required = false, dataType = "int")
    @RequestMapping(value="/info",method = RequestMethod.GET)
    public R getById(Long riskId){
            Map<String, Object> map =RiskService.getRiskJson(riskId, getCompanyId());
            Info info = infoService.getByCId(getCompanyId(),getUser().getUserType());
            map.put("riskJudgeMethod", info.getRiskJudgeMethod());
            return R.ok().put("data",map);
    }


    /**
     *修改风险管控岗位
     * yfh
     * 2020/06/12
     * @param map
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "修改风险管控岗位", notes="修改风险管控岗位")
    @ApiImplicitParams({
     @ApiImplicitParam(paramType = "query",name = "riskId",dataType = "Long",required = true,value = "风险ID"),
     @ApiImplicitParam(paramType = "query",name = "riskCtrlPositionId",dataType = "Long",required = true,value = "管控岗位ID"),
     @ApiImplicitParam(paramType = "query",name = "riskCtrlLevelId",dataType = "Long",required = true,value = "管控层级id")
    })
    @RequestMapping(value = {"/updateRiskPosition"},method = {RequestMethod.POST})
    public R updateRiskPosition(@RequestParam(required = false) Map<String, Object> map) throws Exception {
        try {
            map.put("companyId",getCompanyId());
            JSONObject json = new JSONObject(map);
            riskCtrlService.updateEntRiskCtrl(json);
            return R.ok("操作成功");
        } catch (Exception var3) {
            var3.printStackTrace();
            throw new Exception();
        }
    }
}
