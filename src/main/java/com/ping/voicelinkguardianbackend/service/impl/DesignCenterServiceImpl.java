package com.ping.voicelinkguardianbackend.service.impl;

import java.util.Date;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ping.voicelinkguardianbackend.model.entity.DesignCenter;
import com.ping.voicelinkguardianbackend.mapper.DesignCenterMapper;
import com.ping.voicelinkguardianbackend.model.entity.UserProgress;
import com.ping.voicelinkguardianbackend.model.vo.DesignCenterVO;
import com.ping.voicelinkguardianbackend.service.DesignCenterService;
import com.ping.voicelinkguardianbackend.service.UserProgressService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author 21877
 * @description 针对表【design_center(蓝图设计中心)】的数据库操作Service实现
 * @createDate 2025-11-19 09:56:21
 */
@Service
public class DesignCenterServiceImpl extends ServiceImpl<DesignCenterMapper, DesignCenter>
        implements DesignCenterService {

    @Resource
    private UserProgressService userProgressService;

    @Resource
    private DesignCenterMapper designCenterMapper;

    /**
     * 根据设计中心获取设计中心VO
     *
     * @param designCenter 设计中心
     * @return 设计中心VO
     */
    @Override
    public DesignCenterVO getDesignCenterVO(DesignCenter designCenter) {
        if (designCenter == null) {
            return null;
        }
        DesignCenterVO designCenterVO = new DesignCenterVO();
        BeanUtils.copyProperties(designCenter, designCenterVO);
        return designCenterVO;
    }

    /**
     * 根据设计中心列表获取设计中心VO列表
     *
     * @param designCenterList 设计中心列表
     * @return 设计中心VO列表
     */
    @Override
    public List<DesignCenterVO> getDesignCenterVOList(List<DesignCenter> designCenterList) {
        if (CollUtil.isEmpty(designCenterList)) {
            return new ArrayList<>();
        }
        return designCenterList.stream()
                .map(this::getDesignCenterVO)
                .collect(Collectors.toList());
    }

    @Override
    public DesignCenter getOrCreateDesignCenter(Long userId, String groupName) {
        QueryWrapper<DesignCenter> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userId", userId);
        queryWrapper.eq("groupName", groupName);
        DesignCenter designCenter = designCenterMapper.selectOne(queryWrapper);
        if (designCenter == null) {
            designCenter = new DesignCenter();
            designCenter.setUserId(userId);
            designCenter.setGroupName(groupName);
            designCenter.setCoreScene("暂未填写");
            designCenter.setPresetVoiceCommand("暂未填写");
            designCenter.setSystemResponseLogic("暂未填写");
            designCenter.setCreateTime(new Date());
            designCenter.setUpdateTime(new Date());
            designCenter.setIsDelete(0);
            designCenterMapper.insert(designCenter);
        }
        return designCenter;
    }

    /**
     * 写入设计中心
     *
     * @param userId    用户ID
     * @param groupName 组名
     * @return 是否成功
     */
    @Override
    public boolean writeDesignCenter(Long userId, String groupName
            , String coreScene, String presetVoiceCommand, String systemResponseLogic) {
        DesignCenter designCenter = getOrCreateDesignCenter(userId, groupName);
        designCenter.setCoreScene(coreScene);
        designCenter.setPresetVoiceCommand(presetVoiceCommand);
        designCenter.setSystemResponseLogic(systemResponseLogic);
        designCenter.setUpdateTime(new Date());
        UserProgress progress =
                userProgressService.getOrCreateProgress(userId, groupName);
        if (progress.getCurrentLevel() == 2) {
            designCenterMapper.updateById(designCenter);
            return true;
        }
        return false;
    }
}




