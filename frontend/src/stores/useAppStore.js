// useAppStore.js (변경 없음)
import { defineStore } from 'pinia'
import { computed, ref } from 'vue'

export const useAppStore = defineStore('app', () => {
    const showLayout = ref(true)

    const setShowLayout = (isVisible) => {
        showLayout.value = isVisible
    }

    const getShowLayout = computed(() => showLayout.value)

    return {
        showLayout,
        setShowLayout,
        getShowLayout
    }
})