package com.ping.voicelinkguardianbackend.service.impl;
import java.util.*;
import java.util.stream.Collectors;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ping.voicelinkguardianbackend.mapper.UserProgressMapper;
import com.ping.voicelinkguardianbackend.model.entity.UserProgress;
import com.ping.voicelinkguardianbackend.model.vo.UserProgressVO;
import com.ping.voicelinkguardianbackend.service.UserProgressService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
* @author 21877
* @description 针对表【user_progress(用户进度)】的数据库操作Service实现
* @createDate 2025-11-13 16:33:55
*/
@Service
public class UserProgressServiceImpl extends ServiceImpl<UserProgressMapper, UserProgress>
    implements UserProgressService {

    @Resource
    private UserProgressMapper userProgressMapper;

    /**
     * 获取关卡进度
     *
     * @param userId
     * @return
     */
    @Override
    public UserProgress getOrCreateProgress(Long userId, String groupName) {
        // 1.去数据库查当前id的进度
        QueryWrapper<UserProgress> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userId", userId);
        UserProgress userProgress = userProgressMapper.selectOne(queryWrapper);

        // 2.若没有进度则创建默认进度
        if (userProgress == null) {
            userProgress = new UserProgress(); // 必须先创建对象！
            userProgress.setUserId(userId);
            userProgress.setGroupName(groupName);
            userProgress.setCurrentLevel(1);
            userProgress.setTotalLevels(6);
            userProgress.setCreateTime(new Date());
            userProgress.setUpdateTime(new Date());
            userProgress.setIsDelete(0);
            userProgressMapper.insert(userProgress);
        }
        return userProgress;
    }

    /**
     * 通关当前关卡
     *
     * @param userId
     * @return
     */
    @Override
    public boolean passCurrentLevel(Long userId, String groupName) {
        UserProgress progress = getOrCreateProgress(userId,  groupName);
        if (progress.getCurrentLevel() < progress.getTotalLevels()) {
            progress.setCurrentLevel(progress.getCurrentLevel() + 1);
            userProgressMapper.updateById(progress);
            return true;
        }
        return false;
    }

    /**
     * 获取所有小组进度
     *
     * @return
     */
    @Override
    public List<Map<String, Object>> getAllGroupProgress() {
        return null;
    }

}





