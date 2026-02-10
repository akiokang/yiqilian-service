import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'

// https://vitejs.dev/config/
export default defineConfig({
  plugins: [vue()],
  server: {
    port: 5173, // 固定开发端口为5173
    open: true, // 自动打开浏览器
    proxy: {
      '/api': {
        target: getApiBaseUrl(), // 根据环境变量动态设置API代理目标
        changeOrigin: true,
        secure: false
      }
    }
  },
  define: {
    '__API_BASE_URL__': JSON.stringify(getApiBaseUrl())
  }
})

// 根据环境返回不同的API基础URL
function getApiBaseUrl() {
  const env = process.env.NODE_ENV || 'development'
  
  switch (env) {
    case 'development':
      return 'http://localhost:8080' // 开发环境使用localhost:8080
    case 'uat':
      return '' // UAT环境待配置
    case 'production':
      return '' // 生产环境待配置
    default:
      return 'http://localhost:8080'
  }
}