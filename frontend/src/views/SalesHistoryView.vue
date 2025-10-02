<template>
  <div class="container py-5 my-20">
    <!-- Header -->
    <div class="d-flex align-items-center mb-6">
      <button class="btn btn-light btn-sm me-3" @click="$router.back()">
        <i class="fas fa-chevron-left"></i>
      </button>
      <h1 class="fs-3 fw-bold mb-0">판매 내역</h1>
    </div>

    <!-- Sales History -->
    <div v-if="sales.length > 0" class="row g-4">
      <div v-for="item in sales" :key="item.id" class="col-12 col-md-6">
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
                  <span class="badge bg-warning fs-7 ms-2">판매 완료</span>
                </div>

                <!-- 거래 정보 -->
                <p class="small text-muted mb-2">
                  <i class="fas fa-calendar-alt me-1"></i> {{ item.date }}
                </p>
                <p class="small text-muted mb-3">
                  <i class="fas fa-user-tag me-1"></i> 구매자: {{ item.buyer }}
                </p>

                <!-- 가격 강조 -->
                <div class="mt-auto pt-2 border-top">
                  <span class="fs-5 fw-bolder text-success">{{ item.price.toLocaleString() }} 원</span>
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
        <i class="fas fa-dollar-sign fs-1 text-muted mb-3"></i>
        <p class="mb-0">판매 내역이 없습니다.</p>
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

const sales = ref([
  { id: 1, artworkId: 201, title: '현대 미술 작품', date: '2025-09-01', price: 250000, buyer: '이철수', image: 'https://placehold.co/150x150/0d6efd/ffffff?text=Modern' },
  { id: 2, artworkId: 202, title: '추상화 캔버스', date: '2025-09-10', price: 180000, buyer: '최영희', image: 'https://placehold.co/150x150/dc3545/ffffff?text=Abstract' },
  { id: 3, artworkId: 203, title: '도자기 예술품', date: '2025-09-15', price: 95000, buyer: '홍길동', image: 'https://placehold.co/150x150/6f42c1/ffffff?text=Pottery' }
])

const confirmViewArtwork = (id, title) => {
  showModal('작품 상세 보기', `판매하신 "${title}" 작품 상세 페이지로 이동하시겠습니까?`, 'confirm', 'viewArtwork', id, '이동', false)
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
