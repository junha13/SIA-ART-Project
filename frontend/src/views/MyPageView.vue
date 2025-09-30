<template>
  <div class="bg-white min-vh-100 d-flex flex-column">

    <!-- 상단 앱바 -->
    <header class="sticky-top border-bottom">
      <div class="container d-flex align-items-center justify-content-between py-2">
        <button class="btn btn-link text-body p-0" @click="router.back()">
          <i class="fas fa-chevron-left fs-5"></i>
        </button>
        <div class="fw-bold">마이페이지</div>

        <!-- 더보기 드롭다운: 로그아웃 포함 -->
        <div class="dropdown">
          <button class="btn btn-link text-body p-0" data-bs-toggle="dropdown" aria-expanded="false">
            <i class="fas fa-ellipsis-vertical fs-5"></i>
          </button>
          <ul class="dropdown-menu dropdown-menu-end">
            <li><button class="dropdown-item" @click="goTo('/profile/edit')">프로필 편집</button></li>
            <li><hr class="dropdown-divider" /></li>
            <li><button class="dropdown-item text-danger" @click="confirmLogout">로그아웃</button></li>
          </ul>
        </div>
      </div>
    </header>

    <!-- 본문 -->
    <main class="container py-4 flex-grow-1">

      <!-- 프로필 카드 (굵은 외곽 + 둥근 모서리) -->
      <section class="border border-2 rounded-4 p-3 mb-4">
        <div class="d-flex align-items-center">
          <img
            :src="authStore.user.profileImage"
            alt="avatar"
            class="rounded-circle me-3"
            width="64" height="64"
          />
          <div class="flex-grow-1">
            <div class="fw-bold fs-5 mb-1">{{ authStore.user.name }}</div>
            <div class="text-muted small">{{ authStore.user.role }}</div>
          </div>
        </div>
      </section>

      <!-- 거래 -->
      <section class="border border-2 rounded-4 p-3 mb-4">
        <div class="small fw-bold text-muted mb-2">거래</div>

        <button type="button"
                class="w-100 bg-body-secondary border-0 rounded-2 d-flex justify-content-between align-items-center py-3 px-3 mb-3"
                @click="goTo('/sales-history')">
          <span class="text-body">판매내역</span>
          <span class="ms-1">&gt;</span>
        </button>

        <button type="button"
                class="w-100 bg-body-secondary border-0 rounded-2 d-flex justify-content-between align-items-center py-3 px-3"
                @click="goTo('/purchase-history')">
          <span class="text-body">구매내역</span>
          <span class="ms-1">&gt;</span>
        </button>
      </section>

      <!-- 관심 -->
      <section class="border border-2 rounded-4 p-3 mb-4">
        <div class="small fw-bold text-muted mb-2">관심</div>

        <button type="button"
                class="w-100 bg-body-secondary border-0 rounded-2 d-flex justify-content-between align-items-center py-3 px-3 mb-3"
                @click="goTo('/favorite-products')">
          <span class="text-body">관심상품</span>
          <span class="ms-1">&gt;</span>
        </button>

        <button type="button"
                class="w-100 bg-body-secondary border-0 rounded-2 d-flex justify-content-between align-items-center py-3 px-3 mb-3"
                @click="goTo('/favorite-places')">
          <span class="text-body">관심장소</span>
          <span class="ms-1">&gt;</span>
        </button>

        <button type="button"
                class="w-100 bg-body-secondary border-0 rounded-2 d-flex justify-content-between align-items-center py-3 px-3"
                @click="goTo('/following-artists')">
          <span class="text-body">팔로우한 작가</span>
          <span class="ms-1">&gt;</span>
        </button>
      </section>

      <!-- 설정 / 등록 -->
      <section class="border border-2 rounded-4 p-3 mb-5">
        <div class="small fw-bold text-muted mb-2">설정 / 등록</div>

        <button type="button"
                class="w-100 bg-body-secondary border-0 rounded-2 d-flex justify-content-between align-items-center py-3 px-3 mb-3"
                @click="goTo('/location-settings')">
          <span class="text-body">내 지역 설정</span>
          <span class="ms-1">&gt;</span>
        </button>

        <button type="button"
                class="w-100 bg-body-secondary border-0 rounded-2 d-flex justify-content-between align-items-center py-3 px-3 mb-3"
                @click="goTo('/class/register')">
          <span class="text-body">강좌 등록</span>
          <span class="ms-1">&gt;</span>
        </button>

        <button type="button"
                class="w-100 bg-body-secondary border-0 rounded-2 d-flex justify-content-between align-items-center py-3 px-3"
                @click="goTo('/product/register')">
          <span class="text-body">작품 등록</span>
          <span class="ms-1">&gt;</span>
        </button>
      </section>

      <!-- 로그아웃 확인 모달 -->
      <ConfirmModal
        v-model:isVisible="isModalVisible"
        title="로그아웃 확인"
        message="정말 로그아웃 하시겠습니까? 세션이 종료됩니다."
        type="confirm"
        confirmText="로그아웃"
        cancelText="취소"
        @confirm="handleLogout"
      />
    </main>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import ConfirmModal from '../components/ConfirmModal.vue'
import { useAuthStore } from '@/stores/useAuthStore'

const router = useRouter()
const authStore = useAuthStore()

const isModalVisible = ref(false)
const confirmLogout = () => { isModalVisible.value = true }
const handleLogout = () => {
  authStore.logout()
  router.push('/login')
}
const goTo = (path) => {
  router.push(path)
}
</script>