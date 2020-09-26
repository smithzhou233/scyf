package com.hngf.mapper.device;

import com.hngf.entity.device.DeviceInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Map;
import java.util.List;

/**
 * 设备信息表
 * 
 * @author hngf
 * @email 
 * @date 2020-06-16 15:10:13
 */
@Mapper
public interface DeviceInfoMapper {
    /**
    * @Author: zyj
    * @Description:查看设备管理列表
    * @Param  companyId 企业id  groupId 群组id  deviceTypeId 设备类型id  deviceStatus 设备状态  keyword 关键词
    * @Date 15:51 2020/6/16
    */
    List<DeviceInfo> findList(Map<String, Object> params);

    DeviceInfo findById(Long id);

    void add(DeviceInfo DeviceInfo);

    void update(DeviceInfo DeviceInfo);

    int deleteById(@Param("id")Long id);

    int deleteByIds(@Param("ids") List ids);
}
