package com.ping.voicelinkguardianbackend.cotroller;

import com.ping.voicelinkguardianbackend.common.BaseResponse;
import com.ping.voicelinkguardianbackend.common.ResultUtils;
import com.ping.voicelinkguardianbackend.exception.BusinessException;
import com.ping.voicelinkguardianbackend.exception.ErrorCode;
import com.ping.voicelinkguardianbackend.exception.ThrowUtils;
import com.ping.voicelinkguardianbackend.model.entity.LevelAnswer;
import com.ping.voicelinkguardianbackend.model.entity.User;
import com.ping.voicelinkguardianbackend.model.entity.UserProgress;
import com.ping.voicelinkguardianbackend.model.enums.UserRoleEnum;
import com.ping.voicelinkguardianbackend.model.vo.LevelAnswerVO;
import com.ping.voicelinkguardianbackend.model.vo.UserProgressVO;
import com.ping.voicelinkguardianbackend.service.LevelAnswerService;
import com.ping.voicelinkguardianbackend.service.UserProgressService;
import com.ping.voicelinkguardianbackend.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 教师接口
 */
@RestController
@RequestMapping("/teacher")
public class TeacherController {

    @Resource
    private UserProgressService userProgressService;

    @Resource
    private UserService userService;

    @Resource
    private LevelAnswerService levelAnswerService;

    /**
     * 获取所有用户进度列表
     *
     * @return
     */
    @GetMapping("/all-progress")
    public BaseResponse<List<UserProgressVO>> getAllUserProgress(HttpServletRequest request) {
        User loginUser = userService.getLoginUser(request);
        String userRole = loginUser.getUserRole();
        ThrowUtils.throwIf(!UserRoleEnum.ADMIN.getValue().equals(userRole), ErrorCode.NO_AUTH_ERROR);
        // 1. 获取所有用户进度列表
        List<UserProgress> userProgressList = userProgressService.list();
        // 2. 转换为脱敏的VO列表
        List<UserProgressVO> progressVOList = userService.getUserProgressVOList(userProgressList);
        return ResultUtils.success(progressVOList);
    }

    @GetMapping("/all-answer")
    public BaseResponse<List<LevelAnswerVO>> getAllUserAnswer(HttpServletRequest request) {
        User loginUser = userService.getLoginUser(request);
        String userRole = loginUser.getUserRole();
        ThrowUtils.throwIf(!UserRoleEnum.ADMIN.getValue().equals(userRole), ErrorCode.NO_AUTH_ERROR);
        // 1. 获取所有用户设计列表
        List<LevelAnswer> levelAnswerList = levelAnswerService.list();
        // 2. 转换为脱敏的VO列表
        List<LevelAnswerVO> levelAnswerVOList = levelAnswerService.getLevelAnswerVOList(levelAnswerList);
        return ResultUtils.success(levelAnswerVOList);
    }

    /**
     * 重置所有小组进度
     */
    @PostMapping("/delete-all-progress")
    public BaseResponse<Boolean> deleteAllUserProgress(HttpServletRequest request) {
        User loginUser = userService.getLoginUser(request);
        String userRole = loginUser.getUserRole();
        ThrowUtils.throwIf(!UserRoleEnum.ADMIN.getValue().equals(userRole), ErrorCode.NO_AUTH_ERROR);
        boolean remove = userProgressService.remove(null);
        boolean remove1 = levelAnswerService.remove(null);
        ThrowUtils.throwIf(!remove, new BusinessException(ErrorCode.SYSTEM_ERROR));
        ThrowUtils.throwIf(!remove1, new BusinessException(ErrorCode.SYSTEM_ERROR));
        return ResultUtils.success(true);
    }
}
