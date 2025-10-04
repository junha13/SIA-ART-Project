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
        ]
    },
    optimizeDeps: {
        include: ['quill'],   
        // ⬅️ 핵심: quill을 사전 번들
        // UMD → ESM 변환을 사전에 시켜서 “default export 없음” 문제를 Vite가 자동으로 보정해준 거예요.
    },
})
