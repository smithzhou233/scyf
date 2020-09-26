package com.hngf.web.controller.gent;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.hngf.common.exception.ScyfException;
import com.hngf.common.utils.Constant;
import com.hngf.common.utils.PageUtils;
import com.hngf.common.utils.ParamUtils;
import com.hngf.common.utils.R;
import com.hngf.common.validator.Assert;
import com.hngf.common.validator.ValidatorUtils;
import com.hngf.common.validator.group.AddGroup;
import com.hngf.common.validator.group.UpdateGroup;
import com.hngf.dto.sys.CompanyDto;
import com.hngf.entity.scyf.SecureProduc;
import com.hngf.entity.sys.Company;
import com.hngf.entity.sys.User;
import com.hngf.service.sys.CompanyService;
import com.hngf.service.sys.IndustryService;
import com.hngf.service.sys.OrgService;
import com.hngf.service.sys.UserService;
import com.hngf.web.common.annotation.ApiJsonProperty;
import com.hngf.web.common.annotation.ApiParameterJsonObject;
import com.hngf.web.common.annotation.SysLog;
import com.hngf.web.common.shiro.ShiroUtils;
import com.hngf.web.controller.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 集团级 - 企业管理
 * 集团管理员 对集团企业进行维护 ：
 * 1、集团自身企业信息维护，不能够删除；
 * 2、集团下级企业的增、删、改、查、添加/编辑企业管理员、重置管理员密码
 *
 * 提供的接口服务有：列表treeList、添加save、编辑update、删除delete/{companyId}、详情get/{companyId}
 *     添加管理员/saveAdmin、编辑管理员updateAdmin、重置管理员密码 /resetAdminPwd/{companyId}
 *
 */
@RestController
@RequestMapping("/gent/company")
@Api(value = "【集团】企业管理",tags = {"【集团】企业管理"})
public class GentCompanyController extends BaseController {

    @Autowired
    private CompanyService companyService;
    @Autowired
    private UserService userService;

    @Autowired
    private IndustryService industryService;


    @Autowired
    private OrgService orgService;

    /**
     * 企业列表
     * @return
     */
    @GetMapping("/companyTreeList")
    @ApiOperation(value="【集团(局)级】企业列表",response = CompanyDto.class ,notes = "集团企业列表 属性delPermissionsFlag=1 时不显示删除按钮  ")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "industryCode", value = "行业代码", required = false, paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "keywords", value = "关键字", required = false, paramType = "query", dataType = "string")
    })
    public R comTreeList(@ApiIgnore @RequestParam(required = false) Map<String, Object> params) {
        // 设置当前集团的企业Id
        Long companyId = this.getCompanyId();
        params.put("companyId",companyId);
        List<CompanyDto> companyParentList = this.companyService.getListTree(params) ;
        // 设置集团级的企业 不显示删除按钮
        for(CompanyDto companyDto : companyParentList){
            companyDto.setDelPermissionsFlag(1);
        }
        return R.ok().put("data",companyParentList);
    }

    /**
     * 新增企业
     * @return
     */
    @ApiOperation(value = "保存企业基础信息表", notes="保存企业基础信息表数据" +
            "企业列表接口关联接口如下：<br>" +
            "1、公司类型：0 企业 1集团" +
            "2、评价方式：LS、LEC 目前没有数据字典" +
            "3、行业类别：：/sys/company/queryIndustryTree  <br>" +
            "4、监察机构：/sys/company/queryOrg" +
            "5、监管分类：" +
            "5、经济类型：/sys/company/queryEconomicType" +
            "6、行政区：" +
            "7、状态：正常/经营异常" +
            "8、安全机构设置情况：是/否" +
            "9、规模情况：是/否" +
            "10、规模：/sys/company/queryScale" +
            "11、角色：/sys/company/getRoles  目前并没有角色的权限划分，暂用全部角色")
    @PostMapping("/save")
    public R save(@ApiParameterJsonObject(name="params",
            value = {@ApiJsonProperty(key = "companyName",value = "企业名称", example = "测试企业名称", required = true),
                    @ApiJsonProperty(key = "companyPic",value = "企业logo 附件URL", example = "/2020/08/20/XXX.png" ),
                    @ApiJsonProperty(key = "companyCertificate",value = "企业证书", example = "/2020/08/20/XXX.png" ),
                    @ApiJsonProperty(key = "companyAptitude",value = "企业资质 附件URL", example = "/2020/08/20/XXX.png" ),
                    @ApiJsonProperty(key = "registerNumber",value = "注册号", example = "342424" , required = true),
                    @ApiJsonProperty(key = "orgCode",value = "组织机构代码" , example = "非必填项" , required = true),
                    @ApiJsonProperty(key = "foundDate",value = "成立日期", example = "yyyy-MM-dd HH:mm:ss" ),
                    @ApiJsonProperty(key = "companyDeputy",value = "法定代表人", example = "王二" , required = true),
                    @ApiJsonProperty(key = "companyMobile",value = "联系电话", example = "13838389438" , required = true),
                    @ApiJsonProperty(key = "compayEmail",value = "电子邮箱", example = "email@xx.com" ),
                    @ApiJsonProperty(key = "companyAddress",value = "注册地址" , example = "非必填项", required = true),
                    @ApiJsonProperty(key = "postalCode",value = "邮政编码" , example = "非必填项"),
                    @ApiJsonProperty(key = "provId",value = "所属省", example = "非必填项",  type = "long" ),
                    @ApiJsonProperty(key = "cityId",value = "所属市", example = "非必填项",  type = "long" ),
                    @ApiJsonProperty(key = "countyId",value = "所属县/区", example = "非必填项",  type = "long" ),
                    @ApiJsonProperty(key = "townId",value = "所属乡/街道", example = "非必填项",  type = "long" ),
                    @ApiJsonProperty(key = "economicTypeCode",value = "经济类型代码" , example = "非必填项"),
                    @ApiJsonProperty(key = "industryTypeCoe",value = "行业类别代码", example = "非必填项" ),
                    @ApiJsonProperty(key = "companyAffiliation",value = "企业行政隶属关系", example = "非必填项" ),
                    @ApiJsonProperty(key = "businessScope",value = "经营范围" , example = "非必填项"),
                    @ApiJsonProperty(key = "companyStatus",value = "企业状态", example = "非必填项" ),
                    @ApiJsonProperty(key = "longitude",value = "企业位置经度" , example = "非必填项", required = true),
                    @ApiJsonProperty(key = "latitude",value = "企业位置纬度", example = "非必填项", required = true),
                    @ApiJsonProperty(key = "businessScopeBaidu",value = "企业经营区域（百度）" , example = "非必填项"),
                    @ApiJsonProperty(key = "businessScope3d",value = "企业经营区域（3D）" , example = "非必填项"),
                    @ApiJsonProperty(key = "supervisionId",value = "监管机构ID", example = "非必填项",  type = "long" ),
                    @ApiJsonProperty(key = "companyRootId",value = "顶层集团公司id", example = "非必填项",  type = "long" ),
                    @ApiJsonProperty(key = "companyParentId",value = "上级企业ID", example = "非必填项",  type = "long" ),
                    @ApiJsonProperty(key = "companyTypeId",value = "企业类型0 企业 1集团", example = "非必填项",  type = "int" ),
                    @ApiJsonProperty(key = "companyLevel",value = "企业级别" , example = "非必填项"),
                    @ApiJsonProperty(key = "deployment",value = "部署情况 1 未开始（红） 2 正在部署（黄） 3 部署完成 （绿）", example = "非必填项",  type = "int" ),
                    @ApiJsonProperty(key = "majorName",value = "主要负责人", example = "非必填项" ),
                    @ApiJsonProperty(key = "majorTelephone",value = "主要负责人固定电话号码", example = "非必填项" ),
                    @ApiJsonProperty(key = "majorEmail",value = "主要负责人电子邮箱" , example = "非必填项"),
                    @ApiJsonProperty(key = "secureName",value = "安全负责人" , example = "非必填项"),
                    @ApiJsonProperty(key = "secureTelephone",value = "安全负责人固定电话号码" , example = "非必填项"),
                    @ApiJsonProperty(key = "securePhone",value = "安全负责人移动电话号码" , example = "非必填项"),
                    @ApiJsonProperty(key = "secureEmail",value = "安全负责人电子邮箱", example = "非必填项" ),
                    @ApiJsonProperty(key = "secureSetting",value = "安全机构设置情况", example = "非必填项",  type = "int" ),
                    @ApiJsonProperty(key = "employeePersons",value = "从业人员数量", example = "非必填项",  type = "int" ),
                    @ApiJsonProperty(key = "specialWorkPersons",value = "特种作业人员数量", example = "非必填项",  type = "int" ),
                    @ApiJsonProperty(key = "fulltimeSecurePersons",value = "专职安全生产管理人员数", example = "非必填项",  type = "int" ),
                    @ApiJsonProperty(key = "fulltimeEmergencyPersons",value = "专职应急管理人员数", example = "非必填项",  type = "int" ),
                    @ApiJsonProperty(key = "registerSecureEngineerPersons",value = "注册安全工程师人员数", example = "非必填项",  type = "int" ),
                    @ApiJsonProperty(key = "secureSupervisePersons",value = "安全监管监察机构", example = "非必填项" ),
                    @ApiJsonProperty(key = "productionAddress",value = "生产/经营地址", example = "非必填项"),
                    @ApiJsonProperty(key = "scaleCase",value = "规模情况", example = "非必填项",  type = "int" ),
                    @ApiJsonProperty(key = "companyScale",value = "企业规模", example = "非必填项",  type = "int" ),
                    @ApiJsonProperty(key = "superviseClassify",value = "监管分类" , example = "非必填项"),
                    @ApiJsonProperty(key = "hiddenCheckGovern",value = "隐患排查治理制度", example = "非必填项"),
                    @ApiJsonProperty(key = "hiddenCheckPlan",value = "隐患排查治理计划", example = "非必填项")}) @RequestParam Map<String,Object> params) {
        Assert.isBlank(ParamUtils.paramsToString(params,"companyName"),"企业名称(companyName)不能为空");

        JSONObject paramsJson = new JSONObject(params);
        paramsJson = JSONObject.parseObject(paramsJson.toString().replaceAll("\\\\\"",""));
        Company company = JSON.toJavaObject(paramsJson, Company.class);
        SecureProduc secureProduc = JSON.toJavaObject(paramsJson, SecureProduc.class);
        ValidatorUtils.validateEntity(company, AddGroup.class);
        company.insertPrefix(getUserId());

        ValidatorUtils.validateEntity(secureProduc);
        secureProduc.insertPrefix(getUserId());
        Long companyParentId = company.getCompanyParentId();
        if(null == companyParentId || 0l == companyParentId.longValue()){
            // 把当前集团公司作为父级，
            company.setCompanyParentId(getCompanyId());
        }
        try {
            companyService.save(company, secureProduc);
        } catch (Exception e) {
            e.printStackTrace();
            return R.error("保存失败！");
        }
        return R.ok();

    }

    /**
     * 修改企业
     * @return
     */
    @ApiOperation(value = "修改企业基础信息表数据", notes="修改企业基础信息表数据" +
            "企业列表接口关联接口如下：<br>" +
            "1、公司类型：0 企业 1集团" +
            "2、评价方式：LS、LEC 目前没有数据字典" +
            "3、行业类别：：/sys/company/queryIndustryTree  <br>" +
            "4、监察机构：/sys/company/queryOrg" +
            "5、监管分类：" +
            "5、经济类型：/sys/company/queryEconomicType" +
            "6、行政区：" +
            "7、状态：正常/经营异常" +
            "8、安全机构设置情况：是/否" +
            "9、规模情况：是/否" +
            "10、规模：/sys/company/queryScale" +
            "11、角色：/sys/company/getRoles  目前并没有角色的权限划分，暂用全部角色")
    @PostMapping("/update")
    public R update(@ApiParameterJsonObject(name="params",
            value = {@ApiJsonProperty(key = "companyId",value = "企业Id", example = "企业Id", required = true ,type = "long"),
                    @ApiJsonProperty(key = "companyName",value = "企业名称", example = "测试企业名称", required = true),
                    @ApiJsonProperty(key = "companyPic",value = "企业logo 附件URL", example = "/2020/08/20/XXX.png" ),
                    @ApiJsonProperty(key = "companyCertificate",value = "企业证书", example = "/2020/08/20/XXX.png" ),
                    @ApiJsonProperty(key = "companyAptitude",value = "企业资质 附件URL", example = "/2020/08/20/XXX.png" ),
                    @ApiJsonProperty(key = "registerNumber",value = "注册号", example = "342424", required = true),
                    @ApiJsonProperty(key = "orgCode",value = "组织机构代码" , example = "非必填项" , required = true),
                    @ApiJsonProperty(key = "foundDate",value = "成立日期", example = "yyyy-MM-dd HH:mm:ss" ),
                    @ApiJsonProperty(key = "companyDeputy",value = "法定代表人", example = "王二", required = true ),
                    @ApiJsonProperty(key = "companyMobile",value = "联系电话", example = "13838389438" , required = true),
                    @ApiJsonProperty(key = "compayEmail",value = "电子邮箱", example = "email@xx.com" ),
                    @ApiJsonProperty(key = "companyAddress",value = "注册地址" , example = "非必填项", required = true),
                    @ApiJsonProperty(key = "postalCode",value = "邮政编码" , example = "非必填项"),
                    @ApiJsonProperty(key = "provId",value = "所属省", example = "非必填项",  type = "long" ),
                    @ApiJsonProperty(key = "cityId",value = "所属市", example = "非必填项",  type = "long" ),
                    @ApiJsonProperty(key = "countyId",value = "所属县/区", example = "非必填项",  type = "long" ),
                    @ApiJsonProperty(key = "townId",value = "所属乡/街道", example = "非必填项",  type = "long" ),
                    @ApiJsonProperty(key = "economicTypeCode",value = "经济类型代码" , example = "非必填项"),
                    @ApiJsonProperty(key = "industryTypeCoe",value = "行业类别代码", example = "非必填项" ),
                    @ApiJsonProperty(key = "companyAffiliation",value = "企业行政隶属关系", example = "非必填项" ),
                    @ApiJsonProperty(key = "businessScope",value = "经营范围" , example = "非必填项"),
                    @ApiJsonProperty(key = "companyStatus",value = "企业状态", example = "非必填项" ),
                    @ApiJsonProperty(key = "longitude",value = "企业位置经度" , example = "非必填项", required = true),
                    @ApiJsonProperty(key = "latitude",value = "企业位置纬度", example = "非必填项", required = true),
                    @ApiJsonProperty(key = "businessScopeBaidu",value = "企业经营区域（百度）" , example = "非必填项"),
                    @ApiJsonProperty(key = "businessScope3d",value = "企业经营区域（3D）" , example = "非必填项"),
                    @ApiJsonProperty(key = "supervisionId",value = "监管机构ID", example = "非必填项",  type = "long" ),
                    @ApiJsonProperty(key = "companyRootId",value = "顶层集团公司id", example = "非必填项",  type = "long" ),
                    @ApiJsonProperty(key = "companyParentId",value = "上级企业ID", example = "非必填项",  type = "long" ),
                    @ApiJsonProperty(key = "companyTypeId",value = "企业类型0 企业 1集团", example = "非必填项",  type = "int" ),
                    @ApiJsonProperty(key = "companyLevel",value = "企业级别" , example = "非必填项"),
                    @ApiJsonProperty(key = "deployment",value = "部署情况 1 未开始（红） 2 正在部署（黄） 3 部署完成 （绿）", example = "非必填项",  type = "int" ),
                    @ApiJsonProperty(key = "majorName",value = "主要负责人", example = "非必填项" ),
                    @ApiJsonProperty(key = "majorTelephone",value = "主要负责人固定电话号码", example = "非必填项" ),
                    @ApiJsonProperty(key = "majorEmail",value = "主要负责人电子邮箱" , example = "非必填项"),
                    @ApiJsonProperty(key = "secureName",value = "安全负责人" , example = "非必填项"),
                    @ApiJsonProperty(key = "secureTelephone",value = "安全负责人固定电话号码" , example = "非必填项"),
                    @ApiJsonProperty(key = "securePhone",value = "安全负责人移动电话号码" , example = "非必填项"),
                    @ApiJsonProperty(key = "secureEmail",value = "安全负责人电子邮箱", example = "非必填项" ),
                    @ApiJsonProperty(key = "secureSetting",value = "安全机构设置情况", example = "非必填项",  type = "int" ),
                    @ApiJsonProperty(key = "employeePersons",value = "从业人员数量", example = "非必填项",  type = "int" ),
                    @ApiJsonProperty(key = "specialWorkPersons",value = "特种作业人员数量", example = "非必填项",  type = "int" ),
                    @ApiJsonProperty(key = "fulltimeSecurePersons",value = "专职安全生产管理人员数", example = "非必填项",  type = "int" ),
                    @ApiJsonProperty(key = "fulltimeEmergencyPersons",value = "专职应急管理人员数", example = "非必填项",  type = "int" ),
                    @ApiJsonProperty(key = "registerSecureEngineerPersons",value = "注册安全工程师人员数", example = "非必填项",  type = "int" ),
                    @ApiJsonProperty(key = "secureSupervisePersons",value = "安全监管监察机构", example = "非必填项" ),
                    @ApiJsonProperty(key = "productionAddress",value = "生产/经营地址", example = "非必填项"),
                    @ApiJsonProperty(key = "scaleCase",value = "规模情况", example = "非必填项",  type = "int" ),
                    @ApiJsonProperty(key = "companyScale",value = "企业规模", example = "非必填项",  type = "int" ),
                    @ApiJsonProperty(key = "superviseClassify",value = "监管分类" , example = "非必填项"),
                    @ApiJsonProperty(key = "hiddenCheckGovern",value = "隐患排查治理制度", example = "非必填项"),
                    @ApiJsonProperty(key = "hiddenCheckPlan",value = "隐患排查治理计划", example = "非必填项")}) @RequestParam Map<String,Object> params) {
        Assert.isNull(ParamUtils.paramsToLong(params,"companyId"),"企业ID(companyId)不能为空");
        Assert.isBlank(ParamUtils.paramsToString(params,"companyName"),"企业名称(companyName)不能为空");
        JSONObject paramsJson = new JSONObject(params);
        paramsJson = JSONObject.parseObject(paramsJson.toString().replaceAll("\\\\\"",""));
        Company company = JSON.toJavaObject(paramsJson, Company.class);
        SecureProduc secureProduc = JSON.toJavaObject(paramsJson, SecureProduc.class);
        ValidatorUtils.validateEntity(company, UpdateGroup.class);
        ValidatorUtils.validateEntity(secureProduc);
        company.setUpdatedBy(getUserId());
        secureProduc.setUpdatedBy(getUserId());
        try {
            companyService.update(company, secureProduc);
        } catch (Exception e) {
            e.printStackTrace();
            return R.error("保存失败！");
        }
        return R.ok();

    }

    /**
     * 企业详情
     * @return
     */
    @GetMapping("/get/{companyId}")
    @ApiOperation(value="企业详情",response = CompanyDto.class)
    @ApiImplicitParam(name = "companyId", value = "企业ID", required = true, paramType = "path", dataType = "long")
    public R get(@PathVariable Long companyId, HttpServletRequest req) {
        return R.ok().put("data", this.companyService.getCompanyById(companyId)).put("path",req.getScheme() + "://" + req.getServerName() + ":" + req.getServerPort() + "/source");
    }

    /**
     * 删除企业
     * @return
     */
    @DeleteMapping("/delete/{companyId}")
    @ApiOperation(value="删除企业")
    @ApiImplicitParam(name = "companyId", value = "企业ID", required = true, paramType = "path", dataType = "long")
    public R delete(@PathVariable Long companyId) {
        if(getCompanyId().longValue() == companyId.longValue()){
            return R.error("不能删除自身的集团公司！");
        }
        try {
            this.companyService.removeById(companyId, getUserId());
        } catch (Exception e) {
            e.printStackTrace();
            return R.error("删除失败");
        }
        return R.ok();
    }
    @ApiOperation(value="监管级-主页-企业统计等级数量")
    @GetMapping({"/queryTotal"})
    public R queryTotalForMainPage(@RequestParam Map<String, Object> params) {
        Long cId = getCompanyId();
        params.put("cId", cId);
        List<Map<String, Object>> list =  this.companyService.queryTotalForMainPage(params);
        return R.ok().put("data",list);
    }



    /**
     * 判断管理员账号是否存在
     * @param loginName
     * @param userId
     * @return
     */
    @GetMapping("/userExists")
    @ApiOperation(value="判断管理员账号是否存在")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "loginName", value = "登录名", required = true, paramType = "json", dataType = "string"),
            @ApiImplicitParam(name = "userId", value = "用户ID", required = false, paramType = "json", dataType = "int")
    })
    public R checkUserAccountIsExists(String loginName, Long userId) {
        Assert.isBlank(loginName,"登录名不能为空");
        Map<String, Object> map = new HashMap();
        map.put("loginName", loginName);
        map.put("userId", userId);
        int count = this.userService.checkUserExists(map);
        return count > 0 ? R.error("已存在") : R.ok("可以使用");
    }

    /**
     * 判断手机号是否存在
     * @param phone
     * @param userId
     * @return
     */
    @GetMapping("/phoneExists")
    @ApiOperation(value="判断手机号是否存在")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "phone", value = "手机号", required = true, paramType = "json", dataType = "string"),
            @ApiImplicitParam(name = "userId", value = "用户ID", required = false, paramType = "json", dataType = "int")
    })
    public R checkUserTelephoneIsExists(String phone, Long userId) {
        Assert.isBlank(phone,"手机号不能为空");
        Map<String, Object> map = new HashMap();
        map.put("userMobile", phone);
        map.put("userId", userId);
        int count = this.userService.checkUserExists(map);
        return count > 0 ? R.error("已存在") : R.ok("可以使用");
    }

    /**
     * 新增管理员账号信息
     * @param user
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "新增管理员账号信息", notes="新增之前，需要验证登录名和手机号是否存在<br>" +
            "1，验证登录名接口：/gent/company/userExists <br>" +
            "2，验证手机号接口：/gent/company/phoneExists <br>")
    @RequestMapping(value = {"/saveAdmin"},method = {RequestMethod.POST})
    @SysLog("新增管理员账号信息")
    public R saveAdmin(@RequestBody User user) {
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
            //默认初始化密码为 888888
            user.setPassword(ShiroUtils.sha256(Constant.USER_PASSWORD, user.getSalt()));
            this.companyService.insertCompanyAdmin(user);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ScyfException("保存失败！");
        }

        return R.ok("保存成功!");
    }

    /**
     * 修改管理员账号信息
     * @param userId
     * @param loginName
     * @param userMobile
     * @return
     */
    @ApiOperation(value = "修改管理员账号信息", notes="修改之前，需要验证登录名和手机号是否存在<br>" +
            "1，验证登录名接口：/gent/company/userExists <br>" +
            "2，验证手机号接口：/gent/company/phoneExists <br>")
    @RequestMapping(value = {"/updateAdmin"},method = {RequestMethod.POST})
    @ApiImplicitParams({
            @ApiImplicitParam(name = "loginName", value = "登录名", required = true, paramType = "json", dataType = "string"),
            @ApiImplicitParam(name = "userMobile", value = "手机号", required = false, paramType = "json", dataType = "string"),
            @ApiImplicitParam(name = "userId", value = "用户ID", required = true, paramType = "json", dataType = "int")
    })
    public R updateAdmin(Long userId,String loginName,String userMobile){
        if(null == userId || 0l == userId.longValue()){
            throw new ScyfException("管理员Id不能为空");        }
        if(null != loginName  && loginName.length() > 200 ){
            throw  new ScyfException("用户名长度范围0-200");
        }
        User user = new User();
        user.setUserId(userId);
        user.setLoginName(loginName);
        user.setUserMobile(userMobile);
        user.updatePrefix(getUserId());
        userService.updateUser(user);
        return R.ok("保存成功!");
    }

    /**
     * 添加现有企业列表
     * @param params
     * @return
     */
    @GetMapping("/singleOrgs")
    @ApiOperation(value = "添加现有企业查询列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum", value = "第几页", required = true, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "pageSize", value = "每页多少条数据", required = true, paramType = "query", dataType = "int")
    })
    public R querySingleOrgs(@ApiIgnore @RequestParam(required = false)  Map<String, Object> params) {
        int pageNum = null!= params.get(Constant.PAGE_PAGENUM) ? Integer.parseInt(params.get(Constant.PAGE_PAGENUM).toString()) : 1;
        int pageSize = null != params.get(Constant.PAGE_PAGESIZE) ? Integer.parseInt(params.get(Constant.PAGE_PAGESIZE).toString()) : Constant.PAGE_SIZE;
        return R.ok().put("data",this.companyService.querySingleOrgs(params,pageNum,pageSize,null));
    }

    /**
     * 保存已选择的现有企业
     * @param params
     * @return
     */
    @PostMapping({"/addExist"})
    @ApiOperation(value = "保存已选择的现有企业")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "selectedCompanyId", value = "选择的企业ID", required = true, paramType = "query", dataType = "long"),
            @ApiImplicitParam(name = "selectedOrgIds", value = "选择的组织ID，多个之间逗号分隔", required = true, paramType = "query", dataType = "string")
    })
    public R addExistOrg2Group(@ApiIgnore @RequestParam(required = false)  Map<String, Object> params) {
        Assert.isNull(params.get("selectedCompanyId"),"未选择企业ID");
        Assert.isBlank(ParamUtils.paramsToString(params,"selectedOrgIds"),"未选择组织ID");
        try {
            this.companyService.addExistOrg2Group(params);
        } catch (Exception var3) {
            var3.printStackTrace();
            return R.error("请求失败！");
        }

        return R.ok();
    }


    /**
     * 更新部署状态
     * @param params
     * @return
     */
    @PostMapping({"/setDeployment"})
    @ApiOperation(value = "更新部署状态")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "companyId", value = "企业ID", required = true, paramType = "query", dataType = "long"),
            @ApiImplicitParam(name = "deployment", value = "部署状态", required = true, paramType = "query", dataType = "int")
    })
    public R setDeployment(@ApiIgnore @RequestParam(required = false) Map<String, Object> params) {
        Assert.isNull(ParamUtils.paramsToLong(params,"companyId"),"企业ID不能为空");
        Assert.isNull(ParamUtils.paramsToLong(params,"deployment"),"部署状态不能为空");
        int count = this.companyService.setDeployment(params);
        return count > 0 ? R.ok() : R.error("更新失败");
    }

    /**
     * @Author: lxf
     * @Description: 企业评分 其自身和所辖企业的风险、隐患统计 评价考核
     * @Param
     * @Date 18:10 2020/9/5
     */
    @ApiOperation(value="企业评分", notes = "其自身和所辖企业的风险、隐患统计 ; 所属模块：评价考核")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum", value = "第几页", required = true, paramType = "query", dataType = "int", defaultValue = "1"),
            @ApiImplicitParam(name = "pageSize", value = "每页多少条数据", required = true, paramType = "query", dataType = "int", defaultValue = "10"),
            @ApiImplicitParam(name = "industryCode", value = "行业编码", required = false, paramType = "query", dataType = "string")
    })
    @RequestMapping(value = "/companyScore" ,method = RequestMethod.GET)
    public R companyScore(@ApiIgnore @RequestParam(required = false) Map<String, Object> params) {
        int pageNum = null!= params.get(Constant.PAGE_PAGENUM) ? Integer.parseInt(params.get(Constant.PAGE_PAGENUM).toString()) : 1;
        int pageSize = null != params.get(Constant.PAGE_PAGESIZE) ? Integer.parseInt(params.get(Constant.PAGE_PAGESIZE).toString()) : Constant.PAGE_SIZE;
        // 行业编码
        // 默认该机构下的所有行业
        // 当前机构Id
        Long curOrgId = getCompanyId();
        Object industryCode = params.get("industryCode") ;
        String industryCodes = this.industryService.getCodeListByIndustryCode(curOrgId, null==industryCode?null:industryCode.toString());

        if(null == industryCodes ){
            // 该用户所在机构没有监管行业
            return R.ok().put("data", new PageUtils(null,0,0,10,1));
        }
        if(industryCodes.indexOf(",")>0 ){
            params.remove("industryCode");
            params.put("industryCodes",industryCodes);
        }

        params.put("orgId",curOrgId);
        PageUtils page = companyService.companyScore(params,pageNum,pageSize,null);

        return R.ok().put("data", page);
    }
    /**
     * 风险点总计
     * @param params
     * @return
     */
    @GetMapping({"/gent/riskCount"})
    @ApiOperation(value="【集团首页】风险点总计",notes = "返回值说明：<br>" +
            "total(全部风险点数量),<br>" +
            "list{<br>" +
            "ristCount(风险点数量)，" +
            "riskLevel(风险点等级)" +
            "}"
    )
    @ApiImplicitParams({
            @ApiImplicitParam(name = "companyId",value = "企业Id", paramType = "query", required = true,dataType = "long")  ,
            @ApiImplicitParam(name = "groupId",value = "企业的群组ID", paramType = "query",  dataType = "long")
    })
    public R queryRiskCountForGent(@ApiIgnore @RequestParam(required = false ) Map<String, Object> params) {
        params.put("companyId",getCompanyId());
        params.put("groupId",getGroupId());
        Map<String, Object> result = this.companyService.queryRiskCountForGent(params);
        return R.ok().put("data", result);
    }

    /**
     * 隐患总计
     * @param params
     * @return
     */
    @GetMapping({"/gent/hiddenCount"})
    @ApiOperation(value="【集团首页】隐患总计",notes = "返回值说明：<br>" +
            "total(总隐患数量),<br>" +
            "biggest(重大数量),<br>" +
            "bigger(较大数量),<br>" +
            "third(一般数量),<br>" +
            "fourth(较低数量),<br>" +
            "rate(整改完成率),<br>" +
            "status2(待整改数量),<br>" +
            "status3(待验收数量),<br>" +
            "status4(已完成数量),<br>"
    )
    @ApiImplicitParams({
            @ApiImplicitParam(name = "companyId",value = "企业Id", paramType = "query", required = true,dataType = "long")  ,
            @ApiImplicitParam(name = "groupId",value = "企业的群组ID", paramType = "query",  dataType = "long")
    })
    public R queryHiddenDangerCountForGent(@ApiIgnore @RequestParam(required = false ) Map<String, Object> params) {
        params.put("companyId",getCompanyId());
        params.put("groupId",getGroupId());
        Map<String, Object> result = this.companyService.queryHiddenDangerCountForGent(params);
        return R.ok().put("data", result);
    }

    /**
     * 查询隐患通报
     * @param params
     * @return
     */
    @GetMapping({"/gent/hiddenNotice"})
    @ApiOperation(value="【集团首页】查询隐患通报",notes = "返回值说明：<br>" +
            "hiddenRetifyGroupName(整改部门),<br>" +
            "hiddenRetifyDeadline(整改期限),<br>" +
            "companyName(公司名称),<br>" +
            "hiddenTitle(隐患标题),<br>" +
            "hiddenRetifyBy(整改人),<br>"
    )
    @ApiImplicitParams({
          @ApiImplicitParam(name = "companyId",value = "企业Id", paramType = "query", required = true,dataType = "long")  ,
          @ApiImplicitParam(name = "groupId",value = "企业的群组ID", paramType = "query",  dataType = "long")
    })
    public R queryHiddenDanger(@ApiIgnore @RequestParam(required = false ) Map<String, Object> params) {
        params.put("companyId",getCompanyId());
        params.put("groupId",getGroupId());
        List<Map<String, Object>> list = this.companyService.queryHiddenForGent(params);
        return R.ok().put("data", list);
    }

    /**
     * @Author: lxf
     * @Description:重置管理员密码
     * @Param
     * @Date 09:40 2020/9/21
     */
    @PostMapping("/resetAdminPwd/{companyId}")
    @ApiOperation(value="【集团级】--企业列表--重置管理员密码")
    @ApiImplicitParam(name = "companyId", value = "企业id", required = true, paramType = "path", dataType = "Long")
    public R resetAdminPwd(@PathVariable("companyId") Long companyId){
        CompanyDto companyById = companyService.getCompanyById(companyId);
        if (null==companyById){
            R.error("企业不存在");
        }else {
            User user=new User();
            Long companyAdminId = companyById.getCompanyAdminId();
            //sha256加密
            String salt = RandomStringUtils.randomAlphanumeric(20);
            user.setSalt(salt);
            String password= ShiroUtils.sha256(Constant.USER_PASSWORD, salt);
            user.setPassword(password);
            orgService.resetAdminPwd(companyAdminId,user.getPassword(),companyId,user.getSalt());
        }
        return R.ok();
    }

}
