<template>
  <div class="bg-white min-vh-100 d-flex flex-column">
    <!-- 상단 앱바 -->
    <header class="sticky-top border-bottom">
      <div class="container d-flex align-items-center justify-content-between py-2">
        <button class="btn btn-link text-dark p-0" @click="$router.back()">
          <i class="fas fa-chevron-left fs-5"></i>
        </button>
        <div class="fw-bold text-dark">예술가 프로필</div>
        <button class="btn btn-link text-dark p-0" @click="showProfileSettings">
          <i class="fas fa-ellipsis-vertical fs-5"></i>
        </button>
      </div>
    </header>

    <!-- 본문 -->
    <main class="container py-4 flex-grow-1">
      <!-- 프로필 카드 -->
      <div class="border border-2 rounded-4 p-3 mb-4">
        <div class="row g-3 align-items-center">
          <div class="col-auto">
            <img src="https://via.placeholder.com/110"
                 alt="artist avatar" class="rounded-circle border" width="110" height="110"/>
          </div>

          <div class="col">
            <div class="fw-bold fs-5 text-dark mb-1">{{ artist.name }}</div>
            <div class="small text-dark-50 mb-2">{{ artist.role }}</div>
            <div class="d-flex gap-3 text-muted small">
              <div class="d-flex align-items-center gap-1"><i class="fas fa-palette"></i><span>작품 29</span></div>
              <div class="d-flex align-items-center gap-1"><i class="fas fa-user-friends"></i><span>팔로워 29</span></div>
              <div class="d-flex align-items-center gap-1"><i class="fas fa-user-plus"></i><span>팔로잉 29</span></div>
            </div>
          </div>

          <!-- 세로 탭 -->
          <div class="col-auto ms-auto">
            <div class="d-flex flex-column gap-2">
              <button class="btn btn-sm rounded-pill"
                      :class="activeTab==='intro' ? 'btn-outline-dark' : 'btn-light border'"
                      @click="activeTab='intro'">자기소개</button>
              <button class="btn btn-sm rounded-pill"
                      :class="activeTab==='portfolio' ? 'btn-dark text-white' : 'btn-light border'"
                      @click="activeTab='portfolio'">포트폴리오</button>
              <button class="btn btn-sm rounded-pill"
                      :class="activeTab==='activity' ? 'btn-secondary text-white' : 'btn-light border'"
                      @click="activeTab='activity'">활동 발자취</button>
            </div>
          </div>
        </div>
      </div>

      <!-- 탭: 자기소개 (전체보기 버튼 없음) -->
      <section v-if="activeTab==='intro'" class="mb-5">
        <div class="border rounded-4 p-3 mb-4">
          <div class="fw-bold small text-dark mb-2">자기소개 |</div>
          <p class="small text-muted mb-0">{{ artist.bio }}</p>
        </div>

        <div class="border rounded-4 p-3">
          <div class="fw-bold small text-dark mb-3">경력</div>
          <div class="border-start ps-3">
            <div v-for="(career, i) in artist.career" :key="i" class="mb-2">
              <div class="d-flex align-items-start">
                <i class="fas fa-circle fa-2xs me-2 mt-1 text-secondary"></i>
                <div class="small">
                  <span class="fw-bold">{{ career.year }}</span>
                  <span class="ms-2">{{ career.desc }}</span>
                </div>
              </div>
            </div>
            <div class="d-flex align-items-start">
              <i class="fas fa-arrow-down me-2 mt-1 text-secondary"></i>
              <span class="small text-muted"></span>
            </div>
          </div>
        </div>
      </section>

      <!-- 탭: 포트폴리오 (전체보기 O) -->
      <section v-else-if="activeTab==='portfolio'" class="mb-5">
        <div class="d-flex justify-content-between align-items-center mb-2">
          <div class="fw-bold small text-dark">포트폴리오 |</div>
          <button class="btn btn-link btn-sm text-dark p-0" @click="viewAllPortfolio">전체보기<span class="ms-1">&gt;</span></button>
        </div>
        <div class="row g-3">
          <div v-for="work in portfolio" :key="work.id" class="col-6">
            <div class="card h-100 shadow-sm">
              <img :src="work.image" class="card-img-top" :alt="work.title">
              <div class="card-body p-2">
                <div class="fw-bold small text-dark text-truncate mb-1">{{ work.title }}</div>
                <div class="small text-muted">by {{ work.artist }}</div>
                <div class="small text-muted">구로구 구로동 12</div>
              </div>
            </div>
          </div>
        </div>
      </section>

      <!-- 탭: 활동 발자취 (클래스/게시글만 전체보기 O) -->
      <section v-else>
        <!-- 클래스 -->
        <div class="mb-4">
          <div class="d-flex justify-content-between align-items-center mb-2">
            <div class="fw-bold small text-dark">클래스 |</div>
            <button class="btn btn-link btn-sm text-dark p-0" @click="viewAllClasses">전체보기<span class="ms-1">&gt;</span></button>
          </div>
          <div v-for="cls in classes" :key="cls.id"
               class="d-flex align-items-center justify-content-between border rounded-4 p-3 mb-3">
            <div class="d-flex align-items-center">
              <img :src="cls.thumbnail || 'https://via.placeholder.com/48'"
                   class="rounded me-3" width="48" height="48" alt="thumb">
              <div>
                <div class="fw-bold text-dark">
                  {{ cls.title }} <small class="text-muted ms-1">{{ cls.badge || '모집 중' }}</small>
                </div>
                <div class="small text-muted">{{ cls.desc }}</div>
              </div>
            </div>
            <span class="badge bg-light text-dark">{{ cls.level }}</span>
          </div>
        </div>

        <!-- 게시글 -->
        <div class="mb-3">
          <div class="d-flex justify-content-between align-items-center mb-2">
            <div class="fw-bold small text-dark">게시글 |</div>
          </div>
          <ul class="list-group rounded-4">
            <li v-for="post in posts" :key="post.id"
                class="list-group-item d-flex justify-content-between align-items-center">
              <span class="text-dark">{{ post.title }}</span>
              <small class="text-muted">{{ post.date }}</small>
            </li>
          </ul>
        </div>

        <!-- 페이지 네비 텍스트 -->
        <div class="d-flex justify-content-between align-items-center small text-muted">
          <span>처음 페이지 &lt; 2 &gt;</span>
          <span>마지막 페이지</span>
        </div>
      </section>

      <!-- 모달 -->
      <ConfirmModal
        v-model:isVisible="isModalVisible"
        :title="modalTitle"
        :message="modalMessage"
        :type="modalType"
        :autoHide="modalAutoHide"
      />
    </main>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import ConfirmModal from '../components/ConfirmModal.vue'

const route = useRoute()
const router = useRouter()
const activeTab = ref('intro')

/* 모달(프로필 메뉴용) */
const isModalVisible = ref(false)
const modalTitle = ref('')
const modalMessage = ref('')
const modalType = ref('info')
const modalAutoHide = ref(true)
const showModal = (title, message, type='info', autoHide=true) => {
  modalTitle.value = title
  modalMessage.value = message
  modalType.value = type
  modalAutoHide.value = autoHide
  isModalVisible.value = true
}

const artist = ref({
  id: 'me',
  name: '김준하',
  role: '예술가님',
  bio: '2조의 팀장이자 자랑이자 버팀목이자 기타등등인 고급인력',
  career: [
    { year: '2001년', desc: '예술적인 걸음마' },
    { year: '2015년', desc: '경력1' },
    { year: '2014년', desc: '경력2' },
    { year: '2016년', desc: '경력3' },
    { year: '2020년', desc: '예술적인 입학' },
    { year: '2025년', desc: '최종팀프로젝트 1등' }
  ]
})

const portfolio = ref([
  { id: 1, title: '어떤 예술작품 이름', artist: '김준하', image: 'https://via.placeholder.com/400x300?text=Artwork+1' },
  { id: 2, title: '어떤 예술작품 이름', artist: '김준하', image: 'https://via.placeholder.com/400x300?text=Artwork+2' },
  { id: 3, title: '어떤 예술작품 이름', artist: '김준하', image: 'https://via.placeholder.com/400x300?text=Artwork+3' },
  { id: 4, title: '어떤 예술작품 이름', artist: '김준하', image: 'https://via.placeholder.com/400x300?text=Artwork+4' }
])

const classes = ref([
  { id: 1, title: '수채화 기초', desc: '처음 시작하는 수채화, 기본기부터 차근차근', level: '입문', badge: '모집 마감' },
  { id: 2, title: '수채화 기초', desc: '처음 시작하는 수채화, 기본기부터 차근차근', level: '입문', badge: '모집 마감' }
])

const posts = ref([
  { id: 1, title: '요즘 미술하기 힘드네요…', date: '2025.09.28' },
  { id: 2, title: '그래도 요즘은 할만하네요', date: '2025.11.28' },
  { id: 3, title: '예술을 찾는 사람들 덕분인가?', date: '2025.11.29' },
  { id: 4, title: '이러니 1등을 하지', date: '2025.12.01' }
])

const artistId = computed(() => route.params.id || artist.value.id || 'me')

/* 전체보기: 포트폴리오/클래스/게시글만 */
const viewAllPortfolio = () => router.push(`/artist/${artistId.value}/portfolio`)
const viewAllClasses   = () => router.push(`/artist/${artistId.value}/classes`)
const viewAllPosts     = () => router.push(`/artist/${artistId.value}/posts`)

/* 상단 메뉴 */
const showProfileSettings = () => {
  showModal('프로필 메뉴', '프로필 편집/공유/신고 등 옵션을 여기에 붙일 수 있어요.', 'info', false)
}
</script>