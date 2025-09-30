<!-- ArtworkDetailView.vue -->
<template>
  <div class="container py-3">
    <!-- 상단 헤더 -->
    <div class="d-flex align-items-center mb-3">
      <button class="btn btn-link p-0 me-2" @click="$router.back()">
        <i class="bi bi-arrow-left"></i>
      </button>
      <h5 class="fw-bold mb-0">작품 상세페이지</h5>
    </div>

    <!-- 카테고리 -->
    <div class="mb-2">
      <small class="text-muted"># {{ artwork.category }}</small>
    </div>

    <!-- 작품 이미지 (Metronic 카드 스타일 적용) -->
    <div class="card mb-3 border-0 shadow-sm bg-light">
      <div class="card-body p-2 d-flex justify-content-center align-items-center" style="min-height:300px;">
        <img
          :src="artwork.image"
          class="img-fluid rounded"
          alt="artwork.title"
          style="max-height: 280px; object-fit: contain; background-color: #f8f9fa;"
        />
      </div>
    </div>

    <!-- 작품 정보 -->
    <div class="p-3 bg-light rounded mb-3">
      <h6 class="fw-bold mb-2">작품 정보</h6>
      <div class="d-flex flex-wrap gap-2">
        <span class="badge rounded-pill bg-secondary">재료: {{ artwork.info.material }}</span>
        <span class="badge rounded-pill bg-secondary">무게: {{ artwork.info.weight }}</span>
        <span class="badge rounded-pill bg-secondary">크기: {{ artwork.info.size }}</span>
        <span class="badge rounded-pill bg-secondary">제작년도: {{ artwork.info.year }}</span>
      </div>
    </div>

    <!-- 작품 기본 -->
    <h4 class="fw-bold">{{ artwork.title }}</h4>
    <p class="text-muted mb-1">{{ artwork.subtitle }}</p>

    <!-- 작가 정보 -->
    <div class="d-flex align-items-center mb-3">
      <img
        :src="artwork.artist.profile"
        class="rounded-circle me-2 border"
        alt="작가 프로필"
        style="width: 40px; height: 40px; object-fit: cover"
      />
      <div>
        <p class="mb-0 fw-semibold">{{ artwork.artist.name }}</p>
      </div>
    </div>

    <!-- 상세 설명 -->
    <div class="p-3 bg-white rounded shadow-sm mb-3">
      <h6 class="fw-bold mb-2">상세 설명</h6>
      <p class="mb-0">{{ artwork.description }}</p>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRoute } from 'vue-router'

const route = useRoute()
const artworkId = route.params.id

// 더미 데이터
const artwork = ref({
  id: artworkId,
  title: '엄청난 작품',
  subtitle: '느슨한 예술계를 뒤집어놓았다',
  image: 'https://picsum.photos/600/400?random=1', // 예시: Bootstrap/Picsum 랜덤 이미지
  category: '미술',
  artist: {
    name: '허지서',
    profile: 'https://picsum.photos/100/100?random=2' // 예시: 프로필 사진
  },
  info: {
    size: '45cm x 53cm',
    material: '캔버스 유화물감',
    year: '2023',
    weight: '1.5kg'
  },
  description: '이건 진짜 엄청난 명작이다.'
})
</script>
