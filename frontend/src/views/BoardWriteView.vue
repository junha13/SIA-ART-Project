<template>
  <div class="app-content flex-column-fluid">
    <div class="app-container-fluid">
      
      <!-- 상단 헤더: 제목 동적 변경 및 디자인 유지 -->
      <div class="d-flex align-items-center justify-content-between pt-5 pb-3 mb-5 border-bottom px-3">
        
        <button class="btn btn-icon btn-active-light-primary w-30px h-30px" @click="router.back()">
          <i class="ki-duotone ki-arrow-left fs-2 text-gray-800"></i>
        </button>
        
        <h1 class="page-heading d-flex flex-column justify-content-center text-dark fw-bold fs-3 m-0 position-absolute start-50 translate-middle-x">
          예술을 찾는 사람들
          <!-- 폼 제목: 수정 모드일 때 '수정하기'로 자동 변경 -->
          <span class="fs-7 fw-normal text-gray-600">게시판 글 {{ isEditMode ? '수정하기' : '등록하기' }}</span>
        </h1>
        
        <i class="ki-duotone ki-dots-vertical fs-2 text-gray-800" style="cursor: pointer;"></i>
      </div>
      
      <!-- 메인 컨텐츠 영역 -->
      <div class="card card-flush shadow-sm mx-3 mb-5">
        <div class="card-body p-5">
          
          <!-- 카테고리 선택 -->
          <div class="mb-8">
            <label class="form-label fw-bold text-gray-800">분류 선택</label>
            <div class="d-flex flex-wrap gap-2">
              <button v-for="cat in categories" :key="cat"
                      class="btn btn-sm fw-semibold rounded-pill"
                      :class="postData.category === cat ? 'btn-dark text-white' : 'btn-outline-secondary text-gray-700'"
                      @click="postData.category = cat">
                {{ cat }}
              </button>
            </div>
          </div>

          <!-- 제목 -->
          <div class="mb-8">
            <label class="form-label fw-bold text-gray-800">제목</label>
            <input
                type="text"
                class="form-control bg-white text-dark rounded-2 border border-gray-400"
                v-model="postData.title"
                placeholder="제목을 입력하세요" 
            />
          </div>

          <!-- 내용 (TextArea) -->
          <div class="mb-8">
            <label class="form-label fw-bold text-gray-800">내용</label>
            <textarea
                class="form-control bg-white text-dark rounded-2 border border-gray-400"
                rows="12"
                v-model="postData.content"
                placeholder="내용을 입력하세요"
            ></textarea>
          </div>

          <!-- 파일 첨부 -->
          <div class="mb-8">
            <label class="form-label fw-bold text-gray-800">사진 및 파일 첨부</label>
            <div class="d-flex flex-column border border-dashed border-gray-300 rounded-2 p-5 text-center bg-light-primary">
                <label for="file-upload" class="d-flex flex-column align-items-center justify-content-center" style="cursor: pointer;">
                    <i class="ki-duotone ki-cloud-download fs-2tx text-primary mb-3"></i>
                    <div class="fw-semibold text-gray-600">
                        여기에 파일을 끌어놓거나 <span class="text-primary fw-bolder">왼쪽의 첨부 버튼</span>을 클릭하세요
                        <!-- 기존 파일 표시 기능 추가 -->
                        <div v-if="postData.files.length > 0" class="text-success mt-2 fs-7">
                          * 기존 파일 {{ postData.files.length }}개가 첨부되어 있습니다. 재업로드 시 교체됩니다.
                        </div>
                    </div>
                </label>
                <input type="file" id="file-upload" class="d-none" multiple />
            </div>
          </div>

          <!-- 태그 -->
          <div class="mb-10">
            <label class="form-label fw-bold text-gray-800">태그</label>
            <div class="d-flex flex-wrap gap-2 mb-3">
              <span
                  v-for="(tag, index) in postData.tags"
                  :key="index"
                  class="badge bg-secondary text-white p-2 rounded-pill fs-7 fw-semibold"
              >
                #{{ tag }}
                <i class="ki-duotone ki-cross-circle fs-5 ms-1" style="cursor: pointer;" @click="removeTag(index)"></i>
              </span>
            </div>
            <input
                type="text"
                v-model="newTag"
                @keyup.enter.prevent="addTag"
                class="form-control bg-white text-dark rounded-2 border border-gray-400"
                placeholder="태그를 입력하고 Enter를 누르세요"
            />
          </div>

          <!-- 버튼: 텍스트 동적 변경 및 등록/수정 로직 호출 -->
          <div class="d-flex justify-content-end gap-3">
            <button class="btn btn-light-secondary fw-bold" @click="saveDraft">임시저장</button>
            <button class="btn btn-dark fw-bold" @click="confirmSubmit">
              {{ isEditMode ? '수정 완료' : '등록' }}
            </button>
          </div>
        </div>
      </div>
      
      <!-- Custom Modal (이하 동일) -->
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
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from "vue" 
import { useRoute, useRouter } from "vue-router" 
import ConfirmModal from '../components/ConfirmModal.vue'

const router = useRouter()
const route = useRoute() 

const categories = ["미술", "음악", "공예", "정보"]
const newTag = ref("")

// 하나의 통합된 폼 데이터 상태
const postData = ref({
    id: null,
    category: "미술",
    title: "",
    content: "",
    tags: [],
    files: []
})

// isEditMode computed 속성: URL에 ID가 있으면 수정 모드
const isEditMode = computed(() => !!route.query.id)

// Modal State (이하 동일)
const isModalVisible = ref(false)
const modalTitle = ref('')
const modalMessage = ref('')
const modalType = ref('info')
const modalAutoHide = ref(true)
const modalAction = ref(null)
const modalConfirmText = ref('확인')

// onMounted: 수정 모드일 때 데이터 로드
onMounted(() => {
    if (isEditMode.value) {
        const postId = route.query.id
        
        // ⭐ API에서 불러온다고 가정한 임시 데이터 (실제 로직으로 대체 필요)
        const mockPost = {
            id: postId,
            category: "음악",
            title: `[ID ${postId}] 기존 제목이 로드되었습니다.`,
            content: "여기에 기존 게시글 내용이 로드됩니다. 이 내용을 지우거나 바꿔서 수정할 수 있습니다.",
            tags: ["음악", "작곡", "수정됨"],
            files: [{ name: "file1.jpg" }, { name: "file2.pdf" }]
        }
        
        postData.value = mockPost
    }
})

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
    // 등록이면 게시판 목록으로, 수정이면 상세 페이지로 이동
    const targetPath = isEditMode.value ? `/board/${postData.value.id}` : "/board"
    router.push(targetPath)
  }
}

const addTag = () => {
  const tagText = newTag.value.trim()
  if (tagText && !postData.value.tags.includes(tagText) && postData.value.tags.length < 5) {
    postData.value.tags.push(tagText)
    newTag.value = ''
  } else if (postData.value.tags.length >= 5) {
    showModal('태그 제한', '태그는 최대 5개까지만 추가할 수 있습니다.', 'error')
  }
}

const removeTag = (index) => {
  postData.value.tags.splice(index, 1)
}

const saveDraft = () => {
  showModal('임시저장 완료', '게시글이 브라우저에 임시 저장되었습니다.', 'success')
}

const confirmSubmit = () => {
  if (!postData.value.title || !postData.value.content) {
    // 유효성 검사 실패 시 에러 모달 표시
    showModal('등록 오류', '제목과 내용을 모두 입력해주세요.', 'error')
    return
  }
  
  // ⭐ [핵심 수정] 수정 모드일 때는 확인 모달 없이 바로 submitPost 실행
  if (isEditMode.value) {
    submitPost();
  } else {
    // 등록 모드일 때만 확인 모달 표시
    const actionText = '등록'
    const confirmMessage = '게시글을 등록하시겠습니까?'
    showModal(actionText, confirmMessage, 'confirm', 'submit', actionText, false)
  }
}

const submitPost = () => {
  if (isEditMode.value) {
    // ⭐ 수정 로직 (PUT/PATCH API 호출) 실행
    console.log('게시글 수정 완료 (ID: ' + postData.value.id + '):', postData.value)
    // 수정 완료 후에는 결과 모달을 띄우고 상세 페이지로 이동 준비
    showModal('수정 완료', "게시글이 성공적으로 수정되었습니다!", 'success', 'submitSuccess')
  } else {
    // 등록 로직 (POST API 호출)
    console.log('게시글 등록:', postData.value)
    showModal('등록 완료', "게시글이 성공적으로 등록되었습니다!", 'success', 'submitSuccess')
  }
}
</script>

<style scoped>
/* (스타일은 이전과 동일하게 유지) */
.btn-outline-secondary {
    border-color: #d1d1d1 !important;
    color: var(--bs-gray-700) !important;
}

.btn-dark {
    background-color: var(--bs-dark) !important;
    border-color: var(--bs-dark) !important;
    color: #fff !important;
}

.d-flex.align-items-center.justify-content-between {
    position: relative; 
}
.page-heading.position-absolute {
    z-index: 10;
    max-width: 70%; 
    text-align: center;
}

.form-control.bg-white {
    background-color: #fff !important;
    color: var(--bs-dark) !important;
    border-color: var(--bs-gray-400) !important; 
}
</style>