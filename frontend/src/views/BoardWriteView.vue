<template>
  <div class="container py-5">
    <!-- Header -->
    <div class="d-flex align-items-center mb-6">
      <button class="btn btn-light btn-sm me-3" @click="router.back()">
        <i class="fas fa-chevron-left"></i>
      </button>
      <h2 class="fw-bold mb-0">게시글 작성</h2>
    </div>

    <!-- 카테고리 선택 -->
    <div class="card shadow-sm mb-5">
      <div class="card-body">
        <div class="mb-3">
          <label class="form-label fw-bold">분류 선택</label>
          <div class="d-flex flex-wrap gap-2">
            <button v-for="cat in categories" :key="cat"
                    class="btn btn-sm"
                    :class="selectedCategory === cat ? 'btn-primary' : 'btn-outline-primary'"
                    @click="selectedCategory = cat">
              {{ cat }}
            </button>
          </div>
        </div>

        <!-- 제목 -->
        <div class="mb-3">
          <label class="form-label fw-bold">제목</label>
          <input
              type="text"
              class="form-control form-control-solid"
              v-model="title"
              placeholder="제목을 입력하세요"
          />
        </div>

        <!-- 내용 -->
        <div class="mb-3">
          <label class="form-label fw-bold">내용</label>
          <textarea
              class="form-control form-control-solid"
              rows="6"
              v-model="content"
              placeholder="내용을 입력하세요"
          ></textarea>
        </div>

        <!-- 파일 첨부 -->
        <div class="mb-3">
          <label class="form-label fw-bold">사진 및 파일 첨부</label>
          <input type="file" class="form-control form-control-solid" multiple />
        </div>

        <!-- 태그 -->
        <div class="mb-3">
          <label class="form-label fw-bold">태그 (선택 사항)</label>
          <div class="d-flex flex-wrap gap-2 mb-2">
            <span
                v-for="(tag, index) in postTags"
                :key="index"
                class="badge bg-secondary text-white p-2 rounded-pill fs-7"
            >
              #{{ tag }}
              <button type="button" class="btn-close btn-close-white btn-close-sm ms-1" @click="removeTag(index)"></button>
            </span>
          </div>
          <div class="input-group">
            <input
                type="text"
                v-model="newTag"
                @keyup.enter.prevent="addTag"
                class="form-control form-control-solid"
                placeholder="태그를 입력하고 Enter를 누르세요"
            />
            <button type="button" class="btn btn-outline-secondary" @click="addTag">
              <i class="fas fa-plus"></i>
            </button>
          </div>
        </div>

        <!-- 버튼 -->
        <div class="d-flex justify-content-end gap-2 mt-5">
          <button class="btn btn-light-secondary" @click="saveDraft">임시저장</button>
          <button class="btn btn-primary" @click="confirmSubmit">등록</button>
        </div>
      </div>
    </div>

    <!-- Custom Modal -->
    <ConfirmModal
        v-model:isVisible="isModalVisible"
        :title="modalTitle"
        :message="modalMessage"
        :type="modalType"
        :autoHide="modalAutoHide"
        :confirmText="modalConfirmText"
        @confirm="handleModalConfirm"
    />
  </div>
</template>

<script setup>
import { ref } from "vue"
import { useRouter } from "vue-router"
import ConfirmModal from '../components/ConfirmModal.vue'

const router = useRouter()
const categories = ["미술", "음악", "공예", "정보"]
const selectedCategory = ref("미술")
const title = ref("")
const content = ref("")
const newTag = ref("")
const postTags = ref([])

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

const handleModalConfirm = () => {
  isModalVisible.value = false;
  if (modalAction.value === 'submit') {
    submitPost()
  } else if (modalAction.value === 'submitSuccess') {
    router.push("/board")
  }
}

const addTag = () => {
  const tagText = newTag.value.trim()
  if (tagText && !postTags.value.includes(tagText) && postTags.value.length < 5) {
    postTags.value.push(tagText)
    newTag.value = ''
  } else if (postTags.value.length >= 5) {
    showModal('태그 제한', '태그는 최대 5개까지만 추가할 수 있습니다.', 'error')
  }
}

const removeTag = (index) => {
  postTags.value.splice(index, 1)
}

const saveDraft = () => {
  // 임시저장 로직 (localStorage 등을 사용)
  showModal('임시저장 완료', '게시글이 브라우저에 임시 저장되었습니다.', 'success')
}

const confirmSubmit = () => {
  if (!title.value || !content.value) {
    showModal('등록 오류', '제목과 내용을 모두 입력해주세요.', 'error')
    return
  }
  showModal('등록 확인', '게시글을 등록하시겠습니까?', 'confirm', 'submit', '등록하기', false)
}

const submitPost = () => {
  // 실제 등록 로직
  console.log('게시글 등록:', { title: title.value, content: content.value, category: selectedCategory.value, tags: postTags.value })
  showModal('등록 완료', "게시글이 성공적으로 등록되었습니다!", 'success', 'submitSuccess')
}
</script>

<style scoped>
.btn-close-sm {
  font-size: 0.7rem;
}
</style>
