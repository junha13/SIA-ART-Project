<template>
  <div class="d-flex flex-column" style="height: 100vh;">
    <header class="fixed-top bg-white px-4 pt-3 pb-2 shadow-sm" style="z-index: 1000;">
      <div class="input-group mb-3">
        <input type="text" v-model="searchQuery" class="form-control rounded-pill pe-5 shadow-sm" style="height: 44px; padding-left: 1.5rem;"
               placeholder="구로구 구로동" @keyup.enter="performSearch"/>
        <span class="position-absolute end-0 top-50 translate-middle-y me-3 text-primary cursor-pointer" @click="performSearch">
          <i class="fas fa-search"></i>
        </span>
      </div>

      <div class="d-flex flex-wrap gap-2 mb-2 pb-1 overflow-auto filter-scroll">
        <button
            v-for="filter in filters"
            :key="filter.key"
            class="btn btn-sm rounded-pill fw-bold text-nowrap"
            :class="activeFilter === filter.key ? 'btn-dark' : 'btn-outline-secondary'"
            @click="activeFilter = filter.key">
          {{ filter.label }}
        </button>
        <button class="btn btn-sm rounded-pill btn-outline-secondary" @click="showModal('기타 필터', '추가 필터 기능은 구현되지 않았습니다.', 'info')">
          <i class="fas fa-ellipsis-h"></i>
        </button>
      </div>
    </header>

    <div
        id="archiveMap"
        class="flex-grow-1"
        :style="{ 'margin-top': '180px', 'margin-bottom': '280px' }"
    >
      </div>

    <button
        class="btn btn-lg btn-light rounded-circle shadow-lg position-absolute bottom-0 end-0 me-4 mb-4"
        style="width: 60px; height: 60px; z-index: 1005;"
        @click="showModal('추가', '새로운 정보 등록 기능입니다.', 'info')"
    >
      <i class="fas fa-plus fa-2x text-primary"></i>
    </button>


    <div class="position-fixed bottom-0 start-0 w-100 px-3 pb-3" style="height: 250px; z-index: 1000; margin-bottom: 60px;">
      <div class="d-flex overflow-auto gap-3 h-100 card-scroll">
        <div v-for="item in currentDetailList" :key="item.id + item.type"
             class="card shadow-lg flex-shrink-0" style="width: 200px; cursor: pointer;"
             @click="viewDetail(item)">
          <img :src="item.image" class="card-img-top" height="120" style="object-fit: cover;" :alt="item.title || item.name"/>
          <div class="card-body p-3">
            <h6 class="fw-bold mb-1 text-truncate">{{ item.title || item.name }}</h6>
            <p class="text-muted small mb-0 text-truncate">
              <i class="fas fa-map-marker-alt me-1"></i> {{ item.loc }}
            </p>
          </div>
        </div>
        <div v-if="currentDetailList.length === 0" class="w-100 h-100 d-flex justify-content-center align-items-center">
          <div class="text-center text-muted">
            <i class="fas fa-info-circle mb-2"></i>
            <p class="mb-0">지도에 표시할 정보가 없습니다.</p>
          </div>
        </div>
      </div>
    </div>

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
// 필터 키 변경: 'artwork' -> 'art' (작품), 'studio' (작업실), 'gallery' (갤러리/전시회)
const activeFilter = ref('art')
const searchQuery = ref('구로구 구로동') // 이미지 UI 반영

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

// 필터 (예술작품, 작업실, 갤러리/전시회)
const filters = [
  { key: 'art', label: '예술작품' },
  { key: 'studio', label: '작업실' },
  { key: 'gallery', label: '갤러리' }, // 사용자 요청에 따라 '갤러리(전시회)'로 대체
]

// --- 더미 데이터 확장 (총 21개: art 8, studio 6, gallery 7) ---
const allData = ref([
  // 작품 (Art) - 8개 (기존 artwork을 art로 변경)
  { id: 101, type: 'art', title: '고요한 아침의 빛', category: '회화', image: 'assets/media/avatars/300-11.jpg', lat: 37.5665, lng: 126.9780, loc: '종로구' },
  { id: 102, type: 'art', title: '추상적 선율', category: '회화', image: 'assets/media/avatars/300-12.jpg', lat: 37.5666, lng: 126.9781, loc: '종로구' },
  { id: 103, type: 'art', title: '바나나 코미디', category: '공예', image: 'assets/media/avatars/300-13.jpg', lat: 37.5000, lng: 127.0363, loc: '강남구' },
  { id: 104, type: 'art', title: '도시의 그림자', category: '사진', image: 'assets/media/avatars/300-14.jpg', lat: 37.5500, lng: 126.9200, loc: '마포구' },
  { id: 105, type: 'art', title: '흙과 불의 춤', category: '도예', image: 'assets/media/avatars/300-15.jpg', lat: 37.5501, lng: 126.9201, loc: '마포구' },
  { id: 106, type: 'art', title: '청동의 미소', category: '조각', image: 'assets/media/avatars/300-16.jpg', lat: 37.5250, lng: 126.9000, loc: '영등포구' },
  { id: 107, type: 'art', title: '미래 도시', category: '디지털', image: 'assets/media/avatars/300-17.jpg', lat: 37.4950, lng: 126.8870, loc: '구로구' }, // 구로
  { id: 108, type: 'art', title: '밤의 꿈', category: '회화', image: 'assets/media/avatars/300-18.jpg', lat: 37.4951, lng: 126.8871, loc: '구로구' }, // 구로

  // 작업실 (Studio) - 6개 (기존 artist, class에서 일부 가져와서 studio로 변경)
  { id: 201, type: 'studio', name: '김작가 작업실', bio: '추상화 전문 스튜디오', image: 'assets/media/avatars/300-1.jpg', lat: 37.5670, lng: 126.9785, loc: '종로구' },
  { id: 202, type: 'studio', name: '이도예 스튜디오', bio: '도예 클래스 및 작업 공간', image: 'assets/media/avatars/300-2.jpg', lat: 37.5010, lng: 127.0365, loc: '강남구' },
  { id: 203, type: 'studio', name: '빛과 그림자 사진실', bio: '사진 보정 심화 클래스', image: 'assets/media/avatars/300-8.jpg', lat: 37.5503, lng: 126.9203, loc: '마포구' },
  { id: 204, type: 'studio', name: '최화가 아뜰리에', bio: '인물화/유화 개인 레슨', image: 'assets/media/avatars/300-4.jpg', lat: 37.5251, lng: 126.9001, loc: '영등포구' },
  { id: 205, type: 'studio', name: '정조각 공방', bio: '현대 조각 작업 공간', image: 'assets/media/avatars/300-5.jpg', lat: 37.4952, lng: 126.8872, loc: '구로구' },
  { id: 206, type: 'studio', name: '기타 연주 스튜디오', bio: '음악 레슨 공간', image: 'assets/media/avatars/300-10.jpg', lat: 37.4953, lng: 126.8873, loc: '구로구' },

  // 갤러리 (Gallery/Exhibition) - 7개
  { id: 301, type: 'gallery', title: '종로 현대 갤러리', category: '특별 전시', image: 'assets/media/avatars/300-6.jpg', lat: 37.5675, lng: 126.9790, loc: '종로구' },
  { id: 302, type: 'gallery', title: '강남 미니 갤러리', category: '신진 작가전', image: 'assets/media/avatars/300-7.jpg', lat: 37.5020, lng: 127.0367, loc: '강남구' },
  { id: 303, type: 'gallery', title: '마포 사진 전시회', category: '사진전', image: 'assets/media/avatars/300-9.jpg', lat: 37.5504, lng: 126.9204, loc: '마포구' },
  { id: 304, type: 'gallery', title: '영등포 예술센터', category: '기획 전시', image: 'assets/media/avatars/300-19.jpg', lat: 37.5253, lng: 126.9003, loc: '영등포구' },
  { id: 305, type: 'gallery', title: '구로 디지털 아트홀', category: '미디어 아트', image: 'assets/media/avatars/300-20.jpg', lat: 37.4954, lng: 126.8874, loc: '구로구' },
  { id: 306, type: 'gallery', title: '서울 시립 미술관', category: '상설 전시', image: 'assets/media/avatars/300-21.jpg', lat: 37.5615, lng: 126.9750, loc: '중구' },
  { id: 307, type: 'gallery', title: '홍대 길거리 전시', category: '야외 설치', image: 'assets/media/avatars/300-22.jpg', lat: 37.5505, lng: 126.9205, loc: '마포구' },

])
// --- 더미 데이터 확장 끝 ---

// 1. 현재 선택된 필터에 따른 리스트 (하단 카드 뷰)
const currentDetailList = computed(() => {
  // 현재는 지도 범위나 클러스터링을 반영하지 않고, 필터에 맞는 모든 데이터를 표시합니다.
  // 실제 구현에서는 지도의 경계 내 데이터만 필터링해야 합니다.
  return allData.value.filter(item => item.type === activeFilter.value)
})

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
    })

    // 마커 클릭 이벤트: 단일 마커 클릭 시 해당 마커 위치로 지도를 이동하고 하단 뷰는 그대로 유지
    window.naver.maps.Event.addListener(marker, 'click', () => {
      naverMap.value.setCenter(marker.getPosition());
      // 실제 앱에서는 클릭한 마커에 해당하는 상세 정보만 하단 뷰에 표시할 수 있도록 로직 추가 필요
    })

    // 클러스터링을 위해 마커 객체 자체에 원본 데이터 ID 속성 추가
    marker.dataId = item.id
    markers.value.push(marker)
  })

  // 3. MarkerClustering 인스턴스 생성 및 설정 (이미지 UI 반영)
  if (naverMap.value && markers.value.length > 0) {
    if (window.MarkerClustering) {
      markerClustering.value = new window.MarkerClustering({
        map: naverMap.value,
        markers: markers.value,
        maxZoom: 14,
        minClusterSize: 2,
        // 이미지 UI처럼 단순 핀/클러스터로 보이도록 스타일 변경
        styles: [{
          // 단일 마커 스타일 (이미지 UI의 빨간 핀)
          icon: {
            content: '<div style="color:white; font-size:12px; font-weight:bold; width:30px; height:40px; line-height:30px; text-align:center; background-image:url(\'data:image/svg+xml;utf8,<svg xmlns=\'http://www.w3.org/2000/svg\' viewBox=\'0 0 40 50\'><path fill=\'red\' d=\'M20,0 C9,0 0,9 0,20 C0,30 20,50 20,50 C20,50 40,30 40,20 C40,9 31,0 20,0 Z\'/><circle fill=\'white\' cx=\'20\' cy=\'20\' r=\'8\'/></svg>\'); background-size:contain; background-repeat:no-repeat;"></div>',
            size: new window.naver.maps.Size(40, 50),
            anchor: new window.naver.maps.Point(20, 50)
          },
        },
        {
          // 클러스터 스타일 (검은색 원형)
          icon: {
            content: '<div style="background:#000; color:#fff; font-size:14px; font-weight:bold; border-radius:50%; width:40px; height:40px; line-height:40px; text-align:center;">{text}</div>',
            size: new window.naver.maps.Size(40, 40),
            anchor: new window.naver.maps.Point(20, 20)
          },
          size: new window.naver.maps.Size(40, 40),
          textColor: '#ffffff',
          fontWeight: 'bold'
        }
      ],
        // 클러스터 클릭 이벤트: 지도 확대 대신 하단 뷰를 업데이트하는 기능은 제거 (이미지 UI에 해당 기능 없음)
        listener: (event) => {
          const cluster = event.overlay
          naverMap.value.fitBounds(cluster.getBounds()) // 클러스터 클릭 시 해당 범위로 지도 확대
        }
      })
    } else {
      // MarkerClustering 라이브러리가 로드되지 않은 경우, 일반 마커로 대체
      markers.value.forEach(m => m.setMap(naverMap.value));
      console.warn("MarkerClustering 라이브러리가 로드되지 않았습니다. 일반 마커로 표시합니다.");
    }
  }
}

const initMap = () => {
  if (!window.naver) return

  naverMap.value = new window.naver.maps.Map('archiveMap', {
    center: new window.naver.maps.LatLng(37.4940, 126.8870), // 구로구 구로동 근처
    zoom: 13,
    minZoom: 9,
    maxZoom: 18,
    logoControl: false,
    mapDataControl: false,
    zoomControl: true,
    scaleControl: true,
    mapTypeControl: false, // 맵 타입 컨트롤 제거 (이미지 UI에 없음)
  })

  createMarkers() // 마커 및 클러스터링 초기화

  // 맵 영역을 클릭해도 하단 카드 뷰는 유지 (이미지 UI 반영)
}

// 필터 변경 시 지도 업데이트
watch(activeFilter, () => {
  createMarkers()
})

const performSearch = () => {
  showModal('검색 실행', `'${searchQuery.value}' 위치로 지도를 이동하고 해당 위치의 정보를 필터에 맞게 검색합니다.`, 'info')
  // 실제 로직: 검색어를 기반으로 지도의 중심을 이동하고 createMarkers()를 다시 호출
}

const goBack = () => {
  router.go(-1)
}

const viewDetail = (item) => {
  showModal('상세 보기', `${item.title || item.name}의 상세 페이지로 이동합니다.`, 'info')
  // router.push(`/${item.type}/${item.id}`)
}


onMounted(() => {
  // Naver Map SDK 로드 확인 후 초기화
  if (window.naver) {
    // NOTE: setTimeout은 Canvas 환경에서 라이브러리 로드 타이밍 문제를 임시로 해결하기 위함입니다.
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
/* 필터 가로 스크롤 시 스크롤바 숨김 */
.filter-scroll, .card-scroll {
  -ms-overflow-style: none; /* IE and Edge */
  scrollbar-width: none; /* Firefox */
}
.filter-scroll::-webkit-scrollbar, .card-scroll::-webkit-scrollbar {
  display: none; /* Chrome, Safari, Opera*/
}

/* 돋보기 아이콘 위치 조정을 위한 스타일 */
.input-group {
  position: relative;
}
.input-group input {
  padding-right: 50px !important; /* 돋보기 아이콘 공간 확보 */
}
.cursor-pointer {
  cursor: pointer;
}
</style>