import { ref, computed } from 'vue'
import { defineStore } from 'pinia'

export const useLocationStore = defineStore('location', () => {
    // 1. 상태: 현재 위치 및 검색 범위 (MainView, ArchiveMapView에서 사용)
    const currentLocation = ref({
        name: '구로구 구로동',
        address: '서울시 구로구 구로동',
        lat: 37.4954,
        lng: 126.8874
    })
    const searchRange = ref(3) // 기본값 3km

    // 2. 상태: 관심 지역 목록 (LocationSettingsView에서 CRUD)
    const interestAreas = ref([
        { id: 1, name: '강남구', address: '서울시 강남구', notifications: true, lat: 37.5173, lng: 127.0473 }
    ])

    // 3. 액션: 현재 위치 및 범위 업데이트
    const updateCurrentLocation = (newLocation) => {
        currentLocation.value = newLocation
    }

    const updateSearchRange = (newRange) => {
        searchRange.value = newRange
    }

    // 4. 액션: 관심 지역 관리
    const addInterestArea = (newArea) => {
        interestAreas.value.push(newArea)
    }

    const removeInterestArea = (id) => {
        interestAreas.value = interestAreas.value.filter(a => a.id !== id)
    }

    const toggleAreaNotification = (id) => {
        const area = interestAreas.value.find(a => a.id === id)
        if (area) {
            area.notifications = !area.notifications
        }
    }

    // 5. Getter: (필요시 추가)

    return {
        currentLocation,
        searchRange,
        interestAreas,
        updateCurrentLocation,
        updateSearchRange,
        addInterestArea,
        removeInterestArea,
        toggleAreaNotification
    }
})