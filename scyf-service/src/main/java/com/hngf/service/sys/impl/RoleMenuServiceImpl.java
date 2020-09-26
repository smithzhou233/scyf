package com.hngf.service.sys.impl;

import com.hngf.common.exception.ScyfException;
import com.hngf.common.utils.RedisUtils;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import java.util.Map;
import java.util.List;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hngf.common.utils.PageUtils;
import com.hngf.common.utils.Constant;

import com.hngf.mapper.sys.RoleMenuMapper;
import com.hngf.entity.sys.RoleMenu;
import com.hngf.service.sys.RoleMenuService;


@Service("RoleMenuService")
public class RoleMenuServiceImpl implements RoleMenuService {

    @Autowired
    private RoleMenuMapper RoleMenuMapper;
    @Autowired
    private RedisUtils redisUtils;


    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        int pageNum = null!= params.get("pageNum") ? Integer.parseInt(params.get("pageNum").toString()) : 1;
        int pageSize = null != params.get("pageSize") ? Integer.parseInt(params.get("pageSize").toString()) : Constant.PAGE_SIZE;
        //利用PageHelper分页查询 注意：这个一定要放查询语句的前一行,否则无法进行分页,因为它对紧随其后第一个sql语句有效
        PageHelper.startPage(pageNum, pageSize);
        List<RoleMenu> list = RoleMenuMapper.findList(params);
        PageInfo<RoleMenu> pageInfo = new PageInfo<RoleMenu>(list);
        return new PageUtils(list,(int)pageInfo.getTotal(),pageInfo.getPages(),pageInfo.getPageSize(),pageInfo.getPrePage() + 1);
    }

    @Override
    public RoleMenu getById(Long id){
        return RoleMenuMapper.findById(id);
    }

    @Override
    public List<RoleMenu> getByRoleId(Long roleId) {
        return RoleMenuMapper.findByRoleId(roleId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(RoleMenu RoleMenu) {
        RoleMenuMapper.add(RoleMenu);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(RoleMenu RoleMenu) {
        RoleMenuMapper.update(RoleMenu);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeByIds(List ids) {
        RoleMenuMapper.deleteByIds(ids);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeById(Long id) {
        RoleMenuMapper.deleteById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int removeByRoleId(Long roleId) {
        return RoleMenuMapper.deleteByRoleId(roleId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateMenu(Long roleId, List<Long> menuIds,Long userId) {
        if (null == roleId) {
            throw new ScyfException("角色ID是空的");
        }
        RoleMenuMapper.deleteByRoleId(roleId);
        if (null != menuIds && menuIds.size() > 0) {
            for (Long menuId : menuIds) {
                RoleMenu roleMenu = new RoleMenu();
                roleMenu.setRoleId(roleId);
                roleMenu.setMenuId(menuId);
                roleMenu.setDelFlag(0);
                roleMenu.setCreatedBy(userId);
                RoleMenuMapper.add(roleMenu);
            }
        }
    }
}
