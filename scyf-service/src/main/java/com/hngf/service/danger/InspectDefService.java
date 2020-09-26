package com.hngf.service.danger;

import com.hngf.common.utils.PageUtils;
import com.hngf.dto.danger.InspectDefTreeDto;
import com.hngf.entity.danger.InspectDef;
import com.hngf.entity.danger.InspectItemDef;

import java.util.Map;
import java.util.List;

/**
 * 检查定义表
 *
 * @author hngf
 * @email 
 * @date 2020-05-20 17:36:24
 */
public interface InspectDefService {

    PageUtils queryPage(Map<String, Object> params,Integer pageNum,Integer pageSize,String order);

    /**
    * 根据ID查询
    */
    InspectDef getById(Long id);

    /**
    * 根据公司ID查询
    */
    List<InspectDef> getByCompanyId(Long companyId);

    /**
     * 根据名称查询ID列表
     * @param params
     * @return
     */
    List<Long> getIdByName(Map<String, Object> params);
    List<InspectDef> getInspectDefByMap(Map<String, Object> params);
    /**
     * 查询检查项定义
     * @param params
     * @return
     */
    List<InspectItemDef> getItems(Map<String, Object> params);

    /**
    * 保存
    */
    int save(InspectDef InspectDef);

    /**
    * 更新
    */
    int update(InspectDef InspectDef);

    /**
    * 批量删除
    */
    int removeByIds(List ids);

    /**
    * 删除
    */
    int removeById(Long id);

    /**
     * 查询树状结构
     * @param params
     * @return
     */
    List<InspectDefTreeDto> getTreeItems(Map<String, Object> params);

    /**
     * 添加 检查定义表-检查表-安全检查表 信息
     * @param companyId
     */
    void initBizCheckDef(Long companyId);

    /**
     * 查询风险点下的检查表
     * @param params
     * @return
     * @throws Exception
     */
    List<Map<String, Object>> getRiskPointCheckTables(Map<String, Object> params) throws Exception;

    List<Map<String, Object>> getCheckTables(Map<String, Object> params) throws Exception;

    int deleteBatchByCompanyId(Long companyId, Long updatedBy) ;
}

