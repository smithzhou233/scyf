package com.hngf.service.sys;

import java.util.List;
import java.util.Map;

import com.hngf.common.utils.PageUtils;
import com.hngf.dto.sys.UserTokenDto;
import com.hngf.entity.sys.Menu;
import com.hngf.entity.sys.User;

/**
 * 用户基础信息表
 *
 * @author hngf
 * @email 
 * @date 2020-05-20 14:38:18
 */
public interface UserService {

    PageUtils queryPage(Map<String, Object> params,Integer pageNum,Integer pageSize,String order);

    /**
     * 风险点模块 - 选择现场人员
     * @param params
     * @param pageNum
     * @param pageSize
     * @param order
     * @return
     */
    PageUtils getUserAndStatus(Map<String, Object> params, int pageNum, int pageSize, String order);

    /**
    * 根据ID查询
     * @param id
    */
    User getById(Long id);

    /**
     * 查询用户表信息
     * @param params
    */
    List<User> getUserByMap(Map<String, Object> params);

    /**
    * 根据登录名查询
     * @param loginName
    */
    User getByLoginName(String loginName);

    /**
     * 修改密码
     * @param userId       用户ID
     * @param password     原密码
     * @param newPassword  新密码
     */
    boolean updatePassword(Long userId, String password, String newPassword);
    
    /**
     * 根据用户ID获取有权限的菜单
     * @param userId
     * @return
     */
    List<Menu> getAllMenu(Long userId,String menuType,Long parentId);

    /**
     * 查询用户拥有的角色ID
     * @param userId
     * @return
     */
    List<Long> getRoleId(Long userId);

    /**
    * 保存
    */
    void save(User User);
    /**
     * 导入用户信息
     * @throws Exception
     */
    void importUserInfo(String path,User user) throws Exception;
    /**
    * 更新
    */
    void update(User User);

    /**
    * 批量删除
    */
    void removeByIds(List ids);

    /**
    * 删除
    */
    void removeById(Long id);

    /**
     * 更新用户状态
     * @param params
     * @return
     */
    int updateStatus(Map<String, Object> params);

    /**
     * 重置密码
     * @param params
     * @return
     */
    int resetPassword(Map<String, Object> params);

    List<Map<String, Object>> findUserAndRole(Long userId);

    /**
     * 修改用户账号信息
     * @param user
     * @param roleId
     */
    void updateUser(User user,Integer roleId);

    /**
     * 修改用户账号信息
     * @param user
     * @return
     */
    void updateUser(User user);

    /**
     * @Author: zyj
     * @Description:检查账户或用户名是否存在
     * @Param  loginName登录名称 userMobile用户电话
     * @Date 14:56 2020/6/8
     */
    Integer checkUserMessage(String loginName,String userMobile);

    /**
     * 检查账户或用户名是否存在
     * @param params
     * @return
     */
    Integer checkUserExists(Map<String, Object> params);

    /**
     * 保存token信息
     * @param userTokenDto
     */
    int saveToken(UserTokenDto userTokenDto);

    /**
     * 根据用户ID查询token信息
     * @param userId
     */
    UserTokenDto getTokenById(Long userId);

    String getLoginNameById(Long userId);
}

