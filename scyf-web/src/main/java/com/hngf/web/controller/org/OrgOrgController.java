package com.hngf.web.controller.org;

import com.hngf.common.exception.ScyfException;
import com.hngf.common.utils.Constant;
import com.hngf.common.utils.R;
import com.hngf.common.validator.ValidatorUtils;
import com.hngf.common.validator.group.AddGroup;
import com.hngf.dto.sys.OrgTreeDto;
import com.hngf.entity.sys.Industry;
import com.hngf.entity.sys.Org;
import com.hngf.entity.sys.OrgIndustry;
import com.hngf.entity.sys.User;
import com.hngf.service.sys.IndustryService;
import com.hngf.service.sys.OrgIndustryService;
import com.hngf.service.sys.OrgService;
import com.hngf.service.sys.UserService;
import com.hngf.web.common.annotation.SysLog;
import com.hngf.web.common.shiro.ShiroUtils;
import com.hngf.web.controller.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/org/org")
@Api(value = "【监管级】--行业管理",tags = {"【监管级】行业管理"})
public class OrgOrgController  extends BaseController {
    @Autowired
    private OrgService orgService;

    @Autowired
    private OrgIndustryService orgIndustryService;

    @Autowired
    private IndustryService industryService;

    @Autowired
    private UserService userService;

    @GetMapping("/queryOrgList")
    @ApiOperation(value="【监管级】机构管理--机构Tree", response = OrgTreeDto.class)
    public R queryOrgList()  {
        Long currentOrgId = getCompanyId();
        List<OrgTreeDto>    orgList = new ArrayList<>();
        if (null != currentOrgId && !currentOrgId.equals(0L)) {
            Map<String, Object> params = new HashMap<>();
            params.put("orgId",currentOrgId);
           orgList = this.orgService.findListByParentId(params);
        }

        Map<Object ,Object> treeMap = new HashMap();
        List<OrgTreeDto> resultList = new ArrayList<OrgTreeDto>(); // 存贮顶层的数据
        Object itemTree;
        for(int i = 0;i<orgList.size() && !orgList.isEmpty();i++){
            itemTree = orgList.get(i);
            treeMap.put(orgList.get(i).getOrgId(),orgList.get(i));// 把所有的数据都放到map中
        }

        for(int i =0;i<orgList.size();i++){
            if(!treeMap.containsKey(orgList.get(i).getOrgParentId())){
                resultList.add(orgList.get(i));
            }
        }
        for(int i =0 ;i<orgList.size()&& !orgList.isEmpty();i++){
            OrgTreeDto category = (OrgTreeDto)treeMap.get(orgList.get(i).getOrgParentId());
            if(category!=null ){ // 不等于null，也就意味着有父节点
                if(category.getChildren() == null){
                    category.setChildren(new ArrayList<OrgTreeDto>());
                }
                category.getChildren().add(orgList.get(i)); // 添加到父节点的ChildList集合下
                treeMap.put(orgList.get(i).getOrgParentId(),category);  // 把放好的数据放回到map中
            }
        }
        return R.ok().put("data",resultList);
    }

    @ApiOperation(value="【监管级】机构管理--添加机构" ,notes = "机构类型字典查询：sys/org/dictType")
    @RequestMapping(value = {"/save"}, method = {RequestMethod.POST} )
    public R save(@RequestBody Org org){
        ValidatorUtils.validateEntity(org, AddGroup.class);
        org.addPrefixInit(getUserId());
        orgService.save(org);
        return R.ok();
    }
    /**
     * 信息
     */
    @ApiOperation(value="【监管级】机构管理--机构信息")
    @ApiImplicitParam(name = "orgId", value = "机构ID", paramType = "path", required = true, dataType = "long")
    @RequestMapping(value="/info/{orgId}",method = RequestMethod.GET)
    public R info(@PathVariable("orgId") Long orgId){
        Org Org = orgService.getById(orgId);
        return R.ok().put("data", Org);
    }

    /**
     * 修改
     */
    @ApiOperation(value="【监管级】机构管理--机构修改" ,notes = "机构类型字典查询：sys/org/dictType")
    @SysLog("修改组织机构")
    @PostMapping("/update")
    public R update(@RequestBody Org org){
        ValidatorUtils.validateEntity(org);
        org.updatePrefixInit(getUserId());
        orgService.update(org);
        return R.ok();
    }
    @ApiOperation(value="【监管级】机构管理--机构删除")
    @SysLog("删除组织机构")
    @RequestMapping(value="/delete/{orgId}",method = RequestMethod.DELETE)
    public R updateOrgDelFlg(@PathVariable("orgId") Long orgId) {
        try {
            this.orgService.removeById(orgId);
        } catch (Exception var5) {
            var5.printStackTrace();
            return R.error("删除失败");
        }
        return R.ok("删除成功");
    }

    /**
     * @Author: lxf
     * @Description:点击行业显示的行业树带checked值
     * @Param
     * @Date 15:38 2020/9/17
     */
    @RequestMapping(value = "orgIndustryTree",method = RequestMethod.GET)
    @ApiOperation(value="【监管级】机构管理--点击行业显示的行业树带checked值")
    @ApiImplicitParam(name = "orgId", value = "组织id", required = true, paramType = "query", dataType = "Long")
    public R orgIndustryTree(@RequestParam(value = "orgId") Long orgId) throws Exception {
        List<OrgIndustry> list1=orgIndustryService.orgIndustryChecked(orgId);
        Integer[] checked=new Integer[list1.size()];
        if (list1.size()>0){
            for (int i = 0; i < list1.size(); i++) {
                checked[i]=Integer.valueOf(list1.get(i).getIndustryId().toString());
            }
        }

        return R.ok().put("data",industryService.queryTreeList(null)).put("checked",checked);
    }

    /**
     * @Author: lxf
     * @Description:给组织添加行业
     * @Param
     * @Date 14:40 2020/9/17
     */

    @ApiOperation(value="【监管级】机构管理-添加监管行业")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "orgId", value = "组织id", required = true, paramType = "query", dataType = "Long"),
            @ApiImplicitParam(name = "industryId", value = "所选的行业集合字符串", required = true, paramType = "query", dataType = "string")
    })
    @PostMapping("/insertOrgIndustry")
    public R insertOrgIndustry(Long orgId,String industryId){
        //获取当前登录人id
        Long userId=getUserId();
        orgService.insertOrgIndustry(orgId,industryId,userId);
        return R.ok();
    }
    /**
     * @Author: zyj
     * @Description:添加管理员
     * @Param
     * @Date 11:18 2020/6/4
     */
    @PostMapping("/insertOrgAdmin")
    @ApiOperation(value="【监管级】机构管理-添加机构管理员", notes = "校验登录名是否存在：/org/org/checkedLoginName/{loginName} </br>")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "loginName", value = "登录名称", paramType = "json", dataType = "string"),
            @ApiImplicitParam(name = "password", value = "登录密码",  paramType = "json", dataType = "string"),
            @ApiImplicitParam(name = "userMobile", value = "手机号码", paramType = "json", dataType = "long"),
            @ApiImplicitParam(name = "companyId", value = "监管机构Id", paramType = "json", dataType = "long")
    })
    @SysLog("组织机构添加管理员")
    public R insertOrgAdmin(@RequestBody User user){
        if(null == user || null == user.getLoginName() || StringUtils.isBlank(user.getLoginName())){
            throw  new ScyfException("用户名不能为空");
        }
        if(user.getLoginName().length()>200){
            throw  new ScyfException("用户名的长度不能超过200");
        }
        if(null != user.getPassword() && user.getPassword().length()>100){
            throw  new ScyfException("用户密码的长度不能超过100");
        }
        user.insertPrefix(getUserId());
        //sha256加密
        String salt = RandomStringUtils.randomAlphanumeric(20);
        user.setSalt(salt);
        String password=user.getPassword();
        user.setPassword(ShiroUtils.sha256(password, user.getSalt()));
        orgService.insertOrgAdmin(user);
        return R.ok();
    }

    /**
     * @Author: zyj
     * @Description:检查用户名是否存在
     * @Param
     * @Date 9:16 2020/6/5
     */

    @ApiOperation(value="【监管级】机构管理--检查用户名是否存在")
    @ApiImplicitParam(name = "loginName", value = "用户名", required = true, paramType = "query", dataType = "String")
    @RequestMapping(value = "/checkedLoginName/{loginName}",method = RequestMethod.GET)
    public R checkedLoginName(@PathVariable("loginName") String loginName){
        User check = userService.getByLoginName(loginName);
        if (null != check) {
            throw new ScyfException(loginName + " 已存在！");
        }
        return R.ok("用户名可以使用");
    }
    /**
     * @Author: zyj
     * @Description:修改组织管理员信息
     * @Param companyId 企业id  loginName登录名称 userMobile用户电话  userId用户id
     * @Date 14:56 2020/6/8
     */

    @ApiOperation(value="【监管级】机构管理--修改监管机构的管理员信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "companyId", value = "监管机构Id", required = true, paramType = "json", dataType = "Long"),
            @ApiImplicitParam(name = "loginName", value = "登录名", required = false, paramType = "json", dataType = "string"),
            @ApiImplicitParam(name = "userMobile", value = "手机号码", required = false, paramType = "json", dataType = "string"),
            @ApiImplicitParam(name = "userId", value = "用户Id", required = true, paramType = "json", dataType = "Long")

    })
    @SysLog("修改组织管理员信息")
    @PostMapping("updateOrgAdminName")
    public R updateOrgAdminName(Integer companyId, String loginName, String userMobile, Integer userId){
        orgService.updateOrgAdminName(companyId,loginName,userMobile,userId);
        return R.ok();
    }

    /**
     * 重置管理员密码
     */
    /**
     * @Description:重置管理员密码
     */
    @PostMapping("/resetAdminPwd")
    @ApiOperation(value="【监管级】机构管理--重置管理员密码")
    @ApiImplicitParam(name = "orgId", value = "组织id", required = true, paramType = "query", dataType = "Long")
    public R resetAdminPwd(@RequestParam("orgId") Long orgId){
        OrgTreeDto orgTreeDto= orgService.getOrgById(orgId);
        if (orgTreeDto==null){
            R.error("机构不存在");
        }else {
            User user=new User();
            Long orgAdminId= orgTreeDto.getOrgAdminId();
            //sha256加密
            String salt = RandomStringUtils.randomAlphanumeric(20);
            user.setSalt(salt);
            String password= ShiroUtils.sha256(Constant.USER_PASSWORD, salt);
            user.setPassword(password);
            orgService.resetAdminPwd(orgAdminId,user.getPassword(),orgId,user.getSalt());
        }
        return R.ok();
    }

    /**
     * @Author: lxf
     * @Description: 监管机构下 监管的所有行业列表
     * @Param
     * @Date 18:38 2020/9/21
     */
    @RequestMapping(value = "orgIndustryList",method = RequestMethod.GET)
    @ApiOperation(value="【监管级】机构管理--行业列表", notes = "监管机构 监管的行业列表Tree,只做展示，展示该机构监管的行业列表 用作行业大数据模块中行业下拉框的内容",response = Industry.class)
    public R orgIndustryListTree(HttpServletRequest req) {
        // 获取当前监管机构的Id
        Long curOrgId = getCompanyId();
        List<Industry> queryTreeList = industryService.queryTreeList(curOrgId);
        return R.ok().put("data",queryTreeList).put("path",req.getScheme() + "://" + req.getServerName() + ":" + req.getServerPort() + "/source");
    }

}
