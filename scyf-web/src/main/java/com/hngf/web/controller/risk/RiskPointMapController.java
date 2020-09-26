package com.hngf.web.controller.risk;

import com.hngf.common.enums.BigScreenDataTypeEnum;
import com.hngf.common.exception.ScyfException;
import com.hngf.common.utils.Constant;
import com.hngf.common.utils.PageUtils;
import com.hngf.common.utils.R;
import com.hngf.common.utils.RedisKeys;
import com.hngf.common.utils.RedisUtils;
import com.hngf.common.validator.Assert;
import com.hngf.common.validator.ValidatorUtils;
import com.hngf.dto.danger.RiskSourceDto;
import com.hngf.dto.sys.MenuDto;
import com.hngf.entity.risk.RiskPoint;
import com.hngf.entity.risk.RiskPointMap;
import com.hngf.entity.sys.Menu;
import com.hngf.service.risk.RiskPointMapService;
import com.hngf.service.risk.RiskPointService;
import com.hngf.service.risk.RiskSourceService;
import com.hngf.service.sys.MenuService;
import com.hngf.service.sys.UserService;
import com.hngf.service.utils.SendMessageUtil;
import com.hngf.web.common.annotation.RepeatSubmit;
import com.hngf.web.controller.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.Logical;
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

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 风险点地图标记数据
 *
 * @author hngf
 * @email 
 * @date 2020-05-20 17:36:23
 */
@RestController
@RequestMapping("scyf/riskpointmap")
@Api(value="风险点地图标记数据",tags = {"风险点地图标记数据"})
public class RiskPointMapController extends BaseController {
    @Autowired
    private RiskPointService riskPointService;
    @Autowired
    private RiskPointMapService RiskPointMapService;
    @Autowired
    private RiskSourceService riskSourceService;
    @Autowired
    private RedisUtils redisUtils;

    @Autowired
    private UserService UserService;
    @Autowired
    private MenuService menuService;
    /**
     * 列表
     */
    @RequestMapping(value = "/list",method = RequestMethod.GET)
    @RequiresPermissions("scyf:riskpointmap:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = RiskPointMapService.queryPage(params);

        return R.ok().put("page", page);
    }

    /**
     * zhangfei
     * 2022/06/03
     * 保存风险点设备设施/作业活动
     */
    @PostMapping("/saveDangerSourceMap")
    @RequiresPermissions(value = {"scyf:riskpoint:save","scyf:riskpointmap:save","scyf:riskpointdangersource:save"},logical = Logical.OR)
    @ApiOperation(value="保存风险点设备设施/作业活动",httpMethod="POST",
            notes="界面下拉框关联接口：<br/>" +
            "1.查询责任部门列表接口：/sys/common/getGroupLists；<br/>" +
            "2.查询检查表接口：scyf/inspectdef/list;<br/>" +
            "3.查询危险源接口:scyf/risksource/list；<br/>" +
            "4.修改时回显信息接口：scyf/riskpoint/info/{riskPointId}；<br/>" +
            "5.风险点图片上传接口：scyf/risksource/import；<br/>" +
            "6.查询责任人接口(责任部门下拉框onchange时调用，传入groupId)：sys/user/list", produces="application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "riskPointType", value = "风险点类型（1 设备设施 2 作业活动）", required = true, paramType = "json", dataType = "int"),
            @ApiImplicitParam(name = "riskPointId", value = "风险点ID(更新时必填,新增时不填)", required = false, paramType = "json", dataType = "int"),
            @ApiImplicitParam(name = "dutyGroupId", value = "责任部门", required = true, paramType = "json", dataType = "int"),
            @ApiImplicitParam(name = "nfcBind", value = "电子NFC标签:0未绑定；1为绑定", required = false, paramType = "json", dataType = "int"),
            @ApiImplicitParam(name = "riskPointLevel", value = "风险点的等级", required = false, paramType = "json", dataType = "int"),
            @ApiImplicitParam(name = "isGreatDangerSrc", value = "是否重大危险源", required = false, paramType = "json", dataType = "int"),
            @ApiImplicitParam(name = "dangerSrcId", value = "主危险id", required = false, paramType = "json", dataType = "int"),
            @ApiImplicitParam(name = "isOutOfControl", value = "是否失控1失控0受控", required = false, paramType = "json", dataType = "int"),
            @ApiImplicitParam(name = "mapId", value = "地图ID", required = false, paramType = "json", dataType = "int"),
            @ApiImplicitParam(name = "isActive", value = "是否激活：1激活；0未激活", required = false, paramType = "json", dataType = "int"),
            @ApiImplicitParam(name = "activeStartTime", value = "激活开始时间(格式：2020-06-02 14:34:32)", required = false, paramType = "json", dataType = "string"),
            @ApiImplicitParam(name = "activeEndTime", value = "激活结束时间(格式：2020-06-02 14:34:32)", required = false, paramType = "json", dataType = "string"),
            @ApiImplicitParam(name = "isIpCamera", value = "是否有摄像头0无1有", required = false, paramType = "json", dataType = "int"),
            @ApiImplicitParam(name = "ipCameraUrl", value = "IP摄像头地址", required = false, paramType = "json", dataType = "string"),
            @ApiImplicitParam(name = "riskPointName", value = "风险点名称", required = true, paramType = "json", dataType = "string"),
            @ApiImplicitParam(name = "dutyPersons", value = "责任部门负责人(多个之间逗号分隔)", required = false, paramType = "json", dataType = "string"),
            @ApiImplicitParam(name = "qrcodeBind", value = "作业岗位绑定二维码信息", required = false, paramType = "json", dataType = "string"),
            @ApiImplicitParam(name = "riskTagList", value = "标签列表", required = false, paramType = "json", dataType = "string"),
            @ApiImplicitParam(name = "riskPointDesc", value = "描述", required = false, paramType = "json", dataType = "string"),
            @ApiImplicitParam(name = "riskPointImg", value = "图片", required = false, paramType = "json", dataType = "string"),
            @ApiImplicitParam(name = "riskPointPlaces", value = "风险点位置", required = false, paramType = "json", dataType = "string"),
            @ApiImplicitParam(name = "drawIds", value = "drawIds", required = false, paramType = "json", dataType = "string"),
            @ApiImplicitParam(name = "dangerSourceIds", value = "风险点涉及的危险源(多个之间逗号分隔)", required = false, paramType = "json", dataType = "string"),
            @ApiImplicitParam(name = "latlng", value = "坐标", required = false, paramType = "json", dataType = "string"),
            @ApiImplicitParam(name = "checkedTable", value = "风险检查表ID(多个之间逗号间隔)", required = false, paramType = "json", dataType = "string")
    })
    @RepeatSubmit()
    public R saveDangerSourceMap(@RequestBody RiskPoint riskPoint) throws ParseException{

        Assert.isBlank(riskPoint.getRiskPointName(),"风险点名称不能为空");
        Assert.isNull(riskPoint.getDutyGroupId(),"责任部门必选");

        ValidatorUtils.validateEntity(riskPoint);

        Map<String, Object> params = new HashMap<>();
        params.put("companyId", getCompanyId());
        params.put("riskPointId", riskPoint.getRiskPointId());
        params.put("riskPointName", riskPoint.getRiskPointName());
        int count = riskPointService.count(params);
        if (count > 0) {
            throw new ScyfException("风险点名称已经存在");
        }

        riskPoint.setCompanyId(getCompanyId());
        //新增时，设置创建时间
        if (riskPoint.getRiskPointId() == null) {
            riskPoint.setCreatedBy(getUserId());
            riskPoint.setCreatedTime(new Date());
            riskPoint.setDelFlag(0);
        }else{
            riskPoint.setUpdatedBy(getUserId());
            riskPoint.setUpdatedTime(new Date());
            riskPoint.setCreatedBy(getUserId());
        }
        RiskPointMapService.insertOrUpdate(riskPoint);
        //修改风险点信息  删除redis缓存
        redisUtils.delete(RedisKeys.getriskMeasureItemList("*",riskPoint.getRiskPointId().toString(),"*"));
        redisUtils.delete(RedisKeys.getRiskPointKey("*","*","*"));
        //新增风险点消息通过websocket发送到大屏
        SendMessageUtil.sendBigScreen(getUser(), BigScreenDataTypeEnum.riskPointCount.idType);

        return R.ok();
    }

    /**
     * 添加风险点时 查询危险源列表
     * yfh
     * 2020/06/18
     * @param params
     * @return
     */
    @RequestMapping(value="/querySourceList",method = RequestMethod.GET)
    public R querySourceList(@RequestParam(required = false) Map<String, Object> params){
        params.put("companyId",getCompanyId());
        List<RiskSourceDto> sourceList = riskSourceService.findList(params);
        return R.ok().put("data",sourceList);
    }

    /**
     * 信息
     */
    @RequestMapping(value="/info/{mapId}",method = RequestMethod.GET)
    @RequiresPermissions("scyf:riskpointmap:info")
    public R info(@PathVariable("mapId") Long mapId){
        RiskPointMap RiskPointMap = RiskPointMapService.getById(mapId);

        return R.ok().put("RiskPointMap", RiskPointMap);
    }

    /**
     * 保存
     */
    @PostMapping("/save")
    @RequiresPermissions("scyf:riskpointmap:save")
    public R save(@RequestBody RiskPointMap RiskPointMap){

        ValidatorUtils.validateEntity(RiskPointMap);
        RiskPointMapService.save(RiskPointMap);

        return R.ok();
    }

    /**
     * 修改
     */
    @PostMapping("/update")
    @RequiresPermissions("scyf:riskpointmap:update")
    public R update(@RequestBody RiskPointMap RiskPointMap){
        ValidatorUtils.validateEntity(RiskPointMap);
        RiskPointMapService.update(RiskPointMap);
        
        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping(value="/delete",method = RequestMethod.DELETE)
    @RequiresPermissions("scyf:riskpointmap:delete")
    public R delete(@RequestBody Long[] mapIds){
        RiskPointMapService.removeByIds(Arrays.asList(mapIds));

        return R.ok();
    }

    /**
     * 保存修改风险点位置
     * yfh
     * 2020/07/01
     * @param riskPoint
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "保存修改风险点位置", notes="保存修改风险点位置")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "mapId", value = "地图ID", paramType = "json", required = false, dataType = "int"),
            @ApiImplicitParam(name = "drawIds", value = "标注数据", paramType = "json", required = false, dataType = "string"),
            @ApiImplicitParam(name = "riskPointId", value = "风险点ID", paramType = "json", required = false, dataType = "int"),
    })
    @RequestMapping(value = {"/saveDangerSourceMapData"},method = {RequestMethod.POST})
    public R saveDangerSourceMapData(@RequestBody RiskPoint riskPoint) throws Exception {
        try {

            Long mapId = riskPoint.getMapId();
            RiskPointMapService.insertMapData(riskPoint,mapId,getCompanyId());
            return R.ok("操作成功");
        } catch (Exception var3) {
            var3.printStackTrace();
            throw new Exception();
        }
    }

    /**
     * 根据风险点Id 获取该风险点位置
     * yfh
     * 2020/07/01
     * @param riskPointId
     * @return
     */
    @ApiOperation(value = "根据风险点Id 获取该风险点位置", notes="根据风险点Id 获取该风险点位置",response = RiskPointMap.class)
    @ApiImplicitParam(name = "riskPointId", value = "风险点Id", paramType = "query", required = false, dataType = "long")
    @RequestMapping(value="/getPointLocation",method = RequestMethod.GET)
    public R getPointLocation(Long riskPointId){
        RiskPointMap RiskPointMap = RiskPointMapService.getRiskPointId(riskPointId);

        return R.ok().put("data", RiskPointMap);
    }

    /**
     * 可视化编辑 获取地图上风险点
     * yfh
     * 2020/07/01
     * @param map
     * @return
     */
    @ApiOperation(value = "可视化编辑 获取地图上风险点", notes="可视化编辑 获取地图上风险点  " +
            "风险点Id: riskPointId " +
            "风险点名称 ：riskPointName " +
            "风险点等级 ：riskPointLevel " +
            "标注名称 ：mapName " +
            "标注数据：mapData " +
            "标注类型：mapType")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "mapId", value = "地图ID", paramType = "query",  dataType = "long"),
            @ApiImplicitParam(name = "riskPointType", value = "风险点类型", paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "groupId", value = "责任部门Id", paramType = "query", dataType = "long"),
            @ApiImplicitParam(name = "isGreatDangerSrc", value = "是否重大危险源", paramType = "query", dataType = "int")
    })
    @RequestMapping(value="/getMapPoints",method = RequestMethod.GET)
    public R getMapPoints(@RequestParam(required = false) Map<String,Object> map){
        map.put("companyId",getCompanyId());
        List<Map<String,Object>> data = RiskPointMapService.getMapPoints(map);

        return R.ok().put("data", data);
    }


    /**
     * 查询当前登录用户风险管理风险分布图在线编辑 下的 菜单
     */
    @RequestMapping(value="/getMenuList",method = RequestMethod.GET)
    @ApiOperation(value="查询当前登录用户风险管理风险分布图在线编辑 下的 菜单")
    public R getMenuList(String menuType){
        Long userId = getUserId();
        List<Menu> list;
        Map<String, Object> params = new HashMap<>() ;
        menuType = "1,2,3";
        params.put("menuType",menuType);
        //超级管理员可以访问全部菜单
        if(Constant.SUPER_ADMIN == userId) {
            list = menuService.getAll(params);
        }else {
            list = UserService.getAllMenu(userId,menuType,null);
        }
        List<MenuDto> rootMenu = new ArrayList<>();
        if(StringUtils.isNotEmpty(menuType)   ){
            for (Menu root : list) {
                if (null != root && root.getMenuParentId()==0) {
                    MenuDto menu = new MenuDto();
                    menu.setMenuId(root.getMenuId());
                    menu.setMenuText(root.getMenuText());
                    menu.setMenuType(root.getMenuType());
                    menu.setMenuCss(root.getMenuCss());
                    menu.setMenuParentId(root.getMenuParentId());
                    menu.setMenuUrl(root.getMenuUrl());
                    menu.setSortNo(root.getSortNo());
                    menu.setMenuPermissions(root.getMenuPermissions());
                    menu.setMenuDesc(root.getMenuDesc());
                    rootMenu.add(menu);
                }
            }

            //如果顶级菜单有数据，开始查找子节点
            if (null != rootMenu && rootMenu.size() > 0) {
                for(MenuDto root : rootMenu){
                    //子节点递归查找添加  传递父节点
                    List<MenuDto> mlist = getChildren(root.getMenuId(),list);
                    root.setChildren(mlist);
                    if(null!=mlist && mlist.size()>0 && null!=mlist.get(0).getChildren() && mlist.get(0).getChildren().size()>0){
                        root.setMenuUrl(mlist.get(0).getChildren().get(0).getMenuUrl());
                        mlist.get(0).setMenuUrl(mlist.get(0).getChildren().get(0).getMenuUrl());
                    }
                }
            }
        }
        Map map=new HashMap();
        for(int i=0;i<rootMenu.size();i++){
            MenuDto menuDto=rootMenu.get(i);
            if("风险管理".equals(menuDto.getMenuText())){
                List<MenuDto> children = menuDto.getChildren();
                for(int a=0;a<children.size();a++){
                    MenuDto menuDto1 = children.get(a);
                    if("标准数据库".equals(menuDto1.getMenuText())){
                        map.put("children",menuDto1.getChildren());
                        break;
                    }
                }
                break;
            }
        }
        return R.ok().put("data", map);
    }

    //递归获取children节点
    @ApiIgnore()
    private   List<MenuDto>  getChildren(Long pid,List<Menu> list) {
        List<MenuDto> children = new ArrayList<>();
        if (null != pid){
            list.forEach(data -> {
                //若遍历的数据中的父节点id等于参数id
                //则判定当前节点为该参数id节点下的子节点
                if (null != data && data.getMenuParentId().longValue() == pid.longValue()) {
                    //构造添加结果集合
                    MenuDto menu = new MenuDto();
                    menu.setMenuId(data.getMenuId());
                    menu.setMenuText(data.getMenuText());
                    menu.setMenuType(data.getMenuType());
                    menu.setMenuCss(data.getMenuCss());
                    menu.setMenuParentId(data.getMenuParentId());
                    menu.setMenuUrl(data.getMenuUrl());
                    menu.setSortNo(data.getSortNo());
                    menu.setMenuPermissions(data.getMenuPermissions());
                    menu.setMenuDesc(data.getMenuDesc());
                    children.add(menu);
                }
            });
        }
        //如果children不为空，继续递归遍历children下的子节点
        if (children.size() > 0) {
            children.forEach(data -> {
                data.setChildren(getChildren(data.getMenuId(),list));
                if(null!=data.getChildren()&& data.getChildren().size()>0) {
                    data.setMenuUrl(data.getChildren().get(0).getMenuUrl());
                }
            });
        }
        return children;
    }

}
