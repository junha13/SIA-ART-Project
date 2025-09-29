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
    }
})
