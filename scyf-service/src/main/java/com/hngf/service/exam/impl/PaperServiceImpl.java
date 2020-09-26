package com.hngf.service.exam.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hngf.common.exception.ScyfException;
import com.hngf.common.utils.PageUtils;
import com.hngf.dto.exam.PaperDto;
import com.hngf.entity.exam.Paper;
import com.hngf.entity.exam.PaperMark;
import com.hngf.entity.exam.PaperQuestions;
import com.hngf.entity.exam.UserPaper;
import com.hngf.mapper.exam.PaperMapper;
import com.hngf.mapper.exam.PaperMarkMapper;
import com.hngf.mapper.exam.PaperQuestionsMapper;
import com.hngf.mapper.exam.QuestionsMapper;
import com.hngf.mapper.exam.UserPaperMapper;
import com.hngf.service.exam.PaperService;
import com.hngf.vo.exam.PaperMarkVo;
import com.hngf.vo.exam.PaperToUserVo;
import com.hngf.vo.exam.PaperVo;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service("paperService")
public class PaperServiceImpl implements PaperService {

    @Autowired
    private PaperMapper paperMapper;

    @Autowired
    private PaperMarkMapper paperMarkMapper;

    @Autowired
    private PaperQuestionsMapper paperQuestionsMapper;

    @Autowired
    private QuestionsMapper questionsMapper;

    @Autowired
    private UserPaperMapper userPaperMapper;

    @Override
    public PageUtils queryPage(Map<String, Object> params,Integer pageNum,Integer pageSize,String order) {
        //利用PageHelper分页查询 注意：这个一定要放查询语句的前一行,否则无法进行分页,因为它对紧随其后第一个sql语句有效
        if(StringUtils.isNotBlank(order)){
            PageHelper.startPage(pageNum, pageSize,order);
        }else{
            PageHelper.startPage(pageNum, pageSize);
        }
        List<Paper> list = paperMapper.findGroupList(params);
        PageInfo<Paper> pageInfo = new PageInfo<Paper>(list);
        return new PageUtils(list,(int)pageInfo.getTotal(),pageInfo.getPages(),pageInfo.getPageSize(),pageInfo.getPrePage() + 1);
    }

    @Override
    public Paper getById(Long id){
        if(null != id ){
            return paperMapper.findById(id);
        }
        return null ;
    }
    @Override
    public PaperDto getDtoById(Long id){
        if(null != id ){
            PaperDto paperDto = paperMapper.findDtoById(id);
            if(null != paperDto ){
                Map<String,Object> paramMap = new HashMap<>(4);
                paramMap.put("companyId",paperDto.getCompanyId());
                paperDto.setQuestionsList(questionsMapper.findToPaperInfoList(paramMap));
            }
            return paperDto;
        }
        return null ;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(PaperVo paperVo , Long userId , Long companyId) {
        /*
         *保存试卷基本信息
         */
        Paper paper = getFromPaperVo(paperVo);
        paper.insertPrefix(userId,companyId,0);
        // 保证试卷创建时的状态是未发布
        if(null == paper.getPaperStatus() || 0 != paper.getPaperStatus().intValue() ){
            paper.setPaperStatus(0);
        }
        paperMapper.add(paper);
        // 判断试卷是否创建成功
        Long paperId = paper.getPaperId();
        if(null == paperId || 0 == paperId.intValue() ){
            throw new ScyfException("试卷创建失败！");
        }
        /*
         *保存试卷类型分数和试卷类型试题
         */
        paperVo.setPaperId(paperId);
        disposePaperMarkAndQuestions(paperVo , userId , companyId , true);
    }

    /**
     * 从前端传递的对象PaperVo Bean中获取 Paper
     * @param paperVo
     * @return Paper
     */
    private Paper getFromPaperVo(PaperVo paperVo){
        Paper paper = new Paper();
        try {
            BeanUtils.copyProperties(paper,paperVo);
        } catch (IllegalAccessException e) {
            // paper paperVo NULL
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            // dest can not set bean properties
            e.printStackTrace();
            // custom
            paper.setPaperName(paperVo.getPaperName());
            paper.setPaperInfo(paperVo.getPaperInfo());
            paper.setAnswerTime(paperVo.getAnswerTime());
            paper.setPaperStatus(paperVo.getPaperStatus());
            paper.setGroupId(paperVo.getGroupId());
        }
        return  paper;
    }

    /**
     * 从前端传递的对象PaperMarkVo Bean中获取 PaperMark
     * @param paperMarkVo
     * @return PaperMark
     */
    private PaperMark getFromPaperMarkVo(PaperMarkVo paperMarkVo){
        PaperMark paperMark = new PaperMark();
        try {
            BeanUtils.copyProperties(paperMark,paperMarkVo);
        } catch (IllegalAccessException e) {
            // paperMark paperMarkVo NULL
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            // dest can not set bean properties
            e.printStackTrace();
            // custom
            paperMark.setPaperId(paperMarkVo.getPaperId());
            paperMark.setQuestionsType(paperMarkVo.getQuestionsType());
            paperMark.setMark(paperMarkVo.getMark());
            paperMark.setLessMark(paperMarkVo.getLessMark());
            paperMark.setMoreMark(paperMarkVo.getMoreMark());
        }
        return paperMark;
    }

    /**
     *
     * @param paperVo
     * @param userId
     * @param companyId
     * @param addFlag true 新增；false 修改
     */
    private void disposePaperMarkAndQuestions(PaperVo paperVo , Long userId ,Long companyId , boolean addFlag ){
        // 从试卷信息中获取到 试卷试题类型分数信息
        List<PaperMarkVo> paperMarkVoList = paperVo.getPaperMarkVoList();
        if(addFlag){
            // 新增
            if(null == paperMarkVoList || paperMarkVoList.isEmpty()){
                throw new ScyfException("试卷类型分数不能为空！");
            }
        }else {
            if(null != paperMarkVoList && paperMarkVoList.size() >0 ){
                //删除顺序：先试卷类型试题 再 试卷类型
                // 更新试卷类型试题 删除原有的
                paperQuestionsMapper.deleteBatchByPaperId(paperVo.getPaperId(), userId) ;
                // 更新试卷类型分数 删除原有的
                paperMarkMapper.deleteBatchPaperMarkByPaperId(paperVo.getPaperId(),userId);
            }
        }
        if(addFlag || ( !addFlag && null !=paperMarkVoList && paperMarkVoList.size() >0) ){
            List<PaperMark> paperMarkList = new ArrayList<>();
            for(PaperMarkVo paperMarkVo : paperMarkVoList ){
                paperMarkVo.setPaperId(paperVo.getPaperId());
                PaperMark paperMark = getFromPaperMarkVo(paperMarkVo);
                paperMark.setGroupId(paperVo.getGroupId());
                paperMark.insertPrefix(userId,companyId,0);
                paperMarkList.add(paperMark);
            }
            // 批量增加试卷类型分数
            paperMarkMapper.addBatch(paperMarkList);
            /*
             *保存试卷试题关系
             */
            List<PaperQuestions> paperQuestionsList = new ArrayList<>();
            for(PaperMarkVo paperMarkVo : paperMarkVoList ){
                List<Long> paperMarkIdList = paperMarkMapper.findIdByPaperIdAndQuestionsType(paperMarkVo.getPaperId(),paperMarkVo.getQuestionsType());
                if( null != paperMarkIdList && paperMarkIdList.size() >0 ){
                    if(1 == paperMarkIdList.size()){
                        List<Long> questionsIdList = paperMarkVo.getQuestionsIdList();
                        if(null != questionsIdList ){
                            for(Long questionsId : questionsIdList){
                                PaperQuestions paperQuestions = new PaperQuestions();
                                paperQuestions.setPaperMarkId(paperMarkIdList.get(0));
                                paperQuestions.setQuestionsId(questionsId);
                                paperQuestions.setGroupId(paperVo.getGroupId());
                                paperQuestions.insertPrefix(userId,companyId,0);
                                paperQuestionsList.add(paperQuestions);
                            }
                        }
                    }else  {
                        throw new ScyfException("试卷中试题类型不能重复！");
                    }
                }
            }
            if( paperQuestionsList.isEmpty()){
                throw new ScyfException("试卷中试题不能为空！");
            }
            paperQuestionsMapper.addBatch(paperQuestionsList);
        }
    }
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(PaperVo paperVo , Long userId ,Long companyId) {
        if(null != paperVo && null != paperVo.getPaperId() ){
            // 判断盖试卷是否发布，若是发布则不允许修改
            Integer paperStatus = paperMapper.findPaperStatusById(paperVo.getPaperId());
            if(null != paperStatus ){
                // 试卷状态 0-未发布,1-发布
                if( 1 == paperStatus.intValue() ){
                    throw new ScyfException("试卷已发布，不能修改！");
                }
                Paper paper =  getFromPaperVo(paperVo);
                paper.updatePrefix(userId);
                paperMapper.update(paper);
                /*
                 *更新试卷类型分数和试卷类型试题
                 */
                disposePaperMarkAndQuestions(paperVo , userId , companyId , false);
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeByIds(List ids, Long updatedBy) {
        if(null != ids && ids.size()>0){paperMapper.deleteByIds(ids, updatedBy);}
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeById(Long id, Long updatedBy) {
        if(null != id){
            int result = paperMapper.deleteById(id, updatedBy);
            if(0 == result ){
                throw new ScyfException("试卷已发布,不能删除！");
            }else{
                // 删除试卷内容：试卷类型分数、试卷试题
                // 先删除试卷类型中的试题
                paperQuestionsMapper.deleteBatchByPaperId(id, updatedBy);
                // 后删除试卷类型分数
                paperMarkMapper.deleteBatchPaperMarkByPaperId(id, updatedBy);
            }
        }
    }

    /**
     * 试卷发布/撤销
     * status=0撤销，=1 或者 null 发布
     * @param  paperToUserVo
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updatePaperStatus(PaperToUserVo paperToUserVo , Long userId ,Long  groupId, Long companyId ){
        Long paperId = paperToUserVo.getPaperId() ;
        String paperName = paperToUserVo.getPaperName();
        Integer status = paperToUserVo.getStatus() ;
        if(null != paperId && null !=status ){
            if(1 == status.intValue()){
                //发布
                // 更新试卷状态
                paperMapper.updatePaperStatus(paperId,1, userId);
                // 新增用户试卷关联信息
                // 获取试卷发布到的人员Id集合
                List<Long> userIdList = paperToUserVo.getUserIdList();
                if(null !=userIdList && userIdList.size() > 0 ){
                    // 获取试卷的总分
                    Integer totalMark = paperMarkMapper.getTotalMarkByPaperId(paperId);
                    List<UserPaper> userPaperList = new ArrayList<>(userIdList.size());
                    for(Long id : userIdList ){
                        UserPaper userPaper = new UserPaper();
                        userPaper.insertPrefix(userId,groupId,companyId);
                        userPaper.initAddBatch(id,paperId ,paperName ,0, totalMark);
                        userPaperList.add(userPaper);
                    }
                    userPaperMapper.addBatch(userPaperList);
                }
            }else if(0 == status.intValue() ){
                // 撤销
                List<Long> usedPaperIdList = userPaperMapper.usedPaperIdList(paperId);
                // 先校验试卷是否被提交过
                if(null == usedPaperIdList || 0 == usedPaperIdList.size()){
                    // 更新试卷状态
                    paperMapper.updatePaperStatus(paperId,0, userId);
                    // 删除试卷关联的用户信息
                    userPaperMapper.deleteByPaperId(paperId, companyId, userId, new Date());
                }else{
                    throw new ScyfException("试卷已被使用，不能撤销！");
                }
            }
        }
    }
}
