package com.hngf.web.controller.sys;

import com.hngf.common.utils.Constant;
import com.hngf.common.utils.PageUtils;
import com.hngf.common.utils.R;
import com.hngf.common.validator.ValidatorUtils;
import com.hngf.entity.sys.Info;
import com.hngf.service.sys.InfoService;
import com.hngf.web.common.annotation.SysLog;
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

import java.util.Arrays;
import java.util.Map;


/**
 * 系统信息表
 *
 * @author hngf
 * @email 
 * @date 2020-05-22 09:58:36
 */
@Api(value="系统信息表",tags = {"系统信息表"})
@RestController
@RequestMapping("scyf/info")
public class InfoController extends BaseController {
    @Autowired
    private InfoService infoService;

    /**
    * @Author: zyj
    * @Description:查询系统信息表数据
    * @Param params
    * @Date 14:53 2020/5/22
    */
    @ApiOperation(value = "系统信息表", notes="查询系统信息表数据", response = Info.class)
    @RequestMapping(value = "/list",method = RequestMethod.GET)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum", value = "第几页", required = true, paramType = "query", dataType = "int", defaultValue = "1"),
            @ApiImplicitParam(name = "pageSize", value = "每页多少条数据", required = true, paramType = "query", dataType = "int", defaultValue = "10"),
            @ApiImplicitParam(name = "keyword", value = "查询关键字",  paramType = "query", dataType = "string")
    })
    @RequiresPermissions("scyf:info:list")
    public R list(@RequestParam(required = false) Map<String, Object> params){
        int pageNum = null!= params.get(Constant.PAGE_PAGENUM) ? Integer.parseInt(params.get(Constant.PAGE_PAGENUM).toString()) : 1;
        int pageSize = null != params.get(Constant.PAGE_PAGESIZE) ? Integer.parseInt(params.get(Constant.PAGE_PAGESIZE).toString()) : Constant.PAGE_SIZE;
        PageUtils page = infoService.queryPage(params,pageNum,pageSize,null);

        return R.ok().put("data", page);
    }
    /**
     * @Author: zyj
     * @Description:通过企业id查询数据如果没有添加一条数据
     * @Param companyId 企业id
     * @Date 10:37 2020/5/22
     */
    @ApiOperation(value = "系统信息表", notes="通过企业id查询数据,如果没有添加一条数据", response = Info.class)
    @RequestMapping(value = "/getList",method = RequestMethod.GET)
    @RequiresPermissions("scyf:info:list")
    public R getList(){
        //获取企业id  companyId
        Long companyId=getUser().getCompanyId();
        Info info = infoService.getList(companyId,getUser().getUserType());
        return R.ok().put("data", info);
    }

    /**
     * @Author: zyj
     * @Description:通过id查询数据
     * @Param sysId 系统信息表主键id
     * @Date 14:53 2020/5/22
     */
    @ApiOperation(value = "系统信息表", notes="通过id查询数据", response = Info.class)
    @RequestMapping(value="/info/{sysId}",method = RequestMethod.GET)
    @RequiresPermissions("scyf:info:info")
    public R info(@PathVariable("sysId") Long sysId){
        Info info = infoService.getById(sysId);

        return R.ok().put("data", info);
    }

    /**
     * @Author: zyj
     * @Description:保存信息
     * @Param info  系统信息表实体类
     * @Date 14:53 2020/5/22
     */
    @ApiOperation(value = "系统信息表", notes="新增一条系统信息表数据")
    @PostMapping("/save")
    @RequiresPermissions("scyf:info:save")
    @SysLog("保存系统信息")
    public R save(@RequestBody Info info){

        ValidatorUtils.validateEntity(info);
        info.setCompanyId(getCompanyId());
        infoService.save(info);

        return R.ok();
    }

    /**
     * @Author: zyj
     * @Description:修改信息
     * @Param info  系统信息表实体类
     * @Date 14:53 2020/5/22
     */
    @ApiOperation(value = "系统信息表", notes="修改一条数据")
    @PostMapping("/update")
    @RequiresPermissions("scyf:info:update")
    @SysLog("修改系统信息")
    public R update(@RequestBody Info info){
        ValidatorUtils.validateEntity(info);
        infoService.update(info);
        
        return R.ok();
    }
    /**
     * @Author: zyj
     * @Description:删除信息
     * @Param sysId 系统信息表主键id
     * @Date 14:53 2020/5/22
     */
    @ApiOperation(value = "系统信息表", notes="删除一条数据")
    @RequestMapping(value="/delete",method = RequestMethod.DELETE)
    @RequiresPermissions("scyf:info:delete")
    @SysLog("删除系统信息")
     public R delete(Long sysId){
        infoService.removeById(sysId);
        return R.ok();
     }
    /**
     * @Author: zyj
     * @Description:批量删除信息
     * @Date 14:53 2020/5/22
     */
    @RequestMapping(value="/deletes",method = RequestMethod.DELETE)
    @RequiresPermissions("scyf:info:delete")
    @SysLog("删除系统信息")
    public R deletes(@RequestBody Long[] sysIds){
        infoService.removeByIds(Arrays.asList(sysIds));

        return R.ok();
    }
    @RequestMapping({"/getInfo"})
    public R getInfo() {
        try {
            Info sysInfo = this.infoService.getByCId(super.getCompanyGroupId(),super.getUser().getUserType());
            return  R.ok().put( "data", sysInfo);
        } catch (Exception var2) {
            return  R.ok().put( "data", "处理失败");
        }
    }

}
