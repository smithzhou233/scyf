package com.hngf.service.sys;

import com.hngf.common.utils.PageUtils;
import com.hngf.entity.sys.Setting;
import java.util.Map;
import java.util.List;

/**
 * 
 *
 * @author hngf
 * @email 
 * @date 2020-05-20 17:31:55
 */
public interface SettingService {

    PageUtils queryPage(Map<String, Object> params);
    /**
     * @Author: zyj
     * @Description:【APP】判断版本是否已经更新
     * @Param settingKey 系统配置key
     * @Date 10:32 2020/6/19
     */
    Setting findBySettingKey(String settingKey);
    /**
    * 根据ID查询
    */
    Setting getById(Long id);

    /**
    * 保存
    */
    void save(Setting Setting);

    /**
    * 更新
    */
    void update(Setting Setting);

    /**
    * 批量删除
    */
    void removeByIds(List ids);

    /**
    * 删除
    */
    void removeById(Long id);


    Setting selectByPrimaryKey(String settingKey);

    /**
     * 获取android版本的app版本信息
     * @return
     */
    Map<String, String> getAndroidLastestVersion();

    int updateBySettingKey(Setting setting);

    List<Setting> selectListBySettingKey(String settingKey);


}

