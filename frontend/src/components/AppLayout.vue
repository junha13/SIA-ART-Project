<template>
  <div id="kt_app_root" class="d-flex flex-column flex-root app-root">

    <header
        v-if="appStore.getShowLayout"
        class="fixed-top bg-white border-bottom shadow-sm d-flex align-items-center px-4 py-3"
        style="height: 55px; z-index: 999;"
    >
      <h1 class="fs-4 fw-bolder mb-0 text-dark me-auto cursor-pointer" @click="router.push('/')">
        예술을 찾는 사람들
      </h1>
      <button class="btn btn-icon btn-light" @click="isDrawerOpen = true">
        <i class="fas fa-bars fs-4"></i>
      </button>
    </header>

    <main id="kt_app_content" class="flex-column-fluid app-content" :style="contentPaddingStyle">
      <slot />
    </main>

    <div
        v-if="appStore.getShowLayout"
        class="fixed-bottom bg-white border-top shadow-lg d-flex justify-content-around p-2"
        style="height: 60px; z-index: 1000;"
    >
      <button
          v-for="item in navItems"
          :key="item.path"
          :class="['btn btn-lg fw-bold d-flex flex-column align-items-center p-0',
                 currentRoute === item.path ? 'text-primary' : 'text-muted']"
          @click="router.push(item.path)"
          style="font-size: 0.75rem; line-height: 1;"
      >
        <i :class="[item.iconClass, 'fs-5']"></i>
        <span>{{ item.label }}</span>
      </button>
    </div>

    <div v-if="isDrawerOpen" class="drawer-backdrop" @click="isDrawerOpen = false"></div>
    <div :class="['drawer', { 'open': isDrawerOpen }]">
      <div class="p-4 d-flex flex-column h-100">
        <div class="d-flex justify-content-between align-items-center mb-5">
          <h5 class="fw-bold mb-0">메뉴</h5>
          <button class="btn btn-icon btn-light" @click="isDrawerOpen = false">
            <i class="fas fa-times"></i>
          </button>
        </div>

        <div v-if="authStore.isLoggedIn" class="d-flex align-items-center mb-5 p-3 bg-light rounded-3">
          <img :src="authStore.user.profileImage" alt="user avatar" class="rounded-circle me-3" width="50" height="50" />
          <div>
            <h6 class="fw-bold mb-0">{{ authStore.user.name }}</h6>
            <p class="text-muted small mb-0">{{ authStore.user.role }}</p>
          </div>
        </div>

        <ul class="list-group list-group-flush flex-grow-1">
          <li class="list-group-item cursor-pointer" @click="goTo('/mypage')">
            <i class="fas fa-user-circle me-3"></i> 마이페이지
          </li>
          <li class="list-group-item cursor-pointer" @click="goTo('/artworks')">
            <i class="fas fa-palette me-3"></i> 전체 작품 목록
          </li>
          <li class="list-group-item cursor-pointer" @click="goTo('/board')">
            <i class="fas fa-comments me-3"></i> 커뮤니티 게시판
          </li>
          <li class="list-group-item cursor-pointer" @click="goTo('/classes')">
            <i class="fas fa-chalkboard-teacher me-3"></i> 예술 클래스
          </li>
          <li v-if="authStore.user.role === '예술가님'" class="list-group-item cursor-pointer" @click="goTo('/register-product')">
            <i class="fas fa-feather-alt me-3"></i> 작품 등록
          </li>
        </ul>

        <div class="mt-auto pt-3 border-top">
          <button v-if="authStore.isLoggedIn" class="btn btn-sm btn-outline-danger w-100" @click="handleLogout">
            <i class="fas fa-sign-out-alt me-2"></i> 로그아웃
          </button>
          <div v-else class="d-flex gap-2">
            <button class="btn btn-sm btn-primary w-50" @click="goTo('/login')">로그인</button>
            <button class="btn btn-sm btn-outline-primary w-50" @click="goTo('/register')">회원가입</button>
          </div>
        </div>
      </div>
    </div>

  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { useAuthStore } from '@/stores/useAuthStore'
import { useAppStore } from '@/stores/useAppStore' // useAppStore 임포트

const router = useRouter()
const route = useRoute()
const authStore = useAuthStore()
const appStore = useAppStore() // useAppStore 사용

const isDrawerOpen = ref(false)

// 하단 내비게이션 아이템 정의 (App.vue에서 이동)
const navItems = ref([
  { path: '/main', label: '홈', iconClass: 'fas fa-home' },
  { path: '/', label: '지도', iconClass: 'fas fa-map-marked-alt' },
  { path: '/artworks', label: '작품', iconClass: 'fas fa-palette' },
  { path: '/board', label: '커뮤니티', iconClass: 'fas fa-comments' },
  { path: '/mypage', label: '마이', iconClass: 'fas fa-user' },
])

const currentRoute = computed(() => route.path)

// 헤더와 하단 내비게이션의 높이만큼 콘텐츠 영역에 패딩 적용 (App.vue에서 이동)
const contentPaddingStyle = computed(() => {
  if (appStore.getShowLayout) { // appStore 상태 사용
    return {
      'padding-top': '55px', // Header 높이
      'padding-bottom': '60px', // Footer Nav 높이
      'min-height': '100vh', // 최소 높이 보장
    }
  }
  return {
    'padding-top': '0',
    'padding-bottom': '0',
    'min-height': '100vh',
  }
})

// Drawer 메뉴 클릭 핸들러: 드로어를 닫고 페이지 이동 (App.vue에서 이동)
const goTo = (path) => {
  isDrawerOpen.value = false;
  router.push(path);
};

// 로그아웃 핸들러 (App.vue에서 이동)
const handleLogout = () => {
  authStore.logout() // AuthStore의 logout 액션 호출
  isDrawerOpen.value = false
  router.push('/login')
}
</script>

<style scoped>
/* AppLayout 스타일은 최소화하여 full-screen 레이아웃을 보장합니다. */
.app-content {
  padding: 0 !important; /* 자식 뷰에서 개별 패딩 적용 */
}

/* Drawer/Sidebar Styles (App.vue에서 이동) */
.drawer-backdrop {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.5);
  z-index: 1000;
  transition: opacity 0.3s ease;
}

.drawer {
  position: fixed;
  top: 0;
  right: 0;
  width: 200px;
  max-width: 80%;
  height: 100%;
  background-color: white;
  box-shadow: -2px 0 5px rgba(0, 0, 0, 0.1);
  transform: translateX(100%);
  transition: transform 0.3s ease;
  z-index: 1001;
}

.drawer.open {
  transform: translateX(0);
}

.cursor-pointer {
  cursor: pointer;
}
</style>