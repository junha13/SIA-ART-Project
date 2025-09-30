<template>
  <div class="app-content flex-column-fluid">
    <div class="app-container-fluid">
      
      <!-- 상단 헤더: 게시판 뷰와 디자인 통일 (중앙 제목) -->
      <div class="d-flex align-items-center justify-content-between pt-5 pb-3 mb-5 border-bottom px-3">
        
        <!-- 왼쪽 영역: 뒤로 가기 버튼 -->
        <button class="btn btn-icon btn-active-light-primary w-30px h-30px" @click="$router.back()">
          <i class="ki-duotone ki-arrow-left fs-2 text-gray-800"></i>
        </button>
        
        <!-- 중앙 영역: 페이지 제목 -->
        <h1 class="page-heading d-flex flex-column justify-content-center text-dark fw-bold fs-3 m-0 position-absolute start-50 translate-middle-x">
          전체 작품 목록
        </h1>
        
        <!-- 오른쪽 영역: 메뉴 버튼 -->
        <i class="ki-duotone ki-dots-vertical fs-2 text-gray-800" style="cursor: pointer;"></i>
      </div>

      <!-- 메인 콘텐츠 영역 -->
      <div class="px-3">
        
        <!-- 1. Filters (작품, 위치, 카테고리) -->
        <div class="d-flex flex-wrap gap-2 mb-4 overflow-auto flex-nowrap">
          <button v-for="filter in filters" :key="filter"
                  class="btn btn-sm rounded-pill fw-bold text-nowrap"
                  :class="activeFilter === filter ? 'btn-dark' : 'btn-outline-secondary text-muted'"
                  @click="activeFilter = filter">
            {{ filter }}
          </button>
        </div>

        <!-- 2. Search & Sort -->
        <div class="input-group mb-4 shadow-sm rounded-3 overflow-hidden border border-gray-400">
          <input 
            v-model="searchQuery" 
            type="text" 
            class="form-control bg-white text-dark border-0" 
            placeholder="작품명, 작가, 지역 검색" 
          />
          <button class="btn btn-dark" @click="performSearch">
            <i class="ki-duotone ki-magnifier fs-4 text-white"></i>
          </button>
        </div>

        <div class="d-flex justify-content-between align-items-center mb-5">
          <!-- Sort Dropdown: Dark 스타일 텍스트 및 Metronic 폰트 적용 -->
          <select v-model="sortOrder" class="form-select form-select-sm w-auto fw-bold text-dark border border-gray-400">
            <option value="latest">최신 등록순</option>
            <option value="price_asc">낮은 가격순</option>
            <option value="price_desc">높은 가격순</option>
            <option value="likes">인기순</option>
          </select>
          <!-- Item Count -->
          <span class="text-gray-600 small fw-bold">총 {{ filteredArtworks.length }}개 작품</span>
        </div>

        <!-- 3. Artwork Cards (⭐ 목록형 리스트 뷰로 변경) -->
        <div v-if="filteredArtworks.length > 0" class="list-group list-group-flush g-3 pb-5">
          <div v-for="artwork in filteredArtworks" :key="artwork.id" 
               class="list-group-item card-flush p-0 mb-4"
               @click="router.push(`/artwork/${artwork.id}`)">

            <div class="d-flex shadow-sm rounded-lg overflow-hidden border border-gray-200 cursor-pointer transition-300">
                
                <!-- 왼쪽: 이미지 (200px 고정) -->
                <div class="position-relative w-150px w-sm-200px flex-shrink-0">
                    <img :src="artwork.image" class="h-100 object-cover" :alt="artwork.title"/>
                    <!-- ⭐ 좋아요 뱃지: Dark 배경 -->
                    <span class="badge bg-dark position-absolute top-0 end-0 m-2 p-2 fw-bolder">
                        <i class="ki-duotone ki-heart fs-6 text-danger me-1"></i> {{ artwork.likes }}
                    </span>
                </div>

                <!-- 오른쪽: 정보 영역 -->
                <div class="p-4 flex-grow-1 d-flex flex-column justify-content-between">
                    
                    <div>
                        <!-- 제품명 (제목) -->
                        <h5 class="fw-bolder mb-2 text-gray-900 text-truncate">제품명: {{ artwork.title }}</h5>
                        
                        <!-- 가격 -->
                        <div class="mb-2">
                            <span class="text-gray-700 fw-semibold">가격: </span>
                            <span class="text-dark fw-bolder fs-6">{{ artwork.price.toLocaleString() }}원</span>
                        </div>
                        
                        <!-- 작가 및 위치 -->
                        <p class="text-muted small mb-1">
                            by {{ artwork.artist }}
                        </p>
                    </div>

                    <!-- 위치 정보 (하단에 배치) -->
                    <small class="text-gray-600 text-truncate mt-2">
                        <i class="ki-duotone ki-geolocation fs-7 me-1 text-primary"></i> {{ artwork.location }}
                    </small>
                </div>

            </div>
          </div>
        </div>

        <!-- Empty State -->
        <div v-else class="text-center py-10">
          <i class="ki-duotone ki-box-2 fs-1 text-muted mb-3"></i>
          <p class="text-muted">검색 결과에 해당하는 작품이 없습니다.</p>
        </div>
        
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

// 더미 데이터 (기존 데이터 유지)
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
      case 'latest':
      default:
        // 정렬 기준이 latest (ID 내림차순)와 likes (좋아요 내림차순)가 혼재되어 있으나,
        // 기본적으로 최신순(ID 내림차순)을 유지합니다.
        return b.id - a.id; 
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
/* 상단 헤더의 제목 중앙 정렬 및 배경 설정 */
.d-flex.align-items-center.justify-content-between {
    position: relative; /* 중앙 정렬 제목의 기준점 */
}
.page-heading.position-absolute {
    z-index: 10;
    max-width: 70%; 
    text-align: center;
}

/* ⭐ 목록형 레이아웃을 위한 이미지 크기 및 오브젝트 핏 설정 */
.object-cover {
    object-fit: cover;
}
.w-150px {
    width: 150px;
}
@media (min-width: 576px) {
  .w-sm-200px {
    width: 200px;
  }
}
.h-100 {
    height: 100%;
}

/* ⭐ 기존 디자인 통일 CSS */
/* 카테고리 버튼의 회색 테두리 색상 보정 (게시판 목록과 통일) */
.btn-outline-secondary {
    border-color: #d1d1d1 !important;
    color: var(--bs-gray-700) !important;
}
/* Dark 버튼 스타일 */
.btn-dark {
    background-color: var(--bs-dark) !important;
    border-color: var(--bs-dark) !important;
    color: #fff !important;
}

/* 카드 디자인 */
.cursor-pointer {
  cursor: pointer;
}
.rounded-lg {
  border-radius: 0.75rem !important;
}
.rounded-3 {
  border-radius: 0.5rem !important;
}
.transition-300 {
  transition: transform 0.3s ease, box-shadow 0.3s ease;
}
.transition-300:hover {
  transform: translateY(-3px);
  box-shadow: 0 0.5rem 1rem rgba(0, 0, 0, 0.15) !important;
}
.form-select-sm {
  padding-top: 0.4rem;
  padding-bottom: 0.4rem;
}
/* 검색 입력 필드 대비 강조 */
.form-control.bg-white {
    background-color: #fff !important;
    color: var(--bs-dark) !important;
}
</style>