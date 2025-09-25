import { fileURLToPath, URL } from 'node:url'

import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import vueDevTools from 'vite-plugin-vue-devtools'

// https://vite.dev/config/
export default defineConfig({
  plugins: [
    vue(),
    vueDevTools(),
  ],
  resolve: {
    alias: {
      '@': fileURLToPath(new URL('./src', import.meta.url))
    },
  },
  /*
  server: {
    host: true, // 외부 접속 허용
    port: 5173, // Vite 기본 포트
    allowedHosts: ['lynsey-subconferential-loris.ngrok-free.dev'],
  },
  */
})
