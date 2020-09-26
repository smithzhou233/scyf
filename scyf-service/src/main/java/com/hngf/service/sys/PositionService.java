package com.hngf.service.sys;

import com.hngf.common.utils.PageUtils;
import com.hngf.entity.sys.Position;
import java.util.Map;
import java.util.List;

/**
 * 岗位表
 *
 * @author hngf
 * @email 
 * @date 2020-05-20 17:31:55
 */
public interface PositionService {

    PageUtils queryPage(Map<String, Object> params,Integer pageNum,Integer pageSize,String order);

    /**
    * 根据ID查询
    */
    Position getById(Long id);

    List<Position> getList(Map<String, Object> params);
    /**
     * zyj 查询岗位是否存在
     * @param companyId
     * @param positionTitle
     * @return
     */
    Position findPositionTitle(Long companyId,String positionTitle);
    /**
    * 保存
    */
    void save(Position Position);

    /**
    * 更新
    */
    void update(Position Position);

    /**
    * 批量删除
    */
    void removeByIds(List ids);

    /**
    * 删除
    */
    void removeById(Long id);

    /**
     * 根据group 查询岗位
     * @param paraMap
     * @return
     */
    List<Position> getPositionByGroup(Map<String, Object> paraMap);
}

