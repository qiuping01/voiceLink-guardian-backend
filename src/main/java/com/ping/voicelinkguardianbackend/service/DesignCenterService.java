package com.ping.voicelinkguardianbackend.service;

import com.ping.voicelinkguardianbackend.model.entity.DesignCenter;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ping.voicelinkguardianbackend.model.vo.DesignCenterVO;

import java.util.List;

/**
 * @author 21877
 * @description 针对表【design_center(蓝图设计中心)】的数据库操作Service
 * @createDate 2025-11-19 09:56:21
 */
public interface DesignCenterService extends IService<DesignCenter> {

    /**
     * 根据设计中心获取设计中心VO
     *
     * @param designCenter 设计中心
     * @return 设计中心VO
     */
    DesignCenterVO getDesignCenterVO(DesignCenter designCenter);

    /**
     * 根据设计中心列表获取设计中心VO列表
     *
     * @param designCenterList 设计中心列表
     * @return 设计中心VO列表
     */
    List<DesignCenterVO> getDesignCenterVOList(List<DesignCenter> designCenterList);

    /**
     * 创建设计中心
     *
     * @param userId    用户ID
     * @param groupName 组名
     * @return 设计中心
     */
    DesignCenter getOrCreateDesignCenter(Long userId, String groupName);

    /**
     * 写入设计中心
     *
     * @param userId    用户ID
     * @param groupName 组名
     * @return 是否成功
     */
    boolean writeDesignCenter(Long userId, String groupName, String coreScene, String presetVoiceCommand, String systemResponseLogic);

}
