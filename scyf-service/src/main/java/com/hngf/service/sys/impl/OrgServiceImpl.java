package com.hngf.service.sys.impl;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hngf.common.enums.AccountType;
import com.hngf.common.exception.ScyfException;
import com.hngf.common.utils.Constant;
import com.hngf.common.utils.PageUtils;
import com.hngf.dto.sys.OrgTreeDto;
import com.hngf.entity.sys.Dict;
import com.hngf.entity.sys.Group;
import com.hngf.entity.sys.GroupMember;
import com.hngf.entity.sys.GroupMemberPosition;
import com.hngf.entity.sys.GroupType;
import com.hngf.entity.sys.Org;
import com.hngf.entity.sys.OrgIndustry;
import com.hngf.entity.sys.Position;
import com.hngf.entity.sys.User;
import com.hngf.entity.sys.UserRole;
import com.hngf.mapper.sys.GroupMapper;
import com.hngf.mapper.sys.GroupMemberMapper;
import com.hngf.mapper.sys.GroupMemberPositionMapper;
import com.hngf.mapper.sys.GroupTypeMapper;
import com.hngf.mapper.sys.OrgIndustryMapper;
import com.hngf.mapper.sys.OrgMapper;
import com.hngf.mapper.sys.PositionMapper;
import com.hngf.mapper.sys.UserRoleMapper;
import com.hngf.service.sys.DictService;
import com.hngf.service.sys.OrgService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service("OrgService")
public class OrgServiceImpl implements OrgService {

    @Autowired
    private OrgMapper orgMapper;
    @Autowired
    private GroupMemberMapper groupMemberMapper;
    @Autowired
    private GroupMemberPositionMapper groupMemberPositionMapper;
    @Autowired
    private PositionMapper positionMapper;
    @Autowired
    private DictService dictService;
    @Autowired
    private UserRoleMapper userRoleMapper;
    @Autowired
    private OrgIndustryMapper orgIndustryMapper;
    @Autowired
    private GroupMapper groupMapper;

    @Autowired
    private GroupTypeMapper groupTypeMapper;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        int pageNum = null!= params.get("pageNum") ? Integer.parseInt(params.get("pageNum").toString()) : 1;
        int pageSize = null != params.get("pageSize") ? Integer.parseInt(params.get("pageSize").toString()) : Constant.PAGE_SIZE;
        //利用PageHelper分页查询 注意：这个一定要放查询语句的前一行,否则无法进行分页,因为它对紧随其后第一个sql语句有效
        PageHelper.startPage(pageNum, pageSize);
        List<Org> list = orgMapper.findList(params);
        PageInfo<Org> pageInfo = new PageInfo<Org>(list);
        return new PageUtils(list,(int)pageInfo.getTotal(),pageInfo.getPages(),pageInfo.getPageSize(),pageInfo.getPrePage() + 1);
    }
    /**
     * @Author: zyj
     * @Description:获取所有行政机构
     * @Param
     * @Date 15:43 2020/6/3
     */
    @Override
    public List<OrgTreeDto> getAllOrgs() {
        return orgMapper.getAllOrgs();
    }

    /**
     * @Author: yss
     * @Description:根据父节点获取行政机构
     * @Param
     * @Date 15:43 2020/8/29
     */
    @Override
    public List<OrgTreeDto> findListByParentId(Map<String, Object> params) {
        return orgMapper.getOrgListByPId(params);
    }


    /**
     * @Author: zyj
     * @Description:查询机构类型数据
     * @Param dictType 字典类型
     * @Date 16:28 2020/6/3
     */
    @Override
    public List<Dict> dictType(String dictType) {
       return orgMapper.dictType(dictType);
    }

    @Override
    public List<Org> getList(Map<String, Object> params) {
        return orgMapper.findByMap(params);
    }

    @Override
    public Org getById(Long id){
        return orgMapper.findById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(Org org) {
        //检查同名
        Map<String, Object> params = new HashMap<>();
        params.put("orgName", org.getOrgName());
        List<Org> list = getList(params);
        if (null != list && list.size() > 0) {
            throw new ScyfException(org.getOrgName() + "已存在！");
        }
        if ( null == org.getOrgParentId()){
            org.setOrgParentId(0L);
        }
        //获取组织id
        Long orgId = dictService.getTabId("sys_org");
        org.setOrgId(orgId);
        Long orgParentId = org.getOrgParentId();
        if (null == orgParentId || 0l== orgParentId.longValue()){
            org.setOrgLeft(1);
            org.setOrgRight(2);
            org.setOrgRootId(orgId);
            org.setOrgLevel(1);
        }else {

            OrgTreeDto orgParent = this.getOrgById(orgParentId);
            Long rootId = orgParent.getOrgRootId();
            int nLeft = orgParent.getOrgRight();
            int iLevel = orgParent.getOrgLevel();
            try {
                orgMapper.increment("org_right", 2, String.format("org_root_id = %d and org_right >= %d", rootId, nLeft));
                orgMapper.increment("org_left", 2, String.format("org_root_id = %d and org_left  >= %d", rootId, nLeft));
            } catch (Exception e) {
                e.printStackTrace();
            }
            org.setOrgRootId(rootId);
            org.setOrgLeft(nLeft);
            org.setOrgRight(nLeft + 1);
            org.setOrgLevel(iLevel + 1);
        }
        orgMapper.add(org);
        // 增加默认的 组织部门分类
        GroupType groupType = new GroupType();
        groupType.setCompanyId(orgId);
        groupType.setGroupTypeTitle("行业监管机构");
        groupType.setGroupTypeShowif(1);
        groupType.setGroupTypeDesc("行业监管机构");
        groupTypeMapper.add(groupType);
        //增加默认的组织
        Group group = new Group();
        group.setCompanyId(orgId);
        group.setGroupName(org.getOrgName());
        group.setGroupParent(0L);
        group.setGroupTypeId(groupType.getGroupTypeId());
        group.setGroupLeft(1);
        group.setGroupRight(2);
        group.setGroupLevel(1);
        group.insertPrefix(org.getCreatedBy());
        groupMapper.add(group);

        // 更新监察机构的组织Id
        orgMapper.updateGroupId(group.getGroupId(),orgId);

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(Org org) {
        //检查同名
        Map<String, Object> params = new HashMap<>();
        params.put("orgName", org.getOrgName());
        List<Org> list = getList(params);
        if (null != list && list.size()>0 ) {
            if(list.size()>1){
                throw new ScyfException(org.getOrgName() + "已存在！");
            }else {
                if(org.getOrgId().longValue() != list.get(0).getOrgId().longValue()){
                    throw new ScyfException(org.getOrgName() + "已存在！");
                }
            }
        }
        orgMapper.update(org);
        Group group = new Group();
        Long orgId = org.getOrgId();
        Long orgGroupId = org.getOrgGroupId();
        String orgName = org.getOrgName();
        Long updatedBy = org.getUpdatedBy();
        group.setGroupId(orgGroupId);
        group.setCompanyId(orgId);
        group.setGroupName(orgName);
        group.setUpdatedBy(updatedBy);
        this.groupMapper.update(group);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeByIds(List ids) {
        orgMapper.deleteByIds(ids);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeById(Long id) {
        OrgTreeDto org = this.getOrgById(id);
        Long rootId = org.getOrgRootId();
        Integer orgRight = org.getOrgRight();
        Integer orgLeft = org.getOrgLeft();
        int steps = orgRight - orgLeft + 1;
        try {
            orgMapper.decrement("org_left", steps, String.format("org_root_id = %d and org_left  > %d", rootId, orgLeft));
            orgMapper.decrement("org_right", steps, String.format("org_root_id = %d and org_right > %d", rootId, orgRight));
        } catch (Exception e) {
            e.printStackTrace();
        }
        orgMapper.deleteById(id);
    }
    /**
     * @Author: zyj
     * @Description:删除组织机构
     * @Param
     * @Date 8:47 2020/6/4
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateOrgDelFlg(OrgTreeDto orgTreeDto) throws Exception {
        Long orgRootId=orgTreeDto.getOrgRootId();
        Integer orgLeft= orgTreeDto.getOrgLeft();
        Integer orgRight= orgTreeDto.getOrgRight();
        int steps = orgRight - orgLeft + 1;
        orgMapper.decrement("org_left",steps,String.format("org_root_id = %d and org_left  > %d",orgRootId,orgLeft));
        orgMapper.decrement("org_right",steps,String.format("org_root_id = %d and org_right > %d",orgRootId,orgRight));
        orgMapper.updateOrgDelFlg(orgTreeDto);
    }
    /**
     * @Author: zyj
     * @Description:根据id获取组织机构数据
     * @Param
     * @Date 8:58 2020/6/4
     */
    @Override
    public OrgTreeDto getOrgById(Long orgId) {
       OrgTreeDto orgTreeDto= orgMapper.getOrgById(orgId);
        return orgTreeDto;
    }
    /**
     * @Author: zyj
     * @Description:添加管理员
     * @Param
     * @Date 11:18 2020/6/4
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void insertOrgAdmin(User user) {

        Org curOrg = this.orgMapper.findById(user.getCompanyId());
        if(null == curOrg ){
            throw new ScyfException("用户所属机构未注册！");
        }
        user.setUserName("管理员-" +curOrg.getOrgName());
        user.setUserStatus(0);
        user.setUserType(AccountType.ACCOUNT_TYPE_ORG.ordinal());
        user.setUserSex(1);
        //添加账号表
        orgMapper.addUser(user);
        //获取添加账户信息的主键id
        Long userId=user.getUserId();
        //获取企业id
        Long companyId=user.getCompanyId();
        //修改组织管理员id
        this.orgMapper.updateOrgAdmin(companyId, userId);
         //根据id获取组织机构数据
        OrgTreeDto orgTreeDto = this.orgMapper.getOrgById(companyId);
        //新组织id
        Long orgGroupId = orgTreeDto.getOrgGroupId();

        GroupMember groupMember = new GroupMember();
        groupMember.setCompanyId(companyId);
        groupMember.setGroupId(orgGroupId);
        groupMember.setUserId(userId);
        groupMember.setCreatedTime(new Date());
        groupMember.setCreatedBy(user.getCreatedBy());
        groupMember.setDelFlag(0);
        //维护用户与组织关系
        groupMemberMapper.add(groupMember);

        Position position = new Position();
        //定义岗位表要添加的id
        Long positionId = dictService.getTabId("sys_position");
        position.setCompanyId(companyId);
        position.setPositionId(positionId);
        position.setPositionTitle("管理员");
        position.setPositionDesc("机构内的平台管理员帐号");
        position.setPositionOrder("1");
        position.setCreatedBy(user.getCreatedBy());
        position.setCreatedTime(new Date());
        position.setDelFlag(0);
        //维护群组成员与岗位关系
        positionMapper.add(position);

        GroupMemberPosition groupMemberPosition = new GroupMemberPosition();
        groupMemberPosition.setCompanyId(companyId);
        groupMemberPosition.setGroupId(orgGroupId);
        groupMemberPosition.setUserId(userId);
        groupMemberPosition.setPositionId(positionId);
        groupMemberPosition.setCreatedTime(new Date());
        groupMemberPosition.setCreatedBy(user.getCreatedBy());
        groupMemberPosition.setDelFlag(0);
        groupMemberPositionMapper.add(groupMemberPosition);

        if(null != user.getRoleId() && 0l != user.getRoleId().longValue()){
            UserRole userRole = new UserRole();
            userRole.setRoleId(user.getRoleId());
            userRole.setUserId(userId);
            userRole.setCreatedBy(user.getCreatedBy());
            userRole.setCreatedTime(new Date());
            //维护用户与角色关系
            userRoleMapper.add(userRole);
        }
    }
    /**
     * @Author: zyj
     * @Description:给组织添加行业
     * @Param
     * @Date 16:40 2020/6/4
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void insertOrgIndustry(Long orgId, String industryId,Long userId) {
        //添加之前先删除组织之前的行业
        orgIndustryMapper.deleteIndustry(orgId);
        if (StringUtils.isNotBlank(industryId)){
            JSONArray json = JSONObject.parseArray(industryId);
            //创建一个数组对象 长度和json数组一样 即json.size()
            Integer[] a = new Integer[json.size()];
            //然后将之转换成我们需要的数组就好了
            Integer[] array = json.toArray(a);
            Long[] aa=new Long[json.size()];
            for (int i = 0; i < array.length; i++) {
                aa[i]=Long.valueOf(array[i]);
            }
            for (int i = 0; i < aa.length; i++) {
                OrgIndustry orgIndustry=new OrgIndustry();
                Long aLong = aa[i];
                orgIndustry.setOrgId(orgId);
                orgIndustry.setGroupId(orgId);
                orgIndustry.setIndustryId(aLong);
                orgIndustry.setCreatedBy(userId);
                orgIndustry.setCreatedTime(new Date());
                orgIndustry.setDelFlag(0);
                orgIndustryMapper.add(orgIndustry);
            }
        }
    }
    /**
     * @Author: zyj
     * @Description:重置管理员密码
     * @Param
     * @Date 18:11 2020/6/4
     */
    @Override
    public void resetAdminPwd(Long userId,String password,Long companyId,String salt){
        orgMapper.resetAdminPwd(userId,password,companyId,salt);
    }
    /**
     * @Author: zyj
     * @Description:修改组织管理员信息
     * @Param OrgId 企业id  loginName登录名称 userMobile用户电话  userId用户id
     * @Date 14:56 2020/6/8
     */
    @Override
    public void updateOrgAdminName(Integer companyId, String loginName, String userMobile, Integer userId) {
        orgMapper.updateOrgAdminName(companyId,loginName,userMobile,userId);
    }

    @Override
    public List findIndustrySelectByCompanyId(Long orgId) {
        return orgMapper.findIndustrySelectByCompanyId(orgId);
    }

}
