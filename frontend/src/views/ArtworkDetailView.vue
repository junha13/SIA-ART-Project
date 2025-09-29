<template>
  <div class="container py-5">
    <!-- Header -->
    <div class="d-flex align-items-center mb-6">
      <button class="btn btn-light btn-sm me-3" @click="$router.back()">
        <i class="fas fa-chevron-left"></i>
      </button>
      <h1 class="fs-3 fw-bold mb-0">작품 상세</h1>
      <button class="btn btn-light btn-sm ms-auto" @click="shareArtwork">
        <i class="fas fa-share-alt"></i>
      </button>
    </div>

    <!-- Loading -->
    <div v-if="!artwork" class="text-center py-10">
      <div class="spinner-border text-primary mb-3" role="status"></div>
      <p class="text-muted">작품 정보를 불러오는 중...</p>
    </div>

    <!-- Artwork Detail -->
    <div v-else>
      <!-- Image Slider & Favorite -->
      <div class="card mb-6 shadow-sm position-relative overflow-hidden rounded-xl">

        <!-- Image Area -->
        <div class="artwork-image-container" :style="{ backgroundImage: 'url(' + currentImage + ')' }">
          <!-- Navigation buttons for image slider -->
          <button v-if="artwork.images.length > 1" class="btn btn-icon btn-sm btn-light-dark position-absolute start-0 ms-2" @click="prevImage" style="top: 50%; transform: translateY(-50%); z-index: 5;">
            <i class="fas fa-chevron-left"></i>
          </button>
          <button v-if="artwork.images.length > 1" class="btn btn-icon btn-sm btn-light-dark position-absolute end-0 me-2" @click="nextImage" style="top: 50%; transform: translateY(-50%); z-index: 5;">
            <i class="fas fa-chevron-right"></i>
          </button>

          <!-- Image Index Indicator -->
          <div v-if="artwork.images.length > 1" class="position-absolute bottom-0 start-0 w-100 text-center pb-2" style="z-index: 5;">
            <span v-for="(img, index) in artwork.images" :key="index" class="d-inline-block rounded-circle mx-1" :class="index === currentImageIndex ? 'bg-white' : 'bg-secondary opacity-50'" style="width: 8px; height: 8px;"></span>
          </div>
        </div>

        <!-- Favorite Button -->
        <button
            class="btn btn-icon btn-lg position-absolute top-0 end-0 m-3 shadow-lg rounded-circle"
            :class="isFavorite ? 'btn-danger' : 'btn-light'"
            @click="toggleFavorite"
            style="z-index: 10;"
        >
          <i class="fas fa-heart" :class="isFavorite ? 'text-white' : 'text-danger'"></i>
        </button>
      </div>

      <!-- 작품 제목, 가격, 상태 -->
      <div class="px-3 mb-6">
        <div class="d-flex justify-content-between align-items-end mb-2">
          <h1 class="fs-1 fw-bolder mb-0 text-dark">{{ artwork.title }}</h1>
          <p class="fs-3 fw-bold text-primary mb-0">{{ artwork.price.toLocaleString() }}원</p>
        </div>
        <div class="d-flex gap-2 mb-4">
          <span class="badge bg-light-primary text-primary fw-bold">{{ artwork.category }}</span>
          <span class="badge bg-light-success text-success fw-bold">상태: {{ artwork.condition }}</span>
          <span v-if="artwork.isAvailable" class="badge bg-success text-white fw-bold">판매 가능</span>
          <span v-else class="badge bg-danger text-white fw-bold">판매 완료</span>
        </div>

        <!-- 작가 프로필 영역 (클릭 시 작가 페이지 이동) -->
        <div class="d-flex align-items-center p-3 bg-light rounded-lg mb-4 cursor-pointer">
          <div class="symbol symbol-50px me-4" @click="viewArtist(artwork.artistId)">
            <img :src="artwork.artistAvatar" alt="artist avatar" class="rounded-circle" />
          </div>
          <div class="flex-grow-1" @click="viewArtist(artwork.artistId)">
            <p class="text-muted small mb-0">작가</p>
            <h5 class="fw-bolder mb-0 text-dark">{{ artwork.artist }} <i class="fas fa-chevron-right text-muted fs-8 ms-1"></i></h5>
          </div>
          <!-- 팔로우 버튼 추가 -->
          <button class="btn btn-sm fw-bold" :class="isFollowing ? 'btn-light-secondary' : 'btn-primary'" @click="toggleFollow">
            <i :class="isFollowing ? 'fas fa-check' : 'fas fa-plus'" class="me-1"></i> {{ isFollowing ? '팔로잉' : '팔로우' }}
          </button>
        </div>

      </div>

      <!-- 작품 상세 정보 탭 -->
      <div class="card mb-6 shadow-sm">
        <ul class="nav nav-tabs nav-line-tabs nav-bold px-5 pt-3">
          <li class="nav-item">
            <a class="nav-link" :class="{ active: activeTab === 'details' }" href="#" @click.prevent="activeTab = 'details'">작품 상세</a>
          </li>
          <li class="nav-item">
            <a class="nav-link" :class="{ active: activeTab === 'location' }" href="#" @click.prevent="activeTab = 'location'">거래 위치</a>
          </li>
        </ul>
        <div class="card-body p-5">

          <!-- Tab 1: 작품 상세 -->
          <div v-if="activeTab === 'details'">
            <h3 class="fs-5 fw-bolder mb-3">작품 설명</h3>
            <p class="text-muted small mb-6" style="white-space: pre-line;">{{ artwork.description }}</p>

            <h3 class="fs-5 fw-bolder mb-3">작품 정보</h3>
            <div class="row g-3 mb-6">
              <div class="col-6"><div class="p-3 bg-light rounded-3 small"><span class="fw-bold text-dark">크기:</span> {{ artwork.size }}</div></div>
              <div class="col-6"><div class="p-3 bg-light rounded-3 small"><span class="fw-bold text-dark">재료:</span> {{ artwork.material }}</div></div>
              <div class="col-6"><div class="p-3 bg-light rounded-3 small"><span class="fw-bold text-dark">제작년도:</span> {{ artwork.year }}</div></div>
              <div class="col-6"><div class="p-3 bg-light rounded-3 small"><span class="fw-bold text-dark">무게:</span> {{ artwork.weight }}</div></div>
            </div>

            <h3 class="fs-5 fw-bolder mb-3">태그</h3>
            <div class="d-flex flex-wrap gap-2">
              <span v-for="tag in artwork.tags" :key="tag" class="badge bg-secondary text-white fw-bold p-2">#{{ tag }}</span>
            </div>
          </div>

          <!-- Tab 2: 거래 위치 (지도 연동) -->
          <div v-if="activeTab === 'location'">
            <p class="text-muted mb-3"><i class="fas fa-map-marker-alt text-primary me-1"></i> {{ artwork.fullAddress }}</p>
            <div id="artworkLocationMap" class="rounded-3 shadow-sm" style="height: 300px;"></div>
            <div class="text-muted small mt-2">지도는 대략적인 거래 희망 위치를 나타냅니다.</div>
          </div>

        </div>
      </div>


      <!-- 같은 작가의 다른 작품 (Related) -->
      <div class="card mb-6 shadow-sm">
        <div class="card-header border-0">
          <h3 class="card-title fs-5 fw-bold">같은 작가의 다른 작품</h3>
        </div>
        <div class="card-body">
          <div class="row g-4">
            <div
                v-for="related in relatedArtworks"
                :key="related.id"
                class="col-6 cursor-pointer"
                @click="viewArtwork(related.id)"
            >
              <div class="card h-100 shadow-sm">
                <img :src="related.image" class="card-img-top" :alt="related.title" style="height: 120px; object-fit: cover;" />
                <div class="card-body p-3">
                  <h6 class="fw-bold mb-1 text-truncate fs-6">{{ related.title }}</h6>
                  <p class="text-primary fw-bold small mb-0">
                    {{ related.price.toLocaleString() }}원
                  </p>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>

      <!-- Actions -->
      <div class="d-flex gap-3 fixed-bottom-action-bar">
        <button class="btn btn-light-primary btn-lg flex-fill fw-bolder" @click="confirmContact">
          <i class="fas fa-comment me-1"></i> 문의하기
        </button>
        <button class="btn btn-primary btn-lg flex-fill fw-bolder" :disabled="!artwork.isAvailable" @click="confirmPurchase">
          <i class="fas fa-shopping-cart me-1"></i> {{ artwork.isAvailable ? '구매하기' : '판매 완료' }}
        </button>
      </div>
    </div>

    <!-- Custom Modal -->
    <ConfirmModal
        v-model:isVisible="isModalVisible"
        :title="modalTitle"
        :message="modalMessage"
        :type="modalType"
        :autoHide="modalAutoHide"
        :confirmText="modalConfirmText"
        @confirm="handleModalConfirm"
    />
  </div>
</template>

<script setup>
import { ref, computed, onMounted, watch } from 'vue' // watch import 추가
import { useRoute, useRouter } from 'vue-router'
import ConfirmModal from '../components/ConfirmModal.vue'

const route = useRoute()
const router = useRouter()
const artwork = ref(null)

// State
const isFavorite = ref(false)
const isFollowing = ref(false)
const currentImageIndex = ref(0)
const naverMap = ref(null)
const activeTab = ref('details') // 'details', 'location'

// Modal State & Logic
const isModalVisible = ref(false)
const modalTitle = ref('')
const modalMessage = ref('')
const modalType = ref('info')
const modalAutoHide = ref(true)
const modalAction = ref(null)
const modalConfirmText = ref('확인')

const showModal = (title, message, type = 'info', action = null, confirmText = '확인', autoHide = true) => {
  modalTitle.value = title
  modalMessage.value = message
  modalType.value = type
  modalAction.value = action
  modalConfirmText.value = confirmText
  modalAutoHide.value = autoHide
  isModalVisible.value = true
}

const handleModalConfirm = () => {
  if (modalAction.value === 'purchase') {
    router.push('/purchase-history') // 구매 페이지 이동 (더미)
  }
}


// Mock data (더 풍성하게 채움)
const artworkData = {
  1: {
    id: 1,
    title: '고요한 아침의 빛',
    artist: '김준하',
    artistId: 101,
    artistAvatar: 'public/assets/media/icons/duotune/coding/cod003.svg',
    price: 850000,
    category: '회화 (유화)',
    condition: '매우 좋음',
    isAvailable: true,
    year: 2023,
    size: 'F10 (45.5cm x 53.0cm)',
    material: '캔버스, 유화 물감',
    weight: '1.5 kg',
    location: '서울시 구로구 작업실',
    fullAddress: '서울시 구로구 디지털로 30길 28 (에이스테크노타워)',
    description: "새벽 안개가 걷히고 처음 햇살이 비추는 순간의 평온함을 담아낸 유화 작품입니다. 푸른색과 노란색의 조화가 눈에 띄며, 정적인 분위기 속에서 강한 생명력이 느껴집니다.\n\n[작품 관리 상태]\n- 직사광선을 피해 보관하고 있습니다.\n- 보존 상태 최상입니다.\n- 액자는 포함되지 않습니다.",
    images: [
      'https://placehold.co/800x350/5DADE2/fff?text=Artwork+Main+Image',
      'https://placehold.co/800x350/A3E4D7/000?text=Detail+View+1',
      'https://placehold.co/800x350/F8C471/000?text=Detail+View+2'
    ],
    tags: ['유화', '풍경화', '추상', '파란색', '평화']
  },
  3: { // Related Artwork 1
    id: 3,
    title: '푸른 정원',
    artist: '김준하',
    artistId: 101,
    artistAvatar: 'public/assets/media/icons/duotune/coding/cod003.svg',
    price: 180000,
    category: '회화 (수채화)',
    condition: '좋음',
    isAvailable: true,
    year: 2024,
    size: 'A4',
    material: '종이에 수채화',
    weight: '0.5 kg',
    location: '서울시 구로구 작업실',
    fullAddress: '서울시 구로구 디지털로 30길 28 (에이스테크노타워)',
    description: "싱그러운 초여름 정원을 표현한 수채화 작품입니다. 가볍고 밝은 분위기를 원하시는 분께 추천합니다.",
    images: [
      'https://placehold.co/800x350/A3E4D7/000?text=Related+Art+Main',
    ],
    tags: ['수채화', '정원', '초록색', '가벼운']
  },
  4: { // Related Artwork 2
    id: 4,
    title: '도시의 선율',
    artist: '김준하',
    artistId: 101,
    artistAvatar: 'public/assets/media/icons/duotune/coding/cod003.svg',
    price: 220000,
    category: '조각',
    condition: '새것',
    isAvailable: false, // 판매 완료 예시
    year: 2025,
    size: '30cm x 30cm x 50cm',
    material: '스테인리스 스틸',
    weight: '5 kg',
    location: '서울시 강남구',
    fullAddress: '서울시 강남구 역삼동',
    description: "스테인리스 스틸로 만든 추상 조형물. 도시의 리듬과 삭막함을 표현했습니다. (판매 완료)",
    images: [
      'https://placehold.co/800x350/F8C471/000?text=Related+Art+Main',
    ],
    tags: ['조각', '스틸', '현대미술', '도시']
  }
}

const relatedArtworks = computed(() => {
  // 현재 작품 ID와 다른 작품만 필터링하여 관련 작품으로 표시
  return [
    artworkData[3],
    artworkData[4]
  ].filter(art => art && art.id !== artwork.value?.id)
});

// Computed
const currentImage = computed(() => {
  return artwork.value?.images?.[currentImageIndex.value] || 'https://placehold.co/800x350/AAAAAA/fff?text=Loading'
})

// Image Slider Methods
const prevImage = () => {
  currentImageIndex.value = (currentImageIndex.value - 1 + artwork.value.images.length) % artwork.value.images.length;
}
const nextImage = () => {
  currentImageIndex.value = (currentImageIndex.value + 1) % artwork.value.images.length;
}


// Map Handling (setTimeout 제거)
const initNaverMap = () => {
  // Map 탭이 활성화되었을 때만 지도 초기화 시도
  if (activeTab.value !== 'location') return;

  if (!artwork.value || !window.naver || !document.getElementById('artworkLocationMap')) return

  const lat = artwork.value.lat || 37.5665
  const lng = artwork.value.lng || 126.9780

  naverMap.value = new window.naver.maps.Map('artworkLocationMap', {
    center: new window.naver.maps.LatLng(lat, lng),
    zoom: 16
  })
  new window.naver.maps.Marker({
    position: new window.naver.maps.LatLng(lat, lng),
    map: naverMap.value,
    title: artwork.value.title
  })
}

// Data Loading
const loadArtwork = (id) => {
  artwork.value = artworkData[id] || artworkData[1]
  isFavorite.value = false // 초기 관심 상태
  isFollowing.value = false // 초기 팔로우 상태
  currentImageIndex.value = 0 // 이미지 인덱스 초기화
  activeTab.value = 'details' // 탭 초기화
}

// Vue Router의 파라미터가 변경될 때마다 loadArtwork를 호출
watch(() => route.params.id, (newId) => {
  if (newId) {
    loadArtwork(parseInt(newId));
  }
}, { immediate: true });

// Map 탭 활성화 시 지도 초기화 (최초 로딩 시 initNaverMap이 실패했을 경우 대비)
watch(activeTab, (newTab) => {
  if (newTab === 'location' && artwork.value) {
    // Naver SDK가 로드되어 있는지 확인 후 지도 초기화
    if (window.naver) {
      initNaverMap();
    } else {
      window.addEventListener('load', initNaverMap);
    }
  }
});


// Interaction Handlers (Modal 적용)
const toggleFavorite = () => {
  isFavorite.value = !isFavorite.value
  const title = isFavorite.value ? '관심 작품 등록' : '관심 작품 해제'
  const message = isFavorite.value ? '관심 작품 목록에 추가되었습니다.' : '관심 작품 목록에서 제거되었습니다.'
  const type = isFavorite.value ? 'success' : 'info'
  showModal(title, message, type)
}

const toggleFollow = () => {
  isFollowing.value = !isFollowing.value
  const title = isFollowing.value ? '팔로우 완료' : '팔로우 해제'
  const message = isFollowing.value ? `${artwork.value.artist} 작가님의 새 소식을 받아보세요!` : '작가 팔로우가 해제되었습니다.'
  const type = isFollowing.value ? 'success' : 'info'
  showModal(title, message, type)
}

const shareArtwork = () => {
  const url = window.location.href
  if (navigator.clipboard) {
    navigator.clipboard.writeText(url)
    showModal('링크 복사 완료', '작품 링크가 클립보드에 복사되었습니다.', 'success')
  } else {
    showModal('공유 기능 오류', '클립보드 복사 기능을 지원하지 않습니다.', 'error', null, '확인', false)
  }
}

const viewArtist = (id) => {
  router.push(`/artist/${id}`)
}

const viewArtwork = (id) => {
  // Related artwork 클릭 시 페이지 이동 (새로고침 없이 라우팅만 진행)
  router.push({ name: 'artwork-detail', params: { id } });
  // watch(route.params.id)가 변경된 ID를 감지하여 loadArtwork를 실행합니다.
}

const confirmContact = () => {
  showModal('문의하기', '작가와의 1:1 채팅 문의 페이지로 이동하시겠습니까?', 'confirm', null, '이동', false)
}

const confirmPurchase = () => {
  if (!artwork.value.isAvailable) {
    showModal('구매 불가', '이미 판매 완료된 작품입니다.', 'error')
    return
  }
  showModal('구매 확인', `${artwork.value.title} 작품 구매를 진행하시겠습니까?`, 'confirm', 'purchase', '구매하기', false)
}

// onMounted(() => loadArtwork(parseInt(route.params.id) || 1)) // watch가 처리하므로 불필요

</script>

<style scoped>
.cursor-pointer {
  cursor: pointer;
}
.fixed-bottom-action-bar {
  position: fixed;
  bottom: 0;
  left: 0;
  right: 0;
  background-color: #ffffff;
  border-top: 1px solid #f0f0f0;
  padding: 1rem;
  z-index: 900;
  box-shadow: 0 -2px 10px rgba(0, 0, 0, 0.05);
}
.container {
  padding-bottom: 90px; /* Fixed Footer 높이 확보 */
}
.rounded-xl {
  border-radius: 1rem !important;
}
.rounded-lg {
  border-radius: 0.75rem !important;
}
.artwork-image-container {
  height: 350px;
  width: 100%;
  background-size: cover;
  background-position: center;
  position: relative;
  /* 슬라이더 전환 효과 추가 */
  transition: background-image 0.5s ease-in-out;
}
</style>
