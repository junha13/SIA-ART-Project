<template>
  <div class="bg-white min-vh-100 d-flex flex-column">
    <!-- 상단 앱바 -->

    <!-- 본문 -->
    <main class="container py-4 flex-grow-1 pb-5">
      <!-- 로딩 -->
      <div v-if="!classData" class="text-center py-5">
        <div class="spinner-border text-primary mb-3" role="status"></div>
        <p class="text-muted mb-0">클래스 정보를 불러오는 중...</p>
      </div>

      <!-- 내용 -->
      <div v-else>
        <!-- 큰 썸네일 -->
        <div class="ratio ratio-16x9 mb-3">
          <img :src="classData.image" :alt="classData.title" class="w-100 h-100 rounded-4 object-fit-cover">
        </div>

        <!-- 타이틀 + 카테고리/레벨 칩 -->
        <div class="d-flex align-items-start justify-content-between mb-1">
          <h3 class="fw-bold text-dark mb-0">{{ classData.title }}</h3>
          <div class="d-flex gap-2">
            <span class="badge text-bg-light border rounded-pill px-3">{{ classData.category }}</span>
            <span class="badge text-bg-light border rounded-pill px-3">{{ classData.level }}</span>
          </div>
        </div>
        <div class="text-muted small mb-2">{{ classData.subtitle }}</div>

        <!-- 강사 + 메타 칩 -->
        <div class="d-flex align-items-center gap-2 mb-3">
          <img :src="classData.instructorAvatar" alt="instructor" width="28" height="28" class="rounded-circle">
          <small class="text-dark me-2">{{ classData.instructor }}</small>

          <span class="badge text-bg-light border rounded-pill">{{ durationLabel }}</span>
          <span v-if="isSmallGroup" class="badge text-bg-light border rounded-pill">소수정예</span>
          <span v-if="classData.materialsIncluded" class="badge text-bg-light border rounded-pill">재료비포함</span>
        </div>

<!-- 큰 회색 박스(더 진하게) + 안쪽은 연한 회색 박스들 -->
<section class="bg-secondary-subtle rounded-4 p-3 mb-5">
  <div class="d-flex flex-column gap-3">
    <!-- 강좌 소개 -->
    <div class="bg-light rounded-3 p-3">
      <div class="fw-bold text-dark mb-2">강좌 소개</div>
      <div class="small text-muted">
        {{ classData.description }}
      </div>
    </div>

    <!-- 커리큘럼 -->
    <div class="bg-light rounded-3 p-3">
      <div class="fw-bold text-dark mb-2">커리큘럼</div>
      <ul class="list-unstyled mb-0 small text-dark">
        <li v-for="(week, idx) in classData.curriculum" :key="idx" class="mb-1">
          <span class="text-muted">{{ idx + 1 }}주차:</span>
          <span class="ms-1">{{ week }}</span>
        </li>
      </ul>
    </div>

    <!-- 강좌 특징 -->
    <div class="bg-light rounded-3 p-3">
      <div class="fw-bold text-dark mb-2">강좌 특징</div>
      <div class="small text-dark">
        <div class="mb-1"><span class="text-muted">장소:</span> {{ classData.location }}</div>
        <div class="mb-1"><span class="text-muted">요일:</span> {{ classData.days }}</div>
        <div class="mb-1"><span class="text-muted">수강 정원:</span> {{ classData.capacity }}명</div>
        <div><span class="text-muted">수강료:</span> {{ classData.price.toLocaleString() }}원</div>
      </div>
    </div>
  </div>
</section>
      </div>
    </main>

    <!-- 하단 고정 액션바 -->
    <div class="fixed-bottom bg-white border-top py-3">
      <div class="container">
        <button class="btn btn-primary w-100 rounded-pill py-3 fw-bolder" @click="confirmInquiry">
          <i class="fas fa-comment-dots me-2"></i> 문의하기
        </button>
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
      @confirm="handleConfirm"
    />
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { useRoute } from 'vue-router'
import ConfirmModal from '../components/ConfirmModal.vue'

const route = useRoute()
const classData = ref(null)

/* 모달 상태 */
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

/* 더미 데이터 (기존 구조에 화면 메타 추가) */
const allClasses = {
  1: {
    id: 1,
    title: '유화 마스터',
    subtitle: '전문가를 위한 고급 유화 기법',
    category: '미술',
    level: '고급',
    instructor: '허지서',
    instructorAvatar: 'https://via.placeholder.com/64x64.png?text=HJ',
    description: '강좌 목표, 철학, 수강생이 얻을 수 있는 가치',
    date: '2025-10-12',
    time: '14:00 ~ 16:00',
    location: '서울 구로구 구로동',
    days: '토, 일',
    capacity: 5,
    price: 200000,
    materialsIncluded: true,
    curriculum: ['색채의 이해', '형태 탐구', '창작 실습', '작품 발표'],
    image: 'https://placehold.co/900x540/ddd/000?text=Oil+Master'
  }
}

/* 파생 표시값 */
const durationLabel = computed(() => {
  const n = classData.value?.curriculum?.length || 0
  return n > 0 ? `${n}주 완성` : '과정'
})
const isSmallGroup = computed(() => (classData.value?.capacity || 0) <= 6)

/* 액션 */
const confirmInquiry = () => {
  showModal('문의하기', `"${classData.value.title}" 강좌에 대해 문의하시겠습니까?`, 'confirm', 'inquiry', '문의하기', false)
}
const handleConfirm = () => {
  if (modalAction.value === 'inquiry') {
    showModal('문의 전송 완료', '문의가 전송되었습니다. 강사의 답변을 기다려주세요.', 'success', null, '확인', true)
  }
}

/* 로딩 */
onMounted(() => {
  const id = parseInt(route.params.id) || 1
  classData.value = allClasses[id] || allClasses[1]
})
</script>