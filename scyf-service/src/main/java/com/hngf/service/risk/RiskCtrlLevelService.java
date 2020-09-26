package com.hngf.service.risk;

import com.hngf.common.utils.PageUtils;
import com.hngf.entity.risk.RiskCtrlLevel;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 风险管控层级
 *
 * @author hngf
 * @email 
 * @date 2020-05-20 17:36:24
 */
public interface RiskCtrlLevelService {
    /**
     * @Author: zyj
     * @Description:根据条件返回相应的列表
     * @Date 16:50 2020/5/21
     */
    PageUtils queryPage(Map<String, Object> params,Integer pageNum,Integer pageSize,String order);
    /**
     * @Author: zyj
     * @Description:通过企业id查询统计是否存在
     * @Date 16:47 2020/5/21
     */
     Integer count(@Param("companyId") Long companyId);

    /**
     * @Author: zyj
     * @Description:根据id查询单条数据
     * @Date 14:40 2020/5/22
     */
    RiskCtrlLevel getById(Long id);

    /**
     * @Author: zyj
     * @Description:新增数据
     * @Date 14:40 2020/5/22
     */
    void save(RiskCtrlLevel RiskCtrlLevel);

    /**
     * @Author: zyj
     * @Description:修改数据
     * @Date 14:40 2020/5/22
     */
    void update(RiskCtrlLevel RiskCtrlLevel);

    /**
     * @Author: zyj
     * @Description:批量删除数据
     * @Date 14:40 2020/5/22
     */
    void removeByIds(List ids);

    /**
     * @Author: zyj
     * @Description:删除数据
     * @Date 14:40 2020/5/22
     */
    void removeById(Long id, Long updatedBy);
    /**
    * @Author: zyj
    * @Description:如果count为0初始化数据
    * @Date 16:59 2020/5/21
    */
    void initRiskCtrlLevel(Map<String,Object> params);

    /**
     * 根据企业id 获取企业管控层级信息
     * @param cId
     * @return
     */
    List getRiskCtrlLevelList(Long cId);

    int deleteBatchByCompanyId(Long companyId, Long updatedBy) ;
}

