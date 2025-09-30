<template>
  <div class="container py-4">

    <!-- 상단 헤더 -->
    <div class="d-flex align-items-center mb-3">
      <button class="btn btn-link p-0 me-2" @click="goBack">
        <i class="ki-duotone ki-arrow-left fs-2"></i>
      </button>
      <h5 class="mb-0">작품 등록</h5>
    </div>

    <!-- 이미지 업로드 영역 -->
    <div class="border rounded d-flex align-items-center justify-content-center mb-3" style="height:200px;">
      <div class="text-center">
        <i class="ki-duotone ki-camera fs-1 text-muted"></i>
        <input type="file" class="d-none" ref="fileInput" @change="onFileChange" />
        <button class="btn btn-light btn-sm mt-2" @click="$refs.fileInput.click()">이미지 업로드</button>
      </div>
    </div>

    <!-- 입력 폼 -->
    <form @submit.prevent="submitForm">
      <div class="row g-2 mb-3">
        <div class="col-6">
          <select v-model="form.category" class="form-select">
            <option value="">작품 종류</option>
            <option value="painting">회화</option>
            <option value="sculpture">조각</option>
            <option value="photo">사진</option>
          </select>
        </div>
        <div class="col-6">
          <input type="number" v-model="form.price" class="form-control" placeholder="판매 가격" />
        </div>
      </div>

      <div class="mb-3">
        <input type="text" v-model="form.title" class="form-control" placeholder="작품명" />
      </div>

      <div class="mb-3">
        <input type="text" v-model="form.subtitle" class="form-control" placeholder="부제" />
      </div>

      <div class="mb-3">
        <textarea v-model="form.description" class="form-control" rows="4" placeholder="상세 설명"></textarea>
      </div>

      <!-- 작성 완료 버튼 -->
      <div class="d-grid">
        <button type="submit" class="btn btn-dark">작성 완료</button>
      </div>
    </form>

    <!-- 하단 네비게이션 -->
    <div class="fixed-bottom bg-white border-top d-flex justify-content-around py-2">
      <button class="btn btn-link text-center" @click="goMyPage">
        <i class="ki-duotone ki-user fs-2"></i><br>
        마이페이지
      </button>
      <button class="btn btn-link text-center" @click="goHome">
        <i class="ki-duotone ki-home fs-2"></i><br>
        홈
      </button>
      <button class="btn btn-link text-center" @click="goSearch">
        <i class="ki-duotone ki-search fs-2"></i><br>
        검색
      </button>
    </div>
  </div>
</template>

<script setup>
import { ref } from "vue";

const form = ref({
  category: "",
  price: "",
  title: "",
  subtitle: "",
  description: "",
  image: null,
});

const fileInput = ref(null);

const onFileChange = (e) => {
  const file = e.target.files[0];
  if (file) {
    form.value.image = file;
  }
};

const submitForm = () => {
  console.log("폼 데이터:", form.value);
  alert("작품 등록 완료!");
};

const goBack = () => {
  window.history.back();
};
const goMyPage = () => console.log("마이페이지 이동");
const goHome = () => console.log("홈 이동");
const goSearch = () => console.log("검색 이동");
</script>

<style scoped>
.fixed-bottom {
  height: 70px;
}
</style>
