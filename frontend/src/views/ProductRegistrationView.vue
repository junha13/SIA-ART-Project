<template>
  <div class="container py-5">
    <!-- Header -->
    <div class="d-flex align-items-center mb-6">
      <button class="btn btn-light btn-sm me-3" @click="$router.go(-1)">
        <i class="fas fa-chevron-left"></i>
      </button>
      <h1 class="fs-3 fw-bold mb-0">작품 등록</h1>
    </div>

    <!-- Form -->
    <div class="card">
      <div class="card-body">
        <form @submit.prevent="confirmRegistration" class="row g-5">

          <!-- Image Upload (로직 유지) -->
          <div class="col-12">
            <label class="form-label fw-bold">작품 이미지</label>
            <div class="border border-dashed rounded p-4 text-center">

              <!-- 이미지 없을 때 -->
              <div v-if="product.images.length === 0" class="py-4">
                <i class="fas fa-cloud-upload-alt fs-1 text-muted mb-3"></i>
                <p class="text-muted mb-3">이미지를 업로드하세요 (최대 5장)</p>
                <input type="file" ref="fileInput" @change="handleImageUpload" multiple accept="image/*" class="d-none"/>
                <button type="button" class="btn btn-primary" @click="$refs.fileInput.click()">
                  <i class="fas fa-plus me-2"></i>이미지 선택
                </button>
              </div>

              <!-- 이미지 있을 때 -->
              <div v-else class="row g-3">
                <div v-for="(image, index) in product.images" :key="index" class="col-6 col-md-4 col-lg-3">
                  <div class="position-relative">
                    <img :src="image.url" class="img-fluid rounded" style="height: 150px; width: 100%; object-fit: cover;" alt=""/>
                    <!-- 이미지 제거 버튼 -->
                    <button type="button" class="btn btn-danger btn-sm position-absolute top-0 end-0 m-1 rounded-circle" @click="removeImage(index)">
                      <i class="fas fa-times"></i>
                    </button>
                    <!-- 대표 이미지 표시 -->
                    <div v-if="index === 0" class="position-absolute bottom-0 start-0 m-1">
                      <span class="badge bg-primary">대표</span>
                    </div>
                  </div>
                </div>

                <!-- 추가 버튼 -->
                <div v-if="product.images.length < 5" class="col-6 col-md-4 col-lg-3">
                  <div class="border border-dashed rounded d-flex align-items-center justify-content-center cursor-pointer" style="height: 150px;" @click="$refs.fileInput.click()">
                    <div class="text-center">
                      <i class="fas fa-plus fs-3 text-muted mb-2"></i>
                      <p class="text-muted small mb-0">추가</p>
                    </div>
                  </div>
                </div>
              </div>

            </div>
          </div>

          <!-- Title -->
          <div class="col-12">
            <label class="form-label fw-bold">작품명</label>
            <input type="text" v-model="product.title" class="form-control form-control-solid" placeholder="작품명을 입력하세요" required/>
          </div>

          <!-- Category -->
          <div class="col-md-6">
            <label class="form-label fw-bold">카테고리</label>
            <select v-model="product.category" class="form-select form-select-solid" required>
              <option value="">카테고리 선택</option>
              <option value="회화">회화</option>
              <option value="조각">조각</option>
              <option value="도예">도예</option>
              <option value="사진">사진</option>
              <option value="공예">공예</option>
              <option value="기타">기타</option>
            </select>
          </div>

          <!-- Price -->
          <div class="col-md-6">
            <label class="form-label fw-bold">가격</label>
            <div class="input-group">
              <span class="input-group-text">₩</span>
              <input type="number" v-model="product.price" class="form-control form-control-solid" placeholder="가격" required/>
            </div>
          </div>

          <!-- Size -->
          <div class="col-md-6">
            <label class="form-label fw-bold">크기</label>
            <input type="text" v-model="product.size" class="form-control form-control-solid" placeholder="예: 60cm x 80cm"/>
          </div>

          <!-- Material -->
          <div class="col-md-6">
            <label class="form-label fw-bold">재료</label>
            <input type="text" v-model="product.material" class="form-control form-control-solid" placeholder="예: 캔버스에 아크릴"/>
          </div>

          <!-- Year -->
          <div class="col-md-6">
            <label class="form-label fw-bold">제작년도</label>
            <input type="number" v-model="product.year" class="form-control form-control-solid" placeholder="2025" :max="new Date().getFullYear()"/>
          </div>

          <!-- Condition -->
          <div class="col-md-6">
            <label class="form-label fw-bold">작품 상태</label>
            <select v-model="product.condition" class="form-select form-select-solid">
              <option value="새것">새것</option>
              <option value="매우좋음">매우좋음</option>
              <option value="좋음">좋음</option>
              <option value="보통">보통</option>
            </select>
          </div>

          <!-- Tags -->
          <div class="col-12">
            <label class="form-label fw-bold">태그</label>
            <div class="d-flex flex-wrap gap-2 mb-2">
              <span v-for="(tag, index) in product.tags" :key="index" class="badge bg-light text-dark fw-bold p-2">
                #{{ tag }}
                <button type="button" class="btn-close btn-close-sm ms-1" @click="removeTag(index)"></button>
              </span>
            </div>
            <div class="input-group">
              <input type="text" v-model="newTag" @keyup.enter.prevent="addTag" class="form-control form-control-solid" placeholder="태그를 입력하고 Enter를 누르세요"/>
              <button type="button" class="btn btn-outline-secondary" @click="addTag">
                <i class="fas fa-plus"></i>
              </button>
            </div>
          </div>

          <!-- Description -->
          <div class="col-12">
            <label class="form-label fw-bold">작품 설명</label>
            <textarea v-model="product.description" class="form-control form-control-solid" rows="4" placeholder="작품에 대한 자세한 설명을 입력하세요"></textarea>
          </div>

          <!-- Location -->
          <div class="col-12">
            <label class="form-label fw-bold">위치 선택</label>
            <div id="productMap" style="width:100%; height:300px;" class="rounded border mb-2"></div>
            <p class="small text-muted">
              지도에서 클릭하여 위치를 선택하세요. (현재 위치: 위도 {{ product.lat.toFixed(4) }}, 경도 {{ product.lng.toFixed(4) }})
            </p>
          </div>

          <!-- Availability -->
          <div class="col-12">
            <div class="form-check form-switch">
              <input class="form-check-input" type="checkbox" v-model="product.isAvailable" id="availableSwitch">
              <label class="form-check-label fw-bold" for="availableSwitch">
                판매 가능
              </label>
            </div>
            <small class="text-muted">체크 해제 시 작품이 비공개 상태가 됩니다.</small>
          </div>

          <!-- Submit -->
          <div class="col-12">
            <div class="d-flex gap-3">
              <button type="button" class="btn btn-light-warning fw-bold flex-fill" @click="confirmSaveDraft">
                <i class="fas fa-save me-2"></i>임시저장
              </button>
              <button type="submit" class="btn btn-primary fw-bold flex-fill">
                <i class="fas fa-check me-2"></i>등록하기
              </button>
            </div>
          </div>
        </form>
      </div>
    </div>

    <!-- Custom Modal -->
    <ConfirmModal
        v-model:isVisible="isModalVisible"
        :title="modalTitle"
        :message="modalMessage"
        :type="modalType"
        :autoHide="modalAutoHide"
        :confirmText="modalConfirmText"
        :cancelText="modalCancelText"
        @confirm="handleModalConfirm"
        @cancel="handleModalCancel"
    />
  </div>
</template>

<script>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import ConfirmModal from '../components/ConfirmModal.vue'

export default {
  name: 'ProductRegistrationView',
  components: { ConfirmModal },
  setup() {
    const router = useRouter()
    const newTag = ref('')
    const fileInput = ref(null)

    // Modal State
    const isModalVisible = ref(false)
    const modalTitle = ref('')
    const modalMessage = ref('')
    const modalType = ref('info')
    const modalAutoHide = ref(false)
    const modalAction = ref(null) // 'load', 'save', 'register'
    const modalConfirmText = ref('확인')
    const modalCancelText = ref('취소')


    const product = ref({
      title: '', category: '', price: 0, size: '', material: '',
      year: new Date().getFullYear(), condition: '새것', description: '',
      tags: [], images: [],
      lat: 37.5665, lng: 126.9780, // 서울 시청
      isAvailable: true
    })

    const map = ref(null)
    const marker = ref(null)
    const localStorageKey = 'artworkDraft'

    // --- Modal Logic ---
    const showModal = (title, message, type = 'info', action = null, confirmText = '확인', cancelText = '취소', autoHide = false) => {
      modalTitle.value = title
      modalMessage.value = message
      modalType.value = type
      modalAction.value = action
      modalConfirmText.value = confirmText
      modalCancelText.value = cancelText
      modalAutoHide.value = autoHide
      isModalVisible.value = true
    }

    const handleModalConfirm = () => {
      isModalVisible.value = false;
      if (modalAction.value === 'load') {
        loadDraft(true)
      } else if (modalAction.value === 'save') {
        saveDraft(true)
      } else if (modalAction.value === 'register') {
        registerProduct(true)
      } else if (modalAction.value === 'registrationSuccess') {
        router.push('/mypage')
      }
    }

    const handleModalCancel = () => {
      isModalVisible.value = false;
    }

    // --- Map Handling ---
    const initMap = () => {
      if (!window.naver) return
      const mapOptions = {
        center: new window.naver.maps.LatLng(product.value.lat, product.value.lng),
        zoom: 13, logoControl: false, mapDataControl: false, zoomControl: true
      }
      map.value = new window.naver.maps.Map('productMap', mapOptions)

      marker.value = new window.naver.maps.Marker({
        position: new window.naver.maps.LatLng(product.value.lat, product.value.lng),
        map: map.value, draggable: true
      })

      // 클릭 이벤트 (Naver Map에서 클릭해 위치 저장)
      window.naver.maps.Event.addListener(map.value, 'click', (e) => {
        product.value.lat = e.coord.lat()
        product.value.lng = e.coord.lng()
        marker.value.setPosition(e.coord)
        showModal('위치 설정 완료', `선택된 위치: 위도 ${e.coord.lat().toFixed(4)}, 경도 ${e.coord.lng().toFixed(4)}`, 'info', null, '확인', '취소', true)
      })

      // 마커 드래그 시 위치 업데이트
      window.naver.maps.Event.addListener(marker.value, 'dragend', (e) => {
        product.value.lat = e.coord.lat()
        product.value.lng = e.coord.lng()
        showModal('위치 설정 완료', `드래그된 위치: 위도 ${e.coord.lat().toFixed(4)}, 경도 ${e.coord.lng().toFixed(4)}`, 'info', null, '확인', '취소', true)
      })
    }

    // --- Image / Tag Logic (이전 버전에서 유지) ---
    const handleImageUpload = (event) => { /* ... (로직 유지) ... */ }
    const removeImage = (index) => { product.value.images.splice(index, 1) }
    const addTag = () => {
      const tag = newTag.value.trim()
      if (tag && product.value.tags.length < 10 && !product.value.tags.includes(tag)) {
        product.value.tags.push(tag); newTag.value = ''
      }
    }
    const removeTag = (index) => { product.value.tags.splice(index, 1) }

    // --- Draft & Registration Logic ---
    const confirmSaveDraft = () => {
      showModal('임시 저장 확인', '현재 작성 내용을 임시 저장하시겠습니까?', 'confirm', 'save', '저장', '취소', false)
    }

    const saveDraft = (confirmed = false) => {
      if (!confirmed) return;
      const dataToSave = {
        ...product.value,
        images: product.value.images.map(img => ({ url: img.url, name: img.name }))
      }
      localStorage.setItem(localStorageKey, JSON.stringify(dataToSave))
      showModal('임시 저장 완료', '현재 작성 내용이 브라우저에 임시 저장되었습니다.', 'success', null, '확인', '취소', true)
    }

    const loadDraft = (confirmed = false) => {
      const draft = localStorage.getItem(localStorageKey)
      if (draft) {
        if (!confirmed) {
          showModal('임시 저장 불러오기', '임시 저장된 내용이 있습니다. 불러오시겠습니까?', 'confirm', 'load', '불러오기', '취소', false)
          return
        }

        const parsedDraft = JSON.parse(draft)
        const loadedImages = parsedDraft.images.map(img => ({ ...img, file: null }))

        Object.assign(product.value, { ...parsedDraft, images: loadedImages })

        if (map.value && marker.value) {
          const newLatLng = new window.naver.maps.LatLng(product.value.lat, product.value.lng)
          map.value.setCenter(newLatLng)
          marker.value.setPosition(newLatLng)
        }
        showModal('로드 완료', '임시 저장된 작품 정보를 불러왔습니다.', 'success', null, '확인', '취소', true)
      }
    }

    const confirmRegistration = () => {
      if (product.value.images.length === 0) {
        showModal('등록 오류', '최소 1장의 이미지를 업로드해주세요.', 'error', null, '확인', '취소', true)
        return
      }
      showModal('등록 확인', '작품을 최종 등록하시겠습니까?', 'confirm', 'register', '등록하기', '취소', false)
    }

    // 등록 후 /mypage로 이동
    const registerProduct = (confirmed = false) => {
      if (!confirmed) return;
      console.log('작품 등록 데이터:', product.value)
      localStorage.removeItem(localStorageKey)
      showModal('등록 성공', `"${product.value.title}" 작품이 성공적으로 등록되었습니다.`, 'success', 'registrationSuccess', '확인', '취소', false)
    }

    onMounted(() => {
      if (window.naver) {
        initMap()
        loadDraft()
      } else {
        // Naver Map SDK가 로드될 때까지 기다리는 로직 (index.html에서 defer로 로드)
        window.addEventListener('load', () => {
          if (window.naver) {
            initMap()
            loadDraft()
          } else {
            console.error('Naver Maps SDK 로드 실패.')
          }
        })
      }
    })

    return {
      product, newTag, fileInput,
      isModalVisible, modalTitle, modalMessage, modalType, modalAutoHide, modalConfirmText, modalCancelText,
      handleImageUpload: (event) => {
        const files = Array.from(event.target.files)
        files.forEach(file => {
          if (product.value.images.length < 5) {
            const reader = new FileReader()
            reader.onload = (e) => {
              product.value.images.push({ file: file, url: e.target.result, name: file.name })
            }
            reader.readAsDataURL(file)
          }
        })
        event.target.value = null
      },
      removeImage, addTag, removeTag,
      confirmSaveDraft, confirmRegistration,
      handleModalConfirm, handleModalCancel
    }
  }
}
</script>

<style scoped>
.cursor-pointer { cursor: pointer; }
.btn-close-sm { font-size: 0.7rem; }
.form-control-solid { background-color: #F5F8FA !important; border-color: #F5F8FA !important; }
</style>
