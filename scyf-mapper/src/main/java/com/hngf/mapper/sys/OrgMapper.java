package com.hngf.mapper.sys;

import com.hngf.dto.sys.OrgTreeDto;
import com.hngf.entity.sys.Dict;
import com.hngf.entity.sys.Org;
import com.hngf.entity.sys.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import java.util.List;
import java.util.Map;

/**
 * 组织机构
 * 
 * @author hngf
 * @email 
 * @date 2020-05-20 17:31:55
 */
@Mapper
public interface OrgMapper {

    List<Org> findList(Map<String, Object> params);
    /**
    * @Author: zyj
    * @Description:获取所有行政机构
    * @Param
    * @Date 15:43 2020/6/3
    */
    List<OrgTreeDto> getAllOrgs();
    /**
    * @Author: zyj
    * @Description:查询机构类型数据
    * @Param dictType 字典类型
    * @Date 16:28 2020/6/3
    */
    List<Dict> dictType(@Param("dictType") String dictType);

    List<Org> findByMap(Map<String, Object> params);

    Org findById(Long id);

    void add(Org Org);

    void update(Org Org);

    int deleteById(@Param("id") Long id);

    int deleteByIds(@Param("ids") List ids);
    /**
    * @Author: zyj
    * @Description:删除组织机构
    * @Param
    * @Date 8:47 2020/6/4
    */
    void updateOrgDelFlg(OrgTreeDto orgTreeDto);
    /**
    * @Author: zyj
    * @Description:根据id获取组织机构数据
    * @Param
    * @Date 8:58 2020/6/4
    */
    OrgTreeDto getOrgById(@Param("orgId") Long orgId);

    @Update({"UPDATE sys_org SET ${params} = ${params} + #{steps} WHERE ${condition} and del_flag = 0"})
    void increment(@Param("params") String params, @Param("steps") int steps, @Param("condition") String condition) throws Exception;

    @Update({"UPDATE sys_org SET ${params} = ${params} - #{steps} WHERE ${condition} and del_flag = 0"})

    void decrement(@Param("params") String params, @Param("steps") int steps, @Param("condition") String condition) throws Exception;
    /**
    * @Author: zyj
    * @Description:添加管理员账户信息
    * @Param
    * @Date 11:12 2020/6/4
    */
    void addUser(User user);
    /**
    * @Author: zyj
    * @Description:修改组织管理员id
    * @Param
    * @Date 11:22 2020/6/4
    */
     void updateOrgAdmin(@Param("orgId") Long orgId, @Param("orgAdminId") Long orgAdminId);
     /**
     * @Author: zyj
     * @Description:重置管理员密码
     * @Param
     * @Date 9:30 2020/6/5
     */
     //@Update("UPDATE sys_user set password=#{password,jdbcType=VARCHAR} and salt=#{salt,jdbcType=VARCHAR} where user_id=#{userId}  and company_id=#{companyId} and del_flag=0")
     void resetAdminPwd(@Param("userId") Long userId, @Param("password") String password,@Param("companyId") Long companyId,@Param("salt") String salt);
    /**
     * @Author: zyj
     * @Description:修改组织管理员信息
     * @Param OrgId 企业id  loginName登录名称 userMobile用户电话  userId用户id
     * @Date 14:56 2020/6/8
     */
     void updateOrgAdminName(@Param("companyId") Integer companyId,@Param("loginName") String loginName,@Param("userMobile") String userMobile,@Param("userId") Integer userId);

     /**
     *根据父级Id查询出所有下级
     */
     List getOrgListByPId(Map<String, Object> params);

    /**
     * 查询该机构下所有监管的行业
     * @param orgId
     * @return
     */
    List findIndustrySelectByCompanyId(@Param("orgId")Long orgId);

    /**
     * 更新orgId的groupId
     * @param groupId
     * @param orgId
     * @return
     */
    int updateGroupId(@Param("groupId")Long groupId, @Param("orgId")Long orgId);

}
