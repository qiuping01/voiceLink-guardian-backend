package com.ping.voicelinkguardianbackend.service;

import com.ping.voicelinkguardianbackend.model.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ping.voicelinkguardianbackend.model.entity.UserProgress;
import com.ping.voicelinkguardianbackend.model.vo.LoginUserVO;
import com.ping.voicelinkguardianbackend.model.vo.UserProgressVO;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author 21877
 * @description 针对表【user(用户)】的数据库操作Service
 * @createDate 2025-11-13 16:30:37
 */
public interface UserService extends IService<User> {

    /**
     * 用户注册
     *
     * @param groupName     用户账户
     * @param password      用户密码
     * @param checkPassword 校验密码
     * @return 用户ID
     */
    long userRegister(String groupName, String password,
                      String checkPassword);

    /**
     * 用户登录
     *
     * @param groupName 用户账户
     * @param password  用户密码
     * @param request   Http请求
     * @return 脱敏后的用户信息
     */
    LoginUserVO userLogin(String groupName, String password,
                          HttpServletRequest request);

    /**
     * 获取脱敏类的用户信息
     *
     * @param user 用户
     * @return 脱敏后的用户信息
     */
    LoginUserVO getLoginUserVO(User user);

    /**
     * 获取当前的登录用户
     *
     * @param request HTTP请求对象
     * @return 登录用户
     */
    User getLoginUser(HttpServletRequest request);

    /**
     * 获得脱敏后的用户进度信息
     *
     * @param progress 用户进度
     * @return 脱敏后的用户
     */
    UserProgressVO getUserProgressVO(UserProgress progress);

    /**
     * 获得脱敏后的用户进度信息列表
     *
     * @param userProgressList 用户列表
     * @return 脱敏后的用户进度信息列表
     */
    List<UserProgressVO> getUserProgressVOList(List<UserProgress> userProgressList);

    boolean userLogout(HttpServletRequest request);
}
