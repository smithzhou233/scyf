package com.hngf.service.risk.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hngf.common.utils.PageUtils;
import com.hngf.entity.risk.MapInfo;
import com.hngf.mapper.risk.MapInfoMapper;
import com.hngf.service.risk.MapInfoService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;


@Service("MapInfoService")
public class MapInfoServiceImpl implements MapInfoService {

    @Autowired
    private MapInfoMapper mapInfoMapper;

    @Override
    public PageUtils queryPage(Map<String, Object> params,Integer pageNum,Integer pageSize,String order) {
        //利用PageHelper分页查询 注意：这个一定要放查询语句的前一行,否则无法进行分页,因为它对紧随其后第一个sql语句有效
        if(StringUtils.isNotBlank(order)){
            PageHelper.startPage(pageNum, pageSize,order);
        }else{
            PageHelper.startPage(pageNum, pageSize);
        }
        List<MapInfo> list = mapInfoMapper.findList(params);
        PageInfo<MapInfo> pageInfo = new PageInfo<MapInfo>(list);
        return new PageUtils(list,(int)pageInfo.getTotal(),pageInfo.getPages(),pageInfo.getPageSize(),pageInfo.getPrePage() + 1);
    }

    @Override
    public MapInfo getById(Long id){
        return mapInfoMapper.findById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(MapInfo MapInfo) {
        mapInfoMapper.add(MapInfo);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(MapInfo MapInfo) {
        mapInfoMapper.update(MapInfo);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeByIds(List ids) {
        mapInfoMapper.deleteByIds(ids);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeById(Long id) {
        mapInfoMapper.deleteById(id);
    }

    /**
     * 查看地图类型/使用类型
     * yfh
     * 2020/06/29
     * @param dictType
     * @return
     */
    @Override
    public List<Map<String, Object>> getMapType(String dictType) {
        return mapInfoMapper.getMapType(dictType);
    }

    @Override
    public List<Map<String, Object>> getAllMapList(Map<String, Object> params) {
        return mapInfoMapper.getAllMapList(params);
    }

}
