import { ref } from 'vue';
import axios from 'axios';

const api = axios.create({
    baseURL: 'http://localhost:8080/api/',
});

export function useAxios(url) { // user/insert
    console.log(`${url} : userAxios 함수 실행됨`)
    const data = ref(null);
    const error = ref(null);
    const loading = ref(true);

    const axiosData = async () => {
        console.log(`${url} : axiosData 함수 실행됨`)
        // 로딩에 성공하면 response에 데이터 담고 return함
        try {
            loading.value = true;
            const response = await api.get(url);
            data.value = response.data;
        } catch (err) {
            error.value = err;
        } finally {
            loading.value = false;
        }
    };

    axiosData(); // 함수가 호출되면 즉시 API 호출

    return { data, error, loading };
}

// 그게 가능해? ?? 이건