<template>
  <div class="d-flex flex-column flex-root" id="kt_login">
    <div class="d-flex flex-column flex-column-fluid flex-center p-10 bg-white">
      <div class="mb-10 text-start w-100 w-lg-400px">
        <h1 class="text-dark fw-bolder mb-3" style="font-size: 2.8rem !important; line-height: 1.2;">
          예술을 찾는<br>사람들
        </h1>
      </div>

      <div class="w-100 w-lg-400px p-0">
        <form @submit.prevent="submitLogin" class="py-2">
          <div class="fv-row mb-4">
            <input
                type="text"
                v-model="email"
                class="form-control form-control-lg border-2 rounded-3"
                placeholder="아이디 or 이메일"
                required
            />
          </div>

          <div class="fv-row mb-4">
            <input
                type="password"
                v-model="password"
                class="form-control form-control-lg border-2 rounded-3"
                placeholder="비밀번호"
                required
            />
          </div>

          <div class="fv-row mb-12">
            <label class="form-check form-check-custom form-check-solid">
              <input
                  class="form-check-input"
                  type="checkbox"
                  id="rememberMe"
                  value="1"
              />
              <span class="form-check-label text-muted small" for="rememberMe">
                로그인 정보 저장
              </span>
            </label>
          </div>

          <div class="d-flex flex-column gap-2 mb-5">
            <a href="#" class="text-dark fw-bolder fs-5 text-end" @click.prevent="submitLogin">
              로그인 <i class="fas fa-chevron-right text-dark ms-1"></i>
            </a>
            <router-link to="/register" class="text-dark fw-bolder fs-5 text-end">
              회원가입 <i class="fas fa-chevron-right text-dark ms-1"></i>
            </router-link>
          </div>

        </form>
      </div>
    </div>

    <ConfirmModal
        v-model:isVisible="isModalVisible"
        :title="modalTitle"
        :message="modalMessage"
        :type="modalType"
        :autoHide="modalAutoHide"
        @confirm="handleModalConfirm"
    />
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import ConfirmModal from '../components/ConfirmModal.vue'
import { useAuthStore } from '@/stores/useAuthStore' // Pinia Store Import

const router = useRouter()
const email = ref('')
const password = ref('')
const authStore = useAuthStore() // AuthStore 사용

// Modal State
const isModalVisible = ref(false)
const modalTitle = ref('')
const modalMessage = ref('')
const modalType = ref('info')
const modalAutoHide = ref(true)
const modalAction = ref(null)

const showModal = (title, message, type = 'info', action = null, autoHide = true) => {
  modalTitle.value = title
  modalMessage.value = message
  modalType.value = type
  modalAction.value = action
  modalAutoHide.value = autoHide
  isModalVisible.value = true
}

const submitLogin = () => {
  if (email.value === 'test' && password.value === '1234') { // 더미 성공 조건
    // 로그인 성공 시 AuthStore 액션 호출 및 더미 데이터 전달
    authStore.login({
      name: '테스트 사용자',
      role: '예술가님',
      profileImage: 'assets/media/avatars/300-1.jpg'
    }, 'sample-jwt-token')

    showModal('로그인 성공', `${email.value}님, 환영합니다!`, 'success', 'loginSuccess')
  } else if (email.value && password.value) {
    showModal('로그인 실패', '아이디 또는 비밀번호가 올바르지 않습니다.', 'error')
  } else {
    showModal('로그인 오류', '아이디/이메일과 비밀번호를 모두 입력해주세요.', 'error')
  }
}

const handleModalConfirm = () => {
  isModalVisible.value = false;
  if (modalAction.value === 'loginSuccess') {
    router.push('/')
  }
}

const login = submitLogin;
</script>

<style scoped>
#kt_login {
  min-height: 100vh;
}
.bg-white {
  background-color: #ffffff !important;
}
.form-control-lg {
  border-radius: 0.5rem !important;
  border: 1px solid #ced4da;
}
</style>