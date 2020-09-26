package com.hngf.web.controller.risk;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.hngf.common.utils.Constant;
import com.hngf.common.utils.PageUtils;
import com.hngf.common.utils.R;
import com.hngf.common.validator.ValidatorUtils;
import com.hngf.entity.risk.MapInfo;
import com.hngf.service.risk.MapInfoService;
import com.hngf.web.controller.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 企业地图信息表
 *
 * @author yfh
 * @email 
 * @date 2020-05-26 16:06:37
 */
@Api(value = "企业地图信息管理",tags = {"企业地图信息管理"})
@RestController
@RequestMapping("scyf/mapinfo")
public class MapInfoController extends BaseController{
    @Autowired
    private MapInfoService mapInfoService;

    /**
     * 查询企业地图信息
     * yfh
     * 2020/05/26
     * @param params
     * @return
     */
    @ApiOperation(value = "查询企业地图信息", notes="获取企业地图信息数据 path:访问图片前缀",response = MapInfo.class)
    @ApiImplicitParam(name = "keyword", value = "地图名称", paramType = "query", required = false, dataType = "String")
    @RequestMapping(value = "/list",method = RequestMethod.GET)
    @RequiresPermissions("scyf:mapinfo:list")
    public R list(@RequestParam(required = false) Map<String, Object> params, HttpServletRequest req){
        Map map=new HashMap();
        int pageNum = null!= params.get(Constant.PAGE_PAGENUM) ? Integer.parseInt(params.get(Constant.PAGE_PAGENUM).toString()) : 1;
        int pageSize = null != params.get(Constant.PAGE_PAGESIZE) ? Integer.parseInt(params.get(Constant.PAGE_PAGESIZE).toString()) : Constant.PAGE_SIZE;
        if(null == params.get("groupId") || StringUtils.isEmpty(params.get("groupId").toString()) || StringUtils.isBlank(params.get("groupId").toString())){
            params.put("groupId",getGroupId());
        }
        params.put("companyId",getCompanyId());
        PageUtils page = mapInfoService.queryPage(params,pageNum,pageSize,null);
        map.put("path",req.getScheme() + "://" + req.getServerName() + ":" + req.getServerPort() + "/source");
        map.put("page",page);
        return R.ok().put("data", map);
    }


    /**
     * 根据mapId反显 企业地图信息
     * yfh
     * 2020/05/26
     * @param mapId
     * @return
     */
    @ApiOperation(value = "根据mapId查询企业地图信息", notes="根据mapId获取企业地图信息数据",response = MapInfo.class)
    @RequestMapping(value="/info",method = RequestMethod.GET)
    @RequiresPermissions("scyf:mapinfo:info")
    public R info(Long mapId){
        MapInfo mapInfo = mapInfoService.getById(mapId);

        return R.ok().put("data", mapInfo);
    }

    /**
     * 保存企业地图信息
     * yfh
     * 2020/05/26
     * @param map
     * @return
     */
    @ApiOperation(value = "保存企业地图信息", notes="保存企业地图信息数据")
    @RequestMapping(value = "/save",method = RequestMethod.POST)
    @RequiresPermissions("scyf:mapinfo:save")
    public R save(@RequestParam(required = false) Map<String, Object> map){
        JSONObject paramsJson = new JSONObject(map);
        MapInfo mapInfo = (MapInfo) JSON.toJavaObject(paramsJson, MapInfo.class);
        ValidatorUtils.validateEntity(mapInfo);
        mapInfo.setDelFlag(0);
        mapInfo.setCreatedTime(new Date());
        mapInfo.setCreatedBy(getUserId());
        mapInfo.setUpdatedTime(new Date());
        mapInfo.setUpdatedBy(getUserId());
        mapInfo.setCompanyId(getCompanyId());
        mapInfo.setGroupId(getGroupId());
        mapInfoService.save(mapInfo);

        return R.ok();
    }

    /**
     * 修改企业地图信息
     * yfh
     * 2020/05/26
     * @param map
     * @return
     */
    @ApiOperation(value = "修改企业地图信息", notes="修改企业地图信息数据")
    @PostMapping("/update")
    @RequiresPermissions("scyf:mapinfo:update")
    public R update(@RequestParam(required = false) Map<String, Object> map){
        JSONObject paramsJson = new JSONObject(map);
        MapInfo mapInfo = (MapInfo) JSON.toJavaObject(paramsJson, MapInfo.class);
        ValidatorUtils.validateEntity(mapInfo);
        mapInfo.setUpdatedTime(new Date());
        mapInfo.setUpdatedBy(getUserId());
        mapInfoService.update(mapInfo);
        
        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping(value="/deletes",method = RequestMethod.DELETE)
    @RequiresPermissions("scyf:mapinfo:delete")
    public R deletes(@RequestBody Long[] mapIds){
        mapInfoService.removeByIds(Arrays.asList(mapIds));

        return R.ok();
    }

    /**
     * 删除企业地图信息
     * yfh
     * 2020/05/26
     * @param mapId
     * @return
     */
    @ApiOperation(value = "删除企业地图信息", notes="删除企业地图信息数据")
    @RequestMapping(value="/delete",method = RequestMethod.DELETE)
    @RequiresPermissions("scyf:mapinfo:delete")
    public R delete(Long mapId){
        mapInfoService.removeById(mapId);

        return R.ok();
    }

    /**
     * 查看地图类型
     * yfh
     * 2020/06/29
     * @return
     */
    @ApiOperation(value = "查看地图类型", notes="查看地图类型")
    @RequestMapping(value="getMapType",method = RequestMethod.GET)
    public R getMapType(){
        String dictType="map_type";
        List<Map<String,Object>> mapTypes= mapInfoService.getMapType(dictType);
        return R.ok().put("data", mapTypes);
    }
    /**
     * 查看地图使用类型
     * yfh
     * 2020/06/29
     * @return
     */
    @ApiOperation(value = "查看地图使用类型", notes="查看地图使用类型")
    @RequestMapping(value="getMapUseType",method = RequestMethod.GET)
    public R getMapUseType(){
        String dictType="map_use_type";
        List<Map<String,Object>> mapUseTypes= mapInfoService.getMapType(dictType);
        return R.ok().put("data", mapUseTypes);
    }
    @ApiOperation(value = "背景图列表", notes="根据企业Id和groupId获取背景图列表",response = MapInfo.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "companyId",value = "企业Id", paramType = "query", dataType = "long")  ,
            @ApiImplicitParam(name = "groupId",value = "企业的群组ID", paramType = "query",  dataType = "long")
    })
    @GetMapping("getAllMapList")
    public R getAllMapList(@ApiIgnore @RequestParam(required = false) Map<String, Object> params, HttpServletRequest req){
        if(null == params || null == params.get("companyId") || StringUtils.isBlank("companyId")){
            params.put("companyId",getCompanyId());
        }
        return R.ok().put("data",this.mapInfoService.getAllMapList(params)).put("path",req.getScheme() + "://" + req.getServerName() + ":" + req.getServerPort() + "/source");
    }
}
