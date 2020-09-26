package com.hngf.service.sys.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hngf.common.utils.PageUtils;
import com.hngf.entity.sys.Info;
import com.hngf.mapper.sys.InfoMapper;
import com.hngf.service.sys.InfoService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;




@Service("InfoService")
public class InfoServiceImpl implements InfoService {

    @Autowired
    private InfoMapper infoMapper;
    /**
     * @Author: zyj
     * @Description:查询系统信息表数据
     * @Date 14:53 2020/5/22
     */
    @Override
    public PageUtils queryPage(Map<String, Object> params,Integer pageNum,Integer pageSize,String order) {
        //利用PageHelper分页查询 注意：这个一定要放查询语句的前一行,否则无法进行分页,因为它对紧随其后第一个sql语句有效
        if(StringUtils.isNotBlank(order)){
            PageHelper.startPage(pageNum, pageSize,order);
        }else{
            PageHelper.startPage(pageNum, pageSize);
        }
        List<Info> list = infoMapper.findList(params);
        PageInfo<Info> pageInfo = new PageInfo<Info>(list);
        return new PageUtils(list,(int)pageInfo.getTotal(),pageInfo.getPages(),pageInfo.getPageSize(),pageInfo.getPrePage() + 1);
    }
    @Override
    /**
     * @Author: zyj
     * @Description:通过企业id查询数据如果没有添加一条数据
     * @Date 10:37 2020/5/22
     */
    public Info getList(Long companyId ,Integer userType){
      return this.getByCId(companyId, userType);
    }
    /**
     * @Author: zyj
     * @Description:通过id查询数据
     * @Date 14:53 2020/5/22
     */
    @Override
    public Info getById(Long id){
        return infoMapper.findById(id);
    }
    /**
     * @Author: zyj
     * @Description:保存信息
     * @Date 14:53 2020/5/22
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(Info Info) {
        infoMapper.add(Info);
    }
    /**
     * @Author: zyj
     * @Description:修改信息
     * @Date 14:53 2020/5/22
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(Info Info) {
        infoMapper.update(Info);
    }
    /**
     * @Author: zyj
     * @Description:批量删除信息
     * @Date 14:53 2020/5/22
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeByIds(List ids) {
        infoMapper.deleteByIds(ids);
    }
    /**
     * @Author: zyj
     * @Description:删除信息
     * @Date 14:53 2020/5/22
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeById(Long id) {
        infoMapper.deleteById(id);
    }

    @Override
    public Info getByCId(Long companyId, Integer userType) {
        int infoType = getInfoType(userType);
        if(-1 == infoType ) {
            return null ;
        }
        Info sysInfo = infoMapper.getByCId(companyId, infoType);
        if (sysInfo == null) {
            sysInfo = new Info(companyId, infoType);
            infoMapper.add(sysInfo);
        }

        return sysInfo;
    }
    private int getInfoType ( Integer userType ){
        if(null == userType ){
            return -1 ;
        }else if(2 == userType.intValue() ){
            return 1 ;
        }else if( 3 == userType.intValue() || 4== userType.intValue() ){
            return  0 ;
        }else{
            return  -1 ;
        }
    }
}
