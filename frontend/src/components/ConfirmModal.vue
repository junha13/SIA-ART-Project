<template>
  <!-- Modal Backdrop -->
  <div v-if="isVisible" class="modal-backdrop d-flex align-items-center justify-content-center" style="z-index: 1050;">
    <!-- Modal Container -->
    <div class="modal-dialog modal-dialog-centered" role="document">
      <div class="modal-content shadow-xl rounded-lg border-0 bg-white rounded-xl" :class="modalClasses">
        <div class="modal-body p-5 p-md-8 text-center">

          <!-- ICON AREA (Enhanced) -->
          <div class="mb-5">
            <i
                :class="iconClass"
                class="fs-1 fw-bold mb-3 d-inline-block p-4 rounded-circle"
                :style="iconStyle"
            ></i>
          </div>

          <!-- Title & Message -->
          <h5 class="fw-bolder fs-3 mb-2 text-dark">{{ title }}</h5>
          <!-- 굵기(fw-bold)를 추가하고 텍스트 색상을 text-dark로 유지하여 가독성 극대화 -->
          <p class="text-dark fs-6 fw-bold mb-5" style="white-space: pre-line;">{{ message }}</p>

          <!-- Action Buttons -->
          <div v-if="type === 'confirm'" class="d-flex justify-content-center gap-3">
            <button type="button" class="btn btn-light-danger fw-bold flex-fill" @click="handleCancel">{{ cancelText }}</button>
            <button type="button" class="btn btn-primary fw-bold flex-fill" @click="handleConfirm">{{ confirmText }}</button>
          </div>
          <div v-else class="d-grid">
            <button type="button" class="btn btn-primary fw-bold" @click="handleConfirm">확인</button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, watch } from 'vue';

const props = defineProps({
  // Modal이 보여지는지 여부 (v-model)
  isVisible: {
    type: Boolean,
    default: false,
  },
  // 모달 제목
  title: {
    type: String,
    required: true,
  },
  // 모달 메시지
  message: {
    type: String,
    required: true,
  },
  // 모달 유형: 'info', 'success', 'error', 'confirm'
  type: {
    type: String,
    default: 'info',
    validator: (value) => ['info', 'success', 'error', 'confirm'].includes(value),
  },
  // 확인 버튼 텍스트 (type이 'confirm'일 때 사용)
  confirmText: {
    type: String,
    default: '확인',
  },
  // 취소 버튼 텍스트 (type이 'confirm'일 때 사용)
  cancelText: {
    type: String,
    default: '취소',
  },
  // 자동 숨김 여부 (type이 'success' 또는 'error'일 때 유용)
  autoHide: {
    type: Boolean,
    default: false,
  },
  // 자동 숨김 시간 (밀리초)
  duration: {
    type: Number,
    default: 2000,
  },
});

const emit = defineEmits(['update:isVisible', 'confirm', 'cancel']);

// 모달 유형에 따른 스타일 클래스 및 아이콘 정의
const modalConfig = computed(() => {
  switch (props.type) {
    case 'success':
      return {
        icon: 'fas fa-check-circle', // 아이콘 변경
        color: '#1BC5BD',
        bgColor: '#E8FFF3'
      };
    case 'error':
      return {
        icon: 'fas fa-times-circle', // 아이콘 변경
        color: '#F64E60',
        bgColor: '#FFF5F8'
      };
    case 'confirm':
      return {
        icon: 'fas fa-question-circle', // 아이콘 변경
        color: '#FFA800',
        bgColor: '#FFF8DD'
      };
    case 'info':
    default:
      return {
        icon: 'fas fa-info-circle',
        color: '#3699FF',
        bgColor: '#F1F9FF'
      };
  }
});

const iconClass = computed(() => modalConfig.value.icon);
const iconStyle = computed(() => ({
  color: modalConfig.value.color,
  backgroundColor: modalConfig.value.bgColor,
}));

// 단일 버튼 모드일 때의 스타일 (info, success, error)
const modalClasses = computed(() => ({
  'modal-sm': props.type !== 'confirm',
  'border-primary': props.type === 'info',
  'border-success': props.type === 'success',
  'border-danger': props.type === 'error',
}));

// 확인 버튼 클릭 처리 (type: confirm, info, success, error)
const handleConfirm = () => {
  emit('confirm');
  emit('update:isVisible', false);
};

// 취소 버튼 클릭 처리 (type: confirm)
const handleCancel = () => {
  emit('cancel');
  emit('update:isVisible', false);
};

// 자동 숨김 로직
let timeout = null;
watch(() => props.isVisible, (newVal) => {
  if (timeout) {
    clearTimeout(timeout);
  }
  if (newVal && props.autoHide && props.type !== 'confirm') {
    timeout = setTimeout(() => {
      emit('update:isVisible', false);
    }, props.duration);
  }
}, { immediate: true });
</script>

<style scoped>
.modal-backdrop {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0);
  transition: opacity 0.3s ease;
}
.modal-content {
  animation: modal-in 0.3s cubic-bezier(0.25, 0.46, 0.45, 0.94);
}
@keyframes modal-in {
  0% { transform: scale(0.9); opacity: 0; }
  100% { transform: scale(1); opacity: 1; }
}
/* 모달 크기 조정 - Metronic 기본 클래스 오버라이드 */
.modal-dialog {
  max-width: 90%; /* 모바일에서 더 잘 보이도록 */
}
@media (min-width: 576px) {
  .modal-dialog {
    max-width: 450px;
  }
}
</style>
