<template>
  <div class="app-content flex-column-fluid mt-10 mb-20">
    <div class="app-container-fluid">

      <div class="d-flex align-items-center justify-content-between pt-5 pb-3 mb-5 border-bottom px-3">
        <button class="btn btn-icon btn-active-light-primary w-30px h-30px" @click="$router.back()">
          <i class="ki-duotone ki-arrow-left fs-2 text-gray-800"></i>
        </button>
        <h1 class="page-heading d-flex flex-column justify-content-center text-dark fw-bold fs-3 m-0 position-absolute start-50 translate-middle-x">
          클래스 상세 보기
        </h1>
        <i class="ki-duotone ki-dots-vertical fs-2 text-gray-800" style="cursor: pointer;"></i>
      </div>

      <div class="px-3 pb-25">
        <div v-if="isLoading" class="text-center py-5">
          <div class="spinner-border text-dark mb-3" role="status"></div>
          <p class="text-muted mb-0">클래스 정보를 불러오는 중...</p>
        </div>

        <div v-else-if="!classData" class="text-center py-5">
          <i class="fas fa-exclamation-circle fs-1 text-muted mb-3"></i>
          <p class="text-muted mb-0">클래스 정보를 찾을 수 없습니다.</p>
        </div>

        <div v-else>
          <div class="card shadow-lg rounded-xl mb-6 overflow-hidden">
            <img :src="classData.image" :alt="classData.title" class="img-fluid" style="width: 100%; height: 280px; object-fit: cover;">
          </div>

          <div class="d-flex align-items-start justify-content-between mb-3">
            <div>
              <h3 class="fw-bolder fs-1 text-dark mb-1">{{ classData.title }}</h3>
              <div class="text-muted fs-6">{{ classData.subtitle }}</div>
            </div>
            <div class="d-flex gap-2 flex-shrink-0">
              <span class="badge bg-dark text-white fw-bold py-2 px-3 rounded-pill">{{ classData.category }}</span>
              <span class="badge badge-light-secondary fw-bold text-dark py-2 px-3 rounded-pill">{{ classData.level }}</span>
            </div>
          </div>

          <div class="d-flex align-items-center gap-3 mb-6">
            <img :src="classData.instructorAvatar" alt="instructor" width="35" height="35" class="rounded-circle border border-gray-300 shadow-sm">
            <span class="fw-semibold text-dark">{{ classData.instructor }}</span>
            <span class="badge badge-light-secondary fw-bold rounded-pill px-3 text-dark">{{ durationLabel }}</span>
            <span v-if="isSmallGroup" class="badge badge-light-secondary fw-bold rounded-pill px-3 text-dark">소수정예</span>
            <span v-if="classData.materialsIncluded" class="badge badge-light-secondary fw-bold rounded-pill px-3 text-dark">재료비포함</span>
          </div>

          <div class="d-flex flex-column gap-5">
            <div class="card card-flush p-5 shadow-xs">
              <div class="fw-bolder text-dark fs-5 mb-3">강좌 소개</div>
              <div class="text-gray-700 fs-6">{{ classData.description }}</div>
            </div>

            <div class="card card-flush p-5 shadow-xs">
              <div class="fw-bolder text-dark fs-5 mb-3">커리큘럼</div>
              <ul class="list-unstyled mb-0 text-gray-800 fs-6">
                <li v-for="(week, idx) in classData.curriculum" :key="idx" class="mb-2">
                  <span class="fw-semibold text-dark me-2">{{ idx + 1 }}주차:</span>
                  <span class="ms-1">{{ week }}</span>
                </li>
              </ul>
            </div>

            <div class="card card-flush p-5 shadow-xs">
              <div class="fw-bolder text-dark fs-5 mb-3">강좌 특징</div>
              <div class="text-gray-800 fs-6">
                <div class="mb-2"><span class="fw-semibold text-muted">장소:</span> <span class="text-dark">{{ classData.location }}</span></div>
                <div class="mb-2"><span class="fw-semibold text-muted">요일:</span> <span class="text-dark">{{ classData.days }}</span></div>
                <div class="mb-2"><span class="fw-semibold text-muted">수강 정원:</span> <span class="text-dark">{{ classData.capacity }}명</span></div>
                <div class="pt-2 border-top"><span class="fw-bolder text-muted">수강료:</span> <span class="text-dark fw-bolder">{{ classData.price.toLocaleString() }}원</span></div>
              </div>
            </div>
          </div>

        </div>
      </div>
    </div>

    <div v-if="classData" class="fixed-bottom-action-bar-floating">
      <div class="container px-3">
        <button class="btn btn-dark w-100 rounded-pill py-3 fw-bolder" @click="confirmInquiry">
          <i class="ki-duotone ki-message fs-3 me-2"></i> 문의하기
        </button>
      </div>
    </div>

    <ConfirmModal
        v-model:isVisible="isModalVisible"
        :title="modalTitle"
        :message="modalMessage"
        :type="modalType"
        :confirmText="modalConfirmText"
        :autoHide="modalAutoHide"
        @confirm="handleConfirm"
    />
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { useRoute } from 'vue-router'
import { useClassStore } from '@/stores/useClassStore.js' // 1. 스토어 import
import ConfirmModal from '../components/ConfirmModal.vue'

const route = useRoute()
const classStore = useClassStore() // 2. 스토어 인스턴스 생성

// --- 상태 관리 ---
// 3. 스토어의 상태와 로딩 상태를 computed로 연결
const classData = computed(() => classStore.classDetail)
const isLoading = computed(() => classStore.classesLoading)

/* --- 모달 상태 관리 (기존 코드 유지) --- */
const isModalVisible = ref(false)
const modalTitle = ref('')
const modalMessage = ref('')
const modalType = ref('info')
const modalAutoHide = ref(true)
const modalAction = ref(null)
const modalConfirmText = ref('확인')

const showModal = (title, message, type='info', action=null, confirmText='확인', autoHide=true) => {
  modalTitle.value = title
  modalMessage.value = message
  modalType.value = type
  modalAction.value = action
  modalConfirmText.value = confirmText
  modalAutoHide.value = autoHide
  isModalVisible.value = true
}

/* --- 파생 데이터 (computed properties) --- */
// 커리큘럼 길이에 따라 'n주 완성' 라벨 생성
const durationLabel = computed(() => {
  if (!classData.value?.curriculum) return '과정'
  const n = classData.value.curriculum.length
  return n > 0 ? `${n}주 완성` : '과정'
})

// 정원이 6명 이하일 경우 '소수정예' 라벨 표시 여부 결정
const isSmallGroup = computed(() => (classData.value?.capacity || 0) <= 6)

/* --- 액션 (사용자 이벤트 처리) --- */
const confirmInquiry = () => {
  showModal('문의하기', `"${classData.value.title}" 강좌에 대해 문의하시겠습니까?`, 'confirm', 'inquiry', '문의하기', false)
}

const handleConfirm = () => {
  if (modalAction.value === 'inquiry') {
    // 실제 문의 전송 API 호출 로직이 들어갈 자리
    showModal('문의 전송 완료', '문의가 전송되었습니다. 강사의 답변을 기다려주세요.', 'success', null, '확인', true)
  }
}

/* --- 데이터 로딩 (Lifecycle Hook) --- */
onMounted(async () => {
  // 4. URL 파라미터에서 클래스 ID를 가져와 스토어의 액션을 호출
  const classId = Number(route.params.id)
  if (classId) {
    // fetchClassDetail 액션을 호출하여 API로부터 데이터를 가져오고 스토어 상태를 업데이트
    await classStore.fetchClassDetail(classId)
  }
})
</script>

<style scoped>
/* 기존 스타일은 그대로 유지합니다. */
.d-flex.align-items-center.justify-content-between {
  position: relative;
}
.page-heading.position-absolute {
  z-index: 10;
  max-width: 70%;
  text-align: center;
}
.btn-dark {
  background-color: var(--bs-dark) !important;
  border-color: var(--bs-dark) !important;
  color: #fff !important;
}
.fixed-bottom-action-bar-floating {
  position: fixed;
  bottom: 0;
  left: 0;
  right: 0;
  background-color: rgba(255, 255, 255, 0.95);
  border-top: 1px solid #f0f0f0;
  padding: 1rem 0;
  z-index: 900;
  box-shadow: 0 -2px 15px rgba(0, 0, 0, 0.1);
  backdrop-filter: blur(5px);
}
.shadow-xs {
  box-shadow: 0 0.1rem 0.5rem 0 rgba(0, 0, 0, 0.05) !important;
}
.badge-light-secondary {
  background-color: var(--bs-light-secondary) !important;
}
</style>