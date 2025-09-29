<template>
  <div class="container py-5">
    <div class="d-flex align-items-center mb-6">
      <button class="btn btn-light btn-sm me-3" @click="$router.back()">
        <i class="fas fa-chevron-left"></i>
      </button>
      <h1 class="fs-3 fw-bold mb-0">팔로우한 작가</h1>
    </div>

    <div v-if="artists.length === 0" class="text-center py-10">
      <i class="fas fa-user-slash fs-1 text-muted mb-3"></i>
      <p class="text-muted">아직 팔로우한 작가가 없습니다.</p>
    </div>

    <div v-else class="row g-4">
      <div
          v-for="artist in artists"
          :key="artist.id"
          class="col-md-4 col-sm-6"
      >
        <div class="card h-100 shadow-sm text-center">
          <div class="card-body">
            <img
                :src="artist.avatar"
                class="rounded-circle mb-3"
                alt="artist avatar"
                width="80"
                height="80"
            />
            <h5 class="fw-bold mb-1">{{ artist.name }}</h5>
            <p class="text-muted small mb-3">{{ artist.bio }}</p>
            <div class="d-flex justify-content-center gap-2">
              <button
                  class="btn btn-sm btn-outline-primary"
                  @click="viewProfile(artist.id)"
              >
                <i class="fas fa-user me-1"></i> 프로필
              </button>
              <button
                  class="btn btn-sm btn-danger"
                  @click="confirmUnfollow(artist.id, artist.name)"
              >
                <i class="fas fa-times me-1"></i> 언팔로우
              </button>
            </div>
          </div>
        </div>
      </div>
    </div>

    <ConfirmModal
        v-model:isVisible="isModalVisible"
        :title="modalTitle"
        :message="modalMessage"
        :type="modalType"
        :confirmText="modalConfirmText"
        :autoHide="modalAutoHide"
        @confirm="handleUnfollow"
    />
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import { useRouter } from 'vue-router'
import ConfirmModal from '../components/ConfirmModal.vue'
import { useFollowingStore } from '@/stores/useFollowingStore' // Pinia Store Import

const router = useRouter()
const followingStore = useFollowingStore() // 스토어 사용

// Modal State
const isModalVisible = ref(false)
const modalTitle = ref('')
const modalMessage = ref('')
const modalType = ref('info')
const modalAutoHide = ref(true)
const unfollowId = ref(null)
const modalConfirmText = ref('언팔로우')

const showModal = (title, message, type = 'info', autoHide = true, confirmText = '확인') => {
  modalTitle.value = title
  modalMessage.value = message
  modalType.value = type
  modalAutoHide.value = autoHide
  modalConfirmText.value = confirmText
  isModalVisible.value = true
}

// Mock data 제거, Pinia Getter 사용
const artists = followingStore.getArtists // Pinia Store의 전체 아티스트 목록 사용

const viewProfile = (id) => {
  router.push(`/artist/${id}`)
}

const confirmUnfollow = (id, name) => {
  unfollowId.value = id
  // type을 'confirm'으로 설정하고 autoHide를 false로 설정하여 사용자 확인을 기다립니다.
  showModal('언팔로우 확인', `"${name}" 작가를 정말로 언팔로우 하시겠습니까?`, 'confirm', false, '언팔로우')
}

// 실제 언팔로우 로직
const unfollow = (id) => {
  followingStore.unfollowArtist(id) // Pinia 액션 호출
  showModal('언팔로우 완료', '작가 팔로우가 해제되었습니다.', 'success', true)
}

// 모달 '확인' 버튼 클릭 시 실행될 핸들러
const handleUnfollow = () => {
  if (unfollowId.value) {
    unfollow(unfollowId.value)
    unfollowId.value = null
  }
}
</script>

<style scoped>
.card {
  transition: transform 0.2s ease;
}
.card:hover {
  transform: translateY(-3px);
}
</style>