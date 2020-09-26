package com.hngf.web.controller.sys;

import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import com.alibaba.fastjson.JSONArray;
import com.hngf.common.enums.AccountType;
import com.hngf.common.exception.ScyfException;
import com.hngf.common.utils.Constant;
import com.hngf.common.utils.PageUtils;
import com.hngf.common.utils.R;
import com.hngf.common.validator.Assert;
import com.hngf.common.validator.ValidatorUtils;
import com.hngf.dto.sys.MenuDto;
import com.hngf.entity.sys.*;
import com.hngf.service.sys.GroupMemberPositionGrantService;
import com.hngf.service.sys.MenuService;
import com.hngf.service.sys.PositionService;
import com.hngf.service.sys.UserService;
import com.hngf.web.common.annotation.RepeatSubmit;
import com.hngf.web.common.annotation.SysLog;
import com.hngf.web.common.shiro.ShiroUtils;
import com.hngf.web.controller.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.*;

/**
 * 用户基础信息表
 *
 * @author zhangfei
 * @email 
 * @date 2020-05-20 14:38:18
 */
@RestController
@RequestMapping("sys/user")
@Api(value="用户管理",tags = {"用户管理"})
public class UserController extends BaseController {
    @Autowired
    private UserService UserService;
    @Autowired
    private MenuService menuService;
    @Autowired
    private GroupMemberPositionGrantService groupMemberPositionGrantService;
    @Autowired
    private PositionService positionService;

    /**
     * 列表
     */
    @RequestMapping(value="/list",method = RequestMethod.GET)
    @RequiresPermissions("sys:user:list")
    @ApiOperation(value="列表",response = User.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "groupId", value = "组织ID", required = true, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "pageNum", value = "第几页", required = true, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "pageSize", value = "每页多少条数据", required = true, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "keyword", value = "查询关键字", required = false, paramType = "query", dataType = "string")
    })
    public R list(@RequestParam Map<String, Object> params){
        int pageNum = null!= params.get(Constant.PAGE_PAGENUM) ? Integer.parseInt(params.get(Constant.PAGE_PAGENUM).toString()) : 1;
        int pageSize = null != params.get(Constant.PAGE_PAGESIZE) ? Integer.parseInt(params.get(Constant.PAGE_PAGESIZE).toString()) : Constant.PAGE_SIZE;

        params.put("companyId",getCompanyId());
        //如果没有收到groupId参数，groupId设置为当前用户所属公司的群组根ID
        if (null == params.get("groupId")) {
            params.put("groupId",getCompanyGroupId());
        }
        PageUtils page = UserService.queryPage(params,pageNum,pageSize,null);

        return R.ok().put("data", page);
    }

    /**
     * 查询当前用户有权限的菜单列表
     */
    @RequestMapping(value="/getMenuList",method = RequestMethod.GET)
    @ApiOperation(value="查询当前用户有权限的菜单列表",response = Menu.class,notes="查询一级菜单：menuType=1  parentId =0 ，<br/> 查询二级菜单： menuType=2 , parentId = #{pid}")
    @ApiImplicitParams({
         @ApiImplicitParam(name = "menuType", value = "菜单类型", required = true, paramType = "query", dataType = "String"),
         @ApiImplicitParam(name = "parentId", value = "父级Id", required = false, paramType = "query", dataType = "int"),
    })
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
            return R.ok().put("data", rootMenu);
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
    @RequestMapping(value = "/password",method = RequestMethod.PUT)
    @ApiOperation(value="修改密码")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "password", value = "旧密码", paramType = "form", required = true, dataType = "string"),
            @ApiImplicitParam(name = "newPassword", value = "新密码", paramType = "form", required = true, dataType = "string")
    })
    @RepeatSubmit()
    public R password(String password, String newPassword){
        Assert.isBlank(password, "旧密码不为能空");
        Assert.isBlank(newPassword, "新密码不为能空");

        //原密码
        password = ShiroUtils.sha256(password, ShiroUtils.getUser().getSalt());
        //新密码
        newPassword = ShiroUtils.sha256(newPassword, ShiroUtils.getUser().getSalt());

        //更新密码
        boolean flag = UserService.updatePassword(ShiroUtils.getUserId(), password, newPassword);
        if(!flag){
            return R.error("原密码不正确");
        }
        return R.ok();
    }

    /**
     * 重置密码
     */
    @SysLog("重置密码")
    @RequestMapping(value="/resetPassword/{userId}",method = RequestMethod.PUT)
    @RequiresPermissions("sys:user:update")
    @ApiOperation(value="重置密码")
    @ApiImplicitParam(name = "userId", value = "用户ID", required = true, paramType = "path", dataType = "integer")
    @RepeatSubmit()
    public R resetPassword(@PathVariable("userId") Long userId){
        Assert.isNull(userId, "用户ID不能为空");
        //sha256加密
        String salt = RandomStringUtils.randomAlphanumeric(20);
        //新密码
        String newPassword = ShiroUtils.sha256(Constant.USER_PASSWORD, salt);
        Map<String, Object> params = new HashMap<>();
        params.put("userId", userId);
        params.put("password", newPassword);
        params.put("salt", salt);
        int s = UserService.resetPassword(params);

        return s > 0 ? R.ok() : R.error("更新失败");
    }

    /**
     * 信息
     */
    @RequestMapping(value="/info/{userId}",method = RequestMethod.GET)
    @RequiresPermissions("sys:user:info")
    @ApiOperation(value="信息",response = User.class)
    @ApiImplicitParam(name = "userId", value = "用户ID", required = true, paramType = "path", dataType = "integer")
    public R info(@PathVariable("userId") Long userId){
        User user = UserService.getById(userId);
        if(null != user){
            user.setPassword("");
            user.setSalt("");
        }
        return R.ok().put("data", user);
    }

    /**
     * 选择现场人员
     */
    @GetMapping(value="/getUserAndStatus")
    @RequiresPermissions("sys:user:info")
    @ApiOperation(value="选择现场人员",response = User.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "riskPointId", value = "风险点ID", required = true, paramType = "form", dataType = "int"),
            @ApiImplicitParam(name = "groupId", value = "部门ID", required = false, paramType = "form", dataType = "int"),
            @ApiImplicitParam(name = "pageNum", value = "第几页", required = true, paramType = "form", dataType = "int"),
            @ApiImplicitParam(name = "pageSize", value = "每页多少条数据", required = true, paramType = "form", dataType = "int"),
            @ApiImplicitParam(name = "keyword", value = "查询关键字", required = false, paramType = "form", dataType = "string")
    })
    public R info(@RequestParam Map<String, Object> params){
        int pageNum = null!= params.get(Constant.PAGE_PAGENUM) ? Integer.parseInt(params.get(Constant.PAGE_PAGENUM).toString()) : 1;
        int pageSize = null != params.get(Constant.PAGE_PAGESIZE) ? Integer.parseInt(params.get(Constant.PAGE_PAGESIZE).toString()) : Constant.PAGE_SIZE;
        if (null == params.get("riskPointId") || "".equals(params.get("riskPointId").toString())) {
            return R.error("风险点ID不能为空");
        }
        return R.ok().put("data", UserService.getUserAndStatus(params,pageNum,pageSize,null));
    }

    /**
     * 更新用户状态
     */
    @RequestMapping(value = "/updateStatus/{userId}/{userStatus}",method = RequestMethod.PUT)
    @RequiresPermissions("sys:user:update")
    @ApiOperation(value="更新用户状态")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户ID", required = true, paramType = "path", dataType = "integer"),
            @ApiImplicitParam(name = "userStatus", value = "用户状态(0正常，1锁定)", required = true, paramType = "path", dataType = "integer")
    })
    @SysLog("更新用户状态")
    @RepeatSubmit()
    public R updateStatus(@PathVariable("userId") Long userId,@PathVariable("userStatus") Integer userStatus){
        Assert.isNull(userId, "用户ID不能为空");
        Assert.isNull(userStatus, "用户状态不能为空");

        Map<String, Object> params = new HashMap<>();
        params.put("userId", userId);
        params.put("userStatus", userStatus);
        int s = UserService.updateStatus(params);

        return s > 0 ? R.ok() : R.error("更新失败");
    }

    /**
     * 添加/修改授权部门
     */
    @PostMapping("/saveGrantGroup")
    @RequiresPermissions("sys:user:info")
    @ApiOperation(value="添加授权部门")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "grantGroupId", value = "授权部门ID", required = true, paramType = "query", dataType = "integer"),
            @ApiImplicitParam(name = "userId", value = "被授权用户ID", required = true, paramType = "query", dataType = "integer"),
            @ApiImplicitParam(name = "groupId", value = "被授权用户所在部门ID", required = true, paramType = "query", dataType = "integer"),
            @ApiImplicitParam(name = "positionId", value = "被授权用户所属岗位ID", required = true, paramType = "query", dataType = "integer"),
            @ApiImplicitParam(name = "groupMemberPositionGrantId", value = "部门已授权的主键ID(更新时需要传入，新增时不需要)", required = false, paramType = "query", dataType = "integer")
    })
    @SysLog("添加/修改授权部门")
    @RepeatSubmit()
    public R saveGrantGroup(@RequestBody Map<String, Object> params){
        Assert.isNull(params.get("grantGroupId"),"授权部门ID不能为空");
        Assert.isNull(params.get("userId"),"被授权用户不能为空");
        Assert.isNull(params.get("groupId"),"被授权用所在部门ID不能为空");
        Assert.isNull(params.get("positionId"),"被授权用户所属岗位ID不能为空");

        Long grantGroupId = Long.parseLong(params.get("grantGroupId").toString());
        Long userId = Long.parseLong(params.get("userId").toString());
        Long groupId = Long.parseLong(params.get("groupId").toString());
        Long positionId = Long.parseLong(params.get("positionId").toString());

        Long gmpgId = null;
        if(null != params.get("groupMemberPositionGrantId")){
            gmpgId = Long.parseLong(params.get("groupMemberPositionGrantId").toString());
        }

        GroupMemberPositionGrant grantGroup = null;
        if (null == gmpgId) {
            grantGroup = new GroupMemberPositionGrant();

            grantGroup.setCompanyId(getCompanyId());
            grantGroup.setGroupId(groupId);
            grantGroup.setGrantGroupId(grantGroupId);
            grantGroup.setPositionId(positionId);
            grantGroup.setUserId(userId);
            grantGroup.setCreatedBy(getUserId());
            grantGroup.setDelFlag(0);
            groupMemberPositionGrantService.save(grantGroup);

        }else{
            grantGroup = groupMemberPositionGrantService.getById(gmpgId);

            if (grantGroup != null) {
                grantGroup.setGrantGroupId(grantGroupId);
                grantGroup.setUserId(userId);
                grantGroup.setUpdatedBy(getUserId());
                groupMemberPositionGrantService.update(grantGroup);
            }else{
                return R.error("没有查询到授权记录");
            }
        }
        return R.ok();
    }

    /**
     * 删除授权部门
     */
    @SysLog("删除授权部门")
    @RequestMapping(value = "/delGrantGroup",method = RequestMethod.DELETE)
    @ApiOperation(value="删除授权部门")
    @ApiImplicitParam(name = "groupMemberPositionGrantId", value = "部门已授权的主键ID", required = true, paramType = "query", dataType = "integer")
    public R delGrantGroup(@RequestParam("groupMemberPositionGrantId") Long groupMemberPositionGrantId){
        groupMemberPositionGrantService.removeById(groupMemberPositionGrantId);
        return R.ok();
    }

    /**
     * 检查登录名是否重名
     */
    @GetMapping("/check/{loginName}")
    @RequiresPermissions(value = {"sys:user:info","sys:org:save","sys:company:save","gent:company:save","org:org:save"},logical = Logical.OR)
    @ApiOperation(value="检查登录名是否重名")
    @ApiImplicitParam(name = "loginName", value = "登录名", required = true, paramType = "path", dataType = "string")
    public Boolean check(@PathVariable("loginName") String loginName){
        return null == UserService.getByLoginName(loginName) ;
    }

    /**
     * 个人中心
     */
    @RequestMapping(value="/profile",method = RequestMethod.GET)
    @ApiOperation(value="个人中心",response = User.class)
    public R info(){
        User user = UserService.getByLoginName(getLoginName());
        user.setPassword("");
        user.setSalt("");
        return R.ok().put("data", user);
    }

    /**
     * 保存
     */
    @PostMapping(value= "/save")
    @RequiresPermissions("sys:user:save")
    @ApiOperation(value="保存", produces="application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "loginName", value = "登录名", required = true, paramType = "json", dataType = "string"),
            @ApiImplicitParam(name = "userName", value = "用户名称", required = false, paramType = "json", dataType = "string"),
            @ApiImplicitParam(name = "userMobile", value = "手机号", required = false, paramType = "json", dataType = "string"),
            @ApiImplicitParam(name = "userEmail", value = "邮箱", required = false, paramType = "json", dataType = "string"),
            @ApiImplicitParam(name = "userStatus", value = "账号状态，0正常，1锁定", required = true, paramType = "json", dataType = "integer",defaultValue="0"),
            @ApiImplicitParam(name = "userType", value = "帐户类型：1. 系统帐号； 2.政府帐号   3.公司帐号. 4. 集团帐号", required = true, paramType = "query", dataType = "integer",defaultValue="1"),
            @ApiImplicitParam(name = "userPicture", value = "头像", required = false, paramType = "json", dataType = "string"),
            @ApiImplicitParam(name = "userAddress", value = "地址", required = false, paramType = "json", dataType = "string"),
            @ApiImplicitParam(name = "userSex", value = "性别", required = false, paramType = "json", dataType = "integer"),
            @ApiImplicitParam(name = "userIntro", value = "用户简介", required = false, paramType = "json", dataType = "string"),
            @ApiImplicitParam(name = "userIdcard", value = "身份证号码", required = false, paramType = "json", dataType = "string"),
            @ApiImplicitParam(name = "roleId", value = "所属角色", required = true, paramType = "json", dataType = "integer"),
            @ApiImplicitParam(name = "groupId", value = "所属部门", required = true, paramType = "json", dataType = "integer"),
            @ApiImplicitParam(name = "positionId", value = "所属岗位", required = true, paramType = "json", dataType = "integer")
    })
    @SysLog("新增用户")
    @RepeatSubmit()
    public R save(@RequestBody User User){
        User check = UserService.getByLoginName(User.getLoginName());
        if (null != check) {
            throw new ScyfException(User.getLoginName() + " 已存在！");
        }

        ValidatorUtils.validateEntity(User);
        User.setDelFlag(0);
        User.setCreatedTime(new Date());
        User.setCreatedBy(getUserId());
        //sha256加密
        String salt = RandomStringUtils.randomAlphanumeric(20);
        User.setSalt(salt);
        User.setPassword(ShiroUtils.sha256(Constant.USER_PASSWORD, User.getSalt()));
        //用户的companyId默认为当前管理员的CompanyId
        User.setCompanyId(getCompanyId());
        //usertype设置为当前账号的AccountType
        AccountType curUserAccountType = this.getCurrentUserAccountType();
        User.setUserType(curUserAccountType.ordinal());
        if (null == User.getUserStatus()) {
            User.setUserStatus(0);
        }

        UserService.save(User);

        return R.ok();
    }

    /**
     * 修改
     */
    @PostMapping(value="/update")
    @RequiresPermissions("sys:user:update")
    @ApiOperation(value="修改", produces="application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户ID", required = true, paramType = "json", dataType = "integer"),
            @ApiImplicitParam(name = "loginName", value = "登录名", required = true, paramType = "json", dataType = "string"),
            @ApiImplicitParam(name = "userName", value = "用户名称", required = false, paramType = "json", dataType = "string"),
            @ApiImplicitParam(name = "userMobile", value = "手机号", required = false, paramType = "json", dataType = "string"),
            @ApiImplicitParam(name = "userEmail", value = "邮箱", required = false, paramType = "json", dataType = "string"),
            @ApiImplicitParam(name = "userStatus", value = "账号状态，0正常，1锁定", required = true, paramType = "json", dataType = "integer",defaultValue="0"),
            @ApiImplicitParam(name = "userType", value = "帐户类型：1. 系统帐号； 2.政府帐号   3.公司帐号. 4. 集团帐号", required = true, paramType = "query", dataType = "integer",defaultValue="1"),
            @ApiImplicitParam(name = "userPicture", value = "头像", required = false, paramType = "json", dataType = "string"),
            @ApiImplicitParam(name = "userAddress", value = "地址", required = false, paramType = "json", dataType = "string"),
            @ApiImplicitParam(name = "userSex", value = "性别", required = false, paramType = "json", dataType = "integer"),
            @ApiImplicitParam(name = "userIntro", value = "用户简介", required = false, paramType = "json", dataType = "string"),
            @ApiImplicitParam(name = "userIdcard", value = "身份证号码", required = false, paramType = "json", dataType = "string"),
            @ApiImplicitParam(name = "roleId", value = "所属角色", required = true, paramType = "json", dataType = "integer"),
            @ApiImplicitParam(name = "groupId", value = "所属部门", required = true, paramType = "json", dataType = "integer"),
            @ApiImplicitParam(name = "positionId", value = "所属岗位", required = true, paramType = "json", dataType = "integer")
    })
    @SysLog("修改用户")
    public R update(@RequestBody User User){
        ValidatorUtils.validateEntity(User);
        User.setUpdatedTime(new Date());
        User.setUpdatedBy(getUserId());
        User.setCompanyId(getCompanyId());
        User.setCompanyGroupId(getCompanyGroupId());
        UserService.update(User);
        
        return R.ok();
    }
    /**
     * 删除
     */
    @SysLog("删除用户")
    @RequestMapping(value = "/delete/{userIds}",method = RequestMethod.DELETE)
    @RequiresPermissions("sys:user:delete")
    @ApiOperation(value="删除")
    @ApiImplicitParam(name = "userIds", value = "ID", paramType = "主键id", required = true, dataType = "主键id")
    @RepeatSubmit()
    public R delete(@PathVariable("userIds") Long userIds){
        if (userIds == 1L) {
            return R.error("不能删除超级管理员");
        }
        UserService.removeById(userIds);

        return R.ok();
    }
    /**
     * 删除
     */
    @SysLog("删除用户")
    @RequestMapping(value = "/deletes",method = RequestMethod.DELETE)
    @RequiresPermissions("sys:user:delete")
    @ApiOperation(value="删除")
    @ApiImplicitParam(name = "userIds", value = "ID数组集合", paramType = "数组", required = true, dataType = "数组")
    @RepeatSubmit()
    public R deletes(@RequestBody Long[] userIds){
        for (int i = 0; i < userIds.length; i++) {
            if (userIds[i] == 1L) {
                userIds[i] = -1L;
            }
        }
        UserService.removeByIds(Arrays.asList(userIds));

        return R.ok();
    }

    /**
     * 导入用户信息 zyj
     * @param file
     * @param request
     * @param groupId
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "importUserInfo",method = RequestMethod.POST)
    @ApiOperation(value="导入用户信息")
    @ApiImplicitParam(name = "groupId", value = "组织id",required = true, paramType = "query", dataType = "Long")
    public R importUserInfo(MultipartFile file, HttpServletRequest request,Long groupId) throws Exception {
        String path="";
        //新建文件夹 放入excel表格 并返回文件路径
        path =saveFileAndBackPath(file,request);
        User user=new User();
        user.setDelFlag(0);
        user.setCreatedTime(new Date());
        user.setCreatedBy(getUserId());
        //sha256加密
        String salt = RandomStringUtils.randomAlphanumeric(20);
        user.setSalt(salt);
        user.setPassword(ShiroUtils.sha256(Constant.USER_PASSWORD, user.getSalt()));
        //用户的companyId默认为当前管理员的CompanyId
        user.setCompanyId(getCompanyId());
        //usertype设置为当前账号的AccountType
        AccountType curUserAccountType = this.getCurrentUserAccountType();
        user.setUserType(curUserAccountType.ordinal());
        if (null == user.getUserStatus()) {
            user.setUserStatus(0);
        }
         user.setGroupId(groupId);
        //读取excel文件
        ExcelReader reader = ExcelUtil.getReader(path);
        //获取excel表格内容
        List<List<Object>> readAll = reader.read();
        for(int i = 1; i < readAll.size(); ++i) {
            List<Object> one = (List)readAll.get(i);
            JSONArray data = new JSONArray(one);
            String riskCode1 = one.size()>0 ?data.getString(0):"";
            String riskCode2 = one.size()>1 ?data.getString(1):"";
            String riskCode3 = one.size()>2 ?data.getString(2):"";
            String riskCode4 = one.size()>3 ? data.getString(3):"";
            User check = UserService.getByLoginName(riskCode1);
            if (null != check) {
                throw new ScyfException(riskCode1 + " 已存在！");
            }
            user.setUserName(riskCode2);
            user.setLoginName(riskCode1);
            user.setUserSex(1);
            user.setUserMobile(riskCode3);
            user.setRoleId(5L);
            user.setPositionId(getId("sys_position"));
            Position positionTitle = positionService.findPositionTitle(user.getCompanyId(), riskCode4);
            Position position=new Position();
            if (null==positionTitle){
                position.setCompanyId(user.getCompanyId());
                position.setCreatedTime(new Date());
                position.setCreatedBy(user.getCreatedBy());
                position.setPositionDesc(riskCode4);
                position.setPositionTitle(riskCode4);
                position.setPositionId(user.getPositionId());
                positionService.save(position);
            }else {
                position.setPositionId(positionTitle.getPositionId());
            }
            user.setPositionId(position.getPositionId());
            ValidatorUtils.validateEntity(user);
            UserService.save(user);
        }

        return R.ok("导入成功");
    }
    /**
     * 新建文件夹 放入excel表格 并返回文件路径
     * yfh
     * 2020/06/04
     * @param uploadfile
     * @param request
     * @return
     */
    public String saveFileAndBackPath(MultipartFile uploadfile, HttpServletRequest request) {
        String excelPath = request.getSession().getServletContext().getRealPath("/importExcel");
        FileBean fileBean = this.saveFile(uploadfile, excelPath, "/upload");
        return excelPath + "/" + fileBean.getSaveFileName();
    }
    public FileBean saveFile(MultipartFile uploadfile, String uploadPath, String prefix) {
        FileBean fileBean = new FileBean();

        try {
            if (uploadfile.isEmpty()) {
                return fileBean;
            } else {
                File dir = new File(uploadPath);
                if (!dir.exists()) {
                    dir.mkdirs();
                }

                String originalFileName = uploadfile.getOriginalFilename();
                String extName = originalFileName.substring(originalFileName.lastIndexOf(46) + 1, originalFileName.length());
                //获取保存文件名称
                String saveFileName = this.generateSaveFileName(extName);
                if ("apk".equals(extName)) {
                    saveFileName = originalFileName;
                }

                Long fileSize = uploadfile.getSize();
                logger.debug("###" + originalFileName);
                logger.debug("###" + extName);
                logger.debug("###" + saveFileName);
                logger.debug("###" + fileSize);
                String filepath = Paths.get(uploadPath, saveFileName).toString();
                BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(new File(filepath)));
                stream.write(uploadfile.getBytes());
                stream.close();
                String path = uploadPath + saveFileName;
                String url = path.substring(0, path.lastIndexOf("."));
                if (!"mp4".equals(extName) && !"3gp".equals(extName) && !"mov".equals(extName) && !"m4v".equals(extName) && !"rm".equals(extName) && "rmvb".equals(extName)) {
                }

                fileBean.setOriFileName(originalFileName);
                fileBean.setSaveFileName(saveFileName);
                fileBean.setExt(extName);
                fileBean.setSize(fileSize);
                fileBean.setPath(prefix + "/" + saveFileName);
                fileBean.setMimeType(uploadfile.getContentType());
                return fileBean;
            }
        } catch (IOException var14) {
            throw new RuntimeException("upload file");
        }
    }
    /**
     * 获取保存文件名称
     * @param extName
     * @return
     */
    private String generateSaveFileName(String extName) {
        String fileName = "";
        Calendar calendar = Calendar.getInstance();
        fileName = fileName + calendar.get(1);
        fileName = fileName + (calendar.get(2) + 1);
        fileName = fileName + calendar.get(5);
        fileName = fileName + calendar.get(11);
        fileName = fileName + calendar.get(12);
        fileName = fileName + calendar.get(13);
        fileName = fileName + calendar.get(14);
        Random random = new Random();
        fileName = fileName + random.nextInt(9999);
        fileName = fileName + "." + extName;
        return fileName;
    }
}
