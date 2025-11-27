package com.ping.voicelinkguardianbackend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ping.voicelinkguardianbackend.exception.ErrorCode;
import com.ping.voicelinkguardianbackend.exception.ThrowUtils;
import com.ping.voicelinkguardianbackend.model.dto.*;
import com.ping.voicelinkguardianbackend.mapper.LevelAnswerMapper;
import com.ping.voicelinkguardianbackend.model.entity.LevelAnswer;
import com.ping.voicelinkguardianbackend.model.entity.UserProgress;
import com.ping.voicelinkguardianbackend.model.vo.LevelAnswerVO;
import com.ping.voicelinkguardianbackend.service.LevelAnswerService;
import com.ping.voicelinkguardianbackend.service.UserProgressService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author 21877
 * @description 针对表【level_answer(关卡答案)】的数据库操作Service实现
 * @createDate 2025-11-23 16:48:46
 */
@Service
public class LevelAnswerServiceImpl extends ServiceImpl<LevelAnswerMapper, LevelAnswer>
        implements LevelAnswerService {

    @Resource
    private UserProgressService userProgressService;

    @Resource
    private LevelAnswerMapper levelAnswerMapper;

    /**
     * 获取关卡答案 VO
     *
     * @param levelAnswer
     * @return
     */
    @Override
    public LevelAnswerVO getLevelAnswerVO(LevelAnswer levelAnswer) {
        if (levelAnswer == null) {
            return null;
        }
        LevelAnswerVO levelAnswerVO = new LevelAnswerVO();
        BeanUtils.copyProperties(levelAnswer, levelAnswerVO);
        return levelAnswerVO;
    }

    /**
     * 获取关卡答案 VO 列表
     *
     * @param levelAnswerList
     * @return
     */
    @Override
    public List<LevelAnswerVO> getLevelAnswerVOList(List<LevelAnswer> levelAnswerList) {
        if (levelAnswerList == null) {
            return new ArrayList<>();
        }
        return levelAnswerList.stream()
                .map(this::getLevelAnswerVO)
                .collect(Collectors.toList());
    }

    /**
     * 获取或创建关卡答案
     *
     * @param userId
     * @param groupName
     * @return
     */
    @Override
    public LevelAnswer getOrCreateLevelAnswer(Long userId, String groupName) {
        QueryWrapper<LevelAnswer> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userId", userId);
        queryWrapper.eq("groupName", groupName);
        LevelAnswer levelAnswer = levelAnswerMapper.selectOne(queryWrapper);
        if (levelAnswer == null) {
            levelAnswer = new LevelAnswer();
            levelAnswer.setUserId(userId);
            levelAnswer.setGroupName(groupName);
            levelAnswer.setFirstDilemma("暂未填写");
            levelAnswer.setSecondScene("暂未填写");
            levelAnswer.setSecondRisk("暂未填写");
            levelAnswer.setSecondDesign1("暂未填写");
            levelAnswer.setSecondDesign2("暂未填写");
            levelAnswer.setSecondDesign3("暂未填写");
            levelAnswer.setSecondDesign4("暂未填写");
            levelAnswer.setThirdChallenge("暂未填写");
            levelAnswer.setFourthScene("暂未填写");
            levelAnswer.setFourthAchieve("暂未填写");
            levelAnswer.setFourthExtra("暂未填写");
            levelAnswer.setFourthProblem("暂未填写");
            levelAnswer.setFourthSolution("暂未填写");
            levelAnswer.setFifthComment1("暂未填写");
            levelAnswer.setFifthComment2("暂未填写");
            levelAnswer.setFifthComment3("暂未填写");
            levelAnswer.setFifthComment4("暂未填写");
            levelAnswer.setFifthComment5("暂未填写");
            levelAnswer.setFifthComment6("暂未填写");
            levelAnswer.setSixthEduComment1("暂未填写");
            levelAnswer.setSixthEduComment2("暂未填写");
            levelAnswer.setSixthEduComment3("暂未填写");
            levelAnswer.setSixthFeedback1("暂未填写");
            levelAnswer.setSixthFeedback2("暂未填写");
            levelAnswer.setCreateTime(new Date());
            levelAnswer.setUpdateTime(new Date());
            levelAnswerMapper.insert(levelAnswer);
        }
        return levelAnswer;
    }

    /**
     * 获取当前用户进度
     *
     * @param userId
     * @param groupName
     * @return
     */
    @Override
    public int getCurrentUserLevel(Long userId, String groupName) {
        QueryWrapper<UserProgress> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userId", userId);
        queryWrapper.eq("groupName", groupName);
        UserProgress userProgress = userProgressService.getOne(queryWrapper);
        if (userProgress == null) {
            return 0;
        }
        return userProgress.getCurrentLevel();
    }

    /**
     * 写入第一关答案
     *
     * @param userId
     * @param groupName
     * @param firstLevelRequest
     * @return
     */
    @Override
    public boolean writeFirstAnswer(Long userId, String groupName,
                                    FirstLevelRequest firstLevelRequest) {
        ThrowUtils.throwIf(firstLevelRequest == null, ErrorCode.PARAMS_ERROR);
        int currentUserLevel = getCurrentUserLevel(userId, groupName);
        if (currentUserLevel != 1) {
            return false;
        }
        LevelAnswer levelAnswer = getOrCreateLevelAnswer(userId, groupName);
        String firstDilemma = firstLevelRequest.getFirstDilemma();
        levelAnswer.setFirstDilemma(firstDilemma);
        levelAnswerMapper.updateById(levelAnswer);
        return true;
    }

    /**
     * 写入第二关答案
     *
     * @param userId
     * @param groupName
     * @param secondLevelRequest
     * @return
     */
    @Override
    public boolean writeSecondAnswer(Long userId, String groupName, SecondLevelRequest secondLevelRequest) {
        ThrowUtils.throwIf(secondLevelRequest == null, ErrorCode.PARAMS_ERROR);
        int currentUserLevel = getCurrentUserLevel(userId, groupName);
        if (currentUserLevel != 2) {
            return false;
        }
        LevelAnswer orCreateLevelAnswer = getOrCreateLevelAnswer(userId, groupName);
        String secondScene = secondLevelRequest.getSecondScene();
        String secondRisk = secondLevelRequest.getSecondRisk();
        String secondDesign1 = secondLevelRequest.getSecondDesign1();
        String secondDesign2 = secondLevelRequest.getSecondDesign2();
        String secondDesign3 = secondLevelRequest.getSecondDesign3();
        String secondDesign4 = secondLevelRequest.getSecondDesign4();
        orCreateLevelAnswer.setSecondScene(secondScene);
        orCreateLevelAnswer.setSecondRisk(secondRisk);
        orCreateLevelAnswer.setSecondDesign1(secondDesign1);
        orCreateLevelAnswer.setSecondDesign2(secondDesign2);
        orCreateLevelAnswer.setSecondDesign3(secondDesign3);
        orCreateLevelAnswer.setSecondDesign4(secondDesign4);
        levelAnswerMapper.updateById(orCreateLevelAnswer);
        return true;
    }

    /**
     * 写入第三关答案
     *
     * @param userId
     * @param groupName
     * @param thirdLevelRequest
     * @return
     */
    @Override
    public boolean writeThirdAnswer(Long userId, String groupName,
                                    ThirdLevelRequest thirdLevelRequest) {
        ThrowUtils.throwIf(thirdLevelRequest == null, ErrorCode.PARAMS_ERROR);
        int currentUserLevel = getCurrentUserLevel(userId, groupName);
        if (currentUserLevel != 3) {
            return false;
        }
        LevelAnswer orCreateLevelAnswer = getOrCreateLevelAnswer(userId, groupName);
        String thirdChallenge = thirdLevelRequest.getThirdChallenge();
        orCreateLevelAnswer.setThirdChallenge(thirdChallenge);
        levelAnswerMapper.updateById(orCreateLevelAnswer);
        return true;
    }

    /**
     * 写入第四关答案
     *
     * @param userId
     * @param groupName
     * @param fourthLevelRequest
     * @return
     */
    @Override
    public boolean writeFourthAnswer(Long userId, String groupName, FourthLevelRequest fourthLevelRequest) {
        ThrowUtils.throwIf(fourthLevelRequest == null, ErrorCode.PARAMS_ERROR);
        int currentUserLevel = getCurrentUserLevel(userId, groupName);
        if (currentUserLevel != 4) {
            return false;
        }
        LevelAnswer orCreateLevelAnswer = getOrCreateLevelAnswer(userId, groupName);
        String fourthScene = fourthLevelRequest.getFourthScene();
        String fourthAchieve = fourthLevelRequest.getFourthAchieve();
        String fourthExtra = fourthLevelRequest.getFourthExtra();
        String fourthProblem = fourthLevelRequest.getFourthProblem();
        String fourthSolution = fourthLevelRequest.getFourthSolution();
        orCreateLevelAnswer.setFourthScene(fourthScene);
        orCreateLevelAnswer.setFourthAchieve(fourthAchieve);
        orCreateLevelAnswer.setFourthExtra(fourthExtra);
        orCreateLevelAnswer.setFourthProblem(fourthProblem);
        orCreateLevelAnswer.setFourthSolution(fourthSolution);
        levelAnswerMapper.updateById(orCreateLevelAnswer);
        return true;
    }

    /**
     * 写入第五关答案
     *
     * @param userId
     * @param groupName
     * @param fifthLevelRequest
     * @return
     */
    @Override
    public boolean writeFifthAnswer(Long userId, String groupName, FifthLevelRequest fifthLevelRequest) {
        ThrowUtils.throwIf(fifthLevelRequest == null, ErrorCode.PARAMS_ERROR);
        int currentUserLevel = getCurrentUserLevel(userId, groupName);
        if (currentUserLevel != 5) {
            return false;
        }
        LevelAnswer orCreateLevelAnswer = getOrCreateLevelAnswer(userId, groupName);
        String fifthComment1 = fifthLevelRequest.getFifthComment1();
        String fifthComment2 = fifthLevelRequest.getFifthComment2();
        String fifthComment3 = fifthLevelRequest.getFifthComment3();
        String fifthComment4 = fifthLevelRequest.getFifthComment4();
        String fifthComment5 = fifthLevelRequest.getFifthComment5();
        String fifthComment6 = fifthLevelRequest.getFifthComment6();
        orCreateLevelAnswer.setFifthComment1(fifthComment1);
        orCreateLevelAnswer.setFifthComment2(fifthComment2);
        orCreateLevelAnswer.setFifthComment3(fifthComment3);
        orCreateLevelAnswer.setFifthComment4(fifthComment4);
        orCreateLevelAnswer.setFifthComment5(fifthComment5);
        orCreateLevelAnswer.setFifthComment6(fifthComment6);
        levelAnswerMapper.updateById(orCreateLevelAnswer);
        return true;
    }

    /**
     * 写入第六关答案
     *
     * @param userId
     * @param groupName
     * @param sixthLevelRequest
     * @return
     */
    @Override
    public boolean writeSixthAnswer(Long userId, String groupName, SixthLevelRequest sixthLevelRequest) {
        ThrowUtils.throwIf(sixthLevelRequest == null, ErrorCode.PARAMS_ERROR);
        int currentUserLevel = getCurrentUserLevel(userId, groupName);
        if (currentUserLevel != 6) {
            return false;
        }
        LevelAnswer orCreateLevelAnswer = getOrCreateLevelAnswer(userId, groupName);
        String sixthEduComment1 = sixthLevelRequest.getSixthEduComment1();
        String sixthEduComment2 = sixthLevelRequest.getSixthEduComment2();
        String sixthEduComment3 = sixthLevelRequest.getSixthEduComment3();
        String sixthFeedback1 = sixthLevelRequest.getSixthFeedback1();
        String sixthFeedback2 = sixthLevelRequest.getSixthFeedback2();
        orCreateLevelAnswer.setSixthEduComment1(sixthEduComment1);
        orCreateLevelAnswer.setSixthEduComment2(sixthEduComment2);
        orCreateLevelAnswer.setSixthEduComment3(sixthEduComment3);
        orCreateLevelAnswer.setSixthFeedback1(sixthFeedback1);
        orCreateLevelAnswer.setSixthFeedback2(sixthFeedback2);
        levelAnswerMapper.updateById(orCreateLevelAnswer);
        return true;
    }
}




