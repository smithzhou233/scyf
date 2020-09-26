package com.hngf.service.sys.impl;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hngf.common.utils.PageUtils;

import com.hngf.mapper.sys.UserSiteMapper;
import com.hngf.entity.sys.UserSite;
import com.hngf.service.sys.UserSiteService;


@Service("UserSiteService")
public class UserSiteServiceImpl implements UserSiteService {

    @Autowired
    private UserSiteMapper userSiteMapper;

    @Override
    public PageUtils queryPage(Map<String, Object> params,Integer pageNum,Integer pageSize,String order) {
        //利用PageHelper分页查询 注意：这个一定要放查询语句的前一行,否则无法进行分页,因为它对紧随其后第一个sql语句有效
        if(StringUtils.isNotBlank(order)){
            PageHelper.startPage(pageNum, pageSize,order);
        }else{
            PageHelper.startPage(pageNum, pageSize);
        }
        List<UserSite> list = userSiteMapper.findList(params);
        PageInfo<UserSite> pageInfo = new PageInfo<UserSite>(list);
        return new PageUtils(list,(int)pageInfo.getTotal(),pageInfo.getPages(),pageInfo.getPageSize(),pageInfo.getPrePage() + 1);
    }

    @Override
    public UserSite getSite(Map<String, Object> params){
        return userSiteMapper.findByMap(params);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int save(UserSite userSite) {
        //保存之前先查询，如果已存在坐标记录，直接更新，未存在新增
        Map<String, Object> params = new HashMap<>();
        params.put("userId", userSite.getUserId());
        params.put("companyId", userSite.getCompanyId());
        params.put("groupId", userSite.getGroupId());
        params.put("phoneCode", userSite.getPhoneCode());
        UserSite one = this.getSite(params);
        if (null == one || null == one.getSiteId()) {
            userSite.setCreatedTime(new Date());
            userSite.setSiteTime(new Date());
            return userSiteMapper.add(userSite);
        }
        userSite.setSiteId(one.getSiteId());
        return this.update(userSite);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int update(UserSite userSite) {
        userSite.setSiteTime(new Date());
        return userSiteMapper.update(userSite);
    }
}
