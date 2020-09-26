package com.hngf.mapper.sys;

import com.hngf.entity.sys.CommonClassify;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Map;
import java.util.List;

/**
 * 公司通用分类表
 * 
 * @author hngf
 * @email 
 * @date 2020-05-20 17:31:55
 */
@Mapper
public interface CommonClassifyMapper {
    /**
     * @Author: zyj
     * @Description:默认渲染列表数据
     * @Date 14:48 2020/5/22
     */
    List<CommonClassify> findList(Map<String, Object> params);

    /**
     * 根据类型查询
     * zhangfei
     * 2020/06/08
     * @param params
     * @return
     */
    List<CommonClassify> findByClassifyByType(Map<String, Object> params);

    /**
     * @Author: zyj
     * @Description:通过classifyId获取公司通用分类表数据
     * @Date 14:48 2020/5/22
     */
    CommonClassify findById(Long id);
    /**
     * @Author: zyj
     * @Description:保存信息
     * @Date 14:48 2020/5/22
     */
    void add(CommonClassify CommonClassify);
    /**
     * @Author: zyj
     * @Description:修改信息
     * @Date 14:48 2020/5/22
     */
    void update(CommonClassify CommonClassify);
    /**
     * @Author: zyj
     * @Description:删除信息
     * @Date 14:48 2020/5/22
     */
    int deleteById(@Param("id") Long id);
    /**
     * @Author: zyj
     * @Description:批量删除信息
     * @Date 14:48 2020/5/22
     */
    int deleteByIds(@Param("ids") List ids);

    CommonClassify selectById(@Param("companyId")Long companyId);

    int deleteBatchByCompanyId(@Param("companyId") Long companyId, @Param("updatedBy") Long updatedBy) ;

    int addBatch(@Param("commonClassifyList") List<CommonClassify> commonClassifyList );

}
