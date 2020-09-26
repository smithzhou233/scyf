package com.hngf.service.sys;

import com.hngf.common.utils.PageUtils;
import com.hngf.entity.sys.Message;
import java.util.Map;
import java.util.List;

/**
 * 消息表
 *
 * @author hngf
 * @email 
 * @date 2020-06-11 14:03:07
 */
public interface MessageService {

    PageUtils queryPage(Map<String, Object> params, Integer pageNum, Integer pageSize, String order);

    /**
    * 根据ID查询
    */
    Message getById(Long id);

    /**
     * 查询消息列表
     * @param params
     * @return
     */
    List<Message> getList(Map<String, Object> params);

    /**
     * 查询消息总数
     * @param params
     * @return
     */
    Integer getMessageCount(Map<String, Object> params);

    /**
     * 消息设为已读
     * @param params
     * @return
     */
    Integer setRead(Map<String, Object> params);

    /**
    * 保存
    */
    void save(Message Message);

    /**
    * 更新
    */
    void update(Message Message);

    /**
    * 批量删除
    */
    void removeByIds(List ids);

    /**
    * 删除
    */
    void removeById(Long id);
}

