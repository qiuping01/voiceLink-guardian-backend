package com.ping.voicelinkguardianbackend.cotroller;

import com.ping.voicelinkguardianbackend.common.BaseResponse;
import com.ping.voicelinkguardianbackend.common.ResultUtils;
import com.ping.voicelinkguardianbackend.exception.ErrorCode;
import com.ping.voicelinkguardianbackend.exception.ThrowUtils;
import com.ping.voicelinkguardianbackend.model.dto.UserLoginRequest;
import com.ping.voicelinkguardianbackend.model.dto.UserRegisterRequest;
import com.ping.voicelinkguardianbackend.model.vo.LoginUserVO;
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
    private UserService userService;

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
        // 3. 调用业务层处理
        LoginUserVO loginUserVO = userService.userLogin(userAccount, userPassword, request);
        // 4. 返回成功响应
        return ResultUtils.success(loginUserVO);
    }
}
