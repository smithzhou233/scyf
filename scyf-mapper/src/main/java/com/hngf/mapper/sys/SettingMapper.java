package com.hngf.mapper.sys;

import com.hngf.entity.sys.Setting;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Map;
import java.util.List;

/**
 * 
 * 
 * @author hngf
 * @email 
 * @date 2020-05-20 17:31:55
 */
@Mapper
public interface SettingMapper {

    List<Setting> findList(Map<String, Object> params);
    /**
    * @Author: zyj
    * @Description:【APP】判断版本是否已经更新
    * @Param settingKey 系统配置key
    * @Date 10:32 2020/6/19
    */
    Setting findBySettingKey(String settingKey);

    Setting findById(Long id);

    void add(Setting Setting);

    void update(Setting Setting);

    int deleteById(@Param("id") Long id);

    int deleteByIds(@Param("ids") List ids);

    int updateBySettingKey(Setting setting);

    List<Setting> selectListBySettingKey(@Param("settingKey") String settingKey);

    /**
     * 获取android版本的app版本信息
     * @return
     */
    Map<String, String> getAndroidLastestVersion();
}
