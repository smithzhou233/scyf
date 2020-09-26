package com.hngf.service.sys.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hngf.common.utils.Constant;
import com.hngf.common.utils.PageUtils;
import com.hngf.dto.sys.ElementUIDto;
import com.hngf.entity.sys.Menu;
import com.hngf.entity.sys.Role;
import com.hngf.mapper.sys.MenuMapper;
import com.hngf.mapper.sys.RoleMapper;
import com.hngf.service.sys.RoleService;
import com.hngf.service.sys.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;


@Service("RoleService")
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleMapper roleMapper;
    @Autowired
    private MenuMapper menuMapper;
    @Autowired
    private UserService userService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        int pageNum = null!= params.get("pageNum") ? Integer.parseInt(params.get("pageNum").toString()) : 1;
        int pageSize = null != params.get("pageSize") ? Integer.parseInt(params.get("pageSize").toString()) : Constant.PAGE_SIZE;
        //利用PageHelper分页查询 注意：这个一定要放查询语句的前一行,否则无法进行分页,因为它对紧随其后第一个sql语句有效
        PageHelper.startPage(pageNum, pageSize);
        List<Role> list = roleMapper.findList(params);
        PageInfo<Role> pageInfo = new PageInfo<Role>(list);
        return new PageUtils(list,(int)pageInfo.getTotal(),pageInfo.getPages(),pageInfo.getPageSize(),pageInfo.getPrePage() + 1);
    }

    @Override
    public Role getById(Long id){
        return roleMapper.findById(id);
    }

    @Override
    public List<Role> getList() {
        return roleMapper.findAll();
    }
    /**
     * 查询添加用户的角色
     * zyj
     * @return
     */
    @Override
    public List<Role> findUserRole(){
      return   roleMapper.findUserRole();
    }
    @Override
    public List<Menu> getMenuByRoleId(Long roleId) {
        return roleMapper.findMenuByRoleId(roleId);
    }

    @Override
    public List<ElementUIDto> getElementUIDto(Long roleId,Long userId) {

        List<Menu> data = null;
        if (userId.longValue() == Constant.SUPER_ADMIN) {
            //查询全部菜单
            data = menuMapper.findAll();
        }else{
            //当前用户非超级管理员,列出有权限的菜单列表
            data = userService.getAllMenu(userId,null,null);
        }

        //遍历取出parentId为0的顶级菜单
        List<ElementUIDto> rootMenu = new ArrayList<>();
        for (Menu root : data) {
            if (null != root && root.getMenuParentId() == 0L) {
                ElementUIDto etd = new ElementUIDto();
                etd.setId(root.getMenuId());
                etd.setLabel(root.getMenuText());
                etd.setType(root.getMenuType());
                etd.setIcon(root.getMenuCss());
                etd.setSort(root.getSortNo());
                rootMenu.add(etd);
            }
        }
        //如果顶级菜单有数据，开始查找子节点
        if (null != rootMenu && rootMenu.size() > 0) {
            for (ElementUIDto root : rootMenu) {
                //子节点递归查找添加  传递父节点
                root.setChildren(getChildren(root.getId(),data));
            }
        }
        //清空菜单
        data.clear();
        //给rootMenu排序，按照sortNo字段升序排序
        rootMenu.sort(Comparator.comparing(ElementUIDto::getSort));
        return rootMenu;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(Role Role) {
        roleMapper.add(Role);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(Role Role) {
        roleMapper.update(Role);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeByIds(List ids) {

        roleMapper.deleteByIds(ids);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeById(Long id) {
        roleMapper.deleteById(id);
    }


    //递归获取children节点
    private List<ElementUIDto> getChildren(Long pid,List<Menu> list) {

        List<ElementUIDto> children = new ArrayList<>();

        if (null != pid){

            list.forEach(data -> {
                //若遍历的数据中的父节点id等于参数id
                //则判定当前节点为该参数id节点下的子节点
                if (data.getMenuParentId().longValue() == pid.longValue()) {
                    //构造添加结果集合
                    ElementUIDto etd = new ElementUIDto();
                    etd.setId(data.getMenuId());
                    etd.setLabel(data.getMenuText());
                    etd.setType(data.getMenuType());
                    etd.setIcon(data.getMenuCss());
                    etd.setSort(data.getSortNo());
                    children.add(etd);

                }
            });


        }

        //如果children不为空，继续递归遍历children下的子节点
        if (children.size() > 0) {
            children.forEach(data -> {
                data.setChildren(getChildren(data.getId(),list));
            });
        }
        return children;
    }

    /**
     * 获取所有
     * @param params
     * @return
     */
    @Override
    public List<Map<String,Object>> getListAllByMap (Map<String, Object> params){
        return roleMapper.getListAllByMap(params);
    }
}
