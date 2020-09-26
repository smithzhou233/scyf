package com.hngf.api.common.aspect;

import com.hngf.api.common.annotation.DataFilter;
import com.hngf.common.exception.ScyfException;
import com.hngf.common.utils.Constant;
import com.hngf.common.utils.RedisUtils;
import com.hngf.entity.sys.User;
import com.hngf.service.sys.GroupMemberPositionGrantService;
import com.hngf.service.sys.GroupService;

import org.apache.commons.lang.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * 数据过滤切面
 */
@Component
@Aspect
public class DataFilterAspect {

    @Autowired
    private GroupService groupService;
    @Autowired
    private GroupMemberPositionGrantService groupMemberPositionGrantService;
    @Autowired
    private RedisUtils  redisUtils;
    @Pointcut("@annotation(com.hngf.api.common.annotation.DataFilter)")
    public void dataFilterCut() {

    }

    @Before(value = "dataFilterCut()")
    public void before(JoinPoint joinPoint){

        Object params = joinPoint.getArgs()[0];

        if(params != null && params instanceof Map){
            User user =  (User) redisUtils.get("user",User.class);

            //如果不是超级管理员，则进行数据过滤
            if(user.getUserId() != Constant.SUPER_ADMIN){
                Map map = (Map)params;
                map.put(Constant.SQL_FILTER, getSQLFilter(user, joinPoint));
            }
            return ;
        }

        throw new ScyfException("数据权限接口，只能是Map类型参数，且不能为NULL");
    }

    /**
     * 获取数据过滤的SQL
     */
    private String getSQLFilter(User user, JoinPoint point){
        MethodSignature signature = (MethodSignature) point.getSignature();
        DataFilter dataFilter = signature.getMethod().getAnnotation(DataFilter.class);
        //获取group_id相关表的别名
        String tableAliasGroup = dataFilter.tableAliasGroup();
        if(StringUtils.isNotBlank(tableAliasGroup)){
            tableAliasGroup +=  ".";
        }

        //获取user_id相关表的别名
        String tableAliasUser = dataFilter.tableAliasUser();
        if(StringUtils.isNotBlank(tableAliasUser)){
            tableAliasUser +=  ".";
        }

        //部门ID列表
        Set<Long> groupIdList = new HashSet<>();

        //用户授权的部门ID
        Map<String, Object> params = new HashMap<>();
        params.put("companyId",user.getCompanyId());
        params.put("userId", user.getUserId());
        List<Long> grantList = groupMemberPositionGrantService.getGrantGroupId(params);
        grantList.forEach(grantGroupId ->{
            groupIdList.add(grantGroupId);
        });

        //group子部门ID列表
        if(dataFilter.subGroup()){
            List<Long> subGroupIdList = groupService.getSubGroupIdList(user.getCompanyGroupId());
            groupIdList.addAll(subGroupIdList);
        }

        StringBuilder sqlFilter = new StringBuilder();
        sqlFilter.append(" (");

        if(groupIdList.size() > 0){
            sqlFilter.append(tableAliasGroup).append(dataFilter.groupId()).append(" in(").append(StringUtils.join(groupIdList, ",")).append(")");
        }

        //没有本部门数据权限，也能查询本人数据
        if(dataFilter.user()){
            if(groupIdList.size() > 0){
                sqlFilter.append(" or ");
            }
            sqlFilter.append(tableAliasUser).append(dataFilter.userId()).append("=").append(user.getUserId());
        }

        sqlFilter.append(")");

        if(sqlFilter.toString().trim().equals("()")){
            return null;
        }

        return sqlFilter.toString();
    }
}
