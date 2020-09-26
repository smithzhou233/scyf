package com.hngf.service.sys;

import com.hngf.common.utils.PageUtils;
import com.hngf.entity.sys.UserSite;
import java.util.Map;
import java.util.List;

/**
 * 手机APP位置坐标
 *
 * @author hngf
 * @email 
 * @date 2020-07-09 14:03:19
 */
public interface UserSiteService {

    PageUtils queryPage(Map<String, Object> params, Integer pageNum, Integer pageSize, String order);

    UserSite getSite(Map<String, Object> params);

    /**
    * 保存
    */
    int save(UserSite UserSite);

    /**
    * 更新
    */
    int update(UserSite UserSite);
}

