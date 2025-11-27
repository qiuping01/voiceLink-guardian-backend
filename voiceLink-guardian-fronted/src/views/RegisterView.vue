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
      
      <form @submit.prevent="handleRegister">
        <div class="form-group">
          <label for="groupName">用户名</label>
          <select v-model="registerForm.groupName" id="groupName" required>
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
            v-model="registerForm.password" 
            placeholder="请输入密码" 
            required
          >
        </div>
        
        <div class="form-group">
          <label for="checkPassword">确认密码</label>
          <input 
            type="password" 
            id="checkPassword" 
            v-model="registerForm.checkPassword" 
            placeholder="请再次输入密码" 
            required
          >
          <div v-if="registerForm.password && registerForm.checkPassword && registerForm.password !== registerForm.checkPassword" class="error-message">
            两次输入的密码不一致
          </div>
        </div>
        
        <button type="submit" class="btn btn-primary" :disabled="isLoading">
          {{ isLoading ? '注册中...' : '注册' }}
        </button>
        
        <div v-if="errorMessage" class="error-message">
          {{ errorMessage }}
        </div>
        
        <div v-if="successMessage" class="success-message">
          {{ successMessage }}
        </div>
        
        <div class="link">
          <span>已有账号？</span>
          <router-link to="/">立即登录</router-link>
        </div>
      </form>
    </div>
  </div>
</template>

<script>
import { ref } from 'vue'
import router from '../router'
import { userRegister } from '../api/user'

export default {
  name: 'RegisterView',
  setup() {
    const registerForm = ref({
      groupName: '',
      password: '',
      checkPassword: ''
    })
    
    const isLoading = ref(false)
    const errorMessage = ref('')
    const successMessage = ref('')
    
    const handleRegister = async () => {
      errorMessage.value = ''
      successMessage.value = ''
      
      // 表单验证
      if (!registerForm.value.groupName) {
        errorMessage.value = '请选择用户名'
        return
      }
      
      if (!registerForm.value.password) {
        errorMessage.value = '请输入密码'
        return
      }
      
      if (registerForm.value.password.length < 6) {
        errorMessage.value = '密码长度至少为6位'
        return
      }
      
      if (registerForm.value.password !== registerForm.value.checkPassword) {
        errorMessage.value = '两次输入的密码不一致'
        return
      }
      
      try {
        isLoading.value = true
        const response = await userRegister(registerForm.value)
        
        if (response && (response.code === 0 || response.code === 200 || !response.code)) {
          successMessage.value = '注册成功！'
          // 注册成功后自动跳转到登录页
          setTimeout(() => {
            router.push('/')
          }, 2000)
        } else {
          // 处理后端返回的错误消息，包括账号重复
          errorMessage.value = response.message || '注册失败'
        }
      } catch (error) {
        // 优化错误处理，尝试从error对象中提取更具体的错误信息
        if (error.response && error.response.data && error.response.data.message) {
          errorMessage.value = error.response.data.message
        } else {
          errorMessage.value = '注册失败，请稍后重试'
        }
        console.error('注册错误:', error)
      } finally {
        isLoading.value = false
      }
    }
    
    return {
      registerForm,
      isLoading,
      errorMessage,
      successMessage,
      handleRegister
    }
  }
}
</script>