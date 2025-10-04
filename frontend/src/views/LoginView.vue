<template>
  <div class="d-flex flex-column min-vh-100 bg-white" id="kt_login">
    <div class="container d-flex flex-column justify-content-center py-5 flex-grow-1">
      <div class="mx-auto w-100" style="max-width: 420px;">
        <!-- 헤드라인: 첫 글자만 진하게/진한색 -->
        <div class="mb-5 text-start">
          <h1 class="mb-0 lh-sm">
            <span class="d-block fw-bolder display-5 fs-5x">
              <span class="text-dark">예</span><span class="text-secondary-emphasis">술을</span>
            </span>
            <span class="d-block fw-bolder display-5 fs-5x">
              <span class="text-dark">찾</span><span class="text-secondary-emphasis">는</span>
            </span>
            <span class="d-block fw-bolder display-5 fs-5x">
              <span class="text-dark">사</span><span class="text-secondary-emphasis">람들</span>
            </span>
          </h1>
        </div>

        <!-- 폼 -->
        <form @submit.prevent="submitLogin" class="pt-2">
          <div class="mb-3">
            <input type="text" v-model="loginData.loginId"
                   class="form-control form-control-lg rounded-3 border-2 shadow-none"
                   placeholder="아이디 or 이메일" required />
          </div>

          <div class="mb-3">
            <input type="password" v-model="loginData.loginPw"
                   autocomplete="current-password"
                   class="form-control form-control-lg rounded-3 border-2 shadow-none"
                   placeholder="비밀번호" required />
          </div>

          <div class="form-check mb-4">
            <input class="form-check-input" type="checkbox" id="rememberMe" value="1" v-model="rememberMe" />
            <label class="form-check-label small text-muted" for="rememberMe">
              로그인 정보 저장
            </label>
          </div>

          <div class="d-flex flex-column gap-2">
            <button type="submit"
                    class="btn btn-link p-0 text-end text-dark fw-bold fs-5 text-decoration-none">
              로그인 <span class="ms-1">&gt;</span>
            </button>

            <router-link to="/register"
                         class="btn btn-link p-0 text-end text-secondary fw-bold fs-5 text-decoration-none">
              회원가입 <span class="ms-1">&gt;</span>
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
import { useAuthStore } from '@/stores/useAuthStore'
import axios from 'axios'

const router = useRouter()
const authStore = useAuthStore()

const isModalVisible = ref(false)
const modalTitle = ref('')
const modalMessage = ref('')
const modalType = ref('info')
const modalAutoHide = ref(true)
const modalAction = ref(null)

const loginData = ref({
  loginId: '',
  loginPw: ''
})

const showModal = (title, message, type = 'info', action = null, autoHide = true) => {
  modalTitle.value = title
  modalMessage.value = message
  modalType.value = type
  modalAction.value = action
  modalAutoHide.value = autoHide
  isModalVisible.value = true
}

const rememberMe = ref(false)

const submitLogin = async () => {
  if (!loginData.value.loginId?.trim() || !loginData.value.loginPw?.trim()) {
    showModal('로그인 오류', '아이디/이메일과 비밀번호를 모두 입력해주세요.', 'error')
    return
  }
    try {
    // POST to backend login endpoint
    const { data, status } = await axios.post('http://localhost:8080/api/users/login', {
      loginId: loginData.value.loginId.trim(),
      loginPw: loginData.value.loginPw.trim()
    })
    if (status === 200 && typeof data?.userId === 'number') {
      // 만들어진 최소 userData: 실제 프로젝트에서는 서버가 사용자 정보를 반환하도록 수정 권장
      const userData = { name: loginData.value.loginId.trim(), role: '사용자', profileImage: 'assets/media/avatars/300-1.jpg' }
      // 토큰을 서버에서 받도록 변경 권장; 현재는 임시 토큰 사용
      const token = data?.token || null
      authStore.login(userData, token, rememberMe.value)
      showModal('로그인 성공', '환영합니다!', 'success', 'loginSuccess')
    } else {
      showModal('로그인 실패', '서버 응답이 올바르지 않습니다.', 'error')
      console.error('Unexpected response:', data)
    }
  } catch (err) {
    const msg = err?.response?.data?.error
      || (typeof err?.response?.data === 'string' ? err.response.data : null)
      || '아이디 또는 비밀번호가 올바르지 않습니다.'
    showModal('로그인 실패', msg, 'error')
    console.error('Login error:', err)
  }
}


const handleModalConfirm = () => {
  isModalVisible.value = false
  if (modalAction.value === 'loginSuccess') router.push('/')
}

const login = submitLogin
</script>

<style scoped>
</style>