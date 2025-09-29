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
      <button class="btn btn-icon btn-light p-3" @click="isDrawerOpen = true">
        <i class="fas fa-bars fs-3"></i>
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
          :class="['btn fw-bold d-flex flex-column align-items-center flex-fill p-2',
                 currentRoute === item.path ? 'text-primary' : 'text-muted']"
          @click="router.push(item.path)"
          style="font-size: 0.9rem; line-height: 1;" >
        <i :class="[item.iconClass, 'fs-4']"></i> <span>{{ item.label }}</span>
      </button>
    </div>

    <div v-if="isDrawerOpen" class="drawer-backdrop" @click="isDrawerOpen = false"></div>
    <div :class="['drawer', { 'open': isDrawerOpen }]">
      <div class="p-4 d-flex flex-column h-100">
        <div class="d-flex justify-content-between align-items-center mb-5">
          <h5 class="fw-bold mb-0">메뉴</h5>
          <button class="btn btn-icon btn-light p-3" @click="isDrawerOpen = false">
            <i class="fas fa-times fs-4"></i>
          </button>
        </div>

        <div v-if="authStore.isLoggedIn" class="d-flex align-items-center mb-5 p-3 bg-light rounded-3">
          <img :src="authStore.user.profileImage" alt="user avatar" class="rounded-circle me-3" width="50" height="50" />
          <div>
            <h6 class="fw-bold mb-0 fs-5">{{ authStore.user.name }}</h6> <p class="text-muted mb-0">{{ authStore.user.role }}</p> </div>
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
// Pinia 스토어가 '@/stores/useAuthStore'와 '@/stores/useAppStore' 경로에 있다고 가정합니다.
import { useAuthStore } from '@/stores/useAuthStore'
import { useAppStore } from '@/stores/useAppStore'

const router = useRouter()
const route = useRoute()
const authStore = useAuthStore()
const appStore = useAppStore()

const isDrawerOpen = ref(false)

// 하단 내비게이션 아이템 정의
const navItems = ref([
  { path: '/home', label: '홈', iconClass: 'fas fa-home' },
  { path: '/', label: '지도', iconClass: 'fas fa-map-marked-alt' },
  { path: '/artworks', label: '작품', iconClass: 'fas fa-palette' },
  { path: '/board', label: '커뮤니티', iconClass: 'fas fa-comments' },
  { path: '/mypage', label: '마이', iconClass: 'fas fa-user' },
])

const currentRoute = computed(() => route.path)

// 헤더(55px)와 하단 내비게이션(60px)의 높이만큼 콘텐츠 영역에 패딩 적용
const contentPaddingStyle = computed(() => {
  if (appStore.getShowLayout) {
    return {
      'padding-top': '55px',
      'padding-bottom': '60px',
      'min-height': '100vh',
    }
  }
  return {
    'padding-top': '0',
    'padding-bottom': '0',
    'min-height': '100vh',
  }
})

// Drawer 메뉴 클릭 핸들러: 드로어를 닫고 페이지 이동
const goTo = (path) => {
  isDrawerOpen.value = false;
  router.push(path);
};

// 로그아웃 핸들러
const handleLogout = () => {
  authStore.logout()
  isDrawerOpen.value = false
  router.push('/login')
}
</script>

<style scoped>
/* AppLayout 스타일은 최소화하여 full-screen 레이아웃을 보장합니다. */
.app-content {
  padding: 0 !important;
  /* 자식 뷰의 패딩은 상위 contentPaddingStyle에서 관리됩니다. */
}

/* Drawer/Sidebar Styles */
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

/* 유틸리티 */
.cursor-pointer {
  cursor: pointer;
}

/* 글씨 크기 및 터치 영역 확장 스타일 */
.list-group-item {
  padding-top: 1rem !important;
  padding-bottom: 1rem !important;
  font-size: 1.05rem; /* 글씨 크기 확대 */
}
</style>