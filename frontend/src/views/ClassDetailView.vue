<template>
  <div class="app-content flex-column-fluid mt-10 mb-20">
    <div class="app-container-fluid">
      
      <!-- 상단 헤더: 게시판 뷰와 디자인 통일 (중앙 제목) -->
      <div class="d-flex align-items-center justify-content-between pt-5 pb-3 mb-5 border-bottom px-3">
        
        <button class="btn btn-icon btn-active-light-primary w-30px h-30px" @click="$router.back()">
          <i class="ki-duotone ki-arrow-left fs-2 text-gray-800"></i>
        </button>
        
        <h1 class="page-heading d-flex flex-column justify-content-center text-dark fw-bold fs-3 m-0 position-absolute start-50 translate-middle-x">
          클래스 상세 보기
        </h1>
        
        <i class="ki-duotone ki-dots-vertical fs-2 text-gray-800" style="cursor: pointer;"></i>
      </div>

      <!-- 본문 -->
      <div class="px-3 pb-25"> <!-- 하단바 공간 확보 -->
        <!-- 로딩 -->
        <div v-if="!classData" class="text-center py-5">
          <div class="spinner-border text-dark mb-3" role="status"></div>
          <p class="text-muted mb-0">클래스 정보를 불러오는 중...</p>
        </div>

        <!-- 내용 -->
        <div v-else>
          <!-- 큰 썸네일 -->
          <div class="card shadow-lg rounded-xl mb-6 overflow-hidden">
            <img :src="classData.image" :alt="classData.title" class="img-fluid" style="width: 100%; height: 280px; object-fit: cover;">
          </div>

          <!-- 타이틀 + 카테고리/레벨 칩 -->
          <div class="d-flex align-items-start justify-content-between mb-3">
            <div>
              <h3 class="fw-bolder fs-1 text-dark mb-1">{{ classData.title }}</h3>
              <div class="text-muted fs-6">{{ classData.subtitle }}</div>
            </div>
            <div class="d-flex gap-2 flex-shrink-0">
              <!-- 카테고리 칩 -->
              <span class="badge bg-dark text-white fw-bold py-2 px-3 rounded-pill">{{ classData.category }}</span>
              <!-- 레벨 칩 -->
              <span class="badge badge-light-secondary fw-bold text-dark py-2 px-3 rounded-pill">{{ classData.level }}</span>
            </div>
          </div>

          <!-- 강사 + 메타 칩 -->
          <div class="d-flex align-items-center gap-3 mb-6">
            <img :src="classData.instructorAvatar" alt="instructor" width="35" height="35" class="rounded-circle border border-gray-300 shadow-sm">
            <span class="fw-semibold text-dark">{{ classData.instructor }}</span>

            <!-- ⭐ 메타 정보 (Dark/Gray 톤으로 통합) -->
            <span class="badge badge-light-secondary fw-bold rounded-pill px-3 text-dark">{{ durationLabel }}</span>
            <span v-if="isSmallGroup" class="badge badge-light-secondary fw-bold rounded-pill px-3 text-dark">소수정예</span>
            <span v-if="classData.materialsIncluded" class="badge badge-light-secondary fw-bold rounded-pill px-3 text-dark">재료비포함</span>
          </div>

          <!-- ⭐ 정보 섹션 (이미지 기반) -->
          <div class="d-flex flex-column gap-5">
            <!-- 1. 강좌 소개 -->
            <div class="card card-flush p-5 shadow-xs">
              <div class="fw-bolder text-dark fs-5 mb-3">강좌 소개</div>
              <div class="text-gray-700 fs-6">
                {{ classData.description }}
              </div>
            </div>

            <!-- 2. 커리큘럼 -->
            <div class="card card-flush p-5 shadow-xs">
              <div class="fw-bolder text-dark fs-5 mb-3">커리큘럼</div>
              <ul class="list-unstyled mb-0 text-gray-800 fs-6">
                <li v-for="(week, idx) in classData.curriculum" :key="idx" class="mb-2">
                  <!-- ⭐ 주차 표시를 text-dark로 변경 (클래스 수정) -->
                  <span class="fw-semibold text-dark me-2">{{ idx + 1 }}주차:</span>
                  <span class="ms-1">{{ week }}</span>
                </li>
              </ul>
            </div>

            <!-- 3. 강좌 특징 (위치, 가격 등) -->
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

    <!-- ⭐ 하단 고정 액션바 (화면 위에 얹어진 느낌) -->
    <div class="fixed-bottom-action-bar-floating">
      <div class="container px-3">
        <!-- 문의하기 버튼: Dark 스타일 및 꽉 찬 너비 -->
        <button class="btn btn-dark w-100 rounded-pill py-3 fw-bolder" @click="confirmInquiry">
          <i class="ki-duotone ki-message fs-3 me-2"></i> 문의하기
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
    instructorAvatar: 'https://via.placeholder.com/64x64.png?text=HS', // 이미지 경로 임시 사용
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

<style scoped>
/* 상단 헤더의 제목 중앙 정렬 및 배경 설정 */
.d-flex.align-items-center.justify-content-between {
    position: relative; /* 중앙 정렬 제목의 기준점 */
}
.page-heading.position-absolute {
    z-index: 10;
    max-width: 70%; 
    text-align: center;
}

/* ⭐ Dark 스타일 및 Metronic 클래스 통일 */
.btn-dark {
    background-color: var(--bs-dark) !important;
    border-color: var(--bs-dark) !important;
    color: #fff !important;
}

/* ⭐ Floating Action Bar CSS: 화면 위에 얹어진 느낌 구현 */
.fixed-bottom-action-bar-floating {
  position: fixed;
  bottom: 0;
  left: 0;
  right: 0;
  /* 배경을 투명하게 하거나 약하게 하여 아래 내용이 비치도록 함 */
  background-color: rgba(255, 255, 255, 0.95); 
  border-top: 1px solid #f0f0f0;
  padding: 1rem 0; /* 좌우 패딩을 0으로 하고 컨테이너가 px-3을 갖도록 */
  z-index: 900;
  box-shadow: 0 -2px 15px rgba(0, 0, 0, 0.1);
  backdrop-filter: blur(5px); /* 아래 콘텐츠가 비치는 느낌 (옵션) */
}

/* ⭐ 그림자 약하게 */
.shadow-xs {
  box-shadow: 0 0.1rem 0.5rem 0 rgba(0, 0, 0, 0.05) !important;
}

/* 기타 스타일 */
.badge-light-secondary {
    background-color: var(--bs-light-secondary) !important;
}
.badge-light-info {
    background-color: var(--bs-light-secondary) !important; /* ⭐ Light Info -> Light Secondary로 통일 */
    color: var(--bs-dark) !important;
}
.badge-light-warning {
    background-color: var(--bs-light-secondary) !important; /* ⭐ Light Warning -> Light Secondary로 통일 */
    color: var(--bs-dark) !important;
}
.badge-light-success {
    background-color: var(--bs-light-secondary) !important; /* ⭐ Light Success -> Light Secondary로 통일 */
    color: var(--bs-dark) !important;
}
</style>