import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import { MOCK_ARTISTS } from '@/data/MockData.js'; // 🟢 MockData Import

export const useAuthStore = defineStore('auth', () => {
    // 1. 상태: 인증 및 사용자 정보
    const DUMMY_USER_DATA = MOCK_ARTISTS[0]; // 김준하 (ID 101)를 기본 인증 사용자로 설정

    const isAuthenticated = ref(!!localStorage.getItem('user_token'));
    const user = ref(JSON.parse(localStorage.getItem('user_info')) || {
        ...DUMMY_USER_DATA, // MockData 기반 기본값
        role: "비회원" // 로그인 안된 상태
    });

    // 2. 액션: 로그인/로그아웃 처리
    const login = (token) => {
        // ⭐ [구조 개선]: 실제 API 호출 대신 Mock Data 사용
        const loggedInUser = DUMMY_USER_DATA;

        user.value = loggedInUser;
        isAuthenticated.value = true;
        localStorage.setItem('user_info', JSON.stringify(user.value));
        localStorage.setItem('user_token', token || 'dummy-token-123');
    }

    const logout = () => {
        isAuthenticated.value = false;
        user.value = { name: "게스트", role: "비회원", profileImage: "public/assets/media/svg/avatars/001-boy.svg" };
        localStorage.removeItem('user_info');
        localStorage.removeItem('user_token');
    }

    // 3. Getter
    const getUser = computed(() => user.value);
    const getIsAuthenticated = computed(() => isAuthenticated.value);

    return {
        user,
        isAuthenticated,
        login,
        logout,
        getUser,
        getIsAuthenticated
    }
})