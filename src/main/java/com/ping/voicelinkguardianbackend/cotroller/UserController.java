package com.ping.voicelinkguardianbackend.cotroller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ping.voicelinkguardianbackend.common.BaseResponse;
import com.ping.voicelinkguardianbackend.common.ResultUtils;
import com.ping.voicelinkguardianbackend.exception.ErrorCode;
import com.ping.voicelinkguardianbackend.exception.ThrowUtils;
import com.ping.voicelinkguardianbackend.mapper.UserMapper;
import com.ping.voicelinkguardianbackend.model.dto.*;
import com.ping.voicelinkguardianbackend.model.entity.User;
import com.ping.voicelinkguardianbackend.model.entity.UserProgress;
import com.ping.voicelinkguardianbackend.model.vo.LoginUserVO;
import com.ping.voicelinkguardianbackend.model.vo.UserProgressVO;
import com.ping.voicelinkguardianbackend.service.DesignCenterService;
import com.ping.voicelinkguardianbackend.service.LevelAnswerService;
import com.ping.voicelinkguardianbackend.service.UserProgressService;
import com.ping.voicelinkguardianbackend.service.UserService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 用户接口
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserMapper userMapper;

    @Resource
    private UserService userService;

    @Resource
    private UserProgressService userProgressService;

    @Resource
    private DesignCenterService designCenterService;

    @Resource
    private LevelAnswerService levelAnswerService;

    /**
     * 用户注册
     */
    @PostMapping("/register")
    public BaseResponse<Long> userRegister(@RequestBody UserRegisterRequest userRegisterRequest) {
        // 1. 参数校验
        ThrowUtils.throwIf(userRegisterRequest == null, ErrorCode.PARAMS_ERROR);
        // 2. 提取参数
        String userAccount = userRegisterRequest.getGroupName();
        String userPassword = userRegisterRequest.getPassword();
        String checkPassword = userRegisterRequest.getCheckPassword();
        // 3. 调用业务层处理
        long result = userService.userRegister(userAccount, userPassword, checkPassword);
        // 4. 返回成功响应
        return ResultUtils.success(result);
    }

    /**
     * 用户登录
     */
    @PostMapping("/login")
    public BaseResponse<LoginUserVO> userLogin(@RequestBody UserLoginRequest userLoginRequest
            , HttpServletRequest request) {
        // 1. 参数校验
        ThrowUtils.throwIf(userLoginRequest == null, ErrorCode.PARAMS_ERROR);
        // 2. 提取参数
        String userAccount = userLoginRequest.getGroupName();
        String userPassword = userLoginRequest.getPassword();
        // 3. 调用业务层处理登录
        LoginUserVO loginUserVO = userService.userLogin(userAccount, userPassword, request);
        // 4. 处理用户进度
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("groupName", userAccount);
        User user = userMapper.selectOne(queryWrapper);
        userProgressService.getOrCreateProgress(user.getId(),userAccount);
        levelAnswerService.getOrCreateLevelAnswer(user.getId(),userAccount);
        // 5. 返回成功响应
        return ResultUtils.success(loginUserVO);
    }

    /**
     * 获取当前登录用户
     */
    @GetMapping("/get/login")
    public BaseResponse<LoginUserVO> getLoginUser(HttpServletRequest request) {
        User loginUser = userService.getLoginUser(request);
        return ResultUtils.success(userService.getLoginUserVO(loginUser));
    }

    /**
     * 获取当前进度
     */
    @GetMapping("/progress")
    public BaseResponse<UserProgressVO> getUserProgress(HttpServletRequest request) {
        User loginUser = userService.getLoginUser(request);
        if (loginUser == null) {
            ThrowUtils.throwIf(request == null, ErrorCode.PARAMS_ERROR);
        }
        UserProgress progress =
                userProgressService.getOrCreateProgress(loginUser.getId(),loginUser.getGroupName());
        UserProgressVO userProgressVO = userService.getUserProgressVO(progress);
        return ResultUtils.success(userProgressVO);
    }

    /**
     * 通关
     */
    @PostMapping("/pass-level")
    public BaseResponse<Boolean> passLevel (HttpServletRequest request) {
        User loginUser = userService.getLoginUser(request);
        if (loginUser == null) {
            ThrowUtils.throwIf(request == null, ErrorCode.PARAMS_ERROR);
        }
        boolean b = userProgressService.passCurrentLevel(loginUser.getId(), loginUser.getGroupName());
        return ResultUtils.success(b);
    }

    /**
     * 上一关
     */
    @PostMapping("/back-level")
    public BaseResponse<Boolean> backLevel (HttpServletRequest request) {
        User loginUser = userService.getLoginUser(request);
        if (loginUser == null) {
            ThrowUtils.throwIf(request == null, ErrorCode.PARAMS_ERROR);
        }
        boolean b = userProgressService.backCurrentLevel(loginUser.getId(),
                loginUser.getGroupName());
        return ResultUtils.success(b);
    }

    /**
     * 用户注销
     *
     * @param request
     * @return
     */
    @PostMapping("/logout")
    public BaseResponse<Boolean> userLogout(HttpServletRequest request) {
        ThrowUtils.throwIf(request == null, ErrorCode.PARAMS_ERROR);
        User loginUser = userService.getLoginUser(request);
        Long userId = loginUser.getId();
        String groupName = loginUser.getGroupName();
        userProgressService.setCurrentLevelForOne(userId, groupName);
        boolean result = userService.userLogout(request);
        return ResultUtils.success(result);
    }

    @PostMapping("/design")
    public BaseResponse<Boolean> writeDesign(@RequestBody DesignCenterRequest designCenterRequest
            ,HttpServletRequest request) {
        ThrowUtils.throwIf(designCenterRequest == null,
                ErrorCode.PARAMS_ERROR);
        User loginUser = userService.getLoginUser(request);
        ThrowUtils.throwIf(loginUser == null, ErrorCode.PARAMS_ERROR);
        Long userId = loginUser.getId();
        String groupName = loginUser.getGroupName();
        String coreScene = designCenterRequest.getCoreScene();
        String presetVoiceCommand = designCenterRequest.getPresetVoiceCommand();
        String systemResponseLogic = designCenterRequest.getSystemResponseLogic();
        boolean result = designCenterService.writeDesignCenter(userId, groupName
                , coreScene, presetVoiceCommand, systemResponseLogic);
        return ResultUtils.success(result);
    }

    @PostMapping("/level1")
    public BaseResponse<Boolean> writeLevel1(@RequestBody FirstLevelRequest firstLevelRequest,
                                             HttpServletRequest request) {
        ThrowUtils.throwIf(firstLevelRequest == null,
                ErrorCode.PARAMS_ERROR);
        User loginUser = userService.getLoginUser(request);
        ThrowUtils.throwIf(loginUser == null, ErrorCode.PARAMS_ERROR);
        Long userId = loginUser.getId();
        String groupName = loginUser.getGroupName();
        boolean result = levelAnswerService.writeFirstAnswer(userId, groupName, firstLevelRequest);
        return ResultUtils.success(result);
    }

    @PostMapping("/level2")
    public BaseResponse<Boolean> writeLevel2(@RequestBody SecondLevelRequest secondLevelRequest
            ,HttpServletRequest request) {
        ThrowUtils.throwIf(secondLevelRequest == null,
                ErrorCode.PARAMS_ERROR);
        User loginUser = userService.getLoginUser(request);
        ThrowUtils.throwIf(loginUser == null, ErrorCode.PARAMS_ERROR);
        Long userId = loginUser.getId();
        String groupName = loginUser.getGroupName();
        boolean result = levelAnswerService.writeSecondAnswer(userId, groupName, secondLevelRequest);
        return ResultUtils.success(result);
    }

    @PostMapping("/level3")
    public BaseResponse<Boolean> writeLevel3(@RequestBody ThirdLevelRequest thirdLevelRequest
                                             ,HttpServletRequest request) {
        ThrowUtils.throwIf(thirdLevelRequest == null,
                ErrorCode.PARAMS_ERROR);
        User loginUser = userService.getLoginUser(request);
        ThrowUtils.throwIf(loginUser == null, ErrorCode.PARAMS_ERROR);
        Long userId = loginUser.getId();
        String groupName = loginUser.getGroupName();
        boolean result = levelAnswerService.writeThirdAnswer(userId, groupName, thirdLevelRequest);
        return ResultUtils.success(result);
    }

    @PostMapping("/level4")
    public BaseResponse<Boolean> writeLevel4(@RequestBody FourthLevelRequest fourthLevelRequest
            ,HttpServletRequest request) {
        ThrowUtils.throwIf(fourthLevelRequest == null,
                ErrorCode.PARAMS_ERROR);
        User loginUser = userService.getLoginUser(request);
        ThrowUtils.throwIf(loginUser == null, ErrorCode.PARAMS_ERROR);
        Long userId = loginUser.getId();
        String groupName = loginUser.getGroupName();
        boolean result = levelAnswerService.writeFourthAnswer(userId, groupName, fourthLevelRequest);
        return ResultUtils.success(result);
    }

    @PostMapping("/level5")
    public BaseResponse<Boolean> writeLevel5(@RequestBody FifthLevelRequest fifthLevelRequest
            ,HttpServletRequest request) {
        ThrowUtils.throwIf(fifthLevelRequest == null,
                ErrorCode.PARAMS_ERROR);
        User loginUser = userService.getLoginUser(request);
        ThrowUtils.throwIf(loginUser == null, ErrorCode.PARAMS_ERROR);
        Long userId = loginUser.getId();
        String groupName = loginUser.getGroupName();
        boolean result = levelAnswerService.writeFifthAnswer(userId, groupName, fifthLevelRequest);
        return ResultUtils.success(result);
    }

    @PostMapping("/level6")
    public BaseResponse<Boolean> writeLevel6(@RequestBody SixthLevelRequest sixthLevelRequest
            ,HttpServletRequest request) {
        ThrowUtils.throwIf(sixthLevelRequest == null,
                ErrorCode.PARAMS_ERROR);
        User loginUser = userService.getLoginUser(request);
        ThrowUtils.throwIf(loginUser == null, ErrorCode.PARAMS_ERROR);
        Long userId = loginUser.getId();
        String groupName = loginUser.getGroupName();
        boolean result = levelAnswerService.writeSixthAnswer(userId, groupName, sixthLevelRequest);
        return ResultUtils.success(result);
    }
}
