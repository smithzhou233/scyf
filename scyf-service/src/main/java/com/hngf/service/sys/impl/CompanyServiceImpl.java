package com.hngf.service.sys.impl;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.Console;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hngf.common.enums.AccountType;
import com.hngf.common.exception.ScyfException;
import com.hngf.common.utils.Constant;
import com.hngf.common.utils.PageUtils;
import com.hngf.common.utils.ParamUtils;
import com.hngf.dto.sys.CompanyDto;
import com.hngf.entity.scyf.SecureProduc;
import com.hngf.entity.sys.CommonClassify;
import com.hngf.entity.sys.Company;
import com.hngf.entity.sys.Group;
import com.hngf.entity.sys.GroupMember;
import com.hngf.entity.sys.GroupMemberPosition;
import com.hngf.entity.sys.GroupType;
import com.hngf.entity.sys.OrgIndustry;
import com.hngf.entity.sys.Position;
import com.hngf.entity.sys.User;
import com.hngf.entity.sys.UserRole;
import com.hngf.mapper.danger.HiddenMapper;
import com.hngf.mapper.risk.RiskMapper;
import com.hngf.mapper.scyf.SecureProducMapper;
import com.hngf.mapper.sys.CommonClassifyMapper;
import com.hngf.mapper.sys.CompanyMapper;
import com.hngf.mapper.sys.GroupMapper;
import com.hngf.mapper.sys.GroupMemberMapper;
import com.hngf.mapper.sys.GroupMemberPositionMapper;
import com.hngf.mapper.sys.GroupTypeMapper;
import com.hngf.mapper.sys.OrgIndustryMapper;
import com.hngf.mapper.sys.PositionMapper;
import com.hngf.mapper.sys.UserMapper;
import com.hngf.mapper.sys.UserRoleMapper;
import com.hngf.service.danger.InspectDefService;
import com.hngf.service.danger.InspectTypeService;
import com.hngf.service.risk.RiskCtrlLevelService;
import com.hngf.service.score.ScoreModelService;
import com.hngf.service.sys.CompanyService;
import com.hngf.service.sys.DictService;
import com.hngf.service.sys.EvaluateIndexService;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service("CompanyService")
public class CompanyServiceImpl implements CompanyService {

    @Autowired
    private CompanyMapper companyMapper;
    @Autowired
    private SecureProducMapper secureProducMapper;
    @Autowired
    private GroupMapper groupMapper;
    @Autowired
    private OrgIndustryMapper orgIndustryMapper;
    @Autowired
    private ScoreModelService scoreModelService;
    @Autowired
    private EvaluateIndexService evaluateIndexService;
    @Autowired
    private InspectTypeService inspectTypeService;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private GroupMemberMapper groupMemberMapper;
    @Autowired
    private DictService dictService;
    @Autowired
    private PositionMapper positionMapper;
    @Autowired
    private GroupMemberPositionMapper  groupMemberPositionMapper;

    @Autowired
    private UserRoleMapper userRoleMapper;
    @Autowired
    private RiskMapper riskMapper;
    @Autowired
    private HiddenMapper hiddenMapper;

    @Autowired
    private GroupTypeMapper GroupTypeMapper;

    @Autowired
    private InspectDefService inspectDefService;
    @Autowired
    private RiskCtrlLevelService riskCtrlLevelService;
    @Autowired
    private CommonClassifyMapper commonClassifyMapper;


    @Value("${scyf.riskPoints}")
    private String riskPoints;
    @Value("${scyf.securityPoints}")
    private String securityPoints;
    /**
     * 查询企业基础信息列表
     * @param params
     * @return
     */
    @Override
    public List<CompanyDto> findList(Map<String, Object> params) {
        return companyMapper.findList(params);
    }

    /**
     * 回显企业信息
     * @param id
     * @return
     */
    @Override
    public List<Map<String, Object>> getById(Long id){
        List<Map<String, Object>> list = companyMapper.findById(id);
//        if(null != list && list.size() >0 ){
//            Map<String, Object> map = list.get(0);
//            String evaluateType = evaluateIndexService.getEvaluateTypeByCompanyId(id);
//            map.put("evaluateType",null == evaluateType?"":evaluateType);
//        }
        return list;
    }



    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(Company company, SecureProduc secureProduc) throws Exception {
        if(null == company.getCompanyId() || null == secureProduc.getCompanyId() ){
            throw new ScyfException("companyId为必传项");
        }
        company.setUpdatedTime(new Date());
        companyMapper.update(company);
        secureProduc.setUpdatedTime(new Date());
        secureProducMapper.update(secureProduc);
        Long companyId = company.getCompanyId();
        Long companyGroupId = company.getCompanyGroupId();
        String companyName = company.getCompanyName();
        Long updatedBy = company.getUpdatedBy();
        Group group = new Group();
        if(null == companyGroupId || 0 == companyGroupId.intValue() ){
            Long groupIdByCompanyId = companyMapper.findCompanyGroupIdByCompanyId(company.getCompanyId());
            group.setGroupId(groupIdByCompanyId);
        }else{
            group.setGroupId(companyGroupId);
        }
        Long currentCompanyGroupId = group.getGroupId();
        if(null != currentCompanyGroupId && 0 != currentCompanyGroupId.intValue()){
            // 确定企业群组的ID存在
            group.setCompanyId(companyId);
            group.setGroupName(companyName);
            group.setUpdatedBy(updatedBy);
            group.setUpdatedTime(new Date());
            groupMapper.update(group);
        }
        //更新评价指标
//        this.evaluateIndexService.deleteBatchByCompanyId(companyId,updatedBy );
//        this.evaluateIndexService.initEvaluate(companyId,company.getEvaluateType());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeByIds(List ids) {
        companyMapper.deleteByIds(ids);
    }

    /**
     * 单条删除企业信息
     * yfh
     * 2020/06/08
     * @param companyId
     * @throws Exception
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeById(Long companyId,Long updatedBy) throws Exception {
        // 增加删除条件 add by lxf 2020-09-09 17:00
        List<Map<String, Object>> mapList = companyMapper.selectPro(companyId.toString());
        if(null != mapList && mapList.size() >0 ){
            throw new ScyfException("此集团公司下含有子公司，不能删除！");
        }
        Company company = companyMapper.getCompanyId(companyId);
        Long rootId = company.getCompanyRootId();
        Integer companyRight = company.getCompanyRight();
        Integer companyLeft = company.getCompanyLeft();
        int steps = companyRight - companyLeft + 1;
        companyMapper.decrement("company_left", steps, String.format("company_root_id = %d and company_left  > %d", rootId, companyLeft));
        companyMapper.decrement("company_right", steps, String.format("company_root_id = %d and company_right > %d", rootId, companyRight));
        companyMapper.deleteById(companyId, updatedBy);
        secureProducMapper.deleteById(companyId);
        // 删除企业关联的 管理员 add by lxf 2020-09-09 17:00
        userRoleMapper.deleteByUserId(company.getCompanyAdminId());
        userMapper.deleteById(company.getCompanyAdminId());
        // 删除企业关联的 企业分组 add by lxf 2020-09-09 17:00
        groupMapper.deleteByCompanyId(companyId, updatedBy);
        positionMapper.deleteByCompanyId(companyId, updatedBy);
        groupMemberMapper.deleteByCompanyId(companyId, updatedBy);
        groupMemberPositionMapper.deleteByCompanyId(companyId, updatedBy);
        // 删除 企业关联的 评价指标 add by lxf 2020-09-10
        this.evaluateIndexService.deleteBatchByCompanyId(companyId,updatedBy );
        // 删除 企业关联的 绩效考核模式配置
        this.scoreModelService.deleteBatchByCompanyId(companyId, updatedBy);
        // 删除 企业关联的 隐患巡查5大类
        this.inspectTypeService.deleteBatchByCompanyId(companyId, updatedBy);
        // 删除 企业关联的 检查定义表-检查表-安全检查表 信息
        this.inspectDefService.deleteBatchByCompanyId(companyId, updatedBy);
        // 删除 企业关联的 风险管控信息
        this.riskCtrlLevelService.deleteBatchByCompanyId(companyId, updatedBy);
        // 删除企业的 任务检查类型配置
        this.commonClassifyMapper.deleteBatchByCompanyId(companyId,updatedBy);
    }

    /**
     * 三级联动查询省市县
     * yfh
     * 2020/05/25
     * @param parentId
     * @return
     */
    @Override
    public List<Map<String, Object>> selectPro(String parentId) {
        return companyMapper.selectPro(parentId);
    }

    /**
     * 保存企业信息
     * yfh
     * 2020/06/05
     * @param company
     * @param secureProduc
     * @throws Exception
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(Company company, SecureProduc secureProduc) throws Exception {
        String evaluateType = company.getEvaluateType() ;
        if(null == evaluateType || StringUtils.isBlank(evaluateType)){
            throw new ScyfException("评价方式不能为空");
        }
        Long companyParentId = company.getCompanyParentId();
        if(null == companyParentId ){
            company.setCompanyParentId(0L);
        }
        companyMapper.insertCompany(company);//新增一条企业信息 为获取companyId
        Long companyId =company.getCompanyId();
        String companyName = company.getCompanyName();
        if ( 0 == company.getCompanyParentId().longValue() ) {
            company.setCompanyLeft(1);
            company.setCompanyRight(2);
            company.setCompanyRootId(companyId);
        } else {
            companyParentId = company.getCompanyParentId();
            Company companyParent = this.companyMapper.getCompanyById(companyParentId);
            Long rootId = companyParent.getCompanyRootId();
            int nLeft = companyParent.getCompanyRight();
            this.companyMapper.increment("company_right", 2, String.format("company_root_id = %d and company_right >= %d", rootId, nLeft));
            this.companyMapper.increment("company_left", 2, String.format("company_root_id = %d and company_left  >= %d", rootId, nLeft));
            company.setCompanyRootId(rootId);
            company.setCompanyLeft(nLeft);
            company.setCompanyRight(nLeft + 1);
        }
        this.companyMapper.update(company);

        secureProduc.setCompanyId(companyId);
        if(null == secureProduc.getMajorName()){
            secureProduc.setMajorName( "");
        }
        if(null == secureProduc.getMajorTelephone()){
            secureProduc.setMajorTelephone( "");
        }
        if(null == secureProduc.getMajorPhone()){
            secureProduc.setMajorPhone( "");
        }
        if(null == secureProduc.getSecureName()){
            secureProduc.setSecureName( "");
        }
        if(null == secureProduc.getSecureTelephone()){
            secureProduc.setSecureTelephone( "");
        }
        if(null == secureProduc.getSecurePhone()){
            secureProduc.setSecurePhone( "");
        }
        if(null == secureProduc.getSecureSetting()){
            secureProduc.setSecureSetting( 0);
        }
        if(null == secureProduc.getProductionAddress()){
            secureProduc.setProductionAddress( "");
        }
        if(null == secureProduc.getScaleCase()){
            secureProduc.setScaleCase( 0);
        }
        if(null == secureProduc.getCompanyScale()){
            secureProduc.setCompanyScale( 0);
        }
        if(null == secureProduc.getSuperviseClassify()){
            secureProduc.setSuperviseClassify( "");
        }
        if(null == secureProduc.getHiddenCheckGovern()){
            secureProduc.setHiddenCheckGovern("");
        }
        if(null == secureProduc.getHiddenCheckPlan()){
            secureProduc.setHiddenCheckPlan("");
        }
        this.secureProducMapper.add(secureProduc);

        // 根据企业Id：companyId增加企业部门类型
        // 增加 企业默认的部门类型信息：公司、分公司、车间、班组
        GroupType groupType = new GroupType();
        groupType.insertPrefix(company.getCreatedBy());
        groupType.setCompanyId(companyId);
        groupType.setGroupTypeTitle("公司");
        groupType.setGroupTypeDesc("公司");
        GroupTypeMapper.add(groupType);

        addBatchGroupType(companyId, company.getCreatedBy());

        Group group = new Group();
        group.setCompanyId(companyId);
        group.setGroupName(companyName);
        group.setGroupParent(0L);
        //组织部门默认公司，用户自己维护
        group.setGroupTypeId(groupType.getGroupTypeId());
        group.setGroupLeft(1);
        group.setGroupRight(2);
        group.setGroupLevel(1);
        group.insertPrefix(company.getCreatedBy());
        this.groupMapper.add(group);
        Long comGroupId=group.getGroupId();
        //根据companyId修改企业表companyGroupId
        this.companyMapper.updateComGroupId(comGroupId,companyId);

        //初始化绩效考核模式配置
        this.scoreModelService.initSetting(companyId);
        //新增评价指标
        this.evaluateIndexService.initEvaluate(company.getCreatedBy(),companyId,evaluateType);
        //默认增加隐患巡查5大类
        inspectTypeService.initScheduleCheckTypeSetting(companyId);

        //添加 检查定义表-检查表-安全检查表 信息
        inspectDefService.initBizCheckDef(companyId);
        Map<String, Object> map = new HashMap();
        map.put("companyId", company.getCompanyId());
        map.put("createBy", company.getCreatedBy());
        riskCtrlLevelService.initRiskCtrlLevel(map);
        //任务检查类型配置
        initCommonClassify(companyId,company.getCreatedBy());

    }

    /**
     * 添加企业的时候初始化默认的 任务检查类型配置
     */
    private int initCommonClassify(Long companyId, Long createdBy ){
        List<CommonClassify> commonClassifyList = new ArrayList<>(8);
        String[] typeArray = {"专业性检查","日常检查","节假日前后" ,"事故类比检查" ,"季节性检查" ,"综合性检查"};
        for(int i=0;i<typeArray.length;i++){
            CommonClassify commonClassify = new CommonClassify(companyId, createdBy,4);
            commonClassify.setClassifyName(typeArray[i]);
            commonClassify.setClassifyDesc(typeArray[i]);
            commonClassify.setClassifyValue(String.valueOf(i+1));
            commonClassify.setSortNo(i+1);
            commonClassifyList.add(commonClassify);
        }
        return this.commonClassifyMapper.addBatch(commonClassifyList);
    }

    /**
     * 批量插入企业的部门默认分类信息 add by lxf 2020/08/18 14:00
     * @param companyId
     * @param createdBy
     */
    private void addBatchGroupType(Long companyId, Long createdBy ){
        List<GroupType> groupTypeList = new ArrayList<>(4);
        for(int i=1;i<4 ;i++){
            GroupType groupType = new GroupType();
            groupType.insertPrefix(createdBy);
            groupType.setCompanyId(companyId);
            switch (i){
                case 0:
                    groupType.setGroupTypeTitle("公司");
                    groupType.setGroupTypeDesc("公司");
                    break;
                case 1:
                    groupType.setGroupTypeTitle("分公司");
                    groupType.setGroupTypeDesc("分公司");
                    break;
                case 2:
                    groupType.setGroupTypeTitle("车间");
                    groupType.setGroupTypeDesc("车间");
                    break;
                case 3:
                    groupType.setGroupTypeTitle("班组");
                    groupType.setGroupTypeDesc("班组");
                    break;
                default:
                    break;
            }
            groupTypeList.add(groupType);
        }
        this.GroupTypeMapper.addBatch(groupTypeList);
    }

    /**
     * 查看企业经济类型子类
     * yfh
     * 2020/06/05
     * @return
     */
    @Override
    public  List<Map<String,Object>> queryEconomicType(Integer dictId,String dictType) {
        return companyMapper.queryEconomicType(dictId,dictType);
    }

    /**
     * 查看企业经济类型父类
     * yfh
     * 2020/06/05
     * @param dictType
     * @param ownerId
     * @return
     */
    @Override
    public List<Map<String, Object>> queryEconomicTypeParent(String dictType, String ownerId) {
        return companyMapper.queryEconomicTypeParent(dictType,ownerId);
    }

    /**
     * 企业规模查询
     * yfh
     * 2020/06/05
     * @return
     */
    @Override
    public List<Map<String, Object>> queryScale(String dictType) {
        return companyMapper.queryScale(dictType);
    }

    /**
     *查看企业是否存在
     * yfh
     * 2020/06/08
     * @param companyId
     * @return
     */
    @Override
    public Company getCompanyId(Long companyId) {
        return companyMapper.getCompanyId(companyId);
    }

    /**
     *  新增管理员账号信息
     *  yfh
     *  2020/06/09
     * @param user
     * @param roleId
     */
    @Override
    public void insertCompanyAdmin(User user, Long roleId) {
        Long companyId = user.getCompanyId();
        Company company = companyMapper.getCompanyId(companyId);
        if (null == company ) {
            throw new ScyfException("用户所属企业未注册！");
        }
        user.setUserName("管理员-" + company.getCompanyName());
        Long createdBy = user.getCreatedBy();
        user.setUserStatus(0);
        if (company.getCompanyTypeId().equals(0)) {
            user.setUserType(AccountType.ACCOUNT_TYPE_COMPANY.ordinal());
        } else {
            user.setUserType(AccountType.ACCOUNT_TYPE_GROUP.ordinal());
        }
        userMapper.add(user);
        Long userId = user.getUserId();
        companyMapper.updateCompanyAdmin(companyId, userId);
        Long companyGroupId = company.getCompanyGroupId();
        GroupMember groupMember = new GroupMember();
        groupMember.setCompanyId(companyId);
        groupMember.setGroupId(companyGroupId);
        groupMember.setUserId(userId);
        groupMember.setCreatedTime(new Date());
        groupMember.setCreatedBy(createdBy);
        groupMember.setDelFlag(0);
        groupMemberMapper.add(groupMember);
        Position position = new Position();
        Long positionId = dictService.getTabId("sys_position");
        position.setCompanyId(companyId);
        position.setPositionId(positionId);
        position.setPositionTitle("公司管理员");
        position.setPositionDesc("公司内的平台管理员帐号");
        position.setPositionOrder("1");
        position.setCreatedBy(createdBy);
        position.setCreatedTime(new Date());
        position.setDelFlag(0);
        positionMapper.add(position);
        GroupMemberPosition groupMemberPosition = new GroupMemberPosition();
        groupMemberPosition.setCompanyId(companyId);
        groupMemberPosition.setGroupId(companyGroupId);
        groupMemberPosition.setUserId(userId);
        groupMemberPosition.setPositionId(positionId);
        groupMemberPosition.setCreatedTime(new Date());
        groupMemberPosition.setCreatedBy(createdBy);
        groupMemberPosition.setDelFlag(0);
        groupMemberPositionMapper.add(groupMemberPosition);
        UserRole userRole=new UserRole();
        if (company.getCompanyTypeId().equals(0)) {
            if (roleId != null) {
                userRole.setRoleId((long)roleId);
            } else {
                userRole.setRoleId(4L);
            }
        } else if (roleId != null) {
            userRole.setRoleId((long)roleId);
        } else {
            userRole.setRoleId(6L);
        }
        userRole.setUserId(userId);
        userRole.setDelFlag(0);
        userRoleMapper.add(userRole);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void insertCompanyAdmin(User user) {
        this.insertCompanyAdmin(user, user.getRoleId());
    }

    @Override
    public PageUtils companyScore(Map<String,Object> params, int pageNum, int pageSize, String order) {
        if(StringUtils.isNotBlank(order)){
            PageHelper.startPage(pageNum, pageSize,order);
        }else{
            PageHelper.startPage(pageNum, pageSize);
        }
        List<Map<String, Object>> list = companyMapper.findCompanyScore(params);
        if (null != list && list.size() > 0) {
            list = this.getCompanySafeAndRiskScore(list);
        }
        PageInfo<Map<String, Object>> pageInfo = new PageInfo<>(list);
        return new PageUtils(list,(int)pageInfo.getTotal(),pageInfo.getPages(),pageInfo.getPageSize(),pageInfo.getPrePage() + 1);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteCompanyWithCascade(Long companyId, Long userId) throws Exception {
        if (companyId != null && userId != null) {
            Company company = this.companyMapper.getCompanyId(companyId);
            Long cRootId = company.getCompanyRootId();
            Integer cLeft = company.getCompanyLeft();
            Integer cRight = company.getCompanyRight();
            String cIds = this.companyMapper.findCompanyIdsWithSub(cRootId);
            if (!companyId.equals(company.getCompanyParentId())) {
                int steps = cRight - cLeft + 1;
                this.companyMapper.decrement("company_left", steps, String.format("company_root_id = %d and company_left  > %d", cRootId, cLeft));
                this.companyMapper.decrement("company_right", steps, String.format("company_root_id = %d and company_right > %d", cRootId, cRight));
            }

            this.companyMapper.deleteCompanys(cIds);
            this.secureProducMapper.deleteWithCompanyIds(cIds);
            this.userMapper.deleteByCompanyIds(cIds);
            this.groupMapper.deleteByCompanyIds(cIds);
        } else {
            throw new ScyfException("级联删除集团、企业信息时->参数输入异常：companyId=" + companyId + ",userId=" + userId);
        }
    }

    @Override
    public PageUtils querySingleOrgs(Map<String, Object> params, int pageNum, int pageSize, String order) {
        if(StringUtils.isNotBlank(order)){
            PageHelper.startPage(pageNum, pageSize,order);
        }else{
            PageHelper.startPage(pageNum, pageSize);
        }
        List<Company> list = companyMapper.findSingleOrgs(params);
        PageInfo<Company> pageInfo = new PageInfo<>(list);
        return new PageUtils(list,(int)pageInfo.getTotal(),pageInfo.getPages(),pageInfo.getPageSize(),pageInfo.getPrePage() + 1);
    }

    @Override
    public CompanyDto getCompanyById(Long companyId) {
        CompanyDto companyDto = companyMapper.findCompanyById(companyId);
//        String evaluateType = evaluateIndexService.getEvaluateTypeByCompanyId(companyId);
//        companyDto.setEvaluateType(null == evaluateType?"":evaluateType);
        return companyDto;
    }

    @Override
    public List<CompanyDto> queryCompanyListForTreeTable(Long companyRootId) {
        return companyMapper.selectCompanyListForTreeTable(companyRootId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addExistOrg2Group(Map<String, Object> params) {
        Long selectedCId = Convert.toLong(params.get("selectedCompanyId"));
        if (selectedCId == null) {
            throw new ScyfException("参数异常：selectedCId为空。。。");
        } else {
            String selectedOrgIds = (String)params.get("selectedOrgIds");
            if (StringUtils.isBlank(selectedOrgIds)) {
                throw new ScyfException("参数异常：selectedOrgIds为空。。。");
            } else {
                String[] orgIds = selectedOrgIds.split(",");
                if (orgIds.length == 0) {
                    throw new ScyfException("参数异常：selectedOrgIds=" + selectedOrgIds);
                } else {
                    Company companyParent = this.companyMapper.getCompanyId(selectedCId);
                    if (companyParent == null) {
                        throw new ScyfException("获取集团为空：companyId=" + selectedCId);
                    } else {
                        Long rootId = companyParent.getCompanyRootId();
                        String[] orgArry = orgIds;

                        for(int i = 0; i < orgArry.length; ++i) {
                            String cIdStr = orgArry[i];
                            Map<String, Object> param = new HashMap();
                            param.put("companyId", cIdStr);
                            param.put("rootId", rootId);
                            param.put("parentId", selectedCId);
                            this.companyMapper.addExistOrg2Group(param);
                        }

                    }
                }
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int setDeployment(Map<String, Object> params) {
        return companyMapper.updateDeployment(params);
    }

    private List<Map<String, Object>> getCompanySafeAndRiskScore(List<Map<String, Object>> list) {
        Map<String, Object> rp = (Map) JSONArray.parseObject(this.riskPoints, Map.class);
        Map<String, Object> hd = (Map)JSONArray.parseObject(this.securityPoints, Map.class);
        list.forEach((item) -> {
            Integer totalDangerCount = Integer.parseInt(item.get("totalHiddenCount").toString());
            Integer finishDangerCount = Integer.parseInt(item.get("finishHiddenCount").toString());
            if (totalDangerCount > 0 && finishDangerCount > 0) {
                int finishRate = (new BigDecimal(finishDangerCount * 100)).divideToIntegralValue(new BigDecimal(totalDangerCount)).intValue();
                item.put("finishRate", finishRate);
            } else {
                item.put("finishRate", 0);
            }

            Integer w1 = Integer.parseInt(item.get("oneLevel").toString());
            Integer w2 = Integer.parseInt(item.get("twoLevel").toString());
            Integer w3 = Integer.parseInt(item.get("threeLevel").toString());
            Integer w4 = Integer.parseInt(item.get("fourLevel").toString());
            Integer w = Integer.parseInt(item.get("totalLevel").toString());
            if (w == 0) {
                item.put("riskScore", 0);
            } else {
                item.put("riskScore", this.getRiskScore(w1, w2, w3, w4, rp));
            }

            Integer y1 = Integer.parseInt(item.get("oneHiddenLevel").toString());
         /*   Integer y2 = Integer.parseInt(item.get("twoHiddenLevel").toString());*/
            Integer y3 = Integer.parseInt(item.get("threeHiddenLevel").toString());
       /*     Integer y4 = Integer.parseInt(item.get("fourHiddenLevel").toString());*/
           // item.put("safeScore", this.getSafeScore(y1, y2, y3, y4, hd));
            item.put("safeScore", this.getSafeScore(y1, y3,  hd));
        });
        return list;
    }

    private Integer getRiskScore(Integer w1, Integer w2, Integer w3, Integer w4, Map<String, Object> rp) {
        Integer rp1 = Convert.toInt(rp.get("rp1"));
        Integer rp2 = Convert.toInt(rp.get("rp2"));
        Integer rp3 = Convert.toInt(rp.get("rp3"));
        Integer rp4 = Convert.toInt(rp.get("rp4"));
        int riskScore = w1 * rp1 + w2 * rp2 + w3 * rp3 + w4 * rp4;
        return riskScore;
    }

    private Integer getSafeScore(Integer y1,   Integer y3,   Map<String, Object> hd) {
        Integer hd1 = Convert.toInt(hd.get("hd1"));
     /*   Integer hd2 = Convert.toInt(hd.get("hd2"));*/
        Integer hd3 = Convert.toInt(hd.get("hd3"));
       /* Integer hd4 = Convert.toInt(hd.get("hd4"));*/
       /* int riskScore = y1 * hd1 + y2 * hd2 + y3 * hd3 + y4 * hd4;*/
        int riskScore = y1 * hd1  + y3 * hd3  ;
        return riskScore;
    }


    @Override
    public   List<Map<String, Object>>  queryTotalForMainPage(Map<String, Object> params) {
        Map<String, Object> params1 = (Map) JSON.parseObject(this.riskPoints, Map.class);
        Map<String, Object> params2 = (Map)JSON.parseObject(this.securityPoints, Map.class);
        if(params.get("cId")!=null){
            String cid  =params.get("cId").toString();
            String cIds = this.companyMapper.findCompanyIdsByCId(Long.parseLong(cid));
            params.put("cIds", cIds);
        }
        params.putAll(params1);
        params.putAll(params2);
        List<Map<String, Object>>  list =  companyMapper.selectTotalForMainPage(params);
        return list;
    }

    public Map<String, Object> queryCompanyListForBigScreen(Map<String, Object> params) {
        Map<String, Object> result = new HashMap();
        Long cId = Convert.toLong(params.get("companyId"));
        Map<String, Object> map = this.queryComRootIdAndLeftRight(cId);
        if (map == null) {
            return result;
        } else {
            params.putAll(map);
            List<Map<String, Object>> list = this.companyMapper.findCompanyListForBigScreen(params);
            Map<String, Object> rp = (Map)JSONArray.parseObject(this.riskPoints, Map.class);
            Map<String, Object> hd = (Map)JSONArray.parseObject(this.securityPoints, Map.class);
            String cIds = this.companyMapper.findCompanyIdsByCId(ParamUtils.paramsToLong(params,"companyId"));
            rp.putAll(hd);
            rp.put("companyIds", cIds);
            rp.put("orgCount", list.size());
            Map<String, Object> pointMap = this.riskMapper.findTotalRiskPointAndSecurityPointForGent(rp);
            result.put("companyName", map.get("companyName"));
            result.put("companyList", list);
            result.putAll(pointMap);
            return result;
        }
    }

    @Override
    public Map<String, Object> queryRiskCountForGent(Map<String, Object> params) {
        Map<String, Object> result = new HashMap();
        Long companyId = ParamUtils.paramsToLong(params, "companyId");
        Map<String, Object> map = this.queryComRootIdAndLeftRight(companyId);
        if (map == null) {
            return result;
        } else {
            String cIds = this.companyMapper.findCompanyIdsByCId(companyId);
            List<Map<String, Object>> list = this.riskMapper.findRiskCountForGent(cIds);
            Integer total = 0;

            Integer count;
            for(Iterator var8 = list.iterator(); var8.hasNext(); total = total + count) {
                Map<String, Object> mapInList = (Map)var8.next();
                if(null!=mapInList.get("riskLevel")  ){
                    if(("1").equals(mapInList.get("riskLevel").toString())){
                        mapInList.put("color","#FF0000");
                        mapInList.put("name","重大");
                    }else if(("2").equals(mapInList.get("riskLevel").toString())){
                        mapInList.put("color","#FF6100");
                        mapInList.put("name","较大");
                    }else if(("3").equals(mapInList.get("riskLevel").toString())){
                        mapInList.put("color","#FFFF00");
                        mapInList.put("name","一般");
                    }else if(("4").equals(mapInList.get("riskLevel").toString())){
                        mapInList.put("color","#0000FF");
                        mapInList.put("name","较低");
                    }
                    mapInList.put("value",mapInList.get("ristCount"));
                }
                count = Convert.toInt(mapInList.get("ristCount"));
            }

            result.put("list", list);
            result.put("total", total);
            return result;
        }
    }

    @Override
    public Map<String, Object> queryHiddenDangerCountForGent(Map<String, Object> params) {
        Map<String, Object> result = new HashMap();
        Long companyId = ParamUtils.paramsToLong(params, "companyId");
        Map<String, Object> map = this.queryComRootIdAndLeftRight(companyId);
        if (map == null) {
            return result;
        } else {
            String cIds = this.companyMapper.findCompanyIdsByCId(companyId);
            Map<String, Object> hdCountMap = this.hiddenMapper.findHiddenDangerCountForGent(cIds);
            Map<String, Object> hdStatusCountAndRateMap = this.hiddenMapper.findHiddenDangerCountAndRateForGent(cIds);
            result.putAll(hdCountMap);
            result.putAll(hdStatusCountAndRateMap);
            return result;
        }
    }

    @Override
    public Map<String, Object> queryRiskCountOfOutOfControlForGent(Map<String, Object> params) {
        Map<String, Object> result = new HashMap();
        Long companyId = ParamUtils.paramsToLong(params, "companyId");
        Map<String, Object> map = this.queryComRootIdAndLeftRight(companyId);
        if (map == null) {
            return result;
        } else {
            String cIds = this.companyMapper.findCompanyIdsByCId(companyId);
            List<Map<String, Object>> list = this.riskMapper.findRiskCountOfOutOfControlForGent(cIds);
            Integer total = 0;

            Map ele;
            for(Iterator iterator = list.iterator(); iterator.hasNext(); total = total + ParamUtils.paramsToInteger(ele,"hiddenCount")) {
                ele = (Map)iterator.next();
            }

            result.put("total", total);
            result.put("list", list);
            return result;
        }
    }

    @Override
    public List<Map<String, Object>> queryHiddenForGent(Map<String, Object> params) {
        Long companyId = ParamUtils.paramsToLong(params, "companyId");
        Map<String, Object> map = this.queryComRootIdAndLeftRight(companyId);
        if (map == null) {
            return new ArrayList();
        } else {
            String cIds = this.companyMapper.findCompanyIdsByCId(companyId);
            List<Map<String, Object>> list = this.hiddenMapper.findHiddenListForGent(cIds);
            return list;
        }
    }

    @Override
    public List<Map<String, Object>> queryRiskPointForGent(Map<String, Object> params) {
        Long companyId = ParamUtils.paramsToLong(params, "companyId");
        Map<String, Object> map = this.queryComRootIdAndLeftRight(companyId);
        if (map == null) {
            return new ArrayList();
        } else {
            Map<String, Object> map1 = (Map)JSONArray.parseObject(this.riskPoints, Map.class);
            String companyIds = this.companyMapper.findCompanyIdsByCId(companyId);
            map1.put("companyIds", companyIds);
            List<Map<String, Object>> list = this.riskMapper.findRiskPointForGent(map1);
            return list;
        }
    }

    @Override
    public List<Map<String, Object>> queryHiddenPointForGent(Map<String, Object> params) {
        Long companyId = ParamUtils.paramsToLong(params, "companyId");
        Map<String, Object> map = this.queryComRootIdAndLeftRight(companyId);
        if (map == null) {
            return new ArrayList();
        } else {
            Map<String, Object> map1 = (Map)JSONArray.parseObject(this.securityPoints, Map.class);
            String companyIds = this.companyMapper.findCompanyIdsByCId(companyId);
            map1.put("companyIds", companyIds);
            List<Map<String, Object>> list = this.hiddenMapper.findHiddenPointForGent(map1);
            return list;
        }
    }

    @Override
    public List<Map<String, Object>> queryGroupPositionAndLvl(Map<String, Object> params) {
        List<Map<String, Object>> glist = this.companyMapper.selectGroupPositionAndLvl(params);
        if(null != glist && glist.size() >0 ){
            for(Map<String,Object> map : glist ){
                Object maxLevelObj = map.get("maxlvl");
                if(null != maxLevelObj ){
                    params.put("riskPointLevel",maxLevelObj.toString());
                    params.put("groupId", map.get("groupId"));
                    List<Map<String, Object>> mapList = this.companyMapper.selectGroupPositionAndLvlPointMap(params);
                    if(null != mapList && mapList.size()>0 ){
                        Set<Map.Entry<String, Object>> entrySet = mapList.get(0).entrySet();
                        Iterator<Map.Entry<String, Object>> iterator = entrySet.iterator();
                        while (iterator.hasNext()){
                            Map.Entry<String, Object> entry = iterator.next();
                            map.put(entry.getKey(),entry.getValue());
                        }
                    }
                }
                if(!map.containsKey("riskPointId")){
                    map.put("riskPointId",null);
                    map.put("mapId",null);
                }
            }
        }
        return glist;
    }

    @Override
    public Map<String, Object> getOrgRiskAnalysis(Long orgId) {
        Map<String, Object> resultMap = new HashMap<>();
        //查询机构下的子机构code
        List<Map<String, Object>>  rlist =this.orgIndustryMapper.getOrgRiskAnalysis(orgId);
        List<Long> industryIds = new ArrayList<>();
        List<String> industryCodes =new ArrayList<>();
        Integer totalCount = 0;
        Integer four;
        Integer three;
        if (rlist != null && rlist.size() > 0) {
            Iterator var8 = rlist.iterator();
            Map map;
            while(var8.hasNext()) {
                map = (Map)var8.next();
                industryIds.add(Long.parseLong(map.get("industryId").toString()));
                String industryCode = map.get("industryCode").toString();
                industryCodes.add(industryCode);
                four = this.companyMapper.getCompanyCountByIndustryCode(industryCode);
                totalCount = totalCount + four;
                map.put("count", four);
            }
            var8 = rlist.iterator();
            while(var8.hasNext()) {
                map = (Map)var8.next();
                if (totalCount > 0) {
                    three = (Integer)map.get("count");
                    map.put("rate", (new BigDecimal(three * 100)).divide(new BigDecimal(totalCount), 0, RoundingMode.HALF_UP).intValue() + "%");
                } else {
                    map.put("rate", "0%");
                }
            }
        }
        resultMap.put("companyNumber", rlist);
        resultMap.put("totalCount", totalCount);

        List<OrgIndustry>  orgIndustryList = this.orgIndustryMapper.findListByOrgId(orgId);
        List<Map<String, Object>> companyData =new ArrayList<>();
        Integer threeLevel = 0;
        Integer threeLevelFinish = 0;
        Integer oneLevel = 0;
        Integer oneLevelFinish = 0;
        Integer one = 0;
        Integer two = 0;
        three = 0;
        four = 0;
        BigDecimal riskScore = new BigDecimal(0);
        BigDecimal safeScore = new BigDecimal(0);
        Map<String,Object> params  =new  HashedMap();
        if (null != orgIndustryList && orgIndustryList.size() > 0) {
            Iterator var18 = orgIndustryList.iterator();
            label51:
            while(true) {
                while(true) {
                    if (!var18.hasNext()) {
                        break label51;
                    }
                    OrgIndustry orgIndustry = (OrgIndustry)var18.next();
                    Map<String, Object> data = new HashMap();
                    ((Map)data).put("industryName", orgIndustry.getIndustryName());
                    Long industryId = orgIndustry.getIndustryId();
                    int index = industryIds.indexOf(industryId);
                    Map dataByIndustry;
                    if (index != -1) {
                        params.put("industryCode",(String)industryCodes.get(index));
                        Map<String, Object> rp = (Map) JSONArray.parseObject(this.riskPoints, Map.class);
                        Map<String, Object> hd = (Map)JSONArray.parseObject(this.securityPoints, Map.class);
                        params.putAll(rp);
                        params.putAll(hd);
                        dataByIndustry = this.companyMapper.getCompanyDataByIndustryCode(params);
                        if (null != dataByIndustry && !dataByIndustry.isEmpty()) {
                            dataByIndustry.remove("industryName");
                            ((Map)data).putAll(dataByIndustry);
                            one = one + Integer.parseInt(dataByIndustry.get("one").toString());
                            two = two + Integer.parseInt(dataByIndustry.get("two").toString());
                            three = three + Integer.parseInt(dataByIndustry.get("three").toString());
                            four = four + Integer.parseInt(dataByIndustry.get("four").toString());
                            oneLevel = oneLevel + Integer.parseInt(dataByIndustry.get("oneDangerLevel").toString());
                            oneLevelFinish = oneLevelFinish + Integer.parseInt(dataByIndustry.get("oneDangerLevelFinish").toString());
                            dataByIndustry.put("oneHiddenLevelNoComplete",oneLevel-oneLevelFinish);

                            threeLevel = threeLevel + Integer.parseInt(dataByIndustry.get("threeDangerLevel").toString());
                            threeLevelFinish = threeLevelFinish + Integer.parseInt(dataByIndustry.get("threeDangerLevelFinish").toString());
                            dataByIndustry.put("threeHiddenLevelNoComplete",threeLevel-threeLevelFinish);
                            safeScore=safeScore.add(new BigDecimal(dataByIndustry.get("safeScore").toString()));
                            riskScore= riskScore.add(new BigDecimal(dataByIndustry.get("riskScore").toString()));


                        } else {
                            data = this.initPackageData((Map)data);
                        }
                        companyData.add(data);
                    } else {
                        params.put("industryCode",orgIndustry.getIndustryCode());
                        dataByIndustry = this.companyMapper.getCompanyDataByIndustryCode(params);
                        if (null != dataByIndustry && !dataByIndustry.isEmpty()) {
                            dataByIndustry.remove("industryName");
                            ((Map)data).putAll(dataByIndustry);
                        } else {
                            data = this.initPackageData((Map)data);
                        }
                        companyData.add(data);
                    }
                }
            }
        }
        resultMap.put("companyData", companyData);
        resultMap.put("one", one);
        resultMap.put("two", two);
        resultMap.put("three", three);
        resultMap.put("four", four);
        resultMap.put("oneLevel", oneLevel);
        resultMap.put("oneLevelFinish", oneLevelFinish);
        resultMap.put("threeLevel", threeLevel);
        resultMap.put("threeLevelFinish", threeLevelFinish);
        resultMap.put("safeScore",safeScore);
        resultMap.put("riskScore",riskScore);
        return resultMap;
    }

    @Override
    public PageUtils findListByindustryTypeCode(Map<String, Object> params) {
        int pageNum = null!= params.get("pageNum") ? Integer.parseInt(params.get("pageNum").toString()) : 1;
        int pageSize = null != params.get("pageSize") ? Integer.parseInt(params.get("pageSize").toString()) : Constant.PAGE_SIZE;
        //利用PageHelper分页查询 注意：这个一定要放查询语句的前一行,否则无法进行分页,因为它对紧随其后第一个sql语句有效
        PageHelper.startPage(pageNum, pageSize);
        List<CompanyDto> companyDtoList = this.companyMapper.selectListByindustryTypeCode(params);
        PageInfo<CompanyDto> pageInfo = new PageInfo<>(companyDtoList);
        return  new PageUtils(companyDtoList,(int)pageInfo.getTotal(),pageInfo.getPages(),pageInfo.getPageSize(),pageInfo.getPrePage() + 1);
    }
    @Override
    public List<CompanyDto> fildAllListByIndustryTypeCode(Map<String, Object> params){
        return this.companyMapper.selectListByindustryTypeCode(params);
    }

    private Map<String, Object> initPackageData(Map<String, Object> map) {
        map.put("one", 0);
        map.put("two", 0);
        map.put("three", 0);
        map.put("four", 0);
        map.put("oneLevel", 0);
        map.put("twoLevel", 0);
        map.put("oneDangerLevel", 0);
        map.put("oneDangerLevelFinish", 0);
        map.put("threeDangerLevel", 0);
        map.put("threeDangerLevelFinish", 0);
        map.put("planCheckCount", 0);
        map.put("finishCheckCount", 0);
        map.put("riskScore", 0);
        map.put("safeScore", 100);
        return map;
    }
    public Map<String, Object> queryComRootIdAndLeftRight(Long cId) {
        Map<String, Object> result = new HashMap();
        if (cId == null) {
            Console.error("查询统计某集团下的所有企业列表->queryCompanyListForBigScreen【cId为空】");
            return null;
        } else {
            CompanyDto companyDto = this.getCompanyById(cId);
            if (companyDto == null) {
                Console.error("查询统计某集团下的所有企业列表->queryCompanyListForBigScreen【集团信息为空cId=" + cId + "】");
                return null;
            } else {
                Long cRootId = companyDto.getCompanyRootId();
                Integer cLeft = companyDto.getCompanyLeft();
                Integer cRight = companyDto.getCompanyRight();
                result.put("companyRootId", cRootId);
                result.put("cLeft", cLeft);
                result.put("cRight", cRight);
                result.put("companyName", companyDto.getCompanyName());
                return result;
            }
        }
    }

    /**
     * 根据集团公司的Id查询其以及其所辖的所有企业信息tree
     * @param params
     * @return
     */
    @Override
    public List<CompanyDto> getListTree(Map<String,Object> params){
        Object companyId = params.get("companyId");
        if(null == companyId ){
            throw new ScyfException("公司Id错误!");
        }
        params.put("companyTopId",companyId);
        // 根据companyId查询出其本身及其所辖的公司列表
        List<CompanyDto> companyDtoList = this.companyMapper.selectListByindustryTypeCode(params);
        // 把list 转换为 map
        Map<Long,CompanyDto> companyDtoMap = convertToMap(companyDtoList);
        // 获取顶层公司list
        List<CompanyDto> companyParentList = getParentList(companyDtoList,companyDtoMap);
        // 格式化list集合为Tree型结构
        formatTree(companyDtoList,companyDtoMap);
        return companyParentList;
    }

    private Map<Long,CompanyDto> convertToMap(List<CompanyDto> companyDtoList){
        if(null == companyDtoList || companyDtoList.isEmpty()){
            return null;
        }
        Map<Long,CompanyDto> targetMap= new HashMap<>(companyDtoList.size());
        for(CompanyDto companyDto: companyDtoList){
            targetMap.put(companyDto.getCompanyId(), companyDto);
        }
        return targetMap;
    }

    private List<CompanyDto> getParentList (List<CompanyDto> companyDtoList, Map<Long,CompanyDto> companyDtoMap){
        List<CompanyDto> companyParentList = new ArrayList<>();
        if(null == companyDtoList || companyDtoList.isEmpty()){
            return null;
        }
        for(CompanyDto companyDto: companyDtoList){
            // 我们在存储的时候就是将元素的id为键，元素本身为值存入的
            // 以元素的父id为键，在map里取值，若取不到则，对应的元素不存在，即没有父节点，为顶层节点或游离节点
            // 将顶层节点放入list集合
            if(!companyDtoMap.containsKey(companyDto.getCompanyParentId())){
                companyParentList.add(companyDto);
            }
        }
        return  companyParentList;
    }
    private void formatTree(List<CompanyDto> companyDtoList, Map<Long,CompanyDto> companyDtoMap){
        if(null == companyDtoList || companyDtoList.isEmpty() || null == companyDtoMap || companyDtoMap.isEmpty()){
            return ;
        }
        // 循环数据，将数据放到该节点的父节点的children属性中
        for(CompanyDto companyDto: companyDtoList){
            CompanyDto companyParent = companyDtoMap.get(companyDto.getCompanyParentId());
            if(null != companyParent ){
                if(null == companyParent.getChildren() ){
                    companyParent.setChildren(new ArrayList<CompanyDto>());
                }
                companyParent.getChildren().add(companyDto); // 添加到父节点的ChildList集合下
                companyDtoMap.put(companyDto.getCompanyParentId(),companyParent);  // 把放好的数据放回到map中
            }
        }
    }

    /**
     * 获取集团级大屏的企业地址信息
     * @param companyId
     * @return
     */
    @Override
    public List<Map<String, Object>> getCompanyMapByParentId(Long companyId){
        return this.companyMapper.getCompanyMapByParentId(companyId);
    }

    @Override
    public Integer getCompanyCountByIndustryCode(String industryCode) {
        return this.companyMapper.getCompanyCountByIndustryCode(industryCode);
    }

    @Override
    public List<Map<String, Object>> selectTop10ScoreOrRisk(Map<String, Object> params) {
        return  companyMapper.selectTop10ScoreOrRisk(params);
    }

    @Override
    public Map<String, Object> getCompanyDataByIndustryCode(Map<String, Object> params) {
        return companyMapper.getCompanyDataByIndustryCode( params);
    }
    @Override
    public List<Map<String, Object>> getMonthCountByIndustry(String industryCode) {
        return this.companyMapper.getMonthCountByIndustry(industryCode);
    }
}
