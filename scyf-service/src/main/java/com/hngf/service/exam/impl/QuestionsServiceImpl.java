package com.hngf.service.exam.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hngf.common.exception.ScyfException;
import com.hngf.common.utils.PageUtils;
import com.hngf.entity.exam.Questions;
import com.hngf.mapper.exam.PaperQuestionsMapper;
import com.hngf.mapper.exam.QuestionsMapper;
import com.hngf.service.exam.QuestionsService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;


@Service("questionsService")
public class QuestionsServiceImpl implements QuestionsService {

    @Autowired
    private QuestionsMapper questionsMapper;

    @Autowired
    private PaperQuestionsMapper paperQuestionsMapper;

    @Override
    public PageUtils queryPage(Map<String, Object> params,Integer pageNum,Integer pageSize,String order) {
        //利用PageHelper分页查询 注意：这个一定要放查询语句的前一行,否则无法进行分页,因为它对紧随其后第一个sql语句有效
        if(StringUtils.isNotBlank(order)){
            PageHelper.startPage(pageNum, pageSize,order);
        }else{
            PageHelper.startPage(pageNum, pageSize);
        }
        List<Questions> list = questionsMapper.findGroupList(params);
        PageInfo<Questions> pageInfo = new PageInfo<Questions>(list);
        return new PageUtils(list,(int)pageInfo.getTotal(),pageInfo.getPages(),pageInfo.getPageSize(),pageInfo.getPrePage() + 1);
    }

    @Override
    public PageUtils queryPageToPaper(Map<String, Object> params,Integer pageNum,Integer pageSize) {

        PageHelper.startPage(pageNum, pageSize);

        List<Questions> list = questionsMapper.findPageList(params);
        PageInfo<Questions> pageInfo = new PageInfo<Questions>(list);
        return new PageUtils(list,(int)pageInfo.getTotal(),pageInfo.getPages(),pageInfo.getPageSize(),pageInfo.getPrePage() + 1);
    }

    @Override
    public Questions getById(Long id){
        if(null != id ){
            return questionsMapper.findById(id);
        }
        return null ;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(Questions questions) {
        if(null == questions.getQuestionsType() || 0 == questions.getQuestionsType().intValue() ){throw new ScyfException("试题类型不能为空！");}
        toUpperCaseOnRightAnswer(questions);
        questionsMapper.add(questions);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(Questions questions) {
        if(null != questions && null != questions.getQuestionsId() ){
            // 校验试题是否用来组成试卷，
            long count = paperQuestionsMapper.getCountByQuestionsId(questions.getQuestionsId());
            if(0 == count ) {
                toUpperCaseOnRightAnswer(questions);
                questionsMapper.update(questions);
            }else{
                throw new ScyfException("试题已被使用不能修改！");
            }
        }
    }

    private void toUpperCaseOnRightAnswer(Questions questions){
        String rightAnswer = questions.getRightAnswer();
        if(null != rightAnswer && StringUtils.isNotBlank(rightAnswer)){
            rightAnswer = rightAnswer.trim();
            int rightAnswerLength = rightAnswer.length() ;
            if(",".equals(rightAnswer.charAt(0))){
                rightAnswer = rightAnswer.substring(1);
                rightAnswerLength = rightAnswer.length() ;
            }
            if(",".equals(rightAnswer.charAt(rightAnswerLength-1))){
                rightAnswer = rightAnswer.substring(0,rightAnswerLength-1);
            }
            questions.setRightAnswer(rightAnswer.toUpperCase());
        }
    }
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeByIds(List ids, Long updatedBy) {
        if(null != ids && ids.size() > 0){
            questionsMapper.deleteByIds(ids, updatedBy);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeById(Long id, Long updatedBy) {
        if(null != id ){
            long count = paperQuestionsMapper.getCountByQuestionsId(id);
            if(0 == count ){
                questionsMapper.deleteById(id, updatedBy);
            }else{
                throw new ScyfException("试题已被使用不能删除！");
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void enableOrUnable(List<Long> questionsIdList, Integer questionsStatus, Long updatedBy) {
        if(null != questionsIdList && questionsIdList.size()>0 && null !=questionsStatus ){
            questionsMapper.updateQuestionsStatusByIds(questionsIdList, questionsStatus,updatedBy);
        }
    }
}
