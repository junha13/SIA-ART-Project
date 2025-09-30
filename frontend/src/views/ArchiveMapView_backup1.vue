<template>
  <!-- 지도가 화면 전체를 차지하고, 하단 뷰가 올라오도록 padding-bottom 제거 -->
  <div class="d-flex flex-column" style="height: 100vh;">

    <!-- 1. Top Header & Filter (55px 헤더 + 90px 검색/필터 영역) -->
    <div class="fixed-top bg-white pt-4 pb-3 px-4 shadow-sm" style="z-index: 990; margin-top: 55px;">
      <div class="d-flex align-items-center mb-3">
        <p class="text-muted small mb-0 me-3">현재 위치</p>
        <div class="input-group shadow-md rounded-pill overflow-hidden bg-white flex-grow-1">
          <input type="text" v-model="searchQuery" class="form-control border-0 ps-4" placeholder="작품, 예술인, 클래스 검색" @focus="showDetailView = false"/>
          <button class="btn btn-primary btn-icon rounded-circle me-1 my-1 p-0" @click="performSearch" style="width: 38px; height: 38px;">
            <i class="fas fa-search"></i>
          </button>
        </div>
      </div>
      <!-- Filters -->
      <div class="d-flex flex-wrap gap-2 mb-2">
        <button
            v-for="filter in filters"
            :key="filter.key"
            class="btn btn-sm rounded-pill fw-bold"
            :class="activeFilter === filter.key ? 'btn-primary' : 'btn-outline-dark text-dark'"
            @click="activeFilter = filter.key">
          {{ filter.label }}
        </button>
      </div>
    </div>

    <!-- 2. Naver Map Area (헤더 높이만큼 마진) -->
    <div
        id="archiveMap"
        class="flex-grow-1"
        :style="{ 'margin-top': '145px', 'margin-bottom': showDetailView ? '300px' : '0px', 'transition': 'margin-bottom 0.3s' }"
    >
      <!-- Map Content will be here -->
    </div>

    <!-- 3. Floating Button: 현 지도에서 재검색 -->
    <button
        class="btn btn-lg btn-danger shadow-lg position-absolute start-50 translate-middle-x fw-bold"
        style="top: 155px; z-index: 995;"
        @click="reclusterMarkers"
    >
      <i class="fas fa-sync-alt me-1"></i> 현 지도에서 재검색
    </button>

    <!-- 4. Detail View (하단에서 올라오는 뷰) - 미몽 지도1, 2 참고 -->
    <div :class="['detail-view', { 'show': showDetailView }]" class="card shadow-lg bg-white p-4 rounded-t-xl">
      <div class="handle mx-auto mb-3"></div> <!-- 드래그 핸들 -->
      <h3 class="fs-5 fw-bolder mb-3 text-dark">지도 내 검색된 예술 {{ filteredMarkers.length }}</h3>

      <div v-if="filteredMarkers.length === 0" class="text-center py-5 text-muted">
        <i class="fas fa-map-marker-slash fs-3 mb-2"></i>
        <p>선택된 지역에 예술 정보가 없습니다.</p>
      </div>

      <!-- 상세 리스트 (작품, 예술인, 클래스 탭) -->
      <ul class="nav nav-tabs nav-line-tabs nav-bold mb-4">
        <li class="nav-item">
          <a class="nav-link" :class="{ active: detailTab === 'artwork' }" href="#" @click.prevent="detailTab = 'artwork'">작품 ({{ getTabCount('artwork') }})</a>
        </li>
        <li class="nav-item">
          <a class="nav-link" :class="{ active: detailTab === 'artist' }" href="#" @click.prevent="detailTab = 'artist'">예술인 ({{ getTabCount('artist') }})</a>
        </li>
        <li class="nav-item">
          <a class="nav-link" :class="{ active: detailTab === 'class' }" href="#" @click.prevent="detailTab = 'class'">클래스 ({{ getTabCount('class') }})</a>
        </li>
      </ul>

      <!-- 콘텐츠 리스트 (스크롤 가능) -->
      <div class="content-list overflow-y-auto">
        <div v-for="item in currentDetailList" :key="item.id + item.type" class="d-flex align-items-center border-bottom py-3 cursor-pointer" @click="viewDetail(item)">
          <img :src="item.image" class="rounded me-3" width="60" height="60" style="object-fit: cover;" :alt="item.title || item.name"/>
          <div class="flex-grow-1">
            <h6 class="fw-bold mb-0 text-truncate">{{ item.title || item.name }}</h6>
            <p class="text-muted small mb-0">{{ item.category || item.bio || item.type }}</p>
          </div>
          <i class="fas fa-chevron-right text-muted"></i>
        </div>
      </div>
    </div>

    <!-- Custom Modal (for alerts) -->
    <ConfirmModal v-model:isVisible="isModalVisible" :title="modalTitle" :message="modalMessage" :type="modalType" :autoHide="true"/>
  </div>
</template>

<script setup>
import { ref, onMounted, computed, watch } from 'vue'
import { useRouter } from 'vue-router'
import ConfirmModal from '../components/ConfirmModal.vue'

const router = useRouter()
const naverMap = ref(null)
const markerClustering = ref(null)
const markers = ref([])
const activeFilter = ref('artwork')
const searchQuery = ref('')

// Detail View State (하단 뷰)
const showDetailView = ref(false)
const detailTab = ref('artwork')
const clusteredItemIds = ref([]) // 선택된 클러스터 또는 마커에 포함된 item.id 목록

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

// 필터 (작품, 예술인, 클래스)
const filters = [
  { key: 'artwork', label: '예술작품' },
  { key: 'artist', label: '예술인' },
  { key: 'class', label: '클래스' },
]

// --- 더미 데이터 확장 (총 18개) ---
const allData = ref([
  // 작품 (Artwork) - 8개
  { id: 101, type: 'artwork', title: '고요한 아침의 빛', category: '회화', image: 'assets/media/avatars/300-11.jpg', lat: 37.5665, lng: 126.9780, loc: '종로구' }, // 종로 클러스터
  { id: 102, type: 'artwork', title: '추상적 선율', category: '회화', image: 'assets/media/avatars/300-12.jpg', lat: 37.5666, lng: 126.9781, loc: '종로구' }, // 종로 클러스터
  { id: 103, type: 'artwork', title: '바나나 코미디', category: '공예', image: 'assets/media/avatars/300-13.jpg', lat: 37.5000, lng: 127.0363, loc: '강남구' }, // 강남 클러스터
  { id: 104, type: 'artwork', title: '도시의 그림자', category: '사진', image: 'assets/media/avatars/300-14.jpg', lat: 37.5500, lng: 126.9200, loc: '마포구' }, // 마포 클러스터
  { id: 105, type: 'artwork', title: '흙과 불의 춤', category: '도예', image: 'assets/media/avatars/300-15.jpg', lat: 37.5501, lng: 126.9201, loc: '마포구' }, // 마포 클러스터
  { id: 106, type: 'artwork', title: '청동의 미소', category: '조각', image: 'assets/media/avatars/300-16.jpg', lat: 37.5250, lng: 126.9000, loc: '영등포구' }, // 영등포
  { id: 107, type: 'artwork', title: '미래 도시', category: '디지털', image: 'assets/media/avatars/300-17.jpg', lat: 37.4950, lng: 126.8870, loc: '구로구' }, // 구로
  { id: 108, type: 'artwork', title: '밤의 꿈', category: '회화', image: 'assets/media/avatars/300-18.jpg', lat: 37.4951, lng: 126.8871, loc: '구로구' }, // 구로

  // 예술인 (Artist) - 5개
  { id: 201, type: 'artist', name: '김준하', bio: '추상화 작가', image: 'assets/media/avatars/300-1.jpg', lat: 37.5670, lng: 126.9785, loc: '종로구' }, // 종로 클러스터
  { id: 202, type: 'artist', name: '이예술', bio: '설치미술가', image: 'assets/media/avatars/300-2.jpg', lat: 37.5010, lng: 127.0365, loc: '강남구' }, // 강남 클러스터
  { id: 203, type: 'artist', name: '박작가', bio: '도예가', image: 'assets/media/avatars/300-3.jpg', lat: 37.5502, lng: 126.9202, loc: '마포구' }, // 마포 클러스터
  { id: 204, type: 'artist', name: '최화가', bio: '인물화 전문', image: 'assets/media/avatars/300-4.jpg', lat: 37.5251, lng: 126.9001, loc: '영등포구' }, // 영등포
  { id: 205, type: 'artist', name: '정조각', bio: '현대 조각', image: 'assets/media/avatars/300-5.jpg', lat: 37.4952, lng: 126.8872, loc: '구로구' }, // 구로

  // 클래스 (Class) - 5개
  { id: 301, type: 'class', title: '현대미술 클래스', category: '미술', image: 'assets/media/avatars/300-6.jpg', lat: 37.5675, lng: 126.9790, loc: '종로구' }, // 종로 클러스터
  { id: 302, type: 'class', title: '수채화 기초', category: '미술', image: 'assets/media/avatars/300-7.jpg', lat: 37.5020, lng: 127.0367, loc: '강남구' }, // 강남 클러스터
  { id: 303, type: 'class', title: '사진 보정 심화', category: '사진', image: 'assets/media/avatars/300-8.jpg', lat: 37.5503, lng: 126.9203, loc: '마포구' }, // 마포 클러스터
  { id: 304, type: 'class', title: 'DIY 공예', category: '공예', image: 'assets/media/avatars/300-9.jpg', lat: 37.5252, lng: 126.9002, loc: '영등포구' }, // 영등포
  { id: 305, type: 'class', title: '기타 연주 입문', category: '음악', image: 'assets/media/avatars/300-10.jpg', lat: 37.4953, lng: 126.8873, loc: '구로구' }, // 구로
])
// --- 더미 데이터 확장 끝 ---

// 1. 현재 클러스터링으로 선택된 모든 데이터 (상세 뷰 전체 리스트)
const filteredMarkers = computed(() => {
  return allData.value.filter(item => clusteredItemIds.value.includes(item.id))
})

// 2. 현재 선택된 탭에 따른 상세 리스트
const currentDetailList = computed(() => {
  return filteredMarkers.value.filter(item => item.type === detailTab.value)
})

// 3. 탭별 카운트 계산
const getTabCount = (type) => {
  return filteredMarkers.value.filter(item => item.type === type).length
}

// --- Map & Clustering Logic ---

const createMarkers = () => {
  // 1. 기존 클러스터링 인스턴스 정리
  if (markerClustering.value) {
    markerClustering.value.setMap(null);
  }
  markers.value = []

  // 2. 현재 활성 필터에 맞는 데이터만 선택
  const dataToMap = allData.value.filter(item => item.type === activeFilter.value)

  dataToMap.forEach(item => {
    const marker = new window.naver.maps.Marker({
      position: new window.naver.maps.LatLng(item.lat, item.lng),
      title: item.title || item.name,
      // map: naverMap.value // 클러스터링 사용 시 주석 처리
    })

    // 마커 클릭 이벤트: 단일 마커 클릭 시 해당 아이템만 포함된 배열 전달
    window.naver.maps.Event.addListener(marker, 'click', () => {
      handleClusterClick([item.id])
    })

    // 클러스터링을 위해 마커 객체 자체에 원본 데이터 ID 속성 추가
    marker.dataId = item.id
    markers.value.push(marker)
  })

  // 3. MarkerClustering 인스턴스 생성 및 설정 (안정성 강화)
  if (naverMap.value && markers.value.length > 0) {
    if (window.MarkerClustering) {
      markerClustering.value = new window.MarkerClustering({
        map: naverMap.value,
        markers: markers.value,
        maxZoom: 14, // 줌 레벨 14 이하에서 클러스터링
        minClusterSize: 2,
        // 미몽 시안 참고: 검은색 원형 스타일
        styles: [{
          // 실제 이미지를 사용하지 않고 텍스트 오버레이로 갯수를 표시
          icon: {
            content: '<div style="background:#000; color:#fff; font-size:14px; font-weight:bold; border-radius:50%; width:40px; height:40px; line-height:40px; text-align:center;">{text}</div>',
            size: new window.naver.maps.Size(40, 40),
            anchor: new window.naver.maps.Point(20, 20)
          },
          size: new window.naver.maps.Size(40, 40),
          textColor: '#ffffff',
          fontWeight: 'bold'
        }],
        // 클러스터 클릭 이벤트
        listener: (event) => {
          const cluster = event.overlay
          const clusteredMarkers = cluster.getMarkers()

          // 클러스터에 포함된 모든 마커의 dataId(원래 아이템 ID)를 추출
          const clusterItemIds = clusteredMarkers.map(m => m.dataId)

          // 하단 뷰를 열고 상세 정보를 표시
          handleClusterClick(clusterItemIds)
        }
      })
    } else {
      // MarkerClustering 라이브러리가 로드되지 않은 경우, 일반 마커로 대체
      markers.value.forEach(m => m.setMap(naverMap.value));
      console.warn("MarkerClustering 라이브러리가 로드되지 않았습니다. 일반 마커로 표시합니다.");
    }
  }
}

const handleClusterClick = (itemIds) => {
  clusteredItemIds.value = itemIds

  // 첫 번째 탭의 카운트를 확인하여 탭 기본값 설정 (선택된 클러스터 내에 데이터가 있는 탭으로)
  const availableTypes = ['artwork', 'artist', 'class'].filter(type =>
      allData.value.some(item => itemIds.includes(item.id) && item.type === type)
  )

  if (availableTypes.length > 0) {
    detailTab.value = availableTypes[0] // 데이터가 있는 첫 번째 탭으로 이동
  } else {
    detailTab.value = 'artwork' // 기본값 유지
  }

  // 하단 상세 뷰 표시
  showDetailView.value = true
}

const reclusterMarkers = () => {
  if (!naverMap.value) {
    showModal('지도 로드 오류', 'Naver 지도가 로드되지 않았습니다. 잠시 후 다시 시도해주세요.', 'error')
    return;
  }

  // 클러스터링 인스턴스가 존재하면 강제 재실행, 없으면 마커 재구성
  if (markerClustering.value) {
    markerClustering.value.redraw()
    showModal('재검색 완료', '현 지도 범위 내에서 마커를 재검색했습니다.', 'success')
  } else {
    // 클러스터링 인스턴스가 없을 경우 (라이브러리 로드가 안 됐거나 마커가 1개일 경우)
    createMarkers(); // 마커를 다시 생성하여 클러스터링을 재시도
    showModal('재검색 완료', '현 지도 범위 내에서 마커를 재검색했습니다.', 'success')
  }

  // 재검색 시 하단 상세 뷰는 닫음
  showDetailView.value = false;
}

const initMap = () => {
  if (!window.naver) return

  naverMap.value = new window.naver.maps.Map('archiveMap', {
    center: new window.naver.maps.LatLng(37.5665, 126.9780), // 서울 시청
    zoom: 13,
    minZoom: 9,
    maxZoom: 18,
    logoControl: false,
    mapDataControl: false,
    zoomControl: true,
    scaleControl: true,
    mapTypeControl: true,
  })

  createMarkers() // 마커 및 클러스터링 초기화

  // 맵 영역을 클릭하면 상세 뷰 닫기
  window.naver.maps.Event.addListener(naverMap.value, 'click', () => {
    showDetailView.value = false
  });
}

// 필터 변경 시 지도 업데이트
watch(activeFilter, () => {
  createMarkers()
  showDetailView.value = false // 필터 변경 시 하단 뷰 닫기
  clusteredItemIds.value = [] // 하단 데이터도 초기화
})

const performSearch = () => {
  showModal('검색 실행', `${searchQuery.value}를 지도에 반영하여 검색합니다.`, 'info')
  // 실제 로직: 검색어를 기반으로 allData를 업데이트하고 createMarkers()를 다시 호출
  showDetailView.value = false
}

const viewDetail = (item) => {
  if (item.type === 'artwork') {
    router.push(`/artwork/${item.id}`)
  } else if (item.type === 'artist') {
    router.push(`/artist/${item.id}`)
  } else if (item.type === 'class') {
    router.push(`/class/${item.id}`)
  }
}


onMounted(() => {
  // Naver Map SDK 로드 확인 후 초기화 (index.html에서 defer로 로드 가정)
  if (window.naver) {
    // MarkerClustering 라이브러리가 로드될 때까지 기다림 (실제 환경에서는 index.html에 추가되어야 함)
    // NOTE: 이 setTimeout은 Canvas 환경에서 라이브러리 로드 타이밍 문제를 임시로 해결하기 위함입니다.
    setTimeout(() => {
      initMap();
    }, 500);
  } else {
    // SDK 자체가 로드되지 않은 경우
    window.addEventListener('load', () => {
      setTimeout(() => {
        if (window.naver) initMap();
      }, 500);
    });
  }
})
</script>

<style scoped>
.rounded-t-xl {
  border-top-left-radius: 1.5rem !important;
  border-top-right-radius: 1.5rem !important;
}
.detail-view {
  position: fixed;
  bottom: 60px; /* 하단 네비게이션바 피하기 */
  left: 0;
  right: 0;
  height: 300px; /* 상세 뷰 고정 높이 */
  transform: translateY(100%); /* 기본적으로 숨김 */
  transition: transform 0.3s ease-out;
  z-index: 1000;
  border-top: 1px solid #e9ecef;
}
.detail-view.show {
  transform: translateY(0); /* 보일 때 위로 이동 */
}
.detail-view .handle {
  width: 40px;
  height: 4px;
  background-color: #dee2e6;
  border-radius: 2px;
}
.content-list {
  max-height: calc(100% - 120px); /* 탭과 제목 영역 높이를 뺀 나머지 */
  -webkit-overflow-scrolling: touch;
}
</style>
