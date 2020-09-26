package com.hngf.mapper.exam;

import com.hngf.dto.exam.PaperDto;
import com.hngf.entity.exam.Paper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Map;
import java.util.List;

/**
 * 试卷表
 * 
 * @author hngf
 * @email 
 * @date 2020-08-14 09:39:02
 */
@Mapper
public interface PaperMapper {

    List<Paper> findList(Map<String, Object> params);

    List<Paper> findGroupList(Map<String, Object> params);

    Paper findById(Long id);

    PaperDto findDtoById(Long paperId);

    void add(Paper paper);

    void update(Paper paper);

    int deleteById(@Param("id")Long id, @Param("updatedBy")Long updatedBy );

    int deleteByIds(@Param("ids") List ids, @Param("updatedBy")Long updatedBy );

    /**
     * 根据试卷id查询试卷状态paper_status
     * @param id
     * @return
     */
    Integer findPaperStatusById(@Param("id")Long id);

    /**
     * 根据试卷id更新试卷状态paper_status
     * @param paperId
     * @param status
     * @return
     */
    int updatePaperStatus(@Param("paperId")Long paperId, @Param("status")Integer status, @Param("updatedBy")Long updatedBy );
}
