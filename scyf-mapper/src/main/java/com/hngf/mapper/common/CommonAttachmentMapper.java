package com.hngf.mapper.common;

import com.hngf.entity.common.CommonAttachment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Map;
import java.util.List;

/**
 * 业务通用附件表
 * 
 * @author hngf
 * @email 
 * @date 2020-06-13 15:24:30
 */
@Mapper
public interface CommonAttachmentMapper {

    List<CommonAttachment> findList(Map<String, Object> params);

    CommonAttachment findById(Long id);
    /**
    * @Author: zyj
    * @Description:根据关联id查找附件
    * @Param
    * @Date 16:10 2020/6/13
    */
    List<CommonAttachment> selectByOwnerKey(Long ownerId);
    /**
    * @Author: zyj
    * @Description:删除关联的附件
    * @Param
    * @Date 16:33 2020/6/13
    */
    void deleteByOwnerId(Long ownerId);

    void add(CommonAttachment CommonAttachment);

    int addForeach(List<CommonAttachment> list);

    void update(CommonAttachment CommonAttachment);

    int deleteById(@Param("id")Long id);

    int deleteByIds(@Param("ids") List ids);
}
