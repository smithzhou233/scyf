package com.hngf.service.score;

import com.hngf.common.utils.PageUtils;
import com.hngf.entity.score.ScoreSetting;
import java.util.Map;
import java.util.List;

/**
 * 绩效考核打分规则配置表
 *
 * @author hngf
 * @email 
 * @date 2020-05-27 10:11:53
 */
public interface ScoreSettingService {

    PageUtils queryPage(Map<String, Object> params,Integer pageNum,Integer pageSize,String order);

    /**
    * 根据ID查询
    */
    ScoreSetting getById(Long id);

    /**
    * 根据配置编号查询
     * @param companyId
     * @param settingCode
    */
    ScoreSetting getBySettingCode(Long companyId,Integer settingCode);

    /**
    * 保存
    */
    void save(ScoreSetting ScoreSetting);

    /**
    * 更新
    */
    void update(ScoreSetting ScoreSetting);

    /**
    * 批量删除
    */
    void removeByIds(List ids);

    /**
    * 删除
    */
    void removeById(Long id);
}

