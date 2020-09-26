package com.hngf.service.sys.impl;

import java.util.*;
import java.util.stream.Collectors;

import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import com.alibaba.fastjson.JSONArray;
import com.hngf.common.exception.ScyfException;
import com.hngf.common.utils.RedisKeys;
import com.hngf.common.utils.RedisUtils;
import com.hngf.common.validator.ValidatorUtils;
import com.hngf.dto.sys.UserTokenDto;
import com.hngf.entity.device.DeviceInfo;
import com.hngf.entity.sys.*;
import com.hngf.mapper.sys.*;
import com.hngf.service.sys.MenuService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hngf.common.utils.PageUtils;
import com.hngf.service.sys.UserService;


@Service("UserService")
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper UserMapper;
    @Autowired
    private GroupMemberMapper groupMemberMapper;
    @Autowired
    private GroupMemberPositionMapper groupMemberPositionMapper;
    @Autowired
    private UserRoleMapper userRoleMapper;
    @Autowired
    private MenuService menuService;
    @Autowired
    private RedisUtils redisUtils;
    @Autowired
    private PositionMapper positionMapper;

    @Override
    public PageUtils queryPage(Map<String, Object> params,Integer pageNum,Integer pageSize,String order) {
        //利用PageHelper分页查询 注意：这个一定要放查询语句的前一行,否则无法进行分页,因为它对紧随其后第一个sql语句有效
        if(StringUtils.isNotBlank(order)){
            PageHelper.startPage(pageNum, pageSize,order);
        }else{
            PageHelper.startPage(pageNum, pageSize);
        }
        List<User> list = UserMapper.findList(params);
        PageInfo<User> pageInfo = new PageInfo<User>(list);
        return new PageUtils(list,(int)pageInfo.getTotal(),pageInfo.getPages(),pageInfo.getPageSize(),pageInfo.getPrePage() + 1);
    }

    @Override
    public PageUtils getUserAndStatus(Map<String, Object> params, int pageNum, int pageSize, String order) {
        if(StringUtils.isNotBlank(order)){
            PageHelper.startPage(pageNum, pageSize,order);
        }else{
            PageHelper.startPage(pageNum, pageSize);
        }
        List<User> list = UserMapper.findUserAndStatus(params);
        PageInfo<User> pageInfo = new PageInfo<User>(list);
        return new PageUtils(list,(int)pageInfo.getTotal(),pageInfo.getPages(),pageInfo.getPageSize(),pageInfo.getPrePage() + 1);
    }

    @Override
    public List<User> getUserByMap(Map<String, Object> params) {
        return UserMapper.findByMap(params);
    }

    @Override
    public User getById(Long id){
        String loginName = UserMapper.findLoginNameById(id);
        return this.getByLoginName(loginName);
    }

    @Override
    public User getByLoginName(String loginName) {
        //1.首先从redis中查询
        User user = redisUtils.get(RedisKeys.getUserKey(loginName), User.class);
        //2.如果redis中user为空，从数据中查询
        if (null == user) {
            user = UserMapper.findByLoginName(loginName);
            //3.如果user不为空，将user放入redis
            if (null != user) {
                redisUtils.set(RedisKeys.getUserKey(loginName),user);
            }
        }
        return user;
    }

    @Override
	public List<Menu> getAllMenu(Long userId,String menuType,Long parentId) {
        List<Menu> list = UserMapper.findAllMenu(userId,menuType,parentId);
       // this.getMenuParent(new ArrayList<>(),list,null);
		return list;
	}

    @Override
    public List<Long> getRoleId(Long userId) {
        return userRoleMapper.findRoleIdList(userId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updatePassword(Long userId, String password, String newPassword) {
        int res = UserMapper.updatePassword(userId, password, newPassword);
        if (res == 1) {
            //删除redis缓存中的user
            String loginName = UserMapper.findLoginNameById(userId);
            if (StringUtils.isNotBlank(loginName)) {
                redisUtils.delete(RedisKeys.getUserKey(loginName));
            }
        }
        return res == 1;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(User User) {

        UserMapper.add(User);

        //维护用户与组织关系

        GroupMember groupMember = new GroupMember();
        groupMember.setCompanyId(User.getCompanyId());
        groupMember.setGroupId(User.getGroupId());
        groupMember.setUserId(User.getUserId());
        groupMember.setCreatedBy(User.getCreatedBy());
        groupMember.setCreatedTime(new Date());
        groupMember.setDelFlag(0);
        groupMemberMapper.add(groupMember);

        //维护群组成员与岗位关系
        if (User.getPositionId() != null) {
            GroupMemberPosition groupMemberPosition = new GroupMemberPosition();
            groupMemberPosition.setCompanyId(User.getCompanyId());
            groupMemberPosition.setGroupId(User.getGroupId());
            groupMemberPosition.setUserId(User.getUserId());
            groupMemberPosition.setPositionId(User.getPositionId());
            groupMemberPosition.setCreatedBy(User.getCreatedBy());
            groupMemberPosition.setCreatedTime(new Date());
            groupMemberPosition.setDelFlag(0);
            groupMemberPositionMapper.add(groupMemberPosition);
        }

        //维护用户与角色关系
        UserRole userRole = new UserRole();
        userRole.setRoleId(User.getRoleId());
        userRole.setUserId(User.getUserId());

        userRoleMapper.add(userRole);
    }

    /**
     * zyj 导入用户信息
     * @param path
     * @param user
     * @throws Exception
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void importUserInfo(String path,User user) throws Exception {

            UserMapper.add(user);

            //维护用户与组织关系

            GroupMember groupMember = new GroupMember();
            groupMember.setCompanyId(user.getCompanyId());
            groupMember.setGroupId(user.getGroupId());
            groupMember.setUserId(user.getUserId());
            groupMember.setCreatedBy(user.getCreatedBy());
            groupMember.setCreatedTime(new Date());
            groupMember.setDelFlag(0);
            groupMemberMapper.add(groupMember);

            //维护群组成员与岗位关系
            if (user.getPositionId() != null) {
                GroupMemberPosition groupMemberPosition = new GroupMemberPosition();
                groupMemberPosition.setCompanyId(user.getCompanyId());
                groupMemberPosition.setGroupId(user.getGroupId());
                groupMemberPosition.setUserId(user.getUserId());
                groupMemberPosition.setPositionId(user.getPositionId());
                groupMemberPosition.setCreatedBy(user.getCreatedBy());
                groupMemberPosition.setCreatedTime(new Date());
                groupMemberPosition.setDelFlag(0);
                groupMemberPositionMapper.add(groupMemberPosition);
            }

            //维护用户与角色关系
            UserRole userRole = new UserRole();
            userRole.setRoleId(user.getRoleId());
            userRole.setUserId(user.getUserId());

            userRoleMapper.add(userRole);

        }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(User User) {
        UserMapper.update(User);

        //维护用户与组织关系
        GroupMember groupMember = new GroupMember();
        groupMember.setCompanyId(User.getCompanyId());
        groupMember.setGroupId(User.getGroupId());
        groupMember.setUserId(User.getUserId());
        groupMember.setUpdatedBy(User.getUpdatedBy());
        groupMemberMapper.update(groupMember);

        //维护群组成员与岗位关系
        if (User.getPositionId() != null) {
            GroupMemberPosition groupMemberPosition = new GroupMemberPosition();
            groupMemberPosition.setCompanyId(User.getCompanyId());
            groupMemberPosition.setGroupId(User.getGroupId());
            groupMemberPosition.setUserId(User.getUserId());
            groupMemberPosition.setPositionId(User.getPositionId());
            groupMemberPosition.setUpdatedBy(User.getUpdatedBy());
            groupMemberPositionMapper.update(groupMemberPosition);
        }

        //维护用户与角色关系
        userRoleMapper.deleteByUserId(User.getUserId());//维护之前先删除
        UserRole userRole = new UserRole();
        userRole.setRoleId(User.getRoleId());
        userRole.setUserId(User.getUserId());

        userRoleMapper.add(userRole);

        //删除redis缓存
        if(!"".equals(User.getLoginName()))
            redisUtils.delete(RedisKeys.getUserKey(User.getLoginName()));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeByIds(List ids) {
        UserMapper.deleteByIds(ids);
        //删除用户后，删除用户的角色，组织关系
        for (int i = 0; i < ids.size(); i++) {
            Long userId = (Long)ids.get(i);
            userRoleMapper.deleteByUserId(userId);
            groupMemberMapper.deleteByUserId(userId);
            groupMemberPositionMapper.deleteByUserId(userId);

            //删除redis缓存中的user
            String loginName = UserMapper.findLoginNameById(userId);
            redisUtils.delete(RedisKeys.getUserKey(loginName));
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeById(Long id) {
        UserMapper.deleteById(id);
        //删除用户后，删除用户的角色，组织关系
        userRoleMapper.deleteByUserId(id);
        groupMemberMapper.deleteByUserId(id);
        groupMemberPositionMapper.deleteByUserId(id);

        //删除redis缓存中的user
        String loginName = UserMapper.findLoginNameById(id);
        redisUtils.delete(RedisKeys.getUserKey(loginName));
    }

    @Override
    public int updateStatus(Map<String, Object> params) {
        int res = UserMapper.updateStatus(params);
        if (res > 0) {
            //删除redis缓存中的user
            Long userId = Long.parseLong(params.get("userId").toString());
            String loginName = UserMapper.findLoginNameById(userId);
            if (StringUtils.isNotBlank(loginName)) {
                redisUtils.delete(RedisKeys.getUserKey(loginName));
            }
        }
        return res;
    }

    @Override
    public int resetPassword(Map<String, Object> params) {
        int res = UserMapper.resetPassword(params);
        if (res > 0) {
            //删除redis缓存中的user
            Long userId = Long.parseLong(params.get("userId").toString());
            String loginName = UserMapper.findLoginNameById(userId);
            if (StringUtils.isNotBlank(loginName)) {
                redisUtils.delete(RedisKeys.getUserKey(loginName));
            }
        }
        return res;
    }

    /**
     * 查看用户及用户角色
     * yfh
     * 2020/06/08
     * @param userId
     * @return
     */
    @Override
    public List<Map<String, Object>> findUserAndRole(Long userId) {
        return UserMapper.findUserAndRole(userId);
    }

    /**
     * 修改用户账号信息
     * yfh
     * 2020/06/08
     * @param user
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateUser(User user,Integer roleId) {
        //修改用户账号信息
        UserMapper.updateUser(user);
        //修改用户角色
        UserMapper.updateUserRole(user.getUserId(),roleId);

        //删除redis中的缓存信息
        redisUtils.delete(RedisKeys.getUserKey(user.getLoginName()));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateUser(User user) {
        UserMapper.updateUser(user);
        //删除redis中的缓存信息
        redisUtils.delete(RedisKeys.getUserKey(user.getLoginName()));
    }

    /**
     * @Author: zyj
     * @Description:检查账户或用户名是否存在
     * @Param  loginName登录名称 userMobile用户电话
     * @Date 14:56 2020/6/8
     */
    @Override
    public Integer checkUserMessage(String loginName,String userMobile) {
        return UserMapper.checkUserMessage(loginName,userMobile);
    }

    @Override
    public Integer checkUserExists(Map<String, Object> params) {
        return UserMapper.checkUserExists(params);
    }

    @Override
    public int saveToken(UserTokenDto userTokenDto) {
        int res = UserMapper.updateToken(userTokenDto);
        if (res > 0) {
            //将token信息放入redis中
            redisUtils.set(RedisKeys.getUserTokenKey(userTokenDto.getUserId()),userTokenDto);
        }
        return res;
    }

    @Override
    public UserTokenDto getTokenById(Long userId) {
        //先从redis中查询
        UserTokenDto userTokenDto = redisUtils.get(RedisKeys.getUserTokenKey(userId),UserTokenDto.class);
        if (null == userTokenDto) {
            //如果redis中不存在，从数据库中查询
            userTokenDto = UserMapper.findTokenById(userId);
            if (null != userTokenDto && StringUtils.isNotBlank(userTokenDto.getApiToken())) {
                //如果数据库中的token不为空
                redisUtils.set(RedisKeys.getUserTokenKey(userTokenDto.getUserId()),userTokenDto);
            }
        }
        return userTokenDto;
    }

    @Override
    public String getLoginNameById(Long userId) {
        return UserMapper.findLoginNameById(userId);
    }

    /**
     * 获取菜单的父节点
     * @param parentList
     * @param  list
     * @param newList
     */
    private void getMenuParent(List<Menu> parentList,List<Menu> list,List<Menu> newList){
        if (!list.isEmpty()) {
            int tmp = 0;
            //如果newList不为空，使用newList的迭代器遍历
            Iterator<Menu> menuIterator = null;
            if(null != newList && newList.size() > 0){
                menuIterator = newList.iterator();
            }else{
                menuIterator = list.iterator();
            }
            Menu parent = null;
            while(menuIterator.hasNext()){
                parent = menuIterator.next();
                tmp = 0;
                if (null != parent && parent.getMenuParentId() != 0L) {
                    //判断当前菜单集合中是否存在此菜单的父节点
                    for (Menu chd : list) {
                        if (null != chd && parent.getMenuParentId().intValue() == chd.getMenuId()) {
                            tmp ++;
                        }
                    }
                    //判断父级菜单集合中是否存在当前菜单父节点
                    for(Menu chd : parentList){
                        if (null != chd && parent.getMenuParentId().intValue() == chd.getMenuId()) {
                            tmp ++;
                        }
                    }
                    //不存在当前menu的父节点，查询此menu并放入parentList集合中
                    if (tmp == 0) {
                        parentList.add(menuService.getById(parent.getMenuParentId()));
                    }
                }
            }
            if (parentList.size() > 0) {
                //去除重复元素
                parentList = parentList.stream().distinct().collect(Collectors.toList());
                list.addAll(parentList);
                parentList.clear();
                //将当次循环查询出来的父节点，作为要遍历的集合传入，进行递归
                getMenuParent(new ArrayList<>(),list,parentList);
            }
        }
    }
}
