package com.hngf.mapper.risk;

import com.hngf.entity.risk.RiskCtrlLevel;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Map;
import java.util.List;

/**
 * 风险管控层级
 * 
 * @author hngf
 * @email 
 * @date 2020-05-20 17:36:24
 */
@Mapper
public interface RiskCtrlLevelMapper {
    /**
    * @Author: zyj
    * @Description:根据条件返回相应的列表
    * @Date 16:50 2020/5/21
    */
    List<RiskCtrlLevel> findList(Map<String, Object> params);
    /**
     * @Author: zyj
     * @Description:根据id查询单条数据
     * @Date 14:40 2020/5/22
     */
    RiskCtrlLevel findById(Long id);
    /**
     * @Author: zyj
     * @Description:新增数据
     * @Date 14:40 2020/5/22
     */
    void add(RiskCtrlLevel RiskCtrlLevel);
    /**
     * @Author: zyj
     * @Description:修改数据
     * @Date 14:40 2020/5/22
     */
    void update(RiskCtrlLevel RiskCtrlLevel);
    /**
     * @Author: zyj
     * @Description:删除数据
     * @Date 14:40 2020/5/22
     */
    int deleteById(@Param("id") Long id , @Param("updatedBy")Long updatedBy);
    /**
     * @Author: zyj
     * @Description:批量删除数据
     * @Date 14:40 2020/5/22
     */
    int deleteByIds(@Param("ids") List ids);
    /**
    * @Author: zyj
    * @Description:通过企业id查询统计是否存在
    * @Date 16:47 2020/5/21
    */
    int count(@Param("companyId") Long companyId);

    /**
     * 根据企业id 获取企业管控层级信息
     * @param cId
     * @return
     */
    List<Map<String,Object>> getRiskCtrlLevelList(@Param("companyId")Long cId);

    Integer getCurrentUserCtlLevel(Map<String, Object> map);

    /**
     * 管控层级使用情况，用来做删除之前的判断
     * @param riskCtrlLevelId
     * @return
     */
    Long getUsedCount(@Param("riskCtrlLevelId")Long riskCtrlLevelId);

    int deleteBatchByCompanyId(@Param("companyId") Long companyId, @Param("updatedBy") Long updatedBy) ;
}
