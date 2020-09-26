package com.hngf.web.controller.sys;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.hngf.common.exception.ScyfException;
import com.hngf.common.utils.Constant;
import com.hngf.common.utils.R;
import com.hngf.common.validator.ValidatorUtils;
import com.hngf.common.validator.group.AddGroup;
import com.hngf.common.validator.group.UpdateGroup;
import com.hngf.dto.sys.CompanyDto;
import com.hngf.entity.scyf.SecureProduc;
import com.hngf.entity.sys.Company;
import com.hngf.entity.sys.Industry;
import com.hngf.entity.sys.Role;
import com.hngf.entity.sys.User;
import com.hngf.service.sys.CompanyService;
import com.hngf.service.sys.IndustryService;
import com.hngf.service.sys.OrgService;
import com.hngf.service.sys.RoleService;
import com.hngf.service.sys.UserService;
import com.hngf.web.common.annotation.SysLog;
import com.hngf.web.common.shiro.ShiroUtils;
import com.hngf.web.controller.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 企业基础信息表
 *
 * @author yfh
 * @email 
 * @date 2020-05-20 17:31:55
 */
@Api(value = "企业基础信息管理",tags = {"企业基础信息管理"})
@RestController
@RequestMapping("sys/company")
public class CompanyController extends BaseController {
    @Autowired
    private CompanyService companyService;
    @Autowired
    private IndustryService industryService;
    @Autowired
    private UserService userService;
    @Autowired
    private RoleService roleService;

    @Autowired
    private OrgService orgService;
    /**
     * 查询企业基础信息列表
     * yfh
     * 2020/5/22
     * @param params
     * @return
     */
    @ApiOperation(value = "查询企业基础信息表", notes="获取企业基础信息表数据")
    @RequestMapping(value = "/list",method = RequestMethod.GET)
    @RequiresPermissions("sys:company:list")
    public R list(@RequestParam(required = false) Map<String, Object> params){
        List<CompanyDto> tbCompany=companyService.findList(params);
        for (CompanyDto companyDto : tbCompany) {
            String a="";
            if (StringUtils.isNotBlank(companyDto.getCompanyDeputy())) {
                 a += companyDto.getCompanyDeputy();
            }
            if (StringUtils.isNotBlank(companyDto.getCompanyMobile())){
                a+=":"+companyDto.getCompanyMobile();
            }
            companyDto.setCompanyDeputy(a);
        }
        List<CompanyDto> resultList = new ArrayList<CompanyDto>(); // 存贮顶层的数据
        Map<Object ,Object> treeMap = new HashMap();
        Object itemTree;
        for(int i = 0;i<tbCompany.size() && !tbCompany.isEmpty();i++){
            itemTree = tbCompany.get(i);
            treeMap.put(tbCompany.get(i).getCompanyId(),tbCompany.get(i));// 把所有的数据都放到map中
        }
        for(int i =0;i<tbCompany.size();i++){
            if(!treeMap.containsKey(tbCompany.get(i).getCompanyParentId())){
                resultList.add(tbCompany.get(i));
            }
        }
        // 循环数据，将数据放到该节点的父节点的children属性中
        for(int i =0 ;i<tbCompany.size()&& !tbCompany.isEmpty();i++){
            CompanyDto company = (CompanyDto)treeMap.get(tbCompany.get(i).getCompanyParentId());
            if(company!=null ){
                if(company.getChildren() == null){
                    company.setChildren(new ArrayList<CompanyDto>());
                }
                company.getChildren().add(tbCompany.get(i)); // 添加到父节点的ChildList集合下
                treeMap.put(tbCompany.get(i).getCompanyParentId(),company);  // 把放好的数据放回到map中
            }

        }
        return R.ok().put("data", resultList);
    }


    /**
     * yfh 非系统管理员 账号所属企业的基本信息
     * 2020/06/05
     * @return
     */
    @ApiOperation(value = "回显当前用户所属企业的基础信息", notes="回显企业基础信息" , response = CompanyDto.class )
    @RequestMapping(value="/info",method = RequestMethod.GET)
    public R info(HttpServletRequest req){
        return R.ok().put("data", companyService.getById(getCompanyId())).put("path",req.getScheme() + "://" + req.getServerName() + ":" + req.getServerPort() + "/source");
    }

    /**
     * 保存企业基础信息表
     * yfh
     * 2020/05/27
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "保存企业基础信息表", notes="保存企业基础信息表数据")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "companyName", value = "企业名称",required = true, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "companyPic", value = "企业logo 附件URL",required = false, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "companyCertificate", value = "企业证书 附件URL",required = false, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "companyAptitude", value = "企业资质 附件URL",required = false, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "registerNumber", value = "注册号",required = false, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "orgCode", value = "组织机构代码", required = false, paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "foundDate", value = "成立日期",required = false, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "companyDeputy", value = "法定代表人",required = false, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "companyMobile", value = "联系电话",required = false, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "compayEmail", value = "电子邮箱",required = false, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "companyAddress", value = "注册地址",required = false, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "postalCode", value = "邮政编码",required = false, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "provId", value = "所属省",required = false, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "cityId", value = "所属市",required = false, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "countyId", value = "所属县/区",required = false, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "townId", value = "所属乡/街道",required = false, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "economicTypeCode", value = "经济类型代码",required = false, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "industryTypeCoe", value = "行业类别代码",required = false, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "companyAffiliation", value = "企业行政隶属关系",required = false, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "businessScope", value = "经营范围",required = false, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "companyStatus", value = "企业状态",required = false, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "longitude", value = "企业位置经度",required = false, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "latitude", value = "企业位置纬度",required = false, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "businessScopeBaidu", value = "企业经营区域（百度）",required = false, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "businessScope3d", value = "企业经营区域（3D）",required = false, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "supervisionId", value = "监管机构ID",required = false, paramType = "query", dataType = "Long"),
            @ApiImplicitParam(name = "companyRootId", value = "顶层集团公司id",required = false, paramType = "query", dataType = "Long"),
            @ApiImplicitParam(name = "companyParentId", value = "上级企业ID",required = false, paramType = "query", dataType = "Long"),
            @ApiImplicitParam(name = "companyTypeId", value = "企业类型0 企业 1集团",required = false, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "companyLevel", value = "企业级别",required = false, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "deployment", value = "部署情况 1 未开始（红） 2 正在部署（黄） 3 部署完成 （绿）",required = false, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "majorName", value = "主要负责人",required = false, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "majorTelephone", value = "主要负责人固定电话号码",required = false, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "majorPhone", value = "主要负责人移动电话号码",required = false, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "majorEmail", value = "主要负责人电子邮箱",required = false, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "secureName", value = "安全负责人",required = false, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "secureTelephone", value = "安全负责人固定电话号码",required = false, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "securePhone", value = "安全负责人移动电话号码",required = false, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "secureEmail", value = "安全负责人电子邮箱",required = false, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "secureSetting", value = "安全机构设置情况",required = false, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "employeePersons", value = "从业人员数量",required = false, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "specialWorkPersons", value = "特种作业人员数量",required = false, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "fulltimeSecurePersons", value = "专职安全生产管理人员数",required = false, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "fulltimeEmergencyPersons", value = "专职应急管理人员数",required = false, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "registerSecureEngineerPersons", value = "注册安全工程师人员数",required = false, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "secureSupervisePersons", value = "安全监管监察机构",required = false, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "productionAddress", value = "生产/经营地址",required = false, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "scaleCase", value = "规模情况",required = false, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "companyScale", value = "企业规模",required = false, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "superviseClassify", value = "监管分类",required = false, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "hiddenCheckGovern", value = "隐患排查治理制度",required = false, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "hiddenCheckPlan", value = "隐患排查治理计划",required = false, paramType = "query", dataType = "String")
    })
    @PostMapping("/save")
    @RequiresPermissions("sys:company:save")
    @SysLog("保存企业基础信息")
    public R save(@RequestParam(required = false) Map<String,Object> params) throws Exception {
        getUser();
        JSONObject paramsJson = new JSONObject(params);
        paramsJson = JSONObject.parseObject(paramsJson.toString().replaceAll("\\\\\"",""));
        Company company = (Company) JSON.toJavaObject(paramsJson, Company.class);
        SecureProduc secureProduc = (SecureProduc) JSON.toJavaObject(paramsJson, SecureProduc.class);

        ValidatorUtils.validateEntity(company, AddGroup.class);

        company.insertPrefix(getUserId());
        secureProduc.insertPrefix(getUserId());

        companyService.save(company,secureProduc);
        return R.ok();
    }

    /**
     * 修改企业基础信息表数据
     * yfh
     * 2020/05/22
     * @return
     */
    @ApiOperation(value = "修改企业基础信息表数据", notes="修改企业基础信息表数据")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "companyId", value = "企业ID",required = true, paramType = "query", dataType = "Long"),
            @ApiImplicitParam(name = "companyName", value = "企业名称",required = false, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "companyPic", value = "企业logo 附件URL",required = false, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "companyCertificate", value = "企业证书 附件URL",required = false, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "companyAptitude", value = "企业资质 附件URL",required = false, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "registerNumber", value = "注册号",required = false, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "orgCode", value = "组织机构代码", required = false, paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "foundDate", value = "成立日期",required = false, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "companyDeputy", value = "法定代表人",required = false, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "companyMobile", value = "联系电话",required = false, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "compayEmail", value = "电子邮箱",required = false, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "companyAddress", value = "注册地址",required = false, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "postalCode", value = "邮政编码",required = false, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "provId", value = "所属省",required = false, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "cityId", value = "所属市",required = false, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "countyId", value = "所属县/区",required = false, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "townId", value = "所属乡/街道",required = false, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "economicTypeCode", value = "经济类型代码",required = false, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "industryTypeCoe", value = "行业类别代码",required = false, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "companyAffiliation", value = "企业行政隶属关系",required = false, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "businessScope", value = "经营范围",required = false, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "companyStatus", value = "企业状态",required = false, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "longitude", value = "企业位置经度",required = false, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "latitude", value = "企业位置纬度",required = false, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "businessScopeBaidu", value = "企业经营区域（百度）",required = false, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "businessScope3d", value = "企业经营区域（3D）",required = false, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "companyGroupId", value = "公司对应的根群组ID",required = false, paramType = "query", dataType = "Long"),
            @ApiImplicitParam(name = "supervisionId", value = "监管机构ID",required = false, paramType = "query", dataType = "Long"),
            @ApiImplicitParam(name = "companyRootId", value = "顶层集团公司id",required = false, paramType = "query", dataType = "Long"),
            @ApiImplicitParam(name = "companyParentId", value = "上级企业ID",required = false, paramType = "query", dataType = "Long"),
            @ApiImplicitParam(name = "companyTypeId", value = "企业类型0 企业 1集团",required = false, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "companyLevel", value = "企业级别",required = false, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "deployment", value = "部署情况 1 未开始（红） 2 正在部署（黄） 3 部署完成 （绿）",required = false, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "majorName", value = "主要负责人",required = false, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "majorTelephone", value = "主要负责人固定电话号码",required = false, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "majorPhone", value = "主要负责人移动电话号码",required = false, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "majorEmail", value = "主要负责人电子邮箱",required = false, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "secureName", value = "安全负责人",required = false, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "secureTelephone", value = "安全负责人固定电话号码",required = false, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "securePhone", value = "安全负责人移动电话号码",required = false, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "secureEmail", value = "安全负责人电子邮箱",required = false, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "secureSetting", value = "安全机构设置情况",required = false, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "employeePersons", value = "从业人员数量",required = false, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "specialWorkPersons", value = "特种作业人员数量",required = false, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "fulltimeSecurePersons", value = "专职安全生产管理人员数",required = false, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "fulltimeEmergencyPersons", value = "专职应急管理人员数",required = false, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "registerSecureEngineerPersons", value = "注册安全工程师人员数",required = false, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "secureSupervisePersons", value = "安全监管监察机构",required = false, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "productionAddress", value = "生产/经营地址",required = false, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "scaleCase", value = "规模情况",required = false, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "companyScale", value = "企业规模",required = false, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "superviseClassify", value = "监管分类",required = false, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "hiddenCheckGovern", value = "隐患排查治理制度",required = false, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "hiddenCheckPlan", value = "隐患排查治理计划",required = false, paramType = "query", dataType = "String")
    })
    @PostMapping("/update")
    @RequiresPermissions("sys:company:update")
    @SysLog("修改企业基础信息")
    public R update(@RequestParam(required = false) Map<String,Object> params) throws Exception {
        JSONObject paramsJson = new JSONObject(params);
        paramsJson = JSONObject.parseObject(paramsJson.toString().replaceAll("\\\\\"",""));
        Company company = (Company) JSON.toJavaObject(paramsJson, Company.class);
        SecureProduc secureProduc = (SecureProduc) JSON.toJavaObject(paramsJson, SecureProduc.class);
        ValidatorUtils.validateEntity(company, UpdateGroup.class);
        company.updatePrefix(getUserId());
        ValidatorUtils.validateEntity(secureProduc);
        secureProduc.updatePrefix(getUserId());
        companyService.update(company,secureProduc);
        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping(value="/delete",method = RequestMethod.DELETE)
    @RequiresPermissions("sys:company:delete")
    @SysLog("删除企业基础信息")
    public R delete(@RequestBody Long[] companyIds){
        companyService.removeByIds(Arrays.asList(companyIds));

        return R.ok();
    }

    /**
     * 单条删除企业信息
     * yfh
     * 2020/06/08
     * @param companyId
     * @return
     */
    @RequestMapping(value="/deleteById",method = RequestMethod.DELETE)
    @RequiresPermissions("sys:company:delete")
    @ApiOperation(value = "单条删除企业信息", notes="单条删除企业信息")
    @SysLog("删除企业基础信息")
    public R deleteById( Long companyId) throws Exception {
        companyService.removeById(companyId, getUserId());
        return R.ok();
    }



    /**
     * 三级联动查看省市县乡
     * yfh
     * 2020/05/25
     * @return
     */
    @ApiOperation(value = "查看省市县乡", notes="查看省市县乡")
    @RequestMapping(value = "selectPro",method = RequestMethod.GET)
    public  R selectPro(String parentId){
             List<Map<String,Object>> maps=new ArrayList<>();

            if(null == parentId || StringUtils.isEmpty(parentId) || StringUtils.isBlank(parentId)){
                parentId="0";
                maps=companyService.selectPro(parentId);
            }else {
                maps=companyService.selectPro(parentId);
            }
        return R.ok().put("proList",maps);
    }

    /**
     * 根据行业类别industryCode获取监察机构
     * yfh
     * 2020/06/05
     * @param industryCode
     * @return
     */
    @ApiOperation(value = "获取监察机构", notes="获取监察机构")
    @RequestMapping(value = {"/queryOrg"},method = {RequestMethod.GET})
    public R queryOrg(String industryCode) {
        List<Map<String,Object>> jgList = industryService.getOrgIndustryList(null);
        return R.ok().put("data",jgList);
    }

    /**
     * 查看企业经济类型
     * yfh
     * 2020/06/05
     * @return
     */
    @ApiOperation(value = "企业经济类型", notes="企业经济类型")
    @RequestMapping(value = {"/queryEconomicType"},method = {RequestMethod.GET})
    public R queryEconomicType() {
        List newList=new ArrayList();
        String dictType="economics_type";
        String ownerId="0";
        List<Map<String,Object>> list = companyService.queryEconomicTypeParent(dictType,ownerId);
        for(int i=0;i<list.size();i++){
            Map map=new HashMap();
            Integer dictId=Integer.valueOf(list.get(i).get("dictId").toString());
            String dictName=list.get(i).get("dictName").toString();
            List<Map<String,Object>> jgMap = companyService.queryEconomicType(dictId,dictType);
            map.put("dictParentName",dictName);
            map.put("jgMap",jgMap);
            newList.add(map);
        }
        return R.ok().put("data",newList);
    }

    /**
     * 企业规模查询
     * yfh
     * 2020/06/05
     * @return
     */
    @ApiOperation(value = "企业规模查询", notes="企业规模查询")
    @RequestMapping(value = {"/queryScale"},method = {RequestMethod.GET})
    public R queryScale() {
        String dictType="scale_type";
        List<Map<String,Object>> scaleList = companyService.queryScale(dictType);
        return R.ok().put("data",scaleList);
    }

    /**
     * 行业Tree树查询
     * yfh
     * 2020/06/05
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "行业树查询", notes="行业树查询")
    @RequestMapping(value = {"/queryIndustryTree"},method = {RequestMethod.GET})
    public R queryIndustryTree() throws Exception {

        return R.ok().put("data",industryService.queryTreeList(null));
       }

    /**
     * 行业树列表查询
     * yfh
     * 2020/06/05
     * @return
     */
    @ApiOperation(value = "行业列表查询", notes="行业列表查询")
    @RequestMapping(value = {"/queryIndustryList"},method = {RequestMethod.GET})
    public R queryIndustryList(){
        List<Industry> list = industryService.queryList();
        for (Industry industry : list) {
            industry.setLabel(industry.getIndustryName());
        }
        return R.ok().put("data",list);
    }

    /**
     * 查看管理员账号信息 及角色信息
     * yfh
     * 2020/06/08
     * @param companyId
     * @return
     */
    @ApiOperation(value = "管理员账号信息", notes="管理员账号信息")
    @ApiImplicitParam(name = "companyId", value = "企业Id",paramType = "query" ,required = true ,dataType = "long")
    @RequestMapping(value = {"/getAdmin"},method = {RequestMethod.GET})
    public R getAdmin( Long companyId) {
        Company company = companyService.getCompanyId(companyId);
        if (company == null) {
            return R.error("公司不存在");
        } else {
            Long userId = company.getCompanyAdminId();
            if(userId==null){
                return R.error("用户不存在");
            }else{
                List<Map<String,Object>> userAndRoles=userService.findUserAndRole(userId);
                List<Role> list = roleService.getList();
                Map map=new HashMap(2);
                map.put("userMsg",userAndRoles);
                map.put("roleLists",list);
                return R.ok().put("data", map);
            }
        }
    }

    /**
     * 查看角色信息
     * yfh
     * 2020/06/10
     * @return
     */
    @ApiOperation(value = "查看角色信息", notes="查看角色信息")
    @RequestMapping(value = {"/getRoles"},method = {RequestMethod.GET})
    public R getRoles() {
        List<Role> list = roleService.getList();
        return R.ok().put("data", list);
    }

    /**
     * 修改管理员账号信息
     * yfh
     * 2020/06/08
     * @param userId
     * @param loginName
     * @param userMobile
     * @param roleId
     * @return
     */
    @ApiOperation(value = "修改管理员账号信息", notes="修改管理员账号信息")
    @RequestMapping(value = {"/updateAdmin"},method = {RequestMethod.POST})
    public R updateAdmin(Integer userId,String loginName,String userMobile,Integer roleId){
        if(null == userId || 0l == userId.longValue()){
            throw new ScyfException("管理员Id不能为空");        }
        if(null != loginName  && loginName.length() > 200 ){
            throw  new ScyfException("用户名长度范围0-200");
        }
        User user=new User();
        user.setUserId((long)userId);
        user.setLoginName(loginName);
        user.setUserMobile(userMobile);
        user.setUpdatedBy(getUserId());
        user.setUpdatedTime(new Date());
        user.setDelFlag(0);
        userService.updateUser(user,roleId);
        return R.ok("保存成功!");
    }

    /**
     * 新增管理员账号信息
     * yfh
     * 2020/06/09
     * @param user
     * @param roleId
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "新增管理员账号信息", notes="新增管理员账号信息")
    @RequestMapping(value = {"/saveAdmin"},method = {RequestMethod.POST})
    @SysLog("新增管理员账号信息")
    public R saveAdmin(@RequestBody  User user,Integer roleId) throws Exception {
        if(null == user || null == user.getCompanyId() || 0l == user.getCompanyId().longValue() ){
            throw new ScyfException("用户所属企业Id不能为空！");
        }
        if(user.getCompanyId().longValue() > 99999999999l ){
            throw  new ScyfException("用户所属企业Id不能超过99999999999");
        }
        if(null == user || null == user.getLoginName() || StringUtils.isBlank(user.getLoginName()) ){
            throw new ScyfException("用户名不能为空！");
        }
        if(user.getLoginName().length() > 200 ){
            throw  new ScyfException("用户名长度范围0-200");
        }
        if( null == user.getRoleId() || 0== user.getRoleId().intValue()){
            throw new ScyfException("角色不能为空！");
        }
        user.insertPrefix(getUserId());
        try {
            //sha256加密
            String salt = RandomStringUtils.randomAlphanumeric(20);
            user.setSalt(salt);
            user.setPassword(ShiroUtils.sha256(user.getPassword(), user.getSalt()));
            this.companyService.insertCompanyAdmin(user, user.getRoleId() );
        } catch (Exception var3) {
            var3.printStackTrace();
            throw new Exception();
        }

        return R.ok("保存成功!");
    }
    /**
     * @Author: zyj
     * @Description:重置管理员密码
     * @Param
     * @Date 18:10 2020/9/1
     */
    @PostMapping("/resetAdminPwd")
    @RequiresPermissions("sys:company:save")
    @ApiOperation(value="重置管理员密码")
    @ApiImplicitParam(name = "companyId", value = "企业id", required = true, paramType = "query", dataType = "Long")
    public R resetAdminPwd(@RequestParam("companyId") Long companyId){
        CompanyDto companyById = companyService.getCompanyById(companyId);
        if (null==companyById){
            R.error("企业不存在");
        }else {
            User user=new User();
            Long companyAdminId = companyById.getCompanyAdminId();
            //sha256加密
            String salt = RandomStringUtils.randomAlphanumeric(20);
            user.setSalt(salt);
            String password=ShiroUtils.sha256(Constant.USER_PASSWORD, salt);
            user.setPassword(password);
            orgService.resetAdminPwd(companyAdminId,user.getPassword(),companyId,user.getSalt());
        }
        return R.ok();
    }
}
