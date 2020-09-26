package com.hngf.service.danger.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hngf.common.utils.Constant;
import com.hngf.common.utils.PageUtils;
import com.hngf.entity.danger.InspectType;
import com.hngf.mapper.danger.InspectTypeMapper;
import com.hngf.service.danger.InspectTypeService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;


@Service("InspectTypeService")
public class InspectTypeServiceImpl implements InspectTypeService {

    @Autowired
    private InspectTypeMapper inspectTypeMapper;

    @Override
    public PageUtils queryPage(Map<String, Object> params,Integer pageNum,Integer pageSize,String order) {
        //利用PageHelper分页查询 注意：这个一定要放查询语句的前一行,否则无法进行分页,因为它对紧随其后第一个sql语句有效
        if(StringUtils.isNotBlank(order)){
            PageHelper.startPage(pageNum, pageSize,order);
        }else{
            PageHelper.startPage(pageNum, pageSize);
        }
        List<InspectType> list = inspectTypeMapper.findList(params);
        PageInfo<InspectType> pageInfo = new PageInfo<InspectType>(list);
        return new PageUtils(list,(int)pageInfo.getTotal(),pageInfo.getPages(),pageInfo.getPageSize(),pageInfo.getPrePage() + 1);
    }

    @Override
    public InspectType getById(Long id){
        return inspectTypeMapper.findById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(InspectType InspectType) {
        inspectTypeMapper.add(InspectType);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(InspectType InspectType) {
        inspectTypeMapper.update(InspectType);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeByIds(List ids) {
        inspectTypeMapper.deleteByIds(ids);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeById(Long id) {
        inspectTypeMapper.deleteById(id);
    }

    /**
     * 默认增加隐患巡查5大类
     * @param companyId
     * @return
     */
    @Override
    public int initScheduleCheckTypeSetting(Long companyId) {
        InspectType schedule1 = new InspectType();
        schedule1.setCompanyId(companyId);
        schedule1.setInspectTypeValue("1");
        schedule1.setInspectTypeName("专业性检查");
        schedule1.setInspectTypeDesc("专业性检查");
        schedule1.setInspectType(Constant.CLASSIFY_TYPE_SCHDULE_4);
        schedule1.setCreatedBy(0L);
        schedule1.setSortNo(1);
        schedule1.setCreatedTime(new Date());
        schedule1.setDelFlag(0);
        this.inspectTypeMapper.add(schedule1);
        InspectType schedule2 = new InspectType();
        schedule2.setCompanyId(companyId);
        schedule2.setInspectTypeValue("2");
        schedule2.setInspectTypeName("日常检查");
        schedule2.setInspectTypeDesc("日常检查");
        schedule2.setInspectType(Constant.CLASSIFY_TYPE_SCHDULE_4);
        schedule2.setCreatedBy(0L);
        schedule2.setSortNo(2);
        schedule2.setCreatedTime(new Date());
        this.inspectTypeMapper.add(schedule2);
        InspectType schedule3 = new InspectType();
        schedule3.setCompanyId(companyId);
        schedule3.setInspectTypeValue("3");
        schedule3.setInspectTypeName("节假日前后");
        schedule3.setInspectTypeDesc("节假日前后");
        schedule3.setInspectType(Constant.CLASSIFY_TYPE_SCHDULE_4);
        schedule3.setCreatedBy(0L);
        schedule3.setSortNo(3);
        schedule3.setCreatedTime(new Date());
        this.inspectTypeMapper.add(schedule3);
        InspectType schedule4 = new InspectType();
        schedule4.setCompanyId(companyId);
        schedule4.setInspectTypeValue("4");
        schedule4.setInspectTypeName("事故类比检查");
        schedule4.setInspectTypeDesc("事故类比检查");
        schedule4.setInspectType(Constant.CLASSIFY_TYPE_SCHDULE_4);
        schedule4.setCreatedBy(0L);
        schedule4.setSortNo(4);
        schedule4.setCreatedTime(new Date());
        this.inspectTypeMapper.add(schedule4);
        InspectType schedule5 = new InspectType();
        schedule5.setCompanyId(companyId);
        schedule5.setInspectTypeValue("5");
        schedule5.setInspectTypeName("季节性检查");
        schedule5.setInspectTypeDesc("季节性检查");
        schedule5.setInspectType(Constant.CLASSIFY_TYPE_SCHDULE_4);
        schedule5.setCreatedBy(0L);
        schedule5.setSortNo(5);
        schedule5.setCreatedTime(new Date());
        this.inspectTypeMapper.add(schedule5);
        InspectType schedule6 = new InspectType();
        schedule6.setCompanyId(companyId);
        schedule6.setInspectTypeValue("6");
        schedule6.setInspectTypeName("综合性检查");
        schedule6.setInspectTypeDesc("综合性检查");
        schedule6.setInspectType(Constant.CLASSIFY_TYPE_SCHDULE_4);
        schedule6.setCreatedBy(0L);
        schedule6.setSortNo(6);
        schedule6.setCreatedTime(new Date());
        this.inspectTypeMapper.add(schedule6);
        InspectType hdanger1 = new InspectType();
        hdanger1.setCompanyId(companyId);
        hdanger1.setInspectTypeValue("hdanger");
        hdanger1.setInspectTypeName("其他");
        hdanger1.setInspectTypeDesc("其他隐患类型");
        hdanger1.setInspectType(Constant.CLASSIFY_TYPE_HDANGER_1);
        hdanger1.setCreatedBy(0L);
        hdanger1.setSortNo(1);
        hdanger1.setCreatedTime(new Date());
        this.inspectTypeMapper.add(hdanger1);
        InspectType bizCheckDef1 = new InspectType();
        bizCheckDef1.setCompanyId(companyId);
        bizCheckDef1.setInspectTypeValue("1");
        bizCheckDef1.setInspectTypeName("其他");
        bizCheckDef1.setInspectTypeDesc("其他");
        bizCheckDef1.setInspectType(Constant.CLASSIFY_TYPE_CHECKEDDEF_2);
        bizCheckDef1.setCreatedBy(0L);
        bizCheckDef1.setSortNo(1);
        bizCheckDef1.setCreatedTime(new Date());
        this.inspectTypeMapper.add(bizCheckDef1);
        return 1;
    }

    @Override
    public int deleteBatchByCompanyId(Long companyId, Long updatedBy) {
        return this.inspectTypeMapper.deleteBatchByCompanyId(companyId, updatedBy);
    }
}
