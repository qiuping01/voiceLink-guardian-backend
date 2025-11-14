package com.ping.voicelinkguardianbackend.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ping.voicelinkguardianbackend.exception.BusinessException;
import com.ping.voicelinkguardianbackend.exception.ErrorCode;
import com.ping.voicelinkguardianbackend.model.entity.User;
import com.ping.voicelinkguardianbackend.model.entity.UserProgress;
import com.ping.voicelinkguardianbackend.model.enums.UserRoleEnum;
import com.ping.voicelinkguardianbackend.model.vo.LoginUserVO;
import com.ping.voicelinkguardianbackend.model.vo.UserProgressVO;
import com.ping.voicelinkguardianbackend.service.UserService;
import com.ping.voicelinkguardianbackend.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.ping.voicelinkguardianbackend.constant.UserConstant.USER_LOGIN_STATE;

/**
* @author 21877
* @description 针对表【user(用户)】的数据库操作Service实现
* @createDate 2025-11-13 16:30:36
*/
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService{

    /**
     * 用户注册
     *
     * @param groupName   用户账户
     * @param password  用户密码
     * @param checkPassword 校验密码
     * @return 用户ID
     */
    @Override
    public long userRegister(String groupName, String password, String checkPassword) {
// 1. 校验参数
        if (StrUtil.hasBlank(groupName, password, checkPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "参数为空");
        }

        if (password.length() < 8 || checkPassword.length() < 8) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户密码过短");
        }
        if (!password.equals(checkPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "两次输入的密码不一致");
        }
        // 2. 检查用户账户是否和数据库中已有的重复
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("groupName", groupName);
        long count = this.baseMapper.selectCount(queryWrapper);
        if (count > 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "账号重复");
        }
        // 3. 插入数据到数据库中
        User user = new User();
        user.setGroupName(groupName);
        user.setPassword(password);
        user.setUserRole(UserRoleEnum.USER.getValue());
        boolean saveResult = this.save(user);
        if (!saveResult) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "注册失败，数据库错误");
        }
        return user.getId();
    }

    /**
     * 用户登录
     *
     * @param groupName   用户账户
     * @param password  用户密码
     * @param request      Http请求
     * @return 脱敏后的用户信息
     */
    @Override
    public LoginUserVO userLogin(String groupName, String password, HttpServletRequest request) {
        // 1. 校验
        if (StrUtil.hasBlank(groupName, password)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "参数为空");
        }
        if (password.length() < 8) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "密码错误");
        }
        // 2. 查询数据库中的用户是否存在
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("groupName", groupName);
        queryWrapper.eq("password", password);
        User user = this.baseMapper.selectOne(queryWrapper);
        // 不存在，抛异常
        if (user == null) {
            // 使用英文存储空间更小
            log.info("user login failed, userAccount cannot match userPassword");
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户不存在或密码错误");
        }
        // 4. 保存用户的登录态
        request.getSession().setAttribute(USER_LOGIN_STATE, user);
        return this.getLoginUserVO(user);
    }

    /**
     * 获取脱敏类的用户信息
     *
     * @param user 用户
     * @return 脱敏后的用户信息
     */
    @Override
    public LoginUserVO getLoginUserVO(User user) {
        if (user == null) {
            return null; // null 不转换，节省资源
        }
        LoginUserVO loginUserVO = new LoginUserVO();
        BeanUtils.copyProperties(user, loginUserVO);
        return loginUserVO;
    }

    /**
     * 获取当前的登录用户
     *
     * @param request HTTP请求对象
     * @return 登录用户
     */
    @Override
    public User getLoginUser(HttpServletRequest request) {
        // 1. 先判断是否已经登录
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        User currentUser = (User) userObj;
        if (currentUser == null || currentUser.getId() == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR);
        }
        // 2. 从数据库查询（获取最新结果，追求的话性能上一步即可返回）
        Long userId = currentUser.getId();
        currentUser = this.getById(userId); // 数据库中的最新用户信息
        if (currentUser == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR);
        }
        return currentUser;
    }

    /**
     * 获得脱敏后的用户进度信息
     *
     * @param progress 用户进度
     * @return 脱敏后的用户
     */
    @Override
    public UserProgressVO getUserProgressVO(UserProgress progress) {
        if (progress == null) {
            return null;
        }
        UserProgressVO userProgressVO = new UserProgressVO();
        BeanUtils.copyProperties(progress, userProgressVO);
        return userProgressVO;
    }

    /**
     * 获得脱敏后的用户信息列表
     *
     * @param userProgressList 用户进度信息列表
     * @return 脱敏后的用户进度信息列表
     */
    @Override
    public List<UserProgressVO> getUserProgressVOList(List<UserProgress> userProgressList) {
        if (CollUtil.isEmpty(userProgressList)) {
            return new ArrayList<>();
        }
        return userProgressList.stream()
                .map(this::getUserProgressVO)
                .collect(Collectors.toList());
    }

    @Override
    public boolean userLogout(HttpServletRequest request) {
        // 1. 先判断是否已登录
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        if (userObj == null) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "未登录");
        }
        // 2. 移除登录态
        request.getSession().removeAttribute(USER_LOGIN_STATE);
        return true;
    }
}




