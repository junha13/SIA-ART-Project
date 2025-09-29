<template>
  <div class="container py-5">
    <!-- Header -->
    <div class="d-flex align-items-center mb-4">
      <button class="btn btn-light btn-sm me-3" @click="$router.back()">
        <i class="fas fa-chevron-left"></i>
      </button>
      <h1 class="fs-3 fw-bold mb-0">전체 작품 목록</h1>
    </div>

    <!-- 1. Filters (작품, 위치, 카테고리) -->
    <div class="d-flex flex-wrap gap-2 mb-4">
      <button v-for="filter in filters" :key="filter"
              class="btn btn-sm rounded-pill fw-bold"
              :class="activeFilter === filter ? 'btn-dark' : 'btn-light-dark text-muted'"
              @click="activeFilter = filter">
        {{ filter }}
      </button>
    </div>

    <!-- 2. Search & Sort -->
    <div class="input-group mb-4 shadow-sm rounded-3 overflow-hidden">
      <input v-model="searchQuery" type="text" class="form-control form-control-solid border-0" placeholder="작품명, 작가, 지역 검색" />
      <button class="btn btn-primary" @click="performSearch">
        <i class="fas fa-search"></i>
      </button>
    </div>

    <div class="d-flex justify-content-between align-items-center mb-4">
      <!-- Sort Dropdown -->
      <select v-model="sortOrder" class="form-select form-select-sm w-auto">
        <option value="latest">최신 등록순</option>
        <option value="price_asc">낮은 가격순</option>
        <option value="price_desc">높은 가격순</option>
        <option value="likes">인기순</option>
      </select>
      <!-- Item Count -->
      <span class="text-muted small fw-bold">총 {{ filteredArtworks.length }}개</span>
    </div>

    <!-- 3. Artwork Cards (시안 Group 304.png 스타일 참조) -->
    <div v-if="filteredArtworks.length > 0" class="row g-4">
      <div v-for="artwork in filteredArtworks" :key="artwork.id" class="col-6 col-md-4">
        <div
            class="card h-100 shadow-sm cursor-pointer rounded-lg overflow-hidden transition-300"
            @click="router.push(`/artwork/${artwork.id}`)"
        >
          <!-- 작품 이미지 -->
          <div class="position-relative">
            <img :src="artwork.image" class="card-img-top" :alt="artwork.title" style="height: 180px; object-fit: cover;"/>
            <!-- 좋아요 버튼 오버레이 -->
            <span class="badge bg-danger position-absolute bottom-0 end-0 m-2 p-2">
                <i class="fas fa-heart me-1"></i> {{ artwork.likes }}
            </span>
          </div>
          <div class="card-body p-3">
            <h6 class="fw-bolder mb-1 text-truncate">{{ artwork.title }}</h6>
            <p class="text-muted small mb-1">by {{ artwork.artist }}</p>

            <!-- 가격 및 위치 -->
            <div class="d-flex justify-content-between align-items-center">
              <span class="text-primary fw-bold fs-7">{{ artwork.price.toLocaleString() }}원</span>
              <small class="text-muted text-truncate">
                <i class="fas fa-map-marker-alt me-1 fs-8"></i>{{ artwork.location }}
              </small>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- Empty State -->
    <div v-else class="text-center py-10">
      <i class="fas fa-box-open fs-1 text-muted mb-3"></i>
      <p class="text-muted">검색 결과에 해당하는 작품이 없습니다.</p>
    </div>

    <!-- Custom Modal -->
    <ConfirmModal
        v-model:isVisible="isModalVisible"
        :title="modalTitle"
        :message="modalMessage"
        :type="modalType"
        :autoHide="true"
    />
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import { useRouter } from 'vue-router'
import ConfirmModal from '../components/ConfirmModal.vue'

const router = useRouter()

// Modal State
const isModalVisible = ref(false)
const modalTitle = ref('')
const modalMessage = ref('')
const modalType = ref('info')

const showModal = (title, message, type = 'info') => {
  modalTitle.value = title
  modalMessage.value = message
  modalType.value = type
  isModalVisible.value = true
}

// 필터 및 검색
const filters = ['전체', '회화', '조각', '도예', '사진', '공예']
const activeFilter = ref('전체')
const searchQuery = ref('')
const sortOrder = ref('latest')

// 더미 데이터 (MainView에서 가져온 데이터 확장)
const allArtworks = ref([
  { id: 1, title: '고요한 아침의 빛', artist: '김준하', location: '구로구', price: 150000, likes: 6, category: '회화', image: 'https://placehold.co/200x180/5DADE2/fff?text=Art1' },
  { id: 2, title: '추상적 선율', artist: '이예술', location: '강남구', price: 420000, likes: 18, category: '회화', image: 'https://placehold.co/200x180/A3E4D7/000?text=Art2' },
  { id: 3, title: '바나나 코미디', artist: '익명작가', location: '역삼동', price: 990000, likes: 42, category: '공예', image: 'https://placehold.co/200x180/F8C471/000?text=Comedian' },
  { id: 4, title: '흙의 속삭임', artist: '최도예', location: '영등포구', price: 85000, likes: 12, category: '도예', image: 'https://placehold.co/200x180/EC7063/fff?text=Art4' },
  { id: 5, title: '도시의 그림자', artist: '박사진', location: '마포구', price: 250000, likes: 25, category: '사진', image: 'https://placehold.co/200x180/3699FF/fff?text=Photo' },
  { id: 6, title: '청동의 미소', artist: '정조각', location: '송파구', price: 1200000, likes: 8, category: '조각', image: 'https://placehold.co/200x180/FFA800/fff?text=Sculpture' },
])

const filteredArtworks = computed(() => {
  let result = allArtworks.value;

  // 1. 필터링
  if (activeFilter.value !== '전체') {
    result = result.filter(artwork => artwork.category === activeFilter.value);
  }

  // 2. 검색
  if (searchQuery.value.trim()) {
    const query = searchQuery.value.trim().toLowerCase();
    result = result.filter(artwork =>
        artwork.title.toLowerCase().includes(query) ||
        artwork.artist.toLowerCase().includes(query) ||
        artwork.location.toLowerCase().includes(query)
    );
  }

  // 3. 정렬
  result.sort((a, b) => {
    switch (sortOrder.value) {
      case 'price_asc':
        return a.price - b.price;
      case 'price_desc':
        return b.price - a.price;
      case 'likes':
        return b.likes - a.likes;
      case 'latest':
      default:
        return b.id - a.id; // ID를 최신순으로 가정
    }
  });

  return result;
})

const performSearch = () => {
  if (!searchQuery.value.trim() && activeFilter.value === '전체') {
    showModal('검색 안내', '검색어를 입력하거나 필터를 선택해주세요.', 'info');
  } else {
    showModal('검색 완료', `${filteredArtworks.value.length}개의 작품이 검색되었습니다.`, 'success');
  }
}
</script>

<style scoped>
.cursor-pointer {
  cursor: pointer;
}
.card-img-top {
  border-bottom-left-radius: 0;
  border-bottom-right-radius: 0;
}
.rounded-lg {
  border-radius: 0.75rem !important;
}
.rounded-3 {
  border-radius: 0.5rem !important;
}
.card {
  transition: transform 0.3s ease, box-shadow 0.3s ease;
}
.card:hover {
  transform: translateY(-3px);
  box-shadow: 0 0.5rem 1rem rgba(0, 0, 0, 0.15) !important;
}
.form-select-sm {
  padding-top: 0.4rem;
  padding-bottom: 0.4rem;
}
</style>
