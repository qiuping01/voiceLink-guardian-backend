import { ref } from 'vue'
import { getLoginUser, userLogout } from '../api/user'

// 全局用户状态
const userInfo = ref(null)
const isLoading = ref(false)

/**
 * 获取会话存储的用户信息
 */
export const getSessionUserInfo = () => {
  try {
    const stored = sessionStorage.getItem('userInfo')
    return stored ? JSON.parse(stored) : null
  } catch (error) {
    console.error('解析用户信息失败:', error)
    return null
  }
}

/**
 * 保存用户信息到会话存储
 */
export const saveUserInfo = (info) => {
  try {
    // 只保存必要的非敏感信息
    const safeUserInfo = {
      groupName: info.groupName || '',
      userId: info.userId || ''
    }
    sessionStorage.setItem('userInfo', JSON.stringify(safeUserInfo))
    userInfo.value = safeUserInfo
  } catch (error) {
    console.error('保存用户信息失败:', error)
  }
}

/**
 * 清除用户信息
 */
export const clearUserInfo = () => {
  sessionStorage.removeItem('userInfo')
  sessionStorage.removeItem('token')
  userInfo.value = null
}

/**
 * 从服务器获取当前登录用户信息
 */
export const fetchUserInfo = async () => {
  if (isLoading.value) {
    return userInfo.value
  }

  try {
    isLoading.value = true
    const response = await getLoginUser()
    
    if (response.code === 0 && response.data) {
      saveUserInfo(response.data)
      return response.data
    } else {
      // 如果获取失败，清除会话信息
      clearUserInfo()
      return null
    }
  } catch (error) {
    console.error('获取用户信息失败:', error)
    // 如果是网络错误或未授权，清除会话信息
    if (error.response?.status === 401 || error.response?.status === 403) {
      clearUserInfo()
    }
    return null
  } finally {
    isLoading.value = false
  }
}

/**
 * 确保用户信息存在，如果不存在则尝试从服务器获取
 */
export const ensureUserInfo = async () => {
  // 先检查会话存储
  let info = getSessionUserInfo()
  
  if (info) {
    userInfo.value = info
    return info
  }
  
  // 如果会话中没有，尝试从服务器获取
  return await fetchUserInfo()
}

/**
 * 获取当前用户信息（响应式）
 */
export const getUserInfo = () => userInfo.value

/**
 * 检查用户是否已登录
 */
export const isLoggedIn = () => {
  return !!getSessionUserInfo() || !!userInfo.value
}

/**
 * 检查用户是否为教师
 */
export const isTeacher = () => {
  const info = getSessionUserInfo() || userInfo.value
  return info?.groupName === 'teacher'
}

/**
 * 获取用户名
 */
export const getUsername = () => {
  const info = getSessionUserInfo() || userInfo.value
  return info?.groupName || '未知用户'
}

/**
 * 用户退出登录，调用后端接口并清除本地信息
 */
export const logout = async () => {
  try {
    // 调用后端退出登录接口
    const response = await userLogout()
    
    // 无论后端接口返回成功还是失败，都清除本地信息
    clearUserInfo()
    
    // 返回接口响应结果
    return response
  } catch (error) {
    console.error('调用退出登录接口失败:', error)
    // 即使接口调用失败，也要清除本地信息
    clearUserInfo()
    throw error
  }
}

export default {
  userInfo,
  isLoading,
  getSessionUserInfo,
  saveUserInfo,
  clearUserInfo,
  fetchUserInfo,
  ensureUserInfo,
  getUserInfo,
  isLoggedIn,
  isTeacher,
  getUsername,
  logout
}