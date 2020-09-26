package com.hngf.mapper.sys;


import com.hngf.entity.sys.Info;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Map;
import java.util.List;

/**
 * 系统信息表
 * 
 * @author hngf
 * @email 
 * @date 2020-05-22 09:58:36
 */
@Mapper
public interface InfoMapper {
    /**
     * @Author: zyj
     * @Description:查询系统信息表数据
     * @Date 14:53 2020/5/22
     */
    List<Info> findList(Map<String, Object> params);
    /**
     * @Author: zyj
     * @Description:通过企业id查询数据如果没有添加一条数据
     * @Date 10:37 2020/5/22
     */
    Info getList(Long companyId);
    /**
     * @Author: zyj
     * @Description:通过id查询数据
     * @Date 14:53 2020/5/22
     */
    Info findById(Long id);
    /**
     * @Author: zyj
     * @Description:保存信息
     * @Date 14:53 2020/5/22
     */
    void add(Info Info);
    /**
     * @Author: zyj
     * @Description:修改信息
     * @Date 14:53 2020/5/22
     */
    void update(Info Info);
    /**
     * @Author: zyj
     * @Description:删除信息
     * @Date 14:53 2020/5/22
     */
    int deleteById(@Param("id") Long id);
    /**
     * @Author: zyj
     * @Description:批量删除信息
     * @Date 14:53 2020/5/22
     */
    int deleteByIds(@Param("ids") List ids);

    Info getByCId(@Param("companyId")Long companyId, @Param("infoType")Integer infoType);
}
