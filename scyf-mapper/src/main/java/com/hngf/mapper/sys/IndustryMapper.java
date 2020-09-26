package com.hngf.mapper.sys;

import com.hngf.entity.sys.Dict;
import com.hngf.entity.sys.Industry;
import org.apache.ibatis.annotations.Mapper;
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
@Mapper
public interface IndustryMapper {

    List<Industry> findList(Map<String, Object> params);
    /**
     * @Author: zyj
     * @Description:行业管理 列表显示树结构
     * @Param
     * @Date 11:00 2020/6/3
     */
    List<Industry> queryTreeList(@Param("orgId") Long orgId);

    Industry findById(Long id);

    void add(Industry Industry);

    void update(Industry Industry);

    int deleteById(@Param("id") Long id);

    int deleteByIds(@Param("ids") List ids);
    /**
     * 根据行业类别industryCode获取监察机构
     * @param industryCode
     * @return
     */
    List<Map<String, Object>> getOrgIndustryList(String industryCode);
    /**
    * @Author: zyj
    * @Description:查询行业类型
    * @Param
    * @Date 14:29 2020/6/5
    */
    List<Dict> dictIndustryType(@Param("dictType") String dictType);

    List<Industry> getIndustryTreeByOrgId(@Param("orgId") Long orgId);

    String getIndustryCodeByOrgId(@Param("orgId") Long orgId);

    /**
     * 筛选机构下的所有行业
     *
     * @param orgId
     * @param industryCode 行业编码有值时，此行业及其下属行业
     * @return
     */
    String getCodeListByIndustryCode(@Param("orgId")Long orgId, @Param("industryCode")String industryCode);

    /**
     * 根据行业编码 查询 行业信息 ，用来判断 行业编码是否存在，保持行业编码唯一性
     * @param industryCode
     * @return
     */
    List<Industry> findIndustryListByIndustryCode(@Param("industryCode")String industryCode);
}
