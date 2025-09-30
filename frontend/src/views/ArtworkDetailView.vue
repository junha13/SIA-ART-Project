<!-- ArtworkDetailView.vue -->
<template>
  <div class="container py-3" v-if="item">
    <div class="d-flex align-items-center mb-3">
      <button class="btn btn-link p-0 me-2" @click="$router.back()">
        <i class="bi bi-arrow-left"></i>
      </button>
      <h5 class="fw-bold mb-0">작품 상세페이지</h5>
    </div>

    <div class="mb-2">
      <small class="text-muted"># {{ item.category }}</small>
    </div>

    <div class="card mb-3 border-0 shadow-sm bg-light">
      <div class="card-body p-2 d-flex justify-content-center align-items-center" style="min-height:300px;">
        <img :src="imgSrc" class="img-fluid rounded" :alt="item.title"
             style="max-height: 280px; object-fit: contain; background-color: #f8f9fa;" />
      </div>
    </div>

    <!-- info는 리스트 데이터에 없을 수 있으니 조건부 -->
    <div v-if="item.info" class="p-3 bg-light rounded mb-3">
      <h6 class="fw-bold mb-2">작품 정보</h6>
      <div class="d-flex flex-wrap gap-2">
        <span class="badge rounded-pill bg-secondary">재료: {{ item.info.material }}</span>
        <span class="badge rounded-pill bg-secondary">무게: {{ item.info.weight }}</span>
        <span class="badge rounded-pill bg-secondary">크기: {{ item.info.size }}</span>
        <span class="badge rounded-pill bg-secondary">제작년도: {{ item.info.year }}</span>
      </div>
    </div>

    <h4 class="fw-bold">{{ item.title }}</h4>
    <p class="text-muted mb-1">{{ item.subtitle }}</p>

    <div class="d-flex align-items-center mb-3">
      <img src="https://picsum.photos/100/100?random=2" class="rounded-circle me-2 border"
           alt="작가 프로필" style="width: 40px; height: 40px; object-fit: cover" />
      <div><p class="mb-0 fw-semibold">{{ item.artist }}</p></div>
    </div>

    <div class="p-3 bg-white rounded shadow-sm mb-3">
      <h6 class="fw-bold mb-2">상세 설명</h6>
      <p class="mb-0">{{ item.description || '설명이 없습니다.' }}</p>
    </div>
  </div>

  <div v-else class="container py-5 text-center">
    <p class="text-muted mb-3">해당 작품을 찾을 수 없습니다.</p>
    <button class="btn btn-dark" @click="$router.push('/')">목록으로</button>
  </div>
</template>


<script setup>
import { ref, watch, computed } from 'vue'
import { useRoute } from 'vue-router'

const route = useRoute()
const item = ref(null)

// item.image가 상대경로여도 안전하게 절대 URL로
const imgSrc = computed(() => {
  if (!item.value?.image) return ''
  const base = import.meta.env.BASE_URL || '/'
  // 이미 http(s)면 그대로, 아니면 절대 URL로 변환
  return /^https?:\/\//i.test(item.value.image)
    ? item.value.image
    : new URL(item.value.image, window.location.origin + base).href
})

// 라우터 state → sessionStorage 순으로 복구
const restore = () => {
  const id = Number(route.params.id)

  // 1) history.state에 실려온 데이터 우선
  const st = window.history.state
   if (st?.artwork && Number(st.artwork.id) === id) {
    item.value = st.artwork
    return
  }
  try {
    const raw = sessionStorage.getItem('lastArtwork')
    const cached = raw && JSON.parse(raw)
    if (cached && Number(cached.id) === id) {
      item.value = cached
      return
    }
  } catch (e) {
    // 무시
  }

  // 3) 실패 시
  item.value = null
}

restore()
watch(() => route.params.id, restore)
</script>

