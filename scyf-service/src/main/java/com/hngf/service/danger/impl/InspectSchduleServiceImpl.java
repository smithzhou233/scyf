package com.hngf.service.danger.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hngf.common.exception.ScyfException;
import com.hngf.common.utils.Constant;
import com.hngf.common.utils.PageUtils;
import com.hngf.entity.danger.InspectItemDef;
import com.hngf.entity.danger.InspectSchdule;
import com.hngf.entity.sys.Info;
import com.hngf.mapper.danger.InspectItemDefMapper;
import com.hngf.mapper.danger.InspectSchduleMapper;
import com.hngf.service.danger.InspectSchduleService;
import com.hngf.service.sys.InfoService;
import com.hngf.service.utils.IdKit;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service("InspectSchduleService")
public class InspectSchduleServiceImpl implements InspectSchduleService {

    @Autowired
    private InspectSchduleMapper inspectSchduleMapper;
    @Autowired
    private InspectItemDefMapper inspectItemDefMapper;
    @Autowired
    private InfoService infoService;
    @Override
    public PageUtils queryPage(Map<String, Object> params, int pageNum, int pageSize, String order) {
        if(StringUtils.isNotBlank(order)){
            PageHelper.startPage(pageNum, pageSize,order);
        }else{
            PageHelper.startPage(pageNum, pageSize);
        }
        List<InspectSchdule> list = inspectSchduleMapper.findListByMap(params);
        PageInfo<InspectSchdule> pageInfo = new PageInfo<InspectSchdule>(list);
        return new PageUtils(list,(int)pageInfo.getTotal(),pageInfo.getPages(),pageInfo.getPageSize(),pageInfo.getPrePage() + 1);
    }

    @Override
    public PageUtils querySonList(Map<String, Object> params, int pageNum, int pageSize, String order) {
        if(StringUtils.isNotBlank(order)){
            PageHelper.startPage(pageNum, pageSize,order);
        }else{
            PageHelper.startPage(pageNum, pageSize);
        }
        List<InspectSchdule> list = inspectSchduleMapper.findSonList(params);
        PageInfo<InspectSchdule> pageInfo = new PageInfo<InspectSchdule>(list);
        return new PageUtils(list,(int)pageInfo.getTotal(),pageInfo.getPages(),pageInfo.getPageSize(),pageInfo.getPrePage() + 1);
    }

    @Override
    public InspectSchdule getDetailById(Long scheduleId) {
        List<InspectSchdule> list = inspectSchduleMapper.findDetailByIdAndType(scheduleId,"0");
        if (!list.isEmpty()) {
            return list.get(0);
        }
        throw new ScyfException("任务不存在");
    }

    @Override
    public PageUtils getMyTask(Map<String, Object> params,int pageNum,int pageSize,String order) {
        if(StringUtils.isNotBlank(order)){
            PageHelper.startPage(pageNum, pageSize,order);
        }else{
            PageHelper.startPage(pageNum, pageSize);
        }
        List<InspectSchdule> list = inspectSchduleMapper.findMyTask(params);
        if (!list.isEmpty()) {
            list.forEach(inspectSchdule -> {
                if (null != inspectSchdule) {
                    inspectSchdule.setCheckRecordNo(IdKit.getCheckRecordNo());
                }
            });
        }
        PageInfo<InspectSchdule> pageInfo = new PageInfo<>(list);
        return new PageUtils(list,(int)pageInfo.getTotal(),pageInfo.getPages(),pageInfo.getPageSize(),pageInfo.getPrePage() + 1);
    }

    @Override
    public Integer getMyTaskCoutForAPI(Map<String, Object> params) {
        Integer count  = inspectSchduleMapper.findMyTaskCountForAPI(params);
        return count;
    }


    @Override
    public List<InspectSchdule> queryInspectSchduleList() {
        Map<String,Object> paramMap= new HashMap<>();
        return inspectSchduleMapper.getTodayOverdueTask(paramMap);
    }

    @Override
    public List<InspectSchdule> queryAllOverdueMainTask() {
        Map<String,Object> paramMap= new HashMap<>();
        return inspectSchduleMapper.getAllOverdueMainTask(paramMap);
    }

    @Override
    public List<InspectItemDef> getSchduleInspectItems(Map<String, Object> params) {
        return inspectItemDefMapper.findSchduleInspectItems(params);
    }

    @Override
    public InspectSchdule getMyDetailById(Long scheduleId) {
        List<InspectSchdule> list = inspectSchduleMapper.findDetailByIdAndType(scheduleId,"2");
        if (!list.isEmpty()) {
            return list.get(0);
        }
        throw new ScyfException("任务不存在");
    }

    @Override
    public InspectSchdule getById(Long id){
        return inspectSchduleMapper.findById(id);
    }

    @Override
    public void saveList(List<InspectSchdule> inspectSchduleList) {
        inspectSchduleMapper.addList(inspectSchduleList);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(InspectSchdule InspectSchdule) {
        inspectSchduleMapper.update(InspectSchdule);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int removeById(Long id) {
        InspectSchdule inspectSchdule = this.getById(id);
        if (null == inspectSchdule)
            throw new ScyfException("任务不存在");
        if(inspectSchdule.getStatus() == 2)
            throw new ScyfException("已检查任务禁止删除");

        long endTime = inspectSchdule.getEndDate().getTime();
        long nowTime = (new Date()).getTime();
        if (endTime <= nowTime)
            throw new ScyfException("任务已经到期，逾期后禁止删除");

        //当前任务父级ID如果为0，删除当前任务及子任务
        if (inspectSchdule.getParentScheduleId() == 0) {
            Map<String,Object> map = new HashMap();
            map.put("inspectScheduleId",inspectSchdule.getInspectScheduleId());
            map.put("scheduleDefId",inspectSchdule.getSchduleDefId());
            return inspectSchduleMapper.deleteAllTaskById(map);
        }else{//删除当前任务
            return inspectSchduleMapper.deleteById(id);
        }
    }

    public List<Map<String, Object>> todayCheck(Map<String, Object> params) {
        return this.inspectSchduleMapper.todayCheck(params);
    }

    @Override
    public Boolean checkedDeviceOn(Long companyId,Integer userType) {
        Info info = infoService.getByCId(companyId, userType );
        if (null != info && null != info.getHdangerreviewOn()) {
            return info.getCheckDeviceOn().intValue() == Constant.IS_CHECK_DEVICE_ON_YES;
        }
        return false;
    }

    /**
     * 根据任务ID查询任务执行人列表
     * @param params
     * @return
     */
    public List<InspectSchdule> executorScheduleList(Map<String, Object> params){
        return inspectSchduleMapper.executorScheduleList(params );
    }
}
