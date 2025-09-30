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
      </div>

      <div class="d-flex flex-wrap gap-2 mb-2 pb-1 overflow-auto filter-scroll" style="padding-left: 15px">
        <button
            v-for="filter in filters"
            :key="filter.key"
            class="btn btn-sm rounded-pill fw-bold text-nowrap"
            :class="activeFilter === filter.key ? 'btn-dark' : 'btn-outline-secondary'"
            @click="activeFilter = filter.key">
          {{ filter.label }}
        </button>
        <button class="btn btn-sm rounded-pill btn-outline-secondary"
                @click="showModal('기타 필터', '추가 필터 기능은 구현되지 않았습니다.', 'info')">
          <i class="fas fa-ellipsis-h"></i>
        </button>
      </div>
    </header>

    <main class="flex-grow-1 overflow-auto" style="margin-top: 130px;">
      <div
          id="archiveMap"
          style="height: 45vh; width: 100%; border: 1px solid gray; border-radius: 1rem;"
          class="mb-3"
      ></div>

      <div class="px-1 mb-2">
        <h5 class="fw-bold">선택된 지역의 {{ filters.find(f => f.key === activeFilter)?.label }}</h5>
      </div>


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


        <button
            class="btn btn-lg btn-light rounded-circle shadow-lg fab-add"
            @click="router.push('/product/register')"
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
import { MOCK_MAP_DATA } from '@/data/MockData.js'; // 🟢 MockData Import
import { useLocationStore } from '@/stores/useLocationStore'; // 🟢 Location Store Import


const router = useRouter()
const locationStore = useLocationStore(); // Store 초기화
const naverMap = ref(null)
const markerClustering = ref(null)
const markers = ref([])
const activeFilter = ref('art')
const searchQuery = ref(locationStore.currentLocation.name) // Store의 현재 위치를 기본 검색어로 사용

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
const filters = [
  { key: 'art', label: '예술작품' },
  { key: 'studio', label: '클래스' },
  { key: 'gallery', label: '갤러리' },
]

// --- 🟢 MOCK_MAP_DATA에서 데이터 로드 ---
const allData = ref(MOCK_MAP_DATA);
// --- 데이터 로드 끝 ---

// --- Map & Clustering Logic ---

const createMarkers = () => {
  // ⭐ [오류 수정 후 로직]: 지도의 현재 경계를 얻어와 필터링에 활용
  if (!naverMap.value) return;
  const bounds = naverMap.value.getBounds();

  // 1. 기존 클러스터링 인스턴스 정리
  if (markerClustering.value) {
    markerClustering.value.setMap(null);
  }
  markers.value = []

  // 2. 현재 활성 필터 + 검색 키워드 + 지도 경계 내 데이터만 선택
  const dataToMap = filteredList.value.filter(item => {
    // 🟢 지리적 경계 필터링: item의 좌표가 현재 지도 경계 내에 있는지 확인
    const latLng = new window.naver.maps.LatLng(item.lat, item.lng);
    return bounds.hasLatLng(latLng);
  });

  dataToMap.forEach(item => {
    const marker = new window.naver.maps.Marker({
      position: new window.naver.maps.LatLng(item.lat, item.lng),
      title: item.title || item.name,
    })

    // 마커 클릭 이벤트: 단일 마커 클릭 시 해당 마커 위치로 지도를 이동하고 하단 뷰는 그대로 유지
    window.naver.maps.Event.addListener(marker, 'click', () => {
      naverMap.value.setCenter(marker.getPosition());
    })

    // 클러스터링을 위해 마커 객체 자체에 원본 데이터 ID 속성 추가
    marker.dataId = item.id
    markers.value.push(marker)
  })

  // 3. MarkerClustering 인스턴스 생성 및 설정
  if (naverMap.value && markers.value.length > 0) {
    if (window.MarkerClustering) {
      markerClustering.value = new window.MarkerClustering({
        map: naverMap.value,
        markers: markers.value,
        maxZoom: 14,
        minClusterSize: 2,
        styles: [{
          // ⭐ [오류 수정]: 템플릿 리터럴(백틱)을 사용하여 SVG 문자열 구문 오류 해결
          icon: {
            content: `<div style="color:white; font-size:12px; font-weight:bold; width:30px; height:40px; line-height:30px; text-align:center; background-image:url('data:image/svg+xml;utf8,<svg xmlns=\'http://www.w3.org/2000/svg\' viewBox=\'0 0 40 50\'><path fill=\'red\' d=\'M20,0 C9,0 0,9 0,20 C0,30 20,50 20,50 C20,50 40,30 40,20 C40,9 31,0 20,0 Z\'/><circle fill=\'white\' cx=\'20\' cy=\'20\' r=\'8\'/></svg>\'); background-size:contain; background-repeat:no-repeat;"></div>`,
            size: new window.naver.maps.Size(40, 50),
            anchor: new window.naver.maps.Point(20, 50)
          },
        },
          {
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
        listener: (event) => {
          const cluster = event.overlay
          naverMap.value.fitBounds(cluster.getBounds())
        }
      })
    } else {
      markers.value.forEach(m => m.setMap(naverMap.value));
      console.warn("MarkerClustering 라이브러리가 로드되지 않았습니다. 일반 마커로 표시합니다.");
    }
  }
}

const initMap = () => {
  if (!window.naver) return

  // 🟢 Store의 현재 위치를 지도 중앙 좌표로 사용
  const centerLat = locationStore.currentLocation.lat;
  const centerLng = locationStore.currentLocation.lng;

  naverMap.value = new window.naver.maps.Map('archiveMap', {
    center: new window.naver.maps.LatLng(centerLat, centerLng), // Store 값 사용
    zoom: 13,
    minZoom: 9,
    maxZoom: 18,
    logoControl: false,
    mapDataControl: false,
    zoomControl: true,
    scaleControl: true,
    mapTypeControl: false,
  })

  // ⭐ [핵심 수정]: 지도 이동/줌 정지 시마다 마커 재생성 (Zoom Level 반영)
  window.naver.maps.Event.addListener(naverMap.value, 'idle', createMarkers);

  createMarkers() // 마커 및 클러스터링 초기화
}


const performSearch = () => {
  // 1) 검색어에 맞춰 필터링된 리스트를 기준으로 마커 재생성
  createMarkers()

  // 2) 결과가 있으면 해당 핀들을 모두 담는 bounds로 화면 이동
  if (naverMap.value && filteredList.value.length > 0) {
    const bounds = new window.naver.maps.LatLngBounds()
    filteredList.value.forEach(it => bounds.extend(new window.naver.maps.LatLng(it.lat, it.lng)))
    naverMap.value.fitBounds(bounds)
  } else {
    showModal('검색 결과 없음', `'${searchQuery.value}'에 해당하는 위치를 찾지 못했어요. 다른 지역명을 입력해보세요.`, 'warning')
  }
}

const goBack = () => {
  router.go(-1)
}

const viewDetail = (item) => {
  showModal('상세 보기', `${item.title || item.name}의 상세 페이지로 이동합니다.`, 'info')
}


onMounted(() => {
  // Naver Map SDK 로드 확인 후 초기화
  if (window.naver) {
    setTimeout(() => {
      initMap();
    }, 500);
  } else {
    window.addEventListener('load', () => {
      setTimeout(() => {
        if (window.naver) initMap();
      }, 500);
    });
  }
})

// --- 지도 검색 필터 ---

const extractAreaKeyword = (raw) => {
  if (!raw) return ''
  const s = String(raw).trim()
  const tokens = s.split(/\s+/)
  const bySuffix = tokens.find(t => /[가-힣A-Za-z0-9]+(구|군|시)$/.test(t))
  if (bySuffix) return bySuffix.replace(/[^가-힣A-Za-z0-9]/g, '')

  return tokens[0].replace(/[^가-힣A-Za-z0-9]/g, '')
}

const areaKeyword = computed(() => extractAreaKeyword(searchQuery.value))

// 1) 타입 필터 + 2) 지역 키워드(부분 일치)까지 반영된 최종 리스트 (지리적 경계 필터링은 createMarkers에서 분리됨)
const filteredList = computed(() => {
  const kw = areaKeyword.value
  return allData.value.filter(item => {
    const typeOk = item.type === activeFilter.value

    // 🟢 검색어(kw)가 없으면 지역 필터링을 건너뛰고 타입만 확인 (전체 지역 데이터 표시)
    if (!kw || kw.length < 2) {
      return typeOk
    }

    // 검색어(kw)가 있으면 지역 일치 여부를 확인
    return typeOk && String(item.loc).includes(kw)
  })
})

// 하단 카드 뷰는 filteredList에서 지도 경계 내의 항목만 다시 필터링하여 사용
const currentDetailList = computed(() => {
  if (!naverMap.value) return [];
  const bounds = naverMap.value.getBounds();
  return filteredList.value.filter(item => {
    const latLng = new window.naver.maps.LatLng(item.lat, item.lng);
    return bounds.hasLatLng(latLng);
  });
})

</script>

<style scoped>
/* (Styles omitted for brevity, assume previous styles) */
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