package com.ping.voicelinkguardianbackend.cotroller;

import com.ping.voicelinkguardianbackend.common.BaseResponse;
import com.ping.voicelinkguardianbackend.common.ResultUtils;
import com.ping.voicelinkguardianbackend.model.entity.UserProgress;
import com.ping.voicelinkguardianbackend.model.vo.UserProgressVO;
import com.ping.voicelinkguardianbackend.service.UserProgressService;
import com.ping.voicelinkguardianbackend.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
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

    @GetMapping("/all-progress")
    public BaseResponse<List<UserProgressVO>> getAllUserProgress() {
        // 1. 获取所有用户进度列表
        List<UserProgress> userProgressList = userProgressService.list();

        // 2. 转换为脱敏的VO列表
        List<UserProgressVO> progressVOList = userService.getUserProgressVOList(userProgressList);

        return ResultUtils.success(progressVOList);
    }
}
