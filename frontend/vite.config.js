import { fileURLToPath, URL } from 'node:url'

import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import vueDevTools from 'vite-plugin-vue-devtools'

export default defineConfig({
    plugins: [
        vue(),
        vueDevTools(),
    ],
    resolve: {
        alias: {
            '@': fileURLToPath(new URL('./src', import.meta.url)),
            'vue3-naver-maps': 'vue3-naver-maps/dist/vue3-naver-maps.es.js'
        },
    },
    server: {
        port: 5173,        // 기본 포트는 5173
        host: true,        // 외부에서도 접근 가능하게
        allowedHosts: [
            'hellokiyo.ngrok.io'   // ngrok 도메인 허용
        ],
        proxy: {
            // '/api'로 시작하는 모든 요청을 target 서버로 전달
            '/api': {
                target: 'http://localhost:8080', // 백엔드 서버 주소 (로그에서 확인된 톰캣 포트)
                changeOrigin: true, // 호스트 헤더 변경 (백엔드 서버가 프론트엔드 서버를 통해 요청받는 것처럼 보이도록)
                // rewrite: (path) => path.replace(/^\/api/, '') // 백엔드에 따라 /api 제거가 필요할 수도 있음. 현재는 백엔드에 /api/users로 정의되어 있어 제거 불필요
            }
        },
    },
    optimizeDeps: {
        include: ['quill'],   
        // ⬅️ 핵심: quill을 사전 번들
        // UMD → ESM 변환을 사전에 시켜서 “default export 없음” 문제를 Vite가 자동으로 보정해준 거예요.
    },
})
