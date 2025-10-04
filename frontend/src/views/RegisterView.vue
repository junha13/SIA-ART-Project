<template>
  <div class="d-flex flex-column flex-root" id="kt_register">
    <div class="d-flex flex-column flex-column-fluid flex-center p-10 bg-white">

      <div class="mb-3 text-start w-100 w-lg-400px">
        <!-- 헤드라인: 첫 글자만 진하게/진한색 -->
        <div class="mb-3 text-start">
          <h1 class="lh-sm">
            <span class="d-block fw-bolder display-5 fs-4x">
              <span class="text-dark">예</span><span class="text-secondary-emphasis">술을</span>
            </span>
            <span class="d-block fw-bolder display-5 fs-4x">
              <span class="text-dark">찾</span><span class="text-secondary-emphasis">는</span>
            </span>
            <span class="d-block fw-bolder display-5 fs-4x">
              <span class="text-dark">사</span><span class="text-secondary-emphasis">람들</span>
            </span>
          </h1>
        </div>
      </div>

      <div class="w-100 w-lg-400px p-0">

        <form @submit.prevent="register">

          <div class="fv-row mb-6 d-flex gap-4">
            <label class="form-check form-check-custom form-check-solid">
              <input class="form-check-input" type="radio" name="userType" value="general" v-model="userType"
                id="typeGeneral" checked />
              <span class="form-check-label text-dark fw-bold" for="typeGeneral">
                일반 사용자
              </span>
            </label>
            <label class="form-check form-check-custom form-check-solid">
              <input class="form-check-input" type="radio" name="userType" value="artist" v-model="userType"
                id="typeArtist" />
              <span class="form-check-label text-dark fw-bold" for="typeArtist">
                예술가
              </span>
            </label>
          </div>

          <div class="fv-row mb-4">
            <input type="text" v-model="name" class="form-control form-control-lg border-2 rounded-3"
              placeholder="본인 실명" required />
          </div>

          <div class="fv-row mb-4">
            <input type="email" v-model="email" class="form-control form-control-lg border-2 rounded-3"
              placeholder="이메일" required />
          </div>

          <div class="fv-row mb-4">
            <input type="tel" v-model="phone" class="form-control form-control-lg border-2 rounded-3" placeholder="전화번호"
              required />
          </div>

          <div class="fv-row mb-4">
            <div class="input-group">
              <input type="text" v-model="userId" @input="onLoginIdChange"
                class="form-control form-control-lg border-2 rounded-3" placeholder="아이디 (영어 or 숫자, 4자 이상)" required />

              <button type="button" class="btn btn-light-secondary rounded-3 ms-2 text-dark" @click="checkDuplicateId"
                :class="['check-btn', { 'btn-available': isIdAvailable, 'btn-duplicate': isDuplicateChecked && !isIdAvailable }]"
                :disabled="!userId">
                {{ buttonText }}
              </button>
            </div>
          </div>

          <div class="fv-row mb-4">
            <input type="password" v-model="password" class="form-control form-control-lg border-2 rounded-3"
              placeholder="비밀번호 (영어 + 숫자, 8자 이상)" required />
          </div>

          <div class="fv-row mb-4">
            <div class="input-group">
              <input type="password" v-model="confirmPassword" class="form-control form-control-lg border-2 rounded-3"
                placeholder="비밀번호 확인" required />
              <button class="btn btn-light-secondary rounded-3 ms-2 text-dark" type="button"
                @click="checkPasswordMatch">
                확인
              </button>
            </div>
          </div>

          <div class="fv-row mb-8">
            <input type="text" v-model="nickname" class="form-control form-control-lg border-2 rounded-3"
              placeholder="활동명 (선택 사항)" />
          </div>

          <div class="fv-row mb-10">
            <label class="form-label fw-bold text-dark mb-3">관심 분야 선택</label>
            <div class="d-flex flex-wrap gap-3">
              <label v-for="interest in allInterests" :key="interest"
                class="form-check form-check-custom form-check-solid">
                <input class="form-check-input" type="checkbox" :value="interest" v-model="selectedInterests" />
                <span class="form-check-label text-muted" :for="'interest-' + interest">
                  {{ interest }}
                </span>
              </label>
            </div>
          </div>


          <div class="d-flex flex-column gap-2 mb-5">
            <a href="#" class="text-dark fw-bolder fs-5 text-end" @click.prevent="register">
              회원가입 <span class="ms-1">&gt;</span>
            </a>
            <router-link to="/login" class="text-muted fw-semibold fs-6 text-end">
              로그인으로 돌아가기 <span class="ms-1">&gt;</span>
            </router-link>
          </div>

        </form>
      </div>
    </div>

    <ConfirmModal v-model:isVisible="isModalVisible" :title="modalTitle" :message="modalMessage" :type="modalType"
      :autoHide="modalAutoHide" @confirm="handleModalConfirm" />
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import { useRouter } from 'vue-router'
import ConfirmModal from '../components/ConfirmModal.vue'
import { useAuthStore } from '@/stores/useAuthStore'
import axios from 'axios'

const router = useRouter()
const authStore = useAuthStore()

// 폼 데이터 (단일 소스)
const userType = ref('general') // 'general' | 'artist'
const name = ref('')
const email = ref('')
const phone = ref('')
const userId = ref('')
const password = ref('')
const confirmPassword = ref('')
const nickname = ref('')
const allInterests = ['회화', '조각', '도예', '사진', '공예', '음악', '기타']
const selectedInterests = ref([])

// 아이디 중복확인 상태
const isDuplicateChecked = ref(false)
const isIdAvailable = ref(false)

// 모달 상태
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

// 중복확인 버튼 텍스트
const buttonText = computed(() => {
  if (!isDuplicateChecked.value) return '중복확인'
  return isIdAvailable.value ? '확인완료' : '중복'
})

// 아이디 입력 변경 시, 중복확인 상태 초기화
const onLoginIdChange = () => {
  isDuplicateChecked.value = false
  isIdAvailable.value = false
}

// 비밀번호 일치 확인
const checkPasswordMatch = () => {
  if (password.value && password.value === confirmPassword.value) {
    showModal('비밀번호 확인', '비밀번호가 일치합니다.', 'success')
  } else {
    showModal('비밀번호 오류', '비밀번호가 일치하지 않거나 입력되지 않았습니다.', 'error')
  }
}

// 아이디 중복 확인
const checkDuplicateId = async () => {
  const id = userId.value.trim()

  if (id.length < 4) {
    showModal('중복 확인', '아이디는 4자 이상이어야 합니다.', 'error')
    return
  }

  try {
    const response = await axios.get('http://localhost:8080/api/users/check-id', { userId: id })
    const result = response.data === 'available'
    isDuplicateChecked.value = true
    isIdAvailable.value = result
    alert(result ? '사용 가능한 아이디입니다.' : '이미 사용 중인 아이디입니다.')
  } catch (error) {
    console.error('중복 체크 오류:', error)
    isDuplicateChecked.value = false
    isIdAvailable.value = false
    alert('서버 연결에 실패했습니다.')
  }
}

// 회원가입 실행
const register = async () => {
  if (!name.value || !email.value || !password.value || !confirmPassword.value) {
    showModal('회원가입 오류', '모든 필수 항목을 입력해주세요.', 'error')
    return
  }
  if (password.value !== confirmPassword.value) {
    showModal('회원가입 오류', '비밀번호가 일치하지 않습니다.', 'error')
    return
  }
  if (!isDuplicateChecked.value || !isIdAvailable.value) {
    alert('아이디 중복확인을 먼저 해주세요.')
    return
  }

  const registerData = {
    userId: userId.value.trim(),
    password: password.value,
    name: name.value.trim(),
    nickname: nickname.value.trim(),
    phone: phone.value.trim(),
    email: email.value.trim(),
    userType: userType.value === 'artist' ? '2' : '1',
    selectedInterests: selectedInterests.value.join(',')
  }

  try {
    const response = await axios.get('http://localhost:8080/api/users/register', registerData)

    if (response.data === 'success') {
      // showModal로 바꾸고 싶으면 밑의 alert 대신 showModal 호출
      alert('회원가입이 완료되었습니다!')
      router.push('/') // 또는 '/login'
    } else {
      alert('회원가입에 실패했습니다. 다시 시도해주세요.')
    }
  } catch (error) {
    console.error('회원가입 오류:', error)
    alert('서버 연결에 실패했습니다.')
  }
}

const handleModalConfirm = () => {
  isModalVisible.value = false
  if (modalAction.value === 'registerSuccess') {
    router.push('/login')
  }
}
</script>


<style scoped>
#kt_register {
  min-height: 100vh;
}

.bg-white {
  background-color: #ffffff !important;
}

/* 시안의 둥근 입력 필드 및 테두리 스타일 */
.form-control-lg {
  border-radius: 0.5rem !important;
  border: 1px solid #ced4da;
}

.rounded-3 {
  border-radius: 0.5rem !important;
}

.input-group>.rounded-3 {
  border-top-right-radius: 0 !important;
  border-bottom-right-radius: 0 !important;
}

.input-group .btn {
  border-radius: 0.5rem !important;
}
</style>