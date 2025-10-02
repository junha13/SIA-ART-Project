<template>
  <div class="container py-5 my-20">
    <!-- Header -->
    <div class="d-flex align-items-center mb-6">
      <button class="btn btn-light btn-sm me-3" @click="$router.back()">
        <i class="fas fa-chevron-left"></i>
      </button>
      <h1 class="fs-3 fw-bold mb-0">즐겨찾기한 작품</h1>
    </div>

    <!-- Empty State -->
    <div v-if="favorites.length === 0" class="text-center py-10">
      <i class="fas fa-heart-broken fs-1 text-muted mb-3"></i>
      <p class="text-muted">아직 즐겨찾기한 작품이 없습니다.</p>
    </div>

    <!-- Favorite Products -->
    <div v-else class="row g-4">
      <div v-for="product in favorites" :key="product.id" class="col-md-4 col-sm-6">
        <div class="card h-100 shadow-sm">
          <img :src="product.image" :alt="product.title" class="card-img-top" style="height: 180px; object-fit: cover;"/>
          <div class="card-body">
            <h5 class="fw-bold mb-1">{{ product.title }}</h5>
            <p class="text-muted small mb-2">작가: {{ product.artist }}</p>
            <p class="small text-truncate">{{ product.shortDescription }}</p>
          </div>
          <div class="card-footer d-flex justify-content-between">
            <button class="btn btn-sm btn-danger" @click="confirmRemove(product.id, product.title)">
              <i class="fas fa-times me-1"></i> 제거
            </button>
            <button class="btn btn-sm btn-success" @click="confirmPurchase(product.id, product.title)">
              <i class="fas fa-shopping-cart me-1"></i> 구매
            </button>
          </div>
        </div>
      </div>
    </div>

    <!-- Custom Modal -->
    <ConfirmModal
        v-model:isVisible="isModalVisible"
        :title="modalTitle"
        :message="modalMessage"
        :type="modalType"
        :confirmText="modalConfirmText"
        :cancelText="modalCancelText"
        :autoHide="modalAutoHide"
        @confirm="handleModalConfirm"
        @cancel="handleModalCancel"
    />
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import ConfirmModal from '../components/ConfirmModal.vue'

const router = useRouter()

// Modal State
const isModalVisible = ref(false)
const modalTitle = ref('')
const modalMessage = ref('')
const modalType = ref('info')
const modalAutoHide = ref(true)
const modalAction = ref(null) // 'remove' or 'purchase'
const modalDataId = ref(null)
const modalConfirmText = ref('확인')
const modalCancelText = ref('취소')

const showModal = (title, message, type = 'info', action = null, id = null, confirmText = '확인', cancelText = '취소', autoHide = true) => {
  modalTitle.value = title
  modalMessage.value = message
  modalType.value = type
  modalAction.value = action
  modalDataId.value = id
  modalConfirmText.value = confirmText
  modalCancelText.value = cancelText
  modalAutoHide.value = autoHide
  isModalVisible.value = true
}

// Mock Data
const favorites = ref([
  { id: 1, title: '추상화 작품', artist: '홍길동', shortDescription: '강렬한 색채가 특징인 현대 추상화', image: 'https://via.placeholder.com/400x250/F5A9A9/fff?text=Abstract+Art' },
  { id: 2, title: '풍경화', artist: '이화가', shortDescription: '자연의 아름다움을 담은 풍경화', image: 'https://via.placeholder.com/400x250/A9F5A9/000?text=Landscape+Art' },
  { id: 2, title: '풍경화', artist: '이화가', shortDescription: '자연의 아름다움을 담은 풍경화', image: 'https://via.placeholder.com/400x250/A9F5A9/000?text=Landscape+Art' }
])

const confirmRemove = (id, title) => {
  showModal('제거 확인', `"${title}" 작품을 즐겨찾기에서 제거하시겠습니까?`, 'confirm', 'remove', id, '제거', '취소', false)
}

const removeFavorite = (id) => {
  favorites.value = favorites.value.filter((p) => p.id !== id)
  showModal('제거 완료', '작품이 즐겨찾기에서 제거되었습니다.', 'success', null, null, '확인', '취소', true)
}

const confirmPurchase = (id, title) => {
  showModal('구매 확인', `"${title}" 작품의 구매 페이지로 이동하시겠습니까?`, 'confirm', 'purchase', id, '구매하기', '취소', false)
}

const purchaseProduct = (id) => {
  router.push('/purchase-history')
  showModal('이동 안내', '구매 페이지로 이동합니다.', 'info', null, null, '확인', '취소', true)
}

const handleModalConfirm = () => {
  if (modalAction.value === 'remove') {
    removeFavorite(modalDataId.value)
  } else if (modalAction.value === 'purchase') {
    purchaseProduct(modalDataId.value)
  }
}

const handleModalCancel = () => {
  // 취소 시 아무 동작 없음
}
</script>

<style scoped>
.card-img-top {
  height: 180px;
  object-fit: cover;
}
</style>
