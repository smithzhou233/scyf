package com.hngf.service.sys.impl;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Map;
import java.util.List;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hngf.common.utils.PageUtils;
import com.hngf.common.utils.Constant;

import com.hngf.mapper.sys.CommonClassifyMapper;
import com.hngf.entity.sys.CommonClassify;
import com.hngf.service.sys.CommonClassifyService;


@Service("CommonClassifyService")
public class CommonClassifyServiceImpl implements CommonClassifyService {

    @Autowired
    private CommonClassifyMapper CommonClassifyMapper;
    /**
     * @Author: zyj
     * @Description:默认渲染列表数据
     * @Date 14:48 2020/5/22
     */
    @Override
    public PageUtils queryPage(Map<String, Object> params,Integer pageNum,Integer pageSize,String order) {
        //利用PageHelper分页查询 注意：这个一定要放查询语句的前一行,否则无法进行分页,因为它对紧随其后第一个sql语句有效
        if(StringUtils.isNotBlank(order)){
            PageHelper.startPage(pageNum, pageSize,order);
        }else{
            PageHelper.startPage(pageNum, pageSize);
        }
        List<CommonClassify> list = CommonClassifyMapper.findList(params);
        PageInfo<CommonClassify> pageInfo = new PageInfo<CommonClassify>(list);
        return new PageUtils(list,(int)pageInfo.getTotal(),pageInfo.getPages(),pageInfo.getPageSize(),pageInfo.getPrePage() + 1);
    }

    @Override
    public List<CommonClassify> getByClassifyByType(Map<String, Object> params) {
        return CommonClassifyMapper.findByClassifyByType(params);
    }

    /**
     * @Author: zyj
     * @Description:通过classifyId获取公司通用分类表数据
     * @Date 14:48 2020/5/22
     */
    @Override
    public CommonClassify getById(Long id){
        return CommonClassifyMapper.findById(id);
    }
    /**
     * @Author: zyj
     * @Description:保存信息
     * @Date 14:48 2020/5/22
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(CommonClassify CommonClassify) {
        CommonClassifyMapper.add(CommonClassify);
    }
    /**
     * @Author: zyj
     * @Description:修改信息
     * @Date 14:48 2020/5/22
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(CommonClassify CommonClassify) {
        CommonClassifyMapper.update(CommonClassify);
    }
    /**
     * @Author: zyj
     * @Description:批量删除信息
     * @Date 14:48 2020/5/22
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeByIds(List ids) {
        CommonClassifyMapper.deleteByIds(ids);
    }
    /**
     * @Author: zyj
     * @Description:删除信息
     * @Date 14:48 2020/5/22
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeById(Long id) {
        CommonClassifyMapper.deleteById(id);
    }

}
