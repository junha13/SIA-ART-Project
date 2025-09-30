<template>
  <div class="app-content flex-column-fluid">
    <div class="app-container-fluid">
      
      <!-- 상단 헤더: 통일된 디자인 -->
      <div class="d-flex align-items-center justify-content-between pt-5 pb-3 mb-5 border-bottom px-3">
        
        <button class="btn btn-icon btn-active-light-primary w-30px h-30px" @click="$router.back()">
          <i class="ki-duotone ki-arrow-left fs-2 text-gray-800"></i>
        </button>
        
        <!-- ⭐ H1 태그의 제목 중앙 정렬 및 안전 영역 확보 -->
        <h1 class="page-heading d-flex flex-column justify-content-center text-dark fw-bold fs-3 m-0 position-absolute start-50 translate-middle-x header-title-safe">
          게시글 상세
        </h1>
        
        <i class="ki-duotone ki-dots-vertical fs-2 text-gray-800" style="cursor: pointer;"></i>
      </div>

      <!-- 게시글 본문 컨테이너 -->
      <div class="card card-flush shadow-sm mx-3 mb-5 p-5">

        <!-- 제목 영역 및 메타 데이터 재배치 -->
        <div class="mb-5 border-bottom pb-4">
          <h2 class="fw-bolder text-gray-900 mb-3 fs-1">{{ post.title }}</h2>
          <div class="d-flex justify-content-between flex-wrap text-muted small fw-semibold">
            <!-- 작성자, 분류, 날짜 정보 (좌측) -->
            <div class="d-flex align-items-center text-gray-600 mb-2 fs-6">
              <span class="badge badge-light-secondary fw-bold rounded-pill me-3">{{ post.category }}</span>
              <span class="me-3">작성자: <span class="text-dark fw-semibold">{{ post.author }}</span></span>
              <span class="text-gray-500">{{ post.date }}</span>
            </div>
            <!-- 조회수, 추천수 정보 (우측 - 아이콘 축소) -->
            <div class="d-flex align-items-center mb-2 fs-6">
              <span class="text-gray-600 me-3">
                <i class="ki-duotone ki-eye fs-6 me-1 text-info"></i> 조회 {{ post.views }}
              </span>
              <span class="text-gray-600">
                <i class="ki-duotone ki-heart fs-6 me-1 text-danger"></i> 추천 {{ post.likes }}
              </span>
            </div>
          </div>
        </div>

        <!-- 본문 내용: 폰트 크기 및 줄 간격 조정 -->
        <div class="mb-5">
          <p class="text-gray-800 fs-4 lh-lg">{{ post.content }}</p>
          <div v-if="post.image" class="mt-5 text-center">
            <!-- ⭐ 더미 사진 추가 -->
            <img :src="post.image" alt="첨부 이미지" class="img-fluid rounded shadow-lg border border-gray-300" />
          </div>
        </div>

        <!-- 태그 -->
        <div v-if="post.tags.length > 0" class="mb-5 border-top pt-4">
          <span v-for="tag in post.tags" :key="tag" class="badge badge-light-secondary fw-bold me-2 py-2 px-4 rounded-pill">
            #{{ tag }}
          </span>
        </div>

        <!-- ⭐ 버튼: 크기 축소, 정렬 우측 유지, 순서 변경 -->
        <div class="d-flex justify-content-end gap-2 pt-3 border-top">
          <!-- ⭐ 1. 추천 버튼 (크기: btn-sm) -->
          <button class="btn btn-dark btn-sm fw-bold shadow-sm d-flex align-items-center" @click="likePost">
            <i class="ki-duotone ki-heart fs-6 me-1 text-danger"></i> <span class="text-white">추천 ({{ post.likes }})</span>
          </button>
          <!-- ⭐ 2. 수정 버튼 (크기: btn-sm) -->
          <button class="btn btn-dark btn-sm fw-bold d-flex align-items-center" @click="editPost">
            <i class="ki-duotone ki-pencil fs-6 me-1"></i> <span class="text-white">수정</span>
          </button>
          <!-- ⭐ 3. 삭제 버튼 (크기: btn-sm) -->
          <button class="btn btn-dark btn-sm fw-bold d-flex align-items-center" @click="confirmDelete">
            <i class="ki-duotone ki-trash fs-6 me-1 text-danger"></i> <span class="text-white">삭제</span>
          </button>
        </div>
      </div>

      <!-- ⭐ 댓글 영역: 쉐도우 비율 낮추기 -->
      <div class="card card-flush shadow-xs mx-3">
        <div class="card-body p-5">
          <!-- 댓글 헤더: text-gray-900로 변경하여 다크 톤 유지 -->
          <h5 class="fw-bolder text-gray-900 mb-4">댓글 (<span class="text-gray-900">{{ comments.length }}</span>)</h5>

          <!-- 댓글 입력 -->
          <div class="mb-6 d-flex">
            <input
                v-model="newComment"
                type="text"
                class="form-control me-3 form-control-solid rounded-2 border border-gray-300"
                placeholder="댓글을 입력하세요"
                @keyup.enter="addComment"
            />
            <button class="btn btn-dark fw-bold text-nowrap" @click="addComment">등록</button>
          </div>

          <!-- 댓글 리스트 -->
          <ul class="list-group list-group-flush">
            <li v-for="(comment, index) in comments" :key="index"
                class="list-group-item d-flex justify-content-between align-items-start border-bottom py-4 px-0">
              <div class="d-flex flex-column">
                <span class="text-dark fw-bolder mb-1">{{ comment.author }}</span>
                <span class="text-gray-800">{{ comment.text }}</span>
              </div>
              <div class="d-flex flex-column align-items-end">
                  <small class="text-muted fs-7 mb-2">{{ comment.date }}</small>
                  <!-- 댓글 삭제 버튼도 Dark 스타일로 변경 -->
                  <button class="btn btn-sm btn-dark fw-semibold" @click="removeComment(index)">
                    <span class="text-white">삭제</span>
                  </button>
              </div>
            </li>
          </ul>
        </div>
      </div>

      <!-- Custom Modal (삭제 확인용) -->
      <ConfirmModal
          v-model:isVisible="isModalVisible"
          :title="modalTitle"
          :message="modalMessage"
          :type="modalType"
          :autoHide="modalAutoHide"
          confirmText="삭제하기"
          @confirm="handleDelete"
      />
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from "vue"
import { useRoute, useRouter } from "vue-router"
import ConfirmModal from '../components/ConfirmModal.vue'

const route = useRoute()
const router = useRouter()

// Modal State
const isModalVisible = ref(false)
const modalTitle = ref('')
const modalMessage = ref('')
const modalType = ref('info')
const modalAutoHide = ref(true)

const showModal = (title, message, type = 'info', autoHide = true) => {
  modalTitle.value = title
  modalMessage.value = message
  modalType.value = type
  modalAutoHide.value = autoHide
  isModalVisible.value = true
}

// 임시 데이터 
const post = ref({
  id: route.params.id || 123, 
  category: "미술",
  title: "최종 디자인이 적용된 게시글 상세 페이지입니다.",
  author: "김작가",
  content: "프론트엔드 전문가의 의견을 반영하여, 제목과 본문 가독성을 높이고 버튼의 시각적 계층 구조를 강화했습니다. 추천 버튼의 섀도우를 추가하여 클릭을 유도합니다. 이것이 바로 Metronic 디자인 시스템을 활용한 최적의 커뮤니티 UI입니다.",
  likes: 5,
  views: 123,
  date: "2025.09.29",
  tags: ["미술", "UX개선", "Metronic", "Dark"],
  image: "https://placehold.co/600x300/F5A9A9/fff?text=ATTACHED+IMAGE" // 더미 이미지
})

const comments = ref([
  { author: "이관람", text: "디자인이 훨씬 깔끔하고 보기 좋아졌네요! 특히 댓글 영역이 마음에 듭니다.", date: "2025.09.29" },
  { author: "박수집", text: "추천 버튼이 눈에 확 들어와서 클릭하고 싶어져요. 수고하셨습니다!", date: "2025.09.29" }
])
const newComment = ref("")

onMounted(() => {
    post.value.views++
})

const addComment = () => {
  if (newComment.value.trim() !== "") {
    comments.value.push({
      author: "나", 
      text: newComment.value.trim(),
      date: "방금"
    })
    newComment.value = ""
    showModal('댓글 등록', '댓글이 성공적으로 등록되었습니다.', 'success', true)
  } else {
    showModal('댓글 오류', '내용을 입력해주세요.', 'error', true)
  }
}

const removeComment = (index) => {
  comments.value.splice(index, 1)
  showModal('댓글 삭제', '댓글이 삭제되었습니다.', 'info', true)
}

const likePost = () => {
  post.value.likes++
  showModal('추천 완료', '이 게시글을 추천하셨습니다.', 'success', true)
}

const editPost = () => {
  router.push(`/board/edit?id=${post.value.id}`)
}

const confirmDelete = () => {
  showModal('삭제 확인', '정말 이 게시글을 삭제하시겠습니까? 복구할 수 없습니다.', 'confirm', false)
}

const handleDelete = () => {
  router.push("/board")
  showModal('삭제 완료', '게시글이 삭제되었습니다.', 'success', true)
}
</script>

<style scoped>
/* 상단 헤더의 제목 중앙 정렬 및 배경 설정 */
.d-flex.align-items-center.justify-content-between {
    position: relative; /* 중앙 정렬 제목의 기준점 */
}

/* ⭐ 헤더 제목 안전 영역 확보: max-width를 50%로 축소하여 좌우 버튼 영역 침범 방지 */
.page-heading.position-absolute {
    z-index: 10;
    max-width: 50%; 
    text-align: center;
}

/* 댓글 입력 필드 배경색 설정 */
.form-control-solid {
    background-color: var(--bs-gray-100) !important;
}

/* ⭐ 댓글 영역 섀도우 비율 낮추기 (shadow-sm보다 더 약한 효과) */
.shadow-xs {
  box-shadow: 0 0.1rem 0.5rem 0 rgba(0, 0, 0, 0.05) !important;
}

/* 추천 버튼의 배경색 (Dark 스타일 통일) */
.btn-primary {
    background-color: var(--bs-dark) !important; 
    border-color: var(--bs-dark) !important;     
    color: #fff !important;
}

/* 수정 및 등록 버튼의 배경색 (Dark 스타일 통일) */
.btn-dark {
    background-color: var(--bs-dark) !important;
    border-color: var(--bs-dark) !important;
    color: #fff !important;
}

/* Light Danger 버튼에 대한 별도 처리 (삭제 버튼에 사용됨) */
.btn-light-danger {
    color: var(--bs-danger) !important;
    background-color: transparent !important; /* 배경 투명하게 */
    border-color: transparent !important;
}
</style>