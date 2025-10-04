<template>
  <div class="bg-white min-vh-100 d-flex flex-column mt-20">
    <!-- 본문 -->
    <main class="container py-4 flex-grow-1">
      <!-- 페이지 타이틀 -->
      <h2 class="fw-bold text-dark mb-3">클래스</h2>
      <!-- 클래사 필터 -->
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
      <div v-else class="d-flex flex-column gap-3">
        <div
            v-for="cls in filteredClasses" :key="cls.id"
            class="border border-2 rounded-4 p-3 d-flex align-items-center"
            @click="viewClass(cls.id)"
        >
          <!-- 썸네일 -->
          <img :src="cls.image" :alt="cls.title"
               class="rounded-4 me-3" width="72" height="72"/>

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
              <small class="text-dark">강사 : {{ cls.instructor }}</small>
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
import {computed, onMounted, ref, watch} from 'vue'
import {useRouter} from 'vue-router'
// 외부 모달 컴포넌트 import
import ConfirmModal from '../components/ConfirmModal.vue'
import {useClassStore} from "@/stores/useClassStore.js";

const router = useRouter()
const classStore = useClassStore()

// --- Modal 상태 관리 ---
const isModalVisible = ref(false) // 모달 표시 여부
const modalTitle = ref('')        // 모달 제목
const modalMessage = ref('')      // 모달 내용
const modalType = ref('info')     // 모달 타입 (e.g., info, success, confirm)
// 모달을 띄우는 함수
const showModal = (title, message, type = 'info') => {
  modalTitle.value = title
  modalMessage.value = message
  modalType.value = type
  isModalVisible.value = true
}

// --- 필터/검색 상태 ---
// 사용 가능한 카테고리 목록
const categories = ['전체', '순수 미술', '디자인 & 공예', '사진 & 영상', '디지털 아트', '공연 예술', '확장 예술', '기타'] // 클래스 기본 카테고리 설정
const selectedCategory = ref('전체') // 현재 선택된 카테고리 (기본값 '전체')
const searchQuery = ref('') // 검색 입력 필드 값 (클래스의 제목, 강사, 위치를 검색에 사용)


/**
 * 클래스 집합
 * 컬럼 - class_number,class_name, class_description, class_image, user_number, location, class_category_number
 * 날짜 , 일일,몇일짜리인지
 */

// 클래스 데이터 API 연결
// 🟢 Store의 상태를 computed로 가져와 사용
const classes = computed(() => classStore.classes) // classStore에서 데이터 가져옴
const isLoading = computed(() => classStore.classesLoading) // 로딩 상태 가져옴

// --- 클래스 카테고리 필터링 (로직 변경) ---

// 🟢 필터링 로직 변경: 필터링은 백엔드 API가 담당하도록 수정
const filteredClasses = computed(() => {
  // 백엔드가 이미 필터링/검색된 결과를 반환했다고 가정하고,
  // Store에서 가져온 목록을 그대로 사용합니다.
  return classStore.getClassList // Store의 Getter 사용
})

// 🟢 클래스 데이터를 로드하는 통합 함수
const loadClasses = () => {
  // 🟢 Store의 Action 호출. 검색 파라미터를 객체로 전달.
  classStore.fetchClasses({
    category: selectedCategory.value !== '전체' ? selectedCategory.value : undefined,
    query: searchQuery.value.trim()
  })
  // 로딩이 완료된 후 검색 성공 모달을 띄우는 로직은 fetchClasses Action 내부 또는
  // watch(isLoading)를 통해 처리하는 것이 더 안전합니다.
}

const searchClasses = () => {
  // 검색어가 있든 없든 API를 호출하여 최신 데이터를 가져옵니다.
  loadClasses()
  // 현재는 검색어를 모달로 팝업시키는 시뮬레이션 기능만 함 (선택적)
  // showModal('검색', `"${searchQuery.value}"로 검색했습니다.`, 'info')
}

// 필터나 검색어가 변경될 때마다 데이터를 다시 불러오도록 설정
watch([selectedCategory, searchQuery], () => {
  loadClasses()
})

// 컴포넌트 마운트 시 데이터 로드
onMounted(() => {
  loadClasses()
})

// 클래스 상세로 이동카드 클릭 시 실행: 상세 페이지로 라우팅
const viewClass = (id) => router.push(`/class/${id}`)
</script>