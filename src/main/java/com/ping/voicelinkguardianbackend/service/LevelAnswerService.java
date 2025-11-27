package com.ping.voicelinkguardianbackend.service;

import com.ping.voicelinkguardianbackend.model.dto.*;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ping.voicelinkguardianbackend.model.entity.LevelAnswer;
import com.ping.voicelinkguardianbackend.model.vo.LevelAnswerVO;

import java.util.List;

/**
 * @author 21877
 * @description 针对表【level_answer(关卡答案)】的数据库操作Service
 * @createDate 2025-11-23 16:48:46
 */
public interface LevelAnswerService extends IService<LevelAnswer> {

    /**
     * 获取关卡答案 VO
     *
     * @param levelAnswer
     * @return
     */
    LevelAnswerVO getLevelAnswerVO(LevelAnswer levelAnswer);

    /**
     * 获取关卡答案 VO 列表
     *
     * @param levelAnswerList
     * @return
     */
    List<LevelAnswerVO> getLevelAnswerVOList(List<LevelAnswer> levelAnswerList);

    /**
     * 获取或创建关卡答案
     *
     * @param userId
     * @param groupName
     * @return
     */
    LevelAnswer getOrCreateLevelAnswer(Long userId, String groupName);

    /**
     * 获取当前用户关卡
     *
     * @param userId
     * @param groupName
     * @return
     */
    int getCurrentUserLevel(Long userId, String groupName);

    /**
     * 写入第一关答案
     *
     * @param userId
     * @param groupName
     * @param firstLevelRequest
     * @return
     */
    boolean writeFirstAnswer(Long userId, String groupName,
                             FirstLevelRequest firstLevelRequest);

    /**
     * 写入第二关答案
     *
     * @param userId
     * @param groupName
     * @param secondLevelRequest
     * @return
     */
    boolean writeSecondAnswer(Long userId, String groupName, SecondLevelRequest secondLevelRequest);

    /**
     * 写入第三关答案
     *
     * @param userId
     * @param groupName
     * @param thirdLevelRequest
     * @return
     */
    boolean writeThirdAnswer(Long userId, String groupName,
                             ThirdLevelRequest thirdLevelRequest);

    /**
     * 写入第四关答案
     *
     * @param userId
     * @param groupName
     * @param fourthLevelRequest
     * @return
     */
    boolean writeFourthAnswer(Long userId, String groupName, FourthLevelRequest fourthLevelRequest);

    /**
     * 写入第五关答案
     *
     * @param userId
     * @param groupName
     * @param fifthLevelRequest
     * @return
     */
    boolean writeFifthAnswer(Long userId, String groupName,
                             FifthLevelRequest fifthLevelRequest);

    /**
     * 写入第五关答案
     *
     * @param userId
     * @param groupName
     * @param sixthLevelRequest
     * @return
     */
    boolean writeSixthAnswer(Long userId, String groupName,
                             SixthLevelRequest sixthLevelRequest);
}
