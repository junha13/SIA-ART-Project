<template>
  <div class="container py-5">
    <ConfirmModal
        v-model:isVisible="isModalVisible"
        :title="modalTitle"
        :message="modalMessage"
        :type="modalType"
        :autoHide="modalAutoHide"
        @confirm="handleModalConfirm"
    />

    <div class="mb-5">
      <div class="d-flex align-items-center mb-4">
        <div class="me-auto">
          <h1 class="fs-4 fw-bolder mb-0 text-dark">예술을 찾는 사람들</h1>
          <p class="text-muted small mb-0 fw-semibold">Welcome, 예술가님! (콘텐츠 강조 버전)</p>
        </div>
        <div class="d-flex gap-2">
          <button class="btn btn-sm btn-light btn-icon rounded-circle" @click="router.push('/mypage')">
            <i class="fas fa-user fs-5"></i>
          </button>
          <button class="btn btn-sm btn-light btn-icon rounded-circle" @click="router.push('/favorite-products')">
            <i class="fas fa-heart fs-5"></i>
          </button>
        </div>
      </div>

      <div class="input-group shadow-md rounded-pill overflow-hidden bg-white mb-2">
        <input type="text" v-model="searchQuery" class="form-control border-0 ps-4" placeholder="작품, 예술인, 클래스를 검색해보세요!" />
        <button class="btn btn-primary btn-icon rounded-circle me-1 my-1 p-0" @click="performSearch" style="width: 38px; height: 38px;">
          <i class="fas fa-search"></i>
        </button>
      </div>

      <div class="d-flex justify-content-start align-items-center mt-3">
        <span class="badge bg-light-dark text-dark fw-bold px-3 py-2 rounded-pill shadow-sm cursor-pointer" @click="router.push('/location-settings')">
            <i class="fas fa-map-marker-alt text-danger me-1"></i> {{ locationStore.currentLocation.name }}
        </span>
      </div>
    </div>


    <div class="mb-6">
      <div class="card bg-primary shadow-lg p-5 rounded-xl cursor-pointer text-white transition-300 hover-lift" @click="router.push('/artworks')">
        <div class="d-flex align-items-center">
          <i class="fas fa-star fs-2 me-3"></i>
          <div class="flex-grow-1">
            <h4 class="fw-bolder mb-0 text-white">금주의 추천 작품</h4>
            <p class="text-light mb-0 small fw-semibold">놓칠 수 없는 단 하나의 예술을 만나보세요!</p>
          </div>
          <i class="fas fa-chevron-right fs-4 ms-auto"></i>
        </div>
      </div>
    </div>


    <div class="mb-6">
      <div class="d-flex justify-content-between align-items-center mb-3">
        <h3 class="fs-5 fw-bolder mb-0 text-dark">🥇 금주의 대표 예술인</h3>
        <button class="btn btn-sm btn-light-secondary fw-bold" @click="router.push('/following-artists')">
          <i class="fas fa-users me-1"></i> 전체 보기
        </button>
      </div>

      <div class="d-flex gap-4 overflow-auto pb-3 artwork-list-scroll">
        <div v-for="artist in topArtists" :key="artist.id" class="text-center" style="min-width: 100px;">
          <div
              class="symbol symbol-80px mx-auto mb-1 rounded-circle border border-warning border-3 cursor-pointer"
              @click="router.push(`/artist/${artist.id}`)"
          >
            <img :src="artist.avatar" :alt="artist.name" class="rounded-circle" />
          </div>
          <h6 class="fw-bold mb-0 text-truncate">{{ artist.name }}</h6>
          <p class="text-muted small mb-0">{{ artist.field }}</p>
        </div>
      </div>
    </div>

    <div class="mb-6">
      <div class="d-flex justify-content-between align-items-center mb-3">
        <h3 class="fs-5 fw-bolder mb-0 text-dark">✨ 새로운 트렌드 작품</h3>
        <button class="btn btn-sm btn-light-secondary fw-bold" @click="router.push('/artworks')">
          <i class="fas fa-clock me-1"></i> 최신순
        </button>
      </div>

      <div class="d-flex gap-4 overflow-auto pb-3 artwork-list-scroll">
        <div v-for="artwork in latestArtworks" :key="artwork.id" style="min-width: 160px;">
          <div
              class="card shadow-sm cursor-pointer rounded-lg overflow-hidden transition-300 hover-lift"
              @click="router.push(`/artwork/${artwork.id}`)"
          >
            <img :src="artwork.image" class="card-img-top" :alt="artwork.title" style="height: 180px; object-fit: cover;"/>
            <div class="card-body p-3">
              <h6 class="fw-bolder mb-1 text-truncate fs-6">{{ artwork.title }}</h6>
              <span class="badge bg-light-primary text-primary fw-bold mb-1">{{ artwork.price.toLocaleString() }}원</span>
              <p class="text-muted small mb-0 mt-1">by {{ artwork.artist }}</p>
            </div>
          </div>
        </div>
      </div>
    </div>


    <div class="mb-6">
      <div class="row g-4">
        <div class="col-12 col-md-6">
          <div
              class="card bg-light-warning shadow-sm cursor-pointer h-100 p-5 rounded-lg transition-300 hover-lift"
              @click="router.push('/board')"
          >
            <div class="d-flex align-items-center">
              <i class="fas fa-comments fs-2 text-warning me-3"></i>
              <div>
                <h4 class="fw-bolder mb-0 text-dark">자유 게시판</h4>
                <p class="text-muted mb-0 small">작가와 대중의 소통 공간</p>
              </div>
              <i class="fas fa-chevron-right fs-4 text-warning ms-auto"></i>
            </div>
          </div>
        </div>
        <div class="col-12 col-md-6">
          <div
              class="card bg-light-info shadow-sm cursor-pointer h-100 p-5 rounded-lg transition-300 hover-lift"
              @click="router.push('/classes')"
          >
            <div class="d-flex align-items-center">
              <i class="fas fa-chalkboard-teacher fs-2 text-info me-3"></i>
              <div>
                <h4 class="fw-bolder mb-0 text-dark">예술 클래스</h4>
                <p class="text-muted mb-0 small">숨겨진 재능을 발견하세요</p>
              </div>
              <i class="fas fa-chevron-right fs-4 text-info ms-auto"></i>
            </div>
          </div>
        </div>
      </div>
    </div>

    <button
        class="btn btn-primary btn-lg btn-icon shadow-lg position-fixed bottom-0 end-0 m-5 rounded-circle transition-300"
        style="width: 60px; height: 60px; font-size: 1.5rem; z-index: 1000; bottom: 90px;"
        @click="router.push('/product/register')"
    >
      <i class="fas fa-plus"></i>
    </button>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import { useRouter } from 'vue-router'
import ConfirmModal from '../components/ConfirmModal.vue'
import { useLocationStore } from '@/stores/useLocationStore' // Pinia Store Import

const router = useRouter()
const locationStore = useLocationStore() // 스토어 사용
const searchQuery = ref('')
// const currentLocation = ref('구로구 구로동') // 제거됨

// Modal State 추가
const isModalVisible = ref(false)
const modalTitle = ref('')
const modalMessage = ref('')
const modalType = ref('info')
const modalAutoHide = ref(false)
const modalAction = ref(null)

const showModal = (title, message, type = 'info', action = null, autoHide = false) => {
  modalTitle.value = title
  modalMessage.value = message
  modalType.value = type
  modalAction.value = action
  modalAutoHide.value = autoHide
  isModalVisible.value = true
}


// 더미 데이터
const allArtworks = [
  { id: 1, title: '고요한 아침의 빛', artist: '김준하', location: '구로구', lat: 37.5665, lng: 126.9780, likes: 6, price: 150000, image: 'assets/media/avatars/300-11.jpg' },
  { id: 2, title: '추상적 선율', artist: '이예술', location: '강남구', lat: 37.5700, lng: 126.9820, likes: 18, price: 420000, image: 'assets/media/avatars/300-12.jpg' },
  { id: 3, title: '바나나 코미디', artist: '마우리치오', location: '역삼동', lat: 37.5000, lng: 127.0363, likes: 42, price: 990000, image: 'assets/media/avatars/300-13.jpg' },
  { id: 4, title: '새로운 추상', artist: '익명', location: '영등포구', lat: 37.5250, lng: 126.9000, likes: 18, price: 85000, image: 'assets/media/avatars/300-14.jpg' },
  { id: 5, title: '도시의 그림자', artist: '박사진', location: '마포구', lat: 37.5500, lng: 126.9200, likes: 25, price: 250000, image: 'assets/media/avatars/300-15.jpg' },
  { id: 6, title: '흙과 불의 춤', artist: '최도예', location: '종로구', lat: 37.5790, lng: 126.9749, likes: 12, price: 350000, image: 'assets/media/avatars/300-16.jpg' },
]

const topArtists = ref([
  { id: 1, name: '김준하', field: '추상화', avatar: 'assets/media/avatars/300-1.jpg' },
  { id: 2, name: '이예술', field: '설치미술', avatar: 'assets/media/avatars/300-2.jpg' },
  { id: 3, name: '박작가', field: '도예', avatar: 'assets/media/avatars/300-3.jpg' },
])

// 최신 작품 (ID가 높은 순)
const latestArtworks = computed(() => {
  return [...allArtworks].sort((a, b) => b.id - a.id).slice(0, 5);
});

const performSearch = () => {
  if (!searchQuery.value.trim()) {
    showModal('검색 안내', '검색어를 입력해주세요.', 'info', null, true);
    return;
  }

  // 검색어 입력 시, 검색 페이지로 이동 전 Modal로 안내
  showModal(
      '검색 이동',
      `'${searchQuery.value}'에 대한 검색 결과를 전체 작품 및 예술인 목록에서 확인합니다.`,
      'confirm',
      'redirectToArtworks',
      false
  );
}

const handleModalConfirm = () => {
  if (modalAction.value === 'redirectToArtworks') {
    // 확인 시 /artworks 페이지로 이동
    router.push({ path: '/artworks', query: { q: searchQuery.value } });
  }
}
</script>

<style scoped>
.cursor-pointer {
  cursor: pointer;
}
/* 카드 스타일 */
.card-img-top {
  border-bottom-left-radius: 0;
  border-bottom-right-radius: 0;
}
.rounded-xl {
  border-radius: 1rem !important;
}
.card {
  border-radius: 0.75rem !important;
  overflow: hidden;
  transition: transform 0.3s ease;
}
.rounded-pill {
  border-radius: 50rem !important;
}
.transition-300 {
  transition: all 0.3s ease;
}

/* 호버 시 카드 리프트 효과 */
.hover-lift:hover {
  transform: translateY(-5px);
  box-shadow: 0 0.5rem 1rem rgba(0, 0, 0, 0.15) !important;
}

/* 수평 스크롤 */
.artwork-list-scroll {
  white-space: nowrap;
  -webkit-overflow-scrolling: touch;
  overflow-x: auto;
}
.artwork-list-scroll::-webkit-scrollbar {
  display: none;
}
</style>