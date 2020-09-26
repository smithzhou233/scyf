package com.hngf.web.controller.common;

import com.hngf.common.utils.HttpContextUtils;
import com.hngf.common.utils.R;
import com.hngf.common.validator.Assert;
import com.hngf.dto.sys.GroupMemberPositionDto;
import com.hngf.entity.sys.Group;
import com.hngf.entity.sys.User;
import com.hngf.service.danger.InspectDefService;
import com.hngf.service.sys.CommonClassifyService;
import com.hngf.service.sys.CompanySeoService;
import com.hngf.service.sys.DictService;
import com.hngf.service.sys.GroupMemberPositionService;
import com.hngf.service.sys.GroupService;
import com.hngf.service.sys.InfoService;
import com.hngf.service.sys.PositionService;
import com.hngf.service.sys.RoleService;
import com.hngf.service.sys.UserService;
import com.hngf.web.controller.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * 通用请求
 * @author zhangfei
 * @since 2020/5/23 15:54
 */
@RestController
@RequestMapping("/sys/common")
@Api(value = "通用接口",tags = {"通用接口"})
public class CommonController extends BaseController {

    @Autowired
    private GroupService groupService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private PositionService positionService;
    @Autowired
    private com.hngf.service.sys.GroupTypeService GroupTypeService;
    @Autowired
    private GroupMemberPositionService groupMemberPositionService;
    @Autowired
    private CommonClassifyService commonClassifyService;
    @Autowired
    private UserService userService;
    @Autowired
    private DictService dictService;
    @Autowired
    private InspectDefService inspectDefService;
    @Autowired
    private InfoService infoService;
    @Autowired
    private CompanySeoService companySeoService;

    @Value("${scyf.uploaddir}")
    private String uploadDir;
    /**
     * 查询有权限的组织树形菜单
     * @return
     */
    @GetMapping("/getGroupLists")
    @ApiOperation(value="查询有权限的组织树形菜单")
    public R getGroupLists() {
        Map<String, Object> params = new HashMap();
        params.put("companyOrOrgId", getCompanyId());
        //params.put("groupLeft", this.getGroupLeftOrRight(true, false));
        //params.put("groupRight", this.getGroupLeftOrRight(false, true));
        params.put("groupId", getGroupId());
        return R.ok().put("data", this.groupService.getEleuiTreeList(params));
    }

    /**
     * 查询OEM信息
     * @return
     */
    @GetMapping("/getCompanySeo")
    @ApiOperation(value="查询OEM信息")
    public R getCompanySeo() {
        return R.ok().put("data", this.companySeoService.getById(getCompanyId()));
    }

    /**
     * 查询当前公司下的组织树形菜单
     * @return
     */
    @GetMapping("/getCompanyGroupLists")
    @ApiOperation(value="查询当前公司下的组织树形菜单")
    public R getCompanyGroupLists() {
        Map<String, Object> params = new HashMap();
        params.put("companyOrOrgId", getCompanyId());
        //params.put("groupLeft", this.getGroupLeftOrRight(true, false));
        //params.put("groupRight", this.getGroupLeftOrRight(false, true));
        return R.ok().put("data", this.groupService.getEleuiTreeList(params));
    }

    /**
     * 根据组织ID查找用户
     * @return
     */
    @GetMapping("/getUserByGroupId")
    @ApiOperation(value="根据组织ID查找用户")
    @ApiImplicitParam(name = "groupId", value = "组织ID", required = true, paramType = "form", dataType = "int")
    public R getUserByGroupId(Long groupId) {
        Assert.isNull(groupId,"组织ID不能为空");
        Map<String, Object> params = new HashMap();
        params.put("companyId", getCompanyId());
        params.put("groupId", groupId);
        return R.ok().put("data", userService.getUserByMap(params));
    }

    /**
     * 查询角色列表
     * @return
     */
    @GetMapping("/getRoleList")
    @ApiOperation(value="查询角色列表")
    public R getRoleList() {
        return R.ok().put("data", roleService.getList());
    }
    /**
     * 查询添加用户的角色
     * zyj
     * @return
     */
    @GetMapping("/findUserRole")
    @ApiOperation(value="查询角色列表")
    public R findUserRole() {
        return R.ok().put("data", roleService.findUserRole());
    }

    /**
     * 查询岗位列表
     * @return
     */
    @GetMapping("/getPositionList")
    @ApiOperation(value="查询岗位列表")
    public R getListPosition() {
        Map<String, Object> params = new HashMap<>();
        params.put("companyId",getCompanyId());
        return R.ok().put("data", positionService.getList(params));
    }

    /**
     * 根据groupId查询岗位列表
     * @return
     */
    @GetMapping("/getPositionListByGroupId")
    @ApiOperation(value="查询根据公司查询岗位列表")
    public R getPositionListByGroupId(Long groupId) {
        Map<String, Object> params = new HashMap<>();
        params.put("groupId",groupId);
        params.put("companyId", getCompanyId());  //0715 只查询本公司下的岗位
        return R.ok().put("data", positionService.getPositionByGroup(params));
    }

    @RequestMapping({"/getAccountByGroupAndPstId"})
    public R getAccountByGroupAndPstId( Long positionId, Long groupId) {
        Map<String, Object> map = new HashMap();
        map.put("groupId", groupId);
        map.put("companyId", getCompanyId());
        if (null != positionId && !positionId.equals(0L)) {
            map.put("positionId", positionId);
        }
        List<User> list = this.userService.getUserByMap(map);
        return R.ok().put("data",list);
    }
    @RequestMapping({"/getAllAccountByCompanyId"})
    public R  getAllAccount() {
        Map<String, Object> map = new HashMap();
        map.put("companyId", getCompanyId());
        List<User> list = this.userService.getUserByMap(map);
        return R.ok().put("data",list);
    }

    /**
     * 查询部门类型列表
     * @return
     */
    @GetMapping("/getGrouTypeList")
    @ApiOperation(value="查询部门类型列表")
    public R getGrouTypeList() {
        return R.ok().put("data", GroupTypeService.getList(getCompanyId()));
    }

    @GetMapping("/getSubGroupDetailList")
    @ApiOperation(value="查询当前部门子部门列表")
    public R getSubGroupDetailList(){
       return R.ok().put("data", groupService.getSubGroupDetailList(getGroupId()));
    }
    /**
     * 根据类型查询通用分类
     * @param classifyType
     * @return
     */
    @GetMapping("/getCommonClassifyByType")
    @ApiOperation(value="根据类型查询通用分类",notes = "根据类型查询通用分类;类型：1隐患类型；2检查表类型；4任务检查类型；（可拓展使用，）")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "classifyType", value = "分类", required = true, paramType = "form", dataType = "int"),
        @ApiImplicitParam(name = "classifyValue", value = "类型配置值", required = true, paramType = "form", dataType = "int")
    })
    public R getCommonClassifyByType(Integer classifyType,Integer classifyValue){
        Assert.isNull(classifyType,"类型classifyType 不能为空");
        Map<String, Object> params = new HashMap<>();
        params.put("companyId",getCompanyId());
        params.put("classifyType",classifyType);
        params.put("classifyValue",classifyValue);
        return R.ok().put("data", commonClassifyService.getByClassifyByType(params));
    }

    /**
     * 根据字典类型查询字典数据
     * @param dictType
     * @return
     */
    @GetMapping("/getDictByType")
    @ApiOperation(value="根据字典类型查询字典数据")
    @ApiImplicitParam(name = "dictType", value = "字段类型", required = true, paramType = "form", dataType = "string")
    public R getDictByType(String dictType){
        Assert.isNull(dictType,"字段类型不能为空");
        return R.ok().put("data", dictService.getByDictType(dictType));
    }

    /**
     * 查询系统信息表
     * @return
     */
    @GetMapping("/getSysInfo")
    @ApiOperation(value="查询系统信息表")
    public R getSysInfo(){
        return R.ok().put("data", infoService.getByCId(getCompanyId(),getUser().getUserType()));
    }

    /**
     * 查询静态资源路径
     * @return
     */
    @GetMapping("/getStaticPath")
    @ApiOperation(value="查询静态资源路径")
    public R getStaticPath(){
        HttpServletRequest req = HttpContextUtils.getHttpServletRequest();
        String path = req.getScheme() + "://" + req.getServerName() + ":" + req.getServerPort() + "/source";
        return R.ok().put("data", path);
    }

    /**
     * 查询检查定义列表
     * @return
     */
    @GetMapping("/getInspectDef")
    @ApiOperation(value="查询检查定义列表")
    public R getInspectDef(){
        return R.ok().put("data", inspectDefService.getByCompanyId(getCompanyId()));
    }

    @ApiIgnore()
    public Integer getGroupLeftOrRight(boolean left, boolean right) {
        User user = getUser();
        Integer leftOrRight = null;
        if (left) {
            leftOrRight = user.getOrgLeft();
        } else if (right) {
            leftOrRight = user.getOrgRight();
        }

        GroupMemberPositionDto groupMemberPosition = this.groupMemberPositionService.getByUserIdAndCompanyId(user.getCompanyId(), user.getUserId());
        if (groupMemberPosition != null) {
            if (left) {
                leftOrRight = groupMemberPosition.getGrantGroupLeft();
            } else if (right) {
                leftOrRight = groupMemberPosition.getGrantGroupRight();
            }
        }

//        String groupIdStr = this.getPara("groupId");
//        if (groupIdStr == null || groupIdStr.isEmpty() || groupIdStr.equals("-1")) {
//            groupIdStr = this.getPara("gId");
//        }

        String groupIdStr = null;
        if (groupIdStr != null && !groupIdStr.isEmpty() && !groupIdStr.equals("-1")) {
            Long groupId = Long.parseLong(groupIdStr);
            Group group = groupService.getById(groupId);
            if (group != null) {
                if (left) {
                    leftOrRight = group.getGroupLeft();
                } else if (right) {
                    leftOrRight = group.getGroupRight();
                }
            }
        }
        return leftOrRight;
    }
    /**
     * @Author: zyj
     * @Description:检查账户或用户名是否存在
     * @Param OrgId 企业id  loginName登录名称 userMobile用户电话  userId用户id
     * @Date 14:56 2020/6/8
     */
    @GetMapping("/checkUserMessage")
    @ApiOperation(value="检查账户或用户名是否存在")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "loginName", value = "登录名", required = false, paramType = "json", dataType = "string"),
            @ApiImplicitParam(name = "userMobile", value = "手机号码", required = false, paramType = "json", dataType = "string")
    })
    public R checkUserMessage(String loginName,String userMobile){
       Integer a= userService.checkUserMessage(loginName,userMobile);
       return a>0 ? R.error("已存在") : R.ok("可以使用");
    }
    /**
     * 上传文件
     * yfh
     * 2020/05/29
     * @param file
     * @param req
     * @return
     * @throws IOException
     */
    @ApiOperation(value = "上传文件", notes="上传文件")
    @PostMapping("/import")
    public R importData(MultipartFile file, HttpServletRequest req) throws IOException {
        // 获取文件上传的当前目录路径
        String format = getCurrentDateStr();
        // 根据文件上传的当前目录路径 获取到目录对应的文件夹
        File folder = initUploadFolder(format) ;
        String oldName = file.getOriginalFilename();
        long size = file.getSize();
        String extendName = getCurrentFileExtendName(oldName);
        String newName = UUID.randomUUID().toString() + "." + extendName;
        file.transferTo(new File(folder,newName));
        String allUrl = req.getScheme() + "://" + req.getServerName() + ":" + req.getServerPort() + "/source" + format + newName;
        String url=format + newName;
        return R.ok().put("data", url).put("attachmentName",oldName).put("extendName",extendName).put("size",size).put("allUrl",allUrl);
    }


    private String getCurrentDateStr(){
        return new SimpleDateFormat("/yyyy/MM/dd/").format(new Date());
    }

    private File initUploadFolder(String format){
        String savePath = uploadDir + format;
        File folder = new File(savePath);
        if (!folder.exists()) {
            folder.mkdirs();
        }
        return folder;
    }
    private String getCurrentFileExtendName (String fileSourceName){
       return fileSourceName.substring(fileSourceName .lastIndexOf(".")+1);
    }
}
