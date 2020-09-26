package com.hngf.service.common;

import com.hngf.common.utils.PageUtils;
import com.hngf.entity.common.CommonPost;
import java.util.Map;
import java.util.List;

/**
 * 知识库
 *
 * @author hngf
 * @email 
 * @date 2020-06-04 11:36:32
 */
public interface CommonPostService {

    PageUtils queryPage(Map<String, Object> params, Integer pageNum, Integer pageSize, String order);
    /**
     * @Author: zyj
     * @Description:查询风险规章制度
     * @Param
     * @Date 9:43 2020/6/15
     */
    PageUtils queryInstitution(Map<String, Object> params,Integer pageNum, Integer pageSize, String order);

    /**
     * 查询作业指导书
     * @param params
     * @return
     */
    PageUtils getWorkInstruction(Map<String, Object> params);

    /**
    * 根据ID查询
    */
    CommonPost getById(Long id);

    /**
    * 保存
    */
    void save(CommonPost CommonPost);

    /**
    * 更新
    */
    void update(CommonPost CommonPost);

    /**
    * 批量删除
    */
    void removeByIds(List ids);

    /**
    * 删除
    */
    void removeById(Long id);

    /**
     * 发布知识点
     * @param ids 知识点id集合
     * @param publishBy  发布人id
     * @return
     */
    int publishByIds(List ids, Long publishBy) ;
}

