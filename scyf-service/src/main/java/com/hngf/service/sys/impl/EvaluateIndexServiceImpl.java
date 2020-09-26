package com.hngf.service.sys.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hngf.common.utils.PageUtils;
import com.hngf.entity.sys.EvaluateIndex;
import com.hngf.mapper.sys.EvaluateIndexMapper;
import com.hngf.service.sys.EvaluateIndexService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Map;


@Service("EvaluateIndexService")
public class EvaluateIndexServiceImpl implements EvaluateIndexService {

    @Autowired
    private EvaluateIndexMapper evaluateIndexMapper;

    @Override
    public PageUtils queryPage(Map<String, Object> params,Integer pageNum,Integer pageSize,String order) {
        //利用PageHelper分页查询 注意：这个一定要放查询语句的前一行,否则无法进行分页,因为它对紧随其后第一个sql语句有效
        if(StringUtils.isNotBlank(order)){
            PageHelper.startPage(pageNum, pageSize,order);
        }else{
            PageHelper.startPage(pageNum, pageSize);
        }
        List<EvaluateIndex> list = evaluateIndexMapper.findList(params);
        PageInfo<EvaluateIndex> pageInfo = new PageInfo<EvaluateIndex>(list);
        return new PageUtils(list,(int)pageInfo.getTotal(),pageInfo.getPages(),pageInfo.getPageSize(),pageInfo.getPrePage() + 1);
    }

    @Override
    public EvaluateIndex getById(Long id){
        return evaluateIndexMapper.findById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(EvaluateIndex EvaluateIndex) {
        evaluateIndexMapper.add(EvaluateIndex);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(EvaluateIndex EvaluateIndex) {
        evaluateIndexMapper.update(EvaluateIndex);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeByIds(List ids) {
        evaluateIndexMapper.deleteByIds(ids);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeById(Long id) {
        evaluateIndexMapper.deleteById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void initEvaluate(Long createdBy, Long companyId,String evaluateType1) {
        String[] evaluateType2 = evaluateType1.split(",");
        List<String> evaluateType = Arrays.asList(evaluateType2);
        //LS
        if(evaluateType.contains("水利LS")){
            EvaluateIndex evaluateIndex=new EvaluateIndex();
            evaluateIndex.setCompanyId(companyId);
            evaluateIndex.setEvaluateIndexModel("LS");
            evaluateIndex.setEvaluateIndexType("L");
            evaluateIndex.setEvaluateIndexContent("一般情况下不会发生");
            evaluateIndex.setEvaluateIndexScore((float) 3);
            evaluateIndex.setDelFlag(0);
            evaluateIndexMapper.add(evaluateIndex);
            EvaluateIndex evaluateIndex1=new EvaluateIndex();
            evaluateIndex1.setCompanyId(companyId);
            evaluateIndex1.setEvaluateIndexModel("LS");
            evaluateIndex1.setEvaluateIndexType("L");
            evaluateIndex1.setEvaluateIndexContent("极少情况下才发生");
            evaluateIndex1.setEvaluateIndexScore((float) 6);
            evaluateIndex1.setDelFlag(0);
            evaluateIndexMapper.add(evaluateIndex1);
            EvaluateIndex evaluateIndex2=new EvaluateIndex();
            evaluateIndex2.setCompanyId(companyId);
            evaluateIndex2.setEvaluateIndexModel("LS");
            evaluateIndex2.setEvaluateIndexType("L");
            evaluateIndex2.setEvaluateIndexContent("某些情况下发生");
            evaluateIndex2.setEvaluateIndexScore((float) 18);
            evaluateIndex2.setDelFlag(0);
            evaluateIndexMapper.add(evaluateIndex2);
            EvaluateIndex evaluateIndex3=new EvaluateIndex();
            evaluateIndex3.setCompanyId(companyId);
            evaluateIndex3.setEvaluateIndexModel("LS");
            evaluateIndex3.setEvaluateIndexType("L");
            evaluateIndex3.setEvaluateIndexContent("较多情况下发生");
            evaluateIndex3.setEvaluateIndexScore((float) 36);
            evaluateIndex3.setDelFlag(0);
            evaluateIndexMapper.add(evaluateIndex3);
            EvaluateIndex evaluateIndex4=new EvaluateIndex();
            evaluateIndex4.setCompanyId(companyId);
            evaluateIndex4.setEvaluateIndexModel("LS");
            evaluateIndex4.setEvaluateIndexType("L");
            evaluateIndex4.setEvaluateIndexContent("常常会发生");
            evaluateIndex4.setEvaluateIndexScore((float) 60);
            evaluateIndex4.setDelFlag(0);
            evaluateIndexMapper.add(evaluateIndex4);

            EvaluateIndex evaluateIndex5=new EvaluateIndex();
            evaluateIndex5.setCompanyId(companyId);
            evaluateIndex5.setEvaluateIndexModel("LS");
            evaluateIndex5.setEvaluateIndexType("S");
            evaluateIndex5.setEvaluateIndexContent("极轻微的");
            evaluateIndex5.setEvaluateIndexScore((float) 3);
            evaluateIndex5.setDelFlag(0);
            evaluateIndexMapper.add(evaluateIndex5);
            EvaluateIndex evaluateIndex6=new EvaluateIndex();
            evaluateIndex6.setCompanyId(companyId);
            evaluateIndex6.setEvaluateIndexModel("LS");
            evaluateIndex6.setEvaluateIndexType("S");
            evaluateIndex6.setEvaluateIndexContent("轻微的");
            evaluateIndex6.setEvaluateIndexScore((float) 7);
            evaluateIndex6.setDelFlag(0);
            evaluateIndexMapper.add(evaluateIndex6);
            EvaluateIndex evaluateIndex7=new EvaluateIndex();
            evaluateIndex7.setCompanyId(companyId);
            evaluateIndex7.setEvaluateIndexModel("LS");
            evaluateIndex7.setEvaluateIndexType("S");
            evaluateIndex7.setEvaluateIndexContent("中等的");
            evaluateIndex7.setEvaluateIndexScore((float) 15);
            evaluateIndex7.setDelFlag(0);
            evaluateIndexMapper.add(evaluateIndex7);
            EvaluateIndex evaluateIndex8=new EvaluateIndex();
            evaluateIndex8.setCompanyId(companyId);
            evaluateIndex8.setEvaluateIndexModel("LS");
            evaluateIndex8.setEvaluateIndexType("S");
            evaluateIndex8.setEvaluateIndexContent("重大的");
            evaluateIndex8.setEvaluateIndexScore((float) 40);
            evaluateIndex8.setDelFlag(0);
            evaluateIndexMapper.add(evaluateIndex8);
            EvaluateIndex evaluateIndex9=new EvaluateIndex();
            evaluateIndex9.setCompanyId(companyId);
            evaluateIndex9.setEvaluateIndexModel("LS");
            evaluateIndex9.setEvaluateIndexType("S");
            evaluateIndex9.setEvaluateIndexContent("灾难性的");
            evaluateIndex9.setEvaluateIndexScore((float) 100);
            evaluateIndex9.setDelFlag(0);
            evaluateIndexMapper.add(evaluateIndex9);
        }
        if(evaluateType.contains("LEC")){
            EvaluateIndex evaluateIndex10=new EvaluateIndex();
            evaluateIndex10.setCompanyId(companyId);
            evaluateIndex10.setEvaluateIndexModel("LEC");
            evaluateIndex10.setEvaluateIndexType("L");
            evaluateIndex10.setEvaluateIndexContent("极不可能");
            evaluateIndex10.setEvaluateIndexScore((float) 0.2);
            evaluateIndex10.setDelFlag(0);
            evaluateIndexMapper.add(evaluateIndex10);
            EvaluateIndex evaluateIndex11=new EvaluateIndex();
            evaluateIndex11.setCompanyId(companyId);
            evaluateIndex11.setEvaluateIndexModel("LEC");
            evaluateIndex11.setEvaluateIndexType("L");
            evaluateIndex11.setEvaluateIndexContent("很不可能，可以设想");
            evaluateIndex11.setEvaluateIndexScore((float) 0.5);
            evaluateIndex11.setDelFlag(0);
            evaluateIndexMapper.add(evaluateIndex11);
            EvaluateIndex evaluateIndex12=new EvaluateIndex();
            evaluateIndex12.setCompanyId(companyId);
            evaluateIndex12.setEvaluateIndexModel("LEC");
            evaluateIndex12.setEvaluateIndexType("L");
            evaluateIndex12.setEvaluateIndexContent("可能性小，完全意外");
            evaluateIndex12.setEvaluateIndexScore((float) 1);
            evaluateIndex12.setDelFlag(0);
            evaluateIndexMapper.add(evaluateIndex12);
            EvaluateIndex evaluateIndex13=new EvaluateIndex();
            evaluateIndex13.setCompanyId(companyId);
            evaluateIndex13.setEvaluateIndexModel("LEC");
            evaluateIndex13.setEvaluateIndexType("L");
            evaluateIndex13.setEvaluateIndexContent("可能，但不经常");
            evaluateIndex13.setEvaluateIndexScore((float) 3);
            evaluateIndex13.setDelFlag(0);
            evaluateIndexMapper.add(evaluateIndex13);
            EvaluateIndex evaluateIndex14=new EvaluateIndex();
            evaluateIndex14.setCompanyId(companyId);
            evaluateIndex14.setEvaluateIndexModel("LEC");
            evaluateIndex14.setEvaluateIndexType("L");
            evaluateIndex14.setEvaluateIndexContent("相当可能");
            evaluateIndex14.setEvaluateIndexScore((float) 6);
            evaluateIndex14.setDelFlag(0);
            evaluateIndexMapper.add(evaluateIndex14);
            EvaluateIndex evaluateIndex15=new EvaluateIndex();
            evaluateIndex15.setCompanyId(companyId);
            evaluateIndex15.setEvaluateIndexModel("LEC");
            evaluateIndex15.setEvaluateIndexType("L");
            evaluateIndex15.setEvaluateIndexContent("完全可以预料");
            evaluateIndex15.setEvaluateIndexScore((float) 10);
            evaluateIndex15.setDelFlag(0);
            evaluateIndexMapper.add(evaluateIndex15);
            EvaluateIndex evaluateIndex17=new EvaluateIndex();
            evaluateIndex17.setCompanyId(companyId);
            evaluateIndex17.setEvaluateIndexModel("LEC");
            evaluateIndex17.setEvaluateIndexType("E");
            evaluateIndex17.setEvaluateIndexContent("非常罕见暴露");
            evaluateIndex17.setEvaluateIndexScore((float) 0.5);
            evaluateIndex17.setDelFlag(0);
            evaluateIndexMapper.add(evaluateIndex17);
            EvaluateIndex evaluateIndex18=new EvaluateIndex();
            evaluateIndex18.setCompanyId(companyId);
            evaluateIndex18.setEvaluateIndexModel("LEC");
            evaluateIndex18.setEvaluateIndexType("E");
            evaluateIndex18.setEvaluateIndexContent("每年几次暴露");
            evaluateIndex18.setEvaluateIndexScore((float) 1);
            evaluateIndex18.setDelFlag(0);
            evaluateIndexMapper.add(evaluateIndex18);
            EvaluateIndex evaluateIndex19=new EvaluateIndex();
            evaluateIndex19.setCompanyId(companyId);
            evaluateIndex19.setEvaluateIndexModel("LEC");
            evaluateIndex19.setEvaluateIndexType("E");
            evaluateIndex19.setEvaluateIndexContent("每月一次暴露");
            evaluateIndex19.setEvaluateIndexScore((float) 2);
            evaluateIndex19.setDelFlag(0);
            evaluateIndexMapper.add(evaluateIndex19);
            EvaluateIndex evaluateIndex20=new EvaluateIndex();
            evaluateIndex20.setCompanyId(companyId);
            evaluateIndex20.setEvaluateIndexModel("LEC");
            evaluateIndex20.setEvaluateIndexType("E");
            evaluateIndex20.setEvaluateIndexContent("每周一次或偶然暴露");
            evaluateIndex20.setEvaluateIndexScore((float) 3);
            evaluateIndex20.setDelFlag(0);
            evaluateIndexMapper.add(evaluateIndex20);
            EvaluateIndex evaluateIndex21=new EvaluateIndex();
            evaluateIndex21.setCompanyId(companyId);
            evaluateIndex21.setEvaluateIndexModel("LEC");
            evaluateIndex21.setEvaluateIndexType("E");
            evaluateIndex21.setEvaluateIndexContent("每天工作时间内暴露");
            evaluateIndex21.setEvaluateIndexScore((float) 6);
            evaluateIndex21.setDelFlag(0);
            evaluateIndexMapper.add(evaluateIndex21);
            EvaluateIndex evaluateIndex22=new EvaluateIndex();
            evaluateIndex22.setCompanyId(companyId);
            evaluateIndex22.setEvaluateIndexModel("LEC");
            evaluateIndex22.setEvaluateIndexType("E");
            evaluateIndex22.setEvaluateIndexContent("连续暴露");
            evaluateIndex22.setEvaluateIndexScore((float) 10);
            evaluateIndex22.setDelFlag(0);
            evaluateIndexMapper.add(evaluateIndex22);
            EvaluateIndex evaluateIndex23=new EvaluateIndex();
            evaluateIndex23.setCompanyId(companyId);
            evaluateIndex23.setEvaluateIndexModel("LEC");
            evaluateIndex23.setEvaluateIndexType("C");
            evaluateIndex23.setEvaluateIndexContent("引人注目，不利于基本的安全卫生要求");
            evaluateIndex23.setEvaluateIndexScore((float) 1);
            evaluateIndex23.setDelFlag(0);
            evaluateIndexMapper.add(evaluateIndex23);
            EvaluateIndex evaluateIndex24=new EvaluateIndex();
            evaluateIndex24.setCompanyId(companyId);
            evaluateIndex24.setEvaluateIndexModel("LEC");
            evaluateIndex24.setEvaluateIndexType("C");
            evaluateIndex24.setEvaluateIndexContent("无人员死亡，致残或重伤，或很小的财产损失");
            evaluateIndex24.setEvaluateIndexScore((float) 3);
            evaluateIndex24.setDelFlag(0);
            evaluateIndexMapper.add(evaluateIndex24);
            EvaluateIndex evaluateIndex25=new EvaluateIndex();
            evaluateIndex25.setCompanyId(companyId);
            evaluateIndex25.setEvaluateIndexModel("LEC");
            evaluateIndex25.setEvaluateIndexType("C");
            evaluateIndex25.setEvaluateIndexContent("造成3人以下死亡，或者10人以下重伤，或者1000万元以下直接经济损失");
            evaluateIndex25.setEvaluateIndexScore((float) 7);
            evaluateIndex25.setDelFlag(0);
            evaluateIndexMapper.add(evaluateIndex25);
            EvaluateIndex evaluateIndex26=new EvaluateIndex();
            evaluateIndex26.setCompanyId(companyId);
            evaluateIndex26.setEvaluateIndexModel("LEC");
            evaluateIndex26.setEvaluateIndexType("C");
            evaluateIndex26.setEvaluateIndexContent("造成3人～9人死亡，或者10人～49人重伤，或者1000万元以上5000万元以下直接经济损失");
            evaluateIndex26.setEvaluateIndexScore((float) 15);
            evaluateIndex26.setDelFlag(0);
            evaluateIndexMapper.add(evaluateIndex26);
            EvaluateIndex evaluateIndex27=new EvaluateIndex();
            evaluateIndex27.setCompanyId(companyId);
            evaluateIndex27.setEvaluateIndexModel("LEC");
            evaluateIndex27.setEvaluateIndexType("C");
            evaluateIndex27.setEvaluateIndexContent("造成10人～29人死亡，或者50人～99人重伤，或者5000万元以上1亿元以下直接经济损失");
            evaluateIndex27.setEvaluateIndexScore((float) 40);
            evaluateIndex27.setDelFlag(0);
            evaluateIndexMapper.add(evaluateIndex27);
            EvaluateIndex evaluateIndex28=new EvaluateIndex();
            evaluateIndex28.setCompanyId(companyId);
            evaluateIndex28.setEvaluateIndexModel("LEC");
            evaluateIndex28.setEvaluateIndexType("C");
            evaluateIndex28.setEvaluateIndexContent("造成30人以上（含30人）死亡，或者100人以上重伤（包括急性工业中毒，下同），或者1亿元以上直接经济损失");
            evaluateIndex28.setEvaluateIndexScore((float) 100);
            evaluateIndex28.setDelFlag(0);
            evaluateIndexMapper.add(evaluateIndex28);
        }
        if(evaluateType.contains("LC")){
            EvaluateIndex evaluateIndex29=new EvaluateIndex();
            evaluateIndex29.setCompanyId(companyId);
            evaluateIndex29.setEvaluateIndexModel("LC");
            evaluateIndex29.setEvaluateIndexType("L");
            evaluateIndex29.setEvaluateIndexRemark("(0-1]");
            evaluateIndex29.setEvaluateIndexContent("极低");
            evaluateIndex29.setEvaluateIndexScore((float) 1);
            evaluateIndex29.setDelFlag(0);
            evaluateIndexMapper.add(evaluateIndex29);
            EvaluateIndex evaluateIndex30=new EvaluateIndex();
            evaluateIndex30.setCompanyId(companyId);
            evaluateIndex30.setEvaluateIndexModel("LC");
            evaluateIndex30.setEvaluateIndexType("L");
            evaluateIndex30.setEvaluateIndexRemark("(1-3]");
            evaluateIndex30.setEvaluateIndexContent("低");
            evaluateIndex30.setEvaluateIndexScore((float) 3);
            evaluateIndex30.setDelFlag(0);
            evaluateIndexMapper.add(evaluateIndex30);
            EvaluateIndex evaluateIndex31=new EvaluateIndex();
            evaluateIndex31.setCompanyId(companyId);
            evaluateIndex31.setEvaluateIndexModel("LC");
            evaluateIndex31.setEvaluateIndexType("L");
            evaluateIndex31.setEvaluateIndexRemark("(3-6]");
            evaluateIndex31.setEvaluateIndexContent("中等");
            evaluateIndex31.setEvaluateIndexScore((float) 5);
            evaluateIndex31.setDelFlag(0);
            evaluateIndexMapper.add(evaluateIndex31);
            EvaluateIndex evaluateIndex32=new EvaluateIndex();
            evaluateIndex32.setCompanyId(companyId);
            evaluateIndex32.setEvaluateIndexModel("LC");
            evaluateIndex32.setEvaluateIndexType("L");
            evaluateIndex32.setEvaluateIndexRemark("(6-9]");
            evaluateIndex32.setEvaluateIndexContent("高");
            evaluateIndex32.setEvaluateIndexScore((float) 8);
            evaluateIndex32.setDelFlag(0);
            evaluateIndexMapper.add(evaluateIndex32);
            EvaluateIndex evaluateIndex33=new EvaluateIndex();
            evaluateIndex33.setCompanyId(companyId);
            evaluateIndex33.setEvaluateIndexModel("LC");
            evaluateIndex33.setEvaluateIndexType("L");
            evaluateIndex33.setEvaluateIndexRemark("(9-10]");
            evaluateIndex33.setEvaluateIndexContent("极高");
            evaluateIndex33.setEvaluateIndexScore((float) 10);
            evaluateIndex33.setDelFlag(0);
            evaluateIndexMapper.add(evaluateIndex33);
            EvaluateIndex evaluateIndex34=new EvaluateIndex();
            evaluateIndex34.setCompanyId(companyId);
            evaluateIndex34.setEvaluateIndexModel("LC");
            evaluateIndex34.setEvaluateIndexType("C");
            evaluateIndex34.setEvaluateIndexContent("不严重");
            evaluateIndex34.setEvaluateIndexScore((float) 1);
            evaluateIndex34.setDelFlag(0);
            evaluateIndexMapper.add(evaluateIndex34);
            EvaluateIndex evaluateIndex35=new EvaluateIndex();
            evaluateIndex35.setCompanyId(companyId);
            evaluateIndex35.setEvaluateIndexModel("LC");
            evaluateIndex35.setEvaluateIndexType("C");
            evaluateIndex35.setEvaluateIndexContent("较严重");
            evaluateIndex35.setEvaluateIndexScore((float) 2);
            evaluateIndex35.setDelFlag(0);
            evaluateIndexMapper.add(evaluateIndex35);
            EvaluateIndex evaluateIndex36=new EvaluateIndex();
            evaluateIndex36.setCompanyId(companyId);
            evaluateIndex36.setEvaluateIndexModel("LC");
            evaluateIndex36.setEvaluateIndexType("C");
            evaluateIndex36.setEvaluateIndexContent("严重");
            evaluateIndex36.setEvaluateIndexScore((float) 5);
            evaluateIndex36.setDelFlag(0);
            evaluateIndexMapper.add(evaluateIndex36);
            EvaluateIndex evaluateIndex37=new EvaluateIndex();
            evaluateIndex37.setCompanyId(companyId);
            evaluateIndex37.setEvaluateIndexModel("LC");
            evaluateIndex37.setEvaluateIndexType("C");
            evaluateIndex37.setEvaluateIndexContent("特别严重");
            evaluateIndex37.setEvaluateIndexScore((float) 10);
            evaluateIndex37.setDelFlag(0);
            evaluateIndexMapper.add(evaluateIndex37);
        }
        if(evaluateType.contains("交通LS")){
            EvaluateIndex evaluateIndex38=new EvaluateIndex();
            evaluateIndex38.setCompanyId(companyId);
            evaluateIndex38.setEvaluateIndexModel("LS");
            evaluateIndex38.setEvaluateIndexType("L");
            evaluateIndex38.setEvaluateIndexContent("有充分、有效的防范、控制、监测、保护措施，或员工安全卫生意识相当高，严格执行操作规程，极不可能发生事故或事件.");
            evaluateIndex38.setEvaluateIndexScore((float) 1);
            evaluateIndex38.setDelFlag(0);
            evaluateIndexMapper.add(evaluateIndex38);
            EvaluateIndex evaluateIndex39=new EvaluateIndex();
            evaluateIndex39.setCompanyId(companyId);
            evaluateIndex39.setEvaluateIndexModel("LS");
            evaluateIndex39.setEvaluateIndexType("L");
            evaluateIndex39.setEvaluateIndexContent("危险有害因素一旦发生能及时发现，并定期进行监测，或现场有防范控制措施，并有有效执行或过去偶尔发生危险事故或事件。");
            evaluateIndex39.setEvaluateIndexScore((float) 2);
            evaluateIndex39.setDelFlag(0);
            evaluateIndexMapper.add(evaluateIndex39);
            EvaluateIndex evaluateIndex40=new EvaluateIndex();
            evaluateIndex40.setCompanyId(companyId);
            evaluateIndex40.setEvaluateIndexModel("LS");
            evaluateIndex40.setEvaluateIndexType("L");
            evaluateIndex40.setEvaluateIndexContent("没有保护措施（如没有防护装置、没有个人防护用品等），或未严格按操作程序执行，或危险、有害因素的发生容易被发现（现场有监测系统），或曾经作过监测，或过去曾经发生类似事故或事件，或在异常情况下发生过类似事故或事件");
            evaluateIndex40.setEvaluateIndexScore((float) 3);
            evaluateIndex40.setDelFlag(0);
            evaluateIndexMapper.add(evaluateIndex40);
            EvaluateIndex evaluateIndex41=new EvaluateIndex();
            evaluateIndex41.setCompanyId(companyId);
            evaluateIndex41.setEvaluateIndexModel("LS");
            evaluateIndex41.setEvaluateIndexType("L");
            evaluateIndex41.setEvaluateIndexContent("危险有害因素的发生不能被发现，现场没有检测系统，也未作过任何监测，或在现场有控制措施，但未有效执行或控制措施不当，或危险有害因素常发生或在预期情况下发生");
            evaluateIndex41.setEvaluateIndexScore((float) 4);
            evaluateIndex41.setDelFlag(0);
            evaluateIndexMapper.add(evaluateIndex41);
            EvaluateIndex evaluateIndex42=new EvaluateIndex();
            evaluateIndex42.setCompanyId(companyId);
            evaluateIndex42.setEvaluateIndexModel("LS");
            evaluateIndex42.setEvaluateIndexType("L");
            evaluateIndex42.setEvaluateIndexContent("在现场没有采取防范、监测、保护、控制措施，或危险有害因素的发生不能被发现（没有监测系统），或在正常情况下经常发生此类事故或事件。");
            evaluateIndex42.setEvaluateIndexScore((float) 5);
            evaluateIndex42.setDelFlag(0);
            evaluateIndexMapper.add(evaluateIndex42);
            EvaluateIndex evaluateIndex43=new EvaluateIndex();
            evaluateIndex43.setCompanyId(companyId);
            evaluateIndex43.setEvaluateIndexModel("LS");
            evaluateIndex43.setEvaluateIndexType("S");
            evaluateIndex43.setEvaluateIndexContent("法律、法规及其他要求:完全符合 人:无伤亡 财产损失万元:无损失 环境影响:没有污染 停工:没有停工 公司形象:没有受损");
            evaluateIndex43.setEvaluateIndexScore((float) 1);
            evaluateIndex43.setDelFlag(0);
            evaluateIndexMapper.add(evaluateIndex43);
            EvaluateIndex evaluateIndex44=new EvaluateIndex();
            evaluateIndex44.setCompanyId(companyId);
            evaluateIndex44.setEvaluateIndexModel("LS");
            evaluateIndex44.setEvaluateIndexType("S");
            evaluateIndex44.setEvaluateIndexContent("法律、法规及其他要求:不符合公司的安全操作规程 人:轻微受伤、间歇不舒服 财产损失万元:＜10 环境影响:装置范围污染 停工:受影响不大，几乎不停工 公司形象:公司及周边范围");
            evaluateIndex44.setEvaluateIndexScore((float) 2);
            evaluateIndex44.setDelFlag(0);
            evaluateIndexMapper.add(evaluateIndex44);
            EvaluateIndex evaluateIndex45=new EvaluateIndex();
            evaluateIndex45.setCompanyId(companyId);
            evaluateIndex45.setEvaluateIndexModel("LS");
            evaluateIndex45.setEvaluateIndexType("S");
            evaluateIndex45.setEvaluateIndexContent("法律、法规及其他要求:不符合上级公司或行业的安全方针、制度、规定等 人:截肢、骨折、听力丧失、慢性病 财产损失万元:＞10 环境影响:公司范围内中等污染 停工:一套装置或设备停工 公司形象:地区影响");
            evaluateIndex45.setEvaluateIndexScore((float) 3);
            evaluateIndex45.setDelFlag(0);
            evaluateIndexMapper.add(evaluateIndex45);
            EvaluateIndex evaluateIndex46=new EvaluateIndex();
            evaluateIndex46.setCompanyId(companyId);
            evaluateIndex46.setEvaluateIndexModel("LS");
            evaluateIndex46.setEvaluateIndexType("S");
            evaluateIndex46.setEvaluateIndexContent("法律、法规及其他要求:潜在违反法规、标准 人:丧失劳动能力 财产损失万元:＞25 环境影响:公司内严重污染 停工:2套装置停工或设备停工 公司形象:行业内、省内影响");
            evaluateIndex46.setEvaluateIndexScore((float) 4);
            evaluateIndex46.setDelFlag(0);
            evaluateIndexMapper.add(evaluateIndex46);
            EvaluateIndex evaluateIndex47=new EvaluateIndex();
            evaluateIndex47.setCompanyId(companyId);
            evaluateIndex47.setEvaluateIndexModel("LS");
            evaluateIndex47.setEvaluateIndexType("S");
            evaluateIndex47.setEvaluateIndexContent("法律、法规及其他要求:违反法律、法规、标准 人:死亡 财产损失万元:＞50 环境影响:大规模公司外 停工:部分装置（大于2套）或设备停工 公司形象:重大国际国内影响");
            evaluateIndex47.setEvaluateIndexScore((float) 5);
            evaluateIndex47.setDelFlag(0);
            evaluateIndexMapper.add(evaluateIndex47);
        }
        if(evaluateType.contains("高速A评价")){
            EvaluateIndex evaluateIndex48=new EvaluateIndex();
            evaluateIndex48.setCompanyId(companyId);
            evaluateIndex48.setEvaluateIndexModel("线型特征(A1)25分");
            evaluateIndex48.setEvaluateIndexContent("陡坡路段");
            evaluateIndex48.setEvaluateIndexRemark("15-20分");
            evaluateIndex48.setEvaluateIndexType("A1");
            evaluateIndex48.setDelFlag(0);
            evaluateIndexMapper.add(evaluateIndex48);
            EvaluateIndex evaluateIndex49=new EvaluateIndex();
            evaluateIndex49.setCompanyId(companyId);
            evaluateIndex49.setEvaluateIndexModel("线型特征(A1)25分");
            evaluateIndex49.setEvaluateIndexContent("连续下坡路段");
            evaluateIndex49.setEvaluateIndexRemark("10-15分");
            evaluateIndex49.setEvaluateIndexType("A1");
            evaluateIndex49.setDelFlag(0);
            evaluateIndexMapper.add(evaluateIndex49);
            EvaluateIndex evaluateIndex50=new EvaluateIndex();
            evaluateIndex50.setCompanyId(companyId);
            evaluateIndex50.setEvaluateIndexModel("线型特征(A1)25分");
            evaluateIndex50.setEvaluateIndexContent("急转弯路段");
            evaluateIndex50.setEvaluateIndexRemark("12-16分");
            evaluateIndex50.setEvaluateIndexType("A1");
            evaluateIndex50.setDelFlag(0);
            evaluateIndexMapper.add(evaluateIndex50);

            EvaluateIndex evaluateIndex51=new EvaluateIndex();
            evaluateIndex51.setCompanyId(companyId);
            evaluateIndex51.setEvaluateIndexModel("线位特征(A2)16分");
            evaluateIndex51.setEvaluateIndexContent("学校、体育馆等人口密集路段");
            evaluateIndex51.setEvaluateIndexRemark("8-10分");
            evaluateIndex51.setEvaluateIndexType("A2");
            evaluateIndex51.setDelFlag(0);
            evaluateIndexMapper.add(evaluateIndex51);
            EvaluateIndex evaluateIndex52=new EvaluateIndex();
            evaluateIndex52.setCompanyId(companyId);
            evaluateIndex52.setEvaluateIndexModel("线位特征(A2)16分");
            evaluateIndex52.setEvaluateIndexContent("临崖临水高落差路段");
            evaluateIndex52.setEvaluateIndexRemark("12-16分");
            evaluateIndex52.setEvaluateIndexType("A2");
            evaluateIndex52.setDelFlag(0);
            evaluateIndexMapper.add(evaluateIndex52);

            EvaluateIndex evaluateIndex53=new EvaluateIndex();
            evaluateIndex53.setCompanyId(companyId);
            evaluateIndex53.setEvaluateIndexModel("节点特征(A3)10分");
            evaluateIndex53.setEvaluateIndexContent("与公路交叉口");
            evaluateIndex53.setEvaluateIndexRemark("8分");
            evaluateIndex53.setEvaluateIndexType("A3");
            evaluateIndex53.setDelFlag(0);
            evaluateIndexMapper.add(evaluateIndex53);
            EvaluateIndex evaluateIndex54=new EvaluateIndex();
            evaluateIndex54.setCompanyId(companyId);
            evaluateIndex54.setEvaluateIndexModel("节点特征(A3)10分");
            evaluateIndex54.setEvaluateIndexContent("与铁路交叉口");
            evaluateIndex54.setEvaluateIndexRemark("10分");
            evaluateIndex54.setEvaluateIndexType("A3");
            evaluateIndex54.setDelFlag(0);
            evaluateIndexMapper.add(evaluateIndex54);
            EvaluateIndex evaluateIndex55=new EvaluateIndex();
            evaluateIndex55.setCompanyId(companyId);
            evaluateIndex55.setEvaluateIndexModel("节点特征(A3)10分");
            evaluateIndex55.setEvaluateIndexContent("高速公路出入口");
            evaluateIndex55.setEvaluateIndexRemark("6分");
            evaluateIndex55.setEvaluateIndexType("A3");
            evaluateIndex55.setDelFlag(0);
            evaluateIndexMapper.add(evaluateIndex55);

            EvaluateIndex evaluateIndex56=new EvaluateIndex();
            evaluateIndex56.setCompanyId(companyId);
            evaluateIndex56.setEvaluateIndexModel("气象条件 12分");
            evaluateIndex56.setEvaluateIndexContent("气象恶劣程度");
            evaluateIndex56.setEvaluateIndexRemark("8-12分");
            evaluateIndex56.setEvaluateIndexType("A4");
            evaluateIndex56.setDelFlag(0);
            evaluateIndexMapper.add(evaluateIndex56);

            EvaluateIndex evaluateIndex57=new EvaluateIndex();
            evaluateIndex57.setCompanyId(companyId);
            evaluateIndex57.setEvaluateIndexModel("地质条件(A5)12分");
            evaluateIndex57.setEvaluateIndexContent("地震活动频繁区");
            evaluateIndex57.setEvaluateIndexRemark("8分");
            evaluateIndex57.setEvaluateIndexType("A5");
            evaluateIndex57.setDelFlag(0);
            evaluateIndexMapper.add(evaluateIndex57);
            EvaluateIndex evaluateIndex58=new EvaluateIndex();
            evaluateIndex58.setCompanyId(companyId);
            evaluateIndex58.setEvaluateIndexModel("地质条件(A5)12分");
            evaluateIndex58.setEvaluateIndexContent("滑坡、泥石流、塌方等频发路段");
            evaluateIndex58.setEvaluateIndexRemark("8-12分");
            evaluateIndex58.setEvaluateIndexType("A5");
            evaluateIndex58.setDelFlag(0);
            evaluateIndexMapper.add(evaluateIndex58);

            EvaluateIndex evaluateIndex59=new EvaluateIndex();
            evaluateIndex59.setCompanyId(companyId);
            evaluateIndex59.setEvaluateIndexModel("区位特征(A6)10分");
            evaluateIndex59.setEvaluateIndexContent("省际边界路段");
            evaluateIndex59.setEvaluateIndexRemark("8分");
            evaluateIndex59.setEvaluateIndexType("A6");
            evaluateIndex59.setDelFlag(0);
            evaluateIndexMapper.add(evaluateIndex59);
            EvaluateIndex evaluateIndex60=new EvaluateIndex();
            evaluateIndex60.setCompanyId(companyId);
            evaluateIndex60.setEvaluateIndexModel("区位特征(A6)10分");
            evaluateIndex60.setEvaluateIndexContent("城市出入口");
            evaluateIndex60.setEvaluateIndexRemark("4-6分");
            evaluateIndex60.setEvaluateIndexType("A6");
            evaluateIndex60.setDelFlag(0);
            evaluateIndexMapper.add(evaluateIndex60);

            EvaluateIndex evaluateIndex61=new EvaluateIndex();
            evaluateIndex61.setCompanyId(companyId);
            evaluateIndex61.setEvaluateIndexModel("交通运行特征(A7)15分");
            evaluateIndex61.setEvaluateIndexContent("高速公路");
            evaluateIndex61.setEvaluateIndexRemark("12-15分");
            evaluateIndex61.setEvaluateIndexType("A7");
            evaluateIndex61.setDelFlag(0);
            evaluateIndexMapper.add(evaluateIndex61);
            EvaluateIndex evaluateIndex62=new EvaluateIndex();
            evaluateIndex62.setCompanyId(companyId);
            evaluateIndex62.setEvaluateIndexModel("交通运行特征(A7)15分");
            evaluateIndex62.setEvaluateIndexContent("一级公路");
            evaluateIndex62.setEvaluateIndexRemark("7-11分");
            evaluateIndex62.setEvaluateIndexType("A7");
            evaluateIndex62.setDelFlag(0);
            evaluateIndexMapper.add(evaluateIndex62);
            EvaluateIndex evaluateIndex63=new EvaluateIndex();
            evaluateIndex63.setCompanyId(companyId);
            evaluateIndex63.setEvaluateIndexModel("交通运行特征(A7)15分");
            evaluateIndex63.setEvaluateIndexContent("二、三、四级公路");
            evaluateIndex63.setEvaluateIndexRemark("4-6分");
            evaluateIndex63.setEvaluateIndexType("A7");
            evaluateIndex63.setDelFlag(0);
            evaluateIndexMapper.add(evaluateIndex63);
            EvaluateIndex evaluateIndex64=new EvaluateIndex();
            evaluateIndex64.setCompanyId(companyId);
            evaluateIndex64.setEvaluateIndexModel("交通运行特征(A7)15分");
            evaluateIndex64.setEvaluateIndexContent("等外公路");
            evaluateIndex64.setEvaluateIndexRemark("1-3分");
            evaluateIndex64.setEvaluateIndexType("A7");
            evaluateIndex64.setDelFlag(0);
            evaluateIndexMapper.add(evaluateIndex64);
        }
        if(evaluateType.contains("高速A道路评价") ){
        String[][] evaluateContent = {{"A1","线形、线位特征(A1)10分","长大连续下坡路段","5分"},{"A1","线形、线位特征(A1)10分","临崖临水、高落差路段","10分"},{"A1","线形、线位特征(A1)10分","跨线天桥","5分"},
                {"A2","节点特征（A2）10分","与公路交叉口","8分"},{"A2","节点特征（A2）10分","与铁路交叉口","10分"},{"A2","节点特征（A2）10分","高速公路出入口","6分"},
                {"A3","区位特征（A3）10分","省际边界路段","8分"},{"A3","区位特征（A3）10分","城市出入口","4-6分"},{"A4","气象条件（A4）10分","气象恶劣程度","0-10分"},
                {"A5","地质条件（A5）10分","地震活动频繁区","8分"},{"A5","地质条件（A5）10分","滑坡、泥石流、塌方、落石等频发路段","0-10分"},
                {"A6","交通量指标（A6）25分","v/C年平均日交通量/路段设计规划流量（最大服务交通量/基准通行能力）公路工程技术标准JTG B01-2014","0-25分"},
                {"A7","事故易发、多发路段路段（A7）10分","","0-10分"},
                {"A8","交安设施技术状况（A8）15分","执行JTJ 074-94版《高速公路交通安全设施设计及施工技术规范》","10-15分"},{"A8","交安设施技术状况（A8）15分","执行JTG D81-2006《公路交通安全设施设计规范》","5分"},{"A8","交安设施技术状况（A8）15分","执行JTG D81-2017《公路交通安全设施设计规范》","0分"}};
            insertBatchEvaluateIndex(getEvaluateIndexList(createdBy, companyId, evaluateContent));
        }
        if(evaluateType.contains("高速A桥梁评价") ){
            String[][] evaluateContent = {{"A1","建设规模（A1）20分","特大桥","18-20分"},{"A1","建设规模（A1）20分","大桥","15分"},{"A1","建设规模（A1）20分","中桥","10分"},{"A1","建设规模（A1）20分","小桥","6-8分"},
                    {"A2","桥位特征（A2）10分","跨越江河、海湾桥梁，水流流向与桥梁垂线夹角","0-8分"},{"A2","桥位特征（A2）10分","跨越江河、海湾桥梁，超过设计的通航等级","10分"},{"A2","桥位特征（A2）10分","桥梁运营中超过其设计的承载能力","10分"},{"A2","桥位特征（A2）10分","桥头引道的线形","6-8分"},{"A2","桥位特征（A2）10分","跨越山谷、重丘、河谷桥梁","4-6分"},{"A2","桥位特征（A2）10分","桥隧相连","6分"},{"A2","桥位特征（A2）10分","公跨铁、跨越重要区域（如南水北调干渠、水源保护地）","8分"},
                    {"A3","桥梁技术状况等级（A3）20分","四、五类桥梁","20分"},{"A3","桥梁技术状况等级（A3）20分","三类桥梁","12分"},{"A3","桥梁技术状况等级（A3）20分","二类桥梁","6分"},{"A3","桥梁技术状况等级（A3）20分","一类桥梁","4分"},
                    {"A4","地质条件（A4）5分","地震活动频繁区","5分"},{"A4","地质条件（A4）5分","滑坡、泥石流、塌方、落石等频发路段","0-5分"},{"A5","气象条件（A5）10分","气象恶劣程度","0-10分"},
                    {"A6","交通流特征（A6）25分","重型车日交通量","0-15分"},{"A6","交通流特征（A6）25分","交通量饱和度公路工程技术标准JTG B01-2014","0-10分"},
                    {"A7","交安设施技术状况（A7）10分","不符合JTG D81-2006《公路交通安全设施设计规范》","8-10分"},{"A7","交安设施技术状况（A7）10分","符合JTG D81-2006《公路交通安全设施设计规范》","5分"},{"A7","交安设施技术状况（A7）10分","符合JTG D81-2017《公路交通安全设施设计规范》","0分"}};
            insertBatchEvaluateIndex(getEvaluateIndexList(createdBy, companyId, evaluateContent));
        }
        if(evaluateType.contains("高速A隧道评价") ){
            String[][] evaluateContent = {{"A1","土建结构特征（A1）12分","隧道长度","2-6分"},{"A1","土建结构特征（A1）12分","平面曲线半径","4分"},{"A1","土建结构特征（A1）12分","纵向坡度","2分"},{"A1","土建结构特征（A1）12分","隧道形式","2-4分"},
                    {"A2","接线特征(A2)14分","毗邻隧道","14分"},{"A2","接线特征(A2)14分","连续隧道","12分"},{"A2","接线特征(A2)14分","桥隧相接","10分"},{"A2","接线特征(A2)14分","坡路隧道相连","4-6分"},
                    {"A3","机电设施(A3)20分","机电设施设置不符合现行国家规范、标准的设要求","20分"},{"A3","机电设施(A3)20分","1 类机电设施的隧道","4分"},{"A3","机电设施(A3)20分","2 类机电设施分项设施评定状况有1的隧道","6分"},{"A3","机电设施(A3)20分","3 类机电设施分项设施评定状况有2的隧道","8分"},{"A3","机电设施(A3)20分","4 类机电设施分项设施评定状况有3的隧道","12分"},
                    {"A4","隧道技术状况等级（A4）20分","1类隧道","4分"},{"A4","隧道技术状况等级（A4）20分","2类隧道","6分"},{"A4","隧道技术状况等级（A4）20分","3类隧道","12分"},{"A4","隧道技术状况等级（A4）20分","4、5类隧道","20分"},
                    {"A5","气象条件（A5）10分","气象恶劣程度","0-10分"},{"A6","地质条件（A6）12分","地震活动频繁区","6分"},{"A6","地质条件（A6）12分","滑坡、泥石流、塌方、落石等频发路段","0-12分"},
                    {"A7","交通流特征（A7）15分","重型车日交通量","0-15分"},{"A7","交通流特征（A7）15分","交通量饱和度 公路工程技术标准JTG B01-2014","0-10分"},
                    {"A8","交安设施技术状况（A8）10分","不符合JTG D81-2006《公路交通安全设施设计规范》","8-10分"},{"A8","交安设施技术状况（A8）10分","符合JTG D81-2006《公路交通安全设施设计规范》","5分"},{"A8","交安设施技术状况（A8）10分","符合JTG D81-2017《公路交通安全设施设计规范》","0分"}};
            insertBatchEvaluateIndex(getEvaluateIndexList(createdBy, companyId, evaluateContent));
        }

    }
    private void insertBatchEvaluateIndex( List<EvaluateIndex> evaluateIndexList){
        if(null != evaluateIndexList && evaluateIndexList.size()>0){
            evaluateIndexMapper.addBatch(evaluateIndexList);
        }
    }
    private List<EvaluateIndex> getEvaluateIndexList(Long createdBy, Long companyId , String[][] evaluateContent ){
        List<EvaluateIndex> evaluateIndexList = null ;
        if(null != evaluateContent ){
            int indexLength = evaluateContent.length;
            for(int i = 0; i<indexLength; i++ ){
                String[] temp = evaluateContent[i];
                EvaluateIndex evaluateIndex = new EvaluateIndex ();
                evaluateIndex.insertPrefixInit(createdBy,companyId);
                evaluateIndex.setEvaluateIndexType(temp[0]);
                evaluateIndex.setEvaluateIndexModel(temp[1]);
                evaluateIndex.setEvaluateIndexContent(temp[2]);
                evaluateIndex.setEvaluateIndexRemark(temp[3]);
                evaluateIndexList.add(evaluateIndex);
            }
        }
        return evaluateIndexList;
    }
    @Override
    public String getEvaluateTypeByCompanyId( Long companyId) {
        return this.evaluateIndexMapper.getEvaluateTypeByCompanyId(companyId);
    }

    @Override
    public int deleteBatchByCompanyId(Long companyId, Long updatedBy) {
        return evaluateIndexMapper.deleteBatchByCompanyId(companyId, updatedBy);
    }
}
