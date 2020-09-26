package com.hngf.mapper.sys;

import com.hngf.entity.sys.UserSite;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Map;
import java.util.List;

/**
 * 手机APP位置坐标
 * 
 * @author hngf
 * @email 
 * @date 2020-07-09 14:03:19
 */
@Mapper
public interface UserSiteMapper {

    List<UserSite> findList(Map<String, Object> params);

    UserSite findById(Long id);

    UserSite findByMap(Map<String, Object> params);

    int add(UserSite UserSite);

    int update(UserSite UserSite);
}
