package com.hngf.service.sys;

import com.hngf.common.utils.PageUtils;
import com.hngf.entity.sys.CommonClassify;
import java.util.Map;
import java.util.List;

/**
 * 公司通用分类表
 *
 * @author hngf
 * @email 
 * @date 2020-05-20 17:31:55
 */
public interface CommonClassifyService {
    /**
     * @Author: zyj
     * @Description:默认渲染列表数据
     * @Date 14:48 2020/5/22
     */
    PageUtils queryPage(Map<String, Object> params,Integer pageNum,Integer pageSize,String order);

    List<CommonClassify> getByClassifyByType(Map<String, Object> params);

    /**
     * @Author: zyj
     * @Description:通过classifyId获取公司通用分类表数据
     * @Date 14:48 2020/5/22
     */
    CommonClassify getById(Long id);

    /**
     * @Author: zyj
     * @Description:保存信息
     * @Date 14:48 2020/5/22
     */
    void save(CommonClassify CommonClassify);

    /**
     * @Author: zyj
     * @Description:修改信息
     * @Date 14:48 2020/5/22
     */
    void update(CommonClassify CommonClassify);

    /**
     * @Author: zyj
     * @Description:批量删除信息
     * @Date 14:48 2020/5/22
     */
    void removeByIds(List ids);

    /**
     * @Author: zyj
     * @Description:删除信息
     * @Date 14:48 2020/5/22
     */
    void removeById(Long id);

}

