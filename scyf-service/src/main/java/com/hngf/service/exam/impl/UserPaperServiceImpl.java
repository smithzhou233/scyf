package com.hngf.service.exam.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hngf.common.exception.ScyfException;
import com.hngf.common.utils.PageUtils;
import com.hngf.dto.exam.UserPaperDto;
import com.hngf.dto.exam.UserPaperMarkDto;
import com.hngf.dto.exam.UserQuestionsDto;
import com.hngf.entity.exam.UserPaper;
import com.hngf.entity.exam.UserPaperAnswer;
import com.hngf.entity.exam.UserPaperMarkQuestions;
import com.hngf.mapper.exam.PaperMarkMapper;
import com.hngf.mapper.exam.UserPaperAnswerMapper;
import com.hngf.mapper.exam.UserPaperMapper;
import com.hngf.service.exam.UserPaperService;
import com.hngf.vo.exam.UserPaperVo;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service("userPaperService")
public class UserPaperServiceImpl implements UserPaperService {

    @Autowired
    private UserPaperMapper userPaperMapper;

    @Autowired
    private PaperMarkMapper paperMarkMapper;

    @Autowired
    private UserPaperAnswerMapper userPaperAnswerMapper;

    @Override
    public PageUtils queryPage(Map<String, Object> params,Integer pageNum,Integer pageSize,String order) {
        //利用PageHelper分页查询 注意：这个一定要放查询语句的前一行,否则无法进行分页,因为它对紧随其后第一个sql语句有效
        if(StringUtils.isNotBlank(order)){
            PageHelper.startPage(pageNum, pageSize,order);
        }else{
            PageHelper.startPage(pageNum, pageSize);
        }
        List<UserPaper> list = userPaperMapper.findList(params);
        PageInfo<UserPaper> pageInfo = new PageInfo<UserPaper>(list);
        return new PageUtils(list,(int)pageInfo.getTotal(),pageInfo.getPages(),pageInfo.getPageSize(),pageInfo.getPrePage() + 1);
    }

    @Override
    public UserPaper getById(Long id){
        if(null != id ){return userPaperMapper.findById(id);}
        return null ;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(UserPaper userPaper) {
        userPaperMapper.add(userPaper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(UserPaper userPaper) {
        if(null != userPaper && null != userPaper.getUserPaperId() ){userPaperMapper.update(userPaper);}
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeByIds(List ids, Long updatedBy) {
        if(null != ids && ids.size()>0){userPaperMapper.deleteByIds(ids, updatedBy);}
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeById(Long id, Long updatedBy) {
        if(null != id){userPaperMapper.deleteById(id, updatedBy);}
    }

    /**
     * 考试交卷，保存考试信息
     * @param userPaperVo
     * @param userId
     * @param groupId
     * @param companyId
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void examSumbit(UserPaperVo userPaperVo, Long userId, Long groupId, Long companyId){
        if(null != userPaperVo ){
            Long paperId = userPaperVo.getPaperId();
            Long userPaperId = userPaperVo.getUserPaperId();
            if(null != paperId && null != userPaperId ){
                // 先判断 该试卷是否提交过
                UserPaper curUserPaper = this.userPaperMapper.findById(userPaperId);
                try {
                    if(0 != curUserPaper.getUserPaperStatus().intValue()){
                        throw new ScyfException("试卷已经提交过");
                    }
                } catch (ScyfException e) {
                    e.printStackTrace();
                    throw new ScyfException("试卷已经提交过");
                }
                List<UserPaperMarkQuestions> userPaperMarkQuestionsList = paperMarkMapper.findUserPaperQuestionsListByPaperId(paperId);
                if(null != userPaperMarkQuestionsList && userPaperMarkQuestionsList.size()>0){
                    List<UserPaperAnswer> userPaperAnswerList = new ArrayList<>(userPaperMarkQuestionsList.size());

                    Map<Long,String> paperQuestionsMap = userPaperVo.getPaperQuestionsMap();
                    Integer totalScore = 0 ;
                    for(UserPaperMarkQuestions userPaperMarkQuestions : userPaperMarkQuestionsList ){
                        // 自动算分
                        UserPaperAnswer userPaperAnswer = new UserPaperAnswer();
                        userPaperAnswer.insertPrefix(userId,groupId,companyId);
                        userPaperAnswer.setUserPaperId(userPaperId);
                        String answer = paperQuestionsMap.get(userPaperMarkQuestions.getQuestionsId());
                        userPaperAnswer.initInsert(paperId,userPaperMarkQuestions.getQuestionsId(),userPaperMarkQuestions.getRightAnswer(), answer,  userPaperMarkQuestions.getQuestionsType(),userPaperMarkQuestions.getMark() );
                        // 默认没有答对 得0分
                        userPaperAnswer.setScore(0);

                        // 校验 答案 算分
                        if(null != answer && StringUtils.isNotBlank(answer)) {
                            String rightAnswer = userPaperMarkQuestions.getRightAnswer() ;
                            if(null != rightAnswer ){
                                answer = answer.trim().toUpperCase() ;
                                int answerLength = answer.length() ;
                                if(",".equals(answer.charAt(0))){
                                    answer = answer.substring(1);
                                    answerLength = answerLength-1 ;
                                }
                                if(",".equals(answer.charAt(answerLength-1))){
                                    answer = answer.substring(0,answerLength-1);
                                    answerLength = answerLength-1 ;
                                }
                                if(1== userPaperMarkQuestions.getQuestionsType().intValue() || 3 == userPaperMarkQuestions.getQuestionsType().intValue()  ){
                                    // 单选题 、 判断题
                                    if(answer.equals(rightAnswer)){
                                        //答案 正确 得分
                                        userPaperAnswer.setScore(userPaperMarkQuestions.getMark());
                                        totalScore = Integer.sum(totalScore.intValue(),userPaperMarkQuestions.getMark().intValue());
                                    }
                                }else if( 2 == userPaperMarkQuestions.getQuestionsType().intValue() ){
                                    //多选题
                                    int rightAnswerLength = rightAnswer.length();
                                    if( answerLength == rightAnswerLength  ){
                                        if(answer.equals(rightAnswer)){
                                            //答案 正确 得分
                                            userPaperAnswer.setScore(userPaperMarkQuestions.getMark());
                                            totalScore = Integer.sum(totalScore,userPaperAnswer.getScore());
                                        }
                                    }else if( answerLength < rightAnswerLength){
                                        String[] answerArray = answer.split(",");
                                        boolean flag = true ;
                                        for(String str : answerArray ){
                                            if(rightAnswer.indexOf(str) < 0 ){
                                                // 有一个答案不在正确答案中，不得分
                                                flag = false ;
                                            }
                                        }
                                        if(flag){
                                            // 选少了，且都选对
                                            userPaperAnswer.setScore(userPaperMarkQuestions.getLessMark());
                                            totalScore = Integer.sum(totalScore.intValue(),userPaperAnswer.getScore());
                                        }
                                    }else {
                                        // 选多了 不得分
                                    }
                                }
                            }
                        }
                        userPaperAnswerList.add(userPaperAnswer);
                    }
                    userPaperAnswerMapper.addBatch(userPaperAnswerList);
                    // 更新用户试卷状态为提交 : 0未提交，1提交
                    userPaperMapper.updateUserPaperStatusByUserPaperId(userPaperId, 1, userId);
                    // 更新用户试卷得分
                    userPaperMapper.updateUserScoreByUserPaperId(userPaperId, totalScore);
                }
            }
        }
    }

    /**
     * 根据用户试卷id获取试卷内容，用来进行考试
     * @param id
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public UserPaperDto findDtoById(Long id){
        if(null != id){
            UserPaperDto userPaperDto = userPaperMapper.findDtoById(id);
            Integer userPaperStatus = userPaperDto.getUserPaperStatus();
            if(null != userPaperStatus && 1 == userPaperStatus.intValue() ){
                // 试卷已提交过答案 格式化试题正确答案、用户提交得答案、得分
                List<UserPaperAnswer> userPaperAnswerList = this.userPaperAnswerMapper.findListByUserPaperId( userPaperDto.getUserPaperId());
                if(null != userPaperAnswerList && userPaperAnswerList.size() >0 ){
                    Map<Long, UserPaperAnswer> userPaperAnswerMap = new HashMap<>(userPaperAnswerList.size());
                    for(UserPaperAnswer userPaperAnswer : userPaperAnswerList ){
                        userPaperAnswerMap.put(userPaperAnswer.getQuestionsId(),userPaperAnswer);
                    }
                    List<UserPaperMarkDto> userPaperMarkDtoList = userPaperDto.getUserPaperMarkDtoList();
                    for(UserPaperMarkDto userPaperMarkDto : userPaperMarkDtoList){
                        List<UserQuestionsDto> questionsDtoList = userPaperMarkDto.getQuestionsDtoList();
                        for(UserQuestionsDto userQuestionsDto: questionsDtoList){
                            UserPaperAnswer cur = userPaperAnswerMap.get(userQuestionsDto.getQuestionsId());
//                            userQuestionsDto.setAnswerRight(cur.getDefAnswer());
                            userQuestionsDto.setAnswerUser(cur.getAnswer());
                            userQuestionsDto.setScore(cur.getScore());
                        }
                    }
                }
            }
            return userPaperDto;
        }
        return null;
    }
}
