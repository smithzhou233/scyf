package com.hngf.service.score;

import com.hngf.common.utils.PageUtils;
import com.hngf.entity.score.ScoreLog;
import java.util.Map;
import java.util.List;

/**
 * 绩效考核打分记录日志表
 *
 * @author hngf
 * @email 
 * @date 2020-05-27 10:11:53
 */
public interface ScoreLogService {

    PageUtils queryPage(Map<String, Object> params,Integer pageNum,Integer pageSize,String order);

    /**
    * 根据ID查询
    */
    ScoreLog getById(Long id);

    /**
    * 保存
    */
    void save(ScoreLog ScoreLog);

    /**
    * 更新
    */
    void update(ScoreLog ScoreLog);

    /**
    * 批量删除
    */
    void removeByIds(List ids);

    /**
    * 删除
    */
    void removeById(Long id);
}

