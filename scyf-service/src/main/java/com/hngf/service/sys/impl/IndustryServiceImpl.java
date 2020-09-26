package com.hngf.service.sys.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hngf.common.exception.ScyfException;
import com.hngf.common.utils.PageUtils;
import com.hngf.entity.sys.Dict;
import com.hngf.entity.sys.Industry;
import com.hngf.mapper.sys.IndustryMapper;
import com.hngf.service.sys.IndustryService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service("IndustryService")
public class IndustryServiceImpl implements IndustryService {

    @Autowired
    private IndustryMapper industryMapper;

    @Override
    public PageUtils queryPage(Map<String, Object> params,Integer pageNum,Integer pageSize,String order) {
        //利用PageHelper分页查询 注意：这个一定要放查询语句的前一行,否则无法进行分页,因为它对紧随其后第一个sql语句有效
        if(StringUtils.isNotBlank(order)){
            PageHelper.startPage(pageNum, pageSize,order);
        }else{
            PageHelper.startPage(pageNum, pageSize);
        }
        List<Industry> list = industryMapper.findList(params);
        PageInfo<Industry> pageInfo = new PageInfo<Industry>(list);
        return new PageUtils(list,(int)pageInfo.getTotal(),pageInfo.getPages(),pageInfo.getPageSize(),pageInfo.getPrePage() + 1);
    }
    /**
     * @Author: zyj
     * @Description:行业管理 列表显示树结构
     * @Param
     * @Date 11:00 2020/6/3
     */
    @Override
    public List<Industry> queryTreeList(Long orgId) {
       List<Industry> tbCategories=industryMapper.queryTreeList(orgId);
        for (Industry tbCategory : tbCategories) {
            String name= tbCategory.getIndustryName();
            tbCategory.setLabel(name);
        }
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
       return resultList;
    }
    /**
     * @Author: lxf
     * @Description:行业管理 列表显示
     * @Param
     * @Date 13:00 2020/9/9
     */
    @Override
    public List<Industry> queryList(){
        return industryMapper.queryTreeList(null);
    }

    @Override
    public Industry getById(Long id){
        return industryMapper.findById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(Industry industry) {
        Map<String,Object> params = new HashMap<>(4);
        params.put("industryCode",industry.getIndustryCode());
        int checkResult = this.checkIndustryCodeIsExists(params);
        if(0 == checkResult ){
            industryMapper.add(industry);
        }else {
            throw new ScyfException("行业编码已存在！");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(Industry industry) {
        Map<String,Object> params = new HashMap<>(4);
        params.put("industryId",industry.getIndustryId());
        params.put("industryCode",industry.getIndustryCode());
        int checkResult = this.checkIndustryCodeIsExists(params);
        if(0 == checkResult ){
            industryMapper.update(industry);
        }else {
            throw new ScyfException("行业编码已存在！");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeByIds(List ids) {
        industryMapper.deleteByIds(ids);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeById(Long id) {
        industryMapper.deleteById(id);
    }

    /**
     * 根据行业类别industryCode获取监察机构
     * @param industryCode
     * @return
     */
    @Override
    public List<Map<String, Object>> getOrgIndustryList(String industryCode) {
        return industryMapper.getOrgIndustryList(industryCode);
    }
    /**
     * @Author: zyj
     * @Description:查询行业类型
     * @Param
     * @Date 14:29 2020/6/5
     */
    @Override
    public List<Dict> dictIndustryType(String dictType) {
        return industryMapper.dictIndustryType(dictType);
    }

    @Override
    public List<Industry> getIndustryTreeByOrgId(Long orgId) {
        return industryMapper.getIndustryTreeByOrgId(orgId);
    }

    @Override
    public String getIndustryCodeByOrgId(Long orgId) {
        return industryMapper.getIndustryCodeByOrgId(orgId);
    }



    /**
     * 筛选机构下的所有行业
     *
     * @param orgId
     * @param industryCode 行业编码有值时，此行业及其下属行业
     * @return
     */
    @Override
    public String getCodeListByIndustryCode(Long orgId, String industryCode){
        return industryMapper.getCodeListByIndustryCode(orgId, industryCode);
    }

    /**
     * 根据行业编码 查询 行业信息 ，用来判断 行业编码是否存在，保持行业编码唯一性
     * 0 表示未使用过，可以使用，1表示已使用过，不能使用
     * @param params
     * @return
     */
    @Override
    public int checkIndustryCodeIsExists(Map<String,Object> params){
        if(null == params ){
            return 0 ;
        }
        Object industryCodeObj = params.get("industryCode") ;
        if(null == industryCodeObj || StringUtils.isBlank( industryCodeObj.toString())){
            return 0 ;
        }
        List<Industry> list = this.industryMapper.findIndustryListByIndustryCode(industryCodeObj.toString());
        if(null == list ){
            return 0 ;
        }
        if(1== list.size()){
            // 编码已存在
            Object industryIdObj = params.get("industryId");
            if(null == industryIdObj || "0".equals(industryIdObj.toString()) ){
                return 1 ;
            }
            if(Long.parseLong(industryIdObj.toString()) == list.get(0).getIndustryId().longValue() ) {
                // 编辑
                return 0;
            }
            return 1;
        }
        return 1;
    }
}
