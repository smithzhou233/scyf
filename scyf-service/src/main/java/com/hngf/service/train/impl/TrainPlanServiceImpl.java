package com.hngf.service.train.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hngf.common.utils.PageUtils;
import com.hngf.dto.sys.GroupDetailDto;
import com.hngf.entity.train.TrainPlan;
import com.hngf.mapper.sys.GroupMapper;
import com.hngf.mapper.train.TrainPlanMapper;
import com.hngf.service.train.TrainPlanService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;


@Service("TrainPlanService")
public class TrainPlanServiceImpl implements TrainPlanService {

    @Autowired
    private TrainPlanMapper trainPlanMapper;
    @Autowired
    private GroupMapper groupMapper;

    @Override
    public PageUtils queryPage(Map<String, Object> params,Integer pageNum,Integer pageSize,String order) {
        //利用PageHelper分页查询 注意：这个一定要放查询语句的前一行,否则无法进行分页,因为它对紧随其后第一个sql语句有效
        if(StringUtils.isNotBlank(order)){
            PageHelper.startPage(pageNum, pageSize,order);
        }else{
            PageHelper.startPage(pageNum, pageSize);
        }
        List<Map<String,Object>> list = trainPlanMapper.findList(params);
        PageInfo pageInfo = new PageInfo(list);
        return new PageUtils(list,(int)pageInfo.getTotal(),pageInfo.getPages(),pageInfo.getPageSize(),pageInfo.getPrePage() + 1);
    }
    @Override
    public PageUtils queryPagePc(Map<String, Object> params,Integer pageNum,Integer pageSize,String order) {
        //利用PageHelper分页查询 注意：这个一定要放查询语句的前一行,否则无法进行分页,因为它对紧随其后第一个sql语句有效
        if(StringUtils.isNotBlank(order)){
            PageHelper.startPage(pageNum, pageSize,order);
        }else{
            PageHelper.startPage(pageNum, pageSize);
        }
        List<Map<String,Object>> list = trainPlanMapper.findListPc(params);
        if(list.size()>0){
            for(int i=0;i<list.size();i++){
                Map map=list.get(i);
                String trainType =map.get("trainType").toString();
                String dictType="train_type";
                String dictName=trainPlanMapper.getType(trainType,dictType);
                list.get(i).put("dictName",dictName);
            }
        }
        PageInfo pageInfo = new PageInfo(list);
        return new PageUtils(list,(int)pageInfo.getTotal(),pageInfo.getPages(),pageInfo.getPageSize(),pageInfo.getPrePage() + 1);
    }

    @Override
    public List<Map<String,Object>> getById(Integer id){
        return trainPlanMapper.findById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(TrainPlan TrainPlan) {
        trainPlanMapper.add(TrainPlan);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(TrainPlan TrainPlan) {
        trainPlanMapper.update(TrainPlan);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeByIds(List ids) {
        trainPlanMapper.deleteByIds(ids);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeById(Long id, Long updatedBy) {
        trainPlanMapper.deleteById(id, updatedBy);
    }

    /**
     * 根据计划id查看 培训内容表 是否有该计划的内容
     * yfh
     * 2020/05/26
     * @param trainPlanId
     * @return
     */
    @Override
    public List isDel(Long trainPlanId) {
        return trainPlanMapper.isDel(trainPlanId);
    }

    /**
     * 查看培训计划类型
     * yfh
     * 2020/05/26
     * @param trainType
     * @return
     */
    @Override
    public List<Map<String, Object>> selectTrainType(String trainType) {
        return trainPlanMapper.selectTrainType(trainType);
    }
    /**
     * 修改培训计划 是否提前一天预警
     * yfh
     * 2020/07/09
     * @param trainPlanId
     * @param warnFlag
     * @return
     */
    @Override
    public void updateWarnFlag(Long trainPlanId, Integer warnFlag, Long updatedBy) {
        trainPlanMapper.updateWarnFlag(trainPlanId,warnFlag,updatedBy);
    }

    @Override
    public List<GroupDetailDto> getEleuiTreeList(Map<String, Object> params) {
        List<GroupDetailDto> list = this.groupMapper.getChildren(params);
        return list;
    }
}
