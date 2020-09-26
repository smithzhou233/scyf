package com.hngf.service.sys;

import com.hngf.common.utils.PageUtils;
import com.hngf.entity.sys.Info;

import java.util.Map;
import java.util.List;

/**
 * 系统信息表
 *
 * @author hngf
 * @email 
 * @date 2020-05-22 09:58:36
 */
public interface InfoService {
    /**
     * @Author: zyj
     * @Description:查询系统信息表数据
     * @Date 14:53 2020/5/22
     */
    PageUtils queryPage(Map<String, Object> params,Integer pageNum,Integer pageSize,String order);
    /**
     * @Author: zyj
     * @Description:通过企业id查询数据如果没有添加一条数据
     * @Date 10:37 2020/5/22
     */
    Info getList(Long companyId,Integer userType);

    /**
     * @Author: zyj
     * @Description:通过id查询数据
     * @Date 14:53 2020/5/22
     */
    Info getById(Long id);

    /**
     * @Author: zyj
     * @Description:保存信息
     * @Date 14:53 2020/5/22
     */
    void save(Info Info);

    /**
     * @Author: zyj
     * @Description:修改信息
     * @Date 14:53 2020/5/22
     */
    void update(Info Info);

    /**
     * @Author: zyj
     * @Description:批量删除信息
     * @Date 14:53 2020/5/22
     */
    void removeByIds(List ids);

    /**
     * @Author: zyj
     * @Description:删除信息
     * @Date 14:53 2020/5/22
     */
    void removeById(Long id);

    Info getByCId(Long companyId, Integer userType);
}

