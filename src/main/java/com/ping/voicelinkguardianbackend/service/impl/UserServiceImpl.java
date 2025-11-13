package com.ping.voicelinkguardianbackend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ping.voicelinkguardianbackend.model.entity.User;
import com.ping.voicelinkguardianbackend.service.UserService;
import com.ping.voicelinkguardianbackend.mapper.UserMapper;
import org.springframework.stereotype.Service;

/**
* @author 21877
* @description 针对表【user(用户)】的数据库操作Service实现
* @createDate 2025-11-13 16:30:36
*/
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService{

}




