<template>
  <div class="bg-white min-vh-100 d-flex flex-column">

    <main class="mt-20 container py-4 flex-grow-1">
      <div class="text-center fw-bold text-dark mb-3">예술가 프로필</div>
      <section class="border border-2 rounded-4 p-3 mb-4">
        <div class="row g-3 align-items-center">
          <div class="col-auto">
            <img :src="artist.profileImage"
                 alt="artist avatar" class="rounded-circle border" width="110" height="110"/>
          </div>

          <div class="col">
            <div class="fw-bold fs-5 text-dark mb-1">{{ artist.name }}</div>
            <div class="small text-dark-50 mb-2">{{ artist.role }}</div>
            <div class="d-flex gap-3 text-muted small">
              <div class="d-flex align-items-center gap-1"><i class="fas fa-palette"></i><span>작품 {{ filteredPortfolio.length }}</span></div>
              <div class="d-flex align-items-center gap-1 cursor-pointer" @click="router.push('/following-artists')"><i class="fas fa-user-friends"></i><span>팔로워 {{ artist.followers }}</span></div>
              <div class="d-flex align-items-center gap-1 cursor-pointer" @click="router.push('/following-artists')"><i class="fas fa-user-plus"></i><span>팔로잉 {{ artist.following }}</span></div>
            </div>
          </div>

          <div class="col-auto ms-auto">
            <div class="d-flex flex-column gap-2">
              <button class="btn btn-sm rounded-pill"
                      :class="activeTab==='intro' ? 'btn-dark text-white' : 'btn-light border'"
                      @click="activeTab='intro'">자기소개</button>
              <button class="btn btn-sm rounded-pill"
                      :class="activeTab==='portfolio' ? 'btn-dark text-white' : 'btn-light border'"
                      @click="activeTab='portfolio'">포트폴리오</button>
              <button class="btn btn-sm rounded-pill"
                      :class="activeTab==='activity' ? 'btn-dark text-white' : 'btn-light border'"
                      @click="activeTab='activity'">활동 발자취</button>
            </div>
          </div>
        </div>
      </section>

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

      <section v-else-if="activeTab==='portfolio'" class="mb-5">
        <div class="d-flex justify-content-between align-items-center mb-2">
          <div class="fw-bold small text-dark">포트폴리오 |</div>
          <button class="btn btn-link btn-sm text-dark p-0" @click="viewAllPortfolio">전체보기<span class="ms-1">&gt;</span></button>
        </div>
        <div class="row g-3">
          <div v-for="work in filteredPortfolio" :key="work.id" class="col-6">
            <div class="card h-100 shadow-sm cursor-pointer" @click="router.push(`/artwork/${work.id}`)">
              <img :src="work.image" class="card-img-top" :alt="work.title">
              <div class="card-body p-2">
                <div class="fw-bold small text-dark text-truncate mb-1">{{ work.title }}</div>
                <div class="small text-muted">by {{ work.artist }}</div>
                <div class="small text-muted">{{ work.location }}</div>
              </div>
            </div>
          </div>
        </div>
      </section>

      <section v-else>
        <div class="mb-4">
          <div class="d-flex justify-content-between align-items-center mb-2">
            <div class="fw-bold small text-dark">클래스 |</div>
            <button class="btn btn-link btn-sm text-dark p-0" @click="viewAllClasses">전체보기<span class="ms-1">&gt;</span></button>
          </div>
          <div v-for="cls in filteredClasses" :key="cls.id"
               class="d-flex align-items-center justify-content-between border rounded-4 p-3 mb-3">
            <div class="d-flex align-items-center">
              <img :src="cls.instructorAvatar || 'https://via.placeholder.com/48'"
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

        <div class="mb-3">
          <div class="d-flex justify-content-between align-items-center mb-2">
            <div class="fw-bold small text-dark">게시글 |</div>
            <button class="btn btn-link btn-sm text-dark p-0" @click="viewAllPosts">전체보기<span class="ms-1">&gt;</span></button>
          </div>
          <ul class="list-group rounded-4">
            <li v-for="post in filteredPosts" :key="post.id"
                class="list-group-item d-flex justify-content-between align-items-center" @click="router.push(`/board/${post.id}`)">
              <span class="text-dark">{{ post.title }}</span>
              <small class="text-muted">{{ post.date }}</small>
            </li>
          </ul>
        </div>

        <div class="d-flex justify-content-between align-items-center small text-muted">
          <span>처음 페이지 &lt; 2 &gt;</span>
          <span>마지막 페이지</span>
        </div>
      </section>

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
import { ref, computed, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import ConfirmModal from '../components/ConfirmModal.vue'
import { MOCK_ARTISTS, MOCK_ARTWORKS, MOCK_CLASSES, MOCK_POSTS } from '@/data/MockData.js'

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

// ⭐ 1. 아티스트 ID 설정 (라우트 파라미터가 없으면 101(김준하)을 기본값으로 사용)
const artistId = computed(() => parseInt(route.params.id) || 101)

// ⭐ 2. 아티스트 정보 로드
const artist = computed(() => {
  // ID가 일치하는 아티스트를 찾고, 없으면 첫 번째(김준하)를 반환
  return MOCK_ARTISTS.find(a => a.id === artistId.value) || MOCK_ARTISTS[0]
})

// ⭐ 3. 필터링된 포트폴리오 (작품)
const filteredPortfolio = computed(() => {
  return MOCK_ARTWORKS
      .filter(w => w.artistId === artistId.value)
      .slice(0, 4) // 4개만 표시
})

// ⭐ 4. 필터링된 클래스
const filteredClasses = computed(() => {
  return MOCK_CLASSES
      .filter(c => c.instructorId === artistId.value)
      .slice(0, 2) // 2개만 표시
      .map(cls => ({
        ...cls,
        desc: cls.subtitle,
        level: cls.level,
        badge: cls.level === '입문' ? '모집 중' : '모집 마감',
        thumbnail: cls.instructorAvatar
      }))
})

// ⭐ 5. 필터링된 게시글
const filteredPosts = computed(() => {
  return MOCK_POSTS
      .filter(p => p.authorId === artistId.value)
      .slice(0, 4) // 4개만 표시
})


/* 전체보기: 포트폴리오/클래스/게시글 라우팅 설정 */
const viewAllPortfolio = () => router.push(`/artwork/list?artistId=${artistId.value}`)
const viewAllClasses   = () => router.push(`/class/list?artistId=${artistId.value}`)
const viewAllPosts     = () => router.push(`/board?authorId=${artistId.value}`)

/* 상단 메뉴 */
const showProfileSettings = () => {
  showModal('프로필 메뉴', '프로필 편집/공유/신고 등 옵션을 여기에 붙일 수 있어요.', 'info', false)
}
</script>