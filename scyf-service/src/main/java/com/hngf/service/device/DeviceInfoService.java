package com.hngf.service.device;

import com.alibaba.fastjson.JSONObject;
import com.hngf.common.utils.PageUtils;
import com.hngf.entity.device.DeviceInfo;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.util.Map;
import java.util.List;

/**
 * 设备信息表
 *
 * @author hngf
 * @email 
 * @date 2020-06-16 15:10:13
 */
public interface DeviceInfoService {
    /**
     * @Author: zyj
     * @Description:查看设备管理列表
     * @Param  companyId 企业id  groupId 群组id  deviceTypeId 设备类型id  deviceStatus 设备状态  keyword 关键词
     * @Date 15:51 2020/6/16
     */
    PageUtils queryPage(Map<String, Object> params,Integer pageNum,Integer pageSize,String order);

    /**
    * 根据ID查询
    */
    DeviceInfo getById(Long id);

    /**
    * 保存
    */
    void save(DeviceInfo DeviceInfo);

    /**
    * 更新
    */
    void update(DeviceInfo DeviceInfo);

    /**
    * 批量删除
    */
    void removeByIds(List ids);

    /**
    * 删除
    */
    void removeById(Long id);

    /**
     * 导入设备信息
     * @param path
     * @throws Exception
     */
    void importExcel(String path,Long companyId,Long groupId,Long userId) throws Exception;
    /**
     * 导出设备信息
     * @param cId
     * @throws Exception
     */
    XSSFWorkbook exportExcel(Long cId);
}

