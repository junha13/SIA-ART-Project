<template>
  <div class="container py-5">
    <!-- Header -->
    <div class="d-flex align-items-center mb-4">
      <button class="btn btn-light btn-sm me-3" @click="$router.back()">
        <i class="fas fa-chevron-left"></i>
      </button>
      <h2 class="fw-bold mb-0">게시글 상세</h2>
    </div>

    <!-- 제목 영역 -->
    <div class="mb-4 border-bottom pb-3">
      <h2 class="fw-bold">{{ post.title }}</h2>
      <div class="d-flex justify-content-between text-muted small">
        <span>
          분류: {{ post.category }} | 작성자: {{ post.author }} | {{ post.date }}
        </span>
        <span>
          추천 {{ post.likes }} · 조회 {{ post.views }}
        </span>
      </div>
    </div>

    <!-- 본문 내용 -->
    <div class="mb-4">
      <p>{{ post.content }}</p>
      <div v-if="post.image" class="mt-3">
        <img :src="post.image" alt="첨부 이미지" class="img-fluid rounded" />
      </div>
    </div>

    <!-- 태그 -->
    <div v-if="post.tags.length > 0" class="mb-4">
      <span v-for="tag in post.tags" :key="tag" class="badge bg-secondary me-2">
        #{{ tag }}
      </span>
    </div>

    <!-- 버튼 -->
    <div class="d-flex justify-content-end gap-2 mb-5">
      <button class="btn btn-outline-primary btn-sm" @click="likePost">
        <i class="fas fa-thumbs-up me-1"></i> 추천
      </button>
      <button class="btn btn-outline-warning btn-sm" @click="editPost">
        <i class="fas fa-edit me-1"></i> 수정
      </button>
      <button class="btn btn-outline-danger btn-sm" @click="confirmDelete">
        <i class="fas fa-trash me-1"></i> 삭제
      </button>
    </div>

    <!-- 댓글 -->
    <div class="card shadow-sm">
      <div class="card-body">
        <h5 class="fw-bold mb-3">댓글 ({{ comments.length }})</h5>

        <!-- 댓글 입력 -->
        <div class="mb-4 d-flex">
          <input
              v-model="newComment"
              type="text"
              class="form-control me-2"
              placeholder="댓글을 입력하세요"
              @keyup.enter="addComment"
          />
          <button class="btn btn-primary" @click="addComment">등록</button>
        </div>

        <!-- 댓글 리스트 -->
        <ul class="list-group list-group-flush">
          <li v-for="(comment, index) in comments" :key="index"
              class="list-group-item d-flex justify-content-between align-items-center">
            <span class="text-dark">{{ comment.author }}: {{ comment.text }}</span>
            <small class="text-muted">{{ comment.date }}</small>
            <!-- 실제로는 작성자에게만 보임 -->
            <button class="btn btn-sm btn-outline-danger ms-2" @click="removeComment(index)">
              삭제
            </button>
          </li>
        </ul>
      </div>
    </div>

    <!-- Custom Modal -->
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
</template>

<script setup>
import { ref } from "vue"
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

// 임시 데이터 (API 연동시 실제 데이터로 대체)
const post = ref({
  id: route.params.id,
  category: "미술",
  title: "요즘 미술하기 힘드네요...",
  author: "김작가",
  content: "요즘 미술계가 힘든데, 다들 어떻게 생각하시나요?",
  likes: 5,
  views: 123,
  date: "2025.09.28",
  tags: ["미술", "전시", "일상"],
  image: "https://via.placeholder.com/600x300/F5A9A9/fff?text=Board+Image"
})

const comments = ref([
  { author: "이관람", text: "저도 공감합니다.", date: "2025.09.28" },
  { author: "박수집", text: "힘내세요!", date: "2025.09.28" }
])
const newComment = ref("")

const addComment = () => {
  if (newComment.value.trim() !== "") {
    comments.value.push({
      author: "나", // 로그인된 사용자
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
  router.push(`/board/write?id=${post.value.id}`)
}

const confirmDelete = () => {
  showModal('삭제 확인', '정말 이 게시글을 삭제하시겠습니까? 복구할 수 없습니다.', 'confirm', false)
}

const handleDelete = () => {
  // 실제 삭제 로직
  router.push("/board")
  showModal('삭제 완료', '게시글이 삭제되었습니다.', 'success', true)
}
</script>
