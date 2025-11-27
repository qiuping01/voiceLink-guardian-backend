package com.ping.voicelinkguardianbackend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ping.voicelinkguardianbackend.model.entity.UserProgress;

import java.util.List;
import java.util.Map;

/**
* @author 21877
* @description 针对表【user_progress(用户进度)】的数据库操作Service
* @createDate 2025-11-13 16:33:55
*/
public interface UserProgressService extends IService<UserProgress> {

    /**
     * 获取关卡进度
     *
     * @param userId
     * @return
     */
    UserProgress getOrCreateProgress(Long userId, String groupName);

    /**
     * 通关当前关卡
     *
     * @param userId
     * @return
     */
    boolean passCurrentLevel(Long userId, String groupName);

    /**
     * 回退当前关卡
     *
     * @param userId
     * @return
     */
    boolean backCurrentLevel(Long userId, String groupName);

    /**
     * 获取所有小组进度
     *
     * @return
     */
    List<Map<String,Object>> getAllGroupProgress();

    /**
     * 用户登出时设置当前关卡进度为 1
     *
     * @param userId
     * @param groupName
     * @return
     */
    boolean setCurrentLevelForOne(Long userId, String groupName);

}
