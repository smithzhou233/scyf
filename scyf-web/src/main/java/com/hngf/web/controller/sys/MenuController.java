package com.hngf.web.controller.sys;


import java.util.*;

import com.hngf.dto.sys.MenuDto;
import com.hngf.service.sys.UserService;
import com.hngf.web.common.annotation.SysLog;
import io.swagger.annotations.ApiImplicitParams;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hngf.common.exception.ScyfException;
import com.hngf.common.utils.Constant;
import com.hngf.common.utils.R;
import com.hngf.entity.sys.Menu;
import com.hngf.service.sys.MenuService;
import com.hngf.web.controller.BaseController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import springfox.documentation.annotations.ApiIgnore;


/**
 * 菜单表
 *
 * @author hngf
 * @email 
 * @date 2020-05-20 17:31:55
 */
@RestController
@RequestMapping("sys/menu")
@Api(value = "菜单管理",tags = {"菜单管理"})
public class MenuController extends BaseController{
    @Autowired
    private MenuService menuService;
    @Autowired
    private UserService userService;

    /**
     * 列表
     */
    @RequestMapping(value = "/list",method = RequestMethod.GET)
    @RequiresPermissions("sys:menu:list")
    @ApiOperation(value="列表")
    @ApiImplicitParam(name = "keyword", value = "查询关键字", required = false, paramType = "query", dataType = "string")
    public R list(@RequestParam Map<String, Object> params){
        List<Menu> list = menuService.getAll(params);
        return R.ok().put("data", null);
    }

    /**
     * 列表树
     */
    @RequestMapping(value = "/listtree",method = RequestMethod.GET)
    @ApiOperation(value="树形列表",response = Menu.class)
    public R listtree(){
        Long userId = getUserId();
        List<Menu> list;
        //如果当前用户为超级管理员，列出全部菜单
        if (userId == Constant.SUPER_ADMIN) {
            list = menuService.getAll();
        }else{
            //当前用户非超级管理员,列出有权限的菜单列表
            list = userService.getAllMenu(getUserId(),null,null);
        }

        //遍历取出parentId为0的顶级菜单
        List<MenuDto> rootMenu = new ArrayList<>();
        for (Menu root : list) {
            if (null != root && root.getMenuParentId() == 0L) {
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
                root.setChildren(getChildren(root.getMenuId(),list));
            }
        }
        list.clear();
        //给rootMenu排序，按照sortNo字段升序排序；注释掉，排序在SQL语句中处理
        //rootMenu.sort(Comparator.comparing(MenuDto::getSortNo));
        return R.ok().put("data", rootMenu);
    }


    /**
     * 信息
     */
    @RequestMapping(value="/info/{menuId}",method = RequestMethod.GET)
    @RequiresPermissions("sys:menu:info")
    @ApiOperation(value="信息")
    @ApiImplicitParam(name = "menuId", value = "菜单ID", paramType = "path", required = true, dataType = "integer")
    public R info(@PathVariable("menuId") Long menuId){
        Menu Menu = menuService.getById(menuId);

        return R.ok().put("data", Menu);
    }

    /**
     * 保存
     */
    @PostMapping("/save")
    @RequiresPermissions("sys:menu:save")
    @ApiOperation(value="保存", produces="application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "roleName", value = "角色名称", required = true, paramType = "json", dataType = "string"),
            @ApiImplicitParam(name = "roleDesc", value = "描述", required = false, paramType = "json", dataType = "string"),
            @ApiImplicitParam(name = "roleType", value = "角色类型：1私有、2公共", required = true, paramType = "json", dataType = "integer",defaultValue="0"),
            @ApiImplicitParam(name = "roleOrder", value = "排序", required = false, paramType = "json", dataType = "integer")
    })
    @SysLog("保存菜单")
    public R save(@RequestBody Menu Menu){

    	verifyForm(Menu);
        Menu.setDelFlag(0);
        Menu.setCreatedTime(new Date());
        Menu.setCreatedBy(getUserId());
        menuService.save(Menu);

        return R.ok();
    }

    /**
     * 修改
     */
    @PostMapping("/update")
    @RequiresPermissions("sys:menu:update")
    @ApiOperation(value="修改", produces="application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "roleName", value = "角色名称", required = true, paramType = "json", dataType = "string"),
            @ApiImplicitParam(name = "roleDesc", value = "描述", required = false, paramType = "json", dataType = "string"),
            @ApiImplicitParam(name = "roleType", value = "角色类型：1私有、2公共", required = true, paramType = "json", dataType = "integer",defaultValue="0"),
            @ApiImplicitParam(name = "roleOrder", value = "排序", required = false, paramType = "json", dataType = "integer")
    })
    @SysLog("修改菜单")
    public R update(@RequestBody Menu Menu){
    	verifyForm(Menu);
        Menu.setUpdatedTime(new Date());
        Menu.setUpdatedBy(getUserId());
        menuService.update(Menu);
        
        return R.ok();
    }

    /**
     * 删除
     */
    @SysLog("删除菜单")
    @RequestMapping(value="/delete/{menuId}",method = RequestMethod.DELETE)
    @RequiresPermissions("sys:menu:delete")
    @ApiOperation(value="删除")
    @ApiImplicitParam(name = "menuId", value = "菜单ID", paramType = "path", required = true, dataType = "integer")
    public R delete(@PathVariable("menuId") Long menuId){

        //判断是否有子菜单或按钮
        List<Menu> menuList = menuService.getByParentId(menuId);
        if (null != menuList && menuList.size() > 0) {
            return R.error("请先删除子菜单或按钮");
        }

        menuService.removeById(menuId);

        return R.ok();
    }

    /**
     * 批量删除
     */
    @SysLog("删除菜单")
    @RequestMapping(value="/deleteBatch",method = RequestMethod.DELETE)
    @RequiresPermissions("sys:menu:delete")
    @ApiOperation(value="批量删除")
    @ApiImplicitParam(name = "menuIds", value = "ID数组集合", paramType = "query", required = true, dataType = "数组")
    public R deleteBatch(@RequestBody Long[] menuIds){
        menuService.removeByIds(Arrays.asList(menuIds));
        return R.ok();
    }

    /**
	 * 验证参数是否正确
	 */
    @ApiIgnore()
	private void verifyForm(Menu menu){
		if(StringUtils.isBlank(menu.getMenuText())){
			throw new ScyfException("菜单名称不能为空");
		}
		
		if(menu.getMenuParentId() == null){
			throw new ScyfException("上级菜单不能为空");
		}
		
		//菜单
		/*if(menu.getMenuType() == Constant.MenuType.MENU.getValue()){
			if(StringUtils.isBlank(menu.getMenuUrl())){
				throw new ScyfException("菜单URL不能为空");
			}
		}*/
		
		//上级菜单类型
		/*int parentType = Constant.MenuType.CATALOG.getValue();
		if(menu.getMenuParentId() != 0){
			Menu parentMenu = menuService.getById(menu.getMenuParentId());
			parentType = parentMenu.getMenuType();
		}*/
		
		//目录、菜单
		/*if(menu.getMenuType() == Constant.MenuType.CATALOG.getValue() ||
				menu.getMenuType() == Constant.MenuType.MENU.getValue()){
			if(parentType != Constant.MenuType.CATALOG.getValue() && parentType != Constant.MenuType.CLASSIFY.getValue()){
				throw new ScyfException("上级菜单只能为目录类型");
			}
			return ;
		}*/
		
		//按钮
		/*if(menu.getMenuType() == Constant.MenuType.BUTTON.getValue()){
			if(parentType != Constant.MenuType.MENU.getValue()){
				throw new ScyfException("上级菜单只能为菜单类型");
			}
			return ;
		}*/
	}

    //递归获取children节点
    @ApiIgnore()
    private List<MenuDto> getChildren(Long pid,List<Menu> list) {

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
            });
        }
        return children;
    }
}
