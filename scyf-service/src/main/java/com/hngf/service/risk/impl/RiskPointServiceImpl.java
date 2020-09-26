package com.hngf.service.risk.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hngf.common.exception.ScyfException;
import com.hngf.common.utils.PageUtils;
import com.hngf.common.utils.ParamUtils;
import com.hngf.common.utils.RedisKeys;
import com.hngf.common.utils.RedisUtils;
import com.hngf.entity.risk.RiskPoint;
import com.hngf.entity.risk.RiskPointControlRecordLog;
import com.hngf.entity.risk.RiskSource;
import com.hngf.entity.sys.Company;
import com.hngf.entity.sys.User;
import com.hngf.mapper.risk.RiskPointMapper;
import com.hngf.mapper.sys.UserMapper;
import com.hngf.service.danger.HiddenService;
import com.hngf.service.danger.InspectSchduleService;
import com.hngf.service.risk.RiskCtrlService;
import com.hngf.service.risk.RiskInspectRecordService;
import com.hngf.service.risk.RiskPointControlRecordLogService;
import com.hngf.service.risk.RiskPointScenePersonService;
import com.hngf.service.risk.RiskPointService;
import com.hngf.service.risk.RiskRecordService;
import com.hngf.service.sys.CompanyService;
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


@Service("RiskPointService")
public class RiskPointServiceImpl implements RiskPointService {

    @Autowired
    private RiskPointMapper riskPointMapper;
    @Autowired
    private RiskRecordService riskRecordService;
    @Autowired
    private RiskInspectRecordService riskInspectRecordService;
    @Autowired
    private RiskPointScenePersonService riskPointScenePersonService;
    @Autowired
    private RiskPointControlRecordLogService riskPointControlRecordLogService;
    @Autowired
    private HiddenService hiddenService;
    @Autowired
    private  RiskCtrlService riskCtrlService;
    @Autowired
    private InspectSchduleService inspectSchduleService;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private CompanyService companyService;
    @Autowired
    private RedisUtils redisUtils;


    @Override
    public PageUtils queryPage(Map<String, Object> params,Integer pageNum,Integer pageSize,String order) {
        if(StringUtils.isNotBlank(order)){
            PageHelper.startPage(pageNum, pageSize,order);
        }else{
            PageHelper.startPage(pageNum, pageSize);
        }
        List<Map<String,Object>> list = riskPointMapper.findList(params);
        PageInfo pageInfo = new PageInfo(list);
        return new PageUtils(list,(int)pageInfo.getTotal(),pageInfo.getPages(),pageInfo.getPageSize(),pageInfo.getPrePage() + 1);
    }

    /**
     * App查询风险点列表
     * yfh
     * 2020/06/11
     * @param params
     * @param pageNum
     * @param pageSize
     * @param order
     * @return
     */
    @Override
    public PageUtils queryApiPage(Map<String, Object> params, int pageNum, int pageSize, String order) {
        if(StringUtils.isNotBlank(order)){
            PageHelper.startPage(pageNum, pageSize,order);
        }else{
            PageHelper.startPage(pageNum, pageSize);
        }
        //根据岗位ID 企业ID查看管控层级
        Integer ctlLevel = this.riskCtrlService.getCurrentUserCtlLevel(params);
        if (params.get("module").toString().equals("1")) {
            params.put("ctlLevelOff", "no");
        } else if (params.get("module").toString().equals("2")) {
            params.put("ctlLevelOff", "off");
        }
        List<Map<String, Object>> mapArrayList = new ArrayList();
        List<Map<String, Object>> mapList =getAppRiskPointList(params, ctlLevel);
        if (!mapList.isEmpty()) {
            Iterator var6 = mapList.iterator();

            while(var6.hasNext()) {
                Map<String, Object> list = (Map)var6.next();
                Map<String, Object> map1 = new HashMap();
                map1.put("nfcBind", list.get("nfcBind"));
                map1.put("qrcodeBind", list.get("qrcodeBind"));
                map1.put("riskPointId", list.get("risk_point_id"));
                map1.put("dutyPerson", list.get("dutyPerson"));
                map1.put("riskPointName", list.get("riskPointName"));
                map1.put("riskPointType", list.get("riskPointType"));
                map1.put("riskPointImg", list.get("riskPointImg"));
                map1.put("isOutOfControl", list.get("is_out_of_control"));
                map1.put("spotUserCount", list.get("spotUserCount"));
                map1.put("companyId", list.get("company_id"));
                map1.put("userName", list.get("userName"));
                map1.put("isActive", list.get("isActive"));
                map1.put("groupName", list.get("groupName"));
                if (list.get("riskPointLevel") == null) {
                    map1.put("riskPointLevel", 0);
                } else {
                    map1.put("riskPointLevel", list.get("riskPointLevel"));
                }

                map1.put("dutGroupId", list.get("dutGroupId"));
                map1.put("checkNumberToday", list.get("checkNumberToday"));
                map1.put("createdBy", list.get("createdBy"));
                mapArrayList.add(map1);
            }
        }

        PageInfo pageInfo = new PageInfo(mapArrayList);
        return new PageUtils(mapArrayList,(int)pageInfo.getTotal(),pageInfo.getPages(),pageInfo.getPageSize(),pageInfo.getPrePage() + 1);
    }

    /**
     * 风险点 查询数量
     * yfh
     * @param params
     * @return
     */
    @Override
    public Map<String, Object> queryRiskPointNum(Map<String, Object> params) {
        Map map=new HashMap();
        //根据岗位ID 企业ID查看管控层级
        Integer ctlLevel = this.riskCtrlService.getCurrentUserCtlLevel(params);
        if (params.get("module").toString().equals("1")) {
            params.put("ctlLevelOff", "no");
        } else if (params.get("module").toString().equals("2")) {
            params.put("ctlLevelOff", "off");
        }
        List<Map<String, Object>> mapList =getAppRiskPointList(params, ctlLevel);
        Integer levOne=0;
        Integer levTwo=0;
        Integer levThree=0;
        Integer levFour=0;
        if(mapList.size()>0){
            for(int i=0;i<mapList.size();i++){
                Integer riskPointLevel= Integer.valueOf(mapList.get(i).get("riskPointLevel").toString());
                if(riskPointLevel==1){
                    levOne=levOne+1;
                }
                if(riskPointLevel==2){
                    levTwo=levTwo+1;
                }
                if(riskPointLevel==3){
                    levThree=levThree+1;
                }
                if(riskPointLevel==4){
                    levFour=levFour+1;
                }
            }
        }
        map.put("levOne",levOne);
        map.put("levTwo",levTwo);
        map.put("levThree",levThree);
        map.put("levFour",levFour);
        return map;
    }

    /**
     *  风险分布图 风险点数量总计
     *  yfh
     *  2020/06/30
     * @param groupId
     * @param companyId
     * @return
     */
    @Override
    public String riskPointCount(Integer groupId, Long companyId) {
        return riskPointMapper.riskPointCount(groupId,companyId);
    }

    public List<Map<String, Object>> getAppRiskPointList(Map<String, Object> map, Integer ctlLevel) {
        if (map.get("ctlLevelOff").toString().equals("no")) {
            if (ctlLevel == null) {
                List<Map<String, Object>> mapList = new ArrayList();
                return mapList;
            }

            if (!ctlLevel.equals(4)) {
                map.remove("gId");
                map.remove("aId");
                map.put("ctlLevel", ctlLevel);
            }
        } else if (map.get("ctlLevelOff").toString().equals("off")) {
            map.remove("aId");
            map.remove("ctlLevel");
        }

        List<Map<String, Object>> mapList = this.riskPointMapper.queryRiskPointPage(map);
        return mapList;
    }

    @Override
    public PageUtils getRiskPointList(Map<String, Object> params, Integer pageNum, Integer pageSize, String order) {
        if(StringUtils.isNotBlank(order)){
            PageHelper.startPage(pageNum, pageSize,order);
        }else{
            PageHelper.startPage(pageNum, pageSize);
        }
        List<RiskPoint> list = riskPointMapper.findRiskPointList(params);
        PageInfo pageInfo = new PageInfo(list);
        return new PageUtils(list,(int)pageInfo.getTotal(),pageInfo.getPages(),pageInfo.getPageSize(),pageInfo.getPrePage() + 1);
    }

    @Override
    public PageUtils getRiskPointListByCompanyId(Map<String, Object> params, Integer pageNum, Integer pageSize, String order) {
        //利用PageHelper分页查询 注意：这个一定要放查询语句的前一行,否则无法进行分页,因为它对紧随其后第一个sql语句有效
        if(StringUtils.isNotBlank(order)){
            PageHelper.startPage(pageNum, pageSize,order);
        }else{
            PageHelper.startPage(pageNum, pageSize);
        }
        List<Map<String ,Object>> list =  riskPointMapper.getRiskPointListByCompanyId(params);
        PageInfo pageInfo = new PageInfo(list);
        return  new PageUtils(list,(int)pageInfo.getTotal(),pageInfo.getPages(),pageInfo.getPageSize(),pageInfo.getPrePage() + 1);
    }


    @Override
    public List<RiskPoint> getRiskPointList(Map<String, Object> params) {
        return riskPointMapper.findRiskPointList(params);
    }
    /**
     * @Author: zyj
     * @Description:风险，风险点统计柱形图
     * @Param companyId 企业id
     * @Date 15:16 2020/6/19
     */
    @Override
    public String riskPointLvCount(Long companyId,Integer riskPointLevel) {
        return riskPointMapper.riskPointLvCount(companyId,riskPointLevel);
    }

    @Override
    public List<Map<String, Object>> riskPointLvCountBigScreen(Map<String, Object> params) {
        return riskPointMapper.riskPointLvCountBigScreen(params);
    }

    @Override
    public PageUtils getScheduleRiskPointList(Map<String, Object> params, int pageNum, int pageSize, String order) {
        params.put("ctlLevelOff", "no");
        //检查次数条件去掉
        /*InspectSchdule schedule = inspectSchduleService.getById(Long.parseLong(params.get("scheduleId") + ""));
        if (null != schedule) {
            params.put("checkNumber", schedule.getInspectScheduleCount());
        }else {
            params.put("checkNumber", 1);
        }*/
        Integer ctlLevel = this.riskCtrlService.getCurrentUserCtlLevel(params);
        if (null != ctlLevel && ctlLevel.intValue() != 4) {
            params.put("ctlLevel", ctlLevel);
        }

        if(StringUtils.isNotBlank(order)){
            PageHelper.startPage(pageNum, pageSize,order);
        }else{
            PageHelper.startPage(pageNum, pageSize);
        }
        List<Map<String, Object>> resultList = null;
        //resultList = riskPointMapper.findSchduleRiskPoint(params);
        //此处该用户下的责任风险点
        resultList =  redisUtils.get(RedisKeys.getRiskPointKey(params.get("userId").toString(),params.get("type").toString(),params.get("scheduleId").toString()),List.class);
       if (null==resultList ||resultList.size()==0 ) {
            resultList = riskPointMapper.findSchduleRiskPoint(params);
            redisUtils.set(RedisKeys.getRiskPointKey(params.get("userId").toString(),params.get("type").toString(),params.get("scheduleId").toString()),resultList);
        }
        PageInfo<Map<String, Object>> pageInfo = new PageInfo<>(resultList);
        return new PageUtils(resultList,(int)pageInfo.getTotal(),pageInfo.getPages(),pageInfo.getPageSize(),pageInfo.getPrePage() + 1);
    }

    /**
     * 根据部门ID查询责任人
     * @param params
     * @return
     */
    @Override
    public List<User> queryUser(Map<String, Object> params) {
        return userMapper.findList(params);
    }

    /**
     *风险分级责任台账
     * yfh
     * 2020/06/19
     * @param map
     * @return
     */
    @Override
    public List<Map<String, Object>> findAllListByType(Map<String, Object> map) {
        return riskPointMapper.findAllListByType(map);
    }

    /**
     * 预警产生原因
     * yfh
     * 2020/06/23
     * @param type
     * @return
     */
    @Override
    public List<Map<String, Object>> causeReasons(String type) {
        return riskPointMapper.causeReasons(type);
    }



    @Override
    public RiskPoint getById(Long id){
        RiskPoint riskPoint = riskPointMapper.findById(id);
        if (null == riskPoint || null == riskPoint.getRiskPointId()) {
            return null;
        }
        //riskPoint有数据时，关联查询更多信息
        if (null != riskPoint) {
            Map<String, Object> map = new HashMap();
            map.put("riskPointId", id);
            map.put("companyId", riskPoint.getCompanyId());

            //风险点警告信息
            riskPoint.setRiskPointWarn(this.riskPointControlRecordLogService.riskPointWarn(map));
            map.put("riskPoint", 2);
            //未处理隐患
            riskPoint.setHiddenDangerList(this.hiddenService.getGroupHidden(map));
            //风险点检查记录
            riskPoint.setRiskCheckedRecord(this.riskInspectRecordService.getRiskInspectRecordList(map));
            //风险点现场人员
            riskPoint.setSitePersonnel(this.riskPointScenePersonService.getByRiskPointId(map));
        }
        return riskPoint;
    }

    @Override
    public RiskPoint selectByQrCode(String id) {
        return riskPointMapper.findByQrCode(id);
    }

    @Override
    public RiskPoint getRiskPointById(Long id){
        return riskPointMapper.findRiskPointById(id);
    }

    @Override
    public int count(Map<String, Object> params) {
        return riskPointMapper.countByMap(params);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(RiskPoint RiskPoint) {
        riskPointMapper.add(RiskPoint);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(RiskPoint RiskPoint) {
        riskPointMapper.update(RiskPoint);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateIsActive(Integer isActive, Long[] riskPointIds) {
        List<Long> riskPointIdList = new ArrayList<>();
        if (null != riskPointIds && riskPointIds.length > 0) {
            for (int i = 0; i < riskPointIds.length; i++) {
                if(null != riskPointIds[i])
                    riskPointIdList.add(riskPointIds[i]);
            }
        }
        return riskPointMapper.updateIsActive(isActive,riskPointIdList);
    }

    @Override
    public List<RiskSource> getRiskPointDangerSrc(Map<String, Object> params) {
        List<RiskSource> riskSources = riskPointMapper.findRiskPointDangerSrc(params);
        if (!riskSources.isEmpty()) {
            //危险名称设置为： 风险等级名称 + 危险源名称
            riskSources.forEach(rs ->{
                rs.setRiskDangerName(rs.getRiskDangerLevelName() + rs.getRiskDangerName());
            });
        }
        return riskSources;
    }

    @Override
    public List<Map<String, Object>> warningRiskPoint(Map<String, Object> params) {
        return riskPointMapper.warningRiskPoint(params);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeByIds(List ids) {
        riskPointMapper.deleteByIds(ids);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeById(Long id,Long companyId) {

        riskPointMapper.deleteById(id);

        Map<String, Object> columnMap = new HashMap();
        columnMap.put("riskPointId", id);
        columnMap.put("companyId", companyId);

        //关联删除风险点关联风险表
        this.riskRecordService.removeByMap(columnMap);

        //关联删除风险点关联人员
        this.riskPointScenePersonService.removeByMap(columnMap);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void checkedRiskPointIsControl(Long companyId, Long riskPointId) {
        Map<String, Object> map = new HashMap();
        map.put("companyId", companyId);
        map.put("riskPointId", riskPointId);
        List<RiskPointControlRecordLog> actualControlRecordes = this.riskPointControlRecordLogService.getIsControlRecordList(map);
        RiskPoint riskPoint;
        if (actualControlRecordes.isEmpty()) {
            riskPoint = this.riskPointMapper.findById(riskPointId);
            if (riskPoint != null) {
                riskPoint.setIsOutOfControl(0);
                this.riskPointMapper.update(riskPoint);
            }
        } else {
            riskPoint = this.riskPointMapper.findById(riskPointId);
            if (riskPoint != null) {
                riskPoint.setIsOutOfControl(1);
                this.riskPointMapper.update(riskPoint);
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateRiskPointHiddenCount(Long riskPointId, Integer type) throws Exception {
        if (null != riskPointId) {
            RiskPoint riskPoint = this.getById(riskPointId);
            if (riskPoint != null) {
                if (type == 1) {
                    riskPoint.setHdangerNumber(riskPoint.getHdangerNumber() + 1);
                } else if (type == 0 && riskPoint.getHdangerNumber() > 0) {
                    riskPoint.setHdangerNumber(riskPoint.getHdangerNumber() - 1);
                }
                riskPoint.setUpdatedTime(new Date());
                this.update(riskPoint);
            }
        }
    }

    @Override
    public List<Map<String, Object>> getAllRiskPoint(Long companyId, Long groupId) {
        return riskPointMapper.findAllRiskPoint(companyId,groupId);
    }

    @Override
    public RiskPoint riskPointDetailsAll(Long riskPointId) {
        RiskPoint riskPoint = riskPointMapper.findById(riskPointId);
        if (null == riskPoint || null == riskPoint.getRiskPointId()) {
            return null;
        }

        //riskPoint有数据时，关联查询更多信息
        Map<String, Object> map = new HashMap();
        map.put("riskPointId", riskPointId);
        map.put("companyId", riskPoint.getCompanyId());

        //风险点警告信息
        riskPoint.setRiskPointWarn(this.riskPointControlRecordLogService.riskPointWarn(map));
        map.put("riskPoint", 2);
        //未处理隐患
        riskPoint.setHiddenDangerList(this.hiddenService.getGroupHidden(map));
        //风险点检查记录
        riskPoint.setRiskCheckedRecord(this.riskInspectRecordService.getRiskInspectRecordList(map));
        //风险点现场人员
        riskPoint.setSitePersonnel(this.riskPointScenePersonService.getByRiskPointId(map));

        return riskPoint;
    }

    @Override
    public PageUtils getRiskPointPageForGent(Map<String, Object> params, int pageNum, int pageSize, String order) {
        Long companyId = ParamUtils.paramsToLong(params, "companyId");
        Company company = companyService.getCompanyId(companyId);
        if (null == company || null == company.getCompanyId()) {
            throw new ScyfException("选择的集团、企业不存在：companyId=" + companyId);
        }

        params.put("companyRootId", company.getCompanyRootId());
        if(StringUtils.isNotBlank(order)){
            PageHelper.startPage(pageNum, pageSize,order);
        }else{
            PageHelper.startPage(pageNum, pageSize);
        }
        List<Map<String, Object>> list = riskPointMapper.findRiskPointForGent(params);
        PageInfo<Map<String, Object>> pageInfo = new PageInfo<>(list);
        return new PageUtils(list,(int)pageInfo.getTotal(),pageInfo.getPages(),pageInfo.getPageSize(),pageInfo.getPrePage() + 1);
    }

    @Override
    public List<Map<String, Object>> getRiskPointChuldrenCount(Map<String, Object> params) {
        return riskPointMapper.findRiskPointChuldrenCount(params);
    }


}
