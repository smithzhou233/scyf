package com.hngf.service.risk.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hngf.common.utils.Constant;
import com.hngf.common.utils.PageUtils;
import com.hngf.entity.risk.RiskCtrl;
import com.hngf.mapper.risk.RiskCtrlMapper;
import com.hngf.service.risk.RiskCtrlService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;



@Service("RiskCtrlService")
public class RiskCtrlServiceImpl implements RiskCtrlService {

    @Autowired
    private RiskCtrlMapper RiskCtrlMapper;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        int pageNum = null!= params.get("pageNum") ? Integer.parseInt(params.get("pageNum").toString()) : 1;
        int pageSize = null != params.get("pageSize") ? Integer.parseInt(params.get("pageSize").toString()) : Constant.PAGE_SIZE;
        //利用PageHelper分页查询 注意：这个一定要放查询语句的前一行,否则无法进行分页,因为它对紧随其后第一个sql语句有效
        PageHelper.startPage(pageNum, pageSize);
        List<RiskCtrl> list = RiskCtrlMapper.findList(params);
        PageInfo<RiskCtrl> pageInfo = new PageInfo<RiskCtrl>(list);
        return new PageUtils(list,(int)pageInfo.getTotal(),pageInfo.getPages(),pageInfo.getPageSize(),pageInfo.getPrePage() + 1);
    }

    @Override
    public RiskCtrl getById(Long id){
        return RiskCtrlMapper.findById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(RiskCtrl RiskCtrl) {
        RiskCtrlMapper.add(RiskCtrl);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(RiskCtrl RiskCtrl) {
        RiskCtrlMapper.update(RiskCtrl);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeByIds(List ids) {
        RiskCtrlMapper.deleteByIds(ids);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeById(Long id) {
        RiskCtrlMapper.deleteById(id);
    }

    /**
     * 修改风险管控岗位
     * @param paras
     */
    @Override
    public void updateEntRiskCtrl(JSONObject paras) {
        if(StringUtils.isNotEmpty(paras.getLong("riskId").toString())){
            RiskCtrlMapper.delRiskCtrl(paras.getLong("riskId"),paras.getLong("companyId"));
        }
        String riskCtrlPositionId = paras.getString("riskCtrlPositionId");
        if (StringUtils.isNotEmpty(riskCtrlPositionId.toString()) && riskCtrlPositionId.split(",").length > 0) {
            String[] rcps = riskCtrlPositionId.split(",");
            paras.remove("riskCtrlPositionId");
            for(int i = 0; i < rcps.length; ++i) {
                RiskCtrl riskCtrl = (RiskCtrl) JSON.toJavaObject(paras, RiskCtrl.class);
                riskCtrl.setRiskId(paras.getLong("riskId"));
                Long ctrlPositionId;
                if ("".equals(rcps[i])) {
                    ctrlPositionId = -1L;
                } else {
                    ctrlPositionId = Long.valueOf(rcps[i]);
                }
                riskCtrl.setRiskCtrlPositionId(ctrlPositionId);
                riskCtrl.insertPrefixInit(paras.getLong("createdBy"),paras.getLong("companyId"));
                this.save(riskCtrl);
            }
        }
    }

    /**
     * 根据岗位ID 企业ID查看管控层级
     * yfh
     * 2020/06/11
     * @param params
     * @return
     */
    @Override
    public Integer getCurrentUserCtlLevel(Map<String, Object> params) {
        return RiskCtrlMapper.getCurrentUserCtlLevel(params);
    }
}
