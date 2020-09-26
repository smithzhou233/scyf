package com.hngf.web.controller.sys;

import com.hngf.common.exception.ScyfException;
import com.hngf.common.utils.Constant;
import com.hngf.common.utils.PageUtils;
import com.hngf.common.utils.R;
import com.hngf.common.validator.ValidatorUtils;
import com.hngf.common.validator.group.AddGroup;
import com.hngf.common.validator.group.UpdateGroup;
import com.hngf.dto.sys.OrgDto;
import com.hngf.dto.sys.OrgTreeDto;
import com.hngf.entity.sys.Dict;
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
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 组织机构
 *
 * @author hngf
 * @email 
 * @date 2020-05-20 17:31:55
 */
@RestController
@RequestMapping("sys/org")
@Api(value = "机构管理",tags = {"机构管理"})
public class OrgController extends BaseController {
    @Autowired
    private OrgService orgService;
    @Autowired
    private UserService userService;
    @Autowired
    private IndustryService industryService;
    @Autowired
    private OrgIndustryService orgIndustryService;

    /**
     * 分页列表
     */
    @RequestMapping(value = "/list",method = RequestMethod.GET)
    @RequiresPermissions("sys:org:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = orgService.queryPage(params);

        return R.ok().put("data", page);
    }
    /**
     * @Author: zyj
     * @Description:获取所有行政机构
     * @Param
     * @Date 15:43 2020/6/3
     */
    @ApiOperation(value="获取所有行政机构")
    @RequestMapping(value = "/getAllOrgs",method = RequestMethod.GET)
    @RequiresPermissions("sys:org:list")
    public R getAllOrgs(){
        List<OrgTreeDto> tbCategories= orgService.getAllOrgs();
        // 整体思路：
        // 1、取得所有数据、放入集合List1 （tbCategories）
        // 2、将List1所有数据都放入到map（treeMap）中：元素id为键，元素本身对象为值
        // 3、取得顶层节点放入集合List2中（resultList）
        // 4、遍历List1中的所有数据，通过数据的parentId为键在map中取值
        //      1）如果能取到，则说明该元素有父节点
        //           1、判断该父节点下的childList中是否有已经子节点
        //              1、若无：则创建一个集合，将子节点放入
        //              2、若有：则直接将子节点放入即可
        // 5、把放好的数据放回到map中
        // 6、返回List2（resultList）

        // 注意：整个过程将所有数据取出放入list2（resultList）,返回的也是    //list2

        List<OrgTreeDto> resultList = new ArrayList<OrgTreeDto>(); // 存贮顶层的数据

        Map<Object ,Object> treeMap = new HashMap();
        Object itemTree;

        for(int i = 0;i<tbCategories.size() && !tbCategories.isEmpty();i++){
            itemTree = tbCategories.get(i);
            treeMap.put(tbCategories.get(i).getOrgId(),tbCategories.get(i));// 把所有的数据都放到map中


        }


// 这里也可以用另一种方法，就是拿到集合里的每个元素的父id去数据库中查询，但是，这样与数据库的交互次数就太多了
        // 遍历map得到顶层节点（游离节点也算作顶层节点）
        for(int i =0;i<tbCategories.size();i++){
            // 优点1：整个方法，只查询了一次数据库
            // 优点2：不用知道顶层节点的id
            if(!treeMap.containsKey(tbCategories.get(i).getOrgParentId())){
                // 我们在存储的时候就是将元素的id为键，元素本身为值存入的
                // 以元素的父id为键，在map里取值，若取不到则，对应的元素不存在，即没有父节点，为顶层节点或游离节点
                // 将顶层节点放入list集合
                resultList.add(tbCategories.get(i));
            }
        }

        // 循环数据，将数据放到该节点的父节点的children属性中
        for(int i =0 ;i<tbCategories.size()&& !tbCategories.isEmpty();i++){
            // 数据库中，若一个元素有子节点，那么，该元素的id为子节点的父id
            //treeMap.get(tbCategories.get(i).getParentId()); // 从map集合中找到父节点
            OrgTreeDto category = (OrgTreeDto)treeMap.get(tbCategories.get(i).getOrgParentId());
            if(category!=null ){ // 不等于null，也就意味着有父节点
                // 有了父节点，要判断父节点下存贮字节点的集合是否存在，然后将子节点放入
                if(category.getChildren() == null){
                    // 判断一个集合是否被创建用null：表示结合还没有被分配内存空间(即还没有被创建)，内存大小自然为null
                    // 用集合的size判断集合中是否有元素，为0，没有元素（集合已经被创建），
                    category.setChildren(new ArrayList<OrgTreeDto>());
                }
                category.getChildren().add(tbCategories.get(i)); // 添加到父节点的ChildList集合下

                // 这一步其实可以不要，因为我们修改了数据（添加了子节点，然后在将元素放入到map中，
                // 若键相同，map会自动覆盖掉相同的键值对，达到更新map集合中的数据的目的），但是我们
                // 这里只是从map中取值，而并不关心值的子节点（子节点是对象本身自己封装的。这里我们知道
                // 元素从查询后放入map,父节点放入list，然后通过键来在map中取得对象，之后再将修改过的对象重新放入map当中
                // ,我们并没有直接操作list,但是在list中对象的值却是已经修改过了，这就是对象的引用传递，同一个引用对象是通过
                // 地址值来操作对象的，即有不同的引用，但是对象中的属性是已经通过引用的操作而改变的，所以这里一旦修改过后，无论是map中还是list中，再次取值时都已经是更改过后的值了）
                treeMap.put(tbCategories.get(i).getOrgParentId(),category);  // 把放好的数据放回到map中
            }

        }


        return R.ok().put("data",resultList);
    }


    /**
     * @Author: zyj
     * @Description:查询机构类型数据
     * @Param dictType 字典类型
     * @Date 16:28 2020/6/3
     */
    @ApiOperation(value="查询机构类型数据")
    @RequestMapping(value = "/dictType",method = RequestMethod.GET)
    public R dictType(){
        //机构类型
        String dictType="organization_type";
       List<Dict> list=orgService.dictType(dictType);
       return R.ok().put("data",list);
    }
    /**
     * 树形列表
     */
    @RequestMapping(value = "/listtree",method = RequestMethod.GET)
    @RequiresPermissions("sys:org:list")
    @ApiOperation(value="树形列表")
    public R listtree(){
        Map<String, Object> params = new HashMap<>();
        List<Org> list = orgService.getList(params);
        List<OrgDto> rootOrg = new ArrayList<>();
        list.forEach(root ->{
            if (root.getOrgParentId() == 0L) {
                OrgDto org = new OrgDto();
                org.setOrgId(root.getOrgId());
                org.setOrgName(root.getOrgName());
                org.setOrgCode(root.getOrgCode());
                org.setOrgParentId(root.getOrgParentId());
                org.setOrgTypeId(root.getOrgTypeId());
                org.setOrgLevel(root.getOrgLevel());
                org.setOrgAreaCode(root.getOrgAreaCode());
                org.setOrgAreaName(root.getOrgAreaName());
                org.setOrgGroupId(root.getOrgGroupId());
                org.setOrgAdminId(root.getOrgAdminId());
                org.setOrgRootId(root.getOrgRootId());
                rootOrg.add(org);
            }
        });

        //如果顶级菜单有数据，开始查找子节点
        if (null != rootOrg && rootOrg.size() > 0) {
            rootOrg.forEach(root ->{
                //子节点递归查找添加  传递父节点
                root.setChildren(getChildren(root.getOrgId(),list));
            });
        }

        list.clear();

        return R.ok().put("data", rootOrg);
    }

    /**
     * 信息
     */
    @RequestMapping(value="/info/{orgId}",method = RequestMethod.GET)
    @RequiresPermissions("sys:org:info")
    @ApiOperation(value="信息")
    @ApiImplicitParam(name = "orgId", value = "机构ID", paramType = "path", required = true, dataType = "integer")
    public R info(@PathVariable("orgId") Long orgId){
        Org Org = orgService.getById(orgId);
        return R.ok().put("data", Org);
    }

    /**
     * 保存
     */
    @PostMapping("/save")
    @RequiresPermissions("sys:org:save")
    @ApiOperation(value="保存")
    @SysLog("保存组织机构")
    public R save(@RequestBody Org org) throws Exception {
        ValidatorUtils.validateEntity(org, AddGroup.class);
        org.addPrefixInit(getUserId());
        orgService.save(org);
        return R.ok();
    }

    /**
     * 修改
     */
    @PostMapping("/update")
    @RequiresPermissions("sys:org:update")
    @ApiOperation(value="修改")
    @SysLog("修改组织机构")
    public R update(@RequestBody Org org){
        ValidatorUtils.validateEntity(org, UpdateGroup.class);
        org.updatePrefixInit(getUserId());
        orgService.update(org);
        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping(value="/deletes",method = RequestMethod.DELETE)
    @RequiresPermissions("sys:org:delete")
    @ApiOperation(value="删除")
    @SysLog("删除组织机构")
    @ApiImplicitParam(name = "orgIds", value = "ID数组集合", paramType = "json", required = true, dataType = "数组")
    public R deletes(@RequestBody Long[] orgIds){
        orgService.removeByIds(Arrays.asList(orgIds));
        return R.ok();
    }
    /**
     * @Author: zyj
     * @Description:删除组织机构
     * @Param
     * @Date 8:47 2020/6/4
     */
    @RequestMapping(value="/delete/{orgId}",method = RequestMethod.DELETE)
    @RequiresPermissions("sys:org:delete")
    @ApiOperation(value="删除")
    @SysLog("删除组织机构")
    public R updateOrgDelFlg(@PathVariable("orgId") Long orgId) throws Exception {
       OrgTreeDto orgTreeDto= orgService.getOrgById(orgId);
       orgTreeDto.setDelFlag(1);
       orgService.updateOrgDelFlg(orgTreeDto);
       return R.ok();
    }
    /**
    * @Author: zyj
    * @Description:添加管理员
    * @Param
    * @Date 11:18 2020/6/4
    */
    @PostMapping("/insertOrgAdmin")
    @RequiresPermissions("sys:org:save")
    @ApiOperation(value="保存")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "loginName", value = "登录名称", required = true, paramType = "json", dataType = "string"),
            @ApiImplicitParam(name = "password", value = "登录密码", required = true, paramType = "json", dataType = "string"),
            @ApiImplicitParam(name = "userMobile", value = "手机号码", required = false, paramType = "json", dataType = "long"),
            @ApiImplicitParam(name = "companyId", value = "企业id", required = true, paramType = "json", dataType = "long")

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
    @RequestMapping(value = "/checkedLoginName/{loginName}",method = RequestMethod.GET)
    @RequiresPermissions("sys:org:list")
    @ApiOperation(value="检查用户名是否存在")
    @ApiImplicitParam(name = "loginName", value = "用户名", required = true, paramType = "query", dataType = "String")
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
    @PostMapping("updateOrgAdminName")
    @RequiresPermissions("sys:org:update")
    @ApiOperation(value="修改组织管理员信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "companyId", value = "企业id", required = true, paramType = "json", dataType = "Long"),
            @ApiImplicitParam(name = "loginName", value = "登录名", required = false, paramType = "json", dataType = "string"),
            @ApiImplicitParam(name = "userMobile", value = "手机号码", required = false, paramType = "json", dataType = "string"),
            @ApiImplicitParam(name = "userId", value = "用户id", required = true, paramType = "json", dataType = "Long")

    })
    @SysLog("修改组织管理员信息")
    public R updateOrgAdminName(Integer companyId, String loginName, String userMobile, Integer userId){
        orgService.updateOrgAdminName(companyId,loginName,userMobile,userId);
        return R.ok();
    }
    /**
    * @Author: zyj
    * @Description:点击行业显示的行业树带checked值
    * @Param
    * @Date 17:38 2020/6/4
    */
    @RequestMapping(value = "orgIndustryTree",method = RequestMethod.GET)
    @RequiresPermissions("sys:org:list")
    @ApiOperation(value="点击行业显示的行业树带checked值")
    @ApiImplicitParam(name = "orgId", value = "组织id", required = true, paramType = "query", dataType = "Long")
    public R orgIndustryTree(@RequestParam("orgId") Long orgId) throws Exception {

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
    * @Author: zyj
    * @Description:给组织添加行业
    * @Param
    * @Date 16:40 2020/6/4
    */
    @PostMapping("/insertOrgIndustry")
    @RequiresPermissions("sys:org:save")
    @ApiOperation(value="保存")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "orgId", value = "组织id", required = true, paramType = "query", dataType = "Long"),
            @ApiImplicitParam(name = "industryId", value = "所选的行业集合字符串", required = true, paramType = "query", dataType = "string")
    })
    public R insertOrgIndustry(Long orgId,String industryId){
        //获取当前登录人id
         Long userId=getUserId();
         orgService.insertOrgIndustry(orgId,industryId,userId);
         return R.ok();
    }
    /**
    * @Author: zyj
    * @Description:重置管理员密码
    * @Param
    * @Date 18:10 2020/6/4
    */
    @PostMapping("/resetAdminPwd")
    @RequiresPermissions("sys:org:save")
    @ApiOperation(value="重置管理员密码")
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
            String password=ShiroUtils.sha256(Constant.USER_PASSWORD, salt);
            user.setPassword(password);
            orgService.resetAdminPwd(orgAdminId,user.getPassword(),orgId,user.getSalt());
        }
        return R.ok();
    }
    //递归获取children节点
    @ApiIgnore()
    private List<OrgDto> getChildren(Long pid, List<Org> list) {

        List<OrgDto> children = new ArrayList<>();

        if (null != pid){

            list.forEach(data -> {
                //若遍历的数据中的父节点id等于参数id
                //则判定当前节点为该参数id节点下的子节点
                if (data.getOrgParentId().longValue() == pid.longValue()) {
                    //构造添加结果集合
                    OrgDto org = new OrgDto();
                    org.setOrgId(data.getOrgId());
                    org.setOrgName(data.getOrgName());
                    org.setOrgCode(data.getOrgCode());
                    org.setOrgParentId(data.getOrgParentId());
                    org.setOrgTypeId(data.getOrgTypeId());
                    org.setOrgLevel(data.getOrgLevel());
                    org.setOrgAreaCode(data.getOrgAreaCode());
                    org.setOrgAreaName(data.getOrgAreaName());
                    org.setOrgGroupId(data.getOrgGroupId());
                    org.setOrgAdminId(data.getOrgAdminId());
                    org.setOrgRootId(data.getOrgRootId());

                    children.add(org);
                }
            });


        }

        //如果children不为空，继续递归遍历children下的子节点
        if (children.size() > 0) {
            children.forEach(data -> {
                data.setChildren(getChildren(data.getOrgId(),list));
            });
        }
        return children;
    }

}
