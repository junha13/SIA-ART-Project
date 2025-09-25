import { createRouter, createWebHistory } from 'vue-router'

// import 형식으로 하는게 더 빠르게 들어옴
import HomeView from '../views/HomeView.vue'
import LoginView from '../views/user/LoginView.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'home',
      component: HomeView,
    },
    {
      path: '/login',
      name: 'login',
      component: LoginView,
    },
  ],
})

export default router
