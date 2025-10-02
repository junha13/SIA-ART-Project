<template>
  <div class="container py-5 my-20">
    <!-- Header -->
    <div class="d-flex align-items-center mb-6">
      <button class="btn btn-light btn-sm me-3" @click="$router.back()">
        <i class="fas fa-chevron-left"></i>
      </button>
      <h1 class="fs-3 fw-bold mb-0">구매 내역</h1>
    </div>

    <!-- Purchase History -->
    <div v-if="purchases.length > 0" class="row g-4">
      <div v-for="item in purchases" :key="item.id" class="col-12 col-md-6">
        <div class="card h-100 shadow-sm cursor-pointer hover-shadow-lg" @click="confirmViewArtwork(item.artworkId, item.title)">
          <div class="row g-0">
            <!-- 작품 이미지 -->
            <div class="col-4">
              <img :src="item.image" class="img-fluid rounded-start h-100" style="object-fit: cover;" :alt="item.title" />
            </div>

            <div class="col-8">
              <div class="card-body py-3 pe-3">
                <!-- 제목 및 상태 -->
                <div class="d-flex justify-content-between align-items-start mb-2">
                  <h5 class="fw-bold mb-0 text-truncate">{{ item.title }}</h5>
                  <span class="badge bg-success fs-7 ms-2">구매 완료</span>
                </div>

                <!-- 거래 정보 -->
                <p class="small text-muted mb-2">
                  <i class="fas fa-calendar-alt me-1"></i> {{ item.date }}
                </p>
                <p class="small text-muted mb-3">
                  <i class="fas fa-user-tag me-1"></i> 판매자: {{ item.seller }}
                </p>

                <!-- 가격 강조 -->
                <div class="mt-auto pt-2 border-top">
                  <span class="fs-5 fw-bolder text-primary">{{ item.price.toLocaleString() }} 원</span>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- Empty State -->
    <div v-else class="card shadow-sm">
      <div class="p-5 text-center text-muted">
        <i class="fas fa-shopping-bag fs-1 text-muted mb-3"></i>
        <p class="mb-0">구매 내역이 없습니다.</p>
      </div>
    </div>

    <!-- Custom Modal -->
    <ConfirmModal
        v-model:isVisible="isModalVisible"
        :title="modalTitle"
        :message="modalMessage"
        :type="modalType"
        :confirmText="modalConfirmText"
        :autoHide="modalAutoHide"
        @confirm="handleModalConfirm"
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
const modalAction = ref(null)
const modalDataId = ref(null)
const modalConfirmText = ref('확인')

const showModal = (title, message, type = 'info', action = null, id = null, confirmText = '확인', autoHide = true) => {
  modalTitle.value = title
  modalMessage.value = message
  modalType.value = type
  modalAction.value = action
  modalDataId.value = id
  modalConfirmText.value = confirmText
  modalAutoHide.value = autoHide
  isModalVisible.value = true
}

const purchases = ref([
  { id: 1, artworkId: 101, title: '예술 포스터', date: '2025-09-02', price: 45000, seller: '김준하', image: 'https://placehold.co/150x150/007bff/ffffff?text=Poster' },
  { id: 2, artworkId: 102, title: '수채화 작품', date: '2025-09-08', price: 120000, seller: '박민호', image: 'https://placehold.co/150x150/28a745/ffffff?text=Watercolor' },
  { id: 3, artworkId: 103, title: '공예품', date: '2025-09-20', price: 30000, seller: '주영민', image: 'https://placehold.co/150x150/ffc107/000000?text=Craft' }
])

const confirmViewArtwork = (id, title) => {
  showModal('작품 상세 보기', `"${title}" 작품 상세 페이지로 이동하시겠습니까?`, 'confirm', 'viewArtwork', id, '이동', false)
}

const viewArtwork = (id) => {
  router.push(`/artwork/${id}`)
}

const handleModalConfirm = () => {
  if (modalAction.value === 'viewArtwork') {
    viewArtwork(modalDataId.value)
  }
}
</script>

<style scoped>
.cursor-pointer { cursor: pointer; }
.hover-shadow-lg:hover { box-shadow: 0 1rem 3rem rgba(0, 0, 0, 0.175) !important; }
.rounded-start { border-top-left-radius: 0.75rem !important; border-bottom-left-radius: 0.75rem !important; border-top-right-radius: 0 !important; }
.card { border-radius: 0.75rem; transition: box-shadow 0.3s ease; }
</style>
