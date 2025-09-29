<template>
  <div class="bg-white min-vh-100 d-flex flex-column">
    <!-- 상단 앱바 -->
    <header class="sticky-top border-bottom">
      <div class="container d-flex align-items-center justify-content-between py-2">
        <button class="btn btn-link text-dark p-0" @click="$router.back()">
          <i class="fas fa-chevron-left fs-5"></i>
        </button>
        <div class="fw-bold text-dark">팔로우한 작가</div>
        <div style="width:24px"></div>
      </div>
    </header>

    <!-- 본문 -->
    <main class="container py-4 flex-grow-1">
      <!-- 비어있을 때 -->
      <div v-if="artists.length === 0" class="text-center py-5">
        <i class="fas fa-user-slash fs-1 text-muted mb-3"></i>
        <p class="text-muted mb-0">아직 팔로우한 작가가 없습니다.</p>
      </div>

      <!-- 목록 -->
      <div v-else class="d-flex flex-column gap-3">
        <div
          v-for="artist in artists"
          :key="artist.id"
          class="border border-2 rounded-4 p-3 d-flex align-items-center"
        >
          <img
            :src="artist.avatar"
            alt="artist avatar"
            class="rounded-circle me-3"
            width="56" height="56"
          />

          <div class="flex-grow-1">
            <div class="fw-bold text-dark">{{ artist.name }}</div>
            <div class="small text-muted text-truncate">{{ artist.bio }}</div>
          </div>

          <div class="ms-auto d-flex gap-2">
            <button class="btn btn-outline-dark btn-sm" @click="viewProfile(artist.id)">
              프로필
            </button>
            <button class="btn btn-dark btn-sm" @click="confirmUnfollow(artist.id, artist.name)">
              언팔로우
            </button>
          </div>
        </div>
      </div>

      <!-- 모달 -->
      <ConfirmModal
        v-model:isVisible="isModalVisible"
        :title="modalTitle"
        :message="modalMessage"
        :type="modalType"
        :confirmText="modalConfirmText"
        :autoHide="modalAutoHide"
        @confirm="handleUnfollow"
      />
    </main>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import ConfirmModal from '../components/ConfirmModal.vue'
import { useFollowingStore } from '@/stores/useFollowingStore'

const router = useRouter()
const followingStore = useFollowingStore()

// Modal State
const isModalVisible = ref(false)
const modalTitle = ref('')
const modalMessage = ref('')
const modalType = ref('info')
const modalAutoHide = ref(true)
const modalConfirmText = ref('언팔로우')
const unfollowId = ref(null)

const showModal = (title, message, type = 'info', autoHide = true, confirmText = '확인') => {
  modalTitle.value = title
  modalMessage.value = message
  modalType.value = type
  modalAutoHide.value = autoHide
  modalConfirmText.value = confirmText
  isModalVisible.value = true
}

// Pinia Getter 사용
const artists = followingStore.getArtists

const viewProfile = (id) => router.push(`/artist/${id}`)

const confirmUnfollow = (id, name) => {
  unfollowId.value = id
  showModal('언팔로우 확인', `"${name}" 작가를 정말로 언팔로우 하시겠습니까?`, 'confirm', false, '언팔로우')
}

const unfollow = (id) => {
  followingStore.unfollowArtist(id)
  showModal('언팔로우 완료', '작가 팔로우가 해제되었습니다.', 'success', true)
}

const handleUnfollow = () => {
  if (unfollowId.value) {
    unfollow(unfollowId.value)
    unfollowId.value = null
  }
}
</script>