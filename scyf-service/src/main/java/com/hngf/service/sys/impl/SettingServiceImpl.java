package com.hngf.service.sys.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hngf.common.utils.Constant;
import com.hngf.common.utils.PageUtils;
import com.hngf.entity.sys.Setting;
import com.hngf.mapper.sys.SettingMapper;
import com.hngf.service.sys.SettingService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;


@Service("SettingService")
public class SettingServiceImpl implements SettingService {

    @Autowired
    private SettingMapper settingMapper;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        int pageNum = null!= params.get("pageNum") ? Integer.parseInt(params.get("pageNum").toString()) : 1;
        int pageSize = null != params.get("pageSize") ? Integer.parseInt(params.get("pageSize").toString()) : Constant.PAGE_SIZE;
        //利用PageHelper分页查询 注意：这个一定要放查询语句的前一行,否则无法进行分页,因为它对紧随其后第一个sql语句有效
        PageHelper.startPage(pageNum, pageSize);
        List<Setting> list = settingMapper.findList(params);
        PageInfo<Setting> pageInfo = new PageInfo<Setting>(list);
        return new PageUtils(list,(int)pageInfo.getTotal(),pageInfo.getPages(),pageInfo.getPageSize(),pageInfo.getPrePage() + 1);
    }
    /**
     * @Author: zyj
     * @Description:【APP】判断版本是否已经更新
     * @Param settingKey 系统配置key
     * @Date 10:32 2020/6/19
     */
    @Override
    public Setting findBySettingKey(String settingKey){

        return settingMapper.findBySettingKey(settingKey);
    }
    @Override
    public Setting getById(Long id){
        return settingMapper.findById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(Setting Setting) {
        settingMapper.add(Setting);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(Setting Setting) {
        settingMapper.update(Setting);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeByIds(List ids) {
        settingMapper.deleteByIds(ids);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeById(Long id) {
        settingMapper.deleteById(id);
    }

    @Override
    public Setting selectByPrimaryKey(String settingKey) {
        return this.settingMapper.findBySettingKey(settingKey);
    }
    @Override
    public Map<String, String> getAndroidLastestVersion() {
        return this.settingMapper.getAndroidLastestVersion();
    }

    @Override
    public int updateBySettingKey(Setting setting){
        return this.settingMapper.updateBySettingKey(setting);
    }
    @Override
    public List<Setting> selectListBySettingKey(String settingKey){
        if(null != settingKey && StringUtils.isNotBlank(settingKey)){
            return this.settingMapper.selectListBySettingKey(settingKey);
        }
        return null ;
    }
}
