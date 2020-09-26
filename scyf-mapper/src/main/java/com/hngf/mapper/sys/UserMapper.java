package com.hngf.mapper.sys;

import java.util.List;
import java.util.Map;

import com.hngf.dto.sys.UserTokenDto;
import com.hngf.entity.sys.Menu;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.hngf.entity.sys.User;

/**
 * 用户基础信息表
 * 
 * @author hngf
 * @email 
 * @date 2020-05-20 14:34:01
 */
@Mapper
public interface UserMapper {

    /**
     * 查询用户列表
     * @param params
     * @return
     */
    List<User> findList(Map<String, Object> params);

    /**
     * 只返回用户表信息
     * @param params
     * @return
     */
    List<User> findByMap(Map<String, Object> params);

    /**
     * 根据ID查询登录名
     * @param userId
     * @return
     */
    String findLoginNameById(Long userId);

    /**
     * 风险点模块 - 选择现场人员使用
     * @param params
     * @return
     */
    List<User> findUserAndStatus(Map<String, Object> params);

    /**
     * 查询用户的所有权限
     * @param userId  用户ID
     */
    List<String> findAllPerms(Long userId);

    /**
     * 查询用户的所有菜单ID
     */
    List<Long> findAllMenuId(Long userId);

    /**
     * 根据ID查询
     * @param id
     * @return
     */
    User findById(Long id);
    /**
     * 根据LoginName查询
     * @param loginName
     * @return
     */
    User findByLoginName(String loginName);

    /**
     * 新增
     * @param User
     */
    void add(User User);

    /**
     * 修改
     * @param User
     */
    void update(User User);

    /**
     * 重置密码
     * @param params
     * @return
     */
    int resetPassword(Map<String, Object> params);

    /**
     * 更新用户状态
     * @param params
     * @return
     */
    int updateStatus(Map<String, Object> params);

    /**
     * 单个删除
     * @param id
     * @return
     */
    int deleteById(@Param("id")Long id);

    /**
     * 批量删除
     * @param ids
     * @return
     */
    int deleteByIds(@Param("ids")List ids);

    /**
     * 根据公司ID删除用户
     * @param cIds
     */
    void deleteByCompanyIds(String cIds);

    /**
     * 修改密码-验证原密码是否正确
     * @param userId
     * @param password 原密码
     * @param newPassword 新密码
     * @return
     */
    int updatePassword(@Param("userId")Long userId,@Param("password")String password,@Param("newPassword")String newPassword);

    /**
     * 根据用户ID获取有权限的菜单
     * @param userId
     * @return
     */
	List<Menu> findAllMenu(@Param("userId")Long userId,@Param("menuType")String menuType, @Param("parentId")Long parentId);
    /**
     * 查看用户及用户角色
     * yfh
     * 2020/06/08
     * @param userId
     * @return
     */
    List<Map<String, Object>> findUserAndRole(@Param("userId")Long userId);

    /**
     * 修改用户账号信息
     * @param user
     */
    void updateUser(User user);

    /**
     * 修改用户角色
     * @param userId
     * @param roleId
     */
    void updateUserRole(@Param("userId")Long userId, @Param("roleId")Integer roleId);
    /**
    * @Author: zyj
    * @Description:检查账户或用户名是否存在
    * @Param  loginName登录名称 userMobile用户电话
    * @Date 14:56 2020/6/8
    */
    Integer checkUserMessage(@Param("loginName") String loginName,@Param("userMobile") String userMobile);

    /**
     * 检查账户或用户名是否存在
     * @param params
     * @return
     */
    Integer checkUserExists(Map<String, Object> params);

    /**
     * 根据用户ID查询token信息
     * @param userId
     * @return
     */
    UserTokenDto findTokenById(Long userId);

    /**
     * 更新用户token信息
     * @param token
     * @return
     */
    int updateToken(UserTokenDto token);
}
