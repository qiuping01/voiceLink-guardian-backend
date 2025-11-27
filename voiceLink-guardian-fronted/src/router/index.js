import { createRouter, createWebHistory } from 'vue-router'
import GameView from '../views/GameView.vue'
import LoginView from '../views/LoginView.vue'
import ProgressView from '../views/ProgressView.vue'

const routes = [
  {
    path: '/',
    name: 'login',
    component: LoginView
  },
  {
    path: '/game',
    name: 'game',
    component: GameView,
    meta: {
      requiresAuth: true // 需要登录才能访问
    }
  },
  {
    path: '/progress',
    name: 'progress',
    component: ProgressView,
    meta: {
      requiresAuth: true // 需要登录才能访问
    }
  },
  {
    path: '/register',
    name: 'register',
    component: () => import('../views/RegisterView.vue')
  }
]

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes
})

// 路由守卫 - 检查用户是否已登录
router.beforeEach((to, from, next) => {
  // 检查路由是否需要身份验证
  if (to.matched.some(record => record.meta.requiresAuth)) {
    // 检查是否有登录信息
    const userInfo = sessionStorage.getItem('gameUserInfo')
    if (!userInfo) {
      // 未登录，重定向到登录页面
      next({
        name: 'login',
        query: { redirect: to.fullPath }
      })
    } else {
      // 已登录，继续访问
      next()
    }
  } else {
    // 不需要身份验证的路由直接通过
    next()
  }
})

export default router