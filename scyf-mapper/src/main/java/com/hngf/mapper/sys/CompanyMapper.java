package com.hngf.mapper.sys;

import com.hngf.dto.sys.CompanyDto;
import com.hngf.entity.sys.Company;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import java.util.Map;
import java.util.List;

/**
 * 企业基础信息表
 * 
 * @author hngf
 * @email 
 * @date 2020-05-20 17:31:55
 */
@Mapper
public interface CompanyMapper {

    List<CompanyDto> findList(Map<String, Object> params);

    List<Map<String, Object>> findById(Long id);

    void add(Company Company);

    void update(Company company);

    int deleteById(@Param("companyId") Long companyId,@Param("updatedBy")Long updatedBy);

    int deleteByIds(@Param("ids") List ids);
    /**
     * 三级联动查询省下面的市
     * yfh
     * 2020/05/25
     * @param parentId
     * @return
     */
    List<Map<String, Object>> selectPro(@Param("parentId")String parentId);

    void insertCompany(Company company);

    Company getCompanyById(@Param("companyParentId")Long companyParentId);

    @Update({"UPDATE sys_company SET ${params} = ${params} + #{steps} WHERE ${condition} and del_flag = 0"})
    void increment(@Param("params") String params, @Param("steps") int steps, @Param("condition") String condition) throws Exception;

    void updateComGroupId(@Param("comGroupId")Long comGroupId, @Param("companyId")Long companyId);
    /**
     * 查看企业经济类型子类
     * yfh
     * 2020/06/05
     * @return
     */
    List<Map<String,Object>> queryEconomicType(@Param("dictId")Integer dictId,@Param("dictType")String dictType);

    /**
     * 查看企业经济类型父类
     * yfh
     * 2020/06/05
     * @param dictType
     * @param ownerId
     * @return
     */
    List<Map<String, Object>> queryEconomicTypeParent(@Param("dictType")String dictType, @Param("ownerId")String ownerId);

    /**
     * 企业规模查询
     * yfh
     * 2020/06/05
     * @param dictType
     * @return
     */
    List<Map<String, Object>> queryScale(@Param("dictType")String dictType);

    Company getCompanyId(@Param("companyId")Long companyId);

    @Update({"UPDATE sys_company SET ${params} = ${params} - #{steps} WHERE ${condition} and del_flag = 0"})
    void decrement(@Param("params") String params, @Param("steps") int steps, @Param("condition") String condition) throws Exception;

    void updateCompanyAdmin(@Param("companyId")Long companyId, @Param("userId")Long userId);

    /**
     * 企业评分(监管端使用)
     * @param params
     * @return
     */
    List<Map<String, Object>> findCompanyScore(Map<String, Object> params);


    CompanyDto findCompanyById(Long companyRootId);

    /**
     * 查询机构企业列表
     * @param companyRootId
     * @return
     */
    List<CompanyDto> selectCompanyListForTreeTable(Long companyRootId);

    /**
     * 查询集团、企业列表（含所有子节点）
     * @param companyRootId
     * @return
     */
    String findCompanyIdsWithSub(Long companyRootId);
    /**
     * 查询 企业列表（含所有子节点）
     * @param companyRootId
     * @return
     */
    String findCompanyIdsByCId(Long companyRootId);
    /**
     * 删除多个集团、企业
     * @param cIds
     */
    void deleteCompanys(String cIds);

    /**
     * 查询企业列表-父级ID为0的
     * @param params
     * @return
     */
    List<Company> findSingleOrgs(Map<String, Object> params);


    List<CompanyDto> selectListByindustryTypeCode(Map<String, Object> params);

    /**
     * 设置某个企业为指定集团的下级企业
     * @param params
     */
    void addExistOrg2Group(Map<String, Object> params);

    /**
     * 更新部署状态
     * @param params
     */
    int updateDeployment(Map<String, Object> params);
    List<Map<String, Object>>  selectTotalForMainPage (Map<String, Object> params);

    /**
     * 查询统计某集团下的所有企业列表（含该企业风险点和隐患的统计）
     * @param params
     * @return
     */
    List<Map<String, Object>> findCompanyListForBigScreen(Map<String, Object> params);

    /**
     * 大屏地图展示企业坐标及最大风险等级
     * @param params
     * @return
     */
    List<Map<String,Object>> selectGroupPositionAndLvl(Map<String, Object> params);

    List<Map<String,Object>> selectGroupPositionAndLvlPointMap(Map<String, Object> params);

    Long findCompanyGroupIdByCompanyId(@Param("companyId")Long companyId);

    /**
     * 督导-查询上级的领导
     * @param groupIds
     * @return
     */
    List<Long>  findParentGroupLeaderId(@Param("groupIds")String groupIds,@Param("leaderPositonIds")String leaderPositonIds);

    String findParentGroupIds(@Param("groupId")Long groupId);
    Integer getCompanyCountByIndustryCode(String industryCode);
    Map<String,Object>  getCompanyDataByIndustryCode(Map<String,Object> params);

    /**
     * 获取集团级大屏的企业地址信息
     * @param companyId
     * @return
     */
    List<Map<String, Object>> getCompanyMapByParentId(@Param("companyId")Long companyId);

    List<Map<String, Object>>  selectTop10ScoreOrRisk(Map<String, Object> params);

    List<Map<String, Object>>  getMonthCountByIndustry(String industryCode);
}
