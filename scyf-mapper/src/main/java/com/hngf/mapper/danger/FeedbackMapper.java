package com.hngf.mapper.danger;

import com.hngf.entity.danger.Feedback;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Map;
import java.util.List;

/**
 * 反馈
 * 
 * @author hngf
 * @email 
 * @date 2020-05-20 17:36:24
 */
@Mapper
public interface FeedbackMapper {

    List<Map<String,Object>> findList(Map<String, Object> params);

    Feedback findById(Long id);
    /**
    * @Author: zyj
    * @Description:【APP】我的，反馈列表查询
    * @Param companyId企业id resultValue 处理结果： 0未处理；1已处理 creater 创建人，即检查人
    * @Date 15:50 2020/6/18
    */
    List<Map<String, Object>> findAllList(Map<String, Object> params);
    /**
    * @Author: zyj
    * @Description:【APP】我的，反馈列表详情信息
    * @Param feedbackId 主键id
    * @Date 16:17 2020/6/18
    */
    Map<String, Object> getDetailById(Long feedbackId);

    Long add(Feedback Feedback);

    void update(Feedback Feedback);

    int deleteById(@Param("id") Long id);

    int deleteByIds(@Param("ids") List ids);
}
