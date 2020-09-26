package com.hngf.service.risk.impl;

import com.alibaba.fastjson.JSONObject;
import com.hngf.common.utils.R;
import com.hngf.mapper.risk.*;
import com.hngf.mapper.sys.EvaluateIndexMapper;
import com.hngf.service.risk.RiskSourceService;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import java.util.Map;
import java.util.List;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hngf.common.utils.PageUtils;
import com.hngf.common.utils.Constant;

import com.hngf.entity.risk.Risk;
import com.hngf.service.risk.RiskService;


@Service("RiskService")
public class RiskServiceImpl implements RiskService {

    @Autowired
    private RiskMapper RiskMapper;
    @Autowired
    private  RiskMeasureMapper riskMeasureMapper;
    @Autowired
    private RiskCtrlMapper riskCtrlMapper;
    @Autowired
    private RiskRecordMapper riskRecordMapper;
    @Autowired
    private RiskSourceService riskSourceService;
    @Autowired
    private RiskSourceMapper riskSourceMapper;
    /**
     * 查询风险定义表数据
     * yfh
     * 2020/05/27
     * @param params
     * @return
     */
    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        int pageNum = null!= params.get("pageNum") ? Integer.parseInt(params.get("pageNum").toString()) : 1;
        int pageSize = null != params.get("pageSize") ? Integer.parseInt(params.get("pageSize").toString()) : Constant.PAGE_SIZE;
        //利用PageHelper分页查询 注意：这个一定要放查询语句的前一行,否则无法进行分页,因为它对紧随其后第一个sql语句有效
        PageHelper.startPage(pageNum, pageSize);
        List<Map<String,Object>> list = RiskMapper.findList(params);
        for(int i=0;i<list.size();i++){
            Map map=list.get(i);
            //当数据没有LEC/LC/LS
            String typeId="3";
            if(map.containsKey("typeId")){
                typeId=map.get("typeId").toString();
            }
            if(typeId.equals("0")){//LEC
                String lecL=subNumberText(map.get("lecL").toString());
                String lecE=subNumberText(map.get("lecE").toString());
                String lecC=subNumberText(map.get("lecC").toString());
                String lecD=subNumberText(map.get("lecD").toString());
                map.put("lecL",lecL);
                map.put("lecE",lecE);
                map.put("lecC",lecC);
                map.put("lecD",lecD);
            }
            if(typeId.equals("1")){//LC
                String lcL=subNumberText(map.get("lcL").toString());
                String lcC=subNumberText(map.get("lcC").toString());
                String lcD=subNumberText(map.get("lcD").toString());
                map.put("lcL",lcL);
                map.put("lcC",lcC);
                map.put("lcD",lcD);
            }
            if(typeId.equals("2")){//LS
                String lsL=subNumberText(map.get("lsL").toString());
                String lsS=subNumberText(map.get("lsS").toString());
                String lsR=subNumberText(map.get("lsR").toString());
                map.put("lsL",lsL);
                map.put("lsS",lsS);
                map.put("lsR",lsR);
            }

        }
        PageInfo pageInfo = new PageInfo(list);
        return new PageUtils(list,(int)pageInfo.getTotal(),pageInfo.getPages(),pageInfo.getPageSize(),pageInfo.getPrePage() + 1);
    }

    @Override
    public Risk getById(Long id){
        return RiskMapper.findById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(Risk Risk) {
        RiskMapper.add(Risk);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(Risk Risk) {
        RiskMapper.update(Risk);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeByIds(List ids) {
        RiskMapper.deleteByIds(ids);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeById(Long id) {
        RiskMapper.deleteById(id);
    }

    @Override
    public List<Map<String, Object>> selectLevel(Long companyId) {
        return RiskMapper.selectLevel(companyId);
    }

    @Override
    public List<Map<String, Object>> selectPosition(Long companyId) {
        return RiskMapper.selectPosition(companyId);
    }

    /**
     * 查看登录公司属于 LS或LEC
     * yfh
     * 2020/05/29
     * @param companyId
     * @return
     */
    @Override
    public Integer selModel(Long companyId) {
        return RiskMapper.selModel(companyId);
    }

    /**
     * 查看风险等级
     * yfh
     * 2020/05/29
     * @param evaluateIndexModel
     * @param companyId
     * @return
     */
    @Override
    public List<Map<String, Object>> selectComEvaluate(String evaluateIndexModel, Long companyId) {
        return RiskMapper.selectComEvaluate(evaluateIndexModel,companyId);
    }
    @Override
    public List<Map<String, Object>> selectAEvaluate(String evaluateIndexType, Long companyId) {
        return RiskMapper.selectAEvaluate(evaluateIndexType,companyId);
    }

    /**
     * 查看风险管控层级
     * yfh
     * 2020/05/29
     * @param riskCtrlLevelValue
     * @param companyId
     * @return
     */
    @Override
    public List<Map<String, Object>> riskPositionLevel(Integer riskCtrlLevelValue, Long companyId) {
        return RiskMapper.riskPositionLevel(riskCtrlLevelValue,companyId);
    }

    @Override
    public Map<String, Object> getRiskJson(Long riskId, Long companyId) {
        Map<String, Object> riskJson = RiskMapper.getRiskJson(riskId);
        List<Map<String, Object>> entRiskMeasures = riskMeasureMapper.listMaps(riskId,companyId);
        riskJson.put("entRiskMeasures", entRiskMeasures);
        return riskJson;
    }

    @Override
    public List<Map<String, Object>> queryRisk(JSONObject json) {
        return RiskMapper.queryRisk(json);
    }
    @Override
    public List<Map<String, Object>> queryARisk(JSONObject json) {
        return RiskMapper.queryARisk(json);
    }
    /**
     * 删除风险
     * yfh
     * 2020/06/08
     * @param riskId
     * @param companyId
     */
    @Override
    public R deleteRisk(Integer riskId, Long companyId) {
        try {
            RiskMapper.remove(riskId,companyId);
            riskCtrlMapper.deleteById((long)riskId);
            riskRecordMapper.delete(riskId,companyId);
            riskMeasureMapper.del((long)riskId,companyId);
            riskSourceService.updateLvAndLeaf(riskId, companyId, true);
            riskSourceMapper.removeRiskSource(riskId,companyId);
        } catch (Exception var9) {
            var9.printStackTrace();
            return R.error();
        }
        return null;
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
