package com.hngf.web.controller.risk;

import com.alibaba.fastjson.JSONObject;
import com.hngf.common.exception.ScyfException;
import com.hngf.common.utils.Constant;
import com.hngf.common.utils.FileUtils;
import com.hngf.common.utils.PageUtils;
import com.hngf.common.utils.QRCodeUtil;
import com.hngf.common.utils.R;
import com.hngf.common.validator.Assert;
import com.hngf.common.validator.ValidatorUtils;
import com.hngf.entity.risk.RiskPoint;
import com.hngf.entity.risk.RiskPointCheckRecord;
import com.hngf.entity.risk.RiskPointGuideRel;
import com.hngf.entity.risk.RiskSource;
import com.hngf.entity.sys.User;
import com.hngf.service.bigscreen.BigScreenMessageService;
import com.hngf.service.risk.RiskCtrlService;
import com.hngf.service.risk.RiskPointCheckRecordService;
import com.hngf.service.risk.RiskPointControlRecordService;
import com.hngf.service.risk.RiskPointGuideRelService;
import com.hngf.service.risk.RiskPointService;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 风险点索引表
 *
 * @author yfh
 * @email 
 * @date 2020-05-20 17:36:23
 */
@Api(value = "风险点管理",tags = {"风险点管理"})
@RestController
@RequestMapping("scyf/riskpoint")
public class RiskPointController extends BaseController {
    @Autowired
    private RiskPointService riskPointService;
    @Autowired
    private RiskPointCheckRecordService riskPointCheckRecordService;
    @Autowired
    private RiskPointControlRecordService riskPointControlRecordService;
    @Autowired
    private RiskPointGuideRelService riskPointGuideRelService;
    @Autowired
    private RiskCtrlService riskCtrlService;
    @Autowired
    private BigScreenMessageService bigScreenMessageService;
    @Value("${scyf.uploaddir}")
    private String uploadDir;
    @Value("${scyf.accessPath}")
    private String accessPath;
    /**
     * 列表
     * yfh
     * 2020/05/21
     * zhangfei 修改 2020/06/02
     * @param params
     * @return
     */
    @GetMapping(value = "/list")
    @ApiOperation(value = "查询风险点索引表", httpMethod = "GET",
            notes="查询风险点模块列表：包括设备设施和作业活动。<br/>" +
                    "界面关联接口如下：<br/>" +
                    "1.查询责任部门列表接口：/sys/common/getGroupLists；<br/>" +
                    "2.查询检查表接口：scyf/inspectdef/list;<br/>" +
                    "3.查询危险源接口:scyf/risksource/list；<br/>" +
                    "4.修改时回显信息接口：scyf/riskpoint/info/{riskPointId}；<br/>" +
                    "5.风险点图片上传接口：scyf/risksource/import；<br/>" +
                    "6.查询责任人接口(责任部门下拉框onchange时调用，传入groupId)：sys/user/list <br/>"+

                    "7.批量激活/取消激活接口：scyf/riskpoint/isActive <br/>"+
                    "8.检查表查看接口：scyf/riskpoint/info/{riskPointId} <br/>"+
                    "8.1检查表查看接口 - 标签页（风险表）：scyf/riskrecord/getRiskList <br/>"+
                    "8.2检查表查看接口 - 标签页（安全检查表）：scyf/riskrecord/getMeasureList <br/>"+

                    "9.作业指导书按钮接口：<br/>"+
                    "9.1作业指导书按钮接口 - 选择指导书：common/commonpost/selectWorkInstruction <br/>"+
                    "9.2作业指导书按钮接口 - 保存选择的指导书：scyf/riskpoint/saveWorkInstruction <br/>"+
                    "9.3作业指导书按钮接口 - 删除关联作业指导书：scyf/riskpoint/delWorkInstruction <br/>"+
                    "9.4作业指导书按钮接口 - 查询指导书详情：common/commonpost/info/{postId} <br/>"+

                    "10.现场人员按钮接口： <br/>"+
                    "10.1现场人员按钮接口 - 查询已关联的现场人员信息：scyf/riskpointsceneperson/getSelected <br/>"+
                    "10.2现场人员按钮接口 - 查询可选现场人员列表：sys/user/getUserAndStatus <br/>"+
                    "10.3现场人员按钮接口 - 保存或删除已选现场人员： scyf/riskpointsceneperson/saveOrDelete <br/>"+
                    "10.4现场人员按钮接口 - 已选现场人员上下线：scyf/riskpointsceneperson/update <br/>"+

                    "")
    @ApiImplicitParams({
         @ApiImplicitParam(name = "dutyGroupId", value = "责任部门", paramType = "query", required = false, dataType = "int"),
         @ApiImplicitParam(name = "riskPointLevel", value = "风险点的等级", paramType = "query", required = false, dataType = "string"),
         @ApiImplicitParam(name = "riskPointType", value = "风险点类型", paramType = "query", required = true, dataType = "int"),
         @ApiImplicitParam(name = "isOutOfControl", value = "风险状态(1预警0受控)", paramType = "query", required = false, dataType = "int"),
         @ApiImplicitParam(name = "isActive", value = "是否激活", paramType = "query", required = false, dataType = "int"),
         @ApiImplicitParam(name = "keyword", value = "搜索关键字", paramType = "query", required = false, dataType = "int"),
         @ApiImplicitParam(name = "pageNum", value = "第几页", required = true, paramType = "query", dataType = "int", defaultValue = "1"),
         @ApiImplicitParam(name = "pageSize", value = "每页多少条数据", required = true, paramType = "query", dataType = "int", defaultValue = "1"),
    })
    @RequiresPermissions("scyf:riskpoint:list")
    public R list(@RequestParam(required=false) Map<String, Object> params){
        params.put("companyId",getCompanyId());
        int pageNum = null!= params.get(Constant.PAGE_PAGENUM) ? Integer.parseInt(params.get(Constant.PAGE_PAGENUM).toString()) : 1;
        int pageSize = null != params.get(Constant.PAGE_PAGESIZE) ? Integer.parseInt(params.get(Constant.PAGE_PAGESIZE).toString()) : Constant.PAGE_SIZE;
        PageUtils page = riskPointService.queryPage(params,pageNum,pageSize,null);

        return R.ok().put("data", page);
    }


    /**
     * 风险点【（我的管控的）】   后台主页
     * yfh
     * 2020/07/06
     * @param params
     * @return
     */
    @ApiOperation("风险点【我的管控的】")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query",name = "pageNum",dataType = "Long",required = true,value = "当前页码",defaultValue = "1"),
            @ApiImplicitParam(paramType = "query",name = "pageSize",dataType = "Long",required = true,value = "每页显示记录数",defaultValue = "1000")
    })
    @RequestMapping(value = {"/queryRiskPointPage"},method = {RequestMethod.GET})
    public R queryRiskPointPage(@RequestParam(required = false) Map<String, Object> params){
        try {
            params.put("module","1");
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
     * 风险点 查询数量  后台主页
     * yfh
     * 2020/07/06
     * @param params
     * @return
     */
    @ApiOperation("风险点数量【（我的管控的）】")
    @RequestMapping(value = {"/queryRiskPointNum"},method = {RequestMethod.GET})
    public R queryRiskPointNum(@RequestParam(required = false) Map<String, Object> params){
            Map bigMap=new HashMap();
            Long companyId=getCompanyId();
            String[] a=new String[]{"1","2","3","4"};
            List list=new ArrayList();
            Integer num=0;
            for (int i = 0; i < a.length; i++) {
                Map map=new HashMap();
                String s = a[i];
                Integer cc=Integer.valueOf(s);
                String s1 = riskPointService.riskPointLvCount(companyId, cc);
                if(!s1.equals("0")&&s.equals("1")&&StringUtils.isNotEmpty(s1)){
                    num=num+Integer.valueOf(s1);
                    map.put("name","重大");
                    map.put("value",Integer.valueOf(s1));
                    map.put("color","#FF0000");
                    list.add(map);
                }
                if(!s1.equals("0")&&s.equals("2")&&StringUtils.isNotEmpty(s1)){
                    num=num+Integer.valueOf(s1);
                    map.put("name","较大");
                    map.put("value",Integer.valueOf(s1));
                    map.put("color","#FF6100");
                    list.add(map);
                }
                if(!s1.equals("0")&&s.equals("3")&&StringUtils.isNotEmpty(s1)){
                    num=num+Integer.valueOf(s1);
                    map.put("name","一般");
                    map.put("value",Integer.valueOf(s1));
                    map.put("color","#FFFF00");
                    list.add(map);
                }
                if(!s1.equals("0")&&s.equals("4")&&StringUtils.isNotEmpty(s1)){
                    num=num+Integer.valueOf(s1);
                    map.put("name","较低");
                    map.put("value",Integer.valueOf(s1));
                    map.put("color","#0000FF");
                    list.add(map);
                }

            }
            bigMap.put("data",list);
            bigMap.put("num",num);
            return R.ok().put("data",bigMap);
    }



    /**
     * 后台主页
     * @param params
     * @return
     */
    @GetMapping("index/bigScreenMessage")
    @ApiOperation(value="主页信息",notes = "返回值说明：<br>" +
            "<br>" +
            "riskPointCount(风险点统计):all(总数量),lv1(重大),lv2(较大),lv3(一般),lv4(较低) <br>" +
            "<br>" +
            "todayCheck(今日待检查任务)：all(总数量) ,title(标题),groupName(部门),rate(完成率)<br>" +
            "<br>" +
            "riskPointControlRecord(风险预警信息)：updatedTime(更新时间),causeReasonName(产生原因说明),riskPointName(风险点名称),causeRemark(原因描述)," +
            "detailId(明细ID：隐患、任务、传感器（当前业务层ID）)," +
            "detailType(类型：1隐患告警；2任务逾期告警；3传感器告警；)," +
            "companyId(公司ID),riskPointId(风险点ID),hiddenLevel(隐患等级)," +
            "isOutOfControl(是否失控 1失控0受控),isControl(是否失控：0失控；1受控;),riskPointLevel(风险点等级),isCloseUp(是否闭环：1已闭环；0未闭环),causeReason(产生原因编码)<br>" +
            "<br>" +
            "warningRiskPoint(预警风险点)：all(总数量),lv1(重大),lv2(较大),lv3(一般),lv4(较低)<br>" +
            "<br>" +
            "hiddenCount(隐患统计)：all(总数量),lv1(重大),lv2(较大),lv3(一般),lv4(较低),toBeRectified(待整改),rate(整改完成率),finish(已完成),waitForChecking(待验收)<br>" +
            "<br>" +
            "hiddenBulletin(隐患通报)：time(整改期限),personLiable(责任人),title(隐患名称),groupName(责任部门)<br>")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "companyId", value = "公司ID",required = true, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "groupId", value = "群组ID",required = true, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "path", value = "请求路径：" +
                    "riskPointCount(风险点统计)," +
                    "todayCheck(今日检查)" +
                    "riskPointControlRecord(风险点预警信息)" +
                    "warningRiskPoint(预警风险点)" +
                    "hiddenCount(隐患统计)" +
                    "hiddenBulletin(隐患通报)"
                    ,required = true, paramType = "query", dataType = "string")
    })
    public R bigScreenMessage(@RequestParam Map<String, Object> params) {
        this.hasKey(params, new String[]{"companyId", "groupId", "path"});
        Assert.isNull(params.get("companyId"),"companyId不能为空");
        Assert.isNull(params.get("groupId"),"groupId不能为空");
        Assert.isNull(params.get("path"),"path不能为空");
        String path = (String)params.get("path");
        //判断path路径是否在允许值的范围
        if (!path.equals("riskPointCount")
                && !path.equals("todayCheck")
                && !path.equals("riskPointControlRecord")
                && !path.equals("warningRiskPoint")
                && !path.equals("hiddenCount")
                && !path.equals("hiddenBulletin")) {
            throw new ScyfException("path路径不存在，请确认");
        }
        return R.ok().put("data",bigScreenMessageService.bigScreenMessage(params));
    }



    /**
     * 后台主页
     * @param params
     * @return
     */
    @GetMapping("screenMessage")
    @ApiOperation(value="主页信息",notes = "返回值说明：<br>" +
            "<br>" +
            "riskPointCount(风险点统计):all(总数量),lv1(重大),lv2(较大),lv3(一般),lv4(较低) <br>" +
            "<br>" +
            "todayCheck(今日待检查任务)：all(总数量) ,title(标题),groupName(部门),rate(完成率)<br>" +
            "<br>" +
            "riskPointControlRecord(风险预警信息)：updatedTime(更新时间),causeReasonName(产生原因说明),riskPointName(风险点名称),causeRemark(原因描述)," +
            "detailId(明细ID：隐患、任务、传感器（当前业务层ID）)," +
            "detailType(类型：1隐患告警；2任务逾期告警；3传感器告警；)," +
            "companyId(公司ID),riskPointId(风险点ID),hiddenLevel(隐患等级)," +
            "isOutOfControl(是否失控 1失控0受控),isControl(是否失控：0失控；1受控;),riskPointLevel(风险点等级),isCloseUp(是否闭环：1已闭环；0未闭环),causeReason(产生原因编码)<br>" +
            "<br>" +
            "warningRiskPoint(预警风险点)：all(总数量),lv1(重大),lv2(较大),lv3(一般),lv4(较低)<br>" +
            "<br>" +
            "hiddenCount(隐患统计)：all(总数量),lv1(重大),lv2(较大),lv3(一般),lv4(较低),toBeRectified(待整改),rate(整改完成率),finish(已完成),waitForChecking(待验收)<br>" +
            "<br>" +
            "hiddenBulletin(隐患通报)：time(整改期限),personLiable(责任人),title(隐患名称),groupName(责任部门)<br>")
            @ApiImplicitParam(name = "path", value = "请求路径：" +
                    "riskPointCount(风险点统计)," +
                    "todayCheck(今日检查)" +
                    "riskPointControlRecord(风险点预警信息)" +
                    "warningRiskPoint(预警风险点)" +
                    "hiddenCount(隐患统计)" +
                    "hiddenBulletin(隐患通报)"
                    ,required = true, paramType = "query", dataType = "string")
    public R screenMessage(@RequestParam(required = false) Map<String, Object> params) {
        params.put("companyId",getCompanyId());
        params.put("groupId",getGroupId());
        String path = (String)params.get("path");
        //判断path路径是否在允许值的范围
        if (!path.equals("riskPointCount")
                && !path.equals("todayCheck")
                && !path.equals("riskPointControlRecord")
                && !path.equals("warningRiskPoint")
                && !path.equals("hiddenCount")
                && !path.equals("hiddenBulletin")) {
            throw new ScyfException("path路径不存在，请确认");
        }
        String s = bigScreenMessageService.bigScreenMessage(params);
        JSONObject jsonObject1 =JSONObject.parseObject(s);
        return R.ok().put("data",jsonObject1);
    }

    /**
     * 根据部门ID查询责任人
     * @param params
     * @return
     */
    @GetMapping(value = "/queryUser")
    @ApiOperation(value="根据部门ID查询责任人",response = User.class)
            @ApiImplicitParam(name = "groupId", value = "组织ID", required = true, paramType = "query", dataType = "integer")
    public   R queryUser(@RequestParam(required=false) Map<String, Object> params){
        params.put("companyId",getCompanyId());
        List<User> list = riskPointService.queryUser(params);
        return R.ok().put("data", list);
    }

    /**
     *风险分级责任台账
     * yfh
     * 2020/06/19
     * @param map
     * @return
     */
    @GetMapping({"/dutyIndex"})
    @ApiOperation(value="风险分级责任台账")
    @ApiImplicitParam(name = "groupId", value = "组织ID", required = true, paramType = "query", dataType = "integer")
    public R dutyIndex(@RequestParam Map<String, Object> map) {
        map.put("companyId",getCompanyId());
        List<Map<String, Object>> list = riskPointService.findAllListByType(map);
        return R.ok().put("data",list);
    }
    /**
     *风险管控措施台账
     * yfh
     * 2020/06/19
     * @param map
     * @return
     */
    @GetMapping({"/controlIndex"})
    @ApiOperation(value="风险管控措施台账")
    @ApiImplicitParam(name = "groupId", value = "组织ID", required = true, paramType = "query", dataType = "integer")
    public R controlIndex(@RequestParam Map<String, Object> map) {
        map.put("companyId",getCompanyId());
        map.put("controlMeasure", "controlMeasure");
        List<Map<String, Object>> list = riskPointService.findAllListByType(map);
        return R.ok().put("data",list);
    }
    /**
     * 风险点二维码列表
     * @param params
     * @return
     */
    @GetMapping("/qrCode")
    @RequiresPermissions("scyf:riskpoint:list")
    @ApiOperation(value = "风险点二维码列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "dutyGroupId", value = "责任部门", paramType = "query", required = false, dataType = "int"),
            @ApiImplicitParam(name = "riskPointType", value = "风险点类型", paramType = "query", required = false, dataType = "int"),
            @ApiImplicitParam(name = "keyword", value = "搜索关键字", paramType = "query", required = false, dataType = "String"),
            @ApiImplicitParam(name = "pageNum", value = "第几页", required = true, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "pageSize", value = "每页多少条数据", required = true, paramType = "query", dataType = "int"),
    })
    public R qrCode(@RequestParam Map<String, Object> params) {
        params.put("companyId",getCompanyId());
        int pageNum = null!= params.get(Constant.PAGE_PAGENUM) ? Integer.parseInt(params.get(Constant.PAGE_PAGENUM).toString()) : 1;
        int pageSize = null != params.get(Constant.PAGE_PAGESIZE) ? Integer.parseInt(params.get(Constant.PAGE_PAGESIZE).toString()) : Constant.PAGE_SIZE;
        return R.ok().put("data", riskPointService.getRiskPointList(params,pageNum,pageSize,null));
    }

    /**
     * 绑定风险点二维码
     * @param riskPointId
     * @return
     */
    @GetMapping("/bindQrCode")
    @RequiresPermissions("scyf:riskpoint:list")
    @ApiOperation(value = "绑定风险点二维码")
    @ApiImplicitParam(name = "riskPointId", value = "风险点ID", paramType = "form", required = false, dataType = "int")
    public R bindQrCode(Long riskPointId) {
        Assert.isNull(riskPointId,"风险点ID不能为空");
        RiskPoint riskPoint = new RiskPoint();
        riskPoint.setRiskPointId(riskPointId);
        riskPoint.setQrcodeBind(riskPointId.toString());
        riskPoint.setUpdatedBy(getUserId());
        riskPoint.setUpdatedTime(new Date());
        riskPointService.update(riskPoint);
        return R.ok();
    }

    /**
     * 二维码批量下载及绑定
     * @return
     */
    @GetMapping("/generateQrCodeForFacility")
    @ApiOperation(value = "二维码批量下载及绑定")
    public void generateQrCodeForFacility(HttpServletResponse response) {

        Map<String, Object> params = new HashMap<>();
        params.put("companyId",getCompanyId());
        //查询风险点列表
        List<RiskPoint> list = riskPointService.getRiskPointList(params);

        //组装文件保存路径
        String filePath = this.uploadDir.concat(getLoginName());
        if (!list.isEmpty()) {
            list.forEach(rp ->{
                String content = rp.getQrcodeBind();
                Long riskPointId = rp.getRiskPointId();
                //如果风险点二维码未绑定，执行绑定操作
                if (StringUtils.isEmpty(content) || content.equals("ROOT")) {
                    content = riskPointId.toString();
                    RiskPoint riskPoint = new RiskPoint();
                    riskPoint.setRiskPointId(riskPointId);
                    riskPoint.setQrcodeBind(content);
                    riskPoint.setUpdatedBy(getUserId());
                    this.riskPointService.update(riskPoint);
                }

                Integer riskPointType = rp.getRiskPointType();//风险点类型
                String riskPointName = rp.getRiskPointName();//风险点名称

                try {
                    QRCodeUtil.encode(content, "", filePath.concat(riskPointType == 2 ? "/作业" : "/设备"), false, riskPointName, riskPointName);
                } catch (Exception e) {
                    throw new ScyfException("生成二维码失败!");
                }
            });

            File file = FileUtils.zipFiles(filePath, this.uploadDir.concat(String.valueOf(System.currentTimeMillis())), "二维码");
            try {
                FileUtils.getOutputStream(response, file);
            } catch (Exception e) {
                throw new ScyfException("二维码批量下载失败!");
            }finally {
                FileUtils.delFolder(filePath);
                FileUtils.delFolder(file.getParent());
            }
        }
    }


    /**
     * 风险点检查记录
     * @param params
     * @return
     */
    @GetMapping(value = "/inspectRecord")
    @RequiresPermissions("scyf:riskpoint:list")
    @ApiOperation(value = "风险点检查记录",response = RiskPointCheckRecord.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "dutyGroupId", value = "责任部门", paramType = "query", required = false, dataType = "int"),
            @ApiImplicitParam(name = "riskPointLevel", value = "风险点的等级", paramType = "query", required = false, dataType = "String"),
            @ApiImplicitParam(name = "riskPointType", value = "风险点类型", paramType = "query", required = false, dataType = "int"),
            @ApiImplicitParam(name = "isOutOfControl", value = "风险状态(1预警0受控)", paramType = "query", required = false, dataType = "int"),
            @ApiImplicitParam(name = "isActive", value = "是否激活", paramType = "query", required = false, dataType = "int"),
            @ApiImplicitParam(name = "keyword", value = "搜索关键字", paramType = "query", required = false, dataType = "String"),
            @ApiImplicitParam(name = "pageNum", value = "第几页", required = true, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "pageSize", value = "每页多少条数据", required = true, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "result", value = "检查结果", required = false, paramType = "query", dataType = "int")
    })
    public R inspectRecord(@RequestParam(required=false) Map<String, Object> params){
        int pageNum = null!= params.get(Constant.PAGE_PAGENUM) ? Integer.parseInt(params.get(Constant.PAGE_PAGENUM).toString()) : 1;
        int pageSize = null != params.get(Constant.PAGE_PAGESIZE) ? Integer.parseInt(params.get(Constant.PAGE_PAGESIZE).toString()) : Constant.PAGE_SIZE;
        params.put("companyId", getCompanyId());
        PageUtils page = riskPointCheckRecordService.getListPage(params,pageNum,pageSize,null);
        return R.ok().put("data", page);
    }

    /**
     * 保存风险点关联作业指导书
     * @param riskPointId 风险点ID
     * @param ids 作业指导书ID
     * @return
     */
    @PostMapping("/saveWorkInstruction")
    @RequiresPermissions(value = {"scyf:riskpoint:list","common:commonpost:list","common:commonpost:save"},logical = Logical.OR)
    @ApiOperation(value = "保存风险点关联作业指导书")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "riskPointId", value = "风险点ID", paramType = "form", required = true, dataType = "int"),
            @ApiImplicitParam(name = "ids", value = "作业指导书ID", paramType = "form", allowMultiple=true,required = true, dataType = "int")
    })
    public R saveWorkInstruction(Long riskPointId, Long[] ids) {
        Assert.isNull(riskPointId,"风险点ID不能为空");
        if (null == ids || ids.length < 1) {
            return R.error("作业指导书ID不能为空");
        }

        List<RiskPointGuideRel> list = new ArrayList<>();
        for (int i = 0; i < ids.length; i++) {
            RiskPointGuideRel rpg = new RiskPointGuideRel();
            rpg.setCompanyId(getCompanyId());
            rpg.setRiskPointId(riskPointId);
            rpg.setEntPostId(ids[i]);
            list.add(rpg);
        }
        int res = riskPointGuideRelService.saveBatch(list);

        return res > 0 ? R.ok() : R.error("保存失败");
    }

    /**
     * 删除风险点关联作业指导书
     * @param riskPointId 风险点ID
     * @param postId 作业指导书ID
     * @return
     */
    @RequestMapping(value="/delWorkInstruction",method = RequestMethod.DELETE)
    @RequiresPermissions(value = {"scyf:riskpoint:list","common:commonpost:list","common:commonpost:delete"},logical = Logical.OR)
    @ApiOperation(value = "删除风险点关联作业指导书")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "riskPointId", value = "风险点ID", paramType = "form", required = true, dataType = "int"),
            @ApiImplicitParam(name = "postId", value = "作业指导书ID", paramType = "form", required = true, dataType = "int")
    })
    public R delWorkInstruction(Long riskPointId, Long postId) {
        Assert.isNull(riskPointId,"风险点ID不能为空");
        Assert.isNull(postId,"作业指导书ID不能为空");

        int res = riskPointGuideRelService.removeByPointIdAndPostId(riskPointId,postId,getCompanyId());

        return res > 0 ? R.ok() : R.error("删除失败");
    }

    /**
     * 预警产生原因
     * yfh
     * 2020/06/23
     * @return
     */
    @GetMapping("/causeReasons")
    @ApiOperation(value = "预警产生原因",notes="产生原因: causeReason  " +
            "原因编码：causeValue")
    public R causeReasons(){
        String type="cause_reason_type";
        return R.ok().put("data",riskPointService.causeReasons(type));
    }
    /**
     * 实时预警记录
     * zhangfei
     * 20200608
     * @return
     */
    @GetMapping("/controlRecord")
    @ApiOperation(value = "实时预警记录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "isControl", value = "是否失控：0失控；1受控", paramType = "query", required = false, dataType = "int"),
            @ApiImplicitParam(name = "riskPointLevel", value = "风险点的等级", paramType = "query", required = false, dataType = "int"),
            @ApiImplicitParam(name = "causeReason", value = "产生原因", paramType = "query", required = false, dataType = "String"),
            @ApiImplicitParam(name = "startDate", value = "开始日期", paramType = "query", required = false, dataType = "String"),
            @ApiImplicitParam(name = "endDate", value = "结束日期", paramType = "query", required = false, dataType = "String"),
            @ApiImplicitParam(name = "keyword", value = "搜索关键字", paramType = "query", required = false, dataType = "String"),
            @ApiImplicitParam(name = "pageNum", value = "第几页", required = true, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "pageSize", value = "每页多少条数据", required = true, paramType = "query", dataType = "int"),
    })
    public R controlRecord(@RequestParam Map<String,Object> params){
        int pageNum = null!= params.get(Constant.PAGE_PAGENUM) ? Integer.parseInt(params.get(Constant.PAGE_PAGENUM).toString()) : 1;
        int pageSize = null != params.get(Constant.PAGE_PAGESIZE) ? Integer.parseInt(params.get(Constant.PAGE_PAGESIZE).toString()) : Constant.PAGE_SIZE;
        params.put("companyId", getCompanyId());
        return R.ok().put("data", riskPointControlRecordService.getControlRecord(params,pageNum,pageSize,null));
    }

    /**
     * 历史预警记录
     * zhangfei
     * 20200608
     * @return
     */
    @GetMapping("/historyControlRecord")
    @ApiOperation(value = "历史预警记录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "isControl", value = "是否失控：0失控；1受控", paramType = "query", required = false, dataType = "int"),
            @ApiImplicitParam(name = "riskPointLevel", value = "风险点的等级", paramType = "query", required = false, dataType = "int"),
            @ApiImplicitParam(name = "causeReason", value = "产生原因", paramType = "query", required = false, dataType = "String"),
            @ApiImplicitParam(name = "startDate", value = "开始日期", paramType = "query", required = false, dataType = "String"),
            @ApiImplicitParam(name = "endDate", value = "结束日期", paramType = "query", required = false, dataType = "String"),
            @ApiImplicitParam(name = "keyword", value = "搜索关键字", paramType = "query", required = false, dataType = "String"),
            @ApiImplicitParam(name = "pageNum", value = "第几页", required = true, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "pageSize", value = "每页多少条数据", required = true, paramType = "query", dataType = "int"),
    })
    public R historyControlRecord(@RequestParam Map<String,Object> params){
        int pageNum = null!= params.get(Constant.PAGE_PAGENUM) ? Integer.parseInt(params.get(Constant.PAGE_PAGENUM).toString()) : 1;
        int pageSize = null != params.get(Constant.PAGE_PAGESIZE) ? Integer.parseInt(params.get(Constant.PAGE_PAGESIZE).toString()) : Constant.PAGE_SIZE;
        params.put("companyId", getCompanyId());
        return R.ok().put("data", riskPointControlRecordService.getHistoryControlRecord(params,pageNum,pageSize,null));
    }

    /**
     * 回显风险点索引表信息
     * yfh
     * 2020/05/23
     * zhangfei 2020/06/03 修改，返回更多信息
     * @param riskPointId
     * @return
     */
    @ApiOperation(value = "回显风险点索引表信息", notes="回显风险点索引表信息 path访问图片/文件前缀 ",response = RiskPoint.class)
    @RequestMapping(value="/info",method = RequestMethod.GET)
    @RequiresPermissions("scyf:riskpoint:info")
    public R info(Long riskPointId, HttpServletRequest req){
        Map map=new HashMap();
        Assert.isNull(riskPointId,"风险点ID不能为空");
        RiskPoint riskPoint = riskPointService.getById(riskPointId);
        map.put("riskPoint",riskPoint);
        map.put("path",req.getScheme() + "://" + req.getServerName() + ":" + req.getServerPort()+"/source");
        return R.ok().put("data",map );
    }

    /**
     * 查询风险点信息
     * zhangfei 2020/06/17
     * @param riskPointId
     * @return
     */
    @ApiOperation(value = "查询风险点信息",response = RiskPoint.class)
    @RequestMapping(value="/get/{riskPointId}",method = RequestMethod.GET)
    @RequiresPermissions("scyf:riskpoint:info")
    public R get(@PathVariable("riskPointId") Long riskPointId){
        Assert.isNull(riskPointId,"风险点ID不能为空");
        return R.ok().put("data", riskPointService.getRiskPointById(riskPointId));
    }

    /**
     * 保存风险点索引表信息
     * yfh
     * 2020/05/23
     * @param RiskPoint
     * @return
     */
    @ApiOperation(value = "保存风险点索引表信息", notes="保存风险点索引表信息")
    @PostMapping("/save")
    @RequiresPermissions("scyf:riskpoint:save")
    @RepeatSubmit()
    public R save(@RequestBody RiskPoint RiskPoint){

        ValidatorUtils.validateEntity(RiskPoint);
        RiskPoint.setCreatedTime(new Date());
        RiskPoint.setCreatedBy(getUserId());
        riskPointService.save(RiskPoint);

        return R.ok();
    }

    /**
     * 修改风险点索引表信息
     * yfh
     * 2020/05/23
     * @param RiskPoint
     * @return
     */
    @ApiOperation(value = "修改风险点索引表信息", notes="修改风险点索引表信息")
    @PostMapping("/update")
    @RequiresPermissions("scyf:riskpoint:update")
    public R update(@RequestBody RiskPoint RiskPoint){
        ValidatorUtils.validateEntity(RiskPoint);
        RiskPoint.setUpdatedTime(new Date());
        RiskPoint.setUpdatedBy(getUserId());
        riskPointService.update(RiskPoint);
        
        return R.ok();
    }

    /**
     * 批量激活/取消激活
     * zhangfei
     * 2020/06/02
     * @param isActive 是否激活
     * @param riskPointIds 风险点ID数组
     * @return
     */
    @RequestMapping(value="/isActive",method = RequestMethod.POST)
    @RequiresPermissions("scyf:riskpoint:delete")
    @ApiOperation(value = "批量激活/取消激活")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "isActive", value = "是否激活", required = true, paramType = "form", dataType = "integer"),
            @ApiImplicitParam(name = "riskPointIds", value = "风险点ID数组(如果不传此参数，采用全部更新方案)", required = false, paramType = "form", dataType = "integer")
    })
    public R isActive(Integer isActive,Long [] riskPointIds){

        Assert.isNull(isActive,"是否激活未输入");
        riskPointService.updateIsActive(isActive,riskPointIds);
        return R.ok();
    }

    /**
     * 删除风险点索引表信息
     * zhangfei
     * 2020/06/02
     * @param riskPointId
     * @return
     */

    @RequestMapping(value="/deleteById",method = RequestMethod.DELETE)
    @RequiresPermissions("scyf:riskpoint:delete")
    @ApiOperation(value = "删除风险点索引表信息", notes="删除风险点索引表信息")
    @ApiImplicitParam(name = "riskPointId", value = "风险点ID", required = true, paramType = "json", dataType = "integer")
    public R deleteById(Long riskPointId){
        Assert.isNull(riskPointId,"风险点ID是空的");
        riskPointService.removeById(riskPointId,getCompanyId());
        return R.ok();
    }

    /**
     * 批量删除风险点索引表信息
     * yfh
     * 2020/05/23
     * @param riskPointIds
     * @return
     */
    @ApiOperation(value = "批量删除风险点索引表信息", notes="批量删除风险点索引表信息")
    @RequestMapping(value="/delete",method = RequestMethod.DELETE)
    @RequiresPermissions("scyf:riskpoint:delete")
    public R deleteBatch(@RequestBody Long[] riskPointIds){
        riskPointService.removeByIds(Arrays.asList(riskPointIds));

        return R.ok();
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
        List<RiskSource> riskSourceList = null;
        //危险源ID集合
        List<Long> dangerSrcIds = new ArrayList<>();
        if (ctlLevel != null && ctlLevel != 0) {
            params.put("ctlLevel", ctlLevel);
            riskSourceList = this.riskPointService.getRiskPointDangerSrc(params);
        } else {
            riskSourceList = new ArrayList();
        }

        if (!riskSourceList.isEmpty()) {
            for (RiskSource rs : riskSourceList) {
                dangerSrcIds.add(rs.getRiskDangerId());
            }
        }
        return R.ok().put("data",riskSourceList).put("dangerSrcIds",dangerSrcIds);
    }
    /**
    * @Author: zyj
    * @Description:风险，风险点统计柱形图
    * @Param
    * @Date 15:20 2020/6/19
    */
    @GetMapping("/riskPointLvCount")
    @ApiOperation(value="风险，风险点统计柱形图")
    @RequiresPermissions("scyf:riskpoint:list")
    public R riskPointLvCount(){
        Long companyId=getCompanyId();
        String[] a=new String[]{"风险点","1","2","3","4"};
        String[] end=new String[5];
        end[0]="风险点";
        for (int i = 1; i < a.length; i++) {
            String s = a[i];
            Integer cc=Integer.valueOf(s);
            String s1 = riskPointService.riskPointLvCount(companyId, cc);
            end[i]=s1;
        }
        return R.ok().put("data",end);
    }
    /**
    * @Author: zyj
    * @Description:风险，风险点预警记录统计折线图
    * @Param  year 年份
    * @Date 15:45 2020/6/19
    */
    @GetMapping("/controlRecordLine")
    @ApiOperation(value="风险，风险点预警记录统计折线图")
    @RequiresPermissions("scyf:riskpoint:list")
    @ApiImplicitParam(name = "year", value = "年份",required = true, paramType = "query", dataType = "int")
    public R controlRecordBar(Integer year){
        Long companyId=getCompanyId();
        List<Map<String, Object>> statistics = riskPointControlRecordService.getStatistics(companyId, year);
        String[] month=new String[]{"01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12"};
        String[] months=new String[]{"0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0"};
        if (statistics.size()>0) {
            for (int i = 0; i < month.length; i++) {
                String s = month[i];
                for (Map<String, Object> map : statistics) {
                    if (s.equals(map.get("month").toString())) {
                        months[i] = map.get("value").toString();
                    }
                }
            }
        }
        return R.ok().put("data",months);
    }
    /**
    * @Author: zyj
    * @Description:风险，风险点预警记录统计折线图 年份下拉框
    * @Param
    * @Date 15:34 2020/6/23
    */
    @GetMapping("/getStatisticsYear")
    @ApiOperation(value="风险，风险点预警记录统计折线图 年份下拉框")
    @RequiresPermissions("scyf:riskpoint:list")
    public R getStatisticsYear(){
        Long companyId=getCompanyId();
        List<Map<String, Object>> statisticsYear = riskPointControlRecordService.getStatisticsYear(companyId);
        if (statisticsYear==null){
            return R.error("没有数据");
        }else {
            for (Map<String, Object> map : statisticsYear) {
                String name = map.get("name").toString() + "年";
                map.put("name",name);
            }
        }
        return R.ok().put("data",statisticsYear);
    }


    /**
     * 风险分布图 风险点数量总计
     * yfh
     * 2020/06/30
     * @return
     */
    @GetMapping("/riskPointCount")
    @ApiOperation(value="风险分布图 风险点数量总计")
    public R riskPointCount(Integer groupId){
      String riskPointCount=riskPointService.riskPointCount(groupId,getCompanyId());
        return R.ok().put("data",riskPointCount);
    }
}
