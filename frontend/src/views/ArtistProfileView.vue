<template>
  <div class="container py-5">
    <!-- 프로필 카드 -->
    <div class="card shadow-sm mb-5">
      <div class="card-body text-center">
        <div class="mb-3 position-relative d-inline-block">
          <img src="https://via.placeholder.com/100" alt="artist avatar" class="rounded-circle"/>
          <button class="btn btn-sm btn-light position-absolute top-0 end-0" @click="showProfileSettings">
            <i class="fas fa-cog"></i>
          </button>
        </div>
        <h4 class="fw-bold mb-1">{{ artist.name }}</h4>
        <p class="text-muted small mb-3">{{ artist.role }}</p>
        <div class="d-flex justify-content-center gap-2">
          <button class="btn btn-sm" :class="activeTab === 'intro' ? 'btn-primary' : 'btn-outline-primary'" @click="activeTab = 'intro'">
            자기소개
          </button>
          <button class="btn btn-sm" :class="activeTab === 'portfolio' ? 'btn-dark' : 'btn-outline-dark'" @click="activeTab = 'portfolio'">
            포트폴리오
          </button>
          <button class="btn btn-sm" :class="activeTab === 'activity' ? 'btn-secondary' : 'btn-outline-secondary'" @click="activeTab = 'activity'">
            활동 발자취
          </button>
        </div>
      </div>
    </div>

    <!-- 자기소개 -->
    <div v-if="activeTab === 'intro'" class="card shadow-sm mb-5">
      <div class="card-body">
        <h6 class="fw-bold mb-3">자기소개</h6>
        <p class="text-muted small mb-4">{{ artist.bio }}</p>

        <h6 class="fw-bold mb-3">경력</h6>
        <ul class="timeline">
          <li v-for="(career, index) in artist.career" :key="index">
            <span class="year">{{ career.year }}</span>
            <span class="desc">{{ career.desc }}</span>
          </li>
        </ul>
      </div>
    </div>

    <!-- 포트폴리오 -->
    <div v-if="activeTab === 'portfolio'" class="card shadow-sm mb-5">
      <div class="card-body">
        <h6 class="fw-bold mb-3">포트폴리오</h6>
        <div class="row g-3">
          <div v-for="work in portfolio" :key="work.id" class="col-6">
            <div class="card h-100 shadow-sm">
              <img :src="work.image" class="card-img-top" :alt="work.title"/>
              <div class="card-body p-2">
                <h6 class="fw-bold small mb-1">{{ work.title }}</h6>
                <p class="text-muted small mb-0">by {{ work.artist }}</p>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- 활동 (클래스 + 게시글) -->
    <div v-if="activeTab === 'activity'">
      <!-- 클래스 -->
      <div class="card shadow-sm mb-5">
        <div class="card-body">
          <h6 class="fw-bold mb-3">클래스</h6>
          <div v-for="cls in classes" :key="cls.id" class="d-flex align-items-center justify-content-between border rounded p-3 mb-3">
            <div>
              <h6 class="fw-bold mb-1">{{ cls.title }}</h6>
              <p class="text-muted small mb-0">{{ cls.desc }}</p>
            </div>
            <span class="badge bg-light text-dark">{{ cls.level }}</span>
          </div>
        </div>
      </div>

      <!-- 게시글 -->
      <div class="card shadow-sm">
        <div class="card-body">
          <h6 class="fw-bold mb-3">게시글</h6>
          <ul class="list-group list-group-flush">
            <li v-for="post in posts" :key="post.id" class="list-group-item d-flex justify-content-between align-items-center">
              <span>{{ post.title }}</span>
              <small class="text-muted">{{ post.date }}</small>
            </li>
          </ul>
        </div>
      </div>
    </div>

    <!-- Custom Modal -->
    <ConfirmModal
        v-model:isVisible="isModalVisible"
        :title="modalTitle"
        :message="modalMessage"
        :type="modalType"
        :autoHide="modalAutoHide"
    />
  </div>
</template>

<script setup>
import { ref } from "vue"
import ConfirmModal from '../components/ConfirmModal.vue'

const activeTab = ref("intro") // 기본 탭: 자기소개

// Modal State
const isModalVisible = ref(false)
const modalTitle = ref('')
const modalMessage = ref('')
const modalType = ref('info')
const modalAutoHide = ref(true)

const showModal = (title, message, type = 'info', autoHide = true) => {
  modalTitle.value = title
  modalMessage.value = message
  modalType.value = type
  modalAutoHide.value = autoHide
  isModalVisible.value = true
}

const artist = ref({
  name: "김준하",
  role: "예술가님",
  bio: "2조의 팀장이자 버팀목이자 고급인력",
  career: [
    { year: "2001년", desc: "예술적인 걸음마" },
    { year: "2015년", desc: "경력1" },
    { year: "2014년", desc: "경력2" },
    { year: "2016년", desc: "경력3" },
    { year: "2020년", desc: "예술적인 입학" },
    { year: "2025년", desc: "최종 팀 프로젝트 1등" },
  ],
})

const portfolio = ref([
  { id: 1, title: "작품1", artist: "김준하", image: "https://via.placeholder.com/200x150/F8C471/000?text=Artwork1" },
  { id: 2, title: "작품2", artist: "김준하", image: "https://via.placeholder.com/200x150/EC7063/fff?text=Artwork2" },
])

const classes = ref([
  { id: 1, title: "수채화 기초", desc: "처음 시작하는 수채화", level: "입문" },
  { id: 2, title: "수채화 심화", desc: "표현력 확장", level: "중급" },
])

const posts = ref([
  { id: 1, title: "요즘 미술하기 힘드네요...", date: "2025.09.28" },
  { id: 2, title: "그래도 요즘은 할만하네요", date: "2025.11.28" },
])

const showProfileSettings = () => {
  showModal('프로필 설정', '이곳은 프로필 정보를 수정하거나 작가 계정을 관리하는 페이지입니다.', 'info', false)
}
</script>

<style scoped>
.timeline { list-style: none; padding-left: 0; }
.timeline li { margin-bottom: 1rem; position: relative; padding-left: 1.5rem; }
.timeline li::before { content: ""; position: absolute; left: 0; top: 0.4rem; width: 10px; height: 10px; background: #007bff; border-radius: 50%; }
</style>
