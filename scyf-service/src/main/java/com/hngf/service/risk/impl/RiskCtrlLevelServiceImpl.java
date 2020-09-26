package com.hngf.service.risk.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hngf.common.exception.ScyfException;
import com.hngf.common.utils.PageUtils;
import com.hngf.entity.risk.RiskCtrlLevel;
import com.hngf.mapper.risk.RiskCtrlLevelMapper;
import com.hngf.service.risk.RiskCtrlLevelService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;


@Service("RiskCtrlLevelService")
public class RiskCtrlLevelServiceImpl implements RiskCtrlLevelService {

    @Autowired
    private RiskCtrlLevelMapper riskCtrlLevelMapper;

    @Override
    /**
     * @Author: zyj
     * @Description:根据条件返回相应的列表
     * @Date 16:50 2020/5/21
     */
    public PageUtils queryPage(Map<String, Object> params,Integer pageNum,Integer pageSize,String order) {
        //利用PageHelper分页查询 注意：这个一定要放查询语句的前一行,否则无法进行分页,因为它对紧随其后第一个sql语句有效
        if(StringUtils.isNotBlank(order)){
            PageHelper.startPage(pageNum, pageSize,order);
        }else{
            PageHelper.startPage(pageNum, pageSize);
        }
        List<RiskCtrlLevel> list = riskCtrlLevelMapper.findList(params);
        PageInfo<RiskCtrlLevel> pageInfo = new PageInfo<RiskCtrlLevel>(list);
        return new PageUtils(list,(int)pageInfo.getTotal(),pageInfo.getPages(),pageInfo.getPageSize(),pageInfo.getPrePage() + 1);
    }
    /**
     * @Author: zyj
     * @Description:通过企业id查询统计是否存在
     * @Date 16:47 2020/5/21
     */
    @Override
     public Integer count(Long companyId){
       return riskCtrlLevelMapper.count(companyId);
     }
    /**
     * @Author: zyj
     * @Description:如果count为0初始化数据
     * @Date 16:59 2020/5/21
     */
    @Override
    public void initRiskCtrlLevel(Map<String,Object> params){
       Long companyId =Long.valueOf(params.get("companyId").toString());
       Long creatrBy=Long.valueOf(params.get("createBy").toString());
       List<RiskCtrlLevel> riskCtrlLevelList=new ArrayList<>();
        for (int i = 1; i < 5; i++) {
           try {
               RiskCtrlLevel riskCtrlLevel=new RiskCtrlLevel();
               riskCtrlLevel.setCompanyId(companyId);
               riskCtrlLevel.setRiskCtrlLevelValue(i);
               String title="";
               if (i == 1) {
                   title = "厂级";
               }

               if (i == 2) {
                   title = "车间级";
               }

               if (i == 3) {
                   title = "班组级";
               }

               if (i == 4) {
                   title = "岗位级";
               }
               riskCtrlLevel.setRiskCtrlLevelTitle(title);
               riskCtrlLevel.setRiskCtrlLevelDesc(title);
               riskCtrlLevel.setCreatedTime(new Date());
               riskCtrlLevel.setCreatedBy(creatrBy);
               riskCtrlLevel.setDelFlag(0);
               this.save(riskCtrlLevel);
           }catch (Exception exc){
                exc.printStackTrace();
           }
           /*if (!riskCtrlLevelList.isEmpty()){
               this.save(riskCtrlLevelList);
           }*/
        }
    }

    /**
     * 根据企业id 获取企业管控层级信息
     * @param cId
     * @return
     */
    @Override
    public List getRiskCtrlLevelList(Long cId) {
        return riskCtrlLevelMapper.getRiskCtrlLevelList(cId);
    }

    /**
     * @Author: zyj
     * @Description:根据id查询单条数据
     * @Date 14:40 2020/5/22
     */
    @Override
    public RiskCtrlLevel getById(Long id){
        return riskCtrlLevelMapper.findById(id);
    }

    /**
     * @Author: zyj
     * @Description:新增数据
     * @Date 14:40 2020/5/22
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(RiskCtrlLevel RiskCtrlLevel) {
        riskCtrlLevelMapper.add(RiskCtrlLevel);
    }

    /**
     * @Author: zyj
     * @Description:修改数据
     * @Date 14:40 2020/5/22
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(RiskCtrlLevel RiskCtrlLevel) {
        riskCtrlLevelMapper.update(RiskCtrlLevel);
    }

    /**
     * @Author: zyj
     * @Description:批量删除数据
     * @Date 14:40 2020/5/22
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeByIds(List ids) {
        riskCtrlLevelMapper.deleteByIds(ids);
    }

    /**
     * @Author: zyj
     * @Description:删除数据
     * @Date 14:40 2020/5/22
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeById(Long id, Long updatedBy) {
        Long usedCount = riskCtrlLevelMapper.getUsedCount(id);
        if(null == usedCount || 0 == usedCount.intValue() ){
            riskCtrlLevelMapper.deleteById(id, updatedBy);
        }else{
            throw new ScyfException("管控层级已被使用，不能删除");
        }
    }

    @Override
    public int deleteBatchByCompanyId(Long companyId, Long updatedBy) {
        return this.riskCtrlLevelMapper.deleteBatchByCompanyId(companyId, updatedBy);
    }


}
