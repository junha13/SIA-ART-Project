<template>
  <div v-if="showSplash" class="splash-screen d-flex flex-column justify-content-center align-items-center">

    <div class="splash-text-container text-start">
      <div class="line mb-2">
        <span class="emphasized">예</span>
        <span class="subtle">술을</span>
      </div>
      <div class="line mb-2">
        <span class="emphasized">찾</span>
        <span class="subtle">는</span>
      </div>
      <div class="line">
        <span class="emphasized">사</span>
        <span class="subtle">람들</span>
      </div>
    </div>

    <div class="splash-arrow">
      <i class="fas fa-chevron-up"></i>
    </div>
  </div>

  <div v-else class="main-content-page container-fluid">

    <div class="custom-header-container position-relative overflow-hidden">
      <div class="custom-header-bg-gradient"></div>

      <div class="header-overlay px-4 py-3 d-flex align-items-center justify-content-between">

        <div class="header-title-container mb-0">
          <div class="line">
            <span class="emphasized text-white">예</span>
            <span class="subtle">술을</span>
          </div>
          <div class="line">
            <span class="emphasized text-white">찾</span>
            <span class="subtle">는</span>
          </div>
          <div class="line">
            <span class="emphasized text-white">사</span>
            <span class="subtle">람들</span>
          </div>
        </div>

        <button class="btn btn-icon p-3 three-dots-btn">
          <i class="fas fa-ellipsis-v fs-3"></i>
        </button>
      </div>

      <div class="location-select-top d-flex align-items-center cursor-pointer" @click="handleLocationSelect">
        <i class="fas fa-map-marker-alt text-white fs-5"></i>
        <span class="ms-2 fw-bold text-white fs-6">우리 동네</span>
        <i class="fas fa-chevron-down ms-1 text-white fs-6"></i>
      </div>

    </div>

    <div class="main-cards-section container-fluid px-0 py-3">
      <div class="row g-3">

        <div class="col-7 d-flex flex-column">
          <div class="card-item archive-card flex-fill mb-3" @click="goTo('/archive')">
            <span class="card-text">지역예술<br>아카이브</span>
          </div>
          <div class="card-item store-card flex-fill" @click="goTo('/store')">
            <span class="card-text">상점</span>
          </div>
        </div>

        <div class="col-5 d-flex flex-column">
          <div class="card-item artist-card mb-3" style="height: 180px;" @click="goTo('/artists')">
            <span class="card-text">예술가</span>
          </div>
          <div class="card-item class-card mb-3" style="height: 180px;" @click="goTo('/classes')">
            <span class="card-text">클래스</span>
          </div>
          <div class="card-item community-card" style="height: 180px;" @click="goTo('/board')">
            <span class="card-text">커뮤니티</span>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted } from 'vue';
import { useRouter } from 'vue-router';
import { useAppStore } from '@/stores/useAppStore';
import { useLocationStore } from '@/stores/useLocationStore'; // 위치 스토어 사용

const router = useRouter();
const appStore = useAppStore();
const locationStore = useLocationStore(); // 위치 스토어 초기화

const showSplash = ref(true);
const SPLASH_DURATION = 2500; // 2.5초

onMounted(() => {
  appStore.setShowLayout(false);

  setTimeout(() => {
    showSplash.value = false;
    appStore.setShowLayout(true);
  }, SPLASH_DURATION);
});

onUnmounted(() => {
  appStore.setShowLayout(true);
});

const goTo = (path) => {
  router.push(path);
};

/**
 * [수정된 기능]: '우리 동네' 버튼 클릭 핸들러
 * LocationSettingsView의 '내 위치로' 버튼과 동일하게 Geolocation API를 호출하여
 * 현재 위치를 받아오고 Pinia Store에 저장합니다. (화면 이동 없음)
 */
const handleLocationSelect = () => {
  console.log("'우리 동네' 클릭: 현재 위치 설정 기능 실행");

  // Geolocation 지원 확인
  if (navigator.geolocation) {
    navigator.geolocation.getCurrentPosition(
        (pos) => {
          const lat = pos.coords.latitude;
          const lng = pos.coords.longitude;

          // Pinia Store 액션 호출: 현재 위치 업데이트
          locationStore.updateCurrentLocation({
            name: '현재 위치',
            address: `위도: ${lat.toFixed(4)}, 경도: ${lng.toFixed(4)} (GPS)`,
            lat, lng
          });

          // 사용자에게 알림 (ConfirmModal이나 토스트 메시지를 사용해야 하지만, 여기서는 콘솔로 대체)
          console.log(`✅ 위치 업데이트 성공: Lat ${lat}, Lng ${lng}`);
          alert('현재 위치로 동네가 설정되었습니다!'); // 임시 알림
        },
        (error) => {
          console.error("❌ 위치 정보 접근 실패:", error);
          alert('위치 정보 접근을 허용해주세요.'); // 임시 알림
        }
    );
  } else {
    alert('이 브라우저는 위치 정보(Geolocation)를 지원하지 않습니다.');
  }
};
</script>

<style scoped>
/* ================================================================= */
/* ⚠️ 순수 CSS 최소화: Metronic/Bootstrap 유틸리티 클래스로 대체 불가능한 부분만 유지 */
/* ================================================================= */

/* --- 1. 스플래시 스크린 스타일 (유지) --- */
.splash-screen {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100vh;
  background: linear-gradient(to bottom, #333333 0%, #bbbbbb 70%, #f0f0f0 100%);
  z-index: 1040;
}
.splash-text-container {
  font-size: 44px;
  font-weight: 700;
  line-height: 1.2;
  padding-left: 20px;
  text-shadow: 0 5px 10px rgba(0, 0, 0, 0.5);
}
.splash-text-container .line {
  white-space: nowrap;
}
.splash-text-container .emphasized {
  color: white;
  font-weight: 800;
}
.splash-text-container .subtle {
  color: rgba(255, 255, 255, 0.4);
  font-weight: 600;
}
.splash-arrow {
  position: absolute;
  bottom: 30px;
  color: white;
  opacity: 0.5;
  font-size: 24px;
  animation: bounce 2s infinite;
}
@keyframes bounce {
  0%, 20%, 50%, 80%, 100% { transform: translateY(0); }
  40% { transform: translateY(-10px); }
  60% { transform: translateY(-5px); }
}

/* --- 2. 메인 콘텐츠 상단 디자인 --- */
/* [수정]: 양옆 패딩을 주기 위해 p-0을 제거하고, 대신 container-fluid에 p-4와 같은 Metronic 패딩 클래스를 부여한다고 가정하고 CSS를 조정합니다. */
.main-content-page {
  margin-top: 0;
  background-color: #ffffff;
  min-height: 100%;
  /* [추가]: 모바일 앱 느낌을 위해 양옆에 여백(p-4)이 있다고 가정하고 그 여백을 메꾸도록 margin-x를 음수로 설정합니다. */
  margin-left: -1rem;
  margin-right: -1rem;
}

.custom-header-container {
  height: 325px;
  position: relative;
}
/* [추가]: 사다리꼴 배경 그라데이션 */
.custom-header-bg-gradient {
  position: absolute;
  top: 0;
  left: 0;
  width: 190%;
  height: 85%;

  /* 보라색 계열 그라데이션 적용 */
  background: linear-gradient(to top, #FFFFFF 0%, #000000 95%);
  transform: skewY(-8deg);
  transform-origin: top left;
  box-shadow: 0 5px 15px rgba(0, 0, 0, 0); /* 그림자 추가 */
}

/* header-overlay (제목 영역) 스타일은 유지 */
.header-overlay {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 55px;
  z-index: 1002;
}
.header-title-container {
  font-size: 34px;
  font-weight: bold;
  line-height: 1.1;
  text-shadow: 0 2px 4px rgba(0, 0, 0, 0.5);
  padding-top: 320px;
}
.header-title-container .line {
  line-height: 1.1;
  white-space: nowrap;
  display: block;
}
.header-title-container .emphasized {
  font-weight: 800;
}
.header-title-container .subtle {
  color: rgba(255, 255, 255, 0.4);
  font-weight: 600;
}
.three-dots-btn {
  background: transparent !important;
  border: none !important;
  color: white !important;
}
.location-select-top {
  position: absolute;
  bottom: 40px;
  left: 30px;
  z-index: 10;
  opacity: 0.9;
}
.main-cards-section {
  /* 카드와 상단 도형의 겹침 마진 */
  margin-top: -20px;
  position: relative;
  z-index: 20;
  background-color: transparent !important;
}

/* --- 3. 카드 섹션 스타일 (유지) --- */
.card-item {
  min-height: 140px;
  border-radius: 12px;
  overflow: hidden;
  position: relative;
  background-size: cover;
  background-position: center;
  color: white;
  font-weight: bold;
  cursor: pointer;
  display: flex;
  align-items: flex-end;
  padding: 1rem;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
}
.card-item::before {
  content: "";
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.3);
  z-index: 1;
}
.card-text {
  font-size: 1.2rem;
  line-height: 1.2;
  z-index: 2;
}
.archive-card {
  min-height: 250px;
}

/* 카드별 배경 이미지 및 필터 */
.archive-card {
  background-image: url('@/public/images/art/art1.png');
  filter: grayscale(80%) brightness(1.1);
}
.artist-card {
  background-image: url('@/public/images/art/art2.png');
  filter: grayscale(80%) brightness(1.1);
}
.class-card {
  background-image: url('@/public/images/art/art3.png');
  filter: grayscale(80%) brightness(1.1);
}
.store-card {
  background-image: url('@/public/images/art/art4.png');
  filter: grayscale(80%) brightness(1.1);
}
.community-card {
  background-image: url('@/public/images/art/art5.png');
  filter: grayscale(80%) brightness(1.1);
}
</style>