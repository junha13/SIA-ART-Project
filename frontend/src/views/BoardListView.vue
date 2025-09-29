<template>
  <div class="container py-5">
    <!-- 검색 -->
    <div class="input-group mb-3">
      <input type="text" class="form-control" placeholder="검색어를 입력하세요" v-model="searchQuery" />
      <button class="btn btn-outline-secondary" @click="searchPosts">검색</button>
    </div>

    <!-- 카테고리 -->
    <div class="mb-3">
      <button v-for="cat in categories" :key="cat"
              class="btn btn-sm me-2"
              :class="selectedCategory === cat ? 'btn-primary' : 'btn-outline-primary'"
              @click="selectedCategory = cat">
        {{ cat }}
      </button>
      <button class="btn btn-success btn-sm float-end" @click="goWrite">글쓰기</button>
    </div>

    <!-- 테이블 -->
    <table class="table table-hover">
      <thead>
      <tr>
        <th>번호</th>
        <th>분류</th>
        <th>제목</th>
        <th>글쓴이</th>
        <th>추천</th>
        <th>조회</th>
        <th>시간</th>
      </tr>
      </thead>
      <tbody>
      <tr v-for="post in filteredPosts" :key="post.id" @click="goDetail(post.id)" style="cursor:pointer">
        <td>{{ post.id }}</td>
        <td>{{ post.category }}</td>
        <td>{{ post.title }}</td>
        <td>{{ post.author }}</td>
        <td>{{ post.likes }}</td>
        <td>{{ post.views }}</td>
        <td>{{ post.date }}</td>
      </tr>
      </tbody>
    </table>

    <!-- 페이지네이션 -->
    <nav>
      <ul class="pagination justify-content-center">
        <li v-for="page in 5" :key="page" class="page-item">
          <button class="page-link">{{ page }}</button>
        </li>
      </ul>
    </nav>

    <!-- Custom Modal (import만 해두고 사용 대기) -->
    <ConfirmModal v-model:isVisible="isModalVisible" :title="modalTitle" :message="modalMessage" :type="modalType" :autoHide="true"/>
  </div>
</template>

<script setup>
import { ref, computed } from "vue"
import { useRouter } from "vue-router"
import ConfirmModal from '../components/ConfirmModal.vue'

const router = useRouter()
const searchQuery = ref("")
const selectedCategory = ref("전체")

// Modal State (사용 대기)
const isModalVisible = ref(false)
const modalTitle = ref('')
const modalMessage = ref('')
const modalType = ref('info')

const categories = ["전체", "미술", "음악", "공예", "정보"]

const posts = ref([
  { id: 1, category: "공지", title: "게시판 공지사항입니다.", author: "관리자", likes: 10, views: 200, date: "2025.09.27" },
  { id: 2, category: "미술", title: "요즘 미술하기 힘드네요...", author: "김작가", likes: 5, views: 120, date: "2025.09.28" }
])

const filteredPosts = computed(() => {
  return posts.value.filter(post => {
    const matchCategory = selectedCategory.value === "전체" || post.category === selectedCategory.value
    const matchSearch = post.title.includes(searchQuery.value)
    return matchCategory && matchSearch
  })
})

const searchPosts = () => {
  console.log("검색어:", searchQuery.value)
}

const goWrite = () => {
  router.push("/board/write")
}

const goDetail = (id) => {
  router.push(`/board/${id}`)
}
</script>
