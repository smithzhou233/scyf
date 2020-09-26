package com.hngf.service.danger;

import com.hngf.common.utils.PageUtils;
import com.hngf.entity.danger.Feedback;
import java.util.Map;
import java.util.List;

/**
 * 反馈
 *
 * @author hngf
 * @email 
 * @date 2020-05-20 17:36:24
 */
public interface FeedbackService {

    PageUtils queryPage(Map<String, Object> params);

    /**
    * 根据ID查询
    */
    Feedback getById(Long id);
    /**
     * @Author: zyj
     * @Description:【APP】我的，反馈列表查询
     * @Param companyId企业id resultValue 处理结果： 0未处理；1已处理 creater 创建人，即检查人
     * @Date 15:50 2020/6/18
     */
    PageUtils findAllList(Map<String, Object> params);
    /**
     * @Author: zyj
     * @Description:【APP】我的，反馈列表详情信息
     * @Param feedbackId 主键id
     * @Date 16:17 2020/6/18
     */
    Map<String, Object> getDetailById(Long feedbackId);
    /**
    * @Author: zyj
    * @Description:【APP】提交反馈信息
    * @Param
    * @Date 17:01 2020/6/18
    */
    void saveBaseChecked(Feedback feedback, String url,String extendName);
    /**
    * 保存
    */
    void save(Feedback Feedback);

    /**
    * 更新
    */
    void update(Feedback Feedback);

    /**
    * 批量删除
    */
    void removeByIds(List ids);

    /**
    * 删除
    */
    void removeById(Long id);
}

