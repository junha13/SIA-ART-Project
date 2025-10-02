<template>
  <div class="container py-5 my-20">
    <div class="d-flex align-items-center mb-6">
      <button class="btn btn-light btn-sm me-3" @click="$router.back()">
        <i class="fas fa-chevron-left"></i>
      </button>
      <div>
        <h1 class="fs-3 fw-bold mb-0">내 지역 설정</h1>
        <p class="text-muted small">관심 지역을 설정하세요</p>
      </div>
    </div>

    <div class="card mb-6 shadow-sm">
      <div class="card-header">
        <h3 class="card-title fs-5 fw-bold">현재 위치</h3>
      </div>
      <div class="card-body d-flex align-items-center justify-content-between">
        <div>
          <h5 class="fw-bold mb-1">{{ locationStore.currentLocation.name }}</h5>
          <p class="text-muted small mb-0">{{ locationStore.currentLocation.address }}</p>
        </div>
        <button class="btn btn-light btn-sm" @click="getCurrentLocation">
          <i class="fas fa-crosshairs me-1"></i> 내 위치로
        </button>
      </div>
    </div>

    <div class="card mb-6 shadow-sm">
      <div class="card-header">
        <h3 class="card-title fs-5 fw-bold">지역 선택</h3>
      </div>
      <div class="card-body p-0">
        <div id="locationSettingsMap" style="width: 100%; height: 300px;"></div>
      </div>
      <div class="card-footer text-center text-muted small">
        지도를 클릭하여 관심 지역을 추가하세요
      </div>
    </div>

    <div class="card mb-6 shadow-sm">
      <div class="card-header">
        <h3 class="card-title fs-5 fw-bold">관심 지역</h3>
      </div>
      <div v-if="locationStore.interestAreas.length === 0" class="card-body text-center py-10">
        <i class="fas fa-map-marker-alt fs-1 text-muted mb-3"></i>
        <p class="text-muted">아직 관심 지역이 없습니다.</p>
      </div>
      <ul v-else class="list-group list-group-flush">
        <li v-for="area in locationStore.interestAreas" :key="area.id" class="list-group-item d-flex justify-content-between align-items-center">
          <div>
            <h6 class="fw-bold mb-1">{{ area.name }}</h6>
            <p class="text-muted small mb-0">{{ area.address }}</p>
          </div>
          <div>
            <button class="btn btn-sm btn-outline-primary me-2" @click="toggleNotification(area.id)">
              <i :class="['fas', area.notifications ? 'fa-bell' : 'fa-bell-slash']"></i>
            </button>
            <button class="btn btn-sm btn-danger" @click="confirmRemoveArea(area.id, area.name)">
              <i class="fas fa-times"></i>
            </button>
          </div>
        </li>
      </ul>
    </div>

    <div class="card mb-6 shadow-sm">
      <div class="card-header">
        <h3 class="card-title fs-5 fw-bold">검색 범위</h3>
      </div>
      <div class="card-body">
        <div class="d-flex justify-content-between mb-3">
          <span class="fw-bold">반경</span>
          <span class="text-primary fw-bold">{{ searchRange }}km</span>
        </div>
        <input type="range" min="1" max="10" v-model="searchRange" class="form-range"/>
        <div class="d-flex justify-content-between text-muted small">
          <span>1km</span>
          <span>10km</span>
        </div>
      </div>
    </div>

    <div class="text-center">
      <button class="btn btn-primary px-5" @click="confirmSaveSettings">
        설정 저장
      </button>
    </div>

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
import { ref, onMounted } from 'vue'
import ConfirmModal from '../components/ConfirmModal.vue'
import { useLocationStore } from '@/stores/useLocationStore' // Pinia Store Import

// Pinia Store 사용
const locationStore = useLocationStore()

// Modal State
const isModalVisible = ref(false)
const modalTitle = ref('')
const modalMessage = ref('')
const modalType = ref('info')
const modalAutoHide = ref(true)
const modalAction = ref(null) // 'save', 'remove'
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

const naverMap = ref(null)
// searchRange는 v-model 때문에 로컬 ref로 처리 후 저장 시 Pinia 업데이트
const searchRange = ref(locationStore.searchRange)

// Pinia 상태를 직접 참조
const currentLocation = locationStore.currentLocation // Store의 ref를 직접 사용
const interestAreas = locationStore.interestAreas // Store의 ref를 직접 사용

// ... (initNaverMap, addMarkersToMap 함수 유지)
const initNaverMap = () => {
  if (!window.naver) return
  naverMap.value = new window.naver.maps.Map('locationSettingsMap', {
    center: new window.naver.maps.LatLng(currentLocation.lat, currentLocation.lng),
    zoom: 12
  })

  window.naver.maps.Event.addListener(naverMap.value, 'click', (e) => {
    addInterestArea(e.coord)
  })

  addMarkersToMap()
}

const addMarkersToMap = () => {
  if (!naverMap.value) return
  // 기존 마커 제거 로직 (생략하고 새로 그리는 방식으로 단순화)
  // 실제로는 마커를 배열로 관리하며 clearMap 후 다시 그려야 함.

  interestAreas.forEach(area => { // Pinia의 interestAreas 사용
    new window.naver.maps.Marker({
      position: new window.naver.maps.LatLng(area.lat, area.lng),
      map: naverMap.value,
      title: area.name
    })
  })
}

// getCurrentLocation 함수 내 Pinia 액션 호출
const getCurrentLocation = () => {
  if (navigator.geolocation) {
    navigator.geolocation.getCurrentPosition((pos) => {
      const lat = pos.coords.latitude
      const lng = pos.coords.longitude

      locationStore.updateCurrentLocation({ // Pinia 액션 호출
        name: '현재 위치',
        address: `위도: ${lat.toFixed(4)}, 경도: ${lng.toFixed(4)}`,
        lat, lng
      })

      naverMap.value.setCenter(new window.naver.maps.LatLng(lat, lng))
      showModal('위치 업데이트', '현재 위치로 지도를 이동했습니다.', 'info')
    })
  } else {
    showModal('오류', '위치 정보 접근을 허용해주세요.', 'error')
  }
}

// addInterestArea 함수 내 Pinia 액션 호출
const addInterestArea = (coord) => {
  const newArea = {
    id: Date.now(),
    name: `관심지역 ${interestAreas.length + 1}`,
    address: `위도: ${coord.lat().toFixed(4)}, 경도: ${coord.lng().toFixed(4)}`,
    notifications: true,
    lat: coord.lat(),
    lng: coord.lng()
  }
  locationStore.addInterestArea(newArea) // Pinia 액션 호출
  addMarkersToMap()
  showModal('지역 추가', `${newArea.name}이(가) 관심 지역으로 추가되었습니다.`, 'success')
}

// toggleNotification 함수 내 Pinia 액션 호출
const toggleNotification = (id) => {
  locationStore.toggleAreaNotification(id) // Pinia 액션 호출
  const area = interestAreas.find(a => a.id === id)
  if (area) {
    showModal('알림 설정', `${area.name} 알림이 ${area.notifications ? '켜졌습니다' : '꺼졌습니다'}.`, 'info')
  }
}

const confirmRemoveArea = (id, name) => {
  showModal('지역 제거 확인', `관심 지역 "${name}"을 목록에서 제거하시겠습니까?`, 'confirm', 'remove', id, '제거', false)
}

// removeArea 함수 내 Pinia 액션 호출
const removeArea = (id) => {
  locationStore.removeInterestArea(id) // Pinia 액션 호출
  addMarkersToMap()
  showModal('제거 완료', '관심 지역이 제거되었습니다.', 'success')
}

const confirmSaveSettings = () => {
  showModal('설정 저장 확인', `현재 설정된 지역(${interestAreas.length}개)과 검색 범위(${searchRange.value}km)를 저장하시겠습니까?`, 'confirm', 'save', null, '저장', false)
}

// saveSettings 함수 내 Pinia 액션 호출
const saveSettings = () => {
  locationStore.updateSearchRange(searchRange.value) // Pinia 액션 호출
  showModal('저장 완료', '설정이 성공적으로 저장되었습니다!', 'success')
}

const handleModalConfirm = () => {
  if (modalAction.value === 'save') {
    saveSettings()
  } else if (modalAction.value === 'remove') {
    removeArea(modalDataId.value)
  }
}


onMounted(() => {
  if (window.naver) {
    initNaverMap()
  } else {
    window.addEventListener('load', () => {
      if (window.naver) initNaverMap()
    })
  }
})
</script>