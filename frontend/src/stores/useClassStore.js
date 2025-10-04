import { ref, computed } from 'vue';
import { defineStore } from 'pinia';
import { useApi } from '@/data/useApi'; // 🟢 useApi 임포트

export const useClassStore = defineStore('class', () => {
    // 1. 상태 (State)
    const classes = ref([]);
    const classDetail = ref(null);
    const classesLoading = ref(false);
    const classesError = ref(null);

    // 2. 액션 (Actions)

    /**
     * 클래스 목록을 서버에서 가져옵니다.
     * @param {object} params - 검색 조건 (category, searchQuery, page 등)
     */
    const fetchClasses = async (params = {}) => {
        const { execute, loading, error } = useApi('get', 'classes'); // API 선언
        classesLoading.value = true;

        try {
            // GET 요청 시 params가 쿼리 파라미터로 전달됨
            const responseData = await execute(params);

            // ⭐ Store 상태 업데이트
            classes.value = responseData.data; // 서버 응답 구조에 따라 조정 필요

            classesLoading.value = false;
            classesError.value = null;

            return true; // 성공 반환
        } catch (err) {
            classesError.value = err;
            classesLoading.value = false;
            console.error('클래스 목록 조회 실패:', err);
            return false; // 실패 반환
        }
    };

    /**
     * 특정 클래스 상세 정보를 서버에서 가져옵니다.
     * @param {number} id - 클래스 번호
     */
    const fetchClassDetail = async (id) => {
        const { execute, loading, error } = useApi('get', `classes/${id}`); // 동적 URL 사용

        try {
            const data = await execute();

            // ⭐ Store 상태 업데이트
            classDetail.value = data;
            return true;
        } catch (err) {
            console.error(`클래스 상세 조회 (ID: ${id}) 실패:`, err);
            return false;
        }
    };

    // 3. 게터 (Getters)
    const getClassList = computed(() => classes.value);
    const getIsClassesLoading = computed(() => classesLoading.value);

    return {
        classes,
        classDetail,
        classesLoading,
        classesError,
        fetchClasses,
        fetchClassDetail,
        getClassList,
        getIsClassesLoading
    };
});