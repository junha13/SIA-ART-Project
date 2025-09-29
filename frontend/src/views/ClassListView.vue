<template>
  <div class="container py-5">
    <!-- Header -->
    <div class="d-flex align-items-center mb-4">
      <button class="btn btn-light btn-sm me-3" @click="$router.back()">
        <i class="fas fa-chevron-left"></i>
      </button>
      <h1 class="fs-3 fw-bold mb-0">예술 클래스</h1>
    </div>

    <!-- Filters -->
    <div class="d-flex flex-wrap gap-2 mb-4">
      <button v-for="cat in categories" :key="cat"
              class="btn btn-outline-dark btn-sm"
              :class="{ active: selectedCategory === cat }"
              @click="selectedCategory = cat">
        {{ cat }}
      </button>
    </div>

    <!-- Search -->
    <div class="input-group mb-4">
      <input v-model="searchQuery" type="text" class="form-control" placeholder="클래스 검색" @keyup.enter="searchClasses"/>
      <button class="btn btn-primary" @click="searchClasses">검색</button>
    </div>

    <!-- Empty State -->
    <div v-if="filteredClasses.length === 0" class="text-center py-10">
      <i class="fas fa-chalkboard fs-1 text-muted mb-3"></i>
      <p class="text-muted">등록된 클래스가 없습니다.</p>
    </div>

    <!-- Class Cards -->
    <div v-else class="row g-4">
      <div v-for="cls in filteredClasses" :key="cls.id" class="col-md-4 col-sm-6">
        <div class="card h-100 shadow-sm cursor-pointer" @click="viewClass(cls.id)">
          <img :src="cls.image" class="card-img-top" :alt="cls.title"/>
          <div class="card-body">
            <h5 class="fw-bold mb-1">{{ cls.title }}</h5>
            <p class="text-muted small mb-2">{{ cls.instructor }}</p>
            <p class="small text-truncate">{{ cls.shortDescription }}</p>
          </div>
          <div class="card-footer text-muted small">
            <i class="fas fa-calendar-alt me-1"></i> {{ cls.date }}
          </div>
        </div>
      </div>
    </div>

    <!-- Custom Modal -->
    <ConfirmModal v-model:isVisible="isModalVisible" :title="modalTitle" :message="modalMessage" :type="modalType" :autoHide="true"/>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import { useRouter } from 'vue-router'
import ConfirmModal from '../components/ConfirmModal.vue'

const router = useRouter()

// Modal State
const isModalVisible = ref(false)
const modalTitle = ref('')
const modalMessage = ref('')
const modalType = ref('info')

const showModal = (title, message, type = 'info', autoHide = true) => {
  modalTitle.value = title
  modalMessage.value = message
  modalType.value = type
  isModalVisible.value = true
}

const categories = ["전체", "미술", "음악", "도예", "공예"]
const selectedCategory = ref("전체")
const searchQuery = ref("")

const classes = ref([
  { id: 1, title: '현대미술 클래스', instructor: '김예술', shortDescription: '현대미술의 기초와 창작 방법을 배웁니다.', date: '2025-10-05', category: '미술', image: 'https://placehold.co/400x250/A9F5A9/000?text=Class+1' },
  { id: 2, title: '추상화 워크숍', instructor: '박화가', shortDescription: '색채와 형태를 활용한 추상화 기법을 익힙니다.', date: '2025-10-12', category: '미술', image: 'https://placehold.co/400x250/F8C471/000?text=Class+2' }
])

const filteredClasses = computed(() => {
  return classes.value.filter(cls => {
    const matchCategory = selectedCategory.value === "전체" || cls.category === selectedCategory.value
    const matchSearch = cls.title.includes(searchQuery.value) || cls.instructor.includes(searchQuery.value)
    return matchCategory && matchSearch
  })
})

const searchClasses = () => {
  if (searchQuery.value.trim()) {
    showModal('검색 완료', `"${searchQuery.value}"에 대한 클래스 검색을 실행합니다.`, 'info')
  }
}

const viewClass = (id) => {
  router.push(`/class/${id}`)
}
</script>
