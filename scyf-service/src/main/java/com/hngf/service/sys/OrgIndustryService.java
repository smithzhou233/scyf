package com.hngf.service.sys;

import com.hngf.common.utils.PageUtils;
import com.hngf.entity.sys.Industry;
import com.hngf.entity.sys.OrgIndustry;
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
public interface OrgIndustryService {

    PageUtils queryPage(Map<String, Object> params);

    /**
    * 根据ID查询
    */
    OrgIndustry getById(Long id);

    /**
    * 保存
    */
    void save(OrgIndustry OrgIndustry);

    /**
    * 更新
    */
    void update(OrgIndustry OrgIndustry);

    /**
    * 批量删除
    */
    void removeByIds(List ids);

    /**
    * 删除
    */
    void removeById(Long id);
    /**
     * @Author: zyj
     * @Description:查询所选中组织下的行业
     * @Param
     * @Date 17:46 2020/6/4
     */
    List<OrgIndustry> orgIndustryChecked(@Param("orgId") Long orgId);

    /**
     * 查询下级行业编码
     * @param orgId
     * @return
     */
    List<Industry> selectIndustryCodeByPid(Long orgId);

    /**
     * 根据org查询行业编码
     */
    String selectIndustryCodeByOrgId(Long orgId);


}

