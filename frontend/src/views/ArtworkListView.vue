<!-- ArtworkListView.vue -->
<template>
  <div class="app-content flex-column-fluid">
    <div class="app-container-fluid">
      <!-- 상단 헤더 -->
      <div class="d-flex align-items-center justify-content-between pt-5 pb-3 mb-5 border-bottom px-3">
        <button class="btn btn-icon btn-active-light-primary w-30px h-30px" @click="$router.back()">
          <i class="ki-duotone ki-arrow-left fs-2 text-gray-800"></i>
        </button>
        <h1 class="page-heading d-flex flex-column justify-content-center text-dark fw-bold fs-3 m-0 position-absolute start-50 translate-middle-x">
          전체 작품 목록
        </h1>
        <!-- 오른쪽: 팔로우/메뉴 드롭다운 -->
        <div class="dropdown">
          <button class="btn btn-icon btn-active-light-primary" data-bs-toggle="dropdown">
            <i class="ki-duotone ki-dots-vertical fs-2 text-gray-800"></i>
          </button>
          <ul class="dropdown-menu dropdown-menu-end">
            <li><a class="dropdown-item" href="#">내 팔로잉</a></li>
            <li><a class="dropdown-item" href="#">즐겨찾기</a></li>
            <li><a class="dropdown-item" href="#">신고하기</a></li>
          </ul>
        </div>
      </div>

      <!-- 메인 콘텐츠 -->
      <div class="px-3">
        <!-- 필터 버튼 -->
        <div class="d-flex flex-wrap gap-2 mb-4 overflow-auto flex-nowrap">
          <button v-for="filter in filters" :key="filter"
                  class="btn btn-sm rounded-pill fw-bold text-nowrap"
                  :class="activeFilter === filter ? 'btn-dark' : 'btn-outline-secondary text-muted'"
                  @click="activeFilter = filter">
            {{ filter }}
          </button>
        </div>

        <!-- 검색바 + 드롭다운 -->
        <div class="d-flex mb-4 gap-2">
          <div class="input-group shadow-sm rounded-3 overflow-hidden border border-gray-400">
            <input v-model="searchQuery" type="text" class="form-control bg-white text-dark border-0" placeholder="작품명, 작가, 지역 검색" />
            <button class="btn btn-dark" @click="performSearch">
              <i class="ki-duotone ki-magnifier fs-4 text-white"></i>
            </button>
          </div>
          <div class="dropdown">
            <button class="btn btn-outline-secondary dropdown-toggle" data-bs-toggle="dropdown">
              검색 옵션
            </button>
            <ul class="dropdown-menu">
              <li><a class="dropdown-item" @click="searchType = 'title'">작품명</a></li>
              <li><a class="dropdown-item" @click="searchType = 'artist'">작가</a></li>
              <li><a class="dropdown-item" @click="searchType = 'location'">지역</a></li>
            </ul>
          </div>
        </div>

        <!-- 정렬 -->
        <div class="d-flex justify-content-between align-items-center mb-5">
          <select v-model="sortOrder" class="form-select form-select-sm w-auto fw-bold text-dark border border-gray-400">
            <option value="latest">최신 등록순</option>
            <option value="price_asc">낮은 가격순</option>
            <option value="price_desc">높은 가격순</option>
            <option value="likes">인기순</option>
          </select>
          <span class="text-gray-600 small fw-bold">총 {{ filteredArtworks.length }}개 작품</span>
        </div>

        <!-- 작품 카드 -->
        <div v-if="filteredArtworks.length > 0" class="list-group list-group-flush g-3 pb-5">
          <div v-for="artwork in filteredArtworks" :key="artwork.id" 
               class="list-group-item card-flush p-0 mb-4"
               @click="goDetail(artwork)">
            <div class="d-flex shadow-sm rounded-lg overflow-hidden border border-gray-200 cursor-pointer transition-300">
              <!-- 이미지 -->
              <div class="position-relative w-150px w-sm-200px flex-shrink-0">
                <img :src="artwork.image" class="h-100 object-cover w-100" :alt="artwork.title"/>
                <span class="badge bg-dark position-absolute top-0 end-0 m-2 p-2 fw-bolder">
                  <i class="ki-duotone ki-heart fs-6 text-danger me-1"></i> {{ artwork.likes }}
                </span>
              </div>
              <!-- 정보 -->
              <div class="p-4 flex-grow-1 d-flex flex-column justify-content-between">
                <div>
                  <h5 class="fw-bolder mb-2 text-gray-900 text-truncate">제품명: {{ artwork.title }}</h5>
                  <div class="mb-2">
                    <span class="text-gray-700 fw-semibold">가격: </span>
                    <span class="text-dark fw-bolder fs-6">{{ artwork.price.toLocaleString() }}원</span>
                  </div>
                  <p class="text-muted small mb-1">by {{ artwork.artist }}</p>
                  <!-- 팔로우 버튼 -->
                  <button class="btn btn-sm btn-outline-primary mt-2" @click.stop="toggleFollow(artwork)">
                    <i class="ki-duotone" :class="artwork.following ? 'ki-check' : 'ki-plus' "></i>
                    {{ artwork.following ? '팔로잉' : '팔로우' }}
                  </button>
                </div>
                <small class="text-gray-600 text-truncate mt-2">
                  <i class="ki-duotone ki-geolocation fs-7 me-1 text-primary"></i> {{ artwork.location }}
                </small>
              </div>
            </div>
          </div>
        </div>

        <!-- Empty -->
        <div v-else class="text-center py-10">
          <i class="ki-duotone ki-box-2 fs-1 text-muted mb-3"></i>
          <p class="text-muted">검색 결과에 해당하는 작품이 없습니다.</p>
        </div>
      </div>

      <!-- Modal -->
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
const searchType = ref('title')

// 더미 데이터
const allArtworks = ref([
  { id: 1, title: '놀러가고 싶어요', artist: '주영민', location: '상당구', price: 150000, likes: 6, category: '회화', image: '/assets/media/stock/600x600/img-44.jpg', following: false, subtitle: '느슨한 예술계를 뒤집어놓았다', description: '이건 진짜 엄청난 명작이다.', info: { size: '45cm x 53cm', material: '캔버스 유화물감', year: '2023', weight: '1.5kg' } },
  { id: 2, title: '팝업스토어 가는 사람', artist: '박정훈', location: '곤지암동', price: 420000, likes: 18, category: '회화', image: '/assets/media/stock/600x600/img-62.jpg', following: true, subtitle: '느슨한 예술계를 뒤집어놓았다', description: '이건 진짜 엄청난 명작이다.', info: { size: '45cm x 53cm', material: '캔버스 유화물감', year: '2023', weight: '1.5kg' }  },
  { id: 3, title: '피그마 그만 만질래', artist: '허지서', location: '송도동', price: 990000, likes: 42, category: '공예', image: '/assets/media/stock/600x600/img-37.jpg', following: false, subtitle: '느슨한 예술계를 뒤집어놓았다', description: '이건 진짜 엄청난 명작이다.', info: { size: '45cm x 53cm', material: '캔버스 유화물감', year: '2023', weight: '1.5kg' }  },
  { id: 4, title: '알바하러가는 예원', artist: '고예원', location: '북가좌동', price: 85000, likes: 12, category: '도예', image: '/assets/media/stock/600x600/img-2.jpg', following: false, subtitle: '느슨한 예술계를 뒤집어놓았다', description: '이건 진짜 엄청난 명작이다.', info: { size: '45cm x 53cm', material: '캔버스 유화물감', year: '2023', weight: '1.5kg' }  },
  { id: 5, title: '집에가고 싶은 민호', artist: '박민호', location: '서대문구', price: 250000, likes: 25, category: '사진', image: '/assets/media/stock/600x600/img-54.jpg', following: false, subtitle: '느슨한 예술계를 뒤집어놓았다', description: '이건 진짜 엄청난 명작이다.', info: { size: '45cm x 53cm', material: '캔버스 유화물감', year: '2023', weight: '1.5kg' }  },
  { id: 6, title: '학원가고 싶어요', artist: '김준하', location: '만안구', price: 1200000, likes: 8, category: '조각', image: '/assets/media/stock/600x600/img-43.jpg', following: true, subtitle: '느슨한 예술계를 뒤집어놓았다', description: '이건 진짜 엄청난 명작이다.', info: { size: '45cm x 53cm', material: '캔버스 유화물감', year: '2023', weight: '1.5kg' }  },
])

const filteredArtworks = computed(() => {
  let result = allArtworks.value

  if (activeFilter.value !== '전체') {
    result = result.filter(a => a.category === activeFilter.value)
  }

  if (searchQuery.value.trim()) {
    const query = searchQuery.value.trim().toLowerCase()
    if (searchType.value === 'title') {
      result = result.filter(a => a.title.toLowerCase().includes(query))
    } else if (searchType.value === 'artist') {
      result = result.filter(a => a.artist.toLowerCase().includes(query))
    } else if (searchType.value === 'location') {
      result = result.filter(a => a.location.toLowerCase().includes(query))
    }
  }

  result.sort((a, b) => {
    switch (sortOrder.value) {
      case 'price_asc':
        return a.price - b.price
      case 'price_desc':
        return b.price - a.price
      case 'likes':
        return b.likes - a.likes
      case 'latest':
      default:
        return b.id - a.id
    }
  })

  return result
})

const performSearch = () => {
  if (!searchQuery.value.trim() && activeFilter.value === '전체') {
    showModal('검색 안내', '검색어를 입력하거나 필터를 선택해주세요.', 'info')
  } else {
    showModal('검색 완료', `${filteredArtworks.value.length}개의 작품이 검색되었습니다.`, 'success')
  }
}

const toggleFollow = artwork => {
  artwork.following = !artwork.following
}

const goDetail = (artwork) => {
  // 앱 베이스 경로까지 고려해서 절대 URL 생성 (유틸 없이 한 줄)
  const base = import.meta.env.BASE_URL || '/';
  const absImg = new URL(artwork.image, window.location.origin + base).href;
  const payload = { ...artwork, image: absImg };
  try { sessionStorage.setItem('lastArtwork', JSON.stringify(payload)) } catch {}
  router.push({ path: `/artwork/${artwork.id}`, state: { artwork: payload } })
}
</script>

<style scoped>
.object-cover {
  object-fit: cover;
}
.w-150px {
  width: 150px;
}
@media (min-width: 576px) {
  .w-sm-200px { width: 200px; }
}
.h-100 { height: 100%; }
.cursor-pointer { cursor: pointer; }
.rounded-lg { border-radius: 0.75rem !important; }
.rounded-3 { border-radius: 0.5rem !important; }
.transition-300 { transition: transform 0.3s ease, box-shadow 0.3s ease; }
.transition-300:hover { transform: translateY(-3px); box-shadow: 0 0.5rem 1rem rgba(0, 0, 0, 0.15) !important; }
.form-select-sm { padding-top: 0.4rem; padding-bottom: 0.4rem; }
.form-control.bg-white { background-color: #fff !important; color: var(--bs-dark) !important; }
</style>