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



        <!-- ★ 여기로 버튼 이동: 카드 영역 우하단에 겹친다 -->
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
import axios from 'axios'
import { useLocationStore } from '@/stores/useLocationStore';


const router = useRouter()
const locationStore = useLocationStore();
const naverMap = ref(null)
const markerClustering = ref(null)
const markers = ref([])
const activeFilter = ref('art')
const searchQuery = ref(locationStore.currentLocation.name);

// Modal State
const isModalVisible = ref(false)
const modalTitle = ref('')
const modalMessage = ref('')
const modalType = ref('info')
const showModal = (title, message, type = 'info') => {
  modalTitle.value = title;
  modalMessage.value = message;
  modalType.value = type;
  isModalVisible.value = true;
};

// 필터 (예술작품, 클래스, 갤러리/전시회)
const filters = [
  { key: 'art', label: '예술작품' },
  { key: 'studio', label: '클래스' },
  { key: 'gallery', label: '갤러리' },
]

// --- 더미 데이터 확장 (총 21개: art 8, studio 6, gallery 7) ---
///--- 하드코딩 지우고 백엔드 가져오기--
const allData = ref([])
///--- 로딩-----
const isDataLoading = ref(true)
// --- 더미 데이터 확장 끝 ---

// --- Map & Clustering Logic ---

const createMarkers = () => {
  if (!naverMap.value) return;
  const bounds = naverMap.value.getBounds();

  // 1. 기존 클러스터 인스턴스 정리
  if (markerClustering.value) {
    markerClustering.value.setMap(null);
  }
  markers.value = [];

  // 2. 지도 경계 내에 표시할 데이터 필터링
  const dataToMap = filteredList.value.filter(item => {
    const latLng = new window.naver.maps.LatLng(item.lat, item.lng);
    return bounds.hasLatLng(latLng);
  });

  // 3. Vue의 반응형 배열(markers.value)에 마커 임시 추가
  dataToMap.forEach(item => {
    const marker = new window.naver.maps.Marker({
      position: new window.naver.maps.LatLng(item.lat, item.lng),
      title: item.title || item.name,
    });
    window.naver.maps.Event.addListener(marker, 'click', () => {
      naverMap.value.setCenter(marker.getPosition());
    });
    marker.dataId = item.id;
    markers.value.push(marker);
  });

  // 4. 클러스터링 생성
  if (naverMap.value && markers.value.length > 0) {
    if (window.MarkerClustering) {
      try {
        // --- 👇 [핵심 수정] ---
        // Vue의 프록시를 완전히 제거하기 위해, 순수한 새 마커 배열을 '깊은 복사'하여 생성합니다.
        const rawMarkers = markers.value.map(proxyMarker => {
          const originalMarker = toRaw(proxyMarker);
          const newMarker = new window.naver.maps.Marker({
            position: originalMarker.getPosition(),
            title: originalMarker.getTitle(),
          });
          // 필요한 사용자 정의 데이터가 있다면 같이 복사합니다.
          newMarker.dataId = originalMarker.dataId;
          return newMarker;
        });

        markerClustering.value = new window.MarkerClustering({
          map: toRaw(naverMap.value),
          markers: rawMarkers, // 👈 복제된 '순수 배열'을 라이브러리에 전달
          maxZoom: 14,
          minClusterSize: 2,
          icons: [
            {
              content: `<div style="background:rgba(0,128,255,0.6);color:white;
                 border-radius:50%;width:40px;height:40px;display:flex;
                 align-items:center;justify-content:center;">{text}</div>`,
              size: new naver.maps.Size(40, 40),
              anchor: new naver.maps.Point(20, 20)
            },
            {
              content: `<div style="background:rgba(255,0,0,0.6);color:white;
                 border-radius:50%;width:50px;height:50px;display:flex;
                 align-items:center;justify-content:center;">{text}</div>`,
              size: new naver.maps.Size(50, 50),
              anchor: new naver.maps.Point(25, 25)
            }
          ],
          styles: [
            { // 클러스터 마커 개수가 10개 미만일 때 (파란색 원)
              content: '<div style="cursor:pointer; width:40px; height:40px; line-height:42px; font-size:14px; color:white; text-align:center; font-weight:bold; background:rgba(0, 123, 255, 0.8); border-radius:50%;"><span>{text}</span></div>',
              anchor: new window.naver.maps.Point(20, 20)
            },
            { // 클러스터 마커 개수가 10개 이상 100개 미만일 때 (초록색 원)
              content: '<div style="cursor:pointer; width:50px; height:50px; line-height:52px; font-size:16px; color:white; text-align:center; font-weight:bold; background:rgba(40, 167, 69, 0.8); border-radius:50%;"><span>{text}</span></div>',
              anchor: new window.naver.maps.Point(25, 25)
            },
          ],
        });
        console.log('[createMarkers] MarkerClustering 생성 성공!');
        // --- 核心 수정 끝 ---

      } catch (e) {
        console.error('[createMarkers] MarkerClustering 생성 중 에러 발생!', e);
      }
    } else {
      // 클러스터링 라이브러리가 없을 경우, 원본 마커를 지도에 표시
      markers.value.forEach(m => m.setMap(naverMap.value));
      console.warn("MarkerClustering 라이브러리가 로드되지 않았습니다.");
    }
  }
};
const initMap = () => {
  console.log('[initMap] 함수 실행 시작');
  if (!window.naver || !window.naver.maps) {
    console.error('[initMap] naver.maps 객체가 존재하지 않아 함수를 중단합니다.');
    return;
  }

  const centerLat = locationStore.currentLocation.lat;
  const centerLng = locationStore.currentLocation.lng;

  console.log('[initMap] 지도 객체 생성 시도...');
  naverMap.value = new window.naver.maps.Map('archiveMap', {
    center: new window.naver.maps.LatLng(centerLat, centerLng),
    zoom: 13,
  });
  console.log('[initMap] 지도 객체 생성 완료:', naverMap.value);

  // 'idle' 이벤트는 지도가 처음 준비되었을 때, 그리고 이동이 멈췄을 때 발생합니다.
  window.naver.maps.Event.addListener(naverMap.value, 'idle', () => {
    console.log('[idle Event] 지도 idle 이벤트 발생 -> createMarkers() 호출');
    createMarkers();
  });
};

const performSearch = () => {
  createMarkers()
  if (naverMap.value && filteredList.value.length > 0) {
    const bounds = new window.naver.maps.LatLngBounds()
    filteredList.value.forEach(it => bounds.extend(new window.naver.maps.LatLng(it.lat, it.lng)))
    naverMap.value.fitBounds(bounds)
  } else {
    showModal('검색 결과 없음', `'${searchQuery.value}'에 해당하는 위치를 찾지 못했어요. 다른 지역명을 입력해보세요.`, 'warning')
  }
}

const viewDetail = (item) => {
  showModal('상세 보기', `${item.title || item.name}의 상세 페이지로 이동합니다.`, 'info')
}


onMounted(() => {
  console.log('[onMounted] 컴포넌트 마운트됨 -> initMap() 호출');
  initMap();
});

// --- 지도 검색 필터 ---
const extractAreaKeyword = (raw) => {
  if (!raw) return ''
  const s = String(raw).trim()
  const tokens = s.split(/\s+/)
  const bySuffix = tokens.find(t => /[가-힣A-Za-z0-9]+(구|군|시)$/.test(t))
  if (bySuffix) return bySuffix.replace(/[^가-힣A-Za-z0-9]/g, '')
  return tokens[0].replace(/[^가-힣A-Za-z0-9]/g, '')
}

// 현재 검색어에서 키워드 추출 (ex: '구로구 구로동' -> '구로구')
const areaKeyword = computed(() => extractAreaKeyword(searchQuery.value))

// 1) 타입 필터 + 2) 지역 키워드(부분 일치)까지 반영된 최종 리스트
const filteredList = computed(() => {
  const kw = areaKeyword.value
  return allData.value.filter(item => {
    const typeOk = item.type === activeFilter.value
    if (!kw || kw.length < 2) {
      return typeOk
    }
    return typeOk && String(item.loc).includes(kw)
  })
})

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