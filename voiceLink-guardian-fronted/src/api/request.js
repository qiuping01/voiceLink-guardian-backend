import axios from 'axios'

// 创建axios实例
const service = axios.create({
  baseURL: '/api', // 基础URL，会被vite代理转发到http://localhost:8848/api
  timeout: 10000, // 请求超时时间
  headers: {
    'Content-Type': 'application/json'
  }
})

// 请求拦截器
service.interceptors.request.use(
  config => {
    // 使用session/cookie验证，不需要手动添加token
    // 浏览器会自动处理session cookie
    
    // 添加详细的请求日志
    console.log('=== 请求详情 ===')
    console.log('URL:', config.baseURL + config.url)
    console.log('Method:', config.method)
    console.log('Headers:', JSON.stringify(config.headers, null, 2))
    console.log('Data:', JSON.stringify(config.data, null, 2))
    console.log('==================')
    
    return config
  },
  error => {
    console.error('请求错误:', error)
    return Promise.reject(error)
  }
)

// 响应拦截器
service.interceptors.response.use(
  response => {
    const res = response.data
    
    // 添加详细的响应日志
    console.log('=== 响应详情 ===')
    console.log('Status:', response.status)
    console.log('Status Text:', response.statusText)
    console.log('Headers:', JSON.stringify(response.headers, null, 2))
    console.log('Response Data:', JSON.stringify(res, null, 2))
    console.log('==================')
    
    console.log('API响应:', res)
    
    // 这里可以根据后端返回的数据格式进行统一处理
    // 放宽条件，允许code为0、200或undefined（有些后端可能不返回code）
    if (res.code && res.code !== 0 && res.code !== 200) {
      // 非成功状态码，保留原始响应数据，让前端组件可以获取完整的错误信息
      console.error('响应错误:', res.message)
      const error = new Error(res.message || 'Error')
      error.response = { data: res }
      return Promise.reject(error)
    }
    
    // 使用session/cookie验证，不需要手动保存token
    // 浏览器会自动处理session cookie
    
    return res
  },
  error => {
    console.error('=== 响应错误详情 ===')
    console.error('Error Message:', error.message)
    if (error.response) {
      console.error('Error Status:', error.response.status)
      console.error('Error Status Text:', error.response.statusText)
      console.error('Error Headers:', JSON.stringify(error.response.headers, null, 2))
      console.error('Error Data:', JSON.stringify(error.response.data, null, 2))
    } else if (error.request) {
      console.error('Error Request:', error.request)
    } else {
      console.error('Error Config:', error.config)
    }
    console.error('====================')
    
    console.error('响应错误:', error)
    // 可以在这里处理网络错误、超时等情况
    return Promise.reject(error)
  }
)

export default service