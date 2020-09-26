package com.hngf.service.scyf;

import com.hngf.common.utils.PageUtils;
import com.hngf.entity.scyf.SecureProduc;
import java.util.Map;
import java.util.List;

/**
 * 安全生产基本信息表
 *
 * @author hngf
 * @email 
 * @date 2020-05-22 16:52:31
 */
public interface SecureProducService {

    PageUtils queryPage(Map<String, Object> params);

    /**
    * 根据ID查询
    */
    SecureProduc getById(Long id);

    /**
    * 保存
    */
    void save(SecureProduc SecureProduc);

    /**
    * 更新
    */
    void update(SecureProduc SecureProduc);

    /**
    * 批量删除
    */
    void removeByIds(List ids);

    /**
    * 删除
    */
    void removeById(Long id);
}

