import { ref, computed } from 'vue'
import { defineStore } from 'pinia'
import { MOCK_MAP_DATA, MOCK_HISTORY } from '@/data/MockData.js'; // 🟢 MockData Import

export const useLocationStore = defineStore('location', () => {
    // 1. 상태: 현재 위치 및 검색 범위
    const currentLocation = ref({
        name: '구로구 구로동',
        address: '서울시 구로구 구로동',
        lat: 37.4954,
        lng: 126.8874
    })
    const searchRange = ref(3) // 기본값 3km

    // 2. 상태: 관심 지역 목록 (MockData 기반 초기화)
    const interestAreas = ref([]);

    // 3. 액션: 데이터 초기화 (API 호출 대체)
    const initializeAreas = () => {
        // ⭐ [구조 개선]: MOCK_MAP_DATA에서 'gallery' 타입 데이터를 관심 지역으로 설정
        interestAreas.value = MOCK_MAP_DATA
            .filter(d => d.type === 'gallery')
            .map(g => ({
                id: g.id,
                name: g.title,
                address: g.loc,
                notifications: true,
                lat: g.lat,
                lng: g.lng
            }));
    }

    // 4. 액션: 현재 위치 및 범위 업데이트
    const updateCurrentLocation = (newLocation) => {
        currentLocation.value = newLocation
    }

    const updateSearchRange = (newRange) => {
        searchRange.value = newRange
    }

    // 5. 액션: 관심 지역 관리
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

    // Store 정의 시 초기화 액션을 호출해야 하지만, 뷰에서 onMounted 시 호출하는 것이 Vue3/Pinia 패턴에 더 적합
    initializeAreas();

    return {
        currentLocation,
        searchRange,
        interestAreas,
        updateCurrentLocation,
        updateSearchRange,
        addInterestArea,
        removeInterestArea,
        toggleAreaNotification,
        initializeAreas // 뷰에서 명시적으로 호출할 수 있도록 export
    }
})