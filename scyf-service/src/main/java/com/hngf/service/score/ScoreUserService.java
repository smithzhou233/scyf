package com.hngf.service.score;

import com.hngf.common.utils.PageUtils;
import com.hngf.entity.score.ScoreUser;
import java.util.Map;
import java.util.List;

/**
 * 用户绩效考核总得分表
 *
 * @author zhangfei
 * @email 
 * @date 2020-05-27 10:11:53
 */
public interface ScoreUserService {

    PageUtils queryPage(Map<String, Object> params,Integer pageNum,Integer pageSize,String order);
    /**
    * @Author: zyj
    * @Description:用户得分统计图和部门得分统计图
    * @Param
    * @Date 15:00 2020/6/12
    */
    Map<String, List<Integer>> getMonthGradeStatistics(Map<String, Object> map);
    /**
    * 根据ID查询
    */
    ScoreUser getById(Long id);

    /**
     * 划分等级
     * @param companyId
     * @param groupId
     * @param userId
     * @param settingCode
     * @param detailId
     * @return
     * @throws Exception
     */
    int goGrade(Long companyId, Long groupId, Long userId, Integer settingCode, Long detailId) throws Exception;

    /**
    * 保存
    */
    void save(ScoreUser ScoreUser);

    /**
    * 更新
    */
    void update(ScoreUser ScoreUser);

    /**
    * 批量删除
    */
    void removeByIds(List ids);

    /**
    * 删除
    */
    void removeById(Long id);
}

