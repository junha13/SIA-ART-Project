import { defineStore } from 'pinia'
import { ref, computed } from 'vue'

export const useAuthStore = defineStore('auth', () => {
    // 1. 상태: 인증 및 사용자 정보 (localStorage에서 초기 로드)
    const isAuthenticated = ref(!!localStorage.getItem('user_token'))
    const user = ref(JSON.parse(localStorage.getItem('user_info')) || {
        name: "게스트",
        role: "비회원",
        profileImage: "assets/media/avatars/default.jpg"
    })

    // 2. 액션: 로그인/로그아웃 처리
    // remember: true -> persist to localStorage, false -> sessionStorage
    const login = (userData, token, remember = false) => {
        user.value = userData || { name: "테스트 사용자", role: "일반 사용자", profileImage: "assets/media/avatars/300-1.jpg" }
        isAuthenticated.value = true

        const userJson = JSON.stringify(user.value)
        const accessToken = token || 'dummy-token-123'

        if (remember) {
            localStorage.setItem('user_info', userJson)
            localStorage.setItem('user_token', accessToken)
            // clear any session storage copies
            sessionStorage.removeItem('user_info')
            sessionStorage.removeItem('user_token')
        } else {
            sessionStorage.setItem('user_info', userJson)
            sessionStorage.setItem('user_token', accessToken)
            // clear any local storage copies
            localStorage.removeItem('user_info')
            localStorage.removeItem('user_token')
        }
    }

    const logout = () => {
        isAuthenticated.value = false
        user.value = { name: "게스트", role: "비회원", profileImage: "assets/media/avatars/default.jpg" }
        localStorage.removeItem('user_info')
        localStorage.removeItem('user_token')
        sessionStorage.removeItem('user_info')
        sessionStorage.removeItem('user_token')
    }

    // 3. Getter
    const getUser = computed(() => user.value)
    const getIsAuthenticated = computed(() => isAuthenticated.value)

    return {
        user,
        isAuthenticated,
        login,
        logout,
        getUser,
        getIsAuthenticated
    }
})