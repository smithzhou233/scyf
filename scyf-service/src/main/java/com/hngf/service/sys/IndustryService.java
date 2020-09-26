package com.hngf.service.sys;

import com.hngf.common.utils.PageUtils;
import com.hngf.entity.sys.Dict;
import com.hngf.entity.sys.Industry;
import org.apache.ibatis.annotations.Param;

import java.util.Map;
import java.util.List;

/**
 * 行业领域表
 *
 * @author hngf
 * @email 
 * @date 2020-06-03 10:47:12
 */
public interface IndustryService {

    PageUtils queryPage(Map<String, Object> params, Integer pageNum, Integer pageSize, String order);
    /**
    * @Author: zyj
    * @Description:行业管理 列表显示树结构
    * @Param
    * @Date 11:00 2020/6/3
    */
    List<Industry> queryTreeList(Long orgId);

    /**
    * @Author: lxf
    * @Description:行业管理 列表显示
    * @Param
    * @Date 13:00 2020/9/9
    */
    List<Industry> queryList();
    /**
    * 根据ID查询
    */
    Industry getById(Long id);

    /**
    * 保存
    */
    void save(Industry Industry);

    /**
    * 更新
    */
    void update(Industry Industry);

    /**
    * 批量删除
    */
    void removeByIds(List ids);

    /**
    * 删除
    */
    void removeById(Long id);

    List<Map<String, Object>> getOrgIndustryList(String industryCode);
    /**
     * @Author: zyj
     * @Description:查询行业类型
     * @Param
     * @Date 14:29 2020/6/5
     */
    List<Dict> dictIndustryType(@Param("dictType") String dictType);

    List<Industry> getIndustryTreeByOrgId(@Param("orgId") Long orgId);

    String getIndustryCodeByOrgId(@Param("OrgId") Long orgId );

    /**
     * 筛选机构下的所有行业
     *
     * @param orgId
     * @param industryCode 行业编码有值时，此行业及其下属行业
     * @return
     */
    String getCodeListByIndustryCode(Long orgId, String industryCode);
    /**
     * 根据行业编码 查询 行业信息 ，用来判断 行业编码是否存在，保持行业编码唯一性
     * @param params
     * @return
     */
    int checkIndustryCodeIsExists(Map<String,Object> params);
}

