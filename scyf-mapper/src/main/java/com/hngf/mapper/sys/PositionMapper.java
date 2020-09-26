package com.hngf.mapper.sys;

import com.hngf.entity.sys.Position;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Map;
import java.util.List;

/**
 * 岗位表
 * 
 * @author hngf
 * @email 
 * @date 2020-05-20 17:31:55
 */
@Mapper
public interface PositionMapper {

    List<Position> findList(Map<String, Object> params);

    Position findById(Long id);

    /**
     * 岗位名称
     * @param title
     * @return
     */
    List<Position> findByTitle(String title);

    /**
     * zyj 查询岗位是否存在
     * @param companyId
     * @param positionTitle
     * @return
     */
    Position findPositionTitle(@Param("companyId")Long companyId,@Param("positionTitle") String positionTitle);

    void add(Position Position);

    void update(Position Position);

    int deleteById(@Param("id") Long id);

    int deleteByIds(@Param("ids") List ids);

    List<Position> findByMap(Map<String, Object> params);

    /**
     * 根据企业id查询岗位表信息
     * @param cId
     * @return
     */
    List<Position> getPositionListById(@Param("companyId")Long cId);

    List<Position> getPositionByGroup(Map<String, Object> params);

    int deleteByCompanyId(@Param("companyId") Long companyId , @Param("updatedBy") Long updatedBy );
}
