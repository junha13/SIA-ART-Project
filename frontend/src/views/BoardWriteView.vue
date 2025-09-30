<template>
  <div class="app-content flex-column-fluid my-12">
    <div class="app-container-fluid">

      <div class="d-flex align-items-center justify-content-between pt-5 pb-3 mb-5 border-bottom px-3">

        <button class="btn btn-icon btn-active-light-primary w-30px h-30px" @click="router.back()">
          <i class="ki-duotone ki-arrow-left fs-2 text-gray-800"></i>
        </button>

        <h1 class="page-heading d-flex flex-column justify-content-center text-dark fw-bold fs-3 m-0 position-absolute start-50 translate-middle-x">
          <span class="d-block">예술을 찾는 사람들</span>
          <span class="fs-7 fw-normal text-gray-600">게시판 글 {{ isEditMode ? '수정하기' : '등록하기' }}</span>
        </h1>

        <i class="ki-duotone ki-dots-vertical fs-2 text-gray-800" style="cursor: pointer;"></i>
      </div>

      <div class="card card-flush shadow-sm mx-3 mb-5 write-card-bg">
        <div class="card-body p-5">

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

          <div class="mb-8">
            <label class="form-label fw-bold text-gray-800">제목</label>
            <input
                type="text"
                class="form-control bg-white text-dark rounded-2 border border-gray-400"
                v-model="postData.title"
                placeholder="제목을 입력하세요"
            />
          </div>

          <div class="mb-8">
            <label class="form-label fw-bold text-gray-800">내용</label>
            <textarea
                class="form-control bg-white text-dark rounded-2 border border-gray-400"
                rows="12"
                v-model="postData.content"
                placeholder="내용을 입력하세요"
            ></textarea>
          </div>

          <div class="mb-8">
            <label class="form-label fw-bold text-gray-800">사진 및 파일 첨부</label>
            <div class="d-flex flex-column border border-dashed border-gray-300 rounded-2 p-5 text-center bg-gray-100">
              <label for="file-upload" class="d-flex flex-column align-items-center justify-content-center" style="cursor: pointer;">
                <i class="ki-duotone ki-cloud-download fs-2tx text-primary mb-3"></i>
                <div class="fw-semibold text-gray-600">
                  여기에 파일을 끌어놓거나 <span class="text-primary fw-bolder">버튼</span>을 클릭하세요
                  <div v-if="postData.files.length > 0 && !uploadedFiles.length" class="text-success mt-2 fs-7">
                    * 기존 파일 {{ postData.files.length }}개가 첨부되어 있습니다.
                  </div>
                </div>
              </label>
              <input type="file" id="file-upload" class="d-none" multiple @change="handleFileUpload" />
            </div>

            <div v-if="uploadedFiles.length > 0" class="mt-4">
              <h6 class="fs-7 fw-bold text-gray-700 mb-2">업로드 대기 목록 ({{ uploadedFiles.length }}개):</h6>
              <div class="d-flex flex-wrap gap-2">
                    <span v-for="(file, index) in uploadedFiles" :key="index"
                          class="badge bg-secondary text-white p-2 rounded-pill fs-7 fw-semibold">
                        {{ file.name }}
                        <i class="ki-duotone ki-cross-circle fs-5 ms-1 text-danger" style="cursor: pointer;" @click="removeFile(index)"></i>
                    </span>
              </div>
            </div>
          </div>

          <div class="mb-10">
            <label class="form-label fw-bold text-gray-800">태그</label>
            <div class="d-flex flex-wrap gap-2 mb-3">
              <span
                  v-for="(tag, index) in postData.tags"
                  :key="index"
                  class="badge bg-secondary text-white p-2 rounded-pill fs-7 fw-semibold"
              >
                #{{ tag }}
                <i class="ki-duotone ki-cross-circle fs-5 ms-1 text-danger" style="cursor: pointer;" @click="removeTag(index)"></i>
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

          <div class="d-flex justify-content-end gap-3">
            <button class="btn btn-light-secondary fw-bold" @click="saveDraft">임시저장</button>
            <button class="btn btn-dark fw-bold" @click="confirmSubmit">
              {{ isEditMode ? '수정 완료' : '등록' }}
            </button>
          </div>
        </div>
      </div>

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
import { MOCK_POSTS, MOCK_ARTISTS } from '@/data/MockData.js' // 🟢 MockData Import

const router = useRouter()
const route = useRoute()
const DRAFT_STORAGE_KEY = 'board_post_draft';

const categories = ["미술", "음악", "공예", "정보"]
const newTag = ref("")

// 하나의 통합된 폼 데이터 상태
const postData = ref({
  id: null,
  category: "미술",
  title: "",
  content: "",
  tags: [],
  files: [] // 기존 파일 정보 (수정 모드 로드시 사용)
})

const uploadedFiles = ref([])

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

// ⭐ 로드된 임시 저장 데이터를 postData에 적용하는 함수
const loadDraftData = (draft) => {
  postData.value.category = draft.category || '미술';
  postData.value.title = draft.title || '';
  postData.value.content = draft.content || '';
  postData.value.tags = draft.tags || [];
}

// ⭐ onMounted: 수정 모드일 때 데이터 로드, 아니면 임시 저장 데이터 로드 여부 질문
onMounted(() => {
  if (isEditMode.value) {
    const postId = parseInt(route.query.id) || 2;
    // 🟢 MockData에서 게시글 찾기
    const mockPost = MOCK_POSTS.find(p => p.id === postId) || MOCK_POSTS[0];

    // 수정 모드: 기존 게시글 데이터 로드 (API 호출 시뮬레이션)
    postData.value = {
      id: postId,
      category: mockPost.category,
      title: mockPost.title,
      content: mockPost.content,
      tags: mockPost.tags,
      files: [{ name: "기존_작품1.jpg" }, { name: "참고_자료.pdf" }]
    }
  } else {
    // ⭐ 작성 모드: 임시 저장 데이터 로드 여부 질문
    const savedDraft = localStorage.getItem(DRAFT_STORAGE_KEY);
    if (savedDraft) {
      showModal(
          '임시저장 확인',
          '작성 중이던 글이 있습니다. 이 내용을 불러오시겠습니까?',
          'confirm',
          'load_draft',
          '불러오기',
          false
      );
    }
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
    // 등록 확인 모달 -> 등록 로직 실행
    submitPost()
  } else if (modalAction.value === 'submitSuccess') {
    // 등록/수정 완료 모달 -> 리스트로 이동
    const targetPath = isEditMode.value ? `/board/${postData.value.id}` : "/board"
    // 등록/수정 완료 시에만 임시 저장 데이터 삭제
    localStorage.removeItem(DRAFT_STORAGE_KEY);
    router.push(targetPath)
  } else if (modalAction.value === 'load_draft') {
    // ⭐ 임시 저장 불러오기 선택 시
    const savedDraft = localStorage.getItem(DRAFT_STORAGE_KEY);
    if (savedDraft) {
      try {
        const draft = JSON.parse(savedDraft);
        loadDraftData(draft); // 데이터 로드 함수 호출
      } catch (e) {
        console.error("Failed to parse draft from localStorage", e);
        localStorage.removeItem(DRAFT_STORAGE_KEY);
      }
    }
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

// 파일 처리 핸들러: 파일 상태 업데이트 (발표를 위한 핵심)
const handleFileUpload = (event) => {
  const files = Array.from(event.target.files);
  uploadedFiles.value = files;
  event.target.value = null;
}

const removeFile = (index) => {
  uploadedFiles.value.splice(index, 1);
}

const saveDraft = () => {
  try {
    const draftContent = {
      category: postData.value.category,
      title: postData.value.title,
      content: postData.value.content,
      tags: postData.value.tags
    };
    localStorage.setItem(DRAFT_STORAGE_KEY, JSON.stringify(draftContent));
    // 임시 저장 후에는 페이지를 이동하지 않고 저장 완료 모달만 표시
    showModal('임시저장 완료', '작성 중인 내용이 저장되었습니다. 페이지 이동 시에도 유지됩니다.', 'success', 'none', '확인', true);
  } catch (e) {
    console.error("Error saving draft to localStorage", e);
    showModal('저장 실패', '브라우저 저장 공간 문제로 임시 저장에 실패했습니다.', 'error');
  }
}

const confirmSubmit = () => {
  if (!postData.value.title || !postData.value.content) {
    showModal('등록 오류', '제목과 내용을 모두 입력해주세요.', 'error')
    return
  }

  if (isEditMode.value) {
    submitPost();
  } else {
    const actionText = '등록'
    const confirmMessage = '게시글을 등록하시겠습니까?'
    showModal(actionText, confirmMessage, 'confirm', 'submit', actionText, false)
  }
}

const submitPost = () => {
  const finalData = {
    ...postData.value,
    newFiles: uploadedFiles.value.map(file => file.name)
  }

  if (isEditMode.value) {
    console.log('게시글 수정 완료 (ID: ' + finalData.id + '):', finalData)
    showModal('수정 완료', "게시글이 성공적으로 수정되었습니다!", 'success', 'submitSuccess')
  } else {
    // ⭐ 등록 로직: 게시글 목록 데이터에 새 글 추가 시뮬레이션
    const newPost = {
      id: MOCK_POSTS.length + 1,
      category: finalData.category,
      title: finalData.title,
      content: finalData.content,
      comments: '0',
      author: MOCK_ARTISTS[0].name, // 현재 로그인된 사용자 (김준하)
      authorId: MOCK_ARTISTS[0].id,
      likes: 0,
      views: 0,
      date: new Date().toLocaleDateString('ko-KR'),
      tags: finalData.tags,
      image: finalData.newFiles.length > 0 ? 'https://placehold.co/600x300/F0F0F0/000?text=NEW+POST' : '',
    };
    MOCK_POSTS.unshift(newPost); // 목록 맨 앞에 추가

    console.log('게시글 등록:', newPost)
    showModal('등록 완료', "게시글이 성공적으로 등록되었습니다!", 'success', 'submitSuccess')
  }
}
</script>

<style scoped>
/* ⭐ [수정]: 배경색을 아주 약간만 더 어둡게 조정 */
.write-card-bg {
  background-color: #fcfcfc !important;
}

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