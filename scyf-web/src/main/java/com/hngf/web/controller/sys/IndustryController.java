package com.hngf.web.controller.sys;

import com.hngf.common.utils.Constant;
import com.hngf.common.utils.PageUtils;
import com.hngf.common.utils.R;
import com.hngf.common.validator.ValidatorUtils;
import com.hngf.entity.sys.Dict;
import com.hngf.entity.sys.Industry;
import com.hngf.service.sys.IndustryService;
import com.hngf.web.common.annotation.SysLog;
import com.hngf.web.controller.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.Logical;
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
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 行业领域表
 *
 * @author hngf
 * @email 
 * @date 2020-06-03 10:47:12
 */
@Api(value="行业领域表",tags = {"行业领域表"})
@RestController
@RequestMapping("sys/industry")
public class IndustryController extends BaseController{
    @Autowired
    private IndustryService industryService;

    /**
     * 列表
     */
    @ApiOperation(value = "行业领域表", notes="查询行业领域表数据")
    @RequestMapping(value = "/list",method = RequestMethod.GET)
    @RequiresPermissions("sys:industry:list")
    public R list(@RequestParam Map<String, Object> params){

        int pageNum = null!= params.get(Constant.PAGE_PAGENUM) ? Integer.parseInt(params.get(Constant.PAGE_PAGENUM).toString()) : 1;
        int pageSize = null != params.get(Constant.PAGE_PAGESIZE) ? Integer.parseInt(params.get(Constant.PAGE_PAGESIZE).toString()) : Constant.PAGE_SIZE;

        PageUtils page = industryService.queryPage(params,pageNum,pageSize,null);

        return R.ok().put("data", page);
    }
    /**
     * @Author: zyj
     * @Description:行业管理 列表显示树结构
     * @Param
     * @Date 11:00 2020/6/3
     */
    @ApiOperation(value = "列表显示树结构", notes="列表显示树结构")
    @RequestMapping(value = "/queryTreeList",method = RequestMethod.GET)
    @RequiresPermissions("sys:industry:list")
    public R queryTreeList() throws Exception {
        List<Industry> tbCategories=industryService.queryTreeList(null);
        List<Industry> resultList = new ArrayList<Industry>(); // 存贮顶层的数据

        Map<Object ,Object> treeMap = new HashMap();
        Object itemTree;
        for(int i = 0;i<tbCategories.size() && !tbCategories.isEmpty();i++){
            itemTree = tbCategories.get(i);
            treeMap.put(tbCategories.get(i).getIndustryId(),tbCategories.get(i));// 把所有的数据都放到map中


        }


       // 这里也可以用另一种方法，就是拿到集合里的每个元素的父id去数据库中查询，但是，这样与数据库的交互次数就太多了
        // 遍历map得到顶层节点（游离节点也算作顶层节点）
        for(int i =0;i<tbCategories.size();i++){
            // 优点1：整个方法，只查询了一次数据库
            // 优点2：不用知道顶层节点的id
            if(!treeMap.containsKey(tbCategories.get(i).getIndustryParentId())){
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
            Industry category = (Industry)treeMap.get(tbCategories.get(i).getIndustryParentId());
            if(category!=null ){ // 不等于null，也就意味着有父节点
                // 有了父节点，要判断父节点下存贮字节点的集合是否存在，然后将子节点放入
                if(category.getChildren() == null){
                    // 判断一个集合是否被创建用null：表示结合还没有被分配内存空间(即还没有被创建)，内存大小自然为null
                    // 用集合的size判断集合中是否有元素，为0，没有元素（集合已经被创建），
                    category.setChildren(new ArrayList<Industry>());
                }
                category.getChildren().add(tbCategories.get(i)); // 添加到父节点的ChildList集合下


                treeMap.put(tbCategories.get(i).getIndustryParentId(),category);  // 把放好的数据放回到map中
            }

        }


        return R.ok().put("data",resultList);

    }
    /**
     * @Author: zyj
     * @Description:查询行业类型
     * @Param
     * @Date 14:29 2020/6/5
     */
    @ApiOperation(value = "查询行业类型", notes="查询行业类型")
    @RequestMapping(value = "/dictIndustryType",method = RequestMethod.GET)
    @RequiresPermissions("sys:industry:list")
     public R dictIndustryType(){
        String dictType="industry_type";
       List<Dict> list=industryService.dictIndustryType(dictType);
        return R.ok().put("data",list);
     }
    /**
     * 信息
     */
    @RequestMapping(value="/info/{industryId}",method = RequestMethod.GET)
    @RequiresPermissions("sys:industry:info")
    public R info(@PathVariable("industryId") Long industryId){
        Industry Industry = industryService.getById(industryId);

        return R.ok().put("data", Industry);
    }

    /**
     * 保存
     */
    @ApiOperation(value = "添加数据", notes="添加数据")
    @PostMapping("/save")
    @SysLog("保存行业领域")
    @RequiresPermissions("sys:industry:save")
    public R save(@RequestBody Industry industry){
       if (industry.getIndustryParentId()==null){
           industry.setIndustryParentId(0L);
       }
        industry.setDelFlag(0);
        industry.setCreatedTime(new Date());
        industry.setCreatedBy(getUserId());
        ValidatorUtils.validateEntity(industry);
        industryService.save(industry);

        return R.ok();
    }

    /**
     * 修改
     */
    @ApiOperation(value = "修改数据", notes="修改数据")
    @ApiImplicitParam(name = "industryIds", value = "主键id", required = true, paramType = "query", dataType = "Long")
    @PostMapping("/update")
    @SysLog("修改行业领域")
    @RequiresPermissions("sys:industry:update")
    public R update(@RequestBody Industry Industry){
        ValidatorUtils.validateEntity(Industry);
        Industry.setUpdatedTime(new Date());
        Industry.setUpdatedBy(getUserId());
        industryService.update(Industry);
        
        return R.ok();
    }
    /**
     * 删除
     */
    @ApiOperation(value = "删除数据", notes="删除数据")
    @ApiImplicitParam(name = "industryIds", value = "主键id", required = true, paramType = "query", dataType = "Long")
    @RequestMapping(value="/delete/{industryIds}",method = RequestMethod.DELETE)
    @SysLog("删除行业领域")
    @RequiresPermissions("sys:industry:delete")
    public R delete(@PathVariable("industryIds") Long industryIds){
        industryService.removeById(industryIds);

        return R.ok();
    }
    /**
     * 删除多条
     */
    @RequestMapping(value="/deletes",method = RequestMethod.DELETE)
    @RequiresPermissions("sys:industry:delete")
    @SysLog("删除行业领域")
    public R deletes(@RequestBody Long[] industryIds){
        industryService.removeByIds(Arrays.asList(industryIds));

        return R.ok();
    }
    @ApiOperation(value = "校验行业编码是否可用", notes="在添加和编码行业信息时，对行业编码进行唯一性校验；code=200 可以使用；code=500 已存在")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "industryCode", value = "行业编码", paramType = "query", dataType = "string", required = true),
            @ApiImplicitParam(name = "industryId", value = "行业Id", paramType = "query", dataType = "long")
    })
    @RequiresPermissions(value = {"sys:industry:save", "sys:industry:update"}, logical =  Logical.OR)
    @RequestMapping(value="/industryCodeIsExists",method = RequestMethod.GET)
    public R checkIndustryCodeIsExists(@ApiIgnore @RequestParam(required = false)Map<String,Object> params){
        int result = this.industryService.checkIndustryCodeIsExists(params);
        return 0 == result ? R.ok("可以使用"): R.error("已存在");
    }

}
