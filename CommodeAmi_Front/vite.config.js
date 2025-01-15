import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'

// https://vite.dev/config/
export default defineConfig({
  plugins: [react()],
  server: {
    proxy: {
      // header: {
      //   "Access-Control-Allow-Origin": "*",
      //   "Access-Control-Allow-Headers": "Origin, X-Requested-With, Content-Type, Accept",
      // },
      "^/api": {
        // '^' 추가하여 정확한 경로 매칭
        target: "http://localhost:8080",
        changeOrigin: true,
        rewrite: path => path.replace("/^/api/", ""),
        secure: false, // SSL 관련 검증 비활성화
        configure: (proxy, options) => {
          // proxy 동작 로깅
          proxy.on("proxyReq", (proxyReq, req, res) => {
            console.log("Proxy Request:", req.method, req.url);
          });
          proxy.on("proxyRes", (proxyRes, req, res) => {
            console.log("Proxy Response:", proxyRes.statusCode);
          });
        },
      },
    },
  },
  optimizeDeps: {
    include: ['jwt-decode', 'react', 'react-dom']
  },
})
