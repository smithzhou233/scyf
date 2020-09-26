package com.hngf.mapper.danger;

import com.hngf.entity.danger.InspectDef;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Map;
import java.util.List;

/**
 * 检查定义表
 * 
 * @author hngf
 * @email 
 * @date 2020-05-20 17:36:24
 */
@Mapper
public interface InspectDefMapper {

    List<InspectDef> findList(Map<String, Object> params);

    InspectDef findById(Long id);

    /**
     * 根据名称查询ID列表
     * @param params
     * @return
     */
    List<Long> findIdByName(Map<String, Object> params);

    List<InspectDef> findByMap(Map<String, Object> params);

    int add(InspectDef InspectDef);

    int update(InspectDef InspectDef);

    int deleteById(@Param("id") Long id);

    int deleteByIds(@Param("ids") List ids);

    /**
     *  查询检查定义表-检查表-安全检查表 信息
     * @param companyId
     * @return
     */
    InspectDef selectById(@Param("companyId")Long companyId);
    /**
     *  查询检查定义表-检查表-安全检查表 信息
     * @param companyId
     * @return
     */
    List<InspectDef>  selectListByCompanyId(@Param("companyId")Long companyId);
    /**
     * 查询风险点下的检查表
     * @param params
     * @return
     */
    List<Map<String, Object>> findListByRiskPoint(Map<String, Object> params);

    List<Map<String, Object>> getList(Map<String, Object> params);

    int deleteBatchByCompanyId(@Param("companyId") Long companyId, @Param("updatedBy") Long updatedBy) ;
}
