import request from './request'

/**
 * 保存第二关答案
 * @param {Object} secondLevelRequest - 第二关答案数据
 * @param {string} secondLevelRequest.secondDesign1 - 老人在这种紧急情况下，最可能喊出的语音指令（指令1）
 * @param {string} secondLevelRequest.secondDesign2 - 老人在这种紧急情况下，最可能喊出的语音指令（指令2）
 * @param {string} secondLevelRequest.secondDesign3 - 行空板收到求救信息后，发出什么语音安抚老人（播放语音）
 * @param {string} secondLevelRequest.secondDesign4 - 发送什么信息给家人或云平台（发送内容）
 * @param {string} secondLevelRequest.secondRisk - 在该场景下，老人最可能遇到的风险
 * @param {string} secondLevelRequest.secondScene - 小组重点关注的场景
 * @returns {Promise}
 */
export const writeLevel2 = (secondLevelRequest) => {
  return request({
    url: '/user/level2',
    method: 'POST',
    data: secondLevelRequest
  })
}

/**
 * 保存第三关答案
 * @param {Object} thirdLevelRequest - 第三关答案数据
 * @param {string} thirdLevelRequest.thirdChallenge - 选择的挑战任务
 * @returns {Promise}
 */
export const writeLevel3 = (thirdLevelRequest) => {
  return request({
    url: '/user/level3',
    method: 'POST',
    data: thirdLevelRequest
  })
}

/**
 * 用户登录
 * @param {Object} loginData - 登录数据
 * @param {string} loginData.groupName - 用户名
 * @param {string} loginData.password - 密码
 * @returns {Promise}
 */
export const userLogin = (loginData) => {
  return request({
    url: '/user/login',
    method: 'POST',
    data: loginData
  })
}

/**
 * 用户注册
 * @param {Object} registerData - 注册数据
 * @param {string} registerData.groupName - 用户名
 * @param {string} registerData.password - 密码
 * @param {string} registerData.checkPassword - 确认密码
 * @returns {Promise}
 */
export const userRegister = (registerData) => {
  return request({
    url: '/user/register',
    method: 'POST',
    data: registerData
  })
}

/**
 * 获取当前登录用户
 * @returns {Promise}
 */
export const getLoginUser = () => {
  return request({
    url: '/user/get/login',
    method: 'GET'
  })
}

/**
 * 通关接口
 * @param {number} level - 关卡编号
 * @returns {Promise}
 */
export const passLevel = (level) => {
  return request({
    url: '/user/pass-level',
    method: 'POST',
    data: { level }
  })
}

/**
 * 获取所有用户进度（教师专用）
 * @returns {Promise}
 */
export const getAllUserProgress = () => {
  return request({
    url: '/teacher/all-progress',
    method: 'GET'
  })
}

/**
 * 用户退出登录
 * @returns {Promise}
 */
export const userLogout = () => {
  return request({
    url: '/user/logout',
    method: 'POST'
  })
}

/**
 * 上一关接口
 * @returns {Promise}
 */
export const backLevel = () => {
  return request({
    url: '/user/back-level',
    method: 'POST'
  })
}

/**
 * 重置所有小组进度（教师专用）
 * @returns {Promise}
 */
export const deleteAllUserProgress = () => {
  return request({
    url: '/teacher/delete-all-progress',
    method: 'POST'
  })
}

/**
 * 提交第一关答案
 * @param {Object} firstLevelRequest - 第一关请求参数
 * @param {string} firstLevelRequest.firstDilemma - 选择题答案
 * @returns {Promise}
 */
export const writeLevel1 = (firstLevelRequest) => {
  return request({
    url: '/user/level1',
    method: 'POST',
    data: firstLevelRequest
  })
}

/**
 * 获取所有用户设计蓝图数据（教师专用）
 * @returns {Promise}
 */
export const getAllUserDesign = () => {
  return request({
    url: '/teacher/all-design',
    method: 'GET'
  })
}

/**
 * 获取所有用户答案数据（教师专用）
 * @returns {Promise}
 */
export const getAllUserAnswer = () => {
  return request({
    url: '/teacher/all-answer',
    method: 'GET'
  })
}

/**
 * 保存设计蓝图数据
 * @param {Object} designData - 设计数据
 * @param {string} designData.coreScene - 核心场景
 * @param {string} designData.presetVoiceCommand - 预设语音指令
 * @param {string} designData.systemResponseLogic - 系统响应逻辑
 * @returns {Promise}
 */
export const saveDesign = (designData) => {
  return request({
    url: '/user/design',
    method: 'POST',
    data: designData
  })
}

/**
 * 保存第四关答案
 * @param {Object} fourthLevelRequest - 第四关答案数据
 * @param {string} fourthLevelRequest.fourthAchieve - 实现功能
 * @param {string} fourthLevelRequest.fourthExtra - 额外添加的功能
 * @param {string} fourthLevelRequest.fourthProblem - 遇到的难题
 * @param {string} fourthLevelRequest.fourthScene - 选择的场景
 * @param {string} fourthLevelRequest.fourthSolution - 解决方法
 * @returns {Promise}
 */
export const writeLevel4 = (fourthLevelRequest) => {
  return request({
    url: '/user/level4',
    method: 'POST',
    data: fourthLevelRequest
  })
}

/**
 * 保存第五关答案
 * @param {Object} fifthLevelRequest - 第五关答案数据
 * @param {string} fifthLevelRequest.fifthComment1 - 问题1答案
 * @param {string} fifthLevelRequest.fifthComment2 - 问题2答案
 * @param {string} fifthLevelRequest.fifthComment3 - 问题3答案
 * @param {string} fifthLevelRequest.fifthComment4 - 问题4答案
 * @param {string} fifthLevelRequest.fifthComment5 - 问题5答案
 * @param {string} fifthLevelRequest.fifthComment6 - 问题6答案
 * @returns {Promise}
 */
export const writeLevel5 = (fifthLevelRequest) => {
  return request({
    url: '/user/level5',
    method: 'POST',
    data: fifthLevelRequest
  })
}

/**
 * 保存第六关答案
 * @param {Object} sixthLevelRequest - 第六关答案数据
 * @param {string} sixthLevelRequest.sixthEduComment1 - 教学内容设计
 * @param {string} sixthLevelRequest.sixthEduComment2 - 教学方法与指导
 * @param {string} sixthLevelRequest.sixthEduComment3 - 实践环节安排
 * @param {string} sixthLevelRequest.sixthFeedback1 - 教学优点
 * @param {string} sixthLevelRequest.sixthFeedback2 - 教学改进建议
 * @returns {Promise}
 */
export const writeLevel6 = (sixthLevelRequest) => {
  return request({
    url: '/user/level6',
    method: 'POST',
    data: sixthLevelRequest
  })
}