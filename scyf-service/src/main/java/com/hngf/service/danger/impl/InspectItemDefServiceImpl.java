package com.hngf.service.danger.impl;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hngf.common.utils.PageUtils;
import com.hngf.common.utils.Constant;

import com.hngf.mapper.danger.InspectItemDefMapper;
import com.hngf.entity.danger.InspectItemDef;
import com.hngf.service.danger.InspectItemDefService;


@Service("InspectItemDefService")
public class InspectItemDefServiceImpl implements InspectItemDefService {

    @Autowired
    private InspectItemDefMapper InspectItemDefMapper;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        int pageNum = null!= params.get("pageNum") ? Integer.parseInt(params.get("pageNum").toString()) : 1;
        int pageSize = null != params.get("pageSize") ? Integer.parseInt(params.get("pageSize").toString()) : Constant.PAGE_SIZE;
        //利用PageHelper分页查询 注意：这个一定要放查询语句的前一行,否则无法进行分页,因为它对紧随其后第一个sql语句有效
        PageHelper.startPage(pageNum, pageSize);
        List<InspectItemDef> list = InspectItemDefMapper.findList(params);
        PageInfo<InspectItemDef> pageInfo = new PageInfo<InspectItemDef>(list);
        return new PageUtils(list,(int)pageInfo.getTotal(),pageInfo.getPages(),pageInfo.getPageSize(),pageInfo.getPrePage() + 1);
    }

    @Override
    public InspectItemDef getById(Long id){
        return InspectItemDefMapper.findById(id);
    }

    @Override
    public int getInspectItemCount(Map<String, Object> params) {
        return InspectItemDefMapper.findInspectItemCount(params);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int save(InspectItemDef InspectItemDef) {
        return InspectItemDefMapper.add(InspectItemDef);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int update(InspectItemDef InspectItemDef) {
        return InspectItemDefMapper.update(InspectItemDef);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int removeByIds(List ids) {
        return InspectItemDefMapper.deleteByIds(ids);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int removeById(Long id) {
        int res = InspectItemDefMapper.deleteById(id);
        //删除后关联删除子节点数据
        if (res > 0) {
            Map<String, Object> params = new HashMap<>();
            params.put("parentId", id);

            List<Long> ids = new ArrayList<>();
            getChildrenId(id, ids);
            if(ids.size()>0){
                this.removeByIds(ids);
            }
        }
        return res;
    }

    /**
     * 递归查询检查项下所有子节点
     * @param parentId
     * @param ids
     */
    private void getChildrenId(Long parentId,List<Long> ids){
        if (null != parentId) {
            Map<String, Object> params = new HashMap<>();
            params.put("parentId", parentId);
            List<InspectItemDef> list = InspectItemDefMapper.findList(params);

            if (list.size() > 0) {
                list.forEach(iid ->{
                    ids.add(iid.getInspectItemDefId());
                    getChildrenId(iid.getInspectItemDefId(),ids);
                });
            }
        }
    }
}
