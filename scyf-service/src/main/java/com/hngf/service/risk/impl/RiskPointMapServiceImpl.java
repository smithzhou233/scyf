package com.hngf.service.risk.impl;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.hngf.common.utils.DateUtils;
import com.hngf.entity.danger.InspectPointRel;
import com.hngf.entity.risk.*;
import com.hngf.mapper.risk.*;
import com.hngf.service.danger.InspectPointRelService;
import com.hngf.service.risk.*;
import com.hngf.service.sys.DictService;
import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hngf.common.utils.PageUtils;
import com.hngf.common.utils.Constant;


@Service("RiskPointMapService")
public class RiskPointMapServiceImpl implements RiskPointMapService {

    @Autowired
    private RiskPointMapMapper RiskPointMapMapper;
    @Autowired
    private RiskPointPersonLiableService riskPointPersonLiableService;
    @Autowired
    private RiskPointDangerSourceService riskPointDangerSourceService;
    @Autowired
    private RiskRecordService riskRecordService;
    @Autowired
    private RiskSourceService riskSourceService;
    @Autowired
    private DictService dictService;
    @Autowired
    private RiskPointService riskPointService;
    @Autowired
    private InspectPointRelService inspectPointRelService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        int pageNum = null!= params.get("pageNum") ? Integer.parseInt(params.get("pageNum").toString()) : 1;
        int pageSize = null != params.get("pageSize") ? Integer.parseInt(params.get("pageSize").toString()) : Constant.PAGE_SIZE;
        //利用PageHelper分页查询 注意：这个一定要放查询语句的前一行,否则无法进行分页,因为它对紧随其后第一个sql语句有效
        PageHelper.startPage(pageNum, pageSize);
        List<RiskPointMap> list = RiskPointMapMapper.findList(params);
        PageInfo<RiskPointMap> pageInfo = new PageInfo<RiskPointMap>(list);
        return new PageUtils(list,(int)pageInfo.getTotal(),pageInfo.getPages(),pageInfo.getPageSize(),pageInfo.getPrePage() + 1);
    }

    @Override
    public RiskPointMap getById(Long id){
        return RiskPointMapMapper.findById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void insertOrUpdate(RiskPoint riskPoint) throws ParseException {

        //责任人ID，多个之间逗号分隔
        String dutyPersons = riskPoint.getDutyPersons();

        Long mapId = riskPoint.getMapId();

        Long companyId = riskPoint.getCompanyId();

        if (null == riskPoint.getRiskPointMinLevel()) {
            riskPoint.setRiskPointMinLevel(4);
        }
        if (null == riskPoint.getRiskPointMinLevelSon()) {
            riskPoint.setRiskPointMinLevelSon(4);
        }

        Integer riskPointMinLevel = riskPoint.getRiskPointMinLevel();
        Integer riskPointMinLevelSon = riskPoint.getRiskPointMinLevelSon();


        if(riskPoint.getRiskPointId()!=null){
            //设置开始日期
            if (StringUtils.isNotBlank(riskPoint.getActiveStartTime())) {
                String date= riskPoint.getActiveStartTime();
                riskPoint.setStartTime(DateUtils.stringToDate(date,DateUtils.DATE_TIME_PATTERN));
            }
            //设置结束日期
            if (StringUtils.isNotBlank(riskPoint.getActiveEndTime())) {
                String date= riskPoint.getActiveEndTime();
                riskPoint.setEndTime(DateUtils.stringToDate(date,DateUtils.DATE_TIME_PATTERN));
            }
        }else{
            //设置开始日期
            if (StringUtils.isNotBlank(riskPoint.getActiveStartTime())) {
                String date= riskPoint.getActiveStartTime();
                date = date.replace("Z", "").replace("T"," ").substring(0,19);//
                riskPoint.setStartTime(DateUtils.stringToDate(date,DateUtils.DATE_TIME_PATTERN));
            }
            //设置结束日期
            if (StringUtils.isNotBlank(riskPoint.getActiveEndTime())) {
                String date= riskPoint.getActiveEndTime();
                date = date.replace("Z", "").replace("T"," ").substring(0,19);//
                riskPoint.setEndTime(DateUtils.stringToDate(date,DateUtils.DATE_TIME_PATTERN));
            }
        }

        //风险点等级默认 4
        Integer riskPointLv = 4;
        //
        if (riskPointMinLevelSon < riskPointMinLevel) {
            riskPointLv = riskPointMinLevelSon;
        } else {
            riskPointLv = riskPointMinLevel;
        }
        if(riskPoint.getRiskPointLevel()==null){
            riskPoint.setRiskPointLevel(riskPointLv);
        }

        Long riskPointId;

        List<RiskPointMap> riskPointMapList;

        String drawIds = null;

        //RiskPointId == null ：新增
        if (riskPoint.getRiskPointId() == null) {
            //获取riskPointId
            //riskPointId = dictService.getTabId("scyf_risk_point");
            //riskPoint.setRiskPointId(riskPointId);
            //开始保存 riskpoint
            this.riskPointService.save(riskPoint);
            riskPointId=riskPoint.getRiskPointId();
            riskPointMapList = Lists.newArrayList();
            //如果坐标不为空，保存地图
            if (StringUtils.isNotBlank(riskPoint.getLatlng())) {
                RiskPointMap riskPointMap = new RiskPointMap();
                riskPointMap.setMapId(mapId);
                riskPointMap.setRiskPointId(riskPointId);
                riskPointMap.setMapData(riskPoint.getLatlng());
                riskPointMap.setRiskPointType(riskPoint.getRiskPointType());
                riskPointMap.setMapType(Constant.MAP_DOT);
                riskPointMap.setMapName(riskPoint.getRiskPointName());
                riskPointMap.setCompanyId(companyId);
                riskPointMap.setCreateBy(riskPoint.getCreatedBy());
                riskPointMap.setCreateTime(new Date());
                riskPointMap.setDelFlag(0);
                riskPointMapList.add(riskPointMap);
            }

            if (StringUtils.isNotBlank(riskPoint.getDrawIds())) {
                drawIds = riskPoint.getDrawIds();
                String drawType = "";

                String[] drawIdAll = drawIds.split("#");
                if (drawIdAll.length > 0) {
                    for(int i = 0; i < drawIdAll.length; ++i) {
                        drawType = drawIdAll[i].split(";")[1];
                        drawIds = drawIdAll[i].split(";")[0];
                        RiskPointMap riskPointMap = new RiskPointMap();
                        riskPointMap.setMapId(mapId);
                        riskPointMap.setRiskPointId(riskPointId);
                        if (drawType.equals("Polygon")) {
                            drawIds = drawIds.substring(1, drawIds.length() - 1);
                            riskPointMap.setMapData(drawIds);
                            riskPointMap.setMapType(Constant.MAP_REGION);
                        } else if (drawType.equals("LineString")) {
                            riskPointMap.setMapData(drawIds);
                            riskPointMap.setMapType(Constant.MAP_THREAD);
                        }else {
                            riskPointMap.setMapData(drawIds);
                            riskPointMap.setMapType(Constant.MAP_DOT);
                        }

                        riskPointMap.setRiskPointType(riskPoint.getRiskPointType());
                        riskPointMap.setMapName(riskPoint.getRiskPointName());
                        riskPointMap.setCompanyId(companyId);
                        riskPointMap.setCreateBy(riskPoint.getCreatedBy());
                        riskPointMap.setCreateTime(new Date());
                        riskPointMap.setDelFlag(0);
                        riskPointMapList.add(riskPointMap);
                    }
                }
            }
            //批量保存地图数据
            if(!riskPointMapList.isEmpty()){
                RiskPointMapMapper.addForeach(riskPointMapList);
            }

        }else{//更新
            riskPointId = riskPoint.getRiskPointId();
            this.riskPointService.update(riskPoint);
        }

        //批量新增风险点责任人
        if (StringUtils.isNotBlank(dutyPersons)) {
            String[] userIds = dutyPersons.split(",");
            if (userIds.length > 0) {
                //1.新增之前先删除
                this.riskPointPersonLiableService.removeByRiskPoint(riskPointId,companyId);

                //2.封装需要新增的数据
                List<RiskPointPersonLiable> riskPointPersonLiableList = new ArrayList<>();
                for (int i = 0; i < userIds.length ; i++) {
                    if (null == userIds[i]) {
                        continue;
                    }
                    RiskPointPersonLiable rppl = new RiskPointPersonLiable();
                    rppl.setRiskPointId(riskPointId);
                    rppl.setCompanyId(companyId);
                    rppl.setUserId(Long.valueOf(userIds[i]));
                    riskPointPersonLiableList.add(rppl);
                }

                //3.开始新增
                if (!riskPointPersonLiableList.isEmpty()) {
                    this.riskPointPersonLiableService.saveBatch(riskPointPersonLiableList);
                }
            }

        }
        List<String> ds = Lists.newArrayList();
        if(null != riskPoint.getDangerSrcId()){
            ds.add(riskPoint.getDangerSrcId().toString());
        }
        drawIds =  riskPoint.getDangerSourceIds();
        if (StringUtils.isNotEmpty(drawIds)) {
            List<String> result = Splitter.on(",").trimResults().omitEmptyStrings().splitToList(drawIds);
            result.forEach((s) -> {
                if (!s.equals("undefined") && !ds.contains(s)) {
                    ds.add(s);
                }

            });
        }

        if (!ds.isEmpty()) {

            List<RiskPointDangerSource> riskPointDangerSourceList = Lists.newArrayList();
            //1.新增之前先删除
            Map<String, Object> params = new HashMap<>();
            params.put("riskPointId", riskPointId);
            params.put("companyId", companyId);
            this.riskPointDangerSourceService.removeByMap(params);
            params.clear();


            //2.封装需要新增的数据
            ds.forEach((s) -> {
                RiskPointDangerSource riskPointDangerSource = new RiskPointDangerSource();
                riskPointDangerSource.setCompanyId(companyId);
                riskPointDangerSource.setRiskPointId(riskPointId);
                riskPointDangerSource.setRiskDangerId(Long.valueOf(s));
                riskPointDangerSource.setDelFlag(0);
                riskPointDangerSourceList.add(riskPointDangerSource);
            });
            //3.开始新增
            if (!riskPointDangerSourceList.isEmpty()) {
                this.riskPointDangerSourceService.saveBatch(riskPointDangerSourceList);
            }

            //设置riskPoint 危险源数量
            riskPoint.setDangerSourceNumber(riskPointDangerSourceList.size());


            /**
             * 设置风险点关联风险
             */

            //1.新增之前先删除
            params.put("riskPointId", riskPointId);
            params.put("companyId", companyId);
            this.riskRecordService.removeByMap(params);
            params.clear();

            //2.查询风险数据，封装需要新增的数据
            params.put("companyId", companyId);
            params.put("parentRiskDangerIdList", ds);
            List<RiskSource> riskList = riskSourceService.queryByMap(params);
            if (!riskList.isEmpty()) {
                List<RiskRecord> list = Lists.newArrayList();
                riskList.forEach((ix) -> {
                    RiskRecord riskRecord = new RiskRecord();
                    riskRecord.setRiskId(ix.getRiskDangerId());
                    riskRecord.setCompanyId(companyId);
                    riskRecord.setRiskPointId(riskPointId);
                    riskRecord.setRiskName(ix.getRiskDangerName());
                    riskRecord.setRiskDangerId(ix.getParentRiskDangerId());
                    riskRecord.setDelFlag(0);
                    list.add(riskRecord);
                });
                //3.开始新增
                if (!list.isEmpty()) {
                    this.riskRecordService.saveBatch(list);
                }
                //设置风险数量
                riskPoint.setRiskNumber(list.size());
            }
        }else{
            riskPoint.setDangerSourceNumber(0);
            riskPoint.setRiskNumber(0);
        }

        //更新 riskpoint
        this.riskPointService.update(riskPoint);

        /**
         * 关联风险检查项
         */
        if (StringUtils.isNotBlank(riskPoint.getCheckedTable())) {
            //1.关联之前先删除
            Map<String, Object> columnMap = new HashMap();
            columnMap.put("riskPointId", riskPoint.getRiskPointId());
            columnMap.put("companyId", companyId);
            this.inspectPointRelService.removeByMap(columnMap);

            String[] checkedTableS = riskPoint.getCheckedTable().split(",");
            if (checkedTableS.length > 0) {
                List<InspectPointRel> list = new ArrayList();
                //2.封装需要新增的数据
                for(int i = 0; i < checkedTableS.length; ++i) {
                    InspectPointRel inspectPointRel = new InspectPointRel();
                    inspectPointRel.setRiskPointId(riskPoint.getRiskPointId());
                    inspectPointRel.setInspectTypeId(Long.valueOf(checkedTableS[i]));
                    inspectPointRel.setCompanyId(companyId);
                    inspectPointRel.setCreatedBy(riskPoint.getCreatedBy());
                    inspectPointRel.setCreatedTime(new Date());
                    inspectPointRel.setDelFlag(0);
                    list.add(inspectPointRel);
                }
                //3.开始新增
                if (!list.isEmpty()) {
                    this.inspectPointRelService.saveBatch(list);
                }
            }
        }
    }
    /**
     *新增风险点位置
     * yfh
     * 2020/07/1
     * @param riskPoint
     * @param mapId
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void insertMapData(RiskPoint riskPoint,Long mapId,Long companyId) {
            //删除之前保存的风险点位置
            Long  riskPointId = riskPoint.getRiskPointId();
            RiskPointMapMapper.del(riskPointId,companyId);
            List<RiskPointMap> riskPointMapList = Lists.newArrayList();
            //获取所选风险点所在位置坐标
            String drawIds = riskPoint.getDrawIds();
        if (StringUtils.isNotEmpty(drawIds)) {
            String[] drawIdsAll = drawIds.split("#");

            for(int i = 0; i < drawIdsAll.length; ++i) {
                String dataIds = drawIdsAll[i].split(";")[0];
                String drawType = drawIdsAll[i].split(";")[1];
                Integer typeId = 0;
                if (drawType.equals("Polygon")) {
                    typeId = Constant.MAP_REGION;
                } else if (drawType.equals("LineString")) {
                    typeId = Constant.MAP_THREAD;
                } else if (drawType.equals("Point")) {
                    typeId = Constant.MAP_DOT;
                }

                if (typeId != 0) {
                    RiskPointMap riskPointMap = new RiskPointMap();
                    riskPointMap.setMapId(mapId);
                    riskPointMap.setRiskPointId(riskPointId);
                    riskPointMap.setMapName(riskPoint.getRiskPointName());
                    if (drawType.equals("Polygon")) {
                        dataIds = dataIds.substring(1, dataIds.length() - 1);
                        riskPointMap.setMapData(dataIds);
                    } else if (drawType.equals("LineString")) {
                        riskPointMap.setMapData(dataIds);
                    } else {
                        riskPointMap.setMapData(dataIds);
                    }

                    riskPointMap.setMapType(typeId);
                    riskPointMap.setRiskPointType(riskPoint.getRiskPointType());
                    riskPointMap.setMapName(riskPoint.getRiskPointName());
                    riskPointMap.setCompanyId(companyId);
                    riskPointMap.setCreateBy(riskPoint.getCreatedBy());
                    riskPointMap.setCreateTime(riskPoint.getCreatedTime());
                    riskPointMapList.add(riskPointMap);
                }
            }
        }
            //批量保存地图数据
            if (!riskPointMapList.isEmpty()) {
                RiskPointMapMapper.addForeach(riskPointMapList);
            }
    }
    /**
     * 根据风险点Id 获取该风险点位置
     * yfh
     * 2020/07/01
     * @param riskPointId
     * @return
     */
    @Override
    public RiskPointMap getRiskPointId(Long riskPointId) {
        return RiskPointMapMapper.getRiskPointId(riskPointId);
    }

    /**
     * 可视化编辑 获取地图上风险点
     * yfh
     * 2020/07/01
     * @param map
     * @return
     */
    @Override
    public List<Map<String, Object>> getMapPoints(Map<String, Object> map) {
        return RiskPointMapMapper.getMapPoints(map);
    }

    @Override
    public List<Map<String, Object>> getRiskPointMap(Map<String, Object> map) {
        return RiskPointMapMapper.getRiskPointMap(map);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(RiskPointMap RiskPointMap) {
        RiskPointMapMapper.add(RiskPointMap);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(RiskPointMap RiskPointMap) {
        RiskPointMapMapper.update(RiskPointMap);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeByIds(List ids) {
        RiskPointMapMapper.deleteByIds(ids);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeById(Long id) {
        RiskPointMapMapper.deleteById(id);
    }
}
