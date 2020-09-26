package com.hngf.service.score;

import com.hngf.common.utils.PageUtils;
import com.hngf.entity.score.ScoreModel;

import java.util.Map;
import java.util.List;

/**
 * 绩效考核模式配置表
 *
 * @author hngf
 * @email 
 * @date 2020-05-27 10:11:53
 */
public interface ScoreModelService {

    PageUtils queryPage(Map<String, Object> params,Integer pageNum,Integer pageSize,String order);
    /**
    * @Author: zyj
    * @Description:查询考核模式配置
    * @Param
    * @Date 16:03 2020/6/11
    */
    List<ScoreModel> queryByModelSettingPage(Long companyId);
    /**
     * @Author: zyj
     * @Description:通过企业id,父级企业id,年份,查询初始化分数
     * @Param companyId 企业id  scoreParentId企业父级id year年份
     * @Date 16:19 2020/6/11
     */
    ScoreModel getThisYearinitScore(Long companyId,Long scoreParentId,String year);
    /**
    * @Author: zyj
    * @Description:绩效考核模式配置表添加配置
    * @Param
    * @Date 16:59 2020/6/11
    */
    void updateByModelSetting(ScoreModel scoreModel);
    /**
    * 根据ID查询
    */
    ScoreModel getById(Long id);

    /**
    * 保存
    */
    void save(ScoreModel ScoreModel);

    /**
    * 更新
    */
    void update(ScoreModel ScoreModel);

    /**
    * 批量删除
    */
    void removeByIds(List ids);

    /**
    * 删除
    */
    void removeById(Long id);

    int initSetting(Long companyId) throws Exception;

    int deleteBatchByCompanyId(Long companyId, Long updatedBy) ;
}

