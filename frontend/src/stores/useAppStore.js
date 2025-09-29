import { defineStore } from 'pinia'
import { computed, ref } from 'vue'

export const useAppStore = defineStore('app', () => {
    // 레이아웃 (헤더/네비게이션) 표시 여부만 남김
    const showLayout = ref(true)

    // 액션: 레이아웃 표시/숨김 설정
    const setShowLayout = (isVisible) => {
        showLayout.value = isVisible
    }

    // Getter: 현재 레이아웃 표시 여부
    const getShowLayout = computed(() => showLayout.value)

    return {
        showLayout,
        setShowLayout,
        getShowLayout
    }
})