package com.hngf.service.sys;

import com.hngf.common.utils.PageUtils;
import com.hngf.dto.sys.OrgTreeDto;
import com.hngf.entity.sys.Dict;
import com.hngf.entity.sys.Org;
import com.hngf.entity.sys.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 组织机构
 *
 * @author hngf
 * @email 
 * @date 2020-05-20 17:31:55
 */
public interface OrgService {

    PageUtils queryPage(Map<String, Object> params);
    /**
     * @Author: zyj
     * @Description:获取所有行政机构
     * @Param
     * @Date 15:43 2020/6/3
     */
    List<OrgTreeDto> getAllOrgs();
    /**
     * @Author: yss
     * @Description:根据父节点获取行政机构
     * @Param
     * @Date 15:43 2020/8/29
     */
    List<OrgTreeDto> findListByParentId(Map<String, Object> params);
    /**
     * @Author: zyj
     * @Description:查询机构类型数据
     * @Param dictType 字典类型
     * @Date 16:28 2020/6/3
     */
    List<Dict> dictType(@Param("dictType") String dictType);
    /**
    * 根据ID查询
    */
    Org getById(Long id);

    /**
    * 查询全部
    */
    List<Org> getList(Map<String, Object> params);

    /**
    * 保存
    */
    void save(Org org);

    /**
    * 更新
    */
    void update(Org Org);

    /**
    * 批量删除
    */
    void removeByIds(List ids);

    /**
    * 删除
    */
    void removeById(Long id) ;
    /**
     * @Author: zyj
     * @Description:删除组织机构
     * @Param
     * @Date 8:47 2020/6/4
     */
    void updateOrgDelFlg(OrgTreeDto orgTreeDto) throws Exception;
    /**
     * @Author: zyj
     * @Description:根据id获取组织机构数据
     * @Param
     * @Date 8:58 2020/6/4
     */
    OrgTreeDto getOrgById(@Param("orgId") Long orgId);
    /**
     * @Author: zyj
     * @Description:添加管理员
     * @Param
     * @Date 11:18 2020/6/4
     */
    void insertOrgAdmin(User user);
    /**
     * @Author: zyj
     * @Description:给组织添加行业
     * @Param
     * @Date 16:40 2020/6/4
     */
    void insertOrgIndustry(Long orgId,String industryId,Long userId);
    /**
    * @Author: zyj
    * @Description:重置管理员密码
    * @Param
    * @Date 18:11 2020/6/4
    */
    void resetAdminPwd(Long userId,String password,Long companyId,String salt);
    /**
     * @Author: zyj
     * @Description:修改组织管理员信息
     * @Param OrgId 企业id  loginName登录名称 userMobile用户电话  userId用户id
     * @Date 14:56 2020/6/8
     */
    void updateOrgAdminName(Integer companyId,String loginName,String userMobile,Integer userId);

    List findIndustrySelectByCompanyId(Long orgId);
}

