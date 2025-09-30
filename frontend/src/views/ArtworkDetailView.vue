<template>
  <div class="app-content flex-column-fluid">
    <div class="app-container-fluid">
      
      <!-- 상단 헤더: 통일된 디자인 -->
      <div class="d-flex align-items-center justify-content-between pt-5 pb-3 mb-5 border-bottom px-3">
        
        <button class="btn btn-icon btn-active-light-primary w-30px h-30px" @click="router.back()">
          <i class="ki-duotone ki-arrow-left fs-2 text-gray-800"></i>
        </button>
        
        <h1 class="page-heading d-flex flex-column justify-content-center text-dark fw-bold fs-3 m-0 position-absolute start-50 translate-middle-x">
          작품 상세페이지
        </h1>
        
        <i class="ki-duotone ki-dots-vertical fs-2 text-gray-800" style="cursor: pointer;" @click="shareArtwork"></i>
      </div>

      <div v-if="!artwork" class="text-center py-10">
        <div class="spinner-border text-dark mb-3" role="status"></div>
        <p class="text-muted">작품 정보를 불러오는 중...</p>
      </div>

      <!-- Artwork Detail -->
      <div v-else class="px-3 pb-20"> 
        
        <!-- ⭐ 1. 메인 이미지 + 작품 정보 (가로 분할 및 크기 최적화) -->
        <div class="d-flex mb-6 gap-3 align-items-start">
            
            <!-- 왼쪽: 메인 이미지 (가로 폭 증가 및 높이 증가) -->
            <div class="flex-shrink-0 w-180px w-sm-250px">
                <div class="card shadow-lg rounded-xl overflow-hidden">
                    <img :src="artwork.images[0]" alt="작품 이미지" class="img-fluid" style="width: 100%; height: 240px; object-fit: cover;" />
                </div>
            </div>

            <!-- ⭐ 오른쪽: 작품 정보 (가로 공간 채우기: flex-grow-1 재적용) -->
            <div class="d-flex flex-column flex-grow-1">
                <!-- 분류 뱃지 -->
                <div class="mb-2">
                    <span class="badge bg-dark text-white fw-bold py-2 px-4">{{ artwork.category }}</span>
                </div>

                <!-- 작품 정보 박스 헤더 -->
                <h5 class="fs-6 fw-bold text-dark mb-2">작품 정보</h5>

                <!-- 정보 항목 블록 (수직 나열 및 좌측 정렬) -->
                <div class="d-flex flex-column gap-2 border border-gray-300 rounded-lg p-2">
                    <div v-for="(info, index) in artworkDetails" :key="index"
                         class="p-2 bg-light-secondary rounded-lg small fw-bold text-dark text-truncate border border-gray-300">
                        <!-- ⭐ 텍스트 정렬을 기본(text-start)으로 돌려 가독성 확보 -->
                        <span class="text-gray-700 me-1 d-inline">{{ info.label }}:</span> 
                        <span class="d-inline">{{ info.value }}</span>
                    </div>
                </div>
            </div>
        </div>

        <!-- 2. 제목, 작가, 부제 영역 (이미지 아래 수직 배치) -->
        <div class="mb-6">
          <!-- 제목 -->
          <h1 class="fs-1 fw-bolder mb-1 text-gray-900">{{ artwork.title }}</h1>
          <!-- 부제 (간단 설명) -->
          <p class="text-muted fs-6 mb-3">{{ artwork.shortDescription }}</p>

          <!-- 작가 프로필 영역 (클릭 시 작가 페이지 이동) -->
          <div class="d-flex align-items-center p-3 bg-light rounded-lg cursor-pointer" @click="viewArtist(artwork.artistId)">
            <div class="symbol symbol-35px me-3">
              <img :src="artwork.artistAvatar" alt="artist avatar" class="rounded-circle" />
            </div>
            <div class="flex-grow-1">
              <h5 class="fw-bolder mb-0 text-dark">{{ artwork.artist }} <i class="ki-duotone ki-arrow-right fs-6 text-muted ms-1"></i></h5>
            </div>
            <!-- 팔로우 버튼 (다크 스타일 유지) -->
            <button class="btn btn-sm fw-bold" :class="isFollowing ? 'btn-light-secondary' : 'btn-dark text-white'" @click.stop="toggleFollow">
              <i :class="isFollowing ? 'ki-duotone ki-check' : 'ki-duotone ki-plus'" class="me-1"></i> {{ isFollowing ? '팔로잉' : '팔로우' }}
            </button>
          </div>
        </div>
        
        <!-- 3. 상세 설명 블록 (이미지 참고하여 깔끔한 카드 형태) -->
        <div class="card card-flush shadow-sm mb-6 p-5">
          <h3 class="fs-5 fw-bolder mb-3 text-dark">상세 설명</h3>
          <p class="text-gray-800 small lh-base" style="white-space: pre-line;">{{ artwork.description }}</p>
        </div>

        <!-- 4. 액션 버튼 (고정 하단 바) -->
        <div class="d-flex gap-3 fixed-bottom-action-bar">
          <!-- 관심 작품 버튼 (다크 스타일) -->
          <button 
              class="btn btn-dark btn-lg fw-bolder shadow-lg d-flex align-items-center justify-content-center" 
              :class="{ 'btn-dark-danger': isFavorite }"
              @click="toggleFavorite"
              style="width: 80px;"
          >
            <i class="ki-duotone ki-heart fs-3"></i>
          </button>
          
          <!-- 문의하기 버튼 -->
          <button class="btn btn-light-secondary btn-lg flex-fill fw-bolder d-flex align-items-center justify-content-center" @click="confirmContact">
            <i class="ki-duotone ki-message fs-3 me-2"></i> 문의하기
          </button>
          
          <!-- 구매하기 버튼 (다크 스타일) -->
          <button class="btn btn-dark btn-lg flex-fill fw-bolder d-flex align-items-center justify-content-center" :disabled="!artwork.isAvailable" @click="confirmPurchase">
            <i class="ki-duotone ki-shop fs-3 me-2"></i> {{ artwork.isAvailable ? '구매하기' : '판매 완료' }}
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
  </div>
</template>

<script setup>
import { ref, computed, watch } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import ConfirmModal from '../components/ConfirmModal.vue'

const route = useRoute()
const router = useRouter()
const artwork = ref(null)

// State
const isFavorite = ref(false)
const isFollowing = ref(false)

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
    router.push('/purchase-history') 
  }
}

// Mock data (이미지 내용 기반)
const artworkData = {
  1: {
    id: 1,
    title: '엄청난 작품',
    artist: '허지서',
    artistId: 101,
    artistAvatar: 'https://placehold.co/50x50/3699FF/fff?text=HS',
    price: 850000,
    category: '미술',
    condition: '최상',
    isAvailable: true,
    year: 2023,
    size: '45cm x 53cm',
    material: '캔버스 유화물감',
    weight: '1.5 kg',
    shortDescription: '느슨한 예술계를 뒤집어 놓았다',
    description: "상세 설명\n이건 진짜 엄청난 명작이다.", // 이미지의 상세 설명 반영
    images: [
      'https://placehold.co/800x350/5DADE2/fff?text=Girl+with+a+Pearl+Earring',
    ],
    tags: ['유화', '추상', '파란색']
  }
}

// ⭐ 작품 정보 순서 및 레이블 (이미지 순서에 맞춤)
const artworkDetails = computed(() => {
    if (!artwork.value) return [];
    return [
        { label: '재료', value: artwork.value.material },
        { label: '무게', value: artwork.value.weight },
        { label: '크기', value: artwork.value.size },
        { label: '제작년도', value: artwork.value.year },
    ];
});

// Data Loading
const loadArtwork = (id) => {
  artwork.value = artworkData[id] || artworkData[1]
  isFavorite.value = false 
  isFollowing.value = false 
}

// Vue Router의 파라미터가 변경될 때마다 loadArtwork를 호출
watch(() => route.params.id, (newId) => {
  loadArtwork(parseInt(newId) || 1);
}, { immediate: true });


// Interaction Handlers (Modal 적용)
const toggleFavorite = (event) => {
  event.stopPropagation();
  isFavorite.value = !isFavorite.value
  const title = isFavorite.value ? '관심 작품 등록' : '관심 작품 해제'
  const message = isFavorite.value ? '관심 작품 목록에 추가되었습니다.' : '관심 작품 목록에서 제거되었습니다.'
  const type = isFavorite.value ? 'success' : 'info'
  showModal(title, message, type)
}

const toggleFollow = (event) => {
  event.stopPropagation();
  isFollowing.value = !isFollowing.value
  const title = isFollowing.value ? '팔로우 완료' : '팔로우 해제'
  const message = isFollowing.value ? `${artwork.value.artist} 작가님의 새 소식을 받아보세요!` : '작가 팔로우가 해제되었습니다.'
  const type = isFollowing.value ? 'success' : 'info'
  showModal(title, message, type)
}

const shareArtwork = () => {
  const url = window.location.href
  const tempInput = document.createElement('input');
  document.body.appendChild(tempInput);
  tempInput.value = url;
  tempInput.select();
  document.execCommand('copy');
  document.body.removeChild(tempInput);
  showModal('링크 복사 완료', '작품 링크가 클립보드에 복사되었습니다.', 'success')
}

const viewArtist = (id) => {
  router.push(`/artist/${id}`)
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

/* ⭐ 가로 폭 조정 CSS */
.w-180px {
    width: 180px; /* 이미지 폭을 180px로 키움 */
}
@media (min-width: 576px) {
  .w-sm-250px {
    width: 250px;
  }
}

/* 액션 바 고정 CSS */
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
/* 컨테이너 하단 패딩으로 고정 액션 바 공간 확보 */
.pb-20 { 
    padding-bottom: 8rem !important; 
}

/* ⭐ Dark 스타일 통일 */
.btn-dark {
    background-color: var(--bs-dark) !important;
    border-color: var(--bs-dark) !important;
    color: #fff !important;
}
/* 관심 작품 선택 시 (Dark Danger) */
.btn-dark-danger {
    background-color: var(--bs-danger) !important;
    border-color: var(--bs-danger) !important;
    color: #fff !important;
}

/* Light Gray 버튼 */
.btn-light-secondary {
    background-color: var(--bs-light-secondary) !important;
    color: var(--bs-dark) !important;
    border-color: var(--bs-light-secondary) !important;
}

</style>