import { fileURLToPath, URL } from 'node:url'

import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'

// https://vitejs.dev/config/
export default defineConfig({
  plugins: [
    vue(),
  ],
  resolve: {
    alias: {
      '@': fileURLToPath(new URL('./src', import.meta.url))
    }
  },
  // server: {
  //   // proxy: {
  //   //   '/api': {
  //   //     target: 'http://localhost:8080',  // API 서버 주소
  //   //     changeOrigin: true,  // CORS 문제 해결을 위해 필요
  //   //     // rewrite: (path) => path.replace(/^\/api/, '')
  //   //   },
  //     //  '/login': {
  //     //   target: 'http://localhost:8080',
  //     //   changeOrigin: true,
  //     //   rewrite: (path) => path.replace(/^\/login/, '/login'),
  //     // },
  //   }
  // }
})
