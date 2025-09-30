<template>
  <div class="d-flex flex-column" style="height: 100vh;">
    <header class="fixed-top bg-white px-4 pt-3 pb-2 shadow-sm" style="z-index: 1000;">
      <div class="input-group mb-3" style="padding-left: 15px; padding-right: 15px;">
        <input type="text" v-model="searchQuery" class="form-control rounded-pill pe-5 shadow-sm"
               style="height: 44px; padding-left: 1.5rem;"
               placeholder="구로구 구로동" @keyup.enter="performSearch"/>
        <span class="position-absolute end-0 top-50 translate-middle-y me-3 text-primary cursor-pointer" @click="performSearch">
          <i class="fas fa-search"></i>
        </span>
    <header class="fixed-top bg-white px-4 pt-3 pb-2 shadow-sm" style="z-index: 1000;">
      <div class="input-group mb-3" style="padding-left: 15px; padding-right: 15px;">
        <input type="text" v-model="searchQuery" class="form-control rounded-pill pe-5 shadow-sm"
               style="height: 44px; padding-left: 1.5rem;"
               placeholder="구로구 구로동" @keyup.enter="performSearch"/>
        <span class="position-absolute end-0 top-50 translate-middle-y me-3 text-primary cursor-pointer" @click="performSearch">
          <i class="fas fa-search"></i>
        </span>
      </div>

      <div class="d-flex flex-wrap gap-2 mb-2 pb-1 overflow-auto filter-scroll" style="padding-left: 15px">

      <div class="d-flex flex-wrap gap-2 mb-2 pb-1 overflow-auto filter-scroll" style="padding-left: 15px">
        <button
            v-for="filter in filters"
            :key="filter.key"
            class="btn btn-sm rounded-pill fw-bold text-nowrap"
            :class="activeFilter === filter.key ? 'btn-dark' : 'btn-outline-secondary'"
            class="btn btn-sm rounded-pill fw-bold text-nowrap"
            :class="activeFilter === filter.key ? 'btn-dark' : 'btn-outline-secondary'"
            @click="activeFilter = filter.key">
          {{ filter.label }}
        </button>
        <button class="btn btn-sm rounded-pill btn-outline-secondary"
                @click="showModal('기타 필터', '추가 필터 기능은 구현되지 않았습니다.', 'info')">
          <i class="fas fa-ellipsis-h"></i>
        </button>
        <button class="btn btn-sm rounded-pill btn-outline-secondary"
                @click="showModal('기타 필터', '추가 필터 기능은 구현되지 않았습니다.', 'info')">
          <i class="fas fa-ellipsis-h"></i>
        </button>
      </div>
    </header>
    </header>

    <main class="flex-grow-1 overflow-auto" style="margin-top: 130px;">
      <!-- 지도 -->
      <div
          id="archiveMap"
          style="height: 45vh; width: 100%; border: 1px solid gray; border-radius: 1rem;"
          class="mb-3"
      ></div>

      <div class="px-1 mb-2">
        <h5 class="fw-bold">선택된 지역의 {{ filters.find(f => f.key === activeFilter)?.label }}</h5>
    <main class="flex-grow-1 overflow-auto" style="margin-top: 130px;">
      <!-- 지도 -->
      <div
          id="archiveMap"
          style="height: 45vh; width: 100%; border: 1px solid gray; border-radius: 1rem;"
          class="mb-3"
      ></div>

      <div class="px-1 mb-2">
        <h5 class="fw-bold">선택된 지역의 {{ filters.find(f => f.key === activeFilter)?.label }}</h5>
      </div>



      <!-- ▼▼ 카드 스크롤 래퍼: relative로 만들어 버튼을 안에 겹친다 ▼▼ -->
      <div class="px-1 pb-5 cards-wrap">
        <div class="d-flex overflow-auto gap-3 card-scroll" style="border-radius: 1rem;">
          <div v-for="item in currentDetailList" :key="item.id + item.type"
              class="card flex-shrink-0" style="width: 200px; cursor: pointer;"
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

          <div v-if="currentDetailList.length === 0" class="w-100 h-100 d-flex justify-content-center align-items-center">
            <div class="text-center text-muted">
              <i class="fas fa-info-circle mb-2"></i>
              <p class="mb-0">지도에 표시할 정보가 없습니다.</p>
            </div>
          </div>
        </div>

        

        <!-- ★ 여기로 버튼 이동: 카드 영역 우하단에 겹친다 -->
        <button
          class="btn btn-lg btn-light rounded-circle shadow-lg fab-add"
          @click="goTo('추가', '새로운 정보 등록 기능입니다.', 'info')"
          aria-label="새 항목 추가"
          title="새 항목 추가"
        >
          <i class="ki-duotone ki-plus"></i>
        </button>
      </div>
    </main>

    
    

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
const activeFilter = ref('art')
const searchQuery = ref('구로구 구로동') 

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

// 필터 (예술작품, 클래스, 갤러리/전시회)
// 필터 (예술작품, 클래스, 갤러리/전시회)
const filters = [
  { key: 'art', label: '예술작품' },
  { key: 'studio', label: '클래스' },
  { key: 'gallery', label: '갤러리' }, 
]

// --- 더미 데이터 확장 (총 21개: art 8, studio 6, gallery 7) ---
// --- 더미 데이터 확장 (총 21개: art 8, studio 6, gallery 7) ---
const allData = ref([
  // 작품 (Art) - 8개 (기존 artwork을 art로 변경)
  { id: 101, type: 'art', title: '바나나 코미디', category: '공예', image: 'assets/media/avatars/300-13.jpg', lat: 37.5000, lng: 127.0363, loc: '강남구' },
  { id: 102, type: 'art', title: '추상적 선율', category: '회화', image: 'assets/media/avatars/300-12.jpg', lat: 37.5666, lng: 126.9781, loc: '종로구' },
  { id: 103, type: 'art', title: '도시의 그림자', category: '사진', image: 'assets/media/avatars/300-14.jpg', lat: 37.5500, lng: 126.9200, loc: '마포구' },
  { id: 104, type: 'art', title: '흙과 불의 춤', category: '도예', image: 'assets/media/avatars/300-15.jpg', lat: 37.5501, lng: 126.9201, loc: '마포구' },
  { id: 105, type: 'art', title: '청동의 미소', category: '조각', image: 'assets/media/avatars/300-16.jpg', lat: 37.5250, lng: 126.9000, loc: '영등포구' },
  { id: 106, type: 'art', title: '미래 도시', category: '디지털', image: 'assets/media/avatars/300-17.jpg', lat: 37.4950, lng: 126.8870, loc: '구로구' }, // 구로
  { id: 107, type: 'art', title: '밤의 꿈', category: '회화', image: 'assets/media/avatars/300-18.jpg', lat: 37.4951, lng: 126.8871, loc: '구로구' }, // 구로
  { id: 108, type: 'art', title: '고요한 아침의 빛', category: '회화', image: 'assets/media/avatars/300-11.jpg', lat: 37.5665, lng: 126.9780, loc: '종로구' },
  
  // 클래스 (Studio) - 6개
  { id: 201, type: 'studio', name: '김작가 클래스', bio: '추상화 전문 스튜디오', image: 'assets/media/avatars/300-1.jpg', lat: 37.5670, lng: 126.9785, loc: '종로구' },
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

// --- Map & Clustering Logic ---

const createMarkers = () => {
  // 1. 기존 클러스터링 인스턴스 정리
  if (markerClustering.value) {
    markerClustering.value.setMap(null);
  }
  markers.value = []

  // 2. 현재 활성 필터 + 검색 키워드에 맞는 데이터만 선택
  const dataToMap = filteredList.value
  // 2. 현재 활성 필터 + 검색 키워드에 맞는 데이터만 선택
  const dataToMap = filteredList.value

  dataToMap.forEach(item => {
    const marker = new window.naver.maps.Marker({
      position: new window.naver.maps.LatLng(item.lat, item.lng),
      title: item.title || item.name,
    })

    // 마커 클릭 이벤트: 단일 마커 클릭 시 해당 마커 위치로 지도를 이동하고 하단 뷰는 그대로 유지
    // 마커 클릭 이벤트: 단일 마커 클릭 시 해당 마커 위치로 지도를 이동하고 하단 뷰는 그대로 유지
    window.naver.maps.Event.addListener(marker, 'click', () => {
      naverMap.value.setCenter(marker.getPosition());
      // 실제 앱에서는 클릭한 마커에 해당하는 상세 정보만 하단 뷰에 표시할 수 있도록 로직 추가 필요
      naverMap.value.setCenter(marker.getPosition());
      // 실제 앱에서는 클릭한 마커에 해당하는 상세 정보만 하단 뷰에 표시할 수 있도록 로직 추가 필요
    })

    // 클러스터링을 위해 마커 객체 자체에 원본 데이터 ID 속성 추가
    marker.dataId = item.id
    markers.value.push(marker)
  })

  // 3. MarkerClustering 인스턴스 생성 및 설정 (이미지 UI 반영)
  // 3. MarkerClustering 인스턴스 생성 및 설정 (이미지 UI 반영)
  if (naverMap.value && markers.value.length > 0) {
    if (window.MarkerClustering) {
      markerClustering.value = new window.MarkerClustering({
        map: naverMap.value,
        markers: markers.value,
        maxZoom: 14,
        maxZoom: 14,
        minClusterSize: 2,
        // 이미지 UI처럼 단순 핀/클러스터로 보이도록 스타일 변경
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
    center: new window.naver.maps.LatLng(37.4940, 126.8870), // 구로구 구로동 근처
    zoom: 13,
    minZoom: 9,
    maxZoom: 18,
    logoControl: false,
    mapDataControl: false,
    zoomControl: true,
    scaleControl: true,
    mapTypeControl: false, 
  })

  createMarkers() // 마커 및 클러스터링 초기화

  // 맵 영역을 클릭해도 하단 카드 뷰는 유지 (이미지 UI 반영)
  // 맵 영역을 클릭해도 하단 카드 뷰는 유지 (이미지 UI 반영)
}

// 필터 변경 시 지도 업데이트
watch(searchQuery, () => {
watch(searchQuery, () => {
  createMarkers()
})

const performSearch = () => {
  // 1) 필터 반영된 리스트
  const list = filteredList.value

  // 2) 마커/클러스터 갱신
  createMarkers()

  // 3) 결과가 있으면 해당 핀들을 모두 담는 bounds로 화면 이동
  if (naverMap.value && list.length > 0) {
    const bounds = new window.naver.maps.LatLngBounds()
    list.forEach(it => bounds.extend(new window.naver.maps.LatLng(it.lat, it.lng)))
    naverMap.value.fitBounds(bounds)
  } else {
    // 결과 없으면 간단 안내
    showModal('검색 결과 없음', `'${searchQuery.value}'에 해당하는 위치를 찾지 못했어요. 다른 지역명을 입력해보세요.`, 'warning')
  }
}

const goBack = () => {
  router.go(-1)
}

const viewDetail = (item) => {
  showModal('상세 보기', `${item.title || item.name}의 상세 페이지로 이동합니다.`, 'info')
  // router.push(`/${item.type}/${item.id}`)
  showModal('상세 보기', `${item.title || item.name}의 상세 페이지로 이동합니다.`, 'info')
  // router.push(`/${item.type}/${item.id}`)
}


onMounted(() => {
  // Naver Map SDK 로드 확인 후 초기화
  // Naver Map SDK 로드 확인 후 초기화
  if (window.naver) {
    // NOTE: setTimeout은 Canvas 환경에서 라이브러리 로드 타이밍 문제를 임시로 해결하기 위함입니다.
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

///
/// 지도 검색 필터
///

// --- 검색어에서 지역 키워드(구/군/시 일부) 추출 ---
const extractAreaKeyword = (raw) => {
  if (!raw) return ''
  const s = String(raw).trim()
  // 한글 + 숫자/영문 혼합 대비, 공백 기준으로 토큰화 후 '구/군/시'로 끝나는 단어 우선
  const tokens = s.split(/\s+/)
  const bySuffix = tokens.find(t => /[가-힣A-Za-z0-9]+(구|군|시)$/.test(t))
  if (bySuffix) return bySuffix.replace(/[^가-힣A-Za-z0-9]/g, '')

  // 못 찾으면 첫 토큰 사용 (예: '구로'만 쳐도 '구로' 매칭되게)
  return tokens[0].replace(/[^가-힣A-Za-z0-9]/g, '')
}

// 현재 검색어에서 키워드 추출 (ex: '구로구 구로동' -> '구로구')
const areaKeyword = computed(() => extractAreaKeyword(searchQuery.value))

// 1) 타입 필터 + 2) 지역 키워드(부분 일치)까지 반영된 최종 리스트
const filteredList = computed(() => {
  const kw = areaKeyword.value
  return allData.value.filter(item => {
    const typeOk = item.type === activeFilter.value
    if (!kw) return typeOk
    // loc이 '구로구', '종로구' 처럼 들어있으니 부분 일치 허용
    return typeOk && String(item.loc).includes(kw)
  })
})

// 하단 카드 뷰는 filteredList 사용
const currentDetailList = computed(() => filteredList.value)




</script>

<style scoped>
/* 카드/필터 스크롤에서 스크롤바 감추기 */
.filter-scroll, .card-scroll {
  -ms-overflow-style: none;
  scrollbar-width: none;
}
.filter-scroll::-webkit-scrollbar, .card-scroll::-webkit-scrollbar {
  display: none;
}

/* 메인 패딩 */
main {
  padding-right: 30px;
  padding-left: 30px;
}

/* ▼ 카드 스크롤 래퍼를 상대배치로, 버튼은 절대배치로 */
.cards-wrap {
  position: relative;
}

/* 플로팅 추가 버튼(FAB) – 카드 위 우하단에 겹침 */
.fab-add {
  position: absolute;
  right: 8px;   /* 카드 오른쪽 여백 */
  bottom: 20px; /* 카드 아래쪽 여백 */
  width: 60px;
  height: 60px;
  z-index: 10;  /* 카드 위로 올라오게 */
  display: inline-flex;
  align-items: center;
  justify-content: center;
}

/* 마지막 카드가 버튼에 가리지 않도록 스크롤 패딩 확보 */
.card-scroll {
  padding-right: 80px; /* 버튼 폭 + 여유 */
}

</style>