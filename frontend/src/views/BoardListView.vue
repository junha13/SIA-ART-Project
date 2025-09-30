<template>
  <div class="app-content flex-column-fluid">
    <div class="app-container-fluid">
      <div class="d-flex align-items-center justify-content-between px-3 pt-5 pb-3 border-bottom mb-5">

        <i class="ki-duotone ki-arrow-left fs-2 text-dark" style="cursor: pointer;" @click="goBack"></i>

        <h1 class="page-heading d-flex align-items-center justify-content-center text-dark fw-bold fs-3 m-0 position-absolute start-50 translate-middle-x">
          게시판
        </h1>

        <i class="ki-duotone ki-dots-vertical fs-2 text-dark" style="cursor: pointer;"></i>
      </div>

      <div class="mb-5 px-3">

        <div class="d-flex justify-content-center mb-5">
          <div class="input-group w-100 mw-500px border border-gray-500 rounded-2">

            <button class="btn btn-secondary dropdown-toggle text-dark fw-bold"
                    type="button"
                    data-bs-toggle="dropdown"
                    aria-expanded="false"
                    style="border-top-left-radius: .475rem; border-bottom-left-radius: .475rem;">
              {{ getSearchFieldName(searchField) }}
            </button>
            <ul class="dropdown-menu">
              <li><a class="dropdown-item" href="#" @click.prevent="searchField = 'title_content'">제목+내용</a></li>
              <li><a class="dropdown-item" href="#" @click.prevent="searchField = 'title'">제목</a></li>
              <li><a class="dropdown-item" href="#" @click.prevent="searchField = 'content'">내용</a></li>
              <li><a class="dropdown-item" href="#" @click.prevent="searchField = 'author'">글쓴이</a></li>
              <li><a class="dropdown-item" href="#" @click.prevent="searchField = 'comment'">댓글</a></li>
            </ul>

            <input type="text"
                   class="form-control border-0 bg-white"
                   placeholder="검색어를 입력하세요"
                   v-model="searchQuery"
                   @keyup.enter="searchPosts"
                   style="height: 40px; border-top-left-radius: 0; border-bottom-left-radius: 0;"/>
          </div>
        </div>

        <div class="d-flex align-items-center justify-content-between mb-5">
          <div class="d-flex overflow-auto flex-nowrap me-3">
            <button v-for="cat in categories" :key="cat"
                    class="btn btn-sm text-nowrap rounded-pill me-2 fw-semibold"
                    :class="selectedCategory === cat ? 'btn-dark text-white' : 'btn-outline-secondary text-gray-700'"
                    @click="selectedCategory = cat"
                    style="border-color: #d1d1d1;">
              {{ cat }}
            </button>
          </div>
          <button class="btn btn-dark btn-sm fw-bold text-nowrap" @click="goWrite">글쓰기</button>
        </div>

        <div class="d-flex align-items-center fw-bold fs-7 text-gray-600 mb-3">
            <span
                class="me-3 cursor-pointer"
                :class="{ 'text-dark': selectedBoardType === '전체글' }"
                @click="selectedBoardType = '전체글'">
                전체글
            </span>
            <span
                class="me-3 cursor-pointer"
                :class="{ 'text-dark': selectedBoardType === '인기글' }"
                @click="selectedBoardType = '인기글'">
                인기글
            </span>
            <span
                class="me-3 cursor-pointer"
                :class="{ 'text-dark': selectedBoardType === '공지사항' }"
                @click="selectedBoardType = '공지사항'">
                공지사항
            </span>
        </div>

        <div class="table-responsive">
          <table class="table table-row-dashed table-row-gray-300 align-middle gs-0 gy-4">
            <thead>
            <tr class="fw-normal fs-8 text-gray-500 border-bottom border-gray-200">
              <th class="w-50px text-start cursor-pointer" @click="sortBy('id')">
                번호
                <i v-if="sortColumn === 'id'" :class="sortDirection === 'asc' ? 'ki-duotone ki-up fs-7' : 'ki-duotone ki-down fs-7'"></i>
              </th>
              <th class="w-100px text-start">분류</th>
              <th class="min-w-250px text-start">제목</th>
              <th class="w-100px text-start cursor-pointer" @click="sortBy('author')">글쓴이</th>
              <th class="w-70px text-end cursor-pointer" @click="sortBy('likes')">
                추천
                <i v-if="sortColumn === 'likes'" :class="sortDirection === 'asc' ? 'ki-duotone ki-up fs-7' : 'ki-duotone ki-down fs-7'"></i>
              </th>
              <th class="w-70px text-end cursor-pointer" @click="sortBy('views')">
                조회
                <i v-if="sortColumn === 'views'" :class="sortDirection === 'asc' ? 'ki-duotone ki-up fs-7' : 'ki-duotone ki-down fs-7'"></i>
              </th>
              <th class="w-125px text-start cursor-pointer" @click="sortBy('date')">
                시간
                <i v-if="sortColumn === 'date'" :class="sortDirection === 'asc' ? 'ki-duotone ki-up fs-7' : 'ki-duotone ki-down fs-7'"></i>
              </th>
            </tr>
            </thead>
            <tbody>
            <tr v-for="post in sortedAndFilteredPosts" :key="post.id" @click="goDetail(post.id)" style="cursor:pointer"
                :class="{ 'bg-hover-light-primary': post.category === '공지' }">
              <td class="text-start"><span class="text-gray-600 fw-semibold d-block">{{ post.id }}</span></td>
              <td class="text-start">
                <span class="fw-bold">{{ post.category }}</span>
              </td>
              <td class="text-start">
                <a href="#" class="text-gray-800 fw-bold text-hover-primary fs-6 me-2">
                  {{ post.title }}
                </a>
              </td>
              <td class="text-start"><span class="text-gray-600 fw-semibold d-block fs-7">{{ post.author }}</span></td>
              <td class="text-end">
                <span class="text-gray-600 fw-semibold d-block fs-7">
                  <i class="ki-duotone ki-heart fs-7 me-1 text-danger"></i>
                  {{ post.likes }}
                </span>
              </td>
              <td class="text-end">
                <span class="text-gray-600 fw-semibold d-block fs-7">
                  <i class="ki-duotone ki-eye fs-7 me-1 text-info"></i>
                  {{ post.views }}
                </span>
              </td>
              <td class="text-start"><span class="text-gray-600 fw-semibold d-block fs-7">{{ post.date }}</span></td>
            </tr>
            </tbody>
          </table>
        </div>
      </div>

      <div class="card-footer pt-0 px-3">
        <nav>
          <ul class="pagination justify-content-center">
            <li class="page-item previous disabled">
              <a href="#" class="page-link" style="border-radius: 50%; width: 30px; height: 30px; padding: 0; line-height: 28px; text-align: center;"><i class="ki-duotone ki-left fs-5"></i></a>
            </li>
            <li v-for="page in 5" :key="page" class="page-item" :class="{ 'active': page === 1 }">
              <button class="page-link rounded-circle fw-bold mx-1" style="width: 30px; height: 30px; padding: 0; line-height: 28px; text-align: center;">{{ page }}</button>
            </li>
            <li class="page-item next">
              <a href="#" class="page-link" style="border-radius: 50%; width: 30px; height: 30px; padding: 0; line-height: 28px; text-align: center;"><i class="ki-duotone ki-right fs-5"></i></a>
            </li>
          </ul>
        </nav>
      </div>

      <ConfirmModal v-model:isVisible="isModalVisible" :title="modalTitle" :message="modalMessage" :type="modalType" :autoHide="true"/>
    </div>
  </div>
</template>

<script setup>
import { ref, computed } from "vue"
import { useRouter } from "vue-router"
import ConfirmModal from '../components/ConfirmModal.vue'

const router = useRouter()
const searchQuery = ref("")
const selectedCategory = ref("전체")
const selectedBoardType = ref("전체글")
const searchField = ref("title_content")

const sortColumn = ref("id")
const sortDirection = ref("desc")

const isModalVisible = ref(false)
const modalTitle = ref('')
const modalMessage = ref('')
const modalType = ref('info')

const categories = ["전체", "미술", "음악", "공예", "정보"]

const posts = ref([
  { id: 1, category: "공지", title: "게시판 규정 관련 공지사항입니다.[12]", content: "게시판 이용에 관한 주요 규정입니다. 모두 필독해주세요.", comments: "댓글 내용 1", author: "관리자", likes: 15, views: 200, date: "2025.09.27" },
  { id: 2, category: "미술", title: "미술 빡x 요즘 미술하기 힘드네요...", content: "아이디어가 고갈되어서 힘듭니다.", comments: "아이디어가 좋네요", author: "김춘화", likes: 5, views: 120, date: "2025.09.28" },
  { id: 3, category: "음악", title: "인기글 예시 - 조회수 높음", content: "최근 발표된 새로운 음악 트렌드 분석입니다.", comments: "노래 좋아요", author: "음악가", likes: 50, views: 500, date: "2025.09.29" },
  { id: 4, category: "정보", title: "일반 정보글 예시", content: "유용한 웹사이트 정보를 공유합니다.", comments: "새로운 정보 감사합니다", author: "정보통", likes: 2, views: 50, date: "2025.09.20" }
])

const filteredPosts = computed(() => {
  let filtered = posts.value.filter(post => {
    const matchCategory = selectedCategory.value === "전체" || post.category === selectedCategory.value

    const query = searchQuery.value.toLowerCase()
    let matchSearch = true

    if (query) {
      if (searchField.value === 'title') {
        matchSearch = post.title.toLowerCase().includes(query)
      } else if (searchField.value === 'content') {
        matchSearch = post.content.toLowerCase().includes(query)
      } else if (searchField.value === 'author') {
        matchSearch = post.author.toLowerCase().includes(query)
      } else if (searchField.value === 'title_content') {
        matchSearch = post.title.toLowerCase().includes(query) || post.content.toLowerCase().includes(query)
      } else if (searchField.value === 'comment') {
        matchSearch = post.comments.toLowerCase().includes(query)
      }
    }

    return matchCategory && matchSearch
  })

  if (selectedBoardType.value === '인기글') {
    filtered = filtered.filter(post => post.likes >= 10 || post.views >= 100)
  } else if (selectedBoardType.value === '공지사항') {
    filtered = filtered.filter(post => post.category === '공지')
  }

  return filtered
})

const sortedAndFilteredPosts = computed(() => {
    const list = [...filteredPosts.value]

    list.sort((a, b) => {
        const valA = a[sortColumn.value]
        const valB = b[sortColumn.value]

        let comparison = 0
        if (valA > valB) comparison = 1
        else if (valA < valB) comparison = -1

        return sortDirection.value === 'asc' ? comparison : comparison * -1
    })

    return list
})

const sortBy = (column) => {
    if (sortColumn.value === column) {
        sortDirection.value = sortDirection.value === 'asc' ? 'desc' : 'asc'
    } else {
        sortColumn.value = column
        sortDirection.value = 'desc'
    }
}

const getSearchFieldName = (field) => {
    const names = {
        title: '제목',
        content: '내용',
        author: '글쓴이',
        title_content: '제목+내용',
        comment: '댓글'
    }
    return names[field] || '제목+내용'
}

const searchPosts = () => {
  console.log("검색 필드:", getSearchFieldName(searchField.value), "검색어:", searchQuery.value)
}

const goWrite = () => {
  router.push("/board/write")
}

const goDetail = (id) => {
  router.push(`/board/${id}`)
}

const goBack = () => {
  router.back()
}
</script>

<style scoped>
.cursor-pointer {
    cursor: pointer;
}

/* 상단 헤더의 제목 중앙 정렬을 위한 설정 */
.d-flex.align-items-center.justify-content-between.px-3 {
    position: relative; /* 자식 요소의 absolute 위치 기준 */
}
.page-heading.position-absolute {
    /* position: absolute; top: 50%; left: 50%; transform: translate(-50%, -50%); */
    z-index: 10; /* 버튼 위에 표시되도록 z-index 설정 */
}

.input-group .btn.dropdown-toggle.text-dark {
    color: var(--bs-dark) !important;
    background-color: var(--bs-gray-200);
    border-color: var(--bs-gray-500) !important;
}

.btn-outline-secondary {
    border-color: #d1d1d1 !important;
    color: var(--bs-gray-700) !important;
}

.btn-dark, .pagination .page-item.active .page-link {
    background-color: var(--bs-dark) !important;
    border-color: var(--bs-dark) !important;
    color: #fff !important;
}

.pagination .page-item .page-link {
    border-radius: 50% !important;
    display: flex;
    justify-content: center;
    align-items: center;
    margin: 0 4px;
    color: var(--bs-gray-700);
}

.table tbody tr[style*="cursor:pointer"]:hover {
  background-color: var(--bs-light) !important;
}

.table tbody tr.bg-hover-light-primary:hover {
    background-color: var(--bs-light-primary) !important;
}
</style>