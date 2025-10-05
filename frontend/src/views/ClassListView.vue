<template>
  <div class="bg-white min-vh-100 d-flex flex-column mt-20">
    <main class="container py-4 flex-grow-1">
      <h2 class="fw-bold text-dark mb-3">클래스</h2>
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

      <div class="input-group mb-4">
        <input
            v-model="searchQuery"
            type="text"
            class="form-control rounded-start"
            placeholder="클래스 이름, 장소 등 검색"
            @keyup.enter="searchClasses"
        />
        <button class="btn btn-outline-dark rounded-end" @click="searchClasses" aria-label="검색">
          <i class="fas fa-search"></i>
        </button>
      </div>

      <div v-if="isLoading" class="text-center py-5">
        <div class="spinner-border text-dark" role="status">
          <span class="visually-hidden">Loading...</span>
        </div>
      </div>

      <div v-else-if="filteredClasses.length === 0" class="text-center py-5">
        <i class="fas fa-chalkboard fs-1 text-muted mb-3"></i>
        <p class="text-muted mb-0">등록된 클래스가 없습니다.</p>
      </div>

      <div v-else class="d-flex flex-column gap-3">
        <div
            v-for="cls in filteredClasses" :key="cls.classNumber"
            class="border border-2 rounded-4 p-3 d-flex align-items-center"
            @click="viewClass(cls.classNumber)" style="cursor: pointer;"
        >
          <img :src="cls.classImage" :alt="cls.className"
               class="rounded-4 me-3" width="72" height="72" style="object-fit: cover;"/>

          <div class="flex-grow-1">
            <div class="fw-bold text-dark">
              {{ cls.className }}
              <small class="ms-1 text-muted">{{ cls.durationWeeks }}주 · {{ cls.location }}</small>
            </div>
            <div class="small text-muted text-truncate">
              {{ cls.subtitle }}
            </div>

            <div class="d-flex align-items-center mt-2">
              <small class="text-dark">강사 : {{ cls.instructor }}</small>
            </div>
          </div>

        </div>
      </div>

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
/**
 * 필요한 모듈(vue, vue-router)과 외부 컴포넌트, Pinia 스토어를 가져옵니다.
 */
import {computed, onMounted, ref, watch} from 'vue'
import {useRouter} from 'vue-router'
import ConfirmModal from '../components/ConfirmModal.vue'
import {useClassStore} from "@/stores/useClassStore.js";

/**
 * Vue Router와 Pinia Store 인스턴스를 초기화하여 컴포넌트 내에서 사용할 수 있도록 준비합니다.
 */
const router = useRouter()
const classStore = useClassStore()

/**
 * 모달(팝업) 창의 상태(표시 여부, 제목, 메시지 등)를 관리하는 변수와 함수를 정의합니다.
 */
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

/**
 * 클래스 목록 필터링 및 검색을 위한 상태 변수를 정의합니다.
 * 사용자가 선택한 카테고리(`selectedCategory`)와 입력한 검색어(`searchQuery`)를 저장합니다.
 */
const categories = ['전체', '순수 미술', '디자인 & 공예', '사진 & 영상', '디지털 아트', '공연 예술', '확장 예술', '기타']
const selectedCategory = ref('전체')
const searchQuery = ref('')

/**
 * Pinia Store의 상태(state)를 컴포넌트 내부에서 실시간으로 사용하기 위해
 * computed 속성으로 연결합니다. Store의 데이터가 바뀌면 화면도 자동으로 업데이트됩니다.
 */
const classes = computed(() => classStore.classes)
const isLoading = computed(() => classStore.classesLoading)

/**
 * Store에서 가져온 클래스 목록을 화면에 표시하기 위한 computed 속성입니다.
 * 필터링 로직은 백엔드에서 처리하므로, Store의 `getClassList` getter를 그대로 반환합니다.
 */
const filteredClasses = computed(() => {
  return classStore.getClassList
})

/**
 * 선택된 카테고리와 검색어를 객체 형태로 Pinia Store(useClassStore)의
 * `fetchClasses` 액션에 전달하여 API로부터 데이터를 가져오도록 요청하는 함수입니다.
 */
const loadClasses = () => {
  classStore.fetchClasses({
    category: selectedCategory.value !== '전체' ? selectedCategory.value : undefined,
    query: searchQuery.value.trim()
  })
}

/**
 * 검색 버튼을 클릭하거나 검색창에서 Enter 키를 눌렀을 때 `loadClasses` 함수를 호출하는 역할을 합니다.
 */
const searchClasses = () => {
  loadClasses()
}

/**
 * `selectedCategory` 또는 `searchQuery`의 값이 변경될 때마다 자동으로
 * `loadClasses` 함수를 호출하여 실시간으로 클래스 목록을 다시 불러옵니다.
 */
watch([selectedCategory, searchQuery], () => {
  loadClasses()
})

/**
 * Vue의 생명주기 훅(Lifecycle Hook)으로,
 * 컴포넌트가 화면에 처음 렌더링될 때 초기 클래스 목록을 불러오기 위해 사용됩니다.
 */
onMounted(() => {
  loadClasses()
})

/**
 * 사용자가 클래스 목록의 특정 항목을 클릭했을 때,
 * 해당 클래스의 고유 ID를 받아 상세 페이지('/class/{id}')로 이동시키는 함수입니다.
 */
const viewClass = (id) => router.push(`/class/${id}`)
</script>