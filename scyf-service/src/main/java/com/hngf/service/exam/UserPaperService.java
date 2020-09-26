package com.hngf.service.exam;

import com.hngf.common.utils.PageUtils;
import com.hngf.dto.exam.UserPaperDto;
import com.hngf.entity.exam.UserPaper;
import com.hngf.vo.exam.UserPaperVo;

import java.util.List;
import java.util.Map;

/**
 * 用户试卷表
 *
 * @author lxf
 * @email 
 * @date 2020-08-15 11:57:10
 */
public interface UserPaperService {

    PageUtils queryPage(Map<String, Object> params,Integer pageNum,Integer pageSize,String order);

    /**
    * 根据ID查询
    */
    UserPaper getById(Long id);

    /**
    * 保存
    */
    void save(UserPaper userPaper);

    /**
    * 更新
    */
    void update(UserPaper userPaper);

    /**
    * 批量删除
    */
    void removeByIds(List ids,Long updatedBy);

    /**
    * 删除
    */
    void removeById(Long id,Long updatedBy);

    /**
     * 考试交卷，保存考试信息
     * @param userPaperVo
     * @param userId
     * @param groupId
     * @param companyId
     */
    void examSumbit(UserPaperVo userPaperVo, Long userId, Long groupId, Long companyId);

    /**
     * 根据试卷id获取试卷内容，用来进行考试
     * @param id
     * @return
     */
    UserPaperDto findDtoById(Long id);
}

