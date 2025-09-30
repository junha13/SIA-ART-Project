<template>
  <div class="bg-white min-vh-100 d-flex flex-column">


    <!-- 본문 -->
    <main class="container py-4 flex-grow-1">
      <!-- 페이지 타이틀 -->
      <h2 class="fw-bold text-dark mb-3">클래스</h2>

      <!-- 필터 칩 -->
      <div class="d-flex flex-wrap gap-2 mb-3">
        <button
          v-for="cat in categories" :key="cat"
          class="btn btn-sm rounded-pill"
          :class="selectedCategory === cat ? 'btn-dark text-white' : 'btn-light border text-dark'"
          @click="selectedCategory = cat"
        >
          {{ cat }}
        </button>
      </div>

      <!-- 위치/검색 -->
      <div class="input-group mb-4">
        <input
          v-model="searchQuery"
          type="text"
          class="form-control rounded-start"
          placeholder="구로구 구로동"
          @keyup.enter="searchClasses"
        />
        <button class="btn btn-outline-dark rounded-end" @click="searchClasses" aria-label="검색">
          <i class="fas fa-search"></i>
        </button>
      </div>

      <!-- 비어있음 -->
      <div v-if="filteredClasses.length === 0" class="text-center py-5">
        <i class="fas fa-chalkboard fs-1 text-muted mb-3"></i>
        <p class="text-muted mb-0">등록된 클래스가 없습니다.</p>
      </div>

      <!-- 리스트형 카드 -->
      <div v-else class="d-flex flex-column gap-3" >
        <div
          v-for="cls in filteredClasses" :key="cls.id"
          class="border border-2 rounded-4 p-3 d-flex align-items-center" 
          @click="viewClass(cls.id)"
        >
          <!-- 썸네일 -->
          <img :src="cls.image" :alt="cls.title"
               class="rounded-4 me-3" width="72" height="72" />

          <!-- 본문 -->
          <div class="flex-grow-1">
            <div class="fw-bold text-dark">
              {{ cls.title }}
              <small class="ms-1 text-muted">{{ cls.duration }} · {{ cls.location }}</small>
            </div>
            <div class="small text-muted">
              {{ cls.shortDescription }}
            </div>

            <!-- 강사 -->
            <div class="d-flex align-items-center mt-2">
              <img :src="cls.instructorAvatar" alt="instructor" width="24" height="24" class="rounded-circle me-2" />
              <small class="text-dark">{{ cls.instructor }}</small>
            </div>
          </div>

        </div>
      </div>

      <!-- 모달 -->
      <ConfirmModal
        v-model:isVisible="isModalVisible"
        :title="modalTitle"
        :message="modalMessage"
        :type="modalType"
        :autoHide="true"
      />
    </main>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import { useRouter } from 'vue-router'
import ConfirmModal from '../components/ConfirmModal.vue'

const router = useRouter()

// Modal
const isModalVisible = ref(false)
const modalTitle = ref('')
const modalMessage = ref('')
const modalType = ref('info')
const showModal = (title, message, type='info') => {
  modalTitle.value = title
  modalMessage.value = message
  modalType.value = type
  isModalVisible.value = true
}

// 필터/검색
const categories = ['전체', '미술', '음악', '도예', '공예']
const selectedCategory = ref('미술')   // 스샷처럼 기본 '미술'
const searchQuery = ref('')

// 데이터 (기존 구조 + 화면 메타 보완: duration/location/instructorAvatar)
const classes = ref([
  {
    id: 1,
    title: '수채화 기초',
    instructor: '김준하',
    instructorAvatar: 'https://via.placeholder.com/48x48.png?text=KJ',
    shortDescription: '처음 시작하는 수채화, 기본기부터 차근차근',
    date: '2025-10-05',
    duration: '일일',
    location: '구로구 구로동',
    category: '미술',
    image: 'l'
  },
  {
    id: 2,
    title: '유화 마스터',
    instructor: '허지서',
    instructorAvatar: 'https://via.placeholder.com/48x48.png?text=HJ',
    shortDescription: '전문가를 위한 고급 유화 기법',
    date: '2025-10-12',
    duration: '4주',
    location: '구로구 구로동',
    category: '미술',
    image: ''
  },
  {
    id: 3,
    title: '클래식 기초반',
    instructor: '박연주',
    instructorAvatar: 'https://via.placeholder.com/48x48.png?text=PY',
    shortDescription: '처음 배우는 클래식 음악 이론',
    date: '2025-11-01',
    duration: '2주',
    location: '양천구 목동',
    category: '음악',
    image: ''
  }
])

// 필터링
const filteredClasses = computed(() => {
  const q = searchQuery.value.trim()
  return classes.value.filter(c => {c
    const byCat = selectedCategory.value === '전체' || c.category === selectedCategory.value
    const byQ =
      !q ||
      c.title.includes(q) ||
      c.instructor.includes(q) ||
      (c.location && c.location.includes(q))
    return byCat && byQ
  })
})

// 액션
const searchClasses = () => {
  if (searchQuery.value.trim()) {
    showModal('검색', `"${searchQuery.value}"로 검색했습니다.`, 'info')
  }
}
const viewClass = (id) => router.push(`/class/${id}`)
</script>