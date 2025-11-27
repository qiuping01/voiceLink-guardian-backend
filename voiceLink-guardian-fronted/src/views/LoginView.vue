<template>
  <div class="container">
    <div class="form-container">
      <div class="logo-container">
        <h1>声联守护·终极挑战</h1>
        <h2>AIoT守护者训练营 - 为空巢老人构建紧急呼叫系统</h2>
        <div class="teacher-info">
          指挥官: 刘燚娜老师
        </div>
      </div>
      
      <form @submit.prevent="handleLogin">
        <div class="form-group">
          <label for="groupName">用户名</label>
          <select v-model="loginForm.groupName" id="groupName" required>
            <option value="">请选择用户名</option>
            <option value="teacher">教师</option>
            <option value="demo">演示用户</option>
            <option v-for="n in 10" :key="n" :value="`第${['一', '二', '三', '四', '五', '六', '七', '八', '九', '十'][n-1]}组`">第{{ ['一', '二', '三', '四', '五', '六', '七', '八', '九', '十'][n-1] }}组</option>
          </select>
        </div>
        
        <div class="form-group">
          <label for="password">密码</label>
          <input 
            type="password" 
            id="password" 
            v-model="loginForm.password" 
            placeholder="请输入密码" 
            required
          >
        </div>
        
        <button type="submit" class="btn btn-primary" :disabled="isLoading">
          {{ isLoading ? '登录中...' : '登录' }}
        </button>
        
        <div v-if="errorMessage" class="error-message">
          {{ errorMessage }}
        </div>
        
        <div class="link">
          <span>还没有账号？</span>
          <router-link to="/register">立即注册</router-link>
        </div>
      </form>
    </div>
  </div>
</template>

<script>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { userLogin } from '../api/user'

export default {
  name: 'LoginView',
  setup() {
    const router = useRouter()
    const loginForm = ref({
      groupName: '',
      password: ''
    })
    
    const isLoading = ref(false)
    const errorMessage = ref('')
    
    const handleLogin = async () => {
      errorMessage.value = ''
      
      // 简单的表单验证
      if (!loginForm.value.groupName) {
        errorMessage.value = '请选择用户名'
        return
      }
      
      if (!loginForm.value.password) {
        errorMessage.value = '请输入密码'
        return
      }
      
      try {
        isLoading.value = true
        console.log('开始登录，发送数据:', loginForm.value)
        
        const response = await userLogin(loginForm.value)
        console.log('登录响应:', response)
        
        if (response && (response.code === 0 || response.code === 200 || !response.code)) {
          // 登录成功，保存用户信息（使用session/cookie验证，不需要保存token）
          const userData = response.data || response
          
          // 确保用户数据包含必要的字段
          const userInfo = {
            groupName: userData.groupName || userData.userType || userData.role || userData.userGroup || '未知用户',
            // 不需要保存token，使用session/cookie验证
          }
          
          // 保存用户信息到sessionStorage（用于路由守卫验证和显示）
          sessionStorage.setItem('gameUserInfo', JSON.stringify(userInfo))
          // 同时保存到localStorage作为备份
          localStorage.setItem('userInfo', JSON.stringify(userInfo))
          
          // 判断是否为教师，如果是则跳转到进度页面，否则跳转到闯关页面
          if (userInfo.groupName === 'teacher') {
            await router.push('/progress')
          } else {
            await router.push('/game')
          }
        } else {
          // 显示后端返回的错误消息
          console.error('登录失败，响应:', response)
          errorMessage.value = (response && response.message) || '登录失败'
        }
      } catch (error) {
        console.error('登录异常:', error)
        // 优化错误处理，尝试从error对象中提取更具体的错误信息
        if (error.response && error.response.data && error.response.data.message) {
          errorMessage.value = error.response.data.message
        } else {
          errorMessage.value = '登录失败，请稍后重试'
        }
        console.error('登录错误:', error)
      } finally {
        isLoading.value = false
      }
    }
    
    return {
      loginForm,
      isLoading,
      errorMessage,
      handleLogin
    }
  }
}
</script>

<style scoped>
.container {
  min-height: 100vh;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  display: flex;
  justify-content: center;
  align-items: center;
  padding: 20px;
  font-family: 'Arial', sans-serif;
}

.form-container {
  background: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(10px);
  border-radius: 20px;
  padding: 40px;
  box-shadow: 0 20px 40px rgba(0, 0, 0, 0.1);
  max-width: 450px;
  width: 100%;
}

.logo-container {
  text-align: center;
  margin-bottom: 30px;
}

.logo-container h1 {
  color: #333;
  font-size: 2rem;
  margin-bottom: 10px;
  font-weight: bold;
  background: linear-gradient(135deg, #667eea, #764ba2);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
}

.logo-container h2 {
  color: #666;
  font-size: 1rem;
  margin-bottom: 15px;
  line-height: 1.4;
}

.teacher-info {
  color: #888;
  font-size: 0.9rem;
  font-style: italic;
}

.form-group {
  margin-bottom: 20px;
}

.form-group label {
  display: block;
  margin-bottom: 8px;
  color: #333;
  font-weight: 500;
}

.form-group input,
.form-group select {
  width: 100%;
  padding: 12px 16px;
  border: 2px solid #e1e5e9;
  border-radius: 10px;
  font-size: 1rem;
  transition: all 0.3s ease;
  background: white;
}

.form-group input:focus,
.form-group select:focus {
  outline: none;
  border-color: #667eea;
  box-shadow: 0 0 0 3px rgba(102, 126, 234, 0.1);
}

.btn {
  width: 100%;
  padding: 14px 20px;
  border: none;
  border-radius: 10px;
  font-size: 1.1rem;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s ease;
  margin-bottom: 20px;
}

.btn-primary {
  background: linear-gradient(135deg, #667eea, #764ba2);
  color: white;
}

.btn-primary:hover:not(:disabled) {
  transform: translateY(-2px);
  box-shadow: 0 10px 20px rgba(102, 126, 234, 0.3);
}

.btn:disabled {
  opacity: 0.6;
  cursor: not-allowed;
  transform: none;
}

.error-message {
  background: #fee;
  color: #c33;
  padding: 12px;
  border-radius: 8px;
  margin-bottom: 20px;
  border: 1px solid #fcc;
  font-size: 0.9rem;
}

.link {
  text-align: center;
  color: #666;
}

.link a {
  color: #667eea;
  text-decoration: none;
  font-weight: 500;
}

.link a:hover {
  text-decoration: underline;
}

@media (max-width: 480px) {
  .container {
    padding: 10px;
  }
  
  .form-container {
    padding: 30px 20px;
  }
  
  .logo-container h1 {
    font-size: 1.8rem;
  }
  
  .logo-container h2 {
    font-size: 0.9rem;
  }
}
</style>