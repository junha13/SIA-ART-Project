import { ref, computed } from 'vue';
import { defineStore } from 'pinia';
import { useApi } from '@/data/useApi';

export const useClassStore = defineStore('class', () => {
    /**
     * 상태 (State)
     */
    const classes = ref([]);
    const classDetail = ref(null);
    const classesLoading = ref(false);
    const classesError = ref(null);

    /**
     * 액션 (Actions)
     */

        // 클래스 목록 조회
    const fetchClasses = async (params = {}) => {
            const { execute } = useApi('get', 'classes');
            classesLoading.value = true;
            classesError.value = null; // 이전 에러 초기화

            try {
                const responseData = await execute(params);
                // 👈 [수정 1] responseData 자체가 백엔드에서 보낸 배열이므로 .data를 제거합니다.
                classes.value = responseData;
                return true;
            } catch (err) {
                classesError.value = err;
                console.error('클래스 목록 조회 실패:', err);
                // 👈 [수정 2] 에러 발생 시 목록을 빈 배열로 초기화하여 화면 오류를 방지합니다.
                classes.value = [];
                return false;
            } finally {
                classesLoading.value = false;
            }
        };

    // 클래스 상세 정보 조회
    const fetchClassDetail = async (id) => {
        const { execute } = useApi('get', `classes/${id}`);
        // 👈 [개선 1] 상세 정보 조회 시에도 로딩과 에러 상태를 관리합니다.
        classesLoading.value = true;
        classesError.value = null;
        classDetail.value = null; // 이전 상세 데이터 초기화

        try {
            const responseData = await execute();
            classDetail.value = responseData;
            return true;
        } catch (err) {
            classesError.value = err;
            console.error(`클래스 상세 조회 (ID: ${id}) 실패:`, err);
            return false;
        } finally {
            classesLoading.value = false;
        }
    };

    /**
     * 게터 (Getters)
     */
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