package com.hngf.web.controller.org;

import com.hngf.common.utils.Constant;
import com.hngf.common.utils.PageUtils;
import com.hngf.common.utils.R;
import com.hngf.dto.sys.CompanyDto;
import com.hngf.entity.sys.Company;
import com.hngf.entity.sys.User;
import com.hngf.service.sys.CompanyService;
import com.hngf.service.sys.IndustryService;
import com.hngf.service.sys.OrgService;
import com.hngf.service.sys.UserService;
import com.hngf.web.common.shiro.ShiroUtils;
import com.hngf.web.controller.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 局级 - 企业管理
 */
@RestController
@RequestMapping("/org/company")
@Api(value = "【监管级】企业管理",tags = {"【监管级】企业管理"})
public class OrgCompanyController extends BaseController {
    @Autowired
    private CompanyService companyService;

    @Autowired
    private IndustryService industryService;

    @Autowired
    private UserService userService;

    @Autowired
    private OrgService orgService;

    /**
     * 首页风险分析
     * @return
     */
    @GetMapping("/orgRiskAnalysis")
    @ApiOperation(value="首页风险分析",notes = "返回数据：" +
            "行业领域：companyData <br>" +
            "风险分析：companyNumber <br>" +
            "企业/单位总数：totalCount <br>" +
            "重大风险企业：one  较大风险企业：two   一般风险：three 较低风险：four <br>" +
            "未整改重大隐患：oneLevel  已整改重大隐患：oneLevelFinish <br>" +
            "未整改一般隐患：threeLevel， 已整改一般隐患：threeLevelFinish")
    public R comTreeList() {
        Long companyId = this.getCompanyId();
        Map<String, Object> map  = this.companyService.getOrgRiskAnalysis(companyId);
         return R.ok().put("data", map);
    }

    @ApiOperation(value = "【监管级】企业列表",notes = "企业本身及所辖下级公司tree"+
            "企业列表接口关联接口如下：<br>" +
            "1、搜索框-行业下拉框tree：/org/company/queryIndustryTree  <br>")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum", value = "第几页", required = true, paramType = "query", dataType = "int",defaultValue = "1"),
            @ApiImplicitParam(name = "pageSize", value = "每页多少条数据", required = true, paramType = "query", dataType = "int", defaultValue = "10" ),
            @ApiImplicitParam(name = "industryCode", value = "行业编码", paramType = "query", dataType = "String" ),
            @ApiImplicitParam(name = "keyword", value = "查询关键字", paramType = "query", dataType = "String")
    })
    @RequestMapping(value = "/query", method = RequestMethod.GET)
    public R pageList(@ApiIgnore @RequestParam(required = false) Map<String,Object> params){
        // 根据当前机构Id获取到机构所监管的行业
        String industryCodes = this.industryService.getIndustryCodeByOrgId(getCompanyId());
        // 当前监管机构 有监管行业
        if (StringUtils.isNotBlank(industryCodes)) {
            params.put("industryCodes", industryCodes);
            PageUtils page = this.companyService.findListByindustryTypeCode(params);
            return R.ok().put("data",page);
        }else{
            return R.ok().put("data",new PageUtils(new ArrayList<>(),0,1,10, 1) );
        }
    }


    /**
     * 根据企业id回显企业信息
     * yfh
     * 2020/06/05
     * @param companyId
     * @return
     */
    @ApiOperation(value = "回显企业信息", notes="回显指定Id企业信息"  +
            "企业列表接口关联接口如下：<br>" +
            "1、公司类型：" +
            "2、评价房师：" +
            "3、行业类别：：/sys/company/queryIndustryList  <br>" +
            "4、监察机构：" +
            "5、监管分类：" +
            "5、经济类型：" +
            "6、行政区：" +
            "7、状态：" +
            "8、安全机构设置情况：" +
            "9、规模情况：" +
            "10：规模：", response = CompanyDto.class)
    @RequestMapping(value="/info/{companyId}",method = RequestMethod.GET)
    public R info(@PathVariable("companyId") Long companyId, HttpServletRequest req){
        return R.ok().put("data", companyService.getById(companyId)).put("path",req.getScheme() + "://" + req.getServerName() + ":" + req.getServerPort() + "/source");
    }

    /**
     * @Author: lxf
     * @Description: 企业评分 其自身和所辖企业的风险、隐患统计 评价考核
     * @Param
     * @Date 18:10 2020/9/5
     */
    @ApiOperation(value="【监管级】企业评分", notes = "其自身和所辖企业的风险、隐患统计 ; 所属模块：评价考核")
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
     * 行业Tree树查询
     * yfh
     * 2020/06/05
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "【监管级】--监管机构监管的行业树查询", notes="当前机构监管的所有行业查询结果tree")
    @RequestMapping(value = {"/queryIndustryTree"},method = {RequestMethod.GET})
    public R queryIndustryTree() throws Exception {
        return R.ok().put("data",industryService.queryTreeList(getCompanyId()));
    }

    /**
     * 查看管理员账号信息 及角色信息
     * lxf
     * 2020/09/21
     * @param companyId
     * @return
     */
    @ApiOperation(value = "【监管级】--企业管理员账号信息", notes="管理员账号信息")
    @ApiImplicitParam(name = "companyId", value = "企业Id",paramType = "query" ,required = true ,dataType = "long")
    @RequestMapping(value = {"/getAdmin"},method = {RequestMethod.GET})
    public R getAdmin( Long companyId) {
        if(null == companyId ){
            return R.error("公司Id不能为空");
        }
        Company company = companyService.getCompanyId(companyId);
        if (company == null) {
            return R.error("公司不存在");
        } else {
            Long userId = company.getCompanyAdminId();
            if(userId==null){
                return R.error("用户不存在");
            }else{
                List<Map<String,Object>> userAndRoles=userService.findUserAndRole(userId);
                if(null != userAndRoles && null != userAndRoles.get(0) ){
                    userAndRoles.get(0).remove("roleId");
                    userAndRoles.get(0).remove("userId");
                }
                return R.ok().put("data", userAndRoles);
            }
        }
    }

    /**
     * @Author: lxf
     * @Description:重置管理员密码
     * @Param
     * @Date 09:40 2020/9/21
     */
    @PostMapping("/resetAdminPwd/{companyId}")
    @ApiOperation(value="【监管级】--企业列表--重置管理员密码")
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

    @GetMapping("/findCompanyListByIndustryCode")
    @ApiOperation(value="【监管级】--行业大数据-企业分布 根据行业查询全部企业")
    @ApiImplicitParam(name = "industryCode", value = "行业编码", required = true, paramType = "query", dataType = "Long")
    public R findCompanyList(String industryCode) {
        try {
            Map<String, Object> param = new HashMap();
            List<Map<String, Object>> list = null;
            PageUtils page = null;
            param.put("pageSize",1000);
            if (StringUtils.isBlank(industryCode)) {
                String industryCodes = this.industryService.getIndustryCodeByOrgId(getCompanyId());
                if (StringUtils.isNotBlank(industryCodes)) {
                    param.put("industryCodes", industryCodes);
                    page= this.companyService.findListByindustryTypeCode(param);
                    // list = this.companyService.findListByHylbdm(param);
                }
            } else {
                param.put("industryCode", industryCode);
                page = this.companyService.findListByindustryTypeCode(param);
            }
           return  R.ok().put("data",page.getList());
        } catch (Exception var5) {
            var5.printStackTrace();
             return  R.error("查询出错");
        }
    }
}
