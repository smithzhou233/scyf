package com.hngf.service.risk.impl;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import com.hngf.mapper.risk.RiskPointPersonLiableMapper;
import com.hngf.entity.risk.RiskPointPersonLiable;
import com.hngf.service.risk.RiskPointPersonLiableService;


@Service("RiskPointPersonLiableService")
public class RiskPointPersonLiableServiceImpl implements RiskPointPersonLiableService {

    @Autowired
    private RiskPointPersonLiableMapper riskPointPersonLiableMapper;


    @Override
    @Transactional(rollbackFor = Exception.class)
    public int save(RiskPointPersonLiable RiskPointPersonLiable) {
        return riskPointPersonLiableMapper.add(RiskPointPersonLiable);
    }

    @Override
    public int saveBatch(List<RiskPointPersonLiable> list) {
        return riskPointPersonLiableMapper.addForeach(list);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int removeByRiskPoint(Long riskPointId,Long companyId) {
        return riskPointPersonLiableMapper.deleteByRiskPoint(riskPointId,companyId);
    }
}
