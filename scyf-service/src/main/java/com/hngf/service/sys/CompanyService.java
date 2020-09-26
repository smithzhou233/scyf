package com.hngf.service.sys;

import com.hngf.common.utils.PageUtils;
import com.hngf.dto.sys.CompanyDto;
import com.hngf.entity.scyf.SecureProduc;
import com.hngf.entity.sys.Company;
import com.hngf.entity.sys.User;

import java.util.List;
import java.util.Map;

/**
 * 企业基础信息表
 *
 * @author hngf
 * @email 
 * @date 2020-05-20 17:31:55
 */
public interface CompanyService {

    List<CompanyDto> findList(Map<String, Object> params);

    /**
    * 根据ID查询
    */
    List<Map<String, Object>> getById(Long id);


    /**
    * 更新
    */
    void update(Company company, SecureProduc secureProduc) throws Exception;

    /**
    * 批量删除
    */
    void removeByIds(List ids);

    /**
     * 单条删除企业信息
     * yfh
     * 2020/06/08
     * @param companyId
     * @throws Exception
     */
    void removeById(Long companyId,Long updatedBy) throws Exception;

    /**
     * 三级联动查询省下面的市
     * yfh
     * 2020/05/25
     * @param parentId
     * @return
     */
    List<Map<String, Object>> selectPro(String parentId);


    void save(Company company, SecureProduc secureProduc) throws Exception;

    /**
     * 查看企业经济类型子类
     * @return
     */
    List<Map<String,Object>> queryEconomicType(Integer dictId,String dictType);

    /**
     * 查看企业经济类型父类
     * @param dictType
     * @param ownerId
     * @return
     */
    List<Map<String, Object>> queryEconomicTypeParent(String dictType, String ownerId);

    /**
     * 企业规模查询
     * @param dictType
     * @return
     */
    List<Map<String, Object>> queryScale(String dictType);

    /**
     *查看企业是否存在
     * yfh
     * 2020/06/08
     * @param companyId
     * @return
     */
    Company getCompanyId(Long companyId);

    void insertCompanyAdmin(User user, Long roleId);

    void insertCompanyAdmin(User user);

    /**
     * 企业评分(监管端使用)
     * @param params
     * @param pageNum
     * @param pageSize
     * @param order
     * @return
     */
    PageUtils companyScore(Map<String,Object> params,int pageNum,int pageSize,String order);

    /**
     * 查询机构企业列表
     * @param companyRootId
     * @return
     */
    List<CompanyDto> queryCompanyListForTreeTable(Long companyRootId);

    /**
     * 根据公司ID主键查询公司信息+安全信息
     * @param companyId
     * @return
     */
    CompanyDto getCompanyById(Long companyId);

    /**
     * 企业级联删除
     * @param companyId
     * @param userId
     */
    void deleteCompanyWithCascade(Long companyId, Long userId) throws Exception;

    /**
     * 查询父级ID为0的企业列表
     * @param params
     * @param pageNum
     * @param pageSize
     * @param order
     * @return
     */
    PageUtils querySingleOrgs(Map<String, Object> params, int pageNum, int pageSize, String order);

    /**
     * 设置某个企业为指定集团的下级企业
     * @param params
     * @throws Exception
     */
    void addExistOrg2Group(Map<String, Object> params);

    /**
     * 更新部署状态
     * @param params
     */
    int setDeployment(Map<String, Object> params);

    /**
     * 首页查询隐患等级分类
     */
    List<Map<String, Object>>   queryTotalForMainPage(Map<String, Object> params);

    /**
     * 查询企业列表 -监管层大屏使用
     * @param params
     * @return
     */
    Map<String, Object> queryCompanyListForBigScreen(Map<String, Object> params);

    /**
     * 风险点总计 -监管层大屏使用
     * @param params
     * @return
     */
    Map<String, Object> queryRiskCountForGent(Map<String, Object> params);

    /**
     * 隐患总计 -监管层大屏使用
     * @param params
     * @return
     */
    Map<String, Object> queryHiddenDangerCountForGent(Map<String, Object> params);

    /**
     * 查询统计预警风险点数 -监管层大屏使用
     * @param params
     * @return
     */
    Map<String, Object> queryRiskCountOfOutOfControlForGent(Map<String, Object> params);

    /**
     * 查询隐患通报  -监管层大屏使用
     * @param params
     * @return
     */
    List<Map<String, Object>> queryHiddenForGent(Map<String, Object> params);

    /**
     * 查询统计下级企业风险指数 -监管层大屏使用
     * @param params
     * @return
     */
    List<Map<String, Object>> queryRiskPointForGent(Map<String, Object> params);

    List<Map<String, Object>> queryHiddenPointForGent(Map<String, Object> params);

    List<Map<String,Object>> queryGroupPositionAndLvl(Map<String, Object> params);


    Map<String, Object> getOrgRiskAnalysis(Long orgId);

    /**
     * 根据集团公司的Id查询其以及其所辖的所有企业信息tree
     * @param params
     * @return
     */
    List<CompanyDto> getListTree(Map<String,Object> params);

    /**
     * 监管机构 监管行业的所有企业
     * @param params
     * @return
     */
    PageUtils findListByindustryTypeCode( Map<String, Object> params);

    List<CompanyDto> fildAllListByIndustryTypeCode(Map<String, Object> params) ;

    /**
     * 获取集团级大屏的企业地址信息
     * @param companyId
     * @return
     */
    List<Map<String, Object>> getCompanyMapByParentId(Long companyId);

    Integer getCompanyCountByIndustryCode(String industryCode);
    List<Map<String, Object>>  selectTop10ScoreOrRisk(Map<String, Object> params);
    Map<String,Object>  getCompanyDataByIndustryCode(Map<String, Object> params);
    List<Map<String, Object>> getMonthCountByIndustry(String industryCode);
}

