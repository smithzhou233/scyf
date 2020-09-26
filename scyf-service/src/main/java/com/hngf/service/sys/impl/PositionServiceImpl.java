package com.hngf.service.sys.impl;

import com.hngf.common.exception.ScyfException;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import java.util.Map;
import java.util.List;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hngf.common.utils.PageUtils;

import com.hngf.mapper.sys.PositionMapper;
import com.hngf.entity.sys.Position;
import com.hngf.service.sys.PositionService;


@Service("PositionService")
public class PositionServiceImpl implements PositionService {

    @Autowired
    private PositionMapper PositionMapper;

    @Override
    public PageUtils queryPage(Map<String, Object> params,Integer pageNum,Integer pageSize,String order) {
        //利用PageHelper分页查询 注意：这个一定要放查询语句的前一行,否则无法进行分页,因为它对紧随其后第一个sql语句有效
        if(StringUtils.isNotBlank(order)){
            PageHelper.startPage(pageNum, pageSize,order);
        }else{
            PageHelper.startPage(pageNum, pageSize);
        }
        List<Position> list = PositionMapper.findList(params);
        PageInfo<Position> pageInfo = new PageInfo<Position>(list);
        return new PageUtils(list,(int)pageInfo.getTotal(),pageInfo.getPages(),pageInfo.getPageSize(),pageInfo.getPrePage() + 1);
    }

    @Override
    public Position getById(Long id){
        return PositionMapper.findById(id);
    }

    @Override
    public List<Position> getList(Map<String, Object> params) {
        return PositionMapper.findByMap(params);
    }

    @Override
    public Position findPositionTitle(Long companyId, String positionTitle) {
        return PositionMapper.findPositionTitle(companyId,positionTitle);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(Position Position) {
       /* List<Position> p = PositionMapper.findByTitle(Position.getPositionTitle());
        if (null != p && p.size() > 0) {
            throw new ScyfException(Position.getPositionTitle() + "已存在，不能重复添加");
        }*/
        PositionMapper.add(Position);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(Position Position) {
        PositionMapper.update(Position);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeByIds(List ids) {
        PositionMapper.deleteByIds(ids);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeById(Long id) {
        PositionMapper.deleteById(id);
    }

    @Override
    public List<Position> getPositionByGroup(Map<String, Object> paraMap) {
        return PositionMapper.getPositionByGroup(paraMap);
    }

}
