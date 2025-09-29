<template>
  <div class="container py-5">
    <div class="card shadow-sm mb-5">
      <div class="card-body d-flex align-items-center">
        <div class="symbol symbol-70px me-4">
          <img :src="authStore.user.profileImage" alt="user avatar" class="rounded-circle" />
        </div>
        <div class="flex-grow-1">
          <h5 class="fw-bold mb-1">{{ authStore.user.name }}</h5>
          <p class="text-muted small mb-0">{{ authStore.user.role }}</p>
        </div>
        <button class="btn btn-light btn-sm ms-auto" @click="confirmLogout">
          <i class="fas fa-sign-out-alt"></i> 로그아웃
        </button>
      </div>

      <div class="card-footer pt-4 pb-2 bg-light border-0">
        <p class="fw-bold mb-2 small text-muted">팔로잉 작가 ({{ followingStore.getArtistCount }}명)</p>
        <div class="d-flex gap-3 overflow-auto pb-2">
          <div v-for="artist in followingStore.getArtistsForPreview.value" :key="artist.id" class="text-center" style="min-width: 60px;">
            <div class="symbol symbol-40px mx-auto mb-1 rounded-circle border border-primary cursor-pointer" @click="goTo(`/artist/${artist.id}`)">
              <img :src="artist.avatar" :alt="artist.name" class="rounded-circle" />
            </div>
            <small class="text-muted d-block text-truncate">{{ artist.name }}</small>
          </div>
          <div v-if="followingStore.getArtistCount === 0" class="text-muted small">
            팔로우 중인 작가가 없습니다.
          </div>
        </div>
      </div>
    </div>

    <div class="card shadow-sm mb-4">
      <div class="card-header border-0">
        <h6 class="fw-bold mb-0">거래</h6>
      </div>
      <div class="list-group list-group-flush">
        <button class="list-group-item list-group-item-action d-flex justify-content-between align-items-center" @click="goTo('/sales-history')" >
          <i class="fas fa-box-open me-2 text-warning"></i> 판매 내역 <i class="fas fa-chevron-right text-muted"></i>
        </button>
        <button class="list-group-item list-group-item-action d-flex justify-content-between align-items-center" @click="goTo('/purchase-history')" >
          <i class="fas fa-shopping-bag me-2 text-primary"></i> 구매 내역 <i class="fas fa-chevron-right text-muted"></i>
        </button>
      </div>
    </div>

    <div class="card shadow-sm mb-4">
      <div class="card-header border-0">
        <h6 class="fw-bold mb-0">관심</h6>
      </div>
      <div class="list-group list-group-flush">
        <button class="list-group-item list-group-item-action d-flex justify-content-between align-items-center" @click="goTo('/favorite-products')" >
          <i class="fas fa-heart me-2 text-danger"></i> 관심 작품 <i class="fas fa-chevron-right text-muted"></i>
        </button>
        <button class="list-group-item list-group-item-action d-flex justify-content-between align-items-center" @click="goTo('/favorite-places')" >
          <i class="fas fa-map-marked-alt me-2 text-info"></i> 관심 장소 <i class="fas fa-chevron-right text-muted"></i>
        </button>
        <button class="list-group-item list-group-item-action d-flex justify-content-between align-items-center" @click="goTo('/following-artists')" >
          <i class="fas fa-users me-2 text-success"></i> 팔로우한 작가 <i class="fas fa-chevron-right text-muted"></i>
        </button>
      </div>
    </div>

    <div class="card shadow-sm mb-4">
      <div class="card-header border-0">
        <h6 class="fw-bold mb-0">설정/등록</h6>
      </div>
      <div class="list-group list-group-flush">
        <button class="list-group-item list-group-item-action d-flex justify-content-between align-items-center" @click="goTo('/product/register')" >
          <i class="fas fa-upload me-2 text-dark"></i> 작품 등록 <i class="fas fa-chevron-right text-muted"></i>
        </button>
        <button class="list-group-item list-group-item-action d-flex justify-content-between align-items-center" @click="goTo('/location-settings')" >
          <i class="fas fa-street-view me-2 text-secondary"></i> 내 지역 설정 <i class="fas fa-chevron-right text-muted"></i>
        </button>
        <button class="list-group-item list-group-item-action d-flex justify-content-between align-items-center" @click="goTo('/board')" >
          <i class="fas fa-comments me-2 text-muted"></i> 자유 게시판 <i class="fas fa-chevron-right text-muted"></i>
        </button>
      </div>
    </div>

    <ConfirmModal
        v-model:isVisible="isModalVisible"
        title="로그아웃 확인"
        message="정말 로그아웃 하시겠습니까? 세션이 종료됩니다."
        type="confirm"
        confirmText="로그아웃"
        cancelText="취소"
        @confirm="handleLogout"
    />
  </div>
</template>

<script setup>
import { ref } from "vue"
import { useRouter } from "vue-router"
import ConfirmModal from '../components/ConfirmModal.vue'
import { useAuthStore } from '@/stores/useAuthStore'
import { useFollowingStore } from '@/stores/useFollowingStore'

const router = useRouter()
const isModalVisible = ref(false)
const goTo = (path) => router.push(path)

const authStore = useAuthStore()
const followingStore = useFollowingStore()

// 유저 더미 데이터 제거, Pinia Store에서 가져옴
// const user = ref({ ... }) // 제거됨

// 팔로잉 작가 더미 데이터 제거, Pinia Store Getter를 통해 가져옴
const followingArtists = followingStore.getArtistsForPreview

const confirmLogout = () => {
  isModalVisible.value = true
}

const handleLogout = () => {
  authStore.logout() // Pinia 액션 호출
  router.push("/login")
}
</script>

<style scoped>
.list-group-item {
  font-size: 0.95rem;
  padding: 0.9rem 1rem;
}
.card-header {
  background: #f9fafb;
}
.cursor-pointer {
  cursor: pointer;
}
/* 팔로잉 아티스트 미리보기 영역 스타일 */
.card-footer .d-flex {
  flex-wrap: nowrap;
  overflow-x: auto;
  -webkit-overflow-scrolling: touch;
}
.card-footer .d-flex > div {
  flex: 0 0 auto;
}
</style>