package com.hngf.service.common.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.hngf.common.utils.Constant;
import com.hngf.common.utils.PageUtils;
import com.hngf.dto.commonPost.CommonPostDto;
import com.hngf.entity.common.CommonPost;
import com.hngf.mapper.common.CommonPostMapper;
import com.hngf.service.common.CommonPostService;
import com.hngf.service.risk.RiskPointGuideRelService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;


@Service("CommonPostService")
public class CommonPostServiceImpl implements CommonPostService {

    @Autowired
    private CommonPostMapper commonPostMapper;
    @Autowired
    private RiskPointGuideRelService riskPointGuideRelService;

    @Override
    public PageUtils queryPage(Map<String, Object> params,Integer pageNum,Integer pageSize,String order) {
        //利用PageHelper分页查询 注意：这个一定要放查询语句的前一行,否则无法进行分页,因为它对紧随其后第一个sql语句有效
        if(StringUtils.isNotBlank(order)){
            PageHelper.startPage(pageNum, pageSize,order);
        }else{
            PageHelper.startPage(pageNum, pageSize);
        }
        List<CommonPost> list = commonPostMapper.findList(params);
        PageInfo<CommonPost> pageInfo = new PageInfo<CommonPost>(list);
        return new PageUtils(list,(int)pageInfo.getTotal(),pageInfo.getPages(),pageInfo.getPageSize(),pageInfo.getPrePage() + 1);
    }
    /**
     * @Author: zyj
     * @Description:查询风险规章制度
     * @Param
     * @Date 9:43 2020/6/15
     */
    @Override
    public PageUtils queryInstitution(Map<String, Object> params, Integer pageNum, Integer pageSize, String order) {
        //利用PageHelper分页查询 注意：这个一定要放查询语句的前一行,否则无法进行分页,因为它对紧随其后第一个sql语句有效
        if(StringUtils.isNotBlank(order)){
            PageHelper.startPage(pageNum, pageSize,order);
        }else{
            PageHelper.startPage(pageNum, pageSize);
        }
        List<CommonPostDto> list = commonPostMapper.queryInstitution(params);
        PageInfo<CommonPostDto> pageInfo = new PageInfo<CommonPostDto>(list);
        return new PageUtils(list,(int)pageInfo.getTotal(),pageInfo.getPages(),pageInfo.getPageSize(),pageInfo.getPrePage() + 1);
    }

    @Override
    public PageUtils getWorkInstruction(Map<String, Object> params) {
        String showType = params.get("showType").toString().trim();
        //从风险点作业指导书关系表中查询风险点关联的作业指导书ID，并放入查询条件
        List<Long> postIds = this.riskPointGuideRelService.getPostIdsByRiskPoint(Long.parseLong(params.get("riskPointId").toString()));
        if (null != postIds && postIds.size() > 0) {
            params.put("postIds", postIds);
        }
        //查询未选择指导书，分页查询
        if ("select".equals(showType)) {//查询全部指导书
            int pageNum = null!= params.get(Constant.PAGE_PAGENUM) ? Integer.parseInt(params.get(Constant.PAGE_PAGENUM).toString()) : 1;
            int pageSize = null != params.get(Constant.PAGE_PAGESIZE) ? Integer.parseInt(params.get(Constant.PAGE_PAGESIZE).toString()) : Constant.PAGE_SIZE;
            return this.queryPage(params,pageNum,pageSize,null);
        }else{//查询所选择指导书
            List<CommonPost> list = Lists.newArrayList();
            if (null != postIds && postIds.size() > 0) {
                list = this.commonPostMapper.findList(params);
            }
            return new PageUtils(list,list.size(),1,0,1);
        }

    }

    @Override
    public CommonPost getById(Long id){
        return commonPostMapper.findById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(CommonPost CommonPost) {
        commonPostMapper.add(CommonPost);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(CommonPost CommonPost) {
        commonPostMapper.update(CommonPost);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeByIds(List ids) {
        commonPostMapper.deleteByIds(ids);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeById(Long id) {
        commonPostMapper.deleteById(id);
    }

    /**
     * 发布知识点
     * @param ids 知识点id集合
     * @param publishBy  发布人id
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int publishByIds(List ids, Long publishBy){
        return commonPostMapper.publishByIds(ids,publishBy);
    }
}
