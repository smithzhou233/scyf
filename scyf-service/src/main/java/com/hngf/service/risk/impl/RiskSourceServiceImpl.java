package com.hngf.service.risk.impl;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.util.TypeUtils;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.hngf.common.utils.Constant;
import com.hngf.common.utils.R;
import com.hngf.dto.danger.RiskSourceDto;
import com.hngf.dto.risk.RiskInspectItemDto;
import com.hngf.entity.risk.Risk;
import com.hngf.entity.risk.RiskCtrl;
import com.hngf.entity.risk.RiskCtrlLevel;
import com.hngf.entity.risk.RiskMeasure;
import com.hngf.entity.risk.RiskPoint;
import com.hngf.entity.risk.RiskRecord;
import com.hngf.entity.risk.RiskSource;
import com.hngf.entity.sys.Position;
import com.hngf.mapper.risk.RiskCtrlMapper;
import com.hngf.mapper.risk.RiskMapper;
import com.hngf.mapper.risk.RiskMeasureMapper;
import com.hngf.mapper.risk.RiskPointDangerSourceMapper;
import com.hngf.mapper.risk.RiskPointMapper;
import com.hngf.mapper.risk.RiskRecordMapper;
import com.hngf.mapper.risk.RiskSourceMapper;
import com.hngf.mapper.sys.DictMapper;
import com.hngf.mapper.sys.PositionMapper;
import com.hngf.service.risk.RiskCtrlLevelService;
import com.hngf.service.risk.RiskCtrlService;
import com.hngf.service.risk.RiskService;
import com.hngf.service.risk.RiskSourceService;
import com.hngf.service.sys.DictService;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;


@Service("RiskSourceService")
public class RiskSourceServiceImpl implements RiskSourceService {

    @Autowired
    private RiskSourceMapper riskSourceMapper;
    @Autowired
    private RiskMapper riskMapper;
    @Autowired
    private RiskPointDangerSourceMapper riskPointDangerSourceMapper;
    @Autowired
    private RiskRecordMapper riskRecordMapper;
    @Autowired
    private RiskCtrlService riskCtrlService;
    @Autowired
    private RiskMeasureMapper riskMeasureMapper;
    @Autowired
    private RiskPointMapper riskPointMapper;
    @Autowired
    private RiskCtrlLevelService riskCtrlLevelService;
    @Autowired
    private PositionMapper positionMapper;
    @Autowired
    private DictService dictService;
    @Autowired
    private RiskCtrlMapper riskCtrlMapper;
    @Autowired
    private DictMapper dictMapper;
    @Autowired
    private RiskService riskService;
    static JSONObject entRiskCtrlLevel = new JSONObject();
    static JSONObject position = new JSONObject();
    /**
     * 查询设备危险源列表
     * yfh
     * 2020/05/27
     * @param params
     * @return
     */
    @Override
    public List<RiskSourceDto> findList(Map<String, Object> params) {
        List<RiskSourceDto> list = riskSourceMapper.findList(params);
        return list;
    }

    @Override
    public List<RiskInspectItemDto> getControlInspectItemList(Map<String, Object> params) {
        params.remove("positionId");
        return riskSourceMapper.findControlInspectItemList(params);
    }

    @Override
    public int getControlInspectItemCount(Map<String, Object> map) {
        return riskSourceMapper.findControlInspectItemCount(map);
    }
    /**
     * @Author: zyj
     * @Description:作业活动比较图统计图柱形图
     * @Param riskDangerType危险源类型（1 设备设施 2 作业活动） companyId企业id
     * @Date 16:32 2020/6/19
     */
    @Override
    public List<Map<String, Object>> queryRiskLevel(Integer riskDangerType, Long companyId) {
        return riskSourceMapper.queryRiskLevel(riskDangerType,companyId);
    }
    /**
     * @Author: lxf
     * @Description:作业活动比较图统计图柱形图
     * @Param riskDangerType危险源类型（1 设备设施 2 作业活动） companyId企业id
     * @Date 14:32 2020/9/18
     */
    @Override
    public List<Map<String,Object>> queryRiskLevelAnalyze(Integer riskDangerType, Long companyId){
        return riskSourceMapper.queryRiskLevelAnalyze(riskDangerType,companyId);
    }

    @Override
    public RiskSource getById(Long id){
        return riskSourceMapper.findById(id);
    }

    /**
     * 保存危险源
     * yfh
     * 2020/06/04
     * @param riskSource
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(RiskSource riskSource) {
        RiskSource riskSource1= new RiskSource();
        riskSource1.setParentRiskDangerId(riskSource.getParentRiskDangerId());
        riskSource1.setCompanyId(riskSource.getCompanyId());
        Long riskDangerId = riskSource.getRiskDangerId();

        if (riskSource1.getParentRiskDangerId() != null) {
            riskSource.setParentRiskDangerId(riskSource1.getParentRiskDangerId());
            riskSource.setNodeLevel(2);
            riskSource.setRootNode(riskSource1.getParentRiskDangerId());
        } else {
            riskSource.setParentRiskDangerId(-1L);
            riskSource.setNodeLevel(1);
            riskSource.setRootNode(riskDangerId);
        }
        riskSource.setUpdatedTime(new Date());
        riskSource.setIndustryId(1L);
        riskSource.setDelFlag(0);
        riskSourceMapper.add(riskSource);
        if (riskSource1.getParentRiskDangerId()!=null) {
            Long parentDangerSrcId = riskSource1.getParentRiskDangerId();
            riskSource = new RiskSource();
            riskSource.setRiskDangerId(parentDangerSrcId);
            riskSource.setIsLeaf(0);
            riskSourceMapper.update(riskSource);
            Long nodeLevel=(long)3;
            List<RiskSource> list=riskSourceMapper.list(parentDangerSrcId,nodeLevel);
            if (list != null && list.size() > 0) {
                List<String> ids = new ArrayList();
                list.forEach((item) -> {
                    ids.add(item.getRiskDangerId().toString());
                });
                this.riskMapper.deleteByDangerSrcIds(StringUtils.join(ids, ","));
            }

            List<RiskSource> riskSource2=riskSourceMapper.sel(parentDangerSrcId,nodeLevel);
            if(riskSource2.size()>0){
                for(int a=0;a<riskSource2.size();a++){
                    RiskSource riskSource3=riskSource2.get(a);
                    riskSourceMapper.remove(riskSource3.getRiskDangerId());
                }
            }

            riskPointDangerSourceMapper.delDangerSource(parentDangerSrcId, riskSource1.getCompanyId());
            riskRecordMapper.delDangerSource(parentDangerSrcId, riskSource1.getCompanyId());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(RiskSource riskSource) {
        riskSourceMapper.update(riskSource);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeByIds(List ids) {
        riskSourceMapper.deleteByIds(ids);
    }

    /**
     * 删除单个危险源信息
     * yfh
     *2020/06/08
     * @param riskDangerId
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public R removeById(Long riskDangerId) {
        RiskSource dangerSource = (RiskSource)this.getById(riskDangerId);
        riskSourceMapper.deleteById(riskDangerId);
        if (dangerSource == null) {
            return R.error("危险源不存在，删除失败");
        }else{
            List<RiskRecord> riskRecords = riskRecordMapper.list(riskDangerId,dangerSource.getCompanyId());
            if (riskRecords != null && !riskRecords.isEmpty() && riskRecords.size() > 0) {
                return R.error("危险源已经被使用，编辑风险点删除该危险源");
            } else {
                List<RiskSource> list =null;
                if (dangerSource.getParentRiskDangerId().toString().equals("-1")) {
                    list=riskSourceMapper.getDangerSrcId(dangerSource.getCompanyId(),riskDangerId);
                } else {
                    list=riskSourceMapper.selectDangerSrcId(dangerSource.getCompanyId(),riskDangerId);
                }
                if (!list.isEmpty()) {
                    List<Long> idList = (List)list.stream().map(RiskSource::getRiskDangerId).collect(Collectors.toList());
                    this.riskService.removeByIds(idList);
                    this.removeByIds(idList);
                }
                if (!dangerSource.getParentRiskDangerId().toString().equals("-1")) {
                    List<RiskSource> count = riskSourceMapper.count(dangerSource.getParentRiskDangerId());
                    if (count.size() == 0) {
                        RiskSource _eds = new RiskSource();
                        _eds.setRiskDangerId(dangerSource.getParentRiskDangerId());
                        _eds.setIsLeaf(1);
                        _eds.setRiskDangerLevel(4);
                        this.update(_eds);
                    }else{
                        Integer lvv=riskSourceMapper.selLV(dangerSource.getParentRiskDangerId());
                        RiskSource _eds = new RiskSource();
                        _eds.setRiskDangerId(dangerSource.getParentRiskDangerId());
                        Integer lv = Integer.valueOf(lvv);
                        if (lv != -1) {
                            if (dangerSource.getRiskDangerLevel() < lv) {
                                lv = dangerSource.getRiskDangerLevel();
                            }

                            _eds.setRiskDangerLevel(lv);
                        } else {
                            _eds.setRiskDangerLevel(4);
                        }
                        this.update(_eds);
                    }
                }
                return R.ok("删除成功");
            }
        }
    }

    @Override
    public List<RiskSource> queryByMap(Map<String, Object> params) {
        return riskSourceMapper.findByMap(params);
    }

    /**
     * 检验excel表格填写内容是否规范
     * yfh
     * 2020/06/04
     * @param filePath
     * @param json
     * @throws Exception
     */
    @Override
    public void importExcelCheck(String filePath, JSONObject json) throws Exception {
        Long aId = json.getLong("userId");
        Long cId = json.getLong("companyId");
        List<List<Object>> readAll = readFirstSheet(filePath);
        if (readAll.size() == 0) {
            throw new Exception("导入失败，未解析到数据，请检测Excel下方的工作区是否在第一个");
        } else {
            if (readAll.size() > 2) {
                Set<String> sets = new HashSet();

                for(int i = 3; i < readAll.size(); ++i) {
                    int k = i + 1;
                    List<Object> one = readAll.get(i);
                    JSONArray data = new JSONArray(one);
                    String riskCode = data.getString(0);
                    if (StringUtils.isEmpty(riskCode)) {
                        throw new Exception( "第" + k + "行风险编码是空的，请填写！");
                    }

                    String dangerSrcName = data.getString(1);
                    if (StringUtils.isEmpty(dangerSrcName)) {
                        throw new Exception("第" + k + "行风险编码为【" + riskCode + "】设备名称是空的，请填写！");
                    }

                    String riskName = data.getString(2);
                    if (StringUtils.isEmpty(riskName)) {
                        throw new Exception("第" + k + "行风险编码为【" + riskCode + "】设备检查项是空的，请填写！");
                    }


                    String riskLevelName = data.getString(5);
                    if (StringUtils.isEmpty(riskLevelName)) {
                        throw new Exception("第" + k + "行风险编码为【" + riskCode + "】等级是空的，请填写！");
                    }

                    if (this.getOneEntRiskCode(cId, riskCode)) {
                        throw new Exception( "第" + k + "行风险编码【" + riskCode + "】已存在，请更换！");
                    }

                    sets.add(dangerSrcName);
                    sets.add(riskCode);
                }
            }

        }
    }


    /**
     * 检验LecExcel表格填写内容是否规范
     * yfh
     * 2020/08/25
     * @param filePath
     * @param json
     * @throws Exception
     */
    @Override
    public void importLecExcel(String filePath, JSONObject json) throws Exception {
        Long cId = json.getLong("companyId");
        List<List<Object>> readAll = readFirstSheet(filePath);
        if (readAll.size() == 0) {
            throw new Exception("导入失败，未解析到数据，请检测Excel下方的工作区是否在第一个");
        } else {
            if (readAll.size() > 2) {

                for(int i = 3; i < readAll.size(); ++i) {
                    int k = i + 1;
                    List<Object> one = (List)readAll.get(i);
                    JSONArray data = new JSONArray(one);
                    String riskCode = data.getString(0);
                    if (StringUtils.isEmpty(riskCode)) {
                        throw new Exception( "第" + k + "行序号是空的，请填写！");
                    }

                    String riskName = data.getString(1);
                    if (StringUtils.isEmpty(riskName)) {
                        throw new Exception( "第" + k + "行作业单元是空的，请填写！");
                    }

                    String dangerSrcName = data.getString(3);
                    if (StringUtils.isEmpty(dangerSrcName)) {
                        throw new Exception("第" + k + "行序号为【" + riskCode + "】致险因素是空的，请填写！");
                    }


                    String l = data.getString(4);
                    if (StringUtils.isEmpty(l)) {
                        throw new Exception("第" + k + "行序号为【" + riskCode + "】可能性大小L是空的，请填写！");
                    }

                    String e = data.getString(5);
                    if (StringUtils.isEmpty(e)) {
                        throw new Exception("第" + k + "行序号为【" + riskCode + "】可能性大小E是空的，请填写！");
                    }

                    String c = data.getString(6);
                    if (StringUtils.isEmpty(c)) {
                        throw new Exception("第" + k + "行序号为【" + riskCode + "】可能性大小C是空的，请填写！");
                    }

                    if (this.getOneEntRiskCode(cId, riskCode)) {
                        throw new Exception( "第" + k + "行序号【" + riskCode + "】已存在，请更换！");
                    }
                }
            }

        }
    }

    /**
     * 检验LcExcel表格填写内容是否规范
     * yfh
     * 2020/08/25
     * @param filePath
     * @param json
     * @throws Exception
     */
    @Override
    public void importLcExcel(String filePath, JSONObject json) throws Exception {
        Long cId = json.getLong("companyId");
        List<List<Object>> readAll = readFirstSheet(filePath);
        if (readAll.size() == 0) {
            throw new Exception("导入失败，未解析到数据，请检测Excel下方的工作区是否在第一个");
        } else {
            if (readAll.size() > 2) {

                for(int i = 2; i < readAll.size(); ++i) {
                    int k = i + 1;
                    List<Object> one = (List)readAll.get(i);
                    JSONArray data = new JSONArray(one);
                    String riskCode = data.getString(0);
                    if (StringUtils.isEmpty(riskCode)) {
                        throw new Exception( "第" + k + "行序号是空的，请填写！");
                    }

                    String riskName = data.getString(1);
                    if (StringUtils.isEmpty(riskName)) {
                        throw new Exception( "第" + k + "行作业单元是空的，请填写！");
                    }

                    String dangerSrcName = data.getString(3);
                    if (StringUtils.isEmpty(dangerSrcName)) {
                        throw new Exception("第" + k + "行序号为【" + riskCode + "】致险因素是空的，请填写！");
                    }


                    String l = data.getString(4);
                    if (StringUtils.isEmpty(l)) {
                        throw new Exception("第" + k + "行序号为【" + riskCode + "】可能性大小L是空的，请填写！");
                    }

                    String c = data.getString(5);
                    if (StringUtils.isEmpty(c)) {
                        throw new Exception("第" + k + "行序号为【" + riskCode + "】可能性大小C是空的，请填写！");
                    }

                    if (this.getOneEntRiskCode(cId, riskCode)) {
                        throw new Exception( "第" + k + "行序号【" + riskCode + "】已存在，请更换！");
                    }
                }
            }

        }
    }
    /**
     * 检验LsExcel表格填写内容是否规范
     * yfh
     * 2020/08/28
     * @param filePath
     * @param json
     * @throws Exception
     */
    @Override
    public void importLsExcel(String filePath, JSONObject json) throws Exception {
        Long cId = json.getLong("companyId");
        List<List<Object>> readAll = readFirstSheet(filePath);
        if (readAll.size() == 0) {
            throw new Exception("导入失败，未解析到数据，请检测Excel下方的工作区是否在第一个");
        } else {
            if (readAll.size() > 2) {

                for(int i = 2; i < readAll.size(); ++i) {
                    int k = i + 1;
                    List<Object> one = (List)readAll.get(i);
                    JSONArray data = new JSONArray(one);
                    String riskCode = data.getString(0);
                    if (StringUtils.isEmpty(riskCode)) {
                        throw new Exception( "第" + k + "行序号是空的，请填写！");
                    }

                    String riskName = data.getString(1);
                    if (StringUtils.isEmpty(riskName)) {
                        throw new Exception( "第" + k + "行作业单元是空的，请填写！");
                    }

                    String dangerSrcName = data.getString(3);
                    if (StringUtils.isEmpty(dangerSrcName)) {
                        throw new Exception("第" + k + "行序号为【" + riskCode + "】致险因素是空的，请填写！");
                    }


                    String l = data.getString(4);
                    if (StringUtils.isEmpty(l)) {
                        throw new Exception("第" + k + "行序号为【" + riskCode + "】可能性大小L是空的，请填写！");
                    }

                    String c = data.getString(5);
                    if (StringUtils.isEmpty(c)) {
                        throw new Exception("第" + k + "行序号为【" + riskCode + "】可能性大小S是空的，请填写！");
                    }

                    if (this.getOneEntRiskCode(cId, riskCode)) {
                        throw new Exception( "第" + k + "行序号【" + riskCode + "】已存在，请更换！");
                    }
                }
            }

        }
    }


    /**
     * 检验水利LecExcel表格填写内容是否规范
     * yfh
     * 2020/08/26
     * @param filePath
     * @param json
     * @throws Exception
     */
    @Override
    public void importRiverLecExcel(String filePath, JSONObject json) throws Exception {
        Long cId = json.getLong("companyId");
        List<List<Object>> readAll = readFirstSheet(filePath);
        if (readAll.size() == 0) {
            throw new Exception("导入失败，未解析到数据，请检测Excel下方的工作区是否在第一个");
        } else {
            if (readAll.size() > 2) {

                for(int i = 2; i < readAll.size(); ++i) {
                    int k = i + 1;
                    List<Object> one = (List)readAll.get(i);
                    JSONArray data = new JSONArray(one);
                    String riskCode = data.getString(0);
                    if (StringUtils.isEmpty(riskCode)) {
                        throw new Exception( "第" + k + "行编号是空的，请填写！");
                    }

                    String dangerSrcName = data.getString(2);
                    if (StringUtils.isEmpty(dangerSrcName)) {
                        throw new Exception( "第" + k + "行作业风险点名称是空的，请填写！");
                    }

                    String riskName = data.getString(4);
                    if (StringUtils.isEmpty(riskName)) {
                        throw new Exception( "第" + k + "行检查项名称是空的，请填写！");
                    }


                    String l = data.getString(13);
                    if (StringUtils.isEmpty(l)) {
                        throw new Exception("第" + k + "行编号为【" + riskCode + "】风险评估L是空的，请填写！");
                    }

                    String e = data.getString(14);
                    if (StringUtils.isEmpty(e)) {
                        throw new Exception("第" + k + "行编号为【" + riskCode + "】风险评估E是空的，请填写！");
                    }

                    String c = data.getString(15);
                    if (StringUtils.isEmpty(c)) {
                        throw new Exception("第" + k + "行编号为【" + riskCode + "】风险评估C是空的，请填写！");
                    }

                    String controPos = data.getString(20);
                    if (StringUtils.isEmpty(controPos)) {
                        throw new Exception("第" + k + "行编号为【" + riskCode + "】管控岗位是空的，请填写！");
                    }

                    if (this.getOneEntRiskCode(cId, riskCode)) {
                        throw new Exception( "第" + k + "行编号【" + riskCode + "】已存在，请更换！");
                    }
                }
            }

        }
    }


    /**
     * 检验水利LsExcel表格填写内容是否规范
     * yfh
     * 2020/08/26
     * @param filePath
     * @param json
     * @throws Exception
     */
    @Override
    public void importRiverLsExcel(String filePath, JSONObject json) throws Exception {
        Long cId = json.getLong("companyId");
        List<List<Object>> readAll = readFirstSheet(filePath);
        if (readAll.size() == 0) {
            throw new Exception("导入失败，未解析到数据，请检测Excel下方的工作区是否在第一个");
        } else {
            if (readAll.size() > 2) {

                for(int i = 2; i < readAll.size(); ++i) {
                    int k = i + 1;
                    List<Object> one = (List)readAll.get(i);
                    JSONArray data = new JSONArray(one);
                    String riskCode = data.getString(0);
                    if (StringUtils.isEmpty(riskCode)) {
                        throw new Exception( "第" + k + "行编号是空的，请填写！");
                    }

                    String dangerSrcName = data.getString(2);
                    if (StringUtils.isEmpty(dangerSrcName)) {
                        throw new Exception( "第" + k + "行作业风险点名称是空的，请填写！");
                    }

                    String riskName = data.getString(4);
                    if (StringUtils.isEmpty(riskName)) {
                        throw new Exception( "第" + k + "行检查项名称是空的，请填写！");
                    }


                    String l = data.getString(13);
                    if (StringUtils.isEmpty(l)) {
                        throw new Exception("第" + k + "行编号为【" + riskCode + "】风险评估L是空的，请填写！");
                    }

                    String e = data.getString(14);
                    if (StringUtils.isEmpty(e)) {
                        throw new Exception("第" + k + "行编号为【" + riskCode + "】风险评估S是空的，请填写！");
                    }

                    String controPos = data.getString(19);
                    if (StringUtils.isEmpty(controPos)) {
                        throw new Exception("第" + k + "行编号为【" + riskCode + "】管控岗位是空的，请填写！");
                    }

                    if (this.getOneEntRiskCode(cId, riskCode)) {
                        throw new Exception( "第" + k + "行编号【" + riskCode + "】已存在，请更换！");
                    }
                }
            }

        }
    }
    /**
     * 根据文件路径 获取excel文件内容 并导入数据到数据库
     * yfh
     * 2020/06/04
     * @param filePath
     * @param json
     * @param ids
     * @return
     * @throws Exception
     */
    @Override
    public String importExcel(String filePath, JSONObject json,Long ids) throws Exception {
        //当前用户id
        Long aId = json.getLong("userId");
        Long cId = json.getLong("companyId");
        Integer riskDangerType = json.getInteger("riskDangerType");
        //获取excel表格内容
        List<List<Object>> readAll = readFirstSheet(filePath);
        List<Position> positionList = new ArrayList();
        //判断表格内是否有数据
        if (readAll.size() <= 2) {
            return "0";
        } else {
            Set<String> sets = new HashSet();
            List entRiskCtrlLevels;
            for(int i = 3; i < readAll.size(); ++i) {
                int k = i + 1;
                entRiskCtrlLevels = (List)readAll.get(i);
                JSONArray data = new JSONArray(entRiskCtrlLevels);
                String riskCode = data.getString(0);
                String dangerName = data.getString(1);
                String riskName = data.getString(2);
                sets.add(dangerName);
                sets.add(riskCode);
            }
            Long _sId = ids;
            //根据企业id 获取企业管控层级信息
            entRiskCtrlLevels = riskCtrlLevelService.getRiskCtrlLevelList(cId);
            if (entRiskCtrlLevels.isEmpty()) {
                throw new Exception("企业未配置【管控层级】信息");
            } else {
                int count = 0;
                // 循环读取excel表格每行内容 并插入到数据库
                for(int i = 3; i < readAll.size(); ++i) {
                    int k = i + 1;
                    List<Object> one = (List)readAll.get(i);
                    JSONArray data = new JSONArray(one);
                    List<RiskSource> eds = new ArrayList();
                    String riskCode = data.getString(0);
                    if (StringUtils.isEmpty(riskCode)) {
                        throw new Exception("第" + k + "行风险编码是空的，请填写！");
                    }

                    String dangerName = data.getString(1);
                    if (StringUtils.isEmpty(dangerName)) {
                        throw new Exception("第" + k + "行风险编码为【" + riskCode + "】设备名称是空的，请填写！");
                    }
                    String riskName = data.getString(2);
                    if (StringUtils.isEmpty(riskName)) {
                        throw new Exception("第" + k + "行风险编码为【" + riskCode + "】设备检查项是空的，请填写！");
                    }

                    String riskLevelName = data.getString(5);
                    if (StringUtils.isEmpty(riskLevelName)) {
                        throw new Exception("第" + k + "行风险编码为【" + riskCode + "】等级是空的，请填写！");
                    }
                    //根据风险等级名称 获取风险等级Code码
                    Integer dangerSrcLv = this.dangerSrcLv(riskLevelName);
                    // 根据根危险源名称 获取根危险源Code码
                    String dangerSrcCode = this.dangerSrcCode(dangerName);
                    String riskReason = data.getString(3);
                    String riskConsequence = data.getString(4);
                    //获取下一个危险源id
                    Long id = this.getNextId(_sId, count);
                    //修改sys_uid_sequence表 对应scyf_risk_source的id
                    dictMapper.setTabId("scyf_risk_source",id);
                    ++count;
                    if (!"".equals(dangerName)) {
                        Integer nodeLevel=1;
                        //根据企业id 危险源类型 危险源名称 危险状态 节点层级 获取危险源对象
                        RiskSource riskSource =riskSourceMapper.getRiskSource(cId,riskDangerType,dangerName,nodeLevel);
                        if (riskSource == null) {
                            //添加危险源
                            eds.add(this.buildRiskSource(id, -1L, id, dangerSrcCode, 1, 0, dangerName, cId, riskDangerType, 0));
                        } else {
                            id = riskSource.getRiskDangerId();
                        }
                    }

                    Long pId = 0L;

                    Long riskId = 0L;
                    if (riskName.equals("")) {
                        throw new Exception("第" + k + "行编号为【" + riskCode + "】的风险 ，设备检查项为空，请填写！");
                    }

                    if (pId.equals(0L)) {
                        pId = id;
                    }
                    riskId = this.getNextId(_sId, count);
                    ++count;
                    //添加危险源
                    eds.add(this.buildRiskSource(riskId, pId, id, dangerSrcCode, 3, 1, riskName, cId, riskDangerType, dangerSrcLv));
                    //批量保存危险源
                    riskSourceMapper.saveBatch(eds);
                    //修改风险等级
                    this.updateLvAndIsLeaf(riskId, cId);
                    Risk risk = new Risk();
                    risk.setRiskId(riskId);
                    risk.setCompanyId(cId);
                    risk.setRiskDangerId(riskId);
                    risk.setRiskName(riskName);
                    risk.setRiskCode(riskCode);
                    risk.setRiskHramFactor(riskReason);
                    risk.setRiskConsequence(riskConsequence);
                    risk.setRiskTypeId((long)Constant.RISK_TYPE_CHANGGUI);
                    risk.setRiskLevel(dangerSrcLv);
                    risk.setCreatedBy(aId);
                    risk.setCreatedTime(new Date());
                    risk.setUpdatedBy(aId);
                    risk.setUpdatedTime(new Date());
                    risk.setDelFlag(0);
                    //保存风险
                    this.riskMapper.add(risk);
                    String positionTitle = one.size() > 12 ? data.getString(12) : "";
                    List<RiskCtrl> riskCtrls = new ArrayList();
                    if (!"".equals(positionTitle) && positionTitle.replace("，", ",").split(",").length > 0) {
                        String[] positionTitles = positionTitle.replace("，", ",").split(",");
                        for(int j = 0; j < positionTitles.length; ++j) {
                            RiskCtrl riskCtrl = new RiskCtrl();
                            //获取管控层级id
                            Long ctrlLevelId = this.ctrlLevelId(entRiskCtrlLevels, dangerSrcLv.toString());
                            riskCtrl.setRiskCtrlLevelId(ctrlLevelId);
                            Long positionId = null;
                            if (!StringUtils.isBlank(positionTitles[j])) {
                                try {
                                    //添加岗位表信息 并返回岗位id
                                    positionId = this.getPositionId(positionList, positionTitles[j], cId, aId);
                                } catch (Exception var47) {
                                    var47.printStackTrace();
                                }
                                riskCtrl.setRiskCtrlPositionId(positionId);
                                riskCtrl.setCompanyId(cId);
                                riskCtrl.setRiskId(riskId);
                                riskCtrl.setCreatedBy(aId);
                                riskCtrl.setCreatedTime(new Date());
                                riskCtrl.setUpdatedBy(aId);
                                riskCtrl.setUpdatedTime(new Date());
                                riskCtrl.setDelFlag(0);
                                boolean f = true;
                                for(int l = 0; l < riskCtrls.size(); ++l) {
                                    RiskCtrl e = (RiskCtrl)riskCtrls.get(l);
                                    if (e.getRiskId().equals(riskId) && e.getRiskCtrlPositionId().equals(positionId) && e.getRiskCtrlLevelId().equals(ctrlLevelId)) {
                                        f = false;
                                        break;
                                    }
                                }
                                if (f) {
                                    riskCtrls.add(riskCtrl);
                                }
                            }
                        }
                        if (riskCtrls.size() > 0) {
                            //批量添加风险管控配置表信息
                            this.riskCtrlMapper.saveBatch(riskCtrls);
                        }
                    }else{
                        RiskCtrl riskCtrl = new RiskCtrl();
                        if (!dangerSrcLv.toString().equals("")) {
                            riskCtrl.setRiskCtrlLevelId(this.ctrlLevelId(entRiskCtrlLevels, dangerSrcLv.toString()));
                        }
                        riskCtrl.setCompanyId(cId);
                        riskCtrl.setRiskId(riskId);
                        riskCtrl.setCreatedBy(aId);
                        riskCtrl.setRiskCtrlPositionId(-1L);
                        riskCtrl.setCreatedTime(new Date());
                        riskCtrl.setCreatedBy(aId);
                        riskCtrl.setUpdatedBy(aId);
                        riskCtrl.setUpdatedTime(new Date());
                        riskCtrl.setDelFlag(0);
                        //添加风险管控配置表信息
                        this.riskCtrlMapper.add(riskCtrl);
                    }
                    String riskMeasureType1 = data.getString(6);
                    String riskMeasureType2 = data.getString(7);
                    String riskMeasureType3 = data.getString(8);
                    String riskMeasureType4 = data.getString(9);
                    String riskMeasureType5 = data.getString(10);
                    List<RiskMeasure> list = new ArrayList();
                    //根据管控措施类型不同 返回风险管控措施信息
                    if (riskMeasureType1 != null && !"".equals(riskMeasureType1)) {
                        list = this.buildList(_sId,riskMeasureType1, 1, cId, riskId, aId, (List)list, ";", false);
                    }
                    if (riskMeasureType2 != null && !"".equals(riskMeasureType2)) {
                        list = this.buildList(_sId,riskMeasureType2, 2, cId, riskId, aId, (List)list, ";", false);
                    }

                    if (riskMeasureType3 != null && !"".equals(riskMeasureType3)) {
                        list = this.buildList(_sId,riskMeasureType3, 3, cId, riskId, aId, (List)list, ";", false);
                    }

                    if (riskMeasureType4 != null && !"".equals(riskMeasureType4)) {
                        list = this.buildList(_sId,riskMeasureType4, 4, cId, riskId, aId, (List)list, ";", false);
                    }

                    if (riskMeasureType5 != null && !"".equals(riskMeasureType5)) {
                        list = this.buildList(_sId,riskMeasureType5, 5, cId, riskId, aId, (List)list, ";", false);
                    }
                    if (null != list &&  ((List)list).size() > 0) {
                        Long[] sId;
                        int tempSize = ((List)list).size() ;
                        sId = new Long[]{dictService.getTabId("scyf_risk_source")};
                        List<RiskMeasure> _list = new ArrayList();

                        for(int j = 0; j < tempSize; ++j) {
                            RiskMeasure entRiskMeasure = (RiskMeasure)((List)list).get(j);
                            entRiskMeasure.setRiskMeasureId(sId[0]);
                            _list.add(entRiskMeasure);

                            if ( (0 != j && 0 == j%500 ) || tempSize-1 == j  ) {
                                //批量保存管控措施信息
                                this.riskMeasureMapper.saveBatch(_list);
                                if(tempSize-1 != j){
                                    _list = new ArrayList();
                                }
                            }

                        }
                    }
                }
                return "1";
            }
        }
    }



    /**
     * 根据文件路径 获取excel文件内容 并导入数据LEC/LC/LS到数据库    交通
     * yfh
     * 2020/06/04
     * @param filePath
     * @param json
     * @param ids
     * @return
     * @throws Exception
     */
    @Override
    public String importNewExcel(String filePath, JSONObject json,Long ids,String type) throws Exception {
        //当前用户id
        Long aId = json.getLong("userId");
        Long cId = json.getLong("companyId");
        Integer riskDangerType = json.getInteger("riskDangerType");
        //获取excel表格内容
        List<List<Object>> readAll = readFirstSheet(filePath);
        List<Position> positionList = new ArrayList();
        //判断表格内是否有数据
        if (readAll.size() <= 2) {
            return "0";
        } else {
            Set<String> sets = new HashSet();
            List entRiskCtrlLevels;
            for(int i = 2; i < readAll.size(); ++i) {
                entRiskCtrlLevels = (List)readAll.get(i);
                JSONArray data = new JSONArray(entRiskCtrlLevels);
                String riskCode = data.getString(0);
                String dangerName = data.getString(3);
                sets.add(dangerName);
                sets.add(riskCode);
            }
            Long _sId = ids;
            //根据企业id 获取企业管控层级信息
            entRiskCtrlLevels = riskCtrlLevelService.getRiskCtrlLevelList(cId);
            if (entRiskCtrlLevels.isEmpty()) {
                throw new Exception("企业未配置【管控层级】信息");
            } else {
                int count = 0;
                // 循环读取excel表格每行内容 并插入到数据库
                for(int i = 2; i < readAll.size(); ++i) {
                    if(type.equals("LEC")){
                        int k = i + 1;
                        List<Object> one = (List)readAll.get(i);
                        JSONArray data = new JSONArray(one);
                        List<RiskSource> eds = new ArrayList();
                        String riskCode = data.getString(0);
                        if (StringUtils.isEmpty(riskCode)) {
                            throw new Exception( "第" + k + "行序号是空的，请填写！");
                        }

                        String riskName = data.getString(1);
                        if (StringUtils.isEmpty(riskName)) {
                            throw new Exception( "第" + k + "行作业单元是空的，请填写！");
                        }

                        String dangerSrcName = data.getString(3);
                        if (StringUtils.isEmpty(dangerSrcName)) {
                            throw new Exception("第" + k + "行序号为【" + riskCode + "】致险因素是空的，请填写！");
                        }


                        String l = data.getString(4);
                        if (StringUtils.isEmpty(l)) {
                            throw new Exception("第" + k + "行序号为【" + riskCode + "】可能性大小L是空的，请填写！");
                        }

                        String e = data.getString(5);
                        if (StringUtils.isEmpty(e)) {
                            throw new Exception("第" + k + "行序号为【" + riskCode + "】可能性大小E是空的，请填写！");
                        }

                        String c = data.getString(6);
                        if (StringUtils.isEmpty(c)) {
                            throw new Exception("第" + k + "行序号为【" + riskCode + "】可能性大小C是空的，请填写！");
                        }

                        if (this.getOneEntRiskCode(cId, riskCode)) {
                            throw new Exception( "第" + k + "行序号【" + riskCode + "】已存在，请更换！");
                        }
                        String groupName = data.getString(16);
                        String groupUser = data.getString(18);
                        BigDecimal a2=new BigDecimal(70);
                        BigDecimal a3=new BigDecimal(160);
                        BigDecimal a4=new BigDecimal(320);
                        BigDecimal newL=new BigDecimal(l);
                        BigDecimal newE=new BigDecimal(e);
                        BigDecimal newC=new BigDecimal(c);
                        BigDecimal newD=newL.multiply(newE).multiply(newC);
                        String riskLevel="";
                        if(newD.compareTo(a2) < 1){
                            riskLevel="蓝";
                        }
                        if(newD.compareTo(a2) == 1 && newD.compareTo(a3) < 1){
                            riskLevel="黄";
                        }
                        if(newD.compareTo(a3) == 1 && newD.compareTo(a4) < 1){
                            riskLevel="橙";
                        }
                        if(newD.compareTo(a4) == 1){
                            riskLevel="红";
                        }
                        //根据风险等级名称 获取风险等级Code码
                        Integer dangerSrcLv = this.dangerSrcLv(riskLevel);
                        // 根据根危险源名称 获取根危险源Code码
                        String dangerSrcCode = this.dangerSrcCode(riskName);
                        String riskReason = data.getString(3);
                        String riskConsequence = data.getString(2);
                        String riskDesc = one.size() > 19 ? data.getString(19) : "";
                        //获取下一个危险源id
                        Long id = this.getNextId(_sId, count);
                        //修改sys_uid_sequence表 对应scyf_risk_source的id
                        dictMapper.setTabId("scyf_risk_source",id);
                        ++count;
                        if (!"".equals(riskName)) {
                            Integer nodeLevel=1;
                            //根据企业id 危险源类型 危险源名称 危险状态 节点层级 获取危险源对象
                            RiskSource riskSource =riskSourceMapper.getRiskSource(cId,riskDangerType,riskName,nodeLevel);
                            if (riskSource == null) {
                                //添加危险源
                                eds.add(this.buildRiskSource(id, -1L, id, dangerSrcCode, 1, 0, riskName, cId, riskDangerType, 0));
                            } else {
                                id = riskSource.getRiskDangerId();
                            }
                        }

                        Long pId = 0L;

                        Long riskId = 0L;

                        if (pId.equals(0L)) {
                            pId = id;
                        }
                        riskId = this.getNextId(_sId, count);
                        ++count;
                        //添加危险源
                        eds.add(this.buildRiskSource(riskId, pId, id, dangerSrcCode, 3, 1, "", cId, riskDangerType, dangerSrcLv));
                        //批量保存危险源
                        riskSourceMapper.saveBatch(eds);
                        //修改风险等级
                        this.updateLvAndIsLeaf(riskId, cId);
                        Risk risk = new Risk();
                        risk.setRiskId(riskId);
                        risk.setCompanyId(cId);
                        risk.setRiskDangerId(riskId);
                        risk.setRiskCode(riskCode);
                        risk.setRiskHramFactor(riskReason);
                        risk.setRiskConsequence(riskConsequence);
                        risk.setRiskTypeId((long)Constant.RISK_TYPE_CHANGGUI);
                        risk.setRiskDesc(riskDesc);
                        risk.setRiskLevel(dangerSrcLv);
                        risk.setCreatedBy(aId);
                        risk.setCreatedTime(new Date());
                        risk.setUpdatedBy(aId);
                        risk.setUpdatedTime(new Date());
                        risk.setLecL(newL);
                        risk.setLecE(newE);
                        risk.setLecC(newC);
                        risk.setLecD(newD);
                        risk.setTypeId(0);
                        risk.setGroupName(groupName);
                        risk.setGroupUser(groupUser);
                        risk.setDelFlag(0);
                        //保存风险
                        this.riskMapper.add(risk);
                        String positionTitle = one.size() > 17 ? data.getString(17) : "";
                        List<RiskCtrl> riskCtrls = new ArrayList();
                        if (!"".equals(positionTitle) && positionTitle.replace("，", ",").split(",").length > 0) {
                            String[] positionTitles = positionTitle.replace("，", ",").split(",");
                            for(int j = 0; j < positionTitles.length; ++j) {
                                RiskCtrl riskCtrl = new RiskCtrl();
                                //获取管控层级id
                                Long ctrlLevelId = this.ctrlLevelId(entRiskCtrlLevels, dangerSrcLv.toString());
                                riskCtrl.setRiskCtrlLevelId(ctrlLevelId);
                                Long positionId = null;
                                if (!StringUtils.isBlank(positionTitles[j])) {
                                    try {
                                        //添加岗位表信息 并返回岗位id
                                        positionId = this.getPositionId(positionList, positionTitles[j], cId, aId);
                                    } catch (Exception var47) {
                                        var47.printStackTrace();
                                    }
                                    riskCtrl.setRiskCtrlPositionId(positionId);
                                    riskCtrl.setCompanyId(cId);
                                    riskCtrl.setRiskId(riskId);
                                    riskCtrl.setCreatedBy(aId);
                                    riskCtrl.setCreatedTime(new Date());
                                    riskCtrl.setUpdatedBy(aId);
                                    riskCtrl.setUpdatedTime(new Date());
                                    riskCtrl.setDelFlag(0);
                                    boolean f = true;
                                    for(int l1 = 0; l1 < riskCtrls.size(); ++l1) {
                                        RiskCtrl e1 = (RiskCtrl)riskCtrls.get(l1);
                                        if (e1.getRiskId().equals(riskId) && e1.getRiskCtrlPositionId().equals(positionId) && e1.getRiskCtrlLevelId().equals(ctrlLevelId)) {
                                            f = false;
                                            break;
                                        }
                                    }
                                    if (f) {
                                        riskCtrls.add(riskCtrl);
                                    }
                                }
                            }
                            if (riskCtrls.size() > 0) {
                                //批量添加风险管控配置表信息
                                this.riskCtrlMapper.saveBatch(riskCtrls);
                            }
                        }else{
                            RiskCtrl riskCtrl = new RiskCtrl();
                            if (!dangerSrcLv.toString().equals("")) {
                                riskCtrl.setRiskCtrlLevelId(this.ctrlLevelId(entRiskCtrlLevels, dangerSrcLv.toString()));
                            }
                            riskCtrl.setCompanyId(cId);
                            riskCtrl.setRiskId(riskId);
                            riskCtrl.setCreatedBy(aId);
                            riskCtrl.setRiskCtrlPositionId(-1L);
                            riskCtrl.setCreatedTime(new Date());
                            riskCtrl.setCreatedBy(aId);
                            riskCtrl.setUpdatedBy(aId);
                            riskCtrl.setUpdatedTime(new Date());
                            riskCtrl.setDelFlag(0);
                            //添加风险管控配置表信息
                            this.riskCtrlMapper.add(riskCtrl);
                        }
                        String riskMeasureType1 = data.getString(10);
                        String riskMeasureType2 = data.getString(11);
                        String riskMeasureType3 = data.getString(12);
                        String riskMeasureType4 = data.getString(14);
                        String riskMeasureType5 = data.getString(13);
                        List<RiskMeasure> list = new ArrayList();
                        //根据管控措施类型不同 返回风险管控措施信息
                        if (riskMeasureType1 != null && !"".equals(riskMeasureType1)) {
                            list = this.buildList(_sId,riskMeasureType1, 1, cId, riskId, aId, (List)list, ";", false);
                        }
                        if (riskMeasureType2 != null && !"".equals(riskMeasureType2)) {
                            list = this.buildList(_sId,riskMeasureType2, 2, cId, riskId, aId, (List)list, ";", false);
                        }

                        if (riskMeasureType3 != null && !"".equals(riskMeasureType3)) {
                            list = this.buildList(_sId,riskMeasureType3, 3, cId, riskId, aId, (List)list, ";", false);
                        }

                        if (riskMeasureType4 != null && !"".equals(riskMeasureType4)) {
                            list = this.buildList(_sId,riskMeasureType4, 4, cId, riskId, aId, (List)list, ";", false);
                        }

                        if (riskMeasureType5 != null && !"".equals(riskMeasureType5)) {
                            list = this.buildList(_sId,riskMeasureType5, 5, cId, riskId, aId, (List)list, ";", false);
                        }
                        if (((List)list).size() > 0) {
                            Long[] sId;
                            if (((List)list).size() > 1000) {
                                sId = new Long[]{dictService.getTabId("scyf_risk_source")};
                                List<RiskMeasure> _list = new ArrayList();

                                for(int j = 0; j < ((List)list).size(); ++j) {
                                    if (j % 1000 == 0) {
                                        //批量保存管控措施信息
                                        this.riskMeasureMapper.saveBatch(_list);
                                        _list = new ArrayList();
                                    }

                                    RiskMeasure entRiskMeasure = (RiskMeasure)((List)list).get(j);
                                    entRiskMeasure.setRiskMeasureId(sId[0]);
                                    _list.add(entRiskMeasure);
                                }
                            } else {
                                sId = new Long[]{dictService.getTabId("scyf_risk_source")};
                                for(int c1=0;c1<list.size();c1++){
                                    list.get(c1).setRiskMeasureId(sId[0]);
                                }
                                //批量保存管控措施信息
                                this.riskMeasureMapper.saveBatch(list);
                            }
                        }
                    }else if(type.equals("LC")){
                        int k = i + 1;
                        List<Object> one = (List)readAll.get(i);
                        JSONArray data = new JSONArray(one);
                        List<RiskSource> eds = new ArrayList();
                        String riskCode = data.getString(0);
                        if (StringUtils.isEmpty(riskCode)) {
                            throw new Exception( "第" + k + "行序号是空的，请填写！");
                        }

                        String riskName = data.getString(1);
                        if (StringUtils.isEmpty(riskName)) {
                            throw new Exception( "第" + k + "行作业单元是空的，请填写！");
                        }

                        String dangerSrcName = data.getString(3);
                        if (StringUtils.isEmpty(dangerSrcName)) {
                            throw new Exception("第" + k + "行序号为【" + riskCode + "】致险因素是空的，请填写！");
                        }


                        String l = data.getString(4);
                        if (StringUtils.isEmpty(l)) {
                            throw new Exception("第" + k + "行序号为【" + riskCode + "】可能性大小L是空的，请填写！");
                        }

                        String c = data.getString(5);
                        if (StringUtils.isEmpty(c)) {
                            throw new Exception("第" + k + "行序号为【" + riskCode + "】可能性大小C是空的，请填写！");
                        }

                        if (this.getOneEntRiskCode(cId, riskCode)) {
                            throw new Exception( "第" + k + "行序号【" + riskCode + "】已存在，请更换！");
                        }
                        String groupName = data.getString(15);
                        String groupUser = data.getString(17);
                        BigDecimal a1=new BigDecimal(0);
                        BigDecimal a2=new BigDecimal(5);
                        BigDecimal a3=new BigDecimal(20);
                        BigDecimal a4=new BigDecimal(55);
                        BigDecimal a5=new BigDecimal(100);
                        BigDecimal newL=new BigDecimal(l);
                        BigDecimal newC=new BigDecimal(c);
                        BigDecimal newD=newL.multiply(newC);
                        String riskLevel="";
                        if(newD.compareTo(a1) == 1 && newD.compareTo(a2) < 1){
                            riskLevel="蓝";
                        }
                        if(newD.compareTo(a2) == 1 && newD.compareTo(a3) < 1){
                            riskLevel="黄";
                        }
                        if(newD.compareTo(a3) == 1 && newD.compareTo(a4) < 1){
                            riskLevel="橙";
                        }
                        if(newD.compareTo(a4) == 1 && newD.compareTo(a5) < 1){
                            riskLevel="红";
                        }
                        //根据风险等级名称 获取风险等级Code码
                        Integer dangerSrcLv = this.dangerSrcLv(riskLevel);
                        // 根据根危险源名称 获取根危险源Code码
                        String dangerSrcCode = this.dangerSrcCode(riskName);
                        String riskReason = data.getString(3);
                        String riskConsequence = data.getString(2);
                        String riskDesc = one.size() > 18 ? data.getString(18) : "";
                        //获取下一个危险源id
                        Long id = this.getNextId(_sId, count);
                        //修改sys_uid_sequence表 对应scyf_risk_source的id
                        dictMapper.setTabId("scyf_risk_source",id);
                        ++count;
                        if (!"".equals(riskName)) {
                            Integer nodeLevel=1;
                            //根据企业id 危险源类型 危险源名称 危险状态 节点层级 获取危险源对象
                            RiskSource riskSource =riskSourceMapper.getRiskSource(cId,riskDangerType,riskName,nodeLevel);
                            if (riskSource == null) {
                                //添加危险源
                                eds.add(this.buildRiskSource(id, -1L, id, dangerSrcCode, 1, 0, riskName, cId, riskDangerType, 0));
                            } else {
                                id = riskSource.getRiskDangerId();
                            }
                        }

                        Long pId = 0L;
                        Long riskId = 0L;
                        if (pId.equals(0L)) {
                            pId = id;
                        }
                        riskId = this.getNextId(_sId, count);
                        ++count;
                        //添加危险源
                        eds.add(this.buildRiskSource(riskId, pId, id, dangerSrcCode, 3, 1, "", cId, riskDangerType, dangerSrcLv));
                        //批量保存危险源
                        riskSourceMapper.saveBatch(eds);
                        //修改风险等级
                        this.updateLvAndIsLeaf(riskId, cId);
                        Risk risk = new Risk();
                        risk.setRiskId(riskId);
                        risk.setCompanyId(cId);
                        risk.setRiskDangerId(riskId);
                        risk.setRiskCode(riskCode);
                        risk.setRiskHramFactor(riskReason);
                        risk.setRiskConsequence(riskConsequence);
                        risk.setRiskTypeId((long)Constant.RISK_TYPE_CHANGGUI);
                        risk.setRiskDesc(riskDesc);
                        risk.setRiskLevel(dangerSrcLv);
                        risk.setCreatedBy(aId);
                        risk.setCreatedTime(new Date());
                        risk.setUpdatedBy(aId);
                        risk.setUpdatedTime(new Date());
                        risk.setLcL(newL);
                        risk.setLcC(newC);
                        risk.setLcD(newD);
                        risk.setTypeId(1);
                        risk.setGroupName(groupName);
                        risk.setGroupUser(groupUser);
                        risk.setDelFlag(0);
                        //保存风险
                        this.riskMapper.add(risk);
                        String positionTitle = one.size() > 16 ? data.getString(16) : "";
                        List<RiskCtrl> riskCtrls = new ArrayList();
                        if (positionTitle !=null &&!"".equals(positionTitle) && positionTitle.replace("，", ",").split(",").length > 0) {
                            String[] positionTitles = positionTitle.replace("，", ",").split(",");
                            for(int j = 0; j < positionTitles.length; ++j) {
                                RiskCtrl riskCtrl = new RiskCtrl();
                                //获取管控层级id
                                Long ctrlLevelId = this.ctrlLevelId(entRiskCtrlLevels, dangerSrcLv.toString());
                                riskCtrl.setRiskCtrlLevelId(ctrlLevelId);
                                Long positionId = null;
                                if (!StringUtils.isBlank(positionTitles[j])) {
                                    try {
                                        //添加岗位表信息 并返回岗位id
                                        positionId = this.getPositionId(positionList, positionTitles[j], cId, aId);
                                    } catch (Exception var47) {
                                        var47.printStackTrace();
                                    }
                                    riskCtrl.setRiskCtrlPositionId(positionId);
                                    riskCtrl.setCompanyId(cId);
                                    riskCtrl.setRiskId(riskId);
                                    riskCtrl.setCreatedBy(aId);
                                    riskCtrl.setCreatedTime(new Date());
                                    riskCtrl.setUpdatedBy(aId);
                                    riskCtrl.setUpdatedTime(new Date());
                                    riskCtrl.setDelFlag(0);
                                    boolean f = true;
                                    for(int l2 = 0; l2 < riskCtrls.size(); ++l2) {
                                        RiskCtrl e = (RiskCtrl)riskCtrls.get(l2);
                                        if (e.getRiskId().equals(riskId) && e.getRiskCtrlPositionId().equals(positionId) && e.getRiskCtrlLevelId().equals(ctrlLevelId)) {
                                            f = false;
                                            break;
                                        }
                                    }
                                    if (f) {
                                        riskCtrls.add(riskCtrl);
                                    }
                                }
                            }
                            if (riskCtrls.size() > 0) {
                                //批量添加风险管控配置表信息
                                this.riskCtrlMapper.saveBatch(riskCtrls);
                            }
                        }else{
                            RiskCtrl riskCtrl = new RiskCtrl();
                            if (!dangerSrcLv.toString().equals("")) {
                                riskCtrl.setRiskCtrlLevelId(this.ctrlLevelId(entRiskCtrlLevels, dangerSrcLv.toString()));
                            }
                            riskCtrl.setCompanyId(cId);
                            riskCtrl.setRiskId(riskId);
                            riskCtrl.setCreatedBy(aId);
                            riskCtrl.setRiskCtrlPositionId(-1L);
                            riskCtrl.setCreatedTime(new Date());
                            riskCtrl.setCreatedBy(aId);
                            riskCtrl.setUpdatedBy(aId);
                            riskCtrl.setUpdatedTime(new Date());
                            riskCtrl.setDelFlag(0);
                            //添加风险管控配置表信息
                            this.riskCtrlMapper.add(riskCtrl);
                        }
                        String riskMeasureType1 = data.getString(9);
                        String riskMeasureType2 = data.getString(10);
                        String riskMeasureType3 = data.getString(11);
                        String riskMeasureType4 = data.getString(13);
                        String riskMeasureType5 = data.getString(12);
                        List<RiskMeasure> list = new ArrayList();
                        //根据管控措施类型不同 返回风险管控措施信息
                        if (riskMeasureType1 != null && !"".equals(riskMeasureType1)) {
                            list = this.buildList(_sId,riskMeasureType1, 1, cId, riskId, aId, (List)list, ";", false);
                        }
                        if (riskMeasureType2 != null && !"".equals(riskMeasureType2)) {
                            list = this.buildList(_sId,riskMeasureType2, 2, cId, riskId, aId, (List)list, ";", false);
                        }

                        if (riskMeasureType3 != null && !"".equals(riskMeasureType3)) {
                            list = this.buildList(_sId,riskMeasureType3, 3, cId, riskId, aId, (List)list, ";", false);
                        }

                        if (riskMeasureType4 != null && !"".equals(riskMeasureType4)) {
                            list = this.buildList(_sId,riskMeasureType4, 4, cId, riskId, aId, (List)list, ";", false);
                        }

                        if (riskMeasureType5 != null && !"".equals(riskMeasureType5)) {
                            list = this.buildList(_sId,riskMeasureType5, 5, cId, riskId, aId, (List)list, ";", false);
                        }
                        if (((List)list).size() > 0) {
                            Long[] sId;
                            if (((List)list).size() > 1000) {
                                sId = new Long[]{dictService.getTabId("scyf_risk_source")};
                                List<RiskMeasure> _list = new ArrayList();

                                for(int j = 0; j < ((List)list).size(); ++j) {
                                    if (j % 1000 == 0) {
                                        //批量保存管控措施信息
                                        this.riskMeasureMapper.saveBatch(_list);
                                        _list = new ArrayList();
                                    }

                                    RiskMeasure entRiskMeasure = (RiskMeasure)((List)list).get(j);
                                    entRiskMeasure.setRiskMeasureId(sId[0]);
                                    _list.add(entRiskMeasure);
                                }
                            } else {
                                sId = new Long[]{dictService.getTabId("scyf_risk_source")};
                                for(int c2=0;c2<list.size();c2++){
                                    list.get(c2).setRiskMeasureId(sId[0]);
                                }
                                //批量保存管控措施信息
                                this.riskMeasureMapper.saveBatch(list);
                            }
                        }
                    }else if(type.equals("LS")){
                        int k = i + 1;
                        List<Object> one = (List)readAll.get(i);
                        JSONArray data = new JSONArray(one);
                        List<RiskSource> eds = new ArrayList();
                        String riskCode = data.getString(0);
                        if (StringUtils.isEmpty(riskCode)) {
                            throw new Exception( "第" + k + "行序号是空的，请填写！");
                        }

                        String riskName = data.getString(1);
                        if (StringUtils.isEmpty(riskName)) {
                            throw new Exception( "第" + k + "行作业单元是空的，请填写！");
                        }

                        String dangerSrcName = data.getString(3);
                        if (StringUtils.isEmpty(dangerSrcName)) {
                            throw new Exception("第" + k + "行序号为【" + riskCode + "】致险因素是空的，请填写！");
                        }


                        String l = data.getString(4);
                        if (StringUtils.isEmpty(l)) {
                            throw new Exception("第" + k + "行序号为【" + riskCode + "】可能性大小L是空的，请填写！");
                        }

                        String s = data.getString(5);
                        if (StringUtils.isEmpty(s)) {
                            throw new Exception("第" + k + "行序号为【" + riskCode + "】可能性大小S是空的，请填写！");
                        }

                        if (this.getOneEntRiskCode(cId, riskCode)) {
                            throw new Exception( "第" + k + "行序号【" + riskCode + "】已存在，请更换！");
                        }
                        String groupName = data.getString(15);
                        String groupUser = data.getString(17);
                        BigDecimal a1=new BigDecimal(6);
                        BigDecimal a2=new BigDecimal(8);
                        BigDecimal a3=new BigDecimal(12);
                        BigDecimal a4=new BigDecimal(15);
                        BigDecimal a5=new BigDecimal(16);
                        BigDecimal a6=new BigDecimal(20);
                        BigDecimal a7=new BigDecimal(25);
                        BigDecimal newL=new BigDecimal(l);
                        BigDecimal newS=new BigDecimal(s);
                        BigDecimal newR=newL.multiply(newS);
                        String riskLevel="";
                        if(newR.compareTo(a1) < 1) {
                            riskLevel="蓝";
                        }
                        if(newR.compareTo(a2) > -1 && newR.compareTo(a3) < 1){
                            riskLevel="黄";
                        }
                        if(newR.compareTo(a4) > -1 && newR.compareTo(a5) < 1){
                            riskLevel="橙";
                        }
                        if(newR.compareTo(a6) > -1 && newR.compareTo(a7) < 1){
                            riskLevel="红";
                        }

                            //根据风险等级名称 获取风险等级Code码
                        Integer dangerSrcLv = this.dangerSrcLv(riskLevel);
                        // 根据根危险源名称 获取根危险源Code码
                        String dangerSrcCode = this.dangerSrcCode(riskName);
                        String riskReason = data.getString(3);
                        String riskConsequence = data.getString(2);
                        String riskDesc = one.size() > 18 ? data.getString(18) : "";
                        //获取下一个危险源id
                        Long id = this.getNextId(_sId, count);
                        //修改sys_uid_sequence表 对应scyf_risk_source的id
                        dictMapper.setTabId("scyf_risk_source",id);
                        ++count;
                        if (!"".equals(riskName)) {
                            Integer nodeLevel=1;
                            //根据企业id 危险源类型 危险源名称 危险状态 节点层级 获取危险源对象
                            RiskSource riskSource =riskSourceMapper.getRiskSource(cId,riskDangerType,riskName,nodeLevel);
                            if (riskSource == null) {
                                //添加危险源
                                eds.add(this.buildRiskSource(id, -1L, id, dangerSrcCode, 1, 0, riskName, cId, riskDangerType, 0));
                            } else {
                                id = riskSource.getRiskDangerId();
                            }
                        }

                        Long pId = 0L;
                        Long riskId = 0L;
                        if (pId.equals(0L)) {
                            pId = id;
                        }
                        riskId = this.getNextId(_sId, count);
                        ++count;
                        //添加危险源
                        eds.add(this.buildRiskSource(riskId, pId, id, dangerSrcCode, 3, 1, "", cId, riskDangerType, dangerSrcLv));
                        //批量保存危险源
                        riskSourceMapper.saveBatch(eds);
                        //修改风险等级
                        this.updateLvAndIsLeaf(riskId, cId);
                        Risk risk = new Risk();
                        risk.setRiskId(riskId);
                        risk.setCompanyId(cId);
                        risk.setRiskDangerId(riskId);
                        risk.setRiskCode(riskCode);
                        risk.setRiskHramFactor(riskReason);
                        risk.setRiskConsequence(riskConsequence);
                        risk.setRiskTypeId((long)Constant.RISK_TYPE_CHANGGUI);
                        risk.setRiskDesc(riskDesc);
                        risk.setRiskLevel(dangerSrcLv);
                        risk.setCreatedBy(aId);
                        risk.setCreatedTime(new Date());
                        risk.setUpdatedBy(aId);
                        risk.setUpdatedTime(new Date());
                        risk.setLcL(newL);
                        risk.setLsS(newS);
                        risk.setLsR(newR);
                        risk.setTypeId(1);
                        risk.setGroupName(groupName);
                        risk.setGroupUser(groupUser);
                        risk.setDelFlag(0);
                        //保存风险
                        this.riskMapper.add(risk);
                        String positionTitle = one.size() > 16 ? data.getString(16) : "";
                        List<RiskCtrl> riskCtrls = new ArrayList();
                        if (!"".equals(positionTitle) && positionTitle.replace("，", ",").split(",").length > 0) {
                            String[] positionTitles = positionTitle.replace("，", ",").split(",");
                            for(int j = 0; j < positionTitles.length; ++j) {
                                RiskCtrl riskCtrl = new RiskCtrl();
                                //获取管控层级id
                                Long ctrlLevelId = this.ctrlLevelId(entRiskCtrlLevels, dangerSrcLv.toString());
                                riskCtrl.setRiskCtrlLevelId(ctrlLevelId);
                                Long positionId = null;
                                if (!StringUtils.isBlank(positionTitles[j])) {
                                    try {
                                        //添加岗位表信息 并返回岗位id
                                        positionId = this.getPositionId(positionList, positionTitles[j], cId, aId);
                                    } catch (Exception var47) {
                                        var47.printStackTrace();
                                    }
                                    riskCtrl.setRiskCtrlPositionId(positionId);
                                    riskCtrl.setCompanyId(cId);
                                    riskCtrl.setRiskId(riskId);
                                    riskCtrl.setCreatedBy(aId);
                                    riskCtrl.setCreatedTime(new Date());
                                    riskCtrl.setUpdatedBy(aId);
                                    riskCtrl.setUpdatedTime(new Date());
                                    riskCtrl.setDelFlag(0);
                                    boolean f = true;
                                    for(int l2 = 0; l2 < riskCtrls.size(); ++l2) {
                                        RiskCtrl e = (RiskCtrl)riskCtrls.get(l2);
                                        if (e.getRiskId().equals(riskId) && e.getRiskCtrlPositionId().equals(positionId) && e.getRiskCtrlLevelId().equals(ctrlLevelId)) {
                                            f = false;
                                            break;
                                        }
                                    }
                                    if (f) {
                                        riskCtrls.add(riskCtrl);
                                    }
                                }
                            }
                            if (riskCtrls.size() > 0) {
                                //批量添加风险管控配置表信息
                                this.riskCtrlMapper.saveBatch(riskCtrls);
                            }
                        }else{
                            RiskCtrl riskCtrl = new RiskCtrl();
                            if (!dangerSrcLv.toString().equals("")) {
                                riskCtrl.setRiskCtrlLevelId(this.ctrlLevelId(entRiskCtrlLevels, dangerSrcLv.toString()));
                            }
                            riskCtrl.setCompanyId(cId);
                            riskCtrl.setRiskId(riskId);
                            riskCtrl.setCreatedBy(aId);
                            riskCtrl.setRiskCtrlPositionId(-1L);
                            riskCtrl.setCreatedTime(new Date());
                            riskCtrl.setCreatedBy(aId);
                            riskCtrl.setUpdatedBy(aId);
                            riskCtrl.setUpdatedTime(new Date());
                            riskCtrl.setDelFlag(0);
                            //添加风险管控配置表信息
                            this.riskCtrlMapper.add(riskCtrl);
                        }
                        String riskMeasureType1 = data.getString(9);
                        String riskMeasureType2 = data.getString(10);
                        String riskMeasureType3 = data.getString(11);
                        String riskMeasureType4 = data.getString(13);
                        String riskMeasureType5 = data.getString(12);
                        List<RiskMeasure> list = new ArrayList();
                        //根据管控措施类型不同 返回风险管控措施信息
                        if (riskMeasureType1 != null && !"".equals(riskMeasureType1)) {
                            list = this.buildList(_sId,riskMeasureType1, 1, cId, riskId, aId, (List)list, ";", false);
                        }
                        if (riskMeasureType2 != null && !"".equals(riskMeasureType2)) {
                            list = this.buildList(_sId,riskMeasureType2, 2, cId, riskId, aId, (List)list, ";", false);
                        }

                        if (riskMeasureType3 != null && !"".equals(riskMeasureType3)) {
                            list = this.buildList(_sId,riskMeasureType3, 3, cId, riskId, aId, (List)list, ";", false);
                        }

                        if (riskMeasureType4 != null && !"".equals(riskMeasureType4)) {
                            list = this.buildList(_sId,riskMeasureType4, 4, cId, riskId, aId, (List)list, ";", false);
                        }

                        if (riskMeasureType5 != null && !"".equals(riskMeasureType5)) {
                            list = this.buildList(_sId,riskMeasureType5, 5, cId, riskId, aId, (List)list, ";", false);
                        }
                        if (((List)list).size() > 0) {
                            Long[] sId;
                            if (((List)list).size() > 1000) {
                                sId = new Long[]{dictService.getTabId("scyf_risk_source")};
                                List<RiskMeasure> _list = new ArrayList();

                                for(int j = 0; j < ((List)list).size(); ++j) {
                                    if (j % 1000 == 0) {
                                        //批量保存管控措施信息
                                        this.riskMeasureMapper.saveBatch(_list);
                                        _list = new ArrayList();
                                    }

                                    RiskMeasure entRiskMeasure = (RiskMeasure)((List)list).get(j);
                                    entRiskMeasure.setRiskMeasureId(sId[0]);
                                    _list.add(entRiskMeasure);
                                }
                            } else {
                                sId = new Long[]{dictService.getTabId("scyf_risk_source")};
                                for(int c2=0;c2<list.size();c2++){
                                    list.get(c2).setRiskMeasureId(sId[0]);
                                }
                                //批量保存管控措施信息
                                this.riskMeasureMapper.saveBatch(list);
                            }
                        }
                    }
                }
                return "1";
            }
        }
    }


    /**
     * 根据文件路径 获取excel文件内容 并导入数据LEC/LS到数据库 水利
     * yfh
     * 2020/08/27
     * @param filePath
     * @param json
     * @param ids
     * @return
     * @throws Exception
     */
    @Override
    public String importRiverNewExcel(String filePath, JSONObject json,Long ids,String type) throws Exception {
        //当前用户id
        Long aId = json.getLong("userId");
        Long cId = json.getLong("companyId");
        Integer riskDangerType = json.getInteger("riskDangerType");
        //获取excel表格内容
        List<List<Object>> readAll = readFirstSheet(filePath);
        List<Position> positionList = new ArrayList();
        //判断表格内是否有数据
        if (readAll.size() <= 2) {
            return "0";
        } else {
            Set<String> sets = new HashSet();
            List entRiskCtrlLevels;
            for(int i = 2; i < readAll.size(); ++i) {
                entRiskCtrlLevels = (List)readAll.get(i);
                JSONArray data = new JSONArray(entRiskCtrlLevels);
                String riskCode = data.getString(0);
                String dangerName = data.getString(3);
                sets.add(dangerName);
                sets.add(riskCode);
            }
            Long _sId = ids;
            //根据企业id 获取企业管控层级信息
            entRiskCtrlLevels = riskCtrlLevelService.getRiskCtrlLevelList(cId);
            if (entRiskCtrlLevels.isEmpty()) {
                throw new Exception("企业未配置【管控层级】信息");
            } else {
                int count = 0;
                // 循环读取excel表格每行内容 并插入到数据库
                for(int i = 2; i < readAll.size(); ++i) {
                    if(type.equals("LEC")){
                        int k = i + 1;
                        List<Object> one = (List)readAll.get(i);
                        JSONArray data = new JSONArray(one);
                        List<RiskSource> eds = new ArrayList();
                        String riskCode = data.getString(0);
                        if (StringUtils.isEmpty(riskCode)) {
                            throw new Exception( "第" + k + "行编号是空的，请填写！");
                        }

                        String dangerSrcName = data.getString(2);
                        if (StringUtils.isEmpty(dangerSrcName)) {
                            throw new Exception( "第" + k + "行作业风险点名称是空的，请填写！");
                        }

                        String riskName = data.getString(4);
                        if (StringUtils.isEmpty(riskName)) {
                            throw new Exception( "第" + k + "行检查项名称是空的，请填写！");
                        }


                        String l = data.getString(13);
                        if (StringUtils.isEmpty(l)) {
                            throw new Exception("第" + k + "行编号为【" + riskCode + "】风险评估L是空的，请填写！");
                        }

                        String e = data.getString(14);
                        if (StringUtils.isEmpty(e)) {
                            throw new Exception("第" + k + "行编号为【" + riskCode + "】风险评估E是空的，请填写！");
                        }

                        String c = data.getString(15);
                        if (StringUtils.isEmpty(c)) {
                            throw new Exception("第" + k + "行编号为【" + riskCode + "】风险评估C是空的，请填写！");
                        }

                        String controPos = data.getString(20);
                        if (StringUtils.isEmpty(controPos)) {
                            throw new Exception("第" + k + "行编号为【" + riskCode + "】管控岗位是空的，请填写！");
                        }

                        if (this.getOneEntRiskCode(cId, riskCode)) {
                            throw new Exception( "第" + k + "行编号【" + riskCode + "】已存在，请更换！");
                        }
                        String groupName = data.getString(19);
                        String groupUser = data.getString(21);
                        BigDecimal a1=new BigDecimal(70);
                        BigDecimal a2=new BigDecimal(160);
                        BigDecimal a3=new BigDecimal(320);
                        BigDecimal newL=new BigDecimal(l);
                        BigDecimal newE=new BigDecimal(e);
                        BigDecimal newC=new BigDecimal(c);
                        BigDecimal newD=newL.multiply(newE).multiply(newC);
                        String riskLevel="";
                        if(newD.compareTo(a1) < 1){
                            riskLevel="蓝";
                        }
                        if(newD.compareTo(a1) == 1 && newD.compareTo(a2) < 1){
                            riskLevel="黄";
                        }
                        if(newD.compareTo(a2) == 1 && newD.compareTo(a3) < 1){
                            riskLevel="橙";
                        }
                        if(newD.compareTo(a3) == 1){
                            riskLevel="红";
                        }
                        //根据风险等级名称 获取风险等级Code码
                        Integer dangerSrcLv = this.dangerSrcLv(riskLevel);
                        // 根据根危险源名称 获取根危险源Code码
                        String dangerSrcCode = this.dangerSrcCode(dangerSrcName);
                        String riskReason = data.getString(5);
                        String riskConsequence = data.getString(6);
                        String riskType = data.getString(1);
                        String riskDesc = one.size() > 22 ? data.getString(22) : "";
                        //获取下一个危险源id
                        Long id = this.getNextId(_sId, count);
                        //修改sys_uid_sequence表 对应scyf_risk_source的id
                        dictMapper.setTabId("scyf_risk_source",id);
                        ++count;
                        if (!"".equals(dangerSrcName)) {
                            Integer nodeLevel=1;
                            //根据企业id 危险源类型 危险源名称 危险状态 节点层级 获取危险源对象
                            RiskSource riskSource =riskSourceMapper.getRiskSource(cId,riskDangerType,dangerSrcName,nodeLevel);
                            if (riskSource == null) {
                                //添加危险源
                                eds.add(this.buildRiskSource(id, -1L, id, dangerSrcCode, 1, 0, dangerSrcName, cId, riskDangerType, 0));
                            } else {
                                id = riskSource.getRiskDangerId();
                            }
                        }

                        Long pId = 0L;

                        Long riskId = 0L;

                        if (pId.equals(0L)) {
                            pId = id;
                        }
                        riskId = this.getNextId(_sId, count);
                        ++count;
                        //添加危险源
                        eds.add(this.buildRiskSource(riskId, pId, id, dangerSrcCode, 3, 1, riskName, cId, riskDangerType, dangerSrcLv));
                        //批量保存危险源
                        riskSourceMapper.saveBatch(eds);
                        //修改风险等级
                        this.updateLvAndIsLeaf(riskId, cId);
                        Risk risk = new Risk();
                        risk.setRiskId(riskId);
                        risk.setCompanyId(cId);
                        risk.setRiskType(riskType);
                        risk.setRiskName(riskName);
                        risk.setRiskDangerId(riskId);
                        risk.setRiskCode(riskCode);
                        risk.setRiskHramFactor(riskReason);
                        risk.setRiskConsequence(riskConsequence);
                        risk.setRiskTypeId((long)Constant.RISK_TYPE_CHANGGUI);
                        risk.setRiskDesc(riskDesc);
                        risk.setRiskLevel(dangerSrcLv);
                        risk.setCreatedBy(aId);
                        risk.setCreatedTime(new Date());
                        risk.setUpdatedBy(aId);
                        risk.setUpdatedTime(new Date());
                        risk.setLecL(newL);
                        risk.setLecE(newE);
                        risk.setLecC(newC);
                        risk.setLecD(newD);
                        risk.setTypeId(0);
                        risk.setGroupName(groupName);
                        risk.setGroupUser(groupUser);
                        risk.setDelFlag(0);
                        //保存风险
                        this.riskMapper.add(risk);
                        String positionTitle = one.size() > 20 ? data.getString(20) : "";
                        List<RiskCtrl> riskCtrls = new ArrayList();
                        if (!"".equals(positionTitle) && positionTitle.replace("，", ",").split(",").length > 0) {
                            String[] positionTitles = positionTitle.replace("，", ",").split(",");
                            for(int j = 0; j < positionTitles.length; ++j) {
                                RiskCtrl riskCtrl = new RiskCtrl();
                                //获取管控层级id
                                Long ctrlLevelId = this.ctrlLevelId(entRiskCtrlLevels, dangerSrcLv.toString());
                                riskCtrl.setRiskCtrlLevelId(ctrlLevelId);
                                Long positionId = null;
                                if (!StringUtils.isBlank(positionTitles[j])) {
                                    try {
                                        //添加岗位表信息 并返回岗位id
                                        positionId = this.getPositionId(positionList, positionTitles[j], cId, aId);
                                    } catch (Exception var47) {
                                        var47.printStackTrace();
                                    }
                                    riskCtrl.setRiskCtrlPositionId(positionId);
                                    riskCtrl.setCompanyId(cId);
                                    riskCtrl.setRiskId(riskId);
                                    riskCtrl.setCreatedBy(aId);
                                    riskCtrl.setCreatedTime(new Date());
                                    riskCtrl.setUpdatedBy(aId);
                                    riskCtrl.setUpdatedTime(new Date());
                                    riskCtrl.setDelFlag(0);
                                    boolean f = true;
                                    for(int l1 = 0; l1 < riskCtrls.size(); ++l1) {
                                        RiskCtrl e1 = (RiskCtrl)riskCtrls.get(l1);
                                        if (e1.getRiskId().equals(riskId) && e1.getRiskCtrlPositionId().equals(positionId) && e1.getRiskCtrlLevelId().equals(ctrlLevelId)) {
                                            f = false;
                                            break;
                                        }
                                    }
                                    if (f) {
                                        riskCtrls.add(riskCtrl);
                                    }
                                }
                            }
                            if (riskCtrls.size() > 0) {
                                //批量添加风险管控配置表信息
                                this.riskCtrlMapper.saveBatch(riskCtrls);
                            }
                        }else{
                            RiskCtrl riskCtrl = new RiskCtrl();
                            if (!dangerSrcLv.toString().equals("")) {
                                riskCtrl.setRiskCtrlLevelId(this.ctrlLevelId(entRiskCtrlLevels, dangerSrcLv.toString()));
                            }
                            riskCtrl.setCompanyId(cId);
                            riskCtrl.setRiskId(riskId);
                            riskCtrl.setCreatedBy(aId);
                            riskCtrl.setRiskCtrlPositionId(-1L);
                            riskCtrl.setCreatedTime(new Date());
                            riskCtrl.setCreatedBy(aId);
                            riskCtrl.setUpdatedBy(aId);
                            riskCtrl.setUpdatedTime(new Date());
                            riskCtrl.setDelFlag(0);
                            //添加风险管控配置表信息
                            this.riskCtrlMapper.add(riskCtrl);
                        }
                        String riskMeasureType1 = data.getString(7);
                        String riskMeasureType2 = data.getString(8);
                        String riskMeasureType3 = data.getString(9);
                        String riskMeasureType4 = data.getString(11);
                        String riskMeasureType5 = data.getString(10);
                        List<RiskMeasure> list = new ArrayList();
                        //根据管控措施类型不同 返回风险管控措施信息
                        if (riskMeasureType1 != null && !"".equals(riskMeasureType1)) {
                            list = this.buildList(_sId,riskMeasureType1, 1, cId, riskId, aId, (List)list, ";", false);
                        }
                        if (riskMeasureType2 != null && !"".equals(riskMeasureType2)) {
                            list = this.buildList(_sId,riskMeasureType2, 2, cId, riskId, aId, (List)list, ";", false);
                        }

                        if (riskMeasureType3 != null && !"".equals(riskMeasureType3)) {
                            list = this.buildList(_sId,riskMeasureType3, 3, cId, riskId, aId, (List)list, ";", false);
                        }

                        if (riskMeasureType4 != null && !"".equals(riskMeasureType4)) {
                            list = this.buildList(_sId,riskMeasureType4, 4, cId, riskId, aId, (List)list, ";", false);
                        }

                        if (riskMeasureType5 != null && !"".equals(riskMeasureType5)) {
                            list = this.buildList(_sId,riskMeasureType5, 5, cId, riskId, aId, (List)list, ";", false);
                        }
                        if (((List)list).size() > 0) {
                            Long[] sId;
                            if (((List)list).size() > 1000) {
                                sId = new Long[]{dictService.getTabId("scyf_risk_source")};
                                List<RiskMeasure> _list = new ArrayList();

                                for(int j = 0; j < ((List)list).size(); ++j) {
                                    if (j % 1000 == 0) {
                                        //批量保存管控措施信息
                                        this.riskMeasureMapper.saveBatch(_list);
                                        _list = new ArrayList();
                                    }

                                    RiskMeasure entRiskMeasure = (RiskMeasure)((List)list).get(j);
                                    entRiskMeasure.setRiskMeasureId(sId[0]);
                                    _list.add(entRiskMeasure);
                                }
                            } else {
                                sId = new Long[]{dictService.getTabId("scyf_risk_source")};
                                for(int c1=0;c1<list.size();c1++){
                                    list.get(c1).setRiskMeasureId(sId[0]);
                                }
                                //批量保存管控措施信息
                                this.riskMeasureMapper.saveBatch(list);
                            }
                        }
                    }else if(type.equals("LS")){
                        int k = i + 1;
                        List<Object> one = (List)readAll.get(i);
                        JSONArray data = new JSONArray(one);
                        List<RiskSource> eds = new ArrayList();
                        String riskCode = data.getString(0);
                        if (StringUtils.isEmpty(riskCode)) {
                            throw new Exception( "第" + k + "行编号是空的，请填写！");
                        }

                        String dangerSrcName = data.getString(2);
                        if (StringUtils.isEmpty(dangerSrcName)) {
                            throw new Exception( "第" + k + "行作业风险点名称是空的，请填写！");
                        }

                        String riskName = data.getString(4);
                        if (StringUtils.isEmpty(riskName)) {
                            throw new Exception( "第" + k + "行检查项名称是空的，请填写！");
                        }

                        String l = data.getString(13);
                        if (StringUtils.isEmpty(l)) {
                            throw new Exception("第" + k + "行编号为【" + riskCode + "】风险评估L是空的，请填写！");
                        }

                        String s = data.getString(14);
                        if (StringUtils.isEmpty(s)) {
                            throw new Exception("第" + k + "行编号为【" + riskCode + "】风险评估S是空的，请填写！");
                        }

                        String controPos = data.getString(19);
                        if (StringUtils.isEmpty(controPos)) {
                            throw new Exception("第" + k + "行编号为【" + riskCode + "】管控岗位是空的，请填写！");
                        }

                        if (this.getOneEntRiskCode(cId, riskCode)) {
                            throw new Exception( "第" + k + "行编号【" + riskCode + "】已存在，请更换！");
                        }
                        String groupName = data.getString(18);
                        String groupUser = data.getString(20);
                        BigDecimal a1=new BigDecimal(70);
                        BigDecimal a2=new BigDecimal(160);
                        BigDecimal a3=new BigDecimal(320);
                        BigDecimal newL=new BigDecimal(l);
                        BigDecimal newS=new BigDecimal(s);
                        BigDecimal newR=newL.multiply(newS);
                        String riskLevel="";
                        if(newR.compareTo(a1) < 1){
                            riskLevel="蓝";
                        }
                        if(newR.compareTo(a1) == 1 && newR.compareTo(a2) < 1){
                            riskLevel="黄";
                        }
                        if(newR.compareTo(a2) == 1 && newR.compareTo(a3) < 1){
                            riskLevel="橙";
                        }
                        if(newR.compareTo(a3) == 1){
                            riskLevel="红";
                        }
                        //根据风险等级名称 获取风险等级Code码
                        Integer dangerSrcLv = this.dangerSrcLv(riskLevel);
                        // 根据根危险源名称 获取根危险源Code码
                        String dangerSrcCode = this.dangerSrcCode(dangerSrcName);
                        String riskType = data.getString(1);
                        String riskReason = data.getString(5);
                        String riskConsequence = data.getString(6);
                        String riskDesc = one.size() > 21 ? data.getString(21) : "";
                        //获取下一个危险源id
                        Long id = this.getNextId(_sId, count);
                        //修改sys_uid_sequence表 对应scyf_risk_source的id
                        dictMapper.setTabId("scyf_risk_source",id);
                        ++count;
                        if (!"".equals(dangerSrcName)) {
                            Integer nodeLevel=1;
                            //根据企业id 危险源类型 危险源名称 危险状态 节点层级 获取危险源对象
                            RiskSource riskSource =riskSourceMapper.getRiskSource(cId,riskDangerType,dangerSrcName,nodeLevel);
                            if (riskSource == null) {
                                //添加危险源
                                eds.add(this.buildRiskSource(id, -1L, id, dangerSrcCode, 1, 0, dangerSrcName, cId, riskDangerType, 0));
                            } else {
                                id = riskSource.getRiskDangerId();
                            }
                        }

                        Long pId = 0L;
                        Long riskId = 0L;
                        if (pId.equals(0L)) {
                            pId = id;
                        }
                        riskId = this.getNextId(_sId, count);
                        ++count;
                        //添加危险源
                        eds.add(this.buildRiskSource(riskId, pId, id, dangerSrcCode, 3, 1, riskName, cId, riskDangerType, dangerSrcLv));
                        //批量保存危险源
                        riskSourceMapper.saveBatch(eds);
                        //修改风险等级
                        this.updateLvAndIsLeaf(riskId, cId);
                        Risk risk = new Risk();
                        risk.setRiskId(riskId);
                        risk.setCompanyId(cId);
                        risk.setRiskDangerId(riskId);
                        risk.setRiskCode(riskCode);
                        risk.setRiskType(riskType);
                        risk.setRiskName(riskName);
                        risk.setRiskHramFactor(riskReason);
                        risk.setRiskConsequence(riskConsequence);
                        risk.setRiskTypeId((long)Constant.RISK_TYPE_CHANGGUI);
                        risk.setRiskDesc(riskDesc);
                        risk.setRiskLevel(dangerSrcLv);
                        risk.setCreatedBy(aId);
                        risk.setCreatedTime(new Date());
                        risk.setUpdatedBy(aId);
                        risk.setUpdatedTime(new Date());
                        risk.setLsL(newL);
                        risk.setLsS(newS);
                        risk.setLsR(newR);
                        risk.setTypeId(2);
                        risk.setGroupName(groupName);
                        risk.setGroupUser(groupUser);
                        risk.setDelFlag(0);
                        //保存风险
                        this.riskMapper.add(risk);
                        String positionTitle = one.size() > 19 ? data.getString(19) : "";
                        List<RiskCtrl> riskCtrls = new ArrayList();
                        if (!"".equals(positionTitle) && positionTitle.replace("，", ",").split(",").length > 0) {
                            String[] positionTitles = positionTitle.replace("，", ",").split(",");
                            for(int j = 0; j < positionTitles.length; ++j) {
                                RiskCtrl riskCtrl = new RiskCtrl();
                                //获取管控层级id
                                Long ctrlLevelId = this.ctrlLevelId(entRiskCtrlLevels, dangerSrcLv.toString());
                                riskCtrl.setRiskCtrlLevelId(ctrlLevelId);
                                Long positionId = null;
                                if (!StringUtils.isBlank(positionTitles[j])) {
                                    try {
                                        //添加岗位表信息 并返回岗位id
                                        positionId = this.getPositionId(positionList, positionTitles[j], cId, aId);
                                    } catch (Exception var47) {
                                        var47.printStackTrace();
                                    }
                                    riskCtrl.setRiskCtrlPositionId(positionId);
                                    riskCtrl.setCompanyId(cId);
                                    riskCtrl.setRiskId(riskId);
                                    riskCtrl.setCreatedBy(aId);
                                    riskCtrl.setCreatedTime(new Date());
                                    riskCtrl.setUpdatedBy(aId);
                                    riskCtrl.setUpdatedTime(new Date());
                                    riskCtrl.setDelFlag(0);
                                    boolean f = true;
                                    for(int l2 = 0; l2 < riskCtrls.size(); ++l2) {
                                        RiskCtrl e = (RiskCtrl)riskCtrls.get(l2);
                                        if (e.getRiskId().equals(riskId) && e.getRiskCtrlPositionId().equals(positionId) && e.getRiskCtrlLevelId().equals(ctrlLevelId)) {
                                            f = false;
                                            break;
                                        }
                                    }
                                    if (f) {
                                        riskCtrls.add(riskCtrl);
                                    }
                                }
                            }
                            if (riskCtrls.size() > 0) {
                                //批量添加风险管控配置表信息
                                this.riskCtrlMapper.saveBatch(riskCtrls);
                            }
                        }else{
                            RiskCtrl riskCtrl = new RiskCtrl();
                            if (!dangerSrcLv.toString().equals("")) {
                                riskCtrl.setRiskCtrlLevelId(this.ctrlLevelId(entRiskCtrlLevels, dangerSrcLv.toString()));
                            }
                            riskCtrl.setCompanyId(cId);
                            riskCtrl.setRiskId(riskId);
                            riskCtrl.setCreatedBy(aId);
                            riskCtrl.setRiskCtrlPositionId(-1L);
                            riskCtrl.setCreatedTime(new Date());
                            riskCtrl.setCreatedBy(aId);
                            riskCtrl.setUpdatedBy(aId);
                            riskCtrl.setUpdatedTime(new Date());
                            riskCtrl.setDelFlag(0);
                            //添加风险管控配置表信息
                            this.riskCtrlMapper.add(riskCtrl);
                        }
                        String riskMeasureType1 = data.getString(7);
                        String riskMeasureType2 = data.getString(8);
                        String riskMeasureType3 = data.getString(9);
                        String riskMeasureType4 = data.getString(11);
                        String riskMeasureType5 = data.getString(10);
                        List<RiskMeasure> list = new ArrayList();
                        //根据管控措施类型不同 返回风险管控措施信息
                        if (riskMeasureType1 != null && !"".equals(riskMeasureType1)) {
                            list = this.buildList(_sId,riskMeasureType1, 1, cId, riskId, aId, (List)list, ";", false);
                        }
                        if (riskMeasureType2 != null && !"".equals(riskMeasureType2)) {
                            list = this.buildList(_sId,riskMeasureType2, 2, cId, riskId, aId, (List)list, ";", false);
                        }

                        if (riskMeasureType3 != null && !"".equals(riskMeasureType3)) {
                            list = this.buildList(_sId,riskMeasureType3, 3, cId, riskId, aId, (List)list, ";", false);
                        }

                        if (riskMeasureType4 != null && !"".equals(riskMeasureType4)) {
                            list = this.buildList(_sId,riskMeasureType4, 4, cId, riskId, aId, (List)list, ";", false);
                        }

                        if (riskMeasureType5 != null && !"".equals(riskMeasureType5)) {
                            list = this.buildList(_sId,riskMeasureType5, 5, cId, riskId, aId, (List)list, ";", false);
                        }
                        if (((List)list).size() > 0) {
                            Long[] sId;
                            if (((List)list).size() > 1000) {
                                sId = new Long[]{dictService.getTabId("scyf_risk_source")};
                                List<RiskMeasure> _list = new ArrayList();

                                for(int j = 0; j < ((List)list).size(); ++j) {
                                    if (j % 1000 == 0) {
                                        //批量保存管控措施信息
                                        this.riskMeasureMapper.saveBatch(_list);
                                        _list = new ArrayList();
                                    }

                                    RiskMeasure entRiskMeasure = (RiskMeasure)((List)list).get(j);
                                    entRiskMeasure.setRiskMeasureId(sId[0]);
                                    _list.add(entRiskMeasure);
                                }
                            } else {
                                sId = new Long[]{dictService.getTabId("scyf_risk_source")};
                                for(int c2=0;c2<list.size();c2++){
                                    list.get(c2).setRiskMeasureId(sId[0]);
                                }
                                //批量保存管控措施信息
                                this.riskMeasureMapper.saveBatch(list);
                            }
                        }
                    }
                }
                return "1";
            }
        }
    }


    /**
     * 根据文件路径 获取excel文件内容 并导入数据A评价数据到数据库 高速
     * yfh
     * 2020/09/17
     * @param filePath
     * @param json
     * @param ids
     * @return
     * @throws Exception
     */
    @Override
    public String importNewAExcel(String filePath, JSONObject json,Long ids) throws Exception {
        //当前用户id
        Long aId = json.getLong("userId");
        Long cId = json.getLong("companyId");
        Integer riskDangerType = json.getInteger("riskDangerType");
        //获取excel表格中第一个sheet内容
        List<List<Object>> readAll = readFirstSheet(filePath);
        //判断表格内是否有数据
        if (readAll.size() <= 4) {
            return "0";
        } else {
            Set<String> sets = new HashSet();
            List entRiskCtrlLevels;
            for(int i = 2; i < readAll.size(); ++i) {
                entRiskCtrlLevels = (List)readAll.get(i);
                JSONArray data = new JSONArray(entRiskCtrlLevels);
                String riskCode = data.getString(0);
                String dangerName = data.getString(2);
                sets.add(dangerName);
                sets.add(riskCode);
            }
            //根据企业id 获取企业管控层级信息
            entRiskCtrlLevels = riskCtrlLevelService.getRiskCtrlLevelList(cId);
            if (entRiskCtrlLevels.isEmpty()) {
                throw new Exception("企业未配置【管控层级】信息");
            } else {
                String dealType = json.getString("dealType");
                if(null == dealType || "0".equals(dealType)){
                    dealExcleA7(readAll, entRiskCtrlLevels, ids, aId, cId, riskDangerType);
                }else if("1".equals(dealType )){
                    //道路
                    dealExcleARoad(readAll, entRiskCtrlLevels, ids, aId, cId, riskDangerType);
                }else if("2".equals(dealType )){
                    //桥梁
                    dealExcleABridge(readAll, entRiskCtrlLevels, ids, aId, cId, riskDangerType);
                }else if("3".equals(dealType )){
                    // 隧道
                    dealExcleATunnel(readAll, entRiskCtrlLevels, ids, aId, cId, riskDangerType);
                }
                return "1";
            }
        }
    }

    private List<List<Object>> readFirstSheet(String filePath){
        List<List<Object>> readSheetAll = new ArrayList<>();
        try {
            File file = new File(filePath);
            XSSFWorkbook wb = new XSSFWorkbook(new FileInputStream(file));
            XSSFSheet sheet = wb.getSheetAt(0);
            // 获取行数
            int rows = sheet.getPhysicalNumberOfRows();
            int maxCellsNumber = 0;
            for(int i=0; i<rows; i++){
                XSSFRow curRow = sheet.getRow(i);
                if(null == curRow ){continue;}
                // 获取最后的列数
                int colums = curRow.getPhysicalNumberOfCells();
                if(0 == colums ){continue;}
                int lastCellsNumber = curRow.getLastCellNum();

                List<Object> columList = new ArrayList<>();
                for(int j =0 ;j<lastCellsNumber; j++ ){
                    // 获取单元格
                    XSSFCell curRowCell = curRow.getCell(j);
                   if(null == curRowCell ){ columList.add(""); continue; }
                    // 获取单元格类型
                    CellType cellTypeEnum = curRowCell.getCellTypeEnum();
                    columList.add(getCellValue( curRowCell, cellTypeEnum));
                }
                if(maxCellsNumber <= lastCellsNumber ){maxCellsNumber= lastCellsNumber ;}else{
                    for(int k=0; k< maxCellsNumber-lastCellsNumber ;k++ ){
                        columList.add("");
                    }
                }
                readSheetAll.add(columList);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return readSheetAll;
    }
    private Object getCellValue(XSSFCell curRowCell , CellType cellType){
        Object value = null ;
        // 获取单元格类型
        switch (cellType){
            case NUMERIC:
                value = getNumericValue(curRowCell) ;
                break;
            case BOOLEAN:
                value = curRowCell.getBooleanCellValue() ;
                break;
            case BLANK:
                value =  "";
                break;
            case FORMULA:
                value = getCellValue(curRowCell, curRowCell.getCachedFormulaResultTypeEnum());
                break;
            default:
                value = curRowCell.getStringCellValue() ;
                break ;
        }
        return null == value?"":value ;
    }

    private  Object getNumericValue(XSSFCell cell) {
        double value = cell.getNumericCellValue();
        CellStyle style = cell.getCellStyle();
        if (null == style) {
            return value;
        } else {
            short formatIndex = style.getDataFormat();
            String format = style.getDataFormatString();
            if (isDateType(formatIndex, format)) {
                return DateUtil.date(cell.getDateCellValue());
            } else {
                if (null != format && format.indexOf(46) < 0) {
                    long longPart = (long)value;
                    if ((double)longPart == value) {
                        return longPart;
                    }
                }
                return value;
            }
        }
    }

    private  boolean isDateType(int formatIndex, String format) {
        if (formatIndex != 14 && formatIndex != 31 && formatIndex != 57 && formatIndex != 58 && formatIndex != 20 && formatIndex != 32) {
            return org.apache.poi.ss.usermodel.DateUtil.isADateFormat(formatIndex, format);
        } else {
            return true;
        }
    }
    private void dealExcleA7(List<List<Object>> readAll, List entRiskCtrlLevels, Long ids,  Long aId, Long cId, Integer riskDangerType) throws Exception{
        int count = 0;
        Long _sId = ids;
        // 循环读取excel表格每行内容 并插入到数据库
        for(int i = 4; i < readAll.size(); ++i) {
            int k = i + 1;
            List<Object> one = readAll.get(i);
            JSONArray data = new JSONArray(one);
            List<RiskSource> eds = new ArrayList();
            String riskCode = data.getString(0);
            if (StringUtils.isEmpty(riskCode)) {
                throw new Exception( "第" + k + "行序号是空的，请填写！");
            }
            String riskName = data.getString(1);
            if (StringUtils.isEmpty(riskName)) {
                throw new Exception( "第" + k + "行作业单元是空的，请填写！");
            }

            String riskLevel = data.getString(32);
            if (StringUtils.isEmpty(riskLevel)) {
                throw new Exception( "第" + k + "行等级是空的，请填写！");
            }

            if (this.getOneEntRiskCode(cId, riskCode)) {
                throw new Exception( "第" + k + "行序号【" + riskCode + "】已存在，请更换！");
            }
            String groupName = data.getString(35);
            String groupUser = data.getString(36);
            String A11 = data.getString(7);
            String A12 = data.getString(8);
            String A13 = data.getString(9);
            String tA1 = data.getString(10);
            String A21 = data.getString(11);
            String A22 = data.getString(12);
            String tA2 = data.getString(13);
            String A31 = data.getString(14);
            String A32 = data.getString(15);
            String A33 = data.getString(16);
            String tA3 = data.getString(17);
            String A41 = data.getString(18);
            String tA4 = data.getString(19);
            String A51 = data.getString(20);
            String A52 = data.getString(21);
            String tA5 = data.getString(22);
            String A61 = data.getString(23);
            String A62 = data.getString(24);
            String tA6 = data.getString(25);
            String A71 = data.getString(26);
            String A72 = data.getString(27);
            String A73 = data.getString(28);
            String A74 = data.getString(29);
            String tA7 = data.getString(30);
            String tA = data.getString(31);
            //根据风险等级名称 获取风险等级Code码
            Integer dangerSrcLv = this.dangerSrcLv(riskLevel);
            // 根据根危险源名称 获取根危险源Code码
            String dangerSrcCode = this.dangerSrcCode(riskName);
            String riskConsequence = data.getString(2);
            //获取下一个危险源id
            Long id = this.getNextId(_sId, count);
            //修改sys_uid_sequence表 对应scyf_risk_source的id
            dictMapper.setTabId("scyf_risk_source",id);
            ++count;
            if (!"".equals(riskName)) {
                Integer nodeLevel=1;
                //根据企业id 危险源类型 危险源名称 危险状态 节点层级 获取危险源对象
                RiskSource riskSource =riskSourceMapper.getRiskSource(cId,riskDangerType,riskName,nodeLevel);
                if (riskSource == null) {
                    //添加危险源
                    eds.add(this.buildRiskSource(id, -1L, id, dangerSrcCode, 1, 0, riskName, cId, riskDangerType, 0));
                } else {
                    id = riskSource.getRiskDangerId();
                }
            }

            Long pId = 0L;

            Long riskId = 0L;

            if (pId.equals(0L)) {
                pId = id;
            }
            riskId = this.getNextId(_sId, count);
            ++count;
            //添加危险源
            eds.add(this.buildRiskSource(riskId, pId, id, dangerSrcCode, 3, 1, "", cId, riskDangerType, dangerSrcLv));
            //批量保存危险源
            riskSourceMapper.saveBatch(eds);
            //修改风险等级
            this.updateLvAndIsLeaf(riskId, cId);
            Risk risk = new Risk();
            risk.setRiskId(riskId);
            risk.setCompanyId(cId);
            risk.setRiskDangerId(riskId);
            risk.setRiskCode(riskCode);
            risk.setRiskConsequence(riskConsequence);
            risk.setRiskTypeId((long)Constant.RISK_TYPE_CHANGGUI);
            risk.setRiskLevel(dangerSrcLv);
            risk.setCreatedBy(aId);
            risk.setCreatedTime(new Date());
            risk.setUpdatedBy(aId);
            risk.setUpdatedTime(new Date());
            risk.setA1(A11+","+A12+","+A13+","+tA1);
            risk.setA2(A21+","+A22+","+tA2);
            risk.setA3(A31+","+A32+","+A33+","+tA3);
            risk.setA4(A41+","+tA4);
            risk.setA5(A51+","+A52+","+tA5);
            risk.setA6(A61+","+A62+","+tA6);
            risk.setA7(A71+","+A72+","+A73+","+A74+","+tA7);
            risk.setTa(tA);
            risk.setTypeId(6);
            risk.setGroupName(groupName);
            risk.setGroupUser(groupUser);
            risk.setDelFlag(0);
            //保存风险
            this.riskMapper.add(risk);
            String positionTitle = data.getString(34);
            dealCtrlPositionTitle(positionTitle, entRiskCtrlLevels, aId,cId,dangerSrcLv, riskId);

            String riskMeasureType1 = data.getString(3);
            List<RiskMeasure> list = new ArrayList();
            //根据管控措施类型不同 返回风险管控措施信息
            if (riskMeasureType1 != null && !"".equals(riskMeasureType1)) {
                list = this.buildList(_sId,riskMeasureType1, 1, cId, riskId, aId, (List)list, ";", false);
            }
            if (((List)list).size() > 0) {
                Long[] sId;
                if (((List)list).size() > 1000) {
                    sId = new Long[]{dictService.getTabId("scyf_risk_source")};
                    List<RiskMeasure> _list = new ArrayList();

                    for(int j = 0; j < ((List)list).size(); ++j) {
                        if (j % 1000 == 0) {
                            //批量保存管控措施信息
                            this.riskMeasureMapper.saveBatch(_list);
                            _list = new ArrayList();
                        }

                        RiskMeasure entRiskMeasure = (RiskMeasure)((List)list).get(j);
                        entRiskMeasure.setRiskMeasureId(sId[0]);
                        _list.add(entRiskMeasure);
                    }
                } else {
                    sId = new Long[]{dictService.getTabId("scyf_risk_source")};
                    for(int c1=0;c1<list.size();c1++){
                        list.get(c1).setRiskMeasureId(sId[0]);
                    }
                    //批量保存管控措施信息
                    this.riskMeasureMapper.saveBatch(list);
                }
            }
        }
    }

    private void dealCtrlPositionTitle(String positionTitle,  List entRiskCtrlLevels, Long aId, Long cId, Integer dangerSrcLv, Long riskId){
        List<Position> positionList = new ArrayList();
        List<RiskCtrl> riskCtrls = new ArrayList();
        if (null != positionTitle && !"".equals(positionTitle) && positionTitle.replace("，", ",").split(",").length > 0) {
            String[] positionTitles = positionTitle.replace("，", ",").split(",");
            for(int j = 0; j < positionTitles.length; ++j) {
                RiskCtrl riskCtrl = new RiskCtrl();
                //获取管控层级id
                Long ctrlLevelId = this.ctrlLevelId(entRiskCtrlLevels, dangerSrcLv.toString());
                riskCtrl.setRiskCtrlLevelId(ctrlLevelId);
                Long positionId = null;
                if (!StringUtils.isBlank(positionTitles[j])) {
                    try {
                        //添加岗位表信息 并返回岗位id
                        positionId = this.getPositionId(positionList, positionTitles[j], cId, aId);
                    } catch (Exception var47) {
                        var47.printStackTrace();
                    }
                    riskCtrl.setRiskCtrlPositionId(positionId);
                    riskCtrl.setCompanyId(cId);
                    riskCtrl.setRiskId(riskId);
                    riskCtrl.setCreatedBy(aId);
                    riskCtrl.setCreatedTime(new Date());
                    riskCtrl.setUpdatedBy(aId);
                    riskCtrl.setUpdatedTime(new Date());
                    riskCtrl.setDelFlag(0);
                    boolean f = true;
                    for(int l1 = 0; l1 < riskCtrls.size(); ++l1) {
                        RiskCtrl e1 = (RiskCtrl)riskCtrls.get(l1);
                        if (e1.getRiskId().equals(riskId) && e1.getRiskCtrlPositionId().equals(positionId) && e1.getRiskCtrlLevelId().equals(ctrlLevelId)) {
                            f = false;
                            break;
                        }
                    }
                    if (f) {
                        riskCtrls.add(riskCtrl);
                    }
                }
            }
            if (riskCtrls.size() > 0) {
                //批量添加风险管控配置表信息
                this.riskCtrlMapper.saveBatch(riskCtrls);
            }
        }else{
            RiskCtrl riskCtrl = new RiskCtrl();
            if (!dangerSrcLv.toString().equals("")) {
                riskCtrl.setRiskCtrlLevelId(this.ctrlLevelId(entRiskCtrlLevels, dangerSrcLv.toString()));
            }
            riskCtrl.setCompanyId(cId);
            riskCtrl.setRiskId(riskId);
            riskCtrl.setCreatedBy(aId);
            riskCtrl.setRiskCtrlPositionId(-1L);
            riskCtrl.setCreatedTime(new Date());
            riskCtrl.setCreatedBy(aId);
            riskCtrl.setUpdatedBy(aId);
            riskCtrl.setUpdatedTime(new Date());
            riskCtrl.setDelFlag(0);
            //添加风险管控配置表信息
            this.riskCtrlMapper.add(riskCtrl);
        }
    }

    /**
     * 处理道路excel
     * @param readAll
     * @param entRiskCtrlLevels
     * @param ids
     * @param aId
     * @param cId
     * @param riskDangerType
     * @throws Exception
     */
    private void dealExcleARoad(List<List<Object>> readAll, List entRiskCtrlLevels, Long ids,  Long aId, Long cId, Integer riskDangerType) throws Exception{
        int count = 0;
        Long _sId = ids;
        // 循环读取excel表格每行内容 并插入到数据库
        for(int i = 4; i < readAll.size(); ++i) {
            int k = i + 1;
            List<Object> data = readAll.get(i);
            List<RiskSource> eds = new ArrayList();
            String riskCode = TypeUtils.castToString(data.get(0)) ;
            if (StringUtils.isEmpty(riskCode)) {
                throw new Exception( "第" + k + "行序号是空的，请填写！");
            }
            String riskName = TypeUtils.castToString(data.get(1)) ;
            if (StringUtils.isEmpty(riskName)) {
                throw new Exception( "第" + k + "行作业单元是空的，请填写！");
            }

            String riskLevel = TypeUtils.castToString(data.get(32)) ;
            if (StringUtils.isEmpty(riskLevel)) {
                throw new Exception( "第" + k + "行等级是空的，请填写！");
            }

            if (this.getOneEntRiskCode(cId, riskCode)) {
                throw new Exception( "第" + k + "行序号【" + riskCode + "】已存在，请更换！");
            }
            String groupName =  TypeUtils.castToString(data.get(35)) ;
            String groupUser =  TypeUtils.castToString(data.get(36)) ;
            String A11 = TypeUtils.castToString(data.get(7)) ;
            String A12 = TypeUtils.castToString(data.get(8)) ;
            String A13 = TypeUtils.castToString(data.get(9)) ;
            String tA1 = TypeUtils.castToString(data.get(10)) ;
            String A21 = TypeUtils.castToString(data.get(11)) ;
            String A22 = TypeUtils.castToString(data.get(12)) ;
            String A23 = TypeUtils.castToString(data.get(13)) ;
            String tA2 = TypeUtils.castToString(data.get(14)) ;
            String A31 = TypeUtils.castToString(data.get(15)) ;
            String A32 = TypeUtils.castToString(data.get(16)) ;
            String tA3 = TypeUtils.castToString(data.get(17)) ;
            String A41 = TypeUtils.castToString(data.get(18)) ;
            String tA4 = TypeUtils.castToString(data.get(19)) ;
            String A51 = TypeUtils.castToString(data.get(20)) ;
            String A52 = TypeUtils.castToString(data.get(21)) ;
            String tA5 = TypeUtils.castToString(data.get(22)) ;
            String A61 = TypeUtils.castToString(data.get(23)) ;
            String tA6 = TypeUtils.castToString(data.get(24)) ;
            String A71 = TypeUtils.castToString(data.get(25)) ;
            String tA7 = TypeUtils.castToString(data.get(26)) ;
            String A81 = TypeUtils.castToString(data.get(27)) ;
            String A82 = TypeUtils.castToString(data.get(28)) ;
            String A83 = TypeUtils.castToString(data.get(29)) ;
            String tA8 = TypeUtils.castToString(data.get(30)) ;
            String tA = TypeUtils.castToString(data.get(31)) ;
            //根据风险等级名称 获取风险等级Code码
            Integer dangerSrcLv = this.dangerSrcLv(riskLevel);
            // 根据根危险源名称 获取根危险源Code码
            String dangerSrcCode = this.dangerSrcCode(riskName);
            String riskConsequence = TypeUtils.castToString(data.get(2)) ;
            //获取下一个危险源id
            Long id = this.getNextId(_sId, count);
            //修改sys_uid_sequence表 对应scyf_risk_source的id
            dictMapper.setTabId("scyf_risk_source",id);
            ++count;
            if (!"".equals(riskName)) {
                Integer nodeLevel=1;
                //根据企业id 危险源类型 危险源名称 危险状态 节点层级 获取危险源对象
                RiskSource riskSource =riskSourceMapper.getRiskSource(cId,riskDangerType,riskName,nodeLevel);
                if (riskSource == null) {
                    //添加危险源
                    eds.add(this.buildRiskSource(id, -1L, id, dangerSrcCode, 1, 0, riskName, cId, riskDangerType, 0));
                } else {
                    id = riskSource.getRiskDangerId();
                }
            }

            Long pId = 0L;

            Long riskId = 0L;

            if (pId.equals(0L)) {
                pId = id;
            }
            riskId = this.getNextId(_sId, count);
            ++count;
            //添加危险源
            eds.add(this.buildRiskSource(riskId, pId, id, dangerSrcCode, 3, 1, "", cId, riskDangerType, dangerSrcLv));
            //批量保存危险源
            riskSourceMapper.saveBatch(eds);
            //修改风险等级
            this.updateLvAndIsLeaf(riskId, cId);
            Risk risk = new Risk();
            risk.setRiskId(riskId);
            risk.setCompanyId(cId);
            risk.setRiskDangerId(riskId);
            risk.setRiskCode(riskCode);
            risk.setRiskConsequence(riskConsequence);
            risk.setRiskTypeId((long)Constant.RISK_TYPE_CHANGGUI);
            risk.setRiskLevel(dangerSrcLv);
            risk.setCreatedBy(aId);
            risk.setCreatedTime(new Date());
            risk.setUpdatedBy(aId);
            risk.setUpdatedTime(new Date());
            risk.setA1(A11+","+A12+","+A13+","+tA1);
            risk.setA2(A21+","+A22+","+A23+","+tA2);
            risk.setA3(A31+","+A32+","+tA3);
            risk.setA4(A41+","+tA4);
            risk.setA5(A51+","+A52+","+tA5);
            risk.setA6(A61+","+tA6);
            risk.setA7(A71+","+tA7);
            risk.setA8(A81+","+A82+","+A83+","+tA8);
            risk.setTa(tA);
            risk.setTypeId(6);
            risk.setGroupName(groupName);
            risk.setGroupUser(groupUser);
            risk.setDelFlag(0);
            //保存风险
            this.riskMapper.add(risk);

            String positionTitle = TypeUtils.castToString(data.get(34)) ;
            dealCtrlPositionTitle(positionTitle, entRiskCtrlLevels, aId,cId,dangerSrcLv, riskId);

            String riskMeasureType1 = TypeUtils.castToString(data.get(3)) ;
            List<RiskMeasure> list = new ArrayList();
            //根据管控措施类型不同 返回风险管控措施信息
            if (riskMeasureType1 != null && !"".equals(riskMeasureType1)) {
                list = this.buildList(_sId,riskMeasureType1, 1, cId, riskId, aId, (List)list, ";", false);
            }
            if (list.size() > 0) {
                Long[] sId;
                if (list.size() > 1000) {
                    sId = new Long[]{dictService.getTabId("scyf_risk_source")};
                    List<RiskMeasure> _list = new ArrayList();

                    for(int j = 0; j < ((List)list).size(); ++j) {
                        if (j % 1000 == 0) {
                            //批量保存管控措施信息
                            this.riskMeasureMapper.saveBatch(_list);
                            _list = new ArrayList();
                        }

                        RiskMeasure entRiskMeasure = (RiskMeasure)((List)list).get(j);
                        entRiskMeasure.setRiskMeasureId(sId[0]);
                        _list.add(entRiskMeasure);
                    }
                } else {
                    sId = new Long[]{dictService.getTabId("scyf_risk_source")};
                    for(int c1=0;c1<list.size();c1++){
                        list.get(c1).setRiskMeasureId(sId[0]);
                    }
                    //批量保存管控措施信息
                    this.riskMeasureMapper.saveBatch(list);
                }
            }
        }
    }
    /**
     * 处理桥梁excel
     * @param readAll
     * @param entRiskCtrlLevels
     * @param ids
     * @param aId
     * @param cId
     * @param riskDangerType
     * @throws Exception
     */
    private void dealExcleABridge(List<List<Object>> readAll, List entRiskCtrlLevels, Long ids,  Long aId, Long cId, Integer riskDangerType) throws Exception{
        int count = 0;
        Long _sId = ids;
        // 循环读取excel表格每行内容 并插入到数据库
        for(int i = 4; i < readAll.size(); ++i) {
            int k = i + 1;
            List<Object> one = readAll.get(i);
            JSONArray data = new JSONArray(one);
            List<RiskSource> eds = new ArrayList();
            String riskCode = data.getString(0);
            if (StringUtils.isEmpty(riskCode)) {
                throw new Exception( "第" + k + "行序号是空的，请填写！");
            }
            String riskName = data.getString(1);
            if (StringUtils.isEmpty(riskName)) {
                throw new Exception( "第" + k + "行作业单元是空的，请填写！");
            }

            String riskLevel = data.getString(38);
            if (StringUtils.isEmpty(riskLevel)) {
                throw new Exception( "第" + k + "行等级是空的，请填写！");
            }

            if (this.getOneEntRiskCode(cId, riskCode)) {
                throw new Exception( "第" + k + "行序号【" + riskCode + "】已存在，请更换！");
            }
            String groupName = data.getString(41);
            String groupUser = data.getString(42);
            String A11 = data.getString(7);
            String A12 = data.getString(8);
            String A13 = data.getString(9);
            String A14 = data.getString(10);
            String tA1 = data.getString(11);
            String A21 = data.getString(12);
            String A22 = data.getString(13);
            String A23 = data.getString(14);
            String A24 = data.getString(15);
            String A25 = data.getString(16);
            String A26 = data.getString(17);
            String A27 = data.getString(18);
            String tA2 = data.getString(19);
            String A31 = data.getString(20);
            String A32 = data.getString(21);
            String A33 = data.getString(22);
            String A34 = data.getString(23);
            String tA3 = data.getString(24);
            String A41 = data.getString(25);
            String A42 = data.getString(26);
            String tA4 = data.getString(27);
            String A51 = data.getString(28);
            String tA5 = data.getString(29);
            String A61 = data.getString(30);
            String A62 = data.getString(31);
            String tA6 = data.getString(32);
            String A71 = data.getString(33);
            String A72 = data.getString(34);
            String A73 = data.getString(35);
            String tA7 = data.getString(36);
            String tA = data.getString(37);
            //根据风险等级名称 获取风险等级Code码
            Integer dangerSrcLv = this.dangerSrcLv(riskLevel);
            // 根据根危险源名称 获取根危险源Code码
            String dangerSrcCode = this.dangerSrcCode(riskName);
            String riskConsequence = data.getString(2);
            //获取下一个危险源id
            Long id = this.getNextId(_sId, count);
            //修改sys_uid_sequence表 对应scyf_risk_source的id
            dictMapper.setTabId("scyf_risk_source",id);
            ++count;
            if (!"".equals(riskName)) {
                Integer nodeLevel=1;
                //根据企业id 危险源类型 危险源名称 危险状态 节点层级 获取危险源对象
                RiskSource riskSource =riskSourceMapper.getRiskSource(cId,riskDangerType,riskName,nodeLevel);
                if (riskSource == null) {
                    //添加危险源
                    eds.add(this.buildRiskSource(id, -1L, id, dangerSrcCode, 1, 0, riskName, cId, riskDangerType, 0));
                } else {
                    id = riskSource.getRiskDangerId();
                }
            }

            Long pId = 0L;

            Long riskId = 0L;

            if (pId.equals(0L)) {
                pId = id;
            }
            riskId = this.getNextId(_sId, count);
            ++count;
            //添加危险源
            eds.add(this.buildRiskSource(riskId, pId, id, dangerSrcCode, 3, 1, "", cId, riskDangerType, dangerSrcLv));
            //批量保存危险源
            riskSourceMapper.saveBatch(eds);
            //修改风险等级
            this.updateLvAndIsLeaf(riskId, cId);
            Risk risk = new Risk();
            risk.setRiskId(riskId);
            risk.setCompanyId(cId);
            risk.setRiskDangerId(riskId);
            risk.setRiskCode(riskCode);
            risk.setRiskConsequence(riskConsequence);
            risk.setRiskTypeId((long)Constant.RISK_TYPE_CHANGGUI);
            risk.setRiskLevel(dangerSrcLv);
            risk.setCreatedBy(aId);
            risk.setCreatedTime(new Date());
            risk.setUpdatedBy(aId);
            risk.setUpdatedTime(new Date());
            risk.setA1(A11+","+A12+","+A13+","+A14+","+tA1);
            risk.setA2(A21+","+A22+","+A23+","+A24+","+A25+","+A26+","+A27+","+tA2);
            risk.setA3(A31+","+A32+","+A33+","+A34+","+tA3);
            risk.setA4(A41+","+A42+","+tA4);
            risk.setA5(A51+","+tA5);
            risk.setA6(A61+","+A62+","+tA6);
            risk.setA7(A71+","+A72+","+A73+","+tA7);
            risk.setTa(tA);
            risk.setTypeId(6);
            risk.setGroupName(groupName);
            risk.setGroupUser(groupUser);
            risk.setDelFlag(0);
            //保存风险
            this.riskMapper.add(risk);

            String positionTitle = data.getString(40);
            dealCtrlPositionTitle(positionTitle, entRiskCtrlLevels, aId,cId,dangerSrcLv, riskId);

            String riskMeasureType1 = data.getString(3);
            List<RiskMeasure> list = new ArrayList();
            //根据管控措施类型不同 返回风险管控措施信息
            if (riskMeasureType1 != null && !"".equals(riskMeasureType1)) {
                list = this.buildList(_sId,riskMeasureType1, 1, cId, riskId, aId, (List)list, ";", false);
            }
            if (((List)list).size() > 0) {
                Long[] sId;
                if (((List)list).size() > 1000) {
                    sId = new Long[]{dictService.getTabId("scyf_risk_source")};
                    List<RiskMeasure> _list = new ArrayList();

                    for(int j = 0; j < ((List)list).size(); ++j) {
                        if (j % 1000 == 0) {
                            //批量保存管控措施信息
                            this.riskMeasureMapper.saveBatch(_list);
                            _list = new ArrayList();
                        }

                        RiskMeasure entRiskMeasure = (RiskMeasure)((List)list).get(j);
                        entRiskMeasure.setRiskMeasureId(sId[0]);
                        _list.add(entRiskMeasure);
                    }
                } else {
                    sId = new Long[]{dictService.getTabId("scyf_risk_source")};
                    for(int c1=0;c1<list.size();c1++){
                        list.get(c1).setRiskMeasureId(sId[0]);
                    }
                    //批量保存管控措施信息
                    this.riskMeasureMapper.saveBatch(list);
                }
            }
        }
    }

    /**
     * 处理隧道excel
     * @param readAll
     * @param entRiskCtrlLevels
     * @param ids
     * @param aId
     * @param cId
     * @param riskDangerType
     * @throws Exception
     */
    private void dealExcleATunnel(List<List<Object>> readAll, List entRiskCtrlLevels, Long ids,  Long aId, Long cId, Integer riskDangerType) throws Exception{
        int count = 0;
        Long _sId = ids;
        // 循环读取excel表格每行内容 并插入到数据库
        for(int i = 4; i < readAll.size(); ++i) {
            int k = i + 1;
            List<Object> one = readAll.get(i);
            JSONArray data = new JSONArray(one);
            List<RiskSource> eds = new ArrayList();
            String riskCode = data.getString(0);
            if (StringUtils.isEmpty(riskCode)) {
                throw new Exception( "第" + k + "行序号是空的，请填写！");
            }
            String riskName = data.getString(1);
            if (StringUtils.isEmpty(riskName)) {
                throw new Exception( "第" + k + "行作业单元是空的，请填写！");
            }

            String riskLevel = data.getString(41);
            if (StringUtils.isEmpty(riskLevel)) {
                throw new Exception( "第" + k + "行等级是空的，请填写！");
            }

            if (this.getOneEntRiskCode(cId, riskCode)) {
                throw new Exception( "第" + k + "行序号【" + riskCode + "】已存在，请更换！");
            }
            String groupName = data.getString(44);
            String groupUser = data.getString(45);
            String A11 = data.getString(7);
            String A12 = data.getString(8);
            String A13 = data.getString(9);
            String A14 = data.getString(10);
            String tA1 = data.getString(11);
            String A21 = data.getString(12);
            String A22 = data.getString(13);
            String A23 = data.getString(14);
            String A24 = data.getString(15);
            String tA2 = data.getString(16);
            String A31 = data.getString(17);
            String A32 = data.getString(18);
            String A33 = data.getString(19);
            String A34 = data.getString(20);
            String A35 = data.getString(21);
            String tA3 = data.getString(22);
            String A41 = data.getString(23);
            String A42 = data.getString(24);
            String A43 = data.getString(25);
            String A44 = data.getString(26);
            String tA4 = data.getString(27);
            String A51 = data.getString(28);
            String tA5 = data.getString(29);
            String A61 = data.getString(30);
            String A62 = data.getString(31);
            String tA6 = data.getString(32);
            String A71 = data.getString(33);
            String A72 = data.getString(34);
            String tA7 = data.getString(35);
            String A81 = data.getString(36);
            String A82 = data.getString(37);
            String A83 = data.getString(38);
            String tA8 = data.getString(39);
            String tA = data.getString(40);
            //根据风险等级名称 获取风险等级Code码
            Integer dangerSrcLv = this.dangerSrcLv(riskLevel);
            // 根据根危险源名称 获取根危险源Code码
            String dangerSrcCode = this.dangerSrcCode(riskName);
            String riskConsequence = data.getString(2);
            //获取下一个危险源id
            Long id = this.getNextId(_sId, count);
            //修改sys_uid_sequence表 对应scyf_risk_source的id
            dictMapper.setTabId("scyf_risk_source",id);
            ++count;
            if (!"".equals(riskName)) {
                Integer nodeLevel=1;
                //根据企业id 危险源类型 危险源名称 危险状态 节点层级 获取危险源对象
                RiskSource riskSource =riskSourceMapper.getRiskSource(cId,riskDangerType,riskName,nodeLevel);
                if (riskSource == null) {
                    //添加危险源
                    eds.add(this.buildRiskSource(id, -1L, id, dangerSrcCode, 1, 0, riskName, cId, riskDangerType, 0));
                } else {
                    id = riskSource.getRiskDangerId();
                }
            }

            Long pId = 0L;

            Long riskId = 0L;

            if (pId.equals(0L)) {
                pId = id;
            }
            riskId = this.getNextId(_sId, count);
            ++count;
            //添加危险源
            eds.add(this.buildRiskSource(riskId, pId, id, dangerSrcCode, 3, 1, "", cId, riskDangerType, dangerSrcLv));
            //批量保存危险源
            riskSourceMapper.saveBatch(eds);
            //修改风险等级
            this.updateLvAndIsLeaf(riskId, cId);
            Risk risk = new Risk();
            risk.setRiskId(riskId);
            risk.setCompanyId(cId);
            risk.setRiskDangerId(riskId);
            risk.setRiskCode(riskCode);
            risk.setRiskConsequence(riskConsequence);
            risk.setRiskTypeId((long)Constant.RISK_TYPE_CHANGGUI);
            risk.setRiskLevel(dangerSrcLv);
            risk.setCreatedBy(aId);
            risk.setCreatedTime(new Date());
            risk.setUpdatedBy(aId);
            risk.setUpdatedTime(new Date());
            risk.setA1(A11+","+A12+","+A13+","+A14+","+tA1);
            risk.setA2(A21+","+A22+","+A23+","+A24+","+tA2);
            risk.setA3(A31+","+A32+","+A33+","+A34+","+A35+","+tA3);
            risk.setA4(A41+","+A42+","+A43+","+A44+","+tA4);
            risk.setA5(A51+","+tA5);
            risk.setA6(A61+","+A62+","+tA6);
            risk.setA7(A71+","+A72+","+tA7);
            risk.setA8(A81+","+A82+","+A83+","+tA8);
            risk.setTa(tA);
            risk.setTypeId(6);
            risk.setGroupName(groupName);
            risk.setGroupUser(groupUser);
            risk.setDelFlag(0);
            //保存风险
            this.riskMapper.add(risk);

            String positionTitle = data.getString(43);
            dealCtrlPositionTitle(positionTitle, entRiskCtrlLevels, aId,cId,dangerSrcLv, riskId);

            String riskMeasureType1 = data.getString(3);
            List<RiskMeasure> list = new ArrayList();
            //根据管控措施类型不同 返回风险管控措施信息
            if (riskMeasureType1 != null && !"".equals(riskMeasureType1)) {
                list = this.buildList(_sId,riskMeasureType1, 1, cId, riskId, aId, (List)list, ";", false);
            }
            if (((List)list).size() > 0) {
                Long[] sId;
                if (((List)list).size() > 1000) {
                    sId = new Long[]{dictService.getTabId("scyf_risk_source")};
                    List<RiskMeasure> _list = new ArrayList();

                    for(int j = 0; j < ((List)list).size(); ++j) {
                        if (j % 1000 == 0) {
                            //批量保存管控措施信息
                            this.riskMeasureMapper.saveBatch(_list);
                            _list = new ArrayList();
                        }

                        RiskMeasure entRiskMeasure = (RiskMeasure)((List)list).get(j);
                        entRiskMeasure.setRiskMeasureId(sId[0]);
                        _list.add(entRiskMeasure);
                    }
                } else {
                    sId = new Long[]{dictService.getTabId("scyf_risk_source")};
                    for(int c1=0;c1<list.size();c1++){
                        list.get(c1).setRiskMeasureId(sId[0]);
                    }
                    //批量保存管控措施信息
                    this.riskMeasureMapper.saveBatch(list);
                }
            }
        }
    }

    /**
     * 危险源Excel导出
     * yfh
     * 2020/06/04
     * @param riskDangerType
     * @param cId
     * @return
     */
    @Override
    public XSSFWorkbook exportExcel(Integer riskDangerType, Long cId) {
        XSSFWorkbook wb = null;

        try {
            ResourceLoader resourceLoader = new DefaultResourceLoader();
            Resource resource = null;
            if(riskDangerType==1){
                resource = resourceLoader.getResource("classpath:excel/riskSourceDevice.xlsx");
            }
            if(riskDangerType==2||riskDangerType==14){
                resource = resourceLoader.getResource("classpath:excel/riskSourceActivity.xlsx");
            }

            wb = new XSSFWorkbook(resource.getInputStream());
            XSSFSheet sheet = wb.getSheetAt(0);
            JSONObject json = new JSONObject();
            json.put("riskDangerType", riskDangerType);
            json.put("companyId", cId);
            List<Map<String, Object>> allRiskList = riskService.queryRisk(json);

            for(int r = 0; r < allRiskList.size(); ++r) {
                JSONObject josn = new JSONObject((Map)allRiskList.get(r));
                String riskCode = josn.getString("riskCode");
                String riskName = josn.getString("riskName");
                String dangerSrcName = josn.getString("rootName");
                String riskReason = josn.getString("riskHramFactor");
                String riskConsequence = josn.getString("riskConsequence");
                String levelStr = "蓝";
                int level = josn.getInteger("riskLevel");
                if (level == 1) {
                    levelStr = "红";
                } else if (level == 2) {
                    levelStr = "橙";
                } else if (level == 3) {
                    levelStr = "黄";
                }

                String riskCtrlLevelTitle = josn.getString("riskCtrlLevelTitle");
                String sPositionTitle = josn.getString("sPositionTitle");
                XSSFRow row = sheet.createRow(r + 2);
                this.createCell(row, 0, riskCode);
                this.createCell(row, 1, dangerSrcName);

                this.createCell(row, 2, riskName);
                this.createCell(row, 3, riskReason);
                this.createCell(row, 4, riskConsequence);
                this.createCell(row, 5, levelStr);
                this.createCell(row, 11, riskCtrlLevelTitle);
                this.createCell(row, 12, sPositionTitle);
                String riskMeasureContent = josn.getString("riskMeasureContent");
                if (riskMeasureContent != null && !riskMeasureContent.equals("")) {
                    List<String> type1 = Lists.newArrayList();
                    List<String> type2 = Lists.newArrayList();
                    List<String> type3 = Lists.newArrayList();
                    List<String> type4 = Lists.newArrayList();
                    List<String> type5 = Lists.newArrayList();
                    String[] riskMeasureContents = riskMeasureContent.split("@#@");
                    if (riskMeasureContents.length > 0) {
                        for(int i = 0; i < riskMeasureContents.length; ++i) {
                            String riskMeasure = riskMeasureContents[i];
                            String type = riskMeasure.split("#")[0];
                            String content = "";
                            if (riskMeasure.split("#").length > 1) {
                                content = riskMeasure.split("#")[1];
                                content = content.equals(",") ? "" : content;
                            }

                            switch(Integer.valueOf(type)) {
                                case 1:
                                    type1.add(content);
                                    break;
                                case 2:
                                    type2.add(content);
                                    break;
                                case 3:
                                    type3.add(content);
                                    break;
                                case 4:
                                    type4.add(content);
                                    break;
                                case 5:
                                    type5.add(content);
                            }
                        }

                        this.createCell(row, 6, Joiner.on("、").join(type1));
                        this.createCell(row, 7, Joiner.on("、").join(type2));
                        this.createCell(row, 8, Joiner.on("、").join(type3));
                        this.createCell(row, 9, Joiner.on("、").join(type4));
                        this.createCell(row, 10, Joiner.on("、").join(type5));
                    }
                }
            }
        } catch (Exception var34) {
            var34.printStackTrace();
        }

        return wb;
    }


    /**
     * 危险源 LEC/LC/LS Excel导出  交通
     * yfh
     * 2020/08/26
     * @param riskDangerType
     * @param cId
     * @return
     */
    @Override
    public XSSFWorkbook exportLExcel(Integer riskDangerType, Long cId) {
        XSSFWorkbook wb = null;

        try {
            ResourceLoader resourceLoader = new DefaultResourceLoader();
            Resource resource = resourceLoader.getResource("classpath:excel/exportTemplate.xlsx");
            wb = new XSSFWorkbook(resource.getInputStream());
            XSSFSheet sheet = wb.getSheetAt(0);
            JSONObject json = new JSONObject();
            json.put("riskDangerType", riskDangerType);
            json.put("companyId", cId);
            List<Map<String, Object>> allRiskList = riskService.queryRisk(json);

            for(int r = 0; r < allRiskList.size(); ++r) {
                JSONObject josn = new JSONObject((Map)allRiskList.get(r));
                String riskCode = josn.getString("riskCode");//序号
                String dangerSrcName = josn.getString("rootName");//作业单元
                String riskReason = josn.getString("riskHramFactor");//致险因素
                String riskConsequence = josn.getString("riskConsequence");//典型风险事件
                String riskDesc = josn.getString("riskDesc");//备注
                String levelStr = "较低";//等级
                int level = josn.getInteger("riskLevel");
                if (level == 1) {
                    levelStr = "重大";
                } else if (level == 2) {
                    levelStr = "较大";
                } else if (level == 3) {
                    levelStr = "一般";
                }

                String riskCtrlLevelTitle = josn.getString("riskCtrlLevelTitle");//管控级别
                String sPositionTitle = josn.getString("sPositionTitle");//管控岗位
                String lecD=josn.getString("lecD");
                String lcD=josn.getString("lcD");
                String lsR=josn.getString("lsR");
                String groupName=josn.getString("groupName");
                String groupUser=josn.getString("groupUser");
                String typeId=josn.getString("typeId");
                XSSFRow row = sheet.createRow(r + 2);
                this.createCell(row, 0, riskCode);
                this.createCell(row, 1, dangerSrcName);
                this.createCell(row, 2, riskConsequence);
                this.createCell(row, 3, riskReason);
                if(typeId.equals("0")){//LEC
                    String lecL=subNumberText(josn.getString("lecL"))+",";
                    String lecE=subNumberText(josn.getString("lecE"))+",";
                    String lecC=subNumberText(josn.getString("lecC"));
                    this.createCell(row, 4, lecL+lecE+lecC);
                    this.createCell(row, 5, subNumberText(lecD));
                }
                if(typeId.equals("1")){//LC
                    String lecL=subNumberText(josn.getString("lcL"))+",";
                    String lecC=subNumberText(josn.getString("lcC"));
                    this.createCell(row, 4, lecL+lecC);
                    this.createCell(row, 5, subNumberText(lcD));
                }
                if(typeId.equals("2")){//LS
                    String lsL=subNumberText(josn.getString("lsL"))+",";
                    String lsS1=subNumberText(josn.getString("lsS"));
                    this.createCell(row, 4, lsL+lsS1);
                    this.createCell(row, 5, subNumberText(lsR));
                }
                this.createCell(row, 6, levelStr);
                this.createCell(row, 7, riskCtrlLevelTitle);

                this.createCell(row, 13, "");
                this.createCell(row, 14, groupName);
                this.createCell(row, 15, sPositionTitle);
                this.createCell(row, 16, groupUser);
                this.createCell(row, 17, riskDesc);
                if(typeId.equals("0")){
                    this.createCell(row, 18, "LEC");
                }
                if(typeId.equals("1")){
                    this.createCell(row, 18, "LC");
                }
                if(typeId.equals("2")){
                    this.createCell(row, 18, "LS");
                }
                String riskMeasureContent = josn.getString("riskMeasureContent");
                if (riskMeasureContent != null && !riskMeasureContent.equals("")) {
                    List<String> type1 = Lists.newArrayList();
                    List<String> type2 = Lists.newArrayList();
                    List<String> type3 = Lists.newArrayList();
                    List<String> type4 = Lists.newArrayList();
                    List<String> type5 = Lists.newArrayList();
                    String[] riskMeasureContents = riskMeasureContent.split("@#@");
                    if (riskMeasureContents.length > 0) {
                        for(int i = 0; i < riskMeasureContents.length; ++i) {
                            String riskMeasure = riskMeasureContents[i];
                            String type = riskMeasure.split("#")[0];
                            String content = "";
                            if (riskMeasure.split("#").length > 1) {
                                content = riskMeasure.split("#")[1];
                                content = content.equals(",") ? "" : content;
                            }

                            switch(Integer.valueOf(type)) {
                                case 1:
                                    type1.add(content);
                                    break;
                                case 2:
                                    type2.add(content);
                                    break;
                                case 3:
                                    type3.add(content);
                                    break;
                                case 4:
                                    type4.add(content);
                                    break;
                                case 5:
                                    type5.add(content);
                            }
                        }

                        this.createCell(row, 8, Joiner.on("、").join(type1));
                        this.createCell(row, 9, Joiner.on("、").join(type2));
                        this.createCell(row, 10, Joiner.on("、").join(type3));
                        this.createCell(row, 11, Joiner.on("、").join(type5));
                        this.createCell(row, 12, Joiner.on("、").join(type4));
                    }
                }
            }
        } catch (Exception var34) {
            var34.printStackTrace();
        }

        return wb;
    }


    /**
     * 危险源 高速 A评价表导出
     * yfh
     * 2020/09/17
     * @param riskDangerType
     * @param cId
     * @return
     */
    @Override
    public XSSFWorkbook exportAExcel(Integer riskDangerType, Long cId, Integer evaluateType) {
        XSSFWorkbook wb = null;

        try {
            ResourceLoader resourceLoader = new DefaultResourceLoader();
            Resource resource = resourceLoader.getResource(getResourcePathByEvaluateType(evaluateType));
            wb = new XSSFWorkbook(resource.getInputStream());
            XSSFSheet sheet = wb.getSheetAt(0);
            JSONObject json = new JSONObject();
            json.put("riskDangerType", riskDangerType);
            json.put("companyId", cId);
            List<Map<String, Object>> allRiskList = riskService.queryARisk(json);

            if(null == evaluateType || 0 == evaluateType.intValue()){
                dealExportAExcel(sheet,allRiskList );
            }else if(1== evaluateType.intValue()){
                // 道路
                dealExportAExcelRoad(sheet,allRiskList);
            }else if(2== evaluateType.intValue()){
                // 桥梁
                dealExportAExcelBridge(sheet,allRiskList);
            }else if(3== evaluateType.intValue()){
                // 隧道
                dealExportAExcelTunnel(sheet,allRiskList);
            }

        } catch (Exception var34) {
            var34.printStackTrace();
        }

        return wb;
    }

    private String getResourcePathByEvaluateType(Integer evaluateType){
        if(null == evaluateType || 0 == evaluateType.intValue()){
            return "classpath:excel/gsA.xlsx" ;
        }else if(1 == evaluateType.intValue()){
            // 道路
            return "classpath:excel/gsA8dl.xlsx" ;
        }else if(1 == evaluateType.intValue()){
            // 桥梁
            return "classpath:excel/gsA7ql.xlsx" ;
        }else if(1 == evaluateType.intValue()){
            // 隧道
            return "classpath:excel/gsA8sd.xlsx" ;
        }else {
            return "classpath:excel/gsA.xlsx" ;
        }
    }
    private void dealExportAExcel(XSSFSheet sheet, List<Map<String, Object>> allRiskList ){
        for(int r = 0; r < allRiskList.size(); ++r) {
            JSONObject josn = new JSONObject((Map)allRiskList.get(r));
            String riskCode = josn.getString("riskCode");//序号
            String dangerSrcName = josn.getString("rootName");//作业单元
            String riskConsequence = josn.getString("riskConsequence");//典型风险事件
            String levelStr = "较低";//等级
            int level = josn.getInteger("riskLevel");//等级
            if (level == 1) {
                levelStr = "重大";
            } else if (level == 2) {
                levelStr = "较大";
            } else if (level == 3) {
                levelStr = "一般";
            }

            String riskCtrlLevelTitle = josn.getString("riskCtrlLevelTitle");//管控级别
            String riskMeasureContent = josn.getString("riskMeasureContent");//管控措施
            String replaceRiskMeasureContent = riskMeasureContent.replace("@#@1#", "");
            String positionTitles = josn.getString("sPositionTitle");
            String groupName=josn.getString("groupName");//责任部门
            String groupUser=josn.getString("groupUser");//责任人
            XSSFRow row = sheet.createRow(r + 4);
            this.createCell(row, 0, riskCode);
            this.createCell(row, 1, dangerSrcName);
            this.createCell(row, 2, riskConsequence);
            this.createCell(row, 3, replaceRiskMeasureContent);
            this.createCell(row, 4, "是");
            this.createCell(row, 5, "暂无");
            this.createCell(row, 6, "");

            this.createCell(row, 7, josn.getString("a1").split(",")[0]);
            this.createCell(row, 8, josn.getString("a1").split(",")[1]);
            this.createCell(row, 9, josn.getString("a1").split(",")[2]);
            this.createCell(row, 10, josn.getString("a1").split(",")[3]);
            this.createCell(row, 11, josn.getString("a2").split(",")[0]);
            this.createCell(row, 12, josn.getString("a2").split(",")[1]);
            this.createCell(row, 13, josn.getString("a2").split(",")[2]);
            this.createCell(row, 14, josn.getString("a3").split(",")[0]);
            this.createCell(row, 15, josn.getString("a3").split(",")[1]);
            this.createCell(row, 16, josn.getString("a3").split(",")[2]);
            this.createCell(row, 17, josn.getString("a3").split(",")[3]);
            this.createCell(row, 18, josn.getString("a4").split(",")[0]);
            this.createCell(row, 19, josn.getString("a4").split(",")[1]);
            this.createCell(row, 20, josn.getString("a5").split(",")[0]);
            this.createCell(row, 21, josn.getString("a5").split(",")[1]);
            this.createCell(row, 22, josn.getString("a5").split(",")[2]);
            this.createCell(row, 23, josn.getString("a6").split(",")[0]);
            this.createCell(row, 24, josn.getString("a6").split(",")[1]);
            this.createCell(row, 25, josn.getString("a6").split(",")[2]);
            this.createCell(row, 26, josn.getString("a7").split(",")[0]);
            this.createCell(row, 27, josn.getString("a7").split(",")[1]);
            this.createCell(row, 28, josn.getString("a7").split(",")[2]);
            this.createCell(row, 29, josn.getString("a7").split(",")[3]);
            this.createCell(row, 30, josn.getString("a7").split(",")[4]);
            this.createCell(row, 31, josn.getString("ta"));

            this.createCell(row, 32, levelStr);
            this.createCell(row, 33, riskCtrlLevelTitle);
            this.createCell(row, 34, positionTitles);

            this.createCell(row, 35, groupName);
            this.createCell(row, 36, groupUser);

        }
    }
    // 道路
    private void dealExportAExcelRoad(XSSFSheet sheet, List<Map<String, Object>> allRiskList ){
        for(int r = 0; r < allRiskList.size(); ++r) {
            JSONObject josn = new JSONObject((Map)allRiskList.get(r));
            String riskCode = josn.getString("riskCode");//序号
            String dangerSrcName = josn.getString("rootName");//作业单元
            String riskConsequence = josn.getString("riskConsequence");//典型风险事件
            String levelStr = "较低";//等级
            int level = josn.getInteger("riskLevel");//等级
            if (level == 1) {
                levelStr = "重大";
            } else if (level == 2) {
                levelStr = "较大";
            } else if (level == 3) {
                levelStr = "一般";
            }

            String riskCtrlLevelTitle = josn.getString("riskCtrlLevelTitle");//管控级别
            String riskMeasureContent = josn.getString("riskMeasureContent");//管控措施
            String replaceRiskMeasureContent = riskMeasureContent.replace("@#@1#", "");
            String positionTitles = josn.getString("sPositionTitle");
            String groupName=josn.getString("groupName");//责任部门
            String groupUser=josn.getString("groupUser");//责任人
            XSSFRow row = sheet.createRow(r + 4);
            this.createCell(row, 0, riskCode);
            this.createCell(row, 1, dangerSrcName);
            this.createCell(row, 2, riskConsequence);
            this.createCell(row, 3, replaceRiskMeasureContent);
            this.createCell(row, 4, "是");
            this.createCell(row, 5, "暂无");
            this.createCell(row, 6, "");

            this.createCell(row, 7, josn.getString("a1").split(",")[0]);
            this.createCell(row, 8, josn.getString("a1").split(",")[1]);
            this.createCell(row, 9, josn.getString("a1").split(",")[2]);
            this.createCell(row, 10, josn.getString("a1").split(",")[3]);

            this.createCell(row, 11, josn.getString("a2").split(",")[0]);
            this.createCell(row, 12, josn.getString("a2").split(",")[1]);
            this.createCell(row, 13, josn.getString("a2").split(",")[2]);
            this.createCell(row, 14, josn.getString("a2").split(",")[3]);

            this.createCell(row, 15, josn.getString("a3").split(",")[0]);
            this.createCell(row, 16, josn.getString("a3").split(",")[1]);
            this.createCell(row, 17, josn.getString("a3").split(",")[2]);

            this.createCell(row, 18, josn.getString("a4").split(",")[0]);
            this.createCell(row, 19, josn.getString("a4").split(",")[1]);

            this.createCell(row, 20, josn.getString("a5").split(",")[0]);
            this.createCell(row, 21, josn.getString("a5").split(",")[1]);
            this.createCell(row, 22, josn.getString("a5").split(",")[2]);

            this.createCell(row, 23, josn.getString("a6").split(",")[0]);
            this.createCell(row, 24, josn.getString("a6").split(",")[1]);

            this.createCell(row, 25, josn.getString("a7").split(",")[0]);
            this.createCell(row, 26, josn.getString("a7").split(",")[1]);

            this.createCell(row, 27, josn.getString("a8").split(",")[0]);
            this.createCell(row, 28, josn.getString("a8").split(",")[1]);
            this.createCell(row, 29, josn.getString("a8").split(",")[2]);
            this.createCell(row, 30, josn.getString("a8").split(",")[3]);
            this.createCell(row, 31, josn.getString("ta"));

            this.createCell(row, 32, levelStr);
            this.createCell(row, 33, riskCtrlLevelTitle);
            this.createCell(row, 34, positionTitles);

            this.createCell(row, 35, groupName);
            this.createCell(row, 36, groupUser);

        }
    }
    // 桥梁
    private void dealExportAExcelBridge(XSSFSheet sheet, List<Map<String, Object>> allRiskList ){
        for(int r = 0; r < allRiskList.size(); ++r) {
            JSONObject josn = new JSONObject((Map)allRiskList.get(r));
            String riskCode = josn.getString("riskCode");//序号
            String dangerSrcName = josn.getString("rootName");//作业单元
            String riskConsequence = josn.getString("riskConsequence");//典型风险事件
            String levelStr = "较低";//等级
            int level = josn.getInteger("riskLevel");//等级
            if (level == 1) {
                levelStr = "重大";
            } else if (level == 2) {
                levelStr = "较大";
            } else if (level == 3) {
                levelStr = "一般";
            }

            String riskCtrlLevelTitle = josn.getString("riskCtrlLevelTitle");//管控级别
            String riskMeasureContent = josn.getString("riskMeasureContent");//管控措施
            String replaceRiskMeasureContent = riskMeasureContent.replace("@#@1#", "");
            String positionTitles = josn.getString("sPositionTitle");
            String groupName=josn.getString("groupName");//责任部门
            String groupUser=josn.getString("groupUser");//责任人
            XSSFRow row = sheet.createRow(r + 4);
            this.createCell(row, 0, riskCode);
            this.createCell(row, 1, dangerSrcName);
            this.createCell(row, 2, riskConsequence);
            this.createCell(row, 3, replaceRiskMeasureContent);
            this.createCell(row, 4, "是");
            this.createCell(row, 5, "暂无");
            this.createCell(row, 6, "");

            this.createCell(row, 7, josn.getString("a1").split(",")[0]);
            this.createCell(row, 8, josn.getString("a1").split(",")[1]);
            this.createCell(row, 9, josn.getString("a1").split(",")[2]);
            this.createCell(row, 10, josn.getString("a1").split(",")[3]);
            this.createCell(row, 11, josn.getString("a1").split(",")[4]);

            this.createCell(row, 12, josn.getString("a2").split(",")[0]);
            this.createCell(row, 13, josn.getString("a2").split(",")[1]);
            this.createCell(row, 14, josn.getString("a2").split(",")[2]);
            this.createCell(row, 15, josn.getString("a2").split(",")[3]);
            this.createCell(row, 16, josn.getString("a2").split(",")[4]);
            this.createCell(row, 17, josn.getString("a2").split(",")[5]);
            this.createCell(row, 18, josn.getString("a2").split(",")[6]);
            this.createCell(row, 19, josn.getString("a2").split(",")[7]);

            this.createCell(row, 20, josn.getString("a3").split(",")[0]);
            this.createCell(row, 21, josn.getString("a3").split(",")[1]);
            this.createCell(row, 22, josn.getString("a3").split(",")[2]);
            this.createCell(row, 23, josn.getString("a3").split(",")[3]);
            this.createCell(row, 24, josn.getString("a3").split(",")[4]);

            this.createCell(row, 25, josn.getString("a4").split(",")[0]);
            this.createCell(row, 26, josn.getString("a4").split(",")[1]);
            this.createCell(row, 27, josn.getString("a4").split(",")[2]);

            this.createCell(row, 28, josn.getString("a5").split(",")[0]);
            this.createCell(row, 29, josn.getString("a5").split(",")[1]);

            this.createCell(row, 30, josn.getString("a6").split(",")[0]);
            this.createCell(row, 31, josn.getString("a6").split(",")[1]);
            this.createCell(row, 32, josn.getString("a6").split(",")[2]);

            this.createCell(row, 33, josn.getString("a7").split(",")[0]);
            this.createCell(row, 34, josn.getString("a7").split(",")[1]);
            this.createCell(row, 35, josn.getString("a7").split(",")[2]);
            this.createCell(row, 36, josn.getString("a7").split(",")[3]);
            this.createCell(row, 37, josn.getString("ta"));

            this.createCell(row, 38, levelStr);
            this.createCell(row, 39, riskCtrlLevelTitle);
            this.createCell(row, 40, positionTitles);

            this.createCell(row, 41, groupName);
            this.createCell(row, 42, groupUser);

        }
    }
    // 隧道
    private void dealExportAExcelTunnel(XSSFSheet sheet, List<Map<String, Object>> allRiskList ){
        for(int r = 0; r < allRiskList.size(); ++r) {
            JSONObject josn = new JSONObject((Map)allRiskList.get(r));
            String riskCode = josn.getString("riskCode");//序号
            String dangerSrcName = josn.getString("rootName");//作业单元
            String riskConsequence = josn.getString("riskConsequence");//典型风险事件
            String levelStr = "较低";//等级
            int level = josn.getInteger("riskLevel");//等级
            if (level == 1) {
                levelStr = "重大";
            } else if (level == 2) {
                levelStr = "较大";
            } else if (level == 3) {
                levelStr = "一般";
            }

            String riskCtrlLevelTitle = josn.getString("riskCtrlLevelTitle");//管控级别
            String riskMeasureContent = josn.getString("riskMeasureContent");//管控措施
            String replaceRiskMeasureContent = riskMeasureContent.replace("@#@1#", "");
            String positionTitles = josn.getString("sPositionTitle");
            String groupName=josn.getString("groupName");//责任部门
            String groupUser=josn.getString("groupUser");//责任人
            XSSFRow row = sheet.createRow(r + 4);
            this.createCell(row, 0, riskCode);
            this.createCell(row, 1, dangerSrcName);
            this.createCell(row, 2, riskConsequence);
            this.createCell(row, 3, replaceRiskMeasureContent);
            this.createCell(row, 4, "是");
            this.createCell(row, 5, "暂无");
            this.createCell(row, 6, "");

            this.createCell(row, 7, josn.getString("a1").split(",")[0]);
            this.createCell(row, 8, josn.getString("a1").split(",")[1]);
            this.createCell(row, 9, josn.getString("a1").split(",")[2]);
            this.createCell(row, 10, josn.getString("a1").split(",")[3]);
            this.createCell(row, 11, josn.getString("a1").split(",")[4]);

            this.createCell(row, 12, josn.getString("a2").split(",")[0]);
            this.createCell(row, 13, josn.getString("a2").split(",")[1]);
            this.createCell(row, 14, josn.getString("a2").split(",")[2]);
            this.createCell(row, 15, josn.getString("a2").split(",")[3]);
            this.createCell(row, 16, josn.getString("a2").split(",")[4]);

            this.createCell(row, 17, josn.getString("a3").split(",")[0]);
            this.createCell(row, 18, josn.getString("a3").split(",")[1]);
            this.createCell(row, 19, josn.getString("a3").split(",")[2]);
            this.createCell(row, 20, josn.getString("a3").split(",")[3]);
            this.createCell(row, 21, josn.getString("a3").split(",")[4]);
            this.createCell(row, 22, josn.getString("a3").split(",")[5]);

            this.createCell(row, 23, josn.getString("a4").split(",")[0]);
            this.createCell(row, 24, josn.getString("a4").split(",")[1]);
            this.createCell(row, 25, josn.getString("a4").split(",")[2]);
            this.createCell(row, 26, josn.getString("a4").split(",")[3]);
            this.createCell(row, 27, josn.getString("a4").split(",")[4]);

            this.createCell(row, 28, josn.getString("a5").split(",")[0]);
            this.createCell(row, 29, josn.getString("a5").split(",")[1]);

            this.createCell(row, 30, josn.getString("a6").split(",")[0]);
            this.createCell(row, 31, josn.getString("a6").split(",")[1]);
            this.createCell(row, 32, josn.getString("a6").split(",")[2]);

            this.createCell(row, 33, josn.getString("a7").split(",")[0]);
            this.createCell(row, 34, josn.getString("a7").split(",")[1]);
            this.createCell(row, 35, josn.getString("a7").split(",")[2]);

            this.createCell(row, 36, josn.getString("a8").split(",")[0]);
            this.createCell(row, 37, josn.getString("a8").split(",")[1]);
            this.createCell(row, 38, josn.getString("a8").split(",")[2]);
            this.createCell(row, 39, josn.getString("a8").split(",")[3]);

            this.createCell(row, 40, josn.getString("ta"));

            this.createCell(row, 41, levelStr);
            this.createCell(row, 42, riskCtrlLevelTitle);
            this.createCell(row, 43, positionTitles);

            this.createCell(row, 44, groupName);
            this.createCell(row, 45, groupUser);

        }
    }

    /**
     * 危险源 LEC/LS Excel导出  水利
     * yfh
     * 2020/08/27
     * @param riskDangerType
     * @param cId
     * @return
     */
    @Override
    public XSSFWorkbook exportRiverExcel(Integer riskDangerType, Long cId) {
        XSSFWorkbook wb = null;

        try {
            ResourceLoader resourceLoader = new DefaultResourceLoader();
            Resource resource = resourceLoader.getResource("classpath:excel/exportRiver.xlsx");
            wb = new XSSFWorkbook(resource.getInputStream());
            XSSFSheet sheet = wb.getSheetAt(0);
            JSONObject json = new JSONObject();
            json.put("riskDangerType", riskDangerType);
            json.put("companyId", cId);
            List<Map<String, Object>> allRiskList = riskService.queryRisk(json);

            for(int r = 0; r < allRiskList.size(); ++r) {
                JSONObject josn = new JSONObject((Map)allRiskList.get(r));
                String riskCode = josn.getString("riskCode");//编号
                String riskType = josn.getString("riskType");//类型
                String dangerSrcName = josn.getString("rootName");//风险点名称
                String riskName = josn.getString("riskName");//检查项名称
                String riskReason = josn.getString("riskHramFactor");//事故诱因
                String riskConsequence = josn.getString("riskConsequence");//后果
                String lecD=josn.getString("lecD");//风险值
                String lsR=josn.getString("lsR");//风险值
                String levelStr = "较低";//等级
                int level = josn.getInteger("riskLevel");
                if (level == 1) {
                    levelStr = "重大";
                } else if (level == 2) {
                    levelStr = "较大";
                } else if (level == 3) {
                    levelStr = "一般";
                }
                String riskCtrlLevelTitle = josn.getString("riskCtrlLevelTitle");//管控级别
                String groupName=josn.getString("groupName");//责任部门
                String sPositionTitle = josn.getString("sPositionTitle");//管控岗位
                String groupUser=josn.getString("groupUser");//责任人
                String typeId=josn.getString("typeId");
                XSSFRow row = sheet.createRow(r + 2);
                this.createCell(row, 0, riskCode);
                this.createCell(row, 1, riskType);
                this.createCell(row, 2, dangerSrcName);
                this.createCell(row, 3, riskName);
                this.createCell(row, 4, riskReason);
                this.createCell(row, 5, riskConsequence);

                this.createCell(row, 11, "");
                if(typeId.equals("0")){//LEC
                    String lecL=subNumberText(josn.getString("lecL"))+",";
                    String lecE=subNumberText(josn.getString("lecE"))+",";
                    String lecC=subNumberText(josn.getString("lecC"));
                    this.createCell(row, 12, lecL+lecE+lecC);
                    this.createCell(row, 13, subNumberText(lecD));
                }
                if(typeId.equals("2")){//LS
                    String lsL=subNumberText(josn.getString("lsL"))+",";
                    String lsS=subNumberText(josn.getString("lsS"));
                    this.createCell(row, 12, lsL+lsS);
                    this.createCell(row, 13, subNumberText(lsR));
                }
                this.createCell(row, 14, levelStr);
                this.createCell(row, 15, riskCtrlLevelTitle);

                this.createCell(row, 16, groupName);
                this.createCell(row, 17, sPositionTitle);
                this.createCell(row, 18, groupUser);
                if(typeId.equals("0")){
                    this.createCell(row, 19, "LEC");
                }
                if(typeId.equals("2")){
                    this.createCell(row, 19, "LS");
                }
                String riskMeasureContent = josn.getString("riskMeasureContent");
                if (riskMeasureContent != null && !riskMeasureContent.equals("")) {
                    List<String> type1 = Lists.newArrayList();
                    List<String> type2 = Lists.newArrayList();
                    List<String> type3 = Lists.newArrayList();
                    List<String> type4 = Lists.newArrayList();
                    List<String> type5 = Lists.newArrayList();
                    String[] riskMeasureContents = riskMeasureContent.split("@#@");
                    if (riskMeasureContents.length > 0) {
                        for(int i = 0; i < riskMeasureContents.length; ++i) {
                            String riskMeasure = riskMeasureContents[i];
                            String type = riskMeasure.split("#")[0];
                            String content = "";
                            if (riskMeasure.split("#").length > 1) {
                                content = riskMeasure.split("#")[1];
                                content = content.equals(",") ? "" : content;
                            }

                            switch(Integer.valueOf(type)) {
                                case 1:
                                    type1.add(content);
                                    break;
                                case 2:
                                    type2.add(content);
                                    break;
                                case 3:
                                    type3.add(content);
                                    break;
                                case 4:
                                    type4.add(content);
                                    break;
                                case 5:
                                    type5.add(content);
                            }
                        }

                        this.createCell(row, 6, Joiner.on("、").join(type1));
                        this.createCell(row, 7, Joiner.on("、").join(type2));
                        this.createCell(row, 8, Joiner.on("、").join(type3));
                        this.createCell(row, 9, Joiner.on("、").join(type5));
                        this.createCell(row, 10, Joiner.on("、").join(type4));
                    }
                }
            }
        } catch (Exception var34) {
            var34.printStackTrace();
        }

        return wb;
    }

    /**
     * 修改风险源等级
     * @param riskId
     * @param companyId
     * @param ifNotIn
     * @return
     */
    @Override
    public boolean updateLvAndLeaf(Integer riskId, Long companyId, boolean ifNotIn) {
        //根据id获取危险源对象信息
        RiskSource dangerSource = (RiskSource)this.getById((long)riskId);
        if (dangerSource == null) {
            return true;
        } else {
            //根据父ID 查询危险源信息
            List<RiskSource> EntDangerSourceCount = riskSourceMapper.count(dangerSource.getParentRiskDangerId());
            if (EntDangerSourceCount.size() == 0) {
                RiskSource eds = new RiskSource();
                eds.setRiskDangerId(dangerSource.getParentRiskDangerId());
                eds.setIsLeaf(1);
                eds.setRiskDangerLevel(4);
                //修改风险等级 确定为叶子节点
                this.update(eds);
            }
            //根据父ID 查询最小风险等级 若为空 则返回风险源等级为-1
            Integer lv=riskSourceMapper.selLV1(dangerSource.getParentRiskDangerId(),(long)riskId);

            RiskSource eds = new RiskSource();
            eds.setRiskDangerId(dangerSource.getParentRiskDangerId());
            if (lv != -1) {
                if (dangerSource.getRiskDangerLevel() < lv) {
                    lv = dangerSource.getRiskDangerLevel();
                }

                eds.setRiskDangerLevel(lv);
            } else {
                eds.setRiskDangerLevel(4);
            }
            //修改风险源等级
            this.update(eds);
            //父级危险源ID与根节点相比较
            if (!dangerSource.getParentRiskDangerId().equals(dangerSource.getRootNode())) {

                Integer lv2= riskSourceMapper.selLV3(dangerSource.getRootNode(),(long)riskId);
                eds = new RiskSource();
                eds.setRiskDangerId(dangerSource.getRootNode());
                Integer lv1 = Integer.valueOf(lv2);
                if (lv != -1) {
                    if (lv < lv1) {
                        lv1 = lv;
                    }

                    eds.setRiskDangerLevel(lv1);
                } else {
                    eds.setRiskDangerLevel(4);
                }
                //修改危险源等级
                this.update(eds);
            }
            //修改风险点等级 风险数量
            this.updateRiskPoints(dangerSource, ifNotIn);
            return false;
        }
    }

    /**
     * 查看危险源等级
     * yfh
     * 2020/06/12
     * @return
     */
    @Override
    public List<Map<String, Object>> getRiskDangerLevel() {
        return dictMapper.getRiskDangerLevel();
    }



    private XSSFCell createCell(XSSFRow row, int colIndex, String cellVal) {
        XSSFCell cell = row.createCell(colIndex);
        setCellFont(cell);
        if (cellVal != null) {
            cell.setCellValue(cellVal);
        }

        return cell;
    }

    public static void setCellFont(XSSFCell cell) {
        XSSFCellStyle style = cell.getCellStyle();
        style.getAlignmentEnum();
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setWrapText(true);
        XSSFFont font = style.getFont();
        font.setFontHeightInPoints((short)16);
        font.setFontName("仿宋_GB2312");
        style.getBorderBottomEnum();
        style.setBorderTop(BorderStyle.THIN);
        style.getBorderBottomEnum();
        style.setBorderBottom(BorderStyle.THIN);
        style.getBorderBottomEnum();
        style.setBorderLeft(BorderStyle.THIN);
        style.getBorderBottomEnum();
        style.setBorderRight(BorderStyle.THIN);
    }
    /**
     * 添加岗位表信息 并返回岗位id
     * @param positionList
     * @param positionTitle
     * @param cId
     * @param aId
     * @return
     * @throws Exception
     */
    public Long getPositionId(List<Position> positionList, String positionTitle, Long cId, Long aId) throws Exception {
        Long positionId = null;
        positionList = this.positionMapper.getPositionListById(cId);
        if (!positionList.isEmpty()) {
            positionList.forEach((t) -> {
                position.put(t.getPositionTitle(), t.getPositionId());
            });
            positionId = RiskSourceServiceImpl.position.getLong(positionTitle);
        }

        if (positionId == null) {
            Long pId =dictService.getTabId("sys_position");
            Position position = new Position();
            position.setCompanyId(cId);
            position.setPositionTitle(positionTitle);
            position.setPositionId(pId);
            position.setCreatedBy(aId);
            position.setCreatedTime(DateUtil.date());
            position.setUpdatedBy(aId);
            position.setUpdatedTime(DateUtil.date());
            position.setPositionOrder("1");
            position.setDelFlag(0);
            positionMapper.add(position);
            positionList.add(position);
            positionId = pId;
        }

        return positionId;
    }

    /**
     * 获取风险管控层级id
     * @param entRiskCtrlLevels
     * @param ctrlLevel
     * @return
     */
    public Long ctrlLevelId(List<RiskCtrlLevel> entRiskCtrlLevels, String ctrlLevel) {
        Long ctrlLevelId = 0L;
        if (!entRiskCtrlLevels.isEmpty()) {
            for(int a=0;a<entRiskCtrlLevels.size();a++){
                Map map=(Map) entRiskCtrlLevels.get(a);
                String riskCtrlLevelValue = map.get("riskCtrlLevelValue").toString();
                Long riskCtrlLevelId = (long)map.get("riskCtrlLevelId");
                entRiskCtrlLevel.put(riskCtrlLevelValue,riskCtrlLevelId);
            }
            ctrlLevelId = entRiskCtrlLevel.getLong(ctrlLevel);
        }

        return ctrlLevelId;
    }

    private boolean getOneEntRiskCode(Long cId, String riskCode) {
        Risk er = (Risk)riskMapper.getOne(riskCode,cId);
        return er != null;
    }

    /**
     * 根据风险等级名称 获取风险等级Code码
     * @param lvName
     * @return
     */
    public Integer dangerSrcLv(String lvName) {
        Integer code = 4;
        String red = "红";
        String org = "橙";
        String yel = "黄";
        String blue = "蓝";
        if (red.equals(lvName)) {
            code = 1;
        } else if (org.equals(lvName)) {
            code = 2;
        } else if (yel.equals(lvName)) {
            code = 3;
        } else if (blue.equals(lvName)) {
            code = 4;
        }

        return code;
    }

    /**
     * 根据根危险源名称 获取根危险源Code码
     * @param dangerName
     * @return
     */
    public String dangerSrcCode(String dangerName) {
        String code = "";
        String A = "路段";
        String B = "路口";
        String C = "涵洞";
        String D = "桥梁";
        String E = "车辆";
        String F = "隧道";
        String G = "驾驶员";
        String H = "作业活动";
        String I = "设备设施";
        if (A.equals(dangerName)) {
            code = "LU_DUAN";
        } else if (B.equals(dangerName)) {
            code = "LU_KOU";
        } else if (C.equals(dangerName)) {
            code = "HAN_DONG";
        } else if (D.equals(dangerName)) {
            code = "QIAO_LIANG";
        } else if (E.equals(dangerName)) {
            code = "CAR";
        } else if (F.equals(dangerName)) {
            code = "SUIDAO";
        } else if (G.equals(dangerName)) {
            code = "DRIVER";
        } else if (H.contains(dangerName)) {
            code = "ZY_GANGWEI";
        } else if (I.equals(dangerName)) {
            code = "SHE_BEI";
        } else {
            code = "SHE_BEI";
        }

        return code;
    }

    public Long getNextId(Long sId, Integer count) {
        Long outId = sId + (long)count;
        return outId;
    }

    /**
     * 添加危险源
     * @param id
     * @param pId
     * @param rId
     * @param code
     * @param lv
     * @param isLeaf
     * @param dangerSrcName
     * @param cId
     * @param riskDangerType
     * @param dangerSrcLv
     * @return
     */
    public RiskSource buildRiskSource(Long id, Long pId, Long rId, String code, Integer lv, Integer isLeaf, String dangerSrcName, Long cId, Integer riskDangerType, Integer dangerSrcLv) {
        RiskSource riskSource = new RiskSource();
        riskSource.setRiskDangerId(id);
        riskSource.setRiskDangerName(dangerSrcName);
        riskSource.setCompanyId(cId);
        riskSource.setRiskDangerCode(code);
        riskSource.setParentRiskDangerId(pId);
        if (dangerSrcLv.equals(0)) {
            riskSource.setRiskDangerLevel(4);
        } else {
            riskSource.setRiskDangerLevel(dangerSrcLv);
        }

        riskSource.setRootNode(rId);
        riskSource.setNodeLevel(lv);
        riskSource.setIsLeaf(isLeaf);
        riskSource.setRiskDangerType(riskDangerType);
        riskSource.setCreatedTime(new Date());
        riskSource.setDelFlag(0);
        return riskSource;
    }

    /**
     * 新增风险
     * @param json
     * @param userId
     * @param sid
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public R addEntRisk(JSONObject json, Long userId,Long sid) {
        try {
            Long cId = json.getLong("companyId");
            RiskSource riskSource = JSON.toJavaObject(json, RiskSource.class);
            Risk risk = JSON.toJavaObject(json, Risk.class);
            if(null == risk.getRiskCode() || StringUtils.isBlank(risk.getRiskCode()) || StringUtils.isEmpty(risk.getRiskCode())){
                return R.error("风险编码不能为空！");
            }
            if(null == json.getLong("riskCtrlLevelId") ){
                return R.error("风险管控层级Id不能为空！");
            }
            //根据cid 和风险编码 查询 符合的风险表信息
            List<Risk> risks = riskSourceMapper.selectRisks(cId, risk.getRiskCode(),null);
                Long id = 0L;
                Long parentDangerSrcId = json.getLong("riskDangerId");
                RiskSource dangerSource = this.getById(parentDangerSrcId);
                if(risk.getRiskId()!=null){
                    // 编辑
                    if(null != risks && (risks.size()>1 ||risk.getRiskId().longValue() != risks.get(0).getRiskId().longValue() )){
                        return R.error("风险编码已存在");
                    }
                    id = risk.getRiskId();
                    riskSource.setRiskDangerId(id);
                    riskSource.setRiskDangerName(risk.getRiskName());
                    riskSource.setParentRiskDangerId(parentDangerSrcId);
                    riskSource.setRootNode(dangerSource.getRootNode());
                    riskSource.setUpdatedBy(userId);
                    riskSource.setUpdatedTime(new Date());
                    riskSource.setRiskDangerCode(risk.getRiskCode());
                    riskSource.setRiskDangerLevel(risk.getRiskLevel());
                    this.update(riskSource);
                    this.updateLvAndIsLeaf(id, json.getLong("companyId"));
                    risk.setUpdatedBy(userId);
                    risk.setUpdatedTime(new Date());
                    riskMapper.update(risk);
                }else{
                    if(risks.size()>0){
                        return R.error("风险编码已存在");
                    }
                    id=sid;
                    riskSource.setRiskDangerId(id);
                    riskSource.setRiskDangerName(risk.getRiskName());
                    riskSource.setNodeLevel(3);
                    riskSource.setIndustryId(1L);
                    riskSource.setIsLeaf(1);
                    riskSource.setParentRiskDangerId(parentDangerSrcId);
                    riskSource.setRootNode(dangerSource.getRootNode());
                    riskSource.setCreatedBy(userId);
                    riskSource.setCreatedTime(new Date());
                    riskSource.setRiskDangerCode(risk.getRiskCode());
                    riskSource.setRiskDangerLevel(risk.getRiskLevel());
                    riskSource.setDelFlag(0);
                    riskSourceMapper.add(riskSource);
                    Integer riskTypeId = Constant.RISK_TYPE_ZHUANYE;
                    risk.setCompanyId(json.getLong("companyId"));
                    risk.setRiskTypeId((long)riskTypeId);
                    risk.setRiskId(id);
                    risk.setRiskDangerId(id);
                    risk.setCreatedBy(userId);
                    risk.setCreatedTime(new Date());
                    risk.setDelFlag(0);
                    this.riskMapper.add(risk);
                    this.updateLvAndIsLeaf(id, json.getLong("companyId"));
                }
                json.put("riskId", id);
                json.put("createdBy", userId);
                this.riskCtrlService.updateEntRiskCtrl(json);
                riskMeasureMapper.del(id,json.getLong("companyId"));
                List<RiskMeasure> measureList = new ArrayList();
                String regex = "@#@";
                if (json.getString("riskMeasureType1") != null && !"".equals(json.getString("riskMeasureType1"))) {
                    measureList = this.buildList(sid,json.getString("riskMeasureType1"), 1, json.getLong("companyId"), id, userId, (List)measureList, regex);
                }

                if (json.getString("riskMeasureType2") != null && !"".equals(json.getString("riskMeasureType2"))) {
                    measureList = this.buildList(sid,json.getString("riskMeasureType2"), 2, json.getLong("companyId"), id, userId, (List)measureList, regex);
                }

                if (json.getString("riskMeasureType3") != null && !"".equals(json.getString("riskMeasureType3"))) {
                    measureList = this.buildList(sid,json.getString("riskMeasureType3"), 3, json.getLong("companyId"), id, userId, (List)measureList, regex);
                }

                if (json.getString("riskMeasureType4") != null && !"".equals(json.getString("riskMeasureType4"))) {
                    measureList = this.buildList(sid,json.getString("riskMeasureType4"), 4, json.getLong("companyId"), id, userId, (List)measureList, regex);
                }

                if (json.getString("riskMeasureType5") != null && !"".equals(json.getString("riskMeasureType5"))) {
                    measureList = this.buildList(sid,json.getString("riskMeasureType5"), 5, json.getLong("companyId"), id, userId, (List)measureList, regex);
                }
                if (measureList.size() > 0) {
                    this.riskMeasureMapper.saveBatch(measureList);
                }
                return R.ok("添加成功");
        } catch (Exception var12) {
            var12.printStackTrace();
            return R.error("添加失败");
        }
    }

    public List<RiskMeasure> buildList(Long sid,String content, Integer typeId, Long cId, Long id, Long aId, List<RiskMeasure> list, String regex) {
        return this.buildList(sid,content, typeId, cId, id, aId, list, regex, true);
    }

    /**
     * 返回不同类型管控措施数据
     * @param sid
     * @param content
     * @param typeId
     * @param cId
     * @param id
     * @param aId
     * @param list
     * @param regex
     * @param idFlag
     * @return
     */
    public List<RiskMeasure> buildList(Long sid,String content, Integer typeId, Long cId, Long id, Long aId, List<RiskMeasure> list, String regex, boolean idFlag) {
        try {
            if (regex.equals(";")) {
                content = content.replace("；", ";");
            }

            String[] riskMeasureType = content.split(regex);
            if (riskMeasureType.length > 0) {
                for(int i = 0; i < riskMeasureType.length; ++i) {
                    RiskMeasure riskMeasure = new RiskMeasure();
                    if (idFlag) {
                        riskMeasure.setRiskMeasureId((long)sid);
                    }

                    riskMeasure.setCompanyId(cId);
                    riskMeasure.setRiskId(id);
                    riskMeasure.setRiskMeasureTypeId(typeId);
                    riskMeasure.setRiskMeasureContent(riskMeasureType[i]);
                    riskMeasure.setCreatedTime(new Date());
                    riskMeasure.setCreatedBy(aId);
                    riskMeasure.setUpdatedBy(aId);
                    riskMeasure.setUpdatedTime(new Date());
                    riskMeasure.setDelFlag(0);
                    list.add(riskMeasure);
                }
            }
        } catch (Exception var12) {
            var12.printStackTrace();
        }

        return list;
    }

    public boolean updateLvAndIsLeaf(Long dangerSrcId, Long cId) {
        return this.updateLvAndIsLeaf(dangerSrcId, cId, false);
    }

    /**
     * 修改风险源等级
     * @param dangerSrcId
     * @param cId
     * @param ifNotIn
     * @return
     */
    public boolean updateLvAndIsLeaf(Long dangerSrcId, Long cId, boolean ifNotIn) {
        //根据id获取危险源对象信息
        RiskSource dangerSource = (RiskSource)this.getById(dangerSrcId);
        if (dangerSource == null) {
            return true;
        } else {
            //根据父ID 查询危险源信息
            List<RiskSource> EntDangerSourceCount = riskSourceMapper.count(dangerSource.getParentRiskDangerId());
           if (EntDangerSourceCount.size() == 0) {
               RiskSource eds = new RiskSource();
                eds.setRiskDangerId(dangerSource.getParentRiskDangerId());
                eds.setIsLeaf(1);
                eds.setRiskDangerLevel(4);
                //修改风险等级 确定为叶子节点
                this.update(eds);
            }
           //根据父ID 查询最小风险等级 若为空 则返回风险源等级为-1
            Integer lv=riskSourceMapper.selLV(dangerSource.getParentRiskDangerId());

            RiskSource eds = new RiskSource();
            eds.setRiskDangerId(dangerSource.getParentRiskDangerId());
            if (lv != -1) {
                if (dangerSource.getRiskDangerLevel() < lv) {
                    lv = dangerSource.getRiskDangerLevel();
                }

                eds.setRiskDangerLevel(lv);
            } else {
                eds.setRiskDangerLevel(4);
            }
            //修改风险源等级
            this.update(eds);
            //父级危险源ID与根节点相比较
            if (!dangerSource.getParentRiskDangerId().equals(dangerSource.getRootNode())) {

               Integer lv2= riskSourceMapper.selLV2(dangerSource.getRootNode());
                eds = new RiskSource();
                eds.setRiskDangerId(dangerSource.getRootNode());
                Integer lv1 = Integer.valueOf(lv2);
                if (lv != -1) {
                    if (lv < lv1) {
                        lv1 = lv;
                    }

                    eds.setRiskDangerLevel(lv1);
                } else {
                    eds.setRiskDangerLevel(4);
                }
                //修改危险源等级
                this.update(eds);
            }
            //修改风险点等级 风险数量
            this.updateRiskPoint(dangerSource, ifNotIn);
            return false;
        }
    }

    /**
     * 修改风险点等级 风险数量
     * @param dangerSource
     * @param ifNotIn
     */
    public void updateRiskPoint(RiskSource dangerSource, boolean ifNotIn) {

        List<Map<String, Object>> mapList = this.riskPointDangerSourceMapper.listMaps(dangerSource.getParentRiskDangerId(),dangerSource.getCompanyId());
        if (!mapList.isEmpty()) {

            List<RiskSource> sourceList = riskSourceMapper.sourceList(dangerSource.getParentRiskDangerId(),dangerSource.getCompanyId());
            List<RiskRecord> addEntRiskRecord = new ArrayList();
            List<Long> riskPointIds = new ArrayList();
            mapList.forEach((item) -> {
                JSONObject json = new JSONObject(item);
                Long riskPointId = json.getLong("risk_point_id");
                sourceList.forEach((source) -> {
                    RiskRecord entRiskRecord = new RiskRecord();
                    entRiskRecord.setRiskId(source.getRiskDangerId());
                    entRiskRecord.setRiskPointId(riskPointId);
                    entRiskRecord.setCompanyId(source.getCompanyId());
                    entRiskRecord.setRiskDangerId(source.getParentRiskDangerId());
                    entRiskRecord.setRiskName(source.getRiskDangerName());
                    addEntRiskRecord.add(entRiskRecord);
                });
                riskPointIds.add(riskPointId);
            });
            if (!addEntRiskRecord.isEmpty()) {
                //删除风险点关联风险表信息
                this.riskRecordMapper.remove(dangerSource.getParentRiskDangerId(),riskPointIds);
                //保存风险点关联风险表信息
                this.riskRecordMapper.saveBatch(addEntRiskRecord);
            }
        }
        List<Map<String, Object>> maps = riskRecordMapper.getRiskPointByDangerId(dangerSource.getParentRiskDangerId());
        if (!maps.isEmpty()) {
            List<RiskPoint> list = new ArrayList();
            maps.forEach((item) -> {
                RiskPoint riskPoint = new RiskPoint();
                riskPoint.setRiskPointId(Long.valueOf(item.get("riskPointId").toString()));
                if (item.get("lv") != null) {
                    riskPoint.setRiskPointLevel(Integer.valueOf(item.get("lv").toString()));
                } else {
                    riskPoint.setRiskPointLevel(4);
                }

                riskPoint.setRiskNumber(Integer.valueOf(item.get("riskCount").toString()));
                list.add(riskPoint);
            });
            //修改风险点等级 风险数量
            this.riskPointMapper.updateBatchById(list);
        }
    }

    /**
     * 修改风险点等级 风险数量
     * @param dangerSource
     * @param ifNotIn
     */
    public void updateRiskPoints(RiskSource dangerSource, boolean ifNotIn) {

        List<Map<String, Object>> mapList = this.riskPointDangerSourceMapper.listMaps(dangerSource.getParentRiskDangerId(),dangerSource.getCompanyId());
        if (!mapList.isEmpty()) {

            List<RiskSource> sourceList = riskSourceMapper.sourceLists(dangerSource.getParentRiskDangerId(),dangerSource.getCompanyId(),dangerSource.getRiskDangerId());
            List<RiskRecord> addEntRiskRecord = new ArrayList();
            List<Long> riskPointIds = new ArrayList();
            mapList.forEach((item) -> {
                JSONObject json = new JSONObject(item);
                Long riskPointId = json.getLong("riskPointId");
                sourceList.forEach((source) -> {
                    RiskRecord entRiskRecord = new RiskRecord();
                    entRiskRecord.setRiskId(source.getRiskDangerId());
                    entRiskRecord.setRiskPointId(riskPointId);
                    entRiskRecord.setCompanyId(source.getCompanyId());
                    entRiskRecord.setRiskDangerId(source.getParentRiskDangerId());
                    entRiskRecord.setRiskName(source.getRiskDangerName());
                    addEntRiskRecord.add(entRiskRecord);
                });
                riskPointIds.add(riskPointId);
            });
            if (!addEntRiskRecord.isEmpty()) {
                //删除风险点关联风险表信息
                this.riskRecordMapper.remove(dangerSource.getParentRiskDangerId(),riskPointIds);
                //保存风险点关联风险表信息
                this.riskRecordMapper.saveBatch(addEntRiskRecord);
            }
        }
        List<Map<String, Object>> maps = riskRecordMapper.getRiskPointByDangerId(dangerSource.getParentRiskDangerId());
        if (!maps.isEmpty()) {
            List<RiskPoint> list = new ArrayList();
            maps.forEach((item) -> {
                RiskPoint riskPoint = new RiskPoint();
                riskPoint.setRiskPointId(Long.valueOf(item.get("riskPointId").toString()));
                if (item.get("lv") != null) {
                    riskPoint.setRiskPointLevel(Integer.valueOf(item.get("lv").toString()));
                } else {
                    riskPoint.setRiskPointLevel(4);
                }

                riskPoint.setRiskNumber(Integer.valueOf(item.get("riskCount").toString()));
                list.add(riskPoint);
            });
            //修改风险点等级 风险数量
            this.riskPointMapper.updateBatchById(list);
        }
    }

    /**
     * 去除小数点后面0
     * @param result
     * @return
     */
    public static String subNumberText(String result) {
        if (result.contains(".")) {// 是小数
            while (true) {
                if (result.charAt(result.length() - 1) == '0'){
                    result = result.substring(0, result.length() - 1);
                }else{
                    if (result.endsWith(".")) {
                        result = result.substring(0, result.length() - 1);
                    }
                    break;
                }

            }

        }
        return result;
    }

}
