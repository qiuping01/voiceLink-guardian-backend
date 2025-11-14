package com.ping.voicelinkguardianbackend.cotroller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ping.voicelinkguardianbackend.common.BaseResponse;
import com.ping.voicelinkguardianbackend.common.ResultUtils;
import com.ping.voicelinkguardianbackend.exception.BusinessException;
import com.ping.voicelinkguardianbackend.exception.ErrorCode;
import com.ping.voicelinkguardianbackend.exception.ThrowUtils;
import com.ping.voicelinkguardianbackend.mapper.UserMapper;
import com.ping.voicelinkguardianbackend.model.dto.UserLoginRequest;
import com.ping.voicelinkguardianbackend.model.dto.UserRegisterRequest;
import com.ping.voicelinkguardianbackend.model.entity.User;
import com.ping.voicelinkguardianbackend.model.entity.UserProgress;
import com.ping.voicelinkguardianbackend.model.vo.LoginUserVO;
import com.ping.voicelinkguardianbackend.model.vo.UserProgressVO;
import com.ping.voicelinkguardianbackend.model.vo.UserVO;
import com.ping.voicelinkguardianbackend.service.UserProgressService;
import com.ping.voicelinkguardianbackend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import static com.ping.voicelinkguardianbackend.constant.UserConstant.USER_LOGIN_STATE;

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
     * 用户注销
     *
     * @param request
     * @return
     */
    @PostMapping("/logout")
    public BaseResponse<Boolean> userLogout(HttpServletRequest request) {
        ThrowUtils.throwIf(request == null, ErrorCode.PARAMS_ERROR);
        boolean result = userService.userLogout(request);
        return ResultUtils.success(result);
    }
}
