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
        <i class="ki-duotone ki-abstract-14 fs-2 text-gray-700">
          <span class="path1"></span>
          <span class="path2"></span>
        </i>
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
        <i :class="[item.iconClass, 'fs-4']"></i> <span style="font-size: 13px;">{{ item.label }}</span>
      </button>
    </div>

    <div v-if="isDrawerOpen" class="drawer-backdrop" @click="isDrawerOpen = false"></div>

    <div :class="['drawer drawer-custom', { 'open': isDrawerOpen }]">
      <div class="p-4 d-flex flex-column h-100">

        <div class="d-flex justify-content-between align-items-center mb-4 pb-3 border-bottom">
          <h5 class="fw-bolder fs-3 mb-0 text-dark">메뉴</h5>
          <button class="btn btn-icon btn-active-light-primary p-3" @click="isDrawerOpen = false">
            <i class="fas fa-times fs-4 text-gray-700"></i>
          </button>
        </div>

        <div class="mb-6 p-0 user-info-card">
          <div v-if="authStore.isAuthenticated" class="d-flex align-items-center p-3 user-info-box">
            <img :src="authStore.user.profileImage" alt="user avatar" class="rounded-circle me-3" width="45" height="45" />
            <div>
              <h6 class="fw-bolder mb-0 fs-6 text-dark">{{ authStore.user.name }}</h6>
              <p class="text-primary fw-semibold mb-0 fs-8">{{ authStore.user.role }}</p>
            </div>
          </div>

          <div v-else class-="p-3 bg-light-warning border border-warning rounded-3">
            <h6 class="fw-bolder fs-6 text-dark mb-1">로그인이 필요합니다.</h6>
            <p class="text-warning fw-semibold mb-0 fs-7">기능 사용을 위해 로그인하세요.</p>
          </div>
        </div>


        <ul class="list-group list-group-flush flex-grow-1 menu-list-custom">
          <li class="list-group-item cursor-pointer nav-item-minimal" @click="goTo('/mypage')">
            마이페이지
          </li>
          <li class="list-group-item cursor-pointer nav-item-minimal" @click="goTo('/artworks')">
            전체 작품 목록
          </li>
          <li class="list-group-item cursor-pointer nav-item-minimal" @click="goTo('/board')">
            커뮤니티 게시판
          </li>
          <li class="list-group-item cursor-pointer nav-item-minimal" @click="goTo('/classes')">
            예술 클래스
          </li>
          <li v-if="authStore.user.role === '예술가님'" class="list-group-item cursor-pointer nav-item-minimal" @click="goTo('/product/register')">
            작품 등록 (작가 전용)
          </li>
        </ul>

        <div class="mt-auto pt-3 border-top d-flex justify-content-end">
          <button v-if="authStore.isAuthenticated" class="btn btn-link text-muted fw-bold p-0" @click="handleLogout">
            로그아웃
          </button>
          <div v-else class="d-flex gap-2">
            <button class="btn btn-sm btn-primary w-50 fw-bold" @click="goTo('/login')">로그인</button>
            <button class="btn btn-sm btn-outline-primary w-50 fw-bold" @click="goTo('/register')">회원가입</button>
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
import { useAppStore } from '@/stores/useAppStore'

const router = useRouter()
const route = useRoute()
const authStore = useAuthStore()
const appStore = useAppStore()

const isDrawerOpen = ref(false)

const navItems = ref([
  { path: '/artworks', label: '작품', iconClass: 'fas fa-palette' },
  { path: '/board', label: '커뮤니티', iconClass: 'fas fa-comments' },
  { path: '/', label: '홈', iconClass: 'fas fa-home' },
  { path: '/archive-map', label: '지도', iconClass: 'fas fa-map-marked-alt' },
  { path: '/mypage', label: '마이', iconClass: 'fas fa-user' },
])

const currentRoute = computed(() => route.path)

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

const goTo = (path) => {
  isDrawerOpen.value = false;
  router.push(path);
};

const handleLogout = () => {
  authStore.logout()
  isDrawerOpen.value = false
  router.push('/login')
}
</script>

<style scoped>
.app-content {
  padding: 0 !important;
}

/* --- Drawer/Sidebar Styles --- */
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

.drawer-custom {
  position: fixed;
  top: 0;
  right: 0;
  /* ⭐ 핵심: 폭을 좁게 240px로 설정 */
  width: 180px;
  max-width: 80%;
  height: 100%;
  background-color: white;
  box-shadow: -5px 0 15px rgba(0, 0, 0, 0.1);
  transform: translateX(100%);
  transition: transform 0.3s cubic-bezier(0.25, 0.46, 0.45, 0.94);
  z-index: 1001;
}

.drawer-custom.open {
  transform: translateX(0);
}

/* ⭐ 사용자 정보 카드 스타일링 (시안의 파란색 박스/글씨 반영) */
.user-info-box {
  border: 2px solid var(--bs-primary); /* 파란색 외곽선 */
  border-radius: 10px;
  /* 폰트 크기 조정으로 인해 내부 패딩 조정 */
  padding: 10px !important;
}

/* ⭐ 메뉴 항목 디자인 (아이콘 제거 후 텍스트만) */
.menu-list-custom {
  padding-left: 0 !important;
}

.nav-item-minimal {
  padding-top: 0.8rem !important;
  padding-bottom: 0.8rem !important;
  font-size: 1.05rem;
  color: var(--bs-dark);
  border-bottom: 1px solid var(--bs-gray-200);
  padding-left: 0 !important; /* 좌측 패딩 제거 */
}

.nav-item-minimal:last-child {
  border-bottom: none;
}

.cursor-pointer {
  cursor: pointer;
}
</style>