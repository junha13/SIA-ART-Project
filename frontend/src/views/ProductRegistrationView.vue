<template>
  <div class="container py-4 mt-20">

    <div class="d-flex align-items-center mb-3">
      <button class="btn btn-link p-0 me-2" @click="goBack">
        <i class="ki-duotone ki-arrow-left fs-2"></i>
      </button>
      <h5 class="mb-0">작품 등록</h5>
    </div>

    <div class="d-flex justify-content-center mb-3">
      <div class="border rounded d-flex align-items-center justify-content-center overflow-hidden"
           style="width: 200px; height: 200px; /* 고정된 정사각형 크기 */
                    background-color: #f8f9fa;">

        <img v-if="form.imagePreview" :src="form.imagePreview"
             alt="작품 미리보기"
             class="w-100 h-100"
             style="object-fit: contain; /* ⭐ 이미지가 잘리지 않고 전체 보이도록 */
                        cursor: pointer;"
             @click="$refs.fileInput.click()"
        />

        <div v-else class="text-center" style="cursor: pointer;" @click="$refs.fileInput.click()">
          <i class="ki-duotone ki-camera fs-1 text-muted"></i>
          <div class="btn btn-light btn-sm mt-2">이미지 업로드</div>
        </div>

        <input type="file" class="d-none" ref="fileInput" @change="onFileChange" accept="image/*" />
      </div>
    </div>

    <div v-if="form.image" class="text-muted small mb-3 text-center">
      선택된 파일: {{ form.image.name }}
    </div>

    <form @submit.prevent="submitForm">
      <div class="row g-2 mb-3">
        <div class="col-6">
          <select v-model="form.category" class="form-select">
            <option value="" disabled>작품 종류</option>
            <option value="회화">회화</option>
            <option value="조각">조각</option>
            <option value="사진">사진</option>
            <option value="도예">도예</option>
            <option value="공예">공예</option>
          </select>
        </div>
        <div class="col-6">
          <input
              type="text"
              v-model="form.price"
              @input="filterNumberInput"
              class="form-control"
              placeholder="판매 가격 (숫자)"
              inputmode="numeric"
          />
        </div>
      </div>

      <div class="mb-3">
        <input type="text" v-model="form.title" class="form-control" placeholder="작품명" />
      </div>

      <div class="mb-3">
        <input type="text" v-model="form.subtitle" class="form-control" placeholder="부제" />
      </div>

      <div class="mb-3">
        <input type="text" v-model="form.location" class="form-control" placeholder="위치 (예: 서울 구로구)" />
      </div>

      <div class="mb-3">
        <textarea v-model="form.description" class="form-control" rows="4" placeholder="상세 설명"></textarea>
      </div>

      <div class="d-grid">
        <button type="submit" class="btn btn-dark">작성 완료</button>
      </div>
    </form>

  </div>
</template>

<script setup>
import { ref } from "vue";
import { useRouter } from 'vue-router'

const router = useRouter()

const form = ref({
  category: "",
  price: "",
  title: "",
  subtitle: "",
  location: "", // 위치 필드 추가
  description: "",
  image: null,
  imagePreview: null, // 이미지 미리보기 URL 상태 추가
});

const fileInput = ref(null);

const onFileChange = (e) => {
  const file = e.target.files[0];
  if (file) {
    form.value.image = file;
    form.value.imagePreview = URL.createObjectURL(file);
  } else {
    form.value.image = null;
    form.value.imagePreview = null;
  }
};

const filterNumberInput = (event) => {
  const filteredValue = event.target.value.replace(/[^0-9]/g, '');
  form.value.price = filteredValue;
};

const submitForm = () => {
  if (!form.value.title || !form.value.price || !form.value.category) {
    alert("작품명, 가격, 종류는 필수 입력 사항입니다.");
    return;
  }

  console.log("폼 데이터:", form.value);

  alert("작품 등록 완료!");
  router.push('/artwork/list'); // ArtworkListView로 이동
};

const goBack = () => {
  window.history.back();
};
</script>

<style scoped>
.w-100 { width: 100%; }
.h-100 { height: 100%; }
.border { border: 1px solid var(--bs-gray-300) !important; }
.rounded { border-radius: 0.5rem !important; }
.overflow-hidden { overflow: hidden; }
</style>