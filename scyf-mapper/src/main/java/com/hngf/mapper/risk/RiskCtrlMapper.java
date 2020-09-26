package com.hngf.mapper.risk;

import com.hngf.entity.risk.RiskCtrl;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Map;
import java.util.List;

/**
 * 风险管控配置
 * 
 * @author hngf
 * @email 
 * @date 2020-05-20 17:36:24
 */
@Mapper
public interface RiskCtrlMapper {

    List<RiskCtrl> findList(Map<String, Object> params);

    RiskCtrl findById(Long id);

    void add(RiskCtrl RiskCtrl);

    void update(RiskCtrl RiskCtrl);

    int deleteById(@Param("id") Long id);

    int deleteByIds(@Param("ids") List ids);

    void delRiskCtrl(@Param("riskId")Long riskId, @Param("companyId")Long companyId);

    /**
     * 批量新增风险管控配置表信息
     * @param riskCtrls
     */
    void saveBatch(@Param("list")List<RiskCtrl> riskCtrls);
    /**
     * 根据岗位ID 企业ID查看管控层级
     * yfh
     * 2020/06/11
     * @param params
     * @return
     */
    Integer getCurrentUserCtlLevel(Map<String, Object> params);
}
