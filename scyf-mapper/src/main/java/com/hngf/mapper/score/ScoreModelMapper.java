package com.hngf.mapper.score;

import com.hngf.entity.score.ScoreModel;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Map;
import java.util.List;

/**
 * 绩效考核模式配置表
 * 
 * @author hngf
 * @email 
 * @date 2020-05-27 10:11:53
 */
@Mapper
public interface ScoreModelMapper {

    List<ScoreModel> findList(Map<String, Object> params);

    ScoreModel findById(Long id);

    void add(ScoreModel ScoreModel);

    void update(ScoreModel ScoreModel);

    int deleteById(@Param("id")Long id);

    int deleteByIds(@Param("ids") List ids);
    List<ScoreModel> queryByModelSettingPage(@Param("companyId") Long companyId);
    /**
    * @Author: zyj
    * @Description:通过企业id查询绩效考核配置表
    * @Param
    * @Date 15:17 2020/6/12
    */
    ScoreModel getModelSetting(@Param("companyId") Long companyId,@Param("scoreModelType") Integer scoreModelType,@Param("scoreModelStatus") Integer scoreModelStatus);
    /**
    * @Author: zyj
    * @Description:查询当前企业下面绩效考核配置的所有信息
    * @Param
    * @Date 15:30 2020/6/12
    */
    List<ScoreModel> getModelSettingList(Map<String, Object> map);
    /**
     * @Author: zyj
     * @Description:通过企业id,父级企业id,年份,查询初始化分数
     * @Param companyId 企业id  scoreParentId企业父级id year年份
     * @Date 16:19 2020/6/11
     */
    ScoreModel getThisYearinitScore(@Param("companyId") Long companyId,@Param("scoreParentId") Long scoreParentId,@Param("year") String year);
    /**
    * @Author: zyj
    * @Description:删除当前企业当前年份下级考核数据
    * @Param scoreParentId企业父级id year年份
    * @Date 17:05 2020/6/11
    */
    void deleteByParentId(@Param("scoreParentId") Long scoreParentId,@Param("year") String year);

    int deleteBatchByCompanyId(@Param("companyId") Long companyId, @Param("updatedBy") Long updatedBy) ;
}
