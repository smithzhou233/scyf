package com.hngf.mapper.sys;

import com.hngf.entity.sys.Industry;
import com.hngf.entity.sys.OrgIndustry;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Map;
import java.util.List;

/**
 * 组织机构和行业映射
 * 
 * @author hngf
 * @email 
 * @date 2020-05-20 17:31:55
 */
@Mapper
public interface OrgIndustryMapper {

    List<OrgIndustry> findList(Map<String, Object> params);

    OrgIndustry findById(Long id);

    void add(OrgIndustry OrgIndustry);

    void update(OrgIndustry OrgIndustry);

    int deleteById(@Param("id") Long id);

    int deleteByIds(@Param("ids") List ids);
    /**
    * @Author: zyj
    * @Description:给组织添加行业之前先删除之前组织下的行业
    * @Param
    * @Date 16:34 2020/6/4
    */
    int deleteIndustry(@Param("orgId") Long orgId);
    /**
    * @Author: zyj
    * @Description:查询所选中组织下的行业
    * @Param
    * @Date 17:46 2020/6/4
    */
    List<OrgIndustry> orgIndustryChecked(@Param("orgId") Long orgId);

    /**
     * @Author: yss
     * @Description:局级首页查询风险分析
     * @Param
     * @Date 17:46 2020/6/4
     */
    List<Map<String, Object>>  getOrgRiskAnalysis(@Param("orgId") Long orgId);

    List<OrgIndustry> findListByOrgId(@Param("orgId")Long orgId);

    List<Industry> selectIndustryCodeByPid(@Param("orgId")Long orgId);
    String selectIndustryCodeByOrgId(@Param("orgId")Long orgId);

}
