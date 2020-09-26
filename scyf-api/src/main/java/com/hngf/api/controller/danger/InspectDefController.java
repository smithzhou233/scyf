package com.hngf.api.controller.danger;

import com.hngf.api.controller.BaseController;
import com.hngf.common.utils.ParamUtils;
import com.hngf.common.utils.R;
import com.hngf.common.validator.Assert;
import com.hngf.service.danger.InspectDefService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/scyf/inspectdef")
@Api(value = "检查定义管理",tags = "检查定义管理")
public class InspectDefController  extends BaseController{
    @Autowired
    private com.hngf.service.danger.InspectDefService InspectDefService;

    /**
     * 查询检查项定义树状列表
     */
    @GetMapping(value="/getTreeItems")
    @ApiOperation(value="查询检查项定义树状列表")
    @ApiImplicitParam(name = "inspectDefId", value = "检查表ID", required = true, paramType = "query", dataType = "int")
    public R getTreeItems(Long inspectDefId){
        Assert.isNull(inspectDefId,"检查表ID不能为空");
        Map<String, Object> params = new HashMap<>();
        params.put("inspectDefId", inspectDefId);
        return R.ok().put("data", InspectDefService.getTreeItems(params));
    }

    /**
     * 【风险点下的检查表】列表
     */
    @GetMapping(value="/getInspectDefTables")
    @ApiOperation(value="【风险点下的检查表】列表")
    @ApiImplicitParam(name = "riskPointId", value = "风险点ID", required = true, paramType = "query", dataType = "int")
    public R getInspectDefTables(@RequestParam Map<String, Object> params){
        Assert.isNull(ParamUtils.paramsToLong(params,"riskPointId"),"风险点ID不能为空");
        params.put("companyId", getCompanyId());
        params.put("userId", getUserId());

        String riskPointId = ParamUtils.paramsToString(params,"riskPointId");
        try {
            List bizChecks;
            if (StringUtils.isNotEmpty(riskPointId) && !riskPointId.equals("-1")) {
                bizChecks = this.InspectDefService.getRiskPointCheckTables(params);
                if (bizChecks.isEmpty()) {
                    bizChecks = this.InspectDefService.getCheckTables(params);
                }
            } else {
                bizChecks = this.InspectDefService.getCheckTables(params);
            }

            if (!bizChecks.isEmpty()) {
                Iterator var4 = bizChecks.iterator();

                while (var4.hasNext()) {
                    Map<String, Object> bizCheck = (Map) var4.next();
                    bizCheck.remove("inspectDefTypeId");
                }
            }
            return R.ok().put("data", bizChecks);
        } catch (Exception e) {
            return R.error("查询失败");
        }

    }
}
