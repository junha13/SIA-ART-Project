<template>
  <div class="container py-5 my-20">
    <div class="d-flex align-items-center mb-6">
      <button class="btn btn-light btn-sm me-3" @click="$router.back()">
        <i class="fas fa-chevron-left"></i>
      </button>
      <h1 class="fs-3 fw-bold mb-0">즐겨찾기한 장소</h1>
    </div>

    <div v-if="places.length === 0" class="text-center py-10">
      <i class="fas fa-map-marker-alt fs-1 text-muted mb-3"></i>
      <p class="text-muted">아직 즐겨찾기한 장소가 없습니다.</p>
    </div>

    <div v-else class="row g-4">
      <div v-for="place in places" :key="place.id" class="col-md-6">
        <div class="card h-100 shadow-sm">
          <div class="card-body">
            <h5 class="fw-bold mb-1">{{ place.name }}</h5>
            <p class="text-muted small mb-2">{{ place.address }}</p>
            <p class="small">{{ place.description || '상세 설명이 없습니다.' }}</p>
          </div>
          <div class="card-footer d-flex justify-content-between">
            <button class="btn btn-sm btn-primary" @click="confirmViewOnMap(place.name)">
              <i class="fas fa-map me-1"></i> 지도 보기
            </button>
            <button class="btn btn-sm btn-danger" @click="confirmRemove(place.id, place.name)">
              <i class="fas fa-times me-1"></i> 제거
            </button>
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
        :cancelText="modalCancelText"
        :autoHide="modalAutoHide"
        @confirm="handleModalConfirm"
    />
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import ConfirmModal from '../components/ConfirmModal.vue'
import { useLocationStore } from '@/stores/useLocationStore'; // 🟢 Location Store Import

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

const locationStore = useLocationStore(); // Store 초기화

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

// 🟢 [수정]: Pinia Store의 interestAreas를 직접 참조
const places = locationStore.interestAreas;


const confirmRemove = (id, name) => {
  showModal('제거 확인', `"${name}"을 즐겨찾기에서 제거하시겠습니까?`, 'confirm', 'remove', id, '제거', '취소', false)
}

/**
 * 🟢 [수정]: Store 액션을 호출하여 목록에서 제거
 */
const removePlace = (id) => {
  locationStore.removeInterestArea(id);
  showModal('제거 완료', '장소가 즐겨찾기에서 제거되었습니다.', 'success', null, null, '확인', '취소', true)
}

const confirmViewOnMap = (name) => {
  showModal('지도 보기', `"${name}"의 위치를 지도로 확인하시겠습니까?`, 'confirm', 'viewMap', null, '지도 보기', '취소', false)
}

const viewOnMap = (name) => {
  locationStore.updateCurrentLocation({ // 위치 설정 뷰로 이동 대신 Store 상태 업데이트 시뮬레이션
    name: name,
    address: `"${name}" 위치로 지도를 이동합니다.`,
    lat: 37.5, // 더미 좌표
    lng: 127.0
  });
  showModal('지도 로딩', `${name} 위치를 지도로 로드합니다. (Store 상태 업데이트)`, 'info', null, null, '확인', '취소', true)
}

const handleModalConfirm = () => {
  if (modalAction.value === 'remove') {
    removePlace(modalDataId.value)
  } else if (modalAction.value === 'viewMap') {
    viewOnMap(modalTitle.value.replace('지도 보기: ', ''))
  }
}

onMounted(() => {
  // Store가 초기화되지 않았을 경우를 대비 (useLocationStore에서 이미 호출됨)
  // locationStore.initializeAreas();
});
</script>

<style scoped>
.cursor-pointer { cursor: pointer; }
.hover-shadow-lg:hover { box-shadow: 0 1rem 3rem rgba(0, 0, 0, 0.175) !important; }
.rounded-start { border-top-left-radius: 0.75rem !important; border-bottom-left-radius: 0.75rem !important; border-top-right-radius: 0 !important; }
.card { border-radius: 0.75rem; transition: box-shadow 0.3s ease; }
</style>