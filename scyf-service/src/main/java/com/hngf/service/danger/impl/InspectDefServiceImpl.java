package com.hngf.service.danger.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hngf.common.utils.PageUtils;
import com.hngf.dto.danger.InspectDefTreeDto;
import com.hngf.entity.danger.InspectDef;
import com.hngf.entity.danger.InspectItemDef;
import com.hngf.entity.sys.CommonClassify;
import com.hngf.mapper.danger.InspectDefMapper;
import com.hngf.mapper.danger.InspectItemDefMapper;
import com.hngf.mapper.sys.CommonClassifyMapper;
import com.hngf.service.danger.InspectDefService;
import com.hngf.service.utils.IdKit;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


@Service("InspectDefService")
public class InspectDefServiceImpl implements InspectDefService {

    @Autowired
    private InspectDefMapper inspectDefMapper;
    @Autowired
    private InspectItemDefMapper inspectItemDefMapper;

    @Autowired
    private CommonClassifyMapper commonClassifyMapper;
    @Override
    public PageUtils queryPage(Map<String, Object> params,Integer pageNum,Integer pageSize,String order) {
        if(StringUtils.isNotBlank(order)){
            PageHelper.startPage(pageNum, pageSize,order);
        }else{
            PageHelper.startPage(pageNum, pageSize);
        }
        List<InspectDef> list = inspectDefMapper.findByMap(params);
        PageInfo<InspectDef> pageInfo = new PageInfo<InspectDef>(list);
        return new PageUtils(list,(int)pageInfo.getTotal(),pageInfo.getPages(),pageInfo.getPageSize(),pageInfo.getPrePage() + 1);
    }

    @Override
    public List<InspectDef> getByCompanyId(Long companyId) {
        Map<String, Object> params = new HashMap<>();
        params.put("companyId", companyId);
        return inspectDefMapper.findByMap(params);
    }

    @Override
    public List<InspectItemDef> getItems(Map<String, Object> params) {
        return inspectItemDefMapper.findList(params);
    }

    @Override
    public List<InspectDefTreeDto> getTreeItems(Map<String, Object> params) {
        //根据检查表ID查询所有检查项列表
        List<InspectItemDef> list = getItems(params);
        List<InspectDefTreeDto> tree = new ArrayList<>();

        //获取所有顶级项
        list.forEach(iid -> {
            if (iid.getParentId().intValue() == 0) {
                InspectDefTreeDto ddt = new InspectDefTreeDto();
                ddt.setId(iid.getInspectItemDefId());
                ddt.setLabel(iid.getInspectItemDefName());
                tree.add(ddt);
            }
        });

        //如果顶级项有数据，开始查找子节点
        if (tree.size() > 0) {
            tree.forEach(root ->{
                //子节点递归查找添加  传递父节点
                root.setChildren(getChildren(root.getId(),list));
            });
        }
        //清空菜单
        list.clear();
        return tree;
    }

    @Override
    public InspectDef getById(Long id){
        return inspectDefMapper.findById(id);
    }

    @Override
    public List<Long> getIdByName(Map<String, Object> params) {
        return inspectDefMapper.findIdByName(params);
    }

    @Override
    public List<InspectDef> getInspectDefByMap(Map<String, Object> params) {
        return inspectDefMapper.findByMap(params);
    }
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int save(InspectDef InspectDef) {
        return inspectDefMapper.add(InspectDef);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int update(InspectDef InspectDef) {
        return inspectDefMapper.update(InspectDef);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int removeByIds(List ids) {
        return inspectDefMapper.deleteByIds(ids);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int removeById(Long id) {
        return inspectDefMapper.deleteById(id);
    }

    /**
     * 添加 检查定义表-检查表-安全检查表 信息
     * yfh
     * 2020/06/09
     * @param companyId
     */
    @Override
    public void initBizCheckDef(Long companyId) {
        List<InspectDef> inspectDefs = inspectDefMapper.selectListByCompanyId(companyId);
        if ( null == inspectDefs || inspectDefs.isEmpty()) {
            Map<String,Object> params = new HashMap<>(8);
            params.put("companyId",companyId);
            params.put("classifyType",2);
            params.put("classifyValue","0");
            List<CommonClassify> commonClassifyList = commonClassifyMapper.findList(params);
            CommonClassify newCommonClassify ;
            if (null==commonClassifyList || commonClassifyList.isEmpty() ) {
                newCommonClassify = new CommonClassify();
                newCommonClassify.setClassifyDesc("分级管控");
                newCommonClassify.setClassifyName("分级管控");
                newCommonClassify.setClassifyType(2);
                newCommonClassify.setClassifyValue("0");
                newCommonClassify.setCompanyId(companyId);
                newCommonClassify.setCreatedTime(new Date());
                newCommonClassify.setDelFlag(0);
                this.commonClassifyMapper.add(newCommonClassify);
            }else{
                newCommonClassify = commonClassifyList.get(0);
            }
            Long classifyId = newCommonClassify.getClassifyId();
            InspectDef inspectDef=new InspectDef();
            inspectDef.setInspectDefDesc("分级管控检查表");
            inspectDef.setInspectDefName("分级管控");
            inspectDef.setCompanyId(companyId);
            inspectDef.setInspectDefTypeId(classifyId);
            inspectDef.setCreatedTime(new Date());
            inspectDef.setDelFlag(0);
            this.inspectDefMapper.add(inspectDef);
        }
    }

    @Override
    public List<Map<String, Object>> getRiskPointCheckTables(Map<String, Object> params) throws Exception {
        List<Map<String, Object>> mapList = this.inspectDefMapper.findListByRiskPoint(params);
        Iterator var3 = mapList.iterator();

        while(var3.hasNext()) {
            Map<String, Object> stringObjectMap = (Map)var3.next();
            stringObjectMap.put("checkRecordNo", IdKit.getCheckRecordNo());
        }

        return mapList;
    }

    @Override
    public List<Map<String, Object>> getCheckTables(Map<String, Object> params) throws Exception {
        List<Map<String, Object>> mapList = this.inspectDefMapper.getList(params);
        Iterator var3 = mapList.iterator();

        while(var3.hasNext()) {
            Map<String, Object> stringObjectMap = (Map)var3.next();
            stringObjectMap.put("checkRecordNo", IdKit.getCheckRecordNo());
        }

        return mapList;
    }

    //递归获取children节点
    private List<InspectDefTreeDto> getChildren(Long pid,List<InspectItemDef> list) {

        List<InspectDefTreeDto> children = new ArrayList<>();

        if (null != pid){

            list.forEach(data -> {
                if (data.getParentId().longValue() == pid.longValue()) {
                    //构造添加结果集合
                    InspectDefTreeDto ddt = new InspectDefTreeDto();
                    ddt.setId(data.getInspectItemDefId());
                    ddt.setLabel(data.getInspectItemDefName());
                    children.add(ddt);
                }
            });

        }

        //如果children不为空，继续递归遍历children下的子节点
        if (children.size() > 0) {
            children.forEach(data -> {
                data.setChildren(getChildren(data.getId(),list));
            });
        }
        return children;
    }

    @Override
    public int deleteBatchByCompanyId(Long companyId, Long updatedBy) {
        this.commonClassifyMapper.deleteBatchByCompanyId(companyId, updatedBy);
        return this.inspectDefMapper.deleteBatchByCompanyId(companyId, updatedBy);
    }
}
