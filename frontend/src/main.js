import { createApp } from 'vue';
import { createPinia } from 'pinia';
import router from './router';
import App from './App.vue';
import './style.css';
import 'bootstrap/dist/js/bootstrap.bundle.min.js';
import { quillEditor } from 'vue3-quill';
import 'quill/dist/quill.core.css';
import 'quill/dist/quill.snow.css';

/**
 * Naver Maps API 스크립트를 순서에 맞게 로드하고,
 * naver.maps 객체가 완전히 초기화될 때까지 기다리는 함수.
 */
function loadMapScripts() {
    return new Promise((resolve, reject) => {
        const naverMapsKeyId = import.meta.env.VITE_NAVER_MAPS_KEY_ID;

        if (!naverMapsKeyId) {
            const errorMessage = 'Naver Maps Key ID가 .env 파일에 설정되지 않았습니다.';
            console.error(errorMessage);
            return reject(new Error(errorMessage));
        }

        const mapScript = document.createElement('script');
        mapScript.src = `https://openapi.map.naver.com/openapi/v3/maps.js?ncpKeyId=${naverMapsKeyId}&submodules=clustering`;
        mapScript.async = true;
        mapScript.defer = true;
        document.head.appendChild(mapScript);

        mapScript.onload = () => {
            const clusterScript = document.createElement('script');
            clusterScript.src = '/marker-clustering.js';
            document.head.appendChild(clusterScript);

            clusterScript.onload = () => {
                // 👇 [수정] 스크립트 로드 후, naver.maps 객체가 생성될 때까지 기다립니다.
                const interval = setInterval(() => {
                    if (window.naver && window.naver.maps) {
                        clearInterval(interval);
                        resolve(); // naver.maps 객체가 확인되면 성공 처리
                    }
                }, 100); // 100ms 간격으로 체크

                // 만약 5초가 지나도 로드되지 않으면 에러 처리 (무한 대기 방지)
                setTimeout(() => {
                    if (!window.naver || !window.naver.maps) {
                        clearInterval(interval);
                        reject(new Error('Naver Maps API 초기화 시간 초과'));
                    }
                }, 5000);
            };

            clusterScript.onerror = (error) => {
                console.error('marker-clustering.js 로드 실패', error);
                reject(error);
            };
        };

        mapScript.onerror = (error) => {
            console.error('Naver Maps API 스크립트 로드 실패', error);
            reject(error);
        };
    });
}

/**
 * Vue 애플리케이션을 초기화하고 마운트하는 함수.
 */
function initApp() {
    const app = createApp(App);

    app.use(createPinia());
    app.use(router);
    app.component('quill-editor', quillEditor);

    app.mount('#app');
}

// 지도 스크립트 로드를 먼저 시도하고, 성공하면(.then) Vue 앱을 초기화합니다.
loadMapScripts()
    .then(() => {
        console.log('Naver Maps API와 클러스터링 스크립트가 모두 로드 및 초기화되었습니다.');
        initApp();
    })
    .catch(error => {
        console.error('스크립트 로딩 중 에러가 발생하여 앱을 실행할 수 없습니다.', error);
    });