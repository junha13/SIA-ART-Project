<template>
  <div class="container py-5">
    <!-- Header -->
    <div class="d-flex align-items-center mb-6">
      <button class="btn btn-light btn-sm me-3" @click="$router.back()">
        <i class="fas fa-chevron-left"></i>
      </button>
      <h1 class="fs-3 fw-bold mb-0">즐겨찾기한 장소</h1>
    </div>

    <!-- Empty State -->
    <div v-if="places.length === 0" class="text-center py-10">
      <i class="fas fa-map-marker-alt fs-1 text-muted mb-3"></i>
      <p class="text-muted">아직 즐겨찾기한 장소가 없습니다.</p>
    </div>

    <!-- Favorite Places -->
    <div v-else class="row g-4">
      <div v-for="place in places" :key="place.id" class="col-md-6">
        <div class="card h-100 shadow-sm">
          <div class="card-body">
            <h5 class="fw-bold mb-1">{{ place.name }}</h5>
            <p class="text-muted small mb-2">{{ place.address }}</p>
            <p class="small">{{ place.description }}</p>
          </div>
          <div class="card-footer d-flex justify-content-between">
            <button class="btn btn-sm btn-danger" @click="confirmRemove(place.id, place.name)">
              <i class="fas fa-times me-1"></i> 제거
            </button>
            <button class="btn btn-sm btn-primary" @click="confirmViewOnMap(place.name)">
              <i class="fas fa-map me-1"></i> 지도 보기
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
    />
  </div>
</template>

<script setup>
import { ref } from 'vue'
import ConfirmModal from '../components/ConfirmModal.vue'

// Modal State
const isModalVisible = ref(false)
const modalTitle = ref('')
const modalMessage = ref('')
const modalType = ref('info')
const modalAutoHide = ref(true)
const modalAction = ref(null) // 'remove' or 'viewMap'
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

const places = ref([
  { id: 1, name: '예술의전당', address: '서울 서초구', description: '다양한 공연과 전시가 열리는 복합 문화 공간' },
  { id: 2, name: '홍대 예술 거리', address: '서울 마포구', description: '젊은 예술가들의 작품과 거리 공연을 즐길 수 있는 공간' }
])

const confirmRemove = (id, name) => {
  showModal('제거 확인', `"${name}"을 즐겨찾기에서 제거하시겠습니까?`, 'confirm', 'remove', id, '제거', '취소', false)
}

const removePlace = (id) => {
  places.value = places.value.filter((p) => p.id !== id)
  showModal('제거 완료', '장소가 즐겨찾기에서 제거되었습니다.', 'success', null, null, '확인', '취소', true)
}

const confirmViewOnMap = (name) => {
  showModal('지도 보기', `"${name}"의 위치를 지도로 확인하시겠습니까?`, 'confirm', 'viewMap', null, '지도 보기', '취소', false)
}

const viewOnMap = (name) => {
  // 실제 프로젝트에서는 Naver Map 연동
  showModal('지도 로딩', `${name} 위치를 지도로 로드합니다. (더미)`, 'info', null, null, '확인', '취소', true)
}

const handleModalConfirm = () => {
  if (modalAction.value === 'remove') {
    removePlace(modalDataId.value)
  } else if (modalAction.value === 'viewMap') {
    // 모달 타이틀에서 장소 이름을 가져와야 하지만, 여기서는 더미로 처리
    viewOnMap(modalTitle.value.replace('지도 보기: ', ''))
  }
}
</script>
