package com.hngf.service.danger.impl;

import com.hngf.entity.danger.Hidden;
import com.hngf.entity.sys.Dict;
import com.hngf.mapper.sys.DictMapper;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hngf.common.utils.PageUtils;
import com.hngf.common.utils.Constant;

import com.hngf.mapper.danger.HiddenRecordMapper;
import com.hngf.entity.danger.HiddenRecord;
import com.hngf.service.danger.HiddenRecordService;


@Service("HiddenRecordService")
public class HiddenRecordServiceImpl implements HiddenRecordService {

    @Autowired
    private HiddenRecordMapper HiddenRecordMapper;
    @Autowired
    private DictMapper dictMapper;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        int pageNum = null!= params.get("pageNum") ? Integer.parseInt(params.get("pageNum").toString()) : 1;
        int pageSize = null != params.get("pageSize") ? Integer.parseInt(params.get("pageSize").toString()) : Constant.PAGE_SIZE;
        //利用PageHelper分页查询 注意：这个一定要放查询语句的前一行,否则无法进行分页,因为它对紧随其后第一个sql语句有效
        PageHelper.startPage(pageNum, pageSize);
        List<HiddenRecord> list = HiddenRecordMapper.findList(params);
        PageInfo<HiddenRecord> pageInfo = new PageInfo<HiddenRecord>(list);
        return new PageUtils(list,(int)pageInfo.getTotal(),pageInfo.getPages(),pageInfo.getPageSize(),pageInfo.getPrePage() + 1);
    }
    /**
     * @Author: zyj
     * @Description:隐患年度统计 表格 柱形图
     * @Param companyId 企业id groupId群组id year年份
     * @Date 11:32 2020/6/29
     */
    @Override
    public Map<String, Object> getHdangerYearStatistics(Long companyId, Long groupId, Integer year) {
        Map map=new HashMap();
        map.put("dictType","hidden_level");
        //查询隐患等级 1.重大 2.较大 3.一般 4.较低
        List<Dict> byMap = dictMapper.findByMap(map);
        List<Map<String, Object>> statistics = HiddenRecordMapper.getHdangerYearStatistics(companyId, groupId, year);
        Integer[] counts=new Integer[]{0,0,0,0,0,0};
        //柱形图y轴 重大
        Integer[] yAxis1=new Integer[]{0,0,0,0,0,0,0,0,0,0,0,0};
        //柱形图y轴 较大
        /*Integer[] yAxis2=new Integer[]{0,0,0,0,0,0,0,0,0,0,0,0};*/
        //柱形图y轴 一般
        Integer[] yAxis3=new Integer[]{0,0,0,0,0,0,0,0,0,0,0,0};
        //柱形图y轴 较低
        /*Integer[] yAxis4=new Integer[]{0,0,0,0,0,0,0,0,0,0,0,0};*/
        //先算列表显示统计数量
        if (statistics!=null){
            for (Map<String, Object> statistic : statistics) {
                if (byMap!=null) {
                    if (statistic.get("hdangerLevel") != null) {
                        //隐患等级
                        String hdangerLevel=statistic.get("hdangerLevel").toString();
                        //隐患状态
                        // 2：待整改；
                        //3：待验收；
                        //4：验收通过；
                        //5：验收不通过；
                        String status=statistic.get("status").toString();
                        if (byMap.get(0).getDictCode().equals(hdangerLevel)) {
                            counts[0] = counts[0]+Integer.valueOf(statistic.get("sumCount").toString());
                        }
                        /*if (byMap.get(1).getDictCode().equals(hdangerLevel)){
                            counts[1] = counts[1]+Integer.valueOf(statistic.get("sumCount").toString());
                        }*/
                        if (byMap.get(2).getDictCode().equals(hdangerLevel)){
                            counts[1] = counts[1]+Integer.valueOf(statistic.get("sumCount").toString());
                        }
                        /*if (byMap.get(3).getDictCode().equals(hdangerLevel)){
                            counts[3] = counts[3]+Integer.valueOf(statistic.get("sumCount").toString());
                        }*/
                        if (byMap.get(0).getDictCode().equals(hdangerLevel) && ("2".equals(status) ||"3".equals(status))){
                            counts[2]=counts[2]+Integer.valueOf(statistic.get("sumCount").toString());
                        }
                        /*if (byMap.get(1).getDictCode().equals(hdangerLevel) && ("2".equals(status) ||"3".equals(status))){
                            counts[5]=counts[5]+Integer.valueOf(statistic.get("sumCount").toString());
                        }*/
                        if (byMap.get(2).getDictCode().equals(hdangerLevel) && ("2".equals(status) ||"3".equals(status))){
                            counts[3]=counts[3]+Integer.valueOf(statistic.get("sumCount").toString());
                        }
                        /*if (byMap.get(3).getDictCode().equals(hdangerLevel) && ("2".equals(status) ||"3".equals(status))){
                            counts[7]=counts[7]+Integer.valueOf(statistic.get("sumCount").toString());
                        }*/
                        if (byMap.get(0).getDictCode().equals(hdangerLevel) && ("4".equals(status) ||"5".equals(status))){
                            counts[4]=counts[4]+Integer.valueOf(statistic.get("sumCount").toString());
                        }
                        /*if (byMap.get(1).getDictCode().equals(hdangerLevel) && ("4".equals(status) ||"5".equals(status))){
                            counts[9]=counts[9]+Integer.valueOf(statistic.get("sumCount").toString());
                        }*/
                        if (byMap.get(2).getDictCode().equals(hdangerLevel) && ("4".equals(status) ||"5".equals(status))){
                            counts[5]=counts[5]+Integer.valueOf(statistic.get("sumCount").toString());
                        }
                        /*if (byMap.get(3).getDictCode().equals(hdangerLevel) && ("4".equals(status) ||"5".equals(status))){
                            counts[11]=counts[11]+Integer.valueOf(statistic.get("sumCount").toString());
                        }*/
                    }
                }
            }
            for (int i = 1; i <= yAxis1.length; i++) {
                for (Map<String, Object> statistic : statistics) {
                    String createTime = statistic.get("createTime").toString();
                    //隐患等级
                    String hdangerLevel=statistic.get("hdangerLevel").toString();
                    String iString=String.valueOf(i);
                    if (byMap!=null) {
                        if (iString.equals(createTime) && byMap.get(0).getDictCode().equals(hdangerLevel)) {
                                  yAxis1[i-1]=yAxis1[i-1]+Integer.valueOf(statistic.get("sumCount").toString());
                        }
                        /*if (iString.equals(createTime) && byMap.get(1).getDictCode().equals(hdangerLevel)) {
                            yAxis2[i-1]=yAxis2[i-1]+Integer.valueOf(statistic.get("sumCount").toString());
                        }*/
                        if (iString.equals(createTime) && byMap.get(2).getDictCode().equals(hdangerLevel)) {
                            yAxis3[i-1]=yAxis3[i-1]+Integer.valueOf(statistic.get("sumCount").toString());
                        }
                        /*if (iString.equals(createTime) && byMap.get(3).getDictCode().equals(hdangerLevel)) {
                            yAxis4[i-1]=yAxis4[i-1]+Integer.valueOf(statistic.get("sumCount").toString());
                        }*/
                    }
                }
            }
        }
        Map map2=new LinkedHashMap();
        if(counts.length>0){
            for(int a=0;a<counts.length;a++){
                map2.put("name"+(a+1),counts[a]);
            }
        }
        List list=new ArrayList();
        list.add(map2);
        Map map1=new HashMap();
        map1.put("sumCount",list);
        map1.put("yAxis1",yAxis1);
        /*map1.put("yAxis2",yAxis2);*/
        map1.put("yAxis3",yAxis3);
        /*map1.put("yAxis4",yAxis4);*/
        return map1;
    }
    /**
     * @Author: zyj
     * @Description:隐患类型统计柱形图
     * @Param companyId 企业id groupId群组id startTime 开始时间 YYYY-MM-DD endTime结束时间 YYYY-MM-DD
     * @Date 16:32 2020/6/29
     */
    @Override
    public Map<String, Object> getHdangerTypeStatistics(Long companyId, Long groupId, String startTime, String endTime) {
        Map map=new HashMap();
        List<Map<String, Object>> statistics = HiddenRecordMapper.getHdangerTypeStatistics(companyId, groupId, startTime, endTime);
        //x轴数据
        String[] mapxAxis=new String[statistics.size()];
        //y轴数据 重大
        Integer[] mapyAxis1=new Integer[statistics.size()];
        //y轴数据 较大
        /*Integer[] mapyAxis2=new Integer[statistics.size()];*/
        //y轴数据 一般
        Integer[] mapyAxis3=new Integer[statistics.size()];
        //y轴数据 较低
        /*Integer[] mapyAxis4=new Integer[statistics.size()];*/
        for (int i = 0; i < statistics.size(); i++) {
            mapxAxis[i]=statistics.get(i).get("classifyName").toString();
            mapyAxis1[i]=Integer.valueOf(statistics.get(i).get("sumCount1").toString());
            /*mapyAxis2[i]=Integer.valueOf(statistics.get(i).get("sumCount2").toString());*/
            mapyAxis3[i]=Integer.valueOf(statistics.get(i).get("sumCount3").toString());
            /*mapyAxis4[i]=Integer.valueOf(statistics.get(i).get("sumCount4").toString());*/
        }
        map.put("mapxAxis",mapxAxis);
        map.put("mapyAxis1",mapyAxis1);
        /*map.put("mapyAxis2",mapyAxis2);*/
        map.put("mapyAxis3",mapyAxis3);
        /*map.put("mapyAxis4",mapyAxis4);*/
        return map;
    }

    @Override
    public HiddenRecord getById(Long id){
        return HiddenRecordMapper.findById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(Hidden hidden) {
        HiddenRecordMapper.add(hidden);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(HiddenRecord HiddenRecord) {
        HiddenRecordMapper.update(HiddenRecord);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeByIds(List ids) {
        HiddenRecordMapper.deleteByIds(ids);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeById(Long id) {
        HiddenRecordMapper.deleteById(id);
    }
}
