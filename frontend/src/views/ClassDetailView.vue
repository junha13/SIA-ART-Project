<template>
  <div class="container py-5">
    <!-- Header -->
    <div class="d-flex align-items-center mb-4">
      <button class="btn btn-light btn-sm me-3" @click="$router.back()">
        <i class="fas fa-chevron-left"></i>
      </button>
      <h1 class="fs-3 fw-bold mb-0">클래스 상세</h1>
    </div>

    <div v-if="!classData" class="text-center py-10">
      <div class="spinner-border text-primary mb-3" role="status"></div>
      <p class="text-muted">클래스 정보를 불러오는 중...</p>
    </div>

    <div v-else>
      <div class="card shadow-sm mb-4">
        <img :src="classData.image" class="card-img-top" style="height: 300px; object-fit: cover;"/>
        <div class="card-body">
          <h2 class="fw-bold">{{ classData.title }}</h2>
          <p class="text-muted">강사: {{ classData.instructor }}</p>
          <p>{{ classData.description }}</p>
        </div>
      </div>

      <!-- 커리큘럼 -->
      <div class="mb-3">
        <h5 class="fw-bold">커리큘럼</h5>
        <ul class="list-group list-group-flush">
          <li v-for="(week, idx) in classData.curriculum" :key="idx" class="list-group-item">
            {{ idx + 1 }}주차: {{ week }}
          </li>
        </ul>
      </div>

      <!-- 특징 -->
      <div class="mb-4">
        <h5 class="fw-bold">강좌 특징</h5>
        <p class="mb-1">장소: {{ classData.location }}</p>
        <p class="mb-1">요일: {{ classData.days }}</p>
        <p class="mb-1">정원: {{ classData.capacity }}명</p>
        <p>수강료: {{ classData.price.toLocaleString() }}원</p>
      </div>

      <!-- 신청 -->
      <div class="text-center fixed-bottom-action-bar">
        <button class="btn btn-primary w-100 py-3 fw-bolder" @click="confirmApply">
          <i class="fas fa-edit me-2"></i> 신청하기
        </button>
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
        @confirm="handleApply"
    />
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import ConfirmModal from '../components/ConfirmModal.vue'

const route = useRoute()
const classData = ref(null)

// Modal State
const isModalVisible = ref(false)
const modalTitle = ref('')
const modalMessage = ref('')
const modalType = ref('info')
const modalAutoHide = ref(true)
const modalAction = ref(null)
const modalConfirmText = ref('확인')

const showModal = (title, message, type = 'info', action = null, confirmText = '확인', autoHide = true) => {
  modalTitle.value = title
  modalMessage.value = message
  modalType.value = type
  modalAction.value = action
  modalConfirmText.value = confirmText
  modalAutoHide.value = autoHide
  isModalVisible.value = true
}

const allClasses = {
  1: {
    id: 1,
    title: '현대미술 클래스',
    instructor: '김예술',
    description: '현대미술의 기초와 창작 방법을 배우는 워크숍.',
    date: '2025-10-05',
    time: '14:00 ~ 16:00',
    location: '서울 구로구 구로동',
    days: '토, 일',
    capacity: 5,
    price: 200000,
    curriculum: ['색채의 이해', '형태 탐구', '창작 실습', '작품 발표'],
    image: 'https://placehold.co/600x350/A9F5A9/000?text=Class+1'
  }
}

const confirmApply = () => {
  showModal('신청 확인', `"${classData.value.title}" 클래스를 신청하시겠습니까?`, 'confirm', 'apply', '신청하기', false)
}

const handleApply = () => {
  // 실제 신청 로직
  showModal('신청 완료', '클래스 신청이 완료되었습니다! 마이페이지에서 확인해주세요.', 'success')
}

onMounted(() => {
  const id = parseInt(route.params.id) || 1
  classData.value = allClasses[id] || allClasses[1]
})
</script>

<style scoped>
.fixed-bottom-action-bar {
  position: fixed;
  bottom: 0;
  left: 0;
  right: 0;
  background-color: #ffffff;
  border-top: 1px solid #f0f0f0;
  padding: 1rem;
  z-index: 900;
  box-shadow: 0 -2px 10px rgba(0, 0, 0, 0.05);
}
.container {
  padding-bottom: 90px;
}
</style>
