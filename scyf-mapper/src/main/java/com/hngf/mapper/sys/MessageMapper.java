package com.hngf.mapper.sys;

import com.hngf.entity.sys.Message;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Map;
import java.util.List;

/**
 * 消息表
 * 
 * @author hngf
 * @email 
 * @date 2020-06-11 14:03:07
 */
@Mapper
public interface MessageMapper {

    List<Message> findList(Map<String, Object> params);

    /**
     * 查询列表
     * @param params
     * @return
     */
    List<Message> findByMap(Map<String, Object> params);

    Message findById(Long id);

    void add(Message Message);

    void update(Message Message);

    int deleteById(@Param("id") Long id);

    int deleteByIds(@Param("ids") List ids);

    /**
     * 消息未读统计
     * @param params
     * @return
     */
    int findMessageCount(Map<String, Object> params);

    /**
     * 消息设为已读
     * @param params
     * @return
     */
    int setRead(Map<String, Object> params);
}
